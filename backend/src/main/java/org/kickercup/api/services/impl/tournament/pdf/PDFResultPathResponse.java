//----------------------------------------------------------------------------------
//     Created By: Jonas Jahns
// Contributor(s): None
//    Description: PDF Service
//----------------------------------------------------------------------------------
package org.kickercup.api.services.impl.tournament.pdf;

public class PDFResultPathResponse {
    private String resourcePath;

    public PDFResultPathResponse(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }
}
