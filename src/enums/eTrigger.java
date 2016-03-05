
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum eTrigger {
	NADA, BAJOTECHO, trigger_2, POSINVALIDA, ZONASEGURA, ANTIPIQUETE, ZONAPELEA;
	public static eTrigger fromInteger(int x) {
		switch (x) {
		case 0:
			return NADA;
		case 1:
			return BAJOTECHO;
		case 2:
			return trigger_2;
		case 3:
			return POSINVALIDA;
		case 4:
			return ZONASEGURA;
		case 5:
			return ANTIPIQUETE;
		case 6:
			return ZONAPELEA;
		}
		return null;
	}

	public static int toInteger(eTrigger x) {
		switch (x) {
		case NADA:
			return 0;
		case BAJOTECHO:
			return 1;
		case trigger_2:
			return 2;
		case POSINVALIDA:
			return 3;
		case ZONASEGURA:
			return 4;
		case ANTIPIQUETE:
			return 5;
		case ZONAPELEA:
			return 6;
		}
		return 0;
	}
}
