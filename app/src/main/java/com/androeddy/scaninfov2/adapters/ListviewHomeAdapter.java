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
import com.androeddy.scaninfov2.statics.CapsuleListviewHome;

import java.util.ArrayList;
import java.util.List;

public class ListviewHomeAdapter extends ArrayAdapter<CapsuleListviewHome> {
    Context _context;
    ArrayList<CapsuleListviewHome> _homeArrayList;

    public ListviewHomeAdapter(@NonNull Context context, @NonNull ArrayList<CapsuleListviewHome> homeArrayList) {
        super(context, R.layout.listview_home, homeArrayList);
        _context = context;
        _homeArrayList = homeArrayList;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public CapsuleListviewHome getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable CapsuleListviewHome item) {
        return super.getPosition(item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewKeeper holder;
        View view = convertView;
        if (view == null) {
            holder = new ViewKeeper();
            LayoutInflater layoutInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.listview_home, null);
            holder.name = view.findViewById(R.id.listview_home_name);
            view.setTag(holder);
        } else {
            holder = (ViewKeeper) view.getTag();
        }

        holder.name.setText(_homeArrayList.get(position).getName());

        return view;
    }

    class ViewKeeper {
        TextView name;
    }
}
