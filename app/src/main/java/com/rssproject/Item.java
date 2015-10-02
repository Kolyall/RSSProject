package com.rssproject;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by PC on 01.10.2015.
 */
@DatabaseTable
public  class Item {
    @DatabaseField(generatedId = true, canBeNull = false, columnName = "id")
    public int id;

    @DatabaseField
    private String title;
    @DatabaseField
    private String link;
    @DatabaseField
    private String pubdate;
    @DatabaseField
    private String description;
    @DatabaseField
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
    @DatabaseField(canBeNull = true, foreign = true,columnName = "enclosure_id", foreignAutoCreate = true, foreignAutoRefresh = true)
//    @DatabaseField(canBeNull = true, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    @JacksonXmlProperty(localName = "enclosure")
    @JacksonXmlElementWrapper(useWrapping = false)
    public Enclosure enclosure_id;

    public Enclosure getEnclosure() {
        return enclosure_id;
    }

    public void setEnclosure(Enclosure enclosure) {
        this.enclosure_id = enclosure;
    }

    @Override
    public boolean equals(Object o) {
        Item another = (Item)o;
        if (another.getTitle().equals(getTitle()))
            return true;
        else
            return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id=").append(id);
        sb.append("\n").append(title.trim());
//        sb.append("\n").append(link.trim());
        if (enclosure_id !=null)
        if (enclosure_id.getUrl() !=null)
        sb.append("\n").append("enclosure=").append(enclosure_id.getUrl().trim());
//        sb.append("\n").append(category.trim());
        return sb.toString();
    }
}