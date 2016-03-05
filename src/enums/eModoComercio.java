
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum eModoComercio {
	Compra, Venta;
	public static eModoComercio fromInteger(int x) {
		switch (x) {
		case 1:
			return Compra;
		case 2:
			return Venta;
		}
		return null;
	}

	public static int toInteger(eModoComercio x) {
		switch (x) {
		case Compra:
			return 1;
		case Venta:
			return 2;
		}
		return 0;
	}
}
