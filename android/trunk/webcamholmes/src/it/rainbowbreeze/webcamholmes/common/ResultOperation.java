package it.rainbowbreeze.webcamholmes.common;

import it.rainbowbreeze.libs.common.BaseResultOperation;

public class ResultOperation<T> 
	extends BaseResultOperation<T>
{
	//---------- Constructor
	public ResultOperation()
	{ super(); }
	
	public ResultOperation(Exception ex, int errorReturnCode)
	{ super(ex, errorReturnCode); }
	
	


	//---------- Private fields

	
	
	
	//---------- Public properties
	public final static int RETURNCODE_ERROR_SAVE_PROVIDER_DATA = 500;
	public final static int RETURNCODE_ERROR_LOAD_PROVIDER_DATA = 501;
	public static final int RETURNCODE_ERROR_EMPTY_REPLY = 502;

	
	
	
	//---------- Public methods

	
	
	
	//---------- Private methods
}
