
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum iMinerales {
	HierroCrudo, PlataCruda, OroCrudo, LingoteDeHierro, LingoteDePlata, LingoteDeOro;
	public static iMinerales fromInteger(int x) {
		switch (x) {
		case 192:
			return HierroCrudo;
		case 193:
			return PlataCruda;
		case 194:
			return OroCrudo;
		case 386:
			return LingoteDeHierro;
		case 387:
			return LingoteDePlata;
		case 388:
			return LingoteDeOro;
		}
		return null;
	}

	public static int toInteger(iMinerales x) {
		switch (x) {
		case HierroCrudo:
			return 192;
		case PlataCruda:
			return 193;
		case OroCrudo:
			return 194;
		case LingoteDeHierro:
			return 386;
		case LingoteDePlata:
			return 387;
		case LingoteDeOro:
			return 388;
		}
		return 0;
	}

	public int toInteger() {
		return toInteger(this);
	}
}
