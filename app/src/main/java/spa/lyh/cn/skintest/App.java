package spa.lyh.cn.skintest;

import android.app.Application;

import skin.support.SkinCompatManager;
import skin.support.app.SkinAppCompatViewInflater;
import skin.support.constraint.app.SkinConstraintViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;
import spa.lyh.cn.skintest.skin.SDCardSkinLoader;
import spa.lyh.cn.skintest.skin.SkinInfo;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SkinInfo.getInstance().with(getApplicationContext(),SkinInfo.AUTO);

        SkinCompatManager.withoutActivity(this)
                .addStrategy(new SDCardSkinLoader())          // 自定义加载策略，指定SDCard路径
                .addInflater(new SkinAppCompatViewInflater())           // 基础控件换肤初始化
                .addInflater(new SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
                .addInflater(new SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
                //.addInflater(new SkinCardViewInflater())                // CardView v7 控件换肤初始化[可选]
                .setSkinWindowBackgroundEnable(false)                   // 关闭windowBackground换肤，默认打开[可选]
                //.addStrategy(new ZipSDCardLoader())
                .loadSkin();
    }



}
