
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum eThiefAction {
	Attack, Steal, None;
	public static eThiefAction fromInteger(int x) {
		switch (x) {
		case 1:
			return Attack;
		case 2:
			return Steal;
		case 3:
			return None;
		}
		return null;
	}

	public static int toInteger(eThiefAction x) {
		switch (x) {
		case Attack:
			return 1;
		case Steal:
			return 2;
		case None:
			return 3;
		}
		return 0;
	}
}
