package tave.crezipsa.crezipsa.domain.file.port;

public interface FileStoragePort {
	// 도메인이 외부 저장소에 의존하지 않도록 추상화
	String generateUploadUrl(String fileName, String contentType);
	String generateDownloadUrl(String fileName);
	void deleteFile(String fileName);
}
