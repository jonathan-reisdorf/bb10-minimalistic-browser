package org.crossnode.bb10beol;

import org.xwalk.core.XWalkView;
import org.xwalk.core.JavascriptInterface;


class NavigationJsInterface {
    private MainActivity _activity;
    private BrowserTabManager _browserTabManager;
    private String tabsOverviewFileUrl = "file:///android_asset/tabs.html";
    private String contextMenuFileUrl = "file:///android_asset/context.html";

    NavigationJsInterface(MainActivity activity, BrowserTabManager browserTabManager) {
        _activity = activity;
        _browserTabManager = browserTabManager;
    }

    @JavascriptInterface
    public void openUrl(final String url) {
        _activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!_browserTabManager.currentResourceClient.isSystem) {
                    // TODO load on previous/non-system/new tab in that case!
                    _browserTabManager.load(url);
                }
            }
        });
    }

    @JavascriptInterface
    public void setUserAgentString(final String userAgentString) {
        _activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _browserTabManager.currentTab.setUserAgentString(userAgentString);
            }
        });
    }

    @JavascriptInterface
    public void showTabsOverview() {
        _activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XWalkView webview = _browserTabManager.addTab(tabsOverviewFileUrl, true);
                webview.addJavascriptInterface(new TabsJsInterface(_activity, webview, _browserTabManager), "tabs");
            }
        });
    }

    @JavascriptInterface
    public void showContextMenu() {
        _activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XWalkView webview = _browserTabManager.addTab(contextMenuFileUrl, true);
                webview.addJavascriptInterface(new PageContextInterface(_activity, _browserTabManager), "pageContext");
            }
        });
    }

    @JavascriptInterface
    public void closeSystemTab() {
        _activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _browserTabManager.closeSystemTab();
            }
        });
    }

    @JavascriptInterface
    public void goPrev() {
        _activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _browserTabManager.currentResourceClient.goPrev();
            }
        });
    }

    @JavascriptInterface
    public void goNext() {
        _activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _browserTabManager.currentResourceClient.goNext();
            }
        });
    }
}