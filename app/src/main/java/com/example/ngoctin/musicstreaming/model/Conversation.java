package com.example.ngoctin.musicstreaming.model;

import java.util.ArrayList;

public class Conversation {
    private ArrayList<Message> listMessageData;

    public Conversation(){
        listMessageData = new ArrayList<>();
    }

    public ArrayList<Message> getListMessageData() {
        return listMessageData;
    }
}