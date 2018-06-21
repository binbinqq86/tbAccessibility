package example.tb.com.tbaccessibility;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CaptureService extends AccessibilityService {
    public static String c = "";
    private OverFloatView floatView;
    private String packageName;
    private static final String TAG = "CaptureService";
    private boolean flag = false;
    
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void onAccessibilityEvent(AccessibilityEvent event) {
//		AccessibilityNodeInfo nodeInfo1 = getRootInActiveWindow();
        AccessibilityNodeInfo nodeInfo = event.getSource();
//        recycle(nodeInfo);
        if ((nodeInfo != null) && (nodeInfo.getPackageName() != null)) {
            packageName = nodeInfo.getPackageName().toString();
//            Log.e(TAG,packageName + "$$$$$$$$$packageName");
            if (packageName.equals("com.jcgroup.ease")) {
                //iv_myatte_btn_bg
                //jcer_myatte_history
                //秒针走动会引起页面元素变化，触发此方法，但只是变动的view，并非所有
                List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(packageName + ":id/iv_myatte_btn_bg");
                long time = System.currentTimeMillis();
                String str = new SimpleDateFormat("HH:mm:ss").format(new Date(time));
                Log.e(TAG, "currTime: " + str + "###list.size===" + list.size() + "###flag:=== " + flag);
                for (AccessibilityNodeInfo info : list) {
                    int hour = Integer.parseInt(str.split(":")[0]);
                    int min = Integer.parseInt(str.split(":")[1]);
                    int sec = Integer.parseInt(str.split(":")[2]);
                    Log.e(TAG, "flag:=== " + flag + "###list.size===" + list.size());
                    if (hour == MainActivity.hour
                            && min == MainActivity.min
                            && sec >= MainActivity.sec
                            && !flag) {
                        flag = true;
                        Log.e(TAG, "=========点击我了，停止服务==========");
                        info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        try {
                            FileOutputStream outputStream=new FileOutputStream(new File(Environment.getExternalStorageDirectory(),"tb123.txt"));
                            outputStream.write(str.getBytes());
                            outputStream.flush();
                            outputStream.close();
                            Log.e(TAG, "\n=========文件保存成功==========");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            MainActivityKt.closeSys();
                        }
                    }
                    recycle(info);
                    return;
                }
            }
            recycle(nodeInfo);
        }
        
        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
//			AccessibilityNodeInfo info = event.getSource();  
//			info.findAccessibilityNodeInfosByText("");
//			info.findAccessibilityNodeInfosByViewId("");
//			recycle(info);
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                break;
            default:
                break;
        }
    }
    
    @SuppressLint("NewApi")
    public void recycle(AccessibilityNodeInfo info) {
        if (info == null) {
            return;
        }
        if (info.getChildCount() == 0) {
            Log.e(TAG, "child widget----------------------------" + info.getClassName());
            Log.e(TAG, "showDialog:" + info.canOpenPopup());
            Log.e(TAG, "Text：" + info.getText());
            Log.e(TAG, "windowId:" + info.getWindowId());
            Log.e(TAG, "viewId:" + info.getViewIdResourceName());
        } else {
            for (int i = 0; i < info.getChildCount(); i++) {
                if (info.getChild(i) != null) {
                    recycle(info.getChild(i));
                }
            }
        }
    }
    
    public void onCreate() {
        super.onCreate();
        floatView = new OverFloatView(this, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //toast在小米系统显示不了（需要自己重写toast控件），其他系统OK
                //参考：http://blog.csdn.net/u012846783/article/details/50204687
                Toast.makeText(CaptureService.this, "hello world", Toast.LENGTH_SHORT).show();
                Log.e(TAG,"=========点击我了==========");
                startActivity(new Intent(CaptureService.this, DialogActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
    }
    
    public void onDestroy() {
        super.onDestroy();
        if (floatView != null) {
            floatView.hideFloatView();
            floatView = null;
        }
    }
    
    public void onInterrupt() {
    }
    
    @SuppressLint("NewApi")
    protected void onServiceConnected() {
        AccessibilityServiceInfo serviceInfo = getServiceInfo();
        serviceInfo.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS;
        c = serviceInfo.getId();
        setServiceInfo(serviceInfo);
        super.onServiceConnected();
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        return super.onStartCommand(intent, flags, startId);
    }
}