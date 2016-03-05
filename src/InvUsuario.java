
/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"InvUsuario"')] */
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

public class InvUsuario {

	static boolean TieneObjetosRobables(int UserIndex) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' 22/05/2010: Los items newbies ya no son robables. */
		/* '*************************************************** */

		/* '17/09/02 */
		/* 'Agregue que la función se asegure que el objeto no es un barco */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int i;
		int ObjIndex;

		for (i = (1); i <= (Declaraciones.UserList[UserIndex].CurrentInventorySlots); i++) {
			ObjIndex = Declaraciones.UserList[UserIndex].Invent.Object[i].ObjIndex;
			if (ObjIndex > 0) {
				if ((Declaraciones.ObjData[ObjIndex].OBJType != eOBJType.otLlaves
						&& Declaraciones.ObjData[ObjIndex].OBJType != eOBJType.otBarcos && !ItemNewbie(ObjIndex))) {
					retval = true;
					return retval;
				}
			}
		}

		return retval;

		/* FIXME: ErrHandler : */
		General.LogError("Error en TieneObjetosRobables. Error: " + Err.Number + " - " + Err.description);
		return retval;
	}

	static boolean ClasePuedeUsarItem(int UserIndex, int ObjIndex) {
		return ClasePuedeUsarItem(UserIndex, ObjIndex, String());
	}

	static boolean ClasePuedeUsarItem(int UserIndex, int ObjIndex, String sMotivo) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 14/01/2010 (ZaMa) */
		/*
		 * '14/01/2010: ZaMa - Agrego el motivo por el que no puede equipar/usar
		 * el item.
		 */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO manejador */

		/* 'Admins can use ANYTHING! */
		if (Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) {
			if (Declaraciones.ObjData[ObjIndex].ClaseProhibida[1] != 0) {
				int i;
				for (i = (1); i <= (Declaraciones.NUMCLASES); i++) {
					if (Declaraciones.ObjData[ObjIndex].ClaseProhibida[i] == Declaraciones.UserList[UserIndex].clase) {
						retval = false;
						sMotivo = "Tu clase no puede usar este objeto.";
						return retval;
					}
				}
			}
		}

		retval = true;

		return retval;

		/* FIXME: manejador : */
		General.LogError("Error en ClasePuedeUsarItem");
		return retval;
	}

	static void QuitarNewbieObj(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int j;

		for (j = (1); j <= (Declaraciones.UserList[UserIndex].CurrentInventorySlots); j++) {
			if (Declaraciones.UserList[UserIndex].Invent.Object[j].ObjIndex > 0) {

				if (Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.Object[j].ObjIndex].Newbie == 1) {
					QuitarUserInvItem(UserIndex, j, Declaraciones.MAX_INVENTORY_OBJS);
				}
				UpdateUserInv(false, UserIndex, j);

			}
		}

		/*
		 * '[Barrin 17-12-03] Si el usuario dejó de ser Newbie, y estaba en el
		 * Newbie Dungeon
		 */
		/* 'es transportado a su hogar de origen ;) */
		if (Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].Restringir == eRestrict.restrict_newbie) {

			Declaraciones.WorldPos DeDonde;

			switch (Declaraciones.UserList[UserIndex].Hogar) {
			/* 'Vamos a tener que ir por todo el desierto... uff! */
			case cLindos:
				DeDonde = Declaraciones.Lindos;
				break;

			case cUllathorpe:
				DeDonde = Declaraciones.Ullathorpe;
				break;

			case cBanderbill:
				DeDonde = Declaraciones.Banderbill;
				break;

			default:
				DeDonde = Declaraciones.Nix;
				break;
			}

			UsUaRiOs.WarpUserChar(UserIndex, DeDonde.Map, DeDonde.X, DeDonde.Y, true);

		}
		/* '[/Barrin] */

	}

	static void LimpiarInventario(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int j;

		for (j = (1); j <= (Declaraciones.UserList[UserIndex].CurrentInventorySlots); j++) {
			Declaraciones.UserList[UserIndex].Invent.Object[j].ObjIndex = 0;
			Declaraciones.UserList[UserIndex].Invent.Object[j].Amount = 0;
			Declaraciones.UserList[UserIndex].Invent.Object[j].Equipped = 0;
		}

		Declaraciones.UserList[UserIndex].Invent.NroItems = 0;

		Declaraciones.UserList[UserIndex].Invent.ArmourEqpObjIndex = 0;
		Declaraciones.UserList[UserIndex].Invent.ArmourEqpSlot = 0;

		Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex = 0;
		Declaraciones.UserList[UserIndex].Invent.WeaponEqpSlot = 0;

		Declaraciones.UserList[UserIndex].Invent.CascoEqpObjIndex = 0;
		Declaraciones.UserList[UserIndex].Invent.CascoEqpSlot = 0;

		Declaraciones.UserList[UserIndex].Invent.EscudoEqpObjIndex = 0;
		Declaraciones.UserList[UserIndex].Invent.EscudoEqpSlot = 0;

		Declaraciones.UserList[UserIndex].Invent.AnilloEqpObjIndex = 0;
		Declaraciones.UserList[UserIndex].Invent.AnilloEqpSlot = 0;

		Declaraciones.UserList[UserIndex].Invent.MunicionEqpObjIndex = 0;
		Declaraciones.UserList[UserIndex].Invent.MunicionEqpSlot = 0;

		Declaraciones.UserList[UserIndex].Invent.BarcoObjIndex = 0;
		Declaraciones.UserList[UserIndex].Invent.BarcoSlot = 0;

		Declaraciones.UserList[UserIndex].Invent.MochilaEqpObjIndex = 0;
		Declaraciones.UserList[UserIndex].Invent.MochilaEqpSlot = 0;

	}

	static void TirarOro(int Cantidad, int UserIndex) {
		/* '*************************************************** */
		/* 'Autor: Unknown (orginal version) */
		/* 'Last Modification: 23/01/2007 */
		/*
		 * '23/01/2007 -> Pablo (ToxicWaste): Billetera invertida y explotar oro
		 * en el agua.
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		/* 'If Cantidad > 100000 Then Exit Sub */

		/* 'SI EL Pjta TIENE ORO LO TIRAMOS */
		if ((Cantidad > 0) && (Cantidad <= Declaraciones.UserList[UserIndex].Stats.GLD)) {
			Declaraciones.Obj MiObj;
			/* 'info debug */
			int loops;

			/* 'Seguridad Alkon (guardo el oro tirado si supera los 50k) */
			if (Cantidad >= Declaraciones.MIN_GOLD_AMOUNT_LOG) {
				int j;
				int k;
				int M;
				String Cercanos;
				M = Declaraciones.UserList[UserIndex].Pos.Map;
				for (j = (Declaraciones.UserList[UserIndex].Pos.X - 10); j <= (Declaraciones.UserList[UserIndex].Pos.X
						+ 10); j++) {
					for (k = (Declaraciones.UserList[UserIndex].Pos.Y
							- 10); k <= (Declaraciones.UserList[UserIndex].Pos.Y + 10); k++) {
						if (Extra.InMapBounds(M, j, k)) {
							if (Declaraciones.MapData[M][j][k].UserIndex > 0) {
								Cercanos = Cercanos
										+ Declaraciones.UserList[Declaraciones.MapData[M][j][k].UserIndex].Name + ",";
							}
						}
					}
				}

				Cercanos = vb6.Left(Cercanos, vb6.Len(Cercanos) - 1);
				General.LogDesarrollo(Declaraciones.UserList[UserIndex].Name + " tiró " + Cantidad
						+ " monedas de oro en " + Declaraciones.UserList[UserIndex].Pos.Map + ", "
						+ Declaraciones.UserList[UserIndex].Pos.X + ", " + Declaraciones.UserList[UserIndex].Pos.Y
						+ ". Cercanos: " + Cercanos);
			}
			/* '/Seguridad */
			int Extra;
			int TeniaOro;
			TeniaOro = Declaraciones.UserList[UserIndex].Stats.GLD;
			/* 'Para evitar explotar demasiado */
			if (Cantidad > 500000) {
				Extra = Cantidad - 500000;
				Cantidad = 500000;
			}

			while ((Cantidad > 0)) {

				if (Cantidad > Declaraciones.MAX_INVENTORY_OBJS
						&& Declaraciones.UserList[UserIndex].Stats.GLD > Declaraciones.MAX_INVENTORY_OBJS) {
					MiObj.Amount = Declaraciones.MAX_INVENTORY_OBJS;
					Cantidad = Cantidad - MiObj.Amount;
				} else {
					MiObj.Amount = Cantidad;
					Cantidad = Cantidad - MiObj.Amount;
				}

				MiObj.ObjIndex = Declaraciones.iORO;

				if (Extra.EsGm(UserIndex)) {
					General.LogGM(Declaraciones.UserList[UserIndex].Name,
							"Tiró cantidad:" + MiObj.Amount + " Objeto:" + Declaraciones.ObjData[MiObj.ObjIndex].Name);
				}
				Declaraciones.WorldPos AuxPos;

				if (Declaraciones.UserList[UserIndex].clase == eClass.Pirat
						&& Declaraciones.UserList[UserIndex].Invent.BarcoObjIndex == 476) {
					AuxPos = InvNpc.TirarItemAlPiso(Declaraciones.UserList[UserIndex].Pos, MiObj, false);
					if (AuxPos.X != 0 && AuxPos.Y != 0) {
						Declaraciones.UserList[UserIndex].Stats.GLD = Declaraciones.UserList[UserIndex].Stats.GLD
								- MiObj.Amount;
					}
				} else {
					AuxPos = InvNpc.TirarItemAlPiso(Declaraciones.UserList[UserIndex].Pos, MiObj, true);
					if (AuxPos.X != 0 && AuxPos.Y != 0) {
						Declaraciones.UserList[UserIndex].Stats.GLD = Declaraciones.UserList[UserIndex].Stats.GLD
								- MiObj.Amount;
					}
				}

				/* 'info debug */
				loops = loops + 1;
				if (loops > 100) {
					General.LogError("Error en tiraroro");
					return;
				}

			}
			if (TeniaOro == Declaraciones.UserList[UserIndex].Stats.GLD) {
				Extra = 0;
			}
			if (Extra > 0) {
				Declaraciones.UserList[UserIndex].Stats.GLD = Declaraciones.UserList[UserIndex].Stats.GLD - Extra;
			}

		}

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en TirarOro. Error " + Err.Number + " : " + Err.description);
	}

	static void QuitarUserInvItem(int UserIndex, int Slot, int Cantidad) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		if (Slot < 1 || Slot > Declaraciones.UserList[UserIndex].CurrentInventorySlots) {
			return;
		}

		if (Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount <= Cantidad
				&& Declaraciones.UserList[UserIndex].Invent.Object[Slot].Equipped == 1) {
			Desequipar(UserIndex, Slot, true);
		}

		/* 'Quita un objeto */
		Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount = Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount
				- Cantidad;
		/* '¿Quedan mas? */
		if (Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount <= 0) {
			Declaraciones.UserList[UserIndex].Invent.NroItems = Declaraciones.UserList[UserIndex].Invent.NroItems - 1;
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex = 0;
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount = 0;
		}

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en QuitarUserInvItem. Error " + Err.Number + " : " + Err.description);

	}

	static void UpdateUserInv(boolean UpdateAll, int UserIndex, int Slot) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		Declaraciones.UserOBJ NullObj;
		int LoopC;

		/* 'Actualiza un solo slot */
		if (!UpdateAll) {

			/* 'Actualiza el inventario */
			if (Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex > 0) {
				UsUaRiOs.ChangeUserInv(UserIndex, Slot, Declaraciones.UserList[UserIndex].Invent.Object[Slot]);
			} else {
				UsUaRiOs.ChangeUserInv(UserIndex, Slot, NullObj);
			}

		} else {

			/* 'Actualiza todos los slots */
			for (LoopC = (1); LoopC <= (Declaraciones.UserList[UserIndex].CurrentInventorySlots); LoopC++) {
				/* 'Actualiza el inventario */
				if (Declaraciones.UserList[UserIndex].Invent.Object[LoopC].ObjIndex > 0) {
					UsUaRiOs.ChangeUserInv(UserIndex, LoopC, Declaraciones.UserList[UserIndex].Invent.Object[LoopC]);
				} else {
					UsUaRiOs.ChangeUserInv(UserIndex, LoopC, NullObj);
				}
			}
		}

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en UpdateUserInv. Error " + Err.Number + " : " + Err.description);

	}

	static void DropObj(int UserIndex, int Slot, int num, int Map, int X, int Y) {
		DropObj(UserIndex, Slot, num, Map, X, Y, false);
	}

	static void DropObj(int UserIndex, int Slot, int num, int Map, int X, int Y, boolean isDrop) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 11/5/2010 */
		/*
		 * '11/5/2010 - ZaMa: Arreglo bug que permitia apilar mas de 10k de
		 * items.
		 */
		/* '*************************************************** */

		Declaraciones.Obj DropObj;
		Declaraciones.Obj MapObj;
		String str;

		if (num > 0) {

			DropObj.ObjIndex = Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex;

			if ((ItemNewbie(DropObj.ObjIndex)
					&& (Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User))) {
				Protocol.WriteConsoleMsg(UserIndex, "No puedes tirar objetos newbie.", FontTypeNames.FONTTYPE_INFO);
				return;
			}

			/* ' Users can't drop non-transferible items */
			if (Declaraciones.ObjData[DropObj.ObjIndex].Intransferible == 1
					|| Declaraciones.ObjData[DropObj.ObjIndex].NoSeTira == 1) {
				if (((Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) != 0)) {
					Protocol.WriteConsoleMsg(UserIndex, "¡¡¡No puedes tirar este tipo de objeto!!!",
							FontTypeNames.FONTTYPE_FIGHT);
					return;
				}
			}

			DropObj.Amount = SistemaCombate.MinimoInt(num,
					Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount);

			/* 'Check objeto en el suelo */
			MapObj.ObjIndex = Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][X][Y].ObjInfo.ObjIndex;
			MapObj.Amount = Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][X][Y].ObjInfo.Amount;

			if (MapObj.ObjIndex == 0 || MapObj.ObjIndex == DropObj.ObjIndex) {

				if (MapObj.Amount == Declaraciones.MAX_INVENTORY_OBJS) {
					Protocol.WriteConsoleMsg(UserIndex, "No hay espacio en el piso.", FontTypeNames.FONTTYPE_INFO);
					return;
				}

				if (DropObj.Amount + MapObj.Amount > Declaraciones.MAX_INVENTORY_OBJS) {
					DropObj.Amount = Declaraciones.MAX_INVENTORY_OBJS - MapObj.Amount;
				}

				MakeObj(DropObj, Map, X, Y);
				QuitarUserInvItem(UserIndex, Slot, DropObj.Amount);
				UpdateUserInv(false, UserIndex, Slot);

				if (Declaraciones.ObjData[DropObj.ObjIndex].OBJType == eOBJType.otBarcos) {
					Protocol.WriteConsoleMsg(UserIndex, "¡¡ATENCIÓN!! ¡ACABAS DE TIRAR TU BARCA!",
							FontTypeNames.FONTTYPE_TALK);
				}

				if (!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) {
					General.LogGM(Declaraciones.UserList[UserIndex].Name,
							"Tiró cantidad:" + num + " Objeto:" + Declaraciones.ObjData[DropObj.ObjIndex].Name);
				}

				/*
				 * 'Log de Objetos que se tiran al piso. Pablo (ToxicWaste)
				 * 07/09/07
				 */
				/* 'Es un Objeto que tenemos que loguear? */
				if (((Declaraciones.ObjData[DropObj.ObjIndex].Log == 1)
						|| (Declaraciones.ObjData[DropObj.ObjIndex].OBJType == eOBJType.otLlaves))) {
					General.LogDesarrollo(Declaraciones.UserList[UserIndex].Name + " tiró al piso "
							+ vb6.IIf(isDrop, "", "al morir ") + DropObj.Amount + " "
							+ Declaraciones.ObjData[DropObj.ObjIndex].Name + " Mapa: " + Map + " X: " + X + " Y: " + Y);
					/*
					 * 'Es mucha cantidad? > Subí a 5000 el minimo porque si no
					 * se llenaba el log de cosas al pedo. (NicoNZ)
					 */
				} else if (DropObj.Amount >= Declaraciones.MIN_AMOUNT_LOG) {
					/* 'Si no es de los prohibidos de loguear, lo logueamos. */
					if (Declaraciones.ObjData[DropObj.ObjIndex].NoLog != 1) {
						General.LogDesarrollo(Declaraciones.UserList[UserIndex].Name + " tiró al piso "
								+ vb6.IIf(isDrop, "", "al morir ") + DropObj.Amount + " "
								+ Declaraciones.ObjData[DropObj.ObjIndex].Name + " Mapa: " + Map + " X: " + X + " Y: "
								+ Y);
					}
				} else if ((DropObj.Amount
						* Declaraciones.ObjData[DropObj.ObjIndex].Valor) >= Declaraciones.MIN_VALUE_LOG) {
					/* 'Si no es de los prohibidos de loguear, lo logueamos. */
					if (Declaraciones.ObjData[DropObj.ObjIndex].NoLog != 1) {
						General.LogDesarrollo(Declaraciones.UserList[UserIndex].Name + " tiró al piso "
								+ vb6.IIf(isDrop, "", "al morir ") + DropObj.Amount + " "
								+ Declaraciones.ObjData[DropObj.ObjIndex].Name + " Mapa: " + Map + " X: " + X + " Y: "
								+ Y);
					}
				}
			} else {
				Protocol.WriteConsoleMsg(UserIndex, "No hay espacio en el piso.", FontTypeNames.FONTTYPE_INFO);
			}
		}

	}

	static void EraseObj(int num, int Map, int X, int Y) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		Declaraciones.MapData[Map][X][Y].ObjInfo.Amount = Declaraciones.MapData[Map][X][Y].ObjInfo.Amount - num;

		if (Declaraciones.MapData[Map][X][Y].ObjInfo.Amount <= 0) {
			Declaraciones.MapData[Map][X][Y].ObjInfo.ObjIndex = 0;
			Declaraciones.MapData[Map][X][Y].ObjInfo.Amount = 0;

			modSendData.SendToAreaByPos(Map, X, Y, Protocol.PrepareMessageObjectDelete(X, Y));
		}

	}

	static void MakeObj(Declaraciones.Obj /* FIXME BYREF!! */ Obj, int Map, int X, int Y) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		if (Obj.ObjIndex > 0 && Obj.ObjIndex <= vb6.UBound(Declaraciones.ObjData)) {

			if (Declaraciones.MapData[Map][X][Y].ObjInfo.ObjIndex == Obj.ObjIndex) {
				Declaraciones.MapData[Map][X][Y].ObjInfo.Amount = Declaraciones.MapData[Map][X][Y].ObjInfo.Amount
						+ Obj.Amount;
			} else {
				Declaraciones.MapData[Map][X][Y].ObjInfo = Obj;

				modSendData.SendToAreaByPos(Map, X, Y,
						Protocol.PrepareMessageObjectCreate(Declaraciones.ObjData[Obj.ObjIndex].GrhIndex, X, Y));
			}
		}

	}

	static boolean MeterItemEnInventario(int UserIndex,
			Declaraciones.Obj /* FIXME BYREF!! */ MiObj) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */
		int Slot;

		/* '¿el user ya tiene un objeto del mismo tipo? */
		Slot = 1;

		while (!(Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex == MiObj.ObjIndex
				&& Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount
						+ MiObj.Amount <= Declaraciones.MAX_INVENTORY_OBJS)) {
			Slot = Slot + 1;
			if (Slot > Declaraciones.UserList[UserIndex].CurrentInventorySlots) {
				break; /* FIXME: EXIT DO */
			}
		}

		/* 'Sino busca un slot vacio */
		if (Slot > Declaraciones.UserList[UserIndex].CurrentInventorySlots) {
			Slot = 1;
			while (!(Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex == 0)) {
				Slot = Slot + 1;
				if (Slot > Declaraciones.UserList[UserIndex].CurrentInventorySlots) {
					Protocol.WriteConsoleMsg(UserIndex, "No puedes cargar más objetos.", FontTypeNames.FONTTYPE_FIGHT);
					retval = false;
					return retval;
				}
			}
			Declaraciones.UserList[UserIndex].Invent.NroItems = Declaraciones.UserList[UserIndex].Invent.NroItems + 1;
		}

		if (Slot > Declaraciones.MAX_NORMAL_INVENTORY_SLOTS && Slot <= Declaraciones.MAX_INVENTORY_SLOTS) {
			if (!ItemSeCae(MiObj.ObjIndex)) {
				Protocol.WriteConsoleMsg(UserIndex,
						"No puedes contener objetos especiales en tu "
								+ Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.MochilaEqpObjIndex].Name
								+ ".",
						FontTypeNames.FONTTYPE_FIGHT);
				retval = false;
				return retval;
			}
		}
		/* 'Mete el objeto */
		if (Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount
				+ MiObj.Amount <= Declaraciones.MAX_INVENTORY_OBJS) {
			/* 'Menor que MAX_INV_OBJS */
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex = MiObj.ObjIndex;
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount = Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount
					+ MiObj.Amount;
		} else {
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount = Declaraciones.MAX_INVENTORY_OBJS;
		}

		retval = true;

		UpdateUserInv(false, UserIndex, Slot);

		return retval;
		/* FIXME: ErrHandler : */
		General.LogError("Error en MeterItemEnInventario. Error " + Err.Number + " : " + Err.description);
		return retval;
	}

	static void GetObj(int UserIndex) {
		/* '*************************************************** */
		/* 'Autor: Unknown (orginal version) */
		/* 'Last Modification: 18/12/2009 */
		/* '18/12/2009: ZaMa - Oro directo a la billetera. */
		/* '*************************************************** */

		Declaraciones.ObjData Obj;
		Declaraciones.Obj MiObj;
		String ObjPos;

		/* '¿Hay algun obj? */
		if (Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].ObjInfo.ObjIndex > 0) {
			/* '¿Esta permitido agarrar este obj? */
			if (Declaraciones.ObjData[Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].ObjInfo.ObjIndex].Agarrable != 1) {
				int X;
				int Y;

				X = Declaraciones.UserList[UserIndex].Pos.X;
				Y = Declaraciones.UserList[UserIndex].Pos.Y;

				Obj = Declaraciones.ObjData[Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].ObjInfo.ObjIndex];
				MiObj.Amount = Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][X][Y].ObjInfo.Amount;
				MiObj.ObjIndex = Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][X][Y].ObjInfo.ObjIndex;

				/* ' Oro directo a la billetera! */
				if (Obj.OBJType == otGuita) {
					Declaraciones.UserList[UserIndex].Stats.GLD = Declaraciones.UserList[UserIndex].Stats.GLD
							+ MiObj.Amount;
					/* 'Quitamos el objeto */
					EraseObj(Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][X][Y].ObjInfo.Amount,
							Declaraciones.UserList[UserIndex].Pos.Map, Declaraciones.UserList[UserIndex].Pos.X,
							Declaraciones.UserList[UserIndex].Pos.Y);

					Protocol.WriteUpdateGold(UserIndex);

					if (MiObj.Amount >= Declaraciones.MIN_GOLD_AMOUNT_LOG) {
						ObjPos = " Mapa: " + Declaraciones.UserList[UserIndex].Pos.Map + " X: "
								+ Declaraciones.UserList[UserIndex].Pos.X + " Y: "
								+ Declaraciones.UserList[UserIndex].Pos.Y;
						General.LogDesarrollo(Declaraciones.UserList[UserIndex].Name + " juntó del piso " + MiObj.Amount
								+ " monedas de oro" + Declaraciones.ObjData[MiObj.ObjIndex].Name + ObjPos);
					}
				} else {
					if (MeterItemEnInventario(UserIndex, MiObj)) {

						/* 'Quitamos el objeto */
						EraseObj(Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][X][Y].ObjInfo.Amount,
								Declaraciones.UserList[UserIndex].Pos.Map, Declaraciones.UserList[UserIndex].Pos.X,
								Declaraciones.UserList[UserIndex].Pos.Y);
						/* 'Si no es un usuario común logueamos */
						if (((Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) == 0)) {
							General.LogGM(Declaraciones.UserList[UserIndex].Name,
									"Agarro:" + MiObj.Amount + " Objeto:" + Declaraciones.ObjData[MiObj.ObjIndex].Name);
						}

						/*
						 * 'Log de Objetos que se agarran del piso. Pablo
						 * (ToxicWaste) 07/09/07
						 */
						/* 'Es un Objeto que tenemos que loguear? */
						if (((Declaraciones.ObjData[MiObj.ObjIndex].Log == 1)
								|| (Declaraciones.ObjData[MiObj.ObjIndex].OBJType == eOBJType.otLlaves))) {
							ObjPos = " Mapa: " + Declaraciones.UserList[UserIndex].Pos.Map + " X: "
									+ Declaraciones.UserList[UserIndex].Pos.X + " Y: "
									+ Declaraciones.UserList[UserIndex].Pos.Y;
							General.LogDesarrollo(Declaraciones.UserList[UserIndex].Name + " juntó del piso "
									+ MiObj.Amount + " " + Declaraciones.ObjData[MiObj.ObjIndex].Name + ObjPos);
							/* 'Es mucha cantidad? */
						} else if (MiObj.Amount >= Declaraciones.MIN_AMOUNT_LOG) {
							/*
							 * 'Si no es de los prohibidos de loguear, lo
							 * logueamos.
							 */
							if (Declaraciones.ObjData[MiObj.ObjIndex].NoLog != 1) {
								ObjPos = " Mapa: " + Declaraciones.UserList[UserIndex].Pos.Map + " X: "
										+ Declaraciones.UserList[UserIndex].Pos.X + " Y: "
										+ Declaraciones.UserList[UserIndex].Pos.Y;
								General.LogDesarrollo(Declaraciones.UserList[UserIndex].Name + " juntó del piso "
										+ MiObj.Amount + " " + Declaraciones.ObjData[MiObj.ObjIndex].Name + ObjPos);
							}
						} else if ((MiObj.Amount
								* Declaraciones.ObjData[MiObj.ObjIndex].Valor) >= Declaraciones.MIN_VALUE_LOG) {
							/*
							 * 'Si no es de los prohibidos de loguear, lo
							 * logueamos.
							 */
							if (Declaraciones.ObjData[MiObj.ObjIndex].NoLog != 1) {
								ObjPos = " Mapa: " + Declaraciones.UserList[UserIndex].Pos.Map + " X: "
										+ Declaraciones.UserList[UserIndex].Pos.X + " Y: "
										+ Declaraciones.UserList[UserIndex].Pos.Y;
								General.LogDesarrollo(Declaraciones.UserList[UserIndex].Name + " juntó del piso "
										+ MiObj.Amount + " " + Declaraciones.ObjData[MiObj.ObjIndex].Name + ObjPos);
							}
						}
					}
				}
			}
		} else {
			Protocol.WriteConsoleMsg(UserIndex, "No hay nada aquí.", FontTypeNames.FONTTYPE_INFO);
		}

	}

	static void Desequipar(int UserIndex, int Slot, boolean RefreshChar) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		/* 'Desequipa el item slot del inventario */
		Declaraciones.ObjData Obj;

		if ((Slot < vb6.LBound(Declaraciones.UserList[UserIndex].Invent.Object))
				|| (Slot > vb6.UBound(Declaraciones.UserList[UserIndex].Invent.Object))) {
			return;
		} else if (Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex == 0) {
			return;
		}

		Obj = Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex];

		switch (Obj.OBJType) {
		case otWeapon:
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].Equipped = 0;
			Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex = 0;
			Declaraciones.UserList[UserIndex].Invent.WeaponEqpSlot = 0;

			if (!Declaraciones.UserList[UserIndex].flags.Mimetizado == 1) {
				Declaraciones.UserList[UserIndex].Char.WeaponAnim = Declaraciones.NingunArma;

				if (RefreshChar && Declaraciones.UserList[UserIndex].flags.Navegando != 1) {
					UsUaRiOs.ChangeUserChar(UserIndex, Declaraciones.UserList[UserIndex].Char.body,
							Declaraciones.UserList[UserIndex].Char.Head, Declaraciones.UserList[UserIndex].Char.heading,
							Declaraciones.UserList[UserIndex].Char.WeaponAnim,
							Declaraciones.UserList[UserIndex].Char.ShieldAnim,
							Declaraciones.UserList[UserIndex].Char.CascoAnim);
				}
			}

			break;

		case otFlechas:
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].Equipped = 0;
			Declaraciones.UserList[UserIndex].Invent.MunicionEqpObjIndex = 0;
			Declaraciones.UserList[UserIndex].Invent.MunicionEqpSlot = 0;

			break;

		case otAnillo:
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].Equipped = 0;
			Declaraciones.UserList[UserIndex].Invent.AnilloEqpObjIndex = 0;
			Declaraciones.UserList[UserIndex].Invent.AnilloEqpSlot = 0;

			break;

		case otArmadura:
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].Equipped = 0;
			Declaraciones.UserList[UserIndex].Invent.ArmourEqpObjIndex = 0;
			Declaraciones.UserList[UserIndex].Invent.ArmourEqpSlot = 0;

			if (Declaraciones.UserList[UserIndex].flags.Navegando != 1) {
				General.DarCuerpoDesnudo(UserIndex, Declaraciones.UserList[UserIndex].flags.Mimetizado == 1);
			}
			/* '[TEMPORAL] */
			Declaraciones.UserList[UserIndex].flags.Desnudo = 1;

			if (RefreshChar) {
				UsUaRiOs.ChangeUserChar(UserIndex, Declaraciones.UserList[UserIndex].Char.body,
						Declaraciones.UserList[UserIndex].Char.Head, Declaraciones.UserList[UserIndex].Char.heading,
						Declaraciones.UserList[UserIndex].Char.WeaponAnim,
						Declaraciones.UserList[UserIndex].Char.ShieldAnim,
						Declaraciones.UserList[UserIndex].Char.CascoAnim);
			}

			break;

		case otCASCO:
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].Equipped = 0;
			Declaraciones.UserList[UserIndex].Invent.CascoEqpObjIndex = 0;
			Declaraciones.UserList[UserIndex].Invent.CascoEqpSlot = 0;

			if (!Declaraciones.UserList[UserIndex].flags.Mimetizado == 1) {
				Declaraciones.UserList[UserIndex].Char.CascoAnim = Declaraciones.NingunCasco;

				if (RefreshChar) {
					UsUaRiOs.ChangeUserChar(UserIndex, Declaraciones.UserList[UserIndex].Char.body,
							Declaraciones.UserList[UserIndex].Char.Head, Declaraciones.UserList[UserIndex].Char.heading,
							Declaraciones.UserList[UserIndex].Char.WeaponAnim,
							Declaraciones.UserList[UserIndex].Char.ShieldAnim,
							Declaraciones.UserList[UserIndex].Char.CascoAnim);
				}
			}

			break;

		case otESCUDO:
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].Equipped = 0;
			Declaraciones.UserList[UserIndex].Invent.EscudoEqpObjIndex = 0;
			Declaraciones.UserList[UserIndex].Invent.EscudoEqpSlot = 0;

			if (!Declaraciones.UserList[UserIndex].flags.Mimetizado == 1) {
				Declaraciones.UserList[UserIndex].Char.ShieldAnim = Declaraciones.NingunEscudo;

				if (RefreshChar) {
					UsUaRiOs.ChangeUserChar(UserIndex, Declaraciones.UserList[UserIndex].Char.body,
							Declaraciones.UserList[UserIndex].Char.Head, Declaraciones.UserList[UserIndex].Char.heading,
							Declaraciones.UserList[UserIndex].Char.WeaponAnim,
							Declaraciones.UserList[UserIndex].Char.ShieldAnim,
							Declaraciones.UserList[UserIndex].Char.CascoAnim);
				}
			}

			break;

		case otMochilas:
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].Equipped = 0;
			Declaraciones.UserList[UserIndex].Invent.MochilaEqpObjIndex = 0;
			Declaraciones.UserList[UserIndex].Invent.MochilaEqpSlot = 0;

			InvUsuario.TirarTodosLosItemsEnMochila(UserIndex);
			Declaraciones.UserList[UserIndex].CurrentInventorySlots = Declaraciones.MAX_NORMAL_INVENTORY_SLOTS;
			break;
		}

		if (RefreshChar) {
			Protocol.WriteUpdateUserStats(UserIndex);
		}

		UpdateUserInv(false, UserIndex, Slot);

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en Desquipar. Error " + Err.Number + " : " + Err.description);

	}

	static boolean SexoPuedeUsarItem(int UserIndex, int ObjIndex) {
		return SexoPuedeUsarItem(UserIndex, ObjIndex, String());
	}

	static boolean SexoPuedeUsarItem(int UserIndex, int ObjIndex, String sMotivo) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 14/01/2010 (ZaMa) */
		/*
		 * '14/01/2010: ZaMa - Agrego el motivo por el que no puede equipar/usar
		 * el item.
		 */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		if (Declaraciones.ObjData[ObjIndex].Mujer == 1) {
			retval = Declaraciones.UserList[UserIndex].Genero != eGenero.Hombre;
		} else if (Declaraciones.ObjData[ObjIndex].Hombre == 1) {
			retval = Declaraciones.UserList[UserIndex].Genero != eGenero.Mujer;
		} else {
			retval = true;
		}

		if (!retval) {
			sMotivo = "Tu género no puede usar este objeto.";
		}

		return retval;
		/* FIXME: ErrHandler : */
		General.LogError("SexoPuedeUsarItem");
		return retval;
	}

	static boolean FaccionPuedeUsarItem(int UserIndex, int ObjIndex) {
		return FaccionPuedeUsarItem(UserIndex, ObjIndex, String());
	}

	static boolean FaccionPuedeUsarItem(int UserIndex, int ObjIndex, String sMotivo) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 14/01/2010 (ZaMa) */
		/*
		 * '14/01/2010: ZaMa - Agrego el motivo por el que no puede equipar/usar
		 * el item.
		 */
		/* '*************************************************** */

		if (Declaraciones.ObjData[ObjIndex].Real == 1) {
			if (!ES.criminal(UserIndex)) {
				retval = Extra.esArmada(UserIndex);
			} else {
				retval = false;
			}
		} else if (Declaraciones.ObjData[ObjIndex].Caos == 1) {
			if (ES.criminal(UserIndex)) {
				retval = Extra.esCaos(UserIndex);
			} else {
				retval = false;
			}
		} else {
			retval = true;
		}

		if (!retval) {
			sMotivo = "Tu alineación no puede usar este objeto.";
		}

		return retval;
	}

	static void EquiparInvItem(int UserIndex, int Slot) {
		/* '************************************************* */
		/* 'Author: Unknown */
		/* 'Last modified: 14/01/2010 (ZaMa) */
		/*
		 * '01/08/2009: ZaMa - Now it's not sent any sound made by an invisible
		 * admin
		 */
		/*
		 * '14/01/2010: ZaMa - Agrego el motivo especifico por el que no puede
		 * equipar/usar el item.
		 */
		/* '************************************************* */

		/* FIXME: ON ERROR GOTO ErrHandler */

		/* 'Equipa un item del inventario */
		Declaraciones.ObjData Obj;
		int ObjIndex;
		String sMotivo;

		ObjIndex = Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex;
		Obj = Declaraciones.ObjData[ObjIndex];

		if (Obj.Newbie == 1 && !Extra.EsNewbie(UserIndex)) {
			Protocol.WriteConsoleMsg(UserIndex, "Sólo los newbies pueden usar este objeto.",
					FontTypeNames.FONTTYPE_INFO);
			return;
		}

		switch (Obj.OBJType) {
		case otWeapon:
			if (ClasePuedeUsarItem(UserIndex, ObjIndex, sMotivo)
					&& FaccionPuedeUsarItem(UserIndex, ObjIndex, sMotivo)) {
				/* 'Si esta equipado lo quita */
				if (Declaraciones.UserList[UserIndex].Invent.Object[Slot].Equipped) {
					/* 'Quitamos del inv el item */
					Desequipar(UserIndex, Slot, false);
					/* 'Animacion por defecto */
					if (Declaraciones.UserList[UserIndex].flags.Mimetizado == 1) {
						Declaraciones.UserList[UserIndex].CharMimetizado.WeaponAnim = Declaraciones.NingunArma;
					} else {
						Declaraciones.UserList[UserIndex].Char.WeaponAnim = Declaraciones.NingunArma;
						UsUaRiOs.ChangeUserChar(UserIndex, Declaraciones.UserList[UserIndex].Char.body,
								Declaraciones.UserList[UserIndex].Char.Head,
								Declaraciones.UserList[UserIndex].Char.heading,
								Declaraciones.UserList[UserIndex].Char.WeaponAnim,
								Declaraciones.UserList[UserIndex].Char.ShieldAnim,
								Declaraciones.UserList[UserIndex].Char.CascoAnim);
					}
					return;
				}

				/* 'Quitamos el elemento anterior */
				if (Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex > 0) {
					Desequipar(UserIndex, Declaraciones.UserList[UserIndex].Invent.WeaponEqpSlot, false);
				}

				Declaraciones.UserList[UserIndex].Invent.Object[Slot].Equipped = 1;
				Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex = ObjIndex;
				Declaraciones.UserList[UserIndex].Invent.WeaponEqpSlot = Slot;

				/*
				 * 'El sonido solo se envia si no lo produce un admin invisible
				 */
				if (!(Declaraciones.UserList[UserIndex].flags.AdminInvisible == 1)) {
					modSendData.SendData(SendTarget.ToPCArea, UserIndex,
							Protocol.PrepareMessagePlayWave(Declaraciones.SND_SACARARMA,
									Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y));
				}

				if (Declaraciones.UserList[UserIndex].flags.Mimetizado == 1) {
					Declaraciones.UserList[UserIndex].CharMimetizado.WeaponAnim = UsUaRiOs.GetWeaponAnim(UserIndex,
							ObjIndex);
				} else {
					Declaraciones.UserList[UserIndex].Char.WeaponAnim = UsUaRiOs.GetWeaponAnim(UserIndex, ObjIndex);
					UsUaRiOs.ChangeUserChar(UserIndex, Declaraciones.UserList[UserIndex].Char.body,
							Declaraciones.UserList[UserIndex].Char.Head, Declaraciones.UserList[UserIndex].Char.heading,
							Declaraciones.UserList[UserIndex].Char.WeaponAnim,
							Declaraciones.UserList[UserIndex].Char.ShieldAnim,
							Declaraciones.UserList[UserIndex].Char.CascoAnim);
				}
			} else {
				Protocol.WriteConsoleMsg(UserIndex, sMotivo, FontTypeNames.FONTTYPE_INFO);
			}

			break;

		case otAnillo:
			if (ClasePuedeUsarItem(UserIndex, ObjIndex, sMotivo)
					&& FaccionPuedeUsarItem(UserIndex, ObjIndex, sMotivo)) {
				/* 'Si esta equipado lo quita */
				if (Declaraciones.UserList[UserIndex].Invent.Object[Slot].Equipped) {
					/* 'Quitamos del inv el item */
					Desequipar(UserIndex, Slot, true);
					return;
				}

				/* 'Quitamos el elemento anterior */
				if (Declaraciones.UserList[UserIndex].Invent.AnilloEqpObjIndex > 0) {
					Desequipar(UserIndex, Declaraciones.UserList[UserIndex].Invent.AnilloEqpSlot, true);
				}

				Declaraciones.UserList[UserIndex].Invent.Object[Slot].Equipped = 1;
				Declaraciones.UserList[UserIndex].Invent.AnilloEqpObjIndex = ObjIndex;
				Declaraciones.UserList[UserIndex].Invent.AnilloEqpSlot = Slot;

			} else {
				Protocol.WriteConsoleMsg(UserIndex, sMotivo, FontTypeNames.FONTTYPE_INFO);
			}

			break;

		case otFlechas:
			if (ClasePuedeUsarItem(UserIndex, ObjIndex, sMotivo)
					&& FaccionPuedeUsarItem(UserIndex, ObjIndex, sMotivo)) {

				/* 'Si esta equipado lo quita */
				if (Declaraciones.UserList[UserIndex].Invent.Object[Slot].Equipped) {
					/* 'Quitamos del inv el item */
					Desequipar(UserIndex, Slot, true);
					return;
				}

				/* 'Quitamos el elemento anterior */
				if (Declaraciones.UserList[UserIndex].Invent.MunicionEqpObjIndex > 0) {
					Desequipar(UserIndex, Declaraciones.UserList[UserIndex].Invent.MunicionEqpSlot, true);
				}

				Declaraciones.UserList[UserIndex].Invent.Object[Slot].Equipped = 1;
				Declaraciones.UserList[UserIndex].Invent.MunicionEqpObjIndex = ObjIndex;
				Declaraciones.UserList[UserIndex].Invent.MunicionEqpSlot = Slot;

			} else {
				Protocol.WriteConsoleMsg(UserIndex, sMotivo, FontTypeNames.FONTTYPE_INFO);
			}

			break;

		case otArmadura:
			/* ' [TEMPORAL] If .flags.Navegando = 1 Then Exit Sub */

			/* 'Nos aseguramos que puede usarla */
			if (ClasePuedeUsarItem(UserIndex, ObjIndex, sMotivo) && SexoPuedeUsarItem(UserIndex, ObjIndex, sMotivo)
					&& CheckRazaUsaRopa(UserIndex, ObjIndex, sMotivo)
					&& FaccionPuedeUsarItem(UserIndex, ObjIndex, sMotivo)) {

				/* 'Si esta equipado lo quita */
				if (Declaraciones.UserList[UserIndex].Invent.Object[Slot].Equipped) {
					Desequipar(UserIndex, Slot, false);
					/*
					 * 'Call DarCuerpoDesnudo(UserIndex, .flags.Mimetizado = 1)
					 */
					if (Declaraciones.UserList[UserIndex].flags.Mimetizado == 0
							&& Declaraciones.UserList[UserIndex].flags.Navegando == 0) {
						UsUaRiOs.ChangeUserChar(UserIndex, Declaraciones.UserList[UserIndex].Char.body,
								Declaraciones.UserList[UserIndex].Char.Head,
								Declaraciones.UserList[UserIndex].Char.heading,
								Declaraciones.UserList[UserIndex].Char.WeaponAnim,
								Declaraciones.UserList[UserIndex].Char.ShieldAnim,
								Declaraciones.UserList[UserIndex].Char.CascoAnim);
					}

					Protocol.WriteUpdateUserStats(UserIndex);

					return;
				}

				/* 'Quita el anterior */
				if (Declaraciones.UserList[UserIndex].Invent.ArmourEqpObjIndex > 0) {
					Desequipar(UserIndex, Declaraciones.UserList[UserIndex].Invent.ArmourEqpSlot, true);
				}

				/* 'Lo equipa */
				Declaraciones.UserList[UserIndex].Invent.Object[Slot].Equipped = 1;
				Declaraciones.UserList[UserIndex].Invent.ArmourEqpObjIndex = ObjIndex;
				Declaraciones.UserList[UserIndex].Invent.ArmourEqpSlot = Slot;

				if (Declaraciones.UserList[UserIndex].flags.Mimetizado == 1) {
					Declaraciones.UserList[UserIndex].CharMimetizado.body = Obj.Ropaje;
				} else {
					if (Declaraciones.UserList[UserIndex].flags.Navegando == 0) {
						Declaraciones.UserList[UserIndex].Char.body = Obj.Ropaje;
						UsUaRiOs.ChangeUserChar(UserIndex, Declaraciones.UserList[UserIndex].Char.body,
								Declaraciones.UserList[UserIndex].Char.Head,
								Declaraciones.UserList[UserIndex].Char.heading,
								Declaraciones.UserList[UserIndex].Char.WeaponAnim,
								Declaraciones.UserList[UserIndex].Char.ShieldAnim,
								Declaraciones.UserList[UserIndex].Char.CascoAnim);
					}
				}
				Declaraciones.UserList[UserIndex].flags.Desnudo = 0;
			} else {
				Protocol.WriteConsoleMsg(UserIndex, sMotivo, FontTypeNames.FONTTYPE_INFO);
			}

			break;

		case otCASCO:
			/* ' [TEMPORAL] If .flags.Navegando = 1 Then Exit Sub */
			if (ClasePuedeUsarItem(UserIndex, ObjIndex, sMotivo)) {
				/* 'Si esta equipado lo quita */
				if (Declaraciones.UserList[UserIndex].Invent.Object[Slot].Equipped) {
					Desequipar(UserIndex, Slot, false);
					if (Declaraciones.UserList[UserIndex].flags.Mimetizado == 1) {
						Declaraciones.UserList[UserIndex].CharMimetizado.CascoAnim = Declaraciones.NingunCasco;

					} else if (Declaraciones.UserList[UserIndex].flags.Navegando == 0) {
						Declaraciones.UserList[UserIndex].Char.CascoAnim = Declaraciones.NingunCasco;
						UsUaRiOs.ChangeUserChar(UserIndex, Declaraciones.UserList[UserIndex].Char.body,
								Declaraciones.UserList[UserIndex].Char.Head,
								Declaraciones.UserList[UserIndex].Char.heading,
								Declaraciones.UserList[UserIndex].Char.WeaponAnim,
								Declaraciones.UserList[UserIndex].Char.ShieldAnim,
								Declaraciones.UserList[UserIndex].Char.CascoAnim);
					}
					return;
				}

				/* 'Quita el anterior */
				if (Declaraciones.UserList[UserIndex].Invent.CascoEqpObjIndex > 0) {
					Desequipar(UserIndex, Declaraciones.UserList[UserIndex].Invent.CascoEqpSlot, false);
				}

				/* 'Lo equipa */
				Declaraciones.UserList[UserIndex].Invent.Object[Slot].Equipped = 1;
				Declaraciones.UserList[UserIndex].Invent.CascoEqpObjIndex = ObjIndex;
				Declaraciones.UserList[UserIndex].Invent.CascoEqpSlot = Slot;
				if (Declaraciones.UserList[UserIndex].flags.Mimetizado == 1) {
					Declaraciones.UserList[UserIndex].CharMimetizado.CascoAnim = Obj.CascoAnim;
				} else if (Declaraciones.UserList[UserIndex].flags.Navegando == 0) {
					Declaraciones.UserList[UserIndex].Char.CascoAnim = Obj.CascoAnim;
					UsUaRiOs.ChangeUserChar(UserIndex, Declaraciones.UserList[UserIndex].Char.body,
							Declaraciones.UserList[UserIndex].Char.Head, Declaraciones.UserList[UserIndex].Char.heading,
							Declaraciones.UserList[UserIndex].Char.WeaponAnim,
							Declaraciones.UserList[UserIndex].Char.ShieldAnim,
							Declaraciones.UserList[UserIndex].Char.CascoAnim);
				}

				Protocol.WriteUpdateUserStats(UserIndex);
			} else {
				Protocol.WriteConsoleMsg(UserIndex, sMotivo, FontTypeNames.FONTTYPE_INFO);
			}

			break;

		case otESCUDO:
			/* ' [TEMPORAL] If .flags.Navegando = 1 Then Exit Sub */

			if (ClasePuedeUsarItem(UserIndex, ObjIndex, sMotivo)
					&& FaccionPuedeUsarItem(UserIndex, ObjIndex, sMotivo)) {

				/* 'Si esta equipado lo quita */
				if (Declaraciones.UserList[UserIndex].Invent.Object[Slot].Equipped) {
					Desequipar(UserIndex, Slot, false);
					if (Declaraciones.UserList[UserIndex].flags.Mimetizado == 1) {
						Declaraciones.UserList[UserIndex].CharMimetizado.ShieldAnim = Declaraciones.NingunEscudo;

					} else if (Declaraciones.UserList[UserIndex].flags.Navegando == 0) {
						Declaraciones.UserList[UserIndex].Char.ShieldAnim = Declaraciones.NingunEscudo;
						UsUaRiOs.ChangeUserChar(UserIndex, Declaraciones.UserList[UserIndex].Char.body,
								Declaraciones.UserList[UserIndex].Char.Head,
								Declaraciones.UserList[UserIndex].Char.heading,
								Declaraciones.UserList[UserIndex].Char.WeaponAnim,
								Declaraciones.UserList[UserIndex].Char.ShieldAnim,
								Declaraciones.UserList[UserIndex].Char.CascoAnim);
					}

					Protocol.WriteUpdateUserStats(UserIndex);

					return;
				}

				/* 'Quita el anterior */
				if (Declaraciones.UserList[UserIndex].Invent.EscudoEqpObjIndex > 0) {
					Desequipar(UserIndex, Declaraciones.UserList[UserIndex].Invent.EscudoEqpSlot, false);
				}

				/* 'Lo equipa */
				Declaraciones.UserList[UserIndex].Invent.Object[Slot].Equipped = 1;
				Declaraciones.UserList[UserIndex].Invent.EscudoEqpObjIndex = ObjIndex;
				Declaraciones.UserList[UserIndex].Invent.EscudoEqpSlot = Slot;

				if (Declaraciones.UserList[UserIndex].flags.Mimetizado == 1) {
					Declaraciones.UserList[UserIndex].CharMimetizado.ShieldAnim = Obj.ShieldAnim;
				} else {
					if (Declaraciones.UserList[UserIndex].flags.Navegando == 0) {
						Declaraciones.UserList[UserIndex].Char.ShieldAnim = Obj.ShieldAnim;

						UsUaRiOs.ChangeUserChar(UserIndex, Declaraciones.UserList[UserIndex].Char.body,
								Declaraciones.UserList[UserIndex].Char.Head,
								Declaraciones.UserList[UserIndex].Char.heading,
								Declaraciones.UserList[UserIndex].Char.WeaponAnim,
								Declaraciones.UserList[UserIndex].Char.ShieldAnim,
								Declaraciones.UserList[UserIndex].Char.CascoAnim);
					}
					Protocol.WriteUpdateUserStats(UserIndex);
				}
			} else {
				Protocol.WriteConsoleMsg(UserIndex, sMotivo, FontTypeNames.FONTTYPE_INFO);
			}

			break;

		case otMochilas:
			if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
				Protocol.WriteConsoleMsg(UserIndex, "¡¡Estas muerto!! Solo podes usar items cuando estas vivo. ",
						FontTypeNames.FONTTYPE_INFO);
				return;
			}
			if (Declaraciones.UserList[UserIndex].Invent.Object[Slot].Equipped) {
				Desequipar(UserIndex, Slot, true);
				return;
			}
			if (Declaraciones.UserList[UserIndex].Invent.MochilaEqpObjIndex > 0) {
				Desequipar(UserIndex, Declaraciones.UserList[UserIndex].Invent.MochilaEqpSlot, true);
			}
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].Equipped = 1;
			Declaraciones.UserList[UserIndex].Invent.MochilaEqpObjIndex = ObjIndex;
			Declaraciones.UserList[UserIndex].Invent.MochilaEqpSlot = Slot;
			Declaraciones.UserList[UserIndex].CurrentInventorySlots = Declaraciones.MAX_NORMAL_INVENTORY_SLOTS
					+ Obj.MochilaType * 5;
			Protocol.WriteAddSlots(UserIndex, Obj.MochilaType);
			break;
		}

		/* 'Actualiza */
		UpdateUserInv(false, UserIndex, Slot);

		return;

		/* FIXME: ErrHandler : */
		General.LogError("EquiparInvItem Slot:" + Slot + " - Error: " + Err.Number + " - Error Description : "
				+ Err.description);
	}

	static boolean CheckRazaUsaRopa(int UserIndex, int ItemIndex) {
		return CheckRazaUsaRopa(UserIndex, ItemIndex, String());
	}

	static boolean CheckRazaUsaRopa(int UserIndex, int ItemIndex, String sMotivo) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 14/01/2010 (ZaMa) */
		/*
		 * '14/01/2010: ZaMa - Agrego el motivo por el que no puede equipar/usar
		 * el item.
		 */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		/* 'Verifica si la raza puede usar la ropa */
		if (Declaraciones.UserList[UserIndex].raza == eRaza.Humano
				|| Declaraciones.UserList[UserIndex].raza == eRaza.Elfo
				|| Declaraciones.UserList[UserIndex].raza == eRaza.Drow) {
			retval = (Declaraciones.ObjData[ItemIndex].RazaEnana == 0);
		} else {
			retval = (Declaraciones.ObjData[ItemIndex].RazaEnana == 1);
		}

		/*
		 * 'Solo se habilita la ropa exclusiva para Drows por ahora. Pablo
		 * (ToxicWaste)
		 */
		if ((Declaraciones.UserList[UserIndex].raza != eRaza.Drow) && Declaraciones.ObjData[ItemIndex].RazaDrow) {
			retval = false;
		}

		if (!retval) {
			sMotivo = "Tu raza no puede usar este objeto.";
		}

		return retval;

		/* FIXME: ErrHandler : */
		General.LogError("Error CheckRazaUsaRopa ItemIndex:" + ItemIndex);

		return retval;
	}

	static void UseInvItem(int UserIndex, int Slot) {
		/* '************************************************* */
		/* 'Author: Unknown */
		/* 'Last modified: 10/12/2009 */
		/* 'Handels the usage of items from inventory box. */
		/*
		 * '24/01/2007 Pablo (ToxicWaste) - Agrego el Cuerno de la Armada y la
		 * Legión.
		 */
		/*
		 * '24/01/2007 Pablo (ToxicWaste) - Utilización nueva de Barco en lvl 20
		 * por clase Pirata y Pescador.
		 */
		/*
		 * '01/08/2009: ZaMa - Now it's not sent any sound made by an invisible
		 * admin, except to its own client
		 */
		/*
		 * '17/11/2009: ZaMa - Ahora se envia una orientacion de la posicion
		 * hacia donde esta el que uso el cuerno.
		 */
		/*
		 * '27/11/2009: Budi - Se envia indivualmente cuando se modifica a la
		 * Agilidad o la Fuerza del personaje.
		 */
		/* '08/12/2009: ZaMa - Agrego el uso de hacha de madera elfica. */
		/*
		 * '10/12/2009: ZaMa - Arreglos y validaciones en todos las herramientas
		 * de trabajo.
		 */
		/* '************************************************* */

		Declaraciones.ObjData Obj;
		int ObjIndex;
		Declaraciones.ObjData TargObj;
		Declaraciones.Obj MiObj;

		if (Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount == 0) {
			return;
		}

		Obj = Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex];

		if (Obj.Newbie == 1 && !Extra.EsNewbie(UserIndex)) {
			Protocol.WriteConsoleMsg(UserIndex, "Sólo los newbies pueden usar estos objetos.",
					FontTypeNames.FONTTYPE_INFO);
			return;
		}

		if (Obj.OBJType == eOBJType.otWeapon) {
			if (Obj.proyectil == 1) {

				/*
				 * 'valido para evitar el flood pero no bloqueo. El bloqueo se
				 * hace en WLC con proyectiles.
				 */
				if (!modNuevoTimer.IntervaloPermiteUsar(UserIndex, false)) {
					return;
				}
			} else {
				/* 'dagas */
				if (!modNuevoTimer.IntervaloPermiteUsar(UserIndex)) {
					return;
				}
			}
		} else {
			if (!modNuevoTimer.IntervaloPermiteUsar(UserIndex)) {
				return;
			}
		}

		ObjIndex = Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex;
		Declaraciones.UserList[UserIndex].flags.TargetObjInvIndex = ObjIndex;
		Declaraciones.UserList[UserIndex].flags.TargetObjInvSlot = Slot;

		switch (Obj.OBJType) {
		case otUseOnce:
			if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
				Protocol.WriteConsoleMsg(UserIndex, "¡¡Estás muerto!! Sólo puedes usar ítems cuando estás vivo.",
						FontTypeNames.FONTTYPE_INFO);
				return;
			}

			/* 'Usa el item */
			Declaraciones.UserList[UserIndex].Stats.MinHam = Declaraciones.UserList[UserIndex].Stats.MinHam
					+ Obj.MinHam;
			if (Declaraciones.UserList[UserIndex].Stats.MinHam > Declaraciones.UserList[UserIndex].Stats.MaxHam) {
				Declaraciones.UserList[UserIndex].Stats.MinHam = Declaraciones.UserList[UserIndex].Stats.MaxHam;
			}
			Declaraciones.UserList[UserIndex].flags.Hambre = 0;
			Protocol.WriteUpdateHungerAndThirst(UserIndex);
			/* 'Sonido */

			if (ObjIndex == e_ObjetosCriticos.Manzana || ObjIndex == e_ObjetosCriticos.Manzana2
					|| ObjIndex == e_ObjetosCriticos.ManzanaNewbie) {
				Declaraciones.SonidosMapas.ReproducirSonido(SendTarget.ToPCArea, UserIndex,
						e_SoundIndex.MORFAR_MANZANA);
			} else {
				Declaraciones.SonidosMapas.ReproducirSonido(SendTarget.ToPCArea, UserIndex, e_SoundIndex.SOUND_COMIDA);
			}

			/* 'Quitamos del inv el item */
			QuitarUserInvItem(UserIndex, Slot, 1);

			UpdateUserInv(false, UserIndex, Slot);

			break;

		case otGuita:
			if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
				Protocol.WriteConsoleMsg(UserIndex, "¡¡Estás muerto!! Sólo puedes usar ítems cuando estás vivo.",
						FontTypeNames.FONTTYPE_INFO);
				return;
			}

			Declaraciones.UserList[UserIndex].Stats.GLD = Declaraciones.UserList[UserIndex].Stats.GLD
					+ Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount;
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount = 0;
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex = 0;
			Declaraciones.UserList[UserIndex].Invent.NroItems = Declaraciones.UserList[UserIndex].Invent.NroItems - 1;

			UpdateUserInv(false, UserIndex, Slot);
			Protocol.WriteUpdateGold(UserIndex);

			break;

		case otWeapon:
			if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
				Protocol.WriteConsoleMsg(UserIndex, "¡¡Estás muerto!! Sólo puedes usar ítems cuando estás vivo.",
						FontTypeNames.FONTTYPE_INFO);
				return;
			}

			if (!Declaraciones.UserList[UserIndex].Stats.MinSta > 0) {
				Protocol.WriteConsoleMsg(UserIndex,
						"Estás muy cansad"
								+ vb6.IIf(Declaraciones.UserList[UserIndex].Genero == eGenero.Hombre, "o", "a") + ".",
						FontTypeNames.FONTTYPE_INFO);
				return;
			}

			if (Declaraciones.ObjData[ObjIndex].proyectil == 1) {
				if (Declaraciones.UserList[UserIndex].Invent.Object[Slot].Equipped == 0) {
					Protocol.WriteConsoleMsg(UserIndex, "Antes de usar la herramienta deberías equipartela.",
							FontTypeNames.FONTTYPE_INFO);
					return;
				}
				/* 'Call WriteWorkRequestTarget(UserIndex, Proyectiles) */
				Protocol.WriteMultiMessage(UserIndex, eMessages.WorkRequestTarget, eSkill.Proyectiles);
			} else if (Declaraciones.UserList[UserIndex].flags.TargetObj == Declaraciones.Lena) {
				if (Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex == Declaraciones.DAGA) {
					if (Declaraciones.UserList[UserIndex].Invent.Object[Slot].Equipped == 0) {
						Protocol.WriteConsoleMsg(UserIndex, "Antes de usar la herramienta deberías equipartela.",
								FontTypeNames.FONTTYPE_INFO);
						return;
					}

					Trabajo.TratarDeHacerFogata(Declaraciones.UserList[UserIndex].flags.TargetObjMap,
							Declaraciones.UserList[UserIndex].flags.TargetObjX,
							Declaraciones.UserList[UserIndex].flags.TargetObjY, UserIndex);
				}
			} else {

				switch (ObjIndex) {

				case Declaraciones.CANA_PESCA:
				case Declaraciones.RED_PESCA:
				case Declaraciones.CANA_PESCA_NEWBIE:

					/* ' Lo tiene equipado? */
					if (Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex == ObjIndex) {
						/*
						 * 'Call WriteWorkRequestTarget(UserIndex, eSkill.Pesca)
						 */
						Protocol.WriteMultiMessage(UserIndex, eMessages.WorkRequestTarget, eSkill.Pesca);
					} else {
						Protocol.WriteConsoleMsg(UserIndex, "Debes tener equipada la herramienta para trabajar.",
								FontTypeNames.FONTTYPE_INFO);
					}

					break;

				case Declaraciones.HACHA_LENADOR:
				case Declaraciones.HACHA_LENA_ELFICA:
				case Declaraciones.HACHA_LENADOR_NEWBIE:

					/* ' Lo tiene equipado? */
					if (Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex == ObjIndex) {
						Protocol.WriteMultiMessage(UserIndex, eMessages.WorkRequestTarget, eSkill.Talar);
					} else {
						Protocol.WriteConsoleMsg(UserIndex, "Debes tener equipada la herramienta para trabajar.",
								FontTypeNames.FONTTYPE_INFO);
					}

					break;

				case Declaraciones.PIQUETE_MINERO:
				case Declaraciones.PIQUETE_MINERO_NEWBIE:

					/* ' Lo tiene equipado? */
					if (Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex == ObjIndex) {
						Protocol.WriteMultiMessage(UserIndex, eMessages.WorkRequestTarget, eSkill.Mineria);
					} else {
						Protocol.WriteConsoleMsg(UserIndex, "Debes tener equipada la herramienta para trabajar.",
								FontTypeNames.FONTTYPE_INFO);
					}

					break;

				case Declaraciones.MARTILLO_HERRERO:
				case Declaraciones.MARTILLO_HERRERO_NEWBIE:

					/* ' Lo tiene equipado? */
					if (Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex == ObjIndex) {
						Protocol.WriteMultiMessage(UserIndex, eMessages.WorkRequestTarget, eSkill.Herreria);
					} else {
						Protocol.WriteConsoleMsg(UserIndex, "Debes tener equipada la herramienta para trabajar.",
								FontTypeNames.FONTTYPE_INFO);
					}

					break;

				case Declaraciones.SERRUCHO_CARPINTERO:
				case Declaraciones.SERRUCHO_CARPINTERO_NEWBIE:

					/* ' Lo tiene equipado? */
					if (Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex == ObjIndex) {
						EnivarObjConstruibles(UserIndex);
						Protocol.WriteShowCarpenterForm(UserIndex);
					} else {
						Protocol.WriteConsoleMsg(UserIndex, "Debes tener equipada la herramienta para trabajar.",
								FontTypeNames.FONTTYPE_INFO);
					}

					/* ' Las herramientas no se pueden fundir */
					break;

				default:
					if (Declaraciones.ObjData[ObjIndex].SkHerreria > 0) {
						/* ' Solo objetos que pueda hacer el herrero */
						/*
						 * 'Call WriteWorkRequestTarget(UserIndex, FundirMetal)
						 */
						Protocol.WriteMultiMessage(UserIndex, eMessages.WorkRequestTarget, Declaraciones.FundirMetal);
					}
					break;
				}
			}

		case otPociones:
			if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
				Protocol.WriteConsoleMsg(UserIndex, "¡¡Estás muerto!! Sólo puedes usar ítems cuando estás vivo. ",
						FontTypeNames.FONTTYPE_INFO);
				return;
			}

			if (!modNuevoTimer.IntervaloPermiteGolpeUsar(UserIndex, false)) {
				Protocol.WriteConsoleMsg(UserIndex, "¡¡Debes esperar unos momentos para tomar otra poción!!",
						FontTypeNames.FONTTYPE_INFO);
				return;
			}

			Declaraciones.UserList[UserIndex].flags.TomoPocion = true;
			Declaraciones.UserList[UserIndex].flags.TipoPocion = Obj.TipoPocion;

			switch (Declaraciones.UserList[UserIndex].flags.TipoPocion) {

			/* 'Modif la agilidad */
			case 1:
				Declaraciones.UserList[UserIndex].flags.DuracionEfecto = Obj.DuracionEfecto;

				/* 'Usa el item */
				Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Agilidad] = Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Agilidad]
						+ Matematicas.RandomNumber(Obj.MinModificador, Obj.MaxModificador);
				if (Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Agilidad] > Declaraciones.MAXATRIBUTOS) {
					Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Agilidad] = Declaraciones.MAXATRIBUTOS;
				}
				if (Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Agilidad] > 2
						* Declaraciones.UserList[UserIndex].Stats.UserAtributosBackUP[Agilidad]) {
					Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Agilidad] = 2
							* Declaraciones.UserList[UserIndex].Stats.UserAtributosBackUP[Agilidad];
				}

				/* 'Quitamos del inv el item */
				QuitarUserInvItem(UserIndex, Slot, 1);

				/* ' Los admin invisibles solo producen sonidos a si mismos */
				if (Declaraciones.UserList[UserIndex].flags.AdminInvisible == 1) {
					TCP.EnviarDatosASlot(UserIndex, Protocol.PrepareMessagePlayWave(Declaraciones.SND_BEBER,
							Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y));
				} else {
					modSendData.SendData(SendTarget.ToPCArea, UserIndex,
							Protocol.PrepareMessagePlayWave(Declaraciones.SND_BEBER,
									Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y));
				}
				Protocol.WriteUpdateDexterity(UserIndex);

				/* 'Modif la fuerza */
				break;

			case 2:
				Declaraciones.UserList[UserIndex].flags.DuracionEfecto = Obj.DuracionEfecto;

				/* 'Usa el item */
				Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Fuerza] = Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Fuerza]
						+ Matematicas.RandomNumber(Obj.MinModificador, Obj.MaxModificador);
				if (Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Fuerza] > Declaraciones.MAXATRIBUTOS) {
					Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Fuerza] = Declaraciones.MAXATRIBUTOS;
				}
				if (Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Fuerza] > 2
						* Declaraciones.UserList[UserIndex].Stats.UserAtributosBackUP[Fuerza]) {
					Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Fuerza] = 2
							* Declaraciones.UserList[UserIndex].Stats.UserAtributosBackUP[Fuerza];
				}

				/* 'Quitamos del inv el item */
				QuitarUserInvItem(UserIndex, Slot, 1);

				/* ' Los admin invisibles solo producen sonidos a si mismos */
				if (Declaraciones.UserList[UserIndex].flags.AdminInvisible == 1) {
					TCP.EnviarDatosASlot(UserIndex, Protocol.PrepareMessagePlayWave(Declaraciones.SND_BEBER,
							Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y));
				} else {
					modSendData.SendData(SendTarget.ToPCArea, UserIndex,
							Protocol.PrepareMessagePlayWave(Declaraciones.SND_BEBER,
									Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y));
				}
				Protocol.WriteUpdateStrenght(UserIndex);

				/* 'Pocion roja, restaura HP */
				break;

			case 3:
				/* 'Usa el item */
				Declaraciones.UserList[UserIndex].Stats.MinHp = Declaraciones.UserList[UserIndex].Stats.MinHp
						+ Matematicas.RandomNumber(Obj.MinModificador, Obj.MaxModificador);
				if (Declaraciones.UserList[UserIndex].Stats.MinHp > Declaraciones.UserList[UserIndex].Stats.MaxHp) {
					Declaraciones.UserList[UserIndex].Stats.MinHp = Declaraciones.UserList[UserIndex].Stats.MaxHp;
				}

				/* 'Quitamos del inv el item */
				QuitarUserInvItem(UserIndex, Slot, 1);

				/* ' Los admin invisibles solo producen sonidos a si mismos */
				if (Declaraciones.UserList[UserIndex].flags.AdminInvisible == 1) {
					TCP.EnviarDatosASlot(UserIndex, Protocol.PrepareMessagePlayWave(Declaraciones.SND_BEBER,
							Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y));
				} else {
					modSendData.SendData(SendTarget.ToPCArea, UserIndex,
							Protocol.PrepareMessagePlayWave(Declaraciones.SND_BEBER,
									Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y));
				}

				/* 'Pocion azul, restaura MANA */
				break;

			case 4:
				/* 'Usa el item */
				/* 'nuevo calculo para recargar mana */
				Declaraciones.UserList[UserIndex].Stats.MinMAN = Declaraciones.UserList[UserIndex].Stats.MinMAN
						+ Matematicas.Porcentaje(Declaraciones.UserList[UserIndex].Stats.MaxMAN, 4)
						+ Declaraciones.UserList[UserIndex].Stats.ELV / 2
						+ 40 / Declaraciones.UserList[UserIndex].Stats.ELV;
				if (Declaraciones.UserList[UserIndex].Stats.MinMAN > Declaraciones.UserList[UserIndex].Stats.MaxMAN) {
					Declaraciones.UserList[UserIndex].Stats.MinMAN = Declaraciones.UserList[UserIndex].Stats.MaxMAN;
				}

				/* 'Quitamos del inv el item */
				QuitarUserInvItem(UserIndex, Slot, 1);

				/* ' Los admin invisibles solo producen sonidos a si mismos */
				if (Declaraciones.UserList[UserIndex].flags.AdminInvisible == 1) {
					TCP.EnviarDatosASlot(UserIndex, Protocol.PrepareMessagePlayWave(Declaraciones.SND_BEBER,
							Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y));
				} else {
					modSendData.SendData(SendTarget.ToPCArea, UserIndex,
							Protocol.PrepareMessagePlayWave(Declaraciones.SND_BEBER,
									Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y));
				}

				/* ' Pocion violeta */
				break;

			case 5:
				if (Declaraciones.UserList[UserIndex].flags.Envenenado == 1) {
					Declaraciones.UserList[UserIndex].flags.Envenenado = 0;
					Protocol.WriteConsoleMsg(UserIndex, "Te has curado del envenenamiento.",
							FontTypeNames.FONTTYPE_INFO);
				}
				/* 'Quitamos del inv el item */
				QuitarUserInvItem(UserIndex, Slot, 1);

				/* ' Los admin invisibles solo producen sonidos a si mismos */
				if (Declaraciones.UserList[UserIndex].flags.AdminInvisible == 1) {
					TCP.EnviarDatosASlot(UserIndex, Protocol.PrepareMessagePlayWave(Declaraciones.SND_BEBER,
							Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y));
				} else {
					modSendData.SendData(SendTarget.ToPCArea, UserIndex,
							Protocol.PrepareMessagePlayWave(Declaraciones.SND_BEBER,
									Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y));
				}

				/* ' Pocion Negra */
				break;

			case 6:
				if (Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) {
					QuitarUserInvItem(UserIndex, Slot, 1);
					UsUaRiOs.UserDie(UserIndex);
					Protocol.WriteConsoleMsg(UserIndex, "Sientes un gran mareo y pierdes el conocimiento.",
							FontTypeNames.FONTTYPE_FIGHT);
				}
				break;
			}
			Protocol.WriteUpdateUserStats(UserIndex);
			UpdateUserInv(false, UserIndex, Slot);

		case otBebidas:
			if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
				Protocol.WriteConsoleMsg(UserIndex, "¡¡Estás muerto!! Sólo puedes usar ítems cuando estás vivo.",
						FontTypeNames.FONTTYPE_INFO);
				return;
			}
			Declaraciones.UserList[UserIndex].Stats.MinAGU = Declaraciones.UserList[UserIndex].Stats.MinAGU
					+ Obj.MinSed;
			if (Declaraciones.UserList[UserIndex].Stats.MinAGU > Declaraciones.UserList[UserIndex].Stats.MaxAGU) {
				Declaraciones.UserList[UserIndex].Stats.MinAGU = Declaraciones.UserList[UserIndex].Stats.MaxAGU;
			}
			Declaraciones.UserList[UserIndex].flags.Sed = 0;
			Protocol.WriteUpdateHungerAndThirst(UserIndex);

			/* 'Quitamos del inv el item */
			QuitarUserInvItem(UserIndex, Slot, 1);

			/* ' Los admin invisibles solo producen sonidos a si mismos */
			if (Declaraciones.UserList[UserIndex].flags.AdminInvisible == 1) {
				TCP.EnviarDatosASlot(UserIndex, Protocol.PrepareMessagePlayWave(Declaraciones.SND_BEBER,
						Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y));
			} else {
				modSendData.SendData(SendTarget.ToPCArea, UserIndex,
						Protocol.PrepareMessagePlayWave(Declaraciones.SND_BEBER,
								Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y));
			}

			UpdateUserInv(false, UserIndex, Slot);

			break;

		case otLlaves:
			if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
				Protocol.WriteConsoleMsg(UserIndex, "¡¡Estás muerto!! Sólo puedes usar ítems cuando estás vivo.",
						FontTypeNames.FONTTYPE_INFO);
				return;
			}

			if (Declaraciones.UserList[UserIndex].flags.TargetObj == 0) {
				return;
			}
			TargObj = Declaraciones.ObjData[Declaraciones.UserList[UserIndex].flags.TargetObj];
			/* '¿El objeto clickeado es una puerta? */
			if (TargObj.OBJType == eOBJType.otPuertas) {
				/* '¿Esta cerrada? */
				if (TargObj.Cerrada == 1) {
					/* '¿Cerrada con llave? */
					if (TargObj.Llave > 0) {
						if (TargObj.clave == Obj.clave) {

							Declaraciones.MapData[Declaraciones.UserList[UserIndex].flags.TargetObjMap][Declaraciones.UserList[UserIndex].flags.TargetObjX][Declaraciones.UserList[UserIndex].flags.TargetObjY].ObjInfo.ObjIndex = Declaraciones.ObjData[Declaraciones.MapData[Declaraciones.UserList[UserIndex].flags.TargetObjMap][Declaraciones.UserList[UserIndex].flags.TargetObjX][Declaraciones.UserList[UserIndex].flags.TargetObjY].ObjInfo.ObjIndex].IndexCerrada;
							Declaraciones.UserList[UserIndex].flags.TargetObj = Declaraciones.MapData[Declaraciones.UserList[UserIndex].flags.TargetObjMap][Declaraciones.UserList[UserIndex].flags.TargetObjX][Declaraciones.UserList[UserIndex].flags.TargetObjY].ObjInfo.ObjIndex;
							Protocol.WriteConsoleMsg(UserIndex, "Has abierto la puerta.", FontTypeNames.FONTTYPE_INFO);
							return;
						} else {
							Protocol.WriteConsoleMsg(UserIndex, "La llave no sirve.", FontTypeNames.FONTTYPE_INFO);
							return;
						}
					} else {
						if (TargObj.clave == Obj.clave) {
							Declaraciones.MapData[Declaraciones.UserList[UserIndex].flags.TargetObjMap][Declaraciones.UserList[UserIndex].flags.TargetObjX][Declaraciones.UserList[UserIndex].flags.TargetObjY].ObjInfo.ObjIndex = Declaraciones.ObjData[Declaraciones.MapData[Declaraciones.UserList[UserIndex].flags.TargetObjMap][Declaraciones.UserList[UserIndex].flags.TargetObjX][Declaraciones.UserList[UserIndex].flags.TargetObjY].ObjInfo.ObjIndex].IndexCerradaLlave;
							Protocol.WriteConsoleMsg(UserIndex, "Has cerrado con llave la puerta.",
									FontTypeNames.FONTTYPE_INFO);
							Declaraciones.UserList[UserIndex].flags.TargetObj = Declaraciones.MapData[Declaraciones.UserList[UserIndex].flags.TargetObjMap][Declaraciones.UserList[UserIndex].flags.TargetObjX][Declaraciones.UserList[UserIndex].flags.TargetObjY].ObjInfo.ObjIndex;
							return;
						} else {
							Protocol.WriteConsoleMsg(UserIndex, "La llave no sirve.", FontTypeNames.FONTTYPE_INFO);
							return;
						}
					}
				} else {
					Protocol.WriteConsoleMsg(UserIndex, "No está cerrada.", FontTypeNames.FONTTYPE_INFO);
					return;
				}
			}

			break;

		case otBotellaVacia:
			if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
				Protocol.WriteConsoleMsg(UserIndex, "¡¡Estás muerto!! Sólo puedes usar ítems cuando estás vivo.",
						FontTypeNames.FONTTYPE_INFO);
				return;
			}
			if (!General.HayAgua(Declaraciones.UserList[UserIndex].Pos.Map,
					Declaraciones.UserList[UserIndex].flags.TargetX, Declaraciones.UserList[UserIndex].flags.TargetY)) {
				Protocol.WriteConsoleMsg(UserIndex, "No hay agua allí.", FontTypeNames.FONTTYPE_INFO);
				return;
			}
			MiObj.Amount = 1;
			MiObj.ObjIndex = Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex].IndexAbierta;
			QuitarUserInvItem(UserIndex, Slot, 1);
			if (!MeterItemEnInventario(UserIndex, MiObj)) {
				InvNpc.TirarItemAlPiso(Declaraciones.UserList[UserIndex].Pos, MiObj);
			}

			UpdateUserInv(false, UserIndex, Slot);

			break;

		case otBotellaLlena:
			if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
				Protocol.WriteConsoleMsg(UserIndex, "¡¡Estás muerto!! Sólo puedes usar ítems cuando estás vivo.",
						FontTypeNames.FONTTYPE_INFO);
				return;
			}
			Declaraciones.UserList[UserIndex].Stats.MinAGU = Declaraciones.UserList[UserIndex].Stats.MinAGU
					+ Obj.MinSed;
			if (Declaraciones.UserList[UserIndex].Stats.MinAGU > Declaraciones.UserList[UserIndex].Stats.MaxAGU) {
				Declaraciones.UserList[UserIndex].Stats.MinAGU = Declaraciones.UserList[UserIndex].Stats.MaxAGU;
			}
			Declaraciones.UserList[UserIndex].flags.Sed = 0;
			Protocol.WriteUpdateHungerAndThirst(UserIndex);
			MiObj.Amount = 1;
			MiObj.ObjIndex = Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex].IndexCerrada;
			QuitarUserInvItem(UserIndex, Slot, 1);
			if (!MeterItemEnInventario(UserIndex, MiObj)) {
				InvNpc.TirarItemAlPiso(Declaraciones.UserList[UserIndex].Pos, MiObj);
			}

			UpdateUserInv(false, UserIndex, Slot);

			break;

		case otPergaminos:
			if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
				Protocol.WriteConsoleMsg(UserIndex, "¡¡Estás muerto!! Sólo puedes usar ítems cuando estás vivo.",
						FontTypeNames.FONTTYPE_INFO);
				return;
			}

			if (Declaraciones.UserList[UserIndex].Stats.MaxMAN > 0) {
				if (Declaraciones.UserList[UserIndex].flags.Hambre == 0
						&& Declaraciones.UserList[UserIndex].flags.Sed == 0) {
					modHechizos.AgregarHechizo(UserIndex, Slot);
					UpdateUserInv(false, UserIndex, Slot);
				} else {
					Protocol.WriteConsoleMsg(UserIndex, "Estás demasiado hambriento y sediento.",
							FontTypeNames.FONTTYPE_INFO);
				}
			} else {
				Protocol.WriteConsoleMsg(UserIndex, "No tienes conocimientos de las Artes Arcanas.",
						FontTypeNames.FONTTYPE_INFO);
			}
			break;

		case otMinerales:
			if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
				Protocol.WriteConsoleMsg(UserIndex, "¡¡Estás muerto!! Sólo puedes usar ítems cuando estás vivo.",
						FontTypeNames.FONTTYPE_INFO);
				return;
			}
			/* 'Call WriteWorkRequestTarget(UserIndex, FundirMetal) */
			Protocol.WriteMultiMessage(UserIndex, eMessages.WorkRequestTarget, Declaraciones.FundirMetal);

			break;

		case otInstrumentos:
			if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
				Protocol.WriteConsoleMsg(UserIndex, "¡¡Estás muerto!! Sólo puedes usar ítems cuando estás vivo.",
						FontTypeNames.FONTTYPE_INFO);
				return;
			}

			/* '¿Es el Cuerno Real? */
			if (Obj.Real) {
				if (FaccionPuedeUsarItem(UserIndex, ObjIndex)) {
					if (Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].Pk == false) {
						Protocol.WriteConsoleMsg(UserIndex, "No hay peligro aquí. Es zona segura.",
								FontTypeNames.FONTTYPE_INFO);
						return;
					}

					/*
					 * ' Los admin invisibles solo producen sonidos a si mismos
					 */
					if (Declaraciones.UserList[UserIndex].flags.AdminInvisible == 1) {
						TCP.EnviarDatosASlot(UserIndex, Protocol.PrepareMessagePlayWave(Obj.Snd1,
								Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y));
					} else {
						modSendData.AlertarFaccionarios(UserIndex);
						modSendData.SendData(SendTarget.toMap, Declaraciones.UserList[UserIndex].Pos.Map,
								Protocol.PrepareMessagePlayWave(Obj.Snd1, Declaraciones.UserList[UserIndex].Pos.X,
										Declaraciones.UserList[UserIndex].Pos.Y));
					}

					return;
				} else {
					Protocol.WriteConsoleMsg(UserIndex, "Sólo miembros del ejército real pueden usar este cuerno.",
							FontTypeNames.FONTTYPE_INFO);
					return;
				}
				/* '¿Es el Cuerno Legión? */
			} else if (Obj.Caos) {
				if (FaccionPuedeUsarItem(UserIndex, ObjIndex)) {
					if (Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].Pk == false) {
						Protocol.WriteConsoleMsg(UserIndex, "No hay peligro aquí. Es zona segura.",
								FontTypeNames.FONTTYPE_INFO);
						return;
					}

					/*
					 * ' Los admin invisibles solo producen sonidos a si mismos
					 */
					if (Declaraciones.UserList[UserIndex].flags.AdminInvisible == 1) {
						TCP.EnviarDatosASlot(UserIndex, Protocol.PrepareMessagePlayWave(Obj.Snd1,
								Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y));
					} else {
						modSendData.AlertarFaccionarios(UserIndex);
						modSendData.SendData(SendTarget.toMap, Declaraciones.UserList[UserIndex].Pos.Map,
								Protocol.PrepareMessagePlayWave(Obj.Snd1, Declaraciones.UserList[UserIndex].Pos.X,
										Declaraciones.UserList[UserIndex].Pos.Y));
					}

					return;
				} else {
					Protocol.WriteConsoleMsg(UserIndex, "Sólo miembros de la legión oscura pueden usar este cuerno.",
							FontTypeNames.FONTTYPE_INFO);
					return;
				}
			}
			/* 'Si llega aca es porque es o Laud o Tambor o Flauta */
			/* ' Los admin invisibles solo producen sonidos a si mismos */
			if (Declaraciones.UserList[UserIndex].flags.AdminInvisible == 1) {
				TCP.EnviarDatosASlot(UserIndex, Protocol.PrepareMessagePlayWave(Obj.Snd1,
						Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y));
			} else {
				modSendData.SendData(SendTarget.ToPCArea, UserIndex, Protocol.PrepareMessagePlayWave(Obj.Snd1,
						Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y));
			}

			break;

		case otBarcos:
			/*
			 * 'Verifica si esta aproximado al agua antes de permitirle navegar
			 */
			if (Declaraciones.UserList[UserIndex].Stats.ELV < 25) {
				/* ' Solo pirata y trabajador pueden navegar antes */
				if (Declaraciones.UserList[UserIndex].clase != eClass.Worker
						&& Declaraciones.UserList[UserIndex].clase != eClass.Pirat) {
					Protocol.WriteConsoleMsg(UserIndex, "Para recorrer los mares debes ser nivel 25 o superior.",
							FontTypeNames.FONTTYPE_INFO);
					return;
				} else {
					/* ' Pero a partir de 20 */
					if (Declaraciones.UserList[UserIndex].Stats.ELV < 20) {

						if (Declaraciones.UserList[UserIndex].clase == eClass.Worker
								&& Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Pesca] != 100) {
							Protocol.WriteConsoleMsg(UserIndex,
									"Para recorrer los mares debes ser nivel 20 y además tu skill en pesca debe ser 100.",
									FontTypeNames.FONTTYPE_INFO);
						} else {
							Protocol.WriteConsoleMsg(UserIndex,
									"Para recorrer los mares debes ser nivel 20 o superior.",
									FontTypeNames.FONTTYPE_INFO);
						}

						return;
					} else {
						/*
						 * ' Esta entre 20 y 25, si es trabajador necesita tener
						 * 100 en pesca
						 */
						if (Declaraciones.UserList[UserIndex].clase == eClass.Worker) {
							if (Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Pesca] != 100) {
								Protocol.WriteConsoleMsg(UserIndex,
										"Para recorrer los mares debes ser nivel 20 o superior y además tu skill en pesca debe ser 100.",
										FontTypeNames.FONTTYPE_INFO);
								return;
							}
						}

					}
				}
			}

			if (((Extra.LegalPos(Declaraciones.UserList[UserIndex].Pos.Map, Declaraciones.UserList[UserIndex].Pos.X - 1,
					Declaraciones.UserList[UserIndex].Pos.Y, true, false)
					|| Extra.LegalPos(Declaraciones.UserList[UserIndex].Pos.Map,
							Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y - 1, true,
							false)
					|| Extra.LegalPos(Declaraciones.UserList[UserIndex].Pos.Map,
							Declaraciones.UserList[UserIndex].Pos.X + 1, Declaraciones.UserList[UserIndex].Pos.Y, true,
							false)
					|| Extra.LegalPos(Declaraciones.UserList[UserIndex].Pos.Map,
							Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y + 1, true,
							false))
					&& Declaraciones.UserList[UserIndex].flags.Navegando == 0)
					|| Declaraciones.UserList[UserIndex].flags.Navegando == 1) {
				Trabajo.DoNavega(UserIndex, Obj, Slot);
			} else {
				Protocol.WriteConsoleMsg(UserIndex, "¡Debes aproximarte al agua para usar el barco!",
						FontTypeNames.FONTTYPE_INFO);
			}

			break;
		}

	}

	static void EnivarArmasConstruibles(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		Protocol.WriteBlacksmithWeapons(UserIndex);
	}

	static void EnivarObjConstruibles(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		Protocol.WriteCarpenterObjects(UserIndex);
	}

	static void EnivarArmadurasConstruibles(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		Protocol.WriteBlacksmithArmors(UserIndex);
	}

	static void TirarTodo(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		if (Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].trigger == 6) {
			return;
		}

		TirarTodosLosItems(UserIndex);

		int Cantidad;
		Cantidad = Declaraciones.UserList[UserIndex].Stats.GLD
				- vb6.CLng(Declaraciones.UserList[UserIndex].Stats.ELV) * 10000;

		if (Cantidad > 0) {
			TirarOro(Cantidad, UserIndex);
		}

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en TirarTodo. Error: " + Err.Number + " - " + Err.description);
	}

	static boolean ItemSeCae(int index) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		retval = (Declaraciones.ObjData[index].Real != 1 || Declaraciones.ObjData[index].NoSeCae == 0)
				&& (Declaraciones.ObjData[index].Caos != 1 || Declaraciones.ObjData[index].NoSeCae == 0)
				&& Declaraciones.ObjData[index].OBJType != eOBJType.otLlaves
				&& Declaraciones.ObjData[index].OBJType != eOBJType.otBarcos
				&& Declaraciones.ObjData[index].NoSeCae == 0 && Declaraciones.ObjData[index].Intransferible == 0;

		return retval;
	}

	static void TirarTodosLosItems(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 12/01/2010 (ZaMa) */
		/*
		 * '12/01/2010: ZaMa - Ahora los piratas no explotan items solo si estan
		 * entre 20 y 25
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		int i;
		Declaraciones.WorldPos NuevaPos;
		Declaraciones.Obj MiObj;
		int ItemIndex;
		boolean DropAgua;

		for (i = (1); i <= (Declaraciones.UserList[UserIndex].CurrentInventorySlots); i++) {
			ItemIndex = Declaraciones.UserList[UserIndex].Invent.Object[i].ObjIndex;
			if (ItemIndex > 0) {
				if (ItemSeCae(ItemIndex)) {
					NuevaPos.X = 0;
					NuevaPos.Y = 0;

					/* 'Creo el Obj */
					MiObj.Amount = Declaraciones.UserList[UserIndex].Invent.Object[i].Amount;
					MiObj.ObjIndex = ItemIndex;

					DropAgua = true;
					/* ' Es pirata? */
					if (Declaraciones.UserList[UserIndex].clase == eClass.Pirat) {
						/* ' Si tiene galeon equipado */
						if (Declaraciones.UserList[UserIndex].Invent.BarcoObjIndex == 476) {
							/*
							 * ' Limitación por nivel, después dropea
							 * normalmente
							 */
							if (Declaraciones.UserList[UserIndex].Stats.ELV == 20) {
								/* ' No dropea en agua */
								DropAgua = false;
							}
						}
					}

					UsUaRiOs.Tilelibre(Declaraciones.UserList[UserIndex].Pos, NuevaPos, MiObj, DropAgua, true);

					if (NuevaPos.X != 0 && NuevaPos.Y != 0) {
						DropObj(UserIndex, i, Declaraciones.MAX_INVENTORY_OBJS, NuevaPos.Map, NuevaPos.X, NuevaPos.Y);
					}
				}
			}
		}

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en TirarTodosLosItems. Error: " + Err.Number + " - " + Err.description);
	}

	static boolean ItemNewbie(int ItemIndex) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		if (ItemIndex < 1 || ItemIndex > vb6.UBound(Declaraciones.ObjData)) {
			return retval;
		}

		retval = Declaraciones.ObjData[ItemIndex].Newbie == 1;
		return retval;
	}

	static void TirarTodosLosItemsNoNewbies(int UserIndex) {
 /* '*************************************************** */
 /* 'Author: Unknown */
 /* 'Last Modification: 23/11/2009 */
 /* '07/11/09: Pato - Fix bug #2819911 */
 /* '23/11/2009: ZaMa - Optimizacion de codigo. */
 /* '*************************************************** */
 int i;
 Declaraciones.WorldPos NuevaPos;
 Declaraciones.Obj MiObj;
 int ItemIndex;
 
  if (Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].trigger == 6) {
  return;
  }
  
   for (i = (1); i <= (Declaraciones.UserList[UserIndex].CurrentInventorySlots); i++) {
   ItemIndex = Declaraciones.UserList[UserIndex].Invent.Object[i].ObjIndex;
    if (ItemIndex>0) {
     if (ItemSeCae(ItemIndex) && !ItemNewbie(ItemIndex)) {
     NuevaPos.X = 0;
     NuevaPos.Y = 0;
     
     /* 'Creo MiObj */
     MiObj.Amount = Declaraciones.UserList[UserIndex].Invent.Object[i].Amount;
     MiObj.ObjIndex = ItemIndex;
     /* 'Pablo (ToxicWaste) 24/01/2007 */
     /* 'Tira los Items no newbies en todos lados. */
     UsUaRiOs.Tilelibre().Pos, NuevaPos, MiObj, true, true();
      if (NuevaPos.X != 0 && NuevaPos.Y != 0) {
      DropObj(UserIndex, i, Declaraciones.MAX_INVENTORY_OBJS, NuevaPos.Map, NuevaPos.X, NuevaPos.Y);
     }
    }
   }
  }
 
}

	static void TirarTodosLosItemsEnMochila(int UserIndex) {
 /* '*************************************************** */
 /* 'Author: Unknown */
 /* 'Last Modification: 12/01/09 (Budi) */
 /* '*************************************************** */
 int i;
 Declaraciones.WorldPos NuevaPos;
 Declaraciones.Obj MiObj;
 int ItemIndex;
 
  if (Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].trigger == 6) {
  return;
  }
  
   for (i = (Declaraciones.MAX_NORMAL_INVENTORY_SLOTS+1); i <= (Declaraciones.UserList[UserIndex].CurrentInventorySlots); i++) {
   ItemIndex = Declaraciones.UserList[UserIndex].Invent.Object[i].ObjIndex;
    if (ItemIndex>0) {
     if (ItemSeCae(ItemIndex)) {
     NuevaPos.X = 0;
     NuevaPos.Y = 0;
     
     /* 'Creo MiObj */
     MiObj.Amount = Declaraciones.UserList[UserIndex].Invent.Object[i].Amount;
     MiObj.ObjIndex = ItemIndex;
     UsUaRiOs.Tilelibre().Pos, NuevaPos, MiObj, true, true();
      if (NuevaPos.X != 0 && NuevaPos.Y != 0) {
      DropObj(UserIndex, i, Declaraciones.MAX_INVENTORY_OBJS, NuevaPos.Map, NuevaPos.X, NuevaPos.Y);
     }
    }
   }
  }
 
}

	static eOBJType getObjType(int ObjIndex) {
		eOBJType retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		if (ObjIndex > 0) {
			retval = Declaraciones.ObjData[ObjIndex].OBJType;
		}

		return retval;
	}

	static void moveItem(int UserIndex, int originalSlot, int newSlot) {

		Declaraciones.UserOBJ tmpObj;
		int newObjIndex;
		int originalObjIndex;
		if ((originalSlot <= 0) || (newSlot <= 0)) {
			return;
		}

		if ((originalSlot > Declaraciones.UserList[UserIndex].CurrentInventorySlots)
				|| (newSlot > Declaraciones.UserList[UserIndex].CurrentInventorySlots)) {
			return;
		}

		tmpObj = Declaraciones.UserList[UserIndex].Invent.Object[originalSlot];
		Declaraciones.UserList[UserIndex].Invent.Object[originalSlot] = Declaraciones.UserList[UserIndex].Invent.Object[newSlot];
		Declaraciones.UserList[UserIndex].Invent.Object[newSlot] = tmpObj;

		/* 'Viva VB6 y sus putas deficiencias. */
		if (Declaraciones.UserList[UserIndex].Invent.AnilloEqpSlot == originalSlot) {
			Declaraciones.UserList[UserIndex].Invent.AnilloEqpSlot = newSlot;
		} else if (Declaraciones.UserList[UserIndex].Invent.AnilloEqpSlot == newSlot) {
			Declaraciones.UserList[UserIndex].Invent.AnilloEqpSlot = originalSlot;
		}

		if (Declaraciones.UserList[UserIndex].Invent.ArmourEqpSlot == originalSlot) {
			Declaraciones.UserList[UserIndex].Invent.ArmourEqpSlot = newSlot;
		} else if (Declaraciones.UserList[UserIndex].Invent.ArmourEqpSlot == newSlot) {
			Declaraciones.UserList[UserIndex].Invent.ArmourEqpSlot = originalSlot;
		}

		if (Declaraciones.UserList[UserIndex].Invent.BarcoSlot == originalSlot) {
			Declaraciones.UserList[UserIndex].Invent.BarcoSlot = newSlot;
		} else if (Declaraciones.UserList[UserIndex].Invent.BarcoSlot == newSlot) {
			Declaraciones.UserList[UserIndex].Invent.BarcoSlot = originalSlot;
		}

		if (Declaraciones.UserList[UserIndex].Invent.CascoEqpSlot == originalSlot) {
			Declaraciones.UserList[UserIndex].Invent.CascoEqpSlot = newSlot;
		} else if (Declaraciones.UserList[UserIndex].Invent.CascoEqpSlot == newSlot) {
			Declaraciones.UserList[UserIndex].Invent.CascoEqpSlot = originalSlot;
		}

		if (Declaraciones.UserList[UserIndex].Invent.EscudoEqpSlot == originalSlot) {
			Declaraciones.UserList[UserIndex].Invent.EscudoEqpSlot = newSlot;
		} else if (Declaraciones.UserList[UserIndex].Invent.EscudoEqpSlot == newSlot) {
			Declaraciones.UserList[UserIndex].Invent.EscudoEqpSlot = originalSlot;
		}

		if (Declaraciones.UserList[UserIndex].Invent.MochilaEqpSlot == originalSlot) {
			Declaraciones.UserList[UserIndex].Invent.MochilaEqpSlot = newSlot;
		} else if (Declaraciones.UserList[UserIndex].Invent.MochilaEqpSlot == newSlot) {
			Declaraciones.UserList[UserIndex].Invent.MochilaEqpSlot = originalSlot;
		}

		if (Declaraciones.UserList[UserIndex].Invent.MunicionEqpSlot == originalSlot) {
			Declaraciones.UserList[UserIndex].Invent.MunicionEqpSlot = newSlot;
		} else if (Declaraciones.UserList[UserIndex].Invent.MunicionEqpSlot == newSlot) {
			Declaraciones.UserList[UserIndex].Invent.MunicionEqpSlot = originalSlot;
		}

		if (Declaraciones.UserList[UserIndex].Invent.WeaponEqpSlot == originalSlot) {
			Declaraciones.UserList[UserIndex].Invent.WeaponEqpSlot = newSlot;
		} else if (Declaraciones.UserList[UserIndex].Invent.WeaponEqpSlot == newSlot) {
			Declaraciones.UserList[UserIndex].Invent.WeaponEqpSlot = originalSlot;
		}

		UpdateUserInv(false, UserIndex, originalSlot);
		UpdateUserInv(false, UserIndex, newSlot);
	}

}