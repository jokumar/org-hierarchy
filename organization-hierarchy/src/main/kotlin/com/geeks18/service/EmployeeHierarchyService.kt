package com.geeks18.service

import com.geeks18.entity.Employee
import com.geeks18.entity.EmployeeHierarchy
import com.geeks18.exceptions.ResourceNotFoundException
import com.geeks18.exceptions.ServiceException
import com.geeks18.repository.HierarchyJpaRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.atomic.AtomicReference
import javax.transaction.Transactional


@Component
class EmployeeHierarchyService(@Autowired private val hierarchyJpaRepository: HierarchyJpaRepository )
    : IHierarchyService {
    private val logger = LoggerFactory.getLogger(EmployeeHierarchyService :: class.java.name)


    /**
     * Save the Employee Hierarchy
     */
    @Transactional
     override fun saveEmployeeHierarchy(map: Map<String, String?>) {

        val ceo = AtomicReference("")

        for ((k, v) in map) {
            var emp = EmployeeHierarchy()
            emp.name = k
            emp.supervisorName = v
            hierarchyJpaRepository.save(emp)

            if (!map.containsKey(v)) {
                ceo.set(v)
            }

        }


        var e = EmployeeHierarchy()
        e.name = ceo.get()
        e.supervisorName = null
        hierarchyJpaRepository.save(e)

    }

    /**
     * Get All Employee Hierarchy
     */
    @Transactional
    override fun getAllEmployeeHierarchy(): MutableMap<String, Map<*, *>?> {
        val employeeMap: MutableMap<String, Map<*, *>?> = mutableMapOf()
        val empList = getAll()
        if(empList.isEmpty()){
            return employeeMap;
        }

        val list: List<Employee> = empList.filter { isRoot(it) }

        if(list.isEmpty()){
            logger.error("There is no Root in the hierarchy table ")
            throw ServiceException("SER-0002"," There is no Root in the hierarchy table ") // can be moved to an error constant
        }
        employeeMap[list[0].name] = getDescendants(list[0], empList)
        return employeeMap;
    }

    /**
     * Get Supervisor By Name
     */
    @Transactional
    override fun getSuperVisorByName(name: String,  levels: Int?): MutableMap<String, Map<*, *>> {

        val emp = hierarchyJpaRepository.findByName(name)
                ?: throw ResourceNotFoundException("RES-0001","There is no Employee wih name "+ name) // can be moved to an error constant

        logger.info("Employee retrieved by Name is "+emp.name + " and its supervisor is "+ emp.supervisorName)
        val empDTO=Employee(name = emp.name, supervisorName = emp.supervisorName)
        return getSupervisors(
                empDTO,
                getAll(),
                levels ,
                0
        )
    }

    /**
     * Recursive function the get the supervisor based on the levels
     */
    private fun getSupervisors(element: Employee, list: MutableList<Employee>, levels: Int?,  counter: Int): MutableMap<String, Map<*, *>, > {
        val supervisorMap: MutableMap<String, Map<*, *>> = mutableMapOf();

         if(levels != null && levels == counter  ) return supervisorMap;
         if (element.supervisorName == null) return supervisorMap

        val supervisor = list.first { element.supervisorName.equals(it.name)  }
        supervisorMap[supervisor.name] = getSupervisors(supervisor, list,levels,counter+1)
        return supervisorMap
    }

    /**
     * get the List of all Employee Hierarchy
     */
    private fun getAll():MutableList<Employee> {

        val employeList = hierarchyJpaRepository.findAll()

        val empList: MutableList<Employee> = mutableListOf();

        for (emp in employeList) {
            empList.add(Employee(name = emp!!.name, supervisorName = emp?.supervisorName))
        }
        return empList;
    }

    /**
     * If the employee is the root Element or not
     */
    private fun isRoot(emp: Employee?): Boolean {
        return !Optional.ofNullable(emp?.supervisorName).isPresent
    }

    /**
     * Get the immediate subordinates of  an employee
     */
    private fun getChildren(emp: Employee?, list: List<Employee>): Collection<Employee?> {

        val filterMap = list.filter { emp?.name.equals(it.supervisorName) };
        return filterMap;

    }

    /**
     * Recursive function to Get list of all Subordinates of an employee
     */
    private fun getDescendants(element: Employee?, list: MutableList<Employee>): Map<String, Map<*, *>?> {

        val children: Collection<Employee?> = getChildren(element, list)
        val employeeMap: MutableMap<String, Map<*, *>> = mutableMapOf();
        if (children.isEmpty()) {
            return HashMap<String, Map<*, *>>()
        }
        for (emp in children) {
            if (emp != null) {
                employeeMap[emp.name] = getDescendants(emp, list)
            }
        }


        return employeeMap;
    }


}