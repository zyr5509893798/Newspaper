package com.example.newspaper;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import com.example.newspaper.DataBaseHelper.MyDataBaseHelper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.longsh.optionframelibrary.OptionBottomDialog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Home extends AppCompatActivity {

    private ImageView home_icon;
    private ImageButton home_back;
    private TextView home_name;
    //    private byte[] blob_picture;
    private String user_id;
    private byte[] a = null;
    private SharedPreferences sp;


    //拍照功能参数
    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;
    private final static int CROP_IMAGE = 3;

    //imageUri照片真实路径
    private Uri imageUri;
    //照片存储
    File filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        home_icon = findViewById(R.id.home_icon);
        home_back = findViewById(R.id.home_back);
        home_name = findViewById(R.id.home_name);

        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        SharedPreferences sharedPreferences=getSharedPreferences("user",MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id",null);


        MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(Home.this);
        SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
        Cursor cursor = database.query("user", new String[]{"username", "id"}, "id=?", new String[]{user_id}, null, null, null);
        if (cursor.moveToFirst()) {
            String username = cursor.getString(cursor.getColumnIndex("username"));
            home_name.setText(username);
        }
        cursor.close();
        Cursor cursor1 = database.query("user", new String[]{"icon", "id"}, "id=?", new String[]{user_id}, null, null, null);
        if (cursor1.moveToFirst()) {
            byte[] icon_btye = cursor1.getBlob(cursor1.getColumnIndex("icon"));
            Bitmap bitmap3 = getBitmapFromByte(icon_btye);
            home_icon.setImageBitmap(bitmap3);
            // home_name.setText(username);
        }
        cursor1.close();
        database.close();

//        home_icon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                AlertDialog.Builder dialog = new AlertDialog.Builder (Home.this);
//                dialog.setTitle("是否更改头像？");
//                dialog.setMessage("若更改头像请点击确定");
//                dialog.setCancelable(false);
//                dialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                        Toast.makeText(Home.this, "相机相机相册相册", Toast.LENGTH_SHORT).show();
//
//                        List<String> stringList = new ArrayList<String>();
//                        stringList.add("拍照");
//                        stringList.add("从相册选择");
//                        final OptionBottomDialog optionBottomDialog = new OptionBottomDialog(Home.this, stringList);
//                        optionBottomDialog.setItemClickListener(new AdapterView.OnItemClickListener() {
//
//
//                            @Override
//                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                //取消底部弹框
//                                //optionBottomDialog.dismiss();
//                                switch (position) {
//                                    case 0:
//                                        //测试使用，验证是否为position= 0
//                                        //Toast.makeText(RegisterIn.this,"position 0", Toast.LENGTH_SHORT ).show();
//
//                                        //启动相机程序
//                                        //隐式Intent
//                                        Intent intent_photo = new Intent( "android.media.action.IMAGE_CAPTURE" );
//                                        //putExtra()指定图片的输出地址，填入之前获得的Uri对象
//                                        imageUri = ImageUtils.getImageUri( Home.this );
//                                        intent_photo.putExtra( MediaStore.EXTRA_OUTPUT, imageUri );
//                                        //startActivity( intent_photo );
//                                        startActivityForResult( intent_photo, TAKE_PHOTO );
//                                        //底部弹框消失
//                                        optionBottomDialog.dismiss();
//                                        break;
//                                    case 1:
//                                        //测试使用，验证是否为position= 1
//                                        //Toast.makeText(RegisterIn.this,"position 1", Toast.LENGTH_SHORT ).show();
//                                        //打开相册
//                                        openAlbum();
//                                        //底部弹框消失
//                                        optionBottomDialog.dismiss();
//                                        break;
//                                    default:
//                                        break;
//                                }
//                            }
//                            //写到这了
//
//                        });
//
//                    }
//                });
//                dialog.show();
//            }
//        });

        //返回功能
        home_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

//        MyDataBaseHelper dataBaseHelper2 = new MyDataBaseHelper(Home.this);
//        SQLiteDatabase database2 = dataBaseHelper2.getReadableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("icon", a);
//        database2.insert("user", null, values);
//        values.clear();
//        database2.close();

    }

    //又在这开始写了
    //打开相册
    private void openAlbum() {
        Intent intent_album = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );

        startActivityForResult( intent_album, CHOOSE_PHOTO );
    }

    //剪切图片
    private void startImageCrop(File saveToFile,Uri uri) {
        if(uri == null){
            return ;
        }
        Intent intent = new Intent( "com.android.camera.action.CROP" );
        Log.i( "Test", "startImageCrop: " + "执行到压缩图片了" + "uri is " + uri );
        intent.setDataAndType( uri, "image/*" );//设置Uri及类型
        //uri权限，如果不加的话，   会产生无法加载图片的问题
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra( "crop", "true" );//
        intent.putExtra( "aspectX", 1 );//X方向上的比例
        intent.putExtra( "aspectY", 1 );//Y方向上的比例
        intent.putExtra( "outputX", 250 );//裁剪区的X方向宽
        intent.putExtra( "outputY", 250 );//裁剪区的Y方向宽
        intent.putExtra( "scale", true );//是否保留比例
        intent.putExtra( "outputFormat", Bitmap.CompressFormat.PNG.toString() );
        intent.putExtra( "return-data", false );//是否将数据保留在Bitmap中返回dataParcelable相应的Bitmap数据，防止造成OOM，置位false
        //判断文件是否存在
        //File saveToFile = ImageUtils.getTempFile();
        if (!saveToFile.getParentFile().exists()) {
            saveToFile.getParentFile().mkdirs();
        }
        //将剪切后的图片存储到此文件
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(saveToFile));
        Log.i( "Test", "startImageCrop: " + "即将跳到剪切图片" );
        startActivityForResult( intent, CROP_IMAGE );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //需要对拍摄的照片进行处理编辑
                    //拍照成功的话，就调用BitmapFactory的decodeStream()方法把图片解析成Bitmap对象，然后显示
                    Log.i("Test", "onActivityResult TakePhoto : " + imageUri);
                    //Bitmap bitmap = BitmapFactory.decodeStream( getContentResolver().openInputStream( imageUri ) );
                    //takephoto.setImageBitmap( bitmap );
                    //设置照片存储文件及剪切图片
                    File saveFile = ImageUtils.getTempFile();
                    filePath = ImageUtils.getTempFile();
                    startImageCrop(saveFile, imageUri);
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //选中相册照片显示
                    Log.i("Test", "onActivityResult: 执行到打开相册了");
                    try {
                        imageUri = data.getData(); //获取系统返回的照片的Uri
                        Log.i("Test", "onActivityResult: uriImage is " + imageUri);
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(imageUri,
                                filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String path = cursor.getString(columnIndex);  //获取照片路径
                        cursor.close();
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        //                        photo_taken.setImageBitmap(bitmap);
                        //设置照片存储文件及剪切图片
                        File saveFile = ImageUtils.setTempFile(Home.this);
                        filePath = ImageUtils.getTempFile();
                        startImageCrop(saveFile, imageUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CROP_IMAGE:
                if (resultCode == RESULT_OK) {
                    Log.i("Test", "onActivityResult: CROP_IMAGE" + "进入了CROP");
                    // 通过图片URI拿到剪切图片
                    //bitmap = BitmapFactory.decodeStream( getContentResolver().openInputStream( imageUri ) );
                    //通过FileName拿到图片
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath.toString());


                    //把裁剪后的图片展示出来
                    home_icon.setImageBitmap(bitmap);
                    Bitmap bitmap1 = compressImage(bitmap);
                    a = getBitmapByte(bitmap1);
                    MyDataBaseHelper myDataBaseHelper = new MyDataBaseHelper(Home.this);
                    SQLiteDatabase database = myDataBaseHelper.getReadableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("icon", a);
                    database.update("user", values, "id=?", new String[]{user_id});
                    database.close();
                    String b = a.toString();
                    Log.i( "zyr", b );

                    Intent intent990 = new Intent(Home.this, Main.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent990);
                }
                break;
            default:
                break;
        }
    }

    public static class ImageUtils {
        private static String TAG = "Test";
        public static File tempFile;

        public static Uri getImageUri(Context content) {
            File file = setTempFile( content );
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(Build.VERSION.SDK_INT >= 24){
                //将File对象转换成封装过的Uri对象，这个Uri对象标志着照片的真实路径
                Uri imageUri = FileProvider.getUriForFile( content, "com.example.newspaper.fileprovider", file );
                return imageUri;                                                          //a15927.bottombardemo
            }else{
                //将File对象转换成Uri对象，这个Uri对象标志着照片的真实路径
                Uri imageUri = Uri.fromFile( file );
                return imageUri;
            }
        }

        public static File setTempFile(Context content) {
            //自定义图片名称
            String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance( Locale.CHINA)) + ".png";
            Log.i( TAG, " name : "+name );
            //定义图片存放的位置
            tempFile = new File(content.getExternalCacheDir(),name);
            Log.i( TAG, " tempFile : "+tempFile );
            return tempFile;
        }

        public static File getTempFile() {
            return tempFile;
        }

        private static class Calendar {
            public static long getInstance(Locale china) {
                return 0;
            }
        }
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_collect:
                Intent intent = new Intent(Home.this,StoreActivity.class);
                startActivity(intent);
                break;

            case R.id.home_setting:
                Intent intent4 = new Intent(Home.this,SettingActivity.class);
                startActivity(intent4);
                break;

            case R.id.home_repassword:
                Intent intent99 = new Intent(Home.this,PasswordActivity.class);
                startActivity(intent99);
                break;
            case R.id.home_finish:
                Intent intent5 = new Intent(Home.this, Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                //忘记用户名、密码
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("USER_NAME", "");
                editor.putString("PASSWORD","");
                editor.commit();
                sp.edit().putBoolean("ISCHECK", false).commit();
                sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
                startActivity(intent5);
                break;
            case R.id.home_icon:

                AlertDialog.Builder dialog = new AlertDialog.Builder (Home.this);
                dialog.setTitle("是否更改头像？");
                dialog.setMessage("若更改头像请点击确定");
                dialog.setCancelable(false);
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(Home.this, "相机相机相册相册", Toast.LENGTH_SHORT).show();

                        List<String> stringList = new ArrayList<String>();
                        stringList.add("拍照");
                        stringList.add("从相册选择");
                        final OptionBottomDialog optionBottomDialog = new OptionBottomDialog(Home.this, stringList);
                        optionBottomDialog.setItemClickListener(new AdapterView.OnItemClickListener() {


                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //取消底部弹框
                                //optionBottomDialog.dismiss();
                                switch (position) {
                                    case 0:
                                        //测试使用，验证是否为position= 0
                                        //Toast.makeText(RegisterIn.this,"position 0", Toast.LENGTH_SHORT ).show();

                                        //启动相机程序
                                        //隐式Intent
                                        Intent intent_photo = new Intent( "android.media.action.IMAGE_CAPTURE" );
                                        //putExtra()指定图片的输出地址，填入之前获得的Uri对象
                                        imageUri = ImageUtils.getImageUri( Home.this );
                                        intent_photo.putExtra( MediaStore.EXTRA_OUTPUT, imageUri );
                                        //startActivity( intent_photo );
                                        startActivityForResult( intent_photo, TAKE_PHOTO );
                                        //底部弹框消失
                                        optionBottomDialog.dismiss();
                                        break;
                                    case 1:
                                        //测试使用，验证是否为position= 1
                                        //Toast.makeText(RegisterIn.this,"position 1", Toast.LENGTH_SHORT ).show();
                                        //打开相册
                                        openAlbum();
                                        //底部弹框消失
                                        optionBottomDialog.dismiss();
                                        break;
                                    default:
                                        break;
                                }
                            }
                            //写到这了

                        });

                    }
                });
                dialog.show();
                break;

        }
    }


    //对bitmap进行质量压缩
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public byte[] getBitmapByte(Bitmap bitmap){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    public Bitmap getBitmapFromByte(byte[] temp){   //将二进制转化为bitmap
        if(temp != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
            return bitmap;
        }else{
            return null;
        }
    }
//    public byte[] getBitmapByte(Bitmap bitmap){   //将bitmap转化为byte[]类型也就是转化为二进制
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//        try {
//            out.flush();
//            out.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return out.toByteArray();
//    }


}
