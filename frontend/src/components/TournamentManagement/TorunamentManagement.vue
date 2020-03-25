//----------------------------------------------------------------------------------
//     Created By: Lucas Wierer
// Contributor(s): Moritz Lugbauer (Huge changes)
//    Description: Tournament creation
//----------------------------------------------------------------------------------
<template>
  <div>
    <div class="container">
      <h1 class="text-center paddingTop">Hallo {{ firstName }}!</h1>
      <div class="row align-items-center mt-5">
        <div class="col-sm">
          <router-link to="/tournament-history">
            <dx-button
              text="Turnierhistorie"
              type="default"
              class="primary btn-block mt-3"
            />
          </router-link>
        </div>
        <div class="col-sm text-center">
          <img
            @click="startGame"
            src="../../assets/images/play_button.png"
            style="width:90%; cursor: pointer;"
          />
        </div>
        <div class="col-sm">
          <router-link to="/settings">
            <dx-button
              text="Einstellungen"
              type="default"
              class="primary btn-block mt-3"
            />
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import Vue from "vue";

import { DxButton } from "devextreme-vue";
import { User } from "../../typescript-fetch-client";
import UserService from "../../services/UserService";

export default Vue.extend({
  components: {
    DxButton
  },
  data() {
    return {
      firstName: ""
    };
  },
  mounted() {
    let user = UserService.getUser();
    this.firstName = user.firstName as string;
  },

  methods: {
    startGame() {
      this.$router.push("/new-tournament");
    }
  }
});
</script>

<style lang="scss" scoped>
.paddingTop {
  padding-top: 10%;
}
</style>
