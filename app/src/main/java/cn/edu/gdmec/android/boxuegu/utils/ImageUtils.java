package cn.edu.gdmec.android.boxuegu.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import cn.edu.gdmec.android.boxuegu.activity.UserInfoActivity;

/**
 * Created by student on 18/1/3.
 */


public class ImageUtils {

    public final static int ACTIVITY_RESULT_CAMERA = 0003;//选择 拍照 的返回码
    public final static int ACTIVITY_RESULT_ALBUM = 0004;//选择 相册 的返回码

    public Uri photoUri;// 图片路径的URI
    private Uri tempUri;

    public File picFile;// 图片文件

    private Context context;
    private File tempFile;

    private Uri imageUri;

    // 构造方法
    public ImageUtils(Context context) {
        super();
        this.context = context;
    }

    // 选择拍照的方式
    public void byCamera() {
        try {
            // 创建文件夹
            File uploadFileDir = new File(
                    Environment.getExternalStorageDirectory(), "/icon");

            if (!uploadFileDir.exists()) {
                uploadFileDir.mkdirs();
            }
            // 创建图片，以当前系统时间命名
            picFile = new File(uploadFileDir,
                    SystemClock.currentThreadTimeMillis() + ".png");
            if (!picFile.exists()) {
                picFile.createNewFile();
            }
            // 获取到图片路径
            tempUri = Uri.fromFile(picFile);

            // 启动Camera的Intent，传入图片路径作为存储路径
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
            //启动Intent
            ((UserInfoActivity) context).startActivityForResult(cameraIntent,
                    ACTIVITY_RESULT_CAMERA);

            System.out.println("-->tempUri : " + tempUri.toString()
                    + "-->path:" + tempUri.getPath());
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*//獲取系統版本
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        // 激活相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            SimpleDateFormat timeStampFormat = new SimpleDateFormat(
                    "yyyy_MM_dd_HH_mm_ss");
            String filename = timeStampFormat.format(new Date());
            tempFile = new File(Environment.getExternalStorageDirectory(),
                    filename + ".jpg");
            if (currentapiVersion < 24) {
                // 从文件中创建uri
                imageUri = Uri.fromFile(tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            } else {
                //兼容android7.0 使用共享文件的形式
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, tempFile.getAbsolutePath());
                //检查是否有存储权限，以免崩溃
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    Toast.makeText(context,"请开启存储权限",Toast.LENGTH_SHORT).show();
                    return;
                }
                imageUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            }
        }
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
        ((UserInfoActivity) context).startActivityForResult(intent, ACTIVITY_RESULT_CAMERA);*/

    }

    public static boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }
    // 选择相册的方式
    public void byAlbum() {
        try {
            // 创建文件夹
            File pictureFileDir = new File(
                    Environment.getExternalStorageDirectory(), "/icon");
            if (!pictureFileDir.exists()) {
                pictureFileDir.mkdirs();
            }
            // 创建图片，以当前系统时间命名
            picFile = new File(pictureFileDir,
                    SystemClock.currentThreadTimeMillis() + ".png");
            if (!picFile.exists()) {
                picFile.createNewFile();
            }
            // 获取到图片路径
            tempUri = Uri.fromFile(picFile);

            // 获得剪辑图片的Intent
            final Intent intent = cutImageByAlbumIntent();
            ((UserInfoActivity) context).startActivityForResult(intent,
                    ACTIVITY_RESULT_ALBUM);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 调用图片剪辑程序的Intent
    private Intent cutImageByAlbumIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        //设定宽高比
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //设定剪裁图片宽高
        intent.putExtra("outputX", 40);
        intent.putExtra("outputY", 40);

        intent.putExtra("noFaceDetection", true);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        return intent;
    }

    //通过相机拍照后进行剪辑
    public void cutImageByCamera() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(tempUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        ((UserInfoActivity) context).startActivityForResult(intent,
                ACTIVITY_RESULT_ALBUM);
    }

    // 对图片进行编码成Bitmap
    public Bitmap decodeBitmap() {
        Bitmap bitmap = null;
        try {
            if (tempUri != null) {
                photoUri = tempUri;
                bitmap = BitmapFactory.decodeStream(context
                        .getContentResolver().openInputStream(photoUri));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
}
