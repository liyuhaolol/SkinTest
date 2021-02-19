package spa.lyh.cn.skintest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.SkinAppCompatDelegateImpl;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import skin.support.content.res.SkinCompatResources;
import spa.lyh.cn.lib_utils.SPUtils;
import spa.lyh.cn.lib_utils.translucent.TranslucentUtils;
import spa.lyh.cn.skintest.base.BarColorActivity;
import spa.lyh.cn.skintest.skin.SkinInfo;

public class MainActivity extends BarColorActivity {
    private TextView skin_path,skin_exist;
    private String fileDir,fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TranslucentUtils.setTranslucentBottom(getWindow());
        //加载皮肤，只在Auto模式下需要
        SkinInfo.getInstance().applySkin(this);
        initView();
        refreshView();
        checkFile();
    }

    private void initView(){
        fileName = SPUtils.getString("fileName","red-"+System.currentTimeMillis()+".skin",this);
        skin_path = findViewById(R.id.skin_path);
        skin_exist = findViewById(R.id.skin_exist);
        fileDir = getExternalFilesDir(SkinInfo.skinPath).getAbsolutePath();
        skin_path.setText(fileDir+File.separator+fileName);
    }

    private void refreshView(){
        getWindow().setStatusBarColor(SkinCompatResources.getColor(MainActivity.this,R.color.purple_700));
    }

    private void copyFile2Card(){
        if (checkFile()){
            //存在文件，重新生成文件名
            File file = new File(skin_path.getText().toString());
            file.delete();
            fileName = "red-"+System.currentTimeMillis()+".skin";
            skin_path.setText(fileDir+File.separator+fileName);
        }
        try {
            InputStream is = this.getAssets().open("skins/red.skin");
            File zipFile = new File(fileDir, fileName);
            OutputStream os = new FileOutputStream(zipFile);
            int byteCount;
            byte[] bytes = new byte[1024];
            while ((byteCount = is.read(bytes)) != -1) {
                os.write(bytes, 0, byteCount);
            }
            os.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SPUtils.putString("fileName",fileName,this);
        Toast.makeText(this,"解压缩成功",Toast.LENGTH_SHORT).show();
        checkFile();
    }

    private boolean checkFile(){
        File file = new File(skin_path.getText().toString());
        if (file.exists()){
            skin_exist.setText("存在皮肤文件");
            return true;
        }else {
            skin_exist.setText("不存在皮肤文件");
            return false;
        }
    }

    public void onActivityJump(View v){
        Intent intent = new Intent(this,SecondActivity.class);
        startActivity(intent);
    }

    public void onUnZipFileClick(View v){
        copyFile2Card();
    }

    public void onAutoClick(View v){
        SkinInfo.getInstance().applySkin(this,SkinInfo.AUTO);
    }

    public void onDefaultClick(View v){
        SkinInfo.getInstance().applySkin(this,SkinInfo.LIGHT);
    }

    public void onTheme1Click(View v){
        SkinInfo.getInstance().applySkin(this,SkinInfo.DARK);

    }

    public void onTheme2Click(View v){
        if (checkFile()){
            SkinInfo.getInstance().applySkin(this,fileName);
        }else {
            Toast.makeText(this,"没有解压插件皮肤",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void applySkin() {
        super.applySkin();
        refreshView();
    }


    @NonNull
    @Override
    public AppCompatDelegate getDelegate() {
        return SkinAppCompatDelegateImpl.get(this, this);
    }

}