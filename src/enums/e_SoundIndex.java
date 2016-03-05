
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum e_SoundIndex {
	MUERTE_HOMBRE, MUERTE_MUJER, FLECHA_IMPACTO, CONVERSION_BARCO, MORFAR_MANZANA, SOUND_COMIDA, MUERTE_MUJER_AGUA, MUERTE_HOMBRE_AGUA;
	public static e_SoundIndex fromInteger(int x) {
		switch (x) {
		case 11:
			return MUERTE_HOMBRE;
		case 74:
			return MUERTE_MUJER;
		case 65:
			return FLECHA_IMPACTO;
		case 55:
			return CONVERSION_BARCO;
		case 82:
			return MORFAR_MANZANA;
		case 7:
			return SOUND_COMIDA;
		case 211:
			return MUERTE_MUJER_AGUA;
		case 212:
			return MUERTE_HOMBRE_AGUA;
		}
		return null;
	}

	public static int toInteger(e_SoundIndex x) {
		switch (x) {
		case MUERTE_HOMBRE:
			return 11;
		case MUERTE_MUJER:
			return 74;
		case FLECHA_IMPACTO:
			return 65;
		case CONVERSION_BARCO:
			return 55;
		case MORFAR_MANZANA:
			return 82;
		case SOUND_COMIDA:
			return 7;
		case MUERTE_MUJER_AGUA:
			return 211;
		case MUERTE_HOMBRE_AGUA:
			return 212;
		}
		return 0;
	}

	public int toInteger() {
		return toInteger(this);
	}
}
