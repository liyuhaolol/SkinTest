package spa.lyh.cn.skintest.skin;

import android.content.Context;
import android.util.Log;

import java.io.File;

import skin.support.load.SkinSDCardLoader;
import skin.support.utils.SkinFileUtils;

public class SDCardSkinLoader extends SkinSDCardLoader {
    public static final int SKIN_LOADER_STRATEGY_SDCARD = Integer.MAX_VALUE;

    @Override
    protected String getSkinPath(Context context, String skinName) {
        String path = new File(context.getExternalFilesDir(SkinInfo.skinPath).getAbsolutePath(), skinName).getAbsolutePath();
        return path;
    }

    @Override
    public int getType() {
        return SKIN_LOADER_STRATEGY_SDCARD;
    }
}
