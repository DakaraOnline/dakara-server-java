
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum PlayerType {
	User, Consejero, SemiDios, Dios, Admin, RoleMaster, ChaosCouncil, RoyalCouncil;
	public static PlayerType fromInteger(int x) {
		switch (x) {
		case 1:
			return User;
		case 2:
			return Consejero;
		case 4:
			return SemiDios;
		case 8:
			return Dios;
		case 16:
			return Admin;
		case 32:
			return RoleMaster;
		case 64:
			return ChaosCouncil;
		case 128:
			return RoyalCouncil;
		}
		return null;
	}

	public static int toInteger(PlayerType x) {
		switch (x) {
		case User:
			return 1;
		case Consejero:
			return 2;
		case SemiDios:
			return 4;
		case Dios:
			return 8;
		case Admin:
			return 16;
		case RoleMaster:
			return 32;
		case ChaosCouncil:
			return 64;
		case RoyalCouncil:
			return 128;
		}
		return 0;
	}
}
