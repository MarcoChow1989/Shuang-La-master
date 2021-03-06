package com.qx.www.shuang_la_master.utils;

import android.util.Log;

/**
 * Created by pc on 2016/7/26.
 */
public class ButtonUtil
{
    private static long lastClickTime = 0;
    private static long DIFF = 3000;
    private static int lastButtonId = -1;
    /**
     * 判断两次点击的间隔，如果小于1000，则认为是多次无效点击
     * @return
     */
    public static boolean isFastDoubleClick()
    {
        return isFastDoubleClick(-1,DIFF);
    }
    /**
     * 判断两次点击的间隔，如果小于1000，则认为是多次无效点击
     * @return
     */
    public static boolean isFastDoubleClick(int buttonId)
    {
        return isFastDoubleClick(buttonId,DIFF);
    }

    /**
     * 判断两次点击的间隔，如果小于diff，则认为是多次无效点击
     * @param diff
     * @return
     */
    public static boolean isFastDoubleClick(int buttonId,long diff)
    {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        Log.v("xxxx", "lastButtonId = "+lastButtonId+"  buttonId = "+buttonId);
        if (lastButtonId == buttonId && lastClickTime>0 && timeD < diff)
        {
            Log.v("isFastDoubleClick", "短时间内按钮多次触发");
            return true;
        }

        lastClickTime = time;
        lastButtonId = buttonId;
        return false;
    }
}
