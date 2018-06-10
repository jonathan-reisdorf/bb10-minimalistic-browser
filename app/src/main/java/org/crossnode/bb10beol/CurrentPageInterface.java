package org.crossnode.bb10beol;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;

import org.xwalk.core.JavascriptInterface;


public class CurrentPageInterface {
    private Activity _activity;
    private BrowserResourceClient _resourceClient;

    private String _title;
    private String _url;

    CurrentPageInterface(Activity activity, BrowserResourceClient resourceClient) {
        _activity = activity;
        _resourceClient = resourceClient;

        JSONObject obj = resourceClient.getNavigationItemDetails();

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
                _resourceClient.reload(forceReload);
            }
        });
    }
}
