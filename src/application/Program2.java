package application;

import java.util.ArrayList;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {

		DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
		
		System.out.println("---- INSERT ----");
		Department dpto = new Department(null,"Marketing");
//		departmentDao.insert(dpto);
//		System.out.println("Dados inseridos com sucesso! Id: " + dpto.getId());
		
		System.out.println("---- FIND BY ID ----");
		dpto = departmentDao.findById(5);
		System.out.println(dpto);
		
		System.out.println("---- UPDATE ----");
		dpto.setName("TI");
		departmentDao.update(dpto);
		System.out.println("Update success!");
		
		System.out.println("---- SELECT ----");
		List<Department> listDpto = new ArrayList<>();
		listDpto = departmentDao.findAll();
		listDpto.forEach(System.out::println);

	}

}
