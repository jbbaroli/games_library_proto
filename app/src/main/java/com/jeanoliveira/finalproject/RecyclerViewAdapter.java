package com.jeanoliveira.finalproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Jean on 3/23/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<Games> gameData;

    public RecyclerViewAdapter(Context mContext, List<Games> gameData) {
        this.mContext = mContext;
        this.gameData = gameData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_game, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.txtGameTitle.setText(gameData.get(position).getTitle());
        Picasso.get().load(gameData.get(position).getImage_small()).into(holder.ivGameCover);
    }

    @Override
    public int getItemCount() {
        return gameData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtGameTitle;
        ImageView ivGameCover;

        public ViewHolder(View itemView) {
            super(itemView);

            txtGameTitle = itemView.findViewById(R.id.txtGameTitle);
            ivGameCover = itemView.findViewById(R.id.ivGameCover);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                int pos = getAdapterPosition();

                if (pos != RecyclerView.NO_POSITION) {
                    Games clickedGame = gameData.get(pos);
                    Intent intent = new Intent(mContext, DetailActivity.class);

                    String[] gameDetails = {clickedGame.getGameId(),
                                            clickedGame.getTitle(),
                                            clickedGame.getImage_medium(),
                                            clickedGame.getGame_url(),
                                            clickedGame.getDescription()};

                    intent.putExtra("gameDetails", gameDetails);
                    mContext.startActivity(intent);
                }
                }
            });
        }
    }
}
