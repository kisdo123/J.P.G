package customized.customized.listviewgroup2.groupdata;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by User on 2017-11-13.
 */

public class ListViewItem {
    private Bitmap bitmap;
    private String string;

    public void setBitmap(Bitmap b){
        this.bitmap = b;
    }

    public void setString(String s){
        this.string = s;
    }

    public Bitmap getBitmap(){
        return this.bitmap;
    }

    public String getString(){
        return this.string;
    }
}
