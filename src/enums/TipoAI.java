
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum TipoAI {
	ESTATICO, MueveAlAzar, NpcMaloAtacaUsersBuenos, NPCDEFENSA, GuardiasAtacanCriminales, NpcObjeto, SigueAmo, NpcAtacaNpc, NpcPathfinding, SacerdotePretorianoAi, GuerreroPretorianoAi, MagoPretorianoAi, CazadorPretorianoAi, ReyPretoriano;
	public static TipoAI fromInteger(int x) {
		switch (x) {
		case 1:
			return ESTATICO;
		case 2:
			return MueveAlAzar;
		case 3:
			return NpcMaloAtacaUsersBuenos;
		case 4:
			return NPCDEFENSA;
		case 5:
			return GuardiasAtacanCriminales;
		case 6:
			return NpcObjeto;
		case 8:
			return SigueAmo;
		case 9:
			return NpcAtacaNpc;
		case 10:
			return NpcPathfinding;
		case 20:
			return SacerdotePretorianoAi;
		case 21:
			return GuerreroPretorianoAi;
		case 22:
			return MagoPretorianoAi;
		case 23:
			return CazadorPretorianoAi;
		case 24:
			return ReyPretoriano;
		}
		return null;
	}

	public static int toInteger(TipoAI x) {
		switch (x) {
		case ESTATICO:
			return 1;
		case MueveAlAzar:
			return 2;
		case NpcMaloAtacaUsersBuenos:
			return 3;
		case NPCDEFENSA:
			return 4;
		case GuardiasAtacanCriminales:
			return 5;
		case NpcObjeto:
			return 6;
		case SigueAmo:
			return 8;
		case NpcAtacaNpc:
			return 9;
		case NpcPathfinding:
			return 10;
		case SacerdotePretorianoAi:
			return 20;
		case GuerreroPretorianoAi:
			return 21;
		case MagoPretorianoAi:
			return 22;
		case CazadorPretorianoAi:
			return 23;
		case ReyPretoriano:
			return 24;
		}
		return 0;
	}
}
