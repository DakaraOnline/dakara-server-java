
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum eSpellCasterAction {
	RemoveInvi, Attack;
	public static eSpellCasterAction fromInteger(int x) {
		switch (x) {
		case 1:
			return RemoveInvi;
		case 2:
			return Attack;
		}
		return null;
	}

	public static int toInteger(eSpellCasterAction x) {
		switch (x) {
		case RemoveInvi:
			return 1;
		case Attack:
			return 2;
		}
		return 0;
	}
}
