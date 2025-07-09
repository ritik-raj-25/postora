package com.postora.postora_backend.services;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
	
	String saveFile(MultipartFile file);
	String getFile(String fileName);
	void deleteFile(String fileName);
}
