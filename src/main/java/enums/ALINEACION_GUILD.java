
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum ALINEACION_GUILD {
	ALINEACION_LEGION, ALINEACION_CRIMINAL, ALINEACION_NEUTRO, ALINEACION_CIUDA, ALINEACION_ARMADA, ALINEACION_MASTER;
	public static ALINEACION_GUILD fromInteger(int x) {
		switch (x) {
		case 1:
			return ALINEACION_LEGION;
		case 2:
			return ALINEACION_CRIMINAL;
		case 3:
			return ALINEACION_NEUTRO;
		case 4:
			return ALINEACION_CIUDA;
		case 5:
			return ALINEACION_ARMADA;
		case 6:
			return ALINEACION_MASTER;
		}
		return null;
	}

	public static int toInteger(ALINEACION_GUILD x) {
		switch (x) {
		case ALINEACION_LEGION:
			return 1;
		case ALINEACION_CRIMINAL:
			return 2;
		case ALINEACION_NEUTRO:
			return 3;
		case ALINEACION_CIUDA:
			return 4;
		case ALINEACION_ARMADA:
			return 5;
		case ALINEACION_MASTER:
			return 6;
		}
		return 0;
	}

	public int toInteger() {
		return toInteger(this);
	}
}
