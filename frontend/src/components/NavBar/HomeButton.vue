//----------------------------------------------------------------------------------
//     Created By: Lucas Wierer
// Contributor(s): None
//    Description: Home Button
//----------------------------------------------------------------------------------

<template>
  <div class="mt-5 text-center" v-if="showHome">
    <router-link to="/tournamentmanagement">
      <span>
        <i class="fas fa-home fa-2x" style="color: var(--primary-color);"></i>
      </span>
    </router-link>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import { DxButton } from "devextreme-vue";

export default Vue.extend({
  components: {
    DxButton
  },
  computed: {
    showHome() {
      const blackList = ["/", "/login", "/tournamentmanagement"];
      return !blackList.includes(this.$route.fullPath);
    }
  }
});
</script>

<style scoped></style>
