package com.truyayong.gatherstory.content.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.truyayong.gatherstory.Bus.events.FgUpdateSentenceEvent;
import com.truyayong.gatherstory.Bus.events.UpdateSentenceEvent;
import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.content.activities.ShowArticleActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by alley_qiu on 2017/3/14.
 */

public class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationRecyclerViewAdapter.Holder> {

    private Context mContext;
    private List<FgUpdateSentenceEvent> updateSentenceEvents;

    public NotificationRecyclerViewAdapter(Context mContext, List<FgUpdateSentenceEvent> updateSentenceEvents) {
        this.mContext = mContext;
        this.updateSentenceEvents = updateSentenceEvents;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item_fragment_notification, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final String title = updateSentenceEvents.get(position).getTitle();
        String content = updateSentenceEvents.get(position).getContent();
        holder.tv_sentence_title_notification.setText(title);
        holder.tv_sentence_content_notification.setText(content);
        holder.card_option_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShowArticleActivity.class);
                intent.putExtra("title", title);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return updateSentenceEvents.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_sentence_title_notification)
        TextView tv_sentence_title_notification;
        @Bind(R.id.tv_sentence_content_notification)
        TextView tv_sentence_content_notification;
        @Bind(R.id.card_option_notification)
        CardView card_option_notification;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void addEvent(FgUpdateSentenceEvent event) {
        updateSentenceEvents.add(0,event);
        notifyDataSetChanged();
    }

    public void changeEvents(List<FgUpdateSentenceEvent> updateSentenceEvents) {
        this.updateSentenceEvents = updateSentenceEvents;
        notifyDataSetChanged();
    }
}
