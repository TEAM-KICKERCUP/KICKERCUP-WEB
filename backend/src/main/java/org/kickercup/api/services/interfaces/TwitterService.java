//----------------------------------------------------------------------------------
//     Created By: Jonas Jahns
// Contributor(s): None
//    Description: Twitter API Service interface 
//----------------------------------------------------------------------------------
package org.kickercup.api.services.interfaces;

import org.kickercup.api.services.impl.social.twitter.TwitterResource;
import org.kickercup.api.services.impl.social.twitter.TwitterServiceAction;
import twitter4j.TwitterException;

public interface TwitterService {
    TwitterResource initialize(TwitterServiceAction action, String callbackUrl) throws TwitterException;
    TwitterResource acquireConsent(TwitterResource twitterResource, String pin) throws TwitterException;
    void executeAction(TwitterResource twitterResource) throws TwitterException;

    void updateStatus(TwitterResource twitterResource, String message) throws TwitterException;
    void sendReply(TwitterResource twitterResource, String message, String recipient) throws TwitterException;
    void tweetFeedback(TwitterResource twitterResource, String message) throws TwitterException;
    void replyFeedback(TwitterResource twitterResource, String message) throws TwitterException;
}
