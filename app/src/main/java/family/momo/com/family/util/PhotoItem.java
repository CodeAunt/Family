package family.momo.com.family.util;


import java.io.Serializable;

/**
 * Whether the image has been uploaded is not known from the device.
 * The id is just for ui.
 * And time stamp is for future functions.
 * I don't know how to generate the id.
 * @Author: CodeAunt
 * @date: 2019/03/262323
 *
 */

public class PhotoItem implements Serializable {
    public String path;
    public boolean isPicked;
    public long date;
    // should id includes the date, uploader.
    public long id;

    public PhotoItem(String path, boolean isPicked, long date, long id) {
        this.path = path;
        this.isPicked = isPicked;
        this.date = date;
        this.id = id;
    }

    public boolean isThisImage(String path) {
        return this.path.equalsIgnoreCase(path);
    }
}
