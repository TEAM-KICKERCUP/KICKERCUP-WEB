//----------------------------------------------------------------------------------
//     Created By: Lucas Wierer
// Contributor(s): None
//    Description: Encryption for localstorage
//----------------------------------------------------------------------------------
var encryptor = require("simple-encryptor")(process.env.VUE_APP_SECRET);

class EncryptionService {
  encrypt(payload: string) {
    return encryptor.encrypt(payload);
  }

  decrypt(payload: string) {
    return encryptor.decrypt(payload);
  }
}

export default new EncryptionService();
