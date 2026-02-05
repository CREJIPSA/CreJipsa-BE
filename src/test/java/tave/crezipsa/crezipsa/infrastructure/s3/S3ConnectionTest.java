package tave.crezipsa.crezipsa.infrastructure.s3;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;

@SpringBootTest
@ActiveProfiles("test")
@EnabledIfEnvironmentVariable(named = "RUN_S3_TESTS", matches = "true")
public class S3ConnectionTest {

	@Autowired
	private S3Client s3Client;

	@Test
	void listBuckets() {
		ListObjectsV2Response result = s3Client.listObjectsV2(
			ListObjectsV2Request.builder()
				.bucket(System.getenv("AWS_S3_BUCKET"))
				.build()
		);
		System.out.println(" 연결 성공 버킷 내 객체 수: " + result.contents().size());

	}
}
