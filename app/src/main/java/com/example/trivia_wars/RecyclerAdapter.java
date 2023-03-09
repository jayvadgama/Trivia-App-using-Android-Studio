package com.example.trivia_wars;

import android.view.ViewGroup;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

        private Context context;
        private List<String> myCategoryList;
        private ListItemClickListener mOnClickListener;

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            CardView cardView;
            TextView tvCategory;
            final private ListItemClickListener mOnClickListener;

            ViewHolder(View itemView, ListItemClickListener onClickListener) {
                super(itemView);
                cardView = (CardView) itemView;
                tvCategory = itemView.findViewById(R.id.tv_category);
                mOnClickListener = onClickListener;

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                mOnClickListener.onListItemClick(position);
            }
        }

        public RecyclerAdapter(List<String> categoryList, ListItemClickListener onClickListener) {
            myCategoryList = categoryList;
            this.mOnClickListener = onClickListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (context == null) {
                context = parent.getContext();
            }

            View view = LayoutInflater.from(context).inflate(R.layout.card_layout, parent, false);
            return new ViewHolder(view, mOnClickListener);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            //String categoryTitle = myCategoryList.get(position);
            holder.tvCategory.setText(myCategoryList.get(position));
        }

        @Override
        public int getItemCount() {
            return myCategoryList.size();
        }

        public interface ListItemClickListener{
            void onListItemClick(int position);
        }

    }