
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum eTerrain {
	terrain_bosque, terrain_nieve, terrain_desierto, terrain_ciudad, terrain_campo, terrain_dungeon;
	public static eTerrain fromInteger(int x) {
		switch (x) {
		case 0:
			return terrain_bosque;
		case 1:
			return terrain_nieve;
		case 2:
			return terrain_desierto;
		case 3:
			return terrain_ciudad;
		case 4:
			return terrain_campo;
		case 5:
			return terrain_dungeon;
		}
		return null;
	}

	public static int toInteger(eTerrain x) {
		switch (x) {
		case terrain_bosque:
			return 0;
		case terrain_nieve:
			return 1;
		case terrain_desierto:
			return 2;
		case terrain_ciudad:
			return 3;
		case terrain_campo:
			return 4;
		case terrain_dungeon:
			return 5;
		}
		return 0;
	}
}
