package capstone.android;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.File;
import java.util.ArrayList;

import customized.customized.CustomProgressbarDialog;

import customized.customized.background.GalleryHandler;
import customized.customized.listviewgroup2.ListViewAdapter;
import customized.customized.listviewgroup2.groupdata.CheckableLinearLayout;

public class GalleryActivity extends AppCompatActivity implements AbsListView.OnScrollListener{
    private final String TAG = "GalleryActivity";
    ObjectRef objectRef; // class variable
    IntValue intValue; // class variable
    //BoolValue boolValue; // class variable
    ProgressBar progressBar;
    ArrayList<String> aFilePath; // class variable
    ArrayList<String> returnPath; // must be return;
    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        init();
        getAllImagesContent();
        loadToList();
        setOkbutton();
        setCancleButton();
        objectRef.listView.setOnItemClickListener(onItemClickListener);
    }
    /*
    @Override
    protected void onResume() {
        super.onResume();
        CustomProgressbarDialog customProgressbarDialog = new CustomProgressbarDialog(GalleryActivity.this);
        GalleryHandler handler = new GalleryHandler(customProgressbarDialog);
        DeviceCrawling deviceCrawling = new DeviceCrawling(handler,this.aFilePath,this.intValue);
        customProgressbarDialog.setTitle("이미지 검색");
        customProgressbarDialog.setContent("이미지 검색 중...");
        customProgressbarDialog.setMax(202);
        customProgressbarDialog.show();
        deviceCrawling.setDaemon(true);
        deviceCrawling.start();
        //loadToList();
    }
    */
    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        /*
        // 1. OnScrollListener.SCROLL_STATE_IDLE : 스크롤이 이동하지 않을때의 이벤트(즉 스크롤이 멈추었을때).
        // 2. lastItemVisibleFlag : 리스트뷰의 마지막 셀의 끝에 스크롤이 이동했을때.
        // 3. mLockListView == false : 데이터 리스트에 다음 데이터를 불러오는 작업이 끝났을때.
        // 1, 2, 3 모두가 true일때 다음 데이터를 불러온다.
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag && mLockListView == false) {
            // 화면이 바닦에 닿을때 처리
            // 로딩중을 알리는 프로그레스바를 보인다.
            this.progressBar.setVisibility(View.VISIBLE);

            // 다음 데이터를 불러온다.
            getItem();
        }
        */
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        /*
        // firstVisibleItem : 화면에 보이는 첫번째 리스트의 아이템 번호.
        // visibleItemCount : 화면에 보이는 리스트 아이템의 갯수
        // totalItemCount : 리스트 전체의 총 갯수
        // 리스트의 갯수가 0개 이상이고, 화면에 보이는 맨 하단까지의 아이템 갯수가 총 갯수보다 크거나 같을때.. 즉 리스트의 끝일때. true
        this.lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
        */
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private ListView.OnItemClickListener onItemClickListener = (adapterView, view, i, l) -> {
        CheckableLinearLayout child = getAbsolPosition(i,adapterView);
        if(child.isChecked()){
            this.returnPath.add(this.aFilePath.get(i));
        } else {
            this.returnPath.remove(this.aFilePath.get(i));
        }
        for(int j = 0; j<this.returnPath.size();j++){
            Log.d(TAG,"apath : "+this.returnPath.get(j));
        }
        Log.d(TAG,"size : "+this.returnPath.size());
    };

    private void init(){
        intValue = new IntValue();
        //boolValue = new BoolValue();
        objectRef = new ObjectRef();
        objectRef.listView = (ListView)findViewById(R.id.gal_lv);
        objectRef.buttons = new Button[]{(Button)findViewById(R.id.send_button),(Button)findViewById(R.id.cancel_button)};
        objectRef.listViewAdapter = new ListViewAdapter(this,this.intValue.currentValue,this.intValue.maxValue);
        objectRef.listView.setAdapter(this.objectRef.listViewAdapter);
        objectRef.listView.setOnScrollListener(this);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.GONE);
        intValue.currentValue = this.getIntent().getExtras().getInt("currentSize",0);
        aFilePath = new ArrayList<String>();
        this.returnPath = new ArrayList<String>(intValue.maxValue);
    }

    private void loadToList(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        options.inScaled = true;
        objectRef.listViewAdapter = new ListViewAdapter(this,0,this.aFilePath.size());
        objectRef.listView.setAdapter(objectRef.listViewAdapter);
        for(int i=0;i<this.aFilePath.size();i++){
            Bitmap b = BitmapFactory.decodeFile(this.aFilePath.get(i),options);
            String s = new File(this.aFilePath.get(i)).getName();
            objectRef.listViewAdapter.addItem(b,s+"\n number : "+i);
            //Log.d(TAG,"count :"+i);
        }
    }

    /*
    private void getItem(){
        // 리스트에 다음 데이터를 입력할 동안에 이 메소드가 또 호출되지 않도록 mLockListView 를 true로 설정한다.
        this.mLockListView = true;

        // 다음 20개의 데이터를 불러와서 리스트에 저장한다.

        for(int i = 0; i < 20; i++){
            String label = "Label " + ((page * OFFSET) + i);
            list.add(label);
        }

        // 1초 뒤 프로그레스바를 감추고 데이터를 갱신하고, 중복 로딩 체크하는 Lock을 했던 mLockListView변수를 풀어준다.
        new Handler().postDelayed(() -> {
            GalleryActivity.this.listViewAdapter.notifyDataSetChanged();
            GalleryActivity.this.progressBar.setVisibility(View.GONE);
            GalleryActivity.this.mLockListView = false;
        },1000);
    }
    */

    private void setOkbutton(){
        objectRef.buttons[0].setOnClickListener(view -> {
            intent = new Intent();
            intent.putExtra("gAbsoulteFilePaths",this.returnPath);
            setResult(RESULT_OK,intent);
            finish();
        });
    }

    private void setCancleButton(){
        objectRef.buttons[1].setOnClickListener(view -> {
            intent = new Intent();
            setResult(RESULT_CANCELED,intent);
            finish();
        });
    }
    /*
    private Cursor Crawling(){
        Cursor cursor = null;
        try{
            String[] columns = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Thumbnails.DATA ,MediaStore.Images.ImageColumns.ORIENTATION};
            String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";
            cursor = this.getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
            return cursor;
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
    }
    */
    private void getAllImagesContent(){
        Cursor cursor = null;
        try{
            String[] columns = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Thumbnails.DATA ,MediaStore.Images.ImageColumns.ORIENTATION};
            String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";
            cursor = this.getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String imageLocation = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    this.aFilePath.add(imageLocation);
                    intValue.loadnum++;
                    }
                }

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
    }

    private CheckableLinearLayout getAbsolPosition(int pos, AdapterView<?> adapterView){
        int firstPos = adapterView.getFirstVisiblePosition();
        int wantedPos = pos - firstPos;
        return (CheckableLinearLayout)adapterView.getChildAt(wantedPos);
    }


    private class IntValue{
        final int maxValue = 20;
        int currentValue=0;
        int loadnum = 0;
    }
    /*
    private class BoolValue{
        boolean lastItemVisibleFlag = false;
        boolean mLockListView = false;
    }
    */

    private class ObjectRef{
        ListView listView = null;
        ListViewAdapter listViewAdapter = null;
        Button[] buttons = null;
    }

    private class DeviceCrawling extends Thread{
        private Handler mHandler;
        private ArrayList<String> aFilePaths;
        private IntValue mintValue;
        private final String TAG = "DeviceCrawling";

        public DeviceCrawling(Handler handler, ArrayList<String> afilePaths,IntValue intValue) {
            this.mHandler = handler;
            this.aFilePaths = afilePaths;
            this.mintValue = intValue;
        }

        @Override
        public void run() {
            Cursor cursor = null;
            try{
                String[] columns = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Thumbnails.DATA ,MediaStore.Images.ImageColumns.ORIENTATION};
                String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";
                cursor = getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String imageLocation = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        this.aFilePaths.add(imageLocation);
                        this.mintValue.loadnum++;
                        mHandler.sendMessage(mHandler.obtainMessage(1000));
                        Log.d(TAG,"thread running");
                    }
                }

            } catch (Exception e){
                e.printStackTrace();
            } finally {
                if(cursor != null && !cursor.isClosed()){
                    cursor.close();
                }
            }
            mHandler.sendMessage(mHandler.obtainMessage(9999));
        }

        @Override
        public void interrupt() {
            super.interrupt();
            Log.d(TAG,"Crawling error");
            mHandler.sendMessage(mHandler.obtainMessage(-1));
        }
    }
}
