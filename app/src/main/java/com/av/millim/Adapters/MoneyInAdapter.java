package com.av.millim.Adapters;

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

public class MoneyInAdapter  extends BaseAdapter{

    private static LayoutInflater inflater = null;
    private Fragment activity;
    MyViewHolder holder = null;
    ArrayList<AllTransactions> getTransactionsMoneyIn;

    public MoneyInAdapter(Fragment a, ArrayList<AllTransactions> transactionsArrayList) {
        getTransactionsMoneyIn=transactionsArrayList;
        activity=a;
        inflater = (LayoutInflater) a.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return getTransactionsMoneyIn.size();
    }

    @Override
    public AllTransactions getItem(int position) {
        return getTransactionsMoneyIn.get(position);
    }

    @Override
    public long getItemId(int position) {
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
            convertView = inflater.inflate(R.layout.row_list_money_in, null);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);
            Log.d("row", "Creating row");

        } else {
            holder = (MyViewHolder) convertView.getTag();
            Log.d("row", "Recycling use");
        }


        AllTransactions tu= getTransactionsMoneyIn.get(position);
        String getTypeOfAccount = new StoreData(activity.getContext()).loadTypeOfContactTest();
        if(getTypeOfAccount.equals("1")){
            if(!tu.getFrom_MerchantName().equals("null")){
                holder.userName.setText(tu.getFrom_MerchantName());
            }
            if(!tu.getFrom_UserName().equals("null")){
                holder.userName.setText(tu.getFrom_UserName());
            }
        }else{
            if(!tu.getFrom_UserName().equals("null")){
                holder.userName.setText(tu.getFrom_UserName());

            }
            if(!tu.getFrom_MerchantName().equals("null")){
                holder.userName.setText(tu.getFrom_MerchantName());
            }
        }
        String getDate=tu.getDate().substring(0,10);

        //holder.userName.setText(tu.getFrom_UserName());
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
