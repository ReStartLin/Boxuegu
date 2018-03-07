package cn.edu.gdmec.android.boxuegu.activity;

import android.content.Intent;
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
import cn.edu.gdmec.android.boxuegu.utils.AnalysisUtils;
import cn.edu.gdmec.android.boxuegu.utils.MD5Utils;

public class ModifyPswActivity extends AppCompatActivity {
    private EditText et_original_psw;
    private EditText et_new_psw;
    private EditText et_new_psw_again;
    private String originalPsw;
    private String newPsw;
    private String newPswAgain;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_psw);
        userName = AnalysisUtils.readLoginUserName(this);
        init();
    }

    private void init() {
        TextView tv_main_title = (TextView) findViewById(R.id.tv_main_title);
        tv_main_title.setText("修改密码");
        TextView tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModifyPswActivity.this.finish();
            }
        });

        et_original_psw = (EditText)findViewById(R.id.et_original_psw);
        et_new_psw = (EditText)findViewById(R.id.et_new_psw);
        et_new_psw_again = (EditText)findViewById(R.id.et_new_psw_again);

        Button btn_Save = (Button)findViewById(R.id.btn_save);
        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEditString();
                if (TextUtils.isEmpty(originalPsw)){
                    Toast.makeText(ModifyPswActivity.this,"请输入原始密码",Toast.LENGTH_SHORT).show();
                    return;
                }else if (!MD5Utils.md5(originalPsw).equals(readPsw())){
                    Toast.makeText(ModifyPswActivity.this,"输入的密码和原始密码不一致",Toast.LENGTH_SHORT).show();
                    return;
                }else if (MD5Utils.md5(newPsw).equals(readPsw())){
                    Toast.makeText(ModifyPswActivity.this,"输入的新密码和原始密码不能一致",Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(newPsw)){
                    Toast.makeText(ModifyPswActivity.this,"请输入新密码",Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(newPswAgain)){
                    Toast.makeText(ModifyPswActivity.this,"请再次输入新密码",Toast.LENGTH_SHORT).show();
                    return;
                }else if (!newPsw.equals(newPswAgain)){
                    Toast.makeText(ModifyPswActivity.this,"两次输入的密码不一致 ",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    Toast.makeText(ModifyPswActivity.this,"新密码设置成功 ",Toast.LENGTH_SHORT).show();
                    modifyPsw(newPsw);
                    Intent intent = new Intent(ModifyPswActivity.this,LoginActivity.class);
                    startActivity(intent);
                    SettingActivity.instance.finish();   //关闭设置页面
                    ModifyPswActivity.this.finish();  //关闭当前页面
                }
            }
        });
    }

    private void modifyPsw(String newPsw) {
        String md5Psw = MD5Utils.md5(newPsw);
        SharedPreferences sp = getSharedPreferences("loginInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(userName,md5Psw);
        editor.apply();
    }

    private String readPsw() {
        SharedPreferences sp = getSharedPreferences("loginInfo",MODE_PRIVATE);
        String spPsw = sp.getString(userName, "");
        return spPsw;
    }

    private void getEditString() {
        originalPsw = et_original_psw.getText().toString().trim();
        newPsw = et_new_psw.getText().toString().trim();
        newPswAgain = et_new_psw_again.getText().toString().trim();

    }
}
