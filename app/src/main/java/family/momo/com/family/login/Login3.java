package family.momo.com.family.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import family.momo.com.family.R;
import family.momo.com.family.util.ImgUtil;

public class Login3 extends AppCompatActivity {
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    ImageView login3_img_upload,login3_img_back;
    EditText login3_edt_username;
    Button login3_btn_nextstep;
    Bitmap roundbitmap;

    private String phone;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login3);
        phone = getIntent().getStringExtra("phone");

        setActionBar();
        initViews();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                // 读取uri所在的图片
                try {
                    roundbitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                    login3_img_upload.setImageBitmap(ImgUtil.toRoundBitmap(roundbitmap));
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        login3_edt_username = findViewById(R.id.login3_edt_username);
        login3_edt_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0){
                    Drawable drawable = getResources().getDrawable(R.drawable.corner_green);
                    login3_btn_nextstep.setTextColor(Color.parseColor("#ffffff"));
                    login3_btn_nextstep.setBackground(drawable);
                    login3_btn_nextstep.setClickable(true);
                }else {
                    Drawable drawable = getResources().getDrawable(R.drawable.corner_grey);
                    login3_btn_nextstep.setTextColor(Color.parseColor("#7c7c7c"));
                    login3_btn_nextstep.setBackground(drawable);
                    login3_btn_nextstep.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        login3_img_upload = findViewById(R.id.login3_img_upload);
        login3_btn_nextstep = findViewById(R.id.login3_btn_nextstep);
        login3_img_back = findViewById(R.id.login3_img_back);

        login3_img_upload.setOnClickListener(new imgOrButtonClick());
        login3_btn_nextstep.setOnClickListener(new imgOrButtonClick());
        login3_img_back.setOnClickListener(new imgOrButtonClick());
    }

    class imgOrButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.login3_img_upload:
                    // 激活系统图库，选择一张图片
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
                    startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                    break;
                case R.id.login3_btn_nextstep:
                    username = login3_edt_username.getText().toString().trim();
                    if (login3_edt_username.getText().toString().trim().equals("")){
                        Toast.makeText(Login3.this,"请输入名称",Toast.LENGTH_SHORT).show();
//                    }else if (roundbitmap == null){
//                        Toast.makeText(Login3.this,"请选择一张图片",Toast.LENGTH_SHORT).show();
                    }else {
                        Intent intent1 = new Intent(Login3.this,Login4.class);
                        intent1.putExtra("phone",phone);
                        intent1.putExtra("username",username);
                        startActivity(intent1);
                    }
                    break;
                case R.id.login3_img_back:
                    finish();
                    break;
            }
        }
    }
    private String getJsonString(String phone,String username,Bitmap bitmap){
        JSONObject object = new JSONObject();
        try {
            object.put("phone",phone);
            object.put("username",username);
//            byte[] data = ImgUtil.Bitmap2Bytes(bitmap);
//            object.put("img", Base64.encodeToString(data,0));
            return object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
