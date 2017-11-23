package capstone.android;

import customized.customized.background.BackgroundTessTwo;
import customized.customized.background.CustomHandler;
import customized.customized.listviewgroup1.ListViewAdapter;
import customized.customized.data.Storage;
import opencv.Opencv;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.content.res.AssetManager;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;

public class OcrActivity extends AppCompatActivity {
    private final String TAG = "OcrActivity";
    private final String datapath = Environment.getExternalStorageDirectory() + "/ocrctz/";
    private GridView gridView;
    private ListViewAdapter adapter;
    private Button[] buttonList;
    private Storage storage;
    private TextView textViewjpg;
    private PptBuilder ppt;
    private Opencv opencv;
    private BaseLoaderCallback mLoaderCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        this.mLoaderCallback =new BaseLoaderCallback(this) {
            @Override
            public void onManagerConnected(int status) {
                switch (status) {
                    case LoaderCallbackInterface.SUCCESS:
                    {
                        Log.i(TAG, "OpenCV loaded successfully");
                        OcrActivity.this.opencv = new Opencv(OcrActivity.this,new Mat());
                    } break;
                    default:
                    {
                        super.onManagerConnected(status);
                    } break;
                }
            }
        };
        setting();
        ocrProcessing();
        pptProcessing();
        crop();
    }

    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG+"(-1)", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_3_0, this, mLoaderCallback);
        } else {
            Log.d(TAG+"(-2)", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    private void setting() {
        this.gridView = (GridView) findViewById(R.id.grid_text);
        try{
            this.storage  = getStorage();
        } catch (Exception e){
            e.printStackTrace();
            Log.d(TAG+19,"error :" + e.toString());
        }
        this.opencv = new Opencv(OcrActivity.this);
        this.textViewjpg = (TextView)findViewById(R.id.textView_jpg);
        this.buttonList = new Button[]{(Button)findViewById(R.id.button_trans),(Button)findViewById(R.id.button_finish),(Button)findViewById(R.id.button_editing)};
        Log.d(TAG,"buttonList :"+(buttonList!=null));
        Typeface typeFace_eng = Typeface.createFromAsset(getAssets(), "fonts/cour.ttf");
        Typeface typeFace_kor = Typeface.createFromAsset(getAssets(), "fonts/NanumGothic.ttf");
        this.textViewjpg.setTypeface(typeFace_eng);
        this.buttonList[0].setTypeface(typeFace_kor);
        this.buttonList[1].setTypeface(typeFace_kor);
        this.buttonList[2].setTypeface(typeFace_kor);
        checkJPGdir();
        checktessdata();
    }

    //start는 스레드를 새로 만든 뒤  stack에다가 새로 추가하는것, run은 현재 스레드에서 바로 동작
    private void ocrProcessing() {
        buttonList[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //OcrActivity.this.storage.setPictureImage(0,(OcrActivity.this.opencv.processing0(OcrActivity.this.storage.getPictureImage(0))));
                TempValue[] tempValues = new TempValue[14];
                CustomHandler customHandler = new CustomHandler();
                TessTwo tessTwo[] = new TessTwo[14];
                BackgroundTessTwo[] backgroundTessTwos = new BackgroundTessTwo[14];
                int endCount = 0;
                try{
                    for(int j=0;j<OcrActivity.this.storage.getSize();j++){
                        for(int i=0;i<14;i++){
                            tempValues[i] = new TempValue();
                            tessTwo[i] = new TessTwo(OcrActivity.this.datapath,"eng+kor",i);
                            backgroundTessTwos[i] = new BackgroundTessTwo(tempValues[i],customHandler,tessTwo[i],OcrActivity.this.storage.getPictureImage(j),i);
                            backgroundTessTwos[i].setDaemon(true);
                            backgroundTessTwos[i].start();
                        }
                        try{
                            for(int t=0;t<14;t++){
                                backgroundTessTwos[t].join();
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                        while(true){
                            for(int t=0;t<14;t++){
                                if(backgroundTessTwos[t].getState()==Thread.State.TERMINATED){
                                    endCount = endCount +1;
                                    Log.d(TAG,"end count :" + endCount);
                                }
                            }
                            if(endCount == 14){
                                Log.d(TAG,"exit while(true)");
                                for(int i=0;i<14;i++){
                                    //Log.d(TAG," mVar.ocrtemptext :"+tempValues[i].ocrReuslt +"mVar.ocrconfidence :"+tempValues[i].confidence);
                                    tessTwo[i].tessTwoEnd();
                                    endCount = 0;
                                }
                                break;
                            }
                        }


                        OcrActivity.this.storage.setConfidence(j,tempValues[0].confidence);
                        for(int i=0;i<14;i++) {
                            if (tempValues[i].confidence >= OcrActivity.this.storage.getConfidence(j)){
                                OcrActivity.this.storage.setConfidence(j, tempValues[i].confidence);
                                OcrActivity.this.storage.setOcrResultText(j, tempValues[i].ocrReuslt);
                            }
                        }
                        Log.d(TAG,"Storage's high confidence :" + OcrActivity.this.storage.getConfidence(j));
                        Log.d(TAG,"storage's string :" + OcrActivity.this.storage.getOcrResultText(j));
                    }
                } catch( Exception e){
                    e.printStackTrace();
                } finally {
                    OcrActivity.this.adapter = new ListViewAdapter(OcrActivity.this, OcrActivity.this.storage,-1);
                    OcrActivity.this.gridView.setAdapter(OcrActivity.this.adapter);
                }
            }
        });
    }

    private void pptProcessing() {
        buttonList[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pptxName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/J.P.G/"+"test.pptx";
                try {
                    checkJPGdir();
                    ppt = new PptBuilder(OcrActivity.this.storage);
                    Log.d(TAG+"6", "PptBuilder 생성: " + (ppt != null));
                    ppt.Type1MakeSlide(pptxName);
                    moveTaskToBack(true);
                    finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void crop(){
        buttonList[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OcrActivity.this, CropActivity.class);
                intent.putExtra("Storage","");
                startActivity(intent);
            }
        });
    }

    private void checkJPGdir(){
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File (sdCard.getAbsolutePath() + "/J.P.G");
        if(dir.exists() == false){
            dir.mkdirs();
        }
    }

    private void checktessdata(){

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
                copyFile(File[i].getName());
            } else{
                if(!File[i].exists()) {
                    copyFile(File[i].getName());
                    Log.d(TAG+ " 1-1-1", "생성 :" + File[i].getName());
                } else{
                    Log.d(TAG+ " 1-1-2","이상없음");
                }

            }
        }
    }

    private void copyFile(String FilePath) {
        AssetManager assetManager = OcrActivity.this.getAssets();
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

    private Storage getStorage() throws Exception{
        FileInputStream fis = this.openFileInput("adfwe!@#as");
        ObjectInputStream is = new ObjectInputStream(fis);
        Storage storage = (Storage)is.readObject();
        is.close();
        fis.close();
        return storage;
    }

    public class TempValue{
        public String ocrReuslt=null;
        public int confidence = 0;

        public TempValue(){
            this.ocrReuslt = null;
            this.confidence = 0;
        }
        public void setTempValue(String string, int confidence){
            this.ocrReuslt = string;
            this.confidence =confidence ;
        }

        public void setTempValue(TempValue tempValue){
            this.ocrReuslt = tempValue.ocrReuslt;
            this.confidence = tempValue.confidence;
        }
    }
}