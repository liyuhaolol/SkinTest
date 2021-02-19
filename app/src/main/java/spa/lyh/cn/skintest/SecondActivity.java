package spa.lyh.cn.skintest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.SkinAppCompatDelegateImpl;

import spa.lyh.cn.lib_utils.translucent.TranslucentUtils;
import spa.lyh.cn.skintest.base.BarColorActivity;

public class SecondActivity extends BarColorActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        TranslucentUtils.setTranslucentBoth(getWindow());
    }

    @NonNull
    @Override
    public AppCompatDelegate getDelegate() {
        return SkinAppCompatDelegateImpl.get(this, this);
    }


}
