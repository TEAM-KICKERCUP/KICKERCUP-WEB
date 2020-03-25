//----------------------------------------------------------------------------------
//     Created By: Jonas Jahns
// Contributor(s): None
//    Description: Twitter API Integration
//----------------------------------------------------------------------------------
package org.kickercup.api.services.impl.social.twitter;

import org.kickercup.api.services.interfaces.TwitterService;
import twitter4j.TwitterException;

@FunctionalInterface
public interface TwitterServiceAction {
    void execute(TwitterService service, TwitterResource twitterResource) throws TwitterException;
}
