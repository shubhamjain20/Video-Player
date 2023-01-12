package com.shubham.videoplayerfinal3

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class MyAdapter (var context: Context, var videoList:List<String>) : BaseAdapter() {

    lateinit var videoName: TextView;
    lateinit var videoImage: ImageView;


    override fun getCount(): Int {
        return videoList.size;
    }

    override fun getItem(p0: Int): Any {
        return videoList.get(p0);
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong();
    }

    @SuppressLint("ResourceType")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var convertView = p1;

        convertView = LayoutInflater.from(context).inflate(R.layout.list_view_card,p2,false);
        videoImage =convertView.findViewById(R.id.imageView);
        videoName =convertView.findViewById(R.id.textView2);


        Glide.with(context)
            .load("file://" + videoList.get(p0))
            .centerCrop()
            .placeholder(Color.BLUE)
            .into(videoImage);

        videoName.text = videoList.get(p0).subSequence(videoList.get(p0).length - 20,videoList.get(p0).length);


        return convertView;

    }


}