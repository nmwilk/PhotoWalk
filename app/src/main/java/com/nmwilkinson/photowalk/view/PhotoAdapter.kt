package com.nmwilkinson.rooms.screen.roomslist.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.airbnb.epoxy.EpoxyAdapter
import com.airbnb.epoxy.EpoxyModel
import com.bumptech.glide.Glide
import com.nmwilkinson.photowalk.R
import kotlinx.android.synthetic.main.photo_list_item_view_content.view.*

class PhotoAdapter : EpoxyAdapter() {
    init {
        enableDiffing()
    }

    fun clear() {
        removeAllModels()
        notifyModelsChanged()
    }

    fun setPhotos(photoUrls: List<String>) {
        removeAllModels()
        photoUrls.forEach { url ->
            addModel(PhotoModel(url))
        }
    }
}

class PhotoModel(private val url: String) : EpoxyModel<PhotoListItemView>() {

    init {
        id(url)
    }

    override fun getDefaultLayout(): Int {
        return R.layout.photo_list_item_view
    }

    override fun bind(view: PhotoListItemView) {
        super.bind(view)

        view.tag = url
        view.setImageUrl(url)
    }
}

class PhotoListItemView(context: Context, attributeSet: AttributeSet) : FrameLayout(context, attributeSet) {

    init {
        View.inflate(context, R.layout.photo_list_item_view_content, this)
    }

    fun setImageUrl(url: String) {
        Glide.with(context).load(url).centerCrop().into(image)
    }
}
