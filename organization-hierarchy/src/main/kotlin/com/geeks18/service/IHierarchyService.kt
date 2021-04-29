package com.geeks18.service

interface IHierarchyService {

    fun saveEmployeeHierarchy(map: Map<String, String?>)

    fun getAllEmployeeHierarchy(): MutableMap<String, Map<*, *>?>

    fun getSuperVisorByName(name: String,levels: Int?): MutableMap<String, Map<*, *>>

}