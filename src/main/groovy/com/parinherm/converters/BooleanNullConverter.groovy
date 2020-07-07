package com.parinherm.converters

import org.eclipse.core.databinding.UpdateValueStrategy
import org.eclipse.core.databinding.observable.value.IObservableValue
import org.eclipse.core.runtime.IStatus

class BooleanNullConverter extends UpdateValueStrategy{
	
	@Override
	protected IStatus doSet(IObservableValue observableValue, Object value)
	{
		return super.doSet(observableValue, value == null ? Boolean.FALSE : Boolean.TRUE)
	}
}
