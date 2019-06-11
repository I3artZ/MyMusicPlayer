package com.bartz.mymusicplayer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MyOnClickListener implements View.OnClickListener {

    private final Context context;
    private final RecyclerView recyclerView;

    public MyOnClickListener(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @Override
    public void onClick(final View view) {
        //get index of clicked picture
        int itemPosition = recyclerView.getChildLayoutPosition(view);
        Intent player = new Intent(context, PlayerActivity.class);
        player.putExtra("index", itemPosition);
        if (PlayerActivity.fa != null) {
            PlayerActivity.fa.finish();
        }
        context.startActivity(player);
        //Toast.makeText(context, itemPosition+"", Toast.LENGTH_LONG).show();
    }

}