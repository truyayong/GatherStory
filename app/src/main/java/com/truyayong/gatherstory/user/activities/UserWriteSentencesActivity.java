package com.truyayong.gatherstory.user.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.base.MVPBaseAppCompatActivity;
import com.truyayong.gatherstory.user.presenters.WriteSentencesPresenter;
import com.truyayong.gatherstory.user.view.IWriteSentencesView;

import butterknife.Bind;

public class UserWriteSentencesActivity extends MVPBaseAppCompatActivity<IWriteSentencesView, WriteSentencesPresenter> implements IWriteSentencesView {

    @Bind(R.id.rv_list_write_sentences)
    RecyclerView rv_list_write_sentences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.setupView();
    }

    @Override
    protected WriteSentencesPresenter createPresenter() {
        return new WriteSentencesPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_user_write_sentences;
    }

    @Override
    public RecyclerView getRVArticles() {
        return rv_list_write_sentences;
    }
}
