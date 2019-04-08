package family.momo.com.family.login;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import family.momo.com.family.Database.Bean_User_Info;
import family.momo.com.family.Database.Helper_User_Info;
import family.momo.com.family.Database.Util;
import family.momo.com.family.DefinedViews.VerificationCodeInput;
import family.momo.com.family.MainActivity;
import family.momo.com.family.R;
import family.momo.com.family.util.VariableDataUtil;
import family.momo.com.family.util.sendPostUtil;

public class Login4 extends AppCompatActivity {
    VerificationCodeInput login4_edt_invitecode;
    Button login4_btn_nextstep;
    TextView login4_txt_create;
    ImageView login4_img_back;

    String invitecode;
    String username;
    String phone;
    String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login4);

        username = getIntent().getStringExtra("username");
        phone = getIntent().getStringExtra("phone");
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
        login4_img_back = findViewById(R.id.login4_img_back);
        login4_txt_create = findViewById(R.id.login4_txt_create);
        login4_edt_invitecode = findViewById(R.id.login4_edt_invitecode);
        login4_btn_nextstep = findViewById(R.id.login4_btn_nextstep);
        login4_btn_nextstep.setBackground(getResources().getDrawable(R.drawable.corner_grey));
        login4_btn_nextstep.setOnClickListener(new Login4_Click());
        login4_txt_create.setOnClickListener(new Login4_Click());
        login4_img_back.setOnClickListener(new Login4_Click());
        login4_btn_nextstep.setClickable(false);
        login4_edt_invitecode.setOnCompleteListener(new VerificationCodeInput.Listener() {
            @Override
            public void onComplete(String content) {
                login4_btn_nextstep.setClickable(true);
                login4_btn_nextstep.setBackground(getResources().getDrawable(R.drawable.corner_green));
                login4_btn_nextstep.setTextColor(Color.parseColor("#ffffff"));
                invitecode = content;
            }
        });

    }

    class Login4_Click implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.login4_img_back:
                    finish();
                    break;
                case R.id.login4_btn_nextstep:
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("groupcode",invitecode);
                            jsonObject.put("phone",phone);
                            jsonObject.put("username",username);
                            json = jsonObject.toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new Thread(){
                            @Override
                            public void run() {
                                post();
                            }
                        }.start();

                    break;
                case R.id.login4_txt_create:

                    Intent intent1 = new Intent(Login4.this,Login5.class);
                    intent1.putExtra("phone",phone);
                    intent1.putExtra("username",username);
                    startActivity(intent1);
                    break;

            }
        }
    }
    private void post(){

        sendPostUtil.postRequest(VariableDataUtil.requestAdress+"/consumer/join",json,"utf-8", new sendPostUtil.OnResponseListner() {
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
            Toast.makeText(Login4.this,msg.toString(),Toast.LENGTH_LONG).show();
            if (msg.obj != null){
                Intent intent = new Intent(Login4.this,MainActivity.class);
                intent.putExtra("phone",phone);
                intent.putExtra("username",username);
                intent.putExtra("groupname",(String)msg.obj);

                Helper_User_Info helper_User_Info = new Helper_User_Info(Login4.this);
                SQLiteDatabase sqLiteDatabase = helper_User_Info.getWritableDatabase();
                if (Util.tabbleIsExist(sqLiteDatabase,Bean_User_Info.TABLE_NAME)){
                    ContentValues cV = new ContentValues();
                    cV.put(Bean_User_Info.PHONE,phone);
                    cV.put(Bean_User_Info.USERNAME,username);
                    cV.put(Bean_User_Info.GROUPNAME,(String)msg.obj);
                    sqLiteDatabase.insert(Bean_User_Info.TABLE_NAME, null, cV);
                }

                startActivity(intent);
            }
        }
    };


}
