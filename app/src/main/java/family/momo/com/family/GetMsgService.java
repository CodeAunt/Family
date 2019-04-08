package family.momo.com.family;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

import family.momo.com.family.Database.Bean_Chat_Msg;
import family.momo.com.family.Database.Helper_Chat_Msg;
import family.momo.com.family.util.VariableDataUtil;

import static family.momo.com.family.util.VariableDataUtil.MYID;

public class GetMsgService extends Service {
    public static int groupMsgNum = 2;
    private MsgReceiver msgReceiver;
    private WebSocketClient webc;
    private Draft connDraft = new Draft_17();
    private Intent sendMsg_ActivityIntent = new Intent(VariableDataUtil.Code_Filter_From_Android_Service);

    public GetMsgService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        initClient();
        registBroad();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("momo","onStartCommand OK");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(msgReceiver);
        super.onDestroy();
    }

    private void initClient() {
        try {
            webc = new WebSocketClient(new URI(VariableDataUtil.netAddress+VariableDataUtil.MYID), connDraft) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    Log.e("momom", "链接成功");
                }

                @Override
                public void onMessage(String s) {
                    Log.e("momom", "有信息了：" + s);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(s);
                        String tousername = jsonObject.getString("tousername");
                        String fromusername = jsonObject.getString("fromusername");
                        String message = jsonObject.getString("textMessage");

                        Helper_Chat_Msg _helperChatMsg = new Helper_Chat_Msg(GetMsgService.this);
                        SQLiteDatabase sqLiteDatabase = _helperChatMsg.getWritableDatabase();
                        ContentValues cV = new ContentValues();
                        cV.put(Bean_Chat_Msg.MESSAGE,message);
                        cV.put(Bean_Chat_Msg.FROMUSERNAME,fromusername);
                        cV.put(Bean_Chat_Msg.TOUSERNAME,tousername);
                        sqLiteDatabase.insert(Bean_Chat_Msg.TABLE_NAME, null, cV);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    sendMsg_ActivityIntent.putExtra("msg",s);
                    sendBroadcast(sendMsg_ActivityIntent);
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    Log.e("momom", "哎呀关闭了");
                }

                @Override
                public void onError(Exception e) {
                    Log.e("momom", "哎呀出错了");
                }
            };
            webc.connect();
        }catch (Exception e){

        }finally {
            //initClient();
        }
    }
    private void registBroad(){
        msgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(VariableDataUtil.Code_Filter_From_Android_Activity);
        registerReceiver(msgReceiver,intentFilter);
    }
    class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra("msg");
                webc.send(msg);
        }
    }

}
