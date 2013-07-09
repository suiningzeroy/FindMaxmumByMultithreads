package ian.practice.findmaxmumbymutithreads;

import java.util.Arrays;
import java.util.Random;
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
    int[] largeArray = new int[9];
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
	        		largeArray = initialLargeArray(50);
	        		int maxmum = FindMaxmumInLargeArray(largeArray);
	        		endTime = System.currentTimeMillis();
	        		duration = endTime - startTime;
					
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
    	//String s = null;
    	int[][] dividedArray = DividLargeArrayIntoSmallOnes(largeArray,threadNumber);
    	//for (int i=0; i < dividedArray.length; i++){
    	//	s =  s + Arrays.toString(dividedArray[i]) + "|";
    	//}
    	//System.out.println("large array is " + Arrays.toString(largeArray));
    	//System.out.println("dividedArray is "+ s);
    	return maxmum;
    }
    
    private int[] initialLargeArray(int n){
    	int[] largeArray = new int[n];
    	Random rnd = new Random();
    	
    	for(int i=0; i < n; i++){
    		largeArray[i] = rnd.nextInt(1000);
    	}
    	
    	return largeArray;
    }
    
    private int[][] DividLargeArrayIntoSmallOnes(int[] input,int count){
    	int numberOfElements = getArrayElementsNumber(input,count);
    	int[][] dividedArray = new int[count][numberOfElements];
    	for(int i=0; i < count; i++){
    		if (i != count - 1){
    			dividedArray[i] = Arrays.copyOfRange(input, i*numberOfElements, ((i+1)*numberOfElements));
    		}else{
    			dividedArray[i] = Arrays.copyOfRange(input, i*numberOfElements, input.length);
    		}
    	}
    	return dividedArray;
    }
    
    private int getArrayElementsNumber(int[] input,int count){   	
    	int numberOfElements = 0;
    	
    	if(input.length%count==0){
    		numberOfElements = input.length/count;
    	}
    	else{
    		 numberOfElements = input.length/count + 1;
    	}
    	
    	System.out.println("numberOfElements is " + Integer.toString(numberOfElements));
    	
    	return numberOfElements;
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
