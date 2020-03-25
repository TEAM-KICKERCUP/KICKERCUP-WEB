//----------------------------------------------------------------------------------
//     Created By: Jonas Jahns
// Contributor(s): None
//    Description: Tournament Result Table
//----------------------------------------------------------------------------------
package org.kickercup.api.services.impl.tournament.result;

import org.kickercup.api.services.impl.tournament.result.table.ResultTable;

import java.util.List;

public class TournamentResult {
    private String header;
    private List<String> subHeaders;
    private ResultTable table;

    public TournamentResult(String title, List<String> subHeaders, ResultTable table) {
        this.header = title;
        this.table = table;
        this.subHeaders = subHeaders;
    }

    public ResultTable getTable() {
        return table;
    }

    public void setTable(ResultTable table) {
        this.table = table;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<String> getSubHeaders() {
        return subHeaders;
    }

    public void setSubHeaders(List<String> subHeaders) {
        this.subHeaders = subHeaders;
    }
}
