package cn.edu.gdmec.android.boxuegu.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.gdmec.android.boxuegu.R;
import cn.edu.gdmec.android.boxuegu.activity.LoginActivity;
import cn.edu.gdmec.android.boxuegu.activity.PlayHistoryActivity;
import cn.edu.gdmec.android.boxuegu.activity.SettingActivity;
import cn.edu.gdmec.android.boxuegu.activity.UserInfoActivity;
import cn.edu.gdmec.android.boxuegu.utils.AnalysisUtils;

/**
 * Created by Administrator on 2018/3/7.
 */

public class MyInfoView {
    private Context mContext;
    private final LayoutInflater mInflater;
    private View mCurrentView;
    private LinearLayout ll_head;
    private ImageView iv_head_icon;
    private RelativeLayout rl_course_history;
    private RelativeLayout rl_setting;
    private TextView tv_user_name;

    public MyInfoView(Context mContext){
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    public View getView(){
        if (mCurrentView == null){
            createView();
        }
        return mCurrentView;
    }

    private void createView() {
        initView();
    }

    private void initView(){
        mCurrentView = mInflater.inflate(R.layout.main_view_myinfo, null);
        ll_head = (LinearLayout)mCurrentView.findViewById(R.id.ll_head);
        iv_head_icon = (ImageView)mCurrentView.findViewById(R.id.iv_head_icon);
        rl_course_history = (RelativeLayout)mCurrentView.findViewById(R.id.rl_course_history);
        rl_setting = (RelativeLayout)mCurrentView.findViewById(R.id.rl_setting);
        tv_user_name = (TextView)mCurrentView.findViewById(R.id.tv_user_name);

        setLoginParams(readLoginStatus());
        ll_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断是否已经登录
                if (readLoginStatus()){
                    //跳转到个人登录
                    Intent intent = new Intent(mContext, UserInfoActivity.class);
                    mContext.startActivity(intent);
                }else {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    ((Activity) mContext).startActivityForResult(intent,1);
                }
            }
        });

        rl_course_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (readLoginStatus()){
                    //跳转到播放记录界面
                    Intent intent = new Intent(mContext, PlayHistoryActivity.class);
                    mContext.startActivity(intent);
                }else {
                    Toast.makeText(mContext,"您还未登录，请先登录", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rl_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (readLoginStatus()){
                    //跳转到设置界面
                    Intent intent = new Intent(mContext, SettingActivity.class);
                    ((Activity)mContext).startActivityForResult(intent,1);
                }else {
                    Toast.makeText(mContext,"您还未登录，请先登录",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setLoginParams(boolean isLogin) {
        if (isLogin){
            tv_user_name.setText(AnalysisUtils.readLoginUserName(mContext));
        }else {
            tv_user_name.setText("点击登录");
        }
    }


    private boolean readLoginStatus(){
        @SuppressLint("WrongConstant") SharedPreferences sp =mContext.getSharedPreferences("loginInfo",Context.MODE_APPEND);
        boolean isLogin = sp.getBoolean("isLogin",false);
        return isLogin;
    }

    public void showView(){
        if (mCurrentView == null){
            createView();
        }
        mCurrentView.setVisibility(View.VISIBLE);
    }
}
