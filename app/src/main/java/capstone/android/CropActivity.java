package capstone.android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import customized.customized.data.Storage;

public class CropActivity extends Activity {
    final int REQ_ACTION_PICK_CROP = 1;
    private final String TAG = "CropActivity";
    private Intent getPintent;
    private String ImagePath;
    private String cropedPath;
    private int id;
    private TextView textViewjpg;
    private Bitmap bitmap;
    private ImageView imageview;
    private Button btncheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        imageview = (ImageView) findViewById(R.id.imageView);
        textViewjpg = (TextView)findViewById(R.id.textView_jpg);
        Typeface typeFace_eng = Typeface.createFromAsset(getAssets(), "fonts/cour.ttf");
        textViewjpg.setTypeface(typeFace_eng);

        cropStart();
        check();
    }

    private void cropStart() {
        this.getPintent = getIntent();
        this.id = getPintent.getIntExtra("ListViewid",0);
        this.ImagePath = getPintent.getStringExtra("StoragePath");
        Uri imageuri = Uri.fromFile(new File(this.ImagePath));
        Log.d("CheckPoint7-1", "경로 확인 : " + this.ImagePath);
        Log.d("CheckPoint7-2", "경로 확인 : " + imageuri);

        Intent intent = new Intent("com.android.camera.action.CROP");;
        intent.setDataAndType(imageuri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQ_ACTION_PICK_CROP);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_ACTION_PICK_CROP) {
                this.bitmap = data.getParcelableExtra("data");
                imageview.setImageBitmap(bitmap);
                this.cropedPath = saveBitmaptoJpeg(bitmap);
            }
        }
    }

    public String saveBitmaptoJpeg(Bitmap bitmap){
        String fileapth = Environment.getExternalStorageDirectory().getAbsolutePath()+"/J.P.G/Convert_";
        File origin = new File(this.ImagePath);
        String fileName = origin.getName();
        File file_path = new File(fileapth+fileName);
        try{
            FileOutputStream out = new FileOutputStream(file_path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

        }catch(FileNotFoundException exception){
            Log.e("FileNotFoundException", exception.getMessage());
        }catch(IOException exception){
            Log.e("IOException", exception.getMessage());
        }
        return file_path.getAbsolutePath();
    }

    public void check() {
        btncheck = (Button) findViewById(R.id.button_check);
        btncheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if(CropActivity.this.cropedPath!=null){
                    intent.putExtra("id",Integer.valueOf(CropActivity.this.id));
                    intent.putExtra("CropedPath",String.valueOf(CropActivity.this.cropedPath));
                    CropActivity.this.bitmap.recycle();
                    setResult(RESULT_OK,intent);
                    Log.d(TAG,"RESULT_OK = id :" + CropActivity.this.id+ " CropedPath :" +CropActivity.this.cropedPath);
                } else {
                    setResult(RESULT_CANCELED,intent);
                    Log.d(TAG,"RESULT_CANCELED");
                }
                finish();
            }
        });
    }

}