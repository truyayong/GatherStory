package com.truyayong.gatherstory.im.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.im.adapter.base.BaseViewHolder;
import com.truyayong.gatherstory.user.activities.UserInfomationActivity;
import com.truyayong.gatherstory.user.bean.User;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.Bind;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by alley_qiu on 2017/3/12.
 */

public class ReceiveTextHolder extends BaseViewHolder implements View.OnClickListener, View.OnLongClickListener {


    @Bind(R.id.iv_avatar)
    protected ImageView iv_avatar;

    @Bind(R.id.tv_time)
    protected TextView tv_time;

    @Bind(R.id.tv_message)
    protected TextView tv_message;

    private Context mContext;

    private BmobIMUserInfo mUserInfo;

    public ReceiveTextHolder(Context context, ViewGroup root, OnRecyclerViewListener listener, BmobIMUserInfo userInfo) {
        super(context, root, R.layout.recyclerview_item_received_text_chat, listener);
        this.mContext = context;
        this.mUserInfo = userInfo;
    }

    @Override
    public void bindData(Object o) {
        final BmobIMMessage message = (BmobIMMessage)o;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = dateFormat.format(message.getCreateTime());
        Log.e("truyayong", "time : " + time);
        tv_time.setText(time);
        Picasso.with(mContext).load(mUserInfo.getAvatar()).into(iv_avatar);
        String content =  message.getContent();
        tv_message.setText(content);
        //点击头像跳到用户
        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final User mUser = BmobUser.getCurrentUser(User.class);
                BmobQuery<User> query = new BmobQuery<User>();
                query.addWhereEqualTo("objectId", mUserInfo.getUserId());
                query.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> list, BmobException e) {
                        if (list.isEmpty()) {
                            Toast.makeText(mContext, "此用户不存在", Toast.LENGTH_SHORT).show();
                        } else if (mUser.getObjectId().equals(list.get(0).getObjectId())) {
                            //文章作者为本人
                            return;
                        } else {
                            Intent intent = new Intent(mContext, UserInfomationActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("com.truyayong.oldhouse.user.bean.user", list.get(0));
                            intent.putExtras(bundle);
                            mContext.startActivity(intent);
                        }
                    }
                });
            }
        });
        tv_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("点击"+message.getContent());
                if(onRecyclerViewListener!=null){
                    onRecyclerViewListener.onItemClick(getAdapterPosition());
                }
            }
        });

        tv_message.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onRecyclerViewListener != null) {
                    onRecyclerViewListener.onItemLongClick(getAdapterPosition());
                }
                return true;
            }
        });
    }

    public void showTime(boolean isShow) {
        tv_time.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
}
