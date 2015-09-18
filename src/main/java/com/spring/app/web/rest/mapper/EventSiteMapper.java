package com.spring.app.web.rest.mapper;

import com.spring.app.domain.*;
import com.spring.app.web.rest.dto.EventSiteDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity EventSite and its DTO EventSiteDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EventSiteMapper {

    EventSiteDTO eventSiteToEventSiteDTO(EventSite eventSite);

    EventSite eventSiteDTOToEventSite(EventSiteDTO eventSiteDTO);
}
