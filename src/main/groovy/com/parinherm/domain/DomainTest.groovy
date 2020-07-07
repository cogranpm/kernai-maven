package com.parinherm.domain

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

import groovy.beans.Bindable
import groovy.transform.Canonical
import groovy.transform.CompileStatic

@CompileStatic
@Canonical
class DomainTest {

	@Bindable String stringTest
	@Bindable int intTest	
	@Bindable String comboTest
	@Bindable LocalDate createdDate
	@Bindable LocalTime createdTime
	@Bindable LocalDateTime createdDateTime
	@Bindable boolean boolTest
	
}


