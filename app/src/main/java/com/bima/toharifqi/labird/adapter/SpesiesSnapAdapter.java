package com.bima.toharifqi.labird.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bima.toharifqi.labird.R;
import com.bima.toharifqi.labird.SpesiesActivity;
import com.bima.toharifqi.labird.helper.GlobalValue;
import com.bima.toharifqi.labird.model.SpesiesModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class SpesiesSnapAdapter extends RecyclerView.Adapter<SpesiesSnapAdapter.RecyclerViewHolder> {
    private List<SpesiesModel> spesiesList;
    private LayoutInflater layoutInflater;
    private Context context;

    public SpesiesSnapAdapter(List<SpesiesModel> spesiesList, Context context) {
        this.spesiesList = spesiesList;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = layoutInflater.inflate(R.layout.item_spesies, parent, false);
        return new RecyclerViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, int position) {
        Glide.with(context).load(spesiesList.get(position).getSpesiesImage()).placeholder(R.drawable.species_placeholder).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.spesiesImage);
        holder.name.setText(spesiesList.get(position).getSpesiesName());
        holder.latin.setText(spesiesList.get(position).getSpesiesLatin());

        final SpesiesModel spesiesModelExtra = spesiesList.get(position);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SpesiesActivity.class);
                intent.putExtra("spesiesModelExtra", spesiesModelExtra);

                GlobalValue.spesiesId = spesiesModelExtra.getSpesiesId();
                GlobalValue.courseId = null;

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return spesiesList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView spesiesImage;
        TextView name, latin;
        CardView cardView;
        ProgressBar progressBar;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            spesiesImage = itemView.findViewById(R.id.spesiesImage);
            name = itemView.findViewById(R.id.spesiesName);
            latin = itemView.findViewById(R.id.spesiesLatin);
            cardView = itemView.findViewById(R.id.cardview_spesies);
            progressBar = itemView.findViewById(R.id.progressbar_pic);
        }
    }
}
