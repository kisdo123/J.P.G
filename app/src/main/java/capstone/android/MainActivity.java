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

public class MainActivity extends AppCompatActivity {

    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    private static int PICK_IMAGE_REQUEST = 1;
    private String ImagePath = null;
    static final String TAG = "MainActivity";
    ImageView imgView;
    Button btn_gallery;
    Button btn_cam;
    Button btn_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gallery();
        Cam();
        Change();
    }

    private void gallery() {
        btn_gallery = (Button) findViewById(R.id.btn_gallery);
        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
    }
    private void Cam(){
        btn_cam = (Button)findViewById(R.id.btn_cam);
        btn_cam.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void Change(){
        btn_change = (Button)findViewById(R.id.btn_change);
        btn_change.setOnClickListener(new View.OnClickListener(){
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
                ImagePath = uri_path(uri);
                Log.d("CheckPoint1","이미지 절대 경로 : "+ImagePath);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                int nh = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);

                imgView = (ImageView) findViewById(R.id.imageView);
                imgView.setImageBitmap(scaled);

                Toast.makeText(this, "이미지 선택 완료", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, "취소되었습니다.", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "불러오지 못했습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed()
    {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;
        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
        {
            super.onBackPressed();
        }
        else
        {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }

    }

    private String uri_path(Uri uri){
        String res = null;
        String[] image_data = { MediaStore.Images.Media.DATA };
        Cursor cur = getContentResolver().query(uri, image_data, null, null, null);
        if(cur.moveToFirst()){
            int col = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cur.getString(col);
        }
        cur.close();
        return res;
    }
}