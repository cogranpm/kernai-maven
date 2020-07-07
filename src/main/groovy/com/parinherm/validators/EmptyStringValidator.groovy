package com.parinherm.validators

import org.eclipse.core.databinding.validation.IValidator
import org.eclipse.core.databinding.validation.ValidationStatus
import org.eclipse.core.runtime.IStatus

class EmptyStringValidator implements IValidator{
	
	final String displayName
	
	EmptyStringValidator(final String displayName){
		this.displayName = displayName	
	}

	@Override
	public IStatus validate(final Object value) {
		final String nameValue = String.valueOf(value).replaceAll("\\s", "")
		nameValue.length() > 0 ? ValidationStatus.ok() : ValidationStatus.error("$displayName must be entered")
	}
}
