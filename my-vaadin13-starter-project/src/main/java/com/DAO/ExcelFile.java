package com.DAO;

import java.io.File;

public class ExcelFile {

	private int id;
	private File fileName;

	public ExcelFile() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public File getFileName() {
		return fileName;
	}

	public void setFileName(File fileName) {
		this.fileName = fileName;
	}

}
