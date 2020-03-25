//----------------------------------------------------------------------------------
//     Created By: Lucas Wierer
// Contributor(s): None
//    Description: User Settings Menu
//----------------------------------------------------------------------------------
<template>
  <b-container class="text-center">
    <h1 class="mb-5">Einstellungen</h1>

    <router-link to="/user-settings">
      <dx-button class="primary btn-block mt-4" text="Mein Benutzerkonto" type="default" />
    </router-link>
    <router-link to="/playermanagement">
      <dx-button class="primary btn-block mt-4" text="Spielerverzeichnis" type="default" />
    </router-link>
    <router-link to="/statistic">
      <dx-button class="primary btn-block mt-4" text="Statistiken" type="default"></dx-button>
    </router-link>
    <router-link to="/design-configurator">
      <dx-button class="primary btn-block mt-4" text="Design anpassen" type="default" />
    </router-link>
  </b-container>
</template>

<script lang="ts">
import Vue from "vue";
import { DxButton } from "devextreme-vue";

export default Vue.extend({
  components: {
    DxButton
  }
});
</script>

<style lang="scss" scoped>
</style>