package capstone.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class OcrActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);
        TextView textView = (TextView)findViewById(R.id.textView1);
        textView.setMovementMethod(new ScrollingMovementMethod());
    }

}