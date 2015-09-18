package com.spring.app.repository.search;

import com.spring.app.domain.EventSite;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the EventSite entity.
 */
public interface EventSiteSearchRepository extends ElasticsearchRepository<EventSite, Long> {
}
