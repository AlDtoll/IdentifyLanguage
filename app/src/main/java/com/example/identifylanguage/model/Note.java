package com.example.identifylanguage.model;

public class Note {
    private String text;
    private String resultOfIdentification;

    public Note(String text, String resultOfIdentification) {
        this.text = text;
        this.resultOfIdentification = resultOfIdentification;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getResultOfIdentification() {
        return resultOfIdentification;
    }

    public void setResultOfIdentification(String resultOfIdentification) {
        this.resultOfIdentification = resultOfIdentification;
    }
}
