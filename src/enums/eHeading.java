
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum eHeading {
	NORTH, EAST, SOUTH, WEST;
	public static eHeading fromInteger(int x) {
		switch (x) {
		case 1:
			return NORTH;
		case 2:
			return EAST;
		case 3:
			return SOUTH;
		case 4:
			return WEST;
		}
		return null;
	}

	public static int toInteger(eHeading x) {
		switch (x) {
		case NORTH:
			return 1;
		case EAST:
			return 2;
		case SOUTH:
			return 3;
		case WEST:
			return 4;
		}
		return 0;
	}

	public int toInteger() {
		return toInteger(this);
	}
}
