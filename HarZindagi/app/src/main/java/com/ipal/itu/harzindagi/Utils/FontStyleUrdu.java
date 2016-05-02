package com.ipal.itu.harzindagi.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Ahmed on 11/21/2015.
 */
public class FontStyleUrdu extends TextView {

    public FontStyleUrdu(Context context) {
        super(context);
        Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/futuraL.ttf");
        setTypeface(myTypeface);
    }

    public FontStyleUrdu(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/futuraL.ttf");
        setTypeface(myTypeface);
    }
}
