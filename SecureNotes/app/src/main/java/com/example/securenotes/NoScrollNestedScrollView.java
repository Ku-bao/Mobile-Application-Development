package com.example.securenotes;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.widget.NestedScrollView;

public class NoScrollNestedScrollView extends NestedScrollView {
    private boolean disableScrolling = false;
    public NoScrollNestedScrollView(Context context) {
        super(context);
    }

    public NoScrollNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
//    @Override
//    public void requestChildFocus(View child, View focused) {
//    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0;
    }
}
