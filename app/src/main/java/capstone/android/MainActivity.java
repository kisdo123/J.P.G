package capstone.android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    private static int PICK_IMAGE_REQUEST = 1;
    static final String TAG = "MainActivity";

    ImageView imgView;
    Button btn_gallery = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gallery();
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && null != data) {
                Uri uri = data.getData();

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

    public  void onClick_cam(View view){
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClick_change(View view) {
        Intent intent = new Intent(this, OcrActivity.class);
        startActivity(intent);
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

}