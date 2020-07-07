package com.parinherm.kernai;


import org.eclipse.swt.widgets.Display;


public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
		try {
			MainWindow window = new MainWindow();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
