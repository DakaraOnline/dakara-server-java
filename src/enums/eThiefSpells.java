
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum eThiefSpells {
	Arrow, Paralisis;
	public static eThiefSpells fromInteger(int x) {
		switch (x) {
		case 1:
			return Arrow;
		case 2:
			return Paralisis;
		}
		return null;
	}

	public static int toInteger(eThiefSpells x) {
		switch (x) {
		case Arrow:
			return 1;
		case Paralisis:
			return 2;
		}
		return 0;
	}
}
