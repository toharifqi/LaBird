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

import com.bima.toharifqi.labird.CourseActivity;
import com.bima.toharifqi.labird.R;
import com.bima.toharifqi.labird.helper.GlobalValue;
import com.bima.toharifqi.labird.model.CourseModel;
import com.bumptech.glide.Glide;

import java.util.List;

public class CourseAdapter extends PagerAdapter {
    private List<CourseModel> courseList;
    private LayoutInflater layoutInflater;
    private Context context;

    public CourseAdapter(Context context) {
        this.context = context;
    }

    public CourseAdapter(List<CourseModel> courseList, Context context) {
        this.courseList = courseList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return courseList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_course, container, false);

        ImageView courseImage;
        TextView title;

        courseImage = view.findViewById(R.id.courseImage);
        title = view.findViewById(R.id.courseTitle);

        Glide.with(context).load(courseList.get(position).getCourseImage()).into(courseImage);
        title.setText(courseList.get(position).getCourseTitle());

        final CourseModel courseModelExtra = courseList.get(position);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CourseActivity.class);
                intent.putExtra("courseModelExtra", courseModelExtra);
                GlobalValue.courseId = courseModelExtra.getCourseId();
                GlobalValue.spesiesId = null;

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
