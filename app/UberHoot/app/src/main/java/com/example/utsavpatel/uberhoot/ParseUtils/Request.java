package com.example.utsavpatel.uberhoot.ParseUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.example.utsavpatel.uberhoot.R;
import com.example.utsavpatel.uberhoot.UserList;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.List;

/**
 * Created by danza on 9/19/15.
 */
public class Request extends Activity{
    int RETRY_LIMIT = 5;
    int WAIT_TIME = 2000; //in milis
    ParseObject ParseChat = new ParseObject("Chat");
    UserList context;
    boolean chatStarted;

    public Request(UserList applicationContext) {
        context = applicationContext;
        chatStarted = false;
    }

    public boolean createNewRequest(final ParseUser user, final String requestType, final boolean alive){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("username", user.getUsername());
        ParseCloud.callFunctionInBackground("killAllRequests", params, new FunctionCallback<Boolean>() {
            public void done(Boolean status, ParseException e) {
                if (e == null) {
                    if(status){
                        sendRequestToCloud(user,requestType,alive);
                        Log.d("createNewRequest", "All Previous requests deleted");
                    }
                } else{
                    Log.e("createNewRequest", "All Previous requests could not be deleted");
                    e.printStackTrace();
                }
            }
        });
        return true;
    }

    private void sendRequestToCloud(ParseUser user, String requestType, boolean alive) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("requestType", requestType);
        params.put("alive", alive);
        ParseCloud.callFunctionInBackground("chatRequest", params, new FunctionCallback<String>() {
            @Override
            public void done(String parseObject, ParseException e) {
                if (e == null) {
                    Log.d("sendRequestToCloud","success!: "+parseObject);
                    isChatCreatedAfterRequest(parseObject);
                } else{
                    Log.e("sendRequestToCloud",e.getMessage());
                }
                if(parseObject!=null){
                    Log.d("sendRequestToCloud","parseObject:"+parseObject);
                }
            }
        });
    }

    private void isChatCreatedAfterRequest(String requestId){
        int retry = 0;
        //context.startDia();
        while(retry < RETRY_LIMIT && !chatStarted){
            checkStatusForChat(requestId);
            try {
                Thread.sleep(WAIT_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry++;
        }
        if(!chatStarted){
            context.createToast("Cannot Find Any Users Online");
        }

        context.stopDia();
    }

    private void checkStatusForChat(String requestId){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Request");
        query.whereEqualTo("objectId", requestId);
        query.include("chat");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> requestObj, ParseException e) {
                if (e == null) {
                    if(requestObj.get(0).get("chat")!=null) {
                        chatStarted = true;
                        Log.d("checkStatusForChat", "Chat: " + ((ParseObject) requestObj.get(0).get("chat")).getObjectId());
                        String friendId = "Joker";
                        try {
                            if(((ParseUser) ((ParseObject) requestObj.get(0).get("chat")).get("user1")).fetchIfNeeded().getUsername().equals(ParseUser.getCurrentUser().getUsername())){
                                friendId = ((ParseUser) ((ParseObject) requestObj.get(0).get("chat")).get("user2")).fetchIfNeeded().getUsername();
                            } else {
                                friendId = ((ParseUser) ((ParseObject) requestObj.get(0).get("chat")).get("user1")).fetchIfNeeded().getUsername();
                            }
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        Log.d("ParseObject", "Starting Chat:" + friendId);
                        context.stopDia();
                        context.StartChatActivity(friendId);
                    }
                    else
                        Log.d("checkStatusForChat", "Chat is null");
                } else {
                    Log.d("checkStatusForChat", "Error: " + e.getMessage());
                }
            }
        });
    }
}
