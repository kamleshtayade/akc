package com.spring.app.web.rest.mapper;

import com.spring.app.domain.*;
import com.spring.app.web.rest.dto.EventDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Event and its DTO EventDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EventMapper {

    @Mapping(source = "site.id", target = "siteId")
    EventDTO eventToEventDTO(Event event);

    @Mapping(source = "siteId", target = "site")
    Event eventDTOToEvent(EventDTO eventDTO);

    default EventSite eventsiteFromId(Long id) {
        if (id == null) {
            return null;
        }
        EventSite eventsite = new EventSite();
        eventsite.setId(id);
        return eventsite;
    }
}
