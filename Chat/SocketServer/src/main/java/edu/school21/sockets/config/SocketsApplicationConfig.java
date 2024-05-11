package edu.school21.sockets.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.UsersRepository;
import edu.school21.sockets.repositories.UsersRepositoryImpl;
import edu.school21.sockets.server.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import edu.school21.sockets.services.UserServiceImpl;

import javax.sql.DataSource;

@Configuration
@ComponentScan("edu.school21.sockets")
@PropertySource("classpath:db.properties")
public class SocketsApplicationConfig {
    @Value("${db.url}")
    private String url;

    @Value("${db.password}")
    private String password;

    @Value("${db.user}")
    private String username;

    @Value("${db.driver.name}")
    private String driverName;

    @Bean
    public DataSource hikariDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setPassword(password);
        hikariConfig.setUsername(username);
        hikariConfig.setDriverClassName(driverName);
        return new HikariDataSource(hikariConfig);
    }
//    @Bean
//    public DataSource driverDataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource(url, username, password);
//        dataSource.setDriverClassName(driverName);
//        return dataSource;
//    }
    @Bean(name = "usersRepository")
    public UsersRepository<User> usersRepository() {return new UsersRepositoryImpl(hikariDataSource());
    }
    @Bean(name = "userServiceImpl")
    public UserServiceImpl UserServiceImpl() {
        return new UserServiceImpl(usersRepository());
    }

//    @Bean
//    public UsersRepositoryJdbcTemplateImpl usersRepositoryJdbcTemplate() {
//        return new UsersRepositoryJdbcTemplateImpl(driverDataSource());
//    }
}
