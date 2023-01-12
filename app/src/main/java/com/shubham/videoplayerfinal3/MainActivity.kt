package com.shubham.videoplayerfinal3

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    var videoList = arrayListOf<String>();

    var videoDurationList = arrayListOf<String>();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTitle("Your Videos")
        checkPermissionFromUser();

    }


    @SuppressLint("Range")
    fun getVideos(){

        var contentResolver = contentResolver;
        var uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        var cursor = contentResolver.query(uri,null,null,null,null);

        var mainList = findViewById<ListView>(R.id.mainList);

        if(cursor!=null && cursor.moveToFirst()){

            do {

                var url = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                var duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));




                videoList.add(url);
                videoDurationList.add(duration);



            }while (cursor.moveToNext());

        }

        var listnew = videoList.reversed();
        var videoDurationListNew = videoDurationList.reversed();

        var adapter = MyAdapter(this,listnew);
        mainList.adapter = adapter;

        mainList.setOnItemClickListener { adapterView, view, i, l ->

            var intent = Intent(this,PlayingVideo::class.java);
            intent.putExtra("videodata",listnew.get(i));
            intent.putExtra("videoduration",videoDurationListNew.get(i));
            startActivity(intent);


        }
    }

    fun checkPermissionFromUser(){
        if(ContextCompat.checkSelfPermission(applicationContext,android.Manifest.permission.READ_EXTERNAL_STORAGE)!=
            PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                1)
        }
        else{
            getVideos();
        }
    }

    fun getVideoThumbnail(videoUri: Uri): Bitmap? {

        var path = videoUri.getPath();
        return ThumbnailUtils.createVideoThumbnail(path.toString(), MediaStore.Images.Thumbnails.MINI_KIND);
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_layout,menu);
        return true;

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.theme){
            var intent = Intent(this,dark_mode_activity::class.java);
            startActivity(intent);

        }


        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getVideos();
            } else if(grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Please give Permission to Continue!\nIf denied 2 times please uninstall and reinstall app.", Toast.LENGTH_LONG)
                    .show();
                checkPermissionFromUser()
            }


        }

    }
}