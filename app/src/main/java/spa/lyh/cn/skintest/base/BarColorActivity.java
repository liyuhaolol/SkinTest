package spa.lyh.cn.skintest.base;


import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import skin.support.widget.SkinCompatSupportable;
import spa.lyh.cn.lib_utils.translucent.navbar.NavBarFontColorControler;
import spa.lyh.cn.lib_utils.translucent.statusbar.StatusBarFontColorControler;
import spa.lyh.cn.skintest.skin.SkinInfo;


public class BarColorActivity extends AppCompatActivity implements SkinCompatSupportable {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkBarColor();
    }

    public void checkBarColor(){
        StatusBarFontColorControler.setStatusBarMode(getWindow(), SkinInfo.getInstance().isStatusBarFontDark(this,getLocalClassName()));
        NavBarFontColorControler.setNavBarMode(getWindow(),SkinInfo.getInstance().isNavBarFontDark(this,getLocalClassName()));
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (SkinInfo.getInstance().getInAppThemeMode(this) == SkinInfo.AUTO){
            //自动模式下，重载皮肤
            SkinInfo.getInstance().applySkin(this);
        }
    }

    @Override
    public void applySkin() {
        SkinInfo.getInstance().updateBarColorInfo(this);
        //状态栏转色
        checkBarColor();
    }

}
