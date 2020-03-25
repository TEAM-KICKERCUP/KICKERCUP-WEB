//----------------------------------------------------------------------------------
//     Created By: Lucas Wierer
// Contributor(s): None
//    Description: Success, Info, Error, Warnings Toasts Service
//----------------------------------------------------------------------------------
import notify from "devextreme/ui/notify";

const duration = 2000;

const options = {
  position: { at: "bottom center", my: "center", offset: "0 -60" },
  displayTime: duration
};

class NotificationService {
  success(message: string) {
    notify({ message: message, ...options }, "success");
  }

  info(message: string) {
    notify({ message: message, ...options }, "info");
  }

  warning(message: string) {
    notify({ message: message, ...options }, "warning");
  }

  error(message: string) {
    notify({ message: message, ...options }, "error");
  }
}

export default new NotificationService();
