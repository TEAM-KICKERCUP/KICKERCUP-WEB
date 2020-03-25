//----------------------------------------------------------------------------------
//     Created By: Lucas Wierer
// Contributor(s): Christopher Heid
//    Description: Handling User Information
//----------------------------------------------------------------------------------
import { User } from "@/typescript-fetch-client";
import EncryptionService from "./EncryptionService";

class UserService {
  private key = "user";

  login(user: User) {
    const payload = JSON.stringify(user);
    const encrypted = EncryptionService.encrypt(payload);
    localStorage.setItem(this.key, encrypted);
  }

  logout() {
    localStorage.removeItem(this.key);
  }

  getUser(): User {
    const user = this.getUserOrNull();

    if (user === null) {
      throw new Error("User is not available");
    }

    return user;
  }

  getUserId(): number {
    const user = this.getUserOrNull();

    if (user === null) {
      throw new Error("User is not available");
    } else if (user.id === undefined) {
      throw new Error("User Id is not available");
    }

    return user.id;
  }

  getUserOrNull(): User | null {
    const payload = localStorage.getItem(this.key);

    if (payload === null) {
      return null;
    }

    const decrypted = EncryptionService.decrypt(payload);
    return JSON.parse(decrypted);
  }
}

export default new UserService();
