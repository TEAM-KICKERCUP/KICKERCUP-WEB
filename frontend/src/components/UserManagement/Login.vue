//----------------------------------------------------------------------------------
//     Created By: Lucas Wierer
// Contributor(s): Christopher Heid
//    Description: Startpage to login as a user
//----------------------------------------------------------------------------------
<template>
  <b-container>
    <b-form class="text-center" @submit="login">
      <div class="row">
        <div class="col-sm"></div>
        <div class="col-sm">
          <img src="../../assets/images/logo.png" class="img-fluid mt-5 mb-5" />

          <input v-model="username" class="form-control mb-3" placeholder="Benutzername" />
          <input
            v-model="password"
            type="password"
            class="form-control"
            placeholder="Passwort"
            required
          />

          <dx-button
            :useSubmitBehavior="true"
            text="Anmelden"
            type="default"
            class="primary btn-block mt-3"
          />
        </div>
        <div class="col-sm"></div>
      </div>
    </b-form>

    <b-row style="margin-top: 100px;">
      <b-col sm="6"></b-col>
      <b-col sm="2">
        <label>Noch nicht registriert?</label>
      </b-col>
      <b-col sm="3">
        <router-link to="/usermanagement">
          <dx-button text="Konto erstellen" type="default" class="primary btn-block mt-3" />
        </router-link>
      </b-col>
    </b-row>
  </b-container>
</template>

<script lang="ts">
import Vue from "vue";
import {
  User,
  UserControllerApi,
  DesignControllerApi
} from "../../typescript-fetch-client/index";
import { DxForm, DxSimpleItem } from "devextreme-vue/form";
import { DxButton, DxValidationSummary } from "devextreme-vue";
import UserService from "../../services/UserService";
import ColorService from "../../services/ColorService";
import NotificationService from "../../services/NotificationService";

const api = new UserControllerApi();
const DesignApi = new DesignControllerApi();

export default Vue.extend({
  components: {
    DxForm,
    DxSimpleItem,
    DxButton,
    DxValidationSummary
  },
  data() {
    return {
      username: "",
      password: ""
    };
  },

  methods: {
    async login(e: any) {
      e.preventDefault();
      let user = await api.authUserUsingPOST({
        username: this.username,
        password: this.password
      });

      if (user.id != 0) {
        UserService.login(user);
        this.$router.push("/tournamentmanagement");
        const config = await DesignApi.listByUserUsingGET(user.id as number);
        ColorService.setColor(config);
      } else {
        NotificationService.error(
          "Dieser Benutzer existiert nicht oder das Passwort ist falsch."
        );
      }
    }
  }
});
</script>

<style lang="scss" scoped></style>
