package com.postora.postora_backend.services.impl;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.postora.postora_backend.exceptions.InvalidFileTypeException;
import com.postora.postora_backend.services.FileStorageService;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class FileStorageServiceImpl implements  FileStorageService{
	
	@Value("${aws.bucketName}")
	private String bucketName;
	
	@Value("${aws.region}")
	private String region;
	
	private S3Client s3Client;
	
	public FileStorageServiceImpl(S3Client s3Client) {
		super();
		this.s3Client = s3Client;
	}

	@Override
	public String saveFile(MultipartFile file) {
		// get original file name
		String originalFileName = file.getOriginalFilename();
		
		// check for image (.jpg, .jpeg, .png)
		String fileExtension = file.getContentType();
		System.out.println(fileExtension);
		if(!fileExtension.equals("image/jpg") && !fileExtension.equals("image/jpeg") && !fileExtension.equals("image/png")) {
			throw new InvalidFileTypeException("Upload either of .jpg, .jpeg, or .png file only");
		}
		
		// generate new file name
		String newFileName = UUID.randomUUID().toString() + "_" + originalFileName;
											  
		// Build meta-data of object
		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(bucketName)
				.key(newFileName)
				.contentType(file.getContentType())
				.build();
		
		try {
			s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return newFileName;
	}

	@Override
	public String getFile(String fileName) {
		
		return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileName;
		
	}

	@Override
	public void deleteFile(String fileName) {
		DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
				.bucket(bucketName)
				.key(fileName)
				.build();
		
		s3Client.deleteObject(deleteObjectRequest);
	}

}
