package family.momo.com.family;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import family.momo.com.family.Database.Bean_User_Info;
import family.momo.com.family.Database.Helper_User_Info;
import family.momo.com.family.Database.LocationInfo;
import family.momo.com.family.Database.Util;
import family.momo.com.family.DefinedViews.BadgeView;
import family.momo.com.family.DefinedViews.ReViewPager;
import family.momo.com.family.album.SelectPhotoActivity;
import family.momo.com.family.chat.Chat_msg;
import family.momo.com.family.information.Infomation_fedback;
import family.momo.com.family.information.Infomation_invitecircle;
import family.momo.com.family.information.Infomation_joincircle;
import family.momo.com.family.information.Infomation_mycircle;
import family.momo.com.family.information.Infomation_newcircle;
import family.momo.com.family.information.Infomation_sharecircle_share;
import family.momo.com.family.util.CircleImageView;
import family.momo.com.family.util.VariableDataUtil;
import family.momo.com.family.util.sendPostUtil;

import static java.lang.Float.parseFloat;

public class MainActivity extends AppCompatActivity {

    //album
    private RecyclerView mRecyclerView;
    //    private RecyclerView.Adapter mAdapter;
    private ArrayList<String> mPhotos;
    private HomeAdapter mAdapter;
    private String fileNames[];
    private ImageView mMainPhoto;
    private LocationAdapter mLocationAdapter;
    private ArrayList<LocationInfo> mLocationList;
    private String jsonUp, jsonDown;
    private String albumAddress;

    /*********************************************************1*/
    private Random ra =new Random();
    /**********************************************************/


    //地图116.403694,39.915599
    //白框
    private double mLongitude = 116.403694;
    private double mLatitude = 39.915599;
    private TextureMapView mMap;
    private RecyclerView mLocationBar;
    private FloatingActionButton mBtnLocation;
    private BaiduMap mBaiduMap;
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    //    Context context = new this.getApplicationContext();
    int targetSdkVersion;
    private CircleImageView ivHeadBean;
    private ImageView ivBgBean;

    private Timer mTimer = null;
    private TimerTask mTimerTask = null;

    //个人信息块
    String headimgurl;
    public String OTHERID;//聊天对象身份
    TextView mainactivity_left_txt_groupname,mainactivity_left_txt_phone,mainactivity_left_txt_username;

    Context context = this;
    //ActionBar块
    TextView mainactivity_actionbar_txt_title;
    ImageView mainactivity_actionbar_img_headimg;
    ImageView mainactivity_actionbar_img_right;

    //列表块
    ListView chat_list_users = null;
    ArrayList<Map<String, Object>> data = new ArrayList<>();//定义全局数据集合
    List<String> names = new LinkedList();
    List<String> msgs = new LinkedList();

    private MsgReceiver msgReceiver;
    private Intent serviceIntent;

    //滑动窗体块
    LayoutInflater inflater;
    private View view1, view2, view3, view4;
    private List<View> viewList;//view数组
    private ReViewPager viewPager;  //对应的viewPager
    int[] nor_id = {R.mipmap.icon_photo_nor,R.mipmap.icon_position_nor,R.mipmap.icon_chat_nor,R.mipmap.icon_home_nor};
    int[] pre_id = {R.mipmap.icon_photo_pre,R.mipmap.icon_position_pre,R.mipmap.icon_chat_pre,R.mipmap.icon_home_pre};
    LinearLayout mainactivity_layout_photo,mainactivity_layout_communicate,mainactivity_layout_location,mainactivity_layout_house;
    ImageView mainactivity_img_photo,mainactivity_img_location,mainactivity_img_communicate,mainactivity_img_house;
    TextView mainactivity_txt_photo,mainactivity_txt_location,mainactivity_txt_communicate,mainactivity_txt_house;
    List<ImageView> imageViews = new LinkedList<>();
    List<TextView> textViews = new LinkedList<>();

    //抽屉块
    LinearLayout infomation_layout_mycircle;
    LinearLayout infomation_layout_newcircle;
    LinearLayout infomation_layout_joincircle;
    LinearLayout infomation_layout_invitecircle;
    LinearLayout infomation_layout_sharecircle;
    LinearLayout infomation_layout_fedback;
    public final static int INFOMATION_MYCICLE = 0X001;
    public final static int INFOMATION_NEWCICLE = 0X002;
    public final static int INFOMATION_JOINCICLE = 0X003;
    public final static int INFOMATION_INVITECICLE = 0X004;
    public final static int INFOMATION_SHARECICLE = 0X005;
    public final static int INFOMATION_FEDBACK = 0X005;
    DrawerLayout drawer;
    LinearLayout mainactivity_layout_main;
    LinearLayout mainactivity_layout_left;
    boolean status = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Map
        mLocationClient = new LocationClient(this);
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        initLocationOption();
        mLocationClient.start();



        VariableDataUtil.groupname = getIntent().getStringExtra("groupname");
        VariableDataUtil.phone = getIntent().getStringExtra("phone");
        VariableDataUtil.MYID = getIntent().getStringExtra("username");

        setActionBar();
        initViews();

        getUsers();
        showUsers();
        registBroadcast();
        openService();
        setChangeTab();

        setPersonInfomation();

//可以删除的延迟进入
        if (checkPermissions().size() == 0) {
//            delayEnter();
        } else {
            requestPermissions();
        }

    }

    @Override
    protected void onDestroy() {
        stopService(serviceIntent);
        super.onDestroy();


    }
    @Override
    public void onBackPressed() {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addCategory(Intent.CATEGORY_HOME);
            startActivity(i);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Helper_User_Info helper_User_Info = new Helper_User_Info(MainActivity.this);
        SQLiteDatabase sqLiteDatabase = helper_User_Info.getWritableDatabase();
        if (Util.tabbleIsExist(sqLiteDatabase,Bean_User_Info.TABLE_NAME)){
            ContentValues cV = new ContentValues();
            cV.put(Bean_User_Info.PHONE,VariableDataUtil.phone);
            cV.put(Bean_User_Info.USERNAME,VariableDataUtil.MYID);
            cV.put(Bean_User_Info.GROUPNAME,VariableDataUtil.groupname);
            sqLiteDatabase.update(Bean_User_Info.TABLE_NAME,cV,null,null);
        }
        switch (resultCode){
            case INFOMATION_MYCICLE:
                status = false;
                drawer.closeDrawer(Gravity.LEFT,false);
                getUsers();
                showUsers();
                setPersonInfomation();
                break;
            case INFOMATION_NEWCICLE:
                status = false;
                drawer.closeDrawer(Gravity.LEFT,false);
                getUsers();
                showUsers();
                setPersonInfomation();
                break;
            case INFOMATION_JOINCICLE:
                status = false;
                drawer.closeDrawer(Gravity.LEFT,false);
                getUsers();
                showUsers();
                setPersonInfomation();
                break;
            case INFOMATION_INVITECICLE:
                status = false;
                drawer.closeDrawer(Gravity.LEFT,false);
                break;
            case INFOMATION_SHARECICLE:
                status = false;
                drawer.closeDrawer(Gravity.LEFT,false);
                break;


        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
    //初始化所有组件
    private void initViews(){

        //个人信息
        mainactivity_left_txt_phone = findViewById(R.id.mainactivity_left_txt_phone);
        mainactivity_left_txt_username = findViewById(R.id.mainactivity_left_txt_username);
        mainactivity_left_txt_groupname = findViewById(R.id.mainactivity_left_txt_groupname);

        //ActionBar块
        mainactivity_actionbar_txt_title = findViewById(R.id.mainactivity_actionbar_txt_title);
        mainactivity_actionbar_img_headimg = findViewById(R.id.mainactivity_actionbar_img_headimg);
        mainactivity_actionbar_img_headimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });
        mainactivity_actionbar_img_right = findViewById(R.id.mainactivity_actionbar_img_right);
        mainactivity_actionbar_img_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Infomation_sharecircle_share.class);
                intent.putExtra("groupcode",VariableDataUtil.groupcode);
                startActivity(intent);
            }
        });

        //切换标签块
        inflater = LayoutInflater.from(this);//getLayoutInflater();
        viewPager = (ReViewPager) findViewById(R.id.viewpager);


        view1 = inflater.inflate(R.layout.activity_album,null);
//        initPhotos();
//        initRecylerView();


        view2 = inflater.inflate(R.layout.chat, null);
        SDKInitializer.initialize(getApplicationContext());
        view3 = inflater.inflate(R.layout.activity_map,null);
        view4= inflater.inflate(R.layout.ai_home,null);


        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
        viewList.add(view1);
        viewList.add(view3);
        viewList.add(view2);
        viewList.add(view4);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                for (int j = 0;j <imageViews.size();j++){
                    if (i == j){
                        imageViews.get(j).setImageResource(pre_id[j]);
                        textViews.get(j).setTextColor(Color.parseColor("#0bb399"));
                    }else {
                        imageViews.get(j).setImageResource(nor_id[j]);
                        textViews.get(j).setTextColor(Color.parseColor("#999999"));
                    }

                }
                if (i == 1){
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); //关闭手势滑动
                }else {
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED); //打开手势滑动

                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        mainactivity_layout_photo =findViewById(R.id.mainactivity_layout_photo);
        mainactivity_img_photo = findViewById(R.id.mainactivity_img_photo);
        mainactivity_txt_photo = findViewById(R.id.mainactivity_txt_photo);

        mainactivity_layout_location = findViewById(R.id.mainactivity_layout_location);
        mainactivity_img_location = findViewById(R.id.mainactivity_img_location);
        mainactivity_txt_location = findViewById(R.id.mainactivity_txt_location);

        mainactivity_layout_communicate = findViewById(R.id.mainactivity_layout_communicate);
        mainactivity_img_communicate = findViewById(R.id.mainactivity_img_communicate);
        mainactivity_txt_communicate = findViewById(R.id.mainactivity_txt_communicate);

        mainactivity_layout_house = findViewById(R.id.mainactivity_layout_house);
        mainactivity_img_house = findViewById(R.id.mainactivity_img_house);
        mainactivity_txt_house = findViewById(R.id.mainactivity_txt_house);

        imageViews.clear();
        textViews.clear();
        imageViews.add(mainactivity_img_photo);
        imageViews.add(mainactivity_img_location);
        imageViews.add(mainactivity_img_communicate);
        imageViews.add(mainactivity_img_house);
        textViews.add(mainactivity_txt_photo);
        textViews.add(mainactivity_txt_location);
        textViews.add(mainactivity_txt_communicate);
        textViews.add(mainactivity_txt_house);


        mainactivity_layout_photo.setOnClickListener(new Tap());
        mainactivity_layout_communicate.setOnClickListener(new Tap());
        mainactivity_layout_location.setOnClickListener(new Tap());
        mainactivity_layout_house.setOnClickListener(new Tap());

        //抽屉块
        infomation_layout_mycircle = findViewById(R.id.infomation_layout_mycircle);
        infomation_layout_newcircle = findViewById(R.id.infomation_layout_newcircle);
        infomation_layout_joincircle = findViewById(R.id.infomation_layout_joincircle);
        infomation_layout_invitecircle = findViewById(R.id.infomation_layout_invitecircle);
        infomation_layout_sharecircle = findViewById(R.id.infomation_layout_sharecircle);
        infomation_layout_fedback = findViewById(R.id.infomation_layout_fedback);
        infomation_layout_mycircle.setOnClickListener(new Infomation());
        infomation_layout_newcircle.setOnClickListener(new Infomation());
        infomation_layout_joincircle.setOnClickListener(new Infomation());
        infomation_layout_invitecircle.setOnClickListener(new Infomation());
        infomation_layout_sharecircle.setOnClickListener(new Infomation());
        infomation_layout_fedback.setOnClickListener(new Infomation());
        mainactivity_layout_main = findViewById(R.id.mainactivity_layout_main);
        mainactivity_layout_left = findViewById(R.id.mainactivity_layout_left);
        drawer = findViewById(R.id.sucess_drawer);

        //列表块
        chat_list_users = view2.findViewById(R.id.chat_list_users);

        //侧滑
        drawer.setDrawerListener(new Gest());
        setDrawerLeftEdgeSize(this, drawer,0.5f);

    }
    //获取
    private void getUsers(){
        names.clear();
        msgs.clear();
        names.add(VariableDataUtil.groupname);
        msgs.add(" ");
//        names.add("小明");
//        msgs.add("聊天信息");
//        names.add("小红");
//        msgs.add("聊天信息");
//        names.add("老明");
//        msgs.add("聊天信息");
    }
    //显示聊天成员
    private void showUsers(){
        data.clear();
        for (int i = 0; i < names.size(); i++){
            Map<String, Object> item = new HashMap<>();
            // item.put("img", R.mipmap.classroom);
            item.put("name", names.get(i));
            Log.e("momo","name"+names.get(i));
            item.put("msg", msgs.get(i));
            Log.e("momo","msg"+msgs.get(i));
            data.add(item);
        }
        //获得simpleAdapter
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,data,R.layout.chat_style,new String[]{ "name","msg"},new int[]{R.id.chat_style_txt_name,R.id.chat_style_txt_msg});
        Log.e("momo","simpleAdapter OK");
        chat_list_users.setAdapter(simpleAdapter);
        Log.e("momo","set Adapter OK");
        chat_list_users.setOnItemClickListener(new getInChatMsg());

    }
    //设置个人信息显示
    private void setPersonInfomation() {
        mainactivity_left_txt_groupname.setText(VariableDataUtil.groupname);
        mainactivity_actionbar_txt_title.setText(VariableDataUtil.groupname);
        mainactivity_left_txt_phone.setText(VariableDataUtil.phone);
        mainactivity_left_txt_username.setText(VariableDataUtil.MYID);
    }
    //设置标签切换
    private void setChangeTab(){
        PagerAdapter pagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return viewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // TODO Auto-generated method stub
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                container.addView(viewList.get(position));
// added here
                if(position==0){
                    initPhotos();
                    initRecylerView();
                }

                if(position==1){
                    AskforLocationPermission();
                    initLocationinfo();
                    initLocationRecyclerView();
                }
                return viewList.get(position);
            }
        };
        viewPager.setAdapter(pagerAdapter);
        viewPager.setNoScroll(true);
    }
    //注册广播
    private void registBroadcast(){
        msgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(VariableDataUtil.Code_Filter_From_Android_Service);
        registerReceiver(msgReceiver,intentFilter);
    }
    //打开服务
    private void openService(){
        serviceIntent = new Intent(this,GetMsgService.class);
        startService(serviceIntent);
    }
    //转入私人聊天
    class getInChatMsg implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            OTHERID = new String(names.get(position));
            Log.e("momo",OTHERID);
            Intent intent = new Intent(MainActivity.this,Chat_msg.class);
            intent.putExtra("otherid",OTHERID);
            if (position <1){
                intent.putExtra("isgroup",true);
            }
            startActivity(intent);
        }
    }
    //广播接收器
    class MsgReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra("msg");
            if(viewPager.getCurrentItem() != 3){
                BadgeView badgeView3 = new BadgeView(MainActivity.this);
                badgeView3.setTargetView(mainactivity_img_communicate);
                badgeView3.setBadgeGravity(Gravity.TOP | Gravity.LEFT);
                badgeView3.setTypeface(Typeface.create(Typeface.SANS_SERIF,
                        Typeface.ITALIC));
                badgeView3.setShadowLayer(2, -1, -1, Color.GREEN);
//                badgeView3.setBadgeCount(2);
            }else {
                BadgeView badgeView3 = new BadgeView(MainActivity.this);
                badgeView3.setTargetView(chat_list_users.getAdapter().getView(0,null,null));
                badgeView3.setBadgeGravity(Gravity.TOP | Gravity.LEFT);
                badgeView3.setTypeface(Typeface.create(Typeface.SANS_SERIF,
                        Typeface.ITALIC));
                badgeView3.setShadowLayer(2, -1, -1, Color.GREEN);
                badgeView3.setBadgeCount(GetMsgService.groupMsgNum);
            }
        }
    }
    //切换标签
    class Tap implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.mainactivity_layout_photo:
                    viewPager.setCurrentItem(0,false);
                    break;

                case R.id.mainactivity_layout_location:
                    viewPager.setCurrentItem(1,false);
                    break;
                case R.id.mainactivity_layout_communicate:
                    viewPager.setCurrentItem(2,false);
                    break;
                case R.id.mainactivity_layout_house:
                    viewPager.setCurrentItem(3,false);
                    break;
            }
        }
    }
    //主界面跟随抽屉动
    private class Gest implements DrawerLayout.DrawerListener {

        @Override
        public void onDrawerSlide(@NonNull View view, float v) {
            WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            mainactivity_layout_main.layout(mainactivity_layout_left.getRight(),0, mainactivity_layout_left.getRight()+display.getWidth(),display.getHeight());
        }

        @Override
        public void onDrawerOpened(@NonNull View view) {
            status = false;
        }

        @Override
        public void onDrawerClosed(@NonNull View view) {
            status = true;
        }

        @Override
        public void onDrawerStateChanged(int i) {

        }
    }
    private void setDrawerLeftEdgeSize (Activity activity, DrawerLayout drawerLayout, float displayWidthPercentage) {
        if (activity == null || drawerLayout == null) return;
        try {
            // 找到 ViewDragHelper 并设置 Accessible 为true
            Field leftDraggerField =
                    drawerLayout.getClass().getDeclaredField("mLeftDragger");//Right
            leftDraggerField.setAccessible(true);
            ViewDragHelper leftDragger = (ViewDragHelper) leftDraggerField.get(drawerLayout);

            // 找到 edgeSizeField 并设置 Accessible 为true
            Field edgeSizeField = leftDragger.getClass().getDeclaredField("mEdgeSize");
            edgeSizeField.setAccessible(true);
            int edgeSize = edgeSizeField.getInt(leftDragger);

            // 设置新的边缘大小
            Point displaySize = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(displaySize);
            edgeSizeField.setInt(leftDragger, Math.max(edgeSize, (int) (displaySize.x *
                    displayWidthPercentage)));
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
    }
    //个人信息
    class Infomation implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.infomation_layout_mycircle:
                    Intent intent = new Intent(MainActivity.this,Infomation_mycircle.class);
                    startActivityForResult(intent,INFOMATION_MYCICLE);
                    break;
                case R.id.infomation_layout_newcircle:
                    Intent intent2 = new Intent(MainActivity.this,Infomation_newcircle.class);
                    startActivityForResult(intent2,INFOMATION_NEWCICLE);
                    break;
                case R.id.infomation_layout_joincircle:
                    Intent intent3 = new Intent(MainActivity.this,Infomation_joincircle.class);
                    startActivityForResult(intent3,INFOMATION_JOINCICLE);
                    break;
                case R.id.infomation_layout_invitecircle:
                    Intent intent4 = new Intent(MainActivity.this,Infomation_invitecircle.class);
                    startActivityForResult(intent4,INFOMATION_INVITECICLE);
                    break;
                case R.id.infomation_layout_sharecircle:
//                    Intent intent5 = new Intent(MainActivity.this,Infomation_sharecircle.class);
//                    startActivityForResult(intent5,INFOMATION_SHARECICLE);
                    Intent intent5 = new Intent(Intent.ACTION_SEND);
                    intent5.setData(Uri.parse("http://baidu.com"));
                    intent5.setType("text/plain");
                    intent5.putExtra(Intent.EXTRA_SUBJECT, "分享邀请码111");
                    intent5.putExtra(Intent.EXTRA_TEXT, "http://baidu.com");//extraText为文本的内容
                    intent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//为Activity新建一个任务栈
                    startActivity(Intent.createChooser(intent5, "分享小圈圈"));//R.string.action_share同样是标题
                    break;
                case R.id.infomation_layout_fedback:
                    Intent intent6 = new Intent(MainActivity.this,Infomation_fedback.class);
                    startActivity(intent6);
                    break;
            }
        }
    }

    //album

    protected void setMainPhoto(){
        //todo: set the main photo from the album.
    };
    //初始化照片
    protected void initPhotos(){
        //todo connet to server and get the photos...

        mMainPhoto = findViewById(R.id.id_photo_main);
        mPhotos = new ArrayList<String>();

//        mPhotos.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1554575647376&di=652f71607147268186a1277534ba3389&imgtype=0&src=http%3A%2F%2Fimg.hanyouwang.com%2F2016%2F0727%2F1469590484402184.jpg");
//        mPhotos.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2421291562,4243392474&fm=26&gp=0.jpg");
//        mPhotos.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555170436&di=a8aac80e4fede5ad3e8049c5c6c05ef6&imgtype=jpg&er=1&src=http%3A%2F%2Fwww.fsfl.gov.cn%2Fzwgk_1028934%2Fywdd%2Fesb%2F201806%2FW020180621616804174336.jpg");
////        mPhotos.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555170731&di=a84849556595b15be78d0c5672f58430&imgtype=jpg&er=1&src=http%3A%2F%2Fimg0.pcauto.com.cn%2Fpcauto%2F1606%2F12%2F8304663_7_thumb.jpg");
//        mPhotos.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1554576119182&di=81ecb2d87490e2f00a9bdc7c56e74679&imgtype=0&src=http%3A%2F%2Fi5.hexunimg.cn%2F2013-07-10%2F155988523.jpg");
//
//        mPhotos.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2746132109,1984684096&fm=26&gp=0.jpg");
//        mPhotos.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2804163780,368434744&fm=26&gp=0.jpg");
//        mPhotos.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555171797&di=f0b71b4f25545e356600e51e484ea88d&imgtype=jpg&er=1&src=http%3A%2F%2Fmmbiz.qpic.cn%2Fmmbiz_jpg%2FnyLy94YslNJgUQLFtDBPS3LGAibZw2eHAZpuPwyMMzcBWCsUzwyHUX9s1TmQozTT7HnUlUVBPN6KiaXc9icE67tFw%2F640%3Fwx_fmt%3Djpeg");
//        mPhotos.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2378400466,2541887875&fm=26&gp=0.jpg");
//        mPhotos.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1554577265686&di=86f5df2a683e77399287b07ce77a4184&imgtype=0&src=http%3A%2F%2Fs16.sinaimg.cn%2Fbmiddle%2F540777a7ga65fc30fc5af%26690");
//        mPhotos.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=116558687,3896810209&fm=26&gp=0.jpg");
//        mPhotos.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1554576078505&di=466092630d9cd34c0b97c9f5adb99274&imgtype=0&src=http%3A%2F%2Fd.ifengimg.com%2Fw600_h380%2Fy1.ifengimg.com%2Fa%2F2015_52%2F60a33106973370d.jpg");


        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("groupcode", VariableDataUtil.groupcode);
            jsonDown = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new Thread(){
            @Override
            public void run() {
                postForAlbum();
            }
        }.start();

        /*********************************************2*/
        //random
        if(mPhotos.size()>0) {
            Picasso.with(MainActivity.this)
                    .load(mPhotos.get(ra.nextInt(mPhotos.size() - 1))).placeholder(R.color.lightBlack)
                    .error(R.drawable.icon_photo_nor)
                    .into(mMainPhoto);
        }
        mMainPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPhotos.size()>0) {
                    Picasso.with(MainActivity.this)
                            .load(mPhotos.get(ra.nextInt(mPhotos.size() - 1))).placeholder(R.color.lightBlack)
                            .error(R.drawable.icon_photo_nor)
                            .into(mMainPhoto);
                }
            }
        });
         /********************************************/
    }
    //上传到相册
    public void postForAlbum(){
        sendPostUtil.postRequest(VariableDataUtil.requestAddress +"/picture/down",jsonDown ,"utf-8", new sendPostUtil.OnResponseListner() {
            @Override
            public void onSucess(String response) {
                Message message = new Message();
                message.obj = response;
                handlerAlbum.sendMessage(message);
            }

            @Override
            public void onError(String error) {
                Message message = new Message();
                message.obj = error;
                handlerAlbum.sendMessage(message);
            }
        });
    }
    Handler handlerAlbum = new Handler(){
        @Override
        public void handleMessage(Message msg){
//            Toast.makeText(MainActivity.this, msg.toString(), Toast.LENGTH_SHORT).show();
            Log.d("Tag", "Album Address: "+ (String) msg.obj);
            if (msg.obj != null){
                try {
                    JSONObject jsonObject = new JSONObject((String)msg.obj);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int index = 0; index<jsonArray.length(); index++){
                        JSONObject ImageObject = jsonArray.getJSONObject(index);
                        mPhotos.add(VariableDataUtil.requestAddress+"/images/"+ImageObject.getString("groupcode")+"/"+ImageObject.getString("url"));
                        Log.d("Tag", "Picture Address::::: "+ VariableDataUtil.requestAddress+"/images/"+ImageObject.getString("url"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(mPhotos.size() <= 0){
//            mRecyclerView.setBackgroundResource(R.drawable.icon_background);
                    findViewById(R.id.rl_begin).setVisibility(View.VISIBLE);
                } else{
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    public void initRecylerView(){
        mAdapter = new HomeAdapter();
        mRecyclerView = findViewById(R.id.id_album_recyclerView);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        ImageView fab = (ImageView) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, SelectPhotoActivity.class);
                startActivity(intent);
            }
        });
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>
    {

        @Override
        public HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            HomeAdapter.MyViewHolder holder = new HomeAdapter.MyViewHolder(LayoutInflater.from(
                    MainActivity.this).inflate(R.layout.content_photo_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(HomeAdapter.MyViewHolder holder, int position)
        {
//            String path = "http://img.hexun.com/2011-06-21/130726386.jpg";

            Picasso.with(MainActivity.this)
                    .load(mPhotos.get(position)).placeholder(R.color.lightBlack)
                    .error(R.drawable.icon_photo_nor)
                    .into(holder.mIV);
        }

        @Override
        public int getItemCount() {
            return mPhotos.size();
//            return fileNames.length;
        }
        class MyViewHolder extends RecyclerView.ViewHolder
        {
            //            TextView tv;
            private ImageView mIV;
            public MyViewHolder(View view)
            {
                super(view);
//                tv = (TextView) view.findViewById(R.id.id_num);
                mIV = view.findViewById(R.id.iv_main_content);

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    /**
     * permissions for whole app
     */
    private List<String> checkPermissions() {
        List<String> permissions = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_PHONE_STATE);
        }
        return permissions;
    }


    private void requestPermissions() {
        List<String> permissions = checkPermissions();
        if (permissions.size() > 0) {
            ActivityCompat.requestPermissions(this, permissions.toArray(new String[permissions.size()]), 0);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                break;
            }
        }
    }

    // MAP
    //地图
    private void initLocationOption() {
        //定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
//        LocationClient locationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类实例并配置定位参数
        LocationClientOption locationOption = new LocationClientOption();
        MyLocationListener myLocationListener = new MyLocationListener();
        //注册监听函数
        mLocationClient.registerLocationListener(myLocationListener);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        locationOption.setCoorType("gcj02");
        //可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        locationOption.setNeedDeviceDirect(false);
        //可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        locationOption.setLocationNotify(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        locationOption.setIgnoreKillProcess(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        locationOption.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        locationOption.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        locationOption.SetIgnoreCacheException(false);
        //可选，默认false，设置是否开启Gps定位
        locationOption.setOpenGps(true);
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
        locationOption.setOpenAutoNotifyMode();
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
        locationOption.setOpenAutoNotifyMode(3000,1, LocationClientOption.LOC_SENSITIVITY_HIGHT);
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        mLocationClient.setLocOption(locationOption);
        //开始定位
        mLocationClient.start();
    }


    /**
     * 实现定位回调
     */
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            Log.d("Tag", "Location started");
            if(location.getLatitude() != 4.9E-324){
                //获取纬度信息
                mLatitude = location.getLatitude();
                //获取经度信息
                mLongitude = location.getLongitude();
            }
            Log.d("Tag", "Location: "+mLatitude+","+mLongitude);
            //获取定位精度，默认值为0.0f
            float radius = location.getRadius();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            String coorType = location.getCoorType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            int errorCode = location.getLocType();
        }
    }


    /**********************************************************3*/
    public void initLocationinfo(){
        mMap = findViewById(R.id.mv_baidu_map);
        mMap.showZoomControls(false); //hide the zoom button
        mBaiduMap = mMap.getMap();

        //todo get the online information;
        //接收定位信息
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("groupcode",VariableDataUtil.groupcode);
            jsonDown = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new Thread(){
            @Override
            public void run() {
                postForLocation();// get other location
            }
        }.start();

        mLocationAdapter = new LocationAdapter();
        mLocationList = new ArrayList<LocationInfo>();
        LatLng tmpLocation = new LatLng(mLatitude,mLongitude);
        LocationInfo tmp = new LocationInfo(VariableDataUtil.phone, false, (R.mipmap.user_img1), tmpLocation);
        mLocationList.add(tmp);

        Log.d("Tag", "Location List size: "+mLocationList.size());

        //在这里添加switch定位开关信息
        mBtnLocation = findViewById(R.id.btn_location);
        mBtnLocation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mLocationList.get(0).infoState = !mLocationList.get(0).infoState;
                mLocationAdapter.notifyDataSetChanged();
                mBtnLocation.setImageResource(mLocationList.get(0).infoState?
                        R.mipmap.position_switch_position_open:R.mipmap.position_switch_position_close);
                //TODO 中断或者开启定位信息传输
                //判断状态，开启定位timer或关闭。
                if(mLocationList.get(0).infoState){
                    startLocation();
                    mLocationAdapter.notifyItemChanged(0);
                    MapStatus mMapStatus = new MapStatus.Builder()
                            .target(mLocationList.get(0).location)
                            .zoom(12)
                            .build();
                    MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                    mBaiduMap.setMapStatus(mMapStatusUpdate);
                }else{
                    stopLocation();
                }
            }
        });

        //控制边界显示所有 marker
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(LocationInfo locationinfo: mLocationList) {
            builder.include(locationinfo.location);
        }// 几内亚湾 0，0
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(builder.build());
//        u = MapStatusUpdateFactory.newLatLngZoom(20);
        mBaiduMap.setMapStatus(u);
        u = MapStatusUpdateFactory.zoomTo(6);
        mBaiduMap.setMapStatus(u);
        //
    }
    /**********************************************************/

    public void startLocation() {
        //更改本地定位数据
        mLocationList.get(0).location = new LatLng(mLatitude, mLongitude);
        //定时传输定位
        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("time", ""+System.currentTimeMillis());
                        jsonObject.put("longitude", mLongitude);
                        jsonObject.put("latitude", mLatitude);
                        jsonObject.put("phone", VariableDataUtil.phone);
                        jsonObject.put("groupcode", VariableDataUtil.groupcode);
                        jsonUp = jsonObject.toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new Thread() {
                        @Override
                        public void run() {
                            postUpLocation(); //get other location
                        }
                    }.start();
                }
            };
        }
        if(mTimer != null && mTimerTask != null )
            mTimer.schedule(mTimerTask, 0, 15000*60);
    }

    public void stopLocation(){
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    public void postForLocation(){
        sendPostUtil.postRequest(VariableDataUtil.requestAddress +"/position/down"
                ,jsonDown ,"utf-8", new sendPostUtil.OnResponseListner() {
            @Override
            public void onSucess(String response) {
                Message message = new Message();
                message.obj = response;
                handlerMap.sendMessage(message);
            }

            @Override
            public void onError(String error) {
                Message message = new Message();
                message.obj = error;
                handlerMap.sendMessage(message);
            }
        });
    }

    public void postUpLocation(){
        sendPostUtil.postRequest(VariableDataUtil.requestAddress +"/position/up"
                ,jsonUp ,"utf-8", new sendPostUtil.OnResponseListner() {
                    @Override
                    public void onSucess(String response) {
                        Message message = new Message();
                        message.obj = response;
                        handlerLocation.sendMessage(message);
                    }

                    @Override
                    public void onError(String error) {
                        Message message = new Message();
                        message.obj = error;
                        handlerLocation.sendMessage(message);
                    }
                });
    }

    Handler handlerMap = new Handler(){
        @Override
        public void handleMessage(Message msg){
            Log.d("Tag", "Location Address:" + (String)msg.obj);
            if (msg.obj != null){
                try {
                    LocationInfo tmp;
                    JSONObject jsonObject = new JSONObject((String)msg.obj);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int index = 0; index<jsonArray.length(); index++){
                        JSONObject LocationObject = jsonArray.getJSONObject(index);
                        if(!LocationObject.getString("latitude").equals("null")
                                && !LocationObject.getString("longitude").equals("null")) {
                            Float lat = parseFloat(LocationObject.getString("latitude"));
                            Float lng = parseFloat(LocationObject.getString("longitude"));
                            LatLng tmpLocation = new LatLng(lat, lng);
                            if(LocationObject.getString("phone").equals(VariableDataUtil.phone)){
                                mLocationList.get(0).location = tmpLocation;
                            }else {
                                tmp = new LocationInfo(LocationObject.getString("phone")
                                        , true, (R.mipmap.user_img4), tmpLocation);
                                mLocationList.add(tmp);
                            }
                        }
                    }
                    mLocationAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    // 上传定位handler
    Handler handlerLocation = new Handler(){
        @Override
        public void handleMessage(Message msg){
//            Toast.makeText(MainActivity.this, msg.toString(), Toast.LENGTH_LONG).show();
        }
    };

    public void initLocationRecyclerView(){
        mLocationBar = findViewById(R.id.rv_location_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mLocationBar.setLayoutManager(linearLayoutManager);
        mLocationBar.setAdapter(mLocationAdapter);
        mLocationBar.setItemAnimator(new DefaultItemAnimator());

    }

    class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.MyViewHolder>{

        @Override
        public LocationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LocationAdapter.MyViewHolder holder = new LocationAdapter.MyViewHolder(LayoutInflater.from(
                    MainActivity.this).inflate(R.layout.location_list_item, parent, false));
            return holder;
        }

        @Override
        public int getItemCount(){
            return mLocationList.size();
        }

        @Override
        public void onBindViewHolder(LocationAdapter.MyViewHolder holder, int position){
            OnClickLocationHolder listener = new OnClickLocationHolder(holder);
            holder.mIV.setOnClickListener(listener);


            //TODO Read info from mLocationList and init the head;
            holder.mIV.setImageResource(mLocationList.get(position).portraitPath);
            holder.mIVState.setVisibility(mLocationList.get(position).infoState? View.VISIBLE:View.INVISIBLE);
            holder.mTV.setText(mLocationList.get(position).username);
//            holder.mIV.;

            //todo: 如果同名，则更新list的值

            // todo: init the icon on the map
            // 生成marker bitmap
            if(position==mLocationList.size()-1||position == 0) {
                mBaiduMap.clear();
                mMap.getOverlay().clear();
                mBaiduMap = mMap.getMap();
                for(int index = 0; index<mLocationList.size();index++) {
                    View view = View.inflate(getApplicationContext(), R.layout.map_bean, null);
                    ivHeadBean = view.findViewById(R.id.iv_bean_head);
                    ivBgBean = view.findViewById(R.id.iv_bean_bg);
                    ivBgBean.setImageResource(mLocationList.get(index).infoState ?
                            R.mipmap.position_bg_position_open
                            : R.mipmap.position_bg_position_close);
                    ivHeadBean.setImageResource(mLocationList.get(index).portraitPath); //设置头像
                    BitmapDescriptor descriptor = BitmapDescriptorFactory.fromView(view);
                    OverlayOptions options = new MarkerOptions()
                            .position(mLocationList.get(index).location)
                            .icon(descriptor).zIndex(9).draggable(true)
                            .animateType(MarkerOptions.MarkerAnimateType.grow);
                    mBaiduMap.addOverlay(options);
                }
            }
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            private ImageView mIV;
            private ImageView mIVState;
            private TextView mTV;
            public MyViewHolder(View view){
                super(view);
                mIV = view.findViewById(R.id.iv_location_item);
                mIVState = view.findViewById(R.id.iv_location_item_state);
                mTV = view.findViewById(R.id.tv_location_item);
            }
        }

        private class OnClickLocationHolder implements View.OnClickListener{
            MyViewHolder holder;

            public OnClickLocationHolder(MyViewHolder holder){
                this.holder = holder;
            }

            @Override
            public void onClick(View v){
                int position = holder.getAdapterPosition();
                MapStatus mMapStatus = new MapStatus.Builder()
                        .target(mLocationList.get(position).location)
                        .zoom(12)
                        .build();
                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                mBaiduMap.setMapStatus(mMapStatusUpdate);

                //todo 重新出现marker

            }
        }
    }
    private static final int LOCATION_CODE = 1;
    private LocationManager lm;
    public void AskforLocationPermission(){
        lm = (LocationManager) MainActivity.this.getSystemService(MainActivity.this.LOCATION_SERVICE);
        boolean ok = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (ok) {//开了定位服务
            openGPSSEtting();
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "请授予定位权限", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);
            } else {
                // 有权限了，去放肆吧。
                    Toast.makeText(MainActivity.this, "已获取定位权限", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("BRG","系统检测到未开启GPS定位服务");
            Toast.makeText(MainActivity.this, "系统检测到未开启GPS定位服务", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 1315);
        }
    }
    private boolean checkGpsIsOpen() {
        boolean isOpen;
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isOpen;
    }

    private int GPS_REQUEST_CODE = 1;
    private void openGPSSEtting() {
        if (checkGpsIsOpen()){
//            Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
        }else {
            new AlertDialog.Builder(this).setTitle("open GPS")
                    .setMessage("go to open")
                    //  取消选项
                    .setNegativeButton("cancel",new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Toast.makeText(MainActivity.this, "close", Toast.LENGTH_SHORT).show();
                            // 关闭dialog
                            dialogInterface.dismiss();
                        }
                    })
                    //  确认选项
                    .setPositiveButton("setting", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //跳转到手机原生设置页面
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, GPS_REQUEST_CODE);
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    }

}
