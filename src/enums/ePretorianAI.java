
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum ePretorianAI {
	King, Healer, SpellCaster, SwordMaster, Shooter, Thief, Last;
	public static ePretorianAI fromInteger(int x) {
		switch (x) {
		case 1:
			return King;
		case 2:
			return Healer;
		case 3:
			return SpellCaster;
		case 4:
			return SwordMaster;
		case 5:
			return Shooter;
		case 6:
			return Thief;
		case 7:
			return Last;
		}
		return null;
	}

	public static int toInteger(ePretorianAI x) {
		switch (x) {
		case King:
			return 1;
		case Healer:
			return 2;
		case SpellCaster:
			return 3;
		case SwordMaster:
			return 4;
		case Shooter:
			return 5;
		case Thief:
			return 6;
		case Last:
			return 7;
		}
		return 0;
	}
}
