package org.crossnode.bb10beol;

import org.json.JSONObject;
import org.json.JSONException;

import org.xwalk.core.XWalkView;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkNavigationHistory;
import org.xwalk.core.XWalkNavigationItem;


class BrowserResourceClient extends XWalkResourceClient {
    private XWalkView navigationWebView;
    private XWalkNavigationHistory navigationHistory;

    public boolean isActive = false;
    public boolean isSystem = false;

    BrowserResourceClient(XWalkView view, XWalkView navigationWebView) {
        super(view);

        this.navigationWebView = navigationWebView;
        this.navigationHistory = view.getNavigationHistory();
    }

    @Override
    public void onLoadStarted(XWalkView view, String url) {
        if (!isActive) {
            return;
        }

        try {
            JSONObject obj = new JSONObject();
            addNavigationItemDetails(obj);
            obj.put("type", "loadstart");
            obj.put("url", url);

            onNavigationEvent(obj);
        } catch (JSONException ex) {}
    }

    @Override
    public void onLoadFinished(XWalkView view, String url) {
        if (!isActive) {
            return;
        }

        try {
            JSONObject obj = new JSONObject();
            addNavigationItemDetails(obj);
            obj.put("type", "loadstop");
            obj.put("url", url);

            onNavigationEvent(obj);
        } catch (JSONException ex) {}
    }

    @Override
    public void onProgressChanged(XWalkView view, int progressInPercent) {
        if (!isActive) {
            return;
        }

        try {
            JSONObject obj = new JSONObject();
            obj.put("type", "loadprogress");
            obj.put("progress", progressInPercent);

            onNavigationEvent(obj);
        } catch (JSONException ex) {}
    }

    public JSONObject getNavigationItemDetails(XWalkNavigationItem navigationItem) {
        JSONObject obj = new JSONObject();
        addNavigationItemDetails(navigationItem, obj);
        return obj;
    }

    private JSONObject addNavigationItemDetails(XWalkNavigationItem navigationItem, JSONObject obj) {
        if (navigationItem == null) {
            return obj;
        }

        try {
            obj.put("navigationUrl", navigationItem.getUrl());
            obj.put("navigationOriginalUrl", navigationItem.getOriginalUrl());
            obj.put("navigationTitle", navigationItem.getTitle());
        } catch (JSONException ex) {}

        return obj;
    }

    private JSONObject addNavigationItemDetails(JSONObject obj) {
        try {
            obj.put("navigationHasPrev", navigationHistory.canGoBack());
            obj.put("navigationHasNext", navigationHistory.canGoForward());
        } catch (JSONException ex) {}

        if (navigationHistory.size() < 1) {
            return obj;
        }

        XWalkNavigationItem navigationItem = navigationHistory.getCurrentItem();
        return addNavigationItemDetails(navigationItem, obj);
    }

    public void broadcastNavigationItemDetails() {
        try {
            JSONObject obj = new JSONObject();
            addNavigationItemDetails(obj);
            obj.put("type", "status");

            onNavigationEvent(obj);
        } catch (JSONException ex) {}
    }

    private void onNavigationEvent(JSONObject obj) {
        triggerJavascriptHandler("onNavigationEvent", obj);
    }

    public void triggerJavascriptHandler(String handlerName, JSONObject obj) {
        navigationWebView.evaluateJavascript("window." + handlerName + " && window." + handlerName + "(" + obj + ")", null);
    }

    public void goPrev() {
        navigationHistory.navigate(XWalkNavigationHistory.Direction.BACKWARD, 1);
    }

    public void goNext() {
        navigationHistory.navigate(XWalkNavigationHistory.Direction.FORWARD,1);
    }
}
