//----------------------------------------------------------------------------------
//     Created By: Jonas Jahns
// Contributor(s): None
//    Description: Twitter API Integration
//----------------------------------------------------------------------------------
package org.kickercup.api.services.impl.social.twitter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import javax.validation.constraints.NotNull;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "twitter")
public class TwitterConfiguration {

    @NotNull
    private String consumerApiKey;

    @NotNull
    private String consumerApiSecretKey;

    @NotNull
    private String callbackUrl;

    @NotNull
    private String username;

    public String getConsumerApiKey() {
        return consumerApiKey;
    }

    public void setConsumerApiKey(String consumerApiKey) {
        this.consumerApiKey = consumerApiKey;
    }

    public String getConsumerApiSecretKey() {
        return consumerApiSecretKey;
    }

    public void setConsumerApiSecretKey(String consumerApiSecretKey) {
        this.consumerApiSecretKey = consumerApiSecretKey;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
