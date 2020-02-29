package com.example.newspaper;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newspaper.AllAdapter.CommentAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentActivity extends AppCompatActivity {

    private String news_id;
    private String string_allnum = "0";
    private String long_comment_num = "0";
    private String short_comment_num = "0";
    //    private String newsname;
//    private String images;
//    private String url;
    private TextView text_allnum;
    private ImageButton comment_back;
    private Map map;
    private RecyclerView recyclerView;


    List<Map<String, Object>> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Intent intent = getIntent();
        news_id = intent.getStringExtra("news_id");
        string_allnum = intent.getStringExtra("allnum");
        long_comment_num = intent.getStringExtra("long_comments");
        short_comment_num = intent.getStringExtra("short_comments");

//        newsname = intent.getStringExtra("newsname");
//        url = intent.getStringExtra("url");
//        images = intent.getStringExtra("images");

        recyclerView = findViewById(R.id.comment_recyclerview);
//        comment_back = findViewById(R.id.comment_back);
        text_allnum = findViewById(R.id.comment_allnum);
//        text_allnum.setText(string_allnum);

//        //返回功能
//        comment_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(CommentActivity.this, ContentActivity.class);
//                intent.putExtra("id", news_id);
//                intent.putExtra("url", url);
//                intent.putExtra("newsname", newsname);
//                intent.putExtra("images", images);
//                startActivity(intent);
//                finish();
//            }
//        });
        if (string_allnum.equals("-1")) {
//            map = new HashMap();
//            map.put("cheak", "4");
//            list.add(map);
            string_allnum = "0";
        }

        text_allnum.setText(string_allnum);

        if ( !long_comment_num.equals("0")) {
            map = new HashMap();
            map.put("cheak", "1");
            map.put("item", long_comment_num);
            list.add(map);
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    //获取长评
                    URL url = new URL("https://news-at.zhihu.com/api/4/story/" + news_id + "/long-comments");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    showResponseLong(response.toString());
                    //短评
                    URL url1 = new URL("https://news-at.zhihu.com/api/4/story/" + news_id + "/short-comments");
                    connection = (HttpURLConnection) url1.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in1 = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in1));
                    StringBuilder response1 = new StringBuilder();
                    String line1;
                    while ((line1 = reader.readLine()) != null) {
                        response1.append(line1);
                    }
                    showResponseShort(response1.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    map = new HashMap();
                    map.put("cheak", "4");
                    list.add(map);
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        });

        thread.start();
    }

    //长评的
    public void showResponseLong(final String string) {


        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray("comments");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                int news_id = jsonObject1.getInt("news_id");
                String author = jsonObject1.getString("author");
                String avatar = jsonObject1.getString("avatar");  //头像
                String content = jsonObject1.getString("content");
                String likes = jsonObject1.getString("likes");
//                String news_id = jsonObject1.getString("id");


                map = new HashMap();

//                map.put("news_id",news_id);
                map.put("author", author);
                map.put("avatar", avatar);
                map.put("content", content);
                map.put("likes", likes);
                map.put("cheak", "0");
                map.put("item", "0");
//                map.put("id", news_id);

                list.add(map);
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    recyclerView.setLayoutManager(new LinearLayoutManager(CommentActivity.this));//纵向
                    recyclerView.setAdapter(new CommentAdapter(CommentActivity.this, list));
                    recyclerView.setNestedScrollingEnabled(false);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //短评的
    public void showResponseShort(final String string) {

        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray("comments");

            if (!short_comment_num.equals("0")) {
                map = new HashMap();
                map.put("cheak", "2");
                map.put("item", short_comment_num);
                list.add(map);
            }

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String author = jsonObject1.getString("author");
                String avatar = jsonObject1.getString("avatar");  //头像
                String content = jsonObject1.getString("content");
                String likes = jsonObject1.getString("likes");
//                String news_id = jsonObject1.getString("id");


                map = new HashMap();

//                map.put("news_id",news_id);
                map.put("author", author);
                map.put("avatar", avatar);
                map.put("content", content);
                map.put("likes", likes);
                map.put("cheak", "0");
                map.put("item", "0");
//                map.put("id", news_id);

                list.add(map);
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    recyclerView.setLayoutManager(new LinearLayoutManager(CommentActivity.this));//纵向
                    recyclerView.setAdapter(new CommentAdapter(CommentActivity.this, list));
                    recyclerView.setNestedScrollingEnabled(false);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
