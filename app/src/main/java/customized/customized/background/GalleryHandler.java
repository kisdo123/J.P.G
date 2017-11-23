package customized.customized.background;

import android.os.Handler;
import android.os.Message;

import customized.customized.CustomProgressbarDialog;

/**
 * Created by User on 2017-11-22.
 */

public class GalleryHandler extends Handler {

    private CustomProgressbarDialog hCustomProgressbarDialog;

    public GalleryHandler(CustomProgressbarDialog hCustomProgressbarDialog) {
        this.hCustomProgressbarDialog = hCustomProgressbarDialog;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case 1000:
                hCustomProgressbarDialog.getProgressBar2().incrementProgressBy(1);
                break;
            case 9999:
                hCustomProgressbarDialog.dismiss();
                break;
            default:
                break;
        }
    }
}
