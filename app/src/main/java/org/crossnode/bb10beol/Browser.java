package org.crossnode.bb10beol;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.StrictMode;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import org.chromium.base.library_loader.LibraryLoader;
import org.chromium.base.library_loader.LibraryProcessType;
import org.chromium.base.library_loader.ProcessInitException;
import org.chromium.content.browser.ContentViewCore;
import org.chromium.content.browser.ContentView;
import org.chromium.chrome.browser.WarmupManager;
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

        // TODO wrong thread!
        StrictMode.ThreadPolicy oldPolicy = StrictMode.allowThreadDiskReads();
        try {
            // Normally Main.java will have already loaded the library asynchronously, we only need
            // to load it here if we arrived via another flow, e.g. bookmark access & sync setup.
            LibraryLoader.get(LibraryProcessType.PROCESS_BROWSER).ensureInitialized();
        } catch (ProcessInitException e) {} finally {
            StrictMode.setThreadPolicy(oldPolicy);
        }

        WebContents webContents = WarmupManager.getInstance().takeSpareWebContents(false, false);
        ContentViewCore contentViewCore = ContentViewCore.fromWebContents(webContents);
        ContentView contentView = ContentView.createContentView(activity, contentViewCore);
    }

    public void onResume() {
    }
}
