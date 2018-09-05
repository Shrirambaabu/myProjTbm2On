package com.conext.conext.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.conext.conext.R;
import com.conext.conext.model.Network;
import com.conext.conext.ui.CreateEventActivity;
import com.conext.conext.ui.OtherProfileActivity;
import com.conext.conext.ui.custom.HexagonMaskView;
import com.conext.conext.utils.ItemClickListener;

import java.util.ArrayList;

import static com.conext.conext.utils.Constants.BaseIP;

/**
 * Created by Ashith VL on 7/7/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private ArrayList<Network> mValues;
    private Context mContext;

    public RecyclerViewAdapter(Context context, ArrayList<Network> values) {
        mValues = values;
        mContext = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(mContext).inflate(R.layout.card_network, parent, false));
    }


    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {

        final Network network = mValues.get(position);

        if (network.getDrawable() != null) {
            Glide.with(mContext)
                    .load(BaseIP + "/" + network.getDrawable())
                    .thumbnail(0.5f)
                    .into(holder.imageViewbac);
        }

        if (mValues.get(holder.getAdapterPosition()).getAlumini() == 1)
            holder.imageViewbac.setBorderColor(ContextCompat.getColor(mContext, R.color.border_network));
        else
            holder.imageViewbac.setBorderColor(ContextCompat.getColor(mContext, R.color.black));

        if (mValues.get(holder.getAdapterPosition()).getMentee() == 1)
            holder.mentee.setVisibility(View.VISIBLE);
        if (mValues.get(holder.getAdapterPosition()).getMentor() == 1)
            holder.mentor.setVisibility(View.VISIBLE);

        final boolean[] flag = {true};

        holder.imageViewbac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flag[0]) {
                    if (network.getDrawable() != null) {
                        Glide.with(mContext)
                                .load(BaseIP + "/" + network.getDrawable())
                                .thumbnail(0.5f)
                                .into(holder.imageViewbac);
                    }

                    if (network.getMentee() == 1)
                        holder.mentee.setVisibility(View.VISIBLE);
                    if (network.getMentor() == 1)
                        holder.mentor.setVisibility(View.VISIBLE);
                    holder.create.setVisibility(View.GONE);
                    holder.mentorshipImage.setVisibility(View.GONE);
                    holder.mentorShip.setVisibility(View.GONE);
                    holder.event.setVisibility(View.GONE);
                    holder.eventImage.setVisibility(View.GONE);
                    flag[0] = true;
                } else {

                    if (network.getDrawable() != null) {
                        Glide.with(mContext)
                                .load(BaseIP + "/" + network.getDrawable())
                                .thumbnail(0.5f)
                                .into(holder.imageViewbac);
                    }

                    if (network.getMentee() == 1)
                        holder.mentee.setVisibility(View.VISIBLE);
                    if (network.getMentor() == 1)
                        holder.mentor.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(mContext, OtherProfileActivity.class);
                    intent.putExtra("id", network.getId());
                    mContext.startActivity(intent);
                }
            }
        });

        holder.imageViewbac.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (flag[0]) {
                    holder.imageViewbac.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.blue_hex));
                    holder.mentee.setVisibility(View.GONE);
                    holder.mentor.setVisibility(View.GONE);
                    holder.create.setVisibility(View.VISIBLE);
                    holder.mentorshipImage.setVisibility(View.VISIBLE);
                    holder.mentorShip.setVisibility(View.VISIBLE);
                    holder.event.setVisibility(View.VISIBLE);
                    holder.eventImage.setVisibility(View.VISIBLE);
                    flag[0] = false;
                } else {

                    if (network.getDrawable() != null) {
                        Glide.with(mContext)
                                .load(BaseIP + "/" + network.getDrawable())
                                .thumbnail(0.5f)
                                .into(holder.imageViewbac);
                    }

                    if (network.getMentee() == 1)
                        holder.mentee.setVisibility(View.VISIBLE);
                    if (network.getMentor() == 1)
                        holder.mentor.setVisibility(View.VISIBLE);

                    holder.create.setVisibility(View.GONE);
                    holder.mentorshipImage.setVisibility(View.GONE);
                    holder.mentorShip.setVisibility(View.GONE);
                    holder.event.setVisibility(View.GONE);
                    holder.eventImage.setVisibility(View.GONE);
                    flag[0] = true;
                }
                return true;
            }
        });

        holder.eventImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (flag[0]) {
                    holder.imageViewbac.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.blue_hex));
                    holder.mentee.setVisibility(View.GONE);
                    holder.mentor.setVisibility(View.GONE);
                    holder.create.setVisibility(View.VISIBLE);
                    holder.mentorshipImage.setVisibility(View.VISIBLE);
                    holder.mentorShip.setVisibility(View.VISIBLE);
                    holder.event.setVisibility(View.VISIBLE);
                    holder.eventImage.setVisibility(View.VISIBLE);

                    flag[0] = false;
                } else {

                    if (network.getDrawable() != null) {
                        Glide.with(mContext)
                                .load(BaseIP + "/" + network.getDrawable())
                                .thumbnail(0.5f)
                                .into(holder.imageViewbac);
                    }

                    if (network.getMentee() == 1)
                        holder.mentee.setVisibility(View.VISIBLE);
                    if (network.getMentor() == 1)
                        holder.mentor.setVisibility(View.VISIBLE);
                    holder.create.setVisibility(View.GONE);
                    holder.mentorshipImage.setVisibility(View.GONE);
                    holder.mentorShip.setVisibility(View.GONE);
                    holder.event.setVisibility(View.GONE);
                    holder.eventImage.setVisibility(View.GONE);
                    flag[0] = true;
                }
                return true;
            }
        });

        holder.eventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, CreateEventActivity.class);
                intent.putExtra("id", network.getId());
                Log.e("tag", "Integer" + network.getId());
                mContext.startActivity(intent);

              //  v.getContext().startActivity(new Intent(v.getContext(), CreateEventActivity.class));

                if (network.getMentee() == 1)
                    holder.mentee.setVisibility(View.GONE);
                if (network.getMentor() == 1)
                    holder.mentor.setVisibility(View.GONE);

                holder.event.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                holder.eventImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.lunch));

                final Handler ha = new Handler();
                ha.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        //call function
                        holder.event.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                        holder.eventImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.icon_meeting));
                        ha.postDelayed(this, 1000);
                    }
                }, 1000);


            }
        });

        holder.event.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (flag[0]) {
                    holder.imageViewbac.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.blue_hex));
                    holder.mentee.setVisibility(View.GONE);
                    holder.mentor.setVisibility(View.GONE);
                    holder.create.setVisibility(View.VISIBLE);
                    holder.mentorshipImage.setVisibility(View.VISIBLE);
                    holder.mentorShip.setVisibility(View.VISIBLE);
                    holder.event.setVisibility(View.VISIBLE);
                    holder.eventImage.setVisibility(View.VISIBLE);
                    flag[0] = false;
                } else {

                    if (network.getDrawable() != null) {
                        Glide.with(mContext)
                                .load(BaseIP + "/" + network.getDrawable())
                                .thumbnail(0.5f)
                                .into(holder.imageViewbac);
                    }


                    if (network.getMentee() == 1)
                        holder.mentee.setVisibility(View.VISIBLE);
                    if (network.getMentor() == 1)
                        holder.mentor.setVisibility(View.VISIBLE);
                    holder.create.setVisibility(View.GONE);
                    holder.mentorshipImage.setVisibility(View.GONE);
                    holder.mentorShip.setVisibility(View.GONE);
                    holder.event.setVisibility(View.GONE);
                    holder.eventImage.setVisibility(View.GONE);
                    flag[0] = true;
                }
                return true;
            }
        });
        holder.event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, CreateEventActivity.class);
                intent.putExtra("id", network.getId());
                mContext.startActivity(intent);

                //v.getContext().startActivity(new Intent(v.getContext(), CreateEventActivity.class));
                if (network.getMentee() == 1)
                    holder.mentee.setVisibility(View.GONE);
                if (network.getMentor() == 1)
                    holder.mentor.setVisibility(View.GONE);

                holder.event.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                holder.eventImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.lunch));


                final Handler ha = new Handler();
                ha.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        //call function
                        holder.event.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                        holder.eventImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.icon_meeting));
                        ha.postDelayed(this, 1000);
                    }
                }, 1000);


            }
        });

        holder.create.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (flag[0]) {
                    holder.imageViewbac.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.blue_hex));
                    holder.mentee.setVisibility(View.GONE);
                    holder.mentor.setVisibility(View.GONE);
                    holder.create.setVisibility(View.VISIBLE);
                    holder.mentorshipImage.setVisibility(View.VISIBLE);
                    holder.mentorShip.setVisibility(View.VISIBLE);
                    holder.event.setVisibility(View.VISIBLE);
                    holder.eventImage.setVisibility(View.VISIBLE);
                    flag[0] = false;
                } else {

                    if (network.getDrawable() != null) {
                        Glide.with(mContext)
                                .load(BaseIP + "/" + network.getDrawable())
                                .thumbnail(0.5f)
                                .into(holder.imageViewbac);
                    }

                    if (network.getMentee() == 1)
                        holder.mentee.setVisibility(View.VISIBLE);
                    if (network.getMentor() == 1)
                        holder.mentor.setVisibility(View.VISIBLE);
                    holder.create.setVisibility(View.GONE);
                    holder.mentorshipImage.setVisibility(View.GONE);
                    holder.mentorShip.setVisibility(View.GONE);
                    holder.event.setVisibility(View.GONE);
                    holder.eventImage.setVisibility(View.GONE);
                    flag[0] = true;
                }
                return true;
            }
        });

        holder.mentorshipImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (flag[0]) {
                    holder.imageViewbac.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.blue_hex));
                    holder.mentee.setVisibility(View.GONE);
                    holder.mentor.setVisibility(View.GONE);
                    holder.create.setVisibility(View.VISIBLE);
                    holder.mentorshipImage.setVisibility(View.VISIBLE);
                    holder.mentorShip.setVisibility(View.VISIBLE);
                    holder.event.setVisibility(View.VISIBLE);
                    holder.eventImage.setVisibility(View.VISIBLE);
                    flag[0] = false;
                } else {

                    if (network.getDrawable() != null) {
                        Glide.with(mContext)
                                .load(BaseIP + "/" + network.getDrawable())
                                .thumbnail(0.5f)
                                .into(holder.imageViewbac);
                    }

                    if (network.getMentee() == 1)
                        holder.mentee.setVisibility(View.VISIBLE);
                    if (network.getMentor() == 1)
                        holder.mentor.setVisibility(View.VISIBLE);
                    holder.create.setVisibility(View.GONE);
                    holder.mentorshipImage.setVisibility(View.GONE);
                    holder.mentorShip.setVisibility(View.GONE);
                    holder.event.setVisibility(View.GONE);
                    holder.eventImage.setVisibility(View.GONE);
                    flag[0] = true;
                }
                return true;
            }
        });

        holder.mentorshipImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, CreateEventActivity.class);
                intent.putExtra("id", network.getId());
                mContext.startActivity(intent);

               // v.getContext().startActivity(new Intent(v.getContext(), CreateEventActivity.class));

                if (network.getMentee() == 1)
                    holder.mentee.setVisibility(View.GONE);
                if (network.getMentor() == 1)
                    holder.mentor.setVisibility(View.GONE);

                holder.mentorshipImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.mentorship));
                holder.mentorShip.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                holder.imageViewbac.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.blue_hex));

                final Handler ha = new Handler();
                ha.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        //call function
                        holder.mentorshipImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.icon_tie));
                        holder.mentorShip.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                        ha.postDelayed(this, 1000);
                    }
                }, 1000);


            }
        });

        holder.mentorShip.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (flag[0]) {
                    holder.imageViewbac.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.blue_hex));
                    holder.mentee.setVisibility(View.GONE);
                    holder.mentor.setVisibility(View.GONE);
                    holder.create.setVisibility(View.VISIBLE);
                    holder.mentorshipImage.setVisibility(View.VISIBLE);
                    holder.mentorShip.setVisibility(View.VISIBLE);
                    holder.event.setVisibility(View.VISIBLE);
                    holder.eventImage.setVisibility(View.VISIBLE);
                    flag[0] = false;
                } else {

                    if (network.getDrawable() != null) {
                        Glide.with(mContext)
                                .load(BaseIP + "/" + network.getDrawable())
                                .thumbnail(0.5f)
                                .into(holder.imageViewbac);
                    }

                    if (network.getMentee() == 1)
                        holder.mentee.setVisibility(View.VISIBLE);
                    if (network.getMentor() == 1)
                        holder.mentor.setVisibility(View.VISIBLE);
                    holder.create.setVisibility(View.GONE);
                    holder.mentorshipImage.setVisibility(View.GONE);
                    holder.mentorShip.setVisibility(View.GONE);
                    holder.event.setVisibility(View.GONE);
                    holder.eventImage.setVisibility(View.GONE);
                }
                return true;
            }
        });

        holder.mentorShip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, CreateEventActivity.class);
                intent.putExtra("id", network.getId());
                mContext.startActivity(intent);

                //v.getContext().startActivity(new Intent(v.getContext(), CreateEventActivity.class));

                if (network.getMentee() == 1)
                    holder.mentee.setVisibility(View.GONE);
                if (network.getMentor() == 1)
                    holder.mentor.setVisibility(View.GONE);

                holder.mentorshipImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.mentorship));
                holder.mentorShip.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                holder.imageViewbac.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.blue_hex));

                final Handler ha = new Handler();
                ha.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        //call function
                        holder.mentorshipImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.icon_tie));
                        holder.mentorShip.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                        ha.postDelayed(this, 1000);
                    }
                }, 1000);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //PorterShapeImageView imageViewbac;
        HexagonMaskView imageViewbac;
        TextView mentorShip, create, event;
        ImageView mentorshipImage, mentor, mentee, eventImage;
        ItemClickListener itemClickListener;

        RecyclerViewHolder(View v) {
            super(v);
            //imageViewbac = (PorterShapeImageView) v.findViewById(R.id.profile_pic);
            imageViewbac = (HexagonMaskView) v.findViewById(R.id.profile_pic);
            mentor = (ImageView) v.findViewById(R.id.mentor);
            mentee = (ImageView) v.findViewById(R.id.mentee);

            create = (TextView) v.findViewById(R.id.create);

            eventImage = (ImageView) v.findViewById(R.id.event_image);
            mentorshipImage = (ImageView) v.findViewById(R.id.mentorship_image);

            mentorShip = (TextView) v.findViewById(R.id.mentorship_text);
            event = (TextView) v.findViewById(R.id.event);

            imageViewbac.setOnClickListener(this);
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