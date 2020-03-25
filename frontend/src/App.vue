//----------------------------------------------------------------------------------
//     Created By: Lucas Wierer (Project Scaffolding)
// Contributor(s): None
//    Description: Main Application
//----------------------------------------------------------------------------------
<template>
  <div class="app">
    <nav-bar />
    <router-view />
    <home-button />
  </div>
</template>

<script>
import NavBar from "./components/NavBar/NavBar.vue";
import HomeButton from "./components/NavBar/HomeButton.vue";
import ColorService from "./services/ColorService";
import "@fortawesome/fontawesome-free/css/all.css";
import UserService from "./services/UserService";
import deMessages from "devextreme/localization/messages/de.json";
import { locale, loadMessages } from "devextreme/localization";
import { DesignControllerApi } from "./typescript-fetch-client";

const DesignApi = new DesignControllerApi();

export default {
  components: {
    NavBar,
    HomeButton
  },
  created() {
    loadMessages(deMessages);
    locale(navigator.language);
  },
  async mounted() {
    const user = UserService.getUserOrNull();

    if (user !== null) {
      const config = await DesignApi.listByUserUsingGET(user.id);
      ColorService.setColor(config);
    }
  },
  data() {
    return {};
  }
};
</script>

<style>
@import "./assets/css/style.css";
@import "./assets/css/theme.css";

.app {
  height: 100%;
}
</style>
