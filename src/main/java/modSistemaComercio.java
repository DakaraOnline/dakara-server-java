
/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"modSistemaComercio"')] */
/* '***************************************************** */
/* 'Sistema de Comercio para Argentum Online */
/* 'Programado por Nacho (Integer) */
/* 'integer-x@hotmail.com */
/* '***************************************************** */

/* '************************************************************************** */
/* 'This program is free software; you can redistribute it and/or modify */
/* 'it under the terms of the GNU General Public License as published by */
/* 'the Free Software Foundation; either version 2 of the License, or */
/* '(at your option) any later version. */
/* ' */
/* 'This program is distributed in the hope that it will be useful, */
/* 'but WITHOUT ANY WARRANTY; without even the implied warranty of */
/* 'MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the */
/* 'GNU General Public License for more details. */
/* ' */
/* 'You should have received a copy of the GNU General Public License */
/* 'along with this program; if not, write to the Free Software */
/* 'Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA */
/* '************************************************************************** */

import enums.*;

public class modSistemaComercio {

	static final int REDUCTOR_PRECIOVENTA = 3;

	/* '' */
	/* ' Makes a trade. (Buy or Sell) */
	/* ' */
	/* ' @param Modo The trade type (sell or buy) */
	/* ' @param UserIndex Specifies the index of the user */
	/* ' @param NpcIndex specifies the index of the npc */
	/* ' @param Slot Specifies which slot are you trying to sell / buy */
	/*
	 * ' @param Cantidad Specifies how many items in that slot are you trying to
	 * sell / buy
	 */
	static void Comercio(eModoComercio Modo, int UserIndex, int NpcIndex, int Slot, int Cantidad) {
 /* '************************************************* */
 /* 'Author: Nacho (Integer) */
 /* 'Last modified: 07/06/2010 */
 /* '27/07/08 (MarKoxX) | New changes in the way of trading (now when you buy it rounds to ceil and when you sell it rounds to floor) */
 /* '  - 06/13/08 (NicoNZ) */
 /* '07/06/2010: ZaMa - Los objetos se loguean si superan la cantidad de 1k (antes era solo si eran 1k). */
 /* '************************************************* */
 int Precio = 0;
 Declaraciones.Obj Objeto;
 
 if (Cantidad<1 || Slot<1) {
 return;
 }
 
  if (Modo == eModoComercio.Compra) {
   if (Slot>Declaraciones.MAX_INVENTORY_SLOTS) {
   return;
   } else if (Cantidad>Declaraciones.MAX_INVENTORY_OBJS) {
   modSendData.SendData(SendTarget.ToAll, 0, Protocol.PrepareMessageConsoleMsg(Declaraciones.UserList[UserIndex].Name + " ha sido baneado por el sistema anti-cheats.", FontTypeNames.FONTTYPE_FIGHT));
   ES.Ban(Declaraciones.UserList[UserIndex].Name, "Sistema Anti Cheats", "Intentar hackear el sistema de comercio. Quiso comprar demasiados ítems:" + Cantidad);
   Declaraciones.UserList[UserIndex].flags.Ban = 1;
   Protocol.WriteErrorMsg(UserIndex, "Has sido baneado por el Sistema AntiCheat.");
   Protocol.FlushBuffer(UserIndex);
   TCP.CloseSocket(UserIndex);
   return;
   } else if (!Declaraciones.Npclist[NpcIndex].Invent.Object[Slot].Amount>0) {
   return;
  }
  
  if (Cantidad>Declaraciones.Npclist[NpcIndex].Invent.Object[Slot].Amount) {
  Cantidad = Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Invent.Object[Slot].Amount;
  }
  
  Objeto.Amount = Cantidad;
  Objeto.ObjIndex = Declaraciones.Npclist[NpcIndex].Invent.Object[Slot].ObjIndex;
  
  /* 'El precio, cuando nos venden algo, lo tenemos que redondear para arriba. */
  /* 'Es decir, 1.1 = 2, por lo cual se hace de la siguiente forma Precio = Clng(PrecioFinal + 0.5) Siempre va a darte el proximo numero. O el "Techo" (MarKoxX) */
  
  Precio = vb6.CLng((Declaraciones.ObjData[Declaraciones.Npclist[NpcIndex].Invent.Object[Slot].ObjIndex].Valor / (double) Descuento(UserIndex)*Cantidad)+0.5);
  
   if (Declaraciones.UserList[UserIndex].Stats.GLD<Precio) {
   Protocol.WriteConsoleMsg(UserIndex, "No tienes suficiente dinero.", FontTypeNames.FONTTYPE_INFO);
   return;
  }
  
   if (InvUsuario.MeterItemEnInventario(UserIndex, Objeto) == false) {
   /* 'Call WriteConsoleMsg(UserIndex, "No puedes cargar mas objetos.", FontTypeNames.FONTTYPE_INFO) */
   EnviarNpcInv(UserIndex, Declaraciones.UserList[UserIndex].flags.TargetNPC);
   Protocol.WriteTradeOK(UserIndex);
   return;
  }
  
  Declaraciones.UserList[UserIndex].Stats.GLD = Declaraciones.UserList[UserIndex].Stats.GLD-Precio;
  
  InvNpc.QuitarNpcInvItem(Declaraciones.UserList[UserIndex].flags.TargetNPC, vb6.CByte(Slot), Cantidad);
  
  /* 'Bien, ahora logueo de ser necesario. Pablo (ToxicWaste) 07/09/07 */
  /* 'Es un Objeto que tenemos que loguear? */
   if (Declaraciones.ObjData[Objeto.ObjIndex].Log == 1) {
   General.LogDesarrollo(Declaraciones.UserList[UserIndex].Name + " compró del NPC " + Objeto.Amount + " " + Declaraciones.ObjData[Objeto.ObjIndex].Name);
   /* 'Es mucha cantidad? */
   } else if (Objeto.Amount>=1000) {
   /* 'Si no es de los prohibidos de loguear, lo logueamos. */
    if (Declaraciones.ObjData[Objeto.ObjIndex].NoLog != 1) {
    General.LogDesarrollo(Declaraciones.UserList[UserIndex].Name + " compró del NPC " + Objeto.Amount + " " + Declaraciones.ObjData[Objeto.ObjIndex].Name);
   }
  }
  
  /* 'Agregado para que no se vuelvan a vender las llaves si se recargan los .dat. */
   if (Declaraciones.ObjData[Objeto.ObjIndex].OBJType == otLlaves) {
   ES.WriteVar(Declaraciones.DatPath + "NPCs.dat", "NPC" + Declaraciones.Npclist[NpcIndex].Numero, "obj" + Slot, Objeto.ObjIndex + "-0");
   General.logVentaCasa(Declaraciones.UserList[UserIndex].Name + " compró " + Declaraciones.ObjData[Objeto.ObjIndex].Name);
  }
  
  } else if (Modo == eModoComercio.Venta) {
  
  if (Cantidad>Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount) {
  Cantidad = Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount;
  }
  
  Objeto.Amount = Cantidad;
  Objeto.ObjIndex = Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex;
  
   if (Objeto.ObjIndex == 0) {
   return;
   
   } else if (Declaraciones.ObjData[Objeto.ObjIndex].Intransferible == 1 || Declaraciones.ObjData[Objeto.ObjIndex].NoComerciable == 1) {
   Protocol.WriteConsoleMsg(UserIndex, "No puedes vender este tipo de objeto.", FontTypeNames.FONTTYPE_INFO);
   return;
   } else if ((Declaraciones.Npclist[NpcIndex].TipoItems != Declaraciones.ObjData[Objeto.ObjIndex].OBJType && Declaraciones.Npclist[NpcIndex].TipoItems != eOBJType.otCualquiera) || Objeto.ObjIndex == Declaraciones.iORO) {
   Protocol.WriteConsoleMsg(UserIndex, "Lo siento, no estoy interesado en este tipo de objetos.", FontTypeNames.FONTTYPE_INFO);
   EnviarNpcInv(UserIndex, Declaraciones.UserList[UserIndex].flags.TargetNPC);
   Protocol.WriteTradeOK(UserIndex);
   return;
   } else if (Declaraciones.ObjData[Objeto.ObjIndex].Real == 1) {
    if (Declaraciones.Npclist[NpcIndex].Name != "SR") {
    Protocol.WriteConsoleMsg(UserIndex, "Las armaduras del ejército real sólo pueden ser vendidas a los sastres reales.", FontTypeNames.FONTTYPE_INFO);
    EnviarNpcInv(UserIndex, Declaraciones.UserList[UserIndex].flags.TargetNPC);
    Protocol.WriteTradeOK(UserIndex);
    return;
   }
   } else if (Declaraciones.ObjData[Objeto.ObjIndex].Caos == 1) {
    if (Declaraciones.Npclist[NpcIndex].Name != "SC") {
    Protocol.WriteConsoleMsg(UserIndex, "Las armaduras de la legión oscura sólo pueden ser vendidas a los sastres del demonio.", FontTypeNames.FONTTYPE_INFO);
    EnviarNpcInv(UserIndex, Declaraciones.UserList[UserIndex].flags.TargetNPC);
    Protocol.WriteTradeOK(UserIndex);
    return;
   }
   } else if (Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount<0 || Cantidad == 0) {
   return;
   } else if (Slot<vb6.LBound(Declaraciones.UserList[UserIndex].Invent.Object[]) || Slot>vb6.UBound(Declaraciones.UserList[UserIndex].Invent.Object[])) {
   EnviarNpcInv(UserIndex, Declaraciones.UserList[UserIndex].flags.TargetNPC);
   return;
   } else if (Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.Consejero) {
   Protocol.WriteConsoleMsg(UserIndex, "No puedes vender ítems.", FontTypeNames.FONTTYPE_WARNING);
   EnviarNpcInv(UserIndex, Declaraciones.UserList[UserIndex].flags.TargetNPC);
   Protocol.WriteTradeOK(UserIndex);
   return;
  }
  
  InvUsuario.QuitarUserInvItem(UserIndex, Slot, Cantidad);
  
  /* 'Precio = Round(ObjData(Objeto.ObjIndex).valor / REDUCTOR_PRECIOVENTA * Cantidad, 0) */
  Precio = vb6.Fix(SalePrice(Objeto.ObjIndex)*Cantidad);
  Declaraciones.UserList[UserIndex].Stats.GLD = Declaraciones.UserList[UserIndex].Stats.GLD+Precio;
  
  if (Declaraciones.UserList[UserIndex].Stats.GLD>Declaraciones.MAXORO) {
  Declaraciones.UserList[UserIndex].Stats.GLD = Declaraciones.MAXORO;
  }
  
  int NpcSlot = 0;
  NpcSlot = SlotEnNPCInv(NpcIndex, Objeto.ObjIndex, Objeto.Amount);
  
  /* 'Slot valido */
   if (NpcSlot<=Declaraciones.MAX_INVENTORY_SLOTS) {
   /* 'Mete el obj en el slot */
   Declaraciones.Npclist[NpcIndex].Invent.Object[NpcSlot].ObjIndex = Objeto.ObjIndex;
   Declaraciones.Npclist[NpcIndex].Invent.Object[NpcSlot].Amount = Declaraciones.Npclist[NpcIndex].Invent.Object[NpcSlot].Amount+Objeto.Amount;
    if (Declaraciones.Npclist[NpcIndex].Invent.Object[NpcSlot].Amount>Declaraciones.MAX_INVENTORY_OBJS) {
    Declaraciones.Npclist[NpcIndex].Invent.Object[NpcSlot].Amount = Declaraciones.MAX_INVENTORY_OBJS;
   }
  }
  
  /* 'Bien, ahora logueo de ser necesario. Pablo (ToxicWaste) 07/09/07 */
  /* 'Es un Objeto que tenemos que loguear? */
   if (Declaraciones.ObjData[Objeto.ObjIndex].Log == 1) {
   General.LogDesarrollo(Declaraciones.UserList[UserIndex].Name + " vendió al NPC " + Objeto.Amount + " " + Declaraciones.ObjData[Objeto.ObjIndex].Name);
   /* 'Es mucha cantidad? */
   } else if (Objeto.Amount>=1000) {
   /* 'Si no es de los prohibidos de loguear, lo logueamos. */
    if (Declaraciones.ObjData[Objeto.ObjIndex].NoLog != 1) {
    General.LogDesarrollo(Declaraciones.UserList[UserIndex].Name + " vendió al NPC " + Objeto.Amount + " " + Declaraciones.ObjData[Objeto.ObjIndex].Name);
   }
  }
  
 }
 
 InvUsuario.UpdateUserInv(true, UserIndex, 0);
 Protocol.WriteUpdateUserStats(UserIndex);
 EnviarNpcInv(UserIndex, Declaraciones.UserList[UserIndex].flags.TargetNPC);
 Protocol.WriteTradeOK(UserIndex);
 
 UsUaRiOs.SubirSkill(UserIndex, eSkill.Comerciar, true);
}

	static void IniciarComercioNPC(int UserIndex) {
		/* '************************************************* */
		/* 'Author: Nacho (Integer) */
		/* 'Last modified: 2/8/06 */
		/* '************************************************* */
		EnviarNpcInv(UserIndex, Declaraciones.UserList[UserIndex].flags.TargetNPC);
		Declaraciones.UserList[UserIndex].flags.Comerciando = true;
		Protocol.WriteCommerceInit(UserIndex);
	}

	static int SlotEnNPCInv(int NpcIndex, int Objeto, int Cantidad) {
		int retval = 0;
		/* '************************************************* */
		/* 'Author: Nacho (Integer) */
		/* 'Last modified: 2/8/06 */
		/* '************************************************* */
		retval = 1;
		while (!(Declaraciones.Npclist[NpcIndex].Invent.Object[retval].ObjIndex == Objeto
				&& Declaraciones.Npclist[NpcIndex].Invent.Object[retval].Amount
						+ Cantidad <= Declaraciones.MAX_INVENTORY_OBJS)) {

			retval = retval + 1;
			if (retval > Declaraciones.MAX_INVENTORY_SLOTS) {
				break; /* FIXME: EXIT DO */
			}

		}

		if (retval > Declaraciones.MAX_INVENTORY_SLOTS) {

			retval = 1;

			while (!(Declaraciones.Npclist[NpcIndex].Invent.Object[retval].ObjIndex == 0)) {

				retval = retval + 1;
				if (retval > Declaraciones.MAX_INVENTORY_SLOTS) {
					break; /* FIXME: EXIT DO */
				}

			}

			if (retval <= Declaraciones.MAX_INVENTORY_SLOTS) {
				Declaraciones.Npclist[NpcIndex].Invent.NroItems = Declaraciones.Npclist[NpcIndex].Invent.NroItems + 1;
			}

		}

		return retval;
	}

	static float Descuento(int UserIndex) {
		float retval = 0.0f;
		/* '************************************************* */
		/* 'Author: Nacho (Integer) */
		/* 'Last modified: 2/8/06 */
		/* '************************************************* */
		retval = 1 + Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Comerciar] / (double) 100;
		return retval;
	}

	/* '' */
	/* ' Send the inventory of the Npc to the user */
	/* ' */
	/* ' @param userIndex The index of the User */
	/* ' @param npcIndex The index of the NPC */

	static void EnviarNpcInv(int UserIndex, int NpcIndex) {
		/* '************************************************* */
		/* 'Author: Nacho (Integer) */
		/* 'Last Modified: 06/14/08 */
		/* 'Last Modified By: Nicolás Ezequiel Bouhid (NicoNZ) */
		/* '************************************************* */
		int Slot = 0;
		float val = 0.0f;

		for (Slot = (1); Slot <= (Declaraciones.MAX_NORMAL_INVENTORY_SLOTS); Slot++) {
			if (Declaraciones.Npclist[NpcIndex].Invent.Object[Slot].ObjIndex > 0) {
				Declaraciones.Obj thisObj;

				thisObj.ObjIndex = Declaraciones.Npclist[NpcIndex].Invent.Object[Slot].ObjIndex;
				thisObj.Amount = Declaraciones.Npclist[NpcIndex].Invent.Object[Slot].Amount;

				val = (Declaraciones.ObjData[thisObj.ObjIndex].Valor) / (double) Descuento(UserIndex);

				Protocol.WriteChangeNPCInventorySlot(UserIndex, Slot, thisObj, val);
			} else {
				Declaraciones.Obj DummyObj;
				Protocol.WriteChangeNPCInventorySlot(UserIndex, Slot, DummyObj, 0);
			}
		}
	}

	/* '' */
	/* ' Devuelve el valor de venta del objeto */
	/* ' */
	/*
	 * ' @param ObjIndex El número de objeto al cual le calculamos el precio de
	 * venta
	 */

	static float SalePrice(int ObjIndex) {
		float retval = 0.0f;
		/* '************************************************* */
		/* 'Author: Nicolás (NicoNZ) */
		/* ' */
		/* '************************************************* */
		if (ObjIndex < 1 || ObjIndex > vb6.UBound(Declaraciones.ObjData)) {
			return retval;
		}
		if (InvUsuario.ItemNewbie(ObjIndex)) {
			return retval;
		}

		retval = Declaraciones.ObjData[ObjIndex].Valor / (double) modSistemaComercio.REDUCTOR_PRECIOVENTA;
		return retval;
	}

}