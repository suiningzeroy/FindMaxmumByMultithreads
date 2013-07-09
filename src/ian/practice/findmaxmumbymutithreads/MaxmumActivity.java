package ian.practice.findmaxmumbymutithreads;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MaxmumActivity extends Activity {

	int threadNumber;
    CountDownLatch doneSignal;
    ExecutorService executor;
    int[] largeArray = new int[10000];
    int[] result;
    int[][] dividedArray;
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
				if(threadNum.getText().toString().trim().equals("")){
					durationMessage.setText("please input a Thread Number !");
					Toast.makeText(MaxmumActivity.this, "please input a Thread Number", Toast.LENGTH_LONG).show();
				}else{
					threadNumber = Integer.parseInt(threadNum.getText().toString());
					doneSignal = new CountDownLatch(threadNumber);
	        		executor = Executors.newFixedThreadPool(threadNumber);
	        		result = new int[threadNumber]; 
	        		startTime = System.currentTimeMillis();
	        		int maxmum = FindMaxmumInLargeArray(largeArray);
	        		endTime = System.currentTimeMillis();
	        		duration = startTime - endTime;
					
					durationMessage.setText("there are " + Integer.toString(threadNumber) + 
							" threads ." + "duration is:" + Long.toString(duration));
				}
				
			}
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.maxmum, menu);
        return true;
    }
    
    private int FindMaxmumInLargeArray(int[] largeArray){
    	int maxmum = 0 ;
    	return maxmum;
    }
    
    
	private int FindMaxmunInArray(int[] inputArray){
		int maxmum = 0;
	
		for(int i=0; i < inputArray.length; i++){
			if(inputArray[i]> maxmum)
				maxmum = inputArray[i];
		}
		return maxmum;
		
	}
    
	class Worker implements Runnable {
    	private final CountDownLatch doneSignal;
    	private int[] input;
    	private int maxValue;
    	
    	Worker(int[] inputArray, CountDownLatch doneSignal) {
    		this.doneSignal = doneSignal;
    		this.input =  inputArray;    		
    	}   	


		@Override
		public void run() {
			maxValue = FindMaxmunInArray(input);
			writeMaxmumToResult(maxValue);
			doneSignal.countDown();
		}
		
		private void writeMaxmumToResult(int value){
		}
    }
}
