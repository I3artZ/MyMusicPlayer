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
    public static Activity fa;

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

        fa = this;

        index = getIntent().getIntExtra("index", 0);

        Data data = new Data(this);
        audioList = data.getAudioList();

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



        //mediaPlayer.prepare();
        Uri song = Uri.parse(audioList.get(index).getPath());
        mediaPlayer = MediaPlayer.create(this, song);
        mediaPlayer.seekTo(0);
        totalTime = mediaPlayer.getDuration();

        displayName = audioList.get(index).getDisplayName();
        displayNameTextView.setText(displayName);

        // position bar
        timeElapsedBar.setMax(totalTime);
        timeElapsedBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress,
                                                  boolean fromUser) {
                        // "if" to not iterating twice each time ( was lagging music)
                        if(fromUser) {
                            mediaPlayer.seekTo(progress);
                        }

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }

                });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null){
                    try{
                        Message msg = new Message();
                        msg.what = mediaPlayer.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Log.e("Thread", e + " occurred");
                    }
                }
            }
        }).start();
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
        clearMediaPlayer();
        //mediaPlayer.prepare();
        Uri song = Uri.parse(audioList.get(index).getPath());
        mediaPlayer = MediaPlayer.create(this, song);
        mediaPlayer.seekTo(0);
        totalTime = mediaPlayer.getDuration();

        // position bar
        timeElapsedBar.setMax(totalTime);
        timeElapsedBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress,
                                                  boolean fromUser) {
                        // "if" to not iterating twice each time ( was lagging music)
                        if(fromUser) {
                            mediaPlayer.seekTo(progress);
                        }

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }

                });
        if (wasPlaying)
            clickPlay(view);
        }

    public void clickPrevious(View view){
        if (index == 0)
            index = audioList.size()-1;
        else
            --index;
        boolean wasPlaying = mediaPlayer.isPlaying();
        clearMediaPlayer();
        //mediaPlayer.prepare();
        Uri song = Uri.parse(audioList.get(index).getPath());
        mediaPlayer = MediaPlayer.create(this, song);
        mediaPlayer.seekTo(0);
        totalTime = mediaPlayer.getDuration();

        // position bar
        timeElapsedBar.setMax(totalTime);
        timeElapsedBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress,
                                                  boolean fromUser) {
                        // "if" to not iterating twice each time ( was lagging music)
                        if(fromUser) {
                            mediaPlayer.seekTo(progress);
                        }

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }

                });

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
//            case R.id.action_settings:
//                NavUtils.navigateUpFromSameTask(this);
//                return true;
            case R.id.action_list_view:
                Intent listView = new Intent(this, ListActivity.class);
                startActivity(listView);
                return true;
//            case R.id.action_about:
//                startActivity(new Intent(this, AboutActivity.class));
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearMediaPlayer() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    private void setSong(){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearMediaPlayer();
    }


}
