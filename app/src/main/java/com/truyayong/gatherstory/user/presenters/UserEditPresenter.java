package com.truyayong.gatherstory.user.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.base.BasePresenter;
import com.truyayong.gatherstory.content.activities.IndexActivity;
import com.truyayong.gatherstory.user.activities.UserInfomationActivity;
import com.truyayong.gatherstory.user.bean.User;
import com.truyayong.gatherstory.user.view.IUserEditView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by alley_qiu on 2017/3/5.
 */

public class UserEditPresenter extends BasePresenter<IUserEditView> {

    private Context mContext;
    private static final int SAVE_MENU_ITEM_ID = 1;

    public static final int CHOOSE_HEAD_LOCAL = 0;
    public static final int CHOOSE_HEAD_TAKE = 1;
    public static final int CROP_SMALL_HEAD = 2;
    private Uri tempUri = null;
    private Boolean bUserGender = false;
    private String strUserHeadUrl = "";

    private User mUser = BmobUser.getCurrentUser(User.class);

    public UserEditPresenter(Context context) {
        this.mContext = context;
    }

    public void displayView() {
        Picasso.with(mContext.getApplicationContext()).load(mUser.getUserHeadUrl()).into(getView().getCIVHeadView());

        getView().getLLHeadView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseHead();
            }
        });
        getView().getETName().setText(mUser.getUserName());
        getView().getETDescripetion().setText(mUser.getUserDescription());
        getView().getETLocation().setText(mUser.getUserLocation());
        bUserGender = mUser.getGender();
        if (bUserGender == null || bUserGender == true) {
            getView().getRGGender().check(R.id.rb_male_user_edit);
        } else {
            getView().getRGGender().check(R.id.rb_female_user_edit);
        }
    }

    private void showChooseHead() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("设置头像");
        String[] items = {"选择本地图片", "拍照"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                switch (which) {
                    case CHOOSE_HEAD_LOCAL:
                        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        ((Activity)mContext).startActivityForResult(openAlbumIntent, CHOOSE_HEAD_LOCAL);
                        break;
                    case CHOOSE_HEAD_TAKE:
                        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(
                                Environment.getExternalStorageDirectory(), "image.jpg"));
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        ((Activity)mContext).startActivityForResult(openCameraIntent, CHOOSE_HEAD_TAKE);
                        break;
                }
            }
        });
        builder.create().show();
    }

    private void startImageZoom(final Uri uri) {
        Toast.makeText(mContext, "zoom", Toast.LENGTH_SHORT).show();
        if (uri == null) {
            throw new IllegalArgumentException("Uri can not be null");
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", "1");
        intent.putExtra("aspectY", "1");
        intent.putExtra("outputX", "150");
        intent.putExtra("outputY", "150");
        intent.putExtra("return-data", true);
        ((Activity)mContext).startActivityForResult(intent, CROP_SMALL_HEAD);
    }

    private void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            File file = new File(Environment.getExternalStorageDirectory(), "head.png");
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream stream = new FileOutputStream(file);
                if (photo.compress(Bitmap.CompressFormat.PNG, 90, stream)) {
                    stream.flush();
                    stream.close();
                }
                final BmobFile bmobFile = new BmobFile(file);
                bmobFile.upload(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            String url = bmobFile.getFileUrl();
                            strUserHeadUrl = bmobFile.getFileUrl();
                        }
                    }
                });
            } catch (FileNotFoundException e) {

            } catch (IOException e) {

            }
            getView().getCIVHeadView().setImageBitmap(photo);
        }
    }

    public void onActivityResult(int requestCode, Intent data) {
        switch (requestCode) {
            case CHOOSE_HEAD_LOCAL:
                startImageZoom(data.getData());
                break;
            case CHOOSE_HEAD_TAKE:
                startImageZoom(tempUri);
                break;
            case CROP_SMALL_HEAD:
                setImageToView(data);
                break;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem homeItem = menu.add(0, SAVE_MENU_ITEM_ID, 0, "首页");
        homeItem.setIcon(R.drawable.ic_send_white_24dp);
        homeItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    public void onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            ((Activity)mContext).finish();
        }
        if (item.getItemId() == SAVE_MENU_ITEM_ID) {
            String strUserName = getView().getETName().getText().toString();
            String strUserDescripe = getView().getETDescripetion().getText().toString();
            String strUserLocation = getView().getETLocation().getText().toString();
            if (mUser != null) {
                User newUser = new User();
                if (!"".equals(strUserName.trim())) {
                    newUser.setUserName(strUserName);
                }
                if (!"".equals(strUserDescripe.trim())) {
                    newUser.setUserDescription(strUserDescripe);
                }
                if (!"".equals(strUserLocation.trim())) {
                    newUser.setUserLocation(strUserLocation);
                }
                if (getView().getRGGender().getCheckedRadioButtonId() == R.id.rb_male_user_edit) {
                    bUserGender = true;
                } else {
                    bUserGender = false;
                }
                newUser.setGender(bUserGender);
                if (!"".equals(strUserHeadUrl.trim())) {
                    newUser.setUserHeadUrl(strUserHeadUrl);
                }
                newUser.update(mUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(mContext, "update success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(mContext, IndexActivity.class);
                            mContext.startActivity(intent);
                        } else {
                            Toast.makeText(mContext, "update fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

}
