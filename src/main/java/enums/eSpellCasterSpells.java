
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum eSpellCasterSpells {
	Apocalipsis, RemoInvi;
	public static eSpellCasterSpells fromInteger(int x) {
		switch (x) {
		case 1:
			return Apocalipsis;
		case 2:
			return RemoInvi;
		}
		return null;
	}

	public static int toInteger(eSpellCasterSpells x) {
		switch (x) {
		case Apocalipsis:
			return 1;
		case RemoInvi:
			return 2;
		}
		return 0;
	}

	public int toInteger() {
		return toInteger(this);
	}
}
