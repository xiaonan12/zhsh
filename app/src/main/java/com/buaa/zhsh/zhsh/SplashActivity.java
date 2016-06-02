package com.buaa.zhsh.zhsh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.buaa.zhsh.zhsh.utils.PrefUtils;

/**
 * 闪屏页面
 */
public class SplashActivity extends Activity {

    private ImageView rlRoot ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        rlRoot = (ImageView) findViewById(R.id.imageView1) ;
        //实现动画效果：渐变，缩放，旋转
       RotateAnimation animRotate = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,
               Animation.RELATIVE_TO_SELF,0.5f);
        animRotate.setDuration(1000);
        animRotate.setFillAfter(true);

        ScaleAnimation animScale = new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f) ;
        animScale.setDuration(1000);
        animScale.setFillAfter(true);

        AlphaAnimation animAlpha = new AlphaAnimation(0,1) ;
        animAlpha.setDuration(2000);
        animAlpha.setFillAfter(true);

        AnimationSet animSet = new AnimationSet(true);
        animSet.addAnimation(animRotate);
        animSet.addAnimation(animScale);
        animSet.addAnimation(animAlpha);

        rlRoot.startAnimation(animSet);

        animSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //判断是否进入新手引导页面
                boolean isGuideShow = PrefUtils.getBoolean("is_guide_show",false,getApplicationContext());

                if(isGuideShow){
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }else{
                    startActivity(new Intent(getApplicationContext(),GuideActivity.class));
                }

                finish();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
