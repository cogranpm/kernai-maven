package com.parinherm.domain

import java.time.LocalDateTime

import groovy.beans.Bindable

@Bindable
abstract class BaseEntity {
	BigInteger id
	Boolean dirtyFlag
	Boolean newFlag
	LocalDateTime createdOn
	LocalDateTime updatedOn
	
}
