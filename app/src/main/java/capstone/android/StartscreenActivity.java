package capstone.android;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

public class StartscreenActivity extends AppCompatActivity {
    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                startActivity(new Intent(StartscreenActivity.this, MainActivity.class));
                finish();
            }
        };
        handler.sendEmptyMessageDelayed(0, 2000);
    }
}

