package ian.practice.findmaxmumbymutithreads;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MaxmumActivity extends Activity {

	int threadNumber;
	CountDownLatch startSignal = new CountDownLatch(1);
    CountDownLatch doneSignal;
    ExecutorService executor;
    int[] largeArray = new int[10000];
    int[] result;
    long startTime;
    long endTime;
    long duration;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maxmum);
        final EditText threadNum = (EditText) findViewById(R.id.ThreadNum);
        final TextView durationMessage = (TextView) findViewById(R.id.DurationInfo);
        Button startButton = (Button) findViewById(R.id.Start);
        startButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.maxmum, menu);
        return true;
    }
    
}
