package com.xu.myprojectstool.refresh;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xu.myprojectstool.R;


/**
 * Created by scorpio on 16/5/23.
 */
public class MDProgressDialog extends Dialog {

    private Context mContext ;
    private  View view ;
    private ImageView pic ;
    private int size ;
    public MDProgressDialog(Context context) {
        super(context);
        mContext = context ;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.view_md_progress,null);
        pic = (ImageView) view.findViewById(R.id.gif);
        size = mContext.getResources().getDisplayMetrics().widthPixels / 3 - 50 ;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        setCancelable(true);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size,size);
        params.gravity = Gravity.CENTER;
        view.setLayoutParams(params);
    }


    public MDProgressDialog(Context context, int theme) {
        super(context, theme);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(view);
    }

}
