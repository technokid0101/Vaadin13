package com.DAO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * @author Sushil Pawar
 *
 */
public class CustomerDAOImpl implements CustomerDAO {

	private JdbcTemplate jdbcTemplate;

	public static DriverManagerDataSource getDataSource() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
		ds.setUrl("jdbc:mysql://localhost:3306/newschema");
		ds.setUsername("root");
		ds.setPassword("root");
		return ds;
	}

	public CustomerDAOImpl(DriverManagerDataSource ds) {
		jdbcTemplate = new JdbcTemplate(ds);
	}

	public CustomerDAOImpl() {

	}

	private static CustomerDAOImpl customerDAOImpl;

	public static CustomerDAOImpl getInstance() {
		if (customerDAOImpl == null) {
			return new CustomerDAOImpl(getDataSource());
		} else {
			return customerDAOImpl;
		}
	}

	@Override
	public int add(Customer customer) {
		String sql = "insert into vaadin values(?,?,?,?,?,?)";
		try {
			int counter = jdbcTemplate.update(sql,
					new Object[] { customer.getId(), customer.getFirstName(), customer.getLastName(),
							Date.valueOf(customer.getBirthDate()), customer.getStatus().toString(),
							customer.getEmail() });
			return counter;

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int delete(Long id) {
		String sql = "delete from vaadin where id=?";
		try {
			int counter = jdbcTemplate.update(sql, new Object[] { id });
			return counter;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int update(Customer customer) {
		String sql = "update vaadin set firstName=?,lastName=?,birthDate=?,status=?,email=? where id=?";
		try {
			int counter = jdbcTemplate.update(sql, new Object[] { customer.getFirstName(), customer.getLastName(),
					customer.getBirthDate(), customer.getStatus().toString(), customer.getEmail(), customer.getId() });
			return counter;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int uploadFile(File file, String fileName) {
		Random rId = new Random();
		int id = rId.nextInt();
		String sql = "insert into file_upload values(?,?,?)";
		try {
			Object object[] = new Object[] { id, file, fileName };
			int counter = jdbcTemplate.update(sql, object);
			return counter;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public List<Customer> findAll() {
		List<Customer> customers = jdbcTemplate.query("select * from vaadin", new RowMapper<Customer>() {
			@Override
			public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
				Customer customer = new Customer();
				customer.setId(rs.getLong(1));
				customer.setFirstName(rs.getString(2));
				customer.setLastName(rs.getString(3));
				customer.setBirthDate(rs.getDate(4).toLocalDate());
				customer.setStatus(CustomerStatus.valueOf(rs.getString(5)));
				customer.setEmail(rs.getString(6));
				return customer;
			}
		});
		return customers;
	}

	@Override
	public List<ExcelFile> findAllExcelFile() {
		List<ExcelFile> excelFiles = jdbcTemplate.query("select * from file_upload", new RowMapper<ExcelFile>() {
			@Override
			public ExcelFile mapRow(ResultSet rs, int rowNum) throws SQLException {
				ExcelFile excelFile = new ExcelFile();
				File file = null;
				excelFile.setId(rs.getInt(1));
				Blob blob = rs.getBlob(2);
				InputStream in = blob.getBinaryStream();
				OutputStream out;
				try {
					file = new File("F:\\DownloadFiles\\" + rs.getString(3));
					out = new FileOutputStream(file);
					int len = 0;
					byte[] buff = new byte[4096]; // how much of the blob to read/write at a time
					while ((len = in.read(buff)) != -1) {
						out.write(buff, 0, len);
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				excelFile.setFileName(file);
				file.delete();
				return excelFile;
			}

		});
		return excelFiles;
	}

}
