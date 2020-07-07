package com.parinherm.ui.controls

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter

import org.eclipse.jface.databinding.viewers.ObservableListContentProvider
import org.eclipse.jface.dialogs.MessageDialog
import org.eclipse.jface.layout.GridDataFactory
import org.eclipse.jface.layout.TableColumnLayout
import org.eclipse.jface.viewers.ArrayContentProvider
import org.eclipse.jface.viewers.ComboViewer
import org.eclipse.jface.viewers.LabelProvider
import org.eclipse.jface.viewers.TableViewer
import org.eclipse.jface.viewers.ViewerComparator
import org.eclipse.swt.SWT
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.layout.FillLayout
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.layout.RowLayout
import org.eclipse.swt.widgets.Button
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Label
import org.eclipse.swt.widgets.Text

import com.parinherm.domain.DataTypesList
import com.parinherm.domain.ListItemDetail

class ControlsFactory {
	
	static Button button(Composite parent, String text, Integer style = SWT.PUSH, Closure handler) {
		def button = new Button(parent, style)
		button.text = text
		button.addSelectionListener(widgetSelectedAdapter(handler))
		button
	}
	
	static ComboViewer comboViewer(Composite parent, List<ListItemDetail> input) {
		def combo = new ComboViewer(parent)
		combo.contentProvider = ArrayContentProvider.getInstance()
		def labelProvider = new LabelProvider() {
			@Override
			public String getText(Object element)
			{
				ListItemDetail item = element as ListItemDetail
				item.description
			}
		}
		combo.labelProvider = labelProvider
		combo.input = input
		GridDataFactory.fillDefaults().grab(true, false).applyTo(combo.combo)
		combo
	}
	
	static Composite toolbar(Composite parent) {
		Composite composite = new Composite(parent, SWT.BORDER)
		composite.setLayout(new RowLayout())
		composite
	}
	
	static Composite listSection(Composite parent) {
		Composite composite = new Composite(parent, SWT.BORDER)
		composite.setLayout(new FillLayout(SWT.VERTICAL))
		composite
	}
	
	static Composite listContainer(Composite parent) {
		Composite composite = new Composite(parent, SWT.BORDER)
		composite.setLayout(new FillLayout(SWT.VERTICAL))
		composite
	}
	
	static Composite editContainer(Composite parent) {
		Composite composite = new Composite(parent, SWT.BORDER)
		//composite.setLayout(new FillLayout(SWT.VERTICAL))
		composite.setLayout(new GridLayout(1, true))
		composite
	}
	
	static Composite editForm(Composite parent) {
		Composite composite = new Composite(parent, SWT.BORDER)
		composite.setLayout(new GridLayout(2, false))
		GridDataFactory.fillDefaults().grab(true, true).applyTo(composite)
		composite
	}
	
	
	static TableViewer listView(Composite parent, ObservableListContentProvider contentProvider, Closure selectionHandler, ViewerComparator comparator, List columnDefs ) {
		def listView = new TableViewer(parent, SWT.NONE)
		listView.setComparator(comparator)
		def listTable = listView.getTable()
		listTable.setHeaderVisible(true)
		listTable.setLinesVisible(true)
		TableColumnLayout tableLayout = new TableColumnLayout()
		parent.layout = tableLayout
		columnDefs.eachWithIndex { Map item, index ->
			 
			def column = TableViewerColumnHelper.getColumn(item.name, listView, tableLayout)
			if (item.sort) {
				column.column.addSelectionListener(new SelectionAdapter() {
					 
					@Override
					void widgetSelected(SelectionEvent e) {
						ListComparator viewerComparator = listView.getComparator() as ListComparator
						viewerComparator.column = index
						listView.table.sortDirection = viewerComparator.direction
						listView.table.sortColumn = column.column
						listView.refresh(true)
					}
				})
			}
		}
		listView.setContentProvider(contentProvider)
		listView.addSelectionChangedListener(selectionHandler)
		listView
	}
	
	static Label label(Composite parent, String text) {
		Label label = new Label(parent, SWT.NONE)
		label.text = text
		GridDataFactory.fillDefaults().applyTo(label)
		label
		
	}

	static Label title(Composite parent, String text) {
		Label label = new Label(parent, SWT.NONE)
		label.text = text
		GridDataFactory.fillDefaults().span(2, 1).grab(true, false)applyTo(label)
		label
		
	}

		
	static Label errorLabel(Composite parent) {
		Label label = new Label(parent, SWT.NONE)
		GridDataFactory.fillDefaults().span(2, 1).grab(true, false).applyTo(label)
		label
	}
	
	static Text text(Composite parent, Boolean fill = true) {
		Text text = new Text(parent, SWT.NONE)
		GridDataFactory.fillDefaults().grab(fill, false).applyTo(text)
		text
	}
	
	static Boolean runConfirm(String title, String prompt) {
		Boolean confirm = MessageDialog.openConfirm(Display.getDefault().getActiveShell(), title, prompt)
		confirm
	}

}
