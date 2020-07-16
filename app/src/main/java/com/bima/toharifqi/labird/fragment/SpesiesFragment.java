package com.bima.toharifqi.labird.fragment;


import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bima.toharifqi.labird.R;
import com.bima.toharifqi.labird.adapter.SpesiesAdapter;
import com.bima.toharifqi.labird.listener.IFirebaseLoadDoneSpesies;
import com.bima.toharifqi.labird.model.SpesiesModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpesiesFragment extends Fragment implements IFirebaseLoadDoneSpesies{
    AlertDialog dialog;

    ViewPager viewPagerSpesies;
    SpesiesAdapter spesiesAdapter;

    IFirebaseLoadDoneSpesies iFirebaseLoadDone;

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<SpesiesModel> spesiesList = new ArrayList<>();
            for (DataSnapshot spesiesSnapshot:dataSnapshot.getChildren()){
                spesiesList.add(spesiesSnapshot.getValue(SpesiesModel.class));
            }
            iFirebaseLoadDone.onFirebaseLoadSuccess(spesiesList);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            iFirebaseLoadDone.onFirebaseLoadFailed(databaseError.getMessage());
        }
    };

    Query query;
    View view;


    public SpesiesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_spesies, container, false);

        query = FirebaseDatabase.getInstance().getReference("spesies").orderByKey();

        iFirebaseLoadDone = this;

        //to display loading dialog
        dialog = new SpotsDialog.Builder().setContext(getContext()).build();
        dialog.setMessage("Mohon tunggu...");

        loadSpesies();

        viewPagerSpesies = (ViewPager) view.findViewById(R.id.spesiesViewPager);
        viewPagerSpesies.setPadding(20,0,280,0);
        viewPagerSpesies.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }

    private void loadSpesies() {
        dialog.show();
        query.addValueEventListener(valueEventListener);
    }


    @Override
    public void onFirebaseLoadSuccess(List<SpesiesModel> spesiesList) {
        spesiesAdapter = new SpesiesAdapter(spesiesList, getContext());
        viewPagerSpesies.setAdapter(spesiesAdapter);
        dialog.dismiss();
    }

    @Override
    public void onFirebaseLoadFailed(String message) {
        Toast.makeText(getContext(), ""+message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    @Override
    public void onDestroy() {
        query.removeEventListener(valueEventListener);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        query.addValueEventListener(valueEventListener);
    }

    @Override
    public void onStop() {
        query.removeEventListener(valueEventListener);
        super.onStop();
    }
}
