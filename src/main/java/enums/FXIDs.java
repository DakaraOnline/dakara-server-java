
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum FXIDs {
	FXWARP, FXMEDITARCHICO, FXMEDITARMEDIANO, FXMEDITARGRANDE, FXMEDITARXGRANDE, FXMEDITARXXGRANDE;
	public static FXIDs fromInteger(int x) {
		switch (x) {
		case 1:
			return FXWARP;
		case 4:
			return FXMEDITARCHICO;
		case 5:
			return FXMEDITARMEDIANO;
		case 6:
			return FXMEDITARGRANDE;
		case 16:
			return FXMEDITARXGRANDE;
		case 34:
			return FXMEDITARXXGRANDE;
		}
		return null;
	}

	public static int toInteger(FXIDs x) {
		switch (x) {
		case FXWARP:
			return 1;
		case FXMEDITARCHICO:
			return 4;
		case FXMEDITARMEDIANO:
			return 5;
		case FXMEDITARGRANDE:
			return 6;
		case FXMEDITARXGRANDE:
			return 16;
		case FXMEDITARXXGRANDE:
			return 34;
		}
		return 0;
	}

	public int toInteger() {
		return toInteger(this);
	}
}
