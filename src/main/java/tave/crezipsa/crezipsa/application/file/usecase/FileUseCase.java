package tave.crezipsa.crezipsa.application.file.usecase;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.domain.file.port.FileStoragePort;
import tave.crezipsa.crezipsa.application.file.dto.response.UploadUrlResponse;

@Service
@RequiredArgsConstructor
public class FileUseCase {

	private final FileStoragePort fileStoragePort;


	public UploadUrlResponse generateUploadUrl(String fileName, String contentType) {
		String uploadUrl = fileStoragePort.generateUploadUrl(fileName, contentType);
		String fileUrl = fileStoragePort.generateDownloadUrl(fileName);
		return new UploadUrlResponse(fileName, uploadUrl, fileUrl);
	}

	public void deleteFile(String fileName) {
		fileStoragePort.deleteFile(fileName);
	}

}
