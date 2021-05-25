package com.example.image_search_feat_retrofit2;

import android.graphics.drawable.Drawable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Hit {
    @SerializedName("id")
    @Expose
    String id;
    @SerializedName("pageURL")
    @Expose
    String pageURL;
    @SerializedName("type")
    @Expose
    String type;
    @SerializedName("tags")
    @Expose
    String tags;
    @SerializedName("previewURL")
    @Expose
    String previewURL;
    @SerializedName("previewWidth")
    @Expose
    String previewWidth;
    @SerializedName("previewHeight")
    @Expose
    String previewHeight;
    @SerializedName("webformatURL")
    @Expose
    String webformatURL;
    @SerializedName("webformatWidth")
    @Expose
    String webformatWidth;
    @SerializedName("webformatHeight")
    @Expose
    String webformatHeight;
    @SerializedName("largeImageURL")
    @Expose
    String largeImageURL;
    @SerializedName("fullHDURL")
    @Expose
    String fullHDURL;
    @SerializedName("imageURL")
    @Expose
    String imageURL;
    @SerializedName("imageWidth")
    @Expose
    String imageWidth;
    @SerializedName("imageHeight")
    @Expose
    String imageHeight;
    @SerializedName("imageSize")
    @Expose
    String imageSize;
    @SerializedName("views")
    @Expose
    String views;
    @SerializedName("downloads")
    @Expose
    String downloads;
    @SerializedName("favorites")
    @Expose
    String favorites;
    @SerializedName("likes")
    @Expose
    String likes;
    @SerializedName("comments")
    @Expose
    String comments;
    @SerializedName("user_id")
    @Expose
    String user_id;
    @SerializedName("user")
    @Expose
    String user;
    @SerializedName("userImageURL")
    @Expose
    String userImageURL;

    Drawable previewImage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPageURL() {
        return pageURL;
    }

    public void setPageURL(String pageURL) {
        this.pageURL = pageURL;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getPreviewURL() {
        return previewURL;
    }

    public void setPreviewURL(String previewURL) {
        this.previewURL = previewURL;
    }

    public String getPreviewWidth() {
        return previewWidth;
    }

    public void setPreviewWidth(String previewWidth) {
        this.previewWidth = previewWidth;
    }

    public String getPreviewHeight() {
        return previewHeight;
    }

    public void setPreviewHeight(String previewHeight) {
        this.previewHeight = previewHeight;
    }

    public String getWebformatURL() {
        return webformatURL;
    }

    public void setWebformatURL(String webformatURL) {
        this.webformatURL = webformatURL;
    }

    public String getWebformatWidth() {
        return webformatWidth;
    }

    public void setWebformatWidth(String webformatWidth) {
        this.webformatWidth = webformatWidth;
    }

    public String getWebformatHeight() {
        return webformatHeight;
    }

    public void setWebformatHeight(String webformatHeight) {
        this.webformatHeight = webformatHeight;
    }

    public String getLargeImageURL() {
        return largeImageURL;
    }

    public void setLargeImageURL(String largeImageURL) {
        this.largeImageURL = largeImageURL;
    }

    public String getFullHDURL() {
        return fullHDURL;
    }

    public void setFullHDURL(String fullHDURL) {
        this.fullHDURL = fullHDURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(String imageWidth) {
        this.imageWidth = imageWidth;
    }

    public String getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(String imageHeight) {
        this.imageHeight = imageHeight;
    }

    public String getImageSize() {
        return imageSize;
    }

    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getDownloads() {
        return downloads;
    }

    public void setDownloads(String downloads) {
        this.downloads = downloads;
    }

    public String getFavorites() {
        return favorites;
    }

    public void setFavorites(String favorites) {
        this.favorites = favorites;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUserImageURL() {
        return userImageURL;
    }

    public void setUserImageURL(String userImageURL) {
        this.userImageURL = userImageURL;
    }

    public Drawable getPreviewImage() {
        return previewImage;
    }

    public void setPreviewImage(){
        previewImage = WebHelper.loadImageFromWebOperations(getPreviewURL());
    }
}
