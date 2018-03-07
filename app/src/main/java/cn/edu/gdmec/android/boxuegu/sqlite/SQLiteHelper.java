package cn.edu.gdmec.android.boxuegu.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2018/3/7.
 */

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    public static final String DB_NAME = "bxg.db";
    public static final String U_USERINFO = "userinfo";
    public static final String U_VIDEO_PLAY_LIST = "videoplaylist";

    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建个人信息表
        db.execSQL("CREATE TABLE IF NOT EXISTS " + U_USERINFO + "( "
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "userName VARCHAR, "
                + "nickName VARCHAR, "
                +  "sex VARCHAR, "
                + "signature VARCHAR"
                + ")" );

        //创建视频播放记录表
        db.execSQL("CREATE TABLE IF NOT EXISTS " + U_VIDEO_PLAY_LIST + "( "
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "userName VARCHAR, "
                + "chapterId Int, "
                +  "videoId VARCHAR, "
                + "videoPath VARCHAR, "
                + "title VARCHAR, "
                + "secondTitle VARCHAR "
                + ")" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + U_USERINFO );
        db.execSQL( "DROP TABLE IF EXISTS " + U_VIDEO_PLAY_LIST );
        onCreate(db);
    }
}
