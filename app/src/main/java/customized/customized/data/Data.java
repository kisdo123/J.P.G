package customized.customized.data;

import java.io.Serializable;

/**
 * Created by User on 2017-10-30.
 */

public class Data implements Serializable{
    /* J.P.G 프로젝트에서 사용할 데이터 기술
    *  산재되어 있는 주요한 데이터 형만 모음
    *  클래스 배열임 ( C와 같이 구조체가 되지 않으므로)
    *  절대경로, 원본 이미지 비트맵, 이미지 썸네일, 잘라낸 이미지, Ocr 결과물
    * */
    public String mAbsolutePath;
    public byte[] mPictureImage;
    public byte[] mImageThumnail;
    public byte[] mCropedImage;
    public String mOcrResultText;
    public int mConfidence;

    public Data(){
        this.mAbsolutePath = null;
        this.mPictureImage = null;
        this.mImageThumnail = null;
        this.mCropedImage = null;
        this.mOcrResultText = null;
        this.mConfidence =0;
    }

    public Data(String s, byte[] b, byte[] bb, byte[] bbb){
        this.mAbsolutePath = s;
        this.mPictureImage = b;
        this.mImageThumnail = bb;
        this.mCropedImage = bbb;
        this.mOcrResultText = null;
        this.mConfidence =0;
    }
}
