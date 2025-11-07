package tave.crezipsa.crezipsa.application.file.usecase;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.domain.file.port.FileStoragePort;
import tave.crezipsa.crezipsa.presentation.file.response.DownloadUrlResponse;
import tave.crezipsa.crezipsa.presentation.file.response.UploadUrlResponse;

@Service
@RequiredArgsConstructor
public class FileUseCase {

	private final FileStoragePort fileStoragePort;


	public UploadUrlResponse generateUploadUrl(String fileName, String contentType) {
		String uploadUrl = fileStoragePort.generateUploadUrl(fileName, contentType);
		return new UploadUrlResponse(fileName, uploadUrl);
	}


	public DownloadUrlResponse generateDownloadUrl(String fileName) {
		String downloadUrl = fileStoragePort.generateDownloadUrl(fileName);
		return new DownloadUrlResponse(downloadUrl);
	}


	public void deleteFile(String fileName) {
		fileStoragePort.deleteFile(fileName);
	}

}
