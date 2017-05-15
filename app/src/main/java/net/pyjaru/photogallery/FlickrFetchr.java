package net.pyjaru.photogallery;

import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pyjaru on 2017. 5. 4..
 */

public class FlickrFetchr {
    private static final String TAG = "FlickrFetcher";
    // resource에 string으로 두고, manifest에서 meta-data로 값을 두면 배포 후에도
    // api가 노출되지 않는다카더라 통신.(배포 프로젝트에서는 테스트해보자.)
    private static final String API_KEY = BuildConfig.FLICKR_API_KEY;


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

    public String getUrlString(String urlSpec) throws IOException{
        return new String(getUrlBytes(urlSpec));
    }

    public List<GalleryItem> fetchItems(int page){
        List<GalleryItem> items = new ArrayList<>();

        try {
            String url = Uri.parse("https://api.flickr.com/services/rest/")
                    .buildUpon()
                    .appendQueryParameter("method", "flickr.Photos.getRecent")
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback","1")
                    .appendQueryParameter("extras","url_s")
                    .appendQueryParameter("page",String.valueOf(page))
                    .build().toString();

            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
//            JSONObject jsonBody = new JSONObject(jsonString);
//            parseItems(items, jsonBody);
            parseItemsByGson(items, jsonString);
        }
//        catch (JSONException je){
//            Log.e(TAG, "Failed to parse JSON", je);
//        }
        catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }

        return items;
    }

    //reference : http://m.blog.naver.com/e3jk1234/60197530396
    private void parseItemsByGson(List<GalleryItem> items, String jsonString){
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonElement rootElement = parser.parse(jsonString)
                .getAsJsonObject().get("photos")
                .getAsJsonObject().get("photo");
        List<Photo> photos = (List<Photo>)gson.fromJson(rootElement, new TypeToken<List<Photo>>(){}.getType());

        for(Photo photo : photos){
            if(photo.url_s == null || photo.url_s.isEmpty()) continue;
            GalleryItem item = new GalleryItem();
            item.setId(photo.id);
            item.setCaption(photo.title);
            item.setUrl(photo.url_s);
            items.add(item);
        }
    }

    private void parseItems(List<GalleryItem> items, JSONObject jsonBody)
            throws IOException, JSONException {
        JSONObject photosJsonObject = jsonBody.getJSONObject("photos");
        JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");

        for(int i = 0 ; i < photoJsonArray.length(); i++){
            JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);

            GalleryItem item = new GalleryItem();
            item.setId(photoJsonObject.getString("id"));
            item.setCaption(photoJsonObject.getString("title"));

            if(!photoJsonObject.has("url_s")){
                continue;
            }
            item.setUrl(photoJsonObject.getString("url_s"));

            items.add(item);
        }
    }
}
