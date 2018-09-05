package com.conext.conext.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.conext.conext.R;
import com.conext.conext.model.db.USER_EVENT;
import com.conext.conext.ui.EventDetailsActivity;
import com.conext.conext.utils.ItemClickListener;

import java.util.List;

import static com.conext.conext.utils.Constants.BaseIP;

/**
 * Created by Shriram on 6/26/2017.
 */

public class RecyclerAdapterProfileEvents extends RecyclerView.Adapter<RecyclerAdapterProfileEvents.MyViewHolder> {

    private List<USER_EVENT> profileEventsList;
    private Context context;
    private LayoutInflater inflater;

    public RecyclerAdapterProfileEvents(Context context, List<USER_EVENT> profileEventsList) {

        this.context = context;
        this.profileEventsList = profileEventsList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public RecyclerAdapterProfileEvents.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.card_events_attended, parent, false);
        return new RecyclerAdapterProfileEvents.MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        USER_EVENT profileEvents = profileEventsList.get(position);

        holder.profileEventName.setText(profileEvents.getEventTitle());
        holder.profileEventType.setText(profileEvents.getEventTypeKey() + " | " + profileEvents.getTagKey());
        holder.profileEventDateStart.setText(profileEvents.getEventStartDate());
        holder.profileEventMonthStart.setText(profileEvents.getEventMonthStartDate());

        Glide.with(context)
                .load(BaseIP + "/" + profileEvents.getImageKey())
                .into(holder.profileEventImage);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int posit) {
                Log.e("tag", "click" + profileEventsList.get(position).getEventKey());
                Intent intent = new Intent(context, EventDetailsActivity.class);
                intent.putExtra("id", profileEventsList.get(position).getEventKey());
                (context).startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return profileEventsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView profileEventName, profileEventType, profileEventDateStart, profileEventMonthStart;
        private ImageView profileEventImage;
        private ItemClickListener itemClickListener;


        MyViewHolder(View itemView) {
            super(itemView);

            profileEventName = (TextView) itemView.findViewById(R.id.event_name);
            profileEventType = (TextView) itemView.findViewById(R.id.event_type);
            profileEventDateStart = (TextView) itemView.findViewById(R.id.event_date);
            profileEventMonthStart = (TextView) itemView.findViewById(R.id.event_month);
            profileEventImage = (ImageView) itemView.findViewById(R.id.bac);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v, getLayoutPosition());
        }

        void setItemClickListener(ItemClickListener ic) {
            this.itemClickListener = ic;
        }
    }
}