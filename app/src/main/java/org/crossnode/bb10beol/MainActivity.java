package org.crossnode.bb10beol;

import android.widget.LinearLayout;
import android.os.Bundle;
import android.content.Intent;

import org.xwalk.core.XWalkActivity;
import org.xwalk.core.XWalkFileChooser;

public class MainActivity extends XWalkActivity {
    public Browser browser = null;
    public XWalkFileChooser fileChooser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout mainLayout = (LinearLayout) this.findViewById(R.id.mainLayout);
        browser = new Browser(this, mainLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (browser.isInitialized) {
            browser.onResume();
        }
    }

    @Override
    protected void onXWalkReady() {
        browser.initialize(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (fileChooser != null) {
            fileChooser.onActivityResult(requestCode, resultCode, data);
        }
    }
}
