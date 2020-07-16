package com.bima.toharifqi.labird.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bima.toharifqi.labird.HomeActivity;
import com.bima.toharifqi.labird.ProfileActivity;
import com.bima.toharifqi.labird.R;
import com.bima.toharifqi.labird.model.LeaderBoardModel;

import java.util.List;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.MyViewHolder> {

    private Context context;
    private List<LeaderBoardModel> leaderBoardList;

    public LeaderBoardAdapter(Context context, List<LeaderBoardModel> leaderBoardList) {
        this.context = context;
        this.leaderBoardList = leaderBoardList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_leaderboard, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.name.setText(leaderBoardList.get(position).getName());
        holder.level.setText("Level "+leaderBoardList.get(position).getLevel());
        holder.poin.setText(leaderBoardList.get(position).getPoin()*(-1)+" poin");
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open the profile page if this cardview is clicked
                Intent intent = new Intent(context, ProfileActivity.class);
                String poin = leaderBoardList.get(position).getPoin()*(-1)+" pts";
                String username = leaderBoardList.get(position).getUsername();
                intent.putExtra("poinText", poin);
                intent.putExtra("username", username);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return leaderBoardList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name, level, poin;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.board_name);
            level = itemView.findViewById(R.id.board_level);
            poin = itemView.findViewById(R.id.poin_textview);
            cardView = itemView.findViewById(R.id.board_cardview);
        }
    }
}
