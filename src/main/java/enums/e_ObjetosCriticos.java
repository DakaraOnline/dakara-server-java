
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum e_ObjetosCriticos {
	Manzana, Manzana2, ManzanaNewbie;
	public static e_ObjetosCriticos fromInteger(int x) {
		switch (x) {
		case 1:
			return Manzana;
		case 2:
			return Manzana2;
		case 467:
			return ManzanaNewbie;
		}
		return null;
	}

	public static int toInteger(e_ObjetosCriticos x) {
		switch (x) {
		case Manzana:
			return 1;
		case Manzana2:
			return 2;
		case ManzanaNewbie:
			return 467;
		}
		return 0;
	}

	public int toInteger() {
		return toInteger(this);
	}
}
