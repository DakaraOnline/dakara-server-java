
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum eGenero {
	Hombre, Mujer;
	public static eGenero fromInteger(int x) {
		switch (x) {
		case 1:
			return Hombre;
		case 2:
			return Mujer;
		}
		return null;
	}

	public static int toInteger(eGenero x) {
		switch (x) {
		case Hombre:
			return 1;
		case Mujer:
			return 2;
		}
		return 0;
	}
}
