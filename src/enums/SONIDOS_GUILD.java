
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum SONIDOS_GUILD {
	SND_CREACIONCLAN, SND_ACEPTADOCLAN, SND_DECLAREWAR;
	public static SONIDOS_GUILD fromInteger(int x) {
		switch (x) {
		case 44:
			return SND_CREACIONCLAN;
		case 43:
			return SND_ACEPTADOCLAN;
		case 45:
			return SND_DECLAREWAR;
		}
		return null;
	}

	public static int toInteger(SONIDOS_GUILD x) {
		switch (x) {
		case SND_CREACIONCLAN:
			return 44;
		case SND_ACEPTADOCLAN:
			return 43;
		case SND_DECLAREWAR:
			return 45;
		}
		return 0;
	}
}
