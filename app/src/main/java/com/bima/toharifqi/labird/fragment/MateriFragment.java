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
import com.bima.toharifqi.labird.adapter.MateriAdapter;
import com.bima.toharifqi.labird.listener.IFirebaseLoadDoneMateri;
import com.bima.toharifqi.labird.model.MateriModel;
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
public class MateriFragment extends Fragment implements IFirebaseLoadDoneMateri{

    ViewPager viewPagerMateri;
    MateriAdapter materiAdapter;

    IFirebaseLoadDoneMateri iFirebaseLoadDone;

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List <MateriModel> materiList = new ArrayList<>();
            for (DataSnapshot materiSnapshot:dataSnapshot.getChildren())
                materiList.add(materiSnapshot.getValue(MateriModel.class));
            iFirebaseLoadDone.onFirebaseLoadMateriSuccess(materiList);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            iFirebaseLoadDone.onFirebaseLoadMateriFailed(databaseError.getMessage());
        }
    };

    Query query;
    View view;

    public MateriFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_materi, container, false);

        query = FirebaseDatabase.getInstance().getReference("materi").orderByKey();

        iFirebaseLoadDone = this;

        loadMateri();

        viewPagerMateri = (ViewPager) view.findViewById(R.id.materiViewPager);
        viewPagerMateri.setPadding(20,0,280,0);
        viewPagerMateri.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    private void loadMateri() {
        query.addValueEventListener(valueEventListener);
    }

    @Override
    public void onFirebaseLoadMateriSuccess(List<MateriModel> materiList) {
        materiAdapter = new MateriAdapter(materiList, getContext());
        viewPagerMateri.setAdapter(materiAdapter);
    }

    @Override
    public void onFirebaseLoadMateriFailed(String message) {
        Toast.makeText(getContext(), ""+message, Toast.LENGTH_SHORT).show();
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
