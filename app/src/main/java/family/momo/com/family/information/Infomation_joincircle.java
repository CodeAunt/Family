package family.momo.com.family.information;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import family.momo.com.family.DefinedViews.Dialog_self;
import family.momo.com.family.DefinedViews.VerificationCodeInput;
import family.momo.com.family.MainActivity;
import family.momo.com.family.R;
import family.momo.com.family.util.VariableDataUtil;
import family.momo.com.family.util.sendPostUtil;

public class Infomation_joincircle extends AppCompatActivity {
    int errorTimes = 0;
    String invitecode;
    VerificationCodeInput Infomation_joincircle_edt_invitecode;
    Button Infomation_joincircle_btn_nextstep;
    ImageView Infomation_joincircle_actionbar_img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infomation_joincircle);
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
    private void initViews(){
        Infomation_joincircle_actionbar_img_back = findViewById(R.id.Infomation_joincircle_actionbar_img_back);
        Infomation_joincircle_edt_invitecode = findViewById(R.id.Infomation_joincircle_edt_invitecode);
        Infomation_joincircle_btn_nextstep = findViewById(R.id.Infomation_joincircle_btn_nextstep);
        Infomation_joincircle_btn_nextstep.setBackground(getResources().getDrawable(R.drawable.corner_grey));
        Infomation_joincircle_btn_nextstep.setOnClickListener(new Infomation_joincircle_Click());
        Infomation_joincircle_actionbar_img_back.setOnClickListener(new Infomation_joincircle_Click());
        Infomation_joincircle_btn_nextstep.setClickable(false);
        Infomation_joincircle_edt_invitecode.setOnCompleteListener(new VerificationCodeInput.Listener() {
            @Override
            public void onComplete(String content) {
                Infomation_joincircle_btn_nextstep.setClickable(true);
                Infomation_joincircle_btn_nextstep.setBackground(getResources().getDrawable(R.drawable.corner_green));
                Infomation_joincircle_btn_nextstep.setTextColor(Color.parseColor("#ffffff"));
                invitecode = content;
            }
        });

    }
    class Infomation_joincircle_Click implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.Infomation_joincircle_actionbar_img_back:
                    finish();
                    break;
                case R.id.Infomation_joincircle_btn_nextstep:
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("phone",VariableDataUtil.phone);
                        jsonObject.put("groupcode",invitecode);
                        jsonObject.put("username",VariableDataUtil.MYID);
                        final String json = jsonObject.toString();
                        new Thread(){
                            @Override
                            public void run() {
                                post(json);
                            }
                        }.start();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

            }
        }
    }

    private void post(String json){

        sendPostUtil.postRequest(VariableDataUtil.requestAddress +"/consumer/join",json,"utf-8", new sendPostUtil.OnResponseListner() {
            @Override
            public void onSucess(String response) {
                Message message = new Message();
                message.obj = response;
                handler.sendMessage(message);
            }

            @Override
            public void onError(String error) {
                Message message = new Message();
                message.obj = error;
                handler.sendMessage(message);
            }
        });
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj != null){
                if (errorTimes<5 && errorTimes>0){
                    Dialog_self dialog_self = new Dialog_self(Infomation_joincircle.this);
                    dialog_self.setMsg("没有此邀请码，连续输错5次，账号会冻结1天，已输错"+errorTimes+"次");
                    dialog_self.show();
                }else if (errorTimes == 5){
                    Dialog_self dialog_self = new Dialog_self(Infomation_joincircle.this);
                    dialog_self.setMsg("您的账号已被冻结");
                    dialog_self.show();
                }else {
                    Intent intent = new Intent(Infomation_joincircle.this,MainActivity.class);
                    setResult(MainActivity.INFOMATION_JOINCICLE,intent);
                    VariableDataUtil.groupname = (String)msg.obj;
                    finish();
                }


            }
        }
    };
}
