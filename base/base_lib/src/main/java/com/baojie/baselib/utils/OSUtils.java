package com.baojie.baselib.utils;

import android.os.Build;
import android.text.TextUtils;


/**
 * @Description: 获取操作系统，华为，小米，其他。
 *
 * @Author wangjianzhou@qding.me
 * @Date 2019-11-14 17:55
 * @Version v6.1.1
 */
public class OSUtils {
    private static final String PREFIX_HUAWEI = "HUAWEI";
    private static final String PREFIX_XIAOMI = "XIAOMI";
    private static final String PREFIX_FLYME = "MEIZUS";
    private static final String PREFIX_ONEPLUS = "ONEPLUS";
    private static final String PREFIX_OTHERS = "OTHERS";

    //MIUI标识
    public static final String BRAND_MIUI = "xiaomi";

    //EMUI标识
    public static final String BRAND_EMUI1 = "huawei";
    public static final String BRAND_EMUI2 = "honor";

    //oppo标识
    public static final String BRAND_OPPO = "oppo";

    //vivo标识
    public static final String BRAND_VIVO = "vivo";

    //魅族标识
    public static final String BRAND_FLYME = "meizu";

    //1+手机表示
    public static final String BRAND_ONEPLUS = "oneplus";

    public enum ROM_TYPE {
        MIUI(PREFIX_XIAOMI),
        EMUI(PREFIX_HUAWEI),
        FLYME(PREFIX_FLYME),
        ONEPLUS(PREFIX_FLYME),
        OTHER(PREFIX_OTHERS);

        private String prefix;

        ROM_TYPE(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }
    }

    /**
     * @param
     * @return ROM_TYPE ROM类型的枚举
     * @description获取ROM类型: MIUI, EMUI, FLYME, OTHER
     */

    public static ROM_TYPE getRomType() {
        ROM_TYPE rom_type = ROM_TYPE.OTHER;

        String brand = Build.BRAND;
        if (!TextUtils.isEmpty(brand)) {
            if (brand.toLowerCase().contains(BRAND_EMUI1) || brand.toLowerCase().contains(BRAND_EMUI2)) {
                return ROM_TYPE.EMUI;
            }
            if (brand.toLowerCase().contains(BRAND_MIUI)) {
                return ROM_TYPE.MIUI;
            }
            if (brand.toLowerCase().contains(BRAND_FLYME)) {
                return ROM_TYPE.FLYME;
            }
        }
        return rom_type;
    }

    public static boolean isOppo() {
        String brand = Build.BRAND;
        if (!TextUtils.isEmpty(brand)) {
            if (brand.toLowerCase().contains(BRAND_OPPO)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isXiaoMi() {
        String brand = Build.BRAND;
        if (!TextUtils.isEmpty(brand)) {
            if (brand.toLowerCase().contains(BRAND_MIUI)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isHuawei() {
        String brand = Build.BRAND;
        if (!TextUtils.isEmpty(brand)) {
            if (brand.toLowerCase().contains(BRAND_EMUI1) || brand.toLowerCase().contains(BRAND_EMUI2)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isVivo() {
        String brand = Build.BRAND;
        if (!TextUtils.isEmpty(brand)) {
            if (brand.toLowerCase().contains(BRAND_VIVO)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isFlyme() {
        return ROM_TYPE.FLYME.name().equals(OSUtils.getRomType().name());
    }

    public static boolean isOnePlus() {
        String brand = Build.BRAND;
        if (!TextUtils.isEmpty(brand)) {
            if (brand.toLowerCase().contains(BRAND_ONEPLUS)) {
                return true;
            }
        }
        return false;
    }
}
