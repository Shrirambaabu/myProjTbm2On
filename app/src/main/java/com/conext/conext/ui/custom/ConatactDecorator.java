package com.conext.conext.ui.custom;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Ashith VL on 10/12/2017.
 */

public class ConatactDecorator extends RecyclerView.ItemDecoration {
    private int left;

    public ConatactDecorator(int left) {
        this.left = left;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = -left;
    }
}
