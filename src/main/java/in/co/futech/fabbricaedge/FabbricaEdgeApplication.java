package in.co.futech.fabbricaedge;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Random;

@SpringBootApplication
@IntegrationComponentScan
@EnableScheduling
public class FabbricaEdgeApplication {

	@Autowired
	private EdgeConfiguration edgeConfiguration;

	private static Random random = new Random();

	public static void main(String[] args) {
		new SpringApplicationBuilder(FabbricaEdgeApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);
	}

	@Bean
	public MqttPahoClientFactory createMqttClientFactory() {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		MqttConnectOptions options = new MqttConnectOptions();
		options.setServerURIs(new String[]{edgeConfiguration.getBrokerUrl()});
		options.setUserName(edgeConfiguration.getUsername());
		options.setPassword(edgeConfiguration.getPassword().toCharArray());
		factory.setConnectionOptions(options);
		return factory;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttChannel")
	public MessageHandler sendMessage() {
		MqttPahoMessageHandler messageHandler =
				new MqttPahoMessageHandler(edgeConfiguration.getClientid(), createMqttClientFactory());
		messageHandler.setAsync(true);
		messageHandler.setDefaultTopic(edgeConfiguration.getClientid());
		return messageHandler;
	}

	@Bean
	public MessageChannel mqttChannel() {
		return new DirectChannel();
	}

}
