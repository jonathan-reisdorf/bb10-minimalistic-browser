package org.crossnode.bb10beol;

import android.app.Activity;
import android.content.Context;
import android.content.ClipboardManager;
import android.content.ClipData;

import org.xwalk.core.JavascriptInterface;


public class NavigationConvenienceJsInterface {
    private boolean _isEnabled = false;
    private Activity _activity;
    private String _clipboardText = null;

    NavigationConvenienceJsInterface(Activity activity) {
        _activity = activity;
    }

    @JavascriptInterface
    public void setEnabled(boolean isEnabled) {
        _isEnabled = isEnabled;

        if (!isEnabled) {
            _clipboardText = null;
        }
    }

    @JavascriptInterface
    public String getClipboardText() {
        if (!_isEnabled) {
            return null;
        }

        _updateClipboardText();
        return _clipboardText;
    }

    private void _updateClipboardText() {
        if (!_isEnabled) {
            return;
        }

        ClipboardManager clipboardManager = (ClipboardManager)_activity.getSystemService(Context.CLIPBOARD_SERVICE);
        if (!clipboardManager.hasPrimaryClip()) {
            return;
        }

        ClipData clip = clipboardManager.getPrimaryClip();
        if (clip == null || clip.getItemCount() == 0) {
            return;
        }

        ClipData.Item clipItem = clip.getItemAt(0);

        if (clipItem.getText() == null) {
            return;
        }

        _clipboardText = clipItem.getText().toString();
    }
}
