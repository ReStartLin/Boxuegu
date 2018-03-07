package cn.edu.gdmec.android.boxuegu.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.gdmec.android.boxuegu.R;
import cn.edu.gdmec.android.boxuegu.utils.MD5Utils;

public class FindPswActivity extends AppCompatActivity {
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_psw);
        //获取从登录界面和设置界面传递的数据
        from = getIntent().getStringExtra("from");
        init();
    }

    private void init() {
        TextView tv_main_title = (TextView) findViewById(R.id.tv_main_title);

        final EditText et_validate_name = (EditText) findViewById(R.id.et_validate_name);
        Button btn_validate = (Button) findViewById(R.id.btn_validate);
        final TextView tv_reset_psw = (TextView) findViewById(R.id.tv_reset_psw);
        final EditText et_user_name = (EditText) findViewById(R.id.et_user_name);
        TextView tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        TextView tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FindPswActivity.this.finish();
            }
        });

        if ("security".equals(from)){
            tv_main_title.setText("设置密保");
        }else {
            tv_main_title.setText("找回密码");
            tv_user_name.setVisibility(View.VISIBLE);
            et_user_name.setVisibility(View.VISIBLE);
        }

        btn_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String validateName = et_validate_name.getText().toString().trim();
                if ("security".equals(from)){//设置密保
                    if (TextUtils.isEmpty(validateName)){
                        Toast.makeText(FindPswActivity.this,"请输入要验证的姓名",Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        Toast.makeText(FindPswActivity.this,"密保设置成功",Toast.LENGTH_SHORT).show();
                        //把密保保存到sharprefrencence中
                        saveSecurity(validateName);
                        FindPswActivity.this.finish();
                    }
                }else{
                    //找回密码
                    String userName = et_user_name.getText().toString().trim();
                    String sp_scurity = readSecurity(userName);

                    if (TextUtils.isEmpty(userName)){
                        Toast.makeText(FindPswActivity.this,"请输入用户名",Toast.LENGTH_SHORT).show();
                        return;
                    }else if (!isExistUserName(userName)){
                        Toast.makeText(FindPswActivity.this,"您输入的用户名不存在",Toast.LENGTH_SHORT).show();
                        return;
                    }else if(TextUtils.isEmpty(validateName)){
                        Toast.makeText(FindPswActivity.this,"请输入要验证的姓名",Toast.LENGTH_SHORT).show();
                        return;
                    }else if (!validateName.equals(sp_scurity)){
                        Toast.makeText(FindPswActivity.this,"输入的密保不正确",Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        //用户输入的密保正确，重新给用户一个新密码
                        tv_reset_psw.setVisibility(View.VISIBLE);
                        tv_reset_psw.setText("初始密码：123456");
                        savePsw(userName);
                    }
                }

            }
        });
    }

    //保存初始密码
    private void savePsw(String userName) {
        String md5Psw = MD5Utils.md5("123456");
        SharedPreferences sp = getSharedPreferences("loginInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(userName,md5Psw);
        editor.commit();
    }

    private boolean isExistUserName(String userName) {
        boolean hasUserName = false;
        SharedPreferences sp = getSharedPreferences("loginInfo",MODE_PRIVATE);
        String spPsw = sp.getString(userName, "");
        if (!TextUtils.isEmpty(spPsw)){
            hasUserName = true;
        }
        return hasUserName;

    }

    private String readSecurity(String userName) {
        SharedPreferences sp = getSharedPreferences("loginInfo",MODE_PRIVATE);
        String security = sp.getString(userName + "_security","");
        return security;
    }

    private void saveSecurity(String validateName) {
        SharedPreferences sp = getSharedPreferences("loginInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AnalysisUtils.readLoginUserName(this) + "_security",validateName); //存入用户对应的密保
        editor.apply();
    }
}
