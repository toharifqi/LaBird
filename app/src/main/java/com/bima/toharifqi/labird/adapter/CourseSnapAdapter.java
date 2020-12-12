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
import androidx.recyclerview.widget.RecyclerView;

import com.bima.toharifqi.labird.CourseActivity;
import com.bima.toharifqi.labird.R;
import com.bima.toharifqi.labird.helper.GlobalValue;
import com.bima.toharifqi.labird.model.CourseModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class CourseSnapAdapter extends RecyclerView.Adapter<CourseSnapAdapter.RecyclerViewHolder> {

    private List<CourseModel> courseList;
    private LayoutInflater layoutInflater;
    private Context context;

    public CourseSnapAdapter(List<CourseModel> courseList, Context context) {
        this.courseList = courseList;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = layoutInflater.inflate(R.layout.item_course, parent, false);
        return new RecyclerViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, int position) {

        Glide.with(context).load(courseList.get(position).getCourseImage()).placeholder(R.drawable.species_placeholder).listener(new RequestListener<Drawable>() {
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
        }).into(holder.courseImage);
        holder.title.setText(courseList.get(position).getCourseTitle());

        final CourseModel courseModelExtra = courseList.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CourseActivity.class);
                intent.putExtra("courseModelExtra", courseModelExtra);
                GlobalValue.courseId = courseModelExtra.getCourseId();
                GlobalValue.spesiesId = null;

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView courseImage;
        TextView title;
        ProgressBar progressBar;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            courseImage = itemView.findViewById(R.id.courseImage);
            title = itemView.findViewById(R.id.courseTitle);
            progressBar = itemView.findViewById(R.id.progressbar_pic);
        }
    }
}
