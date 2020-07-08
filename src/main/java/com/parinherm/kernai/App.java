package com.parinherm.kernai;


import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.DisplayRealm;
import org.eclipse.swt.widgets.Display;
import com.parinherm.main.MainWindow;


public class App 
{

    public static void main(String[] args) {

        try
        {
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    try {
                        MainWindow mainwin = new MainWindow(null);
                        mainwin.setBlockOnOpen(true);
                        mainwin.open();
                        Display.getDefault().dispose();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            };
            Realm.runWithDefault(DisplayRealm.getRealm(Display.getDefault()), run);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
