
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum eNickColor {
	ieCriminal, ieCiudadano, ieAtacable;
	public static eNickColor fromInteger(int x) {
		switch (x) {
		case 1:
			return ieCriminal;
		case 2:
			return ieCiudadano;
		case 4:
			return ieAtacable;
		}
		return null;
	}

	public static int toInteger(eNickColor x) {
		switch (x) {
		case ieCriminal:
			return 1;
		case ieCiudadano:
			return 2;
		case ieAtacable:
			return 4;
		}
		return 0;
	}
}
