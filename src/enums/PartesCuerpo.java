
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum PartesCuerpo {
	bCabeza, bPiernaIzquierda, bPiernaDerecha, bBrazoDerecho, bBrazoIzquierdo, bTorso;
	public static PartesCuerpo fromInteger(int x) {
		switch (x) {
		case 1:
			return bCabeza;
		case 2:
			return bPiernaIzquierda;
		case 3:
			return bPiernaDerecha;
		case 4:
			return bBrazoDerecho;
		case 5:
			return bBrazoIzquierdo;
		case 6:
			return bTorso;
		}
		return null;
	}

	public static int toInteger(PartesCuerpo x) {
		switch (x) {
		case bCabeza:
			return 1;
		case bPiernaIzquierda:
			return 2;
		case bPiernaDerecha:
			return 3;
		case bBrazoDerecho:
			return 4;
		case bBrazoIzquierdo:
			return 5;
		case bTorso:
			return 6;
		}
		return 0;
	}
}
