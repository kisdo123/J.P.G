package capstone.android;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.poi.xslf.usermodel.*;

public class PptBuilder
{
    private XMLSlideShow ppt;

    public void MakeSlideLayout(String text,String filePath)
    {
        try {
            File file = new File(filePath + "result.pptx");
            FileOutputStream out = new FileOutputStream(file);
            Log.d("CheckPoint5", "filePath :" + filePath);
            Log.d("CheckPoint6", "text :" + text);
            ppt = new org.apache.poi.xslf.usermodel.XMLSlideShow();
            XSLFSlideMaster slideMaster = ppt.getSlideMasters().get(0);
            XSLFSlideLayout slidelayout = slideMaster.getLayout(SlideLayout.TITLE_AND_CONTENT);
            XSLFSlide slide = ppt.createSlide(slidelayout);
            XSLFTextShape title = slide.getPlaceholder(0);
            title.setText("Result");
            XSLFTextShape body = slide.getPlaceholder(1);
            body.clearText();
            body.addNewTextParagraph().addNewTextRun().setText("text :"+text);
            ppt.write(out);
            out.close();
        } catch ( Exception e){
            e.printStackTrace();
            Log.d("ERROR","ERROR :"+ e.toString());
        }
    }
}
