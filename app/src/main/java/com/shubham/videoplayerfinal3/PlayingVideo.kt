package com.shubham.videoplayerfinal3

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import java.util.*

class PlayingVideo : AppCompatActivity() {
    lateinit var seekBar2: SeekBar;
    lateinit var videoView: VideoView;
    lateinit var videoDuration: String;
    var isFinished: Boolean = false;
    lateinit var videoPath: String;
    lateinit var timer: Timer;
    lateinit var controlsLayout: RelativeLayout;
    lateinit var endTime: TextView;
    lateinit var currentTime: TextView;

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playing_video)

        supportActionBar?.hide();

        videoView = findViewById<VideoView>(R.id.videoView);
        var playpausebutton = findViewById<Button>(R.id.playPauseButton);
        seekBar2 = findViewById<SeekBar>(R.id.seekBar2);
        controlsLayout = findViewById<RelativeLayout>(R.id.controlsLayout);
        endTime = findViewById<TextView>(R.id.endTime);
        currentTime = findViewById<TextView>(R.id.currentTime);
        var mainLayout = findViewById<FrameLayout>(R.id.mainLayout);




        var intent = getIntent().extras;

        videoPath = intent?.getString("videodata").toString();
        videoDuration = intent?.getString("videoduration").toString();

        var totalTime = videoDuration.toLong();

        if (totalTime < 60000)
        {
            endTime.text = "00 : ${(totalTime / 1000) % 60}";
        }
        else if (totalTime >= 60000 && totalTime < 3600000)
        {
            endTime.text = ((totalTime / 60000) % 60).toString() + ":" + ((totalTime / 1000) % 60).toString();

        }
        else
        {
            endTime.text = ((totalTime / 3600000) % 24).toString() + ":" + ((totalTime / 60000) % 60).toString() + ":" + ((totalTime / 1000) % 60).toString();
        }





        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        videoView.setVideoPath("file://" + videoPath);

        videoView.start();

        playpausebutton.setOnClickListener {

            if (isFinished) {
                playpausebutton.background = resources.getDrawable(R.drawable.ic_baseline_pause_circle_24);
            } else {

                if (videoView.isPlaying) {
                   playpausebutton.background = resources.getDrawable(R.drawable.ic_baseline_play_circle_24)
                   videoView.pause();

                } else {
                   playpausebutton.background = resources.getDrawable(R.drawable.ic_baseline_pause_circle_24);
                    videoView.start();
                }

            }

        }

        seekBar2.max = Integer.valueOf(videoDuration);

        timerFunc();

        mainLayout.setOnClickListener {

            if (playpausebutton.visibility == View.INVISIBLE) {
                playpausebutton.visibility = View.VISIBLE;
                seekBar2.visibility = View.VISIBLE;
                controlsLayout.visibility = View.VISIBLE;

                var timer = object : CountDownTimer(4000, 1000) {
                    override fun onTick(p0: Long) {

                    }

                    override fun onFinish() {
                        playpausebutton.visibility = View.INVISIBLE;
                        seekBar2.visibility = View.INVISIBLE;
                        controlsLayout.visibility = View.INVISIBLE;
                    }

                }
                timer.start();
            }


        }

        seekBar2?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p2) {
                    videoView.seekTo(p1);
                    var totalTime1 = p1.toLong();
                    if (totalTime1 < 60000)
                    {
                        currentTime.text = "00 : ${(totalTime1 / 1000) % 60}";
                    }
                    else if (totalTime1 >= 60000 && totalTime1 < 3600000)
                    {
                        currentTime.text = ((totalTime1 / 60000) % 60).toString() + ":" + ((totalTime1 / 1000) % 60).toString();

                    }
                    else
                    {
                        currentTime.text = ((totalTime1 / 3600000) % 24).toString() + ":" + ((totalTime1 / 60000) % 60).toString() + ":" + ((totalTime1 / 1000) % 60).toString();
                    }
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })

        videoView.setOnCompletionListener(object : MediaPlayer.OnCompletionListener {
            override fun onCompletion(p0: MediaPlayer?) {
                p0?.reset();
                videoView.setVideoPath("file://" + videoPath);
                playpausebutton.text = "Play";
                isFinished = true;
                seekBar2.progress = 0;
                timer.cancel();
            }

        })




    }






    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

        }
    }



    private fun timerFunc() {
        timer = Timer();
        timer.schedule(object : TimerTask() {
            override fun run() {
                seekBar2.progress = videoView.currentPosition;


                runOnUiThread(object:Runnable{
                    override fun run() {
                        var totalTime3 = videoView.currentPosition.toLong();

                        if (totalTime3 < 60000)
                        {
                            currentTime.text = "00 : ${(totalTime3 / 1000) % 60}";
                        }
                        else if (totalTime3 >= 60000 && totalTime3 < 3600000)
                        {
                            currentTime.text = ((totalTime3 / 60000) % 60).toString() + ":" + ((totalTime3 / 1000) % 60).toString();

                        }
                        else
                        {
                            currentTime.text = ((totalTime3 / 3600000) % 24).toString() + ":" + ((totalTime3 / 60000) % 60).toString() + ":" + ((totalTime3 / 1000) % 60).toString();


                        }
                    }

                })





            }


        }, 1000, 1000);



    }
}