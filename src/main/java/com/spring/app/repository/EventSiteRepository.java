package com.spring.app.repository;

import com.spring.app.domain.EventSite;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the EventSite entity.
 */
public interface EventSiteRepository extends JpaRepository<EventSite,Long> {

}
