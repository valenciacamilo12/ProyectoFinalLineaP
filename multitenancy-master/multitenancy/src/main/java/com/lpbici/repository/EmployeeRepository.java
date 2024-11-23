package com.lpbici.repository;

import org.springframework.data.repository.CrudRepository;

import com.lpbici.entity.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Long>{

}
