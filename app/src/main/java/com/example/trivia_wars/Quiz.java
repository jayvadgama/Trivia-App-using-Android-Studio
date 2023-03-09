package com.example.trivia_wars;

import java.util.List;

public class Quiz {
    private String category;
    private String title;
    private List<Question> questionsList;

    public Quiz (String category, String title, List<Question>  questionsList){
        this.category = category;
        this.title = title;
        this.questionsList = questionsList;
    }

    public String getcategory(){
        return category;
    }

    public String gettitle(){
        return title;
    }

    public List<Question>  getquestionsList(){
        return questionsList;
    }

    public void setcategory(String category){
        this.category = category;
    }

    public void settitle(String title){
        this.title = title;
    }

    public void setquestionsList(List<Question>  questionsList){
        this.questionsList = questionsList;
    }
}
