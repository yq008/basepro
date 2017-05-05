package com.yq008.basepro.applib.imageselector.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.yq008.basepro.applib.AppActivity;
import com.yq008.basepro.applib.R;
import com.yq008.basepro.applib.imageselector.ImageSelectorConfig;
import com.yq008.basepro.applib.imageselector.model.LocalMedia;
import com.yq008.basepro.applib.imageselector.view.ImagePreviewDeleteActivity;
import com.yq008.basepro.applib.imageselector.view.ImageSelectorActivity;
import com.yq008.basepro.applib.util.permission.PermissionCallback;
import com.yq008.basepro.applib.util.permission.PermissionSDAndCamera;
import com.yq008.basepro.applib.widget.dialog.MyDialog;
import com.yq008.basepro.http.extra.ConfigUrl;
import com.yq008.basepro.util.AppHelper;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Xiay
 */

public class ImageSelectorAdapter extends RecyclerView.Adapter<ImageSelectorAdapter.ViewHolder> {
    private int maxPicCount;
    LocalMedia addIcon;
    public ArrayList<LocalMedia> images = new ArrayList<>();
    AppActivity act;
    int requestCode;
    boolean enableCrop;
    public int REQUEST_PREVIEW_DELETE;
    public int REQUEST_ADD;
    /**
     * 上传图片成功后的地址
     */
    public String[] urlPics;

    /**
     * 表单名
     */
    public String[] formNames;

    /**
     * @param act         Context
     * @param maxPicCount 显示的最多数量
     * @param addIcon     添加图标
     * @param requestCode 请求码，不能传0哦
     * @param enableCrop  是否开启裁剪功能（默认true）
     */
    public ImageSelectorAdapter(AppActivity act, int requestCode, int maxPicCount, LocalMedia addIcon, boolean enableCrop) {
        this.act = act;
        this.addIcon = addIcon;
        images.add(addIcon);
        this.maxPicCount = maxPicCount;
        this.enableCrop = enableCrop;
        this.requestCode = requestCode == 0 ? ImageSelectorActivity.REQUEST_IMAGE : requestCode;
        REQUEST_ADD = this.requestCode + 1;
        REQUEST_PREVIEW_DELETE = this.requestCode - 1;
        urlPics = new String[maxPicCount];
        formNames = new String[maxPicCount];
        for (int i = 0; i < maxPicCount; i++) {
            urlPics[i] = "";
            formNames[i] = "file_" + i;
        }
    }

    /**
     * @param act         Context
     * @param maxPicCount 显示的最多数量
     * @param addIcon     添加图标
     * @param enableCrop  是否开启裁剪功能（默认true）
     */
    public ImageSelectorAdapter(AppActivity act, int maxPicCount, LocalMedia addIcon, boolean enableCrop) {
        this(act, ImageSelectorActivity.REQUEST_IMAGE, maxPicCount, addIcon, enableCrop);
    }

    public ImageSelectorAdapter(AppActivity act, int maxPicCount, LocalMedia addIcon) {
        this(act, ImageSelectorActivity.REQUEST_IMAGE, maxPicCount, addIcon, true);
    }

    public ImageSelectorAdapter(AppActivity act, int maxPicCount) {
        this(act, maxPicCount, null);
    }

    @Override
    public ImageSelectorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_selector_item_result, parent, false);
        return new ImageSelectorAdapter.ViewHolder(itemView);
    }

    /**
     * 刷新图片
     */
    public void refeshPic(ArrayList<LocalMedia> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ImageSelectorAdapter.ViewHolder holder, final int position) {
        if (images.size() <= maxPicCount) {
            holder.imageView.setVisibility(View.VISIBLE);
            RequestManager manager = Glide.with(act);
            final DrawableTypeRequest request;
            if (images.get(position) != addIcon) {
                if (images.get(position).getPath().startsWith(ImageSelectorConfig.serverPathStartWith)) {
                    request = manager.load(ConfigUrl.getDomain() + images.get(position).getPath());
                } else {
                    request = manager.load(new File(images.get(position).getPath()));
                }
                request.centerCrop().into(holder.imageView);

            } else {
                manager.load(AppHelper.getInstance().resIdToUri(addIcon.getAddIconResId())).centerCrop().into(holder.imageView);
            }
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  mOnItemClickListener.onItemClick(v, position);
                    if (images.get(position).equals(addIcon)) {
                        final int mode;
                        if (maxPicCount == 1 || enableCrop == true) {
                            mode = ImageSelectorActivity.MODE_SINGLE;
                        } else {
                            mode = ImageSelectorActivity.MODE_MULTIPLE;
                        }
                        new PermissionSDAndCamera(act, new PermissionCallback(act) {
                            @Override
                            public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                                ImageSelectorActivity.start(act, REQUEST_ADD, maxPicCount - images.size() + 1, mode, true, false, enableCrop);
                            }
                        });

                    } else {
                        startPreview(images, position);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (images != null)
            return images.size();
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    public void startPreview(List<LocalMedia> previewImages, int position) {
        if (addIcon == null) {
            ImagePreviewDeleteActivity.startPreview((Activity) act, images, images, maxPicCount, position, true);
        } else {
            if (images.contains(addIcon)) {//if has addIcon, remove it
                List<LocalMedia> images = new ArrayList<>();
                images.addAll(previewImages);
                images.remove(addIcon);
                ImagePreviewDeleteActivity.startPreview((Activity) act, REQUEST_PREVIEW_DELETE, images, images, maxPicCount, position, false);
            } else {
                ImagePreviewDeleteActivity.startPreview((Activity) act, REQUEST_PREVIEW_DELETE, previewImages, images, maxPicCount, position, false);
            }
        }
    }

    /**
     * 获取上传的数据
     *
     * @return
     */
    public ArrayList<Map<String, Object>> getFormDatas() {
        ArrayList<Map<String, Object>> formNameList = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            String picUrl = images.get(i).getPath();
            if (!picUrl.equals("add") && !picUrl.startsWith(ImageSelectorConfig.serverPathStartWith)) {//判断图片是否是从相册中选择的，是再上传
                Map<String, Object> pic = new HashMap<>();
                pic.put(formNames[i], picUrl);
                formNameList.add(pic);
            }
        }
        return formNameList;
    }

    /**
     * requestCode =REQUEST_PREVIEW_DELETE调用方法
     *
     * @param data Intent
     * @return
     */
    public void onDeleteImage(Intent data) {
        images = (ArrayList<LocalMedia>) data.getSerializableExtra(ImagePreviewDeleteActivity.OUTPUT_LIST);
        for (int i = 0; i < maxPicCount; i++) {
            urlPics[i] = "";
        }
        for (int i = 0; i < images.size(); i++) {
            urlPics[i] = images.get(i).getPath();
        }
        if (images.size() <= maxPicCount) {
            images.add(addIcon);
        }
        notifyDataSetChanged();
    }

    /**
     * requestCode =REQUEST_ADD调用方法
     * @param data Intent
     */
    public void onAddImage(Intent data) {
        ArrayList<String> picPaths = data.getStringArrayListExtra(ImageSelectorActivity.REQUEST_OUTPUT);
        if (picPaths == null) {
            new MyDialog(act).showCancle("获取图片出错,请选择其它图片");
            return;
        }
        for (String picPath : picPaths) {
            images.add(new LocalMedia(picPath));
        }
        if (images.size() <= maxPicCount) {
            images.add(images.size() - 1, images.remove(images.indexOf(addIcon)));
        } else {
            images.remove(addIcon);
        }
        notifyDataSetChanged();
    }

    /**
     * 更新图片数据（接受服务器返回的json数据）
     */
    public void updatePicData(JSONObject jData) {
        for (int i = 0; i < maxPicCount; i++) {
            String file = jData.optString(formNames[i]);
            if (file != null && !file.equals(""))
                urlPics[i] = file;
        }
        images.clear();
        int length = urlPics.length;
        for (int i = 0; i < length; i++) {
            String picUrl = urlPics[i];
            if (!picUrl.equals("")) {
                LocalMedia pic = new LocalMedia(picUrl);
                images.add(pic);
            }
        }
        if (images.size() < length) {
            images.add(addIcon);
        }
    }
}
