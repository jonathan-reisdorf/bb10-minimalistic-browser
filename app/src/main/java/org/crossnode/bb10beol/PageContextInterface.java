package org.crossnode.bb10beol;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;

import org.xwalk.core.JavascriptInterface;


public class PageContextInterface {
    private Activity _activity;
    private BrowserTabManager _browserTabManager;
    private BrowserResourceClient _resourceClient;

    private String _title;
    private String _url;

    PageContextInterface(Activity activity, BrowserTabManager browserTabManager) {
        _activity = activity;
        _browserTabManager = browserTabManager;
        _resourceClient = browserTabManager.previousResourceClient;

        JSONObject obj = _resourceClient.getNavigationItemDetails();

        if (obj == null) {
            return;
        }

        try {
            String navigationUrl = obj.getString("navigationUrl");
            _url = navigationUrl != null ? navigationUrl : obj.getString("navigationOriginalUrl");
            _title = obj.getString("navigationTitle");
        } catch (JSONException ex) {}
    }

    @JavascriptInterface
    public String title() {
        return _title;
    }

    @JavascriptInterface
    public String url() {
        return _url;
    }

    @JavascriptInterface
    public void reload(final boolean forceReload) {
        _activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _browserTabManager.closeSystemTab();
                _resourceClient.reload(forceReload);
            }
        });
    }

    @JavascriptInterface
    public void load(final String url) {
        _activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _browserTabManager.closeSystemTab();
                _resourceClient.load(url);
            }
        });
    }
}
