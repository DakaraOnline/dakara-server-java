
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum TargetType {
	uUsuarios, uNPC, uUsuariosYnpc, uTerreno;
	public static TargetType fromInteger(int x) {
		switch (x) {
		case 1:
			return uUsuarios;
		case 2:
			return uNPC;
		case 3:
			return uUsuariosYnpc;
		case 4:
			return uTerreno;
		}
		return null;
	}

	public static int toInteger(TargetType x) {
		switch (x) {
		case uUsuarios:
			return 1;
		case uNPC:
			return 2;
		case uUsuariosYnpc:
			return 3;
		case uTerreno:
			return 4;
		}
		return 0;
	}
}
