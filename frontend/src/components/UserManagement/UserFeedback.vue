//----------------------------------------------------------------------------------
//     Created By: Jonas Jahns
// Contributor(s): None
//    Description: Provide Feedback to the creators team via Twitter
//----------------------------------------------------------------------------------
<template>
  <div class="container feedback">
    <h1 class="mt-3 mb-5 text-center" v-if="!alreadyGiven">Gib uns dein Feedback</h1>
    <h1 class="completed-message" v-cloak v-if="alreadyGiven">
      Danke für dein Feedback
      <i class="fas fa-heart"></i>
    </h1>
    <form v-if="!alreadyGiven" action="">
      <h1> </h1>
      <label>Dir sind Fehler aufgefallen? Du hast coole neue Ideen für KICKERCUP? Wir freuen uns über deine Rückmeldung.</label>
      <DxTextArea
        :height="90"
        :value.sync="message"
      />
      <dx-drop-down-button
        class="send-button"
        :items="actions"
        key-expr="id"
        @item-click="changeAction"
        :split-button="true"
        @button-click="giveFeedback"
        v-bind:text="sendButtonLabel"
      />
    </form>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import { DxButton, DxDropDownButton, DxTextArea } from "devextreme-vue";
import NotificationService from "../../services/NotificationService";
import {
  TwitterControllerApi,
  BaseAPI
} from "../../typescript-fetch-client";

enum FeedbackType {
  Tweet = "Tweet",
  DirectMessage = "Direct Message"
}

const MIN_FEEDBACK_CHARS: number = 10;
const MAX_FEEDBACK_CHARS: number = 80;

const API: TwitterControllerApi = new TwitterControllerApi();

export default Vue.extend({
    components: {
      DxButton,
      DxDropDownButton,
      DxTextArea
    },
    data() {
      return {
        alreadyGiven: this.$route.query.completed,
        frontendCallbackUrl: location.href,
        feedbackType: FeedbackType.Tweet,
        message: "",
        actions: [ FeedbackType.Tweet, FeedbackType.DirectMessage ],
        sendButtonLabel: "Abschicken als " + FeedbackType.Tweet
      };
  },
  async mounted() {
    this.$data.callbackUrl = location.href;
  },
  methods: {
    async giveFeedback(): Promise<void> {
      if (!this.validateFeedback()) {
        console.log("Feedback is invalid")
      }

      const message: string = this.$data.message;
      const callbackUrl: string = encodeURI(location.href + "?completed=true");

      let sendFeedback: () => void;

      switch(this.$data.feedbackType) {
        case FeedbackType.Tweet: {
          // sendFeedback = async () => await API.tweetFeedbackUsingGET(callbackUrl, message);
          sendFeedback = () => window.open(`https://twitter.com/intent/tweet?text=@KICKERCUP ${message}`, "_blank");
          break;
        } case FeedbackType.DirectMessage: {
          sendFeedback = async () => await API.replyFeedbackUsingGET(callbackUrl, message)
            .then(res => location.href = (res as any).authorizationUrl);
          break;
        } default: {
          sendFeedback = () => NotificationService.error("Das Feedback muss auf nicht abgeschickt werden")
        }
      }

      try {
        await sendFeedback();
      } catch (e) {
        console.log("Feedback cannot be sent, error message: ", e);
        NotificationService.error("Das Feedback konnte nicht abgeschickt werden")
      }
    },
    changeAction(action: any): void {
      this.$data.feedbackType = action.itemData;
      this.$data.sendButtonLabel = "Abschicken als " + action.itemData
    },
    validateFeedback(): boolean {
      const { message, callbackUrl } = this.$data;
      const exists = (value: string) => null !== value && value !== undefined && value !== "";
      if (!exists(message)) {
        NotificationService.info("Bitte fülle das Feedback aus");
        return false;
      }
      if (message.length > MAX_FEEDBACK_CHARS) {
        NotificationService.info(`Das Feedback darf höchstens ${MAX_FEEDBACK_CHARS} Zeichen haben`)
        return false;
      }
      if (message.length < MIN_FEEDBACK_CHARS) {
        NotificationService.info(`Das Feedback muss mindestens ${MIN_FEEDBACK_CHARS} Zeichen haben`)
        return false;
      }
      return true;
    }
  }
});
</script>

<style lang="scss" scoped>
.feedback {
  form {
    & > * {
      display: block;
    }
    
    .send-button {
      max-width: 200px;
      margin-top: 0.5rem;
    }
  }
  .completed-message {
    text-align: center;
  }
}
</style>