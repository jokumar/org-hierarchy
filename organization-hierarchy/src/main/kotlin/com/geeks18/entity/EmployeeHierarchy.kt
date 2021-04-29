package com.geeks18.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "Employee_Hierarchy")
class EmployeeHierarchy {
    @Id
    @Column(name = "name")
    var name: String = ""

    @Column(name = "SUPERVISOR_NAME")
    var supervisorName: String? = null

}