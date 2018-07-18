package com.devandroid.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    final private ListItemClickListener mOnClickListener;
    final private ArrayList<ListItem> mListItems;
    private Context mContext;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public ListAdapter(ArrayList<ListItem> listItems, ListItemClickListener listener) {

        mListItems = listItems;
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public ListAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item, parent, false);
        ListViewHolder viewHolder = new ListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mListItems.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivFigure;
        TextView tvTitle;

        public ListViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            ivFigure = itemView.findViewById(R.id.iv_figure);
            tvTitle = itemView.findViewById(R.id.tv_title);
        }

        void bind(int listIndex) {
            Picasso.with(mContext).load(mListItems.get(listIndex).getFigure()).into(ivFigure);
            tvTitle.setText(mListItems.get(listIndex).getTitle());
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}