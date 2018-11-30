package com.trio.standard.module.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.trio.standard.R;
import com.trio.standard.constant.HttpConstant;

import butterknife.ButterKnife;

/**
 * Created by lixia on 2018/11/30.
 */
public abstract class BaseMvpActivity<V extends BaseView, P extends BasePresenter<V>>
        extends AppCompatActivity implements BaseView {

    protected P mPresenter;
    protected Context mContext;

    protected abstract P createPresenter();

    /**
     * 绑定布局文件
     *
     * @return 布局文件ID
     */
    @LayoutRes
    protected abstract int attachLayoutRes();

    /**
     * 初始化视图控件
     */
    protected abstract void init();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(attachLayoutRes());
        mContext = this;
        mPresenter = createPresenter();
        ButterKnife.bind(this);
        init();
    }

    @Override
    public void showLoading(String msg, boolean cancelable) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(int errorCode, String msg) {
        switch (errorCode) {
            case HttpConstant.error_no_data:
                break;
            case HttpConstant.error_repetitive_operation:
                showToast(mContext.getString(R.string.toast_please_wait));
                break;
            case HttpConstant.error_no_internet:
                showToast(mContext.getString(R.string.error_no_internet));
                break;
            default:
                showToast(msg);
                break;
        }
    }

    public void showToast(String msg) {
        runOnUiThread(() -> Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void setRefreshing(boolean isRefreshing) {

    }

    @Override
    public void onBottom(boolean isBottom) {

    }

    public void toBack(View view) {
        if (view.getVisibility() == View.VISIBLE)
            finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.attach((V) this);
        }
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detach();
        }
        super.onDestroy();
        ButterKnife.unbind(this);
    }


}
