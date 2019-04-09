package family.momo.com.family.information;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import family.momo.com.family.MainActivity;
import family.momo.com.family.R;
import family.momo.com.family.util.VariableDataUtil;

public class Infomation_sharecircle_share extends AppCompatActivity {

    String groupCode;
    TextView Infomation_sharecircle_share_txt_invitecode;
    Button Infomation_sharecircle_share_btn_nextstep;
    ImageView Infomation_sharecircle_share_actionbar_img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infomation_sharecircle_share);
        groupCode = getIntent().getStringExtra("groupcode");
        String temp = groupCode.substring(0,3);
        String temp1 = groupCode.substring(3);
        groupCode = temp+"-"+temp1;

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
        Infomation_sharecircle_share_actionbar_img_back = findViewById(R.id.Infomation_sharecircle_share_actionbar_img_back);
        Infomation_sharecircle_share_actionbar_img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();

            }
        });
        Infomation_sharecircle_share_txt_invitecode = findViewById(R.id.Infomation_sharecircle_share_txt_invitecode);
        Infomation_sharecircle_share_txt_invitecode.setText(groupCode);
        Infomation_sharecircle_share_btn_nextstep = findViewById(R.id.Infomation_sharecircle_share_btn_nextstep);
        Infomation_sharecircle_share_btn_nextstep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "分享邀请码111");
                intent.putExtra(Intent.EXTRA_TEXT, "分享邀请码"+VariableDataUtil.groupcode);//extraText为文本的内容
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//为Activity新建一个任务栈
                startActivity(Intent.createChooser(intent, "分享邀请码333"));//R.string.action_share同样是标题
            }
        });
    }
}
