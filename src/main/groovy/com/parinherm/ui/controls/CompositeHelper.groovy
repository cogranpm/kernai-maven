package com.parinherm.ui.controls

import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Control

class CompositeHelper {
	
	static void clear(Composite composite) {
		for(Control control : composite.getChildren())
			{
				control.dispose();
			}
	}
}
