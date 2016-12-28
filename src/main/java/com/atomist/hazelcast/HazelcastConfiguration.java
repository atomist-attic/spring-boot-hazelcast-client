package com.atomist.hazelcast;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;

@Configuration
public class HazelcastConfiguration {

	@Bean
	public ClientConfig clientConfig() {
		ClientConfig clientConfig = new ClientConfig();
		
		clientConfig.getNetworkConfig().addAddress("127.0.0.1");
		
		return clientConfig;
	}
	
	/**
	 * <P>Temporary. Spring Boot should soon autoconfigure
	 * the {@code HazelcastInstance} bean.
	 * </P>
	 */
	@Configuration
	@ConditionalOnMissingBean(HazelcastInstance.class)
	static class HazelcastClientConfiguration {
		
		@Bean
		public HazelcastInstance hazelcastInstance(ClientConfig clientConfig) {
			return HazelcastClient.newHazelcastClient(clientConfig);
		}
	}
	
}
