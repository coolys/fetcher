package io.cooly.crawler.client;


import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;



@AuthorizedFeignClient(name = "parser")
public interface ParserServiceClient {
    
    @RequestMapping("/api/web-pages")
    List<WebPage> getFetches();
    
    @PostMapping("api/web-pages")
    WebPage createWebPage(WebPage webPage);
}
