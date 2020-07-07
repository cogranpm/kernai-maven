package com.parinherm.persistence

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.logging.*

import com.parinherm.converters.LocalDateTimeConverter
import com.parinherm.domain.BaseEntity

import groovy.json.JsonGenerator
import groovy.json.JsonSlurper
import groovy.sql.Sql

class H2Database implements IDatabase {
	
	Logger logger = Logger.getLogger('groovy.sql')
	Sql db
	JsonGenerator jsonOutputter = new JsonGenerator.Options()
	.addConverter(LocalDateTime){
		 it.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
	}
	.excludeFieldsByName('propertyChangeListeners', 'dirtyFlag', 'newFlag', 'mapFromData').build()
	private final String table = 'ENTITYDATA'
	private final String id_field = 'ID'
	private final String entityclass_field = 'ENTITYCLASS'
	private final String data_field = 'DATA'
	private final String json_format = 'FORMAT JSON'
	

	H2Database(String dir){
		
		//not sure about this
		logger.level = Level.FINE
		
		//shouldn't this be off for stand alone
		//turning on so that can use the console as well
		def auto_server = ";AUTO_SERVER=TRUE"
		
		def url = "jdbc:h2:$dir$auto_server"
		def user = 'sa'
		def password = ''
		def driver = 'org.h2.Driver'
		db = Sql.newInstance(url, user, password, driver)
		createTables()
	}

	def close() {
		db.close()
	}	
	
	private def createTables() {
		def tableddl = """\
					CREATE TABLE IF NOT EXISTS $table($id_field IDENTITY NOT NULL PRIMARY KEY,
					$entityclass_field VARCHAR NOT NULL,
					$data_field JSON NOT NULL);""".stripIndent()
		db.execute(tableddl)
	}
	
	def delete(BaseEntity model) {
		String delete = """\
		DELETE FROM $table WHERE $id_field = ?;
		""".stripIndent()
		db.execute(delete, [model.id])
	}
	
	def persist(BaseEntity model) {
		if (!model.newFlag) {
			model.updatedOn = LocalDateTime.now()
			def json = jsonOutputter.toJson(model)
			update(json, model)
		}
		else {
			model.createdOn = LocalDateTime.now()
			def json = jsonOutputter.toJson(model)
			insert(json, model)
		}
		model.dirtyFlag = false
		model.newFlag = false

	}
	
	private def update(json, BaseEntity model) {
		def update = """\
			UPDATE $table SET $data_field  = ? $json_format WHERE $id_field = ?;
			""".stripIndent()
		def count = db.executeUpdate update, [json, model.id]
	}
	
	private def insert(json, BaseEntity model) {
		def clazz = model.getClass().getName()
		def insert = """\
			INSERT INTO $table ($entityclass_field, $data_field) VALUES (?, ? $json_format);
			""".stripIndent()
		def keys = db.executeInsert insert, [clazz, json]
		model.id = keys[0][0]
	}
	
	def get(BigInteger id, Closure mapper) {
		def select = "SELECT $data_field $json_format FROM $table WHERE $id_field = :id"
		def row = db.firstRow(select, [id: id])
		def jsonRaw = new String(row[0])
		def dataMap = new JsonSlurper().parseText(jsonRaw)
		//have to parse any localdatetime attributes from string representation in database
		dataMap.updatedOn = LocalDateTimeConverter.convertFromString(dataMap.updatedOn)
		dataMap.createdOn = LocalDateTimeConverter.convertFromString(dataMap.createdOn)
		def entity = mapper.call(dataMap, id)
		entity
	}


	//the className is the fully qualified name of the class entity that
	//is being searched in the database, it is a field in the EntityData table
	//the mapper closure should map the json and id into an entity object
	//a list of the entities will be returned
	List getAll(String className, Closure mapper) {
		def list = []
		
		Closure rowParser = {row ->
			def id = row[0]
			def clazzName = row[1]
			def jsonRaw = new String(row[2])
			def dataMap = new JsonSlurper().parseText(jsonRaw)
			//have to parse any localdatetime attributes from string representation in database
			dataMap.updatedOn = LocalDateTimeConverter.convertFromString(dataMap.updatedOn)
			dataMap.createdOn = LocalDateTimeConverter.convertFromString(dataMap.createdOn)
			def entity = mapper.call(dataMap, new BigInteger(id)) 
			list << entity
		}
	
		db.eachRow("SELECT $id_field, $entityclass_field, $data_field $json_format FROM $table WHERE $entityclass_field = :classname", [classname: className], rowParser)
		list
	}


}
