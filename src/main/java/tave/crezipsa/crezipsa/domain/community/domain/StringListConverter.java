package tave.crezipsa.crezipsa.domain.community.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

//imageUrls 컬럼을 JSON 형식으로 DB에 저장하기 위한 컨버터
@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(List<String> attribute) {
		try {
			return objectMapper.writeValueAsString(attribute == null ? new ArrayList<>() : attribute);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("이미지 URL 직렬화 실패", e);
		}
	}

	@Override
	public List<String> convertToEntityAttribute(String dbData) {
		try {
			return objectMapper.readValue(dbData, new TypeReference<>() {});
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

}
