
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum eKingSpells {
	LittleHeal, RemoveParalisis, BlindAttack, StupidityAttack, HealPoison;
	public static eKingSpells fromInteger(int x) {
		switch (x) {
		case 1:
			return LittleHeal;
		case 2:
			return RemoveParalisis;
		case 3:
			return BlindAttack;
		case 4:
			return StupidityAttack;
		case 5:
			return HealPoison;
		}
		return null;
	}

	public static int toInteger(eKingSpells x) {
		switch (x) {
		case LittleHeal:
			return 1;
		case RemoveParalisis:
			return 2;
		case BlindAttack:
			return 3;
		case StupidityAttack:
			return 4;
		case HealPoison:
			return 5;
		}
		return 0;
	}
}
