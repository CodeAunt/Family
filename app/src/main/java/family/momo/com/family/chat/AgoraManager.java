package family.momo.com.family.chat;

import android.content.Context;
import android.util.SparseArray;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import family.momo.com.family.R;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;


public class AgoraManager {

    private static final String TAG = "AgoraManager";

    public static AgoraManager sAgoraManager;

    private RtcEngine mRtcEngine;

    private OnPartyListener mOnPartyListener;

    private int mLocalUid = 0;

    private AgoraManager() {
        mSurfaceViews = new SparseArray<SurfaceView>();
    }

    private SparseArray<SurfaceView> mSurfaceViews;

    public static AgoraManager getInstance() {
        if (sAgoraManager == null) {
            synchronized (AgoraManager.class) {
                if (sAgoraManager == null) {
                    sAgoraManager = new AgoraManager();
                }
            }
        }
        return sAgoraManager;
    }

    private IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {

        /**
         * 當獲取用戶uid的遠程視頻的回調
         */
        @Override
        public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
            if (mOnPartyListener != null) {
                mOnPartyListener.onGetRemoteVideo(uid);
            }
        }

        /**
         * 加入頻道成功的回調
         */
        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            if (mOnPartyListener != null) {
                mOnPartyListener.onJoinChannelSuccess(channel, uid);
            }
        }

        /**
         * 退出頻道
         */
        @Override
        public void onLeaveChannel(RtcStats stats) {
            if (mOnPartyListener != null) {
                mOnPartyListener.onLeaveChannelSuccess();
            }
        }

        /**
         * 用戶uid離線時的回調
         */
        @Override
        public void onUserOffline(int uid, int reason) {
            if (mOnPartyListener != null) {
                mOnPartyListener.onUserOffline(uid);
            }
        }
    };

    /**
     * 初始化RtcEngine
     */
    public void init(Context context) {
        //创建RtcEngine对象，mRtcEventHandler为RtcEngine的回调
        try {
            mRtcEngine = RtcEngine.create(context, context.getString(R.string.agora_app_id), mRtcEventHandler);//开启视频功能
            mRtcEngine.enableVideo();//视频配置，设置为360
//            mRtcEngine.adjustRecordingSignalVolume(400);//录音信号音量，可在 0~400 范围内进行调节
//            mRtcEngine.adjustPlaybackSignalVolume(400);//播放信号音量，可在 0~400 范围内进行调节
//            mRtcEngine.muteLocalAudioStream(false);//停止或继续发送本地音频流
            mRtcEngine.setVideoProfile(Constants.VIDEO_PROFILE_360P, false);
            mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION);//设置为通信模式（默认）
//        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);设置为直播模式
//        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_GAME);设置为游戏模式

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 設置本地視頻，即前置攝像頭預覽
     */
    public AgoraManager setupLocalVideo(Context context) {
        //創建一個SurfaceView用作視頻預覽
        SurfaceView surfaceView = RtcEngine.CreateRendererView(context);
        //將SurfaceView保存起來在SparseArray中，後續會將其加入界面。 key為視頻的用戶id，這裡是本地視頻, 默認id是0
        mSurfaceViews.put(mLocalUid, surfaceView);
        //設置本地視頻，渲染模式選擇VideoCanvas.RENDER_MODE_HIDDEN，如果選其他模式會出現視頻不會填充滿整個SurfaceView的情況，
        // 具體渲染模式參考官方文檔https://docs.agora.io/cn/user_guide/API/android_api.html#set-local-video-view-setuplocalvideo
        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, mLocalUid));
        return this;//返回AgoraManager以作鍊式調用
    }

    public AgoraManager setupRemoteVideo(Context context, int uid) {
        SurfaceView surfaceView = RtcEngine.CreateRendererView(context);
        mSurfaceViews.put(uid, surfaceView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
        return this;
    }

    public AgoraManager joinChannel(String channel) {
        mRtcEngine.joinChannel(null, channel, null, 0);
        return this;
    }

    public void startPreview() {
        mRtcEngine.startPreview();
    }

    public void stopPreview() {
        mRtcEngine.stopPreview();
    }

    public void leaveChannel() {
        mRtcEngine.leaveChannel();
    }

    public void removeSurfaceView(int uid) {
        mSurfaceViews.remove(uid);
    }

    public interface OnPartyListener {
        void onJoinChannelSuccess(String channel, int uid);

        void onGetRemoteVideo(int uid);

        void onLeaveChannelSuccess();

        void onUserOffline(int uid);
    }

    public AgoraManager setOnPartyListener(OnPartyListener listener) {
        mOnPartyListener = listener;
        return this;
    }

    public List<SurfaceView> getSurfaceViews() {
        List<SurfaceView> list = new ArrayList<SurfaceView>();
        for (int i = 0; i < mSurfaceViews.size(); i++) {
            SurfaceView surfaceView = mSurfaceViews.valueAt(i);
            list.add(surfaceView);
        }
        return list;
    }

    public SurfaceView getLocalSurfaceView() {
        return mSurfaceViews.get(mLocalUid);
    }

    public SurfaceView getSurfaceView(int uid) {
        return mSurfaceViews.get(uid);
    }
    public void onLocalAudioMuteClicked(boolean is){
        mRtcEngine.muteLocalAudioStream(is);
    }
    public void onSwitchCameraClicked(){
        mRtcEngine.switchCamera();
    }
}
