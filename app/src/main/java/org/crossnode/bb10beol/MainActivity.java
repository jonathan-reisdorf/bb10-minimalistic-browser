package org.crossnode.bb10beol;

//import android.view.WindowManager;

import android.app.Activity;
import android.widget.LinearLayout;
import android.os.Bundle;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout mainLayout = this.findViewById(R.id.mainLayout);
        Browser browser = new Browser(this, mainLayout);
        browser.initialize("https://google.de");
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
