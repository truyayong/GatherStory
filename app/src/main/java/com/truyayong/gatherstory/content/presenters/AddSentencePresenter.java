package com.truyayong.gatherstory.content.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.truyayong.gatherstory.Bus.RxBus;
import com.truyayong.gatherstory.Bus.events.UpdateSentenceEvent;
import com.truyayong.gatherstory.base.BasePresenter;
import com.truyayong.gatherstory.content.activities.ShowArticleActivity;
import com.truyayong.gatherstory.content.bean.Article;
import com.truyayong.gatherstory.content.bean.Sentence;
import com.truyayong.gatherstory.content.view.IAddSentenceView;
import com.truyayong.gatherstory.im.messages.UpdateSentenceMessage;
import com.truyayong.gatherstory.user.bean.User;
import com.truyayong.gatherstory.utils.DensityUtil;
import com.truyayong.searchmodule.lucene.LuceneUtils;

import net.bither.util.NativeUtil;

import org.apache.lucene.queryParser.ParseException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import rx.Subscriber;

/**
 * Created by alley_qiu on 2017/3/6.
 */

public class AddSentencePresenter extends BasePresenter<IAddSentenceView> {

    private Context mContext;
    private static final int CHOOSE_HEAD_LOCAL = 0;
    private static final int CHOOSE_HEAD_TAKE = 1;
    private static final int CROP_SMALL_HEAD = 2;
    private Uri tempUri = null;

    private static final String TAG = "AddSentencePresenter";

    public AddSentencePresenter(Context context) {
        this.mContext = context;
    }

    public void setupView() {
        getView().getREContent().setEditorHeight(200);
        getView().getREContent().setPlaceholder("在这里开始你的故事...");
        getView().getIBPhoto().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseHead();
            }
        });
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
            default:
                break;
        }
    }

    private void showChooseHead() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("插入图片");
        String[] items = {"选择本地图片", "拍照"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_HEAD_LOCAL:
                        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        ((Activity)mContext).startActivityForResult(openAlbumIntent, CHOOSE_HEAD_LOCAL);
                        break;
                    case CHOOSE_HEAD_TAKE:
                        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "image.jpg"));
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        ((Activity)mContext).startActivityForResult(openCameraIntent, CHOOSE_HEAD_TAKE);
                        break;
                }
            }
        });
        builder.create().show();
    }

    private void startImageZoom(final Uri uri) {
        if (uri == null) {
            throw new IllegalArgumentException("Uri can not be null");
        }
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = ((Activity)mContext).managedQuery(uri, proj, // Which
                // columns
                // to return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        String path = cursor.getString(column_index);
        cursor.close();
        Log.e("truyayong", TAG + " startImageZoom path : " + path);

        BitmapFactory.Options newOps = new BitmapFactory.Options();
        newOps.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, newOps);
        int dpWidth = DensityUtil.px2dp(mContext, DensityUtil.getViewWidth(getView().getREContent()));
        int dpHeight = DensityUtil.px2dp(mContext, DensityUtil.getViewHeight(getView().getREContent()));
        int rate = DensityUtil.caculateInSampleSize(newOps, dpWidth, dpHeight);
        newOps.inJustDecodeBounds = false;
        newOps.inSampleSize = rate;
        Log.e("truyayong", TAG + " startImageZoom inSampleSize : " + rate);
        Bitmap bitmap = BitmapFactory.decodeFile(path, newOps);
        File file = new File(Environment.getExternalStorageDirectory(), "temp1.png");
        try{
            if (!file.exists()) {
                file.createNewFile();
            }
            NativeUtil.compressBitmap(bitmap, 100, file.getAbsolutePath(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final BmobFile bmobFile = new BmobFile(file);
        bmobFile.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    String url = bmobFile.getFileUrl();
                    getView().getREContent().insertImage(url, "xiong");
                }
            }
        });
        bitmap.recycle();
    }

    private void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            File file = new File(Environment.getExternalStorageDirectory(), "temp1.png");
            try{
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream stream = new FileOutputStream(file);
                if (photo.compress(Bitmap.CompressFormat.PNG, 100, stream)) {
                    stream.flush();
                    stream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            final BmobFile bmobFile = new BmobFile(file);
            bmobFile.upload(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        String url = bmobFile.getFileUrl();
                        getView().getREContent().insertImage(url, "xiong");
                    }
                }
            });
        }
    }

    public void publishSentence(String title) {
        final String content = getView().getREContent().getHtml();
        final User mUser = BmobUser.getCurrentUser(User.class);
        final Sentence sentence = new Sentence();
        sentence.setContent(content);
        sentence.setAuthor(mUser);
        sentence.setTitle(title);
        sentence.setAuthorName(mUser.getUserName());
        sentence.setAuthorHeadUrl(mUser.getUserHeadUrl());
        sentence.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Intent tempIntent = new Intent(mContext, ShowArticleActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("new_item", sentence);
                    tempIntent.putExtras(bundle);
                    //发送文章更新事件
                    UpdateSentenceEvent event = new UpdateSentenceEvent();
                    event.setTitle(sentence.getTitle());
                    event.setContent(sentence.getContent());
                    sendUpdateSentenceEvent(event);

                    ((Activity)mContext).setResult(1, tempIntent);
                    ((Activity)mContext).finish();
                } else {
                    Toast.makeText(mContext, "发布节点失败", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //发送节点更新事件给关注文章的用户
    public void sendUpdateSentenceEvent(final UpdateSentenceEvent event) {
        Log.e("truyayong", event.getTitle());
        final BmobQuery<Article> query = new BmobQuery<Article>();
        query.addWhereEqualTo("title", event.getTitle());
        query.findObjects(new FindListener<Article>() {
            @Override
            public void done(List<Article> list, BmobException e) {
                if (e == null) {
                    final Article article = list.get(0);
                    BmobQuery<User> query1 = new BmobQuery<User>();
                    query1.addWhereRelatedTo("followUser", new BmobPointer(article));
                    query1.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> list, BmobException e) {
                            for (User u : list) {
                                //除了本人
                                if (u.getObjectId() != BmobUser.getCurrentUser(User.class).getObjectId()) {
                                    BmobIMUserInfo userInfo = new BmobIMUserInfo(u.getObjectId(), u.getUserName(), u.getUserHeadUrl());
                                    BmobIM.getInstance().startPrivateConversation(userInfo, new ConversationListener() {
                                        @Override
                                        public void done(BmobIMConversation bmobIMConversation, BmobException e) {
                                            if (e == null) {
                                                Log.e("truyayong", bmobIMConversation.toString());
                                                BmobIMConversation conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(),
                                                        bmobIMConversation);
                                                UpdateSentenceMessage msg = new UpdateSentenceMessage();
                                                Map<String, Object> map = new HashMap<String, Object>();
                                                map.put("title", event.getTitle());
                                                map.put("content", event.getContent());
                                                msg.setContent(event.getContent());
                                                msg.setExtraMap(map);
                                                msg.setMsgType(UpdateSentenceMessage.TYPE_UPDATE_SENTENCE_MSG);
                                                conversation.sendMessage(msg, new MessageSendListener() {
                                                    @Override
                                                    public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                                                        if (e == null) {
                                                            Log.e("truyayong", "send bmob message");
                                                        } else {
                                                            Log.e("truyayong", "exception : " + e.toString());
                                                        }

                                                    }
                                                });
                                            } else {
                                                Toast.makeText(mContext, "开启会话失败", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                                }

                            }

                        }
                    });
                } else {
                    Log.e("truyayong", "sendUpdateSentenceEvent error");
                }
            }
        });

    }

}
