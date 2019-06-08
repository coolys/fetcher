package io.cooly.crawler.client;

public class Rule {

    private String selector;
    private String value;
    private String name;
    private int level;
    private String host;
    private String dataType;
    private String configName;

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public Rule() {
    }

    @Override
    public String toString() {
        return "Rule{" +
            "selector='" + selector + '\'' +
            ", value='" + value + '\'' +
            ", name='" + name + '\'' +
            ", level=" + level +
            ", host='" + host + '\'' +
            ", dataType='" + dataType + '\'' +
            ", configName='" + configName + '\'' +
            '}';
    }
}
