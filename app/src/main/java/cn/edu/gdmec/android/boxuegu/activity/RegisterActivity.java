package cn.edu.gdmec.android.boxuegu.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.gdmec.android.boxuegu.R;
import cn.edu.gdmec.android.boxuegu.utils.MD5Utils;

public class RegisterActivity extends AppCompatActivity {
    private TextView tv_main_title;
    private TextView tv_back;
    private Button btn_register;

    private EditText et_user_name,et_psw, et_psw_again;

    private String userName,psw, pswAgain;
    private RelativeLayout rl_title_bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }

    private void init() {
        tv_main_title = findViewById(R.id.tv_main_title);
        tv_main_title.setText("注册");
        tv_back = findViewById(R.id.tv_back);
        rl_title_bar = findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(Color.TRANSPARENT);
        btn_register = findViewById(R.id.btn_register);
        et_user_name = findViewById(R.id.et_user_name);
        et_psw = findViewById(R.id.et_psw);
        et_psw_again = findViewById(R.id.et_psw_again);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.finish();

            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditString();
                if (TextUtils.isEmpty(userName)){
                    Toast.makeText(RegisterActivity.this,"请输入用户名",Toast.LENGTH_LONG).show();
                    return;
                }else  if (TextUtils.isEmpty(psw)){
                    Toast.makeText(RegisterActivity.this,"请输入密码",Toast.LENGTH_LONG).show();
                    return;
                }else  if (TextUtils.isEmpty(pswAgain)){
                    Toast.makeText(RegisterActivity.this,"请再次输入密码",Toast.LENGTH_LONG).show();
                    return;
                }else if (!psw.equals(pswAgain)){
                    Toast.makeText(RegisterActivity.this,"两次的密码不一样",Toast.LENGTH_LONG).show();
                    return;
                }else if (isExistUserName(userName)){
                    Toast.makeText(RegisterActivity.this,"此账户名已经存在",Toast.LENGTH_LONG).show();
                    return;
                }else {
                    Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_LONG).show();
                    //把用户名和密码保持到sharedpreference里面
                    saveRegisterInfo(userName,psw);
                    //注册成功后把用户名传递到LoginActivity.java中
                    Intent data = new Intent();
                    data.putExtra("userName",userName);
                    setResult(RESULT_OK,data);
                    RegisterActivity.this.finish();
                }
            }
        });

    }

    private void getEditString() {
        userName = et_user_name.getText().toString().trim();
        psw = et_psw.getText().toString().trim();
        pswAgain = et_psw_again.getText().toString().trim();
    }
    /**
     * 从sharedpreference中读取输入的用户名，判断sharedpreference中是否有此用户名
     * */
    private boolean isExistUserName(String userName) {
        boolean has_userName = false;
        SharedPreferences sp = getSharedPreferences("loginInfo",MODE_PRIVATE);
        String spPwd = sp.getString(userName,"");
        if (!TextUtils.isEmpty(spPwd)){
            has_userName = true;
        }
        return has_userName;
    }

    private void saveRegisterInfo(String userName, String psw) {
        String md5Psw = MD5Utils.md5(psw);//把密码用MD5加密
        //LoginInfo是文件名
        SharedPreferences sp = getSharedPreferences("loginInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();//获取sp的编辑器
        //userName作为Key，密码作为value
        editor.putString(userName,md5Psw);
        editor.apply();//提交修改
    }
}
