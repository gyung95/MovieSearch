package com.ls.project.myapplication;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ls.project.myapplication.databinding.ItemRecyclerViewBinding;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private ArrayList<MovieItem> movieItems;
    private MovieList movieList;
    private Context context;

    RecyclerViewAdapter(MovieList movieList, Context context){
        this.movieList = movieList;
        this.movieItems = movieList.getMovieList();
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // recyclerView 레이아웃과 바인딩
        ItemRecyclerViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_recycler_view, parent, false);

        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

        // recyclerView의 position에 맞는 데이터 입력
        float rating = Float.parseFloat(movieItems.get(position).getUsrRating().replace("\"",""))/2;
        itemViewHolder.binding.textViewTitle.setText(movieItems.get(position).getTitle().replace("\"","").replace("<b>","").replace("</b>",""));
        itemViewHolder.binding.textViewPubDate.setText(movieItems.get(position).getPubDate().replace("\"",""));
        itemViewHolder.binding.textViewDirector.setText(movieItems.get(position).getDirector().replace("\"",""));
        itemViewHolder.binding.textViewActor.setText(movieItems.get(position).getActor().replace("\"",""));
        itemViewHolder.binding.ratingBar.setRating(rating);

        //글라이드 이미지 라이브러리 사용
        Glide.with(context).load(movieItems.get(position).getImage().replace("\"","")).into(itemViewHolder.binding.imageView);

    }

    
    @Override
    public int getItemCount() {
        return movieItems.size();
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ItemRecyclerViewBinding binding;

        ItemViewHolder(ItemRecyclerViewBinding binding){
            super(binding.getRoot());
            this.binding=binding;
        }
    }

}
