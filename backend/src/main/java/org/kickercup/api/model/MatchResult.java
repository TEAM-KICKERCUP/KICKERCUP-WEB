//----------------------------------------------------------------------------------
//     Created By: Lucas Wierer
// Contributor(s): None
//    Description: Match Result enum
//----------------------------------------------------------------------------------
package org.kickercup.api.model;

public enum MatchResult {
    WON,
    LOST,
    DRAW;

    public double getActualPoints() {
        switch (this) {
            case WON:
                return 1;
            case DRAW:
                return 0.5;
            case LOST:
                return 0;
            default:
                throw new IllegalArgumentException("invalid enum: " + this);
        }
    }
}
