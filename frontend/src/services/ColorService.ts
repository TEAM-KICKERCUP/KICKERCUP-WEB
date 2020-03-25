//----------------------------------------------------------------------------------
//     Created By: Lucas Wierer
// Contributor(s): None
//    Description: User Design Configurator Color Service
//----------------------------------------------------------------------------------
import { Design } from "@/typescript-fetch-client";

interface IColorService {
  setColor(config: Design): void;
}

class ColorService implements IColorService {
  private primaryName: string;
  private secondaryName: string;
  private primaryFontName: string;
  private secondaryFontName: string;

  constructor() {
    this.primaryName = "--primary-color";
    this.secondaryName = "--secondary-color";
    this.primaryFontName = "--primary-font";
    this.secondaryFontName = "--secondary-font";
  }

  setColor(design: Design): void {
    this.setCssVariable(this.primaryName, design.primaryColor as string);
    this.setCssVariable(this.secondaryName, design.secondaryColor as string);
    this.setCssVariable(
      this.primaryFontName,
      this.getContrastColor(design.primaryColor as string)
    );
    this.setCssVariable(
      this.secondaryFontName,
      this.getContrastColor(design.secondaryColor as string)
    );
  }

  private setCssVariable(name: string, val: string) {
    document.documentElement.style.setProperty(name, val);
  }

  // See https://www.w3.org/TR/AERT/#color-contrast
  private getContrastColor(hex: string) {
    if (hex.indexOf("#") === 0) {
      hex = hex.slice(1);
    }
    // convert 3-digit hex to 6-digits.
    if (hex.length === 3) {
      hex = hex[0] + hex[0] + hex[1] + hex[1] + hex[2] + hex[2];
    }
    if (hex.length !== 6) {
      throw new Error("Invalid HEX color.");
    }

    const rgb = [
      parseInt(hex.slice(0, 2), 16),
      parseInt(hex.slice(2, 4), 16),
      parseInt(hex.slice(4, 6), 16)
    ];

    const sum = Math.round((rgb[0] * 299 + rgb[1] * 587 + rgb[2] * 114) / 1000);
    return sum > 128 ? "#000000" : "#FFFFFF";
  }
}

export default new ColorService();
