/*
 custom class to implement databinding for LocalDate java 8 new date classes
 take from the vogella tutorials on datababinding:
 https://www.vogella.com/tutorials/EclipseDataBinding/article.html
 use this class in databinding statements when using the SWT DateTime control
 ie:
		 val dateTimeSelectionProperty: DateTimeSelectionProperty<DateTime, Any> = DateTimeSelectionProperty()
		val dateTimeObservableValue = dateTimeSelectionProperty.observe(dteCreated)
		val model_created = PojoProperties.value<ReferenceItem, LocalDate>("createdDate").observeDetail<ReferenceItem>(model.selectedItem)
 
*/

package com.parinherm.ui.controls


import org.eclipse.jface.databinding.swt.WidgetValueProperty
import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Widget
import org.eclipse.swt.widgets.DateTime
import java.time.temporal.TemporalAccessor
import java.time.LocalTime
import java.time.LocalDate
import java.time.temporal.ChronoField
import java.util.Date
import java.time.LocalDateTime
import java.util.Calendar

class DateTimeSelectionProperty extends WidgetValueProperty<Widget, Object> {
	
	DateTimeSelectionProperty() {
		super(SWT.Selection)
	}
	
	def MONTH_MAPPING_VALUE = 1
	
	def getValueType() {
		return TemporalAccessor.class
	}
	
	def doGetValue(Widget source){
		org.eclipse.swt.widgets.DateTime dateTime  = source as org.eclipse.swt.widgets.DateTime
		if ((dateTime.getStyle() & SWT.TIME) != 0) {
			return LocalTime.of(dateTime.getHours(), dateTime.getMinutes(), dateTime.getSeconds()) as Object
		}
		
		return LocalDate.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDay()) as Object
				
	}
	

	void doSetValue(Widget source, Object value) {
		org.eclipse.swt.widgets.DateTime dateTime  = source as org.eclipse.swt.widgets.DateTime
		def ta = getTemporalAccessor(value)
		
		if(ta != null){
			if((dateTime.getStyle() & SWT.TIME) != 0) {
			dateTime.setTime(ta.get(ChronoField.HOUR_OF_DAY),
				ta.get(ChronoField.MINUTE_OF_HOUR),
				ta.get(ChronoField.SECOND_OF_MINUTE))
			} else {
				dateTime.setDate(ta.get(ChronoField.YEAR),
					ta.get(ChronoField.MONTH_OF_YEAR) - MONTH_MAPPING_VALUE,
					ta.get(ChronoField.DAY_OF_MONTH))
			}
		}
	}
	
	private TemporalAccessor getTemporalAccessor(Object value) {
		TemporalAccessor ta = null
		if (value instanceof Date) {
			ta = LocalDateTime.from((value as Date).toInstant())			
		} else if(value instanceof TemporalAccessor){
			ta = value
		} else if(value instanceof Calendar) {
			ta = LocalDateTime.from((value as Calendar).toInstant())
		}
		return ta
	}
	

}