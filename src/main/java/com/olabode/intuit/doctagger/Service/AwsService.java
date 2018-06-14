package com.olabode.intuit.doctagger.Service;

import java.util.List;

import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3ObjectSummary;


import org.springframework.web.multipart.MultipartFile;

public interface AwsService {

    public PutObjectResult createFolder(String folderName);

    public Boolean deleteFolder(String FolderName);

    public PutObjectResult createFile(String folderName, MultipartFile fileToUpload);

    public Boolean deleteFile(String fileKey);

    List<S3ObjectSummary> listFolder(String folderName);
    
}
