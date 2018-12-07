package com.trio.standard.module.date;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.trio.standard.R;
import com.trio.standard.module.base.BaseMvpFragment;
import com.trio.standard.module.base.BaseView;
import com.trio.standard.mvp.contract.PushContract;
import com.trio.standard.mvp.presenter.PushPresenter;
import com.trio.standard.widgets.CustomToolBar;
import com.trio.standard.widgets.ProgressButton;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lixia on 2018/11/28.
 */

public class DateFragment extends BaseMvpFragment<PushPresenter> implements PushContract.PushView {

    @Bind(R.id.customToolBar)
    CustomToolBar mCustomToolBar;
    @Bind(R.id.bt_date_pick1)
    Button mBtDatePick1;
    @Bind(R.id.tv_date1)
    TextView mTvDate1;
    @Bind(R.id.bt_date_pick2)
    Button mBtDatePick2;
    @Bind(R.id.tv_date2)
    TextView mTvDate2;
    @Bind(R.id.bt_date_pick3)
    Button mBtDatePick3;
    @Bind(R.id.tv_date3)
    TextView mTvDate3;
    @Bind(R.id.bt_push)
    Button mBtPush;
    @Bind(R.id.pb_download_db)
    ProgressButton mPbDownloadDb;
    @Bind(R.id.tv_db_count)
    TextView mTvDbCount;

    private static final int PERMISSION_REQUESTCODE = 1;
    private String mDateStr;

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_date;
    }

    @Override
    protected void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            permission();
    }

    @Override
    protected PushPresenter createPresenter() {
        return new PushPresenter(mContext, this);
    }

    private void permission() {
        String[] permission = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
        requestPermissions(permission, PERMISSION_REQUESTCODE);
    }

    @OnClick({R.id.bt_date_pick1, R.id.bt_date_pick2, R.id.bt_date_pick3, R.id.bt_push})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_date_pick1:
                //默认日期是系统当前日期
                mPresenter.showDataPicker("yyyy年MM月dd日", null, dateStr ->
                        mTvDate1.setText(dateStr)).show();
                break;
            case R.id.bt_date_pick2:
                //可以设置默认日期
                mPresenter.showDataPicker(null, mDateStr, dateStr -> {
                    mDateStr = dateStr;
                    mTvDate2.setText(dateStr);
                }).show();
                break;
            case R.id.bt_date_pick3:
                mPresenter.showDataPicker("yyyy/MM/dd", null, dateStr -> {
                    mTvDate3.setText(dateStr);
                }).show();
                break;
            case R.id.bt_push:
                mPresenter.pushOne("我是Trio推送的标题", "我是Trio推送的正文");
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUESTCODE:
                if (grantResults.length > 0) {
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(getActivity(), R.string.error_system_permission, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    onPermissionGranted();
                }
                break;
            default:
                break;
        }
    }

    private void onPermissionGranted() {

    }

    @Override
    public void getOnePush() {
        showToast("获取推送成功");
    }
}
