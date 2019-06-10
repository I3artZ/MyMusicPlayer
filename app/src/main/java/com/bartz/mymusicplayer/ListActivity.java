package com.bartz.mymusicplayer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.UriPermission;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    //private ArrayList<DataModel> data;
    public static View.OnClickListener myOnClickListener;
    private Context context;
    private ArrayList<String> audioList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        //get data
        //data = DataDownload.getMyData();

        recyclerView = findViewById(R.id.list_recycler_view);
        //myOnClickListener = new MyOnClickListener(this, recyclerView);

        //ensure that each added item is of same size (true - size of each item is fixed it won;t be
        // checked each time after insertion)
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == 0) {
            audioList = scanDeviceForMp3Files();
            if(audioList.size() > 0) {
                System.out.println("audiolista" + audioList.size());
                System.out.println("audiolista" + audioList.toString());
                adapter = new ListAdapter(this, audioList);
                recyclerView.setAdapter(adapter);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu - adding items to actions bar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private ArrayList<String> scanDeviceForMp3Files() {

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION
        };
        final String sortOrder = MediaStore.Audio.AudioColumns.TITLE + " COLLATE LOCALIZED ASC";
        ArrayList<String> mp3Files = new ArrayList<>();

        Cursor cursor = null;
        try {
            Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            cursor = getContentResolver().query(uri, projection, selection,
                    null, sortOrder);
            if (cursor != null) {
                cursor.moveToFirst();

                while (!cursor.isAfterLast()) {
                    String title = cursor.getString(0);
                    String artist = cursor.getString(1);
                    String path = cursor.getString(2);
                    String displayName = cursor.getString(3);
                    String songDuration = cursor.getString(4);
                    cursor.moveToNext();
                    if (path != null && path.endsWith(".mp3")) {
                        mp3Files.add(path);
                    }
                }

            }

            // print to see list of mp3 files
            for (String file : mp3Files) {
                Log.i("TAG", file);
            }

        } catch (Exception e) {
            Log.e("TAG", e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return mp3Files;

//    //check if item bar was selected
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        //handle press on action button
//        switch (item.getItemId()) {
//            case R.id.action_list_view:
//                NavUtils.navigateUpFromSameTask(this);
//                return true;
//            case R.id.action_grid_view:
//                Intent gridView = new Intent(this, GridActivity.class);
//                startActivity(gridView);
//                return true;
//            case R.id.action_about:
//                startActivity(new Intent(this, AboutActivity.class));
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
}
