package com.olabode.intuit.doctagger.Service.Impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.olabode.intuit.doctagger.Data.Repository.DocumentMetadataRepository;
import com.olabode.intuit.doctagger.Service.AwsService;
import com.amazonaws.services.s3.model.DeleteObjectRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class AwsServiceImpl implements AwsService {
    @Autowired
    private DocumentMetadataRepository documentMetadataRepository;

    private static final String FolderEndString = "/";
    private static final String FileExistSuffix = "copy";

    @Value("${aws-bucket-name}")
    private String bucketName;


    @Override
    public PutObjectResult createFolder(String folderName) {
        PutObjectResult folderCreateResult = null;
        try{
            folderCreateResult = getS3Service().putObject(bucketName, formatFolderName(folderName), "");
        }
        catch (AmazonClientException ex) {
            //TO DO: Do Something with this exception
        }

        return folderCreateResult;
    }

    @Override
    public Boolean deleteFolder(String folderName) {

        Boolean folderDeleted = false;

           try{
                getS3Service().deleteObject(new DeleteObjectRequest(bucketName, formatFolderName(folderName)));
                folderDeleted = true;
           }
           catch (AmazonClientException ex) {
               folderDeleted = false;
               //TO DO: Do Something with this exception
           }
           return folderDeleted;
    }

    @Override
    public PutObjectResult createFile(String folderName, MultipartFile fileToUpload) {
        AmazonS3 s3Client = getS3Service();
        String fileUploadKey = getFileUploadKey(formatFileName(folderName, fileToUpload), s3Client);
        PutObjectResult fileCreateResult = null;

        try{
            fileCreateResult = getS3Service().putObject(new PutObjectRequest(bucketName, fileUploadKey, multipartToFile(fileToUpload)));
        }
        catch (AmazonClientException ex) {
            //TO DO: Do Something with this exception
        }
        catch (IOException ex){

        }

        return fileCreateResult;
    }

    @Override
    public Boolean deleteFile(String fileKey) {
        Boolean fileDeleted = false;

        try{
            getS3Service().deleteObject(new DeleteObjectRequest(bucketName, fileKey));
            documentMetadataRepository.deleteByDocumentKey(fileKey);
            fileDeleted = true;
        }
        catch (AmazonClientException ex) {
            fileDeleted = false;
            //TO DO: Do Something with this exception
        }
        return fileDeleted;
    }

    @Override
    public List<S3ObjectSummary> listFolder(String folderName) {
        String folderKey = formatFolderName(folderName);
        return getS3Service().listObjects(new ListObjectsRequest().withBucketName(bucketName).withPrefix(folderName)).getObjectSummaries();
    }

    private AmazonS3 getS3Service(){
        ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
        try {
            credentialsProvider.getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                            "Please make sure that your credentials file is at the correct " +
                            "location (~/.aws/credentials), and is in valid format.",
                    e);
        }

        return AmazonS3ClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withRegion("us-west-2")
                .build();

    }



    private String formatFolderName(String folderName){
        return (folderName.endsWith(FolderEndString)) ? folderName :  String.format("%s%s",folderName, FolderEndString);
    }

    private String formatFileName(String folderName, String fileName){
        return String.format("%s%s", formatFolderName(folderName), fileName);
    }

    private String formatFileName(String folderName, MultipartFile fileToUpload){
        return formatFileName(folderName, fileToUpload.getOriginalFilename());
    }


    private String getFileUploadKey(String fileUploadName, AmazonS3 s3Client){
        int i = 1;
        while(s3Client.doesObjectExist(bucketName, fileUploadName)){
            String suffix = String.format("%s%d", FileExistSuffix, i);
            fileUploadName = String.format("%s%s", fileUploadName.replace(suffix, ""), suffix);
            getFileUploadKey(fileUploadName, s3Client);
        }

        return fileUploadName;
    }

    public File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException
    {
        File tempFile = new File( multipart.getOriginalFilename());
        multipart.transferTo(tempFile);
        return tempFile;
    }

}
