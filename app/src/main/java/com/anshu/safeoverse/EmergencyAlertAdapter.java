package com.anshu.safeoverse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EmergencyAlertAdapter extends RecyclerView.Adapter<EmergencyAlertAdapter.MyViewHolder> {
    Context context;
    ArrayList<newsFeed> arrayList;
    public EmergencyAlertAdapter(Context context, ArrayList<newsFeed> arrayList)
    {
        this.context = context;
        this.arrayList = arrayList;
    }
    @NonNull
    @Override
    public EmergencyAlertAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.news_card,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EmergencyAlertAdapter.MyViewHolder holder, int position) {
        newsFeed news = arrayList.get(position);
        holder.newsHeadline.setText(news.headline);
        holder.newsDescription.setText(news.mainContent);
        Picasso.get()
                .load(news.imageUrl)
                .into(holder.newsImage);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView newsHeadline,newsDescription;
        ImageView newsImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            newsHeadline = itemView.findViewById(R.id.headline);
            newsDescription = itemView.findViewById(R.id.newsDescription);
            newsImage = itemView.findViewById(R.id.newsImage);
        }
    }
}
