package com.parinherm.domain

class DataTypesList {
	
	static def itemString = new ListItemDetail(code: "string", description: "String")
	static def itemInt = new ListItemDetail(code: "int", description: "Integer")
	static def itemDate = new ListItemDetail(code: "date", description: "Date")
	
	
	public static List<ListItemDetail> items = [itemString, itemInt, itemDate]
	
	public static ListItemDetail findByCode(String code) {
		items.find { x -> x.code == code }
	}
}
