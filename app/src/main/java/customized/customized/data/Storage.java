package customized.customized.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by User on 2017-08-27.
 */

public class Storage implements Serializable {
    private String TAG = "Storage";
    private ArrayList<Data> dlist;

    public Storage(){
        this.dlist = new ArrayList<Data>(20);
    }

    public void addImageData(String absolutePath){
        Bitmap b,bb,bbb;
        if(new File(absolutePath).length() > 1024*1024){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            Bitmap temp = BitmapFactory.decodeFile(absolutePath,options);
            b = Bitmap.createScaledBitmap(temp,temp.getWidth(),temp.getHeight(),true);
            bb = ThumbnailUtils.extractThumbnail(b,100,100);
        } else {
            b = BitmapFactory.decodeFile(absolutePath);
            bb = ThumbnailUtils.extractThumbnail(b,100,100);
        }
        bbb = Bitmap.createBitmap(150,150,Bitmap.Config.ARGB_8888);
        byte[] bytes1 = returnByteArray(b);
        byte[] bytes2 = returnByteArray(bb);
        byte[] bytes3 = returnByteArray(bbb);
        Data data = new Data(absolutePath,bytes1,bytes2,bytes3);
        this.dlist.add(data);
        b.recycle();
        bb.recycle();
        bbb.recycle();
    }

    public void setImageData(int i, String absolustePath,Bitmap bitmap,Bitmap thumbnail){
        this.dlist.get(i).mAbsolutePath = absolustePath;
        this.dlist.get(i).mPictureImage = bitmapToByteArray(bitmap);
        this.dlist.get(i).mCropedImage = bitmapToByteArray(thumbnail);
    }

    public void setPictureImage(int i, Bitmap bitmap){ this.dlist.get(i).mPictureImage = bitmapToByteArray(bitmap);}

    public Bitmap getPictureImage(int i){ return byteArrayToBitmap(this.dlist.get(i).mPictureImage); }

    public Bitmap getThumnail(int i){ return byteArrayToBitmap(this.dlist.get(i).mImageThumnail); }

    public void setCropedImage(int i, Bitmap b){ this.dlist.get(i).mCropedImage =  bitmapToByteArray(b); }

    public Bitmap getCropedImage(int i){ return byteArrayToBitmap(this.dlist.get(i).mCropedImage); }

    public String getAbsolutePath(int i){
        return  this.dlist.get(i).mAbsolutePath;
    }

    public void setOcrResultText(int i, String text){
        this.dlist.get(i).mOcrResultText = text;
    }

    public String getOcrResultText(int i){
        return this.dlist.get(i).mOcrResultText;
    }

    public void setConfidence(int i,int confidence){
        this.dlist.get(i).mConfidence = confidence;
    }

    public int getConfidence(int i){
        return this.dlist.get(i).mConfidence;
    }
    public int getSize(){
        return this.dlist.size();
    }

    private void addData(){
        Data data = new Data();
        this.dlist.add(data);
    }

    private byte[] bitmapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return bytes;
    }

    private Bitmap byteArrayToBitmap(byte[] b){
        Bitmap bitmap = BitmapFactory.decodeByteArray(b,0,b.length);
        return bitmap;
    }

    private byte[] returnByteArray(Bitmap b){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return bytes;
    }
}
