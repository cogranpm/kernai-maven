/* trait for all view model classes to implement to get dirty flag */

package com.parinherm.view

import groovy.beans.Bindable

trait BaseViewModel {
	
	@Bindable Boolean dirty = false
}
