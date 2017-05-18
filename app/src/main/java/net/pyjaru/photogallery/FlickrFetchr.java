package net.pyjaru.photogallery;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

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
    private static final String API_KEY = BuildConfig.FLICKR_API_KEY;
    private static final String FETCH_RECENTS_METHOD = "flickr.photos.getRecent";
    private static final String SEARCH_METHOD = "flickr.photos.search";
    private static final Uri ENDPOINT = Uri.parse("https://api.flickr.com/services/rest/")
            .buildUpon()
            .appendQueryParameter("method", "flickr.Photos.getRecent")
            .appendQueryParameter("api_key", API_KEY)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback","1")
            .appendQueryParameter("extras","url_s")
            .build();

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

    public List<GalleryItem> fetchRecentPhotos(Integer page){
        String url = buildUrl(FETCH_RECENTS_METHOD, null, page);
        return downloadGalleryItems(url);
    }

    public List<GalleryItem> searchPhotos(String query, Integer page){
        String url = buildUrl(SEARCH_METHOD, query, page);
        return downloadGalleryItems(url);
    }

    public List<GalleryItem> downloadGalleryItems(String url){
        List<GalleryItem> items = new ArrayList<>();

        try {
            String jsonString = getUrlString(url);
            parseItemsByGson(items, jsonString);
            Log.i(TAG, "Received JSON: " + jsonString);
        }
        catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }

        return items;
    }

    private String buildUrl(String method, String query, Integer page){
        Uri.Builder uriBuilder = ENDPOINT.buildUpon().appendQueryParameter("method",method);

        if(method.equals(SEARCH_METHOD))
            uriBuilder.appendQueryParameter("text", query);
        if(page>0)
            uriBuilder.appendQueryParameter("page", page.toString());

        return uriBuilder.build().toString();
    }

    //reference : http://m.blog.naver.com/e3jk1234/60197530396
    private void parseItemsByGson(List<GalleryItem> items, String jsonString){
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonElement rootElement = parser.parse(jsonString)
                .getAsJsonObject().get("photos")
                .getAsJsonObject().get("photo");
        List<Photo> photos = gson.fromJson(rootElement, new TypeToken<List<Photo>>(){}.getType());

        for(Photo photo : photos){
            if(photo.url_s == null || photo.url_s.isEmpty()) continue;
            GalleryItem item = new GalleryItem();
            item.setId(photo.id);
            item.setCaption(photo.title);
            item.setUrl(photo.url_s);
            items.add(item);
        }
    }
}
