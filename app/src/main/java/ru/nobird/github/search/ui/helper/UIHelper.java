package ru.nobird.github.search.ui.helper;

import android.view.View;
import android.widget.TextView;

public class UIHelper {

    public static void setTextOrHideIfNull(final TextView textView, final String s) {
        if (s == null) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setText(s);
            textView.setVisibility(View.VISIBLE);
        }
    }

}
