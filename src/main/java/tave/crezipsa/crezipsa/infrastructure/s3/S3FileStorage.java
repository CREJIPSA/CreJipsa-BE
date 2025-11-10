package tave.crezipsa.crezipsa.infrastructure.s3;

import java.net.URL;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import tave.crezipsa.crezipsa.domain.file.port.FileStoragePort;

@Component
@RequiredArgsConstructor
public class S3FileStorage implements FileStoragePort {

	private final S3Client s3Client;
	private final S3Presigner s3Presigner;

	@Value("${infra.s3.bucket}")
	private String bucket;

	@Value("${infra.s3.region}")
	private String region;

	@Override
	public String generateUploadUrl(String fileName, String contentType) {

		PutObjectRequest put = PutObjectRequest.builder()
			.bucket(bucket)
			.key(fileName)
			.contentType(contentType)
			.acl("public-read")
			.build();

		PutObjectPresignRequest presign = PutObjectPresignRequest.builder()
			.signatureDuration(Duration.ofMinutes(10))
			.putObjectRequest(put)
			.build();

		PresignedPutObjectRequest result = s3Presigner.presignPutObject(presign);
		return result.url().toString();
	}


	@Override
	public String generateDownloadUrl(String fileName) {
		// fileUrl은 항상 접근 가능한 S3 정적 URL로 대체
		return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, fileName);
	}

	@Override
	public void deleteFile(String fileName) {
		DeleteObjectRequest req = DeleteObjectRequest.builder()
			.bucket(bucket)
			.key(fileName)
			.build();
		s3Client.deleteObject(req);
	}
}
