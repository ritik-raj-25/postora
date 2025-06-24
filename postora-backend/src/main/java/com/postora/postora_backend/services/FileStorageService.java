package com.postora.postora_backend.services;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
	
	String saveFile(String path, MultipartFile file) throws IOException;
	InputStream getFile(String path, String fileName) throws IOException;
	void deleteFile(String path, String fileName);
}
