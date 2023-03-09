package com.example.trivia_wars;

import java.util.List;

public class Question {
    private String question;
    private List<String> answerList;
    private String correctAnswer;

    public Question (String question, List<String> answerList, String correctAnswer){
        this.question = question;
        this.answerList = answerList;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion(){
        return question;
    }

    public List<String> getAnswerList(){
        return answerList;
    }

    public String getCorrectAnswer(){
        return correctAnswer;
    }

    public void setQuestion(String question){
        this.question = question;
    }

    public void setAnswerList(List<String> answerList){
        this.answerList = answerList;
    }

    public void setCorrectAnswer(String correctAnswer){
        this.correctAnswer = correctAnswer;
    }
}
