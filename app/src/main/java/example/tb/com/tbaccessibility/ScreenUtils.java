package example.tb.com.tbaccessibility;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Field;

public class ScreenUtils {
    private ScreenUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }
    
    public static int dip2px(Context context, float mFloat) {
        return (int) (0.5F + mFloat
                * context.getResources().getDisplayMetrics().density);
    }
    
    public static int getScreenHeight(Context paramContext) {
        WindowManager localWindowManager = (WindowManager) paramContext
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        localWindowManager.getDefaultDisplay().getMetrics(localDisplayMetrics);
        return localDisplayMetrics.heightPixels;
    }
    
    public static int getScreenWidth(Context paramContext) {
        WindowManager localWindowManager = (WindowManager) paramContext
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        localWindowManager.getDefaultDisplay().getMetrics(localDisplayMetrics);
        return localDisplayMetrics.widthPixels;
    }
    
    public static int getStatusBarHeight(Context paramContext) {
        try {
            Class localClass = Class.forName("com.android.internal.R$dimen");
            Object localObject2 = localClass.newInstance();
            int j = Integer.parseInt(localClass.getField("status_bar_height")
                    .get(localObject2).toString());
            return paramContext.getResources().getDimensionPixelSize(j);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
    
    public static int px2dip(Context paramContext, float paramFloat) {
        return (int) (0.5F + paramFloat
                / paramContext.getResources().getDisplayMetrics().density);
    }
}