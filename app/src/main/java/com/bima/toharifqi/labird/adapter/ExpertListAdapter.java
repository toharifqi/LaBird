package com.bima.toharifqi.labird.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bima.toharifqi.labird.AddBirdActivity2;
import com.bima.toharifqi.labird.R;
import com.bima.toharifqi.labird.model.ExpertModel;

import java.util.List;

public class ExpertListAdapter extends RecyclerView.Adapter<ExpertListAdapter.MyViewHolder> {

    private Context context;
    private List<ExpertModel> expertModelList;

    public ExpertListAdapter(Context context, List<ExpertModel> expertModelList) {
        this.context = context;
        this.expertModelList = expertModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_expert, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ExpertModel expertModelExtra = expertModelList.get(position);

        holder.expertName.setText(expertModelExtra.getNama());
        holder.expertEmail.setText(expertModelExtra.getEmail());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddBirdActivity2.class);
                intent.putExtra("expertExtraName", expertModelExtra.getNama());
                intent.putExtra("expertExtraUid", expertModelExtra.getuId());

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return expertModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView expertName, expertEmail;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            expertName = itemView.findViewById(R.id.tv_message);
            expertEmail = itemView.findViewById(R.id.tv_expertname);
        }
    }
}
