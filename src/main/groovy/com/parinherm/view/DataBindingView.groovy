package com.parinherm.view


import java.time.LocalDate
import java.time.LocalTime
import org.eclipse.core.databinding.AggregateValidationStatus
import org.eclipse.core.databinding.Binding
import org.eclipse.core.databinding.DataBindingContext
import org.eclipse.core.databinding.UpdateValueStrategy
import org.eclipse.core.databinding.beans.typed.BeanProperties
import org.eclipse.core.databinding.conversion.IConverter
import org.eclipse.core.databinding.observable.ChangeEvent
import org.eclipse.core.databinding.observable.IChangeListener
import org.eclipse.core.databinding.observable.list.IObservableList
import org.eclipse.core.databinding.observable.list.WritableList
import org.eclipse.core.databinding.observable.map.IObservableMap
import org.eclipse.core.databinding.observable.set.IObservableSet
import org.eclipse.core.databinding.observable.value.IObservableValue
import org.eclipse.core.databinding.observable.value.WritableValue
import org.eclipse.core.databinding.validation.IValidator
import org.eclipse.core.databinding.validation.ValidationStatus
import org.eclipse.core.runtime.IStatus
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport
import org.eclipse.jface.databinding.swt.typed.WidgetProperties
import org.eclipse.jface.databinding.viewers.IViewerObservableValue
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider
import org.eclipse.jface.databinding.viewers.typed.ViewerProperties
//import org.eclipse.jface.internal.databinding.swt.DateTimeSelectionProperty
import org.eclipse.jface.layout.GridDataFactory
import org.eclipse.jface.layout.TableColumnLayout
import org.eclipse.jface.viewers.ArrayContentProvider
import org.eclipse.jface.viewers.ComboViewer
import org.eclipse.jface.viewers.ILabelProvider
import org.eclipse.jface.viewers.ISelectionChangedListener
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.jface.viewers.LabelProvider
import org.eclipse.jface.viewers.SelectionChangedEvent
import org.eclipse.jface.viewers.TableViewer
import org.eclipse.jface.viewers.TableViewerColumn
import org.eclipse.swt.SWT
import org.eclipse.swt.custom.SashForm
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.layout.FillLayout
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.Button
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.DateTime
import org.eclipse.swt.widgets.Label
import org.eclipse.swt.widgets.Spinner
import org.eclipse.swt.widgets.Table
import org.eclipse.swt.widgets.Text

import com.parinherm.domain.DataTypesList
import com.parinherm.domain.DomainTest
import com.parinherm.domain.ListItemDetail
import com.parinherm.ui.controls.DateTimeSelectionProperty
import com.parinherm.ui.controls.ControlsFactory
import com.parinherm.ui.controls.TableViewerColumnHelper
import com.parinherm.view.model.DatabindingViewModel

import groovy.transform.CompileStatic


@CompileStatic
class DataBindingView extends Composite {
	
	/* view controls for editing the current entity */
	Label lblStringTest
	Text txtStringTest
	
	Label lblComboTest
	ComboViewer cboComboTest
	
	Label lblIntTest
	Spinner spinIntTest
	
	Label lblCreatedDate
	DateTime dteCreatedDate
	
	Label lblCreatedTime
	DateTime dteCreatedTime
	
	Label lblBoolTest
	Button btnBoolTest
	
	/* view controls for selecting current entity */
	TableViewer listView
	Table listTable
	
	/* view controls for showing errors */
	Label lblError
	
	/* databinding members */
	DataBindingContext ctx = new DataBindingContext()
	DatabindingViewModel model = new DatabindingViewModel()
	WritableList<DomainTest> input
	WritableValue<DomainTest> value
	ObservableListContentProvider contentProvider = new ObservableListContentProvider()
	
	//toolbar buttons
	Button btnSave
	Button btnDelete
	Boolean selectionChange = false
	
	DomainTest selectedItem = null
	
	
	//handler that listens for databinding change events
	IChangeListener listener = new IChangeListener() {
		@Override
		public void handleChange(ChangeEvent event) {
			/* selectionChange flag is set on the list selection event handler, 
			 * which fires before this databinding handler fires
			 * allows view to ignore list selection changes when setting the dirty flag */
			
			
			if(!selectionChange) {
				model.dirty = true
			}
		}
	}
	
	
	public DataBindingView(Composite parent) {
		super(parent, SWT.None)
		def mainComposite = new Composite(this, SWT.NONE)
		mainComposite.setLayout(new FillLayout())
		
		def sashForm = new SashForm(mainComposite, SWT.HORIZONTAL)
		
		def listComposite = new Composite(sashForm, SWT.NONE)
		def editComposite = new Composite(sashForm, SWT.NONE)
		
		/* list */
		listView = new TableViewer(listComposite, SWT.NONE)
		listTable = listView.getTable()
		listTable.setHeaderVisible(true)
		listTable.setLinesVisible(true)
		TableColumnLayout tableLayout = new TableColumnLayout()
		listComposite.setLayout(tableLayout)
		
		TableViewerColumn stringTestColumn = TableViewerColumnHelper.getColumn("String Test", listView, tableLayout)
		TableViewerColumn intTestColumn = TableViewerColumnHelper.getColumn("Integer Test", listView, tableLayout)
		TableViewerColumn comboTestColumn = TableViewerColumnHelper.getColumn("Combo Test", listView, tableLayout)
		TableViewerColumn createdDateColumn = TableViewerColumnHelper.getColumn("Created Date", listView, tableLayout)
		TableViewerColumn createdTimeColumn = TableViewerColumnHelper.getColumn("Created Time", listView, tableLayout)
		def boolTestColumn = TableViewerColumnHelper.getColumn("Bool Test", listView, tableLayout)

		
		listView.setContentProvider(contentProvider)
		listView.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				selectionChange = true
				IStructuredSelection selection = listView.getStructuredSelection()
				selectedItem = selection.firstElement as DomainTest
				addDataBindings()
			}
		})
		
		lblStringTest = ControlsFactory.label(editComposite, "String Test:")
		txtStringTest = new Text(editComposite, SWT.NONE)
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtStringTest)
		
		lblIntTest = ControlsFactory.label(editComposite, "Int Test:")
		spinIntTest = new Spinner(editComposite, SWT.NONE)
		spinIntTest.setMinimum(Integer.MIN_VALUE)
		spinIntTest.setMaximum(Integer.MAX_VALUE)
		
		lblComboTest = ControlsFactory.label(editComposite, "Combo Test:")
		cboComboTest = new ComboViewer(editComposite)
		cboComboTest.setContentProvider(ArrayContentProvider.getInstance())
		cboComboTest.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element)
			{
				ListItemDetail item = element as ListItemDetail
				item.description
			}
		})
		cboComboTest.input = DataTypesList.items
		GridDataFactory.fillDefaults().grab(true, false).applyTo(cboComboTest.combo)
		
		lblCreatedDate = ControlsFactory.label(editComposite, "Date Created")
		dteCreatedDate = new DateTime(editComposite, SWT.DROP_DOWN | SWT.DATE)
		
		lblCreatedTime = ControlsFactory.label(editComposite, "Time Created")
		dteCreatedTime = new DateTime(editComposite, SWT.DROP_DOWN | SWT.TIME)
		
		lblBoolTest = ControlsFactory.label(editComposite, "Bool Test")
		btnBoolTest = new Button(editComposite, SWT.CHECK)
		

		lblError = ControlsFactory.errorLabel(editComposite)
		
		Button btnTest = new Button(editComposite, SWT.PUSH)
		btnTest.text = "Update"
		btnTest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				model.items[0].stringTest = "changed me"
			}
		})
		
		btnSave = new Button(editComposite, SWT.PUSH)
		btnSave.text = "Save"
		btnSave.enabled = false
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				model.dirty = false
			
			}
		})
		
		btnDelete = new Button(editComposite, SWT.PUSH)
		btnDelete.text = "Delete"
		btnDelete.enabled = false
		
		sashForm.setWeights([1, 3] as int[])
		
		
		editComposite.setLayout(new GridLayout(2, false))
		setLayout(new FillLayout())
		addListBindings()
	}
	
	private def addListBindings() {
		IObservableSet<DomainTest> knownElements = contentProvider.getKnownElements()
		def stringTest = BeanProperties.value(DomainTest.class, "stringTest").observeDetail(knownElements)
		def intTest = BeanProperties.value(DomainTest.class, "intTest").observeDetail(knownElements)
		def comboTest = BeanProperties.value(DomainTest.class, "comboTest").observeDetail(knownElements)
		def createdDate = BeanProperties.value(DomainTest.class, "createdDate").observeDetail(knownElements)
		def createdTime = BeanProperties.value(DomainTest.class, "createdTime").observeDetail(knownElements)
		def boolTest = BeanProperties.value(DomainTest.class, "boolTest").observeDetail(knownElements)
		
		IObservableMap[] labelMaps = [stringTest, intTest, comboTest, createdDate, createdTime, boolTest] as IObservableMap[]
		ILabelProvider labelProvider = new ObservableMapLabelProvider(labelMaps) {
				@Override
				public String getColumnText(Object element, int columnIndex) {
					DomainTest mc = element as DomainTest
					switch (columnIndex) {
						case 0:
							return mc.stringTest
							break
						case 1:
							return mc.intTest as String
							break
						case 2:
						//error, should be the friendly description, not the code
							return mc.comboTest
							break
						case 3:
							return mc.createdDate
							break
						case 4:
							return mc.createdTime
							break
						case 5:
							return mc.boolTest
							break
						default:
							return ""
					}
					
				}
		}
		listView.setLabelProvider(labelProvider)
		List<DomainTest> el = model.items
		input = new WritableList(el, DomainTest.class)
		listView.setInput(input)
		

	}
	
	/* this is called whenever the current entity is changed 
	 * in the list viewer, we rebind all the entity editing controls
	 * this is so we can avoid setting the dirty flag when the entity is changed
	 * as opposed to the values in the controls being changed manually
	 */
	private def addDataBindings() {
		ctx.dispose()
		IObservableList dabindings = ctx.getValidationStatusProviders()
		dabindings.each { element ->
			def b = element as Binding
			ctx.removeBinding(b)
		}
		
		//master detail bindings
		//the detail field
		IObservableValue target = WidgetProperties.text(SWT.Modify).observe(txtStringTest)
		IObservableValue targetIntTest = WidgetProperties.spinnerSelection().observe(spinIntTest)
		IObservableValue targetComboTest = ViewerProperties.singleSelection().observe(cboComboTest)
		def targetBoolTest = WidgetProperties.buttonSelection().observe(btnBoolTest)
		//the viewer
		IViewerObservableValue selectedEntity = ViewerProperties.singleSelection().observe(listView)
		//the property in the domain entity
		IObservableValue detailValue = BeanProperties.value("stringTest", String.class).observeDetail(selectedEntity)
		IObservableValue valueIntTest = BeanProperties.value("intTest", int.class).observeDetail(selectedEntity)
		IObservableValue valueComboTest = BeanProperties.value("comboTest", String.class).observeDetail(selectedEntity)
		def valueBoolTest = BeanProperties.value("boolTest", boolean.class).observeDetail(selectedEntity)
		
		DateTimeSelectionProperty dateTimeSelectionProperty = new DateTimeSelectionProperty()
		def targetDateCreated = dateTimeSelectionProperty.observe(dteCreatedDate)
		def valueDateCreated = BeanProperties.value("createdDate", LocalDate.class).observeDetail(selectedEntity)
		
		DateTimeSelectionProperty timeProperty = new DateTimeSelectionProperty()
		def targetTimeCreated = timeProperty.observe(dteCreatedTime)
		def valueTimeCreated = BeanProperties.value("createdTime", LocalTime.class).observeDetail(selectedEntity)
		
		
	
		
		//converting from a combo lookup to a field type, say string
		IConverter convertListItemDetail = IConverter.create(ListItemDetail.class, String.class, 
			{ 
				ListItemDetail o -> o?.code
			}
		)
		
		//converting from a field type to a lookup type
		//need to create a finder method to do it
		IConverter convertToListItemDetail = IConverter.create(String.class, ListItemDetail.class, 
			{ String o -> DataTypesList.findByCode(o)}
			)
	   
		/* just the validators and decorators in the name field */
		IValidator validator = new IValidator() {
			@Override
			public IStatus validate(Object value) {
				String nameValue = String.valueOf(value).replaceAll("\\s", "")
				if (nameValue.length() > 0){
				  return ValidationStatus.ok()
				}
				return ValidationStatus.error("String Test must be entered")
			}
			
		  };
		UpdateValueStrategy updateStrategy = new UpdateValueStrategy()
		updateStrategy.setAfterConvertValidator(validator)
		
		def detailBinding = ctx.bindValue(target, detailValue, updateStrategy, null)
		def intTestBinding = ctx.bindValue(targetIntTest, valueIntTest)
		def comboTestBinding = ctx.bindValue(targetComboTest, valueComboTest, 
			UpdateValueStrategy.create(convertListItemDetail), UpdateValueStrategy.create(convertToListItemDetail))
		
		// this one screws up the dirty binding for some reason
		def dateCreatedBinding = ctx.bindValue(targetDateCreated, valueDateCreated)
		def timeCreatedBinding = ctx.bindValue(targetTimeCreated, valueTimeCreated)
		
		def boolTestBinding = ctx.bindValue(targetBoolTest, valueBoolTest)
		

		//dirty binding
		IObservableList bindings = ctx.getValidationStatusProviders()
		bindings.each { element ->
			def b = element as Binding
			b.target.addChangeListener(listener)
		}
		
		
		//control decorators
		ControlDecorationSupport.create(detailBinding, SWT.TOP | SWT.LEFT)
		
		// error label binding
		final IObservableValue errorObservable = WidgetProperties.text().observe(lblError)
		def allValidationBinding = ctx.bindValue(errorObservable, new AggregateValidationStatus(ctx.getBindings(), AggregateValidationStatus.MAX_SEVERITY), null, null);


		//save button binding
		IObservableValue save = WidgetProperties.enabled().observe(btnSave)
		IObservableValue mdirty= BeanProperties.value("dirty").observe(model)
		def dirtyBinding = ctx.bindValue(save, mdirty)
		
		//this is only needed to set enabled on the action associated with toolbar button
		dirtyBinding.getTarget().addChangeListener(new IChangeListener() {
			@Override
			public void handleChange(ChangeEvent event) {
				//set the enabled of the action associated with toolbar button
			}
		})
		
		//delete button binding
		IObservableValue deleteItemTarget = WidgetProperties.enabled().observe(btnDelete)
		UpdateValueStrategy convertSelectedToBoolean = new UpdateValueStrategy(){
			@Override
			protected IStatus doSet(IObservableValue observableValue, Object value)
			{
				return super.doSet(observableValue, value == null ? Boolean.FALSE : Boolean.TRUE)
			}
		}
		
		
		//a binding that sets delete toolitem to disabled based on whether item in list is selected
		Binding deleteBinding = ctx.bindValue(deleteItemTarget, selectedEntity,  new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER), convertSelectedToBoolean)
		//a listener on above binding that makes sure action enabled is set set toolitem changes, ie can't databind the enbabled of an action
		deleteBinding.getTarget().addChangeListener(new IChangeListener() {
			@Override
			public void handleChange(ChangeEvent event) {

				//deleteAction.setEnabled(deleteToolItem.getEnabled())
			}
		})
		
		selectionChange = false
		
	}
}
