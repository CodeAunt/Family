package family.momo.com.family.login;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import family.momo.com.family.util.VariableDataUtil;
import family.momo.com.family.util.sendPostUtil;

public class Login5 extends AppCompatActivity {
    ImageView login5_img_back;
    EditText login5_edt_groupname;
    Button login5_btn_finish;

    String phone;
    String username;
    String groupname;
    String json;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login5);

        setActionBar();
        initViews();
        phone = getIntent().getStringExtra("phone");
        username = getIntent().getStringExtra("username");

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
        login5_img_back = findViewById(R.id.login5_img_back);
        login5_edt_groupname = findViewById(R.id.login5_edt_groupname);
        login5_edt_groupname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()!=0){
                    Drawable drawable = getResources().getDrawable(R.drawable.corner_green);
                    login5_btn_finish.setTextColor(Color.parseColor("#ffffff"));
                    login5_btn_finish.setBackground(drawable);
                    login5_btn_finish.setClickable(true);
                }else {
                    Drawable drawable = getResources().getDrawable(R.drawable.corner_grey);
                    login5_btn_finish.setTextColor(Color.parseColor("#7c7c7c"));
                    login5_btn_finish.setBackground(drawable);
                    login5_btn_finish.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        login5_btn_finish = findViewById(R.id.login5_btn_finish);
        login5_btn_finish.setClickable(false);
        login5_img_back.setOnClickListener(new Login5_Click());
        login5_btn_finish.setOnClickListener(new Login5_Click());
    }

    class Login5_Click implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.login5_img_back:
                    finish();
                    break;
                case R.id.login5_btn_finish:
                    groupname = login5_edt_groupname.getText().toString().trim();
                    if (!groupname.equals("")){

                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("groupname",groupname);
                            jsonObject.put("phone",phone);
                            jsonObject.put("username",username);
                            json = jsonObject.toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new Thread(){
                            @Override
                            public void run() {
                                Log.e("momo","点击了");
                                post();
                            }
                        }.start();
                    }else {
                        Toast.makeText(Login5.this,"请输入群组名",Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    }
    public void post(){
        String encode="utf-8";

        sendPostUtil.postRequest(VariableDataUtil.requestAddress +"/consumer/create", json,encode, new sendPostUtil.OnResponseListner() {
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
            VariableDataUtil.groupcode = (String) msg.obj;
            if (msg.obj != null){
                Intent intent = new Intent(Login5.this,MainActivity.class);
                intent.putExtra("groupname",groupname);
                intent.putExtra("username",username);
                intent.putExtra("phone",phone);

                Helper_User_Info helper_User_Info = new Helper_User_Info(Login5.this);
                SQLiteDatabase sqLiteDatabase = helper_User_Info.getWritableDatabase();
                if (Util.tabbleIsExist(sqLiteDatabase,Bean_User_Info.TABLE_NAME)){
                    ContentValues cV = new ContentValues();
                    cV.put(Bean_User_Info.PHONE,phone);
                    cV.put(Bean_User_Info.USERNAME,username);
                    cV.put(Bean_User_Info.GROUPNAME,groupname);
                    sqLiteDatabase.insert(Bean_User_Info.TABLE_NAME, null, cV);
                }
                startActivity(intent);

            }
            super.handleMessage(msg);
        }
    };
}
