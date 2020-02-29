package com.example.newspaper;

import android.content.SharedPreferences;
import com.example.newspaper.DataBaseHelper.MyDataBaseHelper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newspaper.AllAdapter.StoreAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private String user_id;
    private ImageButton store_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        recyclerView = findViewById(R.id.store_recyclerview);
        store_back = findViewById(R.id.store_back);

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", null);

        final List<Map<String, Object>> list = new ArrayList<>();
        MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(StoreActivity.this);
        SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
        Cursor cursor = database.query("store", new String[]{"news_id", "user_id", "newsname", "photo", "url"}, "user_id=?", new String[]{user_id}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Map<String, Object> map = new HashMap<>();
                String newsname = cursor.getString(cursor.getColumnIndex("newsname"));
                String images = cursor.getString(cursor.getColumnIndex("photo"));
                String url = cursor.getString(cursor.getColumnIndex("url"));
                String news_id = cursor.getString(cursor.getColumnIndex("news_id"));
                map.put("newsname", newsname);
                map.put("images", images);
                map.put("url", url);
                map.put("news_id", news_id);
                list.add(map);
            } while (cursor.moveToNext());
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(StoreActivity.this));//纵向
        recyclerView.setAdapter(new StoreAdapter(StoreActivity.this, list));//绑适配器

        //返回功能
        store_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        RefreshLayout refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                list.clear();
                MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(StoreActivity.this);
                SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
                Cursor cursor = database.query("store", new String[]{"news_id", "user_id", "newsname", "photo", "url"}, "user_id=?", new String[]{user_id}, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        Map<String, Object> map = new HashMap<>();
                        String newsname = cursor.getString(cursor.getColumnIndex("newsname"));
                        String images = cursor.getString(cursor.getColumnIndex("photo"));
                        String url = cursor.getString(cursor.getColumnIndex("url"));
                        String news_id = cursor.getString(cursor.getColumnIndex("news_id"));
                        map.put("newsname", newsname);
                        map.put("images", images);
                        map.put("url", url);
                        map.put("news_id", news_id);
                        list.add(map);
                    } while (cursor.moveToNext());
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(StoreActivity.this));//纵向
                recyclerView.setAdapter(new StoreAdapter(StoreActivity.this, list));//绑适配器

                refreshlayout.finishRefresh();//传入false表示刷新失败
            }
        });
    }
}
