package com.av.millim.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.av.millim.Data.AttachDocument;
import com.av.millim.Data.StoreData;
import com.av.millim.Model.Transactions;
import com.av.millim.R;

import java.util.ArrayList;

import static com.av.millim.Activites.SignUpActivity.attachDocumentsList;
import static com.av.millim.Activites.SignUpActivity.imageOrDocumentAttachList;
import static com.av.millim.Activites.SignUpActivity.listUrl;

/**
 * Created by Maiada on 11/21/2017.
 */

public class AttachDocumentAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Activity activity;
    MyViewHolder holder = null;
    ArrayList<String> getAttachDocument;

    public AttachDocumentAdapter(Activity a, ArrayList<String> attachDocumentArrayList) {
        getAttachDocument=attachDocumentArrayList;
        activity=a;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return getAttachDocument.size();
    }

    @Override
    public String getItem(int position) {
        return getAttachDocument.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        if (convertView == null)

        {
            convertView = inflater.inflate(R.layout.row_list_image, null);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);
            Log.d("row", "Creating row");

        } else {
            holder = (MyViewHolder) convertView.getTag();
            Log.d("row", "Recycling use");
        }

        holder.nameOfFile.setText(".../"+getAttachDocument.get(position).substring(29,getAttachDocument.get(position).length()));
        holder.deleteAttach.setTag(position);
        final View finalConvertView = convertView;





         holder.deleteAttach.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 attachDocumentsList.remove(position);
                 listUrl.remove(position);
                 notifyDataSetChanged();
             }
         });




        return convertView;

    }




    static  class MyViewHolder{
        ImageView deleteAttach;
        TextView  nameOfFile;
        public  MyViewHolder(View v){

            nameOfFile     =  (TextView) v.findViewById(R.id.txt_name_file);
            deleteAttach   =  (ImageView) v.findViewById(R.id.delete_file_or_image);


            v.setTag(this);
        }
    }
}
