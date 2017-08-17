package com.truyayong.gatherstory.content.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.base.MVPBaseAppCompatActivity;
import com.truyayong.gatherstory.content.presenters.AddSentencePresenter;
import com.truyayong.gatherstory.content.view.IAddSentenceView;

import butterknife.Bind;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import jp.wasabeef.richeditor.RichEditor;

public class AddSentenceActivity extends MVPBaseAppCompatActivity<IAddSentenceView, AddSentencePresenter> implements IAddSentenceView {

    private static final int PUBLISH_MENU_ITEM_ID = 1;

    @Bind(R.id.rich_editor_add_sentence)
    RichEditor rich_editor_add_sentence;
    @Bind(R.id.ib_setting_add_sentence)
    ImageButton ib_setting_add_sentence;
    @Bind(R.id.ib_linkg_add_sentence)
    ImageButton ib_linkg_add_sentence;
    @Bind(R.id.ib_photo_add_sentence)
    ImageButton ib_photo_add_sentence;

    private Intent intent;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.add(0, PUBLISH_MENU_ITEM_ID, 0, "发布");
        menuItem.setIcon(R.drawable.ic_send_white_24dp);
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(0, intent);
            finish();
        }

        if (item.getItemId() == PUBLISH_MENU_ITEM_ID) {
            String title = intent.getStringExtra("title");
            mPresenter.publishSentence(title);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mPresenter.onActivityResult(requestCode, data);
        }
    }

    @Override
    protected AddSentencePresenter createPresenter() {
        return new AddSentencePresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_add_sentence;
    }

    @Override
    public RichEditor getREContent() {
        return rich_editor_add_sentence;
    }

    @Override
    public ImageButton getIBSetting() {
        return ib_setting_add_sentence;
    }

    @Override
    public ImageButton getIBLink() {
        return ib_linkg_add_sentence;
    }

    @Override
    public ImageButton getIBPhoto() {
        return ib_photo_add_sentence;
    }
}
