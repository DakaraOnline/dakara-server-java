
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum TipoHechizo {
	uPropiedades, uEstado, uMaterializa, uInvocacion;
	public static TipoHechizo fromInteger(int x) {
		switch (x) {
		case 1:
			return uPropiedades;
		case 2:
			return uEstado;
		case 3:
			return uMaterializa;
		case 4:
			return uInvocacion;
		}
		return null;
	}

	public static int toInteger(TipoHechizo x) {
		switch (x) {
		case uPropiedades:
			return 1;
		case uEstado:
			return 2;
		case uMaterializa:
			return 3;
		case uInvocacion:
			return 4;
		}
		return 0;
	}
}
