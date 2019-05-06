package io.cooly.crawler.service.impl;

import io.cooly.crawler.domain.WebUrl;
import io.cooly.crawler.repository.WebUrlRepository;
import io.cooly.crawler.repository.search.WebUrlSearchRepository;
import io.cooly.crawler.service.WebUrlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing WebUrl.
 */
@Service
public class WebUrlServiceImpl implements WebUrlService {

    private final Logger log = LoggerFactory.getLogger(WebUrlServiceImpl.class);

    private final WebUrlRepository webUrlRepository;

    private final WebUrlSearchRepository webUrlSearchRepository;

    public WebUrlServiceImpl(WebUrlRepository webUrlRepository, WebUrlSearchRepository webUrlSearchRepository) {
        this.webUrlRepository = webUrlRepository;
        this.webUrlSearchRepository = webUrlSearchRepository;
    }

    /**
     * Save a webUrl.
     *
     * @param webUrl the entity to save
     * @return the persisted entity
     */
    @Override
    public WebUrl save(WebUrl webUrl) {
        log.debug("Request to save WebUrl : {}", webUrl);
        WebUrl result = webUrlRepository.save(webUrl);
        webUrlSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the webUrls.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<WebUrl> findAll(Pageable pageable) {
        log.debug("Request to get all WebUrls");
        return webUrlRepository.findAll(pageable);
    }


    /**
     * Get one webUrl by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<WebUrl> findOne(String id) {
        log.debug("Request to get WebUrl : {}", id);
        return webUrlRepository.findById(id);
    }

    /**
     * Delete the webUrl by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete WebUrl : {}", id);
        webUrlRepository.deleteById(id);
        webUrlSearchRepository.deleteById(id);
    }

    /**
     * Search for the webUrl corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<WebUrl> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WebUrls for query {}", query);
        return webUrlSearchRepository.search(queryStringQuery(query), pageable);    }
}
