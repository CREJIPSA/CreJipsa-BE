package tave.crezipsa.crezipsa.application.search.usecase;

import tave.crezipsa.crezipsa.application.search.dto.response.UnifiedSearchResponse;

public interface SearchUsecase {
	UnifiedSearchResponse search(Long userId, String keyword);
}
