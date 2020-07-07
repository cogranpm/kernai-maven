/* class that represents a list item
 * has a code that will typically be stored
 * and a user friendly string, the description 
 * that will not be stored
 */

package com.parinherm.domain

import groovy.transform.Canonical

@Canonical
class ListItemDetail {
	String description
	String code
	
	public static ListItemDetail findByCode(String code, List<ListItemDetail> items) {
		items.find { x -> x.code == code }
	}
}

