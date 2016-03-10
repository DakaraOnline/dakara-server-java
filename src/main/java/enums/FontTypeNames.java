
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum FontTypeNames {
	FONTTYPE_TALK, FONTTYPE_FIGHT, FONTTYPE_WARNING, FONTTYPE_INFO, FONTTYPE_INFOBOLD, FONTTYPE_EJECUCION, FONTTYPE_PARTY, FONTTYPE_VENENO, FONTTYPE_GUILD, FONTTYPE_SERVER, FONTTYPE_GUILDMSG, FONTTYPE_CONSEJO, FONTTYPE_CONSEJOCAOS, FONTTYPE_CONSEJOVesA, FONTTYPE_CONSEJOCAOSVesA, FONTTYPE_CENTINELA, FONTTYPE_GMMSG, FONTTYPE_GM, FONTTYPE_CITIZEN, FONTTYPE_CONSE, FONTTYPE_DIOS;
	public static FontTypeNames fromInteger(int x) {
		switch (x) {
		case 0:
			return FONTTYPE_TALK;
		case 1:
			return FONTTYPE_FIGHT;
		case 2:
			return FONTTYPE_WARNING;
		case 3:
			return FONTTYPE_INFO;
		case 4:
			return FONTTYPE_INFOBOLD;
		case 5:
			return FONTTYPE_EJECUCION;
		case 6:
			return FONTTYPE_PARTY;
		case 7:
			return FONTTYPE_VENENO;
		case 8:
			return FONTTYPE_GUILD;
		case 9:
			return FONTTYPE_SERVER;
		case 10:
			return FONTTYPE_GUILDMSG;
		case 11:
			return FONTTYPE_CONSEJO;
		case 12:
			return FONTTYPE_CONSEJOCAOS;
		case 13:
			return FONTTYPE_CONSEJOVesA;
		case 14:
			return FONTTYPE_CONSEJOCAOSVesA;
		case 15:
			return FONTTYPE_CENTINELA;
		case 16:
			return FONTTYPE_GMMSG;
		case 17:
			return FONTTYPE_GM;
		case 18:
			return FONTTYPE_CITIZEN;
		case 19:
			return FONTTYPE_CONSE;
		case 20:
			return FONTTYPE_DIOS;
		}
		return null;
	}

	public static int toInteger(FontTypeNames x) {
		switch (x) {
		case FONTTYPE_TALK:
			return 0;
		case FONTTYPE_FIGHT:
			return 1;
		case FONTTYPE_WARNING:
			return 2;
		case FONTTYPE_INFO:
			return 3;
		case FONTTYPE_INFOBOLD:
			return 4;
		case FONTTYPE_EJECUCION:
			return 5;
		case FONTTYPE_PARTY:
			return 6;
		case FONTTYPE_VENENO:
			return 7;
		case FONTTYPE_GUILD:
			return 8;
		case FONTTYPE_SERVER:
			return 9;
		case FONTTYPE_GUILDMSG:
			return 10;
		case FONTTYPE_CONSEJO:
			return 11;
		case FONTTYPE_CONSEJOCAOS:
			return 12;
		case FONTTYPE_CONSEJOVesA:
			return 13;
		case FONTTYPE_CONSEJOCAOSVesA:
			return 14;
		case FONTTYPE_CENTINELA:
			return 15;
		case FONTTYPE_GMMSG:
			return 16;
		case FONTTYPE_GM:
			return 17;
		case FONTTYPE_CITIZEN:
			return 18;
		case FONTTYPE_CONSE:
			return 19;
		case FONTTYPE_DIOS:
			return 20;
		}
		return 0;
	}

	public int toInteger() {
		return toInteger(this);
	}
}
