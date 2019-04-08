package family.momo.com.family.util;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class UploadUtil {
//    private static final String TAG = "uploadFile";
//    private static final int TIME_OUT = 10 * 1000; // 超时时间
//    private static final String CHARSET = "utf-8"; // 设置编码
//    /**
//     * Android上传文件到服务端
//     *
//     * @param file 需要上传的文件
//     * @param RequestURL 请求的rul
//     * @return 返回响应的内容
//     */
//    public static String uploadFile(File file, String RequestURL) {
//        String result = null;
//        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
//        String PREFIX = "--", LINE_END = "\r\n";
//        String CONTENT_TYPE = "multipart/form-data"; // 内容类型
//        try {
//            URL url = new URL(RequestURL);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setReadTimeout(TIME_OUT);
//            conn.setConnectTimeout(TIME_OUT);
//            conn.setDoInput(true); // 允许输入流
//            conn.setDoOutput(true); // 允许输出流
//            conn.setUseCaches(false); // 不允许使用缓存
//            conn.setRequestMethod("POST"); // 请求方式
//            conn.setRequestProperty("Charset", CHARSET); // 设置编码
//            conn.setRequestProperty("connection", "keep-alive");
//            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
//            if (file != null) {
//                /**
//                 * 当文件不为空，把文件包装并且上传
//                 */
//                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
//                StringBuffer sb = new StringBuffer();
//                sb.append(PREFIX);
//                sb.append(BOUNDARY);
//                sb.append(LINE_END);
//                /**
//                 * 这里重点注意： name里面的值为服务端需要key 只有这个key 才可以得到对应的文件
//                 * filename是文件的名字，包含后缀名的 比如:abc.png
//                 */
//                sb.append("Content-Disposition: form-data; name=\"uploadfile\"; filename=\""
//                        + file.getName() + "\"" + LINE_END);
//                sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
//                sb.append(LINE_END);
//                dos.write(sb.toString().getBytes());
//                InputStream is = new FileInputStream(file);
//                byte[] bytes = new byte[1024*1024000];
//                int len = 0;
//                while ((len = is.read(bytes)) != -1) {
//                    dos.write(bytes, 0, len);
//                }
//                is.close();
//                dos.write(LINE_END.getBytes());
//                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
//                dos.write(end_data);
//                dos.flush();
//                /**
//                 * 获取响应码 200=成功 当响应成功，获取响应的流
//                 */
//                int res = conn.getResponseCode();
//                Log.e(TAG, "response code:" + res);
//                // if(res==200)
//                // {
//                Log.e(TAG, "request success");
//                InputStream input = conn.getInputStream();
//                StringBuffer sb1 = new StringBuffer();
//                int ss;
//                while ((ss = input.read()) != -1) {
//                    sb1.append((char) ss);
//                }
//                result = sb1.toString();
//                Log.e(TAG, "result : " + result);
//                // }
//                // else{
//                // Log.e(TAG, "request error");
//                // }
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//    /**
//     * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
//     *
//     * @param url Service net address
//     * @param params text content
//     * @param files pictures
//     * @return String result of Service response
//     * @throws IOException
//     */
//    public static String post(String url, Map<String, String> params, Map<String, File> files)
//            throws IOException {
//        String BOUNDARY = java.util.UUID.randomUUID().toString();
//        String PREFIX = "--", LINEND = "\r\n";
//        String MULTIPART_FROM_DATA = "multipart/form-data";
//        String CHARSET = "UTF-8";
//        URL uri = new URL(url);
//        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
//        conn.setReadTimeout(10 * 1000); // 缓存的最长时间
//        conn.setDoInput(true);// 允许输入
//        conn.setDoOutput(true);// 允许输出
//        conn.setUseCaches(false); // 不允许使用缓存
//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("connection", "keep-alive");
//        conn.setRequestProperty("Charsert", "UTF-8");
//        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);
//        // 首先组拼文本类型的参数
//        StringBuilder sb = new StringBuilder();
//        for (Map.Entry<String, String> entry : params.entrySet()) {
//            sb.append(PREFIX);
//            sb.append(BOUNDARY);
//            sb.append(LINEND);
//            sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
//            sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
//            sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
//            sb.append(LINEND);
//            sb.append(entry.getValue());
//            sb.append(LINEND);
//        }
//        DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
//        outStream.write(sb.toString().getBytes());
//        // 发送文件数据
//        if (files != null)
//            for (Map.Entry<String, File> file : files.entrySet()) {
//                StringBuilder sb1 = new StringBuilder();
//                sb1.append(PREFIX);
//                sb1.append(BOUNDARY);
//                sb1.append(LINEND);
//                sb1.append("Content-Disposition: form-data; name=\"uploadfile\"; filename=\""
//                        + file.getValue().getName() + "\"" + LINEND);
//                sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
//                sb1.append(LINEND);
//                outStream.write(sb1.toString().getBytes());
//                InputStream is = new FileInputStream(file.getValue());
//                byte[] buffer = new byte[1024*1024000];
//                int len = 0;
//                while ((len = is.read(buffer)) != -1) {
//                    outStream.write(buffer, 0, len);
//                }
//                is.close();
//                outStream.write(LINEND.getBytes());
//            }
//        // 请求结束标志
//        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
//        outStream.write(end_data);
//        outStream.flush();
//        // 得到响应码
//        int res = conn.getResponseCode();
//        InputStream in = conn.getInputStream();
//        StringBuilder sb2 = new StringBuilder();
//        if (res == 200) {
//            int ch;
//            while ((ch = in.read()) != -1) {
//                sb2.append((char) ch);
//            }
//        }
//        outStream.close();
//        conn.disconnect();
//        return sb2.toString();
//    }

    private static final String TAG = "uploadFile";
    private static final int TIME_OUT = 10 * 1000;//超时时间
    private static final String CHARSET = "utf-8";//设置编码

    /**
     * android上传文件到服务器
     *
     * @param file       需要上传的文件
     * @param RequestURL  请求的url
     * @return 返回响应的内容
     */
    public static String uploadImage(File file, String RequestURL) {
//        Log.d("Tag", ""+ file.get)
        String result = "error";
        String BOUNDARY = UUID.randomUUID().toString();//边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data";//内容类型
        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true);//允许输入流
            conn.setDoOutput(true);//允许输出流
            conn.setUseCaches(false);//不允许使用缓存
            conn.setRequestMethod("POST");//请求方式
            conn.setRequestProperty("Charset", CHARSET);//设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            conn.connect();

            if (file != null) {
                //当文件不为空，把文件包装并且上传
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                /*StringBuilder sb = new StringBuilder();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                *//*
                 * 这里重点注意：
                 * name里面的值为服务器端需要key,只有这个key才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的。比如:abc.png
                 *//*
                sb.append("Content-Disposition: form-data; name=\"inputName\"; filename=\"" + file.getName() + "\"" + LINE_END);
                //sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
                //sb.append("Content-Type: " + getMIMEType(file) + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());*///此写法会导致无法上传
                dos.writeBytes(PREFIX + BOUNDARY + LINE_END);
                dos.writeBytes("Content-Disposition: form-data; " + "inputname=\"xxxxxxx\"; filename=\"" + file.getName() + "\"" + LINE_END);
                dos.writeBytes(LINE_END);

                FileInputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = -1;
                while ((len = is.read(bytes)) != -1) {
                Log.d("Tag", "Sender read file: "+ bytes+" len:"+ len);
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());

                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();

                /*
                 * 获取响应码  200=成功
                 * 当响应成功，获取响应的流  
                 */
                int res = conn.getResponseCode();
                if (res == 200) {
                    InputStream input = conn.getInputStream();
                    StringBuilder sbs = new StringBuilder();
                    int ss;
                    while ((ss = input.read()) != -1) {
                        sbs.append((char) ss);
                    }
                    result = sbs.toString();
                    Log.i(TAG, "result------------------>>" + result);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String getMIMEType(File file) {
        String fileName = file.getName();
        if (fileName.endsWith("png") || fileName.endsWith("PNG")) {
            return "image/png";
        } else {
            return "image/jpg";
        }
    }
}