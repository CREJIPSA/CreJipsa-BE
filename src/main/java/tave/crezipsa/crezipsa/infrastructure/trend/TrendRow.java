package tave.crezipsa.crezipsa.infrastructure.trend;

public record TrendRow(
		Long id,
		int rank,
		String keyword,
		String platform,
		String category,
		String trendDirection
) {
    public TrendRow(long id, String keyword, String category) {
        this(id, 0, keyword, null, category,null);
    }
    public TrendRow(long id, int rank, String keyword, String platform, String category){this(id, rank, keyword, platform, category, null);}
}
