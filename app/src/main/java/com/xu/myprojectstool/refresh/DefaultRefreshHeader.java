package com.xu.myprojectstool.refresh;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.xu.myprojectstool.R;
import java.text.SimpleDateFormat;
import java.util.Date;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * Created by scorpio on 16/8/17.
 */
public class DefaultRefreshHeader extends FrameLayout implements PtrUIHandler, SwipeRefreshTrigger, SwipeTrigger {
    ImageView pull_to_refresh_image;
    ProgressBar pull_to_refresh_progress;
    TextView pull_to_refresh_text;
    TextView pull_to_refresh_text_time;
    View view;
    View mRotateView;
    boolean isRota = false;
    private int i = 0;
    private String lastTimeKey = "LastTimeKey";
    private long lastTime;

    public DefaultRefreshHeader(Context context) {
        super(context);
        initView(context);
    }

    public DefaultRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DefaultRefreshHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        view = inflate(context, R.layout.pull_to_refresh_haderview, null);
        pull_to_refresh_image = (ImageView) view.findViewById(R.id.pull_to_refresh_image);
        pull_to_refresh_progress = (ProgressBar) view.findViewById(R.id.pull_to_refresh_progress);
        pull_to_refresh_text = (TextView) view.findViewById(R.id.pull_to_refresh_text);
        pull_to_refresh_text_time = (TextView) view.findViewById(R.id.pull_to_refresh_text_time);
        mRotateView = view.findViewById(R.id.refresh_content);
        addView(view);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        pull_to_refresh_text.setText("下拉可以刷新");
        mRotateView.setVisibility(VISIBLE);
        pull_to_refresh_progress.setVisibility(GONE);
        pull_to_refresh_image.setVisibility(VISIBLE);
        pull_to_refresh_image.clearAnimation();
        upLastRefreshTime();
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        // 更新时间
        upLastRefreshTime();
    }

    /**
     * 最后更新时间
     */
    private void upLastRefreshTime() {
        pull_to_refresh_text_time.setText("最后更新:" + getLastTime());
    }


    private String getLastTime() {
        SharedPreferences sp = getContext().getSharedPreferences("TIME", 0);
        long preTime = sp.getLong(lastTimeKey, 0);
        if (preTime == 0) {
            lastTime = new Date().getTime();
        } else {
            lastTime = preTime;
        }
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String dateString = format.format(new Date(lastTime));
        return "今天 " + dateString;
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        pull_to_refresh_text.setText("正在刷新数据中...");
        pull_to_refresh_progress.setVisibility(VISIBLE);
        pull_to_refresh_image.setVisibility(GONE);
        pull_to_refresh_image.clearAnimation();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        pull_to_refresh_text.setText("刷新完成");
        //隐藏动画
        Animation hideanim = new TranslateAnimation(0, 0, 1, 0);
        hideanim.setDuration(200);
        hideanim.setFillAfter(true);
        mRotateView.startAnimation(hideanim);
        // 记录更新时间
        SharedPreferences sp = getContext().getSharedPreferences("TIME", 0);
        lastTime = new Date().getTime();
        sp.edit().putLong(lastTimeKey, lastTime).commit();
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        final int moffsetToRefresh = frame.getOffsetToRefresh();
        final int currentPos = ptrIndicator.getCurrentPosY();
        final int lastPos = ptrIndicator.getLastPosY();
        if (pull_to_refresh_image.isShown()) {
            if (currentPos < moffsetToRefresh && lastPos >= moffsetToRefresh) {
                if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                    Animation anim = new RotateAnimation(-180, 0, pull_to_refresh_image.getWidth() / 2, pull_to_refresh_image.getHeight() / 2);
                    anim.setDuration(200);
                    anim.setFillAfter(true);
                    pull_to_refresh_image.startAnimation(anim);
                    pull_to_refresh_text.setText("下拉可以刷新");
                    isRota = true;
                }
            } else if (currentPos > moffsetToRefresh && lastPos <= moffsetToRefresh) {
                Animation anim = new RotateAnimation(0, -180, pull_to_refresh_image.getWidth() / 2, pull_to_refresh_image.getHeight() / 2);
                anim.setDuration(200);
                anim.setFillAfter(true);
                pull_to_refresh_text.setText("松开立即刷新");
                pull_to_refresh_image.startAnimation(anim);
            }
        }
    }

    @Override
    public void onRefresh() {
        pull_to_refresh_text.setText("正在刷新数据中...");
        pull_to_refresh_progress.setVisibility(VISIBLE);
        pull_to_refresh_image.setVisibility(GONE);
        pull_to_refresh_image.clearAnimation();
    }

    @Override
    public void onPrepare() {
        upLastRefreshTime();
    }

    @Override
    public void onMove(int yScrolled, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            if (yScrolled >= getHeight()) {
                if (isRota) {
                    Animation anim = new RotateAnimation(-180, 0, pull_to_refresh_image.getWidth() / 2, pull_to_refresh_image.getHeight() / 2);
                    anim.setDuration(200);
                    anim.setFillAfter(true);
                    pull_to_refresh_image.startAnimation(anim);
                    pull_to_refresh_text.setText("松开立即刷新");
                    isRota = false;
                }
            } else {
                if (!isRota) {
                    pull_to_refresh_progress.setVisibility(GONE);
                    Animation anim = new RotateAnimation(0, 180, pull_to_refresh_image.getWidth() / 2, pull_to_refresh_image.getHeight() / 2);
                    anim.setDuration(200);
                    anim.setFillAfter(true);
                    pull_to_refresh_text.setText("下拉可以刷新");
                    pull_to_refresh_image.startAnimation(anim);
                    isRota = true;
                }
            }
        } else {
//            setText("REFRESH RETURNING");
        }
    }

    @Override
    public void onRelease() {
    }

    @Override
    public void onComplete() {
        pull_to_refresh_text.setText("刷新完成");
        //隐藏动画
        Animation hideanim = new TranslateAnimation(0, 0, 1, 0);
        hideanim.setDuration(200);
        hideanim.setFillAfter(true);
        mRotateView.startAnimation(hideanim);
        // 记录更新时间
        SharedPreferences sp = getContext().getSharedPreferences("TIME", 0);
        lastTime = new Date().getTime();
        sp.edit().putLong(lastTimeKey, lastTime).commit();
    }

    @Override
    public void onReset() {

    }
}
