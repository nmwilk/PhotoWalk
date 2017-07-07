package com.nmwilkinson.photowalk.flickr;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "photo", strict = false)
public class FlickrPhoto implements Serializable {

    @Attribute(name = "id")
    private String id;

    @Attribute(name = "farm")
    private String farmId;

    @Attribute(name = "secret")
    private String secret;

    @Attribute(name = "server")
    private String serverId;


    public FlickrPhoto(final String id,
                       final String farmId,
                       final String secret,
                       final String serverId) {
        this.id = id;
        this.farmId = farmId;
        this.serverId = serverId;
        this.secret = secret;
    }

    public FlickrPhoto() {
        this.id = "";
        this.farmId = "";
        this.serverId = "";
        this.secret = "";
    }

    public boolean isEqualTo(final FlickrPhoto o) {
        return o.id.equals(id) &&
                o.farmId.equals(farmId) &&
                o.secret.equals(secret) &&
                o.serverId.equals(serverId);
    }

    public String formUrl() {
        return String.format("https://farm%s.staticflickr.com/%s/%s_%s.jpg", farmId, serverId, id, secret);
    }
}