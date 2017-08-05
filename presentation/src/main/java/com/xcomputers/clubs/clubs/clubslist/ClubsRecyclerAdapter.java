package com.xcomputers.clubs.clubs.clubslist;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xcomputers.clubs.R;
import com.xcomputers.clubs.clubs.util.GlideHelper;
import com.xcomputers.networking.clubs.Club;

import java.util.List;

/**
 * Created by xComputers on 05/08/2017.
 */

class ClubsRecyclerAdapter extends RecyclerView.Adapter<ClubsRecyclerAdapter.ClubsViewHolder> {

    private List<Club> clubs;
    private OnItemClickedListener listener;

    ClubsRecyclerAdapter(List<Club> clubs, OnItemClickedListener listener){

        this.listener = listener;
        this.clubs = clubs;
    }

    void setData(List<Club> clubs){
        if(clubs != this.clubs){
            this.clubs = clubs;
        }
        notifyDataSetChanged();
    }

    @Override
    public ClubsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ClubsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.clubs_recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ClubsViewHolder holder, int position) {
        Club club = clubs.get(position);
        holder.clubName.setText(club.getName());
        GlideHelper.loadImageCircle(holder.itemView.getContext(), club.getThumbNailUrl(), holder.clubImage, R.drawable.noimage);
    }

    @Override
    public int getItemCount() {
        return clubs.size();
    }

    class ClubsViewHolder extends RecyclerView.ViewHolder {

        private ImageView clubImage;
        private TextView clubName;

        ClubsViewHolder(View itemView) {
            super(itemView);
            clubImage = (ImageView) itemView.findViewById(R.id.club_image);
            clubName = (TextView) itemView.findViewById(R.id.club_name);
            itemView.setOnClickListener(v -> {
                if(listener != null) {
                    listener.onItemClicked(clubs.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnItemClickedListener{
        void onItemClicked(Club club);
    }
}
