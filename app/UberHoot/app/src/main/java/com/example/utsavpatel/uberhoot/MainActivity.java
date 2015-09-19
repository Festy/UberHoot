package com.example.utsavpatel.uberhoot;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        dbTester();
    }

public void dbTester(){
    // Just checking the DB
    // removing Button to get some space
    Log.e("UberHoot", "Running DB Tester");
    //findViewById(R.id.button).setVisibility(View.GONE);
    ChatDBHelper helper = new ChatDBHelper(getApplicationContext());
    Log.e("UberHoot", "Now creating new User");

    helper.createNewUser("123", "Utsav Patel");
    Log.e("UberHoot", "user entry created ");

    helper.createNewChatActivity("123", "other user", "activity123");
    helper.createNewChatActivity("123", "Random user", "activity124");
    helper.createNewChatActivity("124", "Random user", "activity125");

    helper.addNewChatLine(new ChatLine("This is my first line in chat 123", "other user", "activity123"));
    helper.addNewChatLine(new ChatLine("The one where you fly and I don't" ,"123" ,"activity123"));
    helper.createNewChatActivity("123", "Random user 2", "activity126");
    helper.addNewChatLine(new ChatLine("This is my second line in chat 123", "other user", "activity123"));
    helper.addNewChatLine(new ChatLine("This is my first line in chat 123", "123", "activity123"));

    LinkedHashMap map = helper.getAllChatActivities("123");
    for(Object s:  map.keySet()){
        //Log.e("UberHoot",s+" "+map.get(s) );
    }
    LinkedHashMap<String, HashMap<String, String>> map2 = helper.getChatLines("activity123");
    for(Object s:  map2.values()){
        for(Map.Entry a:((HashMap<String, String>) s).entrySet())
        Log.e("UberHoot",a.getKey()+" "+a.getValue() );
    }



}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
