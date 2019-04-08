package family.momo.com.family.login;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import family.momo.com.family.Database.Bean_User_Info;
import family.momo.com.family.Database.Helper_User_Info;
import family.momo.com.family.Database.Util;
import family.momo.com.family.MainActivity;
import family.momo.com.family.R;

public class Login extends AppCompatActivity {


    Button login_btn_nextstep;
    EditText login_edt_phone;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Helper_User_Info _helperUserInfo = null;
        if(_helperUserInfo == null)
        {
            _helperUserInfo = new Helper_User_Info(Login.this);
        }
        SQLiteDatabase database = _helperUserInfo.getWritableDatabase();
        if (Util.tabbleIsExist(database,Bean_User_Info.TABLE_NAME)){
            Cursor cursor = database.query(Bean_User_Info.TABLE_NAME, null, null, null, null, null, null);
            if(cursor.moveToFirst()) // 显示数据库的内容
            {

                String phone = cursor.getString(cursor.getColumnIndex(Bean_User_Info.PHONE));
                String username = cursor.getString(cursor.getColumnIndex(Bean_User_Info.USERNAME));
                String groupname = cursor.getString(cursor.getColumnIndex(Bean_User_Info.GROUPNAME));

                if (!phone.equals("") && !phone.equals("") && !phone.equals("")){
                    Intent intent = new Intent(Login.this,MainActivity.class);
                    intent.putExtra("groupname",groupname);
                    intent.putExtra("username",username);
                    intent.putExtra("phone",phone);
                    startActivity(intent);
                }

            }
            cursor.close(); // 记得关闭游标对象
        }


        setActionBar();
        initViews();
        edt_change();
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
        login_btn_nextstep = findViewById(R.id.login_btn_nextstep);
        login_edt_phone = findViewById(R.id.login_edt_phone);
        login_btn_nextstep.setOnClickListener(new buttonClick());
        login_btn_nextstep.setClickable(false);

    }
    //编辑框改变
    private void edt_change(){
        login_edt_phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(login_edt_phone, InputMethodManager.SHOW_IMPLICIT);

                    login_edt_phone.setFocusableInTouchMode(true);
                    login_edt_phone.setHint("");
                    login_edt_phone.setGravity(Gravity.LEFT);
                    login_edt_phone.setTextSize(20);
                }
            }
        });

        login_edt_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 11){
                    phone = s.toString();
                    Drawable drawable = getResources().getDrawable(R.drawable.corner_green);
                    login_btn_nextstep.setTextColor(Color.parseColor("#ffffff"));
                    login_btn_nextstep.setBackground(drawable);
                    login_btn_nextstep.setClickable(true);
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                }else {
                    Drawable drawable = getResources().getDrawable(R.drawable.corner_grey);
                    login_btn_nextstep.setTextColor(Color.parseColor("#7c7c7c"));
                    login_btn_nextstep.setBackground(drawable);
                    login_btn_nextstep.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    //按钮点击事件
    class buttonClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (judgePhoneNums(phone)) {

                Intent intent = new Intent(Login.this,Login3.class);
                intent.putExtra("phone",phone);
                startActivity(intent);
            }else {
                Toast.makeText(Login.this,"请输入正确的手机号",Toast.LENGTH_LONG).show();
            }
        }

    }
    // 判断手机号码是否合理
    private boolean judgePhoneNums(String phoneNums) {
        if (isMatchLength(phoneNums, 11)
                && isMobileNO(phoneNums)) {
            return true;
        }
        return false;
    }
    //判断一个字符串的位数
    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }
    //验证手机格式
    public static boolean isMobileNO(String mobileNums) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][3578]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

}
