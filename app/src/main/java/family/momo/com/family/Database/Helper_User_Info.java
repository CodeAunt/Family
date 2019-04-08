package family.momo.com.family.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/*
 * 在这个类的构造函数里面我们调用了父类的构造方法用来创建数据库文
 * 件，第二个构造方法只是为了方便构造（不用些那么多的参数）
 * 这个类继承了 SQLiteOpenHelper 类，并且重写了父类里面的
onCreate方法和 onUpgrade方法，
 * onCreate方法当数据库文件不存在的时候会被调用来创建一个新的数
 * 据库文件（不懂的小伙伴可以百度一下）
 */

public class Helper_User_Info extends SQLiteOpenHelper{

    public static String CREATE_TABLE = "create table "+ Bean_User_Info.TABLE_NAME +"(" +
            Bean_User_Info.USERNAME + " varchar(20) not null, " +
            Bean_User_Info.ID + " Integer primary key autoincrement, " +
            Bean_User_Info.GROUPNAME + " varchar(20) not null,"+
            Bean_User_Info.PHONE + " varchar(20) not null)"; // 用于创建表的SQL语句
    private Context myContext = null;

    public Helper_User_Info(Context context, String name,
                            CursorFactory factory, int version) {
        super(context, Bean_User_Info.DATABASE_NAME, null, Bean_User_Info.DATABASE_VERSION);
    }

    public Helper_User_Info(Context context)
    {
        super(context, Bean_User_Info.DATABASE_NAME, null, Bean_User_Info.DATABASE_VERSION);
        myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("UseDatabase", "创建数据库");
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }
}