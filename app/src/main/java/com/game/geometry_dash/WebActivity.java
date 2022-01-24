package com.game.geometry_dash;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

//import com.halil.ozel.rolldicegame.R;

public class WebActivity extends AppCompatActivity implements WebViewView {
    private WebPresenter presenter;
    private SharedPreferences preferences;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web);
        hideSystemUI(this);

        webView =findViewById(R.id.webView);
        preferences = getSharedPreferences(WebKeys.APP_PREFERENCES, Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
        presenter = new WebPresenter(WebActivity.this, preferences, webView);

        boolean canSee = false;
        Bundle arguments = getIntent().getExtras();

        if (arguments != null) {
            canSee = arguments.getBoolean(WebKeys.CAN_SEE);
        }

//          if (checkIsTablet(getApplicationContext())) {
//            showGame();
//        } else {

            showWeb(canSee, webView);
//        }
    }
    public static void hideSystemUI(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        //| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    @Override
    public void onBackPressed()
    {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            showGame();
            super.onBackPressed();
        }
    }


    public static boolean checkIsTablet(Context context){
        TelephonyManager manager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        if(manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE){
            return true;
        }else{
            return false;
        }

    }

    @Override
    public boolean canShowWeb() {
        return presenter.seenBefore();
    }

    @Override
    public void showWeb(boolean canSee, WebView webView) {
        presenter.showWeb(canSee, webView);
    }





    @Override
    public void showGame() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}