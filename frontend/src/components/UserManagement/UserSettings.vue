//----------------------------------------------------------------------------------
//     Created By: Christopher Heid
// Contributor(s): Lucas Wierer
//    Description: Change existing user account
//----------------------------------------------------------------------------------
<template>
  <b-container>
    <h1 class="text-center mb-5">Mein Benutzerkonto</h1>
    <user-form v-if="loadedUserData" :user="user" @submit="handleSubmit" />
  </b-container>
</template>

<script lang="ts">
import Vue from "vue";
import UserForm from "./UserForm.vue";
import { User, UserControllerApi } from "../../typescript-fetch-client";
import { DxButton } from "devextreme-vue";
import UserService from "../../services/UserService";
import NotificationService from "../../services/NotificationService";

const Api = new UserControllerApi();

export default Vue.extend({
  components: {
    UserForm,
    DxButton
  },
  data() {
    return {
      user: {} as User,
      loadedUserData: false
    };
  },
  async mounted() {
    this.user = await Api.getUserByIdUsingGET(UserService.getUserId());
    this.user.password = "";
    this.loadedUserData = true;
  },
  methods: {
    handleSubmit(user: User) {
      this.user = user;
      this.updateUser();
    },
    async updateUser() {
      try {
        const user = await Api.updateUserUsingPUT(
          this.user,
          UserService.getUserId()
        );
        UserService.login(user);
        NotificationService.success("Dein Benutzerkonto wurde erfolgreich aktualisiert");
      } catch (e) {
        console.error(e);
        NotificationService.error(
          "Dein Benutzerkonto wurde aufgrund eines Fehlers nicht aktualisiert werden. Versuche es später bitte erneut"
        );
      }
    }
  }
});
</script>

<style scoped></style>
