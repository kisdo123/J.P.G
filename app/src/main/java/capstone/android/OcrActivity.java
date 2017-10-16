package capstone.android;

import customstorage.GridAdapter;
import customstorage.Storage;
import gun0912.tedbottompicker.GridSpacingItemDecoration;
import opencv.Opencv;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
/*
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
*/
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OcrActivity extends AppCompatActivity {
    private final String TAG = "Checkpoint";
    private GridView gridView;
    private GridAdapter adapter;
    private ArrayList<Button> buttonList = new ArrayList<>();
    private Storage storage;
    private TessTwo tesstwo = new TessTwo(OcrActivity.this);
    private ArrayList<String> strings= null;
    private ArrayList<Bitmap> bitmaps = null;
    private PptBuilder ppt;
    private Opencv opencv;
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("OpenCV", "OpenCV loaded successfully");
                    OcrActivity.this.opencv = new Opencv(OcrActivity.this,new Mat());
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    private String temp;
    private static int psgmode,ocrmode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);
        setting();
        ocrProcessing();
        pptProcessing();
        editting();
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
        this.storage  = (Storage)getIntent().getSerializableExtra("customstorage");
        this.opencv = new Opencv(OcrActivity.this);
        this.tesstwo = new TessTwo(OcrActivity.this);
        this.bitmaps = new ArrayList<>();
        this.buttonList.add((Button)findViewById(R.id.button_trans));
        this.buttonList.add((Button)findViewById(R.id.button_finish));
        this.buttonList.add((Button)findViewById(R.id.button_editing));
        Log.d("OcrActivity0",
                "storage :"+this.storage.toString()+"\n"+
                        "buttonlist(1) :"+this.buttonList.get(0).toString()+"\n"+
                        "buttonlist(2) :"+this.buttonList.get(1).toString()+"\n"+
                        "gridView :"+this.gridView.toString()+"\n"

        );
    }

    private void ocrProcessing() {
        buttonList.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    tesstwo.tessTwoInit("eng+kor",ocrmode,psgmode);
                    Log.d(TAG+"3", "opencv 생성 확인 :" + "\nopencv 생성: " + (ppt==null));
                    Log.d(TAG+"3_1", "storage size :" +storage.getSize());
                    for(int i=0;i<storage.getSize();i++){
                        Log.d(TAG+"4", "받은 이미지 값 확인 :" + storage.getApath(i));
                        OcrActivity.this.bitmaps.add(BitmapFactory.decodeFile(storage.getApath(i)));
                        Log.d(TAG+"5", "만든 bitmap "+i+" 값 확인 :" + (OcrActivity.this.bitmaps.get(i) == null));
                    }
                    ArrayList<Bitmap> tBitmaps = new ArrayList<Bitmap>();
                    tBitmaps.addAll(opencv.processing0(OcrActivity.this.bitmaps,-1));
                    OcrActivity.this.strings =tesstwo.getOCRResult(tBitmaps);
                    tesstwo.tessTwoEnd();
                    OcrActivity.this.adapter = new GridAdapter(OcrActivity.this, OcrActivity.this.strings,-1);
                    OcrActivity.this.gridView.setAdapter(OcrActivity.this.adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                }
            }
        });
    }

    private void pptProcessing() {
        buttonList.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    checkJPGdir();
                    ppt = new PptBuilder();
                    Log.d(TAG+"6", "PptBuilder 생성: " + (ppt != null));
                    ppt.createPpt(temp, Environment.getExternalStorageDirectory().getAbsolutePath() + "/J.P.G/", storage.getApath(0));
                    moveTaskToBack(true);
                    finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void editting(){
        buttonList.get(2).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                show();
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
    private void show()
    {
        String[] strs = {"1,0","1,1","1,2","1,3","1,4","1,5","1,6","1,7","1,8","1,9","1,10",
                "-1,0","-1,1","-1,2","-1,3","-1,4","-1,5","-1,6","-1,7","-1,8","-1,9","-1,10"};
        final List<String> ListItems = new ArrayList<String>(Arrays.asList(strs));

        final CharSequence[] items =  ListItems.toArray(new String[ ListItems.size()]);

        final List SelectedItems  = new ArrayList();
        int defaultItem = 0;
        SelectedItems.add(defaultItem);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("AlertDialog Title");
        builder.setSingleChoiceItems(items, defaultItem,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SelectedItems.clear();
                        SelectedItems.add(which);
                    }
                });
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String msg="";
                        if (!SelectedItems.isEmpty()) {
                            int index = (int) SelectedItems.get(0);
                            msg = ListItems.get(index);
                        }
                        /*Toast.makeText(getApplicationContext(),
                                "Items Selected.\n"+ msg , Toast.LENGTH_LONG)
                                .show();
                                */
                        switch(msg){
                            case "1,0":ocrmode=1;psgmode=0;break;
                            case "1,1":ocrmode=1;psgmode=1;break;
                            case "1,2":ocrmode=1;psgmode=2;break;
                            case "1,3":ocrmode=1;psgmode=3;break;
                            case "1,4":ocrmode=1;psgmode=4;break;
                            case "1,5":ocrmode=1;psgmode=5;break;
                            case "1,6":ocrmode=1;psgmode=6;break;
                            case "1,7":ocrmode=1;psgmode=7;break;
                            case "1,8":ocrmode=1;psgmode=8;break;
                            case "1,9":ocrmode=1;psgmode=9;break;
                            case "1,10":ocrmode=1;psgmode=10;break;
                            case "-1,0":ocrmode=-1;psgmode=0;break;
                            case "-1,1":ocrmode=-1;psgmode=1;break;
                            case "-1,2":ocrmode=-1;psgmode=2;break;
                            case "-1,3":ocrmode=-1;psgmode=3;break;
                            case "-1,4":ocrmode=-1;psgmode=4;break;
                            case "-1,5":ocrmode=-1;psgmode=5;break;
                            case "-1,6":ocrmode=-1;psgmode=6;break;
                            case "-1,7":ocrmode=-1;psgmode=7;break;
                            case "-1,8":ocrmode=-1;psgmode=8;break;
                            case "-1,9":ocrmode=-1;psgmode=9;break;
                            case "-1,10":ocrmode=-1;psgmode=10;break;
                            default : break;

                        }
                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }
}