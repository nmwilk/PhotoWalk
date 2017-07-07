package com.nmwilkinson.photowalk.flickr;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "rsp", strict = false)
public class FlickrRsp implements Serializable {
    @Element
    private FlickrPhotos photos;

    public FlickrPhotos getPhotos() {
        return photos;
    }
}