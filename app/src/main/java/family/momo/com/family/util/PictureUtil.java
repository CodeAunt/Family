package family.momo.com.family.util;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;

public class PictureUtil {
    public static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    public static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    public static final int PHOTO_REQUEST_CUT = 3;// 结果
    /*
     * 从相册获取
     */
    public static Intent gallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        return intent;
    }

    /*
     * 从相机获取
     */
    public static Intent camera() {
        if (hasSdcard()) {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            return intent;
        }

        return null;
    }

    /*
     * 剪切图片
     */
    public static Intent crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        return intent;
    }

    /*
     * 判断sdcard是否被挂载
     */
    public static boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            getGallery(data);
        } else if (requestCode == PHOTO_REQUEST_CAREMA) {
            getCamere(data);
        } else if (requestCode == PHOTO_REQUEST_CUT) {
            getCrop(data);
        }
    }

    public static Bitmap getCamere(Intent data) {
        if (data != null) {
            // 从相机返回的数据
            Bitmap photo = null;
            //两种方式 获取拍好的图片
            if (data.getData() != null || data.getExtras() != null) { //防止没有返回结果
                Uri uri = data.getData();
                if (uri != null) {
                    photo = BitmapFactory.decodeFile(uri.getPath()); //拿到图片
                }
                if (photo == null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        photo = (Bitmap) bundle.get("data");
                    } else {
                    }
                }
                //处理图片
                //裁剪图片
                return photo;
            }
        }

        return null;
    }
    public static Uri getGallery(Intent data){
        // 从相册返回的数据
        if (data != null) {
            // 得到图片的全路径
            Uri uri = data.getData();
            return uri;
        }
        return null;
    }
    public static Bitmap getCrop(Intent data){
//        File tempFile = new File(Environment.getExternalStorageDirectory(),"temp.jpg");
        // 从剪切图片返回的数据
        if (data != null) {
            Bitmap bitmap = data.getParcelableExtra("data");
            return bitmap;
        }
//        try {
//            // 将临时文件删除
//            tempFile.delete();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return null;
    }
}