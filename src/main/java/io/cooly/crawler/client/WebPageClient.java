package io.cooly.crawler.client;


import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;



@AuthorizedFeignClient(name = "parser")
public interface WebPageClient {
    @RequestMapping("/api/web-pages")
    List<WebPage> getFetches();
}
