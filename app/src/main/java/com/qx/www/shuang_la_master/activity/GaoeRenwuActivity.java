package com.qx.www.shuang_la_master.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.qx.www.shuang_la_master.BaseActivity;
import com.qx.www.shuang_la_master.R;
import com.qx.www.shuang_la_master.adapter.Xianshi_RenwuAdapter;
import com.qx.www.shuang_la_master.common.AutoLoadRecylerView;
import com.qx.www.shuang_la_master.common.DividerItemDecoration;
import com.qx.www.shuang_la_master.domain.XianshiRenwu;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GaoeRenwuActivity extends BaseActivity implements AutoLoadRecylerView.loadMoreListener
{

    @Bind(R.id.toolbar1)
    Toolbar toolbar1;
    @Bind(R.id.id_gaoe_recyler)
    AutoLoadRecylerView idGaoeRecyler;

    private LinearLayoutManager layoutManager;
    private XianshiRenwu xianshiRenwu;
    private Xianshi_RenwuAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaoe);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView()
    {
        toolbar1.setTitle("高额任务");
        setSupportActionBar(toolbar1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar1.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        layoutManager = new LinearLayoutManager(this);
        idGaoeRecyler.setLayoutManager(layoutManager);
        idGaoeRecyler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        idGaoeRecyler.setLoadMoreListener(this);
        //adapter = new Xianshi_RenwuAdapter(xianshiRenwu, this, "", "");
        //idGaoeRecyler.setAdapter(adapter);
    }

    @Override
    public void initData()
    {
//        adapter.setOnItemClickListener(new Xianshi_RenwuAdapter.OnItemClickListener()
//        {
//            @Override
//            public void onItemClick(View view, int position)
//            {
//                Intent intent = new Intent();
//                intent.setClass(GaoeRenwuActivity.this,RenwuDetailActivity.class);
//                startActivity(intent);
//            }
//
//            @Override
//            public void onItemLongClick(View view, int position)
//            {
//
//            }
//        });

    }

    @Override
    public void onLoadMore()
    {

    }
}
