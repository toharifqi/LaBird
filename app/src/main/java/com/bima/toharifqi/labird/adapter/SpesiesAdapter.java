package com.bima.toharifqi.labird.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bima.toharifqi.labird.R;
import com.bima.toharifqi.labird.SpesiesActivity;
import com.bima.toharifqi.labird.helper.GlobalValue;
import com.bima.toharifqi.labird.model.SpesiesModel;
import com.bumptech.glide.Glide;

import java.util.List;

public class SpesiesAdapter extends PagerAdapter {
    private List<SpesiesModel> spesiesList;
    private LayoutInflater layoutInflater;
    private Context context;

    public SpesiesAdapter(Context context) {
        this.context = context;
    }

    public SpesiesAdapter(List<SpesiesModel> spesiesList, Context context) {
        this.spesiesList = spesiesList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return spesiesList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_spesies, container, false);

        ImageView spesiesImage;
        TextView name, latin;

        spesiesImage = view.findViewById(R.id.spesiesImage);
        name = view.findViewById(R.id.spesiesName);
        latin = view.findViewById(R.id.spesiesLatin);

        Glide.with(context).load(spesiesList.get(position).getSpesiesImage()).into(spesiesImage);
        name.setText(spesiesList.get(position).getSpesiesName());
        latin.setText(spesiesList.get(position).getSpesiesLatin());

        final SpesiesModel spesiesModelExtra = spesiesList.get(position);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SpesiesActivity.class);
                intent.putExtra("spesiesModelExtra", spesiesModelExtra);

                GlobalValue.spesiesId = spesiesModelExtra.getSpesiesId();
                GlobalValue.courseId = null;

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
