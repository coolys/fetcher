package io.cooly.crawler.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A WebUrl.
 */
@Document(collection = "web_url")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "web_url")
public class WebUrl implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;

    @Field("url")
    private String url;

    @Field("domain")
    private String domain;

    @Field("fetched")
    private Boolean fetched;

    @Field("created")
    private Instant created;

    @Field("engine")
    private String engine;

    @Field("data_parsed")
    private Object dataParsed;

    @Field("parse_rule")
    private Object rule;

    @Field("level")
    private int level;

    @Field("html")
    private String html;

    @Field("fetch_status")
    private Boolean fetchStatus;

    @Field("fetch_info")
    private Object fetchInfo;



    // coolybot-needle-entity-add-field - Coolybot will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public WebUrl url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDomain() {
        return domain;
    }

    public WebUrl domain(String domain) {
        this.domain = domain;
        return this;
    }


    public Object getRule() {
        return rule;
    }

    public void setRule(Object rule) {
        this.rule = rule;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Boolean isFetched() {
        return fetched;
    }

    public WebUrl fetched(Boolean fetched) {
        this.fetched = fetched;
        return this;
    }

    public void setFetched(Boolean fetched) {
        this.fetched = fetched;
    }

    public Instant getCreated() {
        return created;
    }

    public WebUrl created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public String getEngine() {
        return engine;
    }

    public WebUrl engine(String engine) {
        this.engine = engine;
        return this;
    }

    public Boolean getFetchStatus() {
        return fetchStatus;
    }

    public void setFetchStatus(Boolean fetchStatus) {
        this.fetchStatus = fetchStatus;
    }

    public Object getFetchInfo() {
        return fetchInfo;
    }

    public void setFetchInfo(Object fetchInfo) {
        this.fetchInfo = fetchInfo;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public Object getDataParsed() {
        return dataParsed;
    }

    public void setDataParsed(Object dataParsed) {
        this.dataParsed = dataParsed;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }
    // coolybot-needle-entity-add-getters-setters - Coolybot will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WebUrl webUrl = (WebUrl) o;
        if (webUrl.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), webUrl.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WebUrl{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", domain='" + getDomain() + "'" +
            ", fetched='" + isFetched() + "'" +
            ", created='" + getCreated() + "'" +
            ", engine='" + getEngine() + "'" +
            "}";
    }
}
