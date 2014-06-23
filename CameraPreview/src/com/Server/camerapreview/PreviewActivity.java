package com.Server.camerapreview;


import java.io.ByteArrayOutputStream;
//import java.io.FileOutputStream;
//import java.util.Scanner;

import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;

import android.os.Bundle;
//import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;


//@SuppressLint("NewApi")
public class PreviewActivity extends Activity implements SurfaceHolder.Callback{

	SurfaceView surfaceView;
	SurfaceHolder surfaceHolder;
	Camera camera = null;
	Button stop;
	boolean inPreview = false;
	boolean stat;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preview);
		surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		stop = (Button)findViewById(R.id.button1);
		
		/*A button set to end Camera preview and destroy the preview Surface*/
		stop.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				surfaceDestroyed(surfaceHolder);
			}
			
		});
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		camera = Camera.open();
		if(camera != null){
			try{
				camera.setPreviewDisplay(surfaceHolder);
				camera.setPreviewCallback(new PreviewCallback(){
					
					//the on preview method is called for every preview frame and data
					//is a byte containing information on the preview frame.
					//int pie=0;
					@Override
					public void onPreviewFrame(byte[] data, Camera camera) {
						try{
							
							
							//getting the size of the preview surface
							Camera.Parameters parameters = camera.getParameters();
	                        Size size = parameters.getPreviewSize();
	                        
	                        //converting the data[] byte into a YUVImage format of NV21
	                        //with appropriate height and width
	                        YuvImage image = new YuvImage(data, ImageFormat.NV21,
	                                size.width, size.height, null);
	                        //drawing a rectangle object with the preview details so we can fit 
	                        //each of our frames to the appropriate size
	                        Rect rectangle = new Rect();
	                        rectangle.bottom = size.height;
	                        rectangle.top = 0;
	                        rectangle.left = 0;
	                        rectangle.right = size.width;
	                        
	                        //instantiating Byte array output stream to compress our image
	                        //to JPEG and write our image into the outputStream
	                        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
	                        
	                        //the compression of frame data to JPEG into the output stream returns
	                        //a boolean true if successful and false if unsuccessful 
	                        stat = image.compressToJpeg(rectangle, 100, outStream);
	                        //Log.d(PreviewActivity.class.getName(), "base64 size: "+Base64.encode(data, RESULT_OK).length);
	                        /*
	                        String path = new String(Environment.getExternalStorageDirectory().getPath()) +"/Img/img" + pie++ + ".jpg";
	                        FileOutputStream fos = new FileOutputStream(path, true);
	                        fos.write(outStream.toByteArray());
	                        fos.close();
	                        System.out.println("Frame size: "+outStream.size());
							
	                        new Scanner(System.in).nextLine() ;
							*/
							Log.d(PreviewActivity.class.getName(), "Frame Size: "+outStream.size());
						}catch(Exception x){
							Log.e(PreviewActivity.class.getName(), "Exception: "+x.getMessage());
						}
					}
					
				});
			}catch(Exception x){
				Log.d(PreviewActivity.class.getName(), "Error in surface created: ["+x.getMessage()+"]");
			}
		}else Log.d(PreviewActivity.class.getName(), "Camera null");
	}
	
	//beginning of surface changed method implementation
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		
		//Checking of the preview surface is null
		if (surfaceHolder.getSurface() == null){
			 Log.d(PreviewActivity.class.getName(), "SurfaceHolder is null");
	          return;
	     	}

		//checking our inPreview boolean which tells us if we are currently previewing or not
		//if we are we have to first stop preview then start again
	    if(camera != null && !inPreview){ 
	    	
	    	//getting camera parameters and getting the best size from getBestPreviewSize()
			Camera.Parameters parameters = camera.getParameters();
	        Camera.Size size = getBestPreviewSize(width, height, parameters);
	        
	        //if our size is not null we apply it to our parameters 
	        if (size != null) {
	        	
	            parameters.setPreviewSize(size.width, size.height);
	            //parameters.setPictureFormat();
	           // Log.d(PreviewActivity.class.getName(), "size: "+surfaceHolder;
	           
	            //setting camera parameters
	            camera.setParameters(parameters);
	            try {
	            	
	            	//setting our preview display on our created surface
					camera.setPreviewDisplay(surfaceHolder);
					//surfaceView.setVisibility(0);
					
					//set the orientation to 90 degrees(portrait) to avoid rotation conflict
					camera.setDisplayOrientation(90);
					camera.startPreview();
					
					inPreview = true;
				} catch (Exception e) {
					Log.d(PreviewActivity.class.getName(), "SurfaceChanged: "+e.getMessage());
				}
	        }
	            
        }else Log.d(PreviewActivity.class.getName(), "Camera null");
		
	}

	//surface destroyed method which stops the preview and releases the camera resource
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if(inPreview){
			camera.stopPreview();
			camera.release();
			camera = null;
			inPreview = false;
		}
	}
	
	
	// Getting the best preview size
	private Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                  result=size;
                }
                else {
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

