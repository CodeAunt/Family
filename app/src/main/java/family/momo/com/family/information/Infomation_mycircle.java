package family.momo.com.family.information;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import family.momo.com.family.MainActivity;
import family.momo.com.family.R;
import family.momo.com.family.util.VariableDataUtil;

public class Infomation_mycircle extends AppCompatActivity {
    //ActionBar
    ImageView Infomation_mycircle_actionbar_img_back;


    //列表块
    ListView Infomation_mycircle_listview_groups ;
    ArrayList<Map<String, Object>> datas = new ArrayList<>();//定义全局数据集合
    List<String> groupNames = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infomation_mycircle);

        setActionBar();
        initViews();
        getGroups();
        shouGroups();
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
        Infomation_mycircle_listview_groups = findViewById(R.id.Infomation_mycircle_listview_groups);
        Infomation_mycircle_actionbar_img_back = findViewById(R.id.Infomation_mycircle_actionbar_img_back);
        Infomation_mycircle_actionbar_img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    //获取群组信息
    public void getGroups(){
        groupNames.add(VariableDataUtil.groupname);
//        groupNames.add("Family");
//        groupNames.add("大家庭");
    }
    //显示群组信息
    public void shouGroups(){

        for (int i = 0; i < groupNames.size(); i++){
            Map<String, Object> item = new HashMap<>();
            item.put("groupname", groupNames.get(i));
            datas.add(item);
        }
        //获得simpleAdapter
        SimpleAdapter simpleAdapter = new SimpleAdapter(Infomation_mycircle.this,datas,R.layout.activity_infomation_mycircle_style,new String[]{ "groupname"},new int[]{R.id.activity_infomation_mycircle_style_txt_groupname});
        Infomation_mycircle_listview_groups.setAdapter(simpleAdapter);
        Infomation_mycircle_listview_groups.setOnItemClickListener(new ChooseGroupClick());

    }
    //项目点击事件
    class ChooseGroupClick implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(Infomation_mycircle.this,MainActivity.class);
            VariableDataUtil.groupname = groupNames.get(position);
            setResult(MainActivity.INFOMATION_MYCICLE,intent);
            finish();
        }
    }
}
