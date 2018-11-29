package com.trio.standard.widgets;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.trio.standard.R;

/**
 * Created by lixiaoyan on 2017/9/14.
 */

public class LoadingDialog extends AlertDialog {

    private TextView mTvMsg;
    private String msg;
    private ProgressBar mProgressBar;
    private boolean showProgress;

    public LoadingDialog(Context context, String msg, Boolean showProgress) {
        super(context);
        this.msg = msg;
        this.showProgress = showProgress;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_loading);
        mTvMsg = (TextView) findViewById(R.id.tv_msg);
            mTvMsg.setText(msg);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_progress);
        if (showProgress)
            mProgressBar.setVisibility(View.VISIBLE);
        else
            mProgressBar.setVisibility(View.GONE);
    }

}
