package org.crossnode.bb10beol;

import android.app.Activity;

import org.xwalk.core.XWalkView;
import org.xwalk.core.JavascriptInterface;


class NavigationJsInterface {
    private Activity activity;
    private BrowserTabManager browserTabManager;
    private String tabsOverviewFileUrl = "file:///android_asset/tabs.html";

    NavigationJsInterface(Activity activity, BrowserTabManager browserTabManager) {
        this.activity = activity;
        this.browserTabManager = browserTabManager;
    }

    @JavascriptInterface
    public void openUrl(final String url) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                browserTabManager.load(url);
            }
        });
    }

    @JavascriptInterface
    public void showTabsOverview() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XWalkView webview = browserTabManager.addTab(tabsOverviewFileUrl, true);
                webview.addJavascriptInterface(new TabsJsInterface(activity, webview, browserTabManager), "tabs");
            }
        });
    }

    @JavascriptInterface
    public void closeTabsOverview() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                browserTabManager.closeSystemTab();
            }
        });
    }
}