package customstorage;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by User on 2017-08-27.
 */

public class Storage implements Serializable {
    //private ArrayList<Uri> relativePath = null;
    private ArrayList<String> absolutePath = null;

    public Storage(){
        //this.relativePath = new ArrayList<Uri>();
        this.absolutePath = new ArrayList<String>();
    }
    /*
    public void setRelativePath(int i,Uri s){
        this.relativePath.add(i,s);
    }

    public Uri getRelativePath(int i){
        return this.relativePath.get(i);
    }

    public Uri[] getRelativePaths(){
        Uri[] temp = null;
        Object[] objArray = this.relativePath.toArray();
        for(int i=0;i < objArray.length; i++){
            temp[i] = (Uri) objArray[i];
        }
        return temp;
    }
    */

    public int getSize(){
        return this.absolutePath.size();
    }
    public void setApath(int i,String s){
        this.absolutePath.add(i,s);
    }

    public String getApath(int i){
        return this.absolutePath.get(i);
    }

    public String[] getApathswithSarr(){
        String[] temp = null;
        Object[] objArray = this.absolutePath.toArray();
        for(int i=0;i < objArray.length; i++){
            temp[i] = (String) objArray[i];
        }
        return temp;
    }

    public ArrayList<String> getApathswithAlist(){
        return this.absolutePath;
    }

    public void clearData(){
        //this.relativePath.clear();
        this.absolutePath.clear();
    }
}
