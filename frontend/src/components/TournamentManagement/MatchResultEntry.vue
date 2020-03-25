//----------------------------------------------------------------------------------
//     Created By: Johannes Schweer
// Contributor(s): None
//    Description: Tournament Result will be entered by the user on this page
//----------------------------------------------------------------------------------
<template>
  <b-container class="text-center">
    <h1 class="mb-5 mt-2">{{ tournament.name }}</h1>
    <b-row class="mb-2 border p-3" v-for="match in matches" :key="match.id">
      <b-col sm="2"></b-col>
      <b-col sm="4">
        <span v-if="tournament.gamemode != 'RankedSoloMatch'">
          {{ match.teamHome.playerLeft.firstName }}
          {{ match.teamHome.playerLeft.lastName }} &
          {{ match.teamHome.playerRight.firstName }}
          {{ match.teamHome.playerRight.lastName }} vs.
          {{ match.teamGuest.playerLeft.firstName }}
          {{ match.teamGuest.playerLeft.lastName }} &
          {{ match.teamGuest.playerRight.firstName }}
          {{ match.teamGuest.playerRight.lastName }}
        </span>
        <span v-if="tournament.gamemode == 'RankedSoloMatch'">
          {{ match.teamHome.playerLeft.firstName }}
          {{ match.teamHome.playerLeft.lastName }} vs.
          {{ match.teamGuest.playerLeft.firstName }}
          {{ match.teamGuest.playerLeft.lastName }}
        </span>
      </b-col>
      <b-col sm="4">
        <b-input
          class="mx-2"
          style="width: 60px; display: inline"
          v-model="match.goalsHome"
          :disabled="match.finished"
        />:
        <b-input
          class="mx-2"
          style="width: 60px; display: inline"
          v-model="match.goalsGuest"
          :disabled="match.finished"
        />
      </b-col>
    </b-row>

    <b-row>
      <b-col sm="12">
        <dx-button
          text="Spiele aktualisieren"
          type="default"
          class="primary btn-block mt-5 text-center"
          style="width: auto;"
          @click="updateResult"
        />
      </b-col>
    </b-row>
  </b-container>
</template>

<script lang="ts">
//import tournament from "../../assets/tournamentEntity";
import { DxButton } from "devextreme-vue";
import {
  Tournament,
  Match,
  MatchControllerApi,
  TournamentControllerApi
} from "../../typescript-fetch-client/index";
import Vue from "vue";
import UserService from "../../services/UserService";
import NotificationService from "../../services/NotificationService";

const TournamentApi = new TournamentControllerApi();
const MatchApi = new MatchControllerApi();

export default Vue.extend({
  components: {
    DxButton
  },
  computed: {
    tournamentId() {
      const tournamentId = Number(this.$route.query["tournament-id"]);
      return tournamentId;
    }
  },
  data() {
    return {
      tournament: {} as Tournament,
      matches: [] as Match[]
    };
  },
  async mounted() {
    const userId = UserService.getUserId();
    this.tournament = await TournamentApi.getTournamentByIdUsingGET(
      this.tournamentId
    );

    this.tournament.isStarted = true;
    this.tournament = await TournamentApi.updateTournamentUsingPUT(
      this.tournament,
      this.tournament.id as number
    );
    this.matches = await MatchApi.getAllMatchesUsingGET(this.tournamentId);
  },
  methods: {
    async updateResult() {
      for (const match of this.matches) {
        if (!match.finished && this.tournament.amountGoals != undefined) {
          if (
            (match.goalsGuest as number) > this.tournament.amountGoals ||
            (match.goalsHome as number) > this.tournament.amountGoals
          ) {
            NotificationService.error(
              "Die Tore dürfen nicht mehr als " +
                this.tournament.amountGoals +
                " sein."
            );
          } else {
            await MatchApi.updateMatchUsingPUT(
              match,
              match.id as number,
              this.tournamentId
            );
          }
        }
      }
      this.tournament = await TournamentApi.updateTournamentUsingPUT(
        this.tournament,
        this.tournamentId
      );
      this.matches = await MatchApi.getAllMatchesUsingGET(this.tournamentId);

      if (this.tournament.isFinished) {
        NotificationService.success(
          "Herzlichen Glückwunsch! Das Turnier ist beendet."
        );
        if (this.tournament.id === undefined) return;
        const query: {[key: string]: string} = { tournamentId: this.tournament.id.toString() } 
        this.$router.push({ name: "tournament-tree", query });
      } else {
        NotificationService.info("Ein oder mehrere Spiele sind noch nicht beendet");
      }
    }
  }
});
</script>

<style scoped>
.row-bordered:after {
  content: "";
  display: block;
  border-bottom: 1px solid #ccc;
  margin: 0 15px;
}
</style>
