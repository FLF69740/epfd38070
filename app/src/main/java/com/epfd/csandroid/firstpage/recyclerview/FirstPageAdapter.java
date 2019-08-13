package com.epfd.csandroid.firstpage.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.epfd.csandroid.R;
import com.epfd.csandroid.models.News;

import java.util.List;

public class FirstPageAdapter extends RecyclerView.Adapter<FirstPageViewHolder> {

    private List<News> mNewsList;

    public FirstPageAdapter(List<News> newsList) {
        mNewsList = newsList;
    }

    @NonNull
    @Override
    public FirstPageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.news_recycler_item, parent, false);
        return new FirstPageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FirstPageViewHolder holder, int position) {
        holder.setViewHolder(mNewsList.get(position));
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }
}
