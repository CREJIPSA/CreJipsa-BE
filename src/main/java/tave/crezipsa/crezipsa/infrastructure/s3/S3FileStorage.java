package tave.crezipsa.crezipsa.infrastructure.s3;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import tave.crezipsa.crezipsa.domain.file.port.FileStoragePort;



@Component
@RequiredArgsConstructor
public class S3FileStorage  implements FileStoragePort {

	private final S3Client s3Client;
	private final S3Presigner s3Presigner;

	@Value("${infra.s3.bucket}")
	private String bucket;

	@Override
	public String generateUploadUrl(String fileName, String contentType) {
		PutObjectRequest put = PutObjectRequest.builder()
			.bucket(bucket)
			.key(fileName)
			.contentType(contentType)
			.build();

		PutObjectPresignRequest presign = PutObjectPresignRequest.builder()
			// 필요 시 만료시간 조정
			.signatureDuration(Duration.ofMinutes(10))
			.putObjectRequest(put)
			.build();

		PresignedPutObjectRequest result = s3Presigner.presignPutObject(presign);
		return result.url().toString();
	}

	@Override
	public String generateDownloadUrl(String fileName) {
		GetObjectRequest get = GetObjectRequest.builder()
			.bucket(bucket)
			.key(fileName)
			.build();

		GetObjectPresignRequest presign = GetObjectPresignRequest.builder()
			.signatureDuration(Duration.ofMinutes(10))
			.getObjectRequest(get)
			.build();

		PresignedGetObjectRequest result = s3Presigner.presignGetObject(presign);
		return result.url().toString();
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
