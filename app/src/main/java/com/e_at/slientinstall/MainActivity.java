package com.e_at.slientinstall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private TextView apkPathText;
    private String apkPath;
    private static final int ACTION_REQUEST_CHOOSE_FILE = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        apkPathText = (TextView) findViewById(R.id.apkPathText);
    }

    //选择apk
    public void onChooseApkFile(View view) {
        startActivityForResult(new Intent(this, FileExplorerActivity.class), ACTION_REQUEST_CHOOSE_FILE);
    }

    //秒装
    public void onSilentInstall(View view) {

        if (!isRoot()) {
            Toast.makeText(this, "没有ROOT权限，不能使用秒装", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(apkPath)) {
            Toast.makeText(this, "请选择安装包！", Toast.LENGTH_SHORT).show();
            return;
        }
        final Button button = (Button) view;
        button.setText("安装中");
        new Thread(new Runnable() {
            @Override
            public void run() {
                SilentInstall installHelper = new SilentInstall();
                final boolean result = installHelper.install(apkPath);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result) {
                            Toast.makeText(MainActivity.this, "安装成功！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "安装失败！", Toast.LENGTH_SHORT).show();
                        }
                        button.setText("秒装");
                    }
                });

            }
        }).start();
    }

    //开启智能安装
    public void onForwardToAccessibility(View view) {

    }

    //智能安装
    public void onSmartInstall(View view) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == ACTION_REQUEST_CHOOSE_FILE) {
            apkPath = data.getStringExtra("apk_path");
            if (!TextUtils.isEmpty(apkPath))
                apkPathText.setText(apkPath);
        }
    }

    //是否root
    private boolean isRoot() {
        boolean bool = false;
        try {
            bool = (!new File("/system/bin/su").exists()) || (!new File("/system/xbin/su").exists());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bool;
    }
}
