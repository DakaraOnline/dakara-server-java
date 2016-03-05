
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum PECES_POSIBLES {
	PESCADO1, PESCADO2, PESCADO3, PESCADO4;
	public static PECES_POSIBLES fromInteger(int x) {
		switch (x) {
		case 139:
			return PESCADO1;
		case 544:
			return PESCADO2;
		case 545:
			return PESCADO3;
		case 546:
			return PESCADO4;
		}
		return null;
	}

	public static int toInteger(PECES_POSIBLES x) {
		switch (x) {
		case PESCADO1:
			return 139;
		case PESCADO2:
			return 544;
		case PESCADO3:
			return 545;
		case PESCADO4:
			return 546;
		}
		return 0;
	}

	public int toInteger() {
		return toInteger(this);
	}
}
