package com.example.trivia_wars;

import java.util.List;

public class Score {
    private String category;
    private String title;
    private int score;

    public Score(String title, String category, int score){
        this.category = category;
        this.title = title;
        this.score = score;
    }

    public String getcategory(){
        return category;
    }

    public String gettitle(){
        return title;
    }

    public int  getscore(){
        return score;
    }

    public void setcategory(String category){
        this.category = category;
    }

    public void settitle(String title){
        this.title = title;
    }

    public void setscore(int score){
        this.score = score;
    }
}
