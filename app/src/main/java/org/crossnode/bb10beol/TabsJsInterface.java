package org.crossnode.bb10beol;

import android.app.Activity;

import org.json.JSONArray;

import org.xwalk.core.JavascriptInterface;
import org.xwalk.core.XWalkView;


class TabsJsInterface {
    private Activity _activity;
    private XWalkView _webview;
    private BrowserTabManager _browserTabManager;

    TabsJsInterface(Activity activity, XWalkView webview, BrowserTabManager browserTabManager) {
        this._activity = activity;
        this._webview = webview;
        this._browserTabManager = browserTabManager;
    }

    @JavascriptInterface
    public void getTabs(final String callbackName) {
        _activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _sendTabsArray(callbackName);
            }
        });
    }

    @JavascriptInterface
    public void addTab(final String url, final String userAgent) {
        _activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _browserTabManager.closeSystemTab();
                _browserTabManager.addTab(url, userAgent, false, true);
            }
        });
    }

    @JavascriptInterface
    public void openTab(final int index) {
        _activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _browserTabManager.openTabByIndex(index, true);
            }
        });
    }

    @JavascriptInterface
    public void closeTab(final int index, final String callbackName) {
        _activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _browserTabManager.closeTabByIndex(index);
                _sendTabsArray(callbackName);
            }
        });
    }

    protected void _sendTabsArray(String callbackName) {
        JSONArray tabsArray = _browserTabManager.getTabsArray();
        _webview.evaluateJavascript(callbackName + "(" + tabsArray + ")", null);
    }
}