
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum eAtributos {
	Fuerza, Agilidad, Inteligencia, Carisma, Constitucion;
	public static eAtributos fromInteger(int x) {
		switch (x) {
		case 1:
			return Fuerza;
		case 2:
			return Agilidad;
		case 3:
			return Inteligencia;
		case 4:
			return Carisma;
		case 5:
			return Constitucion;
		}
		return null;
	}

	public static int toInteger(eAtributos x) {
		switch (x) {
		case Fuerza:
			return 1;
		case Agilidad:
			return 2;
		case Inteligencia:
			return 3;
		case Carisma:
			return 4;
		case Constitucion:
			return 5;
		}
		return 0;
	}
}
