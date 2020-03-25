//----------------------------------------------------------------------------------
//     Created By: Moritz Lugbauer
// Contributor(s): Lucas Wierer
//    Description: Tournament creation
//----------------------------------------------------------------------------------
<template>
  <b-container>
    <h2 class="text-center paddingTop">Neues Turnier erstellen</h2>

    <div class="row mt-5">
      <div class="col-sm"></div>
      <div class="col-sm">
        <div v-if="status === 'turniername'">
          <input
            class="form-control"
            type="text"
            placeholder="Turniername"
            v-model="formData.turniername"
          />
          <p class="text-center mt-4">
            Gib deinem Turnier hier einen Namen. Achtung, du kannst diesen Namen später nicht mehr ändern.
          </p>
        </div>

        <div v-else-if="status === 'turniermodus'">
          <div class="form-group">
            <label for="selectModus">Turniermodus:</label>
            <select
              class="form-control"
              id="selectModus"
              v-model="formData.gameMode"
            >
              <option
                :key="optionGame.value"
                v-for="optionGame in optionGameMode"
                v-bind:value="optionGame.value"
                >{{ optionGame.text }}</option
              >
            </select>
          </div>
          <div v-if="modusSelected">
            <p class="text-center mt-4">
              Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam
              nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam
              erat, sed diam voluptua. At vero eos et accusam et justo duo
              dolores et ea rebum. Stet clita kasd gubergren, no sea takimata
              sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit
              amet, consetetur sadipscing
            </p>
          </div>
        </div>

        <div v-if="status === 'torEingabe'">
          <div class="form-group">
            <label for="toreProSatz">Anzahl der Tore bis zum Sieg</label>
            <select
              class="form-control"
              id="toreProSatz"
              v-model="formData.amountGoals"
            >
              <option>1</option>
              <option>2</option>
              <option>3</option>
              <option>4</option>
              <option>5</option>
              <option>6</option>
              <option>7</option>
              <option>8</option>
              <option>9</option>
              <option>10</option>
            </select>
          </div>
          <div class="form-group">
            <label for="tunierWerten">Soll dieses Turnier gewertet werden?</label>
            <select
              class="form-control"
              id="tunierWerten"
              v-model="formData.isRanked"
            >
              <option
                :key="optionRanked.value"
                v-for="optionRanked in optionIsRanked"
                v-bind:value="optionRanked.value"
                >{{ optionRanked.text }}</option
              >
            </select>
          </div>
        </div>
      </div>
      <div class="col-sm"></div>
    </div>

    <div class="container mt-5">
      <div class="row text-center">
        <div class="col-sm-12 text-center">
          <dx-button
            @click="stopp"
            text="Abbrechen"
            type="default"
            class="secondary mr-3 buttonWidth"
          />
          <dx-button
            @click="next"
            text="Weiter"
            type="submit"
            class="primary ml-3 buttonWidth"
          />
        </div>
        
      </div>
    </div>
  </b-container>
</template>

<script lang="ts">
import Vue from "vue";
import {
  Tournament,
  TournamentControllerApi
} from "../../typescript-fetch-client/index";
import { DxForm, DxSimpleItem } from "devextreme-vue/form";
import { DxButton, DxValidationSummary } from "devextreme-vue";
import UserService from "../../services/UserService";
import NotificationService from "../../services/NotificationService";

const Api = new TournamentControllerApi();

export default Vue.extend({
  components: {
    DxForm,
    DxSimpleItem,
    DxButton,
    DxValidationSummary
  },

  data() {
    return {
      tournaments: [] as Tournament[],
      status: "turniername",
      modusSelected: false,
      formData: {
        turniername: "",
        gameMode: "",
        userId: "",
        amountGoals: "",
        isRanked: ""
      },
      optionIsRanked: [
        { text: "Ja", value: "true" },
        { text: "Nein", value: "false" }
      ],
      optionGameMode: [
        { text: "Solo Match", value: "RankedSoloMatch" },
        { text: "Team Match", value: "RankedTeamMatch" },
        { text: "Single K.O.", value: "SingleKO" }
      ]
    };
  },

  methods: {
    async createTournament(payload: {
      tournament: Tournament;
      userId: number;
    }) {
      const tournament = await Api.createTournamentUsingPOST(
        payload.tournament,
        payload.userId
      );
      this.tournaments.push(tournament);
    },
    async next() {
      if (this.status === "turniername") {
        if (this.formData.turniername.trim() === "") {
          NotificationService.error(
          "Der Turniername darf nicht leer sein."
          );
        } else {
          this.status = "turniermodus";
        }
      } else if (this.status === "turniermodus") {
        if (this.formData.gameMode === "") {
          NotificationService.error(
          "Bitte wähle einen Turniermodus aus."
          );
        } else {
          this.status = "torEingabe";
        }
      } else if (this.status === "torEingabe") {
        console.log(this.formData.amountGoals);
        if (this.formData.amountGoals === "" || this.formData.amountGoals === undefined) {
          NotificationService.error(
          "Bitte Anzahl der Tore bis zum Sieg auswählen."
          );
        } else if(this.formData.isRanked === "") {
          NotificationService.error(
          "Bitte auswählen ob das Turnier gewertet werden soll."
          );
        } else {
          let result = await Api.createTournamentUsingPOST(
          {
            name: this.formData.turniername,
            amountGoals: Number(this.formData.amountGoals),
            gamemode: (this.formData
              .gameMode as unknown) as Tournament.GamemodeEnum,
            isRanked: (this.formData.isRanked as unknown) as boolean,
            amountSets: 0
          },
          UserService.getUserId()
        );
        this.$router.push(`/tournament-players?tournament-id=${result.id}`);
        }
      }
    },
    stopp() {
      this.$router.push("/tournamentmanagement");
    }
  }
});
</script>

<style lang="scss" scoped>
.paddingTop {
  padding-top: 10%;
}

.buttonWidth {
  min-width: 110px;
}
</style>
