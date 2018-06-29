package org.crossnode.bb10beol;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import org.mozilla.geckoview.GeckoRuntime;
import org.mozilla.geckoview.GeckoRuntimeSettings;
import org.mozilla.geckoview.GeckoSession;
import org.mozilla.geckoview.GeckoView;


public class Browser {
    private Activity activity;
    private LinearLayout mainLayout;

    //private BrowserTabManager browserTabManager;
    private View navigationView;
    //private BrowserResourceClient navigationResourceClient;

    private String navigationFileUrl = "file:///android_asset/navigation.html";

    Browser(Activity activity, View mainLayout) {
        this.activity = activity;
        this.mainLayout = (LinearLayout) mainLayout;
    }

    private void _initializeRuntime() {
        GeckoRuntimeSettings.Builder settingsBuilder = new GeckoRuntimeSettings.Builder();
        settingsBuilder.javaScriptEnabled(true);
        settingsBuilder.remoteDebuggingEnabled(true);
        settingsBuilder.webFontsEnabled(true);

        GeckoRuntimeSettings settings = settingsBuilder.build();
        GeckoRuntime runtime = GeckoRuntime.create(activity.getApplicationContext());
    }

    public void initialize(String url) {
        _initializeRuntime();
        //GeckoRuntime runtime = new GeckoRuntime();
        //runtime.attachTo(activity);
        // XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);

        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setBackgroundColor(Color.BLACK);

        navigationView = new GeckoView(activity);
        //GeckoSession session = new GeckoSession();
        //session.open(runtime);
        //navigationView.setSession(session);
        //navigationView.setSession(new BrowserTabSession(runtime));
        // BrowserTabSession

        //browserTabManager = new BrowserTabManager(activity, mainLayout, navigationWebView);
        //XWalkView xWalkWebView = browserTabManager.initialize(url);

        //XWalkCookieManager mCookieManager = new XWalkCookieManager();
        //mCookieManager.setAcceptCookie(true);
        //mCookieManager.setAcceptFileSchemeCookies(true);

        //navigationResourceClient = new BrowserResourceClient(navigationWebView, navigationWebView);
        //navigationWebView.setResourceClient(navigationResourceClient);
        //navigationWebView.addJavascriptInterface(new NavigationJsInterface(activity, browserTabManager), "navigation");
        //navigationWebView.addJavascriptInterface(new NavigationConvenienceJsInterface(activity), "navigationConvenience");
        //navigationWebView.loadUrl(navigationFileUrl);

        int navigationHeight = 40;
        navigationHeight = (int) (navigationHeight * Resources.getSystem().getDisplayMetrics().density);
        navigationView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, navigationHeight, (float) 0));

        //mainLayout.addView(xWalkWebView);

        //mainLayout.addView(navigationView);
    }

    public void onResume() {
        //navigationResourceClient.triggerJavascriptHandler("onApplicationResume", null);
    }
}
