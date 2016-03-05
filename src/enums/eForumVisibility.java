
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum eForumVisibility {
	ieGENERAL_MEMBER, ieREAL_MEMBER, ieCAOS_MEMBER;
	public static eForumVisibility fromInteger(int x) {
		switch (x) {
		case 1:
			return ieGENERAL_MEMBER;
		case 2:
			return ieREAL_MEMBER;
		case 4:
			return ieCAOS_MEMBER;
		}
		return null;
	}

	public static int toInteger(eForumVisibility x) {
		switch (x) {
		case ieGENERAL_MEMBER:
			return 1;
		case ieREAL_MEMBER:
			return 2;
		case ieCAOS_MEMBER:
			return 4;
		}
		return 0;
	}
}
