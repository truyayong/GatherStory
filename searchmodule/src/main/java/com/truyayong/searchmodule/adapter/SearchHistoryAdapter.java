package com.truyayong.searchmodule.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.truyayong.searchmodule.R;
import com.truyayong.searchmodule.custom.IOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alley_qiu on 2017/3/7.
 */

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.Holder>{

    private Context context;

    private List<String> historys = new ArrayList<>();

    public SearchHistoryAdapter(Context context, List<String> historys) {
        this.context = context;
        this.historys = historys;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        Holder holder = new Holder(LayoutInflater.from(context).
                inflate(R.layout.item_search_history, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        holder.historyInfo.setText(historys.get(position));

        holder.historyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iOnItemClickListener.onItemClick(historys.get(position));
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iOnItemClickListener.onItemDeleteClick(historys.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return historys.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView historyInfo;
        ImageView delete;

        public Holder(View view) {
            super(view);
            historyInfo = (TextView) view.findViewById(R.id.tv_item_search_history);
            delete = (ImageView) view.findViewById(R.id.iv_item_search_delete);
        }
    }

    private IOnItemClickListener iOnItemClickListener;

    public void setOnItemClickListener(IOnItemClickListener iOnItemClickListener) {
        this.iOnItemClickListener = iOnItemClickListener;
    }
}
