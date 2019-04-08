package family.momo.com.family.chat;

import android.app.Application;

/**
 * 在Application類中初始化RtcEngine，注意在AndroidManifest.xml中配置下Application
 */
public class HsiaoTienApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AgoraManager.getInstance().init(getApplicationContext());
    }
}

