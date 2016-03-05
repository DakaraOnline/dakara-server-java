
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum eNPCType {
	Comun, Revividor, GuardiaReal, Entrenador, Banquero, Noble, DRAGON, Timbero, Guardiascaos, ResucitadorNewbie, Pretoriano, Gobernador;
	public static eNPCType fromInteger(int x) {
		switch (x) {
		case 0:
			return Comun;
		case 1:
			return Revividor;
		case 2:
			return GuardiaReal;
		case 3:
			return Entrenador;
		case 4:
			return Banquero;
		case 5:
			return Noble;
		case 6:
			return DRAGON;
		case 7:
			return Timbero;
		case 8:
			return Guardiascaos;
		case 9:
			return ResucitadorNewbie;
		case 10:
			return Pretoriano;
		case 11:
			return Gobernador;
		}
		return null;
	}

	public static int toInteger(eNPCType x) {
		switch (x) {
		case Comun:
			return 0;
		case Revividor:
			return 1;
		case GuardiaReal:
			return 2;
		case Entrenador:
			return 3;
		case Banquero:
			return 4;
		case Noble:
			return 5;
		case DRAGON:
			return 6;
		case Timbero:
			return 7;
		case Guardiascaos:
			return 8;
		case ResucitadorNewbie:
			return 9;
		case Pretoriano:
			return 10;
		case Gobernador:
			return 11;
		}
		return 0;
	}
}
