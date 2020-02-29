package com.example.newspaper;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import com.example.newspaper.DataBaseHelper.MyDataBaseHelper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {

    private EditText setting_username;
    private EditText setting_password;
    private Button btn_setting_username;
    private Button btn_setting_password;
    private String new_username;
    private String new_password;
    private String user_id;
    private Button setting_finish;
    private ImageButton setting_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setting_username = findViewById(R.id.setting_username);
        setting_finish = findViewById(R.id.setting_finish);
        setting_back = findViewById(R.id.setting_back);


        SharedPreferences sharedPreferences=getSharedPreferences("user",MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id",null);

        //限制输入空格
        setEditTextInputSpace(setting_username);

        //控制最大长度
        int maxLengthUserName =12;
        InputFilter[] fArray =new InputFilter[1];
        fArray[0]=new  InputFilter.LengthFilter(maxLengthUserName);
        setting_username.setFilters(fArray);

        //用户名修改
        setting_finish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new_username = setting_username.getText().toString();
                //检查输入数据
                if (TextUtils.isEmpty(new_username)){
                    Toast.makeText(SettingActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                }else {
                    int find = 0;
                    MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(SettingActivity.this);
                    SQLiteDatabase database = dataBaseHelper.getReadableDatabase(); //打开数据库
                    Cursor cursor = database.query("user", new String[]{"username"}, null, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        do {
                            String username = cursor.getString(cursor.getColumnIndex("username"));
                            if (new_username.equals(username)){
                                find = 1;
                                break;
                            }
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                    if (find == 0) {
                        //向表中添加数据
                        ContentValues values = new ContentValues();
                        values.put("username", new_username);
                        database.update("user", values, "id=?", new String[]{user_id});
                        values.clear();
                        database.close();
                        Toast.makeText(SettingActivity.this, "用户名修改成功", Toast.LENGTH_SHORT).show();
                        Intent intent4 = new Intent(SettingActivity.this, Main.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent4);
                    } else if (find == 1){
                        Toast.makeText(SettingActivity.this,"该用户名已存在", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //返回功能
        setting_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }

    //防止空格回车
    public static void setEditTextInputSpace(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ") || source.toString().contentEquals("\n")) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }
}
