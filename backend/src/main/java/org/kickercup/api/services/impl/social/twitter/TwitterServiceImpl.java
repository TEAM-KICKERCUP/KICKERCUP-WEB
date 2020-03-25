//----------------------------------------------------------------------------------
//     Created By: Jonas Jahns
// Contributor(s): None
//    Description: Twitter API Integration
//----------------------------------------------------------------------------------
package org.kickercup.api.services.impl.social.twitter;

import org.kickercup.api.services.interfaces.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import twitter4j.*;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class TwitterServiceImpl implements TwitterService {

    private static final Logger LOGGER = Logger.getLogger(TwitterServiceImpl.class.getName());

    private TwitterFactory twitterFactory;

    @Autowired
    TwitterConfiguration twitterConfiguration;

    public TwitterServiceImpl(TwitterConfiguration twitterConfiguration) {
        this.twitterConfiguration = twitterConfiguration;
        if (!checkIfVariablesAreAvailable()) {
            throw new NullPointerException("Twitter environment variables are not present!");
        }
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(twitterConfiguration.getConsumerApiKey())
                .setOAuthConsumerSecret(twitterConfiguration.getConsumerApiSecretKey());
        twitterFactory = new TwitterFactory(cb.build());
    }

    public void tweetFeedback(TwitterResource twitterResource, String message) throws TwitterException {
        updateStatus(twitterResource, "@" + twitterConfiguration.getUsername() + " " + message);
    }

    public void replyFeedback(TwitterResource twitterResource, String message) throws TwitterException {
        sendReply(twitterResource, message, twitterConfiguration.getUsername());
    }

    public void updateStatus(TwitterResource twitterResource, String message) throws TwitterException {
        LOGGER.log(Level.INFO, "Updating status ");
        Twitter twitter = twitterResource.getTwitter();

        Status status = twitter.updateStatus(message);
        LOGGER.log(Level.INFO, "Tweet successfully updated on " + status.getCreatedAt());
    }

    public void sendReply(TwitterResource twitterResource, String message, String recipient) throws TwitterException {
        LOGGER.log(Level.INFO, "Sending reply ");
        Twitter twitter = twitterResource.getTwitter();

        DirectMessage directMessage = twitter.sendDirectMessage(recipient, message);
        LOGGER.log(Level.INFO, "Reply successfully sent on " + directMessage.getCreatedAt());
    }

    @Override
    public TwitterResource initialize(TwitterServiceAction action, String callbackUrl) throws TwitterException {
        Twitter twitter = twitterFactory.getInstance();
        RequestToken requestToken = twitter.getOAuthRequestToken(twitterConfiguration.getCallbackUrl());
        String authorizationURL = requestToken.getAuthorizationURL();
        return new TwitterResource(twitter, requestToken, authorizationURL, action, callbackUrl);
    }

    @Override
    public TwitterResource acquireConsent(TwitterResource twitterResource, String pin) throws TwitterException {
        if (null == twitterResource || null == twitterResource.getTwitter()) {
            throw new TwitterException("Resource cannot be empty");
        }
        Twitter twitter = twitterResource.getTwitter();
        try {
            if (pin.length() > 0) {
                twitter.getOAuthAccessToken(twitterResource.getRequestToken(), pin);
            } else {
                twitter.getOAuthAccessToken(twitterResource.getRequestToken());
            }
        } catch (IllegalStateException | TwitterException ie) {
            LOGGER.log(Level.WARNING, "Unable to get the access token, error message: " + ie.getMessage());

            // access token is already available, or consumer key/secret is not set.
            if (!twitter.getAuthorization().isEnabled()) {
                LOGGER.log(Level.SEVERE, "OAuth consumer key/secret is not set");
                throw new TwitterException("Failed getting access token");
            }
        }
        twitterResource.setTwitter(twitter);
        return twitterResource;
    }

    @Override
    public void executeAction(TwitterResource twitterResource) throws TwitterException {
        twitterResource.getTwitterAction().execute(this, twitterResource);
        LOGGER.log(Level.INFO, "Twitter action executed");
    }

    public boolean checkIfVariablesAreAvailable() {
        boolean apiKeyAvailable = variableIsPresent(twitterConfiguration.getConsumerApiKey(), "Consumer API Key does not exist");
        boolean apiSecretKeyAvailable = variableIsPresent(twitterConfiguration.getConsumerApiSecretKey(), "Consumer API Secret Key does not exist");
        boolean callbackUrlAvailable = variableIsPresent(twitterConfiguration.getCallbackUrl(), "Callback Url does not exist");

        return apiKeyAvailable && apiSecretKeyAvailable && callbackUrlAvailable;
    }

    private boolean variableIsPresent(String variable, String errorMessage) {
        if (null == variable || variable.isEmpty()) {
            LOGGER.warning(errorMessage);
            return false;
        }
        return true;
    }
}
