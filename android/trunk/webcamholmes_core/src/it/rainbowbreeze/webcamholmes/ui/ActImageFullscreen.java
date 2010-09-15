/**
 * Copyright (C) 2010 Alfredo Morresi
 * 
 * This file is part of WebcamHolmes project.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * 
 */
package it.rainbowbreeze.webcamholmes.ui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import it.rainbowbreeze.libs.common.ServiceLocator;
import it.rainbowbreeze.libs.ui.ZoomableImageView;
import it.rainbowbreeze.webcamholmes.R;
import it.rainbowbreeze.webcamholmes.common.App;
import it.rainbowbreeze.webcamholmes.common.ResultOperation;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ZoomControls;

import static it.rainbowbreeze.libs.common.ContractHelper.*;

/**
 * Display an image in a fullscreen view, with scroll support
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 *
 */
public class ActImageFullscreen extends Activity {

	//---------- Private fields
	private ZoomableImageView mImage;
	private ActivityHelper mActivityHelper;
	private ZoomControls mZoomControls;
	
	private final static float ZOOM_INCREMENT = 0.1f;
	private static final int DIALOG_PROGRESS = 10;
	private static final int OPTIONMENU_SHOWHIDE_ZOOM = 10;
	private static final int OPTIONMENU_SHARE = 11;
	

	//---------- Public properties

	
	
	
	//---------- Events
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//make the view fullscreen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.actimagefullscreen);
    
        mActivityHelper = checkNotNull(ServiceLocator.get(ActivityHelper.class), "ActivityHelper");
        
        mImage = (ZoomableImageView) findViewById(R.id.actimagefullscreen_imgMain);
        mImage.setBackgroundColor(Color.BLACK);
        mZoomControls = (ZoomControls) findViewById(R.id.actimagefullscreen_zoomcontrols);
        mZoomControls.setOnZoomInClickListener(mOnZoomInClickListener);
        mZoomControls.setOnZoomOutClickListener(mOnZoomOutClickListener);
        
        assignImageToView();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int id)
	 */
	protected Dialog onCreateDialog(int id) {
    	Dialog retDialog = null;
    	
    	switch (id) {
    	case DIALOG_PROGRESS:
    		retDialog = mActivityHelper.createProgressDialog(this, R.string.actimagefullscreen_msgLoadImage);
    		break;
    		
		default:
			retDialog = super.onCreateDialog(id);
    	}
    	
    	return retDialog;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		mImage.onPause();
		super.onPause();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mImage.onResume(getResources());
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, OPTIONMENU_SHOWHIDE_ZOOM, 0, R.string.actimagefullscreen_mnuShowHideZoom)
			.setIcon(android.R.drawable.ic_menu_view);
//		menu.add(0, OPTIONMENU_SHARE, 1, R.string.actwebcam_mnuShare)
//			.setIcon(android.R.drawable.ic_menu_view);
		
		return true;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
		case OPTIONMENU_SHOWHIDE_ZOOM:
			if (View.VISIBLE == mZoomControls.getVisibility()) {
				mZoomControls.setVisibility(View.GONE);
			} else {
				mZoomControls.setVisibility(View.VISIBLE);
			}
			break;
			
		default:
			return super.onOptionsItemSelected(item);
		}
		
		return true;
	}
	
	private OnClickListener mOnZoomInClickListener = new OnClickListener() {
		public void onClick(View v) {
			mImage.incrementScale(ZOOM_INCREMENT);
		}
	};

	private OnClickListener mOnZoomOutClickListener = new OnClickListener() {
		public void onClick(View v) {
			mImage.incrementScale(-ZOOM_INCREMENT);
		}
	};

	
	//---------- Public methods

	


	//---------- Private methods

	/**
	 * Assign dumped webcam image to view
	 */
	private void assignImageToView() {
		
		//show open dialog
		showDialog(DIALOG_PROGRESS);
		
		//and launch the thread that load the image
		runOnUiThread(new Runnable() {
			public void run() {
				FileInputStream fis = null;
				try {
					fis = openFileInput(App.WEBCAM_IMAGE_DUMP_FILE);
					Bitmap bitmap = BitmapFactory.decodeStream(fis);
			        mImage.assignImage(getResources(), bitmap);
			        //dismiss progress dialog
					removeDialog(DIALOG_PROGRESS);			        
				} catch (FileNotFoundException e) {
					//TODO change error return code
					mActivityHelper.reportError(ActImageFullscreen.this, e, ResultOperation.RETURNCODE_ERROR_APPLICATION_ARCHITECTURE);
				}
			}
		});
	}
	
}
