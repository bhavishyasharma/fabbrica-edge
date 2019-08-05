package in.co.futech.fabbricaedge;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@SpringBootApplication
@IntegrationComponentScan
@EnableScheduling
@EnableIntegration
public class FabbricaEdgeApplication implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Autowired
	private TaskScheduler taskScheduler;

	@Autowired
	private EdgeConfiguration edgeConfiguration;

	private static Random random = new Random();

	private List<ScheduledTask> scheduledTasks;

	public static void main(String[] args) {
		ConfigurableApplicationContext context = new SpringApplicationBuilder(FabbricaEdgeApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);
	}

	@PostConstruct
	public void initializeTasks() {
		HashMap<Integer, List<EdgeConfiguration.Measurement>> hashMap = new HashMap<Integer, List<EdgeConfiguration.Measurement>>();
		for(EdgeConfiguration.Measurement measurement: edgeConfiguration.getMeasurements()) {
			if(!hashMap.containsKey(measurement.getFrequency())) {
				List<EdgeConfiguration.Measurement> list = new ArrayList<EdgeConfiguration.Measurement>();
				list.add(measurement);
				hashMap.put(measurement.getFrequency(), list);
			}
			else {
				hashMap.get(measurement.getFrequency()).add(measurement);
			}
		}
		this.scheduledTasks = new ArrayList<>();
		for (HashMap.Entry<Integer, List<EdgeConfiguration.Measurement>> measurements : hashMap.entrySet()) {
			ScheduledTask scheduledTask = applicationContext.getBean(ScheduledTask.class , measurements.getValue());
			taskScheduler.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					scheduledTask.runTask();
				}
			}, measurements.getKey());
			scheduledTasks.add(scheduledTask);
		}
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

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
