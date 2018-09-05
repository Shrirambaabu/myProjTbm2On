package com.conext.conext.ui.custom;

import android.graphics.Color;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.conext.conext.R;

/**
 * Created by Ashith VL on 8/1/2017.
 */

public class NavigationViewItemDecorator extends RecyclerView.ItemDecoration {

    public NavigationViewItemDecorator() {
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int value = parent.getChildLayoutPosition(view);
        LinearLayout linearLayoutUserProfile = (LinearLayout) view.findViewById(R.id.linearlayout);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        ImageView im_icon = (ImageView) view.findViewById(R.id.im_icon);

        if (value == 2)
            outRect.left = 28;
        else if (value == 5) {
            tv_title.setTextColor(Color.parseColor("#00B3FF"));
            tv_title.setTextSize(11f);
            im_icon.setVisibility(View.GONE);
            linearLayoutUserProfile.setEnabled(false);
            linearLayoutUserProfile.setClickable(false);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tv_title.getLayoutParams();
            params.setMargins(0, 4, 0, 0); //substitute parameters for left, top, right, bottom
            tv_title.setLayoutParams(params);
        } else if (value == 6 || value == 8) {
            LinearLayout.LayoutParams t_params = (LinearLayout.LayoutParams) tv_title.getLayoutParams();
            t_params.setMargins(28, 0, 0, 0); //substitute parameters for left, top, right, bottom
            LinearLayout.LayoutParams i_params = (LinearLayout.LayoutParams) im_icon.getLayoutParams();
            i_params.setMargins(28, 0, 0, 0); //substitute parameters for left, top, right, bottom
            linearLayoutUserProfile.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.cutm_recyler_item));
            linearLayoutUserProfile.setEnabled(false);
            linearLayoutUserProfile.setClickable(false);
        } else if (value == 7) {
            LinearLayout.LayoutParams t_params = (LinearLayout.LayoutParams) tv_title.getLayoutParams();
            t_params.setMargins(28, 0, 0, 0); //substitute parameters for left, top, right, bottom
            LinearLayout.LayoutParams i_params = (LinearLayout.LayoutParams) im_icon.getLayoutParams();
            i_params.setMargins(28, 0, 0, 0); //substitute parameters for left, top, right, bottom
            linearLayoutUserProfile.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.cutm_recyler_item2));
        } else if (value == 9 || value == 10 || value == 11) {
            im_icon.setVisibility(View.GONE);
            view.setBackgroundColor(Color.WHITE);
        }
    }
}