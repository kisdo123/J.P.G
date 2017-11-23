package capstone.android;

import android.graphics.Bitmap;
import com.googlecode.tesseract.android.TessBaseAPI;
import static com.googlecode.tesseract.android.TessBaseAPI.*;

//Created by User on 2017-05-14.
public class TessTwo{
    private final String TAG = "TessTwoCheck";
    private String datapath;
    private TessBaseAPI mTess = new TessBaseAPI();

    public TessTwo(String datapath, String language, int i) {
        this.datapath = datapath;
        this.mTess.init(this.datapath, language, OEM_DEFAULT);
        this.mTess.setPageSegMode(i); // 0
    }

    public String getOCRResult(Bitmap b) {
        String str = null;
        mTess.setImage(b);
        str = mTess.getUTF8Text();
        if(str != null)
            return str;
        else
            return("No detected");
    }

    public int getConfidence(){
        return this.mTess.meanConfidence();
    }
    public void stopRecognition() {
            mTess.stop();
        }
    public void onDestroy() {if (mTess != null)mTess.end(); }
    public void tessTwoEnd(){
        this.mTess.end();
    }
}