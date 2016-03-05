
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum ePrivileges {
	Admin, Dios, Especial, SemiDios, Consejero, RoleMaster;
	public static ePrivileges fromInteger(int x) {
		switch (x) {
		case 1:
			return Admin;
		case 2:
			return Dios;
		case 3:
			return Especial;
		case 4:
			return SemiDios;
		case 5:
			return Consejero;
		case 6:
			return RoleMaster;
		}
		return null;
	}

	public static int toInteger(ePrivileges x) {
		switch (x) {
		case Admin:
			return 1;
		case Dios:
			return 2;
		case Especial:
			return 3;
		case SemiDios:
			return 4;
		case Consejero:
			return 5;
		case RoleMaster:
			return 6;
		}
		return 0;
	}
}
