package com.qxc.app;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;


import com.lxp.sign.SignatureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SignActivity extends Activity {
    private SignatureView mSignaturePad;
    private Button mClearButton;
    private Button mSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // 在setContentView之前执行：
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        setContentView(R.layout.activity_main);
        mSignaturePad = (SignatureView) findViewById(R.id.signature_pad);
       // mSignaturePad.setBackgroundResource(R.drawable.a);
//mSignaturePad.setbgmap(R.mipmap.a);
        mSignaturePad.post(new Runnable() {
            @Override
            public void run() {
               // int viewWidth = mSignaturePad.getWidth();
                //int viewHeight = mSignaturePad.getHeight();
                SignatureView.viewwidth=mSignaturePad.getWidth();
                SignatureView.viewhight=mSignaturePad.getHeight();
                //Log.d(TAG, "viewWidth:" + viewWidth + "\nviewHeight:" + viewHeight);
            }
        });
        mSignaturePad.setBackgroundResource(R.mipmap.a);
        mSignaturePad.setOnSignedListener(new SignatureView.OnSignedListener() {
            @Override
            public void onSigned() {
                mSaveButton.setEnabled(true);
                mClearButton.setEnabled(true);
            }

            @Override
            public void onClear() {
                mSaveButton.setEnabled(false);
                mClearButton.setEnabled(false);
            }
        });

        mClearButton = (Button) findViewById(R.id.clear_button);
        mSaveButton = (Button) findViewById(R.id.save_button);

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignaturePad.clear();
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                JSONObject jsonObject1 = getBitbase64(signatureBitmap);
                if (jsonObject1!=null) {
                    Toast.makeText(SignActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    // 获取用户计算后的结果
                    //jsonObject1.toString()
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE); // 将文本内容放到系统剪贴板里。
                    cm.setText(jsonObject1.toString());
                    //Toast.makeText(getBaseContext(), "复制成功!", Toast.LENGTH_LONG).show();

                    intent.putExtra("Base64", jsonObject1.toString()); //将计算的值回传回去
                    // 通过intent对象返回结果，必须要调用一个setResult方法，
                    // setResult(888, data);第一个参数表示结果返回码，一般只要大于1就可以
                    setResult(2, intent);
                    finish(); //结束当前的activity的生命周期
                } else {
                    Toast.makeText(SignActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);

       // File file = new File(Environment.getExternalStorageDirectory(), albumName);
        if (!file.exists()) {
            Log.e("SignatureFile", "Directory not created");
            file.mkdirs();   //mkdir()父文件夹存在时新建  mkdirs()父文件夹不存在时新建
        }
        return file;
    }
/**
 * 保存为jpg图片
 * */
    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    public String addSignatureToGallery(Bitmap signature) {
    //    boolean result = false;
        String filename=String.format("Signature_%d.jpg",System.currentTimeMillis());
        try {

            File photo = new File(getAlbumStorageDir("SignatureFile")+filename);
      if(!photo.exists()){
//              //  if(photo.isFile()){
                    photo.createNewFile();
//            //    }
           }
            saveBitmapToJPG(signature, photo);
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(photo);
            mediaScanIntent.setData(contentUri);
            SignActivity.this.sendBroadcast(mediaScanIntent);
          //  result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filename;
    }
    public JSONObject getBitbase64(Bitmap bitmap){
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
       // OutputStream stream = new FileOutputStream(newBitmap.toString());
     //   Buffer buffer = null;
     //   newBitmap.copyPixelsToBuffer(buffer);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            String s = Base64.encodeToString( baos.toByteArray(), 1);
    JSONObject  jsonObject =new JSONObject();
        try {
            jsonObject.put("base64pic",s);
         //   jsonObject.put("base64pic",i++);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
}
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}


