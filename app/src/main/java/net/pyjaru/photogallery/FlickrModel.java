package net.pyjaru.photogallery;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by pyjaru on 2017. 6. 12..
 */

/*
"photos": { ... }
 */
public class FlickrModel {
    @SerializedName("photos")
    FlickrPhotos mFlickr;

    public FlickrPhotos getFlickr() {
        return mFlickr;
    }

    public void setFlickr(FlickrPhotos mFlickr) {
        this.mFlickr = mFlickr;
    }
}

/*
"page": 1,
"pages": 10,
"perpage": 100,
"total": 1000,
"photo": [ photo1, photo2, ...]
*/
class FlickrPhotos {
    @SerializedName("page")
    int mPage;
    @SerializedName("pages")
    int mPages;
    @SerializedName("perpage")
    int mPerPages;
    @SerializedName("total")
    int mTotal;
    @SerializedName("photo")
    List<FlickrPhoto> mPhotos;

    public int getPage() {
        return mPage;
    }

    public void setPage(int mPage) {
        this.mPage = mPage;
    }

    public int getPages() {
        return mPages;
    }

    public void setPages(int mPages) {
        this.mPages = mPages;
    }

    public int getPerPages() {
        return mPerPages;
    }

    public void setPerPages(int mPerPages) {
        this.mPerPages = mPerPages;
    }

    public int getTotal() {
        return mTotal;
    }

    public void setTotal(int mTotal) {
        this.mTotal = mTotal;
    }

    public List<FlickrPhoto> getPhotos() {
        return mPhotos;
    }

    public void setPhotos(List<FlickrPhoto> mPhotos) {
        this.mPhotos = mPhotos;
    }
}
/*
"id": "34417489724",
"owner": "60219456@N06",
"secret": "94f5a33975",
"server": "4263",
"farm": 5,
"title": "",
"ispublic": 1,
"isfriend": 0,
"isfamily": 0,
"url_s": "https://farm5.staticflickr.com/4263/34417489724_94f5a33975_m.jpg",
"height_s": "180",
"width_s": "240"
*/
class FlickrPhoto {
    @SerializedName("id")
    String mId;
    @SerializedName("owner")
    String mOwner;
    @SerializedName("secret")
    String mSecret;
    @SerializedName("farm")
    int mFarm;
    @SerializedName("title")
    String mTitle;
    @SerializedName("ispublic")
    int mIsPublic;
    @SerializedName("isfriend")
    int mIsFriend;
    @SerializedName("isfamily")
    int mIsFamily;
    @SerializedName("url_s")
    String mUrl;
    @SerializedName("height_s")
    String mHeight;
    @SerializedName("width_s")
    String mWidth;

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getOwner() {
        return mOwner;
    }

    public void setOwner(String mOwner) {
        this.mOwner = mOwner;
    }

    public String getSecret() {
        return mSecret;
    }

    public void setSecret(String mSecret) {
        this.mSecret = mSecret;
    }

    public int getFarm() {
        return mFarm;
    }

    public void setFarm(int mFarm) {
        this.mFarm = mFarm;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getIsPublic() {
        return mIsPublic;
    }

    public void setIsPublic(int mIsPublic) {
        this.mIsPublic = mIsPublic;
    }

    public int getIsFriend() {
        return mIsFriend;
    }

    public void setIsFriend(int mIsFriend) {
        this.mIsFriend = mIsFriend;
    }

    public int getIsFamily() {
        return mIsFamily;
    }

    public void setIsFamily(int mIsFamily) {
        this.mIsFamily = mIsFamily;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getHeight() {
        return mHeight;
    }

    public void setHeight(String mHeight) {
        this.mHeight = mHeight;
    }

    public String getWidth() {
        return mWidth;
    }

    public void setWidth(String mWidth) {
        this.mWidth = mWidth;
    }
}
