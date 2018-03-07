package cn.edu.gdmec.android.boxuegu.activity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.edu.gdmec.android.boxuegu.R;
import cn.edu.gdmec.android.boxuegu.adapter.PlayHistoryAdapter;
import cn.edu.gdmec.android.boxuegu.bean.VideoBean;
import cn.edu.gdmec.android.boxuegu.utils.AnalysisUtils;
import cn.edu.gdmec.android.boxuegu.utils.DBUtils;

public class PlayHistoryActivity extends AppCompatActivity {


    private DBUtils db;
    private ArrayList<VideoBean> vb1;
    private TextView tv_main_title;
    private RelativeLayout rl_title_bar;
    private ListView lv_list;
    private TextView tv_none;
    private PlayHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_history);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        db = DBUtils.getInstance(this);
        vb1 = new ArrayList<VideoBean>();
        vb1 = db.getVideoHistory(AnalysisUtils.readLoginUserName(this));
        init();
    }

    private void init() {
        tv_main_title = (TextView) findViewById(R.id.tv_main_title);
        tv_main_title.setText("播放记录");
        rl_title_bar = (RelativeLayout) findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(Color.parseColor("#30B4FF"));
        TextView tv_back = (TextView) findViewById(R.id.tv_back);
        lv_list = (ListView) findViewById(R.id.lv_list);
        tv_none = (TextView) findViewById(R.id.tv_none);
        if (vb1.size() == 0){
            tv_none.setVisibility(View.VISIBLE);
        }
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter = new PlayHistoryAdapter(this);
        adapter.setData(vb1);
        lv_list.setAdapter(adapter);
    }
}
