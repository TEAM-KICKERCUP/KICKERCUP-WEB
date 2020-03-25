//----------------------------------------------------------------------------------
//     Created By: Jonas Jahns
// Contributor(s): None
//    Description: Twitter API Integration
//----------------------------------------------------------------------------------
package org.kickercup.api.services.impl.social.twitter;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import twitter4j.Twitter;
import twitter4j.auth.RequestToken;

@Component
@Scope(value="session", proxyMode= ScopedProxyMode.TARGET_CLASS)
public class TwitterResource {
    private Twitter twitter;
    private RequestToken requestToken;
    private String authorizationURL;
    private TwitterServiceAction twitterAction;
    private String callbackUrl;

    public TwitterResource() {}

    public TwitterResource(Twitter twitter, RequestToken requestToken, String authorizationURL) {
        this.twitter = twitter;
        this.requestToken = requestToken;
        this.authorizationURL = authorizationURL;
    }

    public TwitterResource(Twitter twitter, RequestToken requestToken, String authorizationURL, TwitterServiceAction twitterAction, String callbackUrl) {
        this.twitter = twitter;
        this.requestToken = requestToken;
        this.authorizationURL = authorizationURL;
        this.twitterAction = twitterAction;
        this.callbackUrl = callbackUrl;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public Twitter getTwitter() {
        return twitter;
    }

    public void setTwitter(Twitter twitter) {
        this.twitter = twitter;
    }

    public RequestToken getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(RequestToken requestToken) {
        this.requestToken = requestToken;
    }

    public String getAuthorizationURL() {
        return authorizationURL;
    }

    public void setAuthorizationURL(String authorizationURL) {
        this.authorizationURL = authorizationURL;
    }

    public TwitterServiceAction getTwitterAction() {
        return twitterAction;
    }

    public void setTwitterAction(TwitterServiceAction twitterAction) {
        this.twitterAction = twitterAction;
    }
}
