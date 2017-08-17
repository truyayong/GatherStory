package com.truyayong.gatherstory.user.adapter;

import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.content.activities.ShowArticleActivity;
import com.truyayong.gatherstory.content.bean.Article;
import com.truyayong.gatherstory.content.bean.Sentence;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by alley_qiu on 2017/3/16.
 */

public class WriteSentencesRecyclerViewAdapter extends RecyclerView.Adapter<WriteSentencesRecyclerViewAdapter.Holder> {

    private List<Sentence> sentences;
    private Context mContext;

    public WriteSentencesRecyclerViewAdapter(Context mContext, List<Sentence> sentences) {
        this.mContext = mContext;
        this.sentences = sentences;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item_write_sentences, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final Sentence sentence = sentences.get(position);
        holder.tv_sentence_title_write_sentences.setText(sentence.getTitle());
        holder.tv_sentence_content_write_sentences.setText(sentence.getContent());
        holder.card_option_write_sentences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShowArticleActivity.class);
                intent.putExtra("title", sentence.getTitle());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sentences.size();
    }


    class Holder extends RecyclerView.ViewHolder {

        @Bind(R.id.card_option_write_sentences)
        CardView card_option_write_sentences;
        @Bind(R.id.tv_sentence_title_write_sentences)
        TextView tv_sentence_title_write_sentences;
        @Bind(R.id.tv_sentence_content_write_sentences)
        TextView tv_sentence_content_write_sentences;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
