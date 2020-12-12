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

import com.bima.toharifqi.labird.MateriActivity;
import com.bima.toharifqi.labird.R;
import com.bima.toharifqi.labird.model.MateriModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;


public class MateriSnapAdapter extends RecyclerView.Adapter<MateriSnapAdapter.RecyclerViewHolder> {

    private ArrayList<MateriModel> materiList;
    private LayoutInflater layoutInflater;
    private Context context;

    public MateriSnapAdapter(ArrayList<MateriModel> materiList, Context context) {
        this.materiList = materiList;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = layoutInflater.inflate(R.layout.item_materi, parent, false);
        return new RecyclerViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, int position) {

        Glide.with(context).load(materiList.get(position).getMateriImage()).placeholder(R.drawable.species_placeholder).listener(new RequestListener<Drawable>() {
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
        }).into(holder.materiImage);
        holder.title.setText(materiList.get(position).getMateriAuthor());
        holder.author.setText(materiList.get(position).getMateriAuthor());

        final MateriModel materiModelExtra = materiList.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MateriActivity.class);
                intent.putExtra("materiModelExtra", materiModelExtra);

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return materiList.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView materiImage;
        TextView title, author;
        ProgressBar progressBar;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            materiImage = itemView.findViewById(R.id.materiImage);
            title = itemView.findViewById(R.id.materiTitle);
            author = itemView.findViewById(R.id.materiAuthor);
            progressBar = itemView.findViewById(R.id.progressbar_pic);
        }
    }
}
