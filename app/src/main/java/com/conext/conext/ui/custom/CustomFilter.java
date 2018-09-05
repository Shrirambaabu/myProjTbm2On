package com.conext.conext.ui.custom;

import android.widget.Filter;

import com.conext.conext.model.Contact;
import com.conext.conext.ui.ContactsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ashith VL on 7/6/2017.
 */

public class CustomFilter extends Filter {
    private ContactsActivity.ContactAdapter contactAdapter;
    List<Contact> contactArrayList;

    public CustomFilter(List<Contact> contactArrayList, ContactsActivity.ContactAdapter contactAdapter) {
        this.contactAdapter = contactAdapter;
        this.contactArrayList = contactArrayList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        //CHECK CONSTRAINT VALIDITY
        if (constraint != null && constraint.length() > 0) {
            //CHANGE TO UPPER
            constraint = constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            ArrayList<Contact> filteredContact = new ArrayList<>();
            for (int i = 0; i < contactArrayList.size(); i++) {
                //CHECK
                if (contactArrayList.get(i).getName().toUpperCase().contains(constraint)) {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredContact.add(contactArrayList.get(i));
                }
            }
            results.count = filteredContact.size();
            results.values = filteredContact;
        } else {
            results.count = contactArrayList.size();
            results.values = contactArrayList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        contactAdapter.contactList = (ArrayList<Contact>) results.values;
        contactAdapter.notifyDataSetChanged();
    }
}
