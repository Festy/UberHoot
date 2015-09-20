package Parse;

import com.parse.ParseUser;

/**
 * Created by danza on 9/19/15.
 */
public class Chat {

    public void startNewChat(final ParseUser user, final String requestType, final boolean alive){
        Request request = new Request();
        boolean requestCreated = request.createNewRequest(user,requestType,alive);
    }


}
