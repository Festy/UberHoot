package com.example.utsavpatel.uberhoot;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by utsavpatel on 9/19/15.
 */
public class ChatLineIDGenerator {
    private int ChatLineID=0;
    private static final ChatLineIDGenerator chatLineIDGenerator= new ChatLineIDGenerator();

    private ChatLineIDGenerator(){

    }

    public synchronized static String getNewID(SQLiteDatabase db){
        return Integer.toString(chatLineIDGenerator.ChatLineID++);
    }
}
