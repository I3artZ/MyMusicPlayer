package com.bartz.mymusicplayer;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    ImageView imageView;

    ImageButton previous;
    ImageButton rewind;
    ImageButton play;
    ImageButton forward;
    ImageButton next;

    SeekBar timeElapsedBar;
    public static Activity pa;

    TextView displayNameTextView;
    TextView elapsedTimeTextView;
    TextView remainingTimeTextView;

    MediaPlayer mediaPlayer;

    int totalTime;
    int currentPosition;
    ArrayList<DataModel> audioList;
    int index;
    String displayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pa = this;

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        try{
            audioList = (ArrayList<DataModel>) bundle.getSerializable("audioList");
            index = bundle.getInt("index");
        } catch (NullPointerException e){
            if (audioList == null) {
                Data data = new Data(this);
                audioList = data.getAudioList();
                index = 0;
            }
        }

        setContentView(R.layout.activity_player);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageView = findViewById(R.id.musicImage);

        previous = findViewById(R.id.previous);
        rewind = findViewById(R.id.rewind);
        play = findViewById(R.id.play);
        forward = findViewById(R.id.forward);
        next = findViewById(R.id.next);

        timeElapsedBar = findViewById(R.id.seekBar);

        displayNameTextView = findViewById(R.id.display_name);
        elapsedTimeTextView = findViewById(R.id.current_time);
        remainingTimeTextView = findViewById(R.id.remaining_time);

        //prepare song to playing
        setSong();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null) {
//                    try {
//                        currentPosition = mediaPlayer.getCurrentPosition();
//                        System.out.println(currentPosition +" current position");
//                    } catch (IllegalStateException eState) {
//                        System.out.println(eState + "current position2");
//                        currentPosition = mediaPlayer.getCurrentPosition();
//                        System.out.println(currentPosition +" current position2");
//                    }
                    try {
                        Message msg = new Message();
                        msg.what = mediaPlayer.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    Log.e("Thread", e + " occurred");
                }
        }}}).start();
    }

    private Handler handler = new Handler(new Handler.Callback() {

        @Override public boolean handleMessage(Message msg) {
            int currentPosition = msg.what; // Update bar
            timeElapsedBar.setProgress(currentPosition); // Update Labels.

            String elapsedTime = DataModel.createTimeLabel(currentPosition);
            elapsedTimeTextView.setText(elapsedTime);
            String remainingTime = "- " + DataModel.createTimeLabel(totalTime - currentPosition);
            remainingTimeTextView.setText(remainingTime);
            return true; } });

    public void clickPlay(View view) {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                play.setImageResource(R.drawable.pause);
        } else {
                // song is playing
                mediaPlayer.pause();
                play.setImageResource(R.drawable.play_button);
            }

    }

    public void clickForward(View view){
        int forwardTime = 10;
        if (mediaPlayer != null){
            currentPosition = timeElapsedBar.getProgress() + forwardTime * 1000;
            mediaPlayer.seekTo(currentPosition);
            timeElapsedBar.setProgress(currentPosition);
        }
    }

    public void clickRewind(View view){
        int rewindTime = 10;
        if (mediaPlayer != null){
            currentPosition = timeElapsedBar.getProgress() - rewindTime * 1000;
            mediaPlayer.seekTo(currentPosition);
            timeElapsedBar.setProgress(currentPosition);
        }
    }

    public void clickNext(View view){
        if (index == audioList.size()-1)
            index = 0;
        else
            ++index;
        boolean wasPlaying = mediaPlayer.isPlaying();
        mediaPlayer.reset();
        setSong();
        if (wasPlaying)
            clickPlay(view);
        }

    public void clickPrevious(View view){
        if (index == 0)
            index = audioList.size()-1;
        else
            --index;
        boolean wasPlaying = mediaPlayer.isPlaying();
        mediaPlayer.reset();
        setSong();
        //start playing previous song if current one was playing
        if (wasPlaying)
            clickPlay(view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_list_view:
                Intent listView = new Intent(this, ListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("audioList", audioList);
                bundle.putInt("index", index);
                bundle.putBoolean("isPlaying", mediaPlayer.isPlaying());
                listView.putExtras(bundle);
                if (ListActivity.la != null) {
                    ListActivity.la.finish();
                }
                startActivity(listView);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void clearMediaPlayer() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    private void setSong(){

        // get song path and create mediaplayer
        Uri song = Uri.parse(audioList.get(index).getPath());
        mediaPlayer = MediaPlayer.create(this, song);

        // set song to beginning
        mediaPlayer.seekTo(0);

        totalTime = mediaPlayer.getDuration();

        // song display name (artist/title)
        displayName = audioList.get(index).getDisplayName();
        displayNameTextView.setText(displayName);

        // position bar
        timeElapsedBar.setMax(totalTime);
        timeElapsedBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress,
                                                  boolean fromUser) {
                        // "if" to not iterate twice each time (music was lagging)
                        if(fromUser) {
                            mediaPlayer.seekTo(progress);
                        }
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}

                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearMediaPlayer();
    }


}
