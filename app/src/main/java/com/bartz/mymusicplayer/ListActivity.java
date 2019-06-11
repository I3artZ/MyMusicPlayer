package com.bartz.mymusicplayer;

import android.Manifest;
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

public class ListActivity extends AppCompatActivity {
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    public static View.OnClickListener myOnClickListener;
    private Context context;
    private ArrayList<DataModel> audioList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == 0) {
            Data data = new Data(this);
            audioList = data.getAudioList();
        }else
            System.exit(0);


        recyclerView = findViewById(R.id.list_recycler_view);
        myOnClickListener = new MyOnClickListener(this, recyclerView);

        //ensure that each added item is of same size (true - size of each item is fixed it won;t be
        // checked each time after insertion)
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ListAdapter(this, audioList);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    //check if item bar was selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //handle press on action button
        switch (item.getItemId()) {
//            case R.id.action_list_view:
//                NavUtils.navigateUpFromSameTask(this);
//                return true;
            case R.id.action_list_view:
                Intent playerView = new Intent(this, PlayerActivity.class);
                startActivity(playerView);
                return true;
//            case R.id.action_about:
//                startActivity(new Intent(this, AboutActivity.class));
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }
//
    @Override
    protected void onStart() {
        super.onStart();
    }
}
