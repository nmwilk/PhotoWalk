package com.nmwilkinson.photowalk.flickr

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * E.g. https://api.flickr.com/services/rest/?method=flickr.test.echo&name=value
 *
 * https://api.flickr.com/services/rest?per_page=1&method=flickr.photos.search&api_key=d8c442c5ff094c6cff746afdae78f11b&lat=54&lon=-1.6
 *
 * Search flickr.photos.search
 */
interface FlickrSearchService {
    object Consts {
        val methodSearch: String = "flickr.photos.search"
    }

    @GET("services/rest?per_page=10&sort=date-posted-desc&accuracy=16&min_upload_date=2017-05-01T00:00:00Z")
    fun imagesAtLocation(@Query("method") method: String, @Query("api_key") apiKey: String, @Query("lat") lat: Double, @Query("lon") lon: Double) : Single<FlickrRsp>
}

