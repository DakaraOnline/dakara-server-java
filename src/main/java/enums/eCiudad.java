
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum eCiudad {
	cUllathorpe, cNix, cBanderbill, cLindos, cArghal, cArkhein, cLastCity;
	public static eCiudad fromInteger(int x) {
		switch (x) {
		case 1:
			return cUllathorpe;
		case 2:
			return cNix;
		case 3:
			return cBanderbill;
		case 4:
			return cLindos;
		case 5:
			return cArghal;
		case 6:
			return cArkhein;
		case 7:
			return cLastCity;
		}
		return null;
	}

	public static int toInteger(eCiudad x) {
		switch (x) {
		case cUllathorpe:
			return 1;
		case cNix:
			return 2;
		case cBanderbill:
			return 3;
		case cLindos:
			return 4;
		case cArghal:
			return 5;
		case cArkhein:
			return 6;
		case cLastCity:
			return 7;
		}
		return 0;
	}

	public int toInteger() {
		return toInteger(this);
	}
}
