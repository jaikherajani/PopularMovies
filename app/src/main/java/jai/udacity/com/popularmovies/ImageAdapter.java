package jai.udacity.com.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.Inflater;

/**
 * Created by jaikh on 20-02-2016.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private String[] image;

    // Constructor
    public ImageAdapter(Context c, String image[]) {
        mContext = c;
        this.image=image;
    }

    @Override
    public int getCount() {
        return image.length;
    }

    @Override
    public Object getItem(int position) {
        return image[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView poster=null;
        TextView moviename = new TextView(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int height =mContext.getResources().getDisplayMetrics().heightPixels;
        int width =mContext.getResources().getDisplayMetrics().widthPixels;
        height=height/2;
        width=width/2;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_main, null);
            convertView.setTag(R.id.grid_image, convertView.findViewById(R.id.grid_image));
        }
            convertView.setLayoutParams(new GridView.LayoutParams(width,height));
            poster = (ImageView) convertView.getTag(R.id.grid_image);
            Picasso.with(mContext)
                    .load(image[position])
                    .into(poster);
        return convertView;
    }
}

