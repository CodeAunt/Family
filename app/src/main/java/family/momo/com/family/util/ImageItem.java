package family.momo.com.family.util;

public class ImageItem {
    public String path;
    public boolean isPicked;
    public long date;
    public long id;

    public ImageItem(String path, boolean isPicked, long date, long id){
        this.path = path;
        this.isPicked = isPicked;
        this.date = date;
        this.id = id;
    }

    public ImageItem(String path, boolean isPicked){
        this.path = path;
        this.isPicked = isPicked;
        this.date = -1;
        this.id = -1;
    }
    public boolean isThisImage(String path){
        return this.path.equalsIgnoreCase(path);
    }
}
