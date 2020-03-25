//----------------------------------------------------------------------------------
//     Created By: Lucas Wierer
// Contributor(s): Christopher Heid (Social Media Buttons)
//    Description: Top Menu
//----------------------------------------------------------------------------------
<template>
  <b-container v-if="show">
    <b-row class="pt-3">
      <b-col sm="5">
        <img
          @click="shareOnFacebook"
          src="../../assets/images/fb_btn.png"
          style="width: 77px"
          class="mr-2"
        />
        <img @click="shareOnTwitter" src="../../assets/images/twitter_btn.png" style="width:80px" />
      </b-col>
      <b-col sm="7">
        <dx-button text="Abmelden" class="secondary float-right" type="default" @click="logout" />
        <div class="float-right mr-3">
          <dx-button class="primary" text="Gib uns Feedback" type="default" @click="giveFeedback" />
        </div>
      </b-col>
    </b-row>
  </b-container>
</template>

<script>
import DxButton from "devextreme-vue/button";
import UserService from "../../services/UserService";

export default {
  components: {
    DxButton
  },
  methods: {
    shareOnFacebook() {
      window.open(
        "https://www.facebook.com/sharer/sharer.php?u=https://i.imgur.com/uci5S4im.png&quote=KICKERCUP ist eine toole online Software zur Durchführung von Tischkickerturnieren",
        "_blank"
      );
    },
    shareOnTwitter() {
      window.open(
        "https://twitter.com/intent/tweet?via=KICKERCUP&text=KICKERCUP ist eine super online Software zur Durchführung von Tischkickerturnieren",
        "_blank"
      );
    },
    giveFeedback() {
      this.$router.push({ name: "feedback" });
    },

    logout() {
      setTimeout(() => {
        UserService.logout();
        location = "/";
      }, 1000);
    }
  },
  computed: {
    show() {
      const blackList = ["/", "/login", "/usermanagement"];
      return !blackList.includes(this.$route.fullPath);
    }
  }
};
</script>

<style lang="scss" scoped></style>
