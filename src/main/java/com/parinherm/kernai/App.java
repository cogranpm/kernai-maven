package com.parinherm.kernai;


import org.eclipse.swt.widgets.Display;
import com.parinherm.main.MainWindow;


public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
		try {
        MainWindow window = new MainWindow(null);
        window.setBlockOnOpen(true);
        window.open();
        Display.getCurrent().dispose();
		} catch (Exception e) {
        e.printStackTrace();
		}
    }
}
