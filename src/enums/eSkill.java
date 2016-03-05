
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum eSkill {
	Magia, Robar, Tacticas, Armas, Meditar, Apunalar, Ocultarse, Supervivencia, Talar, Comerciar, Defensa, Pesca, Mineria, Carpinteria, Herreria, Liderazgo, Domar, Proyectiles, Wrestling, Navegacion;
	public static eSkill fromInteger(int x) {
		switch (x) {
		case 1:
			return Magia;
		case 2:
			return Robar;
		case 3:
			return Tacticas;
		case 4:
			return Armas;
		case 5:
			return Meditar;
		case 6:
			return Apunalar;
		case 7:
			return Ocultarse;
		case 8:
			return Supervivencia;
		case 9:
			return Talar;
		case 10:
			return Comerciar;
		case 11:
			return Defensa;
		case 12:
			return Pesca;
		case 13:
			return Mineria;
		case 14:
			return Carpinteria;
		case 15:
			return Herreria;
		case 16:
			return Liderazgo;
		case 17:
			return Domar;
		case 18:
			return Proyectiles;
		case 19:
			return Wrestling;
		case 20:
			return Navegacion;
		}
		return null;
	}

	public static int toInteger(eSkill x) {
		switch (x) {
		case Magia:
			return 1;
		case Robar:
			return 2;
		case Tacticas:
			return 3;
		case Armas:
			return 4;
		case Meditar:
			return 5;
		case Apunalar:
			return 6;
		case Ocultarse:
			return 7;
		case Supervivencia:
			return 8;
		case Talar:
			return 9;
		case Comerciar:
			return 10;
		case Defensa:
			return 11;
		case Pesca:
			return 12;
		case Mineria:
			return 13;
		case Carpinteria:
			return 14;
		case Herreria:
			return 15;
		case Liderazgo:
			return 16;
		case Domar:
			return 17;
		case Proyectiles:
			return 18;
		case Wrestling:
			return 19;
		case Navegacion:
			return 20;
		}
		return 0;
	}
}
