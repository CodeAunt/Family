package family.momo.com.family.chat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import com.alibaba.idst.util.NlsClient;
import com.alibaba.idst.util.SpeechTranscriberWithRecorder;
import com.alibaba.idst.util.SpeechTranscriberWithRecorderCallback;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import family.momo.com.family.DefinedViews.PartyRoomLayout;
import family.momo.com.family.R;
import family.momo.com.family.util.SoftHideKeyBoardUtil;

import static family.momo.com.family.chat.ChannelActivity.channelActivity;

public class PartyRoomActivity extends AppCompatActivity {
    private static final String TAG="HsiaoTienSpeechDemo";

    private PartyRoomLayout mPartyRoomLayout;
    private LinearLayout buttonLinearLayout;
    private boolean isVolumeOff;
    private TextView mChannel,volumeTextView;

    private ImageView mEndCall,volumeOff,swithCamere;

    private static MyHandler handler;
    private NlsClient client;
    private SpeechTranscriberWithRecorder speechTranscriber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_room);

        setActionBar();
        buttonLinearLayout = (LinearLayout) findViewById(R.id.buttonLinearLayout);
        mPartyRoomLayout = (PartyRoomLayout) findViewById(R.id.party_room_layout);

        mChannel = (TextView) findViewById(R.id.channel);
        volumeTextView = (TextView) findViewById(R.id.volumeTextView);
        String channel = getIntent().getStringExtra("Channel");
        mChannel.setText(channel);
        isVolumeOff = false;
        mEndCall =  findViewById(R.id.end_call);
        mEndCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AgoraManager裡面封裝了掛斷的API, 退出頻道
                stopTranscribe();
                AgoraManager.getInstance().leaveChannel();
                channelActivity.finish();
                finish();
            }
        });

        volumeOff =  findViewById(R.id.volume_off);
        volumeOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isVolumeOff){
                    volumeOff.setImageResource(R.mipmap.btn_volum_off_web);
                    volumeTextView.setText("靜音");

                }else{
                    volumeOff.setImageResource(R.mipmap.btn_volum_on_web);
                    volumeTextView.setText("擴音");
                }
                isVolumeOff=!isVolumeOff;
            AgoraManager.getInstance().onLocalAudioMuteClicked(isVolumeOff);
            }
        });

        swithCamere = findViewById(R.id.switchcamera);
        swithCamere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgoraManager.getInstance().onSwitchCameraClicked();
            }
        });

        //設置前置攝像頭預覽並開啟
        AgoraManager.getInstance()
                .setupLocalVideo(getApplicationContext())
                .setOnPartyListener(mOnPartyListener)
                .joinChannel(channel)
                .startPreview();
        //將攝像頭預覽的SurfaceView加入PartyRoomLayout

        mPartyRoomLayout.addView(AgoraManager.getInstance().getLocalSurfaceView());


        // 第一步，创建client实例，client只需要创建一次，可以用它多次创建transcriber
        client = new NlsClient();
        //UI在主线程更新
        handler= new MyHandler(this);
        startTranscribe();

    }

    @Override
    public void onDestroy(){
        if (speechTranscriber != null){
            speechTranscriber.stop();
        }

        // 最终，退出时释放client
        client.release();
        super.onDestroy();
    }


    // 此方法内启动录音和识别逻辑，为了代码简单便于理解，没有加防止用户重复点击的逻辑，用户应该在真实使用场景中注意
    public void startTranscribe(){

        mChannel.setText("");

        // 第二步，新建识别回调类
        SpeechTranscriberWithRecorderCallback callback = new MyCallback();

        // 第三步，创建识别request
        speechTranscriber = client.createTranscriberWithRecorder(callback);

        // 第四步，设置相关参数
        // 请使用https://help.aliyun.com/document_detail/72153.html 动态生成token
        speechTranscriber.setToken("b3f4910abb084a72bdf2fa37f97715cc");
        // 请使用阿里云语音服务管控台(https://nls-portal.console.aliyun.com/)生成您的appkey
        speechTranscriber.setAppkey("gwmaIKMM6wJIwg95");
        // 设置返回中间结果，更多参数请参考官方文档
        speechTranscriber.enableIntermediateResult(true);
        speechTranscriber.enablePunctuationPrediction(true);
        speechTranscriber.start();
    }
    public void stopTranscribe(){
        // 第八步，停止本次识别
        speechTranscriber.stop();
    }



    private AgoraManager.OnPartyListener mOnPartyListener = new AgoraManager.OnPartyListener() {
        @Override
        public void onJoinChannelSuccess(String channel, final int uid) {
        }

        @Override
        public void onGetRemoteVideo(final int uid) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //注意SurfaceView要創建在主線程
                    AgoraManager.getInstance().setupRemoteVideo(PartyRoomActivity.this, uid);
                    mPartyRoomLayout.addView(AgoraManager.getInstance().getSurfaceView(uid));
                    mPartyRoomLayout.removeView(AgoraManager.getInstance().getLocalSurfaceView());
                    mPartyRoomLayout.addView(AgoraManager.getInstance().getLocalSurfaceView());
                }
            });
        }

        @Override
        public void onLeaveChannelSuccess() {
        }

        @Override
        public void onUserOffline(final int uid) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //從PartyRoomLayout移除遠程視頻的SurfaceView
                    mPartyRoomLayout.removeView(AgoraManager.getInstance().getSurfaceView(uid));
                    //清除缓存的SurfaceView
                    AgoraManager.getInstance().removeSurfaceView(uid);
                }
            });
        }
    };

    /**
     * 返回时退出频道
     */
    @Override
    public void onBackPressed() {

        AgoraManager.getInstance().leaveChannel();
        if (speechTranscriber != null){
            speechTranscriber.stop();

        }
        channelActivity.finish();
        finish();
    }


    //设置Actionbar和状态栏
    private void setActionBar(){
        View decorView = getWindow().getDecorView();
        //decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);//隐藏状态栏
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getSupportActionBar().hide();//隐藏Actionbar
        SoftHideKeyBoardUtil.assistActivity(this);
    }

    // 语音识别回调类，得到语音识别结果后在这里处理
    //    // 注意不要在回调方法里调用transcriber.stop()方法
    //    // 注意不要在回调方法里执行耗时操作
    private static class MyCallback implements SpeechTranscriberWithRecorderCallback {

        // 识别开始
        @Override
        public void onTranscriptionStarted(String msg, int code)
        {
            Log.d(TAG,"OnTranscriptionStarted " + msg + ": " + String.valueOf(code));
        }

        // 请求失败
        @Override
        public void onTaskFailed(String msg, int code)
        {
            Log.d(TAG,"OnTaskFailed " + msg + ": " + String.valueOf(code));
            handler.sendEmptyMessage(0);
        }

        // 识别返回中间结果，只有开启相关选项时才会回调
        @Override
        public void onTranscriptionResultChanged(final String msg, int code)
        {
            Log.d(TAG,"OnTranscriptionResultChanged " + msg + ": " + String.valueOf(code));
            Message message = new Message();
            message.obj = msg;
            handler.sendMessage(message);
        }

        // 开始识别一个新的句子
        @Override
        public void onSentenceBegin(String msg, int code)
        {
            Log.i(TAG, "Sentence begin");
        }

        // 第七步，当前句子识别结束，得到完整的句子文本
        @Override
        public void onSentenceEnd(final String msg, int code)
        {
            Log.d(TAG,"OnSentenceEnd " + msg + ": " + String.valueOf(code));
            Message message = new Message();
            message.obj = msg;
            handler.sendMessage(message);
        }

        // 识别结束
        @Override
        public void onTranscriptionCompleted(final String msg, int code)
        {
            Log.d(TAG,"OnTranscriptionCompleted " + msg + ": " + String.valueOf(code));
            Message message = new Message();
            message.obj = msg;
            handler.sendMessage(message);
            handler.clearResult();
        }

        // 请求结束，关闭连接
        @Override
        public void onChannelClosed(String msg, int code) {
            Log.d(TAG, "OnChannelClosed " + msg + ": " + String.valueOf(code));
        }

        // 手机采集的语音数据的回调
        @Override
        public void onVoiceData(byte[] bytes, int i) {

        }

        // 手机采集的语音音量大小的回调
        @Override
        public void onVoiceVolume(int i) {

        }
    };

    // 根据识别结果更新界面的代码
    private static class MyHandler extends Handler {
        StringBuilder fullResult = new StringBuilder();
        private final WeakReference<PartyRoomActivity> mActivity;

        public MyHandler(PartyRoomActivity activity) {
            mActivity = new WeakReference<PartyRoomActivity>(activity);
        }

        public void clearResult(){
            this.fullResult = new StringBuilder();
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.obj == null) {
                Log.i(TAG, "Empty message received.");
                return;
            }
            String rawResult = (String)msg.obj;
            String result = null;
            String displayResult = null;
            if (rawResult != null && !rawResult.equals("")){
                JSONObject jsonObject = JSONObject.parseObject(rawResult);
                if (jsonObject.containsKey("payload")){
                    result = jsonObject.getJSONObject("payload").getString("result");
                    int time = jsonObject.getJSONObject("payload").getIntValue("time");
                    if (time != -1) {
                        fullResult.append(result);
                        displayResult = fullResult.toString();
                        fullResult.append("\n");
                    } else {
                        displayResult = fullResult.toString() + result;
                    }
                    System.out.println(displayResult);
                    mActivity.get().mChannel.setText(displayResult);    //完整的對話內容
                }
            }
//            mActivity.get().mChannel.setText(result);   //部分對話內容

        }

    }
}
