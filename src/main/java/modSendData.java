
/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"modSendData"')] */
/* '************************************************************** */
/* ' SendData.bas - Has all methods to send data to different user groups. */
/* ' Makes use of the modAreas module. */
/* ' */
/* ' Implemented by Juan Martín Sotuyo Dodero (Maraxus) (juansotuyo@gmail.com) */
/* '************************************************************** */

/* '************************************************************************** */
/* 'This program is free software; you can redistribute it and/or modify */
/* 'it under the terms of the Affero General Public License; */
/* 'either version 1 of the License, or any later version. */
/* ' */
/* 'This program is distributed in the hope that it will be useful, */
/* 'but WITHOUT ANY WARRANTY; without even the implied warranty of */
/* 'MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the */
/* 'Affero General Public License for more details. */
/* ' */
/* 'You should have received a copy of the Affero General Public License */
/* 'along with this program; if not, you can find it at http://www.affero.org/oagpl.html */
/* '************************************************************************** */

/* '' */
/* ' Contains all methods to send data to different user groups. */
/* ' Makes use of the modAreas module. */
/* ' */
/* ' @author Juan Martín Sotuyo Dodero (Maraxus) juansotuyo@gmail.com */
/* ' @version 1.0.0 */
/* ' @date 20070107 */

import enums.*;

public class modSendData {

	static void SendData(SendTarget sndRoute, int sndIndex, String sndData) {
		SendData(sndRoute, sndIndex, sndData, false);
	}

	static void SendData(SendTarget sndRoute, int sndIndex, String sndData, boolean IsDenounce) {
		/* '************************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) - Rewrite of original */
		/* 'Last Modify Date: 14/11/2010 */
		/* 'Last modified by: ZaMa */
		/* '14/11/2010: ZaMa - Now denounces can be desactivated. */
		/* '************************************************************** */
		/* FIXME: ON ERROR RESUME NEXT */
		int LoopC = 0;

		switch (sndRoute) {
		case ToPCArea:
			SendToUserArea(sndIndex, sndData);
			return;

			break;

		case ToAdmins:
			for (LoopC = (1); LoopC <= (Declaraciones.LastUser); LoopC++) {
				if (Declaraciones.UserList[LoopC].ConnID != -1) {
					if (Declaraciones.UserList[LoopC].flags.Privilegios
							&& (PlayerType.Admin || PlayerType.Dios || PlayerType.SemiDios || PlayerType.Consejero)) {
						/* ' Denounces can be desactivated */
						if (IsDenounce) {
							if (Declaraciones.UserList[LoopC].flags.SendDenounces) {
								TCP.EnviarDatosASlot(LoopC, sndData);
							}
						} else {
							TCP.EnviarDatosASlot(LoopC, sndData);
						}
					}
				}
			}
			return;

			break;

		case ToAll:
			for (LoopC = (1); LoopC <= (Declaraciones.LastUser); LoopC++) {
				if (Declaraciones.UserList[LoopC].ConnID != -1) {
					/* 'Esta logeado como usuario? */
					if (Declaraciones.UserList[LoopC].flags.UserLogged) {
						TCP.EnviarDatosASlot(LoopC, sndData);
					}
				}
			}
			return;

			break;

		case ToAllButIndex:
			for (LoopC = (1); LoopC <= (Declaraciones.LastUser); LoopC++) {
				if ((Declaraciones.UserList[LoopC].ConnID != -1) && (LoopC != sndIndex)) {
					/* 'Esta logeado como usuario? */
					if (Declaraciones.UserList[LoopC].flags.UserLogged) {
						TCP.EnviarDatosASlot(LoopC, sndData);
					}
				}
			}
			return;

			break;

		case toMap:
			SendToMap(sndIndex, sndData);
			return;

			break;

		case ToMapButIndex:
			SendToMapButIndex(sndIndex, sndData);
			return;

			break;

		case ToGuildMembers:
			LoopC = modGuilds.m_Iterador_ProximoUserIndex(sndIndex);
			while (LoopC > 0) {
				if ((Declaraciones.UserList[LoopC].ConnID != -1)) {
					TCP.EnviarDatosASlot(LoopC, sndData);
				}
				LoopC = modGuilds.m_Iterador_ProximoUserIndex(sndIndex);
			}
			return;

			break;

		case ToDeadArea:
			SendToDeadUserArea(sndIndex, sndData);
			return;

			break;

		case ToPCAreaButIndex:
			SendToUserAreaButindex(sndIndex, sndData);
			return;

			break;

		case ToClanArea:
			SendToUserGuildArea(sndIndex, sndData);
			return;

			break;

		case ToPartyArea:
			SendToUserPartyArea(sndIndex, sndData);
			return;

			break;

		case ToAdminsAreaButConsejeros:
			SendToAdminsButConsejerosArea(sndIndex, sndData);
			return;

			break;

		case ToNPCArea:
			SendToNpcArea(sndIndex, sndData);
			return;

			break;

		case ToDiosesYclan:
			LoopC = modGuilds.m_Iterador_ProximoUserIndex(sndIndex);
			while (LoopC > 0) {
				if ((Declaraciones.UserList[LoopC].ConnID != -1)) {
					TCP.EnviarDatosASlot(LoopC, sndData);
				}
				LoopC = modGuilds.m_Iterador_ProximoUserIndex(sndIndex);
			}

			LoopC = modGuilds.Iterador_ProximoGM(sndIndex);
			while (LoopC > 0) {
				if ((Declaraciones.UserList[LoopC].ConnID != -1)) {
					TCP.EnviarDatosASlot(LoopC, sndData);
				}
				LoopC = modGuilds.Iterador_ProximoGM(sndIndex);
			}

			return;

			break;

		case ToConsejo:
			for (LoopC = (1); LoopC <= (Declaraciones.LastUser); LoopC++) {
				if ((Declaraciones.UserList[LoopC].ConnID != -1)) {
					if (Declaraciones.UserList[LoopC].flags.Privilegios && PlayerType.RoyalCouncil) {
						TCP.EnviarDatosASlot(LoopC, sndData);
					}
				}
			}
			return;

			break;

		case ToConsejoCaos:
			for (LoopC = (1); LoopC <= (Declaraciones.LastUser); LoopC++) {
				if ((Declaraciones.UserList[LoopC].ConnID != -1)) {
					if (Declaraciones.UserList[LoopC].flags.Privilegios && PlayerType.ChaosCouncil) {
						TCP.EnviarDatosASlot(LoopC, sndData);
					}
				}
			}
			return;

			break;

		case ToRolesMasters:
			for (LoopC = (1); LoopC <= (Declaraciones.LastUser); LoopC++) {
				if ((Declaraciones.UserList[LoopC].ConnID != -1)) {
					if (Declaraciones.UserList[LoopC].flags.Privilegios && PlayerType.RoleMaster) {
						TCP.EnviarDatosASlot(LoopC, sndData);
					}
				}
			}
			return;

			break;

		case ToCiudadanos:
			for (LoopC = (1); LoopC <= (Declaraciones.LastUser); LoopC++) {
				if ((Declaraciones.UserList[LoopC].ConnID != -1)) {
					if (!ES.criminal(LoopC)) {
						TCP.EnviarDatosASlot(LoopC, sndData);
					}
				}
			}
			return;

			break;

		case ToCriminales:
			for (LoopC = (1); LoopC <= (Declaraciones.LastUser); LoopC++) {
				if ((Declaraciones.UserList[LoopC].ConnID != -1)) {
					if (ES.criminal(LoopC)) {
						TCP.EnviarDatosASlot(LoopC, sndData);
					}
				}
			}
			return;

			break;

		case ToReal:
			for (LoopC = (1); LoopC <= (Declaraciones.LastUser); LoopC++) {
				if ((Declaraciones.UserList[LoopC].ConnID != -1)) {
					if (Declaraciones.UserList[LoopC].Faccion.ArmadaReal == 1) {
						TCP.EnviarDatosASlot(LoopC, sndData);
					}
				}
			}
			return;

			break;

		case ToCaos:
			for (LoopC = (1); LoopC <= (Declaraciones.LastUser); LoopC++) {
				if ((Declaraciones.UserList[LoopC].ConnID != -1)) {
					if (Declaraciones.UserList[LoopC].Faccion.FuerzasCaos == 1) {
						TCP.EnviarDatosASlot(LoopC, sndData);
					}
				}
			}
			return;

			break;

		case ToCiudadanosYRMs:
			for (LoopC = (1); LoopC <= (Declaraciones.LastUser); LoopC++) {
				if ((Declaraciones.UserList[LoopC].ConnID != -1)) {
					if (!ES.criminal(LoopC)
							|| (Declaraciones.UserList[LoopC].flags.Privilegios && PlayerType.RoleMaster) != 0) {
						TCP.EnviarDatosASlot(LoopC, sndData);
					}
				}
			}
			return;

			break;

		case ToCriminalesYRMs:
			for (LoopC = (1); LoopC <= (Declaraciones.LastUser); LoopC++) {
				if ((Declaraciones.UserList[LoopC].ConnID != -1)) {
					if (ES.criminal(LoopC)
							|| (Declaraciones.UserList[LoopC].flags.Privilegios && PlayerType.RoleMaster) != 0) {
						TCP.EnviarDatosASlot(LoopC, sndData);
					}
				}
			}
			return;

			break;

		case ToRealYRMs:
			for (LoopC = (1); LoopC <= (Declaraciones.LastUser); LoopC++) {
				if ((Declaraciones.UserList[LoopC].ConnID != -1)) {
					if (Declaraciones.UserList[LoopC].Faccion.ArmadaReal == 1
							|| (Declaraciones.UserList[LoopC].flags.Privilegios && PlayerType.RoleMaster) != 0) {
						TCP.EnviarDatosASlot(LoopC, sndData);
					}
				}
			}
			return;

			break;

		case ToCaosYRMs:
			for (LoopC = (1); LoopC <= (Declaraciones.LastUser); LoopC++) {
				if ((Declaraciones.UserList[LoopC].ConnID != -1)) {
					if (Declaraciones.UserList[LoopC].Faccion.FuerzasCaos == 1
							|| (Declaraciones.UserList[LoopC].flags.Privilegios && PlayerType.RoleMaster) != 0) {
						TCP.EnviarDatosASlot(LoopC, sndData);
					}
				}
			}
			return;

			break;

		case ToHigherAdmins:
			for (LoopC = (1); LoopC <= (Declaraciones.LastUser); LoopC++) {
				if (Declaraciones.UserList[LoopC].ConnID != -1) {
					if (Declaraciones.UserList[LoopC].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios)) {
						TCP.EnviarDatosASlot(LoopC, sndData);
					}
				}
			}
			return;

			break;

		case ToGMsAreaButRmsOrCounselors:
			SendToGMsAreaButRmsOrCounselors(sndIndex, sndData);
			return;

			break;

		case ToUsersAreaButGMs:
			SendToUsersAreaButGMs(sndIndex, sndData);
			return;

			break;

		case ToUsersAndRmsAndCounselorsAreaButGMs:
			SendToUsersAndRmsAndCounselorsAreaButGMs(sndIndex, sndData);
			return;

			break;

		case ToAdminsButCounselors:
			for (LoopC = (1); LoopC <= (Declaraciones.LastUser); LoopC++) {
				if (Declaraciones.UserList[LoopC].ConnID != -1) {
					if (Declaraciones.UserList[LoopC].flags.Privilegios
							&& (PlayerType.Admin || PlayerType.Dios || PlayerType.SemiDios)
							|| ((Declaraciones.UserList[LoopC].flags.Privilegios && (PlayerType.RoleMaster)) != 0
									&& (Declaraciones.UserList[LoopC].flags.Privilegios
											&& (PlayerType.Consejero)) != 0)) {
						TCP.EnviarDatosASlot(LoopC, sndData);
					}
				}
			}
			return;

			break;

		case ToAdminsButCounselorsAndRms:
			for (LoopC = (1); LoopC <= (Declaraciones.LastUser); LoopC++) {
				if (Declaraciones.UserList[LoopC].ConnID != -1) {
					if (Declaraciones.UserList[LoopC].flags.Privilegios
							&& (PlayerType.Admin || PlayerType.Dios || PlayerType.SemiDios)) {
						if ((Declaraciones.UserList[LoopC].flags.Privilegios && (PlayerType.RoleMaster)) == 0) {
							TCP.EnviarDatosASlot(LoopC, sndData);
						}
					}
				}
			}
			return;

			break;

		case ToHigherAdminsButRMs:
			for (LoopC = (1); LoopC <= (Declaraciones.LastUser); LoopC++) {
				if (Declaraciones.UserList[LoopC].ConnID != -1) {
					if (Declaraciones.UserList[LoopC].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios)) {
						if ((Declaraciones.UserList[LoopC].flags.Privilegios && (PlayerType.RoleMaster)) == 0) {
							TCP.EnviarDatosASlot(LoopC, sndData);
						}
					}
				}
			}
			return;

			break;

		case ToRMsAndHigherAdmins:
			for (LoopC = (1); LoopC <= (Declaraciones.LastUser); LoopC++) {
				if ((Declaraciones.UserList[LoopC].ConnID != -1)) {
					if (Declaraciones.UserList[LoopC].flags.Privilegios
							&& (PlayerType.RoleMaster || PlayerType.Admin || PlayerType.Dios)) {
						TCP.EnviarDatosASlot(LoopC, sndData);
					}
				}
			}
			return;
			break;
		}
	}

	static void SendToUserArea(int UserIndex, String sdData) {
		/* '************************************************************** */
		/* 'Author: Lucio N. Tourrilhes (DuNga) */
		/* 'Last Modify Date: Unknow */
		/* ' */
		/* '************************************************************** */
		int LoopC = 0;
		int tempIndex = 0;

		int Map = 0;
		int AreaX = 0;
		int AreaY = 0;

		Map = Declaraciones.UserList[UserIndex].Pos.Map;
		AreaX = Declaraciones.UserList[UserIndex].AreasInfo.AreaPerteneceX;
		AreaY = Declaraciones.UserList[UserIndex].AreasInfo.AreaPerteneceY;

		if (!General.MapaValido(Map)) {
			return;
		}

		for (LoopC = (1); LoopC <= (ModAreas.ConnGroups[Map].CountEntrys); LoopC++) {
			tempIndex = ModAreas.ConnGroups[Map].UserEntrys[LoopC];

			/* 'Esta en el area? */
			if (Declaraciones.UserList[tempIndex].AreasInfo.AreaReciveX && AreaX) {
				if (Declaraciones.UserList[tempIndex].AreasInfo.AreaReciveY && AreaY) {
					if (Declaraciones.UserList[tempIndex].ConnIDValida) {
						TCP.EnviarDatosASlot(tempIndex, sdData);
					}
				}
			}
		}
	}

	static void SendToUserAreaButindex(int UserIndex, String sdData) {
		/* '************************************************************** */
		/* 'Author: Lucio N. Tourrilhes (DuNga) */
		/* 'Last Modify Date: Unknow */
		/* ' */
		/* '************************************************************** */
		int LoopC = 0;
		int TempInt = 0;
		int tempIndex = 0;

		int Map = 0;
		int AreaX = 0;
		int AreaY = 0;

		Map = Declaraciones.UserList[UserIndex].Pos.Map;
		AreaX = Declaraciones.UserList[UserIndex].AreasInfo.AreaPerteneceX;
		AreaY = Declaraciones.UserList[UserIndex].AreasInfo.AreaPerteneceY;

		if (!General.MapaValido(Map)) {
			return;
		}

		for (LoopC = (1); LoopC <= (ModAreas.ConnGroups[Map].CountEntrys); LoopC++) {
			tempIndex = ModAreas.ConnGroups[Map].UserEntrys[LoopC];

			TempInt = Declaraciones.UserList[tempIndex].AreasInfo.AreaReciveX && AreaX;
			/* 'Esta en el area? */
			if (TempInt) {
				TempInt = Declaraciones.UserList[tempIndex].AreasInfo.AreaReciveY && AreaY;
				if (TempInt) {
					if (tempIndex != UserIndex) {
						if (Declaraciones.UserList[tempIndex].ConnIDValida) {
							TCP.EnviarDatosASlot(tempIndex, sdData);
						}
					}
				}
			}
		}
	}

	static void SendToDeadUserArea(int UserIndex, String sdData) {
		/* '************************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modify Date: Unknow */
		/* ' */
		/* '************************************************************** */
		int LoopC = 0;
		int tempIndex = 0;

		int Map = 0;
		int AreaX = 0;
		int AreaY = 0;

		Map = Declaraciones.UserList[UserIndex].Pos.Map;
		AreaX = Declaraciones.UserList[UserIndex].AreasInfo.AreaPerteneceX;
		AreaY = Declaraciones.UserList[UserIndex].AreasInfo.AreaPerteneceY;

		if (!General.MapaValido(Map)) {
			return;
		}

		for (LoopC = (1); LoopC <= (ModAreas.ConnGroups[Map].CountEntrys); LoopC++) {
			tempIndex = ModAreas.ConnGroups[Map].UserEntrys[LoopC];

			/* 'Esta en el area? */
			if (Declaraciones.UserList[tempIndex].AreasInfo.AreaReciveX && AreaX) {
				if (Declaraciones.UserList[tempIndex].AreasInfo.AreaReciveY && AreaY) {
					/* 'Dead and admins read */
					if (Declaraciones.UserList[tempIndex].ConnIDValida == true
							&& (Declaraciones.UserList[tempIndex].flags.Muerto == 1
									|| (Declaraciones.UserList[tempIndex].flags.Privilegios && (PlayerType.Admin
											|| PlayerType.Dios || PlayerType.SemiDios || PlayerType.Consejero)) != 0)) {
						TCP.EnviarDatosASlot(tempIndex, sdData);
					}
				}
			}
		}
	}

	static void SendToUserGuildArea(int UserIndex, String sdData) {
		/* '************************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modify Date: Unknow */
		/* ' */
		/* '************************************************************** */
		int LoopC = 0;
		int tempIndex = 0;

		int Map = 0;
		int AreaX = 0;
		int AreaY = 0;

		Map = Declaraciones.UserList[UserIndex].Pos.Map;
		AreaX = Declaraciones.UserList[UserIndex].AreasInfo.AreaPerteneceX;
		AreaY = Declaraciones.UserList[UserIndex].AreasInfo.AreaPerteneceY;

		if (!General.MapaValido(Map)) {
			return;
		}

		if (Declaraciones.UserList[UserIndex].GuildIndex == 0) {
			return;
		}

		for (LoopC = (1); LoopC <= (ModAreas.ConnGroups[Map].CountEntrys); LoopC++) {
			tempIndex = ModAreas.ConnGroups[Map].UserEntrys[LoopC];

			/* 'Esta en el area? */
			if (Declaraciones.UserList[tempIndex].AreasInfo.AreaReciveX && AreaX) {
				if (Declaraciones.UserList[tempIndex].AreasInfo.AreaReciveY && AreaY) {
					if (Declaraciones.UserList[tempIndex].ConnIDValida
							&& (Declaraciones.UserList[tempIndex].GuildIndex == Declaraciones.UserList[UserIndex].GuildIndex
									|| ((Declaraciones.UserList[tempIndex].flags.Privilegios && PlayerType.Dios)
											&& (Declaraciones.UserList[tempIndex].flags.Privilegios
													&& PlayerType.RoleMaster) == 0))) {
						TCP.EnviarDatosASlot(tempIndex, sdData);
					}
				}
			}
		}
	}

	static void SendToUserPartyArea(int UserIndex, String sdData) {
		/* '************************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modify Date: Unknow */
		/* ' */
		/* '************************************************************** */
		int LoopC = 0;
		int tempIndex = 0;

		int Map = 0;
		int AreaX = 0;
		int AreaY = 0;

		Map = Declaraciones.UserList[UserIndex].Pos.Map;
		AreaX = Declaraciones.UserList[UserIndex].AreasInfo.AreaPerteneceX;
		AreaY = Declaraciones.UserList[UserIndex].AreasInfo.AreaPerteneceY;

		if (!General.MapaValido(Map)) {
			return;
		}

		if (Declaraciones.UserList[UserIndex].PartyIndex == 0) {
			return;
		}

		for (LoopC = (1); LoopC <= (ModAreas.ConnGroups[Map].CountEntrys); LoopC++) {
			tempIndex = ModAreas.ConnGroups[Map].UserEntrys[LoopC];

			/* 'Esta en el area? */
			if (Declaraciones.UserList[tempIndex].AreasInfo.AreaReciveX && AreaX) {
				if (Declaraciones.UserList[tempIndex].AreasInfo.AreaReciveY && AreaY) {
					if (Declaraciones.UserList[tempIndex].ConnIDValida
							&& Declaraciones.UserList[tempIndex].PartyIndex == Declaraciones.UserList[UserIndex].PartyIndex) {
						TCP.EnviarDatosASlot(tempIndex, sdData);
					}
				}
			}
		}
	}

	static void SendToAdminsButConsejerosArea(int UserIndex, String sdData) {
		/* '************************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modify Date: Unknow */
		/* ' */
		/* '************************************************************** */
		int LoopC = 0;
		int tempIndex = 0;

		int Map = 0;
		int AreaX = 0;
		int AreaY = 0;

		Map = Declaraciones.UserList[UserIndex].Pos.Map;
		AreaX = Declaraciones.UserList[UserIndex].AreasInfo.AreaPerteneceX;
		AreaY = Declaraciones.UserList[UserIndex].AreasInfo.AreaPerteneceY;

		if (!General.MapaValido(Map)) {
			return;
		}

		for (LoopC = (1); LoopC <= (ModAreas.ConnGroups[Map].CountEntrys); LoopC++) {
			tempIndex = ModAreas.ConnGroups[Map].UserEntrys[LoopC];

			/* 'Esta en el area? */
			if (Declaraciones.UserList[tempIndex].AreasInfo.AreaReciveX && AreaX) {
				if (Declaraciones.UserList[tempIndex].AreasInfo.AreaReciveY && AreaY) {
					if (Declaraciones.UserList[tempIndex].ConnIDValida) {
						if (Declaraciones.UserList[tempIndex].flags.Privilegios
								&& (PlayerType.SemiDios || PlayerType.Dios || PlayerType.Admin)) {
							TCP.EnviarDatosASlot(tempIndex, sdData);
						}
					}
				}
			}
		}
	}

	static void SendToNpcArea(int NpcIndex, String sdData) {
		/* '************************************************************** */
		/* 'Author: Lucio N. Tourrilhes (DuNga) */
		/* 'Last Modify Date: Unknow */
		/* ' */
		/* '************************************************************** */
		int LoopC = 0;
		int TempInt = 0;
		int tempIndex = 0;

		int Map = 0;
		int AreaX = 0;
		int AreaY = 0;

		Map = Declaraciones.Npclist[NpcIndex].Pos.Map;
		AreaX = Declaraciones.Npclist[NpcIndex].AreasInfo.AreaPerteneceX;
		AreaY = Declaraciones.Npclist[NpcIndex].AreasInfo.AreaPerteneceY;

		if (!General.MapaValido(Map)) {
			return;
		}

		for (LoopC = (1); LoopC <= (ModAreas.ConnGroups[Map].CountEntrys); LoopC++) {
			tempIndex = ModAreas.ConnGroups[Map].UserEntrys[LoopC];

			TempInt = Declaraciones.UserList[tempIndex].AreasInfo.AreaReciveX && AreaX;
			/* 'Esta en el area? */
			if (TempInt) {
				TempInt = Declaraciones.UserList[tempIndex].AreasInfo.AreaReciveY && AreaY;
				if (TempInt) {
					if (Declaraciones.UserList[tempIndex].ConnIDValida) {
						TCP.EnviarDatosASlot(tempIndex, sdData);
					}
				}
			}
		}
	}

	static void SendToAreaByPos(int Map, int AreaX, int AreaY, String sdData) {
 /* '************************************************************** */
 /* 'Author: Lucio N. Tourrilhes (DuNga) */
 /* 'Last Modify Date: Unknow */
 /* ' */
 /* '************************************************************** */
 int LoopC = 0;
 int TempInt = 0;
 int tempIndex = 0;
 
 AreaX = 2 $ (AreaX/9);
 AreaY = 2 $ (AreaY/9);
 
 if (!General.MapaValido(Map)) {
 return;
 }
 
  for (LoopC = (1); LoopC <= (ModAreas.ConnGroups[Map].CountEntrys); LoopC++) {
  tempIndex = ModAreas.ConnGroups[Map].UserEntrys[LoopC];
  
  TempInt = Declaraciones.UserList[tempIndex].AreasInfo.AreaReciveX && AreaX;
  /* 'Esta en el area? */
   if (TempInt) {
   TempInt = Declaraciones.UserList[tempIndex].AreasInfo.AreaReciveY && AreaY;
    if (TempInt) {
     if (Declaraciones.UserList[tempIndex].ConnIDValida) {
     TCP.EnviarDatosASlot(tempIndex, sdData);
    }
   }
  }
 }
}

	static void SendToMap(int Map, String sdData) {
		/* '************************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modify Date: 5/24/2007 */
		/* ' */
		/* '************************************************************** */
		int LoopC = 0;
		int tempIndex = 0;

		if (!General.MapaValido(Map)) {
			return;
		}

		for (LoopC = (1); LoopC <= (ModAreas.ConnGroups[Map].CountEntrys); LoopC++) {
			tempIndex = ModAreas.ConnGroups[Map].UserEntrys[LoopC];

			if (Declaraciones.UserList[tempIndex].ConnIDValida) {
				TCP.EnviarDatosASlot(tempIndex, sdData);
			}
		}
	}

	static void SendToMapButIndex(int UserIndex, String sdData) {
		/* '************************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modify Date: 5/24/2007 */
		/* ' */
		/* '************************************************************** */
		int LoopC = 0;
		int Map = 0;
		int tempIndex = 0;

		Map = Declaraciones.UserList[UserIndex].Pos.Map;

		if (!General.MapaValido(Map)) {
			return;
		}

		for (LoopC = (1); LoopC <= (ModAreas.ConnGroups[Map].CountEntrys); LoopC++) {
			tempIndex = ModAreas.ConnGroups[Map].UserEntrys[LoopC];

			if (tempIndex != UserIndex && Declaraciones.UserList[tempIndex].ConnIDValida) {
				TCP.EnviarDatosASlot(tempIndex, sdData);
			}
		}
	}

	static void SendToGMsAreaButRmsOrCounselors(int UserIndex, String sdData) {
		/* '************************************************************** */
		/* 'Author: Torres Patricio(Pato) */
		/* 'Last Modify Date: 12/02/2010 */
		/* '12/02/2010: ZaMa - Restrinjo solo a dioses, admins y gms. */
		/*
		 * '15/02/2010: ZaMa - Cambio el nombre de la funcion (viejo: ToGmsArea,
		 * nuevo: ToGmsAreaButRMsOrCounselors)
		 */
		/* '************************************************************** */
		int LoopC = 0;
		int tempIndex = 0;

		int Map = 0;
		int AreaX = 0;
		int AreaY = 0;

		Map = Declaraciones.UserList[UserIndex].Pos.Map;
		AreaX = Declaraciones.UserList[UserIndex].AreasInfo.AreaPerteneceX;
		AreaY = Declaraciones.UserList[UserIndex].AreasInfo.AreaPerteneceY;

		if (!General.MapaValido(Map)) {
			return;
		}

		for (LoopC = (1); LoopC <= (ModAreas.ConnGroups[Map].CountEntrys); LoopC++) {
			tempIndex = ModAreas.ConnGroups[Map].UserEntrys[LoopC];

			/* 'Esta en el area? */
			if (Declaraciones.UserList[tempIndex].AreasInfo.AreaReciveX && AreaX) {
				if (Declaraciones.UserList[tempIndex].AreasInfo.AreaReciveY && AreaY) {
					if (Declaraciones.UserList[tempIndex].ConnIDValida) {
						/* ' Exclusivo para dioses, admins y gms */
						if ((Declaraciones.UserList[tempIndex].flags.Privilegios && !PlayerType.User
								&& !PlayerType.Consejero
								&& !PlayerType.RoleMaster) == Declaraciones.UserList[tempIndex].flags.Privilegios) {
							TCP.EnviarDatosASlot(tempIndex, sdData);
						}
					}
				}
			}
		}
	}

	static void SendToUsersAreaButGMs(int UserIndex, String sdData) {
		/* '************************************************************** */
		/* 'Author: Torres Patricio(Pato) */
		/* 'Last Modify Date: 10/17/2009 */
		/* ' */
		/* '************************************************************** */
		int LoopC = 0;
		int tempIndex = 0;

		int Map = 0;
		int AreaX = 0;
		int AreaY = 0;

		Map = Declaraciones.UserList[UserIndex].Pos.Map;
		AreaX = Declaraciones.UserList[UserIndex].AreasInfo.AreaPerteneceX;
		AreaY = Declaraciones.UserList[UserIndex].AreasInfo.AreaPerteneceY;

		if (!General.MapaValido(Map)) {
			return;
		}

		for (LoopC = (1); LoopC <= (ModAreas.ConnGroups[Map].CountEntrys); LoopC++) {
			tempIndex = ModAreas.ConnGroups[Map].UserEntrys[LoopC];

			/* 'Esta en el area? */
			if (Declaraciones.UserList[tempIndex].AreasInfo.AreaReciveX && AreaX) {
				if (Declaraciones.UserList[tempIndex].AreasInfo.AreaReciveY && AreaY) {
					if (Declaraciones.UserList[tempIndex].ConnIDValida) {
						if (Declaraciones.UserList[tempIndex].flags.Privilegios && PlayerType.User) {
							TCP.EnviarDatosASlot(tempIndex, sdData);
						}
					}
				}
			}
		}
	}

	static void SendToUsersAndRmsAndCounselorsAreaButGMs(int UserIndex, String sdData) {
		/* '************************************************************** */
		/* 'Author: Torres Patricio(Pato) */
		/* 'Last Modify Date: 10/17/2009 */
		/* ' */
		/* '************************************************************** */
		int LoopC = 0;
		int tempIndex = 0;

		int Map = 0;
		int AreaX = 0;
		int AreaY = 0;

		Map = Declaraciones.UserList[UserIndex].Pos.Map;
		AreaX = Declaraciones.UserList[UserIndex].AreasInfo.AreaPerteneceX;
		AreaY = Declaraciones.UserList[UserIndex].AreasInfo.AreaPerteneceY;

		if (!General.MapaValido(Map)) {
			return;
		}

		for (LoopC = (1); LoopC <= (ModAreas.ConnGroups[Map].CountEntrys); LoopC++) {
			tempIndex = ModAreas.ConnGroups[Map].UserEntrys[LoopC];

			/* 'Esta en el area? */
			if (Declaraciones.UserList[tempIndex].AreasInfo.AreaReciveX && AreaX) {
				if (Declaraciones.UserList[tempIndex].AreasInfo.AreaReciveY && AreaY) {
					if (Declaraciones.UserList[tempIndex].ConnIDValida) {
						if (Declaraciones.UserList[tempIndex].flags.Privilegios
								&& (PlayerType.User || PlayerType.Consejero || PlayerType.RoleMaster)) {
							TCP.EnviarDatosASlot(tempIndex, sdData);
						}
					}
				}
			}
		}
	}

	static void AlertarFaccionarios(int UserIndex) {
		/* '************************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modify Date: 17/11/2009 */
		/* 'Alerta a los faccionarios, dandoles una orientacion */
		/* '************************************************************** */
		int LoopC = 0;
		int tempIndex = 0;
		int Map = 0;
		FontTypeNames Font;

		if (Extra.esCaos(UserIndex)) {
			Font = FontTypeNames.FONTTYPE_CONSEJOCAOS;
		} else {
			Font = FontTypeNames.FONTTYPE_CONSEJO;
		}

		Map = Declaraciones.UserList[UserIndex].Pos.Map;

		if (!General.MapaValido(Map)) {
			return;
		}

		for (LoopC = (1); LoopC <= (ModAreas.ConnGroups[Map].CountEntrys); LoopC++) {
			tempIndex = ModAreas.ConnGroups[Map].UserEntrys[LoopC];

			if (Declaraciones.UserList[tempIndex].ConnIDValida) {
				if (tempIndex != UserIndex) {
					/* ' Solo se envia a los de la misma faccion */
					if (UsUaRiOs.SameFaccion(UserIndex, tempIndex)) {
						TCP.EnviarDatosASlot(tempIndex,
								Protocol.PrepareMessageConsoleMsg(
										"Escuchas el llamado de un compañero que proviene del "
												+ UsUaRiOs.GetDireccion(UserIndex, tempIndex),
										Font));
					}
				}
			}
		}

	}

}