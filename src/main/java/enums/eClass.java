
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum eClass {
	Mage, Cleric, Warrior, Assasin, Thief, Bard, Druid, Bandit, Paladin, Hunter, Worker, Pirat;
	public static eClass fromInteger(int x) {
		switch (x) {
		case 1:
			return Mage;
		case 2:
			return Cleric;
		case 3:
			return Warrior;
		case 4:
			return Assasin;
		case 5:
			return Thief;
		case 6:
			return Bard;
		case 7:
			return Druid;
		case 8:
			return Bandit;
		case 9:
			return Paladin;
		case 10:
			return Hunter;
		case 11:
			return Worker;
		case 12:
			return Pirat;
		}
		return null;
	}

	public static int toInteger(eClass x) {
		switch (x) {
		case Mage:
			return 1;
		case Cleric:
			return 2;
		case Warrior:
			return 3;
		case Assasin:
			return 4;
		case Thief:
			return 5;
		case Bard:
			return 6;
		case Druid:
			return 7;
		case Bandit:
			return 8;
		case Paladin:
			return 9;
		case Hunter:
			return 10;
		case Worker:
			return 11;
		case Pirat:
			return 12;
		}
		return 0;
	}

	public int toInteger() {
		return toInteger(this);
	}
}
