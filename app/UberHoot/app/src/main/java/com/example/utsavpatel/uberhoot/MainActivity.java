package com.example.utsavpatel.uberhoot;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.victorsima.uber.UberClient;
import com.victorsima.uber.model.Products;
import com.victorsima.uber.model.request.Request;
import com.victorsima.uber.model.request.RequestBody;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import retrofit.RestAdapter;

public class MainActivity extends Activity {

    public static final String UBER_CLIENT_ID = "rdFS48Oi-Bq2pbxmDGpd59-65iqcz8BX";
    public static final String UBER_CLIENT_SECRET = "AXPHWrKqAGj-dlF8EokQFa0leMhpkWB8AeEvxlhY";
    public static final String UBER_SERVER_TOKEN = "raZWSXFJjMHDPthqgOhFLpIBptsfiWm2PceDf1El";
    public static final String UBER_REDIRECT_URI = "https://com.example.utsavpatel.uberhoot";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

    }

   /* @Override
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
    }*/

    public void loginWithUber(View view) {

        /*final WebView web = new WebView(this);
        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl(URLEncoder.encode("https://login.uber.com/oauth/authorize") + "?redirect_uri=" + URLEncoder.encode(UBER_REDIRECT_URI) + "&response_type=code" + "&client_id=" + UBER_CLIENT_ID + "&scope=profile");
        web.setWebViewClient(new WebViewClient() {

            boolean authComplete = false;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Overridea
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("~~~~~~~~~~~~~~~~~", "Fetched URL : " + url);
                if (url.contains("?" + "code" + "=") && authComplete != true) {
                    Uri uri = Uri.parse(url);
                    Log.d("~~~~~~~~~~~~~~~~~", "Got Authorization code : " + uri.getQueryParameter("code"));
                    authComplete = true;
                    web.setVisibility(View.GONE);
                } else if (url.contains("error=")) {
                    Log.d("~~~~~~~~~~~~~~~~~", "CAUGHT ERROR");
                    authComplete = true;
                    setResult(Activity.RESULT_CANCELED, null);
                    Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });*/

        Map<String, String> params = new HashMap<>();
        String auth = "Token " + UBER_SERVER_TOKEN;
        params.put("response_type", "code");
        params.put("client_id", UBER_CLIENT_ID);
        params.put("client_secret", UBER_CLIENT_SECRET);

        WebView webView = new WebView(this);
        this.setContentView(webView);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                Map<String, String> params = new HashMap<>();
                //String auth = "Token " + UBER_SERVER_TOKEN;
                params.put("response_type", "token");
                params.put("client_id", UBER_CLIENT_ID);
                params.put("client_secret", UBER_CLIENT_SECRET);
                //view.loadUrl(url, params);
                Log.e("~~~~~~~~~~~~~~~~~~~~", url);
                if(url.split("code=").length>1){
                    String code = url.split("code=")[1];
                    view.loadUrl("https://login.uber.com/oauth/authorize" + "?client_secret=" + UBER_CLIENT_SECRET
                            + "&client_id=" + UBER_CLIENT_ID + "&grant_type=" + code + "&redirect_uri=" + URLEncoder.encode(UBER_REDIRECT_URI)
                            + "&code=" + code + "&response_type=code");
                    if(!view.getUrl().toLowerCase().contains("error")) {
                        Intent returnAcc = new Intent(getApplicationContext(),HomeActivity.class);
                        startActivity(returnAcc);
                        finish();
                    }
                }
                return false;
            }
        });

//        Map<String, String> params = new HashMap<>();
//        String auth = "Token " + UBER_SERVER_TOKEN;
//        params.put("response_type", "code");
//        params.put("client_id", UBER_CLIENT_ID);
//        params.put("client_secret", UBER_CLIENT_SECRET);

        String url = "https://login.uber.com/oauth/authorize";
        //webView.loadUrl(url, params);
        webView.loadUrl("https://login.uber.com/oauth/authorize" + "?redirect_uri=" + URLEncoder.encode(UBER_REDIRECT_URI) + "&response_type=code" + "&client_id=" + UBER_CLIENT_ID + "&scope=profile");


        /*
        URL url = null;
        try {
            url = new URL("https://login.uber.com/oauth/authorize");
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException in Uber Login URL");
            e.printStackTrace();
        }
        HttpTask httpTask = new HttpTask();
        httpTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, url);
        try {
            String loginFormHtml = httpTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/

        /*
        url = new URL("https://login.uber.com/oauth/authorize?response_type=code&redirect_uri=https%3A%2F%2Fuhoot.bigred.hack&scope=request&client_id=rdFS48Oi-Bq2pbxmDGpd59-65iqcz8BX");

        WebView webView = new WebView(this);
        this.setContentView(webView);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                url = "https://login.uber.com/oauth/authorize?response_type=code&redirect_uri=https%3A%2F%2Fuhoot.bigred.hack&scope=request&client_id=rdFS48Oi-Bq2pbxmDGpd59-65iqcz8BX";
                view.loadUrl(url);
                return false;
            }
        });

        Map<String, String> params = new HashMap<>();
        params.put("response_type", "code");
        params.put("client_id", "rdFS48Oi-Bq2pbxmDGpd59-65iqcz8BX");
        params.put("redirect_uri", "https://uhoot.bigred.hack");

        new HttpTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, url);
        */
    }

    private class HttpTask extends AsyncTask<URL, String, String> {

        /**
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */

        @Override
        protected String doInBackground(URL... params) {
            URL url = params[0];
            HttpURLConnection urlConnection = null;
            String loginPage = "";

            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Authorization", "Token " + UBER_SERVER_TOKEN);
                urlConnection.setRequestProperty("response_type", "code");
                urlConnection.setRequestProperty("client_id", UBER_CLIENT_ID);
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                System.out.println(urlConnection.getResponseCode());
                int data = in.read();
                StringBuilder sb = new StringBuilder();
                while (data != -1) {
                    char curr = (char) data;
                    sb.append(curr);
                    data = in.read();
                    //System.out.println("Current:: " + curr);
                }
                //System.out.println(sb.toString());
                loginPage = sb.toString();
                //publishProgress(loginPage);
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }

            return loginPage;
        }

    }

    /** Called when the user clicks the Proceed button */
    /*public void sendPickupLocation(View view) {
        // Do something in response to button
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        System.out.println("--------------- Pickup Location:: " + message);

        String url = "https://login.uber.com/oauth/authorize";


    }*/
}
