//----------------------------------------------------------------------------------
//     Created By: Lucas Wierer
// Contributor(s): None
//    Description: Display all tournaments the user has ever created
//----------------------------------------------------------------------------------
<template>
  <b-container>
    <h1 class="mb-5 text-center">Turnierhistorie</h1>
    <b-table-simple hover small responsive class="text-center table-bordered table-striped">
      <b-thead>
        <b-tr>
          <b-th>NAME</b-th>
          <b-th>SPIELMODUS</b-th>
          <b-th>#TORE</b-th>
          <b-th>GEWERTET</b-th>
          <b-th>BEENDET</b-th>
          <b-th>AKTIONEN</b-th>
        </b-tr>
      </b-thead>
      <b-tbody>
        <b-tr v-for="tournament in tournaments" :key="tournament.id">
          <b-td
            @click="routeToTournamentResult(tournament)"
            class="tournament-name"
          >{{ tournament.name }}</b-td>
          <b-td>{{ generateGameModeTitle(tournament.gamemode) }}</b-td>
          <b-td>{{ tournament.amountGoals }}</b-td>
          <b-td>
            <i v-if="tournament.isRanked" class="fas fa-check"></i>
            <i v-else class="fas fa-times"></i>
          </b-td>
          <b-td>
            <i v-if="tournament.isFinished" class="fas fa-check"></i>
            <i v-else class="fas fa-times"></i>
          </b-td>
          <b-td>
            <div class="d-flex justify-content-start" style="max-width:110px; margin-left: auto;">
              <b-btn
                v-if="!tournament.isFinished"
                size="sm"
                class="primary ml-2"
                @click="routeToTournament(tournament)"
              >
                <i class="fas fa-play"></i>
              </b-btn>
              <b-btn
                v-if="tournament.isFinished"
                size="sm"
                class="primary ml-2"
                @click="routeToTournamentResult(tournament)"
              >
                <i class="fas fa-poll"></i>
              </b-btn>
              <b-btn
                size="sm"
                v-if="!tournament.isStarted"
                @click="deleteTournament(tournament.id)"
                class="secondary ml-2"
              >
                <i class="fas fa-trash"></i>
              </b-btn>
            </div>
          </b-td>
        </b-tr>
      </b-tbody>
    </b-table-simple>
  </b-container>
</template>

<script lang="ts">
import Vue from "vue";
import {
  TournamentControllerApi,
  Tournament
} from "../../typescript-fetch-client";
import UserService from "../../services/UserService";
import NotificationService from "../../services/NotificationService";

const Api = new TournamentControllerApi();

export default Vue.extend({
  data() {
    return {
      tournaments: [] as Tournament[]
    };
  },
  async mounted() {
    const userId = UserService.getUser().id;
    this.tournaments = await Api.getAllTournamentsUsingGET(userId);
  },
  methods: {
    routeToTournament(tournament: Tournament) {
      if (tournament.id === undefined) return;

      if (!tournament.isStarted) {
        this.$router.push(`tournament-players?tournament-id=${tournament.id}`);
      } else {
        this.$router.push(`/match-result-entry?tournament-id=${tournament.id}`);
      }
    },
    routeToTournamentResult(tournament: Tournament) {
      if (tournament.id === undefined) return;

      const query: { [key: string]: string } = {
        tournamentId: tournament.id.toString()
      };
      if (tournament.isFinished === true) {
        this.$router.push({ name: "tournament-tree", query });
      } else {
        NotificationService.info(
          "Das Turnier ist nicht beendet. Das Ergebnis kann nicht berechnet werden"
        );
      }
    },
    generateGameModeTitle(gameMode: Tournament.GamemodeEnum | undefined) {
      switch (gameMode) {
        case Tournament.GamemodeEnum.DoubleElimination:
          return "DoubleElimination";
        case Tournament.GamemodeEnum.RankedSoloMatch:
          return "Solo Match";
        case Tournament.GamemodeEnum.RankedTeamMatch:
          return "Team Match";
        case Tournament.GamemodeEnum.SingleKO:
          return "Single K.O.";
      }
    },
    async deleteTournament(id: number | undefined) {
      if (id === undefined) return;

      await Api.deleteTournamentUsingDELETE(id);
      let index = this.tournaments.findIndex(t => t.id === id);
      this.tournaments.splice(index, 1);
    }
  }
});
</script>

<style lang="scss" scoped>
.tournament-name {
  cursor: pointer;
}
</style>
