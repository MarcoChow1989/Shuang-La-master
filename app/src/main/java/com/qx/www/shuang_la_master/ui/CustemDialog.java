package com.qx.www.shuang_la_master.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qx.www.shuang_la_master.CountDownTimerListener;
import com.qx.www.shuang_la_master.R;
import com.qx.www.shuang_la_master.service.CountDownTimerService;
import com.qx.www.shuang_la_master.utils.AppUtils;
import com.qx.www.shuang_la_master.utils.ButtonUtil;
import com.qx.www.shuang_la_master.utils.Constants;
import com.qx.www.shuang_la_master.utils.CountDownTimerUtil;
import com.qx.www.shuang_la_master.utils.VolleyInterface;
import com.qx.www.shuang_la_master.utils.VolleyRequest;
import com.rey.material.widget.ProgressView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pc on 2016/6/29.
 */
public class CustemDialog extends AlertDialog.Builder
{
    ImageView mDialogIcon;
    TextView mDialogTitle;
    TextView mDialogSize;
    TextView mDialogMsg1, mDialogMsg2, mDialogMsg3;

    Button mBtShenhe, mBtCancel;
    MyProgressBar myProgressBar;

    private boolean mSdCardIsExist = false;
    private String mPackageName;
    File f;
    String filename;
    private Context context;
    private String title;
    private String reward;
    private String size;
    private String pic;
    private String url;
    private String semi;
    private String uid;
    private String tid;
    private boolean isShenHe = false;

    private CountDownTimerService countDownTimerService;
    private long timer_unit = 1000;
    private long service_distination_total = timer_unit * 350;
    private PackageManager pm;
    AlertDialog.Builder alertDialog;
    Dialog mDialog;
    boolean isFinish = false;

    public CustemDialog(Context context, String title, String pic, String size, String reward, String url, String semi, String uid, String tid, String packet)
    {

        super(context);
        this.context = context;
        this.title = title;
        this.reward = reward;
        this.pic = pic;
        this.size = size;
        this.semi = semi;
        this.uid = uid;
        this.tid = tid;
        this.mPackageName = packet;
        this.url = url.replace("\\/", "/");

        pm = context.getPackageManager();//获得包管理器

        //计时器
        countDownTimerService = CountDownTimerService.getInstance(new MyCountDownLisener()
                , service_distination_total);
        initServiceCountDownTimerStatus();
    }

    private class MyCountDownLisener implements CountDownTimerListener
    {
        @Override
        public void onChange()
        {
            ArrayList<HashMap<String, Object>> list = AppUtils.LoadList(context, pm);
            for (int i = 0; i < list.size(); i++)
            {
                System.out.println("---------runlist-----------" + list.get(i).get("name"));
                if (list.get(i).get("name").equals(title))
                {
                    isFinish = true;
                }
            }

            if ((isFinish == true && countDownTimerService.getCountingTime() == 0 && AppUtils.isAvilible(context, mPackageName)) || countDownTimerService.getCountingTime() == 0 &&
                    AppUtils.isAvilible(context, "com.hb.qx"))
            {
                isShenHe = true;
                SendFinishTask();
            }
            System.out.println(countDownTimerService.getCountingTime());
        }
    }


    public void showDialog()
    {
        alertDialog = new AlertDialog.Builder(context, R.style.selectorDialog);
        alertDialog.setCancelable(false);
        mDialog = alertDialog.create();
        mDialog.show();
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.dialog_renwu);

        mDialogIcon = (ImageView) window.findViewById(R.id.id_dialog_icon);
        mDialogTitle = (TextView) window.findViewById(R.id.id_dialog_title);
        mDialogSize = (TextView) window.findViewById(R.id.id_dialog_size);
        mDialogMsg1 = (TextView) window.findViewById(R.id.id_dialog_msg_1);
        mDialogMsg2 = (TextView) window.findViewById(R.id.id_dialog_msg_2);
        mDialogMsg3 = (TextView) window.findViewById(R.id.id_dialog_msg_3);
        mBtShenhe = (Button) window.findViewById(R.id.id_dialog_shenhe);
        mBtCancel = (Button) window.findViewById(R.id.id_dialog_cancel);
        myProgressBar = (MyProgressBar) window.findViewById(R.id.id_renwu_progressbar);

        if (!getSDPath().equals(""))
        {
            if (fileIsExists(mPackageName) == true)
            {
                myProgressBar.setProgress(100);
            }
        }

        myProgressBar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (!getSDPath().equals("") && downloading == 0)
                {
                    if (fileIsExists(mPackageName) == true)
                    {
                        Uri uri = Uri.fromFile(f);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, "application/vnd.android.package-archive");
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(intent);
                        if (countDownTimerService.getTimerStatus() == CountDownTimerUtil.PREPARE)
                        {
                            countDownTimerService.startCountDown();
                        }
                    } else if (AppUtils.isAvilible(context, mPackageName))
                    {
                        Intent LaunchIntent = context.getPackageManager().getLaunchIntentForPackage(mPackageName);
                        LaunchIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(LaunchIntent);
                        //开启计时器服务
                        if (countDownTimerService.getTimerStatus() == CountDownTimerUtil.PREPARE)
                        {
                            countDownTimerService.startCountDown();
                        }

                    } else
                    {
                        DownLoadApk();
                        if (countDownTimerService.getTimerStatus() == CountDownTimerUtil.PREPARE)
                        {
                            countDownTimerService.startCountDown();
                        }
                    }
                }
            }
        });

        mBtShenhe.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                System.out.println("----countDownTimerService.getCountingTime()---" + countDownTimerService.getCountingTime());
                if (countDownTimerService.getCountingTime() == service_distination_total && isShenHe == true)
                {
                    SendFinishTask();
                } else
                {
                    Toast.makeText(context, "任务还未完成...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBtCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CanelTask();
            }
        });

        //mDialogIcon.setImageResource(pic);
        Glide.with(context)
                .load(pic)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mDialogIcon);

        mDialogTitle.setText(title);
        mDialogSize.setText(size + "M");
    }

    private void CanelTask()
    {
        String url = Constants.BaseUrl + "/site/undoTask";
        String tokenBefroeMD5_UbdoTask = semi + Constants.KEY + "/" + Constants.UNDOTASK_Url;
        String token_UbdoTask = AppUtils.getMd5Value(AppUtils.getMd5Value(tokenBefroeMD5_UbdoTask).substring(AppUtils.getMd5Value(tokenBefroeMD5_UbdoTask).length() - 4) +
                AppUtils.getMd5Value(tokenBefroeMD5_UbdoTask).replace(AppUtils.getMd5Value(tokenBefroeMD5_UbdoTask).substring(AppUtils.getMd5Value(tokenBefroeMD5_UbdoTask).length() - 4), ""));

        System.out.println("token_UbdoTask------------------------" + token_UbdoTask);

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("tid", tid);
        params.put("token", token_UbdoTask);

        VolleyRequest.RequestPost(context, url, "undoTask", params, new VolleyInterface(context,
                VolleyInterface.mSuccessListener, VolleyInterface.mErrorListener)
        {
            @Override
            public void onMySuccess(String result)
            {
                System.out.println("undoTask--------------------:" + result);
                try
                {
                    JSONObject js = new JSONObject(result);
                    String status = js.getString("status");

                    if (status.equals("ok"))
                    {
                        mDialog.dismiss();
                        if (countDownTimerService.getTimerStatus() == CountDownTimerUtil.START)
                        {
                            countDownTimerService.stopCountDown();
                        }
                        Toast.makeText(context, "任务已经放弃!", Toast.LENGTH_SHORT).show();
                    }

                    System.out.println("status----------------------:" + status);

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

    private void SendFinishTask()
    {
        String url = Constants.BaseUrl + "/site/finishTask";
        String tokenBefroeMD5_FinishTask = semi + Constants.KEY + "/" + Constants.FINISHTASK_Url;
        String token_FinishTask = AppUtils.getMd5Value(AppUtils.getMd5Value(tokenBefroeMD5_FinishTask).substring(AppUtils.getMd5Value(tokenBefroeMD5_FinishTask).length() - 4) +
                AppUtils.getMd5Value(tokenBefroeMD5_FinishTask).replace(AppUtils.getMd5Value(tokenBefroeMD5_FinishTask).substring(AppUtils.getMd5Value(tokenBefroeMD5_FinishTask).length() - 4), ""));

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("tid", tid);
        params.put("packet", mPackageName);
        params.put("token", token_FinishTask);

        VolleyRequest.RequestPost(context, url, "finishtask", params, new VolleyInterface(context,
                VolleyInterface.mSuccessListener, VolleyInterface.mErrorListener)
        {
            @Override
            public void onMySuccess(String result)
            {
                System.out.println("finish--------------------:" + result);
                try
                {
                    JSONObject js = new JSONObject(result);
                    String s = js.getString("status");

                    if (s.equals("ok"))
                    {
                        Toast.makeText(context, "任务已经完成!", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
                mDialog.dismiss();
            }

            @Override
            public void onMyError(VolleyError error)
            {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static final int TIMEOUT = 10 * 1000;// 超时
    private File filedir;
    private File downFile;
    private static final int DOWN_OK = 1;
    private static final int DOWN_ERROR = 0;
    int totalSize;// 文件总大小

    private float downloading;
    /**
     * 使用Handler更新UI界面信息
     */
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {

            System.out.println("------downloadCount-----" + msg.getData().getFloat("size") + "------------totalSize-------------" + totalSize);

            downloading = msg.getData().getFloat("size");

            float num = msg.getData().getFloat("size") / totalSize * 100;

            System.out.println("--------------num------------" + num);
            String temp = String.valueOf(num).substring(0, String.valueOf(num).indexOf("."));

            System.out.println("temp---------------------:" + temp);
            myProgressBar.setProgress(Integer.parseInt(temp));
        }
    };


    public void DownLoadApk()
    {
        // 判断文件夹是否存在
        String filepath = getSDPath() + "/shuangla/";
        filedir = new File(filepath);
        if (!filedir.exists())
        {
            filedir.mkdir();
        }
        // 判断文件是否存在
        filename = getSDPath() + "/shuangla/" + mPackageName + "_shangla.apk";
        downFile = new File(filename);
        if (!downFile.exists())
        {
            try
            {
                downFile.createNewFile();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        createThread();
    }

    public String getSDPath()
    {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
            mSdCardIsExist = true;
        }
        return sdDir.toString();
    }

    public void createThread()
    {
        /***
         * 更新UI
         */
        final Handler handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                switch (msg.what)
                {
                    case DOWN_OK:
                        // 下载完成，点击安装
                        Uri uri = Uri.fromFile(downFile);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, "application/vnd.android.package-archive");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        countDownTimerService.startCountDown();
                        break;
                    case DOWN_ERROR:
                        break;
                    default:
                        break;
                }
            }
        };

        final Message message = new Message();
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    long downloadSize = downloadUpdateFile(url, downFile.toString());
                    if (downloadSize > 0)
                    {
                        // 下载成功
                        message.what = DOWN_OK;
                        handler.sendMessage(message);
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                    message.what = DOWN_ERROR;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    /***
     * 下载文件
     *
     * @return
     * @throws MalformedURLException
     */
    public long downloadUpdateFile(String down_url, String file) throws Exception
    {
        int down_step = 5;// 提示step
        int downloadCount = 0;// 已经下载好的大小
        int updateCount = 0;// 已经上传的文件大小
        InputStream inputStream;
        OutputStream outputStream;

        System.out.println("down_url------------------------:" + down_url);

        URL url = new URL(down_url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setConnectTimeout(TIMEOUT);
        httpURLConnection.setReadTimeout(TIMEOUT);
        // 获取下载文件的size
        totalSize = httpURLConnection.getContentLength();
        if (httpURLConnection.getResponseCode() == 404)
        {
            throw new Exception("fail!");
        }
        inputStream = httpURLConnection.getInputStream();
        outputStream = new FileOutputStream(file, false);// 文件存在则覆盖掉
        byte buffer[] = new byte[1024];
        int readsize = 0;
        while ((readsize = inputStream.read(buffer)) != -1)
        {
            outputStream.write(buffer, 0, readsize);
            downloadCount += readsize;// 时时获取下载到的大小

            Message msg = new Message();
            msg.getData().putFloat("size", downloadCount);

            mHandler.sendMessage(msg);

        }
        if (httpURLConnection != null)
        {
            httpURLConnection.disconnect();
        }
        inputStream.close();
        outputStream.close();
        return downloadCount;
    }

    public boolean fileIsExists(String packageName)
    {
        try
        {
            f = new File(getSDPath() + "/shuangla/" + packageName + "_shangla.apk");
            if (!f.exists())
            {
                return false;
            }

        } catch (Exception e)
        {
            // TODO: handle exception
            return false;
        }
        return true;
    }

    /**
     * init countdowntimer buttons status for servce
     */
    private void initServiceCountDownTimerStatus()
    {
        switch (countDownTimerService.getTimerStatus())
        {
            case CountDownTimerUtil.PREPARE:
                break;
            case CountDownTimerUtil.START:
                break;
            case CountDownTimerUtil.PASUSE:
                break;
        }

        System.out.println("timer---------------:" + formateTimer(countDownTimerService.getCountingTime()));
    }


    private String formateTimer(long time)
    {
        String str = "00:00:00";
        int hour = 0;
        if (time >= 1000 * 3600)
        {
            hour = (int) (time / (1000 * 3600));
            time -= hour * 1000 * 3600;
        }
        int minute = 0;
        if (time >= 1000 * 60)
        {
            minute = (int) (time / (1000 * 60));
            time -= minute * 1000 * 60;
        }
        int sec = (int) (time / 1000);
        str = formateNumber(hour) + ":" + formateNumber(minute) + ":" + formateNumber(sec);
        return str;
    }

    private String formateNumber(int time)
    {
        return String.format("%02d", time);
    }


}
