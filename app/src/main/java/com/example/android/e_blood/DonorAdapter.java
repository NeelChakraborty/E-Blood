package com.example.android.e_blood;

import android.app.Activity;
import android.content.Context;
import android.provider.UserDictionary;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chakr_000 on 27-Mar-17.
 */

public class DonorAdapter extends ArrayAdapter<DonorListStructure> {

    public DonorAdapter(Activity context, ArrayList<DonorListStructure> DonorArrayList){
        super(context, 0, (List<DonorListStructure>) DonorArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View donorList = convertView;
        if (donorList == null)
            donorList = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

        final DonorListStructure currentDonor = getItem(position);

        TextView donorName = (TextView)donorList.findViewById(R.id.DonorListName);
        donorName.setText(currentDonor.getName());

        TextView donorPhone = (TextView)donorList.findViewById(R.id.PhoneListItem);
        donorPhone.setText(currentDonor.getPhone());

        TextView donorBloodGroup = (TextView)donorList.findViewById(R.id.BGListItem);
        donorBloodGroup.setText(currentDonor.getBloodGroup());

        TextView donorAddress = (TextView)donorList.findViewById(R.id.AddressListItem);
        donorAddress.setText(currentDonor.getAddress());

        return donorList;
    }
}
