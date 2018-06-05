package example.tb.com.tbaccessibility;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

/**
 * 桌面悬浮窗
 *
 * @author tianbin
 * <p>
 * Created on 2016-1-21 下午4:03:36
 */
public class OverFloatView implements OnTouchListener {
    private Context context;
    private View floatView;
    //起始点击坐标——相对控件左上角
    private float mTouchX;
    private float mTouchY;
    //起始点击坐标——相对屏幕左上角
    private float mTouchRawX;
    private float mTouchRawY;
    private static final int minDistance = 30;//移动的最小距离
    private View.OnClickListener onClickListener;
    private int statusBarHeight;
    private WindowManager windowManager;
    private LayoutParams wmParams;
    
    public OverFloatView(Context paramContext, View.OnClickListener onClickListener) {
        this.context = paramContext;
        this.onClickListener = onClickListener;
        this.windowManager = ((WindowManager) paramContext.getSystemService(Context.WINDOW_SERVICE));
        createFloatView();
    }
    
    private void createFloatView() {
        wmParams = new LayoutParams();
        wmParams.type = LayoutParams.TYPE_TOAST;// 设置window type(toast不需要权限)
        wmParams.format = PixelFormat.RGBA_8888;// 设置图片格式，效果为背景透明
        /*
         * 注意，flag的值可以为：
         * LayoutParams.FLAG_NOT_TOUCH_MODAL 不影响后面的事件
         * LayoutParams.FLAG_NOT_FOCUSABLE 不可聚焦
         * LayoutParams.FLAG_NOT_TOUCHABLE 不可触摸
         */
        wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
        // 调整悬浮窗口至左上角，便于调整坐标
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        wmParams.width = LayoutParams.WRAP_CONTENT;
        wmParams.height = LayoutParams.WRAP_CONTENT;
        wmParams.x = ScreenUtils.getScreenWidth(context);
        wmParams.y = (-100 + ScreenUtils.getScreenHeight(context) / 2);
        statusBarHeight = ScreenUtils.getStatusBarHeight(context);
        floatView = LayoutInflater.from(context).inflate(R.layout.view_capture, null);
        this.floatView.setOnTouchListener(this);
//		this.floatView.setOnClickListener(onClickListener);
        windowManager.addView(floatView, wmParams);
    }
    
    private void updateFloatView(float float1, float float2) {
        this.wmParams.x = (int) (float1 - mTouchX);
        this.wmParams.y = (int) (float2 - mTouchY);
        this.windowManager.updateViewLayout(floatView, wmParams);
    }
    
    public void hideFloatView() {
        if (this.floatView != null)
            this.floatView.setVisibility(View.GONE);
    }
    
    public void showFloatView() {
        if (this.floatView != null)
            this.floatView.setVisibility(View.VISIBLE);
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // 捕获手指触摸按下动作
                // 获取相对View的坐标，即以此View左上角为原点
                mTouchX = event.getX();
                mTouchY = event.getY();
                mTouchRawX = event.getRawX();
                mTouchRawY = event.getRawY() - statusBarHeight;
                break;
            case MotionEvent.ACTION_MOVE: // 捕获手指触摸移动动作
                updateFloatView(event.getRawX(), event.getRawY() - statusBarHeight);
                break;
            case MotionEvent.ACTION_UP: // 捕获手指触摸离开动作
                if (Math.abs(event.getRawX() - mTouchRawX) < ScreenUtils.dip2px(context, minDistance) && Math.abs(event.getRawY() - mTouchRawY) < ScreenUtils.dip2px(context, minDistance)) {
                    onClickListener.onClick(v);
                }
                break;
        }
        return true;
    }
}