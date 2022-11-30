package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {

		DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
		
		System.out.println("---- INSERT ----");
		Department dpto = new Department(null,"Marketing");
		departmentDao.insert(dpto);
		System.out.println("Dados inseridos com sucesso! Id: " + dpto.getId());

	}

}
