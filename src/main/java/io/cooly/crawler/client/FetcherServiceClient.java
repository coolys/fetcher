/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.cooly.crawler.client;

import io.cooly.crawler.domain.WebUrl;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author hungnguyendang
 */
@AuthorizedFeignClient(name = "fetcher")
public interface FetcherServiceClient {
    
    @PostMapping("api/web-urls")
    WebUrl send(WebUrl weburl);
}
