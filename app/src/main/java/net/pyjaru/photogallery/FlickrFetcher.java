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
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pyjaru on 2017. 5. 4..
 */

public class FlickrFetcher {
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
    private void parseItemsByGson(List<GalleryItem> items, String flickrJson){
        Gson gson = new Gson();
        FlickrModel flickr = gson.fromJson(flickrJson, FlickrModel.class);
        List<FlickrPhoto> photos = flickr.getFlickr().getPhotos();

        for(FlickrPhoto photo : photos){
            if(photo == null) continue;
            if(photo.getUrl() == null || photo.getUrl().isEmpty()) continue;
            GalleryItem item = new GalleryItem();
            item.setId(photo.getId());
            item.setCaption(photo.getTitle());
            item.setUrl(photo.getUrl());
            item.setOwner(photo.getOwner());
            items.add(item);
        }
    }
}
