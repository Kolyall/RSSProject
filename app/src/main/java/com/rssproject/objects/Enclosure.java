package com.rssproject.objects;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Nick on 01.10.2015.
 */
@DatabaseTable
public   class Enclosure
        implements Parcelable
{
    public Enclosure(){
    }
    @DatabaseField(generatedId = true, columnName = "id", canBeNull = false)
    private long enclosure_id;
    @DatabaseField
    @JacksonXmlProperty(isAttribute = true)
    private String url;

    public Enclosure(long enclosure_id, String url) {
        this.enclosure_id=enclosure_id;
        this.url=url;
    }

    public String getUrl() {
        return url;
    }

    @ForeignCollectionField(eager = false)
    protected ForeignCollection<Item> items;


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle b = new Bundle();
        b.putLong("enclosure_id", enclosure_id);
        b.putString("url", url);
        dest.writeBundle(b);
    }

    public static final Parcelable.Creator<Enclosure> CREATOR =
            new Parcelable.Creator<Enclosure>() {
                public Enclosure createFromParcel(Parcel in) {

                    //                    category.ItemName = in.readString();
                    Bundle b = in.readBundle();
                    long enclosure_id = b.getLong("enclosure_id");
                    String url = b.getString("url");
                    //                    int imgResID = b.getInt("imgResID");
                    Enclosure item = new Enclosure(enclosure_id,url);
                    return item;
                }

                @Override
                public Enclosure[] newArray(int size) {
                    return new Enclosure[size];
                }
            };

    @Override
    public int describeContents() {
        return 0;
    }
}