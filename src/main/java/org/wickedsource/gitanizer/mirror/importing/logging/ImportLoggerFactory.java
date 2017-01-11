package org.wickedsource.gitanizer.mirror.importing.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wickedsource.gitanizer.core.WorkdirConfiguration;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ImportLoggerFactory {

    private WorkdirConfiguration workdirConfiguration;

    private Map<String, Logger> loggers = new ConcurrentHashMap<>();

    @Autowired
    public ImportLoggerFactory(WorkdirConfiguration workdirConfiguration) {
        this.workdirConfiguration = workdirConfiguration;
    }

    public Logger getImportLoggerForMirror(long mirrorId, String loggerName) {
        String uniqueLoggerName = String.format("mirror-%d-%s", mirrorId, loggerName);
        Path logDir = workdirConfiguration.getWorkdir(mirrorId);
        Path logFile = logDir.resolve("subgit-import.log");

        // Caching loggers so that the same logger is not configured multiple times.
        // If a logger is configured multiple times, it would be added a new FileAppender each time,
        // resulting in multiple log outputs.
        Logger logger = loggers.get(uniqueLoggerName);
        if (logger == null) {
            logger = createLogger(logFile, uniqueLoggerName);
            loggers.put(uniqueLoggerName, logger);
        }

        return logger;
    }

    private Logger createLogger(Path logFile, String loggerName) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = (Logger) LoggerFactory.getLogger(loggerName);
        logger.setLevel(Level.INFO);
        logger.setAdditive(false);

        PatternLayoutEncoder layoutEncoder = new PatternLayoutEncoder();
        layoutEncoder.setPattern("%date %-5level %-20logger %msg%n");
        layoutEncoder.setContext(loggerContext);
        layoutEncoder.start();

        FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
        fileAppender.setFile(logFile.toAbsolutePath().toString());
        fileAppender.setEncoder(layoutEncoder);
        fileAppender.setContext(loggerContext);
        fileAppender.start();
        logger.addAppender(fileAppender);

        return logger;
    }

}
