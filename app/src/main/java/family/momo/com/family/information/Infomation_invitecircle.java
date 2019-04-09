package family.momo.com.family.information;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import family.momo.com.family.Database.Bean_User_Info;
import family.momo.com.family.Database.Helper_User_Info;
import family.momo.com.family.Database.Util;
import family.momo.com.family.MainActivity;
import family.momo.com.family.R;
import family.momo.com.family.login.Login4;
import family.momo.com.family.util.VariableDataUtil;
import family.momo.com.family.util.sendPostUtil;

public class Infomation_invitecircle extends AppCompatActivity {
    //ActionBar
    ImageView Infomation_invitecircle_actionbar_img_back;

    String groupname;

    //列表块
    ListView Infomation_invitecircle_listview_groups ;
    ArrayList<Map<String, Object>> datas = new ArrayList<>();//定义全局数据集合
    List<String> groupNames = new LinkedList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infomation_invitecircle);
        setActionBar();
        initViews();

        getGroups();
        if (groupNames.size() == 0){
            Infomation_invitecircle_listview_groups.setVisibility(View.GONE);
        }else {
            shouGroups();

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
    //初始化组件
    private void initViews(){
        Infomation_invitecircle_listview_groups = findViewById(R.id.Infomation_invitecircle_listview_groups);
        Infomation_invitecircle_actionbar_img_back = findViewById(R.id.Infomation_invitecircle_actionbar_img_back);
        Infomation_invitecircle_actionbar_img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    //获取群组信息
    public void getGroups(){
        groupNames.add(VariableDataUtil.groupname);
    }
    //显示群组信息
    public void shouGroups(){

        for (int i = 0; i < groupNames.size(); i++){
            Map<String, Object> item = new HashMap<>();
            item.put("groupname", groupNames.get(i));
            datas.add(item);
        }//获得simpleAdapter
        SimpleAdapter simpleAdapter = new SimpleAdapter(Infomation_invitecircle.this,datas,R.layout.activity_infomation_mycircle_style,new String[]{ "groupname"},new int[]{R.id.activity_infomation_mycircle_style_txt_groupname});
        Infomation_invitecircle_listview_groups.setAdapter(simpleAdapter);
        Infomation_invitecircle_listview_groups.setOnItemClickListener(new ChooseGroupClick());
        Infomation_invitecircle_listview_groups.setOnItemLongClickListener(new ChooseGroupLong());

    }
    //项目点击事件
    class ChooseGroupClick implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            groupname = groupNames.get(position);
            if (true){
                new Thread(){
                    @Override
                    public void run() {
                        post(groupname);
                    }
                }.start();

            }
        }
    }
    //项目长按事件
    class ChooseGroupLong implements AdapterView.OnItemLongClickListener{
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            ImageView imageView = view.findViewById(R.id.activity_infomation_mycircle_style_img_houose);
            if (imageView.getVisibility()==View.INVISIBLE){
                imageView.setVisibility(View.VISIBLE);
            }else {
                imageView.setVisibility(View.INVISIBLE);
            }

            return true;
        }
    }
    private void post(String groupname){

        sendPostUtil.getGroupCode(groupname, new sendPostUtil.OnResponseListner() {
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
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject((String)(msg.obj));
                Intent intent = new Intent(Infomation_invitecircle.this,Infomation_sharecircle_share.class);
                intent.putExtra("groupcode", jsonObject.get("groupcode").toString());
                startActivity(intent);
                Intent intent1 = new Intent(Infomation_invitecircle.this,MainActivity.class);

                setResult(MainActivity.INFOMATION_INVITECICLE,intent1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };
}
