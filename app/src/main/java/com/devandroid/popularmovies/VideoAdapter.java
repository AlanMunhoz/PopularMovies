package com.devandroid.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.devandroid.popularmovies.Model.Video;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private ListItemClickListener mOnClickListener;
    private ArrayList<Video> mVideo;
    private Context mContext;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    VideoAdapter(ListItemClickListener listener) {
        mOnClickListener = listener;
    }

    public void setListAdapter(ArrayList<Video> video) {
        mVideo = video;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VideoAdapter.VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.video_item, parent, false);
        VideoViewHolder viewHolder = new VideoViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if(mVideo==null)
            return 0;
        return mVideo.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivFigure;
        TextView tvName;

        VideoViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            ivFigure = itemView.findViewById(R.id.iv_figure);
            tvName = itemView.findViewById(R.id.tvName);
        }

        void bind(int listIndex) {
            tvName.setText(mVideo.get(listIndex).getName()/* + "\n" + mVideo.get(listIndex).getYoutubeUrl()*/);
            //Picasso.with(mContext).load("http://i3.ytimg.com/vi/PbRmbhdHDDM/hqdefault.jpg").into(ivFigure);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}