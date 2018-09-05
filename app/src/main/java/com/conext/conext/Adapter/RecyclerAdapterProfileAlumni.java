package com.conext.conext.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.conext.conext.R;
import com.conext.conext.model.ProfileAlumni;

import java.util.List;

/**
 * Created by Shriram on 6/26/2017.
 */

public class RecyclerAdapterProfileAlumni extends RecyclerView.Adapter<RecyclerAdapterProfileAlumni.MyViewHolder> {

    private List<ProfileAlumni> profileAlumniList;
    private Context context;
    private LayoutInflater inflater;
    int pos;

    public RecyclerAdapterProfileAlumni(Context context, List<ProfileAlumni> profileAlumniList) {

        this.context = context;
        this.profileAlumniList = profileAlumniList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }


    @Override
    public RecyclerAdapterProfileAlumni.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.card_profile_alumini, parent, false);
        return new RecyclerAdapterProfileAlumni.MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ProfileAlumni profileAlumni = profileAlumniList.get(position);

        holder.alumniUniversity.setText(profileAlumni.getAlumniUniversity());
        holder.alumniStudy.setText(profileAlumni.getAlumniStudy());
        holder.alumniYear.setText(profileAlumni.getAlumniYear());
    }


    @Override
    public int getItemCount() {
        return profileAlumniList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView alumniUniversity,alumniStudy,alumniYear;
        private ImageView alumniUnivImage,alumniStudyImage,alumniYearImage;





        MyViewHolder(View itemView) {
            super(itemView);


            alumniUniversity=(TextView) itemView.findViewById(R.id.alumini_profile_colg_title);
            alumniStudy=(TextView)itemView.findViewById(R.id.alumini_profile_deg_title);
            alumniYear=(TextView)itemView.findViewById(R.id.alumini_profile_year_title);

            alumniUnivImage=(ImageView) itemView.findViewById(R.id.alumini_profile_colg_logo);
            alumniStudyImage=(ImageView) itemView.findViewById(R.id.alumini_profile_deg_logo);
            alumniYearImage=(ImageView) itemView.findViewById(R.id.alumini_profile_year_logo);

        }
    }
}