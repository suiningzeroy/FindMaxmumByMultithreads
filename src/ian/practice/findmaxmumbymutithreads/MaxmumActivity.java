package ian.practice.findmaxmumbymutithreads;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MaxmumActivity extends Activity {

	int LARGEARRAY_LENGTH = 30000;
	int threadNumber;
	CountDownLatch startSignal = new CountDownLatch(1);
	CountDownLatch doneSignal;
	ExecutorService executor;
	int[] largeArray ;
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
		largeArray = initialLargeArray(LARGEARRAY_LENGTH);
		
		Button startButton = (Button) findViewById(R.id.Start);
		startButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(threadNum.getText().toString().trim().equals("")){
					Toast.makeText(MaxmumActivity.this, "please input a Thread Number", Toast.LENGTH_LONG).show();
				}else{
					threadNumber = Integer.parseInt(threadNum.getText().toString());
					doneSignal = new CountDownLatch(threadNumber);
					executor = Executors.newFixedThreadPool(threadNumber);
					result = new int[threadNumber]; 
					
					startTime = System.currentTimeMillis();	 
					int maxmum = FindMaxmumInLargeArray(largeArray);
					endTime = System.currentTimeMillis();
					duration = endTime - startTime;
					durationMessage.setText("there are " + Integer.toString(threadNumber) + " threads ." 
											+ "Maxmum is " + Integer.toString(maxmum) 
											+ " arrayLength is " + Integer.toString(LARGEARRAY_LENGTH) 
											+ ". duration is:" + Long.toString(duration));
				}
				
			}
		});

	}

	private int FindMaxmumInLargeArray(int[] largeArray){
		int maxmum = 0 ;
		int[] range;
		int sectionLength = largeArray.length/threadNumber;
		
		for (int i = 0; i < threadNumber; ++i) {
			range = getRange(i,sectionLength);
			executor.execute(new Thread(new Worker(i,range,startSignal,doneSignal)));
		}
		
		startSignal.countDown();
		
		try {
				doneSignal.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		 maxmum = FindMaxmunInArray(result);

		return maxmum;
	}

	private int[] getRange(int i, int sectionLength){
		int[] range = new int[2];

		range[0] = i*sectionLength;
		if(i != threadNumber-1){
			range[1] = ((i+1)*sectionLength)-1;
		}else{
			range[1] = largeArray.length-1;
		}
		return range;
	}
	
	private int[] initialLargeArray(int n){
		int[] largeArray = new int[n];
		Random rnd = new Random();
		
		for(int i=0; i < n; i++){
			largeArray[i] = rnd.nextInt(30001);
		}
		
		return largeArray;
	}
	
	private int FindMaxmunInArray(int[] inputArray){
		int maxmum = 0;
	
		for(int i=0; i < inputArray.length; i++){
			if(inputArray[i]> maxmum)
				maxmum = inputArray[i];
		}
		return maxmum;
		
	}
	
	private int FindMaxmunInSetionOfArray(int[] sectionRange)
	{
		int maxmum = 0;
		int rangeLength = sectionRange[1]-sectionRange[0];
		
		for(int i=0; i < rangeLength; i++){
			if(largeArray[i]> maxmum)
				maxmum = largeArray[i];
		}		
		return maxmum;
		
	}
	
	
	class Worker implements Runnable {
		private final CountDownLatch doneSignal;
		private final CountDownLatch startSignal;
		private int[] range;
		private int resultIndex = 0;
		private int maxValue;
		
		Worker(int n, int[] range, CountDownLatch startSignal,CountDownLatch doneSignal) {
			this.doneSignal = doneSignal;
			this.startSignal = startSignal;
			this.resultIndex = n;
			this.range = range;
		} 	
		
		@Override
		public void run() {
			try {
				startSignal.await();
			
				maxValue = FindMaxmunInSetionOfArray(range);
				writeMaxmumToResult(maxValue);
				doneSignal.countDown();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		private void writeMaxmumToResult(int maxmum){
			result[resultIndex] = maxmum;
		}
		
	}
}
