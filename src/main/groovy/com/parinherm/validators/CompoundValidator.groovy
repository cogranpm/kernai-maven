package com.parinherm.validators

import org.eclipse.core.databinding.validation.IValidator
import org.eclipse.core.databinding.validation.ValidationStatus
import org.eclipse.core.runtime.IStatus

class CompoundValidator implements IValidator {

	private final IValidator[] validators

	public CompoundValidator(final IValidator... validators) {
		this.validators = validators
	}

	public IStatus validate(final Object value) {
		IStatus result = ValidationStatus.ok()
		for (IValidator validator : validators) {
			IStatus status = validator.validate(value)
			if (status.getSeverity() > result.getSeverity()) {
				result = status
			}
		}
		return result
	}
}
