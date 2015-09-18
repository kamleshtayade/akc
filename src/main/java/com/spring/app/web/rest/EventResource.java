package com.spring.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.spring.app.domain.Event;
import com.spring.app.repository.EventRepository;
import com.spring.app.repository.search.EventSearchRepository;
import com.spring.app.web.rest.util.PaginationUtil;
import com.spring.app.web.rest.dto.EventDTO;
import com.spring.app.web.rest.mapper.EventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Event.
 */
@RestController
@RequestMapping("/api")
public class EventResource {

    private final Logger log = LoggerFactory.getLogger(EventResource.class);

    @Inject
    private EventRepository eventRepository;

    @Inject
    private EventMapper eventMapper;

    @Inject
    private EventSearchRepository eventSearchRepository;

    /**
     * POST  /events -> Create a new event.
     */
    @RequestMapping(value = "/events",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EventDTO> create(@Valid @RequestBody EventDTO eventDTO) throws URISyntaxException {
        log.debug("REST request to save Event : {}", eventDTO);
        if (eventDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new event cannot already have an ID").body(null);
        }
        Event event = eventMapper.eventDTOToEvent(eventDTO);
        Event result = eventRepository.save(event);
        eventSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/events/" + eventDTO.getId())).body(eventMapper.eventToEventDTO(result));
    }

    /**
     * PUT  /events -> Updates an existing event.
     */
    @RequestMapping(value = "/events",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EventDTO> update(@Valid @RequestBody EventDTO eventDTO) throws URISyntaxException {
        log.debug("REST request to update Event : {}", eventDTO);
        if (eventDTO.getId() == null) {
            return create(eventDTO);
        }
        Event event = eventMapper.eventDTOToEvent(eventDTO);
        Event result = eventRepository.save(event);
        eventSearchRepository.save(event);
        return ResponseEntity.ok().body(eventMapper.eventToEventDTO(result));
    }

    /**
     * GET  /events -> get all the events.
     */
    @RequestMapping(value = "/events",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<EventDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Event> page = eventRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/events", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(eventMapper::eventToEventDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /events/:id -> get the "id" event.
     */
    @RequestMapping(value = "/events/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EventDTO> get(@PathVariable Long id) {
        log.debug("REST request to get Event : {}", id);
        return Optional.ofNullable(eventRepository.findOne(id))
            .map(eventMapper::eventToEventDTO)
            .map(eventDTO -> new ResponseEntity<>(
                eventDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /events/:id -> delete the "id" event.
     */
    @RequestMapping(value = "/events/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Event : {}", id);
        eventRepository.delete(id);
        eventSearchRepository.delete(id);
    }

    /**
     * SEARCH  /_search/events/:query -> search for the event corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/events/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Event> search(@PathVariable String query) {
        return StreamSupport
            .stream(eventSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
