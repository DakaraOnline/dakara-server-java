
/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"InvNpc"')] */
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

public class InvNpc {
	/* '?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿ */
	/* '?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿ */
	/* '?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿ */
	/* ' Modulo Inv & Obj */
	/* '?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿ */
	/* '?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿ */
	/* '?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿ */
	/* 'Modulo para controlar los objetos y los inventarios. */
	/* '?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿ */
	/* '?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿ */
	/* '?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿ */
	static Declaraciones.WorldPos TirarItemAlPiso(Declaraciones.WorldPos Pos, Declaraciones.Obj Obj) {
		return TirarItemAlPiso(Pos, Obj, true);
	}

	static Declaraciones.WorldPos TirarItemAlPiso(Declaraciones.WorldPos Pos, Declaraciones.Obj Obj,
			boolean NotPirata) {
		Declaraciones.WorldPos retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		Declaraciones.WorldPos NuevaPos;
		NuevaPos.X = 0;
		NuevaPos.Y = 0;

		UsUaRiOs.Tilelibre(Pos, NuevaPos, Obj, NotPirata, true);
		if (NuevaPos.X != 0 && NuevaPos.Y != 0) {
			InvUsuario.MakeObj(Obj, Pos.Map, NuevaPos.X, NuevaPos.Y);
		}
		retval = NuevaPos;

		return retval;
		/* FIXME: ErrHandler : */

		return retval;
	}

	static void NPC_TIRAR_ITEMS(Declaraciones.npc /* FIXME BYREF!! */ npc, boolean IsPretoriano) {
		/* '*************************************************** */
		/* 'Autor: Unknown (orginal version) */
		/* 'Last Modification: 28/11/2009 */
		/* 'Give away npc's items. */
		/* '28/11/2009: ZaMa - Implementado drops complejos */
		/* '02/04/2010: ZaMa - Los pretos vuelven a tirar oro. */
		/* '10/04/2011: ZaMa - Logueo los objetos logueables dropeados. */
		/* '*************************************************** */
		/* FIXME: ON ERROR RESUME NEXT */

		int i = 0;
		Declaraciones.Obj MiObj;
		int NroDrop = 0;
		int Random = 0;
		int ObjIndex = 0;

		/* ' Tira todo el inventario */
		if (IsPretoriano) {
			for (i = (1); i <= (Declaraciones.MAX_INVENTORY_SLOTS); i++) {
				if (npc.Invent.Object[i].ObjIndex > 0) {
					MiObj.Amount = npc.Invent.Object[i].Amount;
					MiObj.ObjIndex = npc.Invent.Object[i].ObjIndex;
					TirarItemAlPiso(npc.Pos, MiObj);
				}
			}

			/* ' Dropea oro? */
			if (npc.GiveGLD > 0) {
				TirarOroNpc(npc.GiveGLD, npc.Pos);
			}

			return;
		}

		Random = Matematicas.RandomNumber(1, 100);

		/* ' Tiene 10% de prob de no tirar nada */
		if (Random <= 90) {
			NroDrop = 1;

			if (Random <= 10) {
				NroDrop = NroDrop + 1;

				for (i = (1); i <= (3); i++) {
					/* ' 10% de ir pasando de etapas */
					if (Matematicas.RandomNumber(1, 100) <= 10) {
						NroDrop = NroDrop + 1;
					} else {
						break; /* FIXME: EXIT FOR */
					}
				}

			}

			ObjIndex = npc.Drop[NroDrop].ObjIndex;
			if (ObjIndex > 0) {

				if (ObjIndex == Declaraciones.iORO) {
					TirarOroNpc(npc.Drop[NroDrop].Amount, npc.Pos);
				} else {
					MiObj.Amount = npc.Drop[NroDrop].Amount;
					MiObj.ObjIndex = ObjIndex;

					TirarItemAlPiso(npc.Pos, MiObj);

					if (Declaraciones.ObjData[ObjIndex].Log == 1) {
						General.LogDesarrollo(npc.Name + " dropeó " + MiObj.Amount + " "
								+ Declaraciones.ObjData[ObjIndex].Name + "[" + ObjIndex + "]");
					}

				}
			}

		}

	}

	static boolean QuedanItems(int NpcIndex, int ObjIndex) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR RESUME NEXT */

		int i = 0;
		if (Declaraciones.Npclist[NpcIndex].Invent.NroItems > 0) {
			for (i = (1); i <= (Declaraciones.MAX_INVENTORY_SLOTS); i++) {
				if (Declaraciones.Npclist[NpcIndex].Invent.Object[i].ObjIndex == ObjIndex) {
					retval = true;
					return retval;
				}
			}
		}
		retval = false;
		return retval;
	}

	/* '' */
	/* ' Gets the amount of a certain item that an npc has. */
	/* ' */
	/* ' @param npcIndex Specifies reference to npcmerchant */
	/* ' @param ObjIndex Specifies reference to object */
	/* ' @return The amount of the item that the npc has */
	/* ' @remarks This function reads the Npc.dat file */
	static int EncontrarCant(int NpcIndex, int ObjIndex) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 03/09/08 */
		/* 'Last Modification By: Marco Vanotti (Marco) */
		/*
		 * ' - 03/09/08 EncontrarCant now returns 0 if the npc doesn't have it
		 * (Marco)
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR RESUME NEXT */
		/* 'Devuelve la cantidad original del obj de un npc */

		String ln;
		String npcfile;
		int i = 0;

		npcfile = Declaraciones.DatPath + "NPCs.dat";

		for (i = (1); i <= (Declaraciones.MAX_INVENTORY_SLOTS); i++) {
			ln = ES.GetVar(npcfile, "NPC" + Declaraciones.Npclist[NpcIndex].Numero, "Obj" + i);
			if (ObjIndex == vb6.val(General.ReadField(1, ln, 45))) {
				retval = vb6.val(General.ReadField(2, ln, 45));
				return retval;
			}
		}

		retval = 0;

		return retval;
	}

	static void ResetNpcInv(int NpcIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR RESUME NEXT */

		int i = 0;

		Declaraciones.Npclist[NpcIndex].Invent.NroItems = 0;

		for (i = (1); i <= (Declaraciones.MAX_INVENTORY_SLOTS); i++) {
			Declaraciones.Npclist[NpcIndex].Invent.Object[i].ObjIndex = 0;
			Declaraciones.Npclist[NpcIndex].Invent.Object[i].Amount = 0;
		}

		Declaraciones.Npclist[NpcIndex].InvReSpawn = 0;

	}

	/* '' */
	/* ' Removes a certain amount of items from a slot of an npc's inventory */
	/* ' */
	/* ' @param npcIndex Specifies reference to npcmerchant */
	/* ' @param Slot Specifies reference to npc's inventory's slot */
	/* ' @param antidad Specifies amount of items that will be removed */
	static void QuitarNpcInvItem(int NpcIndex, int Slot, int Cantidad) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 23/11/2009 */
		/* 'Last Modification By: Marco Vanotti (Marco) */
		/*
		 * ' - 03/09/08 Now this sub checks that te npc has an item before
		 * respawning it (Marco)
		 */
		/* '23/11/2009: ZaMa - Optimizacion de codigo. */
		/* '*************************************************** */
		int ObjIndex = 0;
		int iCant = 0;

		ObjIndex = Declaraciones.Npclist[NpcIndex].Invent.Object[Slot].ObjIndex;

		/* 'Quita un Obj */
		if (Declaraciones.ObjData[Declaraciones.Npclist[NpcIndex].Invent.Object[Slot].ObjIndex].Crucial == 0) {
			Declaraciones.Npclist[NpcIndex].Invent.Object[Slot].Amount = Declaraciones.Npclist[NpcIndex].Invent.Object[Slot].Amount
					- Cantidad;

			if (Declaraciones.Npclist[NpcIndex].Invent.Object[Slot].Amount <= 0) {
				Declaraciones.Npclist[NpcIndex].Invent.NroItems = Declaraciones.Npclist[NpcIndex].Invent.NroItems - 1;
				Declaraciones.Npclist[NpcIndex].Invent.Object[Slot].ObjIndex = 0;
				Declaraciones.Npclist[NpcIndex].Invent.Object[Slot].Amount = 0;
				if (Declaraciones.Npclist[NpcIndex].Invent.NroItems == 0
						&& Declaraciones.Npclist[NpcIndex].InvReSpawn != 1) {
					/* 'Reponemos el inventario */
					CargarInvent(NpcIndex);
				}
			}
		} else {
			Declaraciones.Npclist[NpcIndex].Invent.Object[Slot].Amount = Declaraciones.Npclist[NpcIndex].Invent.Object[Slot].Amount
					- Cantidad;

			if (Declaraciones.Npclist[NpcIndex].Invent.Object[Slot].Amount <= 0) {
				Declaraciones.Npclist[NpcIndex].Invent.NroItems = Declaraciones.Npclist[NpcIndex].Invent.NroItems - 1;
				Declaraciones.Npclist[NpcIndex].Invent.Object[Slot].ObjIndex = 0;
				Declaraciones.Npclist[NpcIndex].Invent.Object[Slot].Amount = 0;

				if (!QuedanItems(NpcIndex, ObjIndex)) {
					/* 'Check if the item is in the npc's dat. */
					iCant = EncontrarCant(NpcIndex, ObjIndex);
					if (iCant) {
						Declaraciones.Npclist[NpcIndex].Invent.Object[Slot].ObjIndex = ObjIndex;
						Declaraciones.Npclist[NpcIndex].Invent.Object[Slot].Amount = iCant;
						Declaraciones.Npclist[NpcIndex].Invent.NroItems = Declaraciones.Npclist[NpcIndex].Invent.NroItems
								+ 1;
					}
				}

				if (Declaraciones.Npclist[NpcIndex].Invent.NroItems == 0
						&& Declaraciones.Npclist[NpcIndex].InvReSpawn != 1) {
					/* 'Reponemos el inventario */
					CargarInvent(NpcIndex);
				}
			}
		}
	}

	static void CargarInvent(int NpcIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* 'Vuelve a cargar el inventario del npc NpcIndex */
		int LoopC = 0;
		String ln;
		String npcfile;

		npcfile = Declaraciones.DatPath + "NPCs.dat";

		Declaraciones.Npclist[NpcIndex].Invent.NroItems = vb6
				.val(ES.GetVar(npcfile, "NPC" + Declaraciones.Npclist[NpcIndex].Numero, "NROITEMS"));

		for (LoopC = (1); LoopC <= (Declaraciones.Npclist[NpcIndex].Invent.NroItems); LoopC++) {
			ln = ES.GetVar(npcfile, "NPC" + Declaraciones.Npclist[NpcIndex].Numero, "Obj" + LoopC);
			Declaraciones.Npclist[NpcIndex].Invent.Object[LoopC].ObjIndex = vb6.val(General.ReadField(1, ln, 45));
			Declaraciones.Npclist[NpcIndex].Invent.Object[LoopC].Amount = vb6.val(General.ReadField(2, ln, 45));

		}

	}

	static void TirarOroNpc(int Cantidad,
			Declaraciones.WorldPos /* FIXME BYREF!! */ Pos) {
		/* '*************************************************** */
		/* 'Autor: ZaMa */
		/* 'Last Modification: 13/02/2010 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		if (Cantidad > 0) {
			Declaraciones.Obj MiObj;
			int RemainingGold = 0;

			RemainingGold = Cantidad;

			while ((RemainingGold > 0)) {

				/* ' Tira pilon de 10k */
				if (RemainingGold > Declaraciones.MAX_INVENTORY_OBJS) {
					MiObj.Amount = Declaraciones.MAX_INVENTORY_OBJS;
					RemainingGold = RemainingGold - Declaraciones.MAX_INVENTORY_OBJS;

					/* ' Tira lo que quede */
				} else {
					MiObj.Amount = RemainingGold;
					RemainingGold = 0;
				}

				MiObj.ObjIndex = Declaraciones.iORO;

				TirarItemAlPiso(Pos, MiObj);
			}
		}

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en TirarOro. Error " + Err.Number + " : " + Err.description);
	}

}