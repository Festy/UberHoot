package com.example.utsavpatel.uberhoot.ParseUtils;

import android.content.Context;

import com.example.utsavpatel.uberhoot.UserList;
import com.parse.ParseUser;

/**
 * Created by danza on 9/19/15.
 */
public class ChatUtils {

    UserList context;
    public ChatUtils(UserList applicationContext) {
        context = applicationContext;
    }

    public void startNewChat(final ParseUser user, final String requestType, final boolean alive, UserList userListClass){
        Request request = new Request(context);
        boolean requestCreated = request.createNewRequest(user,requestType,alive);
    }

}
