package pe.edu.cibertec.ProyectoDAWI.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class HikariCpConfig {

    @Value("${DB_FABRIC_URL}")
    private String dbUrl;
    @Value("${DB_FABRIC_USER}")
    private String dbUser;
    @Value("${DB_FABRIC_PASS}")
    private String dbPass;
    @Value("${DB_FABRIC_DRIVER}")
    private String dbDriver;

    @Bean
    public HikariDataSource hikariDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(this.dbUrl);
        config.setUsername(this.dbUser);
        config.setPassword(this.dbPass);
        config.setDriverClassName(this.dbDriver);

        // Verificación de configuración
        System.out.println("DB URL: " + this.dbUrl);
        System.out.println("DB USER: " + this.dbUser);
        System.out.println("DB PASS: " + this.dbPass);
        System.out.println("DB DRIVER: " + this.dbDriver);

        // Otras configuraciones
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(5);
        config.setIdleTimeout(300000L);
        config.setConnectionTimeout(30000L);

        return new HikariDataSource(config);
    }
}
