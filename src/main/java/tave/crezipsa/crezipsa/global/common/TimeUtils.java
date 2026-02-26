package tave.crezipsa.crezipsa.global.common;

import java.time.LocalDateTime;

public final class TimeUtils {

	private TimeUtils() {}

	public static String convertToRelativeTime(LocalDateTime createdAt) {
		LocalDateTime now = LocalDateTime.now();
		long minutes = java.time.Duration.between(createdAt, now).toMinutes();
		long hours = minutes / 60;
		long days = hours / 24;

		if (minutes < 1) return "방금 전";
		if (minutes < 60) return minutes + "분 전";
		if (hours < 24) return hours + "시간 전";
		return days + "일 전";
	}

	public static String preview(String content) {
		return content.substring(0, Math.min(50, content.length()));
	}
}
