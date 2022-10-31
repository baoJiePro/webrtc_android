package com.baojie.baselib.image;

import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

/**
 * @Description: TODO
 * @Author baojie@qding.me
 * @Date 2020-03-02 15:08
 * @Version TODO
 */
public interface OnPictureSelectCallBackListener {
    /**
     * 选择完成回调
     * @param result
     */
    void onResult(List<LocalMedia> result);
}
