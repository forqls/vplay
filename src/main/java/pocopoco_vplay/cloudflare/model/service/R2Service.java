package pocopoco_vplay.cloudflare.model.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class R2Service {
	
	private final S3Client s3Client;
	private final String bucketName;
	private final String publicUrl;
	
	public R2Service(
			@Value("${aws.credentials.access-key}") String accessKey,
            @Value("${aws.credentials.secret-key}") String secretKey,
            @Value("${aws.s3.bucket-name}") String bucketName,
            @Value("${cloudflare.r2.public-url}") String publicUrl,
            @Value("${aws.s3.endpoint}") String endpoint) {
		
		 this.s3Client = S3Client.builder()
	                .region(Region.of("auto")) // R2는 region 개념 없음
	                .endpointOverride(URI.create(endpoint))
	                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
	                .build();
	     this.bucketName = bucketName;
	     this.publicUrl = publicUrl;
	}
	
	public String uploadFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .build(),
                RequestBody.fromBytes(file.getBytes()));

          return publicUrl + "/" + fileName;
          
    }
	
	public boolean deleteFile(String fileName) {
		 try {
		        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
		                .bucket(bucketName)
		                .key(fileName)
		                .build();
		
		        s3Client.deleteObject(deleteRequest);
		        System.out.println("✅ 파일 삭제 성공: " + fileName);
		        return true;
		    } catch (SdkException e) {
		        System.err.println("❌ 파일 삭제 실패: " + fileName);
		        e.printStackTrace();
		        return false;
		    }
	}
	
	public byte[] downloadFile(String fileName) {
	    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
	            .bucket(bucketName)
	            .key(fileName)
	            .build();

	    try (InputStream inputStream = s3Client.getObject(getObjectRequest);
	         ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

	        byte[] buffer = new byte[4096];
	        int bytesRead;
	        while ((bytesRead = inputStream.read(buffer)) != -1) {
	            outputStream.write(buffer, 0, bytesRead);
	        }

	        System.out.println("✅ 파일 다운로드 성공: " + fileName);
	        return outputStream.toByteArray();

	    } catch (IOException | SdkException e) {
	        System.err.println("❌ 파일 다운로드 실패: " + fileName);
	        e.printStackTrace();
	        return null;
	    }
	}
}
