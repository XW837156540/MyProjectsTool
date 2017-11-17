package com.xu.myprojectstool.base;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xu.baselib.Tool.Tools;
import com.xu.baselib.Tool.UITools;
import com.xu.baselib.UITools.CustomDialog;
import com.xu.myprojectstool.R;

import org.xutils.common.Callback;
import org.xutils.common.util.MD5;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/11/14.
 */
public class UpDataManager {

    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private final int DOWN_OK = 200;
    private final int DOWN_ERROR = -1;
    public Context context;
    private String[] msgItem;
    private String path;
    private int isForce;
    private String saveFileName = "";
    private CustomDialog updataDialog;
    private Handler dataHandler;
    String updataMsg = "";
    private boolean interceptFlag = false;
    private ProgressBar mProgress;
    private TextView showText;
    private int progress;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    showText.setText("正在下载中..." + progress + "%");
                    mProgress.setProgress(progress);
                    break;
                case DOWN_OVER:
                    if (dialog != null && dialog.isShowing()) {
                        try {
                            dialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    installApk();
                    break;
                case DOWN_OK:
                    if (dialog != null && dialog.isShowing()) {
                        try {
                            dialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    showText.setText("下载完成");
                    installApk();
                    break;
                case DOWN_ERROR:
                    showText.setText("下载失败,请检查");
                    if (dialog != null && dialog.isShowing()) {
                        try {
                            dialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                default:
                    break;
            }
        }
    };

    public Handler getDataHandler() {
        return dataHandler;
    }

    public void setDataHandler(Handler dataHandler) {
        this.dataHandler = dataHandler;
    }

    public UpDataManager(Context mContext, String msg, String path, int isforce) {

        this.context = mContext;
        msgItem = msg.split(";");
        this.path = path;
        this.isForce = isforce;
        //删除文件下的所有文件 节约存储空间
//        Tools.delAllFile(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/download/");
        saveFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/"
                + MD5.md5(path) + ".apk";

        updataDialog();

    }

    private void updataDialog() {

        updataDialog = new CustomDialog(context, R.style.MyDialog,R.layout.updata_dialog_one);
        updataDialog.setGravity(Gravity.CENTER);
        updataDialog.show();

        if (isForce == 0){
            updataDialog.setVisible(R.id.updata_view, View.VISIBLE);
            updataDialog.setVisible(R.id.text_cancel,View.VISIBLE);
        }else if (isForce == 1){
            updataDialog.setVisible(R.id.updata_view, View.GONE);
            updataDialog.setVisible(R.id.text_cancel,View.GONE);
        }

        for (int i = 0; i < msgItem.length; i++) {

            if (i == msgItem.length - 1) {
                updataMsg =updataMsg + msgItem[i];
            }else{
                updataMsg =updataMsg + msgItem[i] + " \n";
            }

        }

        updataDialog.setText(R.id.updata_msg,updataMsg);
        updataDialog.setOnItemClickListener(R.id.text_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dataHandler != null){
                    dataHandler.sendEmptyMessageDelayed(1,1000);
                }
                updataDialog.dismiss();
            }
        });

        updataDialog.setOnItemClickListener(R.id.text_sure, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updataPrompt(isForce);
                updataDialog.dismiss();
            }
        });


    }

    private void updataPrompt(int isForce) {

        if (Tools.isNetworkAvailable(context)){
            showNoticeDialog(isForce);
        }else{
            UITools.toastShowLong(context,"请先检查网络在重试");
        }
    }

    Dialog dialog;
    private void showNoticeDialog(int isForce) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("版本更新");
        builder.setMessage(updataMsg);

        builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showDownloadDialog(interceptFlag);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dataHandler != null) {
                    dataHandler.sendEmptyMessageDelayed(1, 1000);
                }
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        if (isForce == 1){
            dialog.setCancelable(true);
        }else{
            dialog.setCancelable(false);
        }
        dialog.show();
    }

    private void showDownloadDialog(boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("软件版本更新");
        final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.progeess, null);
        mProgress = (ProgressBar) v.findViewById(R.id.progress);
        showText = (TextView) v.findViewById(R.id.showText);
        builder.setView(v);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                interceptFlag = true;
                if (dataHandler != null) {
                    dataHandler.sendEmptyMessageDelayed(1, 1000);
                }

            }
        });
        dialog = builder.create();
        dialog.setCancelable(cancelable);
        dialog.show();
        try {
            downloadUpdateFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (dataHandler != null) {
            dataHandler.sendEmptyMessageDelayed(1, 1000);
        }
    }

    private void downloadUpdateFile() {

        RequestParams requestParams = new RequestParams(path);
        requestParams.setSaveFilePath(saveFileName);

        x.http().get(requestParams, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                double x_double = current * 1.0;
                double tempresult = x_double / total;
                DecimalFormat df1 = new DecimalFormat("0.00"); // ##.00%
                // 百分比格式，后面不足2位的用0补齐
                String result = df1.format(tempresult);
                Message message = mHandler.obtainMessage();

                progress = (int) (((float) current / total) * 100);
                message.what = DOWN_UPDATE;
                mHandler.sendMessage(message);
            }

            @Override
            public void onSuccess(File result) {
                // 下载成功
                Message message = mHandler.obtainMessage();
                message.what = DOWN_OK;
                mHandler.sendMessage(message);
                installApk();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //下载失败
                Message message = mHandler.obtainMessage();
                message.what = DOWN_ERROR;
                mHandler.sendMessage(message);
                if (ex.getLocalizedMessage().contains("No such file or directory")) {
                    UITools.toastShowLong(context,"对不起,下载的文件不存在");
                } else {
                    UITools.toastShowLong(context,"网络异常");
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    /**
     * 安装apk
     */
    private void installApk() {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(apkfile),
                "application/vnd.android.package-archive");
        context.startActivity(intent);

    }
}
