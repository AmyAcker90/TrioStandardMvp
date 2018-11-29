package com.trio.standard.module.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.trio.standard.R;
import com.trio.standard.constant.HttpConstant;
import com.trio.standard.widgets.LoadingDialog;

import butterknife.ButterKnife;

/**
 * Created by lixia on 2018/11/21.
 */

public abstract class BaseActivity<T extends BasePresenter> extends RxAppCompatActivity implements BaseView {

    protected T mPresenter;
    protected Context mContext;
    private LoadingDialog mLoadingDialog;

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
        ButterKnife.bind(this);
        init();
    }

    @Override
    public void onSuccess(int requestCode, Object data) {

    }

    @Override
    public void onError(int requestCode, int errorCode, String msg) {
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
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.<T>bindToLifecycle();
    }


    @Override
    public void showLoading(String msg, boolean cancelable) {
        hideLoading();
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this, msg, true);
        }
        mLoadingDialog.setMessage(msg);
        mLoadingDialog.setCancelable(cancelable);
        mLoadingDialog.show();
    }

    @Override
    public void hideLoading() {
        runOnUiThread(() -> {
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
                mLoadingDialog = null;
            }
        });
    }

    public void toBack(View view) {
        if (view.getVisibility() == View.VISIBLE)
            finish();
    }

    @Override
    public void setRefreshing(boolean isRefreshing) {

    }

    @Override
    public void onBottom(boolean isBottom) {

    }

    @Override
    public void onProgress(int requestCode, int progress) {

    }
}
