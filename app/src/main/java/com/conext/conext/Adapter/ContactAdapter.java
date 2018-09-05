package com.conext.conext.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.conext.conext.R;
import com.conext.conext.model.Contact;
import com.conext.conext.ui.custom.HexagonMaskView;

import java.util.List;

import static com.conext.conext.utils.Constants.BaseIP;

/**
 * Created by Ashith VL on 6/24/2017.
 */


public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {

    private List<Contact> contactList;

    private Context context;
    private int itemResource;

    public ContactAdapter(Context context, int itemResource, List<Contact> contactList) {
        this.contactList = contactList;
        this.context = context;
        this.itemResource = itemResource;
    }

    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemResource, parent, false);
        return new ContactHolder(v);
    }

    @Override
    public void onBindViewHolder(final ContactHolder holder, final int position) {
        Contact contact = contactList.get(position);
        holder.img.setBorderColor(ContextCompat.getColor(context, R.color.colorPrimary));
        if (contact.getImage() != null) {
            String imageUrl = contact.getImage();
            Glide.with(context)
                    .load(BaseIP + "/" + imageUrl)
                    .thumbnail(0.5f)
                    .into(holder.img);
        }

    }

    @Override
    public int getItemCount() {
        return this.contactList.size();
    }

    class ContactHolder extends RecyclerView.ViewHolder {

        //  private ImageView img;
        private HexagonMaskView img;

        ContactHolder(View itemView) {
            super(itemView);
            // Set up the UI widgets of the holder
            this.img = (HexagonMaskView) itemView.findViewById(R.id.add_contact_image);
        }

    }


}