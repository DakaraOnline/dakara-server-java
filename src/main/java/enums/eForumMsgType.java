
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum eForumMsgType {
	ieGeneral, ieGENERAL_STICKY, ieREAL, ieREAL_STICKY, ieCAOS, ieCAOS_STICKY;
	public static eForumMsgType fromInteger(int x) {
		switch (x) {
		case 0:
			return ieGeneral;
		case 1:
			return ieGENERAL_STICKY;
		case 2:
			return ieREAL;
		case 3:
			return ieREAL_STICKY;
		case 4:
			return ieCAOS;
		case 5:
			return ieCAOS_STICKY;
		}
		return null;
	}

	public static int toInteger(eForumMsgType x) {
		switch (x) {
		case ieGeneral:
			return 0;
		case ieGENERAL_STICKY:
			return 1;
		case ieREAL:
			return 2;
		case ieREAL_STICKY:
			return 3;
		case ieCAOS:
			return 4;
		case ieCAOS_STICKY:
			return 5;
		}
		return 0;
	}

	public int toInteger() {
		return toInteger(this);
	}
}
