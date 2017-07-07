package com.nmwilkinson.photowalk.flickr;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

@Root(strict = false)
public class FlickrPhotos implements Serializable {

    @Attribute
    private
    int total;

    @ElementList(name = "photo", inline = true)
    private List<FlickrPhoto> photoList;

    public void setItemList(List<FlickrPhoto> photoList) {
        this.photoList = photoList;
    }

    public FlickrPhotos(List<FlickrPhoto> photoList) {
        this.photoList = photoList;
    }

    public FlickrPhotos() {
    }

    public List<FlickrPhoto> getPhotoList() {
        return photoList;
    }
}