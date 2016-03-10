
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum eClanType {
	ct_RoyalArmy, ct_Evil, ct_Neutral, ct_GM, ct_Legal, ct_Criminal;
	public static eClanType fromInteger(int x) {
		switch (x) {
		case 0:
			return ct_RoyalArmy;
		case 1:
			return ct_Evil;
		case 2:
			return ct_Neutral;
		case 3:
			return ct_GM;
		case 4:
			return ct_Legal;
		case 5:
			return ct_Criminal;
		}
		return null;
	}

	public static int toInteger(eClanType x) {
		switch (x) {
		case ct_RoyalArmy:
			return 0;
		case ct_Evil:
			return 1;
		case ct_Neutral:
			return 2;
		case ct_GM:
			return 3;
		case ct_Legal:
			return 4;
		case ct_Criminal:
			return 5;
		}
		return 0;
	}

	public int toInteger() {
		return toInteger(this);
	}
}
