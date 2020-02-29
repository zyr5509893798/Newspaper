package com.example.newspaper.AllAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newspaper.ContentActivity;
import com.example.newspaper.R;

import java.util.List;
import java.util.Map;

public class StoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Map<String, Object>> list;
    private Context context;
    public static final int ONE_ITEM = 1;
    public static final int TWO_ITEM = 2;


    public StoreAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

//    @Override
//    public int getItemViewType(int position) {
//        if (list.get(position).getFromtype().equals("2")) {
//            return TWO_ITEM;
//        } else if (list.get(position).getFromtype().equals("1")) {
//            return ONE_ITEM;
//        } else {
//            return THREE_ITEM;
//        }
//    }

    @Override
    public int getItemViewType(int position) {
        if (list.size() == 0) {
            return TWO_ITEM;

        } else {
            return ONE_ITEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(context).inflate(R.layout.item_store, parent, false);
        if (viewType == TWO_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_white_store, parent, false);
            return new WhiteStoreViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_store, parent, false);
            return new RecyclerViewHolder(view);
        }
    }

    @Override //对每一个子控件进行操作，在这里可以对子控件中的内容，按钮监听等进行控制
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;
            recyclerViewHolder.store_newsname.setText(list.get(position).get("newsname").toString());//将子控件中的文本换为map中的文本
//        holder.main_image.setImageURI((Uri) list.get(position).get("images"));
            final String url = list.get(position).get("url").toString(); //这个非常重要
            final String newsname = list.get(position).get("newsname").toString(); //这个非常重要
            final String news_id = list.get(position).get("news_id").toString(); //这个非常重要
            final String url1 = list.get(position).get("images").toString(); //这个非常重要
            String newStr = url1.replace("[","");
            final String newStr2 = newStr.replace("]","");
            Glide.with(context).load(newStr2).into(recyclerViewHolder.store_picture);


            recyclerViewHolder.store_news.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ContentActivity.class);
                    intent.putExtra("url", url);
                    intent.putExtra("id", news_id);
                    intent.putExtra("images", newStr2);
                    intent.putExtra("newsname", newsname);
                    context.startActivity(intent);
                }
            });
        } else if (holder instanceof WhiteStoreViewHolder) {
            WhiteStoreViewHolder whiteStoreViewHolder = (WhiteStoreViewHolder) holder;
            whiteStoreViewHolder.white_store.setText("暂无收藏");
        }

        //在这里写相当于对每一个button子控件绑监听
//        holder.button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(context, allgoods.class); //在Adapter中添加活动跳转要这样写!!!
//                intent.putExtra("shop_id", shop_id);
//                context.startActivity(intent);
//            }
//        }); //position代表“行号”，自上而下递增
    }

    @Override
    public int getItemCount() {
        if (list.size() > 0) {
            return list.size();
        } else {
            return 1;
        }
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView store_newsname;
        private ImageView store_picture;
        private LinearLayout store_news;

        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            store_newsname = itemView.findViewById(R.id.store_newsname);
            store_picture = itemView.findViewById(R.id.store_picture);
            store_news = itemView.findViewById(R.id.store_news);
        }
    }

    class WhiteStoreViewHolder extends RecyclerView.ViewHolder {
        private TextView white_store;

        WhiteStoreViewHolder(@NonNull View itemView) {
            super(itemView);

            white_store = itemView.findViewById(R.id.white_store);
        }
    }
}

