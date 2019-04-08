package family.momo.com.family.login;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mob.MobSDK;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import family.momo.com.family.R;

import static family.momo.com.family.util.VariableDataUtil.APPKEY;
import static family.momo.com.family.util.VariableDataUtil.APPSECRET;

public class Login2 extends AppCompatActivity {

    EditText login2_edt_surecode;
    Button login2_btn_nextstep;
    Button login2_btn_resend;
    ImageView login2_img_back;
    private String phoneNums;

    int i = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        phoneNums = getIntent().getStringExtra("phone");//手机号
        setActionBar();
        initViews();

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
    //初始化组件
    private void initViews(){
        login2_img_back = findViewById(R.id.login2_img_back);
        login2_btn_resend = findViewById(R.id.login2_btn_resend);
        login2_btn_nextstep = findViewById(R.id.login2_btn_nextstep);
        login2_edt_surecode = findViewById(R.id.login2_edt_surecode);
        login2_btn_nextstep.setOnClickListener(new buttonClick());
        login2_btn_nextstep.setClickable(false);

        login2_edt_surecode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0){
                    Drawable drawable = getResources().getDrawable(R.drawable.corner_green);
                    login2_btn_nextstep.setTextColor(Color.parseColor("#ffffff"));
                    login2_btn_nextstep.setBackground(drawable);
                    login2_btn_nextstep.setClickable(true);
                }else {
                    Drawable drawable = getResources().getDrawable(R.drawable.corner_grey);
                    login2_btn_nextstep.setTextColor(Color.parseColor("#7c7c7c"));
                    login2_btn_nextstep.setBackground(drawable);
                    login2_btn_nextstep.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        login2_btn_nextstep.setOnClickListener(new buttonClick());
        login2_btn_resend.setOnClickListener(new buttonClick());
        login2_img_back.setOnClickListener(new buttonClick());

        // 启动短信验证sdk
        MobSDK.init(this, APPKEY, APPSECRET);
        EventHandler eventHandler = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        //注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
    }

    class buttonClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login2_btn_resend:
                    sendsms();
                    break;

                case R.id.login2_btn_nextstep:
                    //将收到的验证码和手机号提交再次核对
                    SMSSDK.submitVerificationCode("86", phoneNums, login2_edt_surecode
                            .getText().toString());
                    break;
                case R.id.login2_img_back:
                    finish();
                    break;
            }
        }
    }
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                login2_btn_resend.setText( i + "s之后");
            } else if (msg.what == -8) {
                login2_btn_resend.setText("获取验证码");
                login2_btn_resend.setClickable(true);
                i = 30;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                Log.e("event", "event=" + event);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 短信注册成功后，返回MainActivity,然后提示
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
                        Toast.makeText(getApplicationContext(), "提交验证码成功",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login2.this,
                                Login3.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("phone",phoneNums);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Toast.makeText(getApplicationContext(), "正在获取验证码",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Login2.this,"验证码不正确",Toast.LENGTH_SHORT).show();
                        ((Throwable) data).printStackTrace();
                    }
                }
            }
        }
    };

    private void sendsms(){
        // 2. 通过sdk发送短信验证
        SMSSDK.getVerificationCode("86", phoneNums);

        // 3. 把按钮变成不可点击，并且显示倒计时（正在获取）

        login2_btn_resend.setClickable(false);
        login2_btn_resend.setText(i + "s之后");
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (; i > 0; i--) {
                    handler.sendEmptyMessage(-9);
                    if (i <= 0) {
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                handler.sendEmptyMessage(-8);
            }
        }).start();
    }
}
