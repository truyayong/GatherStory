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
import com.truyayong.gatherstory.content.bean.Article;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by alley_qiu on 2017/3/15.
 */

public class FollowArticleRecyclerViewAdapter extends RecyclerView.Adapter<FollowArticleRecyclerViewAdapter.Holder> {

    private List<Article> articles;
    private Context mContext;

    public FollowArticleRecyclerViewAdapter(List<Article> articles, Context mContext) {
        this.articles = articles;
        this.mContext = mContext;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item_follow_article, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final Article article = articles.get(position);
        holder.tv_article_title_follow_article.setText(article.getTitle());
        holder.tv_article_content_follow_article.setText(article.getContent());
        holder.card_option_follow_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShowArticleActivity.class);
                intent.putExtra("title", article.getTitle());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        @Bind(R.id.card_option_follow_article)
        CardView card_option_follow_article;
        @Bind(R.id.tv_article_title_follow_article)
        TextView tv_article_title_follow_article;
        @Bind(R.id.tv_article_content_follow_article)
        TextView tv_article_content_follow_article;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
