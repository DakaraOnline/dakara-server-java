
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum eTipoDefArmors {
	ieBaja, ieMedia, ieAlta;
	public static eTipoDefArmors fromInteger(int x) {
		switch (x) {
		case 0:
			return ieBaja;
		case 1:
			return ieMedia;
		case 2:
			return ieAlta;
		}
		return null;
	}

	public static int toInteger(eTipoDefArmors x) {
		switch (x) {
		case ieBaja:
			return 0;
		case ieMedia:
			return 1;
		case ieAlta:
			return 2;
		}
		return 0;
	}
}
