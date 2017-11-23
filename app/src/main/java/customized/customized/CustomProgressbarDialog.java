package customized.customized;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import capstone.android.R;

/**
 * Created by User on 2017-11-17.
 */

public class CustomProgressbarDialog extends AlertDialog{

    LayoutInflater layoutInflater;
    View dialogView;
    TextView title;
    TextView content;
    ProgressBar progressBar1;
    ProgressBar progressBar2;
    AlertDialog.Builder builder;
    AlertDialog dialog;

    public CustomProgressbarDialog(Context context) {
        super(context);
        layoutInflater = getLayoutInflater();
        dialogView = layoutInflater.inflate(R.layout.item_progressbar,null);
        title = dialogView.findViewById(R.id.dialog_title);
        content = dialogView.findViewById(R.id.dialog_contents);
        progressBar1 = dialogView.findViewById(R.id.dialog_progress1);
        progressBar2 = dialogView.findViewById(R.id.dialog_progress2);
        builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
    }
    public CustomProgressbarDialog(Context context,String sTitle, String sContent, int max) {
        super(context);
        layoutInflater = getLayoutInflater();
        dialogView = layoutInflater.inflate(R.layout.item_progressbar,null);
        title = dialogView.findViewById(R.id.dialog_title);
        content = dialogView.findViewById(R.id.dialog_contents);
        progressBar1 = dialogView.findViewById(R.id.dialog_progress1);
        progressBar2 = dialogView.findViewById(R.id.dialog_progress2);
        title.setText(sTitle);
        content.setText(sContent);
        progressBar2.setMax(max);
        builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
    }


    public void setTitle(String s){
        title.setText(s);
    }

    public void setContent(String s){
        content.setText(s);
    }

    public void setProgressBar2(int i){
        progressBar2.setProgress(i);
    }

    public ProgressBar getProgressBar2(){
        return getProgressBar2();
    }

    public void setMax(int max){
        progressBar2.setMax(max);
    }

    public void show(){
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }

}
