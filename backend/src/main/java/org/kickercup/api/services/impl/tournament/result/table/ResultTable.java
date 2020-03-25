//----------------------------------------------------------------------------------
//     Created By: Jonas Jahns
// Contributor(s): None
//    Description: Tournament Result Table
//----------------------------------------------------------------------------------
package org.kickercup.api.services.impl.tournament.result.table;

import java.util.List;

public class ResultTable {
    private List<ResultTableEntry> entries;
    private List<String> columnHeaders;

    public ResultTable() {

    }

    public ResultTable(List<ResultTableEntry> entries, List<String> columnHeaders) {
        this.entries = entries;
        this.columnHeaders = columnHeaders;
    }

    public List<ResultTableEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<ResultTableEntry> entries) {
        this.entries = entries;
    }

    public List<String> getColumnHeaders() {
        return columnHeaders;
    }

    public void setColumnHeaders(List<String> columnHeaders) {
        this.columnHeaders = columnHeaders;
    }
}
