package com.ar_decoration.component;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ar_decoration.activities.R;


public class DrawerSubListAdapter extends BaseAdapter {

    private LayoutInflater m_LayoutInflater;
    private Category m_Category;
    private int m_CategoryPosition;
    private Context m_context;
    private ViewHolder m_ViewHolder;

    public DrawerSubListAdapter(Context vContext, Category vCategory, int vPosition) {
        m_context = vContext;
        m_Category = vCategory;
        m_LayoutInflater = (LayoutInflater) vContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        m_CategoryPosition = vPosition;
    }

    @Override
    public int getCount() {
        return m_Category.getNumSubCategories();
    }

    @Override
    public SubCategory getItem(int vPosition) {
        return m_Category.getSubCategoryAt(vPosition);
    }

    @Override
    public long getItemId(int vPosition) {
        return vPosition;
    }

    @Override
    public View getView(int vPosition, View vConvertView, ViewGroup vParent) {
        if (vConvertView == null) {
            vConvertView = m_LayoutInflater.inflate(R.layout.drawer_sublist_item, null);
            m_ViewHolder = new ViewHolder();
            m_ViewHolder.textView = (TextView) vConvertView.findViewById(R.id.SubTextView);
            m_ViewHolder.imageView = (ImageView) vConvertView.findViewById(R.id.SubImageView);
            vConvertView.setTag(m_ViewHolder);
        } else {
            m_ViewHolder = (ViewHolder) vConvertView.getTag();
        }

        m_ViewHolder.textView.setText(m_Category.getSubCategoryAt(vPosition).getName());
        m_ViewHolder.textView.setTextColor(Color.BLACK);
        m_ViewHolder.imageView.setImageDrawable(m_Category.getSubCategoryAt(vPosition).getDrawable());
        return vConvertView;
    }

    public static class ViewHolder {
        public TextView textView;
        public ImageView imageView;
    }
}
