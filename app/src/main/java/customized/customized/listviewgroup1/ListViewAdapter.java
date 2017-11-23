package customized.customized.listviewgroup1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import capstone.android.R;
import customized.customized.data.Storage;

/**
 * Created by User on 2017-09-26.
 */

public class ListViewAdapter extends BaseAdapter  {
    private LayoutInflater mInflater;
    private Context mContext;
    private Storage mStorage;
    private int mFlag;
    private final String TAG = "ImgListViewAdapter";

    public ListViewAdapter(Context context,Storage storage, int flag){
        this.mContext = context;
        this.mStorage = storage;
        this.mFlag = flag;
        Log.d(TAG,"mcontext :"+context.toString());
    }

    @Override
    public int getCount() {
        return mStorage.getSize();
    }

    @Override
    public Object getItem(int position) {
        return mStorage.getThumnail(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(this.mFlag > 0)
        {
            ListViewData listViewData = new ListViewData();
            if(convertView == null){
                convertView = this.mInflater.inflate(R.layout.item_maindraw,null);
                listViewData.thumnail = (ImageView)convertView.findViewById(R.id.thumbnail_image);
                listViewData.cropedImage = (ImageView)convertView.findViewById(R.id.croped_image);
                convertView.setTag(listViewData);
            } else {
                listViewData = (ListViewData) convertView.getTag();
            }
            //Log.d(TAG,"mStorage getThumnail " + mStorage.getThumnail(position).toString());
            listViewData.thumnail.setImageBitmap(mStorage.getThumnail(position));
            listViewData.cropedImage.setImageBitmap(mStorage.getCropedImage(position));

        } else if (this.mFlag <0){
            TextView textView;
            if(convertView == null){
                convertView = this.mInflater.inflate(R.layout.item_ocrdraw,null);
                textView= (TextView)convertView.findViewById(R.id.textView2);
                convertView.setTag(textView);
            } else {
                textView = (TextView)convertView.getTag();
            }
            textView.setText(mStorage.getOcrResultText(position));
        } else{

        }
        return convertView;
    }

    public class ListViewData{
        public ImageView thumnail;
        public ImageView cropedImage;
    }
}
