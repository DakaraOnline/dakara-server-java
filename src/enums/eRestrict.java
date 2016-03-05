
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum eRestrict {
	restrict_no, restrict_newbie, restrict_armada, restrict_caos, restrict_faccion;
	public static eRestrict fromInteger(int x) {
		switch (x) {
		case 0:
			return restrict_no;
		case 1:
			return restrict_newbie;
		case 2:
			return restrict_armada;
		case 3:
			return restrict_caos;
		case 4:
			return restrict_faccion;
		}
		return null;
	}

	public static int toInteger(eRestrict x) {
		switch (x) {
		case restrict_no:
			return 0;
		case restrict_newbie:
			return 1;
		case restrict_armada:
			return 2;
		case restrict_caos:
			return 3;
		case restrict_faccion:
			return 4;
		}
		return 0;
	}
}
