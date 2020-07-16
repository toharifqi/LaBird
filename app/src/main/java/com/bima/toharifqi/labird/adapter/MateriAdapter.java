package com.bima.toharifqi.labird.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bima.toharifqi.labird.MateriActivity;
import com.bima.toharifqi.labird.R;
import com.bima.toharifqi.labird.model.MateriModel;
import com.bumptech.glide.Glide;

import java.util.List;

public class MateriAdapter extends PagerAdapter {
    private List<MateriModel> materiList;
    private LayoutInflater layoutInflater;
    private Context context;

    public MateriAdapter(Context context) {
        this.context = context;
    }

    public MateriAdapter(List<MateriModel> materiList, Context context) {
        this.materiList = materiList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return materiList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_materi, container, false);

        ImageView materiImage;
        TextView title, author;

        materiImage = view.findViewById(R.id.materiImage);
        title = view.findViewById(R.id.materiTitle);
        author = view.findViewById(R.id.materiAuthor);

        Glide.with(context).load(materiList.get(position).getMateriImage()).into(materiImage);
        title.setText(materiList.get(position).getMateriTitle());
        author.setText(materiList.get(position).getMateriAuthor());

        final MateriModel materiModelExtra = materiList.get(position);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MateriActivity.class);
                intent.putExtra("materiModelExtra", materiModelExtra);

                context.startActivity(intent);
            }
        });

        container.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
