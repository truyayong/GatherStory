package com.truyayong.gatherstory.content.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.base.MVPBaseAppCompatActivity;
import com.truyayong.gatherstory.content.presenters.AddArticlePresenter;
import com.truyayong.gatherstory.content.view.IAddArticleView;

import butterknife.Bind;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import jp.wasabeef.richeditor.RichEditor;

public class AddArticleActivity extends MVPBaseAppCompatActivity<IAddArticleView, AddArticlePresenter> implements IAddArticleView {

    private static final int PUBLISH_MENU_ITEM_ID = 1;
    private Intent intent;

    @Bind(R.id.et_title_add_article)
    EditText et_title_add_article;
    @Bind(R.id.tv_title_warning_add_article)
    TextView tv_title_warning_add_article;
    @Bind(R.id.rich_editor_add_article)
    RichEditor rich_editor_add_article;
    @Bind(R.id.ib_setting_add_article)
    ImageButton ib_setting_add_article;
    @Bind(R.id.ib_link_add_article)
    ImageButton ib_link_add_article;
    @Bind(R.id.ib_photo_add_article)
    ImageButton ib_photo_add_article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("新文章");
        intent = getIntent();
        mPresenter.setupView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(0, intent);
            finish();
        }

        if (item.getItemId() == PUBLISH_MENU_ITEM_ID) {
            mPresenter.publishArticle();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.add(0, PUBLISH_MENU_ITEM_ID, 0, "发布");
        menuItem.setIcon(R.drawable.ic_send_white_24dp);
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected AddArticlePresenter createPresenter() {
        return new AddArticlePresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_add_article;
    }

    @Override
    public EditText getETTitle() {
        return et_title_add_article;
    }

    @Override
    public TextView getTVWarning() {
        return tv_title_warning_add_article;
    }

    @Override
    public RichEditor getREContent() {
        return rich_editor_add_article;
    }

    @Override
    public ImageButton getIBSetting() {
        return ib_setting_add_article;
    }

    @Override
    public ImageButton getIBLink() {
        return ib_link_add_article;
    }

    @Override
    public ImageButton getIBPhoto() {
        return ib_photo_add_article;
    }
}
