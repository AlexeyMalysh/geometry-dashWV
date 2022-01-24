package com.game.geometry_dash;

import android.webkit.WebView;

public interface WebViewView {
    boolean canShowWeb();
    void showWeb(boolean canSee, WebView webView);
    void showGame();
}
