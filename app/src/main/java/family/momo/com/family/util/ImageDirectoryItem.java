package family.momo.com.family.util;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

public class ImageDirectoryItem implements Serializable {

    /** 图片的路径,对应图片是否被选中的数组 */
    public ArrayList<ImageItem> images;

    public ImageDirectoryItem(){
        images = new ArrayList<ImageItem>();
    }

    public ArrayList<ImageItem> getImages() {
        return images;
    }

    /**
     * 把一张图片path添加进这个文件夹中
     * @param path 图片地址
     */
    public void addImage(String path, long date, long id){
        ImageItem image = new ImageItem(path, false, date, id);
        images.add(image);
    }

    public void addImageItem(ImageItem model){
        images.add(model);
    }

    /**
     * 将一张图片从该文件夹中删除
     * @param path 图片地址
     */

    //Todo: refactor
    public void removeImage(String path){
        for (ImageItem image : images){
            if (image.isThisImage(path)){
                images.remove(image);
                break;
            }
        }
    }

    /**
     * 选中该图片
     * @param path 图片地址
     */
    public void setImage(String path){
        for (ImageItem image : images){
            if (image.isThisImage(path)){
                if(image.isPicked){
                    Log.e("zhao", "this image is picked!!!");
                }
                image.isPicked = true;
                break;
            }
        }
    }

    /**
     * 不选中该图片
     * @param path 图片地址
     */
    public void unsetImage(String path){
        for (ImageItem image : images){
            if (image.isThisImage(path)){
                if(!image.isPicked){
                    Log.e("zhao", "this image isn't picked!!!");
                }
                image.isPicked = false;
                break;
            }
        }
    }

    /**
     * 转变图片的选中状态
     */
    public void toggleSetImage(int position){
        ImageItem model = images.get(position);
        model.isPicked = !model.isPicked;
    }

    /**
     * 转变图片的选中状态
     */
    public void toggleSetImage(String path){
        for (ImageItem model : images){
            if (model.path.equalsIgnoreCase(path)) {
                model.isPicked = !model.isPicked;
                break;
            }
        }
    }

    /**
     * 返回该文件夹的所有文件数量
     */
    public int getImageCounts(){
        return images.size();
    }

    /**
     * 根据图片的位置返回该图片的url
     * @param position 图片位置
     */
    public String getImagePath(int position){
        return images.get(position).path;
    }

    /**
     * 根据图片的位置返回该图片是否被选中
     * @param position 图片位置
     */
    public boolean getImagePickOrNot(int position){
        return  images.get(position).isPicked;
    }

    /**
     * 该文件夹中是否有选中的图片
     */
    public boolean hasChoosePic(){
        for (ImageItem model : images){
            if (model.isPicked)
                return true;
        }
        return false;
    }

}
