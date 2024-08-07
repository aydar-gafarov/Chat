package edu.school21.sockets.config;

import edu.school21.sockets.logic.Logic;
import edu.school21.sockets.service.InputMessage;
import edu.school21.sockets.service.OutputMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.*;
import java.net.Socket;
import java.util.List;

@Configuration
@ComponentScan("edu.school21.sockets")
@PropertySource(value = "classpath:application.properties")
public class ClientApplicationConfig {
    @Value("${server.port}")
    private int port;

    @Bean
    public Socket socket() throws IOException {
        return new Socket("localhost", port);
    }

    @Bean
    public InputMessage inputMessage() throws IOException {
        return new InputMessage(socket(), logic());
    }

    @Bean
    public OutputMessage outputMessage() throws IOException {
        return new OutputMessage(socket(), logic());
    }

    @Bean
    public BufferedReader reader() {
        return new BufferedReader(new InputStreamReader(System.in));
    }

    @Bean
    public BufferedWriter out() throws IOException {
        return new BufferedWriter(new OutputStreamWriter(socket().getOutputStream()));
    }

    @Bean
    public BufferedReader in() throws IOException {
        return new BufferedReader(new InputStreamReader(socket().getInputStream()));
    }

    @Bean
    public Logic logic() {
        return new Logic();
    }
}
