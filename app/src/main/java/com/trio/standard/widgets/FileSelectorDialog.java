package com.trio.standard.widgets;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.trio.standard.R;
import com.trio.standard.constant.HttpConstant;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FileSelectorDialog extends DialogFragment {

    @Bind(R.id.file_selector_title_close)
    ImageView mFileSelectorTitleClose;
    @Bind(R.id.file_selector_title_text)
    TextView mFileSelectorTitleText;
    @Bind(R.id.file_selector_list)
    RecyclerView mFileSelectorList;
    @Bind(R.id.file_selector_confirm_btn)
    Button mFileSelectorConfirmBtn;

    private List<FileSelectorBean> fileList = new ArrayList<>();
    private FileSelectorAdapter mFileSelectorAdapter;
    public String mCurrentDir;
    public int filterMode = FILTER_MODE.FILTER_ALL.ordinal();
    private int selectMode = SELECT_MODE.FILE.ordinal();

    public enum FILTER_MODE {
        FILTER_ALL, FILTER_IMAGE, FILTER_TXT, FILTER_AUDIO, FILTER_VIDEO
    }

    public enum SELECT_MODE {
        FILE, DIR
    }

    private int width = ViewGroup.LayoutParams.MATCH_PARENT;
    private int height = ViewGroup.LayoutParams.MATCH_PARENT;

    public interface OnFileSelectorDialogListener {

        void onFileSelectFinish(String path);

        void onFileSelectCancel();
    }

    private OnFileSelectorDialogListener mOnFileSelectorDialogListener;

    public void setOnFileSelectorDialogListener(OnFileSelectorDialogListener onFileSelectorDialogListener) {
        this.mOnFileSelectorDialogListener = onFileSelectorDialogListener;
    }

    public void setFilterMode(int filterMode) {
        this.filterMode = filterMode;
        mCurrentDir = HttpConstant.app_file_dir;
    }

    public void setCurrentDir(String currentDir) {
        mCurrentDir = currentDir;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.file_selector_main, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(width, height);
    }

    private void init() {
        mFileSelectorTitleClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnFileSelectorDialogListener != null)
                    mOnFileSelectorDialogListener.onFileSelectCancel();
                dismiss();
            }
        });
        if (selectMode == SELECT_MODE.FILE.ordinal()) {
            mFileSelectorTitleText.setText(R.string.file_selector_file);
            mFileSelectorConfirmBtn.setVisibility(View.GONE);
        } else {
            mFileSelectorTitleText.setText(R.string.file_selector_dir);
            mFileSelectorConfirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnFileSelectorDialogListener != null)
                        mOnFileSelectorDialogListener.onFileSelectFinish(mCurrentDir);
                    dismiss();
                }
            });
        }
//        mCurrentDir = Environment.getExternalStorageDirectory().getPath();
        //初始化路径
        mCurrentDir = HttpConstant.app_file_dir;
        fileList = getFilesList(mCurrentDir, selectMode, filterMode);
        if (fileList != null) {
            mFileSelectorAdapter = new FileSelectorAdapter();
            mFileSelectorList.setHasFixedSize(true);
            mFileSelectorList.setLayoutManager(new LinearLayoutManager(getActivity()));
            mFileSelectorList.setAdapter(mFileSelectorAdapter);
        } else if (mOnFileSelectorDialogListener != null) {
            Toast.makeText(getActivity(), R.string.file_selector_get_fail, Toast.LENGTH_SHORT).show();
            mOnFileSelectorDialogListener.onFileSelectCancel();
            dismiss();
        }
    }

    private void refreshList(String path) {
        List<FileSelectorBean> newFilesList;
        try {
            newFilesList = getFilesList(path, selectMode, filterMode);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), R.string.file_selector_get_fail, Toast.LENGTH_SHORT).show();
            return;
        }
        fileList.clear();
        fileList.addAll(newFilesList);
        mFileSelectorAdapter.notifyDataSetChanged();
        mFileSelectorList.smoothScrollToPosition(0);
        mCurrentDir = path;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    class FileSelectorAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.file_selector_item, null);
            return new FileItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
            FileItemViewHolder holder = (FileItemViewHolder) viewHolder;
            FileSelectorBean fileSelectorBean = fileList.get(position);
            int icon = 0;
            String name = fileSelectorBean.getName();
            int type = fileSelectorBean.getType();
            switch (type) {
                case FILE_TYPE_DIR:
                    icon = R.mipmap.ic_file_selector_dir;
                    break;
                case FILE_TYPE_FILE:
                    icon = R.mipmap.ic_file_selector_file;
                    break;
                case FILE_TYPE_UPPER:
                    icon = R.mipmap.ic_file_selector_upper;
                    name = getActivity().getString(R.string.file_selector_upper);
                    break;
            }
            if (icon != 0)
                holder.icon.setImageResource(icon);
            if (name != null)
                holder.name.setText(name);
            holder.rootView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            holder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fileList == null || fileList.size() == 0)
                        return;
                    FileSelectorBean fileSelectorBean = fileList.get(position);
                    String path = fileSelectorBean.getPath();
                    if (path == null)
                        return;
                    int type = fileSelectorBean.getType();
                    switch (type) {
                        case FILE_TYPE_DIR:
                        case FILE_TYPE_UPPER:
                            refreshList(path);
                            break;
                        case FILE_TYPE_FILE:
                            if (mOnFileSelectorDialogListener != null)
                                mOnFileSelectorDialogListener.onFileSelectFinish(path);
                            dismiss();
                            break;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return fileList == null ? 0 : fileList.size();
        }

        class FileItemViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.file_selector_item_icon)
            ImageView icon;
            @Bind(R.id.file_selector_item_name)
            TextView name;
            @Bind(R.id.file_selector_item_root)
            LinearLayout rootView;

            FileItemViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    public final int FILE_TYPE_UPPER = 10;

    public final int FILE_TYPE_DIR = 20;

    public final int FILE_TYPE_FILE = 30;

    private FileCompare fileCompare = new FileCompare();

    /**
     * get files list
     *
     * @param dir    the directory
     * @param mode   get files or only directory list
     * @param filter filter file type
     * @return FileSelectorBean List
     */
    public List<FileSelectorBean> getFilesList(String dir, int mode, int filter) {
        File dirFile = new File(dir);
        if (dirFile.exists() && dirFile.isDirectory()) {
            File[] files = dirFile.listFiles();
            List<FileSelectorBean> dirList = new ArrayList<>();
            List<FileSelectorBean> fileList = new ArrayList<>();
            File parentFile = dirFile.getParentFile();

            for (File file : files) {
                FileSelectorBean value = new FileSelectorBean();
                String filePath = file.getAbsolutePath();
                String fileName = file.getName();
                if (file.isHidden())
                    continue;
                if (file.isDirectory()) {
                    value.setName(fileName);
                    value.setPath(filePath);
                    value.setType(FILE_TYPE_DIR);
                    dirList.add(value);
                } else if (mode == SELECT_MODE.FILE.ordinal()) {
                    String extensionName = getExtensionName(fileName);
                    // filter by file extension name
                    if (doFilter(extensionName, filter)) {
                        value.setName(fileName);
                        value.setPath(filePath);
                        value.setType(FILE_TYPE_FILE);
                        value.setExtension(extensionName);
                        fileList.add(value);
                    }
                }
            }
            Collections.sort(dirList, fileCompare);
            Collections.sort(fileList, fileCompare);

            if (parentFile != null) {
                FileSelectorBean upper = new FileSelectorBean();
                upper.setPath(parentFile.getAbsolutePath());
                upper.setName(parentFile.getName());
                upper.setType(FILE_TYPE_UPPER);
                dirList.add(0, upper);
            }

            List<FileSelectorBean> allList = new ArrayList<>();
            allList.addAll(dirList);
            allList.addAll(fileList);
            return allList;
        } else
            return null;
    }

    public String getExtensionName(String fileName) {
        if (!fileName.contains("."))
            return "";
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private class FileCompare implements Comparator<FileSelectorBean> {
        @Override
        public int compare(FileSelectorBean lhs, FileSelectorBean rhs) {
            return lhs.getName().toUpperCase().compareTo(rhs.getName().toUpperCase());
        }
    }


    public final String[] IMAGE_FILTER = new String[]{"png", "jpg", "bmp"};

    public final String[] TXT_FILTER = new String[]{"txt", "c", "cpp", "java", "xml"};

    public final String[] AUDIO_FILTER = new String[]{"mp3", "m4a"};

    public final String[] VIDEO_FILTER = new String[]{"mp4", "avi", "flv"};

    /**
     * filter files
     *
     * @param extensionName file extension name
     * @return if extension name match type, return true
     */
    public boolean doFilter(String extensionName, int filterMode) {
        if (filterMode == FILTER_MODE.FILTER_ALL.ordinal())
            return true;
        if (TextUtils.isEmpty(extensionName))
            return false;
        String[] filter = null;
        if (filterMode == FILTER_MODE.FILTER_AUDIO.ordinal())
            filter = AUDIO_FILTER;
        else if (filterMode == FILTER_MODE.FILTER_IMAGE.ordinal())
            filter = IMAGE_FILTER;
        else if (filterMode == FILTER_MODE.FILTER_TXT.ordinal())
            filter = TXT_FILTER;
        else if (filterMode == FILTER_MODE.FILTER_VIDEO.ordinal())
            filter = VIDEO_FILTER;
        if (filter == null)
            return true;
        for (String f : filter) {
            if (f.equals(extensionName))
                return true;
        }
        return false;
    }

    class FileSelectorBean {
        private int type;

        private String name;

        private String path;

        private String extension;

        public String getExtension() {
            return extension;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
