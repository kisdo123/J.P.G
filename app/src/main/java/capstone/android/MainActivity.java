package capstone.android;
import customstorage.GridAdapter;
import customstorage.Storage;
import gun0912.tedbottompicker.TedBottomPicker;

import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private BackPressCloseHandler backPressCloseHandler;
    private static final int[] checkcode = {111,222};
    private Storage storage;
    private ArrayList<Button> buttonList = new ArrayList<Button>();
    private GridView gridView;
    private GridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 모든 Activity에 종속된 함수들은 Activity가 먼저 실행되어야 할당될 수 있으므로 제일 먼저 실행되게 조작한다.
        setting(); // Activity에 들어갈 모든 객체 및 View 및 Button의 초기화
        gallery();
        Cam();
        Change();
    }

    private void setting() {
        this.storage = new Storage();
        this.buttonList.add((Button)findViewById(R.id.btn_gallery));
        this.buttonList.add((Button)findViewById(R.id.btn_cam));
        this.buttonList.add((Button)findViewById(R.id.btn_change));
        this.gridView = (GridView) findViewById(R.id.gridView1);
        this.adapter = null;
        this.backPressCloseHandler = new BackPressCloseHandler(this);
        Log.d("MainActivity0",
                "storage :"+this.storage.toString()+"\n"+
                "buttonlist(1) :"+this.buttonList.get(0).toString()+"\n"+
                "buttonlist(2) :"+this.buttonList.get(1).toString()+"\n"+
                "buttonlist(3) :"+this.buttonList.get(2).toString()+"\n"+
                "gridView :"+this.gridView.toString()+"\n"+
                "backPressCloseHandler :"+this.backPressCloseHandler.toString()+"\n"

        );
    }
    private void gallery() {
        buttonList.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),  checkcode[1]);*/
                TedBottomPicker bottomSheetDialogFragment  = new TedBottomPicker.Builder(MainActivity.this)
                        .setOnMultiImageSelectedListener(new TedBottomPicker.OnMultiImageSelectedListener(){
                            @Override
                            public void onImagesSelected(ArrayList<Uri> uriList){
                                ArrayList<String> atemp = new ArrayList<String>();
                                for(int i=0;i<uriList.size();i++){
                                    Log.d("TedBottomPicker","temp0 :" +uriList.get(i).getPath().toString());
                                    MainActivity.this.storage.setApath(i,uriList.get(i).getPath().toString());
                                }
                                MainActivity.this.adapter = new GridAdapter(MainActivity.this, MainActivity.this.storage.getApathswithAlist(),10);
                                MainActivity.this.gridView.setAdapter(MainActivity.this.adapter);
                                /*
                                stemp = uriList.get(0).getPath().toString();
                                storage.setabsolutePath(0,stemp);
                                Bitmap bitmap = BitmapFactory.decodeFile(storage.getabsolutePath(0));
                                int nh = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
                                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);
                                imgView.setImageBitmap(scaled);
                                Toast.makeText(MainActivity.this,stemp+"이미지 선택 완료", Toast.LENGTH_SHORT).show();
                                */
                            }
                        })
                        .setSelectMaxCount(20)
                        .showTitle(false)
                        .setCompleteButtonText("선택")
                        .setEmptySelectionText("No Select")
                        .create();
                bottomSheetDialogFragment.show(getSupportFragmentManager());
            }
        });
    }
    private void Cam(){
        buttonList.get(1).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent,checkcode[0]);
            }
        });
    }

    private void Change(){
        buttonList.get(2).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, OcrActivity.class);
                intent.putExtra("customstorage", storage);
                startActivity(intent);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == checkcode[0]) {
                switch(resultCode){
                    case RESULT_OK:
                        String temp = data.getStringExtra("camfilepath");
                        /*
                        storage.setabsolutePath(0,temp);
                        Bitmap bitmap = BitmapFactory.decodeFile(temp);
                        int nh = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
                        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);
                        this.imgView.setImageBitmap(scaled);
                        */
                        ArrayList<String> atemp = new ArrayList<String>();
                        atemp.add(temp);
                        this.adapter = new GridAdapter(MainActivity.this,atemp,10);
                        break;
                    default:
                        Toast.makeText(this, "취소되었습니다.", Toast.LENGTH_SHORT).show();
                        break;
                }

            } /*else if(requestCode == checkcode[1]){
                switch(resultCode){
                    case RESULT_OK:
                        Uri uri = data.getData();
                        Log.d("MainActivity1",uri.toString());
                        storage.setabsolutePath(0,getImageApath(uri));
                        Log.d("MainActivity2","이미지 절대 경로 : "+ storage.getabsolutePath(0));
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            int nh = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
                            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);
                            this.imgView.setImageBitmap(scaled);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(this, "이미지 선택 완료", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(this, "취소되었습니다.", Toast.LENGTH_SHORT).show();
                        break;
                }

            }*/ else {
                Toast.makeText(this, "알 수 없는 오류.", Toast.LENGTH_SHORT).show();
            }
    }
    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    /*
    public String getImageApath(Uri Rpath){
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(Rpath, filePathColumn, null, null, null);
        cursor.moveToFirst();
        Log.d("filePathColumn","filePathColumn :"+ filePathColumn);
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

        String picturePath = cursor.getString(columnIndex);
        Log.d("CursorCheck",
                "getPosition :"+cursor.getPosition()+
                        " getColumnCount :"+cursor.getColumnCount()+
                        " getExtras :"+cursor.getExtras()+
                        " getNtoificationUri :"+cursor.getNotificationUri()+
                        " getColumnNames :"+cursor.getColumnName(0)
                        //+
               // " getColumnNames :"+cursor.getColumnName(2)+
               // " getColumnNames :"+cursor.getColumnName(3)+
               // " getColumnNames :"+cursor.getColumnName(4)+
               // " getColumnNames :"+cursor.getColumnName(5)

        );
        return picturePath;
    }
    */
}