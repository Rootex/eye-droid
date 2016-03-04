package activities;

import io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.logging.Logger;
import notifier.Mail;
import logger.LoggerService;
import com.Server.camerapreview.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;

public class CaptureActivity extends BaseActivity implements
		SurfaceHolder.Callback, Camera.PreviewCallback {

	private static String DELIVERED = "SMS_DELIVERED";
	private static String SENT = "SMS_SENT";
	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	private Camera camera = null;
	private Button capture;
	private boolean inPreview = false;
	private boolean stat;
	private Bitmap bitImage;
	private Bitmap infBitmap;
	private Output output = new Output();
	private static final int NUM_FACES = 30;
	private FaceDetector detector;
	private FaceDetector.Face[] faces = new Face[NUM_FACES];;
	private TextView face;
	private String msg;
	private int facesDetected = 0;
	private long lastTimeStamp = System.nanoTime();
	private long delay = 30000000000L;
	public static final String PREF_NAME = "user_settings";
	// private float eyeDistance;
	private String email;
	private String password;
	private String phoneNumber;
	private String message;
	PointF midPoint;
	Paint myPaint;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contentView = inflater.inflate(R.layout.activity_capture, null, false);
		drawerLayout.addView(contentView, 0);

		msg = "Application started";
		sendMsg(msg, "start");

		surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		capture = (Button) findViewById(R.id.button1);
		face = (TextView) findViewById(R.id.textView1);
		/*
		 * A button set to end Camera activities and destroy the activities
		 * Surface
		 */
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		//Fix this, implement Mail sender in Threads instead.....
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
	}

	// Method responsible for sending intents
	public void sendMsg(String msg, String key) {
		Intent sendIntent = new Intent();
		sendIntent.setClass(this, LoggerService.class);
		sendIntent.putExtra(key, msg);
		startService(sendIntent);
	}

	// Handling the back key press.
	@Override
	public void onBackPressed() {
		surfaceDestroyed(this.surfaceHolder);
		finish();
		startActivity(new Intent(this, HomeActivity.class));
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		camera = Camera.open();
		if (camera != null) {
			try {
				camera.setPreviewDisplay(holder);
			} catch (Exception x) {
				camera.release();
				camera = null;
				Log.d(CaptureActivity.class.getName(),
						"Error in surface created: [" + x.getMessage() + "]");
			}
		} else
			Log.d(CaptureActivity.class.getName(), "Camera null");
	}

	// class name require for logs
	public String activityName() {
		return CaptureActivity.class.getName();
	}

	// beginning of surface changed method implementation
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

		// Checking of the activities surface is null
		if (surfaceHolder.getSurface() == null) {
			Log.d(activityName(), "SurfaceHolder is null");
			return;
		}

		// checking our inPreview boolean which tells us if we are currently
		// previewing or not
		// if we are we have to first stop activities then start again
		if (camera != null && !inPreview) {

			// getting camera parameters and getting the best size from
			// getBestPreviewSize()
			Camera.Parameters parameters = camera.getParameters();
			Camera.Size size = getPreviewSize(width, height, parameters);

			// if our size is not null we apply it to our parameters
			if (size != null) {

				parameters.setPreviewSize(size.width, size.height);
				// parameters.setPictureFormat();
				// Log.d(CaptureActivity.class.getName(),
				// "size: "+surfaceHolder;

				// setting camera parameters
				camera.setParameters(parameters);

				// set the orientation to 90 degrees(portrait) to avoid rotation
				// conflict
				camera.setDisplayOrientation(0);
				camera.startPreview();
				
				infBitmap = Bitmap.createBitmap(size.width, size.height,
						Bitmap.Config.RGB_565);
				
				inPreview = true;
				int buffSize = size.width
						* size.height
						* ImageFormat.getBitsPerPixel(parameters
								.getPreviewFormat()) / 8;
				byte[] Buffer = new byte[buffSize];
				camera.setPreviewCallbackWithBuffer(this);
				camera.addCallbackBuffer(Buffer);
			}

		} else
			Log.d(activityName(), "Camera null");

	}

	public void onPreviewFrame(byte[] data, Camera camera) {

		// getting the size of the activities surface

		// converting the data[] byte into a YUVImage format of NV21
		// with appropriate height and width
		YuvImage image = new YuvImage(data, ImageFormat.NV21,
				infBitmap.getWidth(), infBitmap.getHeight(), null);

		// drawing a rectangle object with the activities details so we can fit
		// each of our frames to the appropriate size
		Rect rectangle = new Rect();
		rectangle.bottom = infBitmap.getHeight();
		rectangle.top = 0;
		rectangle.left = 0;
		rectangle.right = infBitmap.getWidth();
		
		
		// instantiating Byte array Output stream to compress our image
		// to JPEG and write our image into the outputStream
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();

		// the compression of frame data to JPEG into the Output stream returns
		// a boolean true if successful and false if unsuccessful
		stat = image.compressToJpeg(rectangle, 100, outStream);
		
		if (!stat) {
			Log.d(activityName(), "Jpg comression failed");
		}

		detector = new FaceDetector(infBitmap.getWidth(),
				infBitmap.getHeight(), NUM_FACES);

		BitmapFactory.Options inf = new BitmapFactory.Options();
		inf.inPreferredConfig = Bitmap.Config.RGB_565;
		bitImage = BitmapFactory.decodeStream(new ByteArrayInputStream(
				outStream.toByteArray()), null, inf);
		
		Arrays.fill(faces, null);
		facesDetected = detector.findFaces(bitImage, faces);
		
		SharedPreferences sPref = getSharedPreferences(PREF_NAME, 0);
		
		if(sPref.getInt("delay", 0) != 0)
			delay = sPref.getInt("delay", 30) * 1000000000L;
		
		if (facesDetected > 0) {
			if (getCurrentTime() - lastTimeStamp >= delay) {
				msg = facesDetected + " Face Detected";
				sendMsg(msg, "face");
				output.saveFace(bitImage);

				email = sPref.getString("email", "nil");
				password = sPref.getString("password", "nil");
				phoneNumber = sPref.getString("phone", "nil");

				if (sPref.getBoolean("emailCheck", false)) {
					if (email == "nil" || password == "nil") {
						Toast.makeText(getApplicationContext(),
								"Email Settings not set ", Toast.LENGTH_LONG)
								.show();
					} else {
						sendMail(email, password, output.getPath());
					}
				}

				if (sPref.getBoolean("phoneCheck", false)) {
					if (phoneNumber == "nil") {
						Toast.makeText(getApplicationContext(),
								"Phone Settings not set", Toast.LENGTH_LONG)
								.show();
					} else {
						message = "Theres an intruder in your house.";
						sendSms(phoneNumber, message);
					}
				} else {
					Toast.makeText(getApplicationContext(),
							"Phone Settings not checked", Toast.LENGTH_LONG)
							.show();
				}
				lastTimeStamp = getCurrentTime();
			}

			// code that sets the live face detection messages.
			switch (facesDetected) {
			case 1:
				face.setText(facesDetected + " Face detected");
				break;
			default:
				face.setText(facesDetected + " Faces detected");
			}

		} else
			face.setText("No face found");

		capture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				output.saveFrame(bitImage);
				msg = "Image captured";
				sendMsg(msg, "capture");
				Toast.makeText(getApplicationContext(), "Image Captured",
						Toast.LENGTH_SHORT).show();

			}
		});

		camera.addCallbackBuffer(data);
	}

	// surface destroyed method which stops the activities and releases the
	// camera
	// resource

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (inPreview) {
			camera.stopPreview();
			camera.release();
			camera = null;
			inPreview = false;
		}
		stopServ();
		// sSystem.exit(0);
	}

	public void stopServ() {
		stopService(new Intent(this, Logger.class));
	}

	public long getCurrentTime() {
		return System.nanoTime();
	}

	// method that sends the message to email
	public void sendMail(String email, String password, String path) {
		Mail mail = new Mail(email, password);
		String[] to = { email };
		mail.setTo(to);
		mail.setSubject("Intruders Face");
		mail.setFrom(email);
		mail.setBody("Someone just intruded, image of face in attachment");
		// String root = Environment.getExternalStorageDirectory().toString();
		try {
			mail.addAttachment(path);
			if (mail.send()) {
				Toast.makeText(getApplicationContext(), "Mail sent",
						Toast.LENGTH_SHORT).show();
			} else
				Toast.makeText(getApplicationContext(), "mail failed",
						Toast.LENGTH_SHORT).show();
		} catch (Exception x) {
			Log.e(CaptureActivity.class.getName(),
					"Exception: " + x.getMessage());
			x.printStackTrace();
		}
	}

	// method to send sms notification
	public void sendSms(String phoneNumber, String message) {
		SmsManager smsManager = SmsManager.getDefault();

		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
				new Intent(DELIVERED), 0);
		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
				SENT), 0);

		smsManager.sendTextMessage(phoneNumber, null, message, sentPI,
				deliveredPI);
	}

	// Getting the best activities size
	private Camera.Size getPreviewSize(int width, int height,
			Camera.Parameters parameters) {
		Camera.Size result = null;

		for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
			if (size.width <= width && size.height <= height) {
				if (result == null) {
					result = size;
				} else {
					int resultArea = result.width * result.height;
					int newArea = size.width * size.height;

					if (newArea > resultArea) {
						result = size;
					}
				}
			}
		}

		return result;
	}

}
