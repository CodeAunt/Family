package family.momo.com.family.chat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import family.momo.com.family.R;

import static family.momo.com.family.chat.Chat_msg.chatActivity;
import static family.momo.com.family.chat.Chat_msg_members.chat_members_Activity;

public class Chat_msg_members_users extends AppCompatActivity {

    boolean isderectlygroup = false;
    String otherid;
    TextView chat_msg_members_users_txt_name,chat_msg_members_users_txt_phone;
    ImageView chat_msg_members_users_actionbar_img_back,chat_msg_members_users_img_headimg;
    Button chat_msg_members_users_btn_sendmsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_msg_members_users);
        setActionBar();
        initViews();
        getUserInfo();
    }
    //设置Actionbar和状态栏
    private void setActionBar(){
        View decorView = getWindow().getDecorView();
        //decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);//隐藏状态栏
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getSupportActionBar().hide();//隐藏Actionbar
    }
    private void getUserInfo(){
        otherid = getIntent().getStringExtra("otherid");
        isderectlygroup = getIntent().getBooleanExtra("isderectlygroup",false);
        chat_msg_members_users_txt_name.setText(otherid);
        chat_msg_members_users_txt_phone.setText("电话号：16553361498");
    }
    private void initViews(){
        chat_msg_members_users_txt_name = findViewById(R.id.chat_msg_members_users_txt_name);
        chat_msg_members_users_txt_phone = findViewById(R.id.chat_msg_members_users_txt_phone);
        chat_msg_members_users_actionbar_img_back = findViewById(R.id.chat_msg_members_users_actionbar_img_back);
        chat_msg_members_users_actionbar_img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        chat_msg_members_users_img_headimg = findViewById(R.id.chat_msg_members_users_img_headimg);
        chat_msg_members_users_btn_sendmsg = findViewById(R.id.chat_msg_members_users_btn_sendmsg);
        chat_msg_members_users_btn_sendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isderectlygroup){

                    chatActivity.finish();
                }else {
                    chatActivity.finish();
                    chat_members_Activity.finish();
                }

                Intent intent = new Intent(Chat_msg_members_users.this,Chat_msg.class);
                intent.putExtra("otherid",otherid);
                startActivity(intent);
                finish();
            }
        });
    }
}
