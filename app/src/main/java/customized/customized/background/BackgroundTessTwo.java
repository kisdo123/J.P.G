package customized.customized.background;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

import capstone.android.OcrActivity;
import capstone.android.TessTwo;


/**
 * Created by User on 2017-11-04.
 */

public class BackgroundTessTwo extends Thread{
    private final String TAG = "BackgroundTessTwo";
    private final int Start_TessTwo = 0;
    private final int End_TessTwo = 1;
    private int number;
    private OcrActivity.TempValue tTempValue;
    private Handler mhandler;
    private TessTwo mTessTwo;
    private Bitmap mBitmap;


    public BackgroundTessTwo(OcrActivity.TempValue tempValue, Handler handler, TessTwo tessTwo, Bitmap bitmap, int num){
        this.tTempValue = tempValue; // 반드시 세팅해야되는 값
        this.mhandler = handler; // handler
        this.mTessTwo = tessTwo; //
        this.mBitmap = bitmap; //
        this.number = num; //
    }

    @Override
    public void run(){
        Log.d(TAG,"sucess to start thread : " + this.number);
        mhandler.sendMessage(mhandler.obtainMessage(Start_TessTwo,this.number));
        String s = this.mTessTwo.getOCRResult(this.mBitmap);
        int confidence = this.mTessTwo.getConfidence();
        this.tTempValue.setTempValue(s,confidence);
        mhandler.sendMessage(mhandler.obtainMessage(End_TessTwo,this.number,confidence,s));
    }
}
