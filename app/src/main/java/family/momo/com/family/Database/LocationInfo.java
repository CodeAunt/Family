package family.momo.com.family.Database;

import com.baidu.mapapi.model.LatLng;

public class LocationInfo {
    public String username;
    public Boolean infoState;
    // 使用icon临时代替
    public int portraitPath;
//    public String portraitPath = null;
    // todo add the location info;
    //    private
    public LatLng location = new LatLng(5E-324, 5E-324);
    public LocationInfo(String username, Boolean infoState, int portraitPath, LatLng location){
        this.username = username;
        this.infoState = infoState;
        //this.portraitPath = portraitPath;
        this.portraitPath = portraitPath;
        this.location = location;
    }
}
