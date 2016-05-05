package com.xbx.client.utils.updateversion;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.xbx.client.R;
import com.xbx.client.beans.Version;
import com.xbx.client.ui.activity.MainActivity;
import com.xbx.client.utils.Constant;
import com.xbx.client.utils.SharePrefer;
import com.xbx.client.utils.StringUtil;
import com.xbx.client.utils.Util;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpdateUtil {
    /**
     * 下载失败
     */
    public static final int DOWN_ERROR = 12;
    /**
     * 进度条当前的值
     */
    public static int progressVaue;
    /**
     * apk 文件
     */
    public static File file;
    /**
     * 下载任务
     */
    public static DownLoadTask downLoadTask;
    public static int clientVersionCode;
    public static Context context;
    static boolean update = true;// 是否需要
    static Version version = null;
    static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case -1:
                    // 最已是新版本
                    ToastMsg("已是最新版本");
                    break;
                case 1:
                    if (version != null) {
                        final UpdateVersionDialog updateDialog = new UpdateVersionDialog(context, version);
                        updateDialog.setButtonClickListener(new UpdateVersionDialog.CustomClickListener() {

                            @Override
                            public void cancel() {
                                updateDialog.dismiss();
                            }

                            @Override
                            public void confirm() {
                                updateDialog.dismiss();
                                if (!Util.isNull(version.getPath())) {
                                    downApk();
                                } else {
                                    Toast.makeText(context, "APK位置信息错误", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        updateDialog.show();
                    }
                    break;
                case 2:
                    ToastMsg("连接服务器失败");
                    break;
                case DOWN_ERROR:
                    ToastMsg("下载失败,连接中断");
                    SharePrefer.clear(context);
                    break;
            }
        }
    };

    public UpdateUtil(Context con, Version version) {
        update = true;
        keepon = true;
        context = con;
        this.version = version;
        send = new MySendThread();
        try {
            clientVersionCode = getClientVersion();
        } catch (NameNotFoundException e) {
            Log.v("Tag", "client" + e.getMessage());
        }
        if (canUpdate(clientVersionCode, version.getVersionCode())) {
            handler.sendEmptyMessage(1);
        }
    }

    private boolean canUpdate(int clientVersionCode, String versionCode) {
        if (Util.isNull(versionCode)) {
            return false;
        }
        int vCode = Integer.parseInt(versionCode);
        if (clientVersionCode < vCode) {
            return true;
        }
        return false;
    }

    /**
     * --------关于 下载进度条的--------------------------------------
     */
    static NotificationManager manager;
    static Notification notification;
    static RemoteViews remoteViews;

    /* static Myreciver reciver; */
    public static void initNotifation() {
        manager = (NotificationManager) context
                .getSystemService(context.NOTIFICATION_SERVICE);
        notification = new Notification(android.R.drawable.stat_sys_download,
                "正在下载", System.currentTimeMillis());
        remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.notification_layout);
        notification.contentIntent = PendingIntent.getActivity(context, 100,
                new Intent(context, MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        notification.contentView = remoteViews;
        notification.flags = Notification.FLAG_NO_CLEAR;
        notification.defaults = Notification.FLAG_ONLY_ALERT_ONCE;
        remoteViews.setProgressBar(R.id.noti_progressbar, 100, 0, false);
        currentlength = 0;
        progressVaue = 0;
    }

    static String size = "";
    static int alltotal = 0;
    static boolean first = true;
    static boolean keepon = true;
    public static MySendThread send;
    static int currentlength = 0;

    /*
     * static PendingIntent pintent=PendingIntent.getBroadcast(context, 1, new
     * Intent(), PendingIntent.FLAG_NO_CREATE);
     */
    public static void updateDownPro(final int pro, final String size) {
        if (pro > currentlength) {
            remoteViews.setTextViewText(R.id.noti_textview, "正在下载更新(" + size
                    + "):" + "  " + pro + "%");
            remoteViews.setProgressBar(R.id.noti_progressbar, 100, pro, false);
            manager.notify(100, notification);
        }
        currentlength = pro;
    }

    public class MySendThread extends Thread {
        @Override
        public void run() {
            while (keepon) {
                updateDownPro((int) progressVaue * 100 / alltotal,
                        size.substring(0, size.indexOf(".") + 2) + "M");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * ---------------------------------------------------
     */
    public static void downApk() {
        if (isSDCardAvailable()) {
            file = new File(Constant.APK_PATH);
            Log.i("Tag", Constant.APK_PATH);
        } else {
            String apkpath = context.getFilesDir().getAbsolutePath()
                    + "/TutuUser.apk";
            file = new File(apkpath);
            Log.i("Tag", apkpath);
        }
        initNotifation();
        ToastMsg("开始下载！");
        first = true;
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        downLoadTask = new DownLoadTask(version.getPath(),
                file.getAbsolutePath(), 5);
        if (downLoadTask == null) {
            return;
        }
        int num = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(num * 4);
        service.execute(downLoadTask);
        downLoadTask.setListener(new DownLoadTask.DownlaodListener() {
            @Override
            public void update(int total, int len, int threadid) {
                if (progressVaue > 0 && first) {
                    send.start();
                    first = false;
                }
                alltotal = total;
                progressVaue += len;
                size = (float) total / (1024 * 1024) + "";
            }

            @Override
            public void downLoadFinish(int totalSucess) {
                if (totalSucess == 5) {
                    SharePrefer.clear(context);
                    manager.cancel(100);
                    keepon = false;
                    installApk();
                    CancleDownload();
                } else {
                    Message.obtain(handler, DOWN_ERROR).sendToTarget();
                }
            }

            @Override
            public void downLoadError(int type) {
                keepon = false;
                first = false;
                Message.obtain(handler, DOWN_ERROR).sendToTarget();
            }
        });

    }

    private static void CancleDownload() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
//				cancleDownload();
            }
        }).start();
    }

    public static void UserCancleDownload() {
        AlertDialog.Builder b = new AlertDialog.Builder(context);
        b.setMessage("取消下载？");
        b.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancleYes();
                SharePrefer.saveState(context, false);
                SharePrefer.clear(context);
            }
        });
        b.setNegativeButton("取消", null);
        b.create().show();
    }

    private static void cancleYes() {

        update = false;
        first = false;
        if (downLoadTask != null) {
            downLoadTask.cancel();
            downLoadTask = null;
        }
        manager.cancel(100);
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(2001);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                manager.cancel(100);
            }
        }).start();
    }

    /**
     * 安装Apk
     */
    private static void installApk() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void ToastMsg(String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取当前应用的版本号
     *
     * @return
     * @throws NameNotFoundException
     */
    public static int getClientVersion() throws NameNotFoundException {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageInfo(
                context.getPackageName(), 0);
        return packageInfo.versionCode;
    }

    /**
     * 获得网络连接是否可用
     *
     * @param context
     * @return
     */
    public static boolean hasNetwork(Context context) {
        ConnectivityManager con = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo workinfo = con.getActiveNetworkInfo();
        if (workinfo == null || !workinfo.isAvailable()) {
            Toast.makeText(context, "网络连接失败", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 检测sd卡是否存在
     *
     * @return 不存在 false存在true
     */
    public static boolean isSDCardAvailable() {
        String sdState = Environment.getExternalStorageState();// 获得sd卡的状态
        if (sdState.equals(Environment.MEDIA_MOUNTED)) { // 判断SD卡是否存在
            return true;
        }
        return false;
    }
}
