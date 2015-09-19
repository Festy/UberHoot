package com.example.utsavpatel.uberhoot;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by utsavpatel on 9/19/15.
 */
public class ChatLine {

    public String chatString;
    public long timeStamp;
    public String chatStringOwnerID;
    public String chatLineID;
    public String chatActivityID;

    public ChatLine(String chatString, String owner, String chatActivityID){

        this.chatString = chatString;
        this.chatStringOwnerID = owner;
        this.timeStamp = Calendar.getInstance().getTimeInMillis();
        //this.chatLineID = chatLineID;
        this.chatActivityID = chatActivityID;

    }

    public String getChatString(){
        return chatString;
    }

    public String getChatLineOwner(){
        return chatStringOwnerID;
    }

//    public String getChatLineID(){
//        return chatLineID;
//    }

    public String getChatActivityID(){
        return chatActivityID;
    }
//
//    public String getTimeStamp(){
//        return Date.get
////        SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("yyyy.MMMMM.dd GGG hh:mm aaa");
////        simpleDateFormatter.format(timeStamp);
////        return simpleDateFormatter.toString();
//    }



}
