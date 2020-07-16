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

import com.bima.toharifqi.labird.CourseActivity;
import com.bima.toharifqi.labird.R;
import com.bima.toharifqi.labird.helper.GlobalValue;
import com.bima.toharifqi.labird.model.CourseModel;
import com.bumptech.glide.Glide;

import java.util.List;

public class CourseGridAdapter extends RecyclerView.Adapter<CourseGridAdapter.MyViewHolder> {

    private Context context;
    private List<CourseModel> courseList;

    public CourseGridAdapter(Context context, List<CourseModel> courseList) {
        this.context = context;
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_course, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final CourseModel courseModelExtra = courseList.get(position);

        holder.courseTitle.setText(courseList.get(position).getCourseTitle());
        Glide.with(context).load(courseList.get(position).getCourseImage()).into(holder.courseImage);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView courseTitle;
        ImageView courseImage;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            courseImage = itemView.findViewById(R.id.courseImage);
            courseTitle = itemView.findViewById(R.id.courseTitle);
            cardView = itemView.findViewById(R.id.courseCardview);
        }
    }
}
