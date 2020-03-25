//----------------------------------------------------------------------------------
//     Created By: Jonas Jahns
// Contributor(s): None
//    Description: Twitter API Integration
//----------------------------------------------------------------------------------
package org.kickercup.api.services.impl.social.twitter;

public class AuthorizationUrlResponse {
    private String authorizationUrl;

    public AuthorizationUrlResponse(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }

    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

    public void setAuthorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }
}
