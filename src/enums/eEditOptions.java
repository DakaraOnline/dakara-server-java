
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum eEditOptions {
	eo_Gold, eo_Experience, eo_Body, eo_Head, eo_CiticensKilled, eo_CriminalsKilled, eo_Level, eo_Class, eo_Skills, eo_SkillPointsLeft, eo_Nobleza, eo_Asesino, eo_Sex, eo_Raza, eo_addGold, eo_Vida, eo_Poss;
	public static eEditOptions fromInteger(int x) {
		switch (x) {
		case 1:
			return eo_Gold;
		case 2:
			return eo_Experience;
		case 3:
			return eo_Body;
		case 4:
			return eo_Head;
		case 5:
			return eo_CiticensKilled;
		case 6:
			return eo_CriminalsKilled;
		case 7:
			return eo_Level;
		case 8:
			return eo_Class;
		case 9:
			return eo_Skills;
		case 10:
			return eo_SkillPointsLeft;
		case 11:
			return eo_Nobleza;
		case 12:
			return eo_Asesino;
		case 13:
			return eo_Sex;
		case 14:
			return eo_Raza;
		case 15:
			return eo_addGold;
		case 16:
			return eo_Vida;
		case 17:
			return eo_Poss;
		}
		return null;
	}

	public static int toInteger(eEditOptions x) {
		switch (x) {
		case eo_Gold:
			return 1;
		case eo_Experience:
			return 2;
		case eo_Body:
			return 3;
		case eo_Head:
			return 4;
		case eo_CiticensKilled:
			return 5;
		case eo_CriminalsKilled:
			return 6;
		case eo_Level:
			return 7;
		case eo_Class:
			return 8;
		case eo_Skills:
			return 9;
		case eo_SkillPointsLeft:
			return 10;
		case eo_Nobleza:
			return 11;
		case eo_Asesino:
			return 12;
		case eo_Sex:
			return 13;
		case eo_Raza:
			return 14;
		case eo_addGold:
			return 15;
		case eo_Vida:
			return 16;
		case eo_Poss:
			return 17;
		}
		return 0;
	}
}
