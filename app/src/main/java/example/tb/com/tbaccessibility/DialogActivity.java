package example.tb.com.tbaccessibility;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class DialogActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		((TextView)findViewById(R.id.tv)).setText("哈哈哈哈");
	}

}
