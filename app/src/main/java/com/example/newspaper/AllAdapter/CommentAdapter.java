package com.example.newspaper.AllAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newspaper.R;

import java.util.List;
import java.util.Map;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Map<String, Object>> list;
    private Context context;
    public static final int ONE_ITEM = 1;
    public static final int TWO_ITEM = 2;
    public static final int THREE_ITEM = 3;
    public static final int FOUR_ITEM = 4;
    public static final int FIVE_ITEM = 5;
    private String cheak;


    public CommentAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        if (list.size() != 0) {
            cheak = list.get(position).get("cheak").toString();
        }else{
            cheak = "3";
        }
        if (cheak.equals("1")) {
            return TWO_ITEM;
//        } else if (list.get(position).getFromtype().equals("1")) {
//            return ONE_ITEM;
        } else if (cheak.equals("2")){
            return THREE_ITEM;
        } else if (cheak.equals("3")) {
            return FOUR_ITEM;
        } else if (cheak.equals("4")) {
            return FIVE_ITEM;
        } else {
            return ONE_ITEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TWO_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_long_comment, parent, false);
            return new LongViewHolder(view);
        } else if (viewType == THREE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_short_comment, parent, false);
            return new ShortViewHolder(view);
        } else if (viewType == FOUR_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_white_comment, parent, false);
            return new WhiteCommentViewHolder(view);
        } else if (viewType == FIVE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_web_comment, parent, false);
            return new WebCommentViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
            return new RecyclerViewHolder(view);
        }
    }

    @Override //对每一个子控件进行操作，在这里可以对子控件中的内容，按钮监听等进行控制
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
//        holder.comment_author.setText(list.get(position).get("author").toString());//将子控件中的文本换为map中的文本
//        holder.comment_content.setText(list.get(position).get("content").toString());
//        holder.comment_likes.setText(list.get(position).get("likes").toString());
//        final String url1 = list.get(position).get("avatar").toString(); //这个非常重要
//        String newStr = url1.replace("[","");
//        String newStr2 = newStr.replace("]","");
//        Glide.with(context).load(newStr2).into(holder.comment_avatar);
        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;
            recyclerViewHolder.comment_author.setText(list.get(position).get("author").toString());//将子控件中的文本换为map中的文本
            recyclerViewHolder.comment_content.setText(list.get(position).get("content").toString());
            recyclerViewHolder.comment_likes.setText(list.get(position).get("likes").toString());
            final String url1 = list.get(position).get("avatar").toString(); //这个非常重要
            String newStr = url1.replace("[", "");
            String newStr2 = newStr.replace("]", "");
            Glide.with(context).load(newStr2).into(recyclerViewHolder.comment_avatar);
        } else if (holder instanceof LongViewHolder) {
            LongViewHolder longViewHolder = (LongViewHolder) holder;
            longViewHolder.ii.setText(list.get(position).get("item").toString());
            //footViewHolder.iiii.setText("23");
        } else if (holder instanceof ShortViewHolder) {
            ShortViewHolder shortViewHolder = (ShortViewHolder) holder;
            shortViewHolder.jjj.setText(list.get(position).get("item").toString());
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
        if (list.size() == 0) {
            return 1;
        } else {
            return list.size();
        }
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView comment_author;
        private ImageView comment_avatar;
        private TextView comment_content;
        private TextView comment_likes;

        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            comment_author = itemView.findViewById(R.id.comment_author);
            comment_avatar = itemView.findViewById(R.id.comment_avatar);
            comment_content = itemView.findViewById(R.id.comment_content);
            comment_likes = itemView.findViewById(R.id.comment_likes);
        }
    }

    class LongViewHolder extends RecyclerView.ViewHolder {

        private TextView ii;

        LongViewHolder(View itemView) {
            super(itemView);
//            pbLoading = (ProgressBar) itemView.findViewById(R.id.pb_loading);
//            tvLoading = (TextView) itemView.findViewById(R.id.tv_loading);
//            llEnd = (LinearLayout) itemView.findViewById(R.id.ll_end);
            ii = itemView.findViewById(R.id.long_comment_num);
        }
    }

    class ShortViewHolder extends RecyclerView.ViewHolder {

        private TextView jjj;

        ShortViewHolder(View itemView) {
            super(itemView);
//            pbLoading = (ProgressBar) itemView.findViewById(R.id.pb_loading);
//            tvLoading = (TextView) itemView.findViewById(R.id.tv_loading);
//            llEnd = (LinearLayout) itemView.findViewById(R.id.ll_end);
            jjj = itemView.findViewById(R.id.short_comment_num);
        }
    }

    class WhiteCommentViewHolder extends RecyclerView.ViewHolder {

        WhiteCommentViewHolder(View itemView) {
            super(itemView);
        }
    }

    class WebCommentViewHolder extends RecyclerView.ViewHolder {

        WebCommentViewHolder(View itemView) {
            super(itemView);
        }
    }

}
