package com.trio.standard.widgets;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.trio.standard.R;
import com.trio.standard.utils.ImageUtil;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lixia on 2018/9/20.
 */

public class ImagePicker extends LinearLayout {

    public static final int REQUEST_CODE_CHOOSE = 1;

    private GridViewForScrollView mGridView;
    private GridAdapter mAdapter;
    private Context mContext;

    private List<String> mPaths = new ArrayList<>();

    private int default_int = 10;
    private int maxSelectable = default_int;
    private int leftSelectable = default_int - 1;

    public List<String> getPaths() {
        List<String> newPath = new ArrayList<>();
        if (mPaths != null && mPaths.size() > 0) {
            newPath.addAll(mPaths);
            for (int i = 0; i < newPath.size(); i++) {
                if (newPath.get(i) == null)
                    newPath.remove(i);
            }
        }
        return newPath;
    }

    public ImagePicker(Context context) {
        this(context, null);
    }

    public ImagePicker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImagePicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.view_image_pick, null);
        mGridView = (GridViewForScrollView) view.findViewById(R.id.gridview);
        mAdapter = new GridAdapter();
        mPaths.add(null);
        mGridView.setNumColumns(3);
        mGridView.setAdapter(mAdapter);
        //然后使用LayoutParams把控件添加到子view中
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
        addView(view, lp);
    }

    public void setNumColumns(int numColumns) {
        if (mGridView != null)
            mGridView.setNumColumns(numColumns);
    }

    public void setColumnWidth(int columnWidth) {
        if (mGridView != null)
            mGridView.setColumnWidth(columnWidth);
    }

    public void setPaths(List<String> paths) {
        if (mGridView != null && mAdapter != null) {
            if (paths == null || paths.size() == 0) {
                mPaths.clear();
                mPaths.add(null);
            } else {
                for (String s : paths) {
                    if (!mPaths.contains(s))
                        mPaths.add(s);
                }
            }

            if (mPaths.size() == maxSelectable) {
                mPaths.remove(0);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    public void setMaxSelectable(int maxSelectable) {
        this.maxSelectable = maxSelectable;
        if (maxSelectable < 1)
            this.maxSelectable = 1;
        if (maxSelectable > 9)
            this.maxSelectable = 9;
        leftSelectable = maxSelectable;
        this.maxSelectable++;
    }

    private class GridAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mPaths.size();
        }

        @Override
        public Object getItem(int position) {
            return mPaths.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            leftSelectable = maxSelectable - mPaths.size();
            LogUtils.i("mList size: " + mPaths.size()
                    + "maxSelectable: " + maxSelectable
                    + "leftSelectable: " + leftSelectable);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null)
                view = LayoutInflater.from(mContext).inflate(R.layout.item_image_pick, null);
            else
                view = convertView;
            ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
            ImageView ivDel = (ImageView) view.findViewById(R.id.iv_delete);
            if (mPaths.get(position) != null) {
                ivDel.setVisibility(VISIBLE);
                ImageUtil.getInstance(mContext).loadCenterCrop(mPaths.get(position), imageView);
                ivDel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPaths.remove(position);
                        if (mPaths != null && mPaths.size() > 0) {
                            if (mPaths.get(0) != null)
                                mPaths.add(0, null);
                        } else
                            mPaths.add(0, null);
                        notifyDataSetChanged();
                    }
                });
                imageView.setOnClickListener(null);
            } else {
                ivDel.setVisibility(GONE);
                if (position == 0) {
                    imageView.setImageResource(R.mipmap.ic_add_img);
                    imageView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //检查是否有权限
                            boolean isLack = lacksPermissions(mContext, Arrays.asList(
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE));
                            if (!isLack) {
                                Matisse.from((Activity) mContext)
                                        .choose(MimeType.ofAll(), false) // 选择 mime 的类型
                                        .countable(true)//true:选中后显示数字;false:选中后显示对号
                                        .capture(true)//拍照功能
                                        .captureStrategy(new CaptureStrategy(true,
                                                mContext.getPackageName() + ".provider"))
                                        //照片存储路径
                                        .theme(R.style.Matisse_Dracula)//黑色背景
                                        .maxSelectable(leftSelectable) // 图片选择的最多数量
                                        .gridExpectedSize(400)//图片显示表格的大小
                                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                        .thumbnailScale(0.85f) // 缩略图的比例
                                        .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                                        .forResult(REQUEST_CODE_CHOOSE); // 设置作为标记的请求码
                            } else
                                Toast.makeText(mContext, "请前往设置开启权限", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            return view;
        }
    }

    /**
     * 判断权限集合
     * permissions 权限数组
     * return true-表示没有改权限  false-表示权限已开启
     */
    public static boolean lacksPermissions(Context mContexts, List<String> permissions) {
        for (String permission : permissions) {
            if (lacksPermission(mContexts, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否缺少权限
     */
    private static boolean lacksPermission(Context mContexts, String permission) {
        return ContextCompat.checkSelfPermission(mContexts, permission) ==
                PackageManager.PERMISSION_DENIED;
    }
}
