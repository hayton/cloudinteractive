package com.example.cloudinteractive.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestOptions
import com.example.cloudinteractive.R
import com.example.cloudinteractive.model.Photo
import com.example.cloudinteractive.view.PhotoDetailActivity.Companion.arg_id
import com.example.cloudinteractive.view.PhotoDetailActivity.Companion.arg_thumbnail_url
import com.example.cloudinteractive.view.PhotoDetailActivity.Companion.arg_title
import com.example.cloudinteractive.viewmodel.ListFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.list_fragment.*

@AndroidEntryPoint
class ListFragment: Fragment() {

    val viewModel by viewModels<ListFragmentViewModel> ()
    var photoList = ArrayList<Photo>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        viewModel.photosMutableLiveData.observe(viewLifecycleOwner){
            if (it == null) {
                viewModel.reload()
            }

            Log.d("ListFragment", "size= ${it?.size}")
            photoList.addAll(it)
            recyclerview.adapter?.notifyDataSetChanged()
            progressbar.visibility = View.GONE

        }


    }

    fun setupRecyclerView() {
        recyclerview.adapter = Adapter()
        val layoutManager = GridLayoutManager(context, 4).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return 1
                }

            }
        }
        recyclerview.layoutManager = layoutManager
    }


    inner class Adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return ViewHolder(
                inflater.inflate(R.layout.item_photo, parent, false)
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when {
                holder is ViewHolder -> holder.setPhoto(photoList[position])
            }
        }

        override fun getItemCount(): Int {
            return photoList.size
        }

        inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            val thumbnail = itemView.findViewById<ImageView>(R.id.thumbnail)
            val photoId = itemView.findViewById<TextView>(R.id.photoId)
            val photoTitle = itemView.findViewById<TextView>(R.id.photoTitle)

            fun setPhoto(photo: Photo) {
                val glideUrl = GlideUrl(
                    photo.thumbnailUrl,
                    LazyHeaders.Builder()
                        .addHeader("User-Agent", "custom-agent")
                        .build()
                )
                Glide.with(itemView.context)
                    .apply { RequestOptions()
                                .fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                    }
                    .load(glideUrl)
                    .into(thumbnail)

                photoId.text = photo.id.toString()
                photoTitle.text = photo.title

                itemView.setOnClickListener {
                    val intent = Intent(activity, PhotoDetailActivity::class.java).apply {
                        putExtra(arg_id, photo.id.toString())
                        putExtra(arg_title, photo.title)
                        putExtra(arg_thumbnail_url, photo.thumbnailUrl)
                    }

                    startActivity(intent)
                }
            }
        }

    }
}