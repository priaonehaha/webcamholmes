package it.rainbowbreeze.webcamholmes.common;

import it.rainbowbreeze.libs.common.RainbowResultOperation;

public class ResultOperation<T> 
	extends RainbowResultOperation<T>
{
	//---------- Constructor
	public ResultOperation()
	{ super(); }
	
	public ResultOperation(Exception ex, int errorReturnCode)
	{ super(ex, errorReturnCode); }
	
	public ResultOperation(T value)
	{ super(value); }	


	//---------- Private fields

	
	
	
	//---------- Public properties
	public final static int RETURNCODE_ERROR_IMPORT_FROM_RESOURCE = 500;

	
	
	
	//---------- Public methods

	
	
	
	//---------- Private methods
}
