package org.aksw.simba.autoindex.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class MultipartFileHandler{
	
	private List<String> fileList = new ArrayList<String>();
	
	public void validate(MultipartFile multipartFile) {
		if (multipartFile.isEmpty()) {
			throw new IllegalArgumentException("Illegal multipart File");
		}
		
		if(multipartFile.getName().contains("..") || multipartFile.getOriginalFilename().contains("..") ) { //Security Check to prevent HDD Manipulation on Linux
			throw new IllegalArgumentException("Illegal multipart File, Avoid tricks to manipulate");
		}
	}
	public void store(MultipartFile file) throws IOException {
		validate(file);
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		if(filename.contains("..")) {
			throw new IllegalArgumentException("Invalid File Name");
		}
		Path path = Paths.get(".");
		Files.copy(file.getInputStream(), path.resolve(filename),
                StandardCopyOption.REPLACE_EXISTING);
		Path filePath = Paths.get(filename);
		this.fileList.add(filePath.toAbsolutePath().toString());
	}
	public List<String> getFiles() {
		return this.fileList;
	}
	public void deleteAllFiles() throws IOException {
		for(String file : fileList) {
			Files.deleteIfExists(Paths.get(file));
		}
	}
}