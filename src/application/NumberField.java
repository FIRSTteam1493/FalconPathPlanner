package application;

import javafx.scene.control.TextField;

public class NumberField extends TextField
{
	public boolean hasvalue=false;
	public boolean hasint=false;
	public double value=-9999;
	public int intvalue=-9999;

	
    public double getDouble()
    {
       String text;
		try
   	{
		text=this.getText();
       	value=Double.parseDouble(text);
       	hasvalue=true;
   	}
   	catch(NumberFormatException e) {
   		hasvalue=false;
   		value=-9999;
   	}
       return value;
    }


    public double getDoubleOrBlank()
    {
       String text;
		try
   	{
		text=this.getText();
       	value=Double.parseDouble(text);
       	hasvalue=true;
   	}
   	catch(NumberFormatException e) {
   		hasvalue=false;
   		value=-9999;
   	}
       return value;
    }

    
    
    public int getInt()
    {
       String text;
		try
   	{
		text=this.getText();
		double c = 4;
		
       	intvalue=Integer.parseInt(text);
       	hasint=true;
   	}
   	catch(NumberFormatException e) {
   		hasint=false;
   		intvalue=-9999;
   	}
       return intvalue;
    }
    
    
    
}
