//----------------------------------------------------------------------------------
//     Created By: Lucas Wierer
// Contributor(s): Moritz Lugbauer (Added functionality)
//    Description: Player Form to create new players
//----------------------------------------------------------------------------------
<template>
  <div>
    <b-form @submit="submit">
      <b-form-input
        class="my-3"
        placeholder="Vorname"
        v-model="formData.firstName"
        required
      />
      <b-form-input
        class="my-3"
        placeholder="Nachname"
        v-model="formData.lastName"
        required
      />
      <b-form-input
        class="my-3"
        placeholder="E-Mail"
        type="email"
        v-model="formData.emailId"
        required
      />

      <dx-button
        id="button"
        :useSubmitBehavior="true"
        text="Spielerdaten speichern"
        type="default"
        class="primary my-3"
      />
      <div v-if="tournamentId">
        <dx-button
          id="button"
          :useSubmitBehavior="true"
          text="Zurück zur Turniererstellung"
          type="default"
          class="primary my-3"
          @click="backToTournament"
        />
      </div>
    </b-form>
  </div>
</template>

<script>
import { DxForm, DxSimpleItem } from "devextreme-vue/form";
import { DxButton, DxValidationSummary } from "devextreme-vue";

export default {
  components: {
    DxForm,
    DxSimpleItem,
    DxButton,
    DxValidationSummary
  },
  computed: {
    tournamentId() {
      const tournamentId = Number(this.$route.query["tournament-id"]);
      return tournamentId;
    }
  },
  data() {
    return {
      formData: {
        firstName: "",
        lastName: "",
        emailId: ""
      }
    };
  },
  methods: {
    submit(e) {
      e.preventDefault();
      this.$emit("player:create", {
        player: this.formData
      });
    },
    backToTournament() {
      this.$router.push(
        `/tournament-players?tournament-id=${this.tournamentId}`
      );
    }
  }
};
</script>

<style lang="scss" scoped>
#summary {
  padding-left: 10px;
  margin-top: 20px;
  margin-bottom: 10px;
}

#button {
  display: block;
}
</style>
