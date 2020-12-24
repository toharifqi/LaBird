package com.bima.toharifqi.labird.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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

import java.util.List;

public class MateriGridAdapter extends RecyclerView.Adapter<MateriGridAdapter.MyViewHolder> {
    private Context context;
    private List<MateriModel> materiList;

    public MateriGridAdapter(Context context, List<MateriModel> materiList) {
        this.context = context;
        this.materiList = materiList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_materi, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final MateriModel materiModelExtra = materiList.get(position);

        holder.cardView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        holder.materiTitle.setText(materiList.get(position).getMateriTitle());
        holder.materiAuthor.setText(materiList.get(position).getMateriAuthor());
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


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView materiTitle, materiAuthor;
        ImageView materiImage;
        ProgressBar progressBar;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            materiTitle = itemView.findViewById(R.id.materiTitle);
            materiAuthor = itemView.findViewById(R.id.materiAuthor);
            materiImage = itemView.findViewById(R.id.materiImage);
            progressBar = itemView.findViewById(R.id.progressbar_pic);
            cardView = itemView.findViewById(R.id.cardview_materi);
        }
    }
}
