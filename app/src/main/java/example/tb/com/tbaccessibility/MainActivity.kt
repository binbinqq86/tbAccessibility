package example.tb.com.tbaccessibility

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.TypedValue
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

fun closeSys() {
    Handler().postDelayed({
        Log.e("MainActivity","======系统关机啦======")
        Runtime.getRuntime().exec(arrayOf("su", "-c", "reboot -p"))
    }, 10 * 1000)
}

class MainActivity : AppCompatActivity() {
    val TAG: String = MainActivity.javaClass.simpleName
    var mFlag: Boolean = false

    companion object {
        const val hour = 16
        const val min = 55
        const val sec = 52
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var tv = findViewById<TextView>(R.id.tv)
        tv.setOnClickListener{
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            //dialog显示隐藏也会触发，输入字符也会触发
//            AlertDialog.Builder(this).setTitle("test").setMessage("hello world").create().show()
        }
        tv.text = "this is a test"
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f)
        Thread {
            run {
                while (!mFlag) {
                    var str = SimpleDateFormat("HH:mm:ss").format(Date(System.currentTimeMillis()))
                    val hour = str.split(":")[0].toInt()
                    val min = str.split(":")[1].toInt()
                    val sec = str.split(":")[2].toInt()
//                    println("currTime:$str")
                    Log.e(TAG, "currTime: $str")
                    if (hour == MainActivity.hour
                            && min == MainActivity.min
                            && sec >= MainActivity.sec
                            && !mFlag) {
                        mFlag = true
                        Log.e(TAG, "=========finish==========")
                        finish()
                    }
                }
            }
        }.start()






        Handler().postDelayed({
            //            Runtime.getRuntime().exec(arrayOf("su", "-c", "shutdown"))
//            Runtime.getRuntime().exec(arrayOf("su","-c","reboot -p"))
//            Runtime.getRuntime().exec(arrayOf("su", "-c", "reboot"))
        }, 3000)
    }

}
