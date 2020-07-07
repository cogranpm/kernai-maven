package com.parinherm.persistence


import com.parinherm.domain.BaseEntity




interface IDatabase {
	def persist(BaseEntity model)
	def delete(BaseEntity model)
	List getAll(String className, Closure mapper)
	def get(BigInteger id, Closure mapper)
	def close()
}
