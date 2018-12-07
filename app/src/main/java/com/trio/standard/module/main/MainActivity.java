package com.trio.standard.module.main;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.LinearLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.igexin.sdk.PushManager;
import com.trio.standard.R;
import com.trio.standard.module.base.BaseMvpActivity;
import com.trio.standard.module.base.BasePresenter;
import com.trio.standard.module.date.DateFragment;
import com.trio.standard.module.image.FileFragment;
import com.trio.standard.module.music.MusicFragment;
import com.trio.standard.module.show.ShowListFragment;
import com.trio.standard.push.IntentService;
import com.trio.standard.push.PushService;

import butterknife.Bind;

public class MainActivity extends BaseMvpActivity implements BottomNavigationBar.OnTabSelectedListener {

    @Bind(R.id.fragment)
    LinearLayout mFragment;
    @Bind(R.id.bottomNavigationBar)
    BottomNavigationBar mBottomNavigationBar;

    private DateFragment mDateFragment;
    private FileFragment mFileFragment;
    private ShowListFragment mShowListFragment;
    private MusicFragment mMusicFragment;
    public Fragment curFragmentTag;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        PushManager.getInstance().initialize(this.getApplicationContext(), PushService.class);
        //IntentService 为第三方自定义的推送服务事件接收类
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), IntentService.class);

        if (mFileFragment == null)
            mFileFragment = new FileFragment();
        switchFragment(mFileFragment);

        mBottomNavigationBar.clearAll();
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.ic_file, getString(R.string.file))
                        .setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.mipmap.ic_file, getString(R.string.show))
                        .setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.mipmap.ic_file, getString(R.string.home))
                        .setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.mipmap.ic_file, getString(R.string.music))
                        .setActiveColorResource(R.color.colorPrimary))
                .setFirstSelectedPosition(0)
                .initialise();
        mBottomNavigationBar.setTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(int position) {
        switch (position) {
            case 0:
                if (mFileFragment == null)
                    mFileFragment = new FileFragment();
                switchFragment(mFileFragment);
                break;
            case 1:
                if (mShowListFragment == null)
                    mShowListFragment = new ShowListFragment();
                switchFragment(mShowListFragment);
                break;
            case 2:
                if (mDateFragment == null)
                    mDateFragment = new DateFragment();
                switchFragment(mDateFragment);
                break;
            case 3:
                if (mMusicFragment == null)
                    mMusicFragment = new MusicFragment();
                switchFragment(mMusicFragment);
                break;
        }
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    private void switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (!targetFragment.isAdded()) {
            if (curFragmentTag != null)
                transaction.hide(curFragmentTag);
            transaction
                    .add(R.id.fragment, targetFragment)
                    .commit();
        } else {
            transaction
                    .hide(curFragmentTag)
                    .show(targetFragment)
                    .commit();
        }
        curFragmentTag = targetFragment;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (curFragmentTag == mFileFragment)
            mFileFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
