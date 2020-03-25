//----------------------------------------------------------------------------------
//     Created By: Lucas Wierer
// Contributor(s): Christopher Heid
//    Description: Sing-up as a new user
//----------------------------------------------------------------------------------
<template>
  <div class="container">
    <h1 class="title">Neuen Benutzer erstellen</h1>

    <user-form @submit="handleSubmit" :user="user" />
  </div>
</template>

<script lang="ts">
import Vue from "vue";

import { DxForm, DxSimpleItem } from "devextreme-vue/form";
import { DxButton, DxValidationSummary } from "devextreme-vue";
import { User, UserControllerApi } from "../../typescript-fetch-client";
import UserForm from "./UserForm.vue";
import NotificationService from "../../services/NotificationService";

const Api = new UserControllerApi();

export default Vue.extend({
  components: {
    DxForm,
    DxSimpleItem,
    DxButton,
    DxValidationSummary,
    UserForm
  },
  data() {
    return {
      user: {} as User
    };
  },
  methods: {
    handleSubmit(user: User) {
      console.log(user);
      this.user = user;
      this.save();
    },
    async save() {
      try {
        await Api.createUserUsingPOST(this.user);
        this.$router.push("/");
        NotificationService.success(
          "Dein Benutzer wurde erfolgreich erstellt. Du kannst dich ab jetzt anmelden."
        );
      } catch (e) {
        NotificationService.error(JSON.stringify(e));
      }
    }
  }
});
</script>

<style lang="scss" scoped>
.container {
  padding: 0 20px 20px 20px;
  max-width: 1000px;
  margin: auto;
}

.flex-center {
  display: flex;
  justify-content: center;
}

.title {
  text-align: center;
  margin-bottom: 50px;
}

.dx-fieldset .dx-button {
  margin: 10px;
}
</style>