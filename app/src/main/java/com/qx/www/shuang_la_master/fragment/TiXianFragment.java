package com.qx.www.shuang_la_master.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.qx.www.shuang_la_master.BaseFragment;
import com.qx.www.shuang_la_master.R;
import com.qx.www.shuang_la_master.adapter.Detail_TixianAdatper;
import com.qx.www.shuang_la_master.common.AutoLoadRecylerView;
import com.qx.www.shuang_la_master.common.DividerItemDecoration;
import com.qx.www.shuang_la_master.domain.Detail;
import com.qx.www.shuang_la_master.utils.AppUtils;
import com.qx.www.shuang_la_master.utils.Constants;
import com.qx.www.shuang_la_master.utils.VolleyInterface;
import com.qx.www.shuang_la_master.utils.VolleyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TiXianFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, AutoLoadRecylerView.loadMoreListener
{
    @Bind(R.id.id_duihuan_autorecy)
    AutoLoadRecylerView idDuihuanAutorecy;
    @Bind(R.id.common_error_txt)
    TextView commonErrorTxt;
    @Bind(R.id.retry_btn)
    Button retryBtn;
    @Bind(R.id.common_error)
    RelativeLayout commonError;

    private Detail_TixianAdatper adapter;
    private LinearLayoutManager layoutManager;

    SharedPreferences sp;
    String uid;
    String tokenBeforeMd5;
    String token;
    private String page = "0";

    @Override
    protected int getLayoutResource()
    {
        return R.layout.fragment_dui_huan;
    }

    @Override
    protected void initData()
    {
        GetTiXianData();
    }

    private void GetTiXianData()
    {
        String url = Constants.BaseUrl + "user/mingxi";
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("page", page);
        params.put("type", "3");
        params.put("token", token);

        VolleyRequest.RequestPost(context, url, "ZStask", params, new VolleyInterface(context,
                VolleyInterface.mSuccessListener, VolleyInterface.mErrorListener)
        {
            @Override
            public void onMySuccess(String result)
            {
                System.out.println("result--------TiXian----------------:" + result);

                try
                {
                    JSONObject jsonObject = new JSONObject(result);
                    String infos = jsonObject.getString("infos");

                    if (!infos.equals("1"))
                    {
                        Gson gson = new Gson();
                        JSONArray jsonArray = jsonObject.getJSONArray("ytx");
                        Detail detail = gson.fromJson(result, Detail.class);
                        if (jsonArray.length() != 0)
                        {
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                String[] ytx = new String[jsonArray.length()];
                                ytx[i] = (String) jsonArray.opt(i);
                                detail.setYtx(ytx);
                            }
                        }
                        if (detail.getStatus().equals("ok"))
                        {
                            System.out.println("detail----RenWu----------------:" + detail.getInfos().get(0).getMoney());
                            adapter = new Detail_TixianAdatper(detail, context);
                            idDuihuanAutorecy.setAdapter(adapter);
                        }
                    }
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyError(VolleyError error)
            {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void initView()
    {
        layoutManager = new LinearLayoutManager(context);
        idDuihuanAutorecy.setLayoutManager(layoutManager);
        idDuihuanAutorecy.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
        idDuihuanAutorecy.setLoadMoreListener(this);

        sp = context.getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        uid = String.valueOf(sp.getInt("uid", 0));

        tokenBeforeMd5 = GetThePhoneInfo() + Constants.KEY + "/" + Constants.DETATALI_Url;
        token = AppUtils.getMd5Value(AppUtils.getMd5Value(tokenBeforeMd5).substring(AppUtils.getMd5Value(tokenBeforeMd5).length() - 4) + AppUtils.getMd5Value(tokenBeforeMd5).replace(AppUtils.getMd5Value(tokenBeforeMd5).substring(AppUtils.getMd5Value(tokenBeforeMd5).length() - 4), ""));

        System.out.println("tokenBeforeMd5-------明细---------------" + tokenBeforeMd5);
        System.out.println("token-----------明细-----------" + token);
        System.out.println("uid-------------" + uid);

//        adapter = new Detail_TixianAdatper(mList, context);
//        idDuihuanAutorecy.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onRefresh()
    {

    }

    @Override
    public void onLoadMore()
    {

    }

    public String GetThePhoneInfo()
    {
        TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String szImei = TelephonyMgr.getDeviceId();
        return szImei;
    }
}
