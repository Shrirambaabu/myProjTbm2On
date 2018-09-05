package com.conext.conext.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.conext.conext.R;
import com.conext.conext.utils.ItemClickListener;

import java.util.ArrayList;

/**
 * Created by Ashith VL on 10/11/2017.
 */

public class DrawerListAdapter extends RecyclerView.Adapter<DrawerListAdapter.NaviagationHolder> {

    private Context mContext;
    private int[] imageId;
    private ArrayList<String> titles;
    private ItemClickListener mItemClickListener;


    public DrawerListAdapter(Context mContext, ArrayList<String> titles, int[] imageId) {
        this.titles = titles;
        this.mContext = mContext;
        this.imageId = imageId;
    }

    @Override
    public NaviagationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NaviagationHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_drawer_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final NaviagationHolder holder, final int position) {

        Log.e("tag", imageId[position] + titles.get(position));

        holder.updateUI(imageId[position], titles.get(position), mContext);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(holder.itemView, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return titles.size();
    }


    public void setOnItemClickListener(final ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public class NaviagationHolder extends RecyclerView.ViewHolder {

        private TextView tv_title;
        private ImageView im_icon;

        public NaviagationHolder(View itemView) {
            super(itemView);

            final View view = itemView;

            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            im_icon = (ImageView) itemView.findViewById(R.id.im_icon);

        }

        public void updateUI(int imageUrl, String titles, Context context) {

            tv_title.setText(titles);

            Glide.with(context)
                    .load(imageUrl)
                    .into(im_icon);
        }

    }


}
