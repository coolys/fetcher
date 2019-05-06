package io.cooly.crawler.repository.search;

import io.cooly.crawler.domain.WebUrl;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the WebUrl entity.
 */
public interface WebUrlSearchRepository extends ElasticsearchRepository<WebUrl, String> {
}
