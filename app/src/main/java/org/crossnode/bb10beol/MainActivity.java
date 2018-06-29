package org.crossnode.bb10beol;

import android.app.Activity;
import android.view.View;
import android.os.Bundle;

public class MainActivity extends Activity {
    private Browser browser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View mainLayout = this.findViewById(R.id.mainLayout);
        browser = new Browser(this, mainLayout);
        browser.initialize(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
