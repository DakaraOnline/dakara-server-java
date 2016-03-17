/*  AUTOMATICALLY CONVERTED FILE  */

/* 
 * Este archivo fue convertido automaticamente, por un script, desde el 
 * código fuente original de Visual Basic 6.
 */

/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"modBanco"')] */
/* '************************************************************** */
/* ' modBanco.bas - Handles the character's bank accounts. */
/* ' */
/* ' Implemented by Kevin Birmingham (NEB) */
/* ' kbneb@hotmail.com */
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

import enums.*;

public class modBanco {

	public static void IniciarDeposito(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		/* 'Hacemos un Update del inventario del usuario */
		UpdateBanUserInv(true, UserIndex, 0);
		/* 'Actualizamos el dinero */
		Protocol.WriteUpdateUserStats(UserIndex);
		/*
		 * 'Mostramos la ventana pa' comerciar y ver ladear la osamenta. jajaja
		 */
		Protocol.WriteBankInit(UserIndex);
		Declaraciones.UserList[UserIndex].flags.Comerciando = true;

		/* FIXME: ErrHandler : */

	}

	public static void SendBanObj(int UserIndex, int Slot, Declaraciones.UserOBJ Object) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		Declaraciones.UserList[UserIndex].BancoInvent.Object[Slot] = Object;

		Protocol.WriteChangeBankSlot(UserIndex, Slot);

	}

	public static void UpdateBanUserInv(boolean UpdateAll, int UserIndex, int Slot) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		Declaraciones.UserOBJ NullObj;
		int LoopC = 0;

		/* 'Actualiza un solo slot */
		if (! /* FIXME */UpdateAll) {
			/* 'Actualiza el inventario */
			if (Declaraciones.UserList[UserIndex].BancoInvent.Object[Slot].ObjIndex > 0) {
				SendBanObj(UserIndex, Slot, Declaraciones.UserList[UserIndex].BancoInvent.Object[Slot]);
			} else {
				SendBanObj(UserIndex, Slot, NullObj);
			}
		} else {
			/* 'Actualiza todos los slots */
			for (LoopC = (1); LoopC <= (Declaraciones.MAX_BANCOINVENTORY_SLOTS); LoopC++) {
				/* 'Actualiza el inventario */
				if (Declaraciones.UserList[UserIndex].BancoInvent.Object[LoopC].ObjIndex > 0) {
					SendBanObj(UserIndex, LoopC, Declaraciones.UserList[UserIndex].BancoInvent.Object[LoopC]);
				} else {
					SendBanObj(UserIndex, LoopC, NullObj);
				}
			}
		}

	}

	public static void UserRetiraItem(int UserIndex, int i, int Cantidad) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int ObjIndex = 0;

		if (Cantidad < 1) {
			return;
		}

		Protocol.WriteUpdateUserStats(UserIndex);

		if (Declaraciones.UserList[UserIndex].BancoInvent.Object[i].Amount > 0) {

			if (Cantidad > Declaraciones.UserList[UserIndex].BancoInvent.Object[i].Amount) {
				Cantidad = Declaraciones.UserList[UserIndex].BancoInvent.Object[i].Amount;
			}

			ObjIndex = Declaraciones.UserList[UserIndex].BancoInvent.Object[i].ObjIndex;

			/* 'Agregamos el obj que compro al inventario */
			UserReciveObj(UserIndex, vb6.CInt(i), Cantidad);

			if (Declaraciones.ObjData[ObjIndex].Log == 1) {
				General.LogDesarrollo(Declaraciones.UserList[UserIndex].Name + " retiró " + Cantidad + " "
						+ Declaraciones.ObjData[ObjIndex].Name + "[" + ObjIndex + "]");
			}

			/* 'Actualizamos el inventario del usuario */
			InvUsuario.UpdateUserInv(true, UserIndex, 0);
			/* 'Actualizamos el banco */
			UpdateBanUserInv(true, UserIndex, 0);
		}

		/* 'Actualizamos la ventana de comercio */
		UpdateVentanaBanco(UserIndex);

		/* FIXME: ErrHandler : */

	}

	public static void UserReciveObj(int UserIndex, int ObjIndex, int Cantidad) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int Slot = 0;
		int obji = 0;

		if (Declaraciones.UserList[UserIndex].BancoInvent.Object[ObjIndex].Amount <= 0) {
			return;
		}

		obji = Declaraciones.UserList[UserIndex].BancoInvent.Object[ObjIndex].ObjIndex;

		/* '¿Ya tiene un objeto de este tipo? */
		Slot = 1;
		while (!(Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex == obji
				&& Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount
						+ Cantidad <= Declaraciones.MAX_INVENTORY_OBJS)) {

			Slot = Slot + 1;
			if (Slot > Declaraciones.UserList[UserIndex].CurrentInventorySlots) {
				break; /* FIXME: EXIT DO */
			}
		}

		/* 'Sino se fija por un slot vacio */
		if (Slot > Declaraciones.UserList[UserIndex].CurrentInventorySlots) {
			Slot = 1;
			while (!(Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex == 0)) {
				Slot = Slot + 1;

				if (Slot > Declaraciones.UserList[UserIndex].CurrentInventorySlots) {
					Protocol.WriteConsoleMsg(UserIndex, "No podés tener mas objetos.", FontTypeNames.FONTTYPE_INFO);
					return;
				}
			}
			Declaraciones.UserList[UserIndex].Invent.NroItems = Declaraciones.UserList[UserIndex].Invent.NroItems + 1;
		}

		/* 'Mete el obj en el slot */
		if (Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount
				+ Cantidad <= Declaraciones.MAX_INVENTORY_OBJS) {
			/* 'Menor que MAX_INV_OBJS */
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex = obji;
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount = Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount
					+ Cantidad;

			QuitarBancoInvItem(UserIndex, vb6.CByte(ObjIndex), Cantidad);
		} else {
			Protocol.WriteConsoleMsg(UserIndex, "No podés tener mas objetos.", FontTypeNames.FONTTYPE_INFO);
		}

	}

	public static void QuitarBancoInvItem(int UserIndex, int Slot, int Cantidad) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int ObjIndex = 0;

		ObjIndex = Declaraciones.UserList[UserIndex].BancoInvent.Object[Slot].ObjIndex;

		/* 'Quita un Obj */

		Declaraciones.UserList[UserIndex].BancoInvent.Object[Slot].Amount = Declaraciones.UserList[UserIndex].BancoInvent.Object[Slot].Amount
				- Cantidad;

		if (Declaraciones.UserList[UserIndex].BancoInvent.Object[Slot].Amount <= 0) {
			Declaraciones.UserList[UserIndex].BancoInvent.NroItems = Declaraciones.UserList[UserIndex].BancoInvent.NroItems
					- 1;
			Declaraciones.UserList[UserIndex].BancoInvent.Object[Slot].ObjIndex = 0;
			Declaraciones.UserList[UserIndex].BancoInvent.Object[Slot].Amount = 0;
		}

	}

	public static void UpdateVentanaBanco(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		Protocol.WriteBankOK(UserIndex);
	}

	public static void UserDepositaItem(int UserIndex, int Item, int Cantidad) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int ObjIndex = 0;

		if (Declaraciones.UserList[UserIndex].Invent.Object[Item].Amount > 0 && Cantidad > 0) {

			if (Cantidad > Declaraciones.UserList[UserIndex].Invent.Object[Item].Amount) {
				Cantidad = Declaraciones.UserList[UserIndex].Invent.Object[Item].Amount;
			}

			ObjIndex = Declaraciones.UserList[UserIndex].Invent.Object[Item].ObjIndex;

			/* 'Agregamos el obj que deposita al banco */
			UserDejaObj(UserIndex, vb6.CInt(Item), Cantidad);

			if (Declaraciones.ObjData[ObjIndex].Log == 1) {
				General.LogDesarrollo(Declaraciones.UserList[UserIndex].Name + " depositó " + Cantidad + " "
						+ Declaraciones.ObjData[ObjIndex].Name + "[" + ObjIndex + "]");
			}

			/* 'Actualizamos el inventario del usuario */
			InvUsuario.UpdateUserInv(true, UserIndex, 0);

			/* 'Actualizamos el inventario del banco */
			UpdateBanUserInv(true, UserIndex, 0);
		}

		/* 'Actualizamos la ventana del banco */
		UpdateVentanaBanco(UserIndex);
		/* FIXME: ErrHandler : */
	}

	public static void UserDejaObj(int UserIndex, int ObjIndex, int Cantidad) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int Slot = 0;
		int obji = 0;

		if (Cantidad < 1) {
			return;
		}

		obji = Declaraciones.UserList[UserIndex].Invent.Object[ObjIndex].ObjIndex;

		/* '¿Ya tiene un objeto de este tipo? */
		Slot = 1;
		while (!(Declaraciones.UserList[UserIndex].BancoInvent.Object[Slot].ObjIndex == obji
				&& Declaraciones.UserList[UserIndex].BancoInvent.Object[Slot].Amount
						+ Cantidad <= Declaraciones.MAX_INVENTORY_OBJS)) {
			Slot = Slot + 1;

			if (Slot > Declaraciones.MAX_BANCOINVENTORY_SLOTS) {
				break; /* FIXME: EXIT DO */
			}
		}

		/* 'Sino se fija por un slot vacio antes del slot devuelto */
		if (Slot > Declaraciones.MAX_BANCOINVENTORY_SLOTS) {
			Slot = 1;
			while (!(Declaraciones.UserList[UserIndex].BancoInvent.Object[Slot].ObjIndex == 0)) {
				Slot = Slot + 1;

				if (Slot > Declaraciones.MAX_BANCOINVENTORY_SLOTS) {
					Protocol.WriteConsoleMsg(UserIndex, "No tienes mas espacio en el banco!!",
							FontTypeNames.FONTTYPE_INFO);
					return;
				}
			}

			Declaraciones.UserList[UserIndex].BancoInvent.NroItems = Declaraciones.UserList[UserIndex].BancoInvent.NroItems
					+ 1;
		}

		/* 'Slot valido */
		if (Slot <= Declaraciones.MAX_BANCOINVENTORY_SLOTS) {
			/* 'Mete el obj en el slot */
			if (Declaraciones.UserList[UserIndex].BancoInvent.Object[Slot].Amount
					+ Cantidad <= Declaraciones.MAX_INVENTORY_OBJS) {

				/* 'Menor que MAX_INV_OBJS */
				Declaraciones.UserList[UserIndex].BancoInvent.Object[Slot].ObjIndex = obji;
				Declaraciones.UserList[UserIndex].BancoInvent.Object[Slot].Amount = Declaraciones.UserList[UserIndex].BancoInvent.Object[Slot].Amount
						+ Cantidad;

				InvUsuario.QuitarUserInvItem(UserIndex, vb6.CByte(ObjIndex), Cantidad);
			} else {
				Protocol.WriteConsoleMsg(UserIndex, "El banco no puede cargar tantos objetos.",
						FontTypeNames.FONTTYPE_INFO);
			}
		}
	}

	public static void SendUserBovedaTxt(int sendIndex, int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR RESUME NEXT */
		int j = 0;

		Protocol.WriteConsoleMsg(sendIndex, Declaraciones.UserList[UserIndex].Name, FontTypeNames.FONTTYPE_INFO);
		Protocol.WriteConsoleMsg(sendIndex,
				"Tiene " + Declaraciones.UserList[UserIndex].BancoInvent.NroItems + " objetos.",
				FontTypeNames.FONTTYPE_INFO);

		for (j = (1); j <= (Declaraciones.MAX_BANCOINVENTORY_SLOTS); j++) {
			if (Declaraciones.UserList[UserIndex].BancoInvent.Object[j].ObjIndex > 0) {
				Protocol.WriteConsoleMsg(sendIndex,
						"Objeto " + j + " "
								+ Declaraciones.ObjData[Declaraciones.UserList[UserIndex].BancoInvent.Object[j].ObjIndex].Name
								+ " Cantidad:" + Declaraciones.UserList[UserIndex].BancoInvent.Object[j].Amount,
						FontTypeNames.FONTTYPE_INFO);
			}
		}

	}

	public static void SendUserBovedaTxtFromChar(int sendIndex, String charName) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR RESUME NEXT */
		int j = 0;
		String CharFile;
		String Tmp;
		int ObjInd = 0;
		int ObjCant = 0;

		CharFile = Declaraciones.CharPath + charName + ".chr";

		if (General.FileExist(CharFile, 0)) {
			Protocol.WriteConsoleMsg(sendIndex, charName, FontTypeNames.FONTTYPE_INFO);
			Protocol.WriteConsoleMsg(sendIndex,
					"Tiene " + ES.GetVar(CharFile, "BancoInventory", "CantidadItems") + " objetos.",
					FontTypeNames.FONTTYPE_INFO);
			for (j = (1); j <= (Declaraciones.MAX_BANCOINVENTORY_SLOTS); j++) {
				Tmp = ES.GetVar(CharFile, "BancoInventory", "Obj" + j);
				ObjInd = General.ReadField(1, Tmp, vb6.Asc("-"));
				ObjCant = General.ReadField(2, Tmp, vb6.Asc("-"));
				if (ObjInd > 0) {
					Protocol.WriteConsoleMsg(sendIndex,
							"Objeto " + j + " " + Declaraciones.ObjData[ObjInd].Name + " Cantidad:" + ObjCant,
							FontTypeNames.FONTTYPE_INFO);
				}
			}
		} else {
			Protocol.WriteConsoleMsg(sendIndex, "Usuario inexistente: " + charName, FontTypeNames.FONTTYPE_INFO);
		}

	}

}