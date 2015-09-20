package com.example.utsavpatel.uberhoot;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.utsavpatel.uberhoot.utils.Utils;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        displayPage();
        //Get User credentials from Uber
        //SignUp into Parse
        loginIntoParse("ankit","ankit");
    }

    private void displayPage() {
        ImageView img;
        img = (ImageView) findViewById(R.id.imageView);
        img.setImageResource(R.drawable.logo_darker);
        TextView txt1 = (TextView) findViewById(R.id.title);
        TextView txt2 = (TextView) findViewById(R.id.Street);
        TextView txt3 = (TextView) findViewById(R.id.City);
        TextView txt4 = (TextView) findViewById(R.id.State);
        TextView txt5 = (TextView) findViewById(R.id.Country);
        TextView txt6 = (TextView) findViewById(R.id.Zip);
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        double latitude = 42.9530525;
        double longitude = -78.8255916;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            //String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
            txt1.setText("You are currently at: ");
            txt2.setText(address);
            txt3.setText(city);
            txt4.setText(state);
            txt5.setText(country);
            txt6.setText(postalCode);
        } catch (IOException e) {
            e.printStackTrace();
        } catch(Exception f){
            f.printStackTrace();
        }
    }

    private void loginIntoParse(String id, String pass) {
        final ProgressDialog dia = ProgressDialog.show(this, null,
                getString(R.string.alert_wait));
        ParseUser.logInInBackground(id, pass, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Log.d("Login","Loggedn In!");
                    UserList.user = user;
                    dia.dismiss();
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                    Log.e("Login", "Error:" + e.getMessage());
                }
            }
        });
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
    }*/


    public void showChatActivity(View view){
        startActivity(new Intent(HomeActivity.this, UserList.class)); 		// THis passes the user id to the Userlist Activity to retrieve the user list
    }
}
