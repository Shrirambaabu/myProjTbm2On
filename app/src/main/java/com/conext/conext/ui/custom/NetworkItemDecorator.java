package com.conext.conext.ui.custom;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Ashith VL on 8/1/2017.
 */

public class NetworkItemDecorator extends RecyclerView.ItemDecoration {

    public NetworkItemDecorator() {
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

//        int value = parent.getChildLayoutPosition(view);
//        int positionValue = value % 7;
//        if (value > 3) {
//            if (positionValue == 4) {
//                outRect.top = -48;
//                outRect.right = -100;
//                outRect.left = 90;
//            } else if (positionValue == 5) {
//                outRect.top = -48;
//                outRect.left = 24;
//            } else if (positionValue == 6) {
//                outRect.top = -48;
//                outRect.left = -32;
//            } else {
//                outRect.top = -48;
//            }
//        }

        int value = parent.getChildLayoutPosition(view);
        int positionValue = value % 7;
        outRect.right = -10;
        outRect.left = 10;
        if (value < 3) {
            if (positionValue == 0) {
                  outRect.right = -20;
            } else if (positionValue == 1) {
                  outRect.left = 10;
            } else if (positionValue == 2) {
            }
        }

    }
}