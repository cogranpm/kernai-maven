package com.parinherm.domain

import com.parinherm.converters.LocalDateTimeConverter

import groovy.beans.Bindable
import groovy.json.JsonSlurper
import groovy.transform.Canonical


@Canonical
@Bindable
class Question extends BaseEntity {
	String question
	String category
	String answer
	
	static Closure<Question> mapFromData = { Map dataMap, BigInteger id ->
		//def questionMap = new JsonSlurper().parseText(jsonData)
		Question entity = new Question(dataMap)
		entity.id = id
		entity
	}
}
