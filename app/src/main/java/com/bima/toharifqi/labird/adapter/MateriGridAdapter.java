package com.bima.toharifqi.labird.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bima.toharifqi.labird.MateriActivity;
import com.bima.toharifqi.labird.R;
import com.bima.toharifqi.labird.model.MateriModel;
import com.bumptech.glide.Glide;

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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final MateriModel materiModelExtra = materiList.get(position);

        holder.materiTitle.setText(materiList.get(position).getMateriTitle());
        holder.materiAuthor.setText(materiList.get(position).getMateriAuthor());
        Glide.with(context).load(materiList.get(position).getMateriImage()).into(holder.materiImage);
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


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            materiTitle = itemView.findViewById(R.id.materiTitle);
            materiAuthor = itemView.findViewById(R.id.materiAuthor);
            materiImage = itemView.findViewById(R.id.materiImage);
        }
    }
}
