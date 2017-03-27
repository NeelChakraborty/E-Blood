package com.example.android.e_blood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ListMenuItemView;
import android.widget.ListView;

import java.util.ArrayList;

public class DonorList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_list);

        final ArrayList<DonorListStructure> donors = new ArrayList<>();
        donors.add(new DonorListStructure("Souradip", "8981785251", "Behala, Kolkata", "O+"));
        donors.add(new DonorListStructure("Souradip", "8981785251", "Behala, Kolkata", "O+"));
        donors.add(new DonorListStructure("Souradip", "8981785251", "Behala, Kolkata", "O+"));
        donors.add(new DonorListStructure("Souradip", "8981785251", "Behala, Kolkata", "O+"));
        donors.add(new DonorListStructure("Souradip", "8981785251", "Behala, Kolkata", "O+"));
        donors.add(new DonorListStructure("Souradip", "8981785251", "Behala, Kolkata", "O+"));
        donors.add(new DonorListStructure("Souradip", "8981785251", "Behala, Kolkata", "O+"));
        donors.add(new DonorListStructure("Souradip", "8981785251", "Behala, Kolkata", "O+"));
        donors.add(new DonorListStructure("Souradip", "8981785251", "Behala, Kolkata", "O+"));

        DonorAdapter donorAdapter = new DonorAdapter(this, donors);
        ListView listView = (ListView)findViewById(R.id.list);
        listView.setAdapter(donorAdapter);

    }
}
