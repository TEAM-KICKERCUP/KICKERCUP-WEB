//----------------------------------------------------------------------------------
//     Created By: Lucas Wierer
// Contributor(s): Christopher Heid, Jonas Jahns, Johannes Schweer, Moritz Lugbauer
//    Description: Vue.js Router
//----------------------------------------------------------------------------------
import Vue from "vue";
import VueRouter from "vue-router";
import PlayerManagement from "../components/PlayerManagement/PlayerManagement.vue";
import Login from "../components/UserManagement/Login.vue";
import UserManagement from "../components/UserManagement/UserManagement.vue";
import UserSettings from "../components/UserManagement/UserSettings.vue";
import Settings from "../components/Settings/Settings.vue";
import NewTournament from "../components/TournamentManagement/NewTournament.vue";
import TournamentHistory from "../components/TournamentManagement/TournamentHistory.vue";
import TournamentPlayers from "../components/TournamentManagement/TournamentPlayers.vue";
import TorunamentManagement from "../components/TournamentManagement/TorunamentManagement.vue";
import MatchResultEntry from "../components/TournamentManagement/MatchResultEntry.vue";
import TournamentTree from "../components/TournamentResults/TournamentTree.vue";
import DesignConfigurator from "../components/UserDesignConfigurator/DesignConfigurator.vue";
import Statistic from "../components/UserStatistic/Statistic";
import UserService from "@/services/UserService";
import Feedback from "../components/UserManagement/UserFeedback";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "home",
    redirect: "/tournamentmanagement"
  },
  {
    path: "/design-configurator",
    name: "design-configurator",
    component: DesignConfigurator
  },
  {
    path: "/statistic",
    name: "statistic",
    component: Statistic
  },
  {
    path: "/match-result-entry",
    name: "match-result-entry",
    component: MatchResultEntry
  },
  {
    path: "/playermanagement",
    name: "playermanagement",
    component: PlayerManagement
  },

  {
    path: "/login",
    name: "login",
    component: Login
  },
  {
    path: "/usermanagement",
    name: "usermanagement",
    component: UserManagement
  },
  {
    path: "/user-settings",
    name: "user-settings",
    component: UserSettings
  },

  {
    path: "/tournamentmanagement",
    name: "tournamentmanagement",
    component: TorunamentManagement
  },
  {
    path: "/new-tournament",
    name: "new-tournament",
    component: NewTournament
  },
  {
    path: "/tournament-players",
    name: "tournament-players",
    component: TournamentPlayers
  },
  {
    path: "/tournament-history",
    name: "tournament-history",
    component: TournamentHistory
  },
  {
    path: "/settings",
    name: "settings",
    component: Settings
  },

  {
    path: "/tournament-tree",
    name: "tournament-tree",
    component: TournamentTree
  },
  {
    path: "/feedback",
    name: "feedback",
    component: Feedback
  }
];

const router = new VueRouter({
  base: process.env.BASE_URL,
  mode: "history",
  routes
});

router.beforeEach((to, from, next) => {
  const annonymousRoutes = ["/usermanagement", "/login"];
  const isAuthenticated = UserService.getUserOrNull() !== null;

  if (annonymousRoutes.includes(to.path)) next();
  else if (!isAuthenticated) next("/login");
  else next();
});

export default router;
