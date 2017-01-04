package com.song.eleshoppingcart.base;

import android.content.Context;
import android.graphics.Point;
import android.os.Environment;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.io.Closeable;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.logging.Logger;


/**
 * Created by LiCola on  2015/12/05  14:12
 */
public final class Utils {
    /**
     * 修复输入法管理器泄漏
     *
     * @param context
     */
    public static void fixInputMethodManagerLeak(Context context) {
        if (context == null) {
            return;
        }
        try {
            // 对 mCurRootView mServedView mNextServedView 进行置空...
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm == null) {
                return;
            }// author:sodino mail:sodino@qq.com

            Object obj_get = null;
            Field f_mCurRootView = imm.getClass().getDeclaredField("mCurRootView");
            Field f_mServedView = imm.getClass().getDeclaredField("mServedView");
            Field f_mNextServedView = imm.getClass().getDeclaredField("mNextServedView");

            if (f_mCurRootView.isAccessible() == false) {
                f_mCurRootView.setAccessible(true);
            }
            obj_get = f_mCurRootView.get(imm);
            if (obj_get != null) { // 不为null则置为空
                f_mCurRootView.set(imm, null);
            }

            if (f_mServedView.isAccessible() == false) {
                f_mServedView.setAccessible(true);
            }
            obj_get = f_mServedView.get(imm);
            if (obj_get != null) { // 不为null则置为空
                f_mServedView.set(imm, null);
            }

            if (f_mNextServedView.isAccessible() == false) {
                f_mNextServedView.setAccessible(true);
            }
            obj_get = f_mNextServedView.get(imm);
            if (obj_get != null) { // 不为null则置为空
                f_mNextServedView.set(imm, null);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * 检查对象非空
     *
     * @param object
     * @param message
     * @param <T>
     * @return
     */
    public static <T> T checkNotNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

    /**
     * 检查是否在主线程 关键检查 弹出异常
     */
    public static void checkUiThread() {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw new IllegalStateException(
                    "Must be called from the main thread. Was: " + Thread.currentThread());
        }
    }

    /**
     * 检查当前是否在主线程 返回true为主线程
     *
     * @return
     */
    public static boolean checkUiThreadBoolean() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    /**
     * 检查输入是否为空
     *
     * @param values String[]
     * @return Returns true if the values of this string[] is empty ro where are empty
     */
    public static int checkStringIsEmpty(String... values) {
        int location = -1;
        if (values.length == 1) {
            return values[0].isEmpty() ? 0 : -1;
        }
        for (int i = 0, size = values.length; i < size; i++) {
            if (values[i].isEmpty()) {
                return i;
            }
        }
        return location;
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (IOException ignored) {
        }
    }

    /**
     * 计算宽高比
     *
     * @param width
     * @param height
     * @return
     */
    public static float getAspectRatio(int width, int height) {
        float ratio = (float) width / (float) height;
        //宽高比<0.7 表示长图 需要截断处理
        if (ratio < 0.7) {
            return 0.7f;
        }
        //// TODO: 2016/5/11 0011 ratio>3会导致图片不能显示
        return ratio;
    }


    /**
     * 检查图片类型是否为 git
     *
     * @param type
     * @return
     */
    public static boolean checkIsGif(String type) {
        if (type == null || type.isEmpty()) {
            return false;
        }

        if (type.contains("gif") || type.contains("GIF")) {
            return true;
        }
        return false;
    }

    public static String getPinsType(String type) {
        if (type == null || type.isEmpty()) {
            return ".jpeg";
        }

        if (type.contains("jpeg")) {
            return ".jpeg";
        } else if (type.contains("png")) {
            return ".png";
        } else if (type.contains("gif")) {
            return ".gif";
        }

        return ".jpeg";
    }

    /**
     * 根据手机的分辨率从dp转px
     *
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context,float dpValue) {
//        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * getDensity(context) + 0.5f);
    }

    public static int px2dp(Context context,float pxValue) {
//        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / getDensity(context) + 0.5f);
    }

    public static float getDensity(Context context){
        return context.getResources().getDisplayMetrics().density;
    }

    public static int getScreenWidth(Context context) {
        return getPoint(context).x;
    }

    public static int getScreenHeight(Context context) {
        return getPoint(context).y;
    }

    @NonNull
    private static Point getPoint(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return point;
    }

    /**
     * Returns true if {@code annotations} contains an instance of {@code cls}.
     */
    public static boolean isAnnotationPresent(Annotation[] annotations,
                                              Class<? extends Annotation> cls) {
        for (Annotation annotation : annotations) {
            if (cls.isInstance(annotation)) {
                return true;
            }
        }
        return false;
    }



    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static int TextView2Integer(TextView textView) {
        int i = 0;
        try {
            String text = textView.getText().toString();

            if (!TextUtils.isEmpty(text)){
                if (text.startsWith("￥"))
                    text = text.substring(1,text.length());
                i =Integer.valueOf(text);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i ;
    }
}
