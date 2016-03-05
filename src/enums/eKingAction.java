
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum eKingAction {
	HealAllyParalisis, HealAllyPoison, HealAlly, ChaseTarget, MassiveAttack, RetreatAndHeal;
	public static eKingAction fromInteger(int x) {
		switch (x) {
		case 1:
			return HealAllyParalisis;
		case 2:
			return HealAllyPoison;
		case 3:
			return HealAlly;
		case 4:
			return ChaseTarget;
		case 5:
			return MassiveAttack;
		case 6:
			return RetreatAndHeal;
		}
		return null;
	}

	public static int toInteger(eKingAction x) {
		switch (x) {
		case HealAllyParalisis:
			return 1;
		case HealAllyPoison:
			return 2;
		case HealAlly:
			return 3;
		case ChaseTarget:
			return 4;
		case MassiveAttack:
			return 5;
		case RetreatAndHeal:
			return 6;
		}
		return 0;
	}
}
