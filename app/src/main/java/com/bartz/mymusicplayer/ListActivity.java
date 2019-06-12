package com.bartz.mymusicplayer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ListActivity extends AppCompatActivity {
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    public static View.OnClickListener myOnClickListener;
    private Context context;
    private ArrayList<DataModel> audioList;
    private boolean isSortedByArtist = false;
    private boolean isSortedByTime = false;
    private boolean isPlaying = false;
    int index = 0;
    public static Activity la;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // to finish activity from another one
        la = this;

        setContentView(R.layout.activity_list);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == 0) {
            try {
                Intent intent = this.getIntent();
                Bundle bundle = intent.getExtras();
                audioList = (ArrayList<DataModel>) bundle.getSerializable("audioList");
                index = bundle.getInt("index");
                isPlaying = bundle.getBoolean("isPlaying");
                if (audioList == null){
                    Data data = new Data(this);
                    audioList = data.getAudioList();
                    index = 0;
                }
            } catch (NullPointerException e) {
                Data data = new Data(this);
                audioList = data.getAudioList();
                index = 0;
            }
        } else System.exit(0);


        recyclerView = findViewById(R.id.list_recycler_view);
        myOnClickListener = new MyOnClickListener(this, recyclerView, audioList);

        //ensure that each added item is of same size (true - size of each item is fixed it won;t be
        // checked each time after insertion)
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ListAdapter(this, audioList);
        recyclerView.setAdapter(adapter);
        recyclerView.getLayoutManager().scrollToPosition(index);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }


    //check if item bar was selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //handle press on action button
        switch (item.getItemId()) {
            case R.id.action_player:
                Intent playerView = new Intent(this, PlayerActivity.class);
                startActivity(playerView);
                return true;

            case R.id.sort_duration:
                if (!isSortedByTime){
                    Collections.sort(audioList, compareByTime);
                    // update recycler view with sorted data
                    adapter.notifyDataSetChanged();
                    isSortedByTime = true;
                    isSortedByArtist = false;
                }
                else{
                    Collections.reverse(audioList);
                    // update recycler view with sorted data
                        adapter.notifyDataSetChanged();
                        isSortedByTime = false;
                }
                return true;

            case R.id.sort_artist:
                if (!isSortedByArtist){
                    Collections.sort(audioList,compareByArtist);
                    // update recycler view with sorted data
                    adapter.notifyDataSetChanged();
                    isSortedByArtist = true;
                    isSortedByTime = false;
                }
                else{
                    Collections.reverse(audioList);
                    // update recycler view with sorted data
                    adapter.notifyDataSetChanged();
                    isSortedByArtist = false;
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    Comparator<DataModel> compareByArtist= new Comparator<DataModel>() {
        @Override
        public int compare(DataModel dm1, DataModel dm2) {
            return dm1.getAuthor().compareTo(dm2.getAuthor());
        }
    };

    Comparator<DataModel> compareByTime= new Comparator<DataModel>() {
        @Override
        public int compare(DataModel dm1, DataModel dm2) {
            return dm1.getIntDuration().compareTo(dm2.getIntDuration());
        }
    };
    @Override
    protected void onStart() {
        super.onStart();
    }

    public ArrayList<DataModel> getAudioList() {
        return audioList;
    }
}
