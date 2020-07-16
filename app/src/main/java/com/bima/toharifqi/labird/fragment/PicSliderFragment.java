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
import com.bima.toharifqi.labird.adapter.PicSliderAdapter;
import com.bima.toharifqi.labird.helper.GlobalValue;
import com.bima.toharifqi.labird.listener.DepthPageTransformer;
import com.bima.toharifqi.labird.listener.IFirebaseLoadDonePic;
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
public class PicSliderFragment extends Fragment implements IFirebaseLoadDonePic{
    AlertDialog dialog;
    ViewPager viewPagerPic;
    PicSliderAdapter picSliderAdapter;

    IFirebaseLoadDonePic iFirebaseLoadDone;

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<String> picList = new ArrayList<>();
            for (DataSnapshot picSnapshot:dataSnapshot.getChildren())
                picList.add(picSnapshot.getValue().toString());
            iFirebaseLoadDone.onFirebaseLoadSuccess(picList);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            iFirebaseLoadDone.onFirebaseLoadFailed(databaseError.getMessage());
        }
    };

    Query query;
    View view;

    public PicSliderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pic, container, false);

        String spesiesId = GlobalValue.spesiesId;
        String courseId= GlobalValue.courseId;
        //to display loading dialog
        dialog = new SpotsDialog.Builder().setContext(getActivity()).build();
        dialog.setMessage("Mohon tunggu...");

        if (spesiesId==null){
            query = FirebaseDatabase.getInstance().getReference("course").child(courseId).child("birdImage");
        }else {
            query = FirebaseDatabase.getInstance().getReference("spesies").child(spesiesId).child("spesiesPic");
        }

        iFirebaseLoadDone = this;

        loadPic();

        viewPagerPic = (ViewPager) view.findViewById(R.id.picViewPager);
        viewPagerPic.setPageTransformer(true, new DepthPageTransformer());

        return view;
    }

    private void loadPic() {
        dialog.show();
        query.addValueEventListener(valueEventListener);
    }

    @Override
    public void onFirebaseLoadSuccess(List<String> picList) {
        picSliderAdapter = new PicSliderAdapter(picList, getActivity());
        viewPagerPic.setAdapter(picSliderAdapter);
        dialog.dismiss();
    }

    @Override
    public void onFirebaseLoadFailed(String message) {
        Toast.makeText(getActivity(), ""+message, Toast.LENGTH_SHORT).show();
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
