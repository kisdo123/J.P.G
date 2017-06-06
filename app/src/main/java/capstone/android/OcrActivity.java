package capstone.android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

public class OcrActivity extends AppCompatActivity {
    private TextView textView;
    private Button buttontrans;
    private Button buttonfinish;
    private TessTwo doOCR;
    private PptBuilder ppt;
    private Intent getPintent;
    private String imagePath;
    private String temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);
        textView = (TextView) findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());
        buttontrans = (Button) findViewById(R.id.button_trans);
        buttonfinish = (Button) findViewById(R.id.button_finish);
        getPintent = getIntent();
        imagePath = getPintent.getExtras().getString("ImagePath");
        ocrProcessing();
        pptProcessing();
    }

    private void ocrProcessing() {
        buttontrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doOCR = new TessTwo(OcrActivity.this);
                try {
                    Log.d("CheckPoint3", "받은 이미지 값 확인 :" + imagePath);
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    Log.d("CheckPoint4", "만든 bitmap 값 확인 :" + (bitmap == null));
                    temp = doOCR.getOCRResult(bitmap);
                    textView.setText(temp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void pptProcessing() {
        buttonfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    File sdCard = Environment.getExternalStorageDirectory();
                    File dir = new File (sdCard.getAbsolutePath() + "/J.P.G");
                    dir.mkdirs();
                    ppt = new PptBuilder();
                    Log.d("CheckPoint5", "PptBuilder 생성: " + (ppt != null));
                    ppt.MakeSlideLayout(temp, Environment.getExternalStorageDirectory().getAbsolutePath() + "/J.P.G/", imagePath);
                    moveTaskToBack(true);
                    finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}