package io.cooly.crawler.repository;

import io.cooly.crawler.domain.WebUrl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the WebUrl entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WebUrlRepository extends MongoRepository<WebUrl, String> {

}
