package com.truyayong.gatherstory.im.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.truyayong.gatherstory.Bus.RxBus;
import com.truyayong.gatherstory.Bus.events.UpdateSentenceEvent;
import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.im.adapter.ChatAdapter;
import com.truyayong.gatherstory.im.adapter.OnRecyclerViewListener;
import com.truyayong.gatherstory.im.messages.UpdateArticleMessage;
import com.truyayong.gatherstory.im.messages.UpdateSentenceMessage;
import com.truyayong.gatherstory.user.bean.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageListHandler;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import rx.Subscriber;

public class ChatActivity extends AppCompatActivity implements MessageListHandler {

    @Bind(R.id.ll_chat)
    LinearLayout ll_chat;

    @Bind(R.id.sw_refresh)
    SwipeRefreshLayout sw_refresh;

    @Bind(R.id.rc_view)
    RecyclerView rc_view;

    @Bind(R.id.edit_msg)
    EditText edit_msg;


    @Bind(R.id.btn_chat_keyboard)
    Button btn_chat_keyboard;
    @Bind(R.id.btn_chat_send)
    Button btn_chat_send;

    @Bind(R.id.layout_more)
    LinearLayout layout_more;
    @Bind(R.id.layout_add)
    LinearLayout layout_add;
    @Bind(R.id.layout_emo)
    LinearLayout layout_emo;

    ChatAdapter adapter;
    protected LinearLayoutManager layoutManager;
    BmobIMConversation conversation;
    BmobIMUserInfo mUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(),
                (BmobIMConversation)intent.getExtras().get("com.truyayong.oldhouse.BmobIMConversation"));
        mUserInfo = (BmobIMUserInfo)intent.getExtras().get("cn.bmob.newim.bean.BmobIMUserInfo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(mUserInfo.getName());
        initBottomView();
        initSwipeLayout();
        Log.e("truyayong", "ChatActivity");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initBottomView(){
        edit_msg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN||event.getAction()==MotionEvent.ACTION_UP){
                    scrollToBottom();
                }
                return false;
            }
        });
        edit_msg.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                scrollToBottom();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    btn_chat_send.setVisibility(View.VISIBLE);
                    btn_chat_keyboard.setVisibility(View.GONE);
                } else {
                    btn_chat_send.setVisibility(View.GONE);
                    btn_chat_keyboard.setVisibility(View.GONE);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void scrollToBottom() {
        layoutManager.scrollToPositionWithOffset(adapter.getItemCount() - 1, 0);
    }

    private void initSwipeLayout() {
        sw_refresh.setEnabled(true);
        layoutManager = new LinearLayoutManager(this);
        rc_view.setLayoutManager(layoutManager);
        adapter = new ChatAdapter(this,conversation);
        adapter.setUserInfo(mUserInfo);
        rc_view.setAdapter(adapter);
        ll_chat.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ll_chat.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                sw_refresh.setRefreshing(true);
                //自动刷新
                queryMessages(null);
            }
        });
        //下拉加载
        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BmobIMMessage msg = adapter.getFirstMessage();
                queryMessages(msg);
            }
        });
        //设置RecyclerView的点击事件
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                Log.e("truyayong", ""+position);
            }

            @Override
            public boolean onItemLongClick(int position) {
                //这里省了个懒，直接长按就删除了该消息
                conversation.deleteMessage(adapter.getItem(position));
                adapter.remove(position);
                return true;
            }
        });
    }

    @OnClick(R.id.edit_msg)
    public void onEditClick(View view){
        if (layout_more.getVisibility() == View.VISIBLE) {
            layout_add.setVisibility(View.GONE);
            layout_emo.setVisibility(View.GONE);
            layout_more.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btn_chat_keyboard)
    public void onKeyClick(View view){
        showEditState(false);
    }

    @OnClick(R.id.btn_chat_send)
    public void onSendClick(View view){
        sendMessage();
    }

    /**
     * 根据是否点击笑脸来显示文本输入框的状态
     * @param  isEmo 用于区分文字和表情
     * @return void
     */
    private void showEditState(boolean isEmo) {
        edit_msg.setVisibility(View.VISIBLE);
        btn_chat_keyboard.setVisibility(View.GONE);
        edit_msg.requestFocus();
        if (isEmo) {
            layout_more.setVisibility(View.VISIBLE);
            layout_more.setVisibility(View.VISIBLE);
            layout_emo.setVisibility(View.VISIBLE);
            layout_add.setVisibility(View.GONE);
            hideSoftInputView();
        } else {
            layout_more.setVisibility(View.GONE);
            showSoftInputView();
        }
    }

    /**首次加载，可设置msg为null，下拉刷新的时候，默认取消息表的第一个msg作为刷新的起始时间点，默认按照消息时间的降序排列
     * @param msg
     */
    public void queryMessages(BmobIMMessage msg){
        conversation.queryMessages(msg, 10, new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {
                sw_refresh.setRefreshing(false);
                if (e == null) {
                    List<BmobIMMessage> chatEvents = new ArrayList<BmobIMMessage>();
                    if (null != list) {
                        for (BmobIMMessage msg : list) {
                            //过滤掉节点更新消息
                            if (!UpdateSentenceMessage.TYPE_UPDATE_SENTENCE_MSG.equals(msg.getMsgType())) {
                                chatEvents.add(msg);
                            } else {
                                UpdateSentenceEvent event = new UpdateSentenceEvent();
                                event.setTitle(msg.getExtra());
                                event.setContent(msg.getContent());
                                RxBus.getInstance().post(event);
                            }
                        }
                    }
                    if (null != list && chatEvents.size() > 0) {
                        adapter.addMessages(chatEvents);
                        layoutManager.scrollToPositionWithOffset(chatEvents.size() - 1, 0);
                    }
                } else {
                    Toast.makeText(ChatActivity.this, e.getMessage() + "(" + e.getErrorCode() + ")", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onMessageReceive(List<MessageEvent> list) {
        Log.e("truyayong", "聊天页面接收到消息：" + list.size());
        //当注册页面消息监听时候，有消息（包含离线消息）到来时会回调该方法
        //过滤掉文章更新消息
        List<MessageEvent> chatEvents = new ArrayList<>();
        for (MessageEvent e : list) {
            if (!UpdateSentenceMessage.TYPE_UPDATE_SENTENCE_MSG.equals(e.getMessage().getMsgType())) {
                chatEvents.add(e);
            }
        }
        for (int i=0;i<chatEvents.size();i++){
            addMessage2Chat(chatEvents.get(i));
        }
    }

    /**添加消息到聊天界面中
     * @param event
     */
    private void addMessage2Chat(MessageEvent event){
        BmobIMMessage msg =event.getMessage();
        if(conversation!=null && event!=null && conversation.getConversationId().equals(event.getConversation().getConversationId()) //如果是当前会话的消息
                && !msg.isTransient()){//并且不为暂态消息
            if(adapter.findPosition(msg)<0){//如果未添加到界面中
                Log.e("truyayong", "addMessage2Chat");
                adapter.addMessage(msg);
                //更新该会话下面的已读状态
                conversation.updateReceiveStatus(msg);
            }
            scrollToBottom();
        }else{
            Log.e("truyayong", "不是与当前聊天对象的消息");
        }
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 显示软键盘
     */
    public void showSoftInputView() {
        if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .showSoftInput(edit_msg, 0);
        }
    }

    public MessageSendListener listener = new MessageSendListener() {
        @Override
        public void done(BmobIMMessage bmobIMMessage, BmobException e) {
            if (e == null) {
                Log.e("truyayong", "MessageSendListener : " + bmobIMMessage.getMsgType());
                adapter.addMessage(bmobIMMessage);
                edit_msg.setText("");
                scrollToBottom();
            } else {
                Toast.makeText(ChatActivity.this, "MessageSendListener exception : " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 发送文本消息
     */
    private void sendMessage(){
        String text=edit_msg.getText().toString();
        if(TextUtils.isEmpty(text.trim())){
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }
        BmobIMTextMessage msg =new BmobIMTextMessage();
        msg.setContent(text);
        //可设置额外信息
        Map<String,Object> map =new HashMap<>();
        User user = BmobUser.getCurrentUser(User.class);
        map.put("username", user.getUserName());//随意增加信息
        map.put("useravtar", user.getUserHeadUrl());
        msg.setExtraMap(map);
        conversation.sendMessage(msg, listener);
    }

    /**
     * 直接发送远程图片地址
     */
    public void sendRemoteImageMessage(){
        BmobIMImageMessage image =new BmobIMImageMessage();
        image.setRemoteUrl("http://img.lakalaec.com/ad/57ab6dc2-43f2-4087-81e2-b5ab5681642d.jpg");
        conversation.sendMessage(image, listener);
    }

    /**
     * 发送本地图片地址
     */
    public void sendLocalImageMessage(){
        //正常情况下，需要调用系统的图库或拍照功能获取到图片的本地地址，开发者只需要将本地的文件地址传过去就可以发送文件类型的消息
        BmobIMImageMessage image =new BmobIMImageMessage("/storage/emulated/0/bimagechooser/IMG_20160302_172003.jpg");
        conversation.sendMessage(image, listener);
//        //因此也可以使用BmobIMFileMessage来发送文件消息
//        BmobIMFileMessage file =new BmobIMFileMessage("文件地址");
//        c.sendMessage(file,listener);
    }

    @Override
    protected void onResume() {
        //锁屏期间的收到的未读消息需要添加到聊天界面中
        addUnReadMessage();
        //添加页面消息监听器
        BmobIM.getInstance().addMessageListHandler(this);
        // 有可能锁屏期间，在聊天界面出现通知栏，这时候需要清除通知
        BmobNotificationManager.getInstance(this).cancelNotification();
        super.onResume();
    }

    /**
     * 添加未读的通知栏消息到聊天界面
     */
    private void addUnReadMessage(){
        List<MessageEvent> cache = BmobNotificationManager.getInstance(this).getNotificationCacheList();
        if(cache.size()>0){
            int size =cache.size();
            for(int i=0;i<size;i++){
                MessageEvent event = cache.get(i);
                addMessage2Chat(event);
            }
        }
        scrollToBottom();
    }

    @Override
    protected void onPause() {
        //移除页面消息监听器
        BmobIM.getInstance().removeMessageListHandler(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        //更新此会话的所有消息为已读状态
        if(conversation!=null){
            conversation.updateLocalCache();
        }
        hideSoftInputView();
        super.onDestroy();
    }

}
