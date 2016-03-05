
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum eOBJType {
	otUseOnce, otWeapon, otArmadura, otArboles, otGuita, otPuertas, otContenedores, otCarteles, otLlaves, otForos, otPociones, otBebidas, otLena, otFogata, otESCUDO, otCASCO, otAnillo, otTeleport, otYacimiento, otMinerales, otPergaminos, otInstrumentos, otYunque, otFragua, otBarcos, otFlechas, otBotellaVacia, otBotellaLlena, otManchas, otArbolElfico, otMochilas, otYacimientoPez, otCualquiera;
	public static eOBJType fromInteger(int x) {
		switch (x) {
		case 1:
			return otUseOnce;
		case 2:
			return otWeapon;
		case 3:
			return otArmadura;
		case 4:
			return otArboles;
		case 5:
			return otGuita;
		case 6:
			return otPuertas;
		case 7:
			return otContenedores;
		case 8:
			return otCarteles;
		case 9:
			return otLlaves;
		case 10:
			return otForos;
		case 11:
			return otPociones;
		case 13:
			return otBebidas;
		case 14:
			return otLena;
		case 15:
			return otFogata;
		case 16:
			return otESCUDO;
		case 17:
			return otCASCO;
		case 18:
			return otAnillo;
		case 19:
			return otTeleport;
		case 22:
			return otYacimiento;
		case 23:
			return otMinerales;
		case 24:
			return otPergaminos;
		case 26:
			return otInstrumentos;
		case 27:
			return otYunque;
		case 28:
			return otFragua;
		case 31:
			return otBarcos;
		case 32:
			return otFlechas;
		case 33:
			return otBotellaVacia;
		case 34:
			return otBotellaLlena;
		case 35:
			return otManchas;
		case 36:
			return otArbolElfico;
		case 37:
			return otMochilas;
		case 38:
			return otYacimientoPez;
		case 1000:
			return otCualquiera;
		}
		return null;
	}

	public static int toInteger(eOBJType x) {
		switch (x) {
		case otUseOnce:
			return 1;
		case otWeapon:
			return 2;
		case otArmadura:
			return 3;
		case otArboles:
			return 4;
		case otGuita:
			return 5;
		case otPuertas:
			return 6;
		case otContenedores:
			return 7;
		case otCarteles:
			return 8;
		case otLlaves:
			return 9;
		case otForos:
			return 10;
		case otPociones:
			return 11;
		case otBebidas:
			return 13;
		case otLena:
			return 14;
		case otFogata:
			return 15;
		case otESCUDO:
			return 16;
		case otCASCO:
			return 17;
		case otAnillo:
			return 18;
		case otTeleport:
			return 19;
		case otYacimiento:
			return 22;
		case otMinerales:
			return 23;
		case otPergaminos:
			return 24;
		case otInstrumentos:
			return 26;
		case otYunque:
			return 27;
		case otFragua:
			return 28;
		case otBarcos:
			return 31;
		case otFlechas:
			return 32;
		case otBotellaVacia:
			return 33;
		case otBotellaLlena:
			return 34;
		case otManchas:
			return 35;
		case otArbolElfico:
			return 36;
		case otMochilas:
			return 37;
		case otYacimientoPez:
			return 38;
		case otCualquiera:
			return 1000;
		}
		return 0;
	}
}
