package tave.crezipsa.crezipsa.presentation.file.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.application.file.usecase.FileUseCase;
import tave.crezipsa.crezipsa.global.common.dto.GlobalResponseDto;
import tave.crezipsa.crezipsa.application.file.dto.response.UploadUrlResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileController {

	private final FileUseCase fileUseCase;

	@PostMapping("/upload")
	public GlobalResponseDto<UploadUrlResponse> createUploadUrl(
		@RequestParam String fileName,
		@RequestParam String contentType
	) {
		return GlobalResponseDto.success(fileUseCase.generateUploadUrl(fileName, contentType));
	}

	@DeleteMapping
	public GlobalResponseDto<Void> deleteFile(@RequestParam String fileName) {
		fileUseCase.deleteFile(fileName);
		return GlobalResponseDto.success(null);
	}

}
