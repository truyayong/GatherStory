package com.truyayong.gatherstory.content.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.content.activities.ShowArticleActivity;
import com.truyayong.gatherstory.content.bean.Article;
import com.truyayong.gatherstory.content.widget.ListItemMenu;
import com.truyayong.gatherstory.user.activities.UserInfomationActivity;
import com.truyayong.gatherstory.user.bean.User;
import com.truyayong.gatherstory.utils.DensityUtil;
import com.truyayong.richtext.richview.RichView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscriber;

/**
 * Created by alley_qiu on 2017/3/6.
 */

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.Holder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_NORMAL = 1;
    private View headView;

    private List<Article> datas = new ArrayList<>();
    private Context mContext;

    private int menuW, menuH;

    public HomeRecyclerViewAdapter(Context mContext, List<Article> datas) {
        this.datas = datas;
        this.mContext = mContext;
        DisplayMetrics display = new DisplayMetrics();
        Activity mActivity = (Activity) mContext;
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(display);
        menuW = display.widthPixels / 2;
        menuH = LinearLayout.LayoutParams.WRAP_CONTENT;

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (headView != null && viewType == TYPE_HEADER) {
            return new Holder(headView);
        }

        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item_fragment_home, null);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        if (headView == null) return TYPE_NORMAL;
        if (position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            return;
        }

        final int pos = getRealPosition(holder);

        final Article article = datas.get(pos);
        holder.tv_user_name_home.setText(article.getAuthorName());
        Picasso.with(mContext).load(article.getAuthorHeadUrl()).placeholder(R.drawable.profile).into(holder.civ_head_view_home);
        holder.tv_article_title_home.setText(article.getTitle());
        RichView.from(article.getContent()).clickable(false).into(holder.tv_article_content_home);
        holder.tv_favorite_home.setText("" + article.getFavoriteCount() + " 赞");
        holder.tv_follow_home.setText("" + article.getFollowUserCount() + " 关注");
        holder.ll_article_info_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShowArticleActivity.class);
                intent.putExtra("title", article.getTitle());
                mContext.startActivity(intent);
            }
        });

        holder.rl_author_info_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobQuery<User> query = new BmobQuery<User>();
                query.addWhereEqualTo("objectId", article.getAuthor().getObjectId());
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

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListItemMenu menu = new ListItemMenu(menuW, menuH, mContext);
                menu.update();
                int offx = DensityUtil.dp2px(mContext, 24);
                int offy = DensityUtil.dp2px(mContext, 24);
                menu.setAnimationStyle(R.style.MenuAnim);
                menu.showAsDropDown(holder.menu, -menuW + offx, -offy);
            }
        });

        if (position % 3 == 1) {
            holder.ll_form_info_home.setBackgroundColor(Color.parseColor("#bcaaa4"));
        }
    }

    @Override
    public int getItemCount() {
        return headView == null ? datas.size() : datas.size() + 1;
    }


    public class Holder extends RecyclerView.ViewHolder {


        @Bind(R.id.ll_form_info_home)
        LinearLayout ll_form_info_home;
        @Bind(R.id.ll_article_form_info_home)
        LinearLayout ll_article_form_info_home;
        @Bind(R.id.rl_author_info_home)
        View rl_author_info_home;
        @Bind(R.id.civ_head_view_home)
        CircleImageView civ_head_view_home;
        @Bind(R.id.tv_user_name_home)
        TextView tv_user_name_home;
        @Bind(R.id.ll_article_info_home)
        LinearLayout ll_article_info_home;
        @Bind(R.id.tv_article_title_home)
        TextView tv_article_title_home;
        @Bind(R.id.tv_article_content_home)
        TextView tv_article_content_home;
        @Bind(R.id.tv_favorite_home)
        TextView tv_favorite_home;
        @Bind(R.id.tv_follow_home)
        TextView tv_follow_home;
        @Bind(R.id.menu)
        ImageView menu;


        public Holder(View itemView) {
            super(itemView);
            if (itemView == headView) return;
            ButterKnife.bind(this, itemView);
        }
    }

    public void setHeadView(View view) {
        headView = view;
        notifyItemInserted(0);
    }

    private int getRealPosition(RecyclerView.ViewHolder holder) {
        int pos = holder.getLayoutPosition();
        return headView == null ? pos : pos - 1;
    }

    public void add(Article item) {
        this.datas.add(0, item);
        notifyDataSetChanged();
    }
    public void add(List<Article> items) {
        this.datas.addAll(0, items);
        notifyDataSetChanged();
    }
}

