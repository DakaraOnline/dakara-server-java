
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum eForumType {
	ieGeneral, ieREAL, ieCAOS;
	public static eForumType fromInteger(int x) {
		switch (x) {
		case 0:
			return ieGeneral;
		case 1:
			return ieREAL;
		case 2:
			return ieCAOS;
		}
		return null;
	}

	public static int toInteger(eForumType x) {
		switch (x) {
		case ieGeneral:
			return 0;
		case ieREAL:
			return 1;
		case ieCAOS:
			return 2;
		}
		return 0;
	}
}
