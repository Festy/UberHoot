package Parse;

import android.util.Log;

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
public class Request {
    int RETRY_LIMIT = 5;
    int WAIT_TIME = 2000; //in milis
    ParseObject ParseChat = new ParseObject("Chat");

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
        while(retry < RETRY_LIMIT){
            checkStatusForChat(requestId);
            try {
                Thread.sleep(WAIT_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry++;
        }
    }

    private void checkStatusForChat(String requestId){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Request");
        query.whereEqualTo("objectId", requestId);
        query.include("chat");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> requestObj, ParseException e) {
                if (e == null) {
                    if(requestObj.get(0).get("chat")!=null)
                        Log.d("checkStatusForChat", "Chat: " + ((ParseObject)requestObj.get(0).get("chat")).getObjectId());
                    else
                        Log.d("checkStatusForChat", "Chat is null");
                } else {
                    Log.d("checkStatusForChat", "Error: " + e.getMessage());
                }
            }
        });
    }
}
