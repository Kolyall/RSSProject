package com.rssproject;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Created by Nick on 01.10.2015.
 */
public   class Enclosure {
    @JacksonXmlProperty(isAttribute = true)
    private String url;
    public String getUrl() {
        return url;
    }
}