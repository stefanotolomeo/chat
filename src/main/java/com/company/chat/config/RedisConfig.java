package com.company.chat.config;

import com.company.chat.dao.manager.MessageService;
import com.company.chat.dao.model.Audit;
import com.company.chat.dao.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

@Configuration
@ComponentScan(basePackageClasses = { MessageService.class, Message.class })
public class RedisConfig {

	// Setting up the Jedis connection factory.
	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		return new JedisConnectionFactory();
	}

	// Setting up the Redis templates object.
	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		final RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Object.class));
		redisTemplate.setEnableTransactionSupport(true);
		return redisTemplate;
	}

	@Bean
	public HashOperations<String, String, User> userHashOperations(){
		return redisTemplate().opsForHash();
	}

	@Bean
	public HashOperations<String, String, com.company.chat.dao.model.Message> messageHashOperations(){
		return redisTemplate().opsForHash();
	}

	@Bean
	public HashOperations<String, String, Audit> auditHashOperations(){
		return redisTemplate().opsForHash();
	}

	@Bean
	public ValueOperations<String, Object> valueOperations(){
		// (1) Initiliaze ValueOperations Caches (for Index)
		ValueOperations<String, Object> valueOperations = redisTemplate().opsForValue();
		valueOperations.setIfAbsent(Constants.INDEX_MESSAGE, 0L);
		valueOperations.setIfAbsent(Constants.INDEX_USER, 0L);
		valueOperations.setIfAbsent(Constants.INDEX_AUDIT, 0L);
		return valueOperations;
	}

}