package com.rssproject;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

/**
 * Created by PC on 01.10.2015.
 */
@JacksonXmlRootElement(localName = "channel")
public class Channel {
    private String title;
    private String link;
    private String description;
    private String language;
    private String copyright;


    public String getTitle() {
        return title;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public String getLink() {
        return link;
    }

    @JacksonXmlProperty(localName = "item")
    @JacksonXmlElementWrapper(useWrapping = false)
    public List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
    // getters, setters, toString, etc
}