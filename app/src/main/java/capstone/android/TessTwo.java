package capstone.android;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/** Created by User on 2017-05-14.
 *The following notes are referenced in the URL : http://www.thecodecity.com/2016/09/creating-ocr-android-app-using-tesseract.html
 *The code below is the translation of the code below, and you can change the name of the class by changing the name of the class.
 *usage: private MyTessOCR mTessOCR; //-> Class Declaration, anytime you can rename that
 *mTessOCR = new TessOCR(MainActivity.this); //-> Creating Objects, you can other Activity if not.
 *String temp = mTessOCR.getOCRResult(bitmap); //-> start ocr, need to bitmap parameter
 */
/*
참조 URL : http://www.thecodecity.com/2016/09/creating-ocr-android-app-using-tesseract.html
클래스 이름은 마음대로 정한 뒤 해도 됨
사용법:
private MyTessOCR mTessOCR; //-> 클래스 선언 이름 바꿔도 됨
mTessOCR = new TessOCR(MainActivity.this); //-> 객체 생성 꼭 MainActivity 필요는 없고 다른 Activity여도 가능
String temp = mTessOCR.getOCRResult(bitmap); //-> ocr 함수, bitmap이 인자로 필요
*/
public class TessTwo {
    private String datapath;
    private TessBaseAPI mTess;
    Context context;

    public TessTwo(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
        datapath = Environment.getExternalStorageDirectory() + "/ocrctz/";
        File dir = new File(datapath + "/tessdata/");
        Log.d("CheckPoint3","tessdata파일경로 :"+dir.getPath());
        File[] File = new File[2];
        File[0] = new File(datapath + "/tessdata/" + "eng.traineddata");
        File[1] = new File(datapath + "/tessdata/" + "kor.traineddata");
        for(int i=0;i<2;i++)
        {
            if (!File[i].exists()) {
                Log.d("mylog", "in file doesn't exist");
                dir.mkdirs();
                copyFile(context,File[i].getName());
            } else{
                Log.d("mylog","dir 생성 오류");
            }
        }
        mTess = new TessBaseAPI();
        String language = "eng+kor";
        mTess.init(datapath, language);//Auto only
        mTess.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO_ONLY);
    }

    public void stopRecognition() {
        mTess.stop();
    }

    public String getOCRResult(Bitmap bitmap) {
        mTess.setImage(bitmap);
        String result = mTess.getUTF8Text();
        return result;
    }

    public void onDestroy() {
        if (mTess != null)
            mTess.end();
    }

    private void copyFile(Context context,String FilePath) {
        AssetManager assetManager = context.getAssets();
        try {
            InputStream in = assetManager.open("tessdata/"+FilePath);
            OutputStream out = new FileOutputStream(datapath + "/tessdata/" + FilePath);
            byte[] buffer = new byte[2048];
            int read = in.read(buffer);
            while (read != -1) {
                out.write(buffer, 0, read);
                read = in.read(buffer);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            Log.d("mylog", "couldn't copy with the following error : " + e.toString());
        }
    }
}