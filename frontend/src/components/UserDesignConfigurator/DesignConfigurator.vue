//----------------------------------------------------------------------------------
//     Created By: Lucas Wierer
// Contributor(s): None
//    Description: User Design Configurator (Primary and Secondary Color)
//----------------------------------------------------------------------------------
<template>
  <div class="container">
    <h1 class="mt-3 mb-5 text-center">Design</h1>
    <div class="form" style="margin-top: 100px;">
      <div class="dx-fieldset">
        <div class="dx-field">
          <div class="dx-field-label">Primär</div>
          <div class="dx-field-value">
            <dx-color-box
              v-model="design.primaryColor"
              applyValueMode="instantly"
              @valueChanged="changeColor"
            />
          </div>
        </div>
        <div class="dx-field">
          <div class="dx-field-label">Sekundär</div>
          <div class="dx-field-value">
            <dx-color-box
              v-model="design.secondaryColor"
              applyValueMode="instantly"
              @valueChanged="changeColor"
            />
          </div>
        </div>
      </div>
      <dx-button @click="reset" text="Zurücksetzen" type="default" class="secondary" />
    </div>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import { DxColorBox, DxButton } from "devextreme-vue";
import ColorService from "../../services/ColorService";
import { Design, DesignControllerApi } from "../../typescript-fetch-client";
import UserService from "../../services/UserService";

const Api = new DesignControllerApi();

export default Vue.extend({
  components: {
    DxColorBox,
    DxButton
  },
  data() {
    return {
      design: {} as Design,
      debounceTimeout: undefined as number | undefined,
      debounceIntervall: 1000
    };
  },
  async mounted() {
    const config = await Api.listByUserUsingGET(UserService.getUserId());
    this.design = config;
  },
  methods: {
    changeColor() {
      ColorService.setColor(this.design);
      this.sendDebouncedUpdate();
    },
    reset() {
      this.design.primaryColor = "#09833f";
      this.design.secondaryColor = "#d9534f";
      this.changeColor();
    },
    sendDebouncedUpdate() {
      if (this.debounceTimeout) clearTimeout(this.debounceTimeout);
      this.debounceTimeout = setTimeout(() => {
        Api.updateUsingPUT(
          this.design,
          this.design.id as number,
          UserService.getUserId()
        );
      }, this.debounceIntervall);
    }
  }
});
</script>

<style lang="scss" scoped></style>
