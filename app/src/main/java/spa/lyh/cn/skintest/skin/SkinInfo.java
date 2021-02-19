package spa.lyh.cn.skintest.skin;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.XmlResourceParser;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;

import skin.support.SkinCompatManager;
import skin.support.content.res.SkinCompatResources;
import spa.lyh.cn.lib_utils.SPUtils;
import spa.lyh.cn.skintest.R;
import spa.lyh.cn.skintest.skin.model.BarColor;

public class SkinInfo{
    public static final String skinPath = "skins";
    private static HashMap<String,BarColor> map;
    private static SkinInfo instance;

    public final static int INIT = -2;
    public final static int OTHER = -1;
    public final static int AUTO = 0;
    public final static int DARK = 1;
    public final static int LIGHT = 2;

    public final static String AUTO_NAME = "auto";
    public final static String DARK_NAME = "night.skin";
    public final static String LIGHT_NAME = "light";


    public static SkinInfo getInstance(){
        if (instance == null){
            synchronized (SkinInfo.class){
                if (instance == null){
                    instance = new SkinInfo();
                }
            }
        }
        return instance;
    }

    private HashMap<String, BarColor> getMap(Context context){
        if (map == null){
            map = new HashMap<>();
        }
        XmlResourceParser xrp = SkinCompatResources.getXml(context, R.xml.front);
        try {
            int eventType = xrp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        String name = xrp.getName();
                        if (name.equals("item")){
                            BarColor bc = new BarColor();
                            String statusDark = xrp.getAttributeValue(null, "status_bar");
                            String navDark = xrp.getAttributeValue(null, "nav_bar");
                            bc.isStatusBarFontDark = Boolean.parseBoolean(statusDark);
                            bc.isNavBarFontDark = Boolean.parseBoolean(navDark);
                            map.put(xrp.getAttributeValue(null, "key"),bc);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = xrp.next();
            }
        }catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 默认使用的mode，这里强制要求只能使用light或auto
     */
    public void with(Context context,int defaultMode){
        int mode = SPUtils.getInt("themeMode",INIT,context);
        if (defaultMode == LIGHT || defaultMode == AUTO){
            if (mode == INIT){
                setInAppThemeMode(context,defaultMode);
            }
        }else {
            Log.e("SkinInfo","初始主题只能使用LIGHT或AUTO");
        }
    }

    public HashMap<String, BarColor> getBarColorInfo(Context context){
        if (map == null){
            map = getMap(context);
        }
        return map;
    }

    public void updateBarColorInfo(Context context){
        map = getMap(context);
    }

    /**
     * mod只针对集成在app内的主题，不包括下发主题
     * @param context
     * @param mod
     */
    public void setInAppThemeMode(Context context,int mod){
        SPUtils.putInt("themeMode",mod,context);
        switch (mod){
            case AUTO:
                SPUtils.putString("themeFilename",AUTO_NAME,context);
                break;
            case LIGHT:
                SPUtils.putString("themeFilename",LIGHT_NAME,context);
                break;
            case DARK:
                SPUtils.putString("themeFilename",DARK_NAME,context);
                break;
        }
    }

    public int getInAppThemeMode(Context context){
        return SPUtils.getInt("themeMode",AUTO,context);
    }

    /**
     * 主题文件名称
     * @param context
     * @param name
     */
    public void setThemeFileName(Context context,String name){
        SPUtils.putString("themeFilename",name,context);
        SPUtils.putInt("themeMode",OTHER,context);
    }

    public String getThemeFileName(Context context){
        return SPUtils.getString("themeFilename",AUTO_NAME,context);
    }


    /**
     * 判断主题是否是深色，用于auto模式后辅助判断。
     * @param context
     * @return
     */
    public boolean isDark(Context context){
        int mode = getInAppThemeMode(context);
        if (mode == DARK){
            return true;
        }else if (mode == LIGHT || mode == OTHER){
            return false;
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                if ((context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES){
                    return true;
                }else {
                    return false;
                }
            }else{
                return false;
            }
        }
    }

    public boolean isStatusBarFontDark(Context context,String name){
        BarColor color = getBarColorInfo(context).get(name);
        if (color != null){
            return color.isStatusBarFontDark;
        }else {
            //内容为空
            return !isDark(context);
        }
    }

    public boolean isNavBarFontDark(Context context,String name){
        BarColor color = getBarColorInfo(context).get(name);
        if (color != null){
            return color.isNavBarFontDark;
        }else {
            //内容为空
            return !isDark(context);
        }
    }

    public void applySkin(Context context,int mode){
        int old_mode = getInAppThemeMode(context);
        if (mode < AUTO || mode > LIGHT){
            Log.e("SkinInfo","无意义的参数");
            return;
        }
        if (old_mode != mode){
            //主题发生改变
            setInAppThemeMode(context,mode);
            applySkin(context);
        }
    }

    public void applySkin(Context context,String name){
        if (TextUtils.isEmpty(name)){
            return;
        }
        String old_name = getThemeFileName(context);
        if (!old_name.equals(name)){
            //主题发生改变
            setThemeFileName(context,name);
            applySkin(context);
        }
    }

    /**
     * 加载皮肤
     */
    public void applySkin(Context context){
        String name = getThemeFileName(context);
        if (name.equals(LIGHT_NAME)){
            //浅色主题
            SkinCompatManager.getInstance().restoreDefaultTheme();
        }else if (name.equals(DARK_NAME)){
            //深色主题
            SkinCompatManager.getInstance().loadSkin(DARK_NAME, SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS);
        }else if (name.equals(AUTO_NAME)){
            //跟随系统
            if (isDark(context)){
                //当前系统为深色模式
                SkinCompatManager.getInstance().loadSkin(DARK_NAME, SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS);
            }else {
                //当前系统为浅色模式
                SkinCompatManager.getInstance().restoreDefaultTheme();
            }
        }else {
            //下发的主题
            SkinCompatManager.getInstance().loadSkin(name,SDCardSkinLoader.SKIN_LOADER_STRATEGY_SDCARD);
        }
    }
}
