package family.momo.com.family.information;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import family.momo.com.family.Database.Bean_User_Info;
import family.momo.com.family.Database.Helper_User_Info;
import family.momo.com.family.Database.Util;
import family.momo.com.family.MainActivity;
import family.momo.com.family.R;
import family.momo.com.family.login.Login5;
import family.momo.com.family.util.VariableDataUtil;
import family.momo.com.family.util.sendPostUtil;

public class Infomation_newcircle extends AppCompatActivity {
    String groupname;
    EditText Infomation_newcircle_edt_groupname;
    Button Infomation_newcircle_btn_finish;
    ImageView Infomation_newcircle_actionbar_img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infomation_newcircle);

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
        Infomation_newcircle_btn_finish = findViewById(R.id.Infomation_newcircle_btn_finish);
        Infomation_newcircle_btn_finish.setClickable(false);
        Infomation_newcircle_actionbar_img_back = findViewById(R.id.Infomation_newcircle_actionbar_img_back);
        Infomation_newcircle_edt_groupname = findViewById(R.id.Infomation_newcircle_edt_groupname);
        Infomation_newcircle_actionbar_img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Infomation_newcircle_btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupname = Infomation_newcircle_edt_groupname.getText().toString();
                if (true){
                    final JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("groupname",groupname);
                        jsonObject.put("username",VariableDataUtil.MYID);
                        jsonObject.put("phone",VariableDataUtil.phone);
                        new Thread(){
                            @Override
                            public void run() {
                                post(jsonObject.toString());
                            }
                        }.start();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Infomation_newcircle_edt_groupname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()!=0){
                    Drawable drawable = getResources().getDrawable(R.drawable.corner_green);
                    Infomation_newcircle_btn_finish.setTextColor(Color.parseColor("#ffffff"));
                    Infomation_newcircle_btn_finish.setBackground(drawable);
                    Infomation_newcircle_btn_finish.setClickable(true);
                }else {
                    Drawable drawable = getResources().getDrawable(R.drawable.corner_grey);
                    Infomation_newcircle_btn_finish.setTextColor(Color.parseColor("#7c7c7c"));
                    Infomation_newcircle_btn_finish.setBackground(drawable);
                    Infomation_newcircle_btn_finish.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void post(String json){
        String encode="utf-8";

        sendPostUtil.postRequest(VariableDataUtil.requestAdress+"/consumer/create", json,encode, new sendPostUtil.OnResponseListner() {
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
            Toast.makeText(Infomation_newcircle.this,msg.toString(),Toast.LENGTH_LONG).show();
            if (msg.obj != null){
                Intent intent = new Intent(Infomation_newcircle.this,Infomation_sharecircle_share.class);
                intent.putExtra("groupcode",msg.obj.toString());
                startActivity(intent);
                Intent intent1 = new Intent(Infomation_newcircle.this,MainActivity.class);
                VariableDataUtil.groupname = groupname;
                setResult(MainActivity.INFOMATION_NEWCICLE,intent1);
                finish();

            }
            super.handleMessage(msg);
        }
    };
}
