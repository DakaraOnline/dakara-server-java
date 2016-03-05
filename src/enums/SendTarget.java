
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum SendTarget {
	ToAll, toMap, ToPCArea, ToAllButIndex, ToMapButIndex, ToGM, ToNPCArea, ToGuildMembers, ToAdmins, ToPCAreaButIndex, ToAdminsAreaButConsejeros, ToDiosesYclan, ToConsejo, ToClanArea, ToConsejoCaos, ToRolesMasters, ToDeadArea, ToCiudadanos, ToCriminales, ToPartyArea, ToReal, ToCaos, ToCiudadanosYRMs, ToCriminalesYRMs, ToRealYRMs, ToCaosYRMs, ToHigherAdmins, ToGMsAreaButRmsOrCounselors, ToUsersAreaButGMs, ToUsersAndRmsAndCounselorsAreaButGMs, ToAdminsButCounselorsAndRms, ToHigherAdminsButRMs, ToAdminsButCounselors, ToRMsAndHigherAdmins;
	public static SendTarget fromInteger(int x) {
		switch (x) {
		case 1:
			return ToAll;
		case 2:
			return toMap;
		case 3:
			return ToPCArea;
		case 4:
			return ToAllButIndex;
		case 5:
			return ToMapButIndex;
		case 6:
			return ToGM;
		case 7:
			return ToNPCArea;
		case 8:
			return ToGuildMembers;
		case 9:
			return ToAdmins;
		case 10:
			return ToPCAreaButIndex;
		case 11:
			return ToAdminsAreaButConsejeros;
		case 12:
			return ToDiosesYclan;
		case 13:
			return ToConsejo;
		case 14:
			return ToClanArea;
		case 15:
			return ToConsejoCaos;
		case 16:
			return ToRolesMasters;
		case 17:
			return ToDeadArea;
		case 18:
			return ToCiudadanos;
		case 19:
			return ToCriminales;
		case 20:
			return ToPartyArea;
		case 21:
			return ToReal;
		case 22:
			return ToCaos;
		case 23:
			return ToCiudadanosYRMs;
		case 24:
			return ToCriminalesYRMs;
		case 25:
			return ToRealYRMs;
		case 26:
			return ToCaosYRMs;
		case 27:
			return ToHigherAdmins;
		case 28:
			return ToGMsAreaButRmsOrCounselors;
		case 29:
			return ToUsersAreaButGMs;
		case 30:
			return ToUsersAndRmsAndCounselorsAreaButGMs;
		case 31:
			return ToAdminsButCounselorsAndRms;
		case 32:
			return ToHigherAdminsButRMs;
		case 33:
			return ToAdminsButCounselors;
		case 34:
			return ToRMsAndHigherAdmins;
		}
		return null;
	}

	public static int toInteger(SendTarget x) {
		switch (x) {
		case ToAll:
			return 1;
		case toMap:
			return 2;
		case ToPCArea:
			return 3;
		case ToAllButIndex:
			return 4;
		case ToMapButIndex:
			return 5;
		case ToGM:
			return 6;
		case ToNPCArea:
			return 7;
		case ToGuildMembers:
			return 8;
		case ToAdmins:
			return 9;
		case ToPCAreaButIndex:
			return 10;
		case ToAdminsAreaButConsejeros:
			return 11;
		case ToDiosesYclan:
			return 12;
		case ToConsejo:
			return 13;
		case ToClanArea:
			return 14;
		case ToConsejoCaos:
			return 15;
		case ToRolesMasters:
			return 16;
		case ToDeadArea:
			return 17;
		case ToCiudadanos:
			return 18;
		case ToCriminales:
			return 19;
		case ToPartyArea:
			return 20;
		case ToReal:
			return 21;
		case ToCaos:
			return 22;
		case ToCiudadanosYRMs:
			return 23;
		case ToCriminalesYRMs:
			return 24;
		case ToRealYRMs:
			return 25;
		case ToCaosYRMs:
			return 26;
		case ToHigherAdmins:
			return 27;
		case ToGMsAreaButRmsOrCounselors:
			return 28;
		case ToUsersAreaButGMs:
			return 29;
		case ToUsersAndRmsAndCounselorsAreaButGMs:
			return 30;
		case ToAdminsButCounselorsAndRms:
			return 31;
		case ToHigherAdminsButRMs:
			return 32;
		case ToAdminsButCounselors:
			return 33;
		case ToRMsAndHigherAdmins:
			return 34;
		}
		return 0;
	}
}
