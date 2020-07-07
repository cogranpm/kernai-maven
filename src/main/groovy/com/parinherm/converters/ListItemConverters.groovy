package com.parinherm.converters

import org.eclipse.core.databinding.conversion.IConverter

import com.parinherm.domain.DataTypesList
import com.parinherm.domain.ListItemDetail

class ListItemConverters {
	//converting from a combo lookup to a field type, say string
	public final static IConverter convertListItemDetail = IConverter.create(ListItemDetail.class, String.class,
		{
			ListItemDetail o -> o?.code
		}
	)
	
	//converting from a field type to a lookup type
	//need to create a finder method to do it
	/*
	public final static IConverter convertToListItemDetail = IConverter.create(String.class, ListItemDetail.class,
		{ 
			String o -> 
			def item = ListItemDetail.findByCode(o, QuizCategoriesList.items)
			item
		})
	*/
	
	 static IConverter makeConverter(List items) {
		 return IConverter.create(String.class, ListItemDetail.class, {
			 String o ->
			 def item = ListItemDetail.findByCode(o, items)
			 item
		 })
	 }
}
