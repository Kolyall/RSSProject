package com.rssproject;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Nick on 01.10.2015.
 */
@DatabaseTable
public   class Enclosure {
    @DatabaseField(generatedId = true, columnName = "id", canBeNull = false)
    private long enclosure_id;
    @DatabaseField
    @JacksonXmlProperty(isAttribute = true)
    private String url;
    public String getUrl() {
        return url;
    }

    @ForeignCollectionField(eager = false)
    protected ForeignCollection<Item> items;
}