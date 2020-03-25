//----------------------------------------------------------------------------------
//     Created By: Lucas Wierer
// Contributor(s): None
//    Description: Table for display, editing and deletion of players
//----------------------------------------------------------------------------------
<template>
  <div>
    <dx-data-grid
      :data-source="players"
      :allow-column-reordering="true"
      :show-borders="true"
      :row-alternation-enabled="true"
      key-expr="id"
      @row-updated="updatePlayer($event.key, $event.data)"
      @row-removed="deletePlayer($event.key)"
    >
      <dx-paging :enabled="true" />
      <dx-editing :allow-updating="true" :allow-deleting="true" mode="row" />
      <dx-column caption="VORNAME" data-field="firstName" />
      <dx-column caption="NACHNAME" data-field="lastName" />
      <dx-column caption="E-MAIL" data-field="emailId" />
      <dx-column :allowEditing="false" caption="SPIELERNIVEAU" data-field="rankScore" />
    </dx-data-grid>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import {
  Player,
  PlayerControllerApi
} from "../../typescript-fetch-client/index";
import PlayerGrid from "./PlayerGrid.vue";
import DxButton from "devextreme-vue/button";
import {
  DxDataGrid,
  DxColumn,
  DxEditing,
  DxPaging,
  DxLookup
} from "devextreme-vue/data-grid";
import NotificationService from "../../services/NotificationService";

const Api = new PlayerControllerApi();

export default Vue.extend({
  components: {
    DxDataGrid,
    DxColumn,
    DxEditing,
    DxPaging,
    DxButton,
    DxLookup
  },
  props: ["players"],
  methods: {
    async deletePlayer(id: number) {
      Api.deletePlayerUsingDELETE(id)
        .then(() => {
          NotificationService.success("Der Spieler wurde erfolgreich gelöscht.");
        })
        .catch(() => {
          NotificationService.error("Der Spieler konnte leider nicht gelöscht werden, da er schon mal an einem Turnier teilgenommen hat");
        });
    },
    updatePlayer(id: number, player: Player) {
      Api.updatePlayerUsingPUT(player, id)
        .then(() => {
          NotificationService.success("Der Spieler wurde erfolgreich aktualisiert.");
        })
        .catch(() => {
          NotificationService.error("Da ist leider etwas schief gelaufen.");
        });
    }
  }
});
</script>

<style lang="scss" scoped></style>
