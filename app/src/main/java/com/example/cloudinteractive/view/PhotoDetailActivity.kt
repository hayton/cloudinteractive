package com.example.cloudinteractive.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.os.Environment.isExternalStorageRemovable
import android.util.Log
import android.util.LruCache
import androidx.appcompat.app.AppCompatActivity
import com.example.cloudinteractive.R
import kotlinx.android.synthetic.main.photo_detail_activity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.cache.DiskLruCache
import java.io.File
import java.net.URL
import java.nio.file.FileSystem
import java.util.concurrent.locks.ReentrantLock

class PhotoDetailActivity: AppCompatActivity() {
    lateinit var cache: LruCache<String, Bitmap>

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

        val url = intent.getStringExtra(arg_thumbnail_url)

        CoroutineScope(Dispatchers.IO).launch {
            val connection = URL(url).openConnection().apply {
                setRequestProperty("User-Agent", "custom-agent")
            }
            val bitmap = BitmapFactory.decodeStream(connection.getInputStream())
            launch(Dispatchers.Main) {
                thumbnail.setImageBitmap(bitmap)
            }

        }

        photoId.text = intent.getStringExtra(arg_id)
        photoTitle.text = intent.getStringExtra(arg_title)
    }

}