package com.xu.myprojectstool;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.xu.baselib.UITools.CustomDialog;
import com.xu.myprojectstool.base.BaseInfo;
import com.xu.myprojectstool.base.ImageData;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ImageView imageChoose;
    private CustomDialog customDialog;
    private Button btn_second;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageChoose = (ImageView) findViewById(R.id.imageChoose);
        btn_second = (Button) findViewById(R.id.btn_second);

       setClick();

       setView();
    }

    private void setView() {

        ImageData imageData = BaseInfo.getInstance(MainActivity.this).findExist("iv_image_1");

        if (imageData != null){

            Bitmap bitmap = Tools.base64ToBitmap(imageData.getImageUrl());
            imageChoose.setImageBitmap(bitmap);

        }

    }

    private void setClick() {
        btn_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,TimerCheckActivity.class);
                startActivity(intent);
            }
        });

        imageChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customDialog = new CustomDialog(MainActivity.this,R.layout.layout_check_image);
                customDialog.setGravity(Gravity.CENTER);
                customDialog.show();

                customDialog.setOnItemClickListener(R.id.take_camera, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // 启动相机
                        startActivityForResult(intent1, 102);
                    }
                });
                customDialog.setOnItemClickListener(R.id.take_phone, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_PICK,null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                        startActivityForResult(intent,101);
                    }
                });
                customDialog.setOnItemClickListener(R.id.cancle, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        customDialog.dismiss();
                    }
                });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK){

            if (data != null){
//                imageChoose.setImageURI(data.getData());

                Uri mImageCaptureUri = data.getData();

                Bitmap photoBmp = null;

                if (mImageCaptureUri != null) {


                    try {
                        photoBmp = Tools.getBitmapFormUri(mImageCaptureUri,MainActivity.this);

                        ImageData imageData = new ImageData();
                        imageData.setName("iv_image_1");
                        imageData.setImageUrl(Tools.bitmapToBase64(photoBmp));

                        BaseInfo.getInstance(MainActivity.this).saveInfo(imageData);

                        imageChoose.setImageBitmap(photoBmp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }else if (requestCode == 102 && resultCode == RESULT_OK){

            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");
            imageChoose.setImageBitmap(bitmap);
        }
    }
}
