
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum eHealerSpells {
	ParalizeUser, RemoveParalisis, ParalizeNpc, Heal, Tormenta;
	public static eHealerSpells fromInteger(int x) {
		switch (x) {
		case 1:
			return ParalizeUser;
		case 2:
			return RemoveParalisis;
		case 3:
			return ParalizeNpc;
		case 4:
			return Heal;
		case 5:
			return Tormenta;
		}
		return null;
	}

	public static int toInteger(eHealerSpells x) {
		switch (x) {
		case ParalizeUser:
			return 1;
		case RemoveParalisis:
			return 2;
		case ParalizeNpc:
			return 3;
		case Heal:
			return 4;
		case Tormenta:
			return 5;
		}
		return 0;
	}
}
