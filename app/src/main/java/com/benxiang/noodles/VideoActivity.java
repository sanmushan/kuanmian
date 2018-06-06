package com.benxiang.noodles;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class VideoActivity extends AppCompatActivity {
    VideoView videoView;
    RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        videoView = (VideoView) findViewById(R.id.videoView);
        relativeLayout = (RelativeLayout) findViewById(R.id.videoActivity);
        new Thread(new TimeThread()).start();
    }

    private void initVideoView(final int time) {
        {
            try{
                MediaController mc = new MediaController(this);
                mc.setVisibility(View.INVISIBLE);
                videoView.setMediaController(mc);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.FILL_PARENT,
                        RelativeLayout.LayoutParams.FILL_PARENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                videoView.setLayoutParams(layoutParams);
                Uri videoUri = Uri.parse(Environment.getExternalStorageDirectory()
                        .getPath() + "/c.mp4");
                videoView.setVideoURI(videoUri);
                videoView.start();
                //顶层获取焦点
                videoView.requestFocus();
                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        if (time >= 22 || time <= 8){//|| mTime < 8
                            //全屏
                            mediaPlayer.setVolume(0f, 0f);
                        }
                        mediaPlayer.start();
                        mediaPlayer.setLooping(true);
                    }
                });

            }catch(Exception e){
                e.printStackTrace();
            }
        }



        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(VideoActivity.this,MainActivity.class);
                startActivity(in);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(videoView!=null){
            if(videoView!=null){
                videoView.suspend();
            }
        }
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    long time = System.currentTimeMillis();
                    Date date = new Date(time);
//                    SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒 EEEE");
                    SimpleDateFormat format = new SimpleDateFormat("HH");
                    String data = format.format(date);
                    int mTime = Integer.parseInt(data);
                    initVideoView(mTime);
                    break;
                default:
                    break;

            }
        }
    };
    class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Message msg = new Message();
                    msg.what = 1;  //消息(一个整型值)
                    mHandler.sendMessage(msg);// 每隔1秒发送一个msg给mHandler
                    Thread.sleep(35*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }
}
