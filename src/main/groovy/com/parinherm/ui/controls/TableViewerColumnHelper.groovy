package com.parinherm.ui.controls

import org.eclipse.jface.layout.TableColumnLayout
import org.eclipse.jface.viewers.ColumnWeightData
import org.eclipse.jface.viewers.TableViewerColumn
import org.eclipse.jface.viewers.Viewer
import org.eclipse.swt.SWT

class TableViewerColumnHelper {
	
	static TableViewerColumn getColumn(String label, Viewer viewer, TableColumnLayout layout) {
		TableViewerColumn column = new TableViewerColumn(viewer, SWT.LEFT)
		column.getColumn().setText(label)
		column.getColumn().setResizable(true)
		column.getColumn().setMoveable(false)
		layout.setColumnData(column.getColumn(), new ColumnWeightData(100))
		column
	}
}
