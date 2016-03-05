
/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"Extra"')] */
/* 'Argentum Online 0.12.2 */
/* 'Copyright (C) 2002 Márquez Pablo Ignacio */
/* ' */
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
/* ' */
/* 'Argentum Online is based on Baronsoft's VB6 Online RPG */
/* 'You can contact the original creator of ORE at aaron@baronsoft.com */
/* 'for more information about ORE please visit http://www.baronsoft.com/ */
/* ' */
/* ' */
/* 'You can contact me at: */
/* 'morgolock@speedy.com.ar */
/* 'www.geocities.com/gmorgolock */
/* 'Calle 3 número 983 piso 7 dto A */
/* 'La Plata - Pcia, Buenos Aires - Republica Argentina */
/* 'Código Postal 1900 */
/* 'Pablo Ignacio Márquez */

import enums.*;

public class Extra {

	static boolean EsNewbie(int UserIndex) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		retval = Declaraciones.UserList[UserIndex].Stats.ELV <= Declaraciones.LimiteNewbie;
		return retval;
	}

	static boolean esArmada(int UserIndex) {
		boolean retval;
		/* '*************************************************** */
		/* 'Autor: Pablo (ToxicWaste) */
		/* 'Last Modification: 23/01/2007 */
		/* '*************************************************** */

		retval = (Declaraciones.UserList[UserIndex].Faccion.ArmadaReal == 1);
		return retval;
	}

	static boolean esCaos(int UserIndex) {
		boolean retval;
		/* '*************************************************** */
		/* 'Autor: Pablo (ToxicWaste) */
		/* 'Last Modification: 23/01/2007 */
		/* '*************************************************** */

		retval = (Declaraciones.UserList[UserIndex].Faccion.FuerzasCaos == 1);
		return retval;
	}

	static boolean EsGm(int UserIndex) {
		boolean retval;
		/* '*************************************************** */
		/* 'Autor: Pablo (ToxicWaste) */
		/* 'Last Modification: 23/01/2007 */
		/* '*************************************************** */

		retval = (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.Admin || PlayerType.Dios || PlayerType.SemiDios || PlayerType.Consejero));
		return retval;
	}

	static void DoTileEvents(int UserIndex, int Map, int X, int Y) {
		/* '*************************************************** */
		/* 'Autor: Pablo (ToxicWaste) & Unknown (orginal version) */
		/* 'Last Modification: 06/03/2010 */
		/* 'Handles the Map passage of Users. Allows the existance */
		/* 'of exclusive maps for Newbies, Royal Army and Caos Legion members */
		/* 'and enables GMs to enter every map without restriction. */
		/*
		 * 'Uses: Mapinfo(map).Restringir = "NEWBIE" (newbies), "ARMADA",
		 * "CAOS", "FACCION" or "NO".
		 */
		/*
		 * ' 06/03/2010 : Now we have 5 attemps to not fall into a map change or
		 * another teleport while going into a teleport. (Marco)
		 */
		/* '*************************************************** */

		Declaraciones.WorldPos nPos;
		boolean FxFlag;
		int TelepRadio;
		Declaraciones.WorldPos DestPos;

		/* FIXME: ON ERROR GOTO ErrHandler */
		/* 'Controla las salidas */
		if (InMapBounds(Map, X, Y)) {
			if (Declaraciones.MapData[Map][X][Y].ObjInfo.ObjIndex > 0) {
				FxFlag = Declaraciones.ObjData[Declaraciones.MapData[Map][X][Y].ObjInfo.ObjIndex].OBJType == eOBJType.otTeleport;
				TelepRadio = Declaraciones.ObjData[Declaraciones.MapData[Map][X][Y].ObjInfo.ObjIndex].Radio;
			}

			if (Declaraciones.MapData[Map][X][Y].TileExit.Map > 0
					&& Declaraciones.MapData[Map][X][Y].TileExit.Map <= Declaraciones.NumMaps) {

				/*
				 * ' Es un teleport, entra en una posicion random, acorde al
				 * radio (si es 0, es pos fija)
				 */
				/*
				 * ' We have 5 attempts to not falling into another teleport or
				 * a map exit.. If we get to the fifth attemp,
				 */
				/* ' the teleport will act as if its radius = 0. */
				if (FxFlag && TelepRadio > 0) {
					int attemps;
					boolean exitMap;
					do {
						DestPos.X = Declaraciones.MapData[Map][X][Y].TileExit.X
								+ Matematicas.RandomNumber(TelepRadio * (-1), TelepRadio);
						DestPos.Y = Declaraciones.MapData[Map][X][Y].TileExit.Y
								+ Matematicas.RandomNumber(TelepRadio * (-1), TelepRadio);

						attemps = attemps + 1;

						exitMap = Declaraciones.MapData[Declaraciones.MapData[Map][X][Y].TileExit.Map][DestPos.X][DestPos.Y].TileExit.Map > 0
								&& Declaraciones.MapData[Declaraciones.MapData[Map][X][Y].TileExit.Map][DestPos.X][DestPos.Y].TileExit.Map <= Declaraciones.NumMaps;
					} while (!((attemps >= 5 || exitMap == false)));

					if (attemps >= 5) {
						DestPos.X = Declaraciones.MapData[Map][X][Y].TileExit.X;
						DestPos.Y = Declaraciones.MapData[Map][X][Y].TileExit.Y;
					}
					/* ' Posicion fija */
				} else {
					DestPos.X = Declaraciones.MapData[Map][X][Y].TileExit.X;
					DestPos.Y = Declaraciones.MapData[Map][X][Y].TileExit.Y;
				}

				DestPos.Map = Declaraciones.MapData[Map][X][Y].TileExit.Map;

				if (EsGm(UserIndex)) {
					General.LogGM(Declaraciones.UserList[UserIndex].Name, "Utilizó un teleport hacia el mapa "
							+ DestPos.Map + " (" + DestPos.X + "," + DestPos.Y + ")");
				}

				/* ' Si es un mapa que no admite muertos */
				if (Declaraciones.MapInfo[DestPos.Map].OnDeathGoTo.Map != 0) {
					/* ' Si esta muerto no puede entrar */
					if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
						Protocol.WriteConsoleMsg(UserIndex, "Sólo se permite entrar al mapa a los personajes vivos.",
								FontTypeNames.FONTTYPE_INFO);
						ClosestStablePos(Declaraciones.UserList[UserIndex].Pos, nPos);

						if (nPos.X != 0 && nPos.Y != 0) {
							UsUaRiOs.WarpUserChar(UserIndex, nPos.Map, nPos.X, nPos.Y, FxFlag);
						}

						return;
					}
				}

				/* '¿Es mapa de newbies? */
				if (Declaraciones.MapInfo[DestPos.Map].Restringir == eRestrict.restrict_newbie) {
					/* '¿El usuario es un newbie? */
					if (EsNewbie(UserIndex) || EsGm(UserIndex)) {
						if (LegalPos(DestPos.Map, DestPos.X, DestPos.Y, UsUaRiOs.PuedeAtravesarAgua(UserIndex))) {
							UsUaRiOs.WarpUserChar(UserIndex, DestPos.Map, DestPos.X, DestPos.Y, FxFlag);
						} else {
							ClosestLegalPos(DestPos, nPos);
							if (nPos.X != 0 && nPos.Y != 0) {
								UsUaRiOs.WarpUserChar(UserIndex, nPos.Map, nPos.X, nPos.Y, FxFlag);
							}
						}
						/* 'No es newbie */
					} else {
						Protocol.WriteConsoleMsg(UserIndex, "Mapa exclusivo para newbies.",
								FontTypeNames.FONTTYPE_INFO);
						ClosestStablePos(Declaraciones.UserList[UserIndex].Pos, nPos);

						if (nPos.X != 0 && nPos.Y != 0) {
							UsUaRiOs.WarpUserChar(UserIndex, nPos.Map, nPos.X, nPos.Y, false);
						}
					}
					/* '¿Es mapa de Armadas? */
				} else if (Declaraciones.MapInfo[DestPos.Map].Restringir == eRestrict.restrict_armada) {
					/* '¿El usuario es Armada? */
					if (esArmada(UserIndex) || EsGm(UserIndex)) {
						if (LegalPos(DestPos.Map, DestPos.X, DestPos.Y, UsUaRiOs.PuedeAtravesarAgua(UserIndex))) {
							UsUaRiOs.WarpUserChar(UserIndex, DestPos.Map, DestPos.X, DestPos.Y, FxFlag);
						} else {
							ClosestLegalPos(DestPos, nPos);
							if (nPos.X != 0 && nPos.Y != 0) {
								UsUaRiOs.WarpUserChar(UserIndex, nPos.Map, nPos.X, nPos.Y, FxFlag);
							}
						}
						/* 'No es armada */
					} else {
						Protocol.WriteConsoleMsg(UserIndex, "Mapa exclusivo para miembros del ejército real.",
								FontTypeNames.FONTTYPE_INFO);
						ClosestStablePos(Declaraciones.UserList[UserIndex].Pos, nPos);

						if (nPos.X != 0 && nPos.Y != 0) {
							UsUaRiOs.WarpUserChar(UserIndex, nPos.Map, nPos.X, nPos.Y, FxFlag);
						}
					}
					/* '¿Es mapa de Caos? */
				} else if (Declaraciones.MapInfo[DestPos.Map].Restringir == eRestrict.restrict_caos) {
					/* '¿El usuario es Caos? */
					if (esCaos(UserIndex) || EsGm(UserIndex)) {
						if (LegalPos(DestPos.Map, DestPos.X, DestPos.Y, UsUaRiOs.PuedeAtravesarAgua(UserIndex))) {
							UsUaRiOs.WarpUserChar(UserIndex, DestPos.Map, DestPos.X, DestPos.Y, FxFlag);
						} else {
							ClosestLegalPos(DestPos, nPos);
							if (nPos.X != 0 && nPos.Y != 0) {
								UsUaRiOs.WarpUserChar(UserIndex, nPos.Map, nPos.X, nPos.Y, FxFlag);
							}
						}
						/* 'No es caos */
					} else {
						Protocol.WriteConsoleMsg(UserIndex, "Mapa exclusivo para miembros de la legión oscura.",
								FontTypeNames.FONTTYPE_INFO);
						ClosestStablePos(Declaraciones.UserList[UserIndex].Pos, nPos);

						if (nPos.X != 0 && nPos.Y != 0) {
							UsUaRiOs.WarpUserChar(UserIndex, nPos.Map, nPos.X, nPos.Y, FxFlag);
						}
					}
					/* '¿Es mapa de faccionarios? */
				} else if (Declaraciones.MapInfo[DestPos.Map].Restringir == eRestrict.restrict_faccion) {
					/* '¿El usuario es Armada o Caos? */
					if (esArmada(UserIndex) || esCaos(UserIndex) || EsGm(UserIndex)) {
						if (LegalPos(DestPos.Map, DestPos.X, DestPos.Y, UsUaRiOs.PuedeAtravesarAgua(UserIndex))) {
							UsUaRiOs.WarpUserChar(UserIndex, DestPos.Map, DestPos.X, DestPos.Y, FxFlag);
						} else {
							ClosestLegalPos(DestPos, nPos);
							if (nPos.X != 0 && nPos.Y != 0) {
								UsUaRiOs.WarpUserChar(UserIndex, nPos.Map, nPos.X, nPos.Y, FxFlag);
							}
						}
						/* 'No es Faccionario */
					} else {
						Protocol.WriteConsoleMsg(UserIndex,
								"Solo se permite entrar al mapa si eres miembro de alguna facción.",
								FontTypeNames.FONTTYPE_INFO);
						ClosestStablePos(Declaraciones.UserList[UserIndex].Pos, nPos);

						if (nPos.X != 0 && nPos.Y != 0) {
							UsUaRiOs.WarpUserChar(UserIndex, nPos.Map, nPos.X, nPos.Y, FxFlag);
						}
					}
					/*
					 * 'No es un mapa de newbies, ni Armadas, ni Caos, ni
					 * faccionario.
					 */
				} else {
					if (LegalPos(DestPos.Map, DestPos.X, DestPos.Y, UsUaRiOs.PuedeAtravesarAgua(UserIndex))) {
						UsUaRiOs.WarpUserChar(UserIndex, DestPos.Map, DestPos.X, DestPos.Y, FxFlag);
					} else {
						ClosestLegalPos(DestPos, nPos);
						if (nPos.X != 0 && nPos.Y != 0) {
							UsUaRiOs.WarpUserChar(UserIndex, nPos.Map, nPos.X, nPos.Y, FxFlag);
						}
					}
				}

				/*
				 * 'Te fusite del mapa. La criatura ya no es más tuya ni te
				 * reconoce como que vos la atacaste.
				 */
				int aN;

				aN = Declaraciones.UserList[UserIndex].flags.AtacadoPorNpc;
				if (aN > 0) {
					Declaraciones.Npclist[aN].Movement = Declaraciones.Npclist[aN].flags.OldMovement;
					Declaraciones.Npclist[aN].Hostile = Declaraciones.Npclist[aN].flags.OldHostil;
					Declaraciones.Npclist[aN].flags.AttackedBy = "";
				}

				aN = Declaraciones.UserList[UserIndex].flags.NPCAtacado;
				if (aN > 0) {
					if (Declaraciones.Npclist[aN].flags.AttackedFirstBy == Declaraciones.UserList[UserIndex].Name) {
						Declaraciones.Npclist[aN].flags.AttackedFirstBy = "";
					}
				}
				Declaraciones.UserList[UserIndex].flags.AtacadoPorNpc = 0;
				Declaraciones.UserList[UserIndex].flags.NPCAtacado = 0;
			}
		}
		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en DotileEvents. Error: " + Err.Number + " - Desc: " + Err.description);
	}

	static boolean InRangoVision(int UserIndex, int X, int Y) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		if (X > Declaraciones.UserList[UserIndex].Pos.X - Declaraciones.MinXBorder
				&& X < Declaraciones.UserList[UserIndex].Pos.X + Declaraciones.MinXBorder) {
			if (Y > Declaraciones.UserList[UserIndex].Pos.Y - Declaraciones.MinYBorder
					&& Y < Declaraciones.UserList[UserIndex].Pos.Y + Declaraciones.MinYBorder) {
				retval = true;
				return retval;
			}
		}
		retval = false;

		return retval;
	}

	static boolean InVisionRangeAndMap(int UserIndex,
			Declaraciones.WorldPos /* FIXME BYREF!! */ OtherUserPos) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 20/11/2010 */
		/* ' */
		/* '*************************************************** */

		/* ' Same map? */
		if (Declaraciones.UserList[UserIndex].Pos.Map != OtherUserPos.Map) {
			return retval;
		}

		/* ' In x range? */
		if (OtherUserPos.X < Declaraciones.UserList[UserIndex].Pos.X - Declaraciones.MinXBorder
				|| OtherUserPos.X > Declaraciones.UserList[UserIndex].Pos.X + Declaraciones.MinXBorder) {
			return retval;
		}

		/* ' In y range? */
		if (OtherUserPos.Y < Declaraciones.UserList[UserIndex].Pos.Y - Declaraciones.MinYBorder
				&& OtherUserPos.Y > Declaraciones.UserList[UserIndex].Pos.Y + Declaraciones.MinYBorder) {
			return retval;
		}

		retval = true;

		return retval;
	}

	static boolean InRangoVisionNPC(int NpcIndex, int X, int Y) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		if (X > Declaraciones.Npclist[NpcIndex].Pos.X - Declaraciones.MinXBorder
				&& X < Declaraciones.Npclist[NpcIndex].Pos.X + Declaraciones.MinXBorder) {
			if (Y > Declaraciones.Npclist[NpcIndex].Pos.Y - Declaraciones.MinYBorder
					&& Y < Declaraciones.Npclist[NpcIndex].Pos.Y + Declaraciones.MinYBorder) {
				retval = true;
				return retval;
			}
		}
		retval = false;

		return retval;
	}

	static boolean InMapBounds(int Map, int X, int Y) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		if ((Map <= 0 || Map > Declaraciones.NumMaps) || X < Declaraciones.MinXBorder || X > Declaraciones.MaxXBorder
				|| Y < Declaraciones.MinYBorder || Y > Declaraciones.MaxYBorder) {
			retval = false;
		} else {
			retval = true;
		}

		return retval;
	}

	static boolean RhombLegalPos(Declaraciones.WorldPos /* FIXME BYREF!! */ Pos,
			int /* FIXME BYREF!! */ vX, int /* FIXME BYREF!! */ vY, int Distance) {
		return RhombLegalPos(Pos, vX, vY, Distance, false, true, false);
	}

	static boolean RhombLegalPos(Declaraciones.WorldPos /* FIXME BYREF!! */ Pos,
			int /* FIXME BYREF!! */ vX, int /* FIXME BYREF!! */ vY, int Distance, boolean PuedeAgua,
			boolean PuedeTierra, boolean CheckExitTile) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Marco Vanotti (Marco) */
		/* 'Last Modification: - */
		/* ' walks all the perimeter of a rhomb of side "distance + 1", */
		/* ' which starts at Pos.x - Distance and Pos.y */
		/* '*************************************************** */

		int i;

		vX = Pos.X - Distance;
		vY = Pos.Y;

		for (i = (0); i <= (Distance - 1); i++) {
			if ((LegalPos(Pos.Map, vX + i, vY - i, PuedeAgua, PuedeTierra, CheckExitTile))) {
				vX = vX + i;
				vY = vY - i;
				retval = true;
				return retval;
			}
		}

		vX = Pos.X;
		vY = Pos.Y - Distance;

		for (i = (0); i <= (Distance - 1); i++) {
			if ((LegalPos(Pos.Map, vX + i, vY + i, PuedeAgua, PuedeTierra, CheckExitTile))) {
				vX = vX + i;
				vY = vY + i;
				retval = true;
				return retval;
			}
		}

		vX = Pos.X + Distance;
		vY = Pos.Y;

		for (i = (0); i <= (Distance - 1); i++) {
			if ((LegalPos(Pos.Map, vX - i, vY + i, PuedeAgua, PuedeTierra, CheckExitTile))) {
				vX = vX - i;
				vY = vY + i;
				retval = true;
				return retval;
			}
		}

		vX = Pos.X;
		vY = Pos.Y + Distance;

		for (i = (0); i <= (Distance - 1); i++) {
			if ((LegalPos(Pos.Map, vX - i, vY - i, PuedeAgua, PuedeTierra, CheckExitTile))) {
				vX = vX - i;
				vY = vY - i;
				retval = true;
				return retval;
			}
		}

		retval = false;

		return retval;
	}

	static boolean RhombLegalTilePos(Declaraciones.WorldPos /* FIXME BYREF!! */ Pos,
			int /* FIXME BYREF!! */ vX, int /* FIXME BYREF!! */ vY, int Distance, int ObjIndex, int ObjAmount,
			boolean PuedeAgua, boolean PuedeTierra) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: - */
		/* ' walks all the perimeter of a rhomb of side "distance + 1", */
		/* ' which starts at Pos.x - Distance and Pos.y */
		/* ' and searchs for a valid position to drop items */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		int i;
		boolean HayObj;

		int X;
		int Y;
		int MapObjIndex;

		vX = Pos.X - Distance;
		vY = Pos.Y;

		for (i = (0); i <= (Distance - 1); i++) {

			X = vX + i;
			Y = vY - i;

			if ((LegalPos(Pos.Map, X, Y, PuedeAgua, PuedeTierra, true))) {

				/*
				 * ' No hay obj tirado o la suma de lo que hay + lo nuevo <= 10k
				 */
				if (!HayObjeto(Pos.Map, X, Y, ObjIndex, ObjAmount)) {
					vX = X;
					vY = Y;

					retval = true;
					return retval;
				}

			}
		}

		vX = Pos.X;
		vY = Pos.Y - Distance;

		for (i = (0); i <= (Distance - 1); i++) {

			X = vX + i;
			Y = vY + i;

			if ((LegalPos(Pos.Map, X, Y, PuedeAgua, PuedeTierra, true))) {

				/*
				 * ' No hay obj tirado o la suma de lo que hay + lo nuevo <= 10k
				 */
				if (!HayObjeto(Pos.Map, X, Y, ObjIndex, ObjAmount)) {
					vX = X;
					vY = Y;

					retval = true;
					return retval;
				}
			}
		}

		vX = Pos.X + Distance;
		vY = Pos.Y;

		for (i = (0); i <= (Distance - 1); i++) {

			X = vX - i;
			Y = vY + i;

			if ((LegalPos(Pos.Map, X, Y, PuedeAgua, PuedeTierra, true))) {

				/*
				 * ' No hay obj tirado o la suma de lo que hay + lo nuevo <= 10k
				 */
				if (!HayObjeto(Pos.Map, X, Y, ObjIndex, ObjAmount)) {
					vX = X;
					vY = Y;

					retval = true;
					return retval;
				}
			}
		}

		vX = Pos.X;
		vY = Pos.Y + Distance;

		for (i = (0); i <= (Distance - 1); i++) {

			X = vX - i;
			Y = vY - i;

			if ((LegalPos(Pos.Map, X, Y, PuedeAgua, PuedeTierra, true))) {
				/*
				 * ' No hay obj tirado o la suma de lo que hay + lo nuevo <= 10k
				 */
				if (!HayObjeto(Pos.Map, X, Y, ObjIndex, ObjAmount)) {
					vX = X;
					vY = Y;

					retval = true;
					return retval;
				}
			}
		}

		retval = false;

		return retval;

		/* FIXME: ErrHandler : */
		General.LogError("Error en RhombLegalTilePos. Error: " + Err.Number + " - " + Err.description);
		return retval;
	}

	static boolean HayObjeto(int mapa, int X, int Y, int ObjIndex, int ObjAmount) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: - */
		/* 'Checks if there's space in a tile to add an itemAmount */
		/* '*************************************************** */
		int MapObjIndex;
		MapObjIndex = Declaraciones.MapData[mapa][X][Y].ObjInfo.ObjIndex;

		/* ' Hay un objeto tirado? */
		if (MapObjIndex != 0) {
			/* ' Es el mismo objeto? */
			if (MapObjIndex == ObjIndex) {
				/* ' La suma es menor a 10k? */
				retval = (Declaraciones.MapData[mapa][X][Y].ObjInfo.Amount
						+ ObjAmount > Declaraciones.MAX_INVENTORY_OBJS);
			} else {
				retval = true;
			}
		} else {
			retval = false;
		}

		return retval;
	}

	static void ClosestLegalPos(Declaraciones.WorldPos Pos,
			Declaraciones.WorldPos /* FIXME BYREF!! */ nPos) {
		ClosestLegalPos(Pos, nPos, false, true, false);
	}

	static void ClosestLegalPos(Declaraciones.WorldPos Pos,
			Declaraciones.WorldPos /* FIXME BYREF!! */ nPos, boolean PuedeAgua, boolean PuedeTierra,
			boolean CheckExitTile) {
		/* '***************************************************************** */
		/* 'Author: Unknown (original version) */
		/* 'Last Modification: 09/14/2010 (Marco) */
		/* 'History: */
		/* ' - 01/24/2007 (ToxicWaste) */
		/* 'Encuentra la posicion legal mas cercana y la guarda en nPos */
		/* '***************************************************************** */

		boolean Found;
		int LoopC;
		int tX;
		int tY;

		nPos = Pos;
		tX = Pos.X;
		tY = Pos.Y;

		LoopC = 1;

		/* ' La primera posicion es valida? */
		if (LegalPos(Pos.Map, nPos.X, nPos.Y, PuedeAgua, PuedeTierra, CheckExitTile)) {
			Found = true;

			/* ' Busca en las demas posiciones, en forma de "rombo" */
		} else {
			while ((!Found) && LoopC <= 12) {
				if (RhombLegalPos(Pos, tX, tY, LoopC, PuedeAgua, PuedeTierra, CheckExitTile)) {
					nPos.X = tX;
					nPos.Y = tY;
					Found = true;
				}

				LoopC = LoopC + 1;
			}

		}

		if (!Found) {
			nPos.X = 0;
			nPos.Y = 0;
		}

	}

	static void ClosestStablePos(Declaraciones.WorldPos Pos, Declaraciones.WorldPos /* FIXME BYREF!! */ nPos) {
 /* '*************************************************** */
 /* 'Author: Unknown */
 /* 'Last Modification: 09/14/2010 */
 /* 'Encuentra la posicion legal mas cercana que no sea un portal y la guarda en nPos */
 /* '***************************************************************** */
 
 ClosestLegalPos(Pos, nPos, , , true);
 
}

	static int NameIndex(String Name) {
		int retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int UserIndex;

		/* '¿Nombre valido? */
		if (vb6.LenB(Name) == 0) {
			retval = 0;
			return retval;
		}

		if (vb6.InStrB(Name, "+") != 0) {
			Name = vb6.UCase(vb6.Replace(Name, "+", " "));
		}

		UserIndex = 1;
		while (!(vb6.UCase(Declaraciones.UserList[UserIndex].Name) == vb6.UCase(Name))) {

			UserIndex = UserIndex + 1;

			if (UserIndex > Declaraciones.MaxUsers) {
				retval = 0;
				return retval;
			}
		}

		retval = UserIndex;
		return retval;
	}

	static boolean CheckForSameIP(int UserIndex, String UserIP) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int LoopC;

		for (LoopC = (1); LoopC <= (Declaraciones.MaxUsers); LoopC++) {
			if (Declaraciones.UserList[LoopC].flags.UserLogged == true) {
				if (Declaraciones.UserList[LoopC].ip == UserIP && UserIndex != LoopC) {
					retval = true;
					return retval;
				}
			}
		}

		retval = false;
		return retval;
	}

	static boolean CheckForSameName(String Name) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* 'Controlo que no existan usuarios con el mismo nombre */
		int LoopC;

		for (LoopC = (1); LoopC <= (Declaraciones.LastUser); LoopC++) {
			if (Declaraciones.UserList[LoopC].flags.UserLogged) {

				/*
				 * 'If UCase$(UserList(LoopC).Name) = UCase$(Name) And
				 * UserList(LoopC).ConnID <> -1 Then
				 */
				/*
				 * 'OJO PREGUNTAR POR EL CONNID <> -1 PRODUCE QUE UN PJ EN
				 * DETERMINADO
				 */
				/*
				 * 'MOMENTO PUEDA ESTAR LOGUEADO 2 VECES (IE: CIERRA EL SOCKET
				 * DESDE ALLA)
				 */
				/*
				 * 'ESE EVENTO NO DISPARA UN SAVE USER, LO QUE PUEDE SER
				 * UTILIZADO PARA DUPLICAR ITEMS
				 */
				/*
				 * 'ESTE BUG EN ALKON PRODUJO QUE EL SERVIDOR ESTE CAIDO DURANTE
				 * 3 DIAS. ATENTOS.
				 */

				if (vb6.UCase(Declaraciones.UserList[LoopC].Name) == vb6.UCase(Name)) {
					retval = true;
					return retval;
				}
			}
		}

		retval = false;
		return retval;
	}

	static void HeadtoPos(eHeading Head,
			Declaraciones.WorldPos /* FIXME BYREF!! */ Pos) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* 'Toma una posicion y se mueve hacia donde esta perfilado */
		/* '***************************************************************** */

		switch (Head) {
		case NORTH:
			Pos.Y = Pos.Y - 1;

			break;

		case SOUTH:
			Pos.Y = Pos.Y + 1;

			break;

		case EAST:
			Pos.X = Pos.X + 1;

			break;

		case WEST:
			Pos.X = Pos.X - 1;
			break;
		}
	}

	static boolean LegalPos(int Map, int X, int Y) {
		return LegalPos(Map, X, Y, false, true, false);
	}

	static boolean LegalPos(int Map, int X, int Y, boolean PuedeAgua, boolean PuedeTierra, boolean CheckExitTile) {
		boolean retval;
		/* '*************************************************** */
		/* 'Autor: Pablo (ToxicWaste) & Unknown (orginal version) */
		/* 'Last Modification: 23/01/2007 */
		/* 'Checks if the position is Legal. */
		/* '*************************************************** */

		/* '¿Es un mapa valido? */
		if ((Map <= 0 || Map > Declaraciones.NumMaps) || (X < Declaraciones.MinXBorder || X > Declaraciones.MaxXBorder
				|| Y < Declaraciones.MinYBorder || Y > Declaraciones.MaxYBorder)) {
			retval = false;
		} else {
			if (PuedeAgua && PuedeTierra) {
				retval = (Declaraciones.MapData[Map][X][Y].Blocked != 1)
						&& (Declaraciones.MapData[Map][X][Y].UserIndex == 0)
						&& (Declaraciones.MapData[Map][X][Y].NpcIndex == 0);
			} else if (PuedeTierra && !PuedeAgua) {
				retval = (Declaraciones.MapData[Map][X][Y].Blocked != 1)
						&& (Declaraciones.MapData[Map][X][Y].UserIndex == 0)
						&& (Declaraciones.MapData[Map][X][Y].NpcIndex == 0) && (!General.HayAgua(Map, X, Y));
			} else if (PuedeAgua && !PuedeTierra) {
				retval = (Declaraciones.MapData[Map][X][Y].Blocked != 1)
						&& (Declaraciones.MapData[Map][X][Y].UserIndex == 0)
						&& (Declaraciones.MapData[Map][X][Y].NpcIndex == 0) && (General.HayAgua(Map, X, Y));
			} else {
				retval = false;
			}

			if (CheckExitTile) {
				retval = retval && (Declaraciones.MapData[Map][X][Y].TileExit.Map == 0);
			}

		}

		return retval;
	}

	static boolean MoveToLegalPos(int Map, int X, int Y) {
		return MoveToLegalPos(Map, X, Y, false, true);
	}

	static boolean MoveToLegalPos(int Map, int X, int Y, boolean PuedeAgua, boolean PuedeTierra) {
		boolean retval;
		/* '*************************************************** */
		/* 'Autor: ZaMa */
		/* 'Last Modification: 13/07/2009 */
		/*
		 * 'Checks if the position is Legal, but considers that if there's a
		 * casper, it's a legal movement.
		 */
		/*
		 * '13/07/2009: ZaMa - Now it's also legal move where an invisible admin
		 * is.
		 */
		/* '*************************************************** */

		int UserIndex;
		boolean IsDeadChar;
		boolean IsAdminInvisible;

		/* '¿Es un mapa valido? */
		if ((Map <= 0 || Map > Declaraciones.NumMaps) || (X < Declaraciones.MinXBorder || X > Declaraciones.MaxXBorder
				|| Y < Declaraciones.MinYBorder || Y > Declaraciones.MaxYBorder)) {
			retval = false;
		} else {
			UserIndex = Declaraciones.MapData[Map][X][Y].UserIndex;

			if (UserIndex > 0) {
				IsDeadChar = (Declaraciones.UserList[UserIndex].flags.Muerto == 1);
				IsAdminInvisible = (Declaraciones.UserList[UserIndex].flags.AdminInvisible == 1);
			} else {
				IsDeadChar = false;
				IsAdminInvisible = false;
			}

			if (PuedeAgua && PuedeTierra) {
				retval = (Declaraciones.MapData[Map][X][Y].Blocked != 1)
						&& (UserIndex == 0 || IsDeadChar || IsAdminInvisible)
						&& (Declaraciones.MapData[Map][X][Y].NpcIndex == 0);
			} else if (PuedeTierra && !PuedeAgua) {
				retval = (Declaraciones.MapData[Map][X][Y].Blocked != 1)
						&& (UserIndex == 0 || IsDeadChar || IsAdminInvisible)
						&& (Declaraciones.MapData[Map][X][Y].NpcIndex == 0) && (!General.HayAgua(Map, X, Y));
			} else if (PuedeAgua && !PuedeTierra) {
				retval = (Declaraciones.MapData[Map][X][Y].Blocked != 1)
						&& (UserIndex == 0 || IsDeadChar || IsAdminInvisible)
						&& (Declaraciones.MapData[Map][X][Y].NpcIndex == 0) && (General.HayAgua(Map, X, Y));
			} else {
				retval = false;
			}
		}

		return retval;
	}

	static void FindLegalPos(int UserIndex, int Map, int /* FIXME BYREF!! */ X,
			int /* FIXME BYREF!! */ Y) {
		/* '*************************************************** */
		/* 'Autor: ZaMa */
		/* 'Last Modification: 26/03/2009 */
		/* 'Search for a Legal pos for the user who is being teleported. */
		/* '*************************************************** */

		if (Declaraciones.MapData[Map][X][Y].UserIndex != 0 || Declaraciones.MapData[Map][X][Y].NpcIndex != 0) {

			/* ' Se teletransporta a la misma pos a la que estaba */
			if (Declaraciones.MapData[Map][X][Y].UserIndex == UserIndex) {
				return;
			}

			boolean FoundPlace;
			int tX;
			int tY;
			int Rango;
			int OtherUserIndex;

			for (Rango = (1); Rango <= (5); Rango++) {
				for (tY = (Y - Rango); tY <= (Y + Rango); tY++) {
					for (tX = (X - Rango); tX <= (X + Rango); tX++) {
						/* 'Reviso que no haya User ni NPC */
						if (Declaraciones.MapData[Map][tX][tY].UserIndex == 0
								&& Declaraciones.MapData[Map][tX][tY].NpcIndex == 0) {

							if (InMapBounds(Map, tX, tY)) {
								FoundPlace = true;
							}

							break; /* FIXME: EXIT FOR */
						}

					}

					if (FoundPlace) {
						break; /* FIXME: EXIT FOR */
					}
				}

				if (FoundPlace) {
					break; /* FIXME: EXIT FOR */
				}
			}

			/* 'Si encontramos un lugar, listo, nos quedamos ahi */
			if (FoundPlace) {
				X = tX;
				Y = tY;
			} else {
				/* 'Muy poco probable, pero.. */
				/*
				 * 'Si no encontramos un lugar, sacamos al usuario que tenemos
				 * abajo, y si es un NPC, lo pisamos.
				 */
				OtherUserIndex = Declaraciones.MapData[Map][X][Y].UserIndex;
				if (OtherUserIndex != 0) {
					/*
					 * 'Si no encontramos lugar, y abajo teniamos a un usuario,
					 * lo pisamos y cerramos su comercio seguro
					 */
					if (Declaraciones.UserList[OtherUserIndex].ComUsu.DestUsu > 0) {
						/*
						 * 'Le avisamos al que estaba comerciando que se tuvo
						 * que ir.
						 */
						if (Declaraciones.UserList[Declaraciones.UserList[OtherUserIndex].ComUsu.DestUsu].flags.UserLogged) {
							mdlCOmercioConUsuario
									.FinComerciarUsu(Declaraciones.UserList[OtherUserIndex].ComUsu.DestUsu);
							Protocol.WriteConsoleMsg(Declaraciones.UserList[OtherUserIndex].ComUsu.DestUsu,
									"Comercio cancelado. El otro usuario se ha desconectado.",
									FontTypeNames.FONTTYPE_TALK);
							Protocol.FlushBuffer(Declaraciones.UserList[OtherUserIndex].ComUsu.DestUsu);
						}
						/* 'Lo sacamos. */
						if (Declaraciones.UserList[OtherUserIndex].flags.UserLogged) {
							mdlCOmercioConUsuario.FinComerciarUsu(OtherUserIndex);
							Protocol.WriteErrorMsg(OtherUserIndex,
									"Alguien se ha conectado donde te encontrabas, por favor reconéctate...");
							Protocol.FlushBuffer(OtherUserIndex);
						}
					}

					TCP.CloseSocket(OtherUserIndex);
				}
			}
		}

	}

	static boolean LegalPosNPC(int Map, int X, int Y, int AguaValida) {
		return LegalPosNPC(Map, X, Y, AguaValida, false);
	}

	static boolean LegalPosNPC(int Map, int X, int Y, int AguaValida, boolean IsPet) {
		boolean retval;
		/* '*************************************************** */
		/* 'Autor: Unkwnown */
		/* 'Last Modification: 09/23/2009 */
		/* 'Checks if it's a Legal pos for the npc to move to. */
		/*
		 * '09/23/2009: Pato - If UserIndex is a AdminInvisible, then is a legal
		 * pos.
		 */
		/* '*************************************************** */
		boolean IsDeadChar;
		int UserIndex;
		boolean IsAdminInvisible;

		if ((Map <= 0 || Map > Declaraciones.NumMaps) || (X < Declaraciones.MinXBorder || X > Declaraciones.MaxXBorder
				|| Y < Declaraciones.MinYBorder || Y > Declaraciones.MaxYBorder)) {
			retval = false;
			return retval;
		}

		UserIndex = Declaraciones.MapData[Map][X][Y].UserIndex;
		if (UserIndex > 0) {
			IsDeadChar = Declaraciones.UserList[UserIndex].flags.Muerto == 1;
			IsAdminInvisible = (Declaraciones.UserList[UserIndex].flags.AdminInvisible == 1);
		} else {
			IsDeadChar = false;
			IsAdminInvisible = false;
		}

		if (AguaValida == 0) {
			retval = (Declaraciones.MapData[Map][X][Y].Blocked != 1)
					&& (Declaraciones.MapData[Map][X][Y].UserIndex == 0 || IsDeadChar || IsAdminInvisible)
					&& (Declaraciones.MapData[Map][X][Y].NpcIndex == 0)
					&& (Declaraciones.MapData[Map][X][Y].trigger != eTrigger.POSINVALIDA || IsPet)
					&& !General.HayAgua(Map, X, Y);
		} else {
			retval = (Declaraciones.MapData[Map][X][Y].Blocked != 1)
					&& (Declaraciones.MapData[Map][X][Y].UserIndex == 0 || IsDeadChar || IsAdminInvisible)
					&& (Declaraciones.MapData[Map][X][Y].NpcIndex == 0)
					&& (Declaraciones.MapData[Map][X][Y].trigger != eTrigger.POSINVALIDA || IsPet);
		}
		return retval;
	}

	static void SendHelp(int index) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int NumHelpLines;
		int LoopC;

		NumHelpLines = vb6.val(ES.GetVar(Declaraciones.DatPath + "Help.dat", "INIT", "NumLines"));

		for (LoopC = (1); LoopC <= (NumHelpLines); LoopC++) {
			Protocol.WriteConsoleMsg(index, ES.GetVar(Declaraciones.DatPath + "Help.dat", "Help", "Line" + LoopC),
					FontTypeNames.FONTTYPE_INFO);
		}

	}

	static void Expresar(int NpcIndex, int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		if (Declaraciones.Npclist[NpcIndex].NroExpresiones > 0) {
			Object randomi;
			randomi = Matematicas.RandomNumber(1, Declaraciones.Npclist[NpcIndex].NroExpresiones);
			modSendData.SendData(SendTarget.ToPCArea, UserIndex,
					Protocol.PrepareMessageChatOverHead(Declaraciones.Npclist[NpcIndex].Expresiones[randomi],
							Declaraciones.Npclist[NpcIndex].Char.CharIndex, 0x00ffffff));
		}
	}

	static void LookatTile(int UserIndex, int Map, int X, int Y) {
		/* '*************************************************** */
		/* 'Autor: Unknown (orginal version) */
		/* 'Last Modification: 26/03/2009 */
		/*
		 * '13/02/2009: ZaMa - El nombre del gm que aparece por consola al
		 * clickearlo, tiene el color correspondiente a su rango
		 */
		/*
		 * '07/10/2010: ZaMa - Adaptado para que funcione mas de un centinela en
		 * paralelo.
		 */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		/* 'Responde al click del usuario sobre el mapa */
		int FoundChar;
		int FoundSomething;
		int TempCharIndex;
		String Stat;
		FontTypeNames ft;

		/* '¿Rango Visión? (ToxicWaste) */
		if ((vb6.Abs(Declaraciones.UserList[UserIndex].Pos.Y - Y) > AI.RANGO_VISION_Y)
				|| (vb6.Abs(Declaraciones.UserList[UserIndex].Pos.X - X) > AI.RANGO_VISION_X)) {
			return;
		}

		/* '¿Posicion valida? */
		if (InMapBounds(Map, X, Y)) {
			Declaraciones.UserList[UserIndex].flags.TargetMap = Map;
			Declaraciones.UserList[UserIndex].flags.TargetX = X;
			Declaraciones.UserList[UserIndex].flags.TargetY = Y;
			/* '¿Es un obj? */
			if (Declaraciones.MapData[Map][X][Y].ObjInfo.ObjIndex > 0) {
				/* 'Informa el nombre */
				Declaraciones.UserList[UserIndex].flags.TargetObjMap = Map;
				Declaraciones.UserList[UserIndex].flags.TargetObjX = X;
				Declaraciones.UserList[UserIndex].flags.TargetObjY = Y;
				FoundSomething = 1;
			} else if (Declaraciones.MapData[Map][X + 1][Y].ObjInfo.ObjIndex > 0) {
				/* 'Informa el nombre */
				if (Declaraciones.ObjData[Declaraciones.MapData[Map][X
						+ 1][Y].ObjInfo.ObjIndex].OBJType == eOBJType.otPuertas) {
					Declaraciones.UserList[UserIndex].flags.TargetObjMap = Map;
					Declaraciones.UserList[UserIndex].flags.TargetObjX = X + 1;
					Declaraciones.UserList[UserIndex].flags.TargetObjY = Y;
					FoundSomething = 1;
				}
			} else if (Declaraciones.MapData[Map][X + 1][Y + 1].ObjInfo.ObjIndex > 0) {
				if (Declaraciones.ObjData[Declaraciones.MapData[Map][X + 1][Y
						+ 1].ObjInfo.ObjIndex].OBJType == eOBJType.otPuertas) {
					/* 'Informa el nombre */
					Declaraciones.UserList[UserIndex].flags.TargetObjMap = Map;
					Declaraciones.UserList[UserIndex].flags.TargetObjX = X + 1;
					Declaraciones.UserList[UserIndex].flags.TargetObjY = Y + 1;
					FoundSomething = 1;
				}
			} else if (Declaraciones.MapData[Map][X][Y + 1].ObjInfo.ObjIndex > 0) {
				if (Declaraciones.ObjData[Declaraciones.MapData[Map][X][Y
						+ 1].ObjInfo.ObjIndex].OBJType == eOBJType.otPuertas) {
					/* 'Informa el nombre */
					Declaraciones.UserList[UserIndex].flags.TargetObjMap = Map;
					Declaraciones.UserList[UserIndex].flags.TargetObjX = X;
					Declaraciones.UserList[UserIndex].flags.TargetObjY = Y + 1;
					FoundSomething = 1;
				}
			}

			if (FoundSomething == 1) {
				Declaraciones.UserList[UserIndex].flags.TargetObj = Declaraciones.MapData[Map][Declaraciones.UserList[UserIndex].flags.TargetObjX][Declaraciones.UserList[UserIndex].flags.TargetObjY].ObjInfo.ObjIndex;
				if (MostrarCantidad(Declaraciones.UserList[UserIndex].flags.TargetObj)) {
					Protocol.WriteConsoleMsg(UserIndex,
							Declaraciones.ObjData[Declaraciones.UserList[UserIndex].flags.TargetObj].Name + " - "
									+ Declaraciones.MapData[Declaraciones.UserList[UserIndex].flags.TargetObjMap][Declaraciones.UserList[UserIndex].flags.TargetObjX][Declaraciones.UserList[UserIndex].flags.TargetObjY].ObjInfo.Amount
									+ "",
							FontTypeNames.FONTTYPE_INFO);
				} else {
					Protocol.WriteConsoleMsg(UserIndex,
							Declaraciones.ObjData[Declaraciones.UserList[UserIndex].flags.TargetObj].Name,
							FontTypeNames.FONTTYPE_INFO);
				}

			}
			/* '¿Es un personaje? */
			if (Y + 1 <= Declaraciones.YMaxMapSize) {
				if (Declaraciones.MapData[Map][X][Y + 1].UserIndex > 0) {
					TempCharIndex = Declaraciones.MapData[Map][X][Y + 1].UserIndex;
					FoundChar = 1;
				}
				if (Declaraciones.MapData[Map][X][Y + 1].NpcIndex > 0) {
					TempCharIndex = Declaraciones.MapData[Map][X][Y + 1].NpcIndex;
					FoundChar = 2;
				}
			}
			/* '¿Es un personaje? */
			if (FoundChar == 0) {
				if (Declaraciones.MapData[Map][X][Y].UserIndex > 0) {
					TempCharIndex = Declaraciones.MapData[Map][X][Y].UserIndex;
					FoundChar = 1;
				}
				if (Declaraciones.MapData[Map][X][Y].NpcIndex > 0) {
					TempCharIndex = Declaraciones.MapData[Map][X][Y].NpcIndex;
					FoundChar = 2;
				}
			}

			/* 'Reaccion al personaje */
			/* ' ¿Encontro un Usuario? */
			if (FoundChar == 1) {
				if (Declaraciones.UserList[TempCharIndex].flags.AdminInvisible == 0
						|| Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.Dios) {
					/* 'No tiene descRM y quiere que se vea su nombre. */
					if (vb6.LenB(Declaraciones.UserList[TempCharIndex].DescRM) == 0
							&& Declaraciones.UserList[TempCharIndex].showName) {
						if (EsNewbie(TempCharIndex)) {
							Stat = " <NEWBIE>";
						}

						if (Declaraciones.UserList[TempCharIndex].Faccion.ArmadaReal == 1) {
							Stat = Stat + " <Ejército Real> " + "<" + ModFacciones.TituloReal(TempCharIndex) + ">";
						} else if (Declaraciones.UserList[TempCharIndex].Faccion.FuerzasCaos == 1) {
							Stat = Stat + " <Legión Oscura> " + "<" + ModFacciones.TituloCaos(TempCharIndex) + ">";
						}

						if (Declaraciones.UserList[TempCharIndex].GuildIndex > 0) {
							Stat = Stat + " <" + modGuilds.GuildName(Declaraciones.UserList[TempCharIndex].GuildIndex)
									+ ">";
						}

						if (vb6.Len(Declaraciones.UserList[TempCharIndex].desc) > 0) {
							Stat = "Ves a " + Declaraciones.UserList[TempCharIndex].Name + Stat + " - "
									+ Declaraciones.UserList[TempCharIndex].desc;
						} else {
							Stat = "Ves a " + Declaraciones.UserList[TempCharIndex].Name + Stat;
						}

						if (Declaraciones.UserList[TempCharIndex].flags.Privilegios && PlayerType.RoyalCouncil) {
							Stat = Stat + " [CONSEJO DE BANDERBILL]";
							ft = FontTypeNames.FONTTYPE_CONSEJOVesA;
						} else if (Declaraciones.UserList[TempCharIndex].flags.Privilegios && PlayerType.ChaosCouncil) {
							Stat = Stat + " [CONCILIO DE LAS SOMBRAS]";
							ft = FontTypeNames.FONTTYPE_CONSEJOCAOSVesA;
						} else {
							if (!Declaraciones.UserList[TempCharIndex].flags.Privilegios && PlayerType.User) {
								Stat = Stat + " <GAME MASTER>";

								/* ' Elijo el color segun el rango del GM: */
								/* ' Dios */
								if (Declaraciones.UserList[TempCharIndex].flags.Privilegios == PlayerType.Dios) {
									ft = FontTypeNames.FONTTYPE_DIOS;
									/* ' Gm */
								} else if (Declaraciones.UserList[TempCharIndex].flags.Privilegios == PlayerType.SemiDios) {
									ft = FontTypeNames.FONTTYPE_GM;
									/* ' Conse */
								} else if (Declaraciones.UserList[TempCharIndex].flags.Privilegios == PlayerType.Consejero) {
									ft = FontTypeNames.FONTTYPE_CONSE;
									/* ' Rm o Dsrm */
								} else if (Declaraciones.UserList[TempCharIndex].flags.Privilegios == (PlayerType.RoleMaster
										|| PlayerType.Consejero)
										|| Declaraciones.UserList[TempCharIndex].flags.Privilegios == (PlayerType.RoleMaster
												|| PlayerType.Dios)) {
									ft = FontTypeNames.FONTTYPE_EJECUCION;
								}

							} else if (ES.criminal(TempCharIndex)) {
								Stat = Stat + " <CRIMINAL>";
								ft = FontTypeNames.FONTTYPE_FIGHT;
							} else {
								Stat = Stat + " <CIUDADANO>";
								ft = FontTypeNames.FONTTYPE_CITIZEN;
							}
						}
						/* 'Si tiene descRM la muestro siempre. */
					} else {
						Stat = Declaraciones.UserList[TempCharIndex].DescRM;
						ft = FontTypeNames.FONTTYPE_INFOBOLD;
					}

					if (vb6.LenB(Stat) > 0) {
						Protocol.WriteConsoleMsg(UserIndex, Stat, ft);
					}

					FoundSomething = 1;
					Declaraciones.UserList[UserIndex].flags.TargetUser = TempCharIndex;
					Declaraciones.UserList[UserIndex].flags.TargetNPC = 0;
					Declaraciones.UserList[UserIndex].flags.TargetNpcTipo = eNPCType.Comun;
				}
			}

			/* '¿Encontro un NPC? */
			if (FoundChar == 2) {
				String estatus;
				int MinHp;
				int MaxHp;
				int SupervivenciaSkill;
				String sDesc;

				MinHp = Declaraciones.Npclist[TempCharIndex].Stats.MinHp;
				MaxHp = Declaraciones.Npclist[TempCharIndex].Stats.MaxHp;
				SupervivenciaSkill = Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Supervivencia];

				if (Declaraciones.UserList[UserIndex].flags.Privilegios
						&& (PlayerType.SemiDios || PlayerType.Dios || PlayerType.Admin)) {
					estatus = "(" + MinHp + "/" + MaxHp + ") ";
				} else {
					if (Declaraciones.UserList[UserIndex].flags.Muerto == 0) {

						if (SupervivenciaSkill <= 10) {
							estatus = "(Dudoso) ";

						} else if (SupervivenciaSkill <= 20) {
							if (MinHp < (MaxHp / 2)) {
								estatus = "(Herido) ";
							} else {
								estatus = "(Sano) ";
							}

						} else if (SupervivenciaSkill <= 30) {
							if (MinHp < (MaxHp * 0.5)) {
								estatus = "(Malherido) ";
							} else if (MinHp < (MaxHp * 0.75)) {
								estatus = "(Herido) ";
							} else {
								estatus = "(Sano) ";
							}

						} else if (SupervivenciaSkill <= 40) {
							if (MinHp < (MaxHp * 0.25)) {
								estatus = "(Muy malherido) ";
							} else if (MinHp < (MaxHp * 0.5)) {
								estatus = "(Herido) ";
							} else if (MinHp < (MaxHp * 0.75)) {
								estatus = "(Levemente herido) ";
							} else {
								estatus = "(Sano) ";
							}

						} else if (SupervivenciaSkill < 60) {
							if (MinHp < (MaxHp * 0.05)) {
								estatus = "(Agonizando) ";
							} else if (MinHp < (MaxHp * 0.1)) {
								estatus = "(Casi muerto) ";
							} else if (MinHp < (MaxHp * 0.25)) {
								estatus = "(Muy Malherido) ";
							} else if (MinHp < (MaxHp * 0.5)) {
								estatus = "(Herido) ";
							} else if (MinHp < (MaxHp * 0.75)) {
								estatus = "(Levemente herido) ";
							} else if (MinHp < (MaxHp)) {
								estatus = "(Sano) ";
							} else {
								estatus = "(Intacto) ";
							}
						} else {
							estatus = "(" + MinHp + "/" + MaxHp + ") ";
						}
					}
				}

				if (vb6.Len(Declaraciones.Npclist[TempCharIndex].desc) > 1) {
					Stat = Declaraciones.Npclist[TempCharIndex].desc;

					/* '¿Es el rey o el demonio? */
					if (Declaraciones.Npclist[TempCharIndex].NPCtype == eNPCType.Noble) {
						/* 'Es el Rey. */
						if (Declaraciones.Npclist[TempCharIndex].flags.Faccion == 0) {
							/*
							 * 'Si es de la Legión Oscura y usuario común
							 * mostramos el mensaje correspondiente y lo
							 * ejecutamos:
							 */
							if (Declaraciones.UserList[UserIndex].Faccion.FuerzasCaos == 1) {
								Stat = Declaraciones.MENSAJE_REY_CAOS;
								if (Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) {
									if (Declaraciones.UserList[UserIndex].flags.Muerto == 0) {
										UsUaRiOs.UserDie(UserIndex);
									}
								}
							} else if (ES.criminal(UserIndex)) {
								/*
								 * 'Nos fijamos si es criminal enlistable o no
								 * enlistable:
								 */
								/* 'Es criminal no enlistable. */
								if (Declaraciones.UserList[UserIndex].Faccion.CiudadanosMatados > 0
										|| Declaraciones.UserList[UserIndex].Faccion.Reenlistadas > 4) {
									Stat = Declaraciones.MENSAJE_REY_CRIMINAL_NOENLISTABLE;
									/* 'Es criminal enlistable. */
								} else {
									Stat = Declaraciones.MENSAJE_REY_CRIMINAL_ENLISTABLE;
								}
							}
							/* 'Es el demonio */
						} else {
							/*
							 * 'Si es de la Armada Real y usuario común
							 * mostramos el mensaje correspondiente y lo
							 * ejecutamos:
							 */
							if (Declaraciones.UserList[UserIndex].Faccion.ArmadaReal == 1) {
								Stat = Declaraciones.MENSAJE_DEMONIO_REAL;
								/* ' */
								if (Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) {
									if (Declaraciones.UserList[UserIndex].flags.Muerto == 0) {
										UsUaRiOs.UserDie(UserIndex);
									}
								}
							} else if (!ES.criminal(UserIndex)) {
								/*
								 * 'Nos fijamos si es ciudadano enlistable o no
								 * enlistable:
								 */
								/* 'Es ciudadano no enlistable. */
								if (Declaraciones.UserList[UserIndex].Faccion.RecibioExpInicialReal == 1
										|| Declaraciones.UserList[UserIndex].Faccion.Reenlistadas > 4) {
									Stat = Declaraciones.MENSAJE_DEMONIO_CIUDADANO_NOENLISTABLE;
									/* 'Es ciudadano enlistable. */
								} else {
									Stat = Declaraciones.MENSAJE_DEMONIO_CIUDADANO_ENLISTABLE;
								}
							}
						}
					}

					/* 'Enviamos el mensaje propiamente dicho: */
					Protocol.WriteChatOverHead(UserIndex, Stat, Declaraciones.Npclist[TempCharIndex].Char.CharIndex,
							0x00ffffff);
				} else {

					int CentinelaIndex;
					CentinelaIndex = modCentinela.EsCentinela(TempCharIndex);

					if (CentinelaIndex != 0) {
						/*
						 * 'Enviamos nuevamente el texto del centinela según
						 * quien pregunta
						 */
						modCentinela.CentinelaSendClave(UserIndex, CentinelaIndex);
					} else {
						if (Declaraciones.Npclist[TempCharIndex].MaestroUser > 0) {
							Protocol.WriteConsoleMsg(UserIndex,
									estatus + Declaraciones.Npclist[TempCharIndex].Name + " es mascota de "
											+ Declaraciones.UserList[Declaraciones.Npclist[TempCharIndex].MaestroUser].Name
											+ ".",
									FontTypeNames.FONTTYPE_INFO);
						} else {
							sDesc = estatus + Declaraciones.Npclist[TempCharIndex].Name;
							if (Declaraciones.Npclist[TempCharIndex].Owner > 0) {
								sDesc = sDesc + " le pertenece a "
										+ Declaraciones.UserList[Declaraciones.Npclist[TempCharIndex].Owner].Name;
							}
							sDesc = sDesc + ".";

							Protocol.WriteConsoleMsg(UserIndex, sDesc, FontTypeNames.FONTTYPE_INFO);

							if (Declaraciones.UserList[UserIndex].flags.Privilegios
									&& (PlayerType.Dios || PlayerType.Admin)) {
								Protocol.WriteConsoleMsg(
										UserIndex, "Le pegó primero: "
												+ Declaraciones.Npclist[TempCharIndex].flags.AttackedFirstBy + ".",
										FontTypeNames.FONTTYPE_INFO);
							}
						}
					}
				}

				FoundSomething = 1;
				Declaraciones.UserList[UserIndex].flags.TargetNpcTipo = Declaraciones.Npclist[TempCharIndex].NPCtype;
				Declaraciones.UserList[UserIndex].flags.TargetNPC = TempCharIndex;
				Declaraciones.UserList[UserIndex].flags.TargetUser = 0;
				Declaraciones.UserList[UserIndex].flags.TargetObj = 0;
			}

			if (FoundChar == 0) {
				Declaraciones.UserList[UserIndex].flags.TargetNPC = 0;
				Declaraciones.UserList[UserIndex].flags.TargetNpcTipo = eNPCType.Comun;
				Declaraciones.UserList[UserIndex].flags.TargetUser = 0;
			}

			/* '*** NO ENCOTRO NADA *** */
			if (FoundSomething == 0) {
				Declaraciones.UserList[UserIndex].flags.TargetNPC = 0;
				Declaraciones.UserList[UserIndex].flags.TargetNpcTipo = eNPCType.Comun;
				Declaraciones.UserList[UserIndex].flags.TargetUser = 0;
				Declaraciones.UserList[UserIndex].flags.TargetObj = 0;
				Declaraciones.UserList[UserIndex].flags.TargetObjMap = 0;
				Declaraciones.UserList[UserIndex].flags.TargetObjX = 0;
				Declaraciones.UserList[UserIndex].flags.TargetObjY = 0;
				Protocol.WriteMultiMessage(UserIndex, eMessages.DontSeeAnything);
			}
		} else {
			if (FoundSomething == 0) {
				Declaraciones.UserList[UserIndex].flags.TargetNPC = 0;
				Declaraciones.UserList[UserIndex].flags.TargetNpcTipo = eNPCType.Comun;
				Declaraciones.UserList[UserIndex].flags.TargetUser = 0;
				Declaraciones.UserList[UserIndex].flags.TargetObj = 0;
				Declaraciones.UserList[UserIndex].flags.TargetObjMap = 0;
				Declaraciones.UserList[UserIndex].flags.TargetObjX = 0;
				Declaraciones.UserList[UserIndex].flags.TargetObjY = 0;

				Protocol.WriteMultiMessage(UserIndex, eMessages.DontSeeAnything);
			}
		}

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en LookAtTile. Error " + Err.Number + " : " + Err.description);

	}

	static eHeading FindDirection(Declaraciones.WorldPos Pos, Declaraciones.WorldPos Target) {
		eHeading retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* 'Devuelve la direccion en la cual el target se encuentra */
		/* 'desde pos, 0 si la direc es igual */
		/* '***************************************************************** */

		int X;
		int Y;

		X = Pos.X - Target.X;
		Y = Pos.Y - Target.Y;

		/* 'NE */
		if (vb6.Sgn(X) == -1 && vb6.Sgn(Y) == 1) {
			retval = vb6.IIf(Matematicas.RandomNumber(0, 1), eHeading.NORTH, eHeading.EAST);
			return retval;
		}

		/* 'NW */
		if (vb6.Sgn(X) == 1 && vb6.Sgn(Y) == 1) {
			retval = vb6.IIf(Matematicas.RandomNumber(0, 1), eHeading.WEST, eHeading.NORTH);
			return retval;
		}

		/* 'SW */
		if (vb6.Sgn(X) == 1 && vb6.Sgn(Y) == -1) {
			retval = vb6.IIf(Matematicas.RandomNumber(0, 1), eHeading.WEST, eHeading.SOUTH);
			return retval;
		}

		/* 'SE */
		if (vb6.Sgn(X) == -1 && vb6.Sgn(Y) == -1) {
			retval = vb6.IIf(Matematicas.RandomNumber(0, 1), eHeading.SOUTH, eHeading.EAST);
			return retval;
		}

		/* 'Sur */
		if (vb6.Sgn(X) == 0 && vb6.Sgn(Y) == -1) {
			retval = eHeading.SOUTH;
			return retval;
		}

		/* 'norte */
		if (vb6.Sgn(X) == 0 && vb6.Sgn(Y) == 1) {
			retval = eHeading.NORTH;
			return retval;
		}

		/* 'oeste */
		if (vb6.Sgn(X) == 1 && vb6.Sgn(Y) == 0) {
			retval = eHeading.WEST;
			return retval;
		}

		/* 'este */
		if (vb6.Sgn(X) == -1 && vb6.Sgn(Y) == 0) {
			retval = eHeading.EAST;
			return retval;
		}

		/* 'misma */
		if (vb6.Sgn(X) == 0 && vb6.Sgn(Y) == 0) {
			retval = 0;
			return retval;
		}

		return retval;
	}

	static boolean ItemNoEsDeMapa(int index, boolean bIsExit) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		retval = Declaraciones.ObjData[index].OBJType != eOBJType.otPuertas
				&& Declaraciones.ObjData[index].OBJType != eOBJType.otForos
				&& Declaraciones.ObjData[index].OBJType != eOBJType.otCarteles
				&& Declaraciones.ObjData[index].OBJType != eOBJType.otArboles
				&& Declaraciones.ObjData[index].OBJType != eOBJType.otYacimiento
				&& !(Declaraciones.ObjData[index].OBJType == eOBJType.otTeleport && bIsExit);

		return retval;
	}

	static boolean MostrarCantidad(int index) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		retval = Declaraciones.ObjData[index].OBJType != eOBJType.otPuertas
				&& Declaraciones.ObjData[index].OBJType != eOBJType.otForos
				&& Declaraciones.ObjData[index].OBJType != eOBJType.otCarteles
				&& Declaraciones.ObjData[index].OBJType != eOBJType.otArboles
				&& Declaraciones.ObjData[index].OBJType != eOBJType.otYacimiento
				&& Declaraciones.ObjData[index].OBJType != eOBJType.otTeleport;

		return retval;
	}

	static boolean EsObjetoFijo(eOBJType OBJType) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		retval = OBJType == eOBJType.otForos || OBJType == eOBJType.otCarteles || OBJType == eOBJType.otArboles
				|| OBJType == eOBJType.otYacimiento;
		return retval;
	}

	static int RestrictStringToByte(String /* FIXME BYREF!! */ restrict) {
		int retval;
		/* '*************************************************** */
		/* 'Author: Torres Patricio (Pato) */
		/* 'Last Modification: 04/18/2011 */
		/* ' */
		/* '*************************************************** */
		restrict = vb6.UCase(restrict);

		switch (restrict) {
		case "NEWBIE":
			retval = 1;

			break;

		case "ARMADA":
			retval = 2;

			break;

		case "CAOS":
			retval = 3;

			break;

		case "FACCION":
			retval = 4;

			break;

		default:
			retval = 0;
			break;
		}
		return retval;
	}

	static String RestrictByteToString(int restrict) {
		String retval;
		/* '*************************************************** */
		/* 'Author: Torres Patricio (Pato) */
		/* 'Last Modification: 04/18/2011 */
		/* ' */
		/* '*************************************************** */
		switch (restrict) {
		case 1:
			retval = "NEWBIE";

			break;

		case 2:
			retval = "ARMADA";

			break;

		case 3:
			retval = "CAOS";

			break;

		case 4:
			retval = "FACCION";

			break;

		case 0:
			retval = "NO";
			break;
		}
		return retval;
	}

	static int TerrainStringToByte(String /* FIXME BYREF!! */ restrict) {
		int retval;
		/* '*************************************************** */
		/* 'Author: Torres Patricio (Pato) */
		/* 'Last Modification: 04/18/2011 */
		/* ' */
		/* '*************************************************** */
		restrict = vb6.UCase(restrict);

		switch (restrict) {
		case "NIEVE":
			retval = 1;

			break;

		case "DESIERTO":
			retval = 2;

			break;

		case "CIUDAD":
			retval = 3;

			break;

		case "CAMPO":
			retval = 4;

			break;

		case "DUNGEON":
			retval = 5;

			break;

		default:
			retval = 0;
			break;
		}
		return retval;
	}

	static String TerrainByteToString(int restrict) {
		String retval;
		/* '*************************************************** */
		/* 'Author: Torres Patricio (Pato) */
		/* 'Last Modification: 04/18/2011 */
		/* ' */
		/* '*************************************************** */
		switch (restrict) {
		case 1:
			retval = "NIEVE";

			break;

		case 2:
			retval = "DESIERTO";

			break;

		case 3:
			retval = "CIUDAD";

			break;

		case 4:
			retval = "CAMPO";

			break;

		case 5:
			retval = "DUNGEON";

			break;

		case 0:
			retval = "BOSQUE";
			break;
		}
		return retval;
	}

}