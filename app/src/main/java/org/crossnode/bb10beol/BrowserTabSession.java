package org.crossnode.bb10beol;

import org.mozilla.geckoview.GeckoSession;
import org.mozilla.geckoview.GeckoRuntime;


public class BrowserTabSession extends GeckoSession {
    BrowserTabSession(GeckoRuntime runtime) {
        open(runtime);
    }
}
