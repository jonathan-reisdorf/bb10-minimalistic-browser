package org.crossnode.bb10beol;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import org.xwalk.core.XWalkView;
import org.xwalk.core.XWalkCookieManager;
// import org.xwalk.core.XWalkPreferences;


public class Browser {
    private Activity activity;
    private LinearLayout mainLayout;

    private BrowserTabManager browserTabManager;
    private XWalkView navigationWebView;

    private String navigationFileUrl = "file:///android_asset/navigation.html";

    Browser(Activity activity, LinearLayout mainLayout) {
        this.activity = activity;
        this.mainLayout = mainLayout;
    }

    public void initialize(String url) {
        // XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);

        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setBackgroundColor(Color.BLACK);

        navigationWebView = new XWalkView(activity);
        browserTabManager = new BrowserTabManager(activity, mainLayout, navigationWebView);
        XWalkView xWalkWebView = browserTabManager.initialize(url);

        XWalkCookieManager mCookieManager = new XWalkCookieManager();
        mCookieManager.setAcceptCookie(true);
        mCookieManager.setAcceptFileSchemeCookies(true);

        navigationWebView.setResourceClient(new BrowserResourceClient(navigationWebView, navigationWebView));
        navigationWebView.addJavascriptInterface(new NavigationJsInterface(activity, browserTabManager), "navigation");
        navigationWebView.loadUrl(navigationFileUrl);

        int navigationHeight = 40;
        navigationHeight = (int) (navigationHeight * Resources.getSystem().getDisplayMetrics().density);
        navigationWebView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, navigationHeight, (float) 0));

        mainLayout.addView(xWalkWebView);
        mainLayout.addView(navigationWebView);
    }
}
