package com.example.android.e_blood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.UserDictionary;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Button;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chakr_000 on 27-Mar-17.
 */

public class DonorAdapter extends ArrayAdapter<DonorListStructure> {

    public DonorAdapter(DonorListOPos context, ArrayList<DonorListStructure> DonorArrayList){
        super(context.getActivity(), 0, DonorArrayList);
    }

    public DonorAdapter(DonorListONeg context, ArrayList<DonorListStructure> DonorArrayList){
        super(context.getActivity(), 0, DonorArrayList);
    }

    public DonorAdapter(DonorListAPos context, ArrayList<DonorListStructure> DonorArrayList){
        super(context.getActivity(), 0, DonorArrayList);
    }

    public DonorAdapter(DonorListANeg context, ArrayList<DonorListStructure> DonorArrayList){
        super(context.getActivity(), 0, DonorArrayList);
    }

    public DonorAdapter(DonorListBPos context, ArrayList<DonorListStructure> DonorArrayList){
        super(context.getActivity(), 0, DonorArrayList);
    }

    public DonorAdapter(DonorListBNeg context, ArrayList<DonorListStructure> DonorArrayList){
        super(context.getActivity(), 0, DonorArrayList);
    }

    public DonorAdapter(DonorListABPos context, ArrayList<DonorListStructure> DonorArrayList){
        super(context.getActivity(), 0, DonorArrayList);
    }

    public DonorAdapter(DonorListABNeg context, ArrayList<DonorListStructure> DonorArrayList){
        super(context.getActivity(), 0, DonorArrayList);
    }




    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
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


        Button smsbutton = donorList.findViewById(R.id.sms);
        smsbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", currentDonor.getPhone(), null)));
            }
        });

        final Double lat = currentDonor.getLat();
        final Double lng = currentDonor.getLng();

        Button navbutton = donorList.findViewById(R.id.navigate);
        navbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(android.content.Intent.ACTION_VIEW,Uri.parse("http://maps.google.com/?daddr="+lat+","+lng)));

            }
        });

        return donorList;
    }
}
