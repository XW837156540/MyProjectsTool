package com.xu.baselib.UITools;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.xu.baselib.R;


/**
 * Created by scorpio on 16/8/25.
 */

public class MDCoustomDialog {
    private CustomDialog customDialog;

    private Context mContext;

    private View CustomView;

    private int dialog_height;

    private int mWindowAnimationId;

    private boolean isCustomAnimation;

    private FrameLayout mDialogContentView;

    private int dialog_width;
    private int backgroundColor;
    private boolean isAnim = true;

    private int color = Color.WHITE;

    public int getDialog_width() {
        return dialog_width;
    }

    public void setDialog_width(int dialog_width) {
        this.dialog_width = dialog_width;
    }

    private int gravity = Gravity.BOTTOM;

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        if (customDialog != null)
            customDialog.getWindow().setGravity(gravity);
        this.gravity = gravity;
    }

    public int getDialog_height() {
        return dialog_height;
    }

    public void setDialog_height(int dialog_height) {
        this.dialog_height = dialog_height;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public View getCustomView() {
        return CustomView;
    }

    public void setCustomView(View customView) {
        CustomView = customView;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public MDCoustomDialog(Context context) {
        mContext = context;
        dialog_height = context.getResources().getDisplayMetrics().widthPixels;
        dialog_width = dialog_height;
        backgroundColor = Color.WHITE;
        mDialogContentView = new FrameLayout(context);
    }

    public boolean isAnim() {
        return isAnim;
    }

    public void setAnim(boolean anim) {
        isAnim = anim;
    }

    public void create() {
        customDialog = new CustomDialog(mContext);
        customDialog.setCancelable(true);
    }

    public void show() {
        try {
            customDialog.show();
        } catch (Exception e) {

        }
    }

    public void show(final OnShowListener showListener) {
        try {
            customDialog.show();
            showListener.onShow();
        } catch (Exception e) {

        }
    }


    public void dismiss() {
        customDialog.dismiss();
    }

    public void cancel() {

    }


    public void dismiss(final OnDismissListener dismissListener) {
        customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dismissListener.onDismiss();
            }
        });
    }

    public FrameLayout getContentView() {
        return mDialogContentView;
    }

    public Dialog getDialog() {
        return customDialog;
    }


    private class CustomDialog extends Dialog {

        CustomDialog(Context context) {
            super(context);

            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(getDialog_width(), getDialog_height());
            mDialogContentView.setLayoutParams(lp);
            mDialogContentView.setBackgroundColor(getBackgroundColor());
            mDialogContentView.setPadding(0, 0, 0, 0);
            ViewGroup p = (ViewGroup) getCustomView().getParent();
            if (p != null) {
                p.removeAllViews();
            }
            mDialogContentView.removeAllViews();
            mDialogContentView.addView(getCustomView());

            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(mDialogContentView, lp);
            setCancelable(true);
            setCanceledOnTouchOutside(true);
            getWindow().setGravity(getGravity());
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            if (isAnim) {
                if (isCustomAnimation) {
                    getWindow().getAttributes().windowAnimations = mWindowAnimationId;
                } else {
                    getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                }
            }
        }

        @Override
        public void dismiss() {
            super.dismiss();
        }

        public CustomDialog(Context context, int theme) {
            super(context, theme);
        }

        protected CustomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }

    }

    public void removeAllViews() {
        if (mDialogContentView.getParent() != null)
            ((ViewGroup) mDialogContentView.getParent()).removeAllViews();
    }

    public void setWindowAnimations(int animationResId) {
        isCustomAnimation = true;
        mWindowAnimationId = animationResId;
    }

    public interface OnShowListener {
        void onShow();
    }

    public interface OnDismissListener {
        void onDismiss();
    }
}
