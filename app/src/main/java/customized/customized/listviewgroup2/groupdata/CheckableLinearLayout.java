package customized.customized.listviewgroup2.groupdata;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;

import capstone.android.R;
import gun0912.tedbottompicker.TedBottomPicker;

/**
 * Created by User on 2017-11-13.
 */

public class CheckableLinearLayout extends LinearLayout implements Checkable {

    public CheckableLinearLayout(Context context) {
        super(context);
    }
    public CheckableLinearLayout(Context context, AttributeSet attr){super(context,attr);}
    public CheckableLinearLayout(Context context, AttributeSet attr, int defStyle){super(context,attr,defStyle);}

    @Override
    public void setChecked(boolean b) {
        CheckBox c = (CheckBox)findViewById(R.id.cus_gal_chk);
        if(c.isChecked()!= b){
            c.setChecked(b);
        }
    }

    @Override
    public boolean isChecked() {
        CheckBox c = (CheckBox)findViewById(R.id.cus_gal_chk);
        return c.isChecked();
    }

    @Override
    public void toggle() {
        CheckBox cb = (CheckBox) findViewById(R.id.cus_gal_chk) ;
        setChecked(!cb.isChecked());
        // setChecked(mIsChecked ? false : true) ;
    }
}
