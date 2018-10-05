package org.crossnode.bb10beol;


import java.util.ArrayList;

import org.json.JSONArray;

import org.xwalk.core.XWalkNavigationItem;
import org.xwalk.core.XWalkView;

import android.graphics.Color;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;


public class BrowserTabManager {
    private ArrayList<XWalkView> tabs = new ArrayList<>();
    private ArrayList<BrowserResourceClient> resourceClients = new ArrayList<>();

    public XWalkView currentTab = null;
    public XWalkView previousTab = null;
    public BrowserResourceClient currentResourceClient = null;
    public BrowserResourceClient previousResourceClient = null;

    private MainActivity activity;
    private LinearLayout mainLayout;
    private XWalkView navigationWebView;


    BrowserTabManager(MainActivity activity, LinearLayout mainLayout, XWalkView navigationWebView) {
        this.activity = activity;
        this.mainLayout = mainLayout;
        this.navigationWebView = navigationWebView;
    }

    public XWalkView initialize(String url) {
        return addTab(url, null, false, false);
    }

    public XWalkView addTab(String url) {
        return addTab(url, null, false, true);
    }

    public XWalkView addTab(String url, boolean systemTab) {
        return addTab(url, null, systemTab, true);
    }

    public XWalkView addTab(String url, String customUserAgentString, boolean systemTab, boolean openTab) {
        XWalkView xWalkWebView = new XWalkView(activity);

        BrowserResourceClient browserResourceClient = new BrowserResourceClient(xWalkWebView, navigationWebView);

        xWalkWebView.setResourceClient(browserResourceClient);
        xWalkWebView.setUIClient(new BrowserUIClient(activity, xWalkWebView));
        xWalkWebView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, (float) 1));

        if (customUserAgentString != null && !customUserAgentString.equals("")) {
            xWalkWebView.setUserAgentString(customUserAgentString);
        }

        if (url != null && !url.equals("")) {
            xWalkWebView.loadUrl(url);
        }

        if (systemTab) {
            xWalkWebView.setBackgroundColor(Color.BLACK);
            browserResourceClient.isSystem = true;
            browserResourceClient.triggerJavascriptHandler("onSystemTabOpen", null);
        } else {
            tabs.add(xWalkWebView);
            resourceClients.add(browserResourceClient);
        }

        if (currentTab == null) {
            currentTab = xWalkWebView;
            currentResourceClient = browserResourceClient;
            browserResourceClient.isActive = true;
        }

        if (openTab) {
            openTab(xWalkWebView, browserResourceClient);
        }

        return xWalkWebView;
    }

    public void closeSystemTab() {
        closeSystemTab(null, null);
    }

    public void closeSystemTab(XWalkView newTab, BrowserResourceClient newResourceClient) {
        if (newTab == null || newResourceClient == null) {
            newTab = previousTab;
            newResourceClient = previousResourceClient;
        }

        if (newTab == null || newResourceClient == null || !currentResourceClient.isSystem) {
            return;
        }

        openTab(newTab, newResourceClient);

        previousTab.onDestroy();
        previousTab = null;
        previousResourceClient = null;

        newResourceClient.triggerJavascriptHandler("onSystemTabClose", null);
    }

    public void closeTabByIndex(final int index) {
        XWalkView tab = tabs.get(index);
        BrowserResourceClient resourceClient = resourceClients.get(index);

        tabs.remove(tab);
        resourceClients.remove(resourceClient);
        tab.onDestroy();


        boolean isEmptyList = tabs.size() < 1;
        if (isEmptyList) {
            addTab("about:blank", null, false, false);
        }

        if (isEmptyList || previousTab == null || previousResourceClient == null || previousResourceClient == resourceClient) {
            int lastTabIndex = tabs.size() - 1;
            previousTab = tabs.get(lastTabIndex);
            previousResourceClient = resourceClients.get(lastTabIndex);
        }

        if (isEmptyList) {
            closeSystemTab();
        }
    }

    private void openTab(final XWalkView newTab, final BrowserResourceClient newResourceClient) {
        previousTab = currentTab;
        previousResourceClient = currentResourceClient;

        currentTab = newTab;
        currentResourceClient = newResourceClient;

        if (previousTab != null && previousResourceClient != null) {
            previousTab.stopLoading();
            previousResourceClient.isActive = false;
        }

        currentResourceClient.isActive = true;
        currentResourceClient.broadcastNavigationItemDetails();

        mainLayout.removeViewAt(0);
        mainLayout.addView(currentTab, 0);
        mainLayout.invalidate();
    }

    public void openTabByIndex(final int index, final boolean closeSystemTab) {
        XWalkView newTab = tabs.get(index);
        BrowserResourceClient newResourceClient = resourceClients.get(index);

        if (closeSystemTab) {
            closeSystemTab(newTab, newResourceClient);
        } else {
            openTab(newTab, newResourceClient);
        }
    }

    public void load(String url) {
        currentTab.loadUrl(url);
    }

    public JSONArray getTabsArray() {
        JSONArray items = new JSONArray();
        XWalkNavigationItem navigationItem;

        for (int index = 0; index < tabs.size(); index++) {
            navigationItem = tabs.get(index).getNavigationHistory().getCurrentItem();
            items.put(resourceClients.get(index).getNavigationItemDetails(navigationItem));
        }

        return items;
    }

    /*public JSONArray getNavigationHistoryArray(int tabIndex) {
        XWalkNavigationHistory navigationHistory = tabs.get(tabIndex).getNavigationHistory();

        JSONArray history = new JSONArray();
        XWalkNavigationItem navigationItem;

        for (int index = 0; index < navigationHistory.size(); index++) {
            navigationItem = navigationHistory.getItemAt(index);
            history.put(resourceClients.get(tabIndex).getNavigationItemDetails(navigationItem));
        }

        return history;
    }*/
}
