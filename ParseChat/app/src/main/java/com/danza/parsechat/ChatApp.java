package com.danza.parsechat;

import android.app.Application;
import android.util.Log;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import Parse.Chat;

public class ChatApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "Cm9ccxez45NH1WfpXr165sKc8we6CXY0aFU7lTkY", "PWr2hGd42CPSMtSBJv9zwnFO5LPAqUyO2nca8DqS");

        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();

       /* ParseUser user = new ParseUser();
        user.setUsername("ankitsh");
        user.setPassword("ankit");
        user.setEmail("email@example.com");
        //user.signUpInBackground();
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("SignUp","Signup Successful!");
                } else {
                    Log.e("SignUp","Signup Error!:"+e.getMessage());
                    e.printStackTrace();
                }
            }
        });*/


        ParseUser.logInInBackground("ankitsh", "ankit", new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Log.d("Login","Success");
                    Chat chat = new Chat();
                    chat.startNewChat(ParseUser.getCurrentUser(), "random", true);
                } else {
                    Log.e("Login","error:"+e.getMessage());
                }
            }
        });



        /*ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.whereEqualTo("username","ankit");
        final String[] temp = {"1"};
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> userList, ParseException e) {
                if (e == null) {
                    Log.d("user", "Retrieved " + userList.size() + " users");
                    if(userList.size()>0)
                       Log.d("user", "Username " + userList.get(0).get("username"));
                    temp[0] = "3";
                } else {
                    Log.d("user", "Error: " + e.getMessage());
                }
            }
        });
        Log.i("temp",temp[0]);*/


        /*try {
            List<ParseObject> userList = query.findInBackground();
            Log.d("user2", "Retrieved " + userList.size() + " users");
            if(userList.size()>0)
                Log.d("user2", "Username " + userList.get(0).get("username"));
            temp[0] = "2";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.i("temp",temp[0]);*/
    }

}
