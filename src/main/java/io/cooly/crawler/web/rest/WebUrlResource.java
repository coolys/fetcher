package io.cooly.crawler.web.rest;

import io.cooly.crawler.domain.WebUrl;
import io.cooly.crawler.service.CrawlerService;
import io.cooly.crawler.service.WebUrlService;
import io.cooly.crawler.web.rest.errors.BadRequestAlertException;
import io.cooly.crawler.web.rest.util.HeaderUtil;
import io.cooly.crawler.web.rest.util.PaginationUtil;
import io.github.coolys.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * REST controller for managing WebUrl.
 */
@RestController
@RequestMapping("/api")
public class WebUrlResource {

    private final Logger log = LoggerFactory.getLogger(WebUrlResource.class);

    private static final String ENTITY_NAME = "fetcherWebUrl";

    private final WebUrlService webUrlService;
    
    @Autowired
    private CrawlerService crawlerService;

    public WebUrlResource(WebUrlService webUrlService) {
        this.webUrlService = webUrlService;
    }

    /**
     * POST  /web-urls : Create a new webUrl.
     *
     * @param webUrl the webUrl to create
     * @return the ResponseEntity with status 201 (Created) and with body the new webUrl, or with status 400 (Bad Request) if the webUrl has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/web-urls")
    public ResponseEntity<WebUrl> createWebUrl(@RequestBody WebUrl webUrl) throws URISyntaxException, Exception {
        log.debug("REST request to save WebUrl : {}", webUrl);
        if (webUrl.getId() != null) {
            throw new BadRequestAlertException("A new webUrl cannot already have an ID", ENTITY_NAME, "idexists");
        }
        new Thread(() -> {
            try {
                crawlerService.start(webUrl);
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(WebUrlResource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();               
        WebUrl result = webUrlService.save(webUrl);
        return ResponseEntity.created(new URI("/api/web-urls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /web-urls : Updates an existing webUrl.
     *
     * @param webUrl the webUrl to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated webUrl,
     * or with status 400 (Bad Request) if the webUrl is not valid,
     * or with status 500 (Internal Server Error) if the webUrl couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/web-urls")
    public ResponseEntity<WebUrl> updateWebUrl(@RequestBody WebUrl webUrl) throws URISyntaxException {
        log.debug("REST request to update WebUrl : {}", webUrl);
        if (webUrl.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WebUrl result = webUrlService.save(webUrl);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, webUrl.getId().toString()))
            .body(result);
    }

    /**
     * GET  /web-urls : get all the webUrls.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of webUrls in body
     */
    @GetMapping("/web-urls")
    public ResponseEntity<List<WebUrl>> getAllWebUrls(Pageable pageable) {
        log.debug("REST request to get a page of WebUrls");
        Page<WebUrl> page = webUrlService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/web-urls");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /web-urls/:id : get the "id" webUrl.
     *
     * @param id the id of the webUrl to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the webUrl, or with status 404 (Not Found)
     */
    @GetMapping("/web-urls/{id}")
    public ResponseEntity<WebUrl> getWebUrl(@PathVariable String id) {
        log.debug("REST request to get WebUrl : {}", id);
        Optional<WebUrl> webUrl = webUrlService.findOne(id);
        return ResponseUtil.wrapOrNotFound(webUrl);
    }

    /**
     * DELETE  /web-urls/:id : delete the "id" webUrl.
     *
     * @param id the id of the webUrl to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/web-urls/{id}")
    public ResponseEntity<Void> deleteWebUrl(@PathVariable String id) {
        log.debug("REST request to delete WebUrl : {}", id);
        webUrlService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/web-urls?query=:query : search for the webUrl corresponding
     * to the query.
     *
     * @param query the query of the webUrl search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/web-urls")
    public ResponseEntity<List<WebUrl>> searchWebUrls(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of WebUrls for query {}", query);
        Page<WebUrl> page = webUrlService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/web-urls");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
