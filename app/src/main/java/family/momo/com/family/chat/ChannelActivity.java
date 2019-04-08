package family.momo.com.family.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import family.momo.com.family.R;

/**
 * Created by Leon on 2017/4/6.
 */

public class ChannelActivity extends AppCompatActivity {

    public static ChannelActivity channelActivity;
    private static final String TAG = "ChannelActivity";

    private RecyclerView mRecyclerView;
    private FrameLayout mFrameLayout;
//    private String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        channelActivity = this;
        initRecyclerView();
        mFrameLayout = (FrameLayout) findViewById(R.id.surface_view_container);
        Intent intent = new Intent(this, PartyRoomActivity.class);
        intent.putExtra("Channel", "0");
        startActivity(intent);
    }

    /**
     * 初始化频道列表
     */
    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new ChannelAdapter(this, mockChannelList()));
    }

    /**
     *  模拟频道数据
     */
    private List<String> mockChannelList() {
        List<String> channelItems = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
//            channelItems.add("Channel" + i);
        }
//        channelItems.add("蕭恬");
//        channelItems.add("宋慧宇");
//        channelItems.add("罗艳");
//        channelItems.add("Y'垒");
        return channelItems;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //先清空容器
        mFrameLayout.removeAllViews();
        //設置本地前置攝像頭預覽並啟動
        AgoraManager.getInstance().setupLocalVideo(getApplicationContext()).startPreview();
        //將本地攝像頭預覽的SurfaceView添加到容器中
        mFrameLayout.addView(AgoraManager.getInstance().getLocalSurfaceView());
    }

    /**
     * 停止预览
     */
    @Override
    protected void onPause() {
        super.onPause();
        AgoraManager.getInstance().stopPreview();
    }
}
