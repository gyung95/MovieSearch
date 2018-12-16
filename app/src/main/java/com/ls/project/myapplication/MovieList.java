package com.ls.project.myapplication;

import java.util.ArrayList;

public class MovieList {
    private ArrayList<MovieItem> movieList;
    private int listNum;

    public MovieList() {
        movieList=new ArrayList<MovieItem>();
        listNum=0;
    }

    public void addListItem(MovieItem item){
        movieList.add(item);
    }

    public ArrayList<MovieItem> getMovieList() {
        return movieList;
    }

    public void setMovieList(ArrayList<MovieItem> movieList) {
        this.movieList = movieList;
    }

    public int getListNum() {
        return listNum;
    }

    public void setListNum(int listNum) {
        this.listNum = listNum;
    }
}
