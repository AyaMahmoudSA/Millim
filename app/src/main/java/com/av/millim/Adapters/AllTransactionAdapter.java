package com.av.millim.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.av.millim.Data.StoreData;
import com.av.millim.Model.AllTransactions;
import com.av.millim.R;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Maiada on 11/13/2017.
 */

public class AllTransactionAdapter  extends BaseAdapter{

    private static LayoutInflater inflater = null;
    private Fragment activity;
    MyViewHolder holder = null;
    ArrayList<AllTransactions> getAllTransactionses;

    public AllTransactionAdapter(Fragment a, ArrayList<AllTransactions> transactionsArrayList) {
        getAllTransactionses=transactionsArrayList;
        activity=a;
        inflater = (LayoutInflater) a.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return getAllTransactionses.size();
    }

    @Override
    public AllTransactions getItem(int position) {
        return getAllTransactionses.get(position);
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
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null)

        {
            convertView = inflater.inflate(R.layout.row_list_active, null);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);
            Log.d("row", "Creating row");

        } else {
            holder = (MyViewHolder) convertView.getTag();
            Log.d("row", "Recycling use");
        }


        AllTransactions tu= getAllTransactionses.get(position);
        String getDate=tu.getDate().substring(0,10);

        String getTypeOfAccount = new StoreData(activity.getContext()).loadTypeOfContactTest();
        if(getTypeOfAccount.equals("1")){

            if(!tu.getTo_MerchantName().equals("null")){
                holder.userName.setText(tu.getTo_MerchantName());
            }
            if(!tu.getTo_UserPhone().equals("null")){
                holder.userName.setText("0"+tu.getTo_UserPhone());

            }
            if(!tu.getTo_UserName().equals("null")){
                holder.userName.setText(tu.getTo_UserName());

            }

        }else{


            if(!tu.getTo_UserName().equals("null")){
                holder.userName.setText(tu.getTo_UserName());

            }
            if(!tu.getTo_UserPhone().equals("null")){
                holder.userName.setText("0"+tu.getTo_UserPhone());
            }
            if(!tu.getTo_MerchantName().equals("null")){
                holder.userName.setText(tu.getTo_MerchantName());

            }
        }


        holder.date.setText(getDate.replace("-","/"));
        if(tu.getConfirmation().trim().equals("false")){
            holder.status.setText("InProgress");
        }else{
            holder.status.setText("Completed");
        }

        String getCreditAsDecimal =BigDecimal(tu.getCredit());
        holder.credit.setText(getCreditAsDecimal+"  "+"MilliM");




        return convertView;

    }




    static  class MyViewHolder{
        TextView userName,date,status,credit;
        public  MyViewHolder(View v){
            userName    = (TextView)  v.findViewById(R.id.txt_user_name);
            date =  (TextView) v.findViewById(R.id.txt_date);
            status =  (TextView) v.findViewById(R.id.txt_status);
            credit =  (TextView) v.findViewById(R.id.txt_millim_value);


            v.setTag(this);
        }
    }
    public  String BigDecimal(String NumberFormatString){
        BigDecimal bigDecimal_value = new BigDecimal(NumberFormatString);
        BigDecimal string_value = bigDecimal_value.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        String result = string_value.toString();

        return  result ;
    }
}
