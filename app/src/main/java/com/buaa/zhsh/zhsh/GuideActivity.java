package com.buaa.zhsh.zhsh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.buaa.zhsh.zhsh.utils.PrefUtils;

import java.util.ArrayList;

/**
 * 新手引导页面
 * Created by Administrator on 2016/5/30.
 */
public class GuideActivity extends Activity implements View.OnClickListener{
    private ViewPager mViewPager ;
    private int[] mImageIds = new int[]{R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3} ;
    private ArrayList<ImageView> mImageViewList;
    private LinearLayout llContainer;
    private ImageView ivRedPoint;
    private int mPointWidth;
    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏  必须在setContentView之前执行
        requestWindowFeature(Window.FEATURE_NO_TITLE) ;
        setContentView(R.layout.activity_guide);

        mViewPager = (ViewPager) findViewById(R.id.vp_pager);
        llContainer = (LinearLayout)findViewById(R.id.ll_container);
        ivRedPoint = (ImageView) findViewById(R.id.iv_red_point);
        btnStart = (Button) findViewById(R.id.btn_start);
        btnStart.setOnClickListener(this);
        //初始化ViewPager
        mImageViewList = new ArrayList<ImageView>();
        for (int i = 0; i < mImageIds.length; i++){
            ImageView view = new ImageView(this) ;
            view.setBackgroundResource(mImageIds[i]);
            mImageViewList.add(view) ;
            //初始化小圆点
            ImageView pointView = new ImageView(this) ;
            pointView.setImageResource(R.drawable.shape_circle_default);
            //设置小圆点距离
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT) ;
            if(i > 0){
                params.leftMargin = 10 ;
            }
            pointView.setLayoutParams(params);
            llContainer.addView(pointView) ;
        }
        mViewPager.setAdapter(new MyAdapter());

        //视图树，当页面绘制结束以后，计算两个圆点之间的距离
        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            //layout方法执行结束以后调用此方法（此时小圆点的位置已经确定）
            @Override
            public void onGlobalLayout() {
                //移除监听，因为监听只需要执行一次就行
                ivRedPoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                //获取小圆点间距
                mPointWidth = llContainer.getChildAt(1).getLeft() - llContainer.getChildAt(0).getLeft();



            }
        });

        //给mViewPager设置监听事件，页面滑动时候小红点跟着页面滑动
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            //页面滑动时候回调的方法
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //计算小红点的左边距
                int leftMagin = (int) (mPointWidth * positionOffset + mPointWidth * position);
                //修改小红点的左边距
                RelativeLayout.LayoutParams mparams = (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
                mparams.leftMargin = leftMagin ;
                ivRedPoint.setLayoutParams(mparams);

            }

            @Override
            public void onPageSelected(int position) {//最后页面显示开始体验按钮
                if(position==mImageIds.length-1){
                    btnStart.setVisibility(View.VISIBLE);
                }else{
                    btnStart.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    class MyAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return mImageIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = mImageViewList.get(position);
            container.addView(view) ;
            return view ;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public void onClick(View v) {
        //开始体验，记录新手引导已经被展示的状态，下次启动不会再展示
        PrefUtils.putBoolean("is_guide_show",true,this);
        switch (v.getId()){
            case R.id.btn_start:
                startActivity(new Intent(this,MainActivity.class));
                finish();
                break;

            default:
                break;
        }
    }
}
