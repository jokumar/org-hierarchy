package com.geeks18.repository

import com.geeks18.entity.EmployeeHierarchy
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface HierarchyJpaRepository : JpaRepository<EmployeeHierarchy, String?> {
    fun findByName(name: String): EmployeeHierarchy?
}