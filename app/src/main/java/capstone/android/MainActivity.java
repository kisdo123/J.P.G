package capstone.android;
import customized.customized.background.BackgroundProcess2;
import customized.customized.listviewgroup1.ListViewAdapter;
import customized.customized.data.Storage;
import gun0912.tedbottompicker.TedBottomPicker;


import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity{
    private final int checkcode[] ={111,222,333};
    private final String TAG = "MainActivity";
    private BackPressCloseHandler backPressCloseHandler = new BackPressCloseHandler(this);
    private Storage storage;
    private Button[] buttonList;
    private TextView[] textviewList;
    private ListView listView;
    private ListViewAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 모든 Activity에 종속된 함수들은 Activity가 먼저 실행되어야 할당될 수 있으므로 제일 먼저 실행되게 조작한다.
        setting(); // Activity에 들어갈 모든 객체 및 View 및 Button의 초기화
        gallery();
        Cam();
        Change();
        this.listView.setOnItemClickListener(onItemClickListener);
    }

    private void setting() {
        this.storage = new Storage();
        this.buttonList = new Button[]{(Button)findViewById(R.id.btn_gallery),(Button)findViewById(R.id.btn_cam),(Button)findViewById(R.id.btn_change)};
        this.textviewList = new TextView[]{(TextView)findViewById(R.id.tV),(TextView)findViewById(R.id.textCam),(TextView)findViewById(R.id.textGal)};
        this.listView = (ListView) findViewById(R.id.list_view);
        Typeface typeFace_eng = Typeface.createFromAsset(getAssets(), "fonts/cour.ttf");
        Typeface typeFace_kor = Typeface.createFromAsset(getAssets(), "fonts/NanumGothic.ttf");
        this.buttonList[0].setTypeface(typeFace_eng);
        this.buttonList[1].setTypeface(typeFace_eng);
        this.buttonList[2].setTypeface(typeFace_kor);
        this.textviewList[0].setTypeface(typeFace_eng);
        this.textviewList[1].setTypeface(typeFace_eng);
        this.textviewList[2].setTypeface(typeFace_eng);
    }

    private void gallery() {
        buttonList[0].setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,GalleryActivity.class);
            intent.putExtra("currentSize",0);
            startActivityForResult(intent,222);
            /*
            TedBottomPicker bottomSheetDialogFragment  = new TedBottomPicker.Builder(MainActivity.this)
                    .setPreviewMaxCount(100)
                    .setSelectMaxCount(20-storage.getSize())
                    .showTitle(false)
                    .showCameraTile(false)
                    .setEmptySelectionText("선택하지 않았습니다.")
                    .setSelectMaxCountErrorText("최대 20개 까지입니다")
                    .setCompleteButtonText("선택 완료")
                    .setEmptySelectionText("No Select")
                    .setOnMultiImageSelectedListener(new TedBottomPicker.OnMultiImageSelectedListener(){
                        @Override
                        public void onImagesSelected(ArrayList<Uri> uriList){
                            ArrayList<String> atemp = new ArrayList<String>();
                            for(int i=0;i<uriList.size();i++){
                                Log.d("TedBottomPicker","temp0 :" +uriList.get(i).getPath());
                                MainActivity.this.storage.addImageData(uriList.get(i).getPath());
                            }
                            MainActivity.this.adapter = new ListViewAdapter(MainActivity.this, MainActivity.this.storage,10);
                            MainActivity.this.listView.setAdapter(MainActivity.this.adapter);
                            visible();
                        }
                    })
                    .create();
            bottomSheetDialogFragment.show(getSupportFragmentManager());
            */
        });
    }

    private void Cam(){
        buttonList[1].setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
            startActivityForResult(intent,checkcode[0]);
        });
    }

    private void Change(){
        buttonList[2].setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OcrActivity.class);
            try {
                writeObjectFile();
            } catch (Exception e){
                e.printStackTrace();
            }
            startActivity(intent);
        });
        //buttonList[2].setVisibility(View.GONE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ArrayList<String> absolList;
        int id;
        String cropedPath;
        if (requestCode == checkcode[0]) {
                switch(resultCode){
                    case RESULT_OK:
                        absolList = data.getStringArrayListExtra("CabsoulteFilePaths");
                        for(int i = 0; i<absolList.size();i++){
                            this.storage.addImageData(absolList.get(i));
                        }
                        try{
                            this.adapter = new ListViewAdapter(MainActivity.this,this.storage,10);
                            this.listView.setAdapter(this.adapter);
                            visible();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    default:
                        Toast.makeText(this, "취소되었습니다.", Toast.LENGTH_SHORT).show();
                        break;
                }
        } else if(requestCode == checkcode[1]) {
            switch(resultCode){
                case RESULT_OK:
                    absolList = data.getStringArrayListExtra("gAbsoulteFilePaths");
                    for(int i = 0; i < absolList.size(); i++){
                        this.storage.addImageData(absolList.get(i));
                    }
                    this.adapter = new ListViewAdapter(MainActivity.this,this.storage,10);
                    this.listView.setAdapter(this.adapter);
                    visible();
                    Toast.makeText(this, "확인.", Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(this, "취소되었습니다.", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        } else if(requestCode == checkcode[2]){
            switch (resultCode){
                case RESULT_OK:
                    id = data.getIntExtra("id",0);
                    cropedPath = data.getStringExtra("CropedPath");
                    Bitmap b = BitmapFactory.decodeFile(cropedPath);
                    View v = this.listView.getChildAt(id);
                    ImageView imageView = v.findViewById(R.id.croped_image);
                    imageView.setImageBitmap(b);
                    this.storage.setCropedImage(id,b);
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(this,"Crop 취소",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        } else {
            Toast.makeText(this, "알 수 없는 오류.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    private void visible(){
        textviewList[1].setVisibility(View.GONE);
        textviewList[2].setVisibility(View.GONE);
        buttonList[0].setVisibility(View.GONE);
        buttonList[1].setVisibility(View.GONE);
        buttonList[2].setVisibility(View.VISIBLE);
    }

    private void writeObjectFile() throws Exception{
        FileOutputStream fos = this.openFileOutput("adfwe!@#as",Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(this.storage);
        os.close();
        fos.close();
    }

    private ListView.OnItemClickListener onItemClickListener = (adapterView, view, i, l) -> {
        Intent intent = new Intent(MainActivity.this, CropActivity.class);
        String s = this.storage.getAbsolutePath(i);
        int first = this.listView.getFirstVisiblePosition();
        int want = first-1;
        intent.putExtra("ListViewid",want);
        intent.putExtra("StoragePath",s);
        startActivityForResult(intent,this.checkcode[2]);
    };

    private LinearLayout getAbsolPosition(int pos, AdapterView<?> adapterView){
        int firstPos = adapterView.getFirstVisiblePosition();
        int wantedPos = pos - firstPos;
        if (wantedPos < 0 || wantedPos >= adapterView.getChildCount()) {
            return null;
        }
        return (LinearLayout)adapterView.getChildAt(wantedPos);
    }
}