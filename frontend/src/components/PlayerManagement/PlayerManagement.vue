//----------------------------------------------------------------------------------
//     Created By: Lucas Wierer
// Contributor(s): Christopher Heid (Minor changes)
//    Description: PlayerForm and PlayerGrid on a combined module
//----------------------------------------------------------------------------------
<template>
  <div class="container">
    <h1 class="mb-5 text-center">Spielerverzeichnis</h1>
    <player-form @player:create="createPlayer" />
    <player-grid :players="players" />
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import {
  Player,
  PlayerControllerApi
} from "../../typescript-fetch-client/index";
import PlayerGrid from "./PlayerGrid.vue";
import PlayerForm from "./PlayerForm.vue";
import UserService from "../../services/UserService";
import NotificationService from "../../services/NotificationService";

const Api = new PlayerControllerApi();

export default Vue.extend({
  props: ["redirect"],
  components: {
    "player-grid": PlayerGrid,
    "player-form": PlayerForm
  },
  data() {
    return {
      players: [] as Player[],
      currentPlayer: { emailId: "" } as Player
    };
  },
  async mounted() {
    this.players = await Api.getAllPlayersUsingGET(UserService.getUserId());
  },
  methods: {
    async createPlayer(payload: { player: Player }) {
      try {
        const player = await Api.createPlayerUsingPOST(
          payload.player,
          UserService.getUserId()
        );
        NotificationService.success("Spieler wurde erstellt.");
        this.players.push(player);
        if (this.redirect) {
          this.$router.push(this.redirect);
        }
      } catch {
        NotificationService.error("Da ist leider etwas schief gelaufen.");
      }
    }
  }
});
</script>

<style lang="scss" scoped></style>
