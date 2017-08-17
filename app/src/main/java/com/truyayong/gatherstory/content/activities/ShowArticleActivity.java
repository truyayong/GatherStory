package com.truyayong.gatherstory.content.activities;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.base.MVPBaseAppCompatActivity;
import com.truyayong.gatherstory.base.MVPBaseFragmentActivity;
import com.truyayong.gatherstory.content.presenters.ShowArticlePresenter;
import com.truyayong.gatherstory.content.view.IShowArticleView;

import butterknife.Bind;

public class ShowArticleActivity extends MVPBaseFragmentActivity<IShowArticleView, ShowArticlePresenter> implements IShowArticleView {

    private Intent intent;

    @Bind(R.id.pb_process_show_article)
    ProgressBar pb_process_show_article;
    @Bind(R.id.swipe_refresh_show_article)
    SwipeRefreshLayout swipe_refresh_show_article;
    @Bind(R.id.recyclerView_show_article)
    RecyclerView recyclerView_show_article;
    @Bind(R.id.ib_add_sentence_show_article)
    ImageButton ib_add_sentence_show_article;
    @Bind(R.id.ib_article_follow_show_article)
    ImageButton ib_article_follow_show_article;
    @Bind(R.id.ib_article_favorite_show_article)
    ImageButton ib_article_favorite_show_article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        mPresenter.displayView(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            mPresenter.onActivityResult(data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    protected ShowArticlePresenter createPresenter() {
        return new ShowArticlePresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_show_article;
    }

    @Override
    public ProgressBar getPBProcess() {
        return pb_process_show_article;
    }

    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipe_refresh_show_article;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView_show_article;
    }

    @Override
    public ImageButton getIBAddSentence() {
        return ib_add_sentence_show_article;
    }

    @Override
    public ImageButton getIBFollowArticle() {
        return ib_article_follow_show_article;
    }

    @Override
    public ImageButton getIBFavoriteArticle() {
        return ib_article_favorite_show_article;
    }
}
