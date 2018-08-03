package org.crossnode.bb10beol;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import org.chromium.content.browser.ContentViewCore;
import org.chromium.content.browser.ContentView;
import org.chromium.chrome.browser.WebContentsFactory;
import org.chromium.content_public.browser.WebContents;

public class Browser {
    private Activity activity;
    private LinearLayout mainLayout;

    Browser(Activity activity, LinearLayout mainLayout) {
        this.activity = activity;
        this.mainLayout = mainLayout;
    }

    public void initialize(String url) {
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setBackgroundColor(Color.BLACK);

        WebContents webContents = WebContentsFactory.createWebContents(false, false);
        ContentViewCore contentViewCore = ContentViewCore.fromWebContents(webContents);
        ContentView contentView = ContentView.createContentView(activity, contentViewCore);
    }

    public void onResume() {
    }
}
