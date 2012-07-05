package com.openhci.graffiti;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Timer;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

public class AndroidAccelerometer extends Activity {

	SensorManager sensorManager;
	boolean accelerometerPresent;
	Sensor accelerometerSensor;
	FileWriter fw;
	BufferedWriter bw;
	TextView textInfo, textX, textY, textZ, cusorX, cusorY;
	Button btn;
	boolean control = false;
	boolean controlFile = false;
	int counter = 0;
	int trainCounter;
	float threshold = 3;

	float absX;
	float absY;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

//		 connectSocket();
		// 將BufferedWeiter與FileWrite物件做連結
		//
		textInfo = (TextView) findViewById(R.id.info);
		textX = (TextView) findViewById(R.id.textx);
		textY = (TextView) findViewById(R.id.texty);
		textZ = (TextView) findViewById(R.id.textz);
		cusorX = (TextView) findViewById(R.id.cusorX);
		cusorY = (TextView) findViewById(R.id.cusorY);
		btn = (Button) findViewById(R.id.btn);

		btn.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					control = true;
					controlFile = true;
					try {
						File file = new File("/sdcard/HCI/test" + counter
								+ ".txt");
						file.delete();

						file = new File("/sdcard/HCI/test" + counter
								+ ".txt.scale");
						file.delete();

						file = new File("/sdcard/HCI/predict" + counter
								+ ".txt");
						file.delete();

						fw = new FileWriter("/sdcard/HCI/test" + counter
								+ ".txt", false);
						bw = new BufferedWriter(fw);
						bw.write("+1 ");

						trainCounter = 0;

					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				case MotionEvent.ACTION_UP:

					if (controlFile) {
						try {
							bw.newLine();
							bw.flush();

//							String scaleResult = svm_scale.main(new String[] {
//									"-r", "/sdcard/HCI/train2.range",
//									"/sdcard/HCI/test" + counter + ".txt" });
//
//							FileWriter fw2 = new FileWriter("/sdcard/HCI/test"
//									+ counter + ".txt.scale", false);
//							BufferedWriter bw2 = new BufferedWriter(fw2); // 將BufferedWeiter與FileWrite物件做連結
//
//							bw2.write(scaleResult);
//							bw2.flush();

//							svm_predict
//									.main(new String[] {
//											"/sdcard/HCI/test" + counter
//													+ ".txt.scale",
//											"/sdcard/HCI/train2.model",
//											"/sdcard/HCI/predict" + counter
//													+ ".txt" });
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						control = false;
						counter++;
						break;
					}
				}
				// TODO Auto-generated method stub
				return false;
			}
		});

		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> sensorList = sensorManager
				.getSensorList(Sensor.TYPE_ACCELEROMETER);

		if (sensorList.size() > 0) {
			accelerometerPresent = true;
			accelerometerSensor = sensorList.get(0);

			String strSensor = "Name: " + accelerometerSensor.getName()
					+ "\nVersion: "
					+ String.valueOf(accelerometerSensor.getVersion())
					+ "\nVendor: " + accelerometerSensor.getVendor()
					+ "\nType: "
					+ String.valueOf(accelerometerSensor.getType()) + "\nMax: "
					+ String.valueOf(accelerometerSensor.getMaximumRange())
					+ "\nResolution: "
					+ String.valueOf(accelerometerSensor.getResolution())
					+ "\nPower: "
					+ String.valueOf(accelerometerSensor.getPower())
					+ "\nClass: " + accelerometerSensor.getClass().toString();

			textInfo.setText(strSensor);

		} else {
			accelerometerPresent = false;
		}

	}

	private void connectSocket() {
		// TODO Auto-generated method stub

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Socket socket;
				try {
					
					InetAddress serverAddr = InetAddress
							.getByName("192.168.1.194");
					// InetAddress serverAddr = InetAddress.getLocalHost();
					while (true) {
						Log.d("TCP", "C: Connecting...");
						try {
							socket = new Socket(serverAddr, 9999);

							while (true) {
								String message = String.format("m,%f,%f",absX,absY);
								Log.d("TCP", "StringAfter");
								try {
									Log.d("TCP", "C: Sending: " + message);
									PrintWriter out = new PrintWriter(
											new BufferedWriter(
													new OutputStreamWriter(
															socket.getOutputStream())),
											true);
									out.println(message);
								} catch (Exception e) {
									Log.e("TCP", "S: Error", e);
									e.printStackTrace();
									socket.close();
								} finally {

								}
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}// TCPServer.SERVERIP

			}
		});
		thread.start();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (accelerometerPresent) {
			sensorManager.registerListener(accelerometerListener,
					accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
			Toast.makeText(this, "Register accelerometerListener",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		if (accelerometerPresent) {
			sensorManager.unregisterListener(accelerometerListener);
			Toast.makeText(this, "Unregister accelerometerListener",
					Toast.LENGTH_LONG).show();
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private SensorEventListener accelerometerListener = new SensorEventListener() {

		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			textX.setText("X: " + String.valueOf(event.values[0]));
			textY.setText("Y: " + String.valueOf(event.values[1]));
			textZ.setText("Z: " + String.valueOf(event.values[2]));
			
			absX = event.values[0] > 0 ? event.values[0] - threshold
					: event.values[0] + threshold;
			absY = event.values[1] > 0 ? event.values[1] - threshold
					: event.values[1] + threshold;

			if (Math.abs(event.values[0]) < threshold)
				absX = 0;
			else
				absX = -(absX * 100 / 6);
			
			if (Math.abs(event.values[1]) < threshold)
				absY = 0;
			else 
				absY = absY* 100 / 6;

			cusorX.setText("cuX: "+ absX);
			cusorY.setText("cuY: "+ absY);

			if (control && trainCounter < 30) {
				try {
					Log.d("acc", event.values[0] + "," + event.values[1] + ","
							+ event.values[2]);

					bw.write(String.valueOf(trainCounter * 3) + ":"
							+ String.valueOf(event.values[0]) + " "
							+ String.valueOf(trainCounter * 3 + 1) + ":"
							+ String.valueOf(event.values[1]) + " "
							+ String.valueOf(trainCounter * 3 + 2) + ":"
							+ String.valueOf(event.values[2]) + " ");
					trainCounter++;
					// bw.newLine();
					bw.flush();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	};
}