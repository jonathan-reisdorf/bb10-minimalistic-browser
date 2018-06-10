package org.crossnode.bb10beol;

import android.app.Activity;

import org.xwalk.core.XWalkView;
import org.xwalk.core.JavascriptInterface;


class NavigationJsInterface {
    private Activity activity;
    private BrowserTabManager browserTabManager;
    private String tabsOverviewFileUrl = "file:///android_asset/tabs.html";
    private String contextMenuFileUrl = "file:///android_asset/context.html";

    NavigationJsInterface(Activity activity, BrowserTabManager browserTabManager) {
        this.activity = activity;
        this.browserTabManager = browserTabManager;
    }

    @JavascriptInterface
    public void openUrl(final String url) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!browserTabManager.currentResourceClient.isSystem) {
                    // TODO load on previous/non-system/new tab in that case!
                    browserTabManager.load(url);
                }
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
    public void showContextMenu() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XWalkView webview = browserTabManager.addTab(contextMenuFileUrl, true);
                webview.addJavascriptInterface(new CurrentPageInterface(activity, browserTabManager.previousResourceClient), "currentPage");
            }
        });
    }

    @JavascriptInterface
    public void closeSystemTab() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                browserTabManager.closeSystemTab();
            }
        });
    }

    @JavascriptInterface
    public void goPrev() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                browserTabManager.currentResourceClient.goPrev();
            }
        });
    }

    @JavascriptInterface
    public void goNext() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                browserTabManager.currentResourceClient.goNext();
            }
        });
    }
}