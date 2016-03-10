
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum eRaza {
	Humano, Elfo, Drow, Gnomo, Enano;
	public static eRaza fromInteger(int x) {
		switch (x) {
		case 1:
			return Humano;
		case 2:
			return Elfo;
		case 3:
			return Drow;
		case 4:
			return Gnomo;
		case 5:
			return Enano;
		}
		return null;
	}

	public static int toInteger(eRaza x) {
		switch (x) {
		case Humano:
			return 1;
		case Elfo:
			return 2;
		case Drow:
			return 3;
		case Gnomo:
			return 4;
		case Enano:
			return 5;
		}
		return 0;
	}

	public int toInteger() {
		return toInteger(this);
	}
}
