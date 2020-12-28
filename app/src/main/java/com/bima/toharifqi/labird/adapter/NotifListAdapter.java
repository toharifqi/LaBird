package com.bima.toharifqi.labird.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bima.toharifqi.labird.AddBirdActivity;
import com.bima.toharifqi.labird.R;
import com.bima.toharifqi.labird.SpesiesActivity;
import com.bima.toharifqi.labird.helper.GlobalValue;
import com.bima.toharifqi.labird.helper.GlobalValueUser;
import com.bima.toharifqi.labird.model.NotifModel;
import com.bima.toharifqi.labird.model.SpesiesModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class NotifListAdapter extends RecyclerView.Adapter<NotifListAdapter.ViewHolder> {
    private Context context;
    private List<NotifModel> notifModelList;

    public NotifListAdapter(Context context, List<NotifModel> notifModelList) {
        this.context = context;
        this.notifModelList = notifModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final NotifModel notifModel = notifModelList.get(position);
        final DatabaseReference spesiesDb = FirebaseDatabase.getInstance().getReference("spesies").child(notifModel.getSpesiesId());

        holder.expertName.setText("Dari " + notifModel.getExpertName());
        holder.message.setText(notifModel.getMessage());
        holder.timeStamp.setText(notifModel.getTimeStamp());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spesiesDb.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        GlobalValue.spesiesId = notifModel.getSpesiesId();

                        String spesiesName = dataSnapshot.child("spesiesName").getValue().toString();
                        String spesiesLatin = dataSnapshot.child("spesiesLatin").getValue().toString();
                        String spesiesImage = dataSnapshot.child("spesiesImage").getValue().toString();
                        String spesiesSound = dataSnapshot.child("spesiesImage").getValue().toString();
                        String spesiesId = dataSnapshot.child("spesiesId").getValue().toString();
                        String uploadedBy = dataSnapshot.child("uploadedBy").getValue().toString();
                        String sDescText = dataSnapshot.child("sDescText").getValue().toString();
                        String sSoundText = dataSnapshot.child("sSoundText").getValue().toString();
                        String sDistributionText = dataSnapshot.child("sDistributionText").getValue().toString();
                        String sHabitText = dataSnapshot.child("sHabitText").getValue().toString();
                        String sFoundedText = dataSnapshot.child("sFoundedText").getValue().toString();

                        SpesiesModel spesiesModel = new SpesiesModel(spesiesName, spesiesLatin, spesiesImage, spesiesSound,
                                spesiesId, uploadedBy, sDescText, sSoundText, sDistributionText, sHabitText, sFoundedText);

                        onSuccessLoad(spesiesModel, notifModel.getNotifId());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        onFailedLoad(databaseError.getMessage());
                    }
                });

            }
        });
    }

    private void onFailedLoad(String message) {
        Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
    }

    private void onSuccessLoad(SpesiesModel spesiesModel, String notifId) {
        Intent intent = new Intent(context, SpesiesActivity.class);
        intent.putExtra("spesiesModelExtra", spesiesModel);

        DatabaseReference notifDb = FirebaseDatabase.getInstance().getReference("notification").child(GlobalValueUser.uId).child(notifId);
        notifDb.removeValue();

        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return notifModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView message, expertName, timeStamp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.tv_message);
            expertName = itemView.findViewById(R.id.tv_expertname);
            timeStamp = itemView.findViewById(R.id.tv_timestamp);
        }
    }
}
