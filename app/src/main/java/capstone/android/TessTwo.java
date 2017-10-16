package capstone.android;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import com.googlecode.tesseract.android.TessBaseAPI;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.googlecode.tesseract.android.TessBaseAPI.*;
import static com.googlecode.tesseract.android.TessBaseAPI.PageSegMode.*;

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
    private final String TAG = "TessTwoCheck";
    private final String datapath;
    private ArrayList<String> strings;
    private TessBaseAPI mTess;
    Context context;

    public TessTwo(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.datapath = Environment.getExternalStorageDirectory() + "/ocrctz/";
        this.mTess = new TessBaseAPI();
        this.strings = new ArrayList<String>();
    }
    public void checktessdata(){
        File dir = new File(this.datapath + "tessdata/");
        Log.d(TAG+" 1","tessdata파일경로 :"+dir.getPath());
        File[] File = new File[2];
        File[0] = new File(this.datapath + "tessdata/" + "eng.traineddata");
        File[1] = new File(this.datapath + "tessdata/" + "kor.traineddata");
        for(int i=0;i<2;i++)
        {
            if (!dir.exists()) {
                Log.d(TAG+" 1-1", "in file doesn't exist");
                dir.mkdirs();
                copyFile(this.context, File[i].getName());
            } else{
                if(!File[i].exists()) {
                    copyFile(this.context, File[i].getName());
                    Log.d(TAG+ " 1-1-1", "생성 :" + File[i].getName());
                } else{
                    Log.d(TAG+ " 1-1-2","이상없음");
                }

            }
        }
    }
    public void tessTwoInit(String language,int psmMode, int ocrMode){
        checktessdata();
        if(ocrMode > 0){
            this.mTess.init(this.datapath, language, OEM_TESSERACT_ONLY);
        } else {
            this.mTess.init(this.datapath, language, OEM_DEFAULT);
        }
        setPsgMode(psmMode);
    }

    public void tessTwoEnd(){
        this.mTess.end();
    }
    public ArrayList<String> getOCRResult(ArrayList<Bitmap> convertbitmap) {
        Log.d(TAG+"1","convertbitmap size"+ convertbitmap.size());
        for(int i=0;i<convertbitmap.size();i++){
            mTess.setImage(convertbitmap.get(i));
            this.strings.add(mTess.getUTF8Text());
        }
        return this.strings;
    }

    private void setPsgMode(int i){
        switch(i){
            case 0: mTess.setPageSegMode(PSM_AUTO); break;
            case 1: mTess.setPageSegMode(PSM_SINGLE_COLUMN); break;
            case 2: mTess.setPageSegMode(PSM_SINGLE_BLOCK_VERT_TEXT); break;
            case 3: mTess.setPageSegMode(PSM_SINGLE_BLOCK); break;
            case 4: mTess.setPageSegMode(PSM_SINGLE_LINE); break;
            case 5: mTess.setPageSegMode(PSM_SINGLE_WORD); break;
            case 6: mTess.setPageSegMode(PSM_CIRCLE_WORD); break;
            case 7: mTess.setPageSegMode(PSM_SINGLE_CHAR); break;
            case 8: mTess.setPageSegMode(PSM_SPARSE_TEXT); break;
            case 9: mTess.setPageSegMode(PSM_SPARSE_TEXT_OSD); break;
            case 10: mTess.setPageSegMode(PSM_RAW_LINE); break;
            default: mTess.setPageSegMode(PSM_AUTO); break;
        }

    }
    public void stopRecognition() {
        mTess.stop();
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
            Log.d(TAG+" 2-1", "couldn't copy with the following error : " + e.toString());
        }
    }
}