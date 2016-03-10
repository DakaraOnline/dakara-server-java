
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum eMessages {
	DontSeeAnything, NPCSwing, NPCKillUser, BlockedWithShieldUser, BlockedWithShieldother, UserSwing, SafeModeOn, SafeModeOff, ResuscitationSafeOff, ResuscitationSafeOn, NobilityLost, CantUseWhileMeditating, NPCHitUser, UserHitNPC, UserAttackedSwing, UserHittedByUser, UserHittedUser, WorkRequestTarget, HaveKilledUser, UserKill, EarnExp, Home, CancelHome, FinishHome;
	public static eMessages fromInteger(int x) {
		switch (x) {
		case 0:
			return DontSeeAnything;
		case 1:
			return NPCSwing;
		case 2:
			return NPCKillUser;
		case 3:
			return BlockedWithShieldUser;
		case 4:
			return BlockedWithShieldother;
		case 5:
			return UserSwing;
		case 6:
			return SafeModeOn;
		case 7:
			return SafeModeOff;
		case 8:
			return ResuscitationSafeOff;
		case 9:
			return ResuscitationSafeOn;
		case 10:
			return NobilityLost;
		case 11:
			return CantUseWhileMeditating;
		case 12:
			return NPCHitUser;
		case 13:
			return UserHitNPC;
		case 14:
			return UserAttackedSwing;
		case 15:
			return UserHittedByUser;
		case 16:
			return UserHittedUser;
		case 17:
			return WorkRequestTarget;
		case 18:
			return HaveKilledUser;
		case 19:
			return UserKill;
		case 20:
			return EarnExp;
		case 21:
			return Home;
		case 22:
			return CancelHome;
		case 23:
			return FinishHome;
		}
		return null;
	}

	public static int toInteger(eMessages x) {
		switch (x) {
		case DontSeeAnything:
			return 0;
		case NPCSwing:
			return 1;
		case NPCKillUser:
			return 2;
		case BlockedWithShieldUser:
			return 3;
		case BlockedWithShieldother:
			return 4;
		case UserSwing:
			return 5;
		case SafeModeOn:
			return 6;
		case SafeModeOff:
			return 7;
		case ResuscitationSafeOff:
			return 8;
		case ResuscitationSafeOn:
			return 9;
		case NobilityLost:
			return 10;
		case CantUseWhileMeditating:
			return 11;
		case NPCHitUser:
			return 12;
		case UserHitNPC:
			return 13;
		case UserAttackedSwing:
			return 14;
		case UserHittedByUser:
			return 15;
		case UserHittedUser:
			return 16;
		case WorkRequestTarget:
			return 17;
		case HaveKilledUser:
			return 18;
		case UserKill:
			return 19;
		case EarnExp:
			return 20;
		case Home:
			return 21;
		case CancelHome:
			return 22;
		case FinishHome:
			return 23;
		}
		return 0;
	}

	public int toInteger() {
		return toInteger(this);
	}
}
