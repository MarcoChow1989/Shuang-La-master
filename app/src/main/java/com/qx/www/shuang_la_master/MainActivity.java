package com.qx.www.shuang_la_master;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.qx.www.shuang_la_master.activity.DetilActivity;
import com.qx.www.shuang_la_master.activity.MakeMonneyActivity;
import com.qx.www.shuang_la_master.activity.MoreActivity;
import com.qx.www.shuang_la_master.activity.ShouTuActivity;
import com.rey.material.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements View.OnClickListener
{
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.id_detil)
    Button idDetil;
    @Bind(R.id.id_more)
    Button idMore;
    @Bind(R.id.id_linear_zhuanqian)
    LinearLayout idLinearZhuanqian;
    @Bind(R.id.id_linear_shoutu)
    LinearLayout idLinearShoutu;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView()
    {
        toolbar.setTitle("爽啦");
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN)
        {
            if ((System.currentTimeMillis() - exitTime) > 2000)
            {
                Toast.makeText(getApplicationContext(), "再按一次退出应用",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else
            {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.id_detil, R.id.id_more, R.id.id_linear_zhuanqian, R.id.id_linear_shoutu})
    public void onClick(View view)
    {

        Intent intent = new Intent();
        switch (view.getId())
        {
            case R.id.id_detil:
                intent.setClass(this, DetilActivity.class);
                break;
            case R.id.id_more:
                intent.setClass(this, MoreActivity.class);
                break;
            case R.id.id_linear_zhuanqian:
                intent.setClass(this, MakeMonneyActivity.class);
                break;
            case R.id.id_linear_shoutu:
                intent.setClass(this, ShouTuActivity.class);
                break;
        }
        startActivity(intent);
    }

}
