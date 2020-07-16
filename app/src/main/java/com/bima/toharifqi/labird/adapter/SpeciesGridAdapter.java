package com.bima.toharifqi.labird.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bima.toharifqi.labird.R;
import com.bima.toharifqi.labird.SpesiesActivity;
import com.bima.toharifqi.labird.helper.GlobalValue;
import com.bima.toharifqi.labird.model.SpesiesModel;
import com.bumptech.glide.Glide;

import java.util.List;

public class SpeciesGridAdapter extends RecyclerView.Adapter<SpeciesGridAdapter.MyViewHolder> {

    private Context context;
    private List<SpesiesModel> spesiesList;

    public SpeciesGridAdapter(Context context, List<SpesiesModel> spesiesList) {
        this.context = context;
        this.spesiesList = spesiesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_spesies_grid, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final SpesiesModel spesiesModelExtra = spesiesList.get(position);

        holder.spesiesName.setText(spesiesList.get(position).getSpesiesName());
        Glide.with(context).load(spesiesList.get(position).getSpesiesImage()).into(holder.spesiesThumbnail);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SpesiesActivity.class);
                intent.putExtra("spesiesModelExtra", spesiesModelExtra);

                GlobalValue.spesiesId = spesiesModelExtra.getSpesiesId();

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return spesiesList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView spesiesName;
        ImageView spesiesThumbnail;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            spesiesName = itemView.findViewById(R.id.spesies_name_id);
            spesiesThumbnail = itemView.findViewById(R.id.spesies_img_id);
            cardView = itemView.findViewById(R.id.cardview_id);
        }
    }
}
