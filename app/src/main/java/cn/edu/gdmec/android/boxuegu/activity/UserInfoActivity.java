package cn.edu.gdmec.android.boxuegu.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import cn.edu.gdmec.android.boxuegu.R;
import cn.edu.gdmec.android.boxuegu.bean.UserBean;
import cn.edu.gdmec.android.boxuegu.utils.AnalysisUtils;
import cn.edu.gdmec.android.boxuegu.utils.DBUtils;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tv_user_name;
    private TextView tv_signature;
    private RelativeLayout rl_signature;
    private RelativeLayout rl_head;
    private TextView tv_sex;
    private RelativeLayout rl_sex;
    private TextView tv_nickName;
    private RelativeLayout rl_nickName;
    private TextView tv_back;
    private TextView tv_main_title;
    private RelativeLayout rl_title_bar;
    private String spUserName;
    private static  final int  CHANGE_NICKNAME = 4;//修改昵称的自定义常量
    private static  final int  CHANGE_SIGNATURE = 5;//修改签名的自定义常量
    private String new_info;
    private ImageView iv_head_icon;
    private Bitmap head;
    private static String path = "/sdcard/myHead/";// sd路径
    private TextView tv_select_gallery;
    private TextView tv_select_camera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        //设置界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        spUserName = AnalysisUtils.readLoginUserName(this);
        init();
        initData();
        setListener();
    }

    //自定义跳转方法
    private void enterActivityForResult(Class<?> to,int requestCode,Bundle b){
        Intent i = new Intent(this,to);
        i.putExtras(b);
        startActivityForResult(i,requestCode);

    }
    private void setListener() {
        tv_back.setOnClickListener(this);
        rl_nickName.setOnClickListener(this);
        rl_sex.setOnClickListener(this);
        rl_signature.setOnClickListener(this);

    }

    private void init() {
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_main_title = (TextView) findViewById(R.id.tv_main_title);
        tv_main_title.setText("个人资料");
        rl_title_bar = (RelativeLayout) findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(Color.parseColor("#30B4FF"));
        rl_nickName = (RelativeLayout) findViewById(R.id.rl_nickName);
        tv_nickName = (TextView) findViewById(R.id.tv_nickName);
        rl_sex = (RelativeLayout) findViewById(R.id.rl_sex);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        rl_signature = (RelativeLayout) findViewById(R.id.rl_singature);
        tv_signature = (TextView) findViewById(R.id.tv_signature);
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        iv_head_icon = (ImageView) findViewById(R.id.iv_head_icon);
        rl_head = (RelativeLayout) findViewById(R.id.rl_head);
        Bitmap bt = BitmapFactory.decodeFile(path + "head.jpg");// 从SD卡中找头像，转换成Bitmap
        if (bt != null) {
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(bt);// 转换成drawable
            iv_head_icon.setImageDrawable(drawable);
        } else {
            /**
             * 如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD中
             *
             */
        }

    }
    //从数据库中获取数据
    private  void initData(){
        UserBean bean = null;
        bean  = DBUtils.getInstance(this).getUserInfo(spUserName);
        //首先判断一下数据库中是否有数据
        if(bean ==null){
            bean = new UserBean();
            bean.userName = spUserName;
            bean.nickName = "问答精灵";
            bean.sex = "男";
            bean.signature = "问答精灵";
            //保存用户信息到数据库
            DBUtils.getInstance(this).saveUserInfo(bean);
        }
        setValue(bean);
    }
    //为界面控件设置值
    public void setValue(UserBean bean) {
        tv_nickName.setText(bean.nickName);
        tv_sex.setText(bean.sex);
        tv_signature.setText(bean.signature);
        tv_user_name.setText(bean.userName);
        rl_head.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_back:
                this.finish();
                break;
            case R.id.rl_nickName://呢称的点击事件
                String name = tv_nickName.getText().toString();//获取昵称控件上的数据
                Bundle bdName = new Bundle();
                bdName.putString("content",name);//传递界面上的昵称数据
                bdName.putString("title","昵称");//传递界面的标题
                bdName.putInt("flag",1);//flag 传递1表示是昵称
                enterActivityForResult(ChangeUserInfoActivity.class,CHANGE_NICKNAME,bdName);
                break;
            case R.id.rl_sex:
                String sex = tv_sex.getText().toString();
                sexDialog(sex);
                break;
            case R.id.rl_singature: //签名的点击事件
                String signature = tv_signature.getText().toString();//获取昵称控件上的数据
                Bundle bdSignature = new Bundle();
                bdSignature.putString("content",signature);//传递界面上的签名数据
                bdSignature.putString("title","签名");//传递界面的标题
                bdSignature.putInt("flag",2);//flag 传递2表示是签名
                enterActivityForResult(ChangeUserInfoActivity.class,CHANGE_SIGNATURE,bdSignature);
                break;
            case R.id.rl_head:
                showTypeDialog();
                break;
            default:
                break;
        }
    }

    private void showTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.head_image, null);
        tv_select_gallery = (TextView) view.findViewById(R.id.tv_select_gallery);
        tv_select_camera = (TextView) view.findViewById(R.id.tv_select_camera);
        tv_select_gallery.setOnClickListener(new View.OnClickListener() {// 在相册中选取
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 1);
                dialog.dismiss();
            }
        });
        tv_select_camera.setOnClickListener(new View.OnClickListener() {// 调用照相机
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent2.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "head.jpg")));
                startActivityForResult(intent2, 2);// 采用ForResult打开
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }


    @Override
    protected  void  onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());// 裁剪图片
                }

                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory() + "/head.jpg");
                    cropPhoto(Uri.fromFile(temp));// 裁剪图片
                }

                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");
                    if (head != null) {
                        /**
                         * 上传服务器代码
                         */
                        setPicToView(head);// 保存在SD卡中
                        iv_head_icon.setImageBitmap(head);// 用ImageView显示出来
                    }
                }
            case CHANGE_NICKNAME:

                if(data!=null){
                    new_info = data.getStringExtra("nickName");//从个人资料界面回传过来的数据
                    if(TextUtils.isEmpty(new_info)||new_info==null){
                        return;
                    }
                    tv_nickName.setText(new_info);
                    //更新数据库中的呢称字段
                    DBUtils.getInstance(UserInfoActivity.this).updateUserInfo("nickName", new_info,spUserName);

                }

                break;
            case CHANGE_SIGNATURE:

                if(data!=null){
                    new_info = data.getStringExtra("signature");//从个人资料界面回传过来的数据
                    if(TextUtils.isEmpty(new_info)||new_info==null){
                        return;
                    }
                    tv_signature.setText(new_info);
                    //更新数据库中的签名字段
                    DBUtils.getInstance(UserInfoActivity.this).updateUserInfo("signature", new_info,spUserName);

                }

                break;
            default:
                break;
        }

    }


    //修改性别的弹出框
    private void sexDialog(String sex) {
        int sexFlag = 0;
        if("男".equals(sex)){
            sexFlag = 0;
        }else if("女".equals(sex)){
            sexFlag = 1;
        }
        final String items[] = {"男","女"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("性别");//设置标题
        builder.setSingleChoiceItems(items, sexFlag, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(UserInfoActivity.this,items[which],Toast.LENGTH_SHORT).show();;
                setSex(items[which]);
            }
        });
        builder.show();

    }
    //更新界面上的性别数据
    private void setSex(String sex) {
        tv_sex.setText(sex);
        //更新数据库中的性别数据
        DBUtils.getInstance(this).updateUserInfo("sex",sex,spUserName);
    }
    /**
     * 调用系统的裁剪功能
     *
     * @param uri
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 40);
        intent.putExtra("outputY", 40);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName = path + "head.jpg";// 图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {

        }
    }
}
