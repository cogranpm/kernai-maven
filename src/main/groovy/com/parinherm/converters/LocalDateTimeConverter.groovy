package com.parinherm.converters

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeConverter {
	
	static def convertFromString(String input) {
		//groovy trick, use groovy truth to check null or empty
		if (input) {
			LocalDateTime.parse(input, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

		} else {
			null
		}
	}


}
