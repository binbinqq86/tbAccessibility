package example.tb.com.tbaccessibility;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CaptureService extends AccessibilityService {
    public static String c = "";
    private OverFloatView floatView;
    private String packageName;
    private static final String TAG = "CaptureService";
    
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
                List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(packageName + ":id/iv_myatte_btn_bg");
                long time = System.currentTimeMillis();
                String str = new SimpleDateFormat("HH:mm:ss").format(new Date(time));
                Log.e(TAG, "currTime: " + str + "###list.size===" + list.size() + "###MainActivity.Companion.getFlag():=== " + MainActivity.Companion.getFlag());
                for (AccessibilityNodeInfo info : list) {
                    int hour = Integer.parseInt(str.split(":")[0]);
                    int min = Integer.parseInt(str.split(":")[1]);
                    int sec = Integer.parseInt(str.split(":")[2]);
                    Log.e(TAG, "MainActivity.Companion.getFlag():=== " + MainActivity.Companion.getFlag() + "###list.size===" + list.size());
                    if (hour == MainActivity.hour
                            && min >= MainActivity.min
                            && sec >= MainActivity.sec
                            && !MainActivity.Companion.getFlag()) {
                        MainActivity.Companion.setFlag(true);
                        Log.e(TAG, "=========点击我了，停止服务==========");
                        info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
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
//        floatView = new OverFloatView(this, new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                //toast在小米系统显示不了（需要自己重写toast控件），其他系统OK
//                //参考：http://blog.csdn.net/u012846783/article/details/50204687
//                Toast.makeText(CaptureService.this, "hello world", Toast.LENGTH_SHORT).show();
//                Log.e(TAG,"=========点击我了==========");
//                startActivity(new Intent(CaptureService.this, DialogActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//            }
//        });
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