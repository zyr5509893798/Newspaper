package com.example.newspaper.AllAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.example.newspaper.ContentActivity;

import java.util.List;
import java.util.Map;

public class MyPagerAdapter extends PagerAdapter {
    private List<ImageView> data;
    private List<Map<String, Object>> imagelist;
    Context context;
    public MyPagerAdapter(List<ImageView> data, List<Map<String, Object>> imagelist, Context context) {
        this.imagelist=imagelist;
        this.data=data;
        this.context=context;
    }

    @Override
    public int getCount() {
        //返回一个无穷大的值，
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {

        return arg0==arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //注意，这里什么也不做!!!
//        MyPagerAdapter.(data.get(position % data.size()));
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView image=data.get((position)%data.size());
        final String url = imagelist.get((position)%data.size()).get("url").toString(); //这个非常重要
        final String news_id = imagelist.get((position)%data.size()).get("news_id").toString();
        final String newsname = imagelist.get((position)%data.size()).get("newsname").toString();
        final String images = imagelist.get((position)%data.size()).get("images").toString();
        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        ViewParent vp=image.getParent();
        if(vp!=null){
            ViewGroup vg=(ViewGroup) vp;
            vg.removeView(image);
        }
        final int iii = data.size();
        final String ii = String.valueOf(iii);
        image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                int jj;
//                if (position%data.size() == 2) {
//                    jj = 0;
//                } else if (position%data.size() == 3) {
//                    jj = 1;
//                } else if (position%data.size() == 0) {
//                    jj = 2;
//                } else if (position%data.size() == 1) {
//                    jj = 3;
//                }else if (position%data.size() == 4) {
//                    jj = 4;
//                } else {
//                    jj = 0;
//                }
                // TODO Auto-generated method stub
                Intent intent = new Intent(context, ContentActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("id", news_id);
                intent.putExtra("newsname", newsname);
                intent.putExtra("images", images);
                context.startActivity(intent);
//                Toast.makeText(context, "点击了图片", Toast.LENGTH_SHORT).show();
                //Log.d("zyr", ii);
            }
        });
        container.addView(data.get((position)%data.size()));
        return data.get((position)%data.size());
    }


}

