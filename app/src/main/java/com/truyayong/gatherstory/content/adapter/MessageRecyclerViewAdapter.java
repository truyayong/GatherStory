package com.truyayong.gatherstory.content.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.truyayong.gatherstory.Bus.events.FgUserMessageEvent;
import com.truyayong.gatherstory.Bus.events.IndexUserMessageEvent;
import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.im.activities.ChatActivity;
import com.truyayong.gatherstory.user.bean.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by alley_qiu on 2017/3/18.
 */

public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.Holder> {

    private Context mContext;
    private List<FgUserMessageEvent> events = new ArrayList<>();

    public MessageRecyclerViewAdapter(Context mContext, List<FgUserMessageEvent> events) {
        this.events = events;
        this.mContext = mContext;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item_fragment_message, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final FgUserMessageEvent event = events.get(position);
        Log.e("truyayong", "adapter, userId : " + event.getUserId() + " userName : " + event.getUserName());
        if (event.getUserHead() == null || "".equals(event.getUserHead())) {
            event.setUserHead("http://bmob-cdn-9483.b0.upaiyun.com/2017/03/07/ca3ef7b200024374a4414037203bbe53.png");
        }
        if (event.getUserName() == null || "".equals(event.getUserName())) {
            event.setUserName("神秘带头大哥的私信");
        }
        holder.tv_user_name_user_message.setText(event.getUserName());
        holder.tv_content_user_message.setText(event.getMessage());
        Picasso.with(mContext).load(event.getUserHead()).placeholder(R.drawable.ic_about_truyayong_24dp).into(holder.civ_head_view_user_message);
        holder.card_option_user_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobQuery<User> query = new BmobQuery<>();
                query.addWhereEqualTo("objectId", event.getUserId());
                query.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> list, BmobException e) {
                        if (e == null && !list.isEmpty()) {
                            sendMessage(list.get(0));
                        } else {
                            Log.e("truyayong", "sendMessage fail size : " + list.size());
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        @Bind(R.id.card_option_user_message)
        CardView card_option_user_message;
        @Bind(R.id.civ_head_view_user_message)
        CircleImageView civ_head_view_user_message;
        @Bind(R.id.tv_user_name_user_message)
        TextView tv_user_name_user_message;
        @Bind(R.id.tv_content_user_message)
        TextView tv_content_user_message;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void addEvent(FgUserMessageEvent event) {
        Log.e("truyayong", "addEvent, userId : " + event.getUserId() + " userName : " + event.getUserName() + " message : " + event.getMessage());
        events.add(0, event);
        notifyDataSetChanged();
    }

    public void changeEvents(List<FgUserMessageEvent> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    private void sendMessage(User user) {
        final BmobIMUserInfo userInfo = new BmobIMUserInfo(user.getObjectId(), user.getUserName(), user.getUserHeadUrl());
        BmobIM.getInstance().startPrivateConversation(userInfo, new ConversationListener() {
            @Override
            public void done(BmobIMConversation bmobIMConversation, BmobException e) {
                if (e == null) {
                    Intent chatIntent = new Intent(mContext, ChatActivity.class);
                    Bundle chatBundle = new Bundle();
                    chatBundle.putSerializable("com.truyayong.oldhouse.BmobIMConversation", bmobIMConversation);
                    chatBundle.putSerializable("cn.bmob.newim.bean.BmobIMUserInfo", userInfo);
                    chatIntent.putExtras(chatBundle);
                    mContext.startActivity(chatIntent);
                } else {
                    Toast.makeText(mContext, "开启会话失败", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}
