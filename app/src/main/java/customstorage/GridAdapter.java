package customstorage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import capstone.android.R;

/**
 * Created by User on 2017-09-26.
 */

public class GridAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mcontext;
    private ArrayList<String> arrayList;
    private ArrayList<Bitmap> thumnails;
    private int flag;
    private final String TAG = "ImgGridAdapter";

    public GridAdapter(Context context, ArrayList<String> strings,int flag){
        this.mcontext = context;
        this.arrayList = strings;
        this.flag = flag;
        this.thumnails = new ArrayList<Bitmap>();
        Log.d(TAG,"mcontext :"+context.toString());
        for(int i=0;i<this.arrayList.size();i++){
            Log.d(TAG,"arrayList "+ i+" :" + arrayList.get(i).toString());
        }
        if(flag > 0){
            setThumnails(this.arrayList);
        } else if (flag < 0){
            //Non operation
        }

    }

    private void setThumnails(ArrayList<String> strings){
        String temp;
        for(int i=0; i<strings.size(); i++){
            temp = strings.get(i).toString();
            Log.d(TAG,"temp "+i+" :"+temp.toString());
            Bitmap bitmap = BitmapFactory.decodeFile(temp);
            Log.d(TAG,"Bitmap is null:" + (bitmap==null));
            int nh = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
            bitmap = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);
            Log.d(TAG,"Bitmap is null:" + (bitmap==null));
            this.thumnails.add(bitmap);
        }
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        this.mInflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(this.flag > 0)
        {
            ImageView imageView;
            if(convertView == null){
                convertView = this.mInflater.inflate(R.layout.imageitem,null);
                imageView = (ImageView)convertView.findViewById(R.id.grid_image);
                convertView.setTag(imageView);
            } else {
                imageView = (ImageView)convertView.getTag();
            }
            imageView.setImageBitmap(thumnails.get(position));

        } else if (this.flag <0){
            TextView textView;
            if(convertView == null){
                convertView = this.mInflater.inflate(R.layout.textitem,null);
                textView= (TextView)convertView.findViewById(R.id.textView2);
                convertView.setTag(textView);
            } else {
                textView = (TextView)convertView.getTag();
            }
            textView.setText(this.arrayList.get(position));
        } else{

        }
        return convertView;
    }
}
