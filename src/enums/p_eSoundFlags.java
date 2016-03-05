
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum p_eSoundFlags {
	ninguna, Lluvia;
	public static p_eSoundFlags fromInteger(int x) {
		switch (x) {
		case 0:
			return ninguna;
		case 1:
			return Lluvia;
		}
		return null;
	}

	public static int toInteger(p_eSoundFlags x) {
		switch (x) {
		case ninguna:
			return 0;
		case Lluvia:
			return 1;
		}
		return 0;
	}

	public int toInteger() {
		return toInteger(this);
	}
}
