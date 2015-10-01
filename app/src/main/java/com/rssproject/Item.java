package com.rssproject;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Created by PC on 01.10.2015.
 */
public  class Item {
    private String title;
    private String link;
    private String pubdate;
    private String description;
    private String category;

    public String getPubdate() {
        return pubdate;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    @JacksonXmlProperty(localName = "enclosure")
    @JacksonXmlElementWrapper(useWrapping = false)
    public Enclosure enclosure;

    public Enclosure getEnclosure() {
        return enclosure;
    }
}