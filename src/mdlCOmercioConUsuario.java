/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"mdlCOmercioConUsuario"')] */
/* '************************************************************** */
/* ' mdlComercioConUsuarios.bas - Allows players to commerce between themselves. */
/* ' */
/* ' Designed and implemented by Alejandro Santos (AlejoLP) */
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

/* '[Alejo] */
import enums.*;

public class mdlCOmercioConUsuario {

	static final int MAX_OFFER_SLOTS = 20;
	static final int GOLD_OFFER_SLOT = /* FANCY EXPRESSION */ MAX_OFFER_SLOTS + 1;

	static public class tCOmercioUsuario {
		public int DestUsu;
		public String DestNick;
		public int[] Objeto;
		public int GoldAmount;

		public int[] cant;
		public boolean Acepto;
		public boolean Confirmo;
	}

	static public class tOfferItem {
		public int ObjIndex;
		public int Amount;
	}

	/* 'origen: origen de la transaccion, originador del comando */
	/* 'destino: receptor de la transaccion */
	static void IniciarComercioConUsuario(int Origen, int Destino) {
		/* '*************************************************** */
		/* 'Autor: Unkown */
		/* 'Last Modification: 25/11/2009 */
		/* ' */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		/* 'Si ambos pusieron /comerciar entonces */
		if (Declaraciones.UserList[Origen].ComUsu.DestUsu == Destino
				&& Declaraciones.UserList[Destino].ComUsu.DestUsu == Origen) {

			if (Declaraciones.UserList[Origen].flags.Comerciando || Declaraciones.UserList[Destino].flags.Comerciando) {
				Protocol.WriteConsoleMsg(Origen, "No puedes comerciar en este momento", FontTypeNames.FONTTYPE_TALK);
				Protocol.WriteConsoleMsg(Destino, "No puedes comerciar en este momento", FontTypeNames.FONTTYPE_TALK);
				return;
			}

			/* 'Actualiza el inventario del usuario */
			InvUsuario.UpdateUserInv(true, Origen, 0);
			/* 'Decirle al origen que abra la ventanita. */
			Protocol.WriteUserCommerceInit(Origen);
			Declaraciones.UserList[Origen].flags.Comerciando = true;

			/* 'Actualiza el inventario del usuario */
			InvUsuario.UpdateUserInv(true, Destino, 0);
			/* 'Decirle al origen que abra la ventanita. */
			Protocol.WriteUserCommerceInit(Destino);
			Declaraciones.UserList[Destino].flags.Comerciando = true;

			/* 'Call EnviarObjetoTransaccion(Origen) */
		} else {
			/* 'Es el primero que comercia ? */
			Protocol.WriteConsoleMsg(Destino,
					Declaraciones.UserList[Origen].Name + " desea comerciar. Si deseas aceptar, escribe /COMERCIAR.",
					FontTypeNames.FONTTYPE_TALK);
			Declaraciones.UserList[Destino].flags.TargetUser = Origen;

		}

		Protocol.FlushBuffer(Destino);

		return;
		/* FIXME: ErrHandler : */
		General.LogError("Error en IniciarComercioConUsuario: " + Err.description);
	}

	static void EnviarOferta(int UserIndex, int OfferSlot) {
		/* '*************************************************** */
		/* 'Autor: Unkown */
		/* 'Last Modification: 25/11/2009 */
		/* 'Sends the offer change to the other trading user */
		/*
		 * '25/11/2009: ZaMa - Implementado nuevo sistema de comercio con
		 * ofertas variables.
		 */
		/* '*************************************************** */
		int ObjIndex = 0;
		int ObjAmount = 0;
		int OtherUserIndex = 0;

		/* FIXME: ON ERROR GOTO ErrHandler */

		OtherUserIndex = Declaraciones.UserList[UserIndex].ComUsu.DestUsu;

		if (OfferSlot == mdlCOmercioConUsuario.GOLD_OFFER_SLOT) {
			ObjIndex = Declaraciones.iORO;
			ObjAmount = Declaraciones.UserList[OtherUserIndex].ComUsu.GoldAmount;
		} else {
			ObjIndex = Declaraciones.UserList[OtherUserIndex].ComUsu.Objeto[OfferSlot];
			ObjAmount = Declaraciones.UserList[OtherUserIndex].ComUsu.cant[OfferSlot];
		}

		Protocol.WriteChangeUserTradeSlot(UserIndex, OfferSlot, ObjIndex, ObjAmount);
		Protocol.FlushBuffer(UserIndex);

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en EnviarOferta. Error: " + Err.description + ". UserIndex: " + UserIndex
				+ ". OfferSlot: " + OfferSlot);
	}

	static void FinComerciarUsu(int UserIndex) {
		/* '*************************************************** */
		/* 'Autor: Unkown */
		/* 'Last Modification: 25/11/2009 */
		/* '25/11/2009: ZaMa - Limpio los arrays (por el nuevo sistema) */
		/* '*************************************************** */
		int i = 0;

		/* FIXME: ON ERROR GOTO ErrHandler */

		if (Declaraciones.UserList[UserIndex].ComUsu.DestUsu > 0
				&& Declaraciones.UserList[UserIndex].ComUsu.DestUsu < Declaraciones.MaxUsers) {
			Protocol.WriteUserCommerceEnd(UserIndex);
		}

		Declaraciones.UserList[UserIndex].ComUsu.Acepto = false;
		Declaraciones.UserList[UserIndex].ComUsu.Confirmo = false;
		Declaraciones.UserList[UserIndex].ComUsu.DestUsu = 0;

		for (i = (1); i <= (mdlCOmercioConUsuario.MAX_OFFER_SLOTS); i++) {
			Declaraciones.UserList[UserIndex].ComUsu.cant[i] = 0;
			Declaraciones.UserList[UserIndex].ComUsu.Objeto[i] = 0;
		}

		Declaraciones.UserList[UserIndex].ComUsu.GoldAmount = 0;
		Declaraciones.UserList[UserIndex].ComUsu.DestNick = "";
		Declaraciones.UserList[UserIndex].flags.Comerciando = false;

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en FinComerciarUsu. Error: " + Err.description + ". UserIndex: " + UserIndex);
	}

	static void AceptarComercioUsu(int UserIndex) {
		/* '*************************************************** */
		/* 'Autor: Unkown */
		/* 'Last Modification: 06/05/2010 */
		/*
		 * '25/11/2009: ZaMa - Ahora se traspasan hasta 5 items + oro al
		 * comerciar
		 */
		/*
		 * '06/05/2010: ZaMa - Ahora valida si los usuarios tienen los items que
		 * ofertan.
		 */
		/* '*************************************************** */
		Declaraciones.Obj TradingObj;
		int OtroUserIndex = 0;
		int OfferSlot = 0;

		Declaraciones.UserList[UserIndex].ComUsu.Acepto = true;

		OtroUserIndex = Declaraciones.UserList[UserIndex].ComUsu.DestUsu;

		/* ' User valido? */
		if (OtroUserIndex <= 0 || OtroUserIndex > Declaraciones.MaxUsers) {
			FinComerciarUsu(UserIndex);
			return;
		}

		/* ' Acepto el otro? */
		if (Declaraciones.UserList[OtroUserIndex].ComUsu.Acepto == false) {
			return;
		}

		/* ' Aceptaron ambos, chequeo que tengan los items que ofertaron */
		if (!HasOfferedItems(UserIndex)) {

			Protocol.WriteConsoleMsg(UserIndex, "¡¡¡El comercio se canceló porque no posees los ítems que ofertaste!!!",
					FontTypeNames.FONTTYPE_FIGHT);
			Protocol.WriteConsoleMsg(OtroUserIndex, "¡¡¡El comercio se canceló porque "
					+ Declaraciones.UserList[UserIndex].Name + " no posee los ítems que ofertó!!!",
					FontTypeNames.FONTTYPE_FIGHT);

			FinComerciarUsu(UserIndex);
			FinComerciarUsu(OtroUserIndex);
			Protocol.FlushBuffer(OtroUserIndex);

			return;

		} else if (!HasOfferedItems(OtroUserIndex)) {

			Protocol.WriteConsoleMsg(UserIndex, "¡¡¡El comercio se canceló porque "
					+ Declaraciones.UserList[OtroUserIndex].Name + " no posee los ítems que ofertó!!!",
					FontTypeNames.FONTTYPE_FIGHT);
			Protocol.WriteConsoleMsg(OtroUserIndex,
					"¡¡¡El comercio se canceló porque no posees los ítems que ofertaste!!!",
					FontTypeNames.FONTTYPE_FIGHT);

			FinComerciarUsu(UserIndex);
			FinComerciarUsu(OtroUserIndex);
			Protocol.FlushBuffer(OtroUserIndex);

			return;

		}

		/* ' Envio los items a quien corresponde */
		for (OfferSlot = (1); OfferSlot <= (mdlCOmercioConUsuario.MAX_OFFER_SLOTS + 1); OfferSlot++) {

			/* ' Items del 1er usuario */
			/* ' Le pasa el oro */
			if (OfferSlot == mdlCOmercioConUsuario.GOLD_OFFER_SLOT) {
				/* ' Quito la cantidad de oro ofrecida */
				Declaraciones.UserList[UserIndex].Stats.GLD = Declaraciones.UserList[UserIndex].Stats.GLD
						- Declaraciones.UserList[UserIndex].ComUsu.GoldAmount;
				/* ' Log */
				if (Declaraciones.UserList[UserIndex].ComUsu.GoldAmount >= Declaraciones.MIN_GOLD_AMOUNT_LOG) {
					General.LogDesarrollo(Declaraciones.UserList[UserIndex].Name + " soltó oro en comercio seguro con "
							+ Declaraciones.UserList[OtroUserIndex].Name + ". Cantidad: "
							+ Declaraciones.UserList[UserIndex].ComUsu.GoldAmount);
				}
				/* ' Update Usuario */
				Protocol.WriteUpdateGold(UserIndex);
				/* ' Se la doy al otro */
				Declaraciones.UserList[OtroUserIndex].Stats.GLD = Declaraciones.UserList[OtroUserIndex].Stats.GLD
						+ Declaraciones.UserList[UserIndex].ComUsu.GoldAmount;
				/* ' Update Otro Usuario */
				Protocol.WriteUpdateGold(OtroUserIndex);

				/* ' Le pasa lo ofertado de los slots con items */
			} else if (Declaraciones.UserList[UserIndex].ComUsu.Objeto[OfferSlot] > 0) {
				TradingObj.ObjIndex = Declaraciones.UserList[UserIndex].ComUsu.Objeto[OfferSlot];
				TradingObj.Amount = Declaraciones.UserList[UserIndex].ComUsu.cant[OfferSlot];

				/* 'Quita el objeto y se lo da al otro */
				if (!InvUsuario.MeterItemEnInventario(OtroUserIndex, TradingObj)) {
					InvNpc.TirarItemAlPiso(Declaraciones.UserList[OtroUserIndex].Pos, TradingObj);
				}

				Trabajo.QuitarObjetos(TradingObj.ObjIndex, TradingObj.Amount, UserIndex);

				/*
				 * 'Es un Objeto que tenemos que loguear? Pablo (ToxicWaste)
				 * 07/09/07
				 */
				if (((Declaraciones.ObjData[TradingObj.ObjIndex].Log == 1)
						|| (Declaraciones.ObjData[TradingObj.ObjIndex].OBJType == eOBJType.otLlaves))) {
					General.LogDesarrollo(Declaraciones.UserList[UserIndex].Name + " le pasó en comercio seguro a "
							+ Declaraciones.UserList[OtroUserIndex].Name + " " + TradingObj.Amount + " "
							+ Declaraciones.ObjData[TradingObj.ObjIndex].Name);
					/* 'Es mucha cantidad? */
				} else if (TradingObj.Amount >= Declaraciones.MIN_AMOUNT_LOG) {
					/* 'Si no es de los prohibidos de loguear, lo logueamos. */
					if (Declaraciones.ObjData[TradingObj.ObjIndex].NoLog != 1) {
						General.LogDesarrollo(Declaraciones.UserList[UserIndex].Name + " le pasó en comercio seguro a "
								+ Declaraciones.UserList[OtroUserIndex].Name + " " + TradingObj.Amount + " "
								+ Declaraciones.ObjData[TradingObj.ObjIndex].Name);
					}
				} else if ((TradingObj.Amount
						* Declaraciones.ObjData[TradingObj.ObjIndex].Valor) >= Declaraciones.MIN_VALUE_LOG) {
					/* 'Si no es de los prohibidos de loguear, lo logueamos. */
					if (Declaraciones.ObjData[TradingObj.ObjIndex].NoLog != 1) {
						General.LogDesarrollo(Declaraciones.UserList[UserIndex].Name + " le pasó en comercio seguro a "
								+ Declaraciones.UserList[OtroUserIndex].Name + " " + TradingObj.Amount + " "
								+ Declaraciones.ObjData[TradingObj.ObjIndex].Name);
					}
				}
			}

			/* ' Items del 2do usuario */
			/* ' Le pasa el oro */
			if (OfferSlot == mdlCOmercioConUsuario.GOLD_OFFER_SLOT) {
				/* ' Quito la cantidad de oro ofrecida */
				Declaraciones.UserList[OtroUserIndex].Stats.GLD = Declaraciones.UserList[OtroUserIndex].Stats.GLD
						- Declaraciones.UserList[OtroUserIndex].ComUsu.GoldAmount;
				/* ' Log */
				if (Declaraciones.UserList[OtroUserIndex].ComUsu.GoldAmount >= Declaraciones.MIN_GOLD_AMOUNT_LOG) {
					General.LogDesarrollo(Declaraciones.UserList[OtroUserIndex].Name
							+ " soltó oro en comercio seguro con " + Declaraciones.UserList[UserIndex].Name
							+ ". Cantidad: " + Declaraciones.UserList[OtroUserIndex].ComUsu.GoldAmount);
				}
				/* ' Update Usuario */
				Protocol.WriteUpdateGold(OtroUserIndex);
				/* 'y se la doy al otro */
				Declaraciones.UserList[UserIndex].Stats.GLD = Declaraciones.UserList[UserIndex].Stats.GLD
						+ Declaraciones.UserList[OtroUserIndex].ComUsu.GoldAmount;

				/* ' Update Otro Usuario */
				Protocol.WriteUpdateGold(UserIndex);

				/* ' Le pasa la oferta de los slots con items */
			} else if (Declaraciones.UserList[OtroUserIndex].ComUsu.Objeto[OfferSlot] > 0) {
				TradingObj.ObjIndex = Declaraciones.UserList[OtroUserIndex].ComUsu.Objeto[OfferSlot];
				TradingObj.Amount = Declaraciones.UserList[OtroUserIndex].ComUsu.cant[OfferSlot];

				/* 'Quita el objeto y se lo da al otro */
				if (!InvUsuario.MeterItemEnInventario(UserIndex, TradingObj)) {
					InvNpc.TirarItemAlPiso(Declaraciones.UserList[UserIndex].Pos, TradingObj);
				}

				Trabajo.QuitarObjetos(TradingObj.ObjIndex, TradingObj.Amount, OtroUserIndex);

				/*
				 * 'Es un Objeto que tenemos que loguear? Pablo (ToxicWaste)
				 * 07/09/07
				 */
				if (((Declaraciones.ObjData[TradingObj.ObjIndex].Log == 1)
						|| (Declaraciones.ObjData[TradingObj.ObjIndex].OBJType == eOBJType.otLlaves))) {
					General.LogDesarrollo(Declaraciones.UserList[OtroUserIndex].Name + " le pasó en comercio seguro a "
							+ Declaraciones.UserList[UserIndex].Name + " " + TradingObj.Amount + " "
							+ Declaraciones.ObjData[TradingObj.ObjIndex].Name);
					/* 'Es mucha cantidad? */
				} else if (TradingObj.Amount >= Declaraciones.MIN_AMOUNT_LOG) {
					/* 'Si no es de los prohibidos de loguear, lo logueamos. */
					if (Declaraciones.ObjData[TradingObj.ObjIndex].NoLog != 1) {
						General.LogDesarrollo(Declaraciones.UserList[OtroUserIndex].Name
								+ " le pasó en comercio seguro a " + Declaraciones.UserList[UserIndex].Name + " "
								+ TradingObj.Amount + " " + Declaraciones.ObjData[TradingObj.ObjIndex].Name);
					}
				} else if ((TradingObj.Amount
						* Declaraciones.ObjData[TradingObj.ObjIndex].Valor) >= Declaraciones.MIN_VALUE_LOG) {
					/* 'Si no es de los prohibidos de loguear, lo logueamos. */
					if (Declaraciones.ObjData[TradingObj.ObjIndex].NoLog != 1) {
						General.LogDesarrollo(Declaraciones.UserList[OtroUserIndex].Name
								+ " le pasó en comercio seguro a " + Declaraciones.UserList[UserIndex].Name + " "
								+ TradingObj.Amount + " " + Declaraciones.ObjData[TradingObj.ObjIndex].Name);
					}
				}
			}

		}

		/* ' End Trade */
		FinComerciarUsu(UserIndex);
		FinComerciarUsu(OtroUserIndex);
		Protocol.FlushBuffer(OtroUserIndex);

	}

	static void AgregarOferta(int UserIndex, int OfferSlot, int ObjIndex, int Amount, boolean IsGold) {
		/* '*************************************************** */
		/* 'Autor: ZaMa */
		/* 'Last Modification: 24/11/2009 */
		/* 'Adds gold or items to the user's offer */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		/* ' Si ya confirmo su oferta, no puede cambiarla! */
		if (!Declaraciones.UserList[UserIndex].ComUsu.Confirmo) {
			if (IsGold) {
				/* ' Agregamos (o quitamos) mas oro a la oferta */
				Declaraciones.UserList[UserIndex].ComUsu.GoldAmount = Declaraciones.UserList[UserIndex].ComUsu.GoldAmount
						+ Amount;

				/* ' Imposible que pase, pero por las dudas.. */
				if (Declaraciones.UserList[UserIndex].ComUsu.GoldAmount < 0) {
					Declaraciones.UserList[UserIndex].ComUsu.GoldAmount = 0;
				}
			} else {
				/*
				 * ' Agreamos (o quitamos) el item y su cantidad en el slot
				 * correspondiente
				 */
				/* ' Si es 0 estoy modificando la cantidad, no agregando */
				if (ObjIndex > 0) {
					Declaraciones.UserList[UserIndex].ComUsu.Objeto[OfferSlot] = ObjIndex;
				}
				Declaraciones.UserList[UserIndex].ComUsu.cant[OfferSlot] = Declaraciones.UserList[UserIndex].ComUsu.cant[OfferSlot]
						+ Amount;

				/* 'Quitó todos los items de ese tipo */
				if (Declaraciones.UserList[UserIndex].ComUsu.cant[OfferSlot] <= 0) {
					/* ' Removemos el objeto para evitar conflictos */
					Declaraciones.UserList[UserIndex].ComUsu.Objeto[OfferSlot] = 0;
					Declaraciones.UserList[UserIndex].ComUsu.cant[OfferSlot] = 0;
				}
			}
		}

		return;
		/* FIXME: ErrHandler : */
		General.LogError("Error en AgregarOferta. Error: " + Err.description + ". UserIndex: " + UserIndex);
	}

	static boolean PuedeSeguirComerciando(int UserIndex) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Autor: ZaMa */
		/* 'Last Modification: 24/11/2009 */
		/*
		 * 'Validates wether the conditions for the commerce to keep going are
		 * satisfied
		 */
		/* '*************************************************** */
		int OtroUserIndex = 0;
		boolean ComercioInvalido = false;

		/* FIXME: ON ERROR GOTO ErrHandler */

		OtroUserIndex = Declaraciones.UserList[UserIndex].ComUsu.DestUsu;

		/* ' Usuario valido? */
		if (OtroUserIndex <= 0 || OtroUserIndex > Declaraciones.MaxUsers) {
			ComercioInvalido = true;
		}

		if (!ComercioInvalido) {
			/* ' Estan logueados? */
			if (Declaraciones.UserList[OtroUserIndex].flags.UserLogged == false
					|| Declaraciones.UserList[UserIndex].flags.UserLogged == false) {
				ComercioInvalido = true;
			}
		}

		if (!ComercioInvalido) {
			/* ' Se estan comerciando el uno al otro? */
			if (Declaraciones.UserList[OtroUserIndex].ComUsu.DestUsu != UserIndex) {
				ComercioInvalido = true;
			}
		}

		if (!ComercioInvalido) {
			/* ' El nombre del otro es el mismo que al que le comercio? */
			if (Declaraciones.UserList[OtroUserIndex].Name != Declaraciones.UserList[UserIndex].ComUsu.DestNick) {
				ComercioInvalido = true;
			}
		}

		if (!ComercioInvalido) {
			/* ' Mi nombre es el mismo que al que el le comercia? */
			if (Declaraciones.UserList[UserIndex].Name != Declaraciones.UserList[OtroUserIndex].ComUsu.DestNick) {
				ComercioInvalido = true;
			}
		}

		if (!ComercioInvalido) {
			/* ' Esta vivo? */
			if (Declaraciones.UserList[OtroUserIndex].flags.Muerto == 1) {
				ComercioInvalido = true;
			}
		}

		/* ' Fin del comercio */
		if (ComercioInvalido == true) {
			FinComerciarUsu(UserIndex);

			if (OtroUserIndex > 0 && OtroUserIndex <= Declaraciones.MaxUsers) {
				FinComerciarUsu(OtroUserIndex);
				Protocol.FlushBuffer(OtroUserIndex);
			}

			return retval;
		}

		retval = true;

		return retval;

		/* FIXME: ErrHandler : */
		General.LogError("Error en PuedeSeguirComerciando. Error: " + Err.description + ". UserIndex: " + UserIndex);
		return retval;
	}

	static boolean HasOfferedItems(int UserIndex) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Autor: ZaMa */
		/* 'Last Modification: 05/06/2010 */
		/*
		 * 'Checks whether the user has the offered items in his inventory or
		 * not.
		 */
		/* '*************************************************** */

		tOfferItem[] OfferedItems;
		int Slot = 0;
		int SlotAux = 0;
		int SlotCount = 0;

		int ObjIndex = 0;

		/* ' Agrupo los items que son iguales */
		for (Slot = (1); Slot <= (mdlCOmercioConUsuario.MAX_OFFER_SLOTS); Slot++) {

			ObjIndex = Declaraciones.UserList[UserIndex].ComUsu.Objeto[Slot];

			if (ObjIndex > 0) {

				for (SlotAux = (0); SlotAux <= (SlotCount - 1); SlotAux++) {

					if (ObjIndex == OfferedItems[SlotAux].ObjIndex) {
						/* ' Son iguales, aumento la cantidad */
						OfferedItems[SlotAux].Amount = OfferedItems[SlotAux].Amount
								+ Declaraciones.UserList[UserIndex].ComUsu.cant[Slot];
						break; /* FIXME: EXIT FOR */
					}

				}

				/* ' No encontro otro igual, lo agrego */
				if (SlotAux == SlotCount) {
					OfferedItems[SlotCount].ObjIndex = ObjIndex;
					OfferedItems[SlotCount].Amount = Declaraciones.UserList[UserIndex].ComUsu.cant[Slot];

					SlotCount = SlotCount + 1;
				}

			}

		}

		/* ' Chequeo que tengan la cantidad en el inventario */
		for (Slot = (0); Slot <= (SlotCount - 1); Slot++) {
			if (!UsUaRiOs.HasEnoughItems(UserIndex, OfferedItems[Slot].ObjIndex, OfferedItems[Slot].Amount)) {
				return retval;
			}
		}

		/* ' Compruebo que tenga el oro que oferta */
		if (Declaraciones.UserList[UserIndex].Stats.GLD < Declaraciones.UserList[UserIndex].ComUsu.GoldAmount) {
			return retval;
		}

		retval = true;

		return retval;
	}

}