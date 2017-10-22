package com.ar_decoration.activities;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.ar_decoration.component.Category;
import com.ar_decoration.component.DrawerListAdapter;
import com.ar_decoration.component.DrawerSubListAdapter;
import com.ar_decoration.component.ImageViewDragShadowBuilder;
import com.ar_decoration.component.OperationGLSurfaceView;
import com.ar_decoration.component.SubCategory;
import com.ar_decoration.component.Types;
import com.ar_decoration.component.Types.ControlMode;
import com.ar_decoration.component.Types.DataType;

import java.util.ArrayList;
import java.util.List;

public class OperationActivity extends Activity {
    public static ControlMode controlMode = ControlMode.NONE;

    private OperationGLSurfaceView m_SurfaceView;
    private ListView m_ListView;
    private ListView m_SubListView;
    private DrawerListAdapter m_ListAdapter;
    private DrawerSubListAdapter m_SubListAdapter;
    private DrawerLayout m_DrawerLayout;
    private List<Category> m_Categories = new ArrayList<>();

    private static final float DRAWER_WIDTH_WEIGHT = 0.40f;

    public OperationActivity() {
    }

    public void onTranslateClick(View vView) {
        controlMode = ControlMode.MODE_TRANSLATE;
    }

    public void onRotateClick(View vView) {
        controlMode = ControlMode.MODE_ROTATE;
    }

    public void onScaleClick(View vView) {
        controlMode = ControlMode.MODE_SCALE;
    }

    @Override
    public void onConfigurationChanged(Configuration vNewConfig) {
        super.onConfigurationChanged(vNewConfig);
    }

    public void setSubList(int vPosition, final Category vCategory) {
        m_ListAdapter.setSelectedPosition(vPosition);
        m_ListAdapter.notifyDataSetInvalidated();
        m_SubListAdapter = new DrawerSubListAdapter(getApplicationContext(), vCategory, vPosition);
        m_SubListView.setAdapter(m_SubListAdapter);
        m_SubListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        Toast.makeText(getApplicationContext(), vCategory.getSubCategoryAt(position).getName(), Toast.LENGTH_SHORT).show();
                    }
                });
        m_SubListView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        ClipData.Item item = new ClipData.Item(vCategory.getSubCategoryAt(position).getModelPath());
                        ClipData dragData = new ClipData(DataType.MODEL_DATA.toString(), new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                        View.DragShadowBuilder shadow = new ImageViewDragShadowBuilder(view, vCategory.getSubCategoryAt(position).getDrawable());
                        m_DrawerLayout.closeDrawers();
                        return view.startDrag(dragData, shadow, null, 0);
                    }
                }
        );
    }

    public Category getCategoryAt(int vIndex) {
        return m_Categories.get(vIndex);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_operation);

        m_SurfaceView = new OperationGLSurfaceView(this);
        m_SurfaceView.requestFocus();
        m_SurfaceView.setFocusableInTouchMode(true);

        FrameLayout mainFrameLayout = (FrameLayout) findViewById(R.id.MainFrameLayout);
        mainFrameLayout.addView(m_SurfaceView);

        m_DrawerLayout = (DrawerLayout) findViewById(R.id.DrawerLayout);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout buttonLinearLayout = (LinearLayout) inflater.inflate(R.layout.button_layout, mainFrameLayout, false);
        mainFrameLayout.addView(buttonLinearLayout);

        initDrawer();
    }

    protected void onResume() {
        super.onResume();
        m_SurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        m_SurfaceView.onPause();
    }

    private void initDrawer() {
        setUpDrawerParams();
        m_ListView = (ListView) findViewById(R.id.DrawerListView);
        m_SubListView = (ListView) findViewById(R.id.DrawerSubListView);

        String categories[] = getResources().getStringArray(R.array.categories);
        for (int i = 0; i < categories.length; ++i) {
            String tmpArray[] = categories[i].split(",");
            Category category = new Category(tmpArray[0].trim(), tmpArray[1].trim());
            int arrayID = getResources().getIdentifier(category.getId(), "array", getPackageName());
            String subCategories[] = getResources().getStringArray(arrayID);
            for (int k = 0; k < subCategories.length; ++k) {
                String tmpArray2[] = subCategories[k].split(",");
                category.addSubCategory(new SubCategory(category.getName(), tmpArray2[0].trim(), tmpArray2[1].trim(), tmpArray2[2].trim()));
            }
            m_Categories.add(category);
        }
        m_ListAdapter = new DrawerListAdapter(getApplicationContext(), m_Categories);
        m_ListView.setAdapter(m_ListAdapter);

        setSubList(0, getCategoryAt(0));
        m_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                setSubList(position, getCategoryAt(position));
            }
        });
    }

    private void setUpDrawerParams() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;

        LinearLayout linerLayout = (LinearLayout) findViewById(R.id.LeftDrawerLinearLayout);
        ViewGroup.LayoutParams params = linerLayout.getLayoutParams();
        float drawerWidth = screenWidth * DRAWER_WIDTH_WEIGHT;
        params.width = (int) drawerWidth;
        linerLayout.setLayoutParams(params);
    }
}