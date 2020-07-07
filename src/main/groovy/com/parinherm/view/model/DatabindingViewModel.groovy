package com.parinherm.view.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

import com.parinherm.domain.DataTypesList
import com.parinherm.domain.DomainTest
import com.parinherm.main.AppCache
import com.parinherm.main.MainWindow

import groovy.beans.Bindable


//compile static not compatible with @Singleton
//@CompileStatic
class DatabindingViewModel {
	
	List<DomainTest> items = [buildNewDomainTest("apples", 0, true), 
		buildNewDomainTest("orange", 365, false), 
		buildNewDomainTest("lemon", 900, true)]
	
	/* this is best place for the dirty field
	 * as it really applies to the view
	 */
	@Bindable Boolean dirty = false
	
	private def buildNewDomainTest(String stringTest, int dateOffSet, boolean bool) {
		int intTest = MainWindow.cache.getRandomInt()
		String comboTest = DataTypesList.items[MainWindow.cache.getRandomInt(DataTypesList.items.size())].code
		LocalDate createdDate = LocalDateTime.now().toLocalDate().minusDays(dateOffSet)
		LocalTime createdTime = LocalDateTime.now().minusDays(dateOffSet).toLocalTime()
		LocalDateTime createdDateTime = LocalDateTime.now().minusDays(dateOffSet)
		new DomainTest(stringTest, intTest, comboTest, createdDate, createdTime, createdDateTime, bool)
	}
	

}
