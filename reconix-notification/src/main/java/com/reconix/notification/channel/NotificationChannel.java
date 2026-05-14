package com.reconix.notification.channel;

import com.reconix.notification.dto.FraudAlertEvent;

public interface NotificationChannel {
    
    /**
     * Envia notificação através do canal
     */
    void send(FraudAlertEvent event, String recipient);
    
    /**
     * Verifica se o canal está habilitado
     */
    boolean isEnabled();
    
    /**
     * Nome do canal
     */
    String getChannelName();
}
