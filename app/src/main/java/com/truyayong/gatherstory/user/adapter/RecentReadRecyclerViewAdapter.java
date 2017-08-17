package com.truyayong.gatherstory.user.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.content.activities.ShowArticleActivity;
import com.truyayong.gatherstory.content.bean.RecentArticle;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by alley_qiu on 2017/3/17.
 */

public class RecentReadRecyclerViewAdapter extends RecyclerView.Adapter<RecentReadRecyclerViewAdapter.Holder>{

    private Context mContext;
    private List<RecentArticle> datas = new ArrayList<>();

    public RecentReadRecyclerViewAdapter(List<RecentArticle> datas, Context mContext) {
        this.datas = datas;
        this.mContext = mContext;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item_recent_read, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        final RecentArticle recentArticle = datas.get(position);
        holder.tv_article_title_recent_read.setText(recentArticle.getTitle());
        holder.tv_article_content_recent_read.setText(recentArticle.getContent());
        holder.card_option_recent_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShowArticleActivity.class);
                intent.putExtra("title", recentArticle.getTitle());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        @Bind(R.id.card_option_recent_read)
        CardView card_option_recent_read;
        @Bind(R.id.tv_article_title_recent_read)
        TextView tv_article_title_recent_read;
        @Bind(R.id.tv_article_content_recent_read)
        TextView tv_article_content_recent_read;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
