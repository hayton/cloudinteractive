package com.example.cloudinteractive.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestOptions
import com.example.cloudinteractive.R
import kotlinx.android.synthetic.main.photo_detail_activity.*

class PhotoDetailActivity: AppCompatActivity() {

    companion object {
        const val arg_id = "arg_id"
        const val arg_title = "arg_title"
        const val arg_thumbnail_url = "arg_thumbnail_url"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_detail_activity)

        contentView.setOnClickListener {
            onBackPressed()
        }

        val glideUrl = GlideUrl(
            intent.getStringExtra(arg_thumbnail_url),
            LazyHeaders.Builder()
                .addHeader("User-Agent", "custom-agent")
                .build()
        )
        Glide.with(this)
            .apply { RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
            }
            .load(glideUrl)
            .into(thumbnail)

        photoId.text = intent.getStringExtra(arg_id)
        photoTitle.text = intent.getStringExtra(arg_title)
    }
}