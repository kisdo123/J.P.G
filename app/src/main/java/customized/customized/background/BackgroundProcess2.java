package customized.customized.background;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;

import customized.customized.data.Storage;

/**
 * Created by User on 2017-11-05.
 */

public class BackgroundProcess2 extends AsyncTask<Object,Integer,Integer> {
    private final int progress = 999;
    private final int max = 888;
    private Context mContext;
    private ProgressDialog mProgressDialog;
    private Storage mStorage;
    private ArrayList<String> mArrayList = new ArrayList<>();

    //Background 초기화
    public BackgroundProcess2(Context context){
        this.mContext = context;
    }

    @Override //Background 시작전 준비할 사항들
    protected void onPreExecute() {
        this.mProgressDialog = new ProgressDialog(this.mContext);
        this.mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        this.mProgressDialog.setTitle("Bitmap 로딩");
        this.mProgressDialog.setMessage("비트맵 생성중");
        this.mProgressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Object... objects) {
        this.mStorage = (Storage)objects[0];
        this.mArrayList = (ArrayList<String>)objects[1];
        publishProgress(888,this.mArrayList.size());
        for(int i=0;i<this.mArrayList.size();i++){
            this.mStorage.addImageData(mArrayList.get(i).toString());
            publishProgress(999,i); // 진행도 증가
        }
        return 0;
    }

    @Override // AsyncTask 진행중에 publishProgress로 받을 코드
    protected void onProgressUpdate(Integer... integers){
        if (integers[0].equals(progress)) {
            this.mProgressDialog.setProgress(integers[1]);
        }
        else if (integers[0].equals(max)) {
            this.mProgressDialog.setMax(integers[1]);
        }
    }

    @Override // doInBackground 작업이 끝났을 때의 코드
    protected void onPostExecute(Integer result) {
        this.mProgressDialog.dismiss();
        Toast.makeText(this.mContext, Integer.toString(result)+ "개의 작업 완료", Toast.LENGTH_SHORT).show();
    }
}
