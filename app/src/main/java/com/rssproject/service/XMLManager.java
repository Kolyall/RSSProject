package com.rssproject.service;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.rssproject.R;
import com.rssproject.objects.Channel;
import com.rssproject.objects.Enclosure;
import com.rssproject.objects.Item;
import com.rssproject.database.DatabaseHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class XMLManager {
    private final String LOG_TAG = getClass().getSimpleName();
	private Context mContext;
	public XMLManager(Context context) {
		this.mContext = context;
	}

    public boolean loadList() throws URISyntaxException, IOException {
        String[] listlinks = mContext.getResources().getStringArray(R.array.listlinks);
        for (String url:listlinks) {
            String str_xml = getXmlFromUrl(url);
            Channel root = convertXML(str_xml);
//        deleteDBItems();
            addToOrmLite(root);
        }
        boolean success = true;
		return success;
	}


    private void addToOrmLite(Channel root) {
        RuntimeExceptionDao<Item, Integer> daoItem = (new DatabaseHelper(mContext)).getItemDataDao();
        RuntimeExceptionDao<Enclosure, Integer> daoEnclosure = (new DatabaseHelper(mContext)).getEnclosureDataDao();
        // query for all of the data objects in the database
        List<Item> list = daoItem.queryForAll();
        for (Item item: root.getItems()){
            if (!list.contains(item)){
                // store it in the database
                daoEnclosure.create(item.getEnclosure());
                item.setCategory(item.getCategory().trim());
                item.setPubdate(item.getPubdate().trim().replaceAll("\\s+", ""));

                String item_date = item.getPubdate();
                SimpleDateFormat curFormater = new SimpleDateFormat("EEE,ddMMMyyyyHH:mm:ssZ", Locale.ENGLISH);

                Date dateObj = null;
                try {
                    dateObj = curFormater.parse(item_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                item.setD_date(dateObj);
                daoItem.create(item);
            }
        }
    }

    private Channel convertXML(String in) throws IOException {
        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        XmlMapper xmlMapper = new XmlMapper(module);
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Channel mChannel = xmlMapper.readValue(in, Channel.class);
        Log.e(LOG_TAG, "getItemsSize: " + mChannel.getItems().size());
        return mChannel;
    }

    public String getXmlFromUrl(String url) throws IOException {
        Document doc = Jsoup.connect(url)
                .ignoreContentType(true).timeout(7000).userAgent("Mozilla").get();
        String xml = doc
                .select("channel")
                .toString();
        // return XML
        return xml;
    }


    private void writeToFile(String data,String file) {
        try {
            FileOutputStream fileOutput = new FileOutputStream(Environment
                    .getExternalStorageDirectory().getAbsolutePath()+"/Download/"+file);

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutput);
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
        }
    }

    //deleting previous items from DB
    private void deleteDBItems() {
        RuntimeExceptionDao<Item, Integer> simpleDao = (new DatabaseHelper(mContext)).getItemDataDao();
        // query for all of the data objects in the database
        List<Item> list = simpleDao.queryForAll();
        // our string builder for building the content-view
        // if we already have items in the database
        for (Item simple : list) {
            simpleDao.delete(simple);
            Log.i(LOG_TAG, "deleting simple(" + simple.id + ")");
        }
    }
}
