package cn.edu.gdmec.android.boxuegu.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import cn.edu.gdmec.android.boxuegu.R;
import cn.edu.gdmec.android.boxuegu.adapter.VideoListAdapter;
import cn.edu.gdmec.android.boxuegu.bean.VideoBean;
import cn.edu.gdmec.android.boxuegu.utils.AnalysisUtils;
import cn.edu.gdmec.android.boxuegu.utils.DBUtils;

public class VideoListActivity extends AppCompatActivity implements View.OnClickListener{

    private int chapterId;
    private String intro;
    private DBUtils db;
    private ArrayList<VideoBean> videoList;
    private TextView tv_intro;
    private TextView tv_video;
    private ListView lv_video_list;
    private TextView tv_chapter_intro;
    private ScrollView sv_chapter_intro;
    private VideoListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        //设置界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //从课程界面传递过来的章节id
        chapterId = getIntent().getIntExtra("id",0);
        //从课程界面传递过来的章节简介
        intro = getIntent().getStringExtra("intro");

        db = DBUtils.getInstance(VideoListActivity.this);
        initData();
        initView();
    }

    private void initView() {
        tv_intro = (TextView) findViewById(R.id.tv_intro);
        tv_video = (TextView) findViewById(R.id.tv_video);
        lv_video_list = (ListView) findViewById(R.id.lv_video_list);

        tv_chapter_intro = (TextView) findViewById(R.id.tv_chapter_intro);
        sv_chapter_intro = (ScrollView) findViewById(R.id.sv_chapter_intro);
        adapter = new VideoListAdapter(this, new VideoListAdapter.OnSelectListener() {
            @Override
            public void onSelect(int position, ImageView iv) {
                adapter.setSelectedPosition(position);
                VideoBean bean = videoList.get(position);
                String videoPath = bean.videoPath;
                adapter.notifyDataSetChanged();
                if (TextUtils.isEmpty(videoPath)){
                    Toast.makeText(VideoListActivity.this,"本地没有此视频，暂时无法播放",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    //判断用户是否登陆，若登陆则把此视频添加到数据库
                    if (readLoginStatus()){
                        String userName = AnalysisUtils.readLoginUserName(VideoListActivity.this);
                        db.saveVideoList(videoList.get(position),userName);
                    }
                    //跳转到视频播放界面
                    Intent intent = new Intent(VideoListActivity.this,VideoPlayActivity.class);
                    intent.putExtra("videoPath",videoPath);
                    intent.putExtra("position",position);
                    startActivityForResult(intent,1);
                }
            }
        });
        lv_video_list.setAdapter(adapter);
        tv_intro.setOnClickListener(this);
        tv_video.setOnClickListener(this);
        adapter.setData(videoList);
        tv_chapter_intro.setText(intro);
        tv_intro.setBackgroundColor(Color.parseColor("#30B4FF"));
        tv_video.setBackgroundColor(Color.parseColor("#ffffff"));
        tv_intro.setTextColor(Color.parseColor("#ffffff"));
        tv_video.setTextColor(Color.parseColor("#000000"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null){
            int position = data.getIntExtra("position",0);
            adapter.setSelectedPosition(position);
            //视频选项卡被选中的时候所有图标的颜色值
            lv_video_list.setVisibility(View.VISIBLE);
            sv_chapter_intro.setVisibility(View.GONE);
            tv_intro.setBackgroundColor(Color.parseColor("#ffffff"));
            tv_video.setBackgroundColor(Color.parseColor("#30B4FF"));
            tv_intro.setTextColor(Color.parseColor("#000000"));
            tv_video.setTextColor(Color.parseColor("#ffffff"));
        }
    }

    private boolean readLoginStatus() {
        SharedPreferences sp = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin",false);
        return isLogin;
    }

    //设置视频列表本地数据
    private void initData() {
        JSONArray jsonArray;
        InputStream is;
        try {
            is = getResources().getAssets().open("data.json");
            jsonArray = new JSONArray(read(is));

            videoList = new ArrayList<VideoBean>();
            for (int i=0;i<jsonArray.length();i++){
                VideoBean bean = new VideoBean();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getInt("chapterId") == chapterId){
                    bean.chapterId = jsonObject.getInt("chapterId");
                    bean.videoId = Integer.parseInt(jsonObject.getString("videoId"));
                    bean.title = jsonObject.getString("title");
                    bean.secondTitle = jsonObject.getString("secondTitle");
                    bean.videoPath = jsonObject.getString("videoPath");
                    videoList.add(bean);
                }
                bean = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //读取数据流，参数in是数据流
    private String read(InputStream in) {
        BufferedReader reader  = null;
        StringBuilder sb = null;
        String line = null;

        try {
            sb = new StringBuilder();//实例化一个stringBuilder对象
            //用inputStreamReader把in这个字节流转换成字符流BufferReader
            reader = new BufferedReader(new InputStreamReader(in));
            while((line = reader.readLine()) != null){
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }finally {
            if (in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_intro:
                //简介
                lv_video_list.setVisibility(View.GONE);
                sv_chapter_intro.setVisibility(View.VISIBLE);
                tv_intro.setBackgroundColor(Color.parseColor("#30B4FF"));
                tv_video.setBackgroundColor(Color.parseColor("#ffffff"));
                tv_intro.setTextColor(Color.parseColor("#ffffff"));
                tv_video.setTextColor(Color.parseColor("#000000"));
                break;
            case R.id.tv_video:
                lv_video_list.setVisibility(View.VISIBLE);
                sv_chapter_intro.setVisibility(View.GONE);
                tv_intro.setBackgroundColor(Color.parseColor("#ffffff"));
                tv_video.setBackgroundColor(Color.parseColor("#30B4FF"));
                tv_intro.setTextColor(Color.parseColor("#000000"));
                tv_video.setTextColor(Color.parseColor("#ffffff"));
                break;
            default:
                break;
        }
    }
}
