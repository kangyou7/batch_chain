package com.glyde.mall.batch.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 트랜잭션매니저를 ChainedTransactionManager로 변경하는 설정
 * 
 * @author zeta
 *
 */
@Configuration
public class ChainTxConfig {

	@Bean(name="chainedTransactionManager")
	@Primary
	public PlatformTransactionManager transactionManager(@Qualifier("txManagerGlyde") PlatformTransactionManager tx1,
			@Qualifier("txManagerCJ") PlatformTransactionManager tx2) {
		return new ChainedTransactionManager(tx1, tx2);
	}
}
