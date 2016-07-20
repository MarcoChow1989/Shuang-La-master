package com.qx.www.shuang_la_master.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.qx.www.shuang_la_master.BaseActivity;
import com.qx.www.shuang_la_master.R;
import com.qx.www.shuang_la_master.domain.Tixian;
import com.qx.www.shuang_la_master.utils.AppUtils;
import com.qx.www.shuang_la_master.utils.Constants;
import com.qx.www.shuang_la_master.utils.VolleyInterface;
import com.qx.www.shuang_la_master.utils.VolleyRequest;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WeiXinTiXianActivity extends BaseActivity
{

    @Bind(R.id.toolbar1)
    Toolbar toolbar1;
    @Bind(R.id.id_weixintixian_edittext)
    EditText idWeixintixianEdittext;
    @Bind(R.id.id_weixintixian_shiyuan)
    TextView idWeixintixianShiyuan;
    @Bind(R.id.id_weixintixian_sanshiyuan)
    TextView idWeixintixianSanshiyuan;
    @Bind(R.id.id_weixintixian_wushiyuan)
    TextView idWeixintixianWushiyuan;
    @Bind(R.id.id_weixintixian_yibaiyuan)
    TextView idWeixintixianYibaiyuan;
    @Bind(R.id.id_weixintixian_radiogroup)
    RadioGroup idWeixintixianRadiogroup;
    @Bind(R.id.id_weixintixian_zhichu)
    TextView idWeixintixianZhichu;
    @Bind(R.id.id_weixintixian_yue)
    TextView idWeixintixianYue;
    @Bind(R.id.id_weixintixian_bt)
    Button idWeixintixianBt;
    @Bind(R.id.id_weixintixian_rb1)
    RadioButton idWeixintixianRb1;
    @Bind(R.id.id_weixintixian_rb2)
    RadioButton idWeixintixianRb2;
    @Bind(R.id.id_weixintixian_rb3)
    RadioButton idWeixintixianRb3;
    @Bind(R.id.id_weixintixian_rb4)
    RadioButton idWeixintixianRb4;

    String money;
    SharedPreferences sp;
    private String uid;
    private String pid;
    private String zh;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wei_xin_ti_xian);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView()
    {
        toolbar1.setTitle("微信提现");
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

        sp = getSharedPreferences("LoginInfo", MODE_PRIVATE);
        uid = String.valueOf(sp.getInt("uid", 0));


        idWeixintixianRb1.setChecked(true);
        idWeixintixianRb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    idWeixintixianZhichu.setText("10");
                    pid = "1";
                    idWeixintixianRb2.setChecked(false);
                    idWeixintixianRb3.setChecked(false);
                    idWeixintixianRb4.setChecked(false);
                }
            }
        });

        idWeixintixianRb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    idWeixintixianZhichu.setText("29");
                    pid = "2";
                    idWeixintixianRb1.setChecked(false);
                    idWeixintixianRb3.setChecked(false);
                    idWeixintixianRb4.setChecked(false);
                }
            }
        });

        idWeixintixianRb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    idWeixintixianZhichu.setText("48");
                    pid = "3";
                    idWeixintixianRb2.setChecked(false);
                    idWeixintixianRb1.setChecked(false);
                    idWeixintixianRb4.setChecked(false);
                }
            }
        });

        idWeixintixianRb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    idWeixintixianZhichu.setText("95");
                    pid = "4";
                    idWeixintixianRb2.setChecked(false);
                    idWeixintixianRb3.setChecked(false);
                    idWeixintixianRb1.setChecked(false);
                }
            }
        });
    }

    @Override
    public void initData()
    {
        money = getIntent().getStringExtra("money");
        idWeixintixianYue.setText(money);
    }

    @OnClick(R.id.id_weixintixian_bt)
    public void onClick()
    {
        if (!"".equals(idWeixintixianEdittext.getText().toString().trim()))
        {
            name = idWeixintixianEdittext.getText().toString().trim();
            GetTixianData();
        } else
        {
            Toast.makeText(this, "用户姓名为空!", Toast.LENGTH_LONG).show();
        }
    }

    private void GetTixianData()
    {
        String url = Constants.BaseUrl + "/tixian/index";
        // TODO: 2016/7/20 账号
        String tokenBefroeMD5_TiXian = GetThePhoneInfo() + Constants.KEY + "/" + Constants.TIXIANZHONGXI_Url;
        String token_TiXian = AppUtils.getMd5Value(AppUtils.getMd5Value(tokenBefroeMD5_TiXian).substring(AppUtils.getMd5Value(tokenBefroeMD5_TiXian).length() - 4) +
                AppUtils.getMd5Value(tokenBefroeMD5_TiXian).replace(AppUtils.getMd5Value(tokenBefroeMD5_TiXian).substring(AppUtils.getMd5Value(tokenBefroeMD5_TiXian).length() - 4), ""));

        System.out.println("token_TiXian------------------------" + token_TiXian);

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("pid", pid);
        params.put("type", "2");
        params.put("zh", zh);
        params.put("name", name);
        params.put("token", token_TiXian);

        VolleyRequest.RequestPost(this, url, "TiXian", params, new VolleyInterface(this,
                VolleyInterface.mSuccessListener, VolleyInterface.mErrorListener)
        {
            @Override
            public void onMySuccess(String result)
            {
                System.out.println("TiXian--------------------:" + result);
                Gson gson = new Gson();

                Tixian tixian = gson.fromJson(result, Tixian.class);

                System.out.println("-----------money------------:" + tixian.getInfos().getMoney());
                money = AppUtils.numZhuanHuan(tixian.getInfos().getMoney());
            }

            @Override
            public void onMyError(VolleyError error)
            {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String GetThePhoneInfo()
    {
        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String szImei = TelephonyMgr.getDeviceId();
        return szImei;
    }
}