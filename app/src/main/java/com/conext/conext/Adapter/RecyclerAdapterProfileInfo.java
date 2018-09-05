package com.conext.conext.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.conext.conext.R;
import com.conext.conext.model.Skill;

import java.util.List;

/**
 * Created by Shriram on 6/26/2017.
 */

public class RecyclerAdapterProfileInfo extends RecyclerView.Adapter<RecyclerAdapterProfileInfo.MyViewHolder> {

    private List<String> profileInfoList;
    private List<Skill> profileInfList;
    private Context context;
    private LayoutInflater inflater;
    private int res;

    public RecyclerAdapterProfileInfo(Context context, List<String> profileInfoList, int res) {

        this.context = context;
        this.profileInfoList = profileInfoList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.res = res;

    }

    @Override
    public RecyclerAdapterProfileInfo.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(res, parent, false);
        return new RecyclerAdapterProfileInfo.MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.infoProfile.setText(profileInfoList.get(position));
    }


    @Override
    public int getItemCount() {
        if (profileInfoList == null)
            return 0;
        else
            return profileInfoList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView infoProfile;


        MyViewHolder(View itemView) {
            super(itemView);

            infoProfile = (TextView) itemView.findViewById(R.id.info_details);


        }
    }
}