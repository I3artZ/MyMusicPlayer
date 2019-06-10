package com.bartz.mymusicplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<String> audioList;

    public ListAdapter(Context context, ArrayList<String> audioList){
        this.context = context;
        this.audioList = audioList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        TextView textViewAuthor;
        TextView textViewDuration;
        ImageView imageViewPicture;


        public MyViewHolder(View itemView) {
            super(itemView);
            //assign extracted values to proper views in list item
            this.textViewTitle = itemView.findViewById(R.id.list_item_text_title);
            this.textViewAuthor = itemView.findViewById(R.id.list_item_text_author);
            this.textViewDuration = itemView.findViewById(R.id.list_item_text_duration);
            this.imageViewPicture = itemView.findViewById(R.id.list_item_image);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        view.setOnClickListener(ListActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        //set content to proper views
        TextView textViewTitle = holder.textViewTitle;
        TextView textViewAuthor = holder.textViewAuthor;
        TextView textViewDatePublished = holder.textViewDuration;
        ImageView imageViewPicture = holder.imageViewPicture;

        //textViewTitle.setText(dataSet.get(listPosition));
//        textViewAuthor.setText(dataSet.get(listPosition).getAuthor());
//        textViewDatePublished.setText(dataSet.get(listPosition).getDateTaken());
//        imageViewPicture.setImageBitmap(dataSet.get(listPosition).getImage());
    }


    @Override
    public int getItemCount() {
        //return dataSet.size();
        //System.out.println("audiolista"+audioList.size());
        return 10;
    }

}