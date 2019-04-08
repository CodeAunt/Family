package family.momo.com.family.information;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import family.momo.com.family.R;
import family.momo.com.family.util.VariableDataUtil;

public class Infomation_fedback extends AppCompatActivity {

    WebView Infomation_fedback_webview_fedback;
    ImageView Infomation_fedback_actionbar_img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infomation_fedback);
        setActionBar();
        initViews();


    }
    @Override
    public void onBackPressed() {
        if (Infomation_fedback_webview_fedback.canGoBack()){
            Infomation_fedback_webview_fedback.goBack();
        }else {
            finish();
        }
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
        Infomation_fedback_actionbar_img_back = findViewById(R.id.Infomation_fedback_actionbar_img_back);
        Infomation_fedback_actionbar_img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Infomation_fedback_webview_fedback.canGoBack()){
                    Infomation_fedback_webview_fedback.goBack();
                }else {
                    finish();
                }
            }
        });
        Infomation_fedback_webview_fedback = findViewById(R.id.Infomation_fedback_webview_fedback);
        Infomation_fedback_webview_fedback.getSettings().setJavaScriptEnabled(true);
        Infomation_fedback_webview_fedback.getSettings().setDomStorageEnabled(true);

        String postData = "nickname="+VariableDataUtil.MYID+"&avatar="+VariableDataUtil.headImgUrl+"&openid="+VariableDataUtil.phone;
        Infomation_fedback_webview_fedback.postUrl(VariableDataUtil.fedBackUrl,postData.getBytes());

    }
}
