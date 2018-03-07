package cn.edu.gdmec.android.boxuegu.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.gdmec.android.boxuegu.R;
import cn.edu.gdmec.android.boxuegu.utils.MD5Utils;

public class LoginActivity extends AppCompatActivity {
    private TextView tv_back;
    private TextView tv_register;
    private TextView tv_find_psw;
    private TextView btn_login;
    private TextView tv_main_title;
    private EditText et_user_name;
    private EditText et_psw;
    private String userName;
    private String psw;
    private String md5Psw;
    private String spPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }

    /*
    * 获取页面控件*/
    private void init() {
        tv_main_title = (TextView)findViewById(R.id.tv_main_title);
        tv_main_title.setText("登录");
        tv_back = (TextView)findViewById(R.id.tv_back);
        tv_register = (TextView)findViewById(R.id.tv_register);
        tv_find_psw = (TextView)findViewById(R.id.tv_find_psw);
        btn_login = (TextView)findViewById(R.id.btn_login);
        et_user_name = (EditText)findViewById(R.id.et_user_name);
        et_psw = (EditText)findViewById(R.id.et_psw);
        tv_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                LoginActivity.this.finish();
            }
        });
        //立即注册按钮的点击事件
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(intent,1);
            }
        });

        //找回密码控件的点击事件
        tv_find_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到找回密码界面
                Intent intent = new Intent(LoginActivity.this,FindPswActivity.class);
                startActivity(intent);
            }
        });
        //登录按钮的点击事件
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = et_user_name.getText().toString().trim();
                psw = et_psw.getText().toString().trim();
                md5Psw = MD5Utils.md5(psw);
                spPsw = readPsw(userName);
                if (TextUtils.isEmpty(userName)){
                    Toast.makeText(LoginActivity.this,"请输入用户名",Toast.LENGTH_LONG).show();
                }else  if (TextUtils.isEmpty(psw)){
                    Toast.makeText(LoginActivity.this,"请输入密码",Toast.LENGTH_LONG).show();
                }else if (md5Psw.equals(spPsw)){
                    Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_LONG).show();
                    //保持登录状态和登录的用户名
                    saveLoginStatus(true,userName);
                    Intent data = new Intent();
                    data.putExtra("isLogin",true);
                    setResult(RESULT_OK,data);
                    //跳转到主页
                    LoginActivity.this.finish();
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }else if (!TextUtils.isEmpty(spPsw) && !md5Psw.equals(spPsw)){
                    Toast.makeText(LoginActivity.this,"输入的用户名和密码不一致",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(LoginActivity.this,"此用户名不存在",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void saveLoginStatus(boolean status, String userName) {
        //loginInfo表示文件名
        SharedPreferences sp = getSharedPreferences("loginInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit(); //获取编辑器
        editor.putBoolean("isLogin",status);        //存入boolean类型的登录状态
        editor.putString("loginUserName",userName); //存入登录时的用户名
        editor.apply();
    }


    private String readPsw(String userName) {
        SharedPreferences sp = getSharedPreferences("loginInfo",MODE_PRIVATE);
        return  sp.getString(userName,"");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            //从注册界面传递过来的用户名
            String userName = data.getStringExtra("userName");
            if (!TextUtils.isEmpty(userName)){
                et_user_name.setText(userName);
                //设置光标的位置
                et_user_name.setSelection(userName.length());
            }
        }
    }
}
