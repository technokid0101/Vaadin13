package com.DAO;

import java.io.File;
import java.util.List;

/**
 * @author Sushil Pawar
 *
 */
public interface CustomerDAO {
	int add(Customer customer);

	List<Customer> findAll();

	int delete(Long id);

	int update(Customer customer);

	int uploadFile(File file,String fileName);

	List<ExcelFile> findAllExcelFile();
}
