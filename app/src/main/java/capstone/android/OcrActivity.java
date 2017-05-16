package capstone.android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class OcrActivity extends AppCompatActivity
{
    private TextView textView;
    private Button button;
    private TessTwo doOCR;
    private Intent getPintent;
    private String imagePath; // 이 위로 선언하자마자 초기화 하지 않은 이유는 상수가 아닐시 onCreate되기전에 값요청을 해서 팅겨버림.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);
        textView = (TextView)findViewById(R.id.textView1);
        button = (Button)findViewById(R.id.button2);
        getPintent = getIntent();
        textView.setMovementMethod(new ScrollingMovementMethod());
        imagePath = getPintent.getExtras().getString("ImagePath");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doOCR = new TessTwo(OcrActivity.this);
                if(imagePath != null) {
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    String temp = doOCR.getOCRResult(bitmap);
                    textView.setText(temp);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "값전달이 되지 않음." , Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

}