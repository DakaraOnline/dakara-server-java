
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum eMochilas {
	Mediana, Grande;
	public static eMochilas fromInteger(int x) {
		switch (x) {
		case 1:
			return Mediana;
		case 2:
			return Grande;
		}
		return null;
	}

	public static int toInteger(eMochilas x) {
		switch (x) {
		case Mediana:
			return 1;
		case Grande:
			return 2;
		}
		return 0;
	}
}
