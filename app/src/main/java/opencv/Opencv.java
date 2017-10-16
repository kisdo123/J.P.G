package opencv;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.googlecode.leptonica.android.MorphApp;

import org.opencv.android.Utils;
import org.opencv.core.*;
import org.opencv.imgcodecs.*;
import org.opencv.imgproc.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2017-09-14.
 */

public class Opencv {

    private final String TAG = "OpencvCheck";
    private final String writepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/J.P.G/";
    private Context context;
    private Mat mat;
    private Bitmap bitmap;
    public Opencv(Context context){
        setContext(context);
    }

    public Opencv(Context context, Mat mat){
        setContext(context);
        this.mat = mat;
    }

    public ArrayList<Bitmap> processing0(ArrayList<Bitmap> bitmaps,int i){
        this.mat = new Mat();
        ArrayList<Bitmap> tbitmaps = new ArrayList<Bitmap>();
        Log.d(TAG+"1", "storage size :" +bitmaps.size());
        for(int j = 0;j<bitmaps.size();j++){
            this.bitmap = bitmaps.get(j);
            mat=bitmapTomat(this.bitmap,mat); // Bitmap을 Mat으로
            //Log.d(TAG+" 0",mat.toString());
            //writeImage("bitmapTomat",mat);
            mat=rgbTogray(mat); // Gray 변환
            //Log.d(TAG+" 1",mat.toString());
            //writeImage("grayImage",mat);
            mat=blurImage2(mat); // Blur 처리
            //Log.d(TAG+" 2",mat.toString());
            //writeImage("bluredImage",mat);
            mat=adaptiveThreshold(mat); // adaptiveThreshold 처리
            //Log.d(TAG+" 3",mat.toString());
            //writeImage("binaryImage",mat);
            this.bitmap=matTobitmap(mat);
            Log.d(TAG+"2", "Bitmap is null:" +(this.bitmap == null));
            tbitmaps.add(this.bitmap);
        }

        /*
        if(i>0){
            mat=erosionMorp(mat); // Erosion 처리
            Log.d(TAG+" 3_3",mat.toString());
            writeImage("erodeImage",mat);
        } else {
            mat=invertImage(mat);
            Log.d(TAG+" 3_4",mat.toString());
            writeImage("invertImage",mat);
            mat=dilationMorp(mat);
            Log.d(TAG+" 3_5",mat.toString());
            writeImage("dilationImage",mat);
        }
        mat = drawcontours(mat);
        Log.d(TAG+" 3_5",mat.toString());
        writeImage("drawcontours",mat);
        bitmap=matTobitmap(mat); // Mat을 Bitmap으로
        Log.d(TAG+" 4",mat.toString());
        */
        /*
        mat=bitmapTomat(bitmap,mat); // Bitmap을 Mat으로
        Log.d(TAG+" 0",mat.toString());
        writeImage("bitmapTomat",mat);
        //
        mat = setcvttype(mat);
        //
        this.mat=grabcut(bitmap); //
        Log.d(TAG+" 6",this.mat.toString());
        writeImage("grabcutImage",mat);
        //
        mat=cannyImage(mat);
        Log.d(TAG+" 7",mat.toString());
        writeImage("cannyimage",mat);
        //
        //
        //
        //
        //
        mat=grabcut(mat); //
        Log.d(TAG+" 6",mat.toString());
        writeImage("grabcutImage",mat);
        */
        return tbitmaps;
    }
    private Context setContext(Context context){
        this.context = context;
        return this.context;
    }

    public Mat bitmapTomat(Bitmap bmp,Mat mat){ // bitmap을 mat으로
        Bitmap bmp32 = bmp.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32,mat);
        Imgproc.cvtColor(mat,mat,Imgproc.COLOR_BGRA2BGR);
        return mat;
    }

    public Bitmap matTobitmap(Mat mat){
        Bitmap bitmap = Bitmap.createBitmap(mat.cols(),mat.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat,bitmap);
        return bitmap;
    }

    public Mat fileTomat(String path,Mat mat){ // filepath를 mat으로
        mat = Imgcodecs.imread(path);
        return mat;
    }

    public void writeImage(String fileName,Mat mat){
        Imgcodecs.imwrite(writepath+fileName+".jpg",mat);
    }

    public Mat rgbTogray(Mat mat){ // rgb형식을 gray로
        Imgproc.cvtColor(mat,mat,Imgproc.COLOR_BGR2GRAY);
        return mat;
    }

    public Mat cannyImage(Mat mat){
        Imgproc.Canny(mat,mat,80,100);
        return mat;
    }

    public Mat adaptiveThreshold(Mat mat){ // 영상 이진화.
        Imgproc.adaptiveThreshold(mat,mat,255,Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY,255,0);
        return mat;
    }

    public Mat invertImage(Mat mat){ // 흑백 반전
        Core.bitwise_not(mat,mat);
        return mat;
    }

    public Mat blurImage1(Mat mat){ // 블러 처리
        Size s = new Size(30,30);
        Imgproc.blur(mat,mat,s);
        //Imgproc.medianBlur();
        return mat;
    }

    public Mat blurImage2(Mat mat){
        Imgproc.GaussianBlur(mat, mat, new Size(3,3), 2);
        return mat;
    }

    public Mat grabcut(Bitmap bitmap){
        Mat src = new Mat(bitmap.getWidth(), bitmap.getHeight(), CvType.CV_8UC4);
        Utils.bitmapToMat(bitmap, src);

        // init new matrices
        Mat dst = new Mat(bitmap.getWidth(), bitmap.getHeight(), CvType.CV_8UC4);
        Mat tmp = new Mat(bitmap.getWidth(), bitmap.getHeight(), CvType.CV_8UC4);
        Mat alpha = new Mat(bitmap.getWidth(), bitmap.getHeight(), CvType.CV_8UC4);

        // convert image to grayscale
        Imgproc.cvtColor(src, tmp, Imgproc.COLOR_BGR2GRAY);

        // threshold the image to create alpha channel with complete transparency in black background region and zero transparency in foreground object region.
        Imgproc.threshold(tmp, alpha, 0, 125, Imgproc.THRESH_BINARY);

        // split the original image into three single channel.
        List<Mat> rgb = new ArrayList<Mat>(3);
        Core.split(src, rgb);

        // Create the final result by merging three single channel and alpha(BGRA order)
        List<Mat> rgba = new ArrayList<Mat>(4);
        rgba.add(rgb.get(0));
        rgba.add(rgb.get(1));
        rgba.add(rgb.get(2));
        rgba.add(alpha);
        Core.merge(rgba, dst);

        Imgproc.cvtColor(dst, dst, Imgproc.COLOR_RGBA2mRGBA);
        return dst;
    }

    public Mat setcvttype(Mat mat){
        Imgproc.cvtColor(mat,mat,Imgproc.COLOR_BGR2GRAY);
        return mat;
    }
    public Mat resizeMat(Mat mat,int num){
        Imgproc.resize(mat,mat,new Size(num*mat.cols(),num*mat.rows()),0,0,Imgproc.INTER_LINEAR);
        return mat;
    }

    public Mat erosionMorp(Mat mat){
        Imgproc.erode(mat,mat, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(50, 50)));
        return mat;
    }

    public Mat dilationMorp(Mat mat){
        Imgproc.dilate(mat,mat,Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(25,25)));
        return mat;
    }

    public Mat drawcontours(Mat mat){
        Mat hierarchy = new Mat();
        Scalar CONTOUR_COLOR = new Scalar(255,0,255);
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(mat,contours,hierarchy,Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(mat,contours,3,CONTOUR_COLOR);
        return mat;
    }
}
