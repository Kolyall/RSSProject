package com.rssproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        (new MYAcink()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    class MYAcink extends AsyncTask<String,String,String>{

        private Object ass;

        @Override
        protected String doInBackground(String[] params) {
            String xml="<channel><item><title>My Tilte  </title></item></channel>";
            try {
                xml =  getAss();
                writeToFile(xml,"xml.xml");
            } catch (IOException e) {
                e.printStackTrace();
            }
            JacksonXmlModule module = new JacksonXmlModule();
            module.setDefaultUseWrapper(false);
            XmlMapper xmlMapper = new XmlMapper(module);
            xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            try {
                Channel mChannel = xmlMapper.readValue(xml, Channel.class);
                for(Item item:mChannel.getItems()){
                    Log.e("item",""+item.getPubdate()+" "+(item.getEnclosure()!=null?item.getEnclosure().getUrl():null));
                }
                Log.e("Channel",""+mChannel.getTitle()+"\n"+
                                ""+mChannel.getLanguage()+"\n"+
                                ""+mChannel.getLink()+"\n"+
                                ""+mChannel.getDescription()+"\n"+
                                ""+mChannel.getCopyright()+"\n"
                );

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        public String getAss() throws IOException {
            Document doc = Jsoup.connect("http://ria.ru/export/rss2/politics/index.xml").userAgent("Mozilla").get();
//            Log.e("", doc.toString());
//            return "<channel><item><title>My Tilte  </title></item></channel>";
            return doc
                    .select("channel")
                    .toString();
        }
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void writeToFile(String data,String file) {
        try {
            FileOutputStream fileOutput = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/"+file);

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutput);
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            ////Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
