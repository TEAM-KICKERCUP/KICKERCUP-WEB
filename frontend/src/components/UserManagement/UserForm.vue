//----------------------------------------------------------------------------------
//     Created By: Lucas Wierer
// Contributor(s): Christopher Heid
//    Description: Form which displays the users account information
//----------------------------------------------------------------------------------
<template>
  <b-form @submit="onSubmit">
    <b-row class="my-3">
      <b-col>
        <b-form-input
          required
          placeholder="Dein Benutzername"
          v-model="user.username"
        />
      </b-col>
      <b-col>
        <b-form-input
          required
          type="email"
          placeholder="E-Mail"
          v-model="user.email"
        />
      </b-col>
    </b-row>
    <b-row class="my-3">
      <b-col>
        <b-form-input required placeholder="Vorname" v-model="user.firstName" />
      </b-col>
      <b-col>
        <b-form-input required placeholder="Nachname" v-model="user.lastName" />
      </b-col>
    </b-row>
    <b-row class="my-3">
      <b-col>
        <b-form-input
          type="password"
          required
          placeholder="Passwort"
          v-model="user.password"
        />
      </b-col>
      <b-col>
        <b-form-input
          type="password"
          required
          placeholder="Passwort wiederholen"
          v-model="passwordRepeat"
        />
      </b-col>
    </b-row>
    <div class="dx-fieldset flex-center">
      <dx-button
        class="primary m-3"
        text="Speichern"
        :useSubmitBehavior="true"
        type="default"
      />
      <dx-button
        class="secondary m-3"
        @click="cancel"
        text="Abbrechen"
        type="default"
      />
      <b-btn type="submit" style="display: none">Ok</b-btn>
    </div>
  </b-form>
</template>

<script lang="ts">
import Vue from "vue";
import { User } from "../../typescript-fetch-client";
import NotificationService from "../../services/NotificationService";
import { DxButton, DxValidationSummary } from "devextreme-vue";

export default Vue.extend({
  components: {
    DxButton
  },
  props: {
    user: {
      type: Object as () => User,
      default: {} as User
    }
  },
  data() {
    return {
      passwordRepeat: ""
    };
  },
  methods: {
    cancel() {
      this.$router.go(-1);
    },
    onSubmit(evt: any) {
      evt.preventDefault();
      this.save();
    },
    async save() {
      if (this.user.password !== this.passwordRepeat) {
        NotificationService.error(
          "Die eingegebenen Passwörter stimmen nicht überein"
        );

        return;
      }
      this.$emit("submit", this.user);
    }
  }
});
</script>

<style scoped>
.flex-center {
  display: flex;
  justify-content: center;
}
</style>
