package org.crossnode.bb10beol;

import org.json.JSONException;
import org.json.JSONObject;
import org.xwalk.core.JavascriptInterface;


public class CurrentPageInterface {
    private String _title;
    private String _url;

    CurrentPageInterface(JSONObject obj) {
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
}
