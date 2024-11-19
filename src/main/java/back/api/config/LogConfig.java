package back.api.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.filter.LevelFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.spi.FilterReply;
import jakarta.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogConfig {

    @PostConstruct
    public void configureLogback() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        // Configura o Appender para INFO
        FileAppender<ILoggingEvent> infoAppender = createFileAppender(context, "Logs/info.log", Level.INFO);
        context.getLogger("ROOT").addAppender(infoAppender);

        // Configura o Appender para WARN
        FileAppender<ILoggingEvent> warnAppender = createFileAppender(context, "Logs/warn.log", Level.WARN);
        context.getLogger("ROOT").addAppender(warnAppender);

        // Configura o Appender para ERROR
        FileAppender<ILoggingEvent> errorAppender = createFileAppender(context, "Logs/error.log", Level.ERROR);
        context.getLogger("ROOT").addAppender(errorAppender);
    }

    private FileAppender<ILoggingEvent> createFileAppender(LoggerContext context, String filePath, Level level) {
        FileAppender<ILoggingEvent> appender = new FileAppender<>();
        appender.setContext(context);
        appender.setFile(filePath);

        // Configura o formato do log
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss} %-5level [%thread] %logger{36} - %msg%n");
        encoder.start();

        appender.setEncoder(encoder);

        // Adiciona o filtro de nível
        LevelFilter filter = createLevelFilter(level);
        appender.addFilter(filter);

        appender.start();
        return appender;
    }

    private LevelFilter createLevelFilter(Level level) {
        LevelFilter filter = new LevelFilter();
        filter.setLevel(level);
        filter.setOnMatch(FilterReply.ACCEPT);
        filter.setOnMismatch(FilterReply.DENY);
        filter.start();
        return filter;
    }
}