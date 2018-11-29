package com.trio.standard.module.image;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.trio.standard.R;
import com.trio.standard.api.bean.FileBean;
import com.trio.standard.module.base.BaseFragment;
import com.trio.standard.module.base.BaseView;
import com.trio.standard.module.file.FileActivity;
import com.trio.standard.utils.ImageUtil;
import com.trio.standard.widgets.ImagePicker;
import com.trio.standard.widgets.ProgressButton;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static android.app.Activity.RESULT_OK;
import static com.trio.standard.widgets.ImagePicker.REQUEST_CODE_CHOOSE;

/**
 * Created by lixia on 2018/11/28.
 */

public class ImageFragment extends BaseFragment<ImagePresenter> implements ImageMainView {
    @Bind(R.id.rb_one)
    RadioButton mRbOne;
    @Bind(R.id.rb_nine)
    RadioButton mRbNine;
    @Bind(R.id.radiogroup)
    RadioGroup mRadiogroup;
    @Bind(R.id.imagepicker)
    ImagePicker mImagepicker;
    @Bind(R.id.pb_upload)
    ProgressButton mProgressBt;
    @Bind(R.id.image1)
    ImageView mImage1;
    @Bind(R.id.image2)
    ImageView mImage2;
    @Bind(R.id.image3)
    ImageView mImage3;
    @Bind(R.id.image4)
    ImageView mImage4;
    @Bind(R.id.btn_file)
    Button mBtnFile;

    private String imageUrl = "http://pic22.nipic.com/20120621/1628220_155636709122_2.jpg";

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_image;
    }

    @Override
    protected void init() {
        mImagepicker.setNumColumns(3);
        //图片选择器最多可以选择的图片数量
        mImagepicker.setMaxSelectable(1);

        mRadiogroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_one:
                    mImagepicker.setMaxSelectable(1);
                    mImagepicker.setPaths(new ArrayList<String>());
                    break;
                case R.id.rb_nine:
                    mImagepicker.setMaxSelectable(9);
                    mImagepicker.setPaths(new ArrayList<String>());
                    break;
            }
        });
        ImageUtil.getInstance(getActivity()).load(imageUrl, mImage1);
        ImageUtil.getInstance(getActivity()).loadCenterCrop(imageUrl, mImage2);
        ImageUtil.getInstance(getActivity()).loadRound(imageUrl, mImage3);
        ImageUtil.getInstance(getActivity()).loadRadius(imageUrl, mImage4, 30);

        mPresenter = new ImagePresenter(this);
        mProgressBt.setState(ProgressButton.STATE.NORMAL, getString(R.string.toast_upload_file));
        mProgressBt.setOnClickListener(v -> {
            List<String> result = mImagepicker.getPaths();
            if (result.size() > 0) {
                mPresenter.uploadFiles(result);
            } else
                showToast(getString(R.string.error_image_num));
        });
        mBtnFile.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, FileActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CHOOSE) {
                List<Uri> mSelected = Matisse.obtainResult(data);
                List<String> paths = Matisse.obtainPathResult(data);
                mImagepicker.setPaths(paths);
                for (String s : paths) {
                    //更新图库资源
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    File f = new File(s);
                    Uri contentUri = Uri.fromFile(f);
                    mediaScanIntent.setData(contentUri);
                    getActivity().sendBroadcast(mediaScanIntent);
                }
            }
        }
    }

    @Override
    public void onProgress(int requestCode, int progress) {
        super.onProgress(requestCode, progress);
        mProgressBt.setState(ProgressButton.STATE.PERCENT, null);
        mProgressBt.setProgress(progress);
    }

    @Override
    public void loadingDone(List<FileBean> data) {
        mProgressBt.setState(ProgressButton.STATE.NORMAL, getString(R.string.toast_upload_finish));
    }
}
