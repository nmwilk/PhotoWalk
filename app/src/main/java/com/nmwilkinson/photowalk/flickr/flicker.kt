package com.nmwilkinson.photowalk.flickr

import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

object Flickr {
    val apiKey = "d8c442c5ff094c6cff746afdae78f11b"
    val secret = "cfc52e5d43aca08f"

    fun createImageDownloader(): FlickrSearchService {
        val builder = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(interceptor)
        val retrofit = Retrofit.Builder()
                .addConverterFactory(SimpleXmlConverterFactory.createNonStrict())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(builder.build())
                .baseUrl("https://api.flickr.com")
                .build()
        return retrofit.create(FlickrSearchService::class.java)
    }
}

