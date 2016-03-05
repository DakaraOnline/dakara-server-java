
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum eHealerAction {
	HealAllyParalisis, ParalizePet, ParalizeUser, AttackUser, HealAlly, RescueFarAlly;
	public static eHealerAction fromInteger(int x) {
		switch (x) {
		case 1:
			return HealAllyParalisis;
		case 2:
			return ParalizePet;
		case 3:
			return ParalizeUser;
		case 4:
			return AttackUser;
		case 5:
			return HealAlly;
		case 6:
			return RescueFarAlly;
		}
		return null;
	}

	public static int toInteger(eHealerAction x) {
		switch (x) {
		case HealAllyParalisis:
			return 1;
		case ParalizePet:
			return 2;
		case ParalizeUser:
			return 3;
		case AttackUser:
			return 4;
		case HealAlly:
			return 5;
		case RescueFarAlly:
			return 6;
		}
		return 0;
	}
}
