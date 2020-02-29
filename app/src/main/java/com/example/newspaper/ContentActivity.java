package com.example.newspaper;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import com.example.newspaper.DataBaseHelper.MyDataBaseHelper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class ContentActivity extends AppCompatActivity {

    private String url;
    private String id;
    private ImageButton content_massage;
    private ImageButton content_one;
    private ImageButton content_store;
    private ImageButton content_share;
    private ImageButton content_back;
    private TextView content_message_num;
    private TextView content_one_num;
    private String comments;
    private String long_comments;
    private String short_comments;
    private String user_id;
    private String newsname;
    private String images;

    private static final String TAG = ContentActivity.class.getSimpleName();

    private String errorHtml = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        id = intent.getStringExtra("id");
        newsname = intent.getStringExtra("newsname");
        images = intent.getStringExtra("images");

        content_message_num = findViewById(R.id.content_massage_num);
        content_one_num = findViewById(R.id.content_one_num);

        final ProgressDialog progressDialog = new ProgressDialog(ContentActivity.this);
        progressDialog.setTitle("加载中");
        progressDialog.setMessage("正在加载中......");
        progressDialog.setCancelable(true);
        progressDialog.show();

//        Thread t = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                progressDialog.dismiss();
//            }
//        });
//        t.start();
        errorHtml = "<html><body><h1> </h1><h2>网络似乎开小差了……</h2></body></html>";
        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(url);
        //progressDialog.dismiss();

        content_massage = findViewById(R.id.content_massage);
        content_one = findViewById(R.id.content_one);
        content_store = findViewById(R.id.content_store);
        //content_share = findViewById(R.id.content_share);
        content_back = findViewById(R.id.content_back);

        SharedPreferences sharedPreferences=getSharedPreferences("user",MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id",null);

        //返回功能
        content_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ContentActivity.this, Main.class);
//                startActivity(intent);
                finish();
            }
        });

//        final boolean[] isIconChange = {false};
//        content_one.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isIconChange[0]) {
//                    content_one.setBackgroundResource(R.drawable.one1);
//                    isIconChange[0] = false;
//                } else {
//                    content_one.setBackgroundResource(R.drawable.one2);
//                    isIconChange[0] = true;
//                }
//            }
//        });

        MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(ContentActivity.this);
        SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
        Cursor cursor = database.query("store", new String[]{"news_id", "user_id"}, "user_id=? and news_id=?", new String[]{user_id, id}, null, null, null);
        if (cursor.moveToFirst()) {
            content_store.setBackgroundResource(R.drawable.store4);
        } else {
            content_store.setBackgroundResource(R.drawable.store3);
        }

        cursor.close();
        database.close();


//        final boolean[] isIconChange1 = {false};
//        content_store.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isIconChange1[0]) {
//                    content_store.setBackgroundResource(R.drawable.store1);
//                    isIconChange1[0] = false;
//                } else {
//                    content_store.setBackgroundResource(R.drawable.store2);
//                    isIconChange1[0] = true;
//                }
//            }
//        });

        Thread thread;

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    Date date = new Date();
                    //获取附加信息
                    URL url = new URL("https://news-at.zhihu.com/api/3/story-extra/" + id);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    //读取刚刚获取的输入流
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    showResponse(response.toString());
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    comments = "-1";
                    long_comments = "0";
                    short_comments = "0";
                    WifiOFF();
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

        content_massage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContentActivity.this,CommentActivity.class);
                intent.putExtra("news_id", id);
                intent.putExtra("allnum", comments);
                intent.putExtra("long_comments", long_comments);
                intent.putExtra("short_comments", short_comments);
//                intent.putExtra("url", url);
//                intent.putExtra("images", images);
//                intent.putExtra("newsname", newsname);
                startActivity(intent);
            }
        });

        content_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(ContentActivity.this);
                SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
                Cursor cursor = database.query("store", new String[]{"news_id", "user_id"}, "user_id=? and news_id=?", new String[]{user_id, id}, null, null, null);
                if (cursor.moveToFirst()) {
                    database.delete("store", "news_id=? and user_id=?", new String[]{id,user_id});
                    content_store.setBackgroundResource(R.drawable.store3);
                    Toast.makeText(ContentActivity.this, "已取消收藏", Toast.LENGTH_SHORT).show();
                } else {
                    ContentValues values = new ContentValues();
                    values.put("user_id", user_id);
                    values.put("news_id", id);
                    values.put("url", url);
                    values.put("newsname", newsname);
                    values.put("photo", images);
                    database.insert("store", null, values);
                    values.clear();
                    content_store.setBackgroundResource(R.drawable.store4);
                    Toast.makeText(ContentActivity.this, "已收藏", Toast.LENGTH_SHORT).show();
                }

                cursor.close();
                database.close();

            }
        });
    }

    //在线程中进行到catch 无网络
    public void WifiOFF() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(ContentActivity.this);
                dialog.setTitle("网络连接失败");
                dialog.setMessage("请检查网络设置。");
                dialog.setCancelable(true);
//                dialog.setPositiveButton("退出", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
//                    }
//                });
                dialog.show();
            }
        });
    }

    //评论
    public void showResponse( final String string){


        try {
            JSONObject jsonObject1 = new JSONObject(string);

            comments = jsonObject1.getString("comments");
            long_comments = jsonObject1.getString("long_comments");
            short_comments = jsonObject1.getString("short_comments");
            final String popularity = jsonObject1.getString("popularity");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    content_message_num.setText(comments);
                    content_one_num.setText(popularity);

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

    //    private void setErrorPage(WebView view) {
//        RelativeLayout layout = new RelativeLayout(context);
//        View v = LayoutInflater.from(context).inflate(R.layout.item_white_main, null);
//        layout.addView(v);
//        layout.setGravity(Gravity.CENTER);
//        RelativeLayout.LayoutParams relparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//        relparams.addRule(RelativeLayout.CENTER_IN_PARENT);
//        addContentView(layout, relparams);
//        layout.setVisibility(View.VISIBLE);
//        view.setVisibility(View.GONE);
//    }
    @Override
    protected void onResume() {

        super.onResume();
        Log.i(TAG, "–onResume()–");
    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i(TAG, "-MyWebViewClient->shouldOverrideUrlLoading()–");
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.i(TAG, "-MyWebViewClient->onPageStarted()–");
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.i(TAG, "-MyWebViewClient->onPageFinished()–");
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

            Log.i(TAG, "-MyWebViewClient->onReceivedError()–\n errorCode="+errorCode+" \ndescription="+description+" \nfailingUrl="+failingUrl);
            //这里进行无网络或错误处理，具体可以根据errorCode的值进行判断，做跟详细的处理。
            view.loadData(errorHtml, "text/html", "UTF-8");

        }
    }


}
