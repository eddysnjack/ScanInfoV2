package com.androeddy.scaninfov2.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.androeddy.scaninfov2.R;
import com.androeddy.scaninfov2.statics.DBTableSearchHistory;

import java.util.ArrayList;

public class AdapterListviewSearchHistory extends ArrayAdapter {
    private Context context;
    private int resource;
    private ArrayList<DBTableSearchHistory> dbTableSearchHistories;

    public AdapterListviewSearchHistory(@NonNull Context context, @NonNull ArrayList<DBTableSearchHistory> dbTableSearchHistories) {
        super(context, R.layout.listview_settings_history, dbTableSearchHistories);
        this.context = context;
        this.resource = R.layout.listview_settings_history;
        this.dbTableSearchHistories = dbTableSearchHistories;
    }


    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable Object item) {
        return super.getPosition(item);
    }


    private class ViewHolder {
        TextView name;
        TextView barcode;
        TextView date;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        View view = convertView;
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(resource, null);

            holder.barcode = view.findViewById(R.id.listview_settings_history_medicineBarcode);
            holder.date = view.findViewById(R.id.listview_settings_history_medicineDate);
            holder.name = view.findViewById(R.id.listview_settings_history_medicineName);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (dbTableSearchHistories.get(position) != null) {
            DBTableSearchHistory dbTableSearchHistory = dbTableSearchHistories.get(position);
            holder.name.setText(dbTableSearchHistory.getMedicineName());
            holder.barcode.setText(dbTableSearchHistory.getMedicineBarcode());
            holder.date.setText(dbTableSearchHistory.getDate());
        }
        return view;
    }


    //---------------- my methosd
//    public void filter(String searchText) {
//        searchText = searchText.toLowerCase();
//        dbTableSearchHistories.clear();
//        if (searchText.length() == 0) {
//            dbTableSearchHistories.addAll(originList);
//        } else {
//            for (BillInfo bill : originList) {
//                if (bill.toString().toLowerCase().contains(searchText)) {
//                    dbTableSearchHistories.add(bill);
//                }
//            }
//        }
//        notifyDataSetChanged();
//    }

//    public void sortByNumber() {
//        Collections.sort(dbTableSearchHistories, new Comparator<BillInfo>() {
//            @Override
//            public int compare(BillInfo d1, BillInfo d2) {
//                //tolowerCase Önemli!! yoksa yanlış gidiyor aga :))
//                return d1.getBillNumber().toLowerCase().compareTo(d2.getBillNumber().toLowerCase());
//            }
//        });
//        notifyDataSetChanged();
//    }
//
//    public void sortByDate() {
//        Collections.sort(dbTableSearchHistories, new Comparator<BillInfo>() {
//            @Override
//            public int compare(BillInfo d1, BillInfo d2) {
//                //tolowerCase Önemli!! yoksa yanlış gidiyor aga :))
//                return d1.getBillDate().toLowerCase().compareTo(d2.getBillDate().toLowerCase());
//            }
//        });
//        notifyDataSetChanged();
//    }
}
