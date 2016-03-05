
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum RELACIONES_GUILD {
	GUERRA, PAZ, ALIADOS;
	public static RELACIONES_GUILD fromInteger(int x) {
		switch (x) {
		case -1:
			return GUERRA;
		case 0:
			return PAZ;
		case 1:
			return ALIADOS;
		}
		return null;
	}

	public static int toInteger(RELACIONES_GUILD x) {
		switch (x) {
		case GUERRA:
			return -1;
		case PAZ:
			return 0;
		case ALIADOS:
			return 1;
		}
		return 0;
	}
}
