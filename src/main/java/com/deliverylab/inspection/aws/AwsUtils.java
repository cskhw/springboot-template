package com.deliverylab.inspection.aws;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.util.IOUtils;

@Configurable
@Component
public class AwsUtils {
    @Autowired
    private AmazonS3 s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    // S3에 파일 업로드
    public PutObjectResult upload(String path, MultipartFile file) throws Exception {
        try {
            // 파일 이름, 업로드
            String fileName = file.getOriginalFilename();
            String filePath = path + fileName;
            InputStream inputStream = file.getInputStream();

            // 메타데이터 설정
            ObjectMetadata metadata = new ObjectMetadata();

            byte[] bytes = IOUtils.toByteArray(inputStream);
            metadata.setContentLength(bytes.length);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

            return s3Client
                    .putObject(new PutObjectRequest(bucketName, filePath, byteArrayInputStream, metadata));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
