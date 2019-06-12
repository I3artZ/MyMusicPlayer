package com.bartz.mymusicplayer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

public class MyOnClickListener implements View.OnClickListener {

    private final Context context;
    private final RecyclerView recyclerView;
    private ArrayList<DataModel> audioList;

    public MyOnClickListener(Context context, RecyclerView recyclerView, ArrayList<DataModel> audioList) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.audioList = audioList;
    }

    @Override
    public void onClick(final View view) {
        //get index of clicked picture
        int itemPosition = recyclerView.getChildLayoutPosition(view);

        Intent player = new Intent(context, PlayerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("audioList", audioList);
        bundle.putInt("index", itemPosition);
        player.putExtras(bundle);
        if (PlayerActivity.pa != null) {
            PlayerActivity.pa.finish();
        }
        context.startActivity(player);
        //Toast.makeText(context, itemPosition+"", Toast.LENGTH_LONG).show();
    }

}