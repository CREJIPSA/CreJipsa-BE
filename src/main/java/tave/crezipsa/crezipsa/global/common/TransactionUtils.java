package tave.crezipsa.crezipsa.global.common;

import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public final class TransactionUtils {

	private TransactionUtils() {}

	/**
	 * 활성 트랜잭션이 있으면 커밋 후에, 없으면 즉시 실행한다.
	 * 캐시 퇴거를 트랜잭션 커밋 이전에 수행하면 다른 요청이 커밋 전 스냅샷을
	 * 캐시에 채워넣는 레이스 컨디션이 발생하므로 이 메서드로 연기한다.
	 */
	public static void afterCommit(Runnable action) {
		if (TransactionSynchronizationManager.isActualTransactionActive()) {
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
				@Override
				public void afterCommit() {
					action.run();
				}
			});
		} else {
			action.run();
		}
	}
}
