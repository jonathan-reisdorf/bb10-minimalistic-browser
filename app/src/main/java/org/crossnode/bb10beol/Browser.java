package org.crossnode.bb10beol;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import org.mozilla.geckoview.GeckoRuntime;
import org.mozilla.geckoview.GeckoRuntimeSettings;
import org.mozilla.geckoview.GeckoView;


public class Browser {
    private Activity activity;
    private LinearLayout mainLayout;

    //private BrowserTabManager browserTabManager;
    private GeckoView navigationView;
    //private BrowserResourceClient navigationResourceClient;

    private String navigationFileUrl = "file:///android_asset/navigation.html";

    Browser(Activity activity, View mainLayout) {
        this.activity = activity;
        this.mainLayout = (LinearLayout) mainLayout;
    }

    private GeckoRuntime _initializeRuntime() {
        GeckoRuntimeSettings.Builder settingsBuilder = new GeckoRuntimeSettings.Builder();
        settingsBuilder.javaScriptEnabled(true);
        settingsBuilder.remoteDebuggingEnabled(false);
        settingsBuilder.webFontsEnabled(true);

        GeckoRuntimeSettings settings = settingsBuilder.build();
        return GeckoRuntime.create(activity.getApplicationContext(), settings);
    }

    public void initialize(String url) {
        GeckoRuntime runtime = _initializeRuntime();

        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setBackgroundColor(Color.BLACK);

        BrowserTabSession navigationSession = new BrowserTabSession(runtime);
        navigationView = new GeckoView(activity);
        navigationView.setSession(navigationSession);

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

        navigationSession.loadUri("http://html5test.com");

        int navigationHeight = 40;
        navigationHeight = (int) (navigationHeight * Resources.getSystem().getDisplayMetrics().density);
        //navigationView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, navigationHeight, (float) 0));

        //mainLayout.addView(xWalkWebView);

        mainLayout.addView(navigationView);
    }

    public void onResume() {
        //navigationResourceClient.triggerJavascriptHandler("onApplicationResume", null);
    }
}
