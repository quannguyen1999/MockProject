package com.main;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;

//to enable @CreateDate,...
@EnableJpaAuditing
@SpringBootApplication
public class ManagementViettusBackEndApplication {

	//use host for socket
	@Value("${rt-server.host}")
	private String host;

	//use port for socket
	@Value("${rt-server.port}")
	private Integer port;

	//config message socket
	@Bean
	public SocketIOServer socketIOServer() {
		Configuration config = new Configuration();
		config.setHostname(host);
		config.setPort(port);
		return new SocketIOServer(config);
	}

	public static void main(String[] args) {
		SpringApplication.run(ManagementViettusBackEndApplication.class, args);
	}
}
