package customized.customized.background;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by User on 2017-11-05.
 */

public class CustomHandler extends Handler{
    private final String TAG = "CustomHandler";
    private final int Start_TessTwo = 0;
    private final int End_TessTwo = 1;

    public CustomHandler(){
    }
    @Override
    public void handleMessage(Message msg){
        switch (msg.what){
            case Start_TessTwo:
                Log.d("handler add : ", String.valueOf(msg.arg1));
                break;
            case End_TessTwo :
                int i = msg.arg1 ;
                int confidence = msg.arg2;
                String s = (String)msg.obj;
                Log.d(TAG,"thread end num : "+i+", confidence : "+confidence +", OcrText :" + s);
                break;
        }
    }
}
