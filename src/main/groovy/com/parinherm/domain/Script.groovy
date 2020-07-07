package com.parinherm.domain

import com.parinherm.view.BaseViewModel

import groovy.beans.Bindable
import groovy.transform.Canonical
import groovy.transform.CompileStatic

@Canonical
@Bindable
@CompileStatic
class Script implements BaseViewModel {
	String name
	String body
}
