package com.example.utsavpatel.uberhoot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by utsavpatel on 9/19/15.
 */
public class ChatDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION_NO = 16;
    private static final String DATABASE_NAME = "mydb";
    public static final String USER_TABLE_NAME="user_table";
    public static final String CHAT_LINE_TABLE_NAME="chat_line_table";
    public static final String CHAT_ACTIVITY_TABLE_NAME="chat_activity_table";

    private static final String CREATE_USER_TABLE=
            "create table"+" "+
                    USER_TABLE_NAME+" ( "+
                    "user_id text primary key not null"+", "+
                    "user_name text"+" "+
                    ")";

    private static final String CREATE_CHAT_ACTIVITY_TABLE=
            "create table"+" "+
                    CHAT_ACTIVITY_TABLE_NAME+" ( "+
                    "chat_activity_id text primary key not null"+", "+
                    "other_user_id text"+" ,"+
                    "last_updated integer"+", "+
                    "user_id text"+" , "+
                    "FOREIGN KEY(user_id)"+" "+
                    "REFERENCES "+USER_TABLE_NAME+"(user_id)"+
                    " )";

    private static final String CREATE_CHAT_LINE_TABLE =
            "create table"+" "+
                    CHAT_LINE_TABLE_NAME  +" ( "+
                    "chat_line_id integer primary key not null"+", "+
                    //"user_name text"+", "+
                    "chat_string text"+", "+
                    "timestamp text"+", "+
                    "ownership_flag text"+", "+
                    "chat_activity_id text"+", "+
                    "FOREIGN KEY(chat_activity_id)"+" "+
                    "REFERENCES "+CHAT_ACTIVITY_TABLE_NAME+"("+"chat_activity_id"+")"+
                    ")";

    public ChatDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION_NO);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.e("UberHoot", "onCreate DB");
            db.execSQL(CREATE_USER_TABLE);
            Log.e("UberHoot", "created user table");

        }catch (Exception e){
            Log.e("UberHoot",e.getMessage());
        }

        try {
            db.execSQL(CREATE_CHAT_ACTIVITY_TABLE);
            Log.e("UberHoot", "created chat activity table");
        }
        catch (Exception e){
            Log.e("UberHoot",e.getMessage());
        }
        try{
            db.execSQL(CREATE_CHAT_LINE_TABLE);
            Log.e("UberHoot", "created chat line table");
        }catch (Exception e){
            Log.e("UberHoot",e.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("UberHoot", "Dropping old tables");
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CHAT_LINE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CHAT_ACTIVITY_TABLE_NAME);

        onCreate(db);
    }

    public int addEntry(String chatString, int chatID, int userID){
        int chatStringID=0;
        return 0;
    }

    public void deleteChatString(String chatID, int userID){

    }

    public void deleteChatBox(String charID, int userID){

    }

    public void createNewUser(String userID, String userName){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put("user_id", userID);
        contentValue.put("user_name", userName);

        db.insert(USER_TABLE_NAME, "",contentValue);
        //db.insert(USER_TABLE_NAME, "", )
    }

    public void createNewChatActivity(String userID, String otherUserID, String chatActivityID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("user_id", userID);
        contentValues.put("other_user_id", otherUserID);
        contentValues.put("chat_activity_id", chatActivityID);
        contentValues.put("last_updated" , Calendar.getInstance().getTimeInMillis());


        db.insert(CHAT_ACTIVITY_TABLE_NAME,"", contentValues);
    }

    public void addNewChatLine(ChatLine chatLine){

        ContentValues contentValues = new ContentValues();
        contentValues.put("chat_line_id", getNewChatLineID());
        contentValues.put("chat_activity_id", chatLine.getChatActivityID());
        contentValues.put("timestamp", Calendar.getInstance().getTimeInMillis());
        contentValues.put("ownership_flag", chatLine.getChatLineOwner());
        contentValues.put("chat_string", chatLine.getChatString());


        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(CHAT_LINE_TABLE_NAME, "", contentValues);

        ContentValues contentValues1 = new ContentValues();
        contentValues1.put("last_updated",Calendar.getInstance().getTimeInMillis());
        db.updateWithOnConflict(CHAT_ACTIVITY_TABLE_NAME, contentValues1, "chat_activity_id=?", new String[]{chatLine.getChatActivityID()}, SQLiteDatabase.CONFLICT_REPLACE);
    }

    private synchronized String getNewChatLineID(){
        SQLiteDatabase db = this.getReadableDatabase();

//        Cursor cursor = db.rawQuery("SELECT * from " + CHAT_ACTIVITY_TABLE_NAME , new String[]{});
//        if(cursor==null || cursor.getCount()==0){
//            return Integer.toString(1);
//        }
        Cursor cursor = db.rawQuery("SELECT max(chat_line_id) from "+CHAT_LINE_TABLE_NAME, new String[]{});
        cursor.moveToFirst();
        if(cursor==null || cursor.getCount()<=0){
            // this shoudnlt be executed
            Log.e("UberHoot", "First Line");
            return Integer.toString(1);
        }
        else{
            // cursor count must be one
            if(cursor.getString(0)==null) return Integer.toString(1);
            else return Integer.toString(Integer.parseInt(cursor.getString(0))+1);
        }
    }

    public HashMap<String, String> getAllUsers(){
        SQLiteDatabase db = getReadableDatabase();
        HashMap<String, String> map = new HashMap<String, String>();
        Cursor cursor = db.rawQuery("SELECT * FROM user_table", null);
        if(cursor.getCount()>0){
            for(int i=0; i<cursor.getCount(); i++){
                map.put(cursor.getString(0), cursor.getString(1));
                cursor.moveToNext();
            }
            return map;
        }
        return null;
    }

    public LinkedHashMap<String, String> getAllChatActivities(String ownerUserID){
        // This method will only return other user name and activity id.
        // If the user clicks, then get the whole chat.
        // also the reasults are sorted 'recentness' ;)

        SQLiteDatabase db = getReadableDatabase();
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        Cursor cursor = db.rawQuery("SELECT * FROM "+CHAT_ACTIVITY_TABLE_NAME+" WHERE user_id=? ORDER BY last_updated DESC", new String[]{ownerUserID});
        cursor.moveToFirst();
        for(int i=0; i<cursor.getCount(); i++){
            map.put(cursor.getString(cursor.getColumnIndex("other_user_id")), cursor.getString(cursor.getColumnIndex("chat_activity_id")));
            cursor.moveToNext();
        }
        if (map.size()>0) return map;
        else return null;
    }

    public  LinkedHashMap<String, HashMap<String, String>> getChatLines(String chatActivityID){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+CHAT_LINE_TABLE_NAME+" WHERE chat_activity_id=? ORDER BY timestamp DESC", new String[]{chatActivityID});
        if(cursor==null) return null;
        cursor.moveToFirst();
        LinkedHashMap<String, HashMap<String, String>> map = new LinkedHashMap<String, HashMap<String, String>>();
        for(int i=0; i<cursor.getCount(); i++){
            HashMap<String, String> innerMap = new HashMap<String, String>();
            innerMap.put(cursor.getString(cursor.getColumnIndex("ownership_flag")), cursor.getString(cursor.getColumnIndex("chat_string")));
            map.put(cursor.getString(cursor.getColumnIndex("chat_line_id")),innerMap );
            cursor.moveToNext();
        }
        if(map.size()>0) return map;
        else return null;
    }






}
