
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum eTrigger6 {
	TRIGGER6_PERMITE, TRIGGER6_PROHIBE, TRIGGER6_AUSENTE;
	public static eTrigger6 fromInteger(int x) {
		switch (x) {
		case 1:
			return TRIGGER6_PERMITE;
		case 2:
			return TRIGGER6_PROHIBE;
		case 3:
			return TRIGGER6_AUSENTE;
		}
		return null;
	}

	public static int toInteger(eTrigger6 x) {
		switch (x) {
		case TRIGGER6_PERMITE:
			return 1;
		case TRIGGER6_PROHIBE:
			return 2;
		case TRIGGER6_AUSENTE:
			return 3;
		}
		return 0;
	}
}
