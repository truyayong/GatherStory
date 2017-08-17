package com.truyayong.gatherstory.content.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.truyayong.gatherstory.base.BasePresenter;
import com.truyayong.gatherstory.content.activities.ShowArticleActivity;
import com.truyayong.gatherstory.content.bean.Article;
import com.truyayong.gatherstory.content.bean.Sentence;
import com.truyayong.gatherstory.content.view.IAddArticleView;
import com.truyayong.gatherstory.user.bean.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by alley_qiu on 2017/3/6.
 */

public class AddArticlePresenter extends BasePresenter<IAddArticleView> {

    private Context mContext;

    public AddArticlePresenter(Context context) {
        this.mContext = context;
    }

    public void setupView() {
        getView().getREContent().setEditorHeight(200);
        getView().getREContent().setPlaceholder("在这里开始你的故事...");
        getView().getETTitle().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 100) {
                    getView().getTVWarning().setText("字数超过100！");
                } else {
                    getView().getTVWarning().setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void publishArticle() {
        final String title = getView().getETTitle().getText().toString();
        final String content = getView().getREContent().getHtml();
        final User mUser = BmobUser.getCurrentUser(User.class);
        final Article article = new Article();
        article.setTitle(title);
        article.setAuthor(mUser);
        article.increment("followUserCount");
        BmobRelation relation = new BmobRelation();
        relation.add(mUser);
        article.setFollowUser(relation);

        article.setContent(content);
        article.setAuthorHeadUrl(mUser.getUserHeadUrl());
        article.setAuthorName(mUser.getUserName());
        article.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {

                    Sentence articleItem = new Sentence();
                    articleItem.setAuthor(mUser);
                    articleItem.setArticle(article);
                    articleItem.setTitle(article.getTitle());
                    articleItem.setContent(content);
                    articleItem.setAuthorName(mUser.getUserName());
                    articleItem.setAuthorHeadUrl(mUser.getUserHeadUrl());
                    articleItem.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                User user = new User();
                                BmobRelation followArticle = new BmobRelation();
                                followArticle.add(article);
                                user.setFollowArticle(followArticle);
                                user.increment("followArticleCount");
                                user.update(mUser.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        Intent intent = new Intent(mContext, ShowArticleActivity.class);
                                        intent.putExtra("title", title);
                                        intent.putExtra("content", content);
                                        mContext.startActivity(intent);
                                        ((Activity)mContext).finish();
                                    }
                                });

                            } else {
                                Toast.makeText(mContext, "发布失败2", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(mContext, "发布失败1", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
