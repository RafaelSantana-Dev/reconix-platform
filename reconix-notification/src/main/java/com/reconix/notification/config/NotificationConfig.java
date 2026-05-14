package com.reconix.notification.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "notification")
public class NotificationConfig {
    
    private Email email = new Email();
    private Slack slack = new Slack();
    private Webhook webhook = new Webhook();
    private Websocket websocket = new Websocket();
    
    @Data
    public static class Email {
        private boolean enabled = true;
        private String from;
        private String fromName;
    }
    
    @Data
    public static class Slack {
        private boolean enabled = false;
        private String webhookUrl;
    }
    
    @Data
    public static class Webhook {
        private boolean enabled = true;
        private int timeoutSeconds = 10;
    }
    
    @Data
    public static class Websocket {
        private boolean enabled = true;
    }
}
