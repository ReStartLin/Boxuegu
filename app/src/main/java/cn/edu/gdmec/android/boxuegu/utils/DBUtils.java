package cn.edu.gdmec.android.boxuegu.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import cn.edu.gdmec.android.boxuegu.bean.UserBean;
import cn.edu.gdmec.android.boxuegu.bean.VideoBean;
import cn.edu.gdmec.android.boxuegu.sqlite.SQLiteHelper;

/**
 * Created by Administrator on 2018/3/7.
 */

public class DBUtils {
    private static SQLiteHelper helper;
    private static SQLiteDatabase db;
    private static DBUtils instance = null;
    public DBUtils(Context context) {
        helper = new SQLiteHelper (context);
        db = helper.getWritableDatabase();
    }

    public static DBUtils getInstance(Context context){
        if( instance == null ){
            instance = new DBUtils(context);
        }
        return instance;
    }
    public void saveUserInfo(UserBean bean){
        ContentValues cv = new ContentValues ();
        cv.put("userName",bean.userName);
        cv.put("nickName",bean.nickName);
        cv.put("sex",bean.sex);
        cv.put("signature",bean.signature);
        db.insert(SQLiteHelper.U_USERINFO,null,cv);

    }
    public UserBean getUserInfo(String userName){
        String sql = "SELECT * FROM " + SQLiteHelper.U_USERINFO + " WHERE userName =? ";
        Cursor cursor = db.rawQuery(sql,new String[]{userName});
        UserBean bean = null;
        while (cursor.moveToNext()){
            bean = new UserBean();
            bean.userName = cursor.getString(cursor.getColumnIndex("userName"));
            bean.nickName = cursor.getString(cursor.getColumnIndex("nickName"));
            bean.sex = cursor.getString(cursor.getColumnIndex("sex"));
            bean.signature = cursor.getString(cursor.getColumnIndex("signature"));
        }
        cursor.close();
        return bean;
    }
    public void updateUserInfo(String key,String value,String userName){
        ContentValues cv = new ContentValues();
        cv.put(key,value);
        db.update(SQLiteHelper.U_USERINFO,cv,"userName =? ",new String[]{ userName });
    }

    public void saveVideoList(VideoBean bean, String userName) {
        //判断如果里面已经有此播放记录则需要删除在重新存放
        if (hasVideoPlay(bean.chapterId,bean.chapterId,userName)){
            //删除之前的播放记录
            boolean isDelete = delVideoPlay(bean.chapterId,bean.videoId,userName);
            if (!isDelete){
                //没有删除成功
                return;
            }
        }
        ContentValues cv = new ContentValues();
        cv.put("userName",userName);
        cv.put("chapterId",bean.chapterId);
        cv.put("videoId",bean.videoId);
        cv.put("videoPath",bean.videoPath);
        cv.put("title",bean.title);
        cv.put("secondTitle",bean.secondTitle);
        db.insert(SQLiteHelper.U_VIDEO_PLAY_LIST,null,cv);
    }

    private boolean delVideoPlay(int chapterId, int videoId, String userName) {
        boolean delSuccess = false;
        int row = db.delete(SQLiteHelper.U_VIDEO_PLAY_LIST," chapterId=? AND videoId=? AND userName=?",
                new String[]{chapterId + "",videoId + "", userName});
        if (row>0){
            delSuccess = true;
        }
        return delSuccess;
    }

    private boolean hasVideoPlay(int chapterId, int videoId, String userName) {
        boolean hasVideo = false;
        String sql = "SELECT * FROM " + SQLiteHelper.U_VIDEO_PLAY_LIST + " WHERE chapterId=? AND videoID = ? AND userName = ?";
        Cursor cursor = db.rawQuery(sql,new String[]{chapterId + "",videoId + "", userName});
        if (cursor.moveToFirst()){
            hasVideo = true;
        }
        cursor.close();
        return hasVideo;
    }

    public ArrayList<VideoBean> getVideoHistory(String userName) {
        String sql = "SELECT * FROM " + SQLiteHelper.U_VIDEO_PLAY_LIST + " WHERE userName = ?";
        Cursor cursor = db.rawQuery(sql,new String[]{userName});
        ArrayList<VideoBean> vb1 = new ArrayList<>();
        VideoBean bean = null;
        while(cursor.moveToNext()){
            bean = new VideoBean();
            bean.chapterId = cursor.getInt(cursor.getColumnIndex("chapterId"));
            bean.videoId = cursor.getInt(cursor.getColumnIndex("videoId"));
            bean.videoPath = cursor.getString(cursor.getColumnIndex("videoPath"));
            bean.title = cursor.getString(cursor.getColumnIndex("title"));
            bean.secondTitle = cursor.getString(cursor.getColumnIndex("secondTitle"));
            vb1.add(bean);
            bean = null;
        }
        cursor.close();
        return vb1;
    }
}
