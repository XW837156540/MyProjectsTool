package com.xu.myprojectstool.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.xu.myprojectstool.R;

/**
 * Created by Ricky on 2016/12/22.
 */

public class DefaultRefreshFooter extends RelativeLayout implements SwipeTrigger, SwipeLoadMoreTrigger {

    private ProgressBar mProgressBar;

    private TextView mTextView;

    public DefaultRefreshFooter(Context context) {
        super(context);
        initView(context);
    }

    public DefaultRefreshFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_default_refresh_footer, this, true);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mTextView = (TextView) findViewById(R.id.text);
        mTextView.setText("上拉加载更多");
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onPrepare() {
        mTextView.setText("上拉加载更多");
    }

    @Override
    public void onMove(int yScrolled, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            if (Math.abs(yScrolled) <= getHeight()) {
                mTextView.setText("上拉加载更多");
            } else {
                mTextView.setText("释放加载更多");
            }
        } else {
//            setText("LOAD MORE RETURNING");
        }
    }

    @Override
    public void onRelease() {
        mProgressBar.setVisibility(VISIBLE);
        mTextView.setText("加载中...");
    }

    @Override
    public void onComplete() {
        mProgressBar.setVisibility(GONE);
        mTextView.setText("加载完成");
    }

    @Override
    public void onReset() {
        mTextView.setText("上拉加载更多");
    }
}
