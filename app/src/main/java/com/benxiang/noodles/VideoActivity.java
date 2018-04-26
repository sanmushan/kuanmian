package com.benxiang.noodles;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {
    VideoView videoView;
    RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        videoView = (VideoView) findViewById(R.id.videoView);
        relativeLayout = (RelativeLayout) findViewById(R.id.videoActivity);
        //初始化 videoView
        initVideoView();
    }
    private void initVideoView() {
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
}
