package family.momo.com.family.chat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import family.momo.com.family.Database.Bean_Chat_Msg;
import family.momo.com.family.Database.Helper_Chat_Msg;
import family.momo.com.family.DefinedViews.SoftKeyboardStateHelper;
import family.momo.com.family.GetMsgService;
import family.momo.com.family.R;
import family.momo.com.family.util.PictureUtil;
import family.momo.com.family.util.ScreenUtil;
import family.momo.com.family.util.SoftHideKeyBoardUtil;
import family.momo.com.family.util.VariableDataUtil;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;

import static family.momo.com.family.util.PictureUtil.PHOTO_REQUEST_CAREMA;
import static family.momo.com.family.util.PictureUtil.getCamere;
import static family.momo.com.family.util.PictureUtil.getCrop;
import static family.momo.com.family.util.PictureUtil.getGallery;
import static family.momo.com.family.util.VariableDataUtil.MYID;

public class Chat_msg extends AppCompatActivity {
    public static Activity chatActivity;
    //Intent变量
    private Intent sendMsg_Android_ServiceIntent  = new Intent(VariableDataUtil.Code_Filter_From_Android_Activity);
    private Intent ServiceIntent ;

    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    //身份变量
    String OTHERID = "666";//聊天对象身份
    boolean isgroup = false;

    //自写类对象
    private MsgAdapter adapter;
    private MsgReceiver msgReceiver;

    //组件变量
    private RelativeLayout chat_msg_root;
    private EditText chat_msg_edt_input;
    private Button chat_msg_btn_send;
    private RecyclerView msgRecyclerView;
    LinearLayoutManager layoutManager;

    private ImageView chat_msg_img_attach;
    private ImageView chat_msg_actionbar_img_headimg;
    private ImageView chat_msg_actionbar_img_right;
    private TextView chat_msg_actionbar_txt_title;
    LinearLayout chat_msg_layout_video,chat_msg_layout_voice,chat_msg_layout_photo,chat_msg_layout_picture;
    LinearLayout chat_msg_layout2;
    RelativeLayout chat_msg_relative;
    TextView chat_msg_style_txt_name_left;


    //组件需求对象
    private List<Msg> msgList = new ArrayList<Msg>();
    int stausCode = 1;
    boolean iskeyboard = false;

    //视频


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_msg);

        chatActivity = this;
        setActionBar();

        initViews();//初始化组件

        setUserInfo();//设置用户身份
        initMsgs();// 初始化消息数据
        getMsgs();
        setListenerFotEditText(chat_msg_edt_input);
        registBroadcast();//注册消息

        openService();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (getGallery(data) == null){
                Toast.makeText(Chat_msg.this,"没有选择图片",Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == PHOTO_REQUEST_CAREMA) {
            if (getCamere(data) == null){
                Toast.makeText(Chat_msg.this,"没有拍照",Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == PHOTO_REQUEST_CUT) {
            getCrop(data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onDestroy() {
        unregisterReceiver(msgReceiver);
        super.onDestroy();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {


            // 获取当前获得当前焦点所在View
            View view = getCurrentFocus();
            if (isClickEt(view, event) && (view instanceof TextView)) {


                // 如果不是edittext，则隐藏键盘


                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    // 隐藏键盘
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                if (chat_msg_layout2.getVisibility() == View.VISIBLE){
                    msgRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
                    chat_msg_relative.getLayoutParams().height = ((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics()));;
                    chat_msg_layout2.setVisibility(View.GONE);
                    stausCode *=-1;
                }
            }
            return super.dispatchTouchEvent(event);
        }
/**
 * 看源码可知superDispatchTouchEvent 是个抽象方法，用于自定义的Window
 * 此处目的是为了继续将事件由dispatchTouchEvent(MotionEvent
 * event)传递到onTouchEvent(MotionEvent event) 必不可少，否则所有组件都不能触发
 * onTouchEvent(MotionEvent event)
 */
        if (getWindow().superDispatchTouchEvent(event)) {
            return true;
        }
        return onTouchEvent(event);
    }



    //设置Actionbar和状态栏
    private void setActionBar(){
        View decorView = getWindow().getDecorView();
        //decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);//隐藏状态栏
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getSupportActionBar().hide();//隐藏Actionbar
        SoftHideKeyBoardUtil.assistActivity(this);
    }
    private void initViews(){
        chat_msg_style_txt_name_left = findViewById(R.id.chat_msg_style_txt_name_left);
        chat_msg_root = findViewById(R.id.chat_msg_root);
        chat_msg_edt_input = (EditText) findViewById(R.id.chat_msg_edt_input);

        chat_msg_edt_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==0){
                    chat_msg_btn_send.setVisibility(View.GONE);
                    chat_msg_img_attach.setVisibility(View.VISIBLE);
                }else {
                    chat_msg_btn_send.setVisibility(View.VISIBLE);
                    chat_msg_img_attach.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        chat_msg_btn_send = (Button) findViewById(R.id.chat_msg_btn_send);
        chat_msg_btn_send.setOnClickListener(new click());
        chat_msg_layout2 = findViewById(R.id.chat_msg_layout2);
        chat_msg_relative = findViewById(R.id.chat_msg_relative);
        chat_msg_img_attach = findViewById(R.id.chat_msg_img_attach);
        chat_msg_img_attach.setOnClickListener(new click());

        //底部更多块
        chat_msg_layout_photo = findViewById(R.id.chat_msg_layout_photo);
        chat_msg_layout_photo.setOnClickListener(new click());
        chat_msg_layout_picture = findViewById(R.id.chat_msg_layout_picture);
        chat_msg_layout_picture.setOnClickListener(new click());
        chat_msg_layout_voice = findViewById(R.id.chat_msg_layout_voice);
        chat_msg_layout_voice.setOnClickListener(new click());
        chat_msg_layout_video = findViewById(R.id.chat_msg_layout_video);
        chat_msg_layout_video.setOnClickListener(new click());

        //actionbar
        chat_msg_actionbar_img_headimg = findViewById(R.id.chat_msg_actionbar_img_headimg);
        chat_msg_actionbar_img_headimg.setOnClickListener(new click());
        chat_msg_actionbar_img_right = findViewById(R.id.chat_msg_actionbar_img_right);
        chat_msg_actionbar_img_right.setOnClickListener(new click());
        chat_msg_actionbar_txt_title = findViewById(R.id.chat_msg_actionbar_txt_title);

        msgRecyclerView = (RecyclerView) findViewById(R.id.chat_msg_list_msgs);
        layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        adapter.setOnItemClickListener(new MsgAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View v, List<Msg> msgs, int position) {
                if (isgroup){
                    //viewName可区分item及item内部控件
                    switch (v.getId()){
                        case R.id.chat_msg_style_img_headimg_left:
                            Intent intent = new Intent(Chat_msg.this,Chat_msg_members_users.class);
                            String name = msgs.get(position).getName();
                            intent.putExtra("otherid",name);
                            intent.putExtra("isderectlygroup",true);
                            startActivity(intent);
                            break;
                        default:
                            break;
                    }
                }
            }

        });
        adapter.setOnItemLongClickListener(new MsgAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View v, List<Msg> msgs, int position) {
                switch (v.getId()){
                    case R.id.chat_msg_style_txt_msg_left:
                        Toast.makeText(Chat_msg.this,msgs.get(position).getContent(),Toast.LENGTH_LONG).show();
                    break;
                }
            }
        });
        msgRecyclerView.setAdapter(adapter);


    }
    private void registBroadcast(){
        msgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(VariableDataUtil.Code_Filter_From_Android_Service);
        registerReceiver(msgReceiver,intentFilter);
    }
    private void openService(){
        ServiceIntent = new Intent(this,GetMsgService.class);
        startService(ServiceIntent);
    }
    private void initMsgs() {

            Msg msg1 = new Msg("Hello guy.","秋妍", Msg.TYPE_RECEIVED,isgroup);
            msgList.add(msg1);
            msgList.add(msg1);
            msgList.add(msg1);
            msgList.add(msg1);
            msgList.add(msg1);
            msgList.add(msg1);
            msgList.add(msg1);
            msgList.add(msg1);
            Msg msg2 = new Msg("H",MYID, Msg.TYPE_SENT,isgroup);
            msgList.add(msg2);
            Msg msg3 = new Msg("This is Tom. Nice talking to you. ","里洁", Msg.TYPE_RECEIVED,isgroup);
            msgList.add(msg3);

            msgRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }
    private void sendMsgToService(Object obj){
        sendMsg_Android_ServiceIntent.putExtra("msg",(String)obj);
        sendBroadcast(sendMsg_Android_ServiceIntent);

    }
    private void setUserInfo(){
        Intent intent = this.getIntent();
        OTHERID = intent.getStringExtra("otherid");
        isgroup = intent.getBooleanExtra("isgroup",false);
        chat_msg_actionbar_txt_title.setText(OTHERID);
        if (isgroup){
            chat_msg_actionbar_img_right.setVisibility(View.VISIBLE);
        }else {
            chat_msg_actionbar_img_right.setVisibility(View.INVISIBLE);
        }
    }
    private void setListenerFotEditText(View view){
//        SoftKeyboardStateHelper softKeyboardStateHelper = new SoftKeyboardStateHelper(view);
//        softKeyboardStateHelper.addSoftKeyboardStateListener(new SoftKeyboardStateHelper.SoftKeyboardStateListener() {
//            @Override
//            public void onSoftKeyboardOpened(int keyboardHeightInPx) {
//                msgRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
//                chat_msg_relative.getLayoutParams().height = ((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics()));
//                chat_msg_layout2.setVisibility(View.GONE);
//                stausCode =1;
//            }
//
//            @Override
//            public void onSoftKeyboardClosed() {
//                chat_msg_relative.getLayoutParams().height = ((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics()));
//                chat_msg_layout2.setVisibility(View.GONE);
//                stausCode =1;
//            }
//        });
        new ScreenUtil(this).observeInputlayout(view, new ScreenUtil.OnInputActionListener() {
            @Override
            public void onOpen() {
                iskeyboard = true;
                msgRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
                chat_msg_relative.getLayoutParams().height = ((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics()));
                chat_msg_layout2.setVisibility(View.GONE);
                stausCode =1;
            }

            @Override
            public void onClose() {
                iskeyboard = false;
            }
        });
    }
    class click implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.chat_msg_layout_picture:
                    // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
                    if (PictureUtil.gallery() != null){
                        startActivityForResult(PictureUtil.gallery(), PictureUtil.PHOTO_REQUEST_GALLERY);
                    }else {
                        Toast.makeText(Chat_msg.this,"未选择图片",Toast.LENGTH_LONG).show();

                    }
                    break;
                case R.id.chat_msg_layout_photo:

                    // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
                    if (PictureUtil.camera() != null){
                        startActivityForResult(PictureUtil.camera(),PHOTO_REQUEST_CAREMA );
                    }else {
                        Toast.makeText(Chat_msg.this,"内存不可用",Toast.LENGTH_LONG).show();
                    }
                    break;
                case R.id.chat_msg_layout_voice:
                    break;
                case R.id.chat_msg_layout_video:
                    Intent intent2 = new Intent(Chat_msg.this, ChannelActivity.class);
                    startActivity(intent2);
                    break;

                case R.id.chat_msg_btn_send:
                    String content = chat_msg_edt_input.getText().toString();
                    if (!"".equals(content)) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("message",content);
                            jsonObject.put("username",MYID);
                            if (isgroup){
                                jsonObject.put("to","All");
                            }else {

                                jsonObject.put("to",OTHERID);
                            }

                            sendMsgToService(jsonObject.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // 清空输入框中的内容
                        chat_msg_edt_input.setText("");
                        msgRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
                    }
                    break;
                case R.id.chat_msg_img_attach:
                    if (stausCode == 1){
                        if (iskeyboard){
                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            if (inputMethodManager != null) {
                                // 隐藏键盘
                                inputMethodManager.hideSoftInputFromWindow(chat_msg_edt_input.getWindowToken(), 0);
                            }
                        }else {
                            msgRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
                            chat_msg_relative.getLayoutParams().height = ((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 337, getResources().getDisplayMetrics()));;
                            chat_msg_layout2.setVisibility(View.VISIBLE);
                            stausCode *=-1;
                        }


                    }else {
                        msgRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
                        chat_msg_relative.getLayoutParams().height = ((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics()));;
                        chat_msg_layout2.setVisibility(View.GONE);
                        stausCode *=-1;
                    }
                    break;

                case R.id.chat_msg_actionbar_img_headimg:
                    finish();
                    break;
                case R.id.chat_msg_actionbar_img_right:
                    Intent intent5 = new Intent(Chat_msg.this, ChannelActivity.class);
                    startActivity(intent5);
//                    Intent intent = new Intent(Chat_msg.this,Chat_msg_members.class);
//                    intent.putExtra("otherid",OTHERID);
//                    startActivity(intent);
                    break;

            }
        }
    }
    class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
//            String msg = intent.getStringExtra("msg");
//            JSONObject jsonObject = null;
//            try {
//                jsonObject = new JSONObject(msg);
//                Msg msgType;
//                if (jsonObject.getString("fromusername").equals(MYID)){
//                    msgType = new Msg(jsonObject.getString("textMessage"), Msg.TYPE_SENT);
//                    msgList.add(msgType);
//                }else {
//                    msgType = new Msg(jsonObject.getString("textMessage"),jsonObject.getString("fromusername"), Msg.TYPE_RECEIVED,isgroup);
//                    msgList.add(msgType);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

            getMsgs();
            // 当有新消息时，刷新ListView中的显示
            adapter.notifyItemInserted(msgList.size() - 1);
            // 将ListView定位到最后一行
            msgRecyclerView.scrollToPosition(msgList.size() - 1);
        }
    }
    public boolean isClickEt(View view, MotionEvent event) {
        if (view != null && ((view instanceof EditText)|| (view instanceof Button)))  {
            int[] leftTop = { 0, 0 };
            // 获取输入框当前的location位置
            chat_msg_relative.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            // 此处根据输入框左上位置和宽高获得右下位置
            int bottom = top + chat_msg_relative.getHeight();
            int right = left + chat_msg_relative.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
            // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    private void getMsgs(){
        msgList.clear();
        Helper_Chat_Msg _helperChatMsg = null;
        if(_helperChatMsg == null)
        {
            _helperChatMsg = new Helper_Chat_Msg(Chat_msg.this);
        }
        SQLiteDatabase database = _helperChatMsg.getWritableDatabase();
        Cursor cursor = database.query(Bean_Chat_Msg.TABLE_NAME, null, null, null, null, null, null);
        Msg msg1;
        if(cursor.moveToFirst()) // 显示数据库的内容
        {
            for(; !cursor.isAfterLast(); cursor.moveToNext()) // 获取查询游标中的数据
            {
                String from = cursor.getString(cursor.getColumnIndex(Bean_Chat_Msg.FROMUSERNAME));
                String to = cursor.getString(cursor.getColumnIndex(Bean_Chat_Msg.TOUSERNAME));
                String message = cursor.getString(cursor.getColumnIndex(Bean_Chat_Msg.MESSAGE));
                if (from.equals(MYID)){
                    msg1 = new Msg(message, Msg.TYPE_SENT);
                    msgList.add(msg1);
                }else {
                    msg1 = new Msg(message,from, Msg.TYPE_RECEIVED,isgroup);
                    msgList.add(msg1);
                }
            }
        }
        cursor.close(); // 记得关闭游标对象

    }

}
