package com.postora.postora_backend.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.postora.postora_backend.exceptions.InvalidFileTypeException;
import com.postora.postora_backend.services.FileStorageService;

@Service
public class FileStorageServiceImpl implements  FileStorageService{

	@Override
	public String saveFile(String path, MultipartFile file) throws IOException {
		// get file name
		String fileName = file.getOriginalFilename();
		
		// check for image (.jpg, .jpeg, .png)
		String fileExtension = fileName.substring(fileName.lastIndexOf('.'));
		if(!fileExtension.equals(".jpg") && !fileExtension.equals(".jpeg") && !fileExtension.equals(".png")) {
			throw new InvalidFileTypeException("Upload either of .jpg, .jpeg, or .png file only");
		}
		
		// generate new file name
		String newFileName = UUID.randomUUID().toString()
											  .concat(fileName);
		
		// create full path
		Path fullPath = Paths.get(path, newFileName);
		
		// create directory if not already exist
		File fileObj = new File(path);
		if(!fileObj.exists()) {
			fileObj.mkdirs();
		}
		
		//save file
		Files.copy(file.getInputStream(), fullPath);
		
		return newFileName;
		
	}

	@Override
	public InputStream getFile(String path, String fileName) throws IOException {
		// get full path
		Path fullPath = Paths.get(path, fileName);
		
		//get input stream
		InputStream inputStream = new FileInputStream(fullPath.toFile());
		
		return inputStream;
	}

	@Override
	public void deleteFile(String path, String fileName) {
		Path fullPath = Paths.get(path, fileName);
		File file = fullPath.toFile();
		if(file.exists()) {
			file.delete();
		}
	}

}
