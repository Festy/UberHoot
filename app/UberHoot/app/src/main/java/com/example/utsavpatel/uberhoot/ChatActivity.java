package com.example.utsavpatel.uberhoot;

import android.util.Log;
import java.util.*;

/**
 * Created by utsavpatel on 9/19/15.
 */
public class ChatActivity {

    private ArrayList<ChatLine> chatLineList;
    private String chatActivityID;

    public ChatActivity(String chatActivityID){
        chatLineList = new ArrayList<ChatLine>();
        this.chatActivityID = chatActivityID; //ChatActivityIDGenerator.getNewID();
    }

    public boolean deleteChat(){
        //TODO: Delete from the database
        return true;
    }

    public ArrayList<ChatLine> getChatLineList(){
        if(chatLineList==null){
            Log.e("UberHoot", "Requested chat text for a null chat");
            return null;
        }
        return chatLineList;
    }

    public void addChatLine(ChatLine chatLine){
        chatLine.chatActivityID = this.chatActivityID;
        chatLineList.add(chatLine);
    }

    public String getChatActivityID(){
        return chatActivityID;
    }

    public int getSize(){
        return chatLineList.size();
    }



//    private boolean flushDatabase()
}
