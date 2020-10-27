package com.company.chat.config;

import com.company.chat.websocket.component.WebSocketListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@ComponentScan(basePackageClasses = { WebSocketListener.class })
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	private final String WS_ENDPOINT = "/websocketApp";
	private final String WS_API_PREFIX_ENDPOINT = "/app";
	private final String WS_TOPIC_PREFIX = "/topic";

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint(WS_ENDPOINT).withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {

		registry.setApplicationDestinationPrefixes(WS_API_PREFIX_ENDPOINT);
		registry.enableSimpleBroker(WS_TOPIC_PREFIX);

		// TODO: future improvements
		/*ReactorNettyTcpClient<byte[]> client = new ReactorNettyTcpClient<>(tcpClient -> tcpClient.host("localhost").port(61616).noSSL(),
				new StompReactorNettyCodec());

		registry.setApplicationDestinationPrefixes("/app");
		registry.enableStompBrokerRelay("/topic")
				.setAutoStartup(true)
				//.setSystemLogin("admin")
				//.setSystemPasscode("admin")
				.setClientLogin("guest")
				.setClientPasscode("guest")
				.setTcpClient(client);
		*/
	}
}
