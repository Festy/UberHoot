package com.example.utsavpatel.uberhoot;

/**
 * Created by utsavpatel on 9/19/15.
 */
public class ChatActivityIDGenerator {
    private int ChatActivityID=0;
    private static final ChatActivityIDGenerator chatActivityIDGenerator= new ChatActivityIDGenerator();

    private ChatActivityIDGenerator(){

    }

    public synchronized static String getNewID(){
        //TODO: This will fail once the app has stopped, as it will start again at 0.
        //TODO: So, write code that will return max(chat_activity_id) from the table.
        //TODO: And set this as the new starting ID
        //TODO: Do the same for other generator too.
        return Integer.toString(chatActivityIDGenerator.ChatActivityID++);
    }
}
