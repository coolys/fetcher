package io.cooly.crawler.service;

import io.cooly.crawler.domain.WebUrl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing WebUrl.
 */
public interface WebUrlService {

    /**
     * Save a webUrl.
     *
     * @param webUrl the entity to save
     * @return the persisted entity
     */
    WebUrl save(WebUrl webUrl);

    /**
     * Get all the webUrls.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<WebUrl> findAll(Pageable pageable);


    /**
     * Get the "id" webUrl.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<WebUrl> findOne(String id);

    /**
     * Delete the "id" webUrl.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the webUrl corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<WebUrl> search(String query, Pageable pageable);
}
