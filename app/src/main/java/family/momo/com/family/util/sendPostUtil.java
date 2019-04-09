package family.momo.com.family.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class sendPostUtil {
    /**
     *get请求封装
     */
    public static void getRequest(String url, String jsonString, String encode, OnResponseListner listner) {
        if (listner!=null) {
            try {
                URL path = new URL(url);
                if (path!=null) {
                    HttpURLConnection con = (HttpURLConnection) path.openConnection();
                    con.setRequestMethod("GET");    //设置请求方式
                    con.setRequestProperty("Content-type", "application/json");
                    con.setConnectTimeout(3000);    //链接超时3秒
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    OutputStream os = con.getOutputStream();
                    os.write(jsonString.getBytes(encode));
                    os.close();
                    if (con.getResponseCode() == 200) {    //应答码200表示请求成功
                        onSucessResopond(encode, listner, con);
                    }
                }
            } catch (Exception error) {
                error.printStackTrace();
                onError(listner, error);
            }
        }
    }

    /**
     * POST请求
     */
    public static void postRequest(String url,String jsonString,String encode,OnResponseListner listner){
        if (listner!=null) {
            try {
                URL path = new URL(url);
                if (path!=null){
                    HttpURLConnection con = (HttpURLConnection) path.openConnection();
                    con.setRequestMethod("POST");   //设置请求方法POST
                    con.setRequestProperty("Content-type", "application/json");
                    con.setConnectTimeout(3000);
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    byte[] bytes = jsonString.getBytes();
                    OutputStream outputStream = con.getOutputStream();
                    outputStream.write(bytes);
                    outputStream.close();
                    if (con.getResponseCode()==200){
                        onSucessResopond(encode, listner,  con);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                onError(listner, e);
            }
        }
    }

    private static void onError(OnResponseListner listner,Exception onError) {
        listner.onError(onError.toString());
    }

    private static void onSucessResopond(String encode, OnResponseListner listner, HttpURLConnection con) throws IOException, IOException {
        InputStream inputStream = con.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();//创建内存输出流
        int len = 0;
        byte[] bytes = new byte[1024];
        if (inputStream != null) {
            while ((len = inputStream.read(bytes)) != -1) {
                baos.write(bytes, 0, len);
            }
            String str = new String(baos.toByteArray(), encode);
            listner.onSucess(str);
        }
    }


    public interface OnResponseListner {
        void onSucess(String response);
        void onError(String error);
    }

    public static void getGroupMember(String groupname,OnResponseListner onResponseListner){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("groupname",groupname);
            postRequest(VariableDataUtil.requestAddress +"consumer/getgroupmember",jsonObject.toString(),"utf-8",onResponseListner);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void getGroupName(String groupcode,OnResponseListner onResponseListner){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("groupname",groupcode);
            postRequest(VariableDataUtil.requestAddress +"consumer/getgroups",jsonObject.toString(),"utf-8",onResponseListner);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void getGroupCode(String groupname,OnResponseListner onResponseListner){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("groupname",groupname);
            postRequest(VariableDataUtil.requestAddress +"consumer/getgroupcode",jsonObject.toString(),"utf-8",onResponseListner);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String newCircle(){
        return "success";
    }

}
