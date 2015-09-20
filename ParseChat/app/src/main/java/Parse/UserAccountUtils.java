package Parse;

import android.util.Log;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by danza on 9/19/15.
 */
public class UserAccountUtils {
    public void signUpUser(final String username, String password, String email){
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("SignUp", "Success. username:" + username);
                } else {
                    Log.e("SignUp", "Error. username:" + username);
                }
            }
        });
    }

    public void signInUser(final String username, String password){
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (e == null && user != null) {
                    Log.d("Login", "Success. username:" + user.getUsername());
                } else if (user == null) {
                    Log.e("SignUp", "usernameOrPasswordIsInvalid. username:" + username);
                } else {
                    Log.e("SignUp", "somethingWentWrong. username:" + username);
                }
            }
        });
    }
}
