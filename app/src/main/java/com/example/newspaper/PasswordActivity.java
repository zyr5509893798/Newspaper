package com.example.newspaper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.newspaper.DataBaseHelper.MyDataBaseHelper;

public class PasswordActivity extends AppCompatActivity {

    private EditText password_et_oldp;
    private EditText password_et_newp;
    private EditText password_et_rep;
    private String password_oldp;
    private String password_newp;
    private String password_rep;
    private Button password_change;
    private String user_id;
    private String username;
    private String password;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        SharedPreferences sharedPreferences=getSharedPreferences("user",MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id",null);
        MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(PasswordActivity.this);
        SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
        Cursor cursor = database.query("user", new String[]{"id", "username", "password"}, "id=?", new String[]{user_id}, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                username = cursor.getString(cursor.getColumnIndex("username"));
                password = cursor.getString(cursor.getColumnIndex("password"));
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();

        password_change = findViewById(R.id.password_change);

        password_et_oldp = findViewById(R.id.pw_et_oldpassword);
        password_et_newp = findViewById(R.id.pw_et_newpassword);
        password_et_rep = findViewById(R.id.pw_et_repassword);

        setEditTextInputSpace(password_et_oldp);
        setEditTextInputSpace(password_et_newp);
        setEditTextInputSpace(password_et_rep);

        int maxLengthPassword = 12;
        InputFilter[] fArray =new InputFilter[1];
        fArray[0]=new  InputFilter.LengthFilter(maxLengthPassword);
        password_et_oldp.setFilters(fArray);
        password_et_newp.setFilters(fArray);
        password_et_rep.setFilters(fArray);

        //password_et_oldp.setText(password);

        password_change.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                password_oldp = password_et_oldp.getText().toString();
                password_newp = password_et_newp.getText().toString();
                password_rep = password_et_rep.getText().toString();
                Log.i( "zyr", " password_old= : "+password_oldp );
                Log.i( "zyr", " password_new= : "+password_newp );
                if (!password_oldp.equals(password)) {
                    Log.i( "zyr", " !password_old= : "+password_oldp );
                    Toast.makeText(PasswordActivity.this,"原密码输入不正确", Toast.LENGTH_SHORT).show();
                } else {
                    if (!password_newp.equals(password_rep)) {
                        Toast.makeText(PasswordActivity.this, "新密码与确认密码不相符", Toast.LENGTH_SHORT).show();
                    }else {

                        if (password_newp.length() < 6) {
                            Toast.makeText(PasswordActivity.this, "密码长度不能小于6位！", Toast.LENGTH_SHORT).show();
                        } else {
                            MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(PasswordActivity.this);
                            SQLiteDatabase database = dataBaseHelper.getReadableDatabase(); //打开数据库
                            ContentValues values = new ContentValues();
                            values.put("password", password_newp);
                            database.update("user", values, "id=?", new String[]{user_id});
                            values.clear();
                            database.close();
                            Toast.makeText(PasswordActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
                            //记住用户名、密码、
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("PASSWORD",password_newp);
                            editor.commit();
                            finish();
                        }
                    }
                }
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
