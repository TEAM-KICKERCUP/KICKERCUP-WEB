//----------------------------------------------------------------------------------
//     Created By: Lucas Wierer
// Contributor(s): None
//    Description: Displays the user sepcific statistic
//----------------------------------------------------------------------------------
<template>
  <b-container>
    <h2 class="text-center m-5">Rangliste</h2>
    <DxChart id="chart" :data-source="playerData" :value-axis="{visualRange: scale}">
      <DxSeries
        argument-field="name"
        value-field="rankScore"
        name="Spielerniveau"
        type="bar"
        color="#2e8c47"
      />
      <dx-legend verticalAlignment="bottom" horizontalAlignment="center"></dx-legend>
      <DxTooltip :enabled="true" :customize-tooltip="customizeTooltip" />
    </DxChart>

    <h2 class="text-center m-5">Deine bisherigen Aktivitäten</h2>
    <b-table-simple hover small responsive class="text-center table-bordered table-striped">
      <b-thead>
        <b-tr>
          <b-th>ANZAHL DEINER SPIELER</b-th>
          <b-th>GESPIELTE TURNIERE</b-th>
          <b-th>ABGESCHLOSSENE SPIELE</b-th>
          <b-th>BISHER ERZIELTE TORE</b-th>
        </b-tr>
      </b-thead>
      <b-tbody>
        <b-tr>
          <b-td>{{statistic.playersCount}}</b-td>
          <b-td>{{statistic.tournamentCount}}</b-td>
          <b-td>{{statistic.matchCount}}</b-td>
          <b-td>{{statistic.goalsCount}}</b-td>
        </b-tr>
      </b-tbody>
    </b-table-simple>
  </b-container>
</template>

<script lang="ts">
import Vue from "vue";
import {
  PlayerControllerApi,
  Player,
  StatisticsControllerApi,
  Statistic
} from "../../typescript-fetch-client";
import UserService from "../../services/UserService";
import { DxChart, DxSeries, DxLegend, DxTooltip } from "devextreme-vue/chart";

const Api = new PlayerControllerApi();
const ApiStatistic = new StatisticsControllerApi();

export default Vue.extend({
  components: {
    DxChart,
    DxSeries,
    DxLegend,
    DxTooltip
  },
  computed: {
    scale(): (number | null)[] {
      if (this.players.length === 0) return [0, null];

      const arr = this.players.map(p => p.rankScore as number);
      const minScale = Math.min(...arr) - 20;
      return [minScale, null];
    },
    playerData(): { name: string; rankScore: number }[] {
      if (this.players.length === 0) return [];

      return this.players.map(p => ({
        name: `${p.firstName} ${p.lastName}`,
        rankScore: p.rankScore as number
      }));
    }
  },
  data() {
    return {
      statistic: {} as Statistic,
      players: [] as Player[]
    };
  },
  async mounted() {
    this.statistic = await ApiStatistic.getStatsUsingGET(
      UserService.getUserId()
    );
    this.players = this.statistic.players as Player[];
  },
  methods: {
    customizeTooltip(pointInfo: any) {
      return {
        text: `${pointInfo.argumentText}<br/>${pointInfo.valueText}`
      };
    }
  }
});
</script>

<style scoped></style>
