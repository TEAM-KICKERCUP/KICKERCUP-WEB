//----------------------------------------------------------------------------------
//     Created By: Moritz Lugbauer
// Contributor(s): Lucas Wierer
//    Description: Selection of players which should attend to a tournament 
//----------------------------------------------------------------------------------
<template>
  <div>
    <div class="container">
      <h1 class="text-center paddingTop">Turnierteilnehmer</h1>
      <div class="row align-items-center">
        <div class="col-5">
          <div class="paddingTopSearch">
            <h3 class="text-center">Spielerverzeichnis</h3>
            <dx-data-grid
              id="gridContainerSearchTable"
              :data-source="players"
              :show-borders="true"
              :hover-state-enabled="true"
              :selection="{ mode: 'multiple' }"
              :row-alternation-enabled="true"
              @selection-changed="onSelectionChangedRightTabelle"
            >
              <dx-search-panel :visible="true" placeholder="SUCHEN"/>
              <dx-column data-field="firstName" caption="VORNAME" />
              <dx-column data-field="lastName" caption="NACHNAME" />
              <dx-column data-field="rankScore" caption="SPIELERNIVEAU" />
            </dx-data-grid>
          </div>

          <div class="row fixed-row-bottom">
            <dx-button
              @click="newPlayer"
              text="Neuen Spieler erstellen"
              type="default"
              class="primary buttonWidth mr-4"
            />
            <dx-button
              @click="editPlayer"
              text="Spieler bearbeiten"
              type="default"
              class="primary buttonWidth"
            />
          </div>
        </div>

        <div class="col-2 paddingTop">
          <span style="font-size: 70px;">
            <i @click="putPlayerLeft" class="fas fa-arrow-circle-left"></i>
            <i @click="putPlayerRight" class="fas fa-arrow-circle-right"></i>
          </span>
        </div>

        <div class="col-5">
          <h3 class="text-center">Mitspieler</h3>

          <div class="paddingTop">
            <dx-data-grid
              id="gridContainer"
              :data-source="playerTournament"
              :show-borders="true"
              :hover-state-enabled="true"
              :selection="{ mode: 'multiple' }"
              :row-alternation-enabled="true"
              @selection-changed="onSelectionChangedLeftTabelle"
            >
              <dx-column data-field="firstName" caption="VORNAME" />
              <dx-column data-field="lastName" caption="NACHNAME" />
              <dx-column data-field="rankScore" caption="SPIELERNIVEAU" />
            </dx-data-grid>
          </div>

          <div class="row fixed-row-bottom">
            <dx-button
              @click="exit"
              text="Abbrechen"
              type="default"
              class="secondary buttonWidth mr-4"
            />
            <dx-button
              @click="save"
              text="Turnier starten "
              type="default"
              class="primary buttonWidth mr-4"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import {
  PlayerControllerApi,
  Player,
  TournamentControllerApi
} from "../../typescript-fetch-client";
import UserService from "../../services/UserService";
import NotificationService from "../../services/NotificationService";

const Api = new PlayerControllerApi();
const ApiTournament = new TournamentControllerApi();

import { DxForm, DxSimpleItem } from "devextreme-vue/form";
import { DxButton, DxValidationSummary,  } from "devextreme-vue";
import {
  DxDataGrid,
  DxScrolling,
  DxSorting,
  DxLoadPanel,
  DxColumn,
  DxFilterRow,
  DxSearchPanel
} from "devextreme-vue/data-grid";

export default Vue.extend({
  components: {
    DxForm,
    DxSimpleItem,
    DxButton,
    DxValidationSummary,
    DxDataGrid,
    DxScrolling,
    DxSorting,
    DxLoadPanel,
    DxColumn,
    DxSearchPanel
  },

  computed: {
    tournamentId(): number {
      const tournamentId = Number(this.$route.query["tournament-id"]);
      return tournamentId;
    }
  },

  data() {
    return {
      players: [] as Player[],
      playerTournament: [] as Player[],
      selectedPlayerRight: [] as Player[],
      selectedPlayerLeft: [] as Player[]
    };
  },

  async mounted() {
    const userId = UserService.getUser().id;
    this.players = await Api.getAllPlayersUsingGET(userId);
  },

  methods: {
    search() {
      alert("Nach Spielern suchen");
    },
    newPlayer() {
      this.$router.push(`/playermanagement?tournament-id=${this.tournamentId}`);
    },
    editPlayer() {
      this.$router.push(`/playermanagement?tournament-id=${this.tournamentId}`);
    },
    async save() {
      try {
        let result = await ApiTournament.teamMakingUsingPOST(
          this.tournamentId,
          this.playerTournament
        );
        this.$router.push(
          `/match-result-entry?tournament-id=${this.tournamentId}`
        );
      } catch (error) {
        NotificationService.error(
          "Das Turnier konnte mit dieser Teilnehmeranzahl nicht gestartet werden."
          );
      }
      
    },
    exit() {
      this.$router.push("/tournamentmanagement");
    },
    putPlayerLeft() {
      if(this.selectedPlayerLeft.length == 0) {
        NotificationService.error(
          "Bitte einen Spieler in der rechten Tabelle auswählen."
          );
      } else {
        this.players.push(...this.selectedPlayerLeft);
        for (let player of this.selectedPlayerLeft) {
          const index = this.playerTournament.findIndex(e => e.id === player.id)
          this.playerTournament.splice(index, 1);
          this.selectedPlayerLeft = [];
        }
      }
    },
    putPlayerRight() {
      if(this.selectedPlayerRight.length == 0) {
        NotificationService.error(
          "Bitte einen Spieler in der linken Tabelle auswählen."
          );
      } else {
        this.playerTournament.push(...this.selectedPlayerRight);
        for (let player of this.selectedPlayerRight) {
          const index = this.players.findIndex(e => e.id === player.id)
          this.players.splice(index, 1);
          this.selectedPlayerRight = [];
        }
      } 
    },
    onSelectionChangedRightTabelle({ selectedRowsData }: any) {
      this.$data.selectedPlayerRight = selectedRowsData;
    },
    onSelectionChangedLeftTabelle({ selectedRowsData }: any) {
      this.$data.selectedPlayerLeft = selectedRowsData;
    }
  }
});
</script>

<style lang="scss" scoped>
.paddingTop {
  padding-top: 8%;
}

.paddingTopSearch {
  padding-top: 6%;
}

.buttonWidth {
  min-width: 175px;
}

.fixed-row-bottom {
  position: fixed;
  bottom: 20%;
}

#gridContainer {
  max-height: 250px;
  min-height: 250px;
}

#gridContainerSearchTable {
  max-height: 300px;
  min-height: 300px;
}
 
</style>
