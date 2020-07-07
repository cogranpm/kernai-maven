/*
 * implements view for saving a single question
 * going to test out jface databinding to a groovy map
 * 
 * current best way to do this is store json column in database which can 
 * convert to a groovy map easily, which can be converted easily to 
 * jface writableMap and observed easily
 * no domain class required, no mapping, always the same sql query
 * catch is you can't query fields at database level
 *  
https://stackoverflow.com/questions/31560084/jface-databinding-map-property-to-swt-text-field
Finally found it... map entries can be observed via

IObservableValue o = Observables.observeMapEntry(map, "test")
 * 
 * 
 * 
 * problem with maps is that haven't figured out how to observe a list of maps
 * in the tableviewer, to get that nice table updates as edit field is changed
 * groovy does however allow map based pojo construction, and also a single line 
 * output pojo to json
 * 
 * package com.example.groovy
import groovy.json.JsonBuilder  
import groovy.json.JsonSlurper  
import groovy.transform.ToString

class JsonToObject {
    public static void main(String[] args) {
        // Person object
        def person = new Person(firstName: "John", lastName: "Doe")
        // Json String
        def personJSON = new JsonBuilder(person).toPrettyString()
        // Json String to Map
        def personMap = new JsonSlurper().parseText(personJSON)
        // using Map to convert to Person object type
        def newPerson = new Person(personMap)
        println(person)
        println(newPerson)
        assert newPerson.firstName.equals(person.firstName)
        assert newPerson.lastName.equals(person.lastName)
    }
}

 * 
 * 
 */


package com.parinherm.view

import static com.parinherm.main.MainWindow.cache

import org.eclipse.core.databinding.AggregateValidationStatus
import org.eclipse.core.databinding.Binding
import org.eclipse.core.databinding.DataBindingContext
import org.eclipse.core.databinding.UpdateValueStrategy
import org.eclipse.core.databinding.beans.typed.BeanProperties
import org.eclipse.core.databinding.observable.ChangeEvent
import org.eclipse.core.databinding.observable.IChangeListener
import org.eclipse.core.databinding.observable.list.IObservableList
import org.eclipse.core.databinding.observable.list.WritableList
import org.eclipse.core.databinding.observable.map.IObservableMap
import org.eclipse.core.databinding.observable.set.IObservableSet
import org.eclipse.core.databinding.observable.value.ComputedValue
import org.eclipse.core.databinding.observable.value.IObservableValue
import org.eclipse.core.databinding.observable.value.WritableValue
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport
import org.eclipse.jface.databinding.swt.typed.WidgetProperties
import org.eclipse.jface.databinding.viewers.IViewerObservableValue
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider
import org.eclipse.jface.databinding.viewers.typed.ViewerProperties
import org.eclipse.jface.viewers.ComboViewer
import org.eclipse.jface.viewers.ILabelProvider
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.jface.viewers.TableViewer
import org.eclipse.jface.viewers.Viewer
import org.eclipse.swt.SWT
import org.eclipse.swt.custom.SashForm
import org.eclipse.swt.layout.FillLayout
import org.eclipse.swt.widgets.Button
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Label
import org.eclipse.swt.widgets.Table
import org.eclipse.swt.widgets.Text

import com.parinherm.converters.BooleanNullConverter
import com.parinherm.converters.ListItemConverters
import com.parinherm.domain.DomainTest
import com.parinherm.domain.Question
import com.parinherm.domain.QuizCategoriesList
import com.parinherm.ui.controls.ControlsFactory
import com.parinherm.ui.controls.ListComparator
import com.parinherm.validators.CompoundValidator
import com.parinherm.validators.EmptyStringValidator

import groovy.json.JsonSlurper



class QuizView extends Composite{
	//def props = [id:0, questionText:'', answerText:'']
	DataBindingContext dbc = new DataBindingContext()
	TableViewer listView
	Table listTable 
	Text txtQuestion
	Text txtAnswer
	ComboViewer cboCategories
	Label lblError
	WritableValue value = new WritableValue()
	WritableList wl 
	ObservableListContentProvider contentProvider = new ObservableListContentProvider()
	Boolean pauseDirtyListener = false
	Button btnSave
	Button btnNew
	Button btnDelete
	
	//WritableMap wm = new WritableMap()
	
	
	//Question model = new Question(id:0, question:'something', category:'testing', answer:'no answer yet')
	Question model


	
	
	DomainTest selectedItem = null
	
	
	//handler that listens for databinding change events
	IChangeListener listener = {
		if(!pauseDirtyListener) {
			model.dirtyFlag = true
		}
	}
	/*
	IChangeListener listener = new IChangeListener() {
		@Override
		public void handleChange(ChangeEvent event) {
			* selectionChange flag is set on the list selection event handler,
			 * which fires before this databinding handler fires
			 * allows view to ignore list selection changes when setting the dirty flag *
			if(!pauseDirtyListener) {
				dirtyFlag = true
			}
		}
	}
	*/

	
	
	QuizView(Composite parent){
		super(parent, SWT.NONE)

		//domain entity is then bound to the ui controls
		
		//this is map based stuff
		//wm.putAll(questionMap)
		//value.setValue(wm)
		def sashForm = new SashForm(this, SWT.HORIZONTAL)
		
		def listSection = ControlsFactory.listSection(sashForm)
		def mainComposite = ControlsFactory.editContainer(sashForm)
		def buttonsBar = ControlsFactory.toolbar(mainComposite)
		def editComposite = ControlsFactory.editForm(mainComposite)
		
		//only the tableviewer can appear in this composite
		def listComposite = ControlsFactory.listContainer(listSection)
		
		/* list */
		
		Closure listSelectionHandler = {
			IStructuredSelection selection = listView.getStructuredSelection()
			updateUserInterface(Optional.ofNullable(selection.firstElement as Question))
			
		}
		List columnDefs = [ [name: 'Question', sort: true], [name: 'Answer', sort: true]]
		listView = ControlsFactory.listView(listComposite, contentProvider, listSelectionHandler, new InnerViewerComparator(), columnDefs) 

		def title = ControlsFactory.title(editComposite, "Questions")
		lblError = ControlsFactory.errorLabel(editComposite)
		Label lblQuestion = ControlsFactory.label(editComposite, "Question")
		txtQuestion = ControlsFactory.text(editComposite, true)
		
		Label lblAnswer = ControlsFactory.label(editComposite, "Answer")
		txtAnswer = ControlsFactory.text(editComposite, true)
		
		Label lblCategory = ControlsFactory.label(editComposite, "Category")
		cboCategories = ControlsFactory.comboViewer(editComposite, QuizCategoriesList.items)
		
		btnSave = ControlsFactory.button(buttonsBar, "&Save"){persist()}
		btnSave.enabled = false
		
		btnNew = ControlsFactory.button(buttonsBar, "&New"){
			Question newModel = new Question(id: 0, dirtyFlag: true, newFlag: true)
			updateUserInterface(Optional.ofNullable(newModel))
		}
		
		btnDelete = ControlsFactory.button(buttonsBar, "&Delete"){
			def confirm = ControlsFactory.runConfirm("Delete Question", "Delete Question, are you sure?")
			if (!confirm) { return}
			cache.db.delete(model)
			wl.remove(model)
			value.setValue(null)
			model = null
			updateUserInterface(Optional.ofNullable(model))
		}
		btnDelete.enabled = false
		
		/*
		IObservableValue target =  WidgetProperties.text(SWT.Modify).observe(txtId)
		IObservableValue model = Observables.observeMapEntry(wm, "question")
		dbc.bindValue(target, model)
		*/
		//addBindings()
		
		sashForm.setWeights([1, 3] as int[])
		setLayout(new FillLayout())
		addListBindings()
		updateUserInterface(Optional.ofNullable(model))
	}
	
	private def addListBindings() {
		IObservableSet<DomainTest> knownElements = contentProvider.getKnownElements()
		def col_question = BeanProperties.value(Question.class, "question").observeDetail(knownElements)
		def col_answer = BeanProperties.value(Question.class, "answer").observeDetail(knownElements)
		
		IObservableMap[] labelMaps = [col_question, col_answer] as IObservableMap[]
		ILabelProvider labelProvider = new ObservableMapLabelProvider(labelMaps) {
					@Override
					public String getColumnText(Object element, int columnIndex) {
						def mc = element as Question
						switch (columnIndex) {
							case 0:
								return mc.question
								break
							case 1:
								return mc.answer
								break
							default:
								return ""
						}
					}
				}

		listView.setLabelProvider(labelProvider)
		def list = cache.db.getAll(Question.class.getName(), Question.mapFromData)
		wl = new WritableList(list, Question.class)
		listView.setInput(wl)

	}
	
	
	private def addBindings() {
		dbc.dispose()
		IObservableList dabindings = dbc.getValidationStatusProviders()
		dabindings.each { element ->
			def b = element as Binding
			dbc.removeBinding(b)
		}
		final IObservableValue co_question = WidgetProperties.text(SWT.Modify).observe(txtQuestion)
		final IObservableValue mo_question = BeanProperties.value("question").observeDetail(value)

		final IObservableValue co_answer = WidgetProperties.text(SWT.Modify).observe(txtAnswer)
		final IObservableValue mo_answer = BeanProperties.value("answer").observeDetail(value)
		
		final IObservableValue co_categroy = ViewerProperties.singleSelection().observe(cboCategories)
		final IObservableValue mo_category = BeanProperties.value("category").observeDetail(value)
		

		
		// create a validators library
		//create a validation class for each unique validation
		
		// look to this link:
		//https://eclipsesource.com/blogs/2012/08/22/improving-reuse-of-jface-data-binding-validators/
		/*
		 * The solution is to create an IValidator implementation whose only job is to execute a list of validators one after the other and return their aggregated results:
		*/

		final UpdateValueStrategy updateStrategy = new UpdateValueStrategy().tap { 
			afterConvertValidator = new CompoundValidator(new EmptyStringValidator("Question"))
		}
  
		def binding_question = dbc.bindValue(co_question, mo_question, updateStrategy, null)
		def binding_answer = dbc.bindValue(co_answer, mo_answer, null, null)
		def binding_category = dbc.bindValue(co_categroy, mo_category,
			UpdateValueStrategy.create(ListItemConverters.convertListItemDetail), 
			UpdateValueStrategy.create(ListItemConverters.makeConverter(QuizCategoriesList.items)))
		def errorDecorator = ControlDecorationSupport.create(binding_question, SWT.TOP | SWT.LEFT)
		
		// error label binding
		final IObservableValue errorObservable = WidgetProperties.text().observe(lblError)
		//def allValidationBinding = dbc.bindValue(errorObservable, new AggregateValidationStatus(dbc.getBindings(), AggregateValidationStatus.MAX_SEVERITY), null, null)
		def aggValStatus = new AggregateValidationStatus(dbc.getBindings(), AggregateValidationStatus.MAX_SEVERITY)
		def isValidationOK = ComputedValue.create({ -> aggValStatus.getValue().isOK()})
		def saveBtnObservable = WidgetProperties.enabled().observe(btnSave)
		dbc.bindValue(saveBtnObservable, isValidationOK)
		
		IObservableList bindings = dbc.getValidationStatusProviders()
		bindings.each { element ->
			Binding b = element as Binding
			b.target.addChangeListener(listener)
		}
		
		
		IObservableValue save = WidgetProperties.enabled().observe(btnSave)
		//IObservableValue mdirty= BeanProperties.value("dirtyFlag").observe(model)
		IObservableValue mdirty= BeanProperties.value("dirtyFlag").observeDetail(value)
		def dirtyBinding = dbc.bindValue(save, mdirty)
		

		//delete button binding
		IObservableValue deleteItemTarget = WidgetProperties.enabled().observe(btnDelete)
		
		IViewerObservableValue selectedEntity = ViewerProperties.singleSelection().observe(listView)
		//a binding that sets delete toolitem to disabled based on whether item in list is selected
		Binding deleteBinding = dbc.bindValue(deleteItemTarget, selectedEntity,  new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER), new BooleanNullConverter())
		//a listener on above binding that makes sure action enabled is set set toolitem changes, ie can't databind the enbabled of an action
		deleteBinding.getTarget().addChangeListener(new IChangeListener() {
			@Override
			public void handleChange(ChangeEvent event) {
				//deleteAction.setEnabled(deleteToolItem.getEnabled())
			}
		})

	}
	
	private def enableUserInterface(Boolean enable) {
		txtAnswer?.enabled = enable
		txtQuestion?.enabled = enable	
		cboCategories?.combo?.enabled = enable
	}
	
	private def updateUserInterface(Optional<Question> entity) {
		if (entity.isPresent()) {
			enableUserInterface(true)
		}
		else
		{
			enableUserInterface(false)
			return
		}
		pauseDirtyListener = true
		model = entity.get()
		addBindings()
		value.setValue(model)
		pauseDirtyListener = false
	}
	
	private def persist() {
		Boolean wasNew = model.newFlag
		cache.db.persist(model)
		if (wasNew) { wl.add(model)}
		//test loading the persisted value
		Question loadedQuestion = cache.db.get(model.id, Question.mapFromData)
		println "saved: $loadedQuestion"
	}
	
	class InnerViewerComparator extends ListComparator {
		
		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			int val = 0
			switch (this.propertyIndex) {
				case 0:
					val = compareQuestion(e1 as Question, e2 as Question)
					break
				case 1:
					val = compareAnswer(e1 as Question, e2 as Question)
					break
				default:
					val = 0
			}
			(this.direction == SWT.DOWN) ? -val : val
				
		}
		
		private int compareQuestion(Question a, Question b) {
			if (a == null || b == null) {
				return 0
			}
			a.question.compareTo(b.question)
		}
		
		private int compareAnswer(Question a, Question b) {
			if (a == null || b == null) {
				return 0
			}
			a.answer.compareTo(b.answer)
		}
	}
}
