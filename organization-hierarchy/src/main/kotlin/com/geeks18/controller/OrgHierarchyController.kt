package com.geeks18.controller


import com.geeks18.service.IHierarchyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
class OrgHierarchyController(@Autowired private val employeeHierarchyService: IHierarchyService) {


    @PostMapping("/organization-structure")
    fun createOrganization(@RequestBody employeeMap: Map<String, String?>) =
            employeeHierarchyService.saveEmployeeHierarchy(employeeMap)


    @GetMapping("/organization-structure")
    fun getAllEmployee() = employeeHierarchyService.getAllEmployeeHierarchy()


    @GetMapping("/organization-structure/{name}")
    fun getEmployeeByName(@PathVariable("name") name: String, @RequestParam("levels", required = false) levels: Int?) = employeeHierarchyService.getSuperVisorByName(name, levels)


}