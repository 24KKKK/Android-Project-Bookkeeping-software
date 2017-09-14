package com.example.account;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab);

        // 得到TabActivity中的TabHost对象
        TabHost tabHost = getTabHost();
        // 内容：采用布局文件中的布局
        LayoutInflater.from(this).inflate(R.layout.activity_main,
                tabHost.getTabContentView(), true);
        //   tabHost.setBackgroundColor(Color.GRAY);
        // 加上标签
        // 参数设置：新增的TabSpec的标签，标签中显示的字样
        // setContent设置内容对应的View资源标号
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("记录账单")
                .setContent(new Intent(this,WriteActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("查询账单")
                .setContent(new Intent(this,SecondActivity.class)));
    }

}
