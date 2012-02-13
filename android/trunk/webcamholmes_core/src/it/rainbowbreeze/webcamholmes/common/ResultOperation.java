package it.rainbowbreeze.webcamholmes.common;

import it.rainbowbreeze.libs.common.RainbowResultOperation;

public class ResultOperation<ResultType> 
    extends RainbowResultOperation<ResultType>
{
    //---------- Constructor
    public ResultOperation()
    { super(); }
    
    public ResultOperation(Exception ex, int errorReturnCode)
    { super(ex, errorReturnCode); }
    
    public ResultOperation(ResultType value)
    { super(value); }

    public ResultOperation(int returnCode, ResultType value)
    { super(returnCode, value); }


    //---------- Private fields

    
    
    

    //---------- Public properties
    public final static int RETURNCODE_ERROR_IMPORT_FROM_RESOURCE = 500;

    
    
    
    //---------- Public methods

    
    
    
    //---------- Private methods
}
