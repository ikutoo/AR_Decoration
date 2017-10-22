package com.ar_decoration.component;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ar_decoration.activities.R;

import java.util.List;

public class DrawerListAdapter extends BaseAdapter {

    private Context m_Context;
    private LayoutInflater m_Inflater;
    private List<Category> m_Categories;
    private int m_SelectedPosition = -1;

    public DrawerListAdapter(Context vContext, List<Category> vCategories) {
        m_Context = vContext;
        m_Categories = vCategories;
        m_Inflater = LayoutInflater.from(m_Context);
    }

    @Override
    public int getCount() {
        return m_Categories.size();
    }

    @Override
    public Category getItem(int vPosition) {
        return m_Categories.get(vPosition);
    }

    @Override
    public long getItemId(int vPosition) {
        return vPosition;
    }

    @Override
    public View getView(int vPosition, View vConvertView, ViewGroup vParent) {
        ViewHolder holder;
        if (vConvertView == null) {
            vConvertView = m_Inflater.inflate(R.layout.drawer_list_item, null);
            holder = new ViewHolder();
            holder.textView = (TextView) vConvertView.findViewById(R.id.TextView);
            holder.layout = (LinearLayout) vConvertView.findViewById(R.id.LinearLayout);
            vConvertView.setTag(holder);
        } else {
            holder = (ViewHolder) vConvertView.getTag();
        }
        if (m_SelectedPosition == vPosition) {
            holder.textView.setTextColor(Color.WHITE);
            holder.layout.setBackgroundColor(Color.TRANSPARENT);
        } else {
            holder.textView.setTextColor(Color.BLUE);
            holder.layout.setBackgroundColor(Color.LTGRAY);
        }
        holder.textView.setText(m_Categories.get(vPosition).getName());
        holder.textView.setTextColor(Color.BLACK);
        return vConvertView;
    }

    public static class ViewHolder {
        public TextView textView;
        public LinearLayout layout;
    }

    public void setSelectedPosition(int vPosition) {
        m_SelectedPosition = vPosition;
    }
}