package com.truyayong.gatherstory.user.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.user.activities.UserInfomationActivity;
import com.truyayong.gatherstory.user.bean.User;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by alley_qiu on 2017/3/15.
 */

public class FollowMeRecyclerViewAdapter extends RecyclerView.Adapter<FollowMeRecyclerViewAdapter.Holder> {

    private Context mContext;
    private List<User> users;

    public FollowMeRecyclerViewAdapter(Context mContext, List<User> users) {
        this.mContext = mContext;
        this.users = users;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item_follow_me, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final User user = users.get(position);
        Picasso.with(mContext).load(user.getUserHeadUrl()).into(holder.civ_head_view_follow_me);
        holder.tv_user_name_follow_me.setText(user.getUserName());
        holder.tv_user_description_follow_me.setText(user.getUserDescription());
        holder.card_option_follow_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserInfomationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("com.truyayong.oldhouse.user.bean.user", user);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    class Holder extends RecyclerView.ViewHolder {

        @Bind(R.id.card_option_follow_me)
        CardView card_option_follow_me;
        @Bind(R.id.civ_head_view_follow_me)
        CircleImageView civ_head_view_follow_me;
        @Bind(R.id.tv_user_name_follow_me)
        TextView tv_user_name_follow_me;
        @Bind(R.id.tv_user_description_follow_me)
        TextView tv_user_description_follow_me;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
