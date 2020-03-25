//----------------------------------------------------------------------------------
//     Created By: Jonas Jahns
// Contributor(s): None
//    Description: Displays the Tournament Tree with results after a tournament is finished
//----------------------------------------------------------------------------------
<template>
  <div class="container">
    <h1 class="mt-3 mb-5 text-center">Turnierergebnisse</h1>
    <div class="tournament-result">
      <h1>{{ result.header }}</h1>
      <p v-bind:key="sub" v-for="sub of result.subHeaders">{{ sub }}</p>
      <dx-data-grid
        :data-source="result.table.entries"
        :allow-column-reordering="false"
        :show-borders="true"
        :row-alternation-enabled="true"
      >
        <dx-column data-field="place" sort-order="asc" caption="PLATZ" alignment="left"/>
        <dx-column data-field="players" caption="SPIELER" alignment="right" />
        <dx-column data-field="wins" caption="GEWONNENE SPIELE" />
        <dx-column data-field="goalsScored" caption="ERZIELTE TORE" />
        <dx-column data-field="counterGoals" caption="GEGENTORE" />
      </dx-data-grid>
    </div>
    <dx-button @click="downloadPDF" title="Download Turnierergebnisse" type="default" class="secondary download-result-button" v-if="tournamentId !== undefined" v-cloak>
      <i class="fas fa-file-download download-result-icon"></i>
    </dx-button>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import { DxButton } from "devextreme-vue";
import { DxDataGrid, DxColumn } from "devextreme-vue/data-grid";
import ColorService from "../../services/ColorService";
import { Design } from "../../typescript-fetch-client";
import NotificationService from "../../services/NotificationService";
import {
  TournamentControllerApi,
  BaseAPI,
  BASE_PATH,
  TournamentResult,
  TournamentTableEntry
} from "../../typescript-fetch-client";

const API: TournamentControllerApi = new TournamentControllerApi();

enum DisplayType {
  table = "table",
  tree = "tree"
}

export default Vue.extend({
  components: {
    DxButton,
    DxDataGrid,
    DxColumn
  },
  data() {
    return {
      displayType: DisplayType.table,
      tournamentId: undefined,
      result: {
        "header": " ",
        "subHeaders": [
            " ",
            " "
        ],
        "table": {
            "entries": [
                {
                    "players": [
                        " "
                    ],
                    "place": 0,
                    "goalsScored": 0,
                    "counterGoals": 0,
                    "wins": 0
                }
            ],
            "columnHeaders": [
                "Rank",
                "Player",
                "Player"
            ]
        }
      }
    };
  },
  async mounted() {
    this.$data.tournamentId = this.$route.query.tournamentId;
    try {
      const result: TournamentResult = await API.getTournamentResultUsingGET(this.$data.tournamentId, "de");
      this.$data.result = this.postFix(result);
    } catch (e) {
      console.log("Failed to get result", e);
      NotificationService.error("Das Turnierergbnis konnte leider nicht erstellt werden.")    
    }
  },
  methods: {
    postFix(result: TournamentResult): TournamentResult {
      if (!result || !result.table || !result.table.entries) return result;
      result.table.entries = result.table.entries.map((entry, index) => {
        // remove empty right player
        if (entry.players && (!entry.players[1] || entry.players[1] == "" || entry.players[1] == " ")) {
          entry.players =  [entry.players[0]];
        } else if (entry.players && !entry.players[1].startsWith(" ")) {
          entry.players[1] = " " + entry.players[1];
        }
        return entry;
      });
      return result;
    },
    setDisplayTypeTable() {
      this.setDisplayType(DisplayType.table);
    },
    setDisplayTypeTree() {
      this.setDisplayType(DisplayType.tree);
    },
    setDisplayType(displayType: DisplayType) {
      this.$data.displayType = displayType;
    },
    async downloadPDF() {
      let relativeUrl: any;
      try { 
        relativeUrl = await API.getTournamentResultPDFUsingGET(this.$data.tournamentId)
      } catch (e) {
        console.log("The tournament result pdf could not be build, error message: ", e);
        NotificationService.error("Das PDF mit den Turnierergebnissen konnte nicht erstellt werden");
      }

      try {
        await this.downloadFile(
          BASE_PATH + relativeUrl.resourcePath,
          `tournamentResult_${this.$data.result.header.replace(" ", "_")}.pdf`
        );
      } catch (e) {
        console.log("The tournament result pdf could not be downloaded, error message: ", e);
        NotificationService.error("Das PDF mit den Turnierergebnissen konnte nicht heruntergeladen werden")
      }
    },
    async downloadFile(url: string, fileName: string) {
      return await fetch(url).then(response => {
        return response.blob().then((file) => {
            var a = document.createElement("a");
            a.href = URL.createObjectURL(file);
            a.setAttribute("download", fileName);
            a.click();
          }
        );
      });
    }
  }
});
</script>

<style lang="scss" scoped>
.download-result-button {
  margin-top: 1rem;
  .download-result-icon {
    font-size: 1.3rem;
  }
}
</style>
