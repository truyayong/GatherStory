package com.truyayong.gatherstory.content.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.content.bean.Sentence;
import com.truyayong.gatherstory.user.activities.UserInfomationActivity;
import com.truyayong.gatherstory.user.bean.User;
import com.truyayong.richtext.richview.RichView;

import org.apache.lucene.search.Query;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import rx.Subscriber;

/**
 * Created by alley_qiu on 2017/3/6.
 */

public class ShowArticleAdapter extends RecyclerView.Adapter<ShowArticleAdapter.Holder> {

    private final int TYPE_HEAD = 0;
    private final int TYPE_NORMAL = 1;
    private final int TYPE_TAIL = 2;

    private Context mContext;
    private View headView;
    private View tailView;

    private List<Sentence> datas;

    public ShowArticleAdapter(Context context, View headView, View tailView, List<Sentence> datas) {
        this.mContext = context;
        this.headView = headView;
        this.tailView = tailView;
        this.datas = datas;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (headView != null && viewType == TYPE_HEAD) {
            return new Holder(headView);
        }

        if (tailView != null && viewType == TYPE_TAIL) {
            return new Holder(tailView);
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item_show_article, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {

        if (getItemViewType(position) == TYPE_HEAD) {
            return;
        }

        if(getItemViewType(position) == TYPE_TAIL) {
            return;
        }


        int pos = getRealPosition(holder);
        final Sentence articleItem = datas.get(pos);
        if (holder instanceof Holder) {
            Holder h = (Holder) holder;
            Picasso.with(mContext).load(articleItem.getAuthorHeadUrl()).into(h.civ_author_icon_show_article);
            h.tv_author_name_show_article.setText(articleItem.getAuthorName());
            h.ll_author_info_show_article.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BmobQuery<User> query = new BmobQuery<User>();
                    query.addWhereEqualTo("objectId", articleItem.getAuthor().getObjectId());
                    query.findObjectsObservable(User.class).subscribe(new Subscriber<List<User>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable throwable) {

                        }

                        @Override
                        public void onNext(List<User> users) {
                            User mUser = BmobUser.getCurrentUser(User.class);
                            if (users.isEmpty()) {
                                Toast.makeText(mContext, "此用户不存在", Toast.LENGTH_SHORT).show();
                            } else if (mUser.getObjectId().equals(users.get(0).getObjectId())) {
                                //文章作者为本人
                                return;
                            } else {
                                Intent intent = new Intent(mContext, UserInfomationActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("com.truyayong.oldhouse.user.bean.user", users.get(0));
                                intent.putExtras(bundle);
                                mContext.startActivity(intent);
                            }

                        }
                    });

                }
            });

            RichView.from(articleItem.getContent()).clickable(false).into(h.tv_sentence_content_show_article);

        }
    }

    @Override
    public int getItemCount() {
        int count = datas.size();
        if (headView != null) {
            count++;
        }
        if (tailView != null) {
            count++;
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (headView == null) return TYPE_NORMAL;
        if (position == 0) return TYPE_HEAD;
        if (position == getItemCount() - 1) return TYPE_TAIL;
        return TYPE_NORMAL;
    }

    class Holder extends RecyclerView.ViewHolder {

        @Bind(R.id.ll_author_info_show_article)
        LinearLayout ll_author_info_show_article;
        @Bind(R.id.civ_author_icon_show_article)
        ImageView civ_author_icon_show_article;
        @Bind(R.id.tv_author_name_show_article)
        TextView tv_author_name_show_article;
        @Bind(R.id.tv_sentence_content_show_article)
        TextView tv_sentence_content_show_article;

        public Holder(View itemView) {
            super(itemView);
            if (itemView == headView) return;
            if (itemView == tailView) return;
            ButterKnife.bind(this, itemView);
        }
    }

    private int getRealPosition(RecyclerView.ViewHolder holder) {
        int pos = holder.getLayoutPosition();
        return headView == null ? pos : pos - 1;
    }

    public void add(Sentence item) {
        datas.add(item);
        notifyDataSetChanged();
    }

    public void add(List<Sentence> items) {
        datas.addAll(0,items);
        notifyDataSetChanged();
    }
}
