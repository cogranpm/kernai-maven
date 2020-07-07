package com.parinherm.ui.controls

import org.eclipse.jface.viewers.ViewerComparator
import org.eclipse.swt.SWT

class ListComparator extends ViewerComparator {
	protected final static Integer DESCENDING = 1
	protected Integer direction = DESCENDING
	protected Integer propertyIndex
	
	ListComparator() {
		super()
		this.propertyIndex = 0
		this.direction = DESCENDING
	}
	
	void setColumn(int column) {
		if(column == this.propertyIndex) {
			this.direction = 1 - this.direction
		} else {
			this.propertyIndex = column
			this.direction = DESCENDING
		}
	}
	
	int getDirection() {
		if (this.direction == DESCENDING) {
			return SWT.DOWN
		} else {
			return SWT.UP
		}
	}
}
