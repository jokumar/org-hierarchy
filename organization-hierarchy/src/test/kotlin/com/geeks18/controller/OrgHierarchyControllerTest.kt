package com.geeks18.controller


import com.geeks18.entity.EmployeeHierarchy
import com.geeks18.exceptions.ResourceNotFoundException
import com.geeks18.exceptions.ServiceException
import com.geeks18.repository.HierarchyJpaRepository
import com.geeks18.service.EmployeeHierarchyService
import com.nhaarman.mockito_kotlin.mock
import junit.framework.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner



@RunWith(MockitoJUnitRunner:: class)
class EmployeeHierarchyServiceTest {

    val hierarchyJpaRepository : HierarchyJpaRepository = mock()
    val employeeHierarchyService=  EmployeeHierarchyService(hierarchyJpaRepository)

    @Test
    fun getAllEmployee(){


        Mockito.`when`(hierarchyJpaRepository.findAll()).thenReturn(getEmployeeList())

        val employeeMap =  employeeHierarchyService.getAllEmployeeHierarchy();

        assertTrue(employeeMap.containsKey("Jonas"))
        assertTrue(employeeMap.get("Jonas")!!.containsKey("Sophie"))
    }

    @Test
    fun getAllEmployee_Empty(){

        Mockito.`when`(hierarchyJpaRepository.findAll()).thenReturn(mutableListOf())

        val employeeMap =  employeeHierarchyService.getAllEmployeeHierarchy();

        assertTrue(employeeMap.isEmpty())

    }



    @Test(expected = ServiceException::class)
    fun getAllEmployee_NoRoot() {
        Mockito.`when`(hierarchyJpaRepository.findAll()).thenReturn(getEmployeeList_noRoot())
        val employeeMap = employeeHierarchyService.getAllEmployeeHierarchy();
    }



    @Test(expected= ResourceNotFoundException:: class)
    fun getSuperVisorByName_no_resource_found(){

       // Mockito.`when`(hierarchyJpaRepository.findAll()).thenReturn(getEmployeeList())
        Mockito.`when`(hierarchyJpaRepository.findByName("")).thenReturn(null)
        employeeHierarchyService.getSuperVisorByName("",0)

    }


    @Test
    fun getSuperVisorByName_InfiniteLevels(){

        Mockito.`when`(hierarchyJpaRepository.findAll()).thenReturn(getEmployeeList())
        Mockito.`when`(hierarchyJpaRepository.findByName("Pete")).thenReturn(getEmployeebyName())
        val employeeMap =  employeeHierarchyService.getSuperVisorByName("Pete",5)
        assertTrue(employeeMap.containsKey("Nick"))
        assertTrue(employeeMap.get("Nick")!!.containsKey("Sophie"))
    }

    @Test
    fun getSuperVisorByName_zeroLevels(){

        Mockito.`when`(hierarchyJpaRepository.findAll()).thenReturn(getEmployeeList())
        Mockito.`when`(hierarchyJpaRepository.findByName("Pete")).thenReturn(getEmployeebyName())
        val employeeMap =  employeeHierarchyService.getSuperVisorByName("Pete",0)
        assertTrue(employeeMap.isEmpty())

    }

    fun getEmployeebyName(): EmployeeHierarchy{

        var emp = EmployeeHierarchy()
        emp.supervisorName="Nick"
        emp.name="Pete"
        return emp
    }

        fun getEmployeeList_noRoot(): MutableList<EmployeeHierarchy>{
        val empList: MutableList<EmployeeHierarchy> = mutableListOf();
        var emp1 = EmployeeHierarchy()
        emp1.supervisorName="Nick"
        emp1.name="Pete"

        var emp2 = EmployeeHierarchy()
        emp2.supervisorName="Nick"
        emp2.name="Barbara"

        var emp3 = EmployeeHierarchy()
        emp3.supervisorName="Sophie"
        emp3.name="Nick"

        var emp4 = EmployeeHierarchy()
        emp4.supervisorName="Jonas"
        emp4.name="Sophie"



        empList.add(emp1)
        empList.add(emp2)
        empList.add(emp3)
        empList.add(emp4)


        return empList
    }

    fun getEmployeeList(): MutableList<EmployeeHierarchy>{
        val empList= getEmployeeList_noRoot();

        var emp5 = EmployeeHierarchy()
        emp5.name="Jonas"
        empList.add(emp5)
        return empList;
    }

}