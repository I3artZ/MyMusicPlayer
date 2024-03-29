package com.bartz.mymusicplayer;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class Data {

    Context mContext;
    ArrayList<DataModel> audioList;

    public Data(Context context){
        mContext = context;
        audioList = scanDeviceForMp3Files();
        }

    private ArrayList<DataModel> scanDeviceForMp3Files(){
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION
        };
        final String sortOrder = MediaStore.Audio.AudioColumns.TITLE + " COLLATE LOCALIZED ASC";
        List<String> mp3Files = new ArrayList<>();
        ArrayList<DataModel> audioList = new ArrayList<>();

        Cursor cursor = null;
        try {
            Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            cursor = mContext.getContentResolver().query(uri, projection, selection,
                    null, sortOrder);
            if( cursor != null){
                cursor.moveToFirst();
                int n = 0;
                while( !cursor.isAfterLast() ){
                    String title = cursor.getString(0);
                    String artist = cursor.getString(1);
                    String path = cursor.getString(2);
                    String displayName  = cursor.getString(3);
                    String songDuration = cursor.getString(4);
                    cursor.moveToNext();
                    if(path != null && path.endsWith(".mp3")) {
                        mp3Files.add(path);
                        if (artist.equals("<unknown>") && title.contains(" - ")){
                            String[] parts = title.split(" - ");
                            artist = parts[0];
                            title  = parts[1];
                        }
                        audioList.add(new DataModel(n, title, artist, songDuration, displayName, path));
                        n++;
                    }
                }

            }

            // print to see list of mp3 files
            for( String file : mp3Files) {
                Log.i("TAG", file);
            }

        } catch (Exception e) {
            Log.e("TAG", e.toString());
        }finally{
            if( cursor != null){
                cursor.close();
            }
        }
        return audioList;
    }

    public ArrayList<DataModel> getAudioList() {
        return audioList;
    }
}
