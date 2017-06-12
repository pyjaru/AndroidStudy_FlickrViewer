package net.pyjaru.photogallery;

/**
 * Created by pyjaru on 2017. 6. 12..
 */

import android.content.Context;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
public class PhotoTest {
    private static final String TAG = "PhotoTest";
    private Uri testUri;

    private String makeTestUrl(){
        testUri = Uri.parse("https://api.flickr.com/services/rest/")
                .buildUpon()
                .appendQueryParameter("method", "flickr.Photos.getRecent")
                .appendQueryParameter("api_key", BuildConfig.FLICKR_API_KEY)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback","1")
                .appendQueryParameter("extras","url_s")
                .build();
        return testUri.toString();
    }

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        }finally {
            connection.disconnect();
        }
    }

    @Test
    public void testGsonParsing() throws Exception {
        String url = makeTestUrl();
        String jsonString = new String(getUrlBytes(url));

        //LOG For Information.
        Log.i(TAG, "## Get Url ##\n" + url);
        Log.i(TAG, "## Get JsonString for Parsing ##\n" + jsonString);

        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonElement rootElement = parser.parse(jsonString)
                .getAsJsonObject().get("photos")
                .getAsJsonObject().get("photo");
        List<Photo> photos = gson.fromJson(rootElement, new TypeToken<List<Photo>>(){}.getType());

        FlickrModel flickr = gson.fromJson(jsonString, FlickrModel.class);
        List<FlickrPhoto> photos2 = flickr.getFlickr().getPhotos();

        Assert.assertEquals(photos.size(), photos.size());
    }


/*
{"photos":{"page":1,"pages":10,"perpage":100,"total":1000,"photo":[{"id":"34414771374","owner":"85197777@N08","secret":"e11331c961","server":"4195","farm":5,"title":"IMGL0357","ispublic":1,"isfriend":0,"isfamily":0,"url_s":"https:\/\/farm5.staticflickr.com\/4195\/34414771374_e11331c961_m.jpg","height_s":"240","width_s":"160"},{"id":"34414772534","owner":"149634353@N08","secret":"141d9f62a8","server":"4202","farm":5,"title":"CHR_9340.jpg","ispublic":1,"isfriend":0,"isfamily":0,"url_s":"https:\/\/farm5.staticflickr.com\/4202\/34414772534_141d9f62a8_m.jpg","height_s":"240","width_s":"159"},{"id":"34414774774","owner":"149449068@N03","secret":"46e9a32898","server":"4241","farm":5,"title":"Wife showing off the fit \u2193 Check below \u2193","ispublic":1,"isfriend":0,"isfamily":0,"url_s":"https:\/\/farm5.staticflickr.com\/4241\/34414774774_46e9a32898_m.jpg","height_s":"240","width_s":"240"},{"id":"34414775214","owner":"11351129@N00","secret":"aa6a3309cc","server":"4275","farm":5,"title":"Virginia Giant \u2122","ispublic":1,"isfriend":0,"isfamily":0,"url_s":"https:\/\/farm5.staticflickr.com\/4275\/34414775214_aa6a3309cc_m.jpg","height_s":"240","width_s":"240"},{"id":"34414775674","owner":"147696170@N02","secret":"51f14d6929","server":"4283","farm":5,"title":"previsao-do-tempo.jpg","ispublic":1,"isfriend":0,"isfamily":0,"url_s":"https:\/\/farm5.staticflickr.com\/4283\/34414775674_51f14d6929_m.jpg","height_s":"240","width_s":"240"},{"id":"34414776864","owner":"149704464@N05","secret":"b193f63d3a","server":"4258","farm":5,"title":"Thank you! \u2764\ufe0f","ispublic":1,"isfriend":0,"isfamily":0,"url_s":"https:\/\/farm5.staticflickr.com\/4258\/34414776864_b193f63d3a_m.jpg","height_s":"240","width_s":"240"},{"id":"34414778264","owner":"85977298@N07","secret":"4cf962c3f8","server":"4259","farm":5,"title":"IMG_2332_LR.jpg","ispublic":1,"isfriend":0,"isfamily":0,"url_s":"https:\/\/farm5.staticflickr.com\/4259\/34414778264_4cf962c3f8_m.jpg","height_s":"180","width_s":"240"},{"id":"34414787394","owner":"150874425@N06","secret":"68115a8f5c","server":"4232","farm":5,"title":"Bolo pra crian\u00e7as Minecraft","ispublic":1,"isfriend":0,"isfamily":0},{"id":"34414788294","owner":"129366241@N05","secret":"a77f796248","server":"4217","farm":5,"title":"_MG_0270.jpg","ispublic":1,"isfriend":0,"isfamily":0,"url_s":"https:\/\/farm5.staticflickr.com\/4217\/34414788294_a77f796248_m.jpg","height_s":"240","width_s":"160"},{"id":"34449523553","owner":"151631125@N06","secret":"1db9f0bf6e","server":"4264","farm":5,"title":"2017-06-12_04-52-56","ispublic":1,"isfriend":0,"isfamily":0,"url_s":"https:\/\/farm5.staticflickr.com\/4264\/34449523553_1db9f0bf6e_m.jpg","height_s":"240","width_s":"180"},{"id":"34449525013","owner":"150054720@N02","secret":"fa65af664a","server":"4195","farm":5,"title":"\u0627\u0633\u062a\u06cc\u062c \u0631\u0642\u0635 \u0646\u0648\u0631 \u0645\u062a\u0641\u0627\u0648\u062a 2018","ispublic":1,"isfriend":0,"isfamily":0,"url_s":"https:\/\/farm5.staticflickr.com\/4195\/34449525013_fa65af664a_m.jpg","height_s":"240","width_s":"180"},{"id":"34449525803","owner":"50664180@N00","secret":"58b6b55224","server":"4238","farm":5,"title":"Photo","ispublic":1,"isfriend":0,"isfamily":0,"url_s":"https:\/\/farm5.staticflickr.com\/4238\/34449525803_58b6b55224_m.jpg","height_s":"180","width_s":"240"},{"id":"34449526593","owner":"135941449@N03","secret":"c9f1c099b7","server":"4263","farm":5,"title":"2017-06-12_07-59-08","ispublic":1,"isfriend":0,"isfamily":0,"url_s":"https:\/\/farm5.staticflickr.com\/4263\/34449526593_c9f1c099b7_m.jpg","height_s":"160","width_s":"240"},{"id":"34449529223","owner":"129312814@N08","secret":"2c60e0e0c7","server":"4199","farm":5,"title":"Rabanne Paco, Invictus Intense, EDT, 5 ml","ispublic":1,"isfriend":0,"isfamily":0,"url_s":"https:\/\/farm5.staticflickr.com\/4199\/34449529223_2c60e0e0c7_m.jpg","height_s":"240","width_s":"180"},{"id":"34449530253","owner":"131065951@N07","secret":"d06e99bd48","server":"4269","farm":5,"title":"DSC_3951","ispublic":1,"isfriend":0,"isfamily":0,"url_s":"ht

* */

}
