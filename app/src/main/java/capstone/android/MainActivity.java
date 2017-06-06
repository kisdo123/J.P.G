package capstone.android;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import static capstone.android.R.id.btn_cam;
import static capstone.android.R.id.btn_change;
import static capstone.android.R.id.btn_gallery;

public class MainActivity extends AppCompatActivity {

    private BackPressCloseHandler backPressCloseHandler;
    private static int PICK_IMAGE_REQUEST = 1;
    private String ImagePath = null;
    ImageView imgView;
    Button btngallery;
    Button btncam;
    Button btnchange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backPressCloseHandler = new BackPressCloseHandler(this);
        gallery();
        Cam();
        Change();
    }

    private void gallery() {
        btngallery = (Button) findViewById(btn_gallery);
        btngallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
    }
    private void Cam(){
        btncam = (Button)findViewById(btn_cam);
        btncam.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivity(intent);
            }
        });
    }

    private void Change(){
        btnchange = (Button)findViewById(btn_change);
        btnchange.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, OcrActivity.class);
                intent.putExtra("ImagePath",ImagePath);
                Log.d("CheckPoint2","이미지 경로 확인"+ImagePath);
                startActivity(intent);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && null != data) {
                Uri uri = data.getData();
                ImagePath = getImageApath(uri);
                Log.d("CheckPoint1","이미지 절대 경로 : "+ImagePath);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                int nh = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);

                imgView = (ImageView) findViewById(R.id.imageView);
                imgView.setImageBitmap(scaled);

                Toast.makeText(this, "이미지 선택 완료", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "취소되었습니다.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    private String getImageApath(Uri Rpath){
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(Rpath, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        return picturePath;
    }
}