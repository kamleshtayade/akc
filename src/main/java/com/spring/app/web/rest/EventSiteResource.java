package com.spring.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.spring.app.domain.EventSite;
import com.spring.app.repository.EventSiteRepository;
import com.spring.app.repository.search.EventSiteSearchRepository;
import com.spring.app.web.rest.util.PaginationUtil;
import com.spring.app.web.rest.dto.EventSiteDTO;
import com.spring.app.web.rest.mapper.EventSiteMapper;
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
 * REST controller for managing EventSite.
 */
@RestController
@RequestMapping("/api")
public class EventSiteResource {

    private final Logger log = LoggerFactory.getLogger(EventSiteResource.class);

    @Inject
    private EventSiteRepository eventSiteRepository;

    @Inject
    private EventSiteMapper eventSiteMapper;

    @Inject
    private EventSiteSearchRepository eventSiteSearchRepository;

    /**
     * POST  /eventSites -> Create a new eventSite.
     */
    @RequestMapping(value = "/eventSites",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EventSiteDTO> create(@Valid @RequestBody EventSiteDTO eventSiteDTO) throws URISyntaxException {
        log.debug("REST request to save EventSite : {}", eventSiteDTO);
        if (eventSiteDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new eventSite cannot already have an ID").body(null);
        }
        EventSite eventSite = eventSiteMapper.eventSiteDTOToEventSite(eventSiteDTO);
        EventSite result = eventSiteRepository.save(eventSite);
        eventSiteSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/eventSites/" + eventSiteDTO.getId())).body(eventSiteMapper.eventSiteToEventSiteDTO(result));
    }

    /**
     * PUT  /eventSites -> Updates an existing eventSite.
     */
    @RequestMapping(value = "/eventSites",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EventSiteDTO> update(@Valid @RequestBody EventSiteDTO eventSiteDTO) throws URISyntaxException {
        log.debug("REST request to update EventSite : {}", eventSiteDTO);
        if (eventSiteDTO.getId() == null) {
            return create(eventSiteDTO);
        }
        EventSite eventSite = eventSiteMapper.eventSiteDTOToEventSite(eventSiteDTO);
        EventSite result = eventSiteRepository.save(eventSite);
        eventSiteSearchRepository.save(eventSite);
        return ResponseEntity.ok().body(eventSiteMapper.eventSiteToEventSiteDTO(result));
    }

    /**
     * GET  /eventSites -> get all the eventSites.
     */
    @RequestMapping(value = "/eventSites",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<EventSiteDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<EventSite> page = eventSiteRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/eventSites", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(eventSiteMapper::eventSiteToEventSiteDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /eventSites/:id -> get the "id" eventSite.
     */
    @RequestMapping(value = "/eventSites/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EventSiteDTO> get(@PathVariable Long id) {
        log.debug("REST request to get EventSite : {}", id);
        return Optional.ofNullable(eventSiteRepository.findOne(id))
            .map(eventSiteMapper::eventSiteToEventSiteDTO)
            .map(eventSiteDTO -> new ResponseEntity<>(
                eventSiteDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /eventSites/:id -> delete the "id" eventSite.
     */
    @RequestMapping(value = "/eventSites/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete EventSite : {}", id);
        eventSiteRepository.delete(id);
        eventSiteSearchRepository.delete(id);
    }

    /**
     * SEARCH  /_search/eventSites/:query -> search for the eventSite corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/eventSites/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<EventSite> search(@PathVariable String query) {
        return StreamSupport
            .stream(eventSiteSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
