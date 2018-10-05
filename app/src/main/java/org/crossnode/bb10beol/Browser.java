package org.crossnode.bb10beol;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import org.xwalk.core.XWalkView;
import org.xwalk.core.XWalkCookieManager;
// import org.xwalk.core.XWalkPreferences;


public class Browser {
    public boolean isInitialized = false;

    private MainActivity activity;
    private LinearLayout mainLayout;

    private BrowserTabManager tabManager;
    private XWalkView navigationWebView;
    private BrowserResourceClient navigationResourceClient;

    private String navigationFileUrl = "file:///android_asset/navigation.html";

    Browser(MainActivity activity, LinearLayout mainLayout) {
        this.activity = activity;
        this.mainLayout = mainLayout;
    }

    public void initialize(String url) {
        // XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);

        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setBackgroundColor(Color.BLACK);

        navigationWebView = new XWalkView(activity);
        tabManager = new BrowserTabManager(activity, mainLayout, navigationWebView);
        XWalkView xWalkWebView = tabManager.initialize(url);

        XWalkCookieManager mCookieManager = new XWalkCookieManager();
        mCookieManager.setAcceptCookie(true);
        mCookieManager.setAcceptFileSchemeCookies(true);

        navigationResourceClient = new BrowserResourceClient(navigationWebView, navigationWebView);
        navigationWebView.setResourceClient(navigationResourceClient);
        navigationWebView.addJavascriptInterface(new NavigationJsInterface(activity, tabManager), "navigation");
        navigationWebView.addJavascriptInterface(new NavigationConvenienceJsInterface(activity), "navigationConvenience");
        navigationWebView.loadUrl(navigationFileUrl);

        int navigationHeight = 40;
        navigationHeight = (int) (navigationHeight * Resources.getSystem().getDisplayMetrics().density);
        navigationWebView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, navigationHeight, (float) 0));

        mainLayout.addView(xWalkWebView);
        mainLayout.addView(navigationWebView);
        isInitialized = true;
    }

    public void onResume() {
        navigationResourceClient.triggerJavascriptHandler("onApplicationResume", null);
    }
}
