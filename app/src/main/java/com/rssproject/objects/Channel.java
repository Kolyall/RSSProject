package com.rssproject.objects;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

/**
 * Created by PC on 01.10.2015.
 */
@JacksonXmlRootElement(localName = "channel")
public class Channel
//        implements Parcelable
{
    private String title;
    private String link;
    private String description;
    private String language;
    private String copyright;

    public Channel(){

    }
//    public Channel(String title, String link, String description, String language, String copyright, List<Item> items) {
//        this.title=title;
//        this.link=link;
//        this.description=description;
//        this.language=language;
//        this.copyright=copyright;
//        this.items=items;
//    }


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

//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        Bundle b = new Bundle();
//        b.putString("title", title);
//        b.putString("link", link);
//        b.putString("description", description);
//        b.putString("language", language);
//        b.putString("copyright", copyright);
////        b.putParcelableArrayList("items", (ArrayList<? extends Parcelable>) items);
//        dest.writeBundle(b);
////        dest.writeList(items);
//        dest.writeTypedList(items);
//    }
//
//    public static final Parcelable.Creator<Channel> CREATOR =
//            new Parcelable.Creator<Channel>() {
//                public Channel createFromParcel(Parcel in) {
//
//                    //                    category.ItemName = in.readString();
//                    Bundle b = in.readBundle();
//                    String title = b.getString("title");
//                    String link = b.getString("link");
//                    String description = b.getString("description");
//                    String language = b.getString("language");
//                    String copyright = b.getString("copyright");
//                    List<Item> items = null;
//                    items = new ArrayList<Item>();
////                    in.readList(items,null);
//                    in.readTypedList(items, Item.CREATOR);
//                    //                    int imgResID = b.getInt("imgResID");
//                    Channel item = new Channel(title,
//                                                link,
//                                                description,
//                                                language,
//                                                copyright,items);
//                    return item;
//                }
//
//                @Override
//                public Channel[] newArray(int size) {
//                    return new Channel[size];
//                }
//            };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
}