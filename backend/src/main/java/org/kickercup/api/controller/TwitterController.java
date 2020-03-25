//----------------------------------------------------------------------------------
//     Created By: Jonas Jahns
// Contributor(s): None
//    Description: REST Controller for Twitter API Integration
//----------------------------------------------------------------------------------
package org.kickercup.api.controller;

import org.kickercup.api.services.impl.social.twitter.AuthorizationUrlResponse;
import org.kickercup.api.services.impl.social.twitter.TwitterResource;
import org.kickercup.api.services.interfaces.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import twitter4j.TwitterException;

@RestController
@RequestMapping("/api/v1/twitter")
public class TwitterController {

    @Autowired
    private TwitterService service;

    @Autowired
    private TwitterResource twitterResource;

    @GetMapping("/reply/feedback")
    public AuthorizationUrlResponse replyFeedback(@RequestParam String message,
                                                  @RequestParam String callbackUrl) throws TwitterException {
        twitterResource = service.initialize((srv, res) -> srv.replyFeedback(res, message), callbackUrl);
        return new AuthorizationUrlResponse(twitterResource.getAuthorizationURL());
    }

    @GetMapping("/tweet/feedback")
    public AuthorizationUrlResponse tweetFeedback(@RequestParam String message, @RequestParam String callbackUrl) throws TwitterException {
        twitterResource = service.initialize((srv, res) -> srv.tweetFeedback(res, message), callbackUrl);
        return new AuthorizationUrlResponse(twitterResource.getAuthorizationURL());
    }

    @GetMapping("/callback")
    public RedirectView callback(@RequestParam(required = false, name = "oauth_token") String oauthToken,
                           @RequestParam(required = true, name = "oauth_verifier") String oauthVerifier) throws TwitterException {
        TwitterResource res = service.acquireConsent(twitterResource, oauthVerifier);
        service.executeAction(res);
        return new RedirectView(res.getCallbackUrl());
    }
}
