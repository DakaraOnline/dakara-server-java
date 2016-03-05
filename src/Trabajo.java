
/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"Trabajo"')] */
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

public class Trabajo {

	static final int GASTO_ENERGIA_TRABAJADOR = 2;
	static final int GASTO_ENERGIA_NO_TRABAJADOR = 6;

	static void DoPermanecerOculto(int UserIndex) {
		/* '******************************************************** */
		/* 'Autor: Nacho (Integer) */
		/* 'Last Modif: 11/19/2009 */
		/* 'Chequea si ya debe mostrarse */
		/*
		 * 'Pablo (ToxicWaste): Cambie los ordenes de prioridades porque sino no
		 * andaba.
		 */
		/*
		 * '13/01/2010: ZaMa - Now hidden on boat pirats recover the proper boat
		 * body.
		 */
		/*
		 * '13/01/2010: ZaMa - Arreglo condicional para que el bandido camine
		 * oculto.
		 */
		/* '******************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].Counters.TiempoOculto = Declaraciones.UserList[UserIndex].Counters.TiempoOculto
				- 1;
		if (Declaraciones.UserList[UserIndex].Counters.TiempoOculto <= 0) {
			if (Declaraciones.UserList[UserIndex].clase == eClass.Hunter
					&& Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Ocultarse] > 90) {
				if (Declaraciones.UserList[UserIndex].Invent.ArmourEqpObjIndex == 648
						|| Declaraciones.UserList[UserIndex].Invent.ArmourEqpObjIndex == 360) {
					Declaraciones.UserList[UserIndex].Counters.TiempoOculto = Admin.IntervaloOculto;
					return;
				}
			}
			Declaraciones.UserList[UserIndex].Counters.TiempoOculto = 0;
			Declaraciones.UserList[UserIndex].flags.Oculto = 0;

			if (Declaraciones.UserList[UserIndex].flags.Navegando == 1) {
				if (Declaraciones.UserList[UserIndex].clase == eClass.Pirat) {
					/* ' Pierde la apariencia de fragata fantasmal */
					UsUaRiOs.ToggleBoatBody(UserIndex);
					Protocol.WriteConsoleMsg(UserIndex, "¡Has recuperado tu apariencia normal!",
							FontTypeNames.FONTTYPE_INFO);
					UsUaRiOs.ChangeUserChar(UserIndex, Declaraciones.UserList[UserIndex].Char.body,
							Declaraciones.UserList[UserIndex].Char.Head, Declaraciones.UserList[UserIndex].Char.heading,
							Declaraciones.NingunArma, Declaraciones.NingunEscudo, Declaraciones.NingunCasco);
				}
			} else {
				if (Declaraciones.UserList[UserIndex].flags.invisible == 0) {
					Protocol.WriteConsoleMsg(UserIndex, "Has vuelto a ser visible.", FontTypeNames.FONTTYPE_INFO);
					UsUaRiOs.SetInvisible(UserIndex, Declaraciones.UserList[UserIndex].Char.CharIndex, false);
				}
			}
		}

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en Sub DoPermanecerOculto");

	}

	static void DoOcultarse(int UserIndex) {
 /* '*************************************************** */
 /* 'Author: Unknown */
 /* 'Last Modification: 13/01/2010 (ZaMa) */
 /* 'Pablo (ToxicWaste): No olvidar agregar IntervaloOculto=500 al Server.ini. */
 /* 'Modifique la fórmula y ahora anda bien. */
 /* '13/01/2010: ZaMa - El pirata se transforma en galeon fantasmal cuando se oculta en agua. */
 /* '*************************************************** */
 
 /* FIXME: ON ERROR GOTO ErrHandler */
 
 double Suerte = 0.0;
 int res = 0;
 int Skill = 0;
 
  Skill = Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Ocultarse];
  
  Suerte = (((0.000002*Skill-0.0002)*Skill+0.0064)*Skill+0.1124)*100;
  
  res = Matematicas.RandomNumber(1, 100);
  
   if (res<=Suerte) {
   
   Declaraciones.UserList[UserIndex].flags.Oculto = 1;
   Suerte = (-0.000001*(100-Skill) $ 3);
   Suerte = Suerte+(0.00009229*(100-Skill) $ 2);
   Suerte = Suerte+(-0.0088*(100-Skill));
   Suerte = Suerte+(0.9571);
   Suerte = Suerte*Admin.IntervaloOculto;
   
    if (Declaraciones.UserList[UserIndex].clase == eClass.Bandit) {
    Declaraciones.UserList[UserIndex].Counters.TiempoOculto = vb6.Int(Suerte/2);
    } else {
    Declaraciones.UserList[UserIndex].Counters.TiempoOculto = Suerte;
   }
   
   /* ' No es pirata o es uno sin barca */
    if (Declaraciones.UserList[UserIndex].flags.Navegando == 0) {
    UsUaRiOs.SetInvisible(UserIndex, Declaraciones.UserList[UserIndex].Char.CharIndex, true);
    
    Protocol.WriteConsoleMsg(UserIndex, "¡Te has escondido entre las sombras!", FontTypeNames.FONTTYPE_INFO);
    /* ' Es un pirata navegando */
    } else {
    /* ' Le cambiamos el body a galeon fantasmal */
    Declaraciones.UserList[UserIndex].Char.body = Declaraciones.iFragataFantasmal;
    /* ' Actualizamos clientes */
    UsUaRiOs.ChangeUserChar(UserIndex, Declaraciones.UserList[UserIndex].Char.body, Declaraciones.UserList[UserIndex].Char.Head, Declaraciones.UserList[UserIndex].Char.heading, Declaraciones.NingunArma, Declaraciones.NingunEscudo, Declaraciones.NingunCasco);
   }
   
   UsUaRiOs.SubirSkill(UserIndex, eSkill.Ocultarse, true);
   } else {
   /* '[CDT 17-02-2004] */
    if (!Declaraciones.UserList[UserIndex].flags.UltimoMensaje == 4) {
    Protocol.WriteConsoleMsg(UserIndex, "¡No has logrado esconderte!", FontTypeNames.FONTTYPE_INFO);
    Declaraciones.UserList[UserIndex].flags.UltimoMensaje = 4;
   }
   /* '[/CDT] */
   
   UsUaRiOs.SubirSkill(UserIndex, eSkill.Ocultarse, false);
  }
  
  Declaraciones.UserList[UserIndex].Counters.Ocultando = Declaraciones.UserList[UserIndex].Counters.Ocultando+1;
 
 return;
 
 /* FIXME: ErrHandler : */
 General.LogError("Error en Sub DoOcultarse");
 
}

	static void DoNavega(int UserIndex,
			Declaraciones.ObjData /* FIXME BYREF!! */ Barco, int Slot) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 13/01/2010 (ZaMa) */
		/*
		 * '13/01/2010: ZaMa - El pirata pierde el ocultar si desequipa barca.
		 */
		/*
		 * '16/09/2010: ZaMa - Ahora siempre se va el invi para los clientes al
		 * equipar la barca (Evita cortes de cabeza).
		 */
		/*
		 * '10/12/2010: Pato - Limpio las variables del inventario que hacen
		 * referencia a la barca, sino el pirata que la última barca que equipo
		 * era el galeón no explotaba(Y capaz no la tenía equipada :P).
		 */
		/* '*************************************************** */

		float ModNave = 0.0f;

		ModNave = ModNavegacion(Declaraciones.UserList[UserIndex].clase, UserIndex);

		if (Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Navegacion] / ModNave < Barco.MinSkill) {
			Protocol.WriteConsoleMsg(UserIndex, "No tienes suficientes conocimientos para usar este barco.",
					FontTypeNames.FONTTYPE_INFO);
			Protocol.WriteConsoleMsg(UserIndex,
					"Para usar este barco necesitas " + Barco.MinSkill * ModNave + " puntos en navegacion.",
					FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/* ' No estaba navegando */
		if (Declaraciones.UserList[UserIndex].flags.Navegando == 0) {
			Declaraciones.UserList[UserIndex].Invent.BarcoObjIndex = Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex;
			Declaraciones.UserList[UserIndex].Invent.BarcoSlot = Slot;

			Declaraciones.UserList[UserIndex].Char.Head = 0;

			/* ' No esta muerto */
			if (Declaraciones.UserList[UserIndex].flags.Muerto == 0) {

				UsUaRiOs.ToggleBoatBody(UserIndex);

				/* ' Pierde el ocultar */
				if (Declaraciones.UserList[UserIndex].flags.Oculto == 1) {
					Declaraciones.UserList[UserIndex].flags.Oculto = 0;
					UsUaRiOs.SetInvisible(UserIndex, Declaraciones.UserList[UserIndex].Char.CharIndex, false);
					Protocol.WriteConsoleMsg(UserIndex, "¡Has vuelto a ser visible!", FontTypeNames.FONTTYPE_INFO);
				}

				/*
				 * ' Siempre se ve la barca (Nunca esta invisible), pero solo
				 * para el cliente.
				 */
				if (Declaraciones.UserList[UserIndex].flags.invisible == 1) {
					UsUaRiOs.SetInvisible(UserIndex, Declaraciones.UserList[UserIndex].Char.CharIndex, false);
				}

				/* ' Esta muerto */
			} else {
				Declaraciones.UserList[UserIndex].Char.body = Declaraciones.iFragataFantasmal;
				Declaraciones.UserList[UserIndex].Char.ShieldAnim = Declaraciones.NingunEscudo;
				Declaraciones.UserList[UserIndex].Char.WeaponAnim = Declaraciones.NingunArma;
				Declaraciones.UserList[UserIndex].Char.CascoAnim = Declaraciones.NingunCasco;
			}

			/* ' Comienza a navegar */
			Declaraciones.UserList[UserIndex].flags.Navegando = 1;

			/* ' Estaba navegando */
		} else {
			Declaraciones.UserList[UserIndex].Invent.BarcoObjIndex = 0;
			Declaraciones.UserList[UserIndex].Invent.BarcoSlot = 0;

			/* ' No esta muerto */
			if (Declaraciones.UserList[UserIndex].flags.Muerto == 0) {
				Declaraciones.UserList[UserIndex].Char.Head = Declaraciones.UserList[UserIndex].OrigChar.Head;

				if (Declaraciones.UserList[UserIndex].clase == eClass.Pirat) {
					if (Declaraciones.UserList[UserIndex].flags.Oculto == 1) {
						/* ' Al desequipar barca, perdió el ocultar */
						Declaraciones.UserList[UserIndex].flags.Oculto = 0;
						Declaraciones.UserList[UserIndex].Counters.Ocultando = 0;
						Protocol.WriteConsoleMsg(UserIndex, "¡Has recuperado tu apariencia normal!",
								FontTypeNames.FONTTYPE_INFO);
					}
				}

				if (Declaraciones.UserList[UserIndex].Invent.ArmourEqpObjIndex > 0) {
					Declaraciones.UserList[UserIndex].Char.body = Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.ArmourEqpObjIndex].Ropaje;
				} else {
					General.DarCuerpoDesnudo(UserIndex);
				}

				if (Declaraciones.UserList[UserIndex].Invent.EscudoEqpObjIndex > 0) {
					Declaraciones.UserList[UserIndex].Char.ShieldAnim = Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.EscudoEqpObjIndex].ShieldAnim;
				}
				if (Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex > 0) {
					Declaraciones.UserList[UserIndex].Char.WeaponAnim = UsUaRiOs.GetWeaponAnim(UserIndex,
							Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex);
				}
				if (Declaraciones.UserList[UserIndex].Invent.CascoEqpObjIndex > 0) {
					Declaraciones.UserList[UserIndex].Char.CascoAnim = Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.CascoEqpObjIndex].CascoAnim;
				}

				/*
				 * ' Al dejar de navegar, si estaba invisible actualizo los
				 * clientes
				 */
				if (Declaraciones.UserList[UserIndex].flags.invisible == 1) {
					UsUaRiOs.SetInvisible(UserIndex, Declaraciones.UserList[UserIndex].Char.CharIndex, true);
				}

				/* ' Esta muerto */
			} else {
				Declaraciones.UserList[UserIndex].Char.body = Declaraciones.iCuerpoMuerto;
				Declaraciones.UserList[UserIndex].Char.Head = Declaraciones.iCabezaMuerto;
				Declaraciones.UserList[UserIndex].Char.ShieldAnim = Declaraciones.NingunEscudo;
				Declaraciones.UserList[UserIndex].Char.WeaponAnim = Declaraciones.NingunArma;
				Declaraciones.UserList[UserIndex].Char.CascoAnim = Declaraciones.NingunCasco;
			}

			/* ' Termina de navegar */
			Declaraciones.UserList[UserIndex].flags.Navegando = 0;
		}

		/* ' Actualizo clientes */
		UsUaRiOs.ChangeUserChar(UserIndex, Declaraciones.UserList[UserIndex].Char.body,
				Declaraciones.UserList[UserIndex].Char.Head, Declaraciones.UserList[UserIndex].Char.heading,
				Declaraciones.UserList[UserIndex].Char.WeaponAnim, Declaraciones.UserList[UserIndex].Char.ShieldAnim,
				Declaraciones.UserList[UserIndex].Char.CascoAnim);

		Protocol.WriteNavigateToggle(UserIndex);

	}

	static void FundirMineral(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		if (Declaraciones.UserList[UserIndex].flags.TargetObjInvIndex > 0) {

			if (Declaraciones.ObjData[Declaraciones.UserList[UserIndex].flags.TargetObjInvIndex].OBJType == eOBJType.otMinerales
					&& Declaraciones.ObjData[Declaraciones.UserList[UserIndex].flags.TargetObjInvIndex].MinSkill <= Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Mineria]
							/ ModFundicion(Declaraciones.UserList[UserIndex].clase)) {
				DoLingotes(UserIndex);
			} else {
				Protocol.WriteConsoleMsg(UserIndex,
						"No tienes conocimientos de minería suficientes para trabajar este mineral.",
						FontTypeNames.FONTTYPE_INFO);
			}

		}

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en FundirMineral. Error " + Err.Number + " : " + Err.description);

	}

	static void FundirArmas(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */
		if (Declaraciones.UserList[UserIndex].flags.TargetObjInvIndex > 0) {
			if (Declaraciones.ObjData[Declaraciones.UserList[UserIndex].flags.TargetObjInvIndex].OBJType == eOBJType.otWeapon) {
				if (Declaraciones.ObjData[Declaraciones.UserList[UserIndex].flags.TargetObjInvIndex].SkHerreria <= Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Herreria]
						/ ModHerreriA(Declaraciones.UserList[UserIndex].clase)) {
					DoFundir(UserIndex);
				} else {
					Protocol.WriteConsoleMsg(UserIndex,
							"No tienes los conocimientos suficientes en herrería para fundir este objeto.",
							FontTypeNames.FONTTYPE_INFO);
				}
			}
		}

		return;
		/* FIXME: ErrHandler : */
		General.LogError("Error en FundirArmas. Error " + Err.Number + " : " + Err.description);
	}

	static boolean TieneObjetos(int ItemIndex, int cant, int UserIndex) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 10/07/2010 */
		/* '10/07/2010: ZaMa - Ahora cant es long para evitar un overflow. */
		/* '*************************************************** */

		int i = 0;
		int Total = 0;
		for (i = (1); i <= (Declaraciones.UserList[UserIndex].CurrentInventorySlots); i++) {
			if (Declaraciones.UserList[UserIndex].Invent.Object[i].ObjIndex == ItemIndex) {
				Total = Total + Declaraciones.UserList[UserIndex].Invent.Object[i].Amount;
			}
		}

		if (cant <= Total) {
			retval = true;
			return retval;
		}

		return retval;
	}

	static void QuitarObjetos(int ItemIndex, int cant, int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 05/08/09 */
		/*
		 * '05/08/09: Pato - Cambie la funcion a procedimiento ya que se usa
		 * como procedimiento siempre, y fixie el bug 2788199
		 */
		/* '*************************************************** */

		int i = 0;
		for (i = (1); i <= (Declaraciones.UserList[UserIndex].CurrentInventorySlots); i++) {
			if (Declaraciones.UserList[UserIndex].Invent.Object[i].ObjIndex == ItemIndex) {
				if (Declaraciones.UserList[UserIndex].Invent.Object[i].Amount <= cant
						&& Declaraciones.UserList[UserIndex].Invent.Object[i].Equipped == 1) {
					InvUsuario.Desequipar(UserIndex, i, true);
				}

				Declaraciones.UserList[UserIndex].Invent.Object[i].Amount = Declaraciones.UserList[UserIndex].Invent.Object[i].Amount
						- cant;
				if (Declaraciones.UserList[UserIndex].Invent.Object[i].Amount <= 0) {
					cant = vb6.Abs(Declaraciones.UserList[UserIndex].Invent.Object[i].Amount);
					Declaraciones.UserList[UserIndex].Invent.NroItems = Declaraciones.UserList[UserIndex].Invent.NroItems
							- 1;
					Declaraciones.UserList[UserIndex].Invent.Object[i].Amount = 0;
					Declaraciones.UserList[UserIndex].Invent.Object[i].ObjIndex = 0;
				} else {
					cant = 0;
				}

				InvUsuario.UpdateUserInv(false, UserIndex, i);

				if (cant == 0) {
					return;
				}
			}
		}

	}

	static void HerreroQuitarMateriales(int UserIndex, int ItemIndex, int CantidadItems) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 16/11/2009 */
		/*
		 * '16/11/2009: ZaMa - Ahora considera la cantidad de items a construir
		 */
		/* '*************************************************** */
		if (Declaraciones.ObjData[ItemIndex].LingH > 0) {
			QuitarObjetos(Declaraciones.LingoteHierro, Declaraciones.ObjData[ItemIndex].LingH * CantidadItems,
					UserIndex);
		}
		if (Declaraciones.ObjData[ItemIndex].LingP > 0) {
			QuitarObjetos(Declaraciones.LingotePlata, Declaraciones.ObjData[ItemIndex].LingP * CantidadItems,
					UserIndex);
		}
		if (Declaraciones.ObjData[ItemIndex].LingO > 0) {
			QuitarObjetos(Declaraciones.LingoteOro, Declaraciones.ObjData[ItemIndex].LingO * CantidadItems, UserIndex);
		}
	}

	static void CarpinteroQuitarMateriales(int UserIndex, int ItemIndex, int CantidadItems) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 16/11/2009 */
		/* '16/11/2009: ZaMa - Ahora quita tambien madera elfica */
		/* '*************************************************** */
		if (Declaraciones.ObjData[ItemIndex].Madera > 0) {
			QuitarObjetos(Declaraciones.Lena, Declaraciones.ObjData[ItemIndex].Madera * CantidadItems, UserIndex);
		}
		if (Declaraciones.ObjData[ItemIndex].MaderaElfica > 0) {
			QuitarObjetos(Declaraciones.LenaElfica, Declaraciones.ObjData[ItemIndex].MaderaElfica * CantidadItems,
					UserIndex);
		}
	}

	static boolean CarpinteroTieneMateriales(int UserIndex, int ItemIndex, int Cantidad) {
		return CarpinteroTieneMateriales(UserIndex, ItemIndex, Cantidad, false);
	}

	static boolean CarpinteroTieneMateriales(int UserIndex, int ItemIndex, int Cantidad, boolean ShowMsg) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 16/11/2009 */
		/* '16/11/2009: ZaMa - Agregada validacion a madera elfica. */
		/*
		 * '16/11/2009: ZaMa - Ahora considera la cantidad de items a construir
		 */
		/* '*************************************************** */

		if (Declaraciones.ObjData[ItemIndex].Madera > 0) {
			if (!TieneObjetos(Declaraciones.Lena, Declaraciones.ObjData[ItemIndex].Madera * Cantidad, UserIndex)) {
				if (ShowMsg) {
					Protocol.WriteConsoleMsg(UserIndex, "No tienes suficiente madera.", FontTypeNames.FONTTYPE_INFO);
				}
				retval = false;
				return retval;
			}
		}

		if (Declaraciones.ObjData[ItemIndex].MaderaElfica > 0) {
			if (!TieneObjetos(Declaraciones.LenaElfica, Declaraciones.ObjData[ItemIndex].MaderaElfica * Cantidad,
					UserIndex)) {
				if (ShowMsg) {
					Protocol.WriteConsoleMsg(UserIndex, "No tienes suficiente madera élfica.",
							FontTypeNames.FONTTYPE_INFO);
				}
				retval = false;
				return retval;
			}
		}

		retval = true;

		return retval;
	}

	static boolean HerreroTieneMateriales(int UserIndex, int ItemIndex, int CantidadItems) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 16/11/2009 */
		/* '16/11/2009: ZaMa - Agregada validacion a madera elfica. */
		/* '*************************************************** */
		if (Declaraciones.ObjData[ItemIndex].LingH > 0) {
			if (!TieneObjetos(Declaraciones.LingoteHierro, Declaraciones.ObjData[ItemIndex].LingH * CantidadItems,
					UserIndex)) {
				Protocol.WriteConsoleMsg(UserIndex, "No tienes suficientes lingotes de hierro.",
						FontTypeNames.FONTTYPE_INFO);
				retval = false;
				return retval;
			}
		}
		if (Declaraciones.ObjData[ItemIndex].LingP > 0) {
			if (!TieneObjetos(Declaraciones.LingotePlata, Declaraciones.ObjData[ItemIndex].LingP * CantidadItems,
					UserIndex)) {
				Protocol.WriteConsoleMsg(UserIndex, "No tienes suficientes lingotes de plata.",
						FontTypeNames.FONTTYPE_INFO);
				retval = false;
				return retval;
			}
		}
		if (Declaraciones.ObjData[ItemIndex].LingO > 0) {
			if (!TieneObjetos(Declaraciones.LingoteOro, Declaraciones.ObjData[ItemIndex].LingO * CantidadItems,
					UserIndex)) {
				Protocol.WriteConsoleMsg(UserIndex, "No tienes suficientes lingotes de oro.",
						FontTypeNames.FONTTYPE_INFO);
				retval = false;
				return retval;
			}
		}
		retval = true;
		return retval;
	}

	static boolean TieneMaterialesUpgrade(int UserIndex, int ItemIndex) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Torres Patricio (Pato) */
		/* 'Last Modification: 12/08/2009 */
		/* ' */
		/* '*************************************************** */
		int ItemUpgrade = 0;

		ItemUpgrade = Declaraciones.ObjData[ItemIndex].Upgrade;

		if (Declaraciones.ObjData[ItemUpgrade].LingH > 0) {
			if (!TieneObjetos(Declaraciones.LingoteHierro,
					vb6.CInt(Declaraciones.ObjData[ItemUpgrade].LingH
							- Declaraciones.ObjData[ItemIndex].LingH * Declaraciones.PORCENTAJE_MATERIALES_UPGRADE),
					UserIndex)) {
				Protocol.WriteConsoleMsg(UserIndex, "No tienes suficientes lingotes de hierro.",
						FontTypeNames.FONTTYPE_INFO);
				retval = false;
				return retval;
			}
		}

		if (Declaraciones.ObjData[ItemUpgrade].LingP > 0) {
			if (!TieneObjetos(Declaraciones.LingotePlata,
					vb6.CInt(Declaraciones.ObjData[ItemUpgrade].LingP
							- Declaraciones.ObjData[ItemIndex].LingP * Declaraciones.PORCENTAJE_MATERIALES_UPGRADE),
					UserIndex)) {
				Protocol.WriteConsoleMsg(UserIndex, "No tienes suficientes lingotes de plata.",
						FontTypeNames.FONTTYPE_INFO);
				retval = false;
				return retval;
			}
		}

		if (Declaraciones.ObjData[ItemUpgrade].LingO > 0) {
			if (!TieneObjetos(Declaraciones.LingoteOro,
					vb6.CInt(Declaraciones.ObjData[ItemUpgrade].LingO
							- Declaraciones.ObjData[ItemIndex].LingO * Declaraciones.PORCENTAJE_MATERIALES_UPGRADE),
					UserIndex)) {
				Protocol.WriteConsoleMsg(UserIndex, "No tienes suficientes lingotes de oro.",
						FontTypeNames.FONTTYPE_INFO);
				retval = false;
				return retval;
			}
		}

		if (Declaraciones.ObjData[ItemUpgrade].Madera > 0) {
			if (!TieneObjetos(Declaraciones.Lena,
					vb6.CInt(Declaraciones.ObjData[ItemUpgrade].Madera
							- Declaraciones.ObjData[ItemIndex].Madera * Declaraciones.PORCENTAJE_MATERIALES_UPGRADE),
					UserIndex)) {
				Protocol.WriteConsoleMsg(UserIndex, "No tienes suficiente madera.", FontTypeNames.FONTTYPE_INFO);
				retval = false;
				return retval;
			}
		}

		if (Declaraciones.ObjData[ItemUpgrade].MaderaElfica > 0) {
			if (!TieneObjetos(Declaraciones.LenaElfica, vb6.CInt(Declaraciones.ObjData[ItemUpgrade].MaderaElfica
					- Declaraciones.ObjData[ItemIndex].MaderaElfica * Declaraciones.PORCENTAJE_MATERIALES_UPGRADE),
					UserIndex)) {
				Protocol.WriteConsoleMsg(UserIndex, "No tienes suficiente madera élfica.", FontTypeNames.FONTTYPE_INFO);
				retval = false;
				return retval;
			}
		}

		retval = true;
		return retval;
	}

	static void QuitarMaterialesUpgrade(int UserIndex, int ItemIndex) {
		/* '*************************************************** */
		/* 'Author: Torres Patricio (Pato) */
		/* 'Last Modification: 12/08/2009 */
		/* ' */
		/* '*************************************************** */
		int ItemUpgrade = 0;

		ItemUpgrade = Declaraciones.ObjData[ItemIndex].Upgrade;

		if (Declaraciones.ObjData[ItemUpgrade].LingH > 0) {
			QuitarObjetos(Declaraciones.LingoteHierro,
					vb6.CInt(Declaraciones.ObjData[ItemUpgrade].LingH
							- Declaraciones.ObjData[ItemIndex].LingH * Declaraciones.PORCENTAJE_MATERIALES_UPGRADE),
					UserIndex);
		}
		if (Declaraciones.ObjData[ItemUpgrade].LingP > 0) {
			QuitarObjetos(Declaraciones.LingotePlata,
					vb6.CInt(Declaraciones.ObjData[ItemUpgrade].LingP
							- Declaraciones.ObjData[ItemIndex].LingP * Declaraciones.PORCENTAJE_MATERIALES_UPGRADE),
					UserIndex);
		}
		if (Declaraciones.ObjData[ItemUpgrade].LingO > 0) {
			QuitarObjetos(Declaraciones.LingoteOro,
					vb6.CInt(Declaraciones.ObjData[ItemUpgrade].LingO
							- Declaraciones.ObjData[ItemIndex].LingO * Declaraciones.PORCENTAJE_MATERIALES_UPGRADE),
					UserIndex);
		}
		if (Declaraciones.ObjData[ItemUpgrade].Madera > 0) {
			QuitarObjetos(Declaraciones.Lena,
					vb6.CInt(Declaraciones.ObjData[ItemUpgrade].Madera
							- Declaraciones.ObjData[ItemIndex].Madera * Declaraciones.PORCENTAJE_MATERIALES_UPGRADE),
					UserIndex);
		}
		if (Declaraciones.ObjData[ItemUpgrade].MaderaElfica > 0) {
			QuitarObjetos(Declaraciones.LenaElfica, vb6.CInt(Declaraciones.ObjData[ItemUpgrade].MaderaElfica
					- Declaraciones.ObjData[ItemIndex].MaderaElfica * Declaraciones.PORCENTAJE_MATERIALES_UPGRADE),
					UserIndex);
		}

		QuitarObjetos(ItemIndex, 1, UserIndex);
	}

	static boolean PuedeConstruir(int UserIndex, int ItemIndex, int CantidadItems) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 24/08/2009 */
		/* '24/08/2008: ZaMa - Validates if the player has the required skill */
		/*
		 * '16/11/2009: ZaMa - Validates if the player has the required amount
		 * of materials, depending on the number of items to make
		 */
		/* '*************************************************** */
		retval = HerreroTieneMateriales(UserIndex, ItemIndex, CantidadItems) && vb6.Round(
				Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Herreria]
						/ ModHerreriA(Declaraciones.UserList[UserIndex].clase),
				0) >= Declaraciones.ObjData[ItemIndex].SkHerreria;
		return retval;
	}

	static boolean PuedeConstruirHerreria(int ItemIndex) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */
		int i = 0;

		for (i = (1); i <= (vb6.UBound(Declaraciones.ArmasHerrero)); i++) {
			if (Declaraciones.ArmasHerrero[i] == ItemIndex) {
				retval = true;
				return retval;
			}
		}
		for (i = (1); i <= (vb6.UBound(Declaraciones.ArmadurasHerrero)); i++) {
			if (Declaraciones.ArmadurasHerrero[i] == ItemIndex) {
				retval = true;
				return retval;
			}
		}
		retval = false;
		return retval;
	}

	static void HerreroConstruirItem(int UserIndex, int ItemIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 30/05/2010 */
		/*
		 * '16/11/2009: ZaMa - Implementado nuevo sistema de construccion de
		 * items.
		 */
		/* '22/05/2010: ZaMa - Los caos ya no suben plebe al trabajar. */
		/* '30/05/2010: ZaMa - Los pks no suben plebe al trabajar. */
		/* '*************************************************** */
		int CantidadItems = 0;
		boolean TieneMateriales = false;
		int OtroUserIndex = 0;

		if (Declaraciones.UserList[UserIndex].flags.Comerciando) {
			OtroUserIndex = Declaraciones.UserList[UserIndex].ComUsu.DestUsu;

			if (OtroUserIndex > 0 && OtroUserIndex <= Declaraciones.MaxUsers) {
				Protocol.WriteConsoleMsg(UserIndex, "¡¡Comercio cancelado, no puedes comerciar mientras trabajas!!",
						FontTypeNames.FONTTYPE_TALK);
				Protocol.WriteConsoleMsg(OtroUserIndex, "¡¡Comercio cancelado por el otro usuario!!",
						FontTypeNames.FONTTYPE_TALK);

				TCP.LimpiarComercioSeguro(UserIndex);
				Protocol.FlushBuffer(OtroUserIndex);
			}
		}

		CantidadItems = Declaraciones.UserList[UserIndex].Construir.PorCiclo;

		if (Declaraciones.UserList[UserIndex].Construir.Cantidad < CantidadItems) {
			CantidadItems = Declaraciones.UserList[UserIndex].Construir.Cantidad;
		}

		if (Declaraciones.UserList[UserIndex].Construir.Cantidad > 0) {
			Declaraciones.UserList[UserIndex].Construir.Cantidad = Declaraciones.UserList[UserIndex].Construir.Cantidad
					- CantidadItems;
		}

		if (CantidadItems == 0) {
			Protocol.WriteStopWorking(UserIndex);
			return;
		}

		if (PuedeConstruirHerreria(ItemIndex)) {

			while (CantidadItems > 0 && !TieneMateriales) {
				if (PuedeConstruir(UserIndex, ItemIndex, CantidadItems)) {
					TieneMateriales = true;
				} else {
					CantidadItems = CantidadItems - 1;
				}
			}

			/* ' Chequeo si puede hacer al menos 1 item */
			if (!TieneMateriales) {
				Protocol.WriteConsoleMsg(UserIndex, "No tienes suficientes materiales.", FontTypeNames.FONTTYPE_INFO);
				Protocol.WriteStopWorking(UserIndex);
				return;
			}

			/* 'Sacamos energía */
			if (Declaraciones.UserList[UserIndex].clase == eClass.Worker) {
				/* 'Chequeamos que tenga los puntos antes de sacarselos */
				if (Declaraciones.UserList[UserIndex].Stats.MinSta >= Trabajo.GASTO_ENERGIA_TRABAJADOR) {
					Declaraciones.UserList[UserIndex].Stats.MinSta = Declaraciones.UserList[UserIndex].Stats.MinSta
							- Trabajo.GASTO_ENERGIA_TRABAJADOR;
					Protocol.WriteUpdateSta(UserIndex);
				} else {
					Protocol.WriteConsoleMsg(UserIndex, "No tienes suficiente energía.", FontTypeNames.FONTTYPE_INFO);
					return;
				}
			} else {
				/* 'Chequeamos que tenga los puntos antes de sacarselos */
				if (Declaraciones.UserList[UserIndex].Stats.MinSta >= Trabajo.GASTO_ENERGIA_NO_TRABAJADOR) {
					Declaraciones.UserList[UserIndex].Stats.MinSta = Declaraciones.UserList[UserIndex].Stats.MinSta
							- Trabajo.GASTO_ENERGIA_NO_TRABAJADOR;
					Protocol.WriteUpdateSta(UserIndex);
				} else {
					Protocol.WriteConsoleMsg(UserIndex, "No tienes suficiente energía.", FontTypeNames.FONTTYPE_INFO);
					return;
				}
			}

			HerreroQuitarMateriales(UserIndex, ItemIndex, CantidadItems);
			/* ' AGREGAR FX */

			switch (Declaraciones.ObjData[ItemIndex].OBJType) {

			case otWeapon:
				Protocol.WriteConsoleMsg(UserIndex,
						"Has construido " + vb6.IIf(CantidadItems > 1, CantidadItems + " armas!", "el arma!"),
						FontTypeNames.FONTTYPE_INFO);
				break;

			case otESCUDO:
				Protocol.WriteConsoleMsg(UserIndex,
						"Has construido " + vb6.IIf(CantidadItems > 1, CantidadItems + " escudos!", "el escudo!"),
						FontTypeNames.FONTTYPE_INFO);
				break;

			case Is == eOBJType.otCASCO:
				Protocol.WriteConsoleMsg(UserIndex,
						"Has construido " + vb6.IIf(CantidadItems > 1, CantidadItems + " cascos!", "el casco!"),
						FontTypeNames.FONTTYPE_INFO);
				break;

			case otArmadura:
				Protocol.WriteConsoleMsg(UserIndex,
						"Has construido " + vb6.IIf(CantidadItems > 1, CantidadItems + " armaduras", "la armadura!"),
						FontTypeNames.FONTTYPE_INFO);

				break;
			}

			Declaraciones.Obj MiObj;

			MiObj.Amount = CantidadItems;
			MiObj.ObjIndex = ItemIndex;
			if (!InvUsuario.MeterItemEnInventario(UserIndex, MiObj)) {
				InvNpc.TirarItemAlPiso(Declaraciones.UserList[UserIndex].Pos, MiObj);
			}

			/* 'Log de construcción de Items. Pablo (ToxicWaste) 10/09/07 */
			if (Declaraciones.ObjData[MiObj.ObjIndex].Log == 1) {
				General.LogDesarrollo(Declaraciones.UserList[UserIndex].Name + " ha construído " + MiObj.Amount + " "
						+ Declaraciones.ObjData[MiObj.ObjIndex].Name);
			}

			UsUaRiOs.SubirSkill(UserIndex, eSkill.Herreria, true);
			modSendData.SendData(SendTarget.ToPCArea, UserIndex,
					Protocol.PrepareMessagePlayWave(Declaraciones.MARTILLOHERRERO,
							Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y));

			if (!ES.criminal(UserIndex)) {
				Declaraciones.UserList[UserIndex].Reputacion.PlebeRep = Declaraciones.UserList[UserIndex].Reputacion.PlebeRep
						+ Declaraciones.vlProleta;
				if (Declaraciones.UserList[UserIndex].Reputacion.PlebeRep > Declaraciones.MAXREP) {
					Declaraciones.UserList[UserIndex].Reputacion.PlebeRep = Declaraciones.MAXREP;
				}
			}

			Declaraciones.UserList[UserIndex].Counters.Trabajando = Declaraciones.UserList[UserIndex].Counters.Trabajando
					+ 1;
		}
	}

	static boolean PuedeConstruirCarpintero(int ItemIndex) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */
		int i = 0;

		for (i = (1); i <= (vb6.UBound(Declaraciones.ObjCarpintero)); i++) {
			if (Declaraciones.ObjCarpintero[i] == ItemIndex) {
				retval = true;
				return retval;
			}
		}
		retval = false;

		return retval;
	}

	static void CarpinteroConstruirItem(int UserIndex, int ItemIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 28/05/2010 */
		/* '24/08/2008: ZaMa - Validates if the player has the required skill */
		/*
		 * '16/11/2009: ZaMa - Implementado nuevo sistema de construccion de
		 * items
		 */
		/* '22/05/2010: ZaMa - Los caos ya no suben plebe al trabajar. */
		/* '28/05/2010: ZaMa - Los pks no suben plebe al trabajar. */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		int CantidadItems = 0;
		boolean TieneMateriales = false;
		int WeaponIndex = 0;
		int OtroUserIndex = 0;

		if (Declaraciones.UserList[UserIndex].flags.Comerciando) {
			OtroUserIndex = Declaraciones.UserList[UserIndex].ComUsu.DestUsu;

			if (OtroUserIndex > 0 && OtroUserIndex <= Declaraciones.MaxUsers) {
				Protocol.WriteConsoleMsg(UserIndex, "¡¡Comercio cancelado, no puedes comerciar mientras trabajas!!",
						FontTypeNames.FONTTYPE_TALK);
				Protocol.WriteConsoleMsg(OtroUserIndex, "¡¡Comercio cancelado por el otro usuario!!",
						FontTypeNames.FONTTYPE_TALK);

				TCP.LimpiarComercioSeguro(UserIndex);
				Protocol.FlushBuffer(OtroUserIndex);
			}
		}

		WeaponIndex = Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex;

		if (WeaponIndex != Declaraciones.SERRUCHO_CARPINTERO
				&& WeaponIndex != Declaraciones.SERRUCHO_CARPINTERO_NEWBIE) {
			Protocol.WriteConsoleMsg(UserIndex, "Debes tener equipado el serrucho para trabajar.",
					FontTypeNames.FONTTYPE_INFO);
			Protocol.WriteStopWorking(UserIndex);
			return;
		}

		CantidadItems = Declaraciones.UserList[UserIndex].Construir.PorCiclo;

		if (Declaraciones.UserList[UserIndex].Construir.Cantidad < CantidadItems) {
			CantidadItems = Declaraciones.UserList[UserIndex].Construir.Cantidad;
		}

		if (Declaraciones.UserList[UserIndex].Construir.Cantidad > 0) {
			Declaraciones.UserList[UserIndex].Construir.Cantidad = Declaraciones.UserList[UserIndex].Construir.Cantidad
					- CantidadItems;
		}

		if (CantidadItems == 0) {
			Protocol.WriteStopWorking(UserIndex);
			return;
		}

		if (vb6.Round(
				Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Carpinteria]
						/ ModCarpinteria(Declaraciones.UserList[UserIndex].clase),
				0) >= Declaraciones.ObjData[ItemIndex].SkCarpinteria && PuedeConstruirCarpintero(ItemIndex)) {

			/* ' Calculo cuantos item puede construir */
			while (CantidadItems > 0 && !TieneMateriales) {
				if (CarpinteroTieneMateriales(UserIndex, ItemIndex, CantidadItems)) {
					TieneMateriales = true;
				} else {
					CantidadItems = CantidadItems - 1;
				}
			}

			/* ' No tiene los materiales ni para construir 1 item? */
			if (!TieneMateriales) {
				/* ' Para que muestre el mensaje */
				CarpinteroTieneMateriales(UserIndex, ItemIndex, 1, true);
				Protocol.WriteStopWorking(UserIndex);
				return;
			}

			/* 'Sacamos energía */
			if (Declaraciones.UserList[UserIndex].clase == eClass.Worker) {
				/* 'Chequeamos que tenga los puntos antes de sacarselos */
				if (Declaraciones.UserList[UserIndex].Stats.MinSta >= Trabajo.GASTO_ENERGIA_TRABAJADOR) {
					Declaraciones.UserList[UserIndex].Stats.MinSta = Declaraciones.UserList[UserIndex].Stats.MinSta
							- Trabajo.GASTO_ENERGIA_TRABAJADOR;
					Protocol.WriteUpdateSta(UserIndex);
				} else {
					Protocol.WriteConsoleMsg(UserIndex, "No tienes suficiente energía.", FontTypeNames.FONTTYPE_INFO);
					return;
				}
			} else {
				/* 'Chequeamos que tenga los puntos antes de sacarselos */
				if (Declaraciones.UserList[UserIndex].Stats.MinSta >= Trabajo.GASTO_ENERGIA_NO_TRABAJADOR) {
					Declaraciones.UserList[UserIndex].Stats.MinSta = Declaraciones.UserList[UserIndex].Stats.MinSta
							- Trabajo.GASTO_ENERGIA_NO_TRABAJADOR;
					Protocol.WriteUpdateSta(UserIndex);
				} else {
					Protocol.WriteConsoleMsg(UserIndex, "No tienes suficiente energía.", FontTypeNames.FONTTYPE_INFO);
					return;
				}
			}

			CarpinteroQuitarMateriales(UserIndex, ItemIndex, CantidadItems);
			Protocol.WriteConsoleMsg(UserIndex,
					"Has construido " + CantidadItems + vb6.IIf(CantidadItems == 1, " objeto!", " objetos!"),
					FontTypeNames.FONTTYPE_INFO);

			Declaraciones.Obj MiObj;
			MiObj.Amount = CantidadItems;
			MiObj.ObjIndex = ItemIndex;
			if (!InvUsuario.MeterItemEnInventario(UserIndex, MiObj)) {
				InvNpc.TirarItemAlPiso(Declaraciones.UserList[UserIndex].Pos, MiObj);
			}

			/* 'Log de construcción de Items. Pablo (ToxicWaste) 10/09/07 */
			if (Declaraciones.ObjData[MiObj.ObjIndex].Log == 1) {
				General.LogDesarrollo(Declaraciones.UserList[UserIndex].Name + " ha construído " + MiObj.Amount + " "
						+ Declaraciones.ObjData[MiObj.ObjIndex].Name);
			}

			UsUaRiOs.SubirSkill(UserIndex, eSkill.Carpinteria, true);
			modSendData.SendData(SendTarget.ToPCArea, UserIndex,
					Protocol.PrepareMessagePlayWave(Declaraciones.LABUROCARPINTERO,
							Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y));

			if (!ES.criminal(UserIndex)) {
				Declaraciones.UserList[UserIndex].Reputacion.PlebeRep = Declaraciones.UserList[UserIndex].Reputacion.PlebeRep
						+ Declaraciones.vlProleta;
				if (Declaraciones.UserList[UserIndex].Reputacion.PlebeRep > Declaraciones.MAXREP) {
					Declaraciones.UserList[UserIndex].Reputacion.PlebeRep = Declaraciones.MAXREP;
				}
			}

			Declaraciones.UserList[UserIndex].Counters.Trabajando = Declaraciones.UserList[UserIndex].Counters.Trabajando
					+ 1;
		}

		return;
		/* FIXME: ErrHandler : */
		General.LogError("Error en CarpinteroConstruirItem. Error " + Err.Number + " : " + Err.description
				+ ". UserIndex:" + UserIndex + ". ItemIndex:" + ItemIndex);
	}

	static int MineralesParaLingote(iMinerales Lingote) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */
		switch (Lingote) {
		case HierroCrudo:
			retval = 14;
			break;

		case PlataCruda:
			retval = 20;
			break;

		case OroCrudo:
			retval = 35;
			break;

		default:
			retval = 10000;
			break;
		}
		return retval;
	}

	static void DoLingotes(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 16/11/2009 */
		/*
		 * '16/11/2009: ZaMa - Implementado nuevo sistema de construccion de
		 * items
		 */
		/* '*************************************************** */
		/* ' Call LogTarea("Sub DoLingotes") */
		int Slot = 0;
		int obji = 0;
		int CantidadItems = 0;
		boolean TieneMinerales = false;
		int OtroUserIndex = 0;

		if (Declaraciones.UserList[UserIndex].flags.Comerciando) {
			OtroUserIndex = Declaraciones.UserList[UserIndex].ComUsu.DestUsu;

			if (OtroUserIndex > 0 && OtroUserIndex <= Declaraciones.MaxUsers) {
				Protocol.WriteConsoleMsg(UserIndex, "¡¡Comercio cancelado, no puedes comerciar mientras trabajas!!",
						FontTypeNames.FONTTYPE_TALK);
				Protocol.WriteConsoleMsg(OtroUserIndex, "¡¡Comercio cancelado por el otro usuario!!",
						FontTypeNames.FONTTYPE_TALK);

				TCP.LimpiarComercioSeguro(UserIndex);
				Protocol.FlushBuffer(OtroUserIndex);
			}
		}

		CantidadItems = SistemaCombate.MaximoInt(1, vb6.CInt((Declaraciones.UserList[UserIndex].Stats.ELV - 4) / 5));

		Slot = Declaraciones.UserList[UserIndex].flags.TargetObjInvSlot;
		obji = Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex;

		while (CantidadItems > 0 && !TieneMinerales) {
			if (Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount >= MineralesParaLingote(obji)
					* CantidadItems) {
				TieneMinerales = true;
			} else {
				CantidadItems = CantidadItems - 1;
			}
		}

		if (!TieneMinerales || Declaraciones.ObjData[obji].OBJType != eOBJType.otMinerales) {
			Protocol.WriteConsoleMsg(UserIndex, "No tienes suficientes minerales para hacer un lingote.",
					FontTypeNames.FONTTYPE_INFO);
			return;
		}

		Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount = Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount
				- MineralesParaLingote(obji) * CantidadItems;
		if (Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount < 1) {
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount = 0;
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex = 0;
		}

		Declaraciones.Obj MiObj;
		MiObj.Amount = CantidadItems;
		MiObj.ObjIndex = Declaraciones.ObjData[Declaraciones.UserList[UserIndex].flags.TargetObjInvIndex].LingoteIndex;
		if (!InvUsuario.MeterItemEnInventario(UserIndex, MiObj)) {
			InvNpc.TirarItemAlPiso(Declaraciones.UserList[UserIndex].Pos, MiObj);
		}

		InvUsuario.UpdateUserInv(false, UserIndex, Slot);
		Protocol.WriteConsoleMsg(UserIndex,
				"¡Has obtenido " + CantidadItems + " lingote" + vb6.IIf(CantidadItems == 1, "", "s") + "!",
				FontTypeNames.FONTTYPE_INFO);

		Declaraciones.UserList[UserIndex].Counters.Trabajando = Declaraciones.UserList[UserIndex].Counters.Trabajando
				+ 1;
	}

	static void DoFundir(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 03/06/2010 */
		/*
		 * '03/06/2010 - Pato: Si es el último ítem a fundir y está equipado lo
		 * desequipamos.
		 */
		/*
		 * '11/03/2010 - ZaMa: Reemplazo división por producto para uan mejor
		 * performanse.
		 */
		/* '*************************************************** */
		int i = 0;
		int num = 0;
		int Slot = 0;
		int[] Lingotes;
		int OtroUserIndex = 0;

		int ItemIndex = 0;

		if (Declaraciones.UserList[UserIndex].flags.Comerciando) {
			OtroUserIndex = Declaraciones.UserList[UserIndex].ComUsu.DestUsu;

			if (OtroUserIndex > 0 && OtroUserIndex <= Declaraciones.MaxUsers) {
				Protocol.WriteConsoleMsg(UserIndex, "¡¡Comercio cancelado, no puedes comerciar mientras trabajas!!",
						FontTypeNames.FONTTYPE_TALK);
				Protocol.WriteConsoleMsg(OtroUserIndex, "¡¡Comercio cancelado por el otro usuario!!",
						FontTypeNames.FONTTYPE_TALK);

				TCP.LimpiarComercioSeguro(UserIndex);
				Protocol.FlushBuffer(OtroUserIndex);
			}
		}

		Slot = Declaraciones.UserList[UserIndex].flags.TargetObjInvSlot;

		ItemIndex = Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex;
		Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount = Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount
				- 1;

		if (Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount < 1) {
			if (Declaraciones.UserList[UserIndex].Invent.Object[Slot].Equipped == 1) {
				InvUsuario.Desequipar(UserIndex, Slot, true);
			}

			Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount = 0;
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex = 0;
		}

		num = Matematicas.RandomNumber(10, 25);

		Lingotes[0] = (Declaraciones.ObjData[Declaraciones.UserList[UserIndex].flags.TargetObjInvIndex].LingH * num)
				* 0.01;
		Lingotes[1] = (Declaraciones.ObjData[Declaraciones.UserList[UserIndex].flags.TargetObjInvIndex].LingP * num)
				* 0.01;
		Lingotes[2] = (Declaraciones.ObjData[Declaraciones.UserList[UserIndex].flags.TargetObjInvIndex].LingO * num)
				* 0.01;

		Declaraciones.Obj[] MiObj;

		for (i = (0); i <= (2); i++) {
			MiObj[i].Amount = Lingotes[i];
			/* 'Una gran negrada pero práctica */
			MiObj[i].ObjIndex = Declaraciones.LingoteHierro + i;

			if (MiObj[i].Amount > 0) {
				if (!InvUsuario.MeterItemEnInventario(UserIndex, MiObj[i])) {
					InvNpc.TirarItemAlPiso(Declaraciones.UserList[UserIndex].Pos, MiObj[i]);
				}
			}
		}

		InvUsuario.UpdateUserInv(false, UserIndex, Slot);
		Protocol.WriteConsoleMsg(UserIndex,
				"¡Has obtenido el " + num + "% de los lingotes utilizados para la construcción del objeto!",
				FontTypeNames.FONTTYPE_INFO);

		if (Declaraciones.ObjData[ItemIndex].Log == 1) {
			General.LogDesarrollo(Declaraciones.UserList[UserIndex].Name + " ha fundido el ítem "
					+ Declaraciones.ObjData[ItemIndex].Name);
		}

		Declaraciones.UserList[UserIndex].Counters.Trabajando = Declaraciones.UserList[UserIndex].Counters.Trabajando
				+ 1;

	}

	static void DoUpgrade(int UserIndex, int ItemIndex) {
		/* '*************************************************** */
		/* 'Author: Torres Patricio (Pato) */
		/* 'Last Modification: 12/08/2009 */
		/* '12/08/2009: Pato - Implementado nuevo sistema de mejora de items */
		/* '*************************************************** */
		int ItemUpgrade = 0;
		int WeaponIndex = 0;
		int OtroUserIndex = 0;

		ItemUpgrade = Declaraciones.ObjData[ItemIndex].Upgrade;

		if (Declaraciones.UserList[UserIndex].flags.Comerciando) {
			OtroUserIndex = Declaraciones.UserList[UserIndex].ComUsu.DestUsu;

			if (OtroUserIndex > 0 && OtroUserIndex <= Declaraciones.MaxUsers) {
				Protocol.WriteConsoleMsg(UserIndex, "¡¡Comercio cancelado, no puedes comerciar mientras trabajas!!",
						FontTypeNames.FONTTYPE_TALK);
				Protocol.WriteConsoleMsg(OtroUserIndex, "¡¡Comercio cancelado por el otro usuario!!",
						FontTypeNames.FONTTYPE_TALK);

				TCP.LimpiarComercioSeguro(UserIndex);
				Protocol.FlushBuffer(OtroUserIndex);
			}
		}

		/* 'Sacamos energía */
		if (Declaraciones.UserList[UserIndex].clase == eClass.Worker) {
			/* 'Chequeamos que tenga los puntos antes de sacarselos */
			if (Declaraciones.UserList[UserIndex].Stats.MinSta >= Trabajo.GASTO_ENERGIA_TRABAJADOR) {
				Declaraciones.UserList[UserIndex].Stats.MinSta = Declaraciones.UserList[UserIndex].Stats.MinSta
						- Trabajo.GASTO_ENERGIA_TRABAJADOR;
				Protocol.WriteUpdateSta(UserIndex);
			} else {
				Protocol.WriteConsoleMsg(UserIndex, "No tienes suficiente energía.", FontTypeNames.FONTTYPE_INFO);
				return;
			}
		} else {
			/* 'Chequeamos que tenga los puntos antes de sacarselos */
			if (Declaraciones.UserList[UserIndex].Stats.MinSta >= Trabajo.GASTO_ENERGIA_NO_TRABAJADOR) {
				Declaraciones.UserList[UserIndex].Stats.MinSta = Declaraciones.UserList[UserIndex].Stats.MinSta
						- Trabajo.GASTO_ENERGIA_NO_TRABAJADOR;
				Protocol.WriteUpdateSta(UserIndex);
			} else {
				Protocol.WriteConsoleMsg(UserIndex, "No tienes suficiente energía.", FontTypeNames.FONTTYPE_INFO);
				return;
			}
		}

		if (ItemUpgrade <= 0) {
			return;
		}
		if (!TieneMaterialesUpgrade(UserIndex, ItemIndex)) {
			return;
		}

		if (PuedeConstruirHerreria(ItemUpgrade)) {

			WeaponIndex = Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex;

			if (WeaponIndex != Declaraciones.MARTILLO_HERRERO && WeaponIndex != Declaraciones.MARTILLO_HERRERO_NEWBIE) {
				Protocol.WriteConsoleMsg(UserIndex, "Debes equiparte el martillo de herrero.",
						FontTypeNames.FONTTYPE_INFO);
				return;
			}

			if (vb6.Round(
					Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Herreria]
							/ ModHerreriA(Declaraciones.UserList[UserIndex].clase),
					0) < Declaraciones.ObjData[ItemUpgrade].SkHerreria) {
				Protocol.WriteConsoleMsg(UserIndex, "No tienes suficientes skills.", FontTypeNames.FONTTYPE_INFO);
				return;
			}

			switch (Declaraciones.ObjData[ItemIndex].OBJType) {
			case otWeapon:
				Protocol.WriteConsoleMsg(UserIndex, "Has mejorado el arma!", FontTypeNames.FONTTYPE_INFO);

				/* 'Todavía no hay, pero just in case */
				break;

			case otESCUDO:
				Protocol.WriteConsoleMsg(UserIndex, "Has mejorado el escudo!", FontTypeNames.FONTTYPE_INFO);

				break;

			case otCASCO:
				Protocol.WriteConsoleMsg(UserIndex, "Has mejorado el casco!", FontTypeNames.FONTTYPE_INFO);

				break;

			case otArmadura:
				Protocol.WriteConsoleMsg(UserIndex, "Has mejorado la armadura!", FontTypeNames.FONTTYPE_INFO);
				break;
			}

			UsUaRiOs.SubirSkill(UserIndex, eSkill.Herreria, true);
			modSendData.SendData(SendTarget.ToPCArea, UserIndex,
					Protocol.PrepareMessagePlayWave(Declaraciones.MARTILLOHERRERO,
							Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y));

		} else if (PuedeConstruirCarpintero(ItemUpgrade)) {

			WeaponIndex = Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex;
			if (WeaponIndex != Declaraciones.SERRUCHO_CARPINTERO
					&& WeaponIndex != Declaraciones.SERRUCHO_CARPINTERO_NEWBIE) {
				Protocol.WriteConsoleMsg(UserIndex, "Debes equiparte un serrucho.", FontTypeNames.FONTTYPE_INFO);
				return;
			}

			if (vb6.Round(
					Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Carpinteria]
							/ ModCarpinteria(Declaraciones.UserList[UserIndex].clase),
					0) < Declaraciones.ObjData[ItemUpgrade].SkCarpinteria) {
				Protocol.WriteConsoleMsg(UserIndex, "No tienes suficientes skills.", FontTypeNames.FONTTYPE_INFO);
				return;
			}

			switch (Declaraciones.ObjData[ItemIndex].OBJType) {
			case otFlechas:
				Protocol.WriteConsoleMsg(UserIndex, "Has mejorado la flecha!", FontTypeNames.FONTTYPE_INFO);

				break;

			case otWeapon:
				Protocol.WriteConsoleMsg(UserIndex, "Has mejorado el arma!", FontTypeNames.FONTTYPE_INFO);

				break;

			case otBarcos:
				Protocol.WriteConsoleMsg(UserIndex, "Has mejorado el barco!", FontTypeNames.FONTTYPE_INFO);
				break;
			}

			UsUaRiOs.SubirSkill(UserIndex, eSkill.Carpinteria, true);
			modSendData.SendData(SendTarget.ToPCArea, UserIndex,
					Protocol.PrepareMessagePlayWave(Declaraciones.LABUROCARPINTERO,
							Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y));
		} else {
			return;
		}

		QuitarMaterialesUpgrade(UserIndex, ItemIndex);

		Declaraciones.Obj MiObj;
		MiObj.Amount = 1;
		MiObj.ObjIndex = ItemUpgrade;

		if (!InvUsuario.MeterItemEnInventario(UserIndex, MiObj)) {
			InvNpc.TirarItemAlPiso(Declaraciones.UserList[UserIndex].Pos, MiObj);
		}

		if (Declaraciones.ObjData[ItemIndex].Log == 1) {
			General.LogDesarrollo(Declaraciones.UserList[UserIndex].Name + " ha mejorado el ítem "
					+ Declaraciones.ObjData[ItemIndex].Name + " a " + Declaraciones.ObjData[ItemUpgrade].Name);
		}

		Declaraciones.UserList[UserIndex].Reputacion.PlebeRep = Declaraciones.UserList[UserIndex].Reputacion.PlebeRep
				+ Declaraciones.vlProleta;
		if (Declaraciones.UserList[UserIndex].Reputacion.PlebeRep > Declaraciones.MAXREP) {
			Declaraciones.UserList[UserIndex].Reputacion.PlebeRep = Declaraciones.MAXREP;
		}

		Declaraciones.UserList[UserIndex].Counters.Trabajando = Declaraciones.UserList[UserIndex].Counters.Trabajando
				+ 1;
	}

	static float ModNavegacion(eClass clase, int UserIndex) {
		float retval = 0.0f;
		/* '*************************************************** */
		/* 'Autor: Unknown (orginal version) */
		/* 'Last Modification: 27/11/2009 */
		/*
		 * '27/11/2009: ZaMa - A worker can navigate before only if it's an
		 * expert fisher
		 */
		/*
		 * '12/04/2010: ZaMa - Arreglo modificador de pescador, para que navegue
		 * con 60 skills.
		 */
		/* '*************************************************** */
		switch (clase) {
		case Pirat:
			retval = 1;
			break;

		case Worker:
			if (Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Pesca] == 100) {
				retval = 1.71;
			} else {
				retval = 2;
			}
			break;

		default:
			retval = 2;
			break;
		}

		return retval;
	}

	static float ModFundicion(eClass clase) {
		float retval = 0.0f;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		switch (clase) {
		case Worker:
			retval = 1;
			break;

		default:
			retval = 3;
			break;
		}

		return retval;
	}

	static int ModCarpinteria(eClass clase) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		switch (clase) {
		case Worker:
			retval = 1;
			break;

		default:
			retval = 3;
			break;
		}

		return retval;
	}

	static float ModHerreriA(eClass clase) {
		float retval = 0.0f;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */
		switch (clase) {
		case Worker:
			retval = 1;
			break;

		default:
			retval = 4;
			break;
		}

		return retval;
	}

	static int ModDomar(eClass clase) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */
		switch (clase) {
		case Druid:
			retval = 6;
			break;

		case Hunter:
			retval = 6;
			break;

		case Cleric:
			retval = 7;
			break;

		default:
			retval = 10;
			break;
		}
		return retval;
	}

	static int FreeMascotaIndex(int UserIndex) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 02/03/09 */
		/*
		 * '02/03/09: ZaMa - Busca un indice libre de mascotas, revisando los
		 * types y no los indices de los npcs
		 */
		/* '*************************************************** */
		int j = 0;
		for (j = (1); j <= (Declaraciones.MAXMASCOTAS); j++) {
			if (Declaraciones.UserList[UserIndex].MascotasType[j] == 0) {
				retval = j;
				return retval;
			}
		}
		return retval;
	}

	static void DoDomar(int UserIndex, int NpcIndex) {
		/* '*************************************************** */
		/* 'Author: Nacho (Integer) */
		/* 'Last Modification: 01/05/2010 */
		/*
		 * '12/15/2008: ZaMa - Limits the number of the same type of pet to 2.
		 */
		/*
		 * '02/03/2009: ZaMa - Las criaturas domadas en zona segura, esperan
		 * afuera (desaparecen).
		 */
		/*
		 * '01/05/2010: ZaMa - Agrego bonificacion 11% para domar con flauta
		 * magica.
		 */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int puntosDomar = 0;
		int puntosRequeridos = 0;
		boolean CanStay = false;
		int petType = 0;
		int NroPets = 0;

		if (Declaraciones.Npclist[NpcIndex].MaestroUser == UserIndex) {
			Protocol.WriteConsoleMsg(UserIndex, "Ya domaste a esa criatura.", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		if (Declaraciones.UserList[UserIndex].NroMascotas < Declaraciones.MAXMASCOTAS) {

			if (Declaraciones.Npclist[NpcIndex].MaestroNpc > 0 || Declaraciones.Npclist[NpcIndex].MaestroUser > 0) {
				Protocol.WriteConsoleMsg(UserIndex, "La criatura ya tiene amo.", FontTypeNames.FONTTYPE_INFO);
				return;
			}

			if (!PuedeDomarMascota(UserIndex, NpcIndex)) {
				Protocol.WriteConsoleMsg(UserIndex, "No puedes domar más de dos criaturas del mismo tipo.",
						FontTypeNames.FONTTYPE_INFO);
				return;
			}

			puntosDomar = vb6.CInt(Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Carisma])
					* vb6.CInt(Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Domar]);

			/* ' 20% de bonificacion */
			if (Declaraciones.UserList[UserIndex].Invent.AnilloEqpObjIndex == Declaraciones.FLAUTAELFICA) {
				puntosRequeridos = Declaraciones.Npclist[NpcIndex].flags.Domable * 0.8;

				/* ' 11% de bonificacion */
			} else if (Declaraciones.UserList[UserIndex].Invent.AnilloEqpObjIndex == Declaraciones.FLAUTAMAGICA) {
				puntosRequeridos = Declaraciones.Npclist[NpcIndex].flags.Domable * 0.89;

			} else {
				puntosRequeridos = Declaraciones.Npclist[NpcIndex].flags.Domable;
			}

			if (puntosRequeridos <= puntosDomar && Matematicas.RandomNumber(1, 5) == 1) {
				int index = 0;
				Declaraciones.UserList[UserIndex].NroMascotas = Declaraciones.UserList[UserIndex].NroMascotas + 1;
				index = FreeMascotaIndex(UserIndex);
				Declaraciones.UserList[UserIndex].MascotasIndex[index] = NpcIndex;
				Declaraciones.UserList[UserIndex].MascotasType[index] = Declaraciones.Npclist[NpcIndex].Numero;

				Declaraciones.Npclist[NpcIndex].MaestroUser = UserIndex;

				NPCs.FollowAmo(NpcIndex);
				NPCs.ReSpawnNpc(Declaraciones.Npclist[NpcIndex]);

				Protocol.WriteConsoleMsg(UserIndex, "La criatura te ha aceptado como su amo.",
						FontTypeNames.FONTTYPE_INFO);

				/* ' Es zona segura? */
				CanStay = (Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].Pk == true);

				if (!CanStay) {
					petType = Declaraciones.Npclist[NpcIndex].Numero;
					NroPets = Declaraciones.UserList[UserIndex].NroMascotas;

					NPCs.QuitarNPC(NpcIndex);

					Declaraciones.UserList[UserIndex].MascotasType[index] = petType;
					Declaraciones.UserList[UserIndex].NroMascotas = NroPets;

					Protocol.WriteConsoleMsg(UserIndex,
							"No se permiten mascotas en zona segura. Éstas te esperarán afuera.",
							FontTypeNames.FONTTYPE_INFO);
				}

				UsUaRiOs.SubirSkill(UserIndex, eSkill.Domar, true);

			} else {
				if (!Declaraciones.UserList[UserIndex].flags.UltimoMensaje == 5) {
					Protocol.WriteConsoleMsg(UserIndex, "No has logrado domar la criatura.",
							FontTypeNames.FONTTYPE_INFO);
					Declaraciones.UserList[UserIndex].flags.UltimoMensaje = 5;
				}

				UsUaRiOs.SubirSkill(UserIndex, eSkill.Domar, false);
			}
		} else {
			Protocol.WriteConsoleMsg(UserIndex, "No puedes controlar más criaturas.", FontTypeNames.FONTTYPE_INFO);
		}

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en DoDomar. Error " + Err.Number + " : " + Err.description);

	}

	/* '' */
	/* ' Checks if the user can tames a pet. */
	/* ' */
	/* ' @param integer userIndex The user id from who wants tame the pet. */
	/* ' @param integer NPCindex The index of the npc to tome. */
	/* ' @return boolean True if can, false if not. */
	static boolean PuedeDomarMascota(int UserIndex, int NpcIndex) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'This function checks how many NPCs of the same type have */
		/* 'been tamed by the user. */
		/* 'Returns True if that amount is less than two. */
		/* '*************************************************** */
		int i = 0;
		int numMascotas = 0;

		for (i = (1); i <= (Declaraciones.MAXMASCOTAS); i++) {
			if (Declaraciones.UserList[UserIndex].MascotasType[i] == Declaraciones.Npclist[NpcIndex].Numero) {
				numMascotas = numMascotas + 1;
			}
		}

		if (numMascotas <= 1) {
			retval = true;
		}

		return retval;
	}

	static void DoAdminInvisible(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 12/01/2010 (ZaMa) */
		/* 'Makes an admin invisible o visible. */
		/*
		 * '13/07/2009: ZaMa - Now invisible admins' chars are erased from all
		 * clients, except from themselves.
		 */
		/*
		 * '12/01/2010: ZaMa - Los druidas pierden la inmunidad de ser atacados
		 * cuando pierden el efecto del mimetismo.
		 */
		/* '*************************************************** */

		if (Declaraciones.UserList[UserIndex].flags.AdminInvisible == 0) {
			/* ' Sacamos el mimetizmo */
			if (Declaraciones.UserList[UserIndex].flags.Mimetizado == 1) {
				Declaraciones.UserList[UserIndex].Char.body = Declaraciones.UserList[UserIndex].CharMimetizado.body;
				Declaraciones.UserList[UserIndex].Char.Head = Declaraciones.UserList[UserIndex].CharMimetizado.Head;
				Declaraciones.UserList[UserIndex].Char.CascoAnim = Declaraciones.UserList[UserIndex].CharMimetizado.CascoAnim;
				Declaraciones.UserList[UserIndex].Char.ShieldAnim = Declaraciones.UserList[UserIndex].CharMimetizado.ShieldAnim;
				Declaraciones.UserList[UserIndex].Char.WeaponAnim = Declaraciones.UserList[UserIndex].CharMimetizado.WeaponAnim;
				Declaraciones.UserList[UserIndex].Counters.Mimetismo = 0;
				Declaraciones.UserList[UserIndex].flags.Mimetizado = 0;
				/*
				 * ' Se fue el efecto del mimetismo, puede ser atacado por npcs
				 */
				Declaraciones.UserList[UserIndex].flags.Ignorado = false;
			}

			Declaraciones.UserList[UserIndex].flags.AdminInvisible = 1;
			Declaraciones.UserList[UserIndex].flags.invisible = 1;
			Declaraciones.UserList[UserIndex].flags.Oculto = 1;
			Declaraciones.UserList[UserIndex].flags.OldBody = Declaraciones.UserList[UserIndex].Char.body;
			Declaraciones.UserList[UserIndex].flags.OldHead = Declaraciones.UserList[UserIndex].Char.Head;
			Declaraciones.UserList[UserIndex].Char.body = 0;
			Declaraciones.UserList[UserIndex].Char.Head = 0;

			/* ' Solo el admin sabe que se hace invi */
			TCP.EnviarDatosASlot(UserIndex,
					Protocol.PrepareMessageSetInvisible(Declaraciones.UserList[UserIndex].Char.CharIndex, true));
			/*
			 * 'Le mandamos el mensaje para que borre el personaje a los
			 * clientes que estén cerca
			 */
			modSendData.SendData(SendTarget.ToPCAreaButIndex, UserIndex,
					Protocol.PrepareMessageCharacterRemove(Declaraciones.UserList[UserIndex].Char.CharIndex));
		} else {
			Declaraciones.UserList[UserIndex].flags.AdminInvisible = 0;
			Declaraciones.UserList[UserIndex].flags.invisible = 0;
			Declaraciones.UserList[UserIndex].flags.Oculto = 0;
			Declaraciones.UserList[UserIndex].Counters.TiempoOculto = 0;
			Declaraciones.UserList[UserIndex].Char.body = Declaraciones.UserList[UserIndex].flags.OldBody;
			Declaraciones.UserList[UserIndex].Char.Head = Declaraciones.UserList[UserIndex].flags.OldHead;

			/* ' Solo el admin sabe que se hace visible */
			TCP.EnviarDatosASlot(UserIndex, Protocol.PrepareMessageCharacterChange(
					Declaraciones.UserList[UserIndex].Char.body, Declaraciones.UserList[UserIndex].Char.Head,
					Declaraciones.UserList[UserIndex].Char.heading, Declaraciones.UserList[UserIndex].Char.CharIndex,
					Declaraciones.UserList[UserIndex].Char.WeaponAnim,
					Declaraciones.UserList[UserIndex].Char.ShieldAnim, Declaraciones.UserList[UserIndex].Char.FX,
					Declaraciones.UserList[UserIndex].Char.loops, Declaraciones.UserList[UserIndex].Char.CascoAnim));
			TCP.EnviarDatosASlot(UserIndex,
					Protocol.PrepareMessageSetInvisible(Declaraciones.UserList[UserIndex].Char.CharIndex, false));

			/*
			 * 'Le mandamos el mensaje para crear el personaje a los clientes
			 * que estén cerca
			 */
			UsUaRiOs.MakeUserChar(true, Declaraciones.UserList[UserIndex].Pos.Map, UserIndex,
					Declaraciones.UserList[UserIndex].Pos.Map, Declaraciones.UserList[UserIndex].Pos.X,
					Declaraciones.UserList[UserIndex].Pos.Y, true);
		}

	}

	static void TratarDeHacerFogata(int Map, int X, int Y, int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int Suerte = 0;
		int exito = 0;
		Declaraciones.Obj Obj;
		Declaraciones.WorldPos posMadera;

		if (!Extra.LegalPos(Map, X, Y)) {
			return;
		}

		posMadera.Map = Map;
		posMadera.X = X;
		posMadera.Y = Y;

		if (Declaraciones.MapData[Map][X][Y].ObjInfo.ObjIndex != 58) {
			Protocol.WriteConsoleMsg(UserIndex, "Necesitas clickear sobre lena para hacer ramitas.",
					FontTypeNames.FONTTYPE_INFO);
			return;
		}

		if (Matematicas.Distancia(posMadera, Declaraciones.UserList[UserIndex].Pos) > 2) {
			Protocol.WriteConsoleMsg(UserIndex, "Estás demasiado lejos para prender la fogata.",
					FontTypeNames.FONTTYPE_INFO);
			return;
		}

		if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
			Protocol.WriteConsoleMsg(UserIndex, "No puedes hacer fogatas estando muerto.", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		if (Declaraciones.MapData[Map][X][Y].ObjInfo.Amount < 3) {
			Protocol.WriteConsoleMsg(UserIndex, "Necesitas por lo menos tres troncos para hacer una fogata.",
					FontTypeNames.FONTTYPE_INFO);
			return;
		}

		int SupervivenciaSkill = 0;

		SupervivenciaSkill = Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Supervivencia];

		if (SupervivenciaSkill < 6) {
			Suerte = 3;
		} else if (SupervivenciaSkill <= 34) {
			Suerte = 2;
		} else {
			Suerte = 1;
		}

		exito = Matematicas.RandomNumber(1, Suerte);

		if (exito == 1) {
			Obj.ObjIndex = Declaraciones.FOGATA_APAG;
			Obj.Amount = Declaraciones.MapData[Map][X][Y].ObjInfo.Amount / 3;

			Protocol.WriteConsoleMsg(UserIndex, "Has hecho " + Obj.Amount + " fogatas.", FontTypeNames.FONTTYPE_INFO);

			InvUsuario.MakeObj(Obj, Map, X, Y);

			/* 'Seteamos la fogata como el nuevo TargetObj del user */
			Declaraciones.UserList[UserIndex].flags.TargetObj = Declaraciones.FOGATA_APAG;

			UsUaRiOs.SubirSkill(UserIndex, eSkill.Supervivencia, true);
		} else {
			/* '[CDT 17-02-2004] */
			if (!Declaraciones.UserList[UserIndex].flags.UltimoMensaje == 10) {
				Protocol.WriteConsoleMsg(UserIndex, "No has podido hacer la fogata.", FontTypeNames.FONTTYPE_INFO);
				Declaraciones.UserList[UserIndex].flags.UltimoMensaje = 10;
			}
			/* '[/CDT] */

			UsUaRiOs.SubirSkill(UserIndex, eSkill.Supervivencia, false);
		}

	}

	static void DoPescar(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 28/05/2010 */
		/* '16/11/2009: ZaMa - Implementado nuevo sistema de extraccion. */
		/*
		 * '11/05/2010: ZaMa - Arreglo formula de maximo de items
		 * contruibles/extraibles.
		 */
		/*
		 * '05/13/2010: Pato - Refix a la formula de maximo de items
		 * construibles/extraibles.
		 */
		/* '22/05/2010: ZaMa - Los caos ya no suben plebe al trabajar. */
		/* '28/05/2010: ZaMa - Los pks no suben plebe al trabajar. */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		int Suerte = 0;
		int res = 0;
		int CantidadItems = 0;
		int Skill = 0;

		if (Declaraciones.UserList[UserIndex].clase == eClass.Worker) {
			QuitarSta(UserIndex, Declaraciones.EsfuerzoPescarPescador);
		} else {
			QuitarSta(UserIndex, Declaraciones.EsfuerzoPescarGeneral);
		}

		Skill = Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Pesca];
		Suerte = vb6.Int(-0.00125 * Skill * Skill - 0.3 * Skill + 49);

		res = Matematicas.RandomNumber(1, Suerte);

		if (res <= 6) {
			Declaraciones.Obj MiObj;

			if (Declaraciones.UserList[UserIndex].clase == eClass.Worker) {
				CantidadItems = MaxItemsExtraibles(Declaraciones.UserList[UserIndex].Stats.ELV);

				MiObj.Amount = Matematicas.RandomNumber(1, CantidadItems);
			} else {
				MiObj.Amount = 1;
			}

			MiObj.ObjIndex = Declaraciones.Pescado;

			if (!InvUsuario.MeterItemEnInventario(UserIndex, MiObj)) {
				InvNpc.TirarItemAlPiso(Declaraciones.UserList[UserIndex].Pos, MiObj);
			}

			Protocol.WriteConsoleMsg(UserIndex, "¡Has pescado un lindo pez!", FontTypeNames.FONTTYPE_INFO);

			UsUaRiOs.SubirSkill(UserIndex, eSkill.Pesca, true);
		} else {
			/* '[CDT 17-02-2004] */
			if (!Declaraciones.UserList[UserIndex].flags.UltimoMensaje == 6) {
				Protocol.WriteConsoleMsg(UserIndex, "¡No has pescado nada!", FontTypeNames.FONTTYPE_INFO);
				Declaraciones.UserList[UserIndex].flags.UltimoMensaje = 6;
			}
			/* '[/CDT] */

			UsUaRiOs.SubirSkill(UserIndex, eSkill.Pesca, false);
		}

		if (!ES.criminal(UserIndex)) {
			Declaraciones.UserList[UserIndex].Reputacion.PlebeRep = Declaraciones.UserList[UserIndex].Reputacion.PlebeRep
					+ Declaraciones.vlProleta;
			if (Declaraciones.UserList[UserIndex].Reputacion.PlebeRep > Declaraciones.MAXREP) {
				Declaraciones.UserList[UserIndex].Reputacion.PlebeRep = Declaraciones.MAXREP;
			}
		}

		Declaraciones.UserList[UserIndex].Counters.Trabajando = Declaraciones.UserList[UserIndex].Counters.Trabajando
				+ 1;

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en DoPescar. Error " + Err.Number + " : " + Err.description);
	}

	static void DoPescarRed(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		int iSkill = 0;
		int Suerte = 0;
		int res = 0;
		boolean EsPescador = false;
		int CantidadItems = 0;

		if (Declaraciones.UserList[UserIndex].clase == eClass.Worker) {
			QuitarSta(UserIndex, Declaraciones.EsfuerzoPescarPescador);
			EsPescador = true;
		} else {
			QuitarSta(UserIndex, Declaraciones.EsfuerzoPescarGeneral);
			EsPescador = false;
		}

		iSkill = Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Pesca];

		/* ' m = (60-11)/(1-10) */
		/* ' y = mx - m*10 + 11 */

		Suerte = vb6.Int(-0.00125 * iSkill * iSkill - 0.3 * iSkill + 49);

		if (Suerte > 0) {
			res = Matematicas.RandomNumber(1, Suerte);

			if (res <= 6) {

				Declaraciones.Obj MiObj;

				if (EsPescador) {
					CantidadItems = MaxItemsExtraibles(Declaraciones.UserList[UserIndex].Stats.ELV);
					MiObj.Amount = Matematicas.RandomNumber(1, CantidadItems);
				} else {
					MiObj.Amount = 1;
				}

				MiObj.ObjIndex = Declaraciones.ListaPeces[Matematicas.RandomNumber(1, Declaraciones.NUM_PECES)];

				if (!InvUsuario.MeterItemEnInventario(UserIndex, MiObj)) {
					InvNpc.TirarItemAlPiso(Declaraciones.UserList[UserIndex].Pos, MiObj);
				}

				Protocol.WriteConsoleMsg(UserIndex, "¡Has pescado algunos peces!", FontTypeNames.FONTTYPE_INFO);

				UsUaRiOs.SubirSkill(UserIndex, eSkill.Pesca, true);
			} else {
				if (!Declaraciones.UserList[UserIndex].flags.UltimoMensaje == 6) {
					Protocol.WriteConsoleMsg(UserIndex, "¡No has pescado nada!", FontTypeNames.FONTTYPE_INFO);
					Declaraciones.UserList[UserIndex].flags.UltimoMensaje = 6;
				}

				UsUaRiOs.SubirSkill(UserIndex, eSkill.Pesca, false);
			}
		}

		Declaraciones.UserList[UserIndex].Reputacion.PlebeRep = Declaraciones.UserList[UserIndex].Reputacion.PlebeRep
				+ Declaraciones.vlProleta;
		if (Declaraciones.UserList[UserIndex].Reputacion.PlebeRep > Declaraciones.MAXREP) {
			Declaraciones.UserList[UserIndex].Reputacion.PlebeRep = Declaraciones.MAXREP;
		}

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en DoPescarRed");
	}

	/* '' */
	/* ' Try to steal an item / gold to another character */
	/* ' */
	/* ' @param LadrOnIndex Specifies reference to user that stoles */
	/* ' @param VictimaIndex Specifies reference to user that is being stolen */

	static void DoRobar(int LadrOnIndex, int VictimaIndex) {
		/* '************************************************* */
		/* 'Author: Unknown */
		/* 'Last modified: 05/04/2010 */
		/* 'Last Modification By: ZaMa */
		/*
		 * '24/07/08: Marco - Now it calls to WriteUpdateGold(VictimaIndex and
		 * LadrOnIndex) when the thief stoles gold. (MarKoxX)
		 */
		/* '27/11/2009: ZaMa - Optimizacion de codigo. */
		/* '18/12/2009: ZaMa - Los ladrones ciudas pueden robar a pks. */
		/*
		 * '01/04/2010: ZaMa - Los ladrones pasan a robar oro acorde a su nivel.
		 */
		/*
		 * '05/04/2010: ZaMa - Los armadas no pueden robarle a ciudadanos jamas.
		 */
		/* '23/04/2010: ZaMa - No se puede robar mas sin energia. */
		/* '23/04/2010: ZaMa - El alcance de robo pasa a ser de 1 tile. */
		/* '************************************************* */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int OtroUserIndex = 0;

		if (!Declaraciones.MapInfo[Declaraciones.UserList[VictimaIndex].Pos.Map].Pk) {
			return;
		}

		if (Declaraciones.UserList[VictimaIndex].flags.EnConsulta) {
			Protocol.WriteConsoleMsg(LadrOnIndex, "¡¡¡No puedes robar a usuarios en consulta!!!",
					FontTypeNames.FONTTYPE_INFO);
			return;
		}

		if (Declaraciones.UserList[LadrOnIndex].flags.Seguro) {
			if (!ES.criminal(VictimaIndex)) {
				Protocol.WriteConsoleMsg(LadrOnIndex, "Debes quitarte el seguro para robarle a un ciudadano.",
						FontTypeNames.FONTTYPE_FIGHT);
				return;
			}
		} else {
			if (Declaraciones.UserList[LadrOnIndex].Faccion.ArmadaReal == 1) {
				if (!ES.criminal(VictimaIndex)) {
					Protocol.WriteConsoleMsg(LadrOnIndex,
							"Los miembros del ejército real no tienen permitido robarle a ciudadanos.",
							FontTypeNames.FONTTYPE_FIGHT);
					return;
				}
			}
		}

		/* ' Caos robando a caos? */
		if (Declaraciones.UserList[VictimaIndex].Faccion.FuerzasCaos == 1
				&& Declaraciones.UserList[LadrOnIndex].Faccion.FuerzasCaos == 1) {
			Protocol.WriteConsoleMsg(LadrOnIndex, "No puedes robar a otros miembros de la legión oscura.",
					FontTypeNames.FONTTYPE_FIGHT);
			return;
		}

		if (SistemaCombate.TriggerZonaPelea(LadrOnIndex, VictimaIndex) != TRIGGER6_AUSENTE) {
			return;
		}

		/* ' Tiene energia? */
		if (Declaraciones.UserList[LadrOnIndex].Stats.MinSta < 15) {
			if (Declaraciones.UserList[LadrOnIndex].Genero == eGenero.Hombre) {
				Protocol.WriteConsoleMsg(LadrOnIndex, "Estás muy cansado para robar.", FontTypeNames.FONTTYPE_INFO);
			} else {
				Protocol.WriteConsoleMsg(LadrOnIndex, "Estás muy cansada para robar.", FontTypeNames.FONTTYPE_INFO);
			}

			return;
		}

		/* ' Quito energia */
		QuitarSta(LadrOnIndex, 15);

		boolean GuantesHurto = false;

		if (Declaraciones.UserList[LadrOnIndex].Invent.AnilloEqpObjIndex == Declaraciones.GUANTE_HURTO) {
			GuantesHurto = true;
		}

		if (Declaraciones.UserList[VictimaIndex].flags.Privilegios && PlayerType.User) {

			int Suerte = 0;
			int res = 0;
			int RobarSkill = 0;

			RobarSkill = Declaraciones.UserList[LadrOnIndex].Stats.UserSkills[eSkill.Robar];

			if (RobarSkill <= 10) {
				Suerte = 35;
			} else if (RobarSkill <= 20) {
				Suerte = 30;
			} else if (RobarSkill <= 30) {
				Suerte = 28;
			} else if (RobarSkill <= 40) {
				Suerte = 24;
			} else if (RobarSkill <= 50) {
				Suerte = 22;
			} else if (RobarSkill <= 60) {
				Suerte = 20;
			} else if (RobarSkill <= 70) {
				Suerte = 18;
			} else if (RobarSkill <= 80) {
				Suerte = 15;
			} else if (RobarSkill <= 90) {
				Suerte = 10;
			} else if (RobarSkill < 100) {
				Suerte = 7;
			} else {
				Suerte = 5;
			}

			res = Matematicas.RandomNumber(1, Suerte);

			/* 'Exito robo */
			if (res < 3) {
				if (Declaraciones.UserList[VictimaIndex].flags.Comerciando) {
					OtroUserIndex = Declaraciones.UserList[VictimaIndex].ComUsu.DestUsu;

					if (OtroUserIndex > 0 && OtroUserIndex <= Declaraciones.MaxUsers) {
						Protocol.WriteConsoleMsg(VictimaIndex, "¡¡Comercio cancelado, te están robando!!",
								FontTypeNames.FONTTYPE_TALK);
						Protocol.WriteConsoleMsg(OtroUserIndex, "¡¡Comercio cancelado por el otro usuario!!",
								FontTypeNames.FONTTYPE_TALK);

						TCP.LimpiarComercioSeguro(VictimaIndex);
						Protocol.FlushBuffer(OtroUserIndex);
					}
				}

				if ((Matematicas.RandomNumber(1, 50) < 25)
						&& (Declaraciones.UserList[LadrOnIndex].clase == eClass.Thief)) {
					if (InvUsuario.TieneObjetosRobables(VictimaIndex)) {
						RobarObjeto(LadrOnIndex, VictimaIndex);
					} else {
						Protocol.WriteConsoleMsg(LadrOnIndex,
								Declaraciones.UserList[VictimaIndex].Name + " no tiene objetos.",
								FontTypeNames.FONTTYPE_INFO);
					}
					/* 'Roba oro */
				} else {
					if (Declaraciones.UserList[VictimaIndex].Stats.GLD > 0) {
						int N = 0;

						if (Declaraciones.UserList[LadrOnIndex].clase == eClass.Thief) {
							/*
							 * ' Si no tine puestos los guantes de hurto roba un
							 * 50% menos. Pablo (ToxicWaste)
							 */
							if (GuantesHurto) {
								N = Matematicas.RandomNumber(Declaraciones.UserList[LadrOnIndex].Stats.ELV * 50,
										Declaraciones.UserList[LadrOnIndex].Stats.ELV * 100);
							} else {
								N = Matematicas.RandomNumber(Declaraciones.UserList[LadrOnIndex].Stats.ELV * 25,
										Declaraciones.UserList[LadrOnIndex].Stats.ELV * 50);
							}
						} else {
							N = Matematicas.RandomNumber(1, 100);
						}
						if (N > Declaraciones.UserList[VictimaIndex].Stats.GLD) {
							N = Declaraciones.UserList[VictimaIndex].Stats.GLD;
						}
						Declaraciones.UserList[VictimaIndex].Stats.GLD = Declaraciones.UserList[VictimaIndex].Stats.GLD
								- N;

						Declaraciones.UserList[LadrOnIndex].Stats.GLD = Declaraciones.UserList[LadrOnIndex].Stats.GLD
								+ N;
						if (Declaraciones.UserList[LadrOnIndex].Stats.GLD > Declaraciones.MAXORO) {
							Declaraciones.UserList[LadrOnIndex].Stats.GLD = Declaraciones.MAXORO;
						}

						Protocol.WriteConsoleMsg(LadrOnIndex,
								"Le has robado " + N + " monedas de oro a " + Declaraciones.UserList[VictimaIndex].Name,
								FontTypeNames.FONTTYPE_INFO);
						/* 'Le actualizamos la billetera al ladron */
						Protocol.WriteUpdateGold(LadrOnIndex);

						/* 'Le actualizamos la billetera a la victima */
						Protocol.WriteUpdateGold(VictimaIndex);
						Protocol.FlushBuffer(VictimaIndex);
					} else {
						Protocol.WriteConsoleMsg(LadrOnIndex,
								Declaraciones.UserList[VictimaIndex].Name + " no tiene oro.",
								FontTypeNames.FONTTYPE_INFO);
					}
				}

				UsUaRiOs.SubirSkill(LadrOnIndex, eSkill.Robar, true);
			} else {
				Protocol.WriteConsoleMsg(LadrOnIndex, "¡No has logrado robar nada!", FontTypeNames.FONTTYPE_INFO);
				Protocol.WriteConsoleMsg(VictimaIndex,
						"¡" + Declaraciones.UserList[LadrOnIndex].Name + " ha intentado robarte!",
						FontTypeNames.FONTTYPE_INFO);
				Protocol.FlushBuffer(VictimaIndex);

				UsUaRiOs.SubirSkill(LadrOnIndex, eSkill.Robar, false);
			}

			if (!ES.criminal(LadrOnIndex)) {
				if (!ES.criminal(VictimaIndex)) {
					UsUaRiOs.VolverCriminal(LadrOnIndex);
				}
			}

			/* ' Se pudo haber convertido si robo a un ciuda */
			if (ES.criminal(LadrOnIndex)) {
				Declaraciones.UserList[LadrOnIndex].Reputacion.LadronesRep = Declaraciones.UserList[LadrOnIndex].Reputacion.LadronesRep
						+ Declaraciones.vlLadron;
				if (Declaraciones.UserList[LadrOnIndex].Reputacion.LadronesRep > Declaraciones.MAXREP) {
					Declaraciones.UserList[LadrOnIndex].Reputacion.LadronesRep = Declaraciones.MAXREP;
				}
			}
		}

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en DoRobar. Error " + Err.Number + " : " + Err.description);

	}

	/* '' */
	/* ' Check if one item is stealable */
	/* ' */
	/* ' @param VictimaIndex Specifies reference to victim */
	/* ' @param Slot Specifies reference to victim's inventory slot */
	/* ' @return If the item is stealable */
	static boolean ObjEsRobable(int VictimaIndex, int Slot) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' Agregué los barcos */
		/* ' Esta funcion determina qué objetos son robables. */
		/* ' 22/05/2010: Los items newbies ya no son robables. */
		/* '*************************************************** */

		int OI = 0;

		OI = Declaraciones.UserList[VictimaIndex].Invent.Object[Slot].ObjIndex;

		retval = Declaraciones.ObjData[OI].OBJType != eOBJType.otLlaves
				&& Declaraciones.UserList[VictimaIndex].Invent.Object[Slot].Equipped == 0
				&& Declaraciones.ObjData[OI].Real == 0 && Declaraciones.ObjData[OI].Caos == 0
				&& Declaraciones.ObjData[OI].OBJType != eOBJType.otBarcos && !InvUsuario.ItemNewbie(OI)
				&& Declaraciones.ObjData[OI].Intransferible == 0 && Declaraciones.ObjData[OI].NoRobable == 0;
		return retval;
	}

	/* '' */
	/* ' Try to steal an item to another character */
	/* ' */
	/* ' @param LadrOnIndex Specifies reference to user that stoles */
	/* ' @param VictimaIndex Specifies reference to user that is being stolen */
	static void RobarObjeto(int LadrOnIndex, int VictimaIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 02/04/2010 */
		/*
		 * '02/04/2010: ZaMa - Modifico la cantidad de items robables por el
		 * ladron.
		 */
		/* '*************************************************** */

		boolean flag = false;
		int i = 0;

		flag = false;

		/* 'Comenzamos por el principio o el final? */
		if (Matematicas.RandomNumber(1, 12) < 6) {
			i = 1;
			while (!flag && i <= Declaraciones.UserList[VictimaIndex].CurrentInventorySlots) {
				/* 'Hay objeto en este slot? */
				if (Declaraciones.UserList[VictimaIndex].Invent.Object[i].ObjIndex > 0) {
					if (ObjEsRobable(VictimaIndex, i)) {
						if (Matematicas.RandomNumber(1, 10) < 4) {
							flag = true;
						}
					}
				}
				if (!flag) {
					i = i + 1;
				}
			}
		} else {
			i = Declaraciones.UserList[VictimaIndex].CurrentInventorySlots;
			while (!flag && i > 0) {
				/* 'Hay objeto en este slot? */
				if (Declaraciones.UserList[VictimaIndex].Invent.Object[i].ObjIndex > 0) {
					if (ObjEsRobable(VictimaIndex, i)) {
						if (Matematicas.RandomNumber(1, 10) < 4) {
							flag = true;
						}
					}
				}
				if (!flag) {
					i = i - 1;
				}
			}
		}

		if (flag) {
			Declaraciones.Obj MiObj;
			int num = 0;
			int ObjAmount = 0;

			ObjAmount = Declaraciones.UserList[VictimaIndex].Invent.Object[i].Amount;

			/*
			 * 'Cantidad al azar entre el 5% y el 10% del total, con minimo 1.
			 */
			num = SistemaCombate.MaximoInt(1, Matematicas.RandomNumber(ObjAmount * 0.05, ObjAmount * 0.1));

			MiObj.Amount = num;
			MiObj.ObjIndex = Declaraciones.UserList[VictimaIndex].Invent.Object[i].ObjIndex;

			Declaraciones.UserList[VictimaIndex].Invent.Object[i].Amount = ObjAmount - num;

			if (Declaraciones.UserList[VictimaIndex].Invent.Object[i].Amount <= 0) {
				InvUsuario.QuitarUserInvItem(VictimaIndex, vb6.CByte(i), 1);
			}

			InvUsuario.UpdateUserInv(false, VictimaIndex, vb6.CByte(i));

			if (!InvUsuario.MeterItemEnInventario(LadrOnIndex, MiObj)) {
				InvNpc.TirarItemAlPiso(Declaraciones.UserList[LadrOnIndex].Pos, MiObj);
			}

			if (Declaraciones.UserList[LadrOnIndex].clase == eClass.Thief) {
				Protocol.WriteConsoleMsg(LadrOnIndex,
						"Has robado " + MiObj.Amount + " " + Declaraciones.ObjData[MiObj.ObjIndex].Name,
						FontTypeNames.FONTTYPE_INFO);
			} else {
				Protocol.WriteConsoleMsg(LadrOnIndex,
						"Has hurtado " + MiObj.Amount + " " + Declaraciones.ObjData[MiObj.ObjIndex].Name,
						FontTypeNames.FONTTYPE_INFO);
			}
		} else {
			Protocol.WriteConsoleMsg(LadrOnIndex, "No has logrado robar ningún objeto.", FontTypeNames.FONTTYPE_INFO);
		}

		/* 'If exiting, cancel de quien es robado */
		UsUaRiOs.CancelExit(VictimaIndex);

	}

	static void DoApunalar(int UserIndex, int VictimNpcIndex, int VictimUserIndex, int dano) {
		/* '*************************************************** */
		/* 'Autor: Nacho (Integer) & Unknown (orginal version) */
		/* 'Last Modification: 04/17/08 - (NicoNZ) */
		/* 'Simplifique la cuenta que hacia para sacar la suerte */
		/* 'y arregle la cuenta que hacia para sacar el dano */
		/* '*************************************************** */
		int Suerte = 0;
		int Skill = 0;

		Skill = Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Apunalar];

		switch (Declaraciones.UserList[UserIndex].clase) {
		case Assasin:
			Suerte = vb6.Int(((0.00003 * Skill - 0.002) * Skill + 0.098) * Skill + 4.25);

			break;

		case Cleric:
		case eClass.Paladin:
		case eClass.Pirat:
			Suerte = vb6.Int(((0.000003 * Skill + 0.0006) * Skill + 0.0107) * Skill + 4.93);

			break;

		case Bard:
			Suerte = vb6.Int(((0.000002 * Skill + 0.0002) * Skill + 0.032) * Skill + 4.81);

			break;

		default:
			Suerte = vb6.Int(0.0361 * Skill + 4.39);
			break;
		}

		if (Matematicas.RandomNumber(0, 100) < Suerte) {
			if (VictimUserIndex != 0) {
				if (Declaraciones.UserList[UserIndex].clase == eClass.Assasin) {
					dano = vb6.Round(dano * 1.4, 0);
				} else {
					dano = vb6.Round(dano * 1.5, 0);
				}

				Declaraciones.UserList[VictimUserIndex].Stats.MinHp = Declaraciones.UserList[VictimUserIndex].Stats.MinHp
						- dano;
				Protocol.WriteConsoleMsg(UserIndex,
						"Has apunalado a " + Declaraciones.UserList[VictimUserIndex].Name + " por " + dano,
						FontTypeNames.FONTTYPE_FIGHT);
				Protocol.WriteConsoleMsg(VictimUserIndex,
						"Te ha apunalado " + Declaraciones.UserList[UserIndex].Name + " por " + dano,
						FontTypeNames.FONTTYPE_FIGHT);

				Protocol.FlushBuffer(VictimUserIndex);
			} else {
				Declaraciones.Npclist[VictimNpcIndex].Stats.MinHp = Declaraciones.Npclist[VictimNpcIndex].Stats.MinHp
						- vb6.Int(dano * 2);
				Protocol.WriteConsoleMsg(UserIndex, "Has apunalado la criatura por " + vb6.Int(dano * 2),
						FontTypeNames.FONTTYPE_FIGHT);
				/* '[Alejo] */
				SistemaCombate.CalcularDarExp(UserIndex, VictimNpcIndex, dano * 2);
			}

			UsUaRiOs.SubirSkill(UserIndex, eSkill.Apunalar, true);
		} else {
			Protocol.WriteConsoleMsg(UserIndex, "¡No has logrado apunalar a tu enemigo!", FontTypeNames.FONTTYPE_FIGHT);
			UsUaRiOs.SubirSkill(UserIndex, eSkill.Apunalar, false);
		}

	}

	static void DoAcuchillar(int UserIndex, int VictimNpcIndex, int VictimUserIndex, int dano) {
		/* '*************************************************** */
		/* 'Autor: ZaMa */
		/* 'Last Modification: 12/01/2010 */
		/* '*************************************************** */

		if (Matematicas.RandomNumber(1, 100) <= Declaraciones.PROB_ACUCHILLAR) {
			dano = vb6.Int(dano * Declaraciones.DANO_ACUCHILLAR);

			if (VictimUserIndex != 0) {

				Declaraciones.UserList[VictimUserIndex].Stats.MinHp = Declaraciones.UserList[VictimUserIndex].Stats.MinHp
						- dano;
				Protocol.WriteConsoleMsg(UserIndex,
						"Has acuchillado a " + Declaraciones.UserList[VictimUserIndex].Name + " por " + dano,
						FontTypeNames.FONTTYPE_FIGHT);
				Protocol.WriteConsoleMsg(VictimUserIndex,
						Declaraciones.UserList[UserIndex].Name + " te ha acuchillado por " + dano,
						FontTypeNames.FONTTYPE_FIGHT);

			} else {

				Declaraciones.Npclist[VictimNpcIndex].Stats.MinHp = Declaraciones.Npclist[VictimNpcIndex].Stats.MinHp
						- dano;
				Protocol.WriteConsoleMsg(UserIndex, "Has acuchillado a la criatura por " + dano,
						FontTypeNames.FONTTYPE_FIGHT);
				SistemaCombate.CalcularDarExp(UserIndex, VictimNpcIndex, dano);

			}
		}

	}

	static void DoGolpeCritico(int UserIndex, int VictimNpcIndex, int VictimUserIndex, int dano) {
		/* '*************************************************** */
		/* 'Autor: Pablo (ToxicWaste) */
		/* 'Last Modification: 28/01/2007 */
		/*
		 * '01/06/2010: ZaMa - Valido si tiene arma equipada antes de preguntar
		 * si es vikinga.
		 */
		/* '*************************************************** */
		int Suerte = 0;
		int Skill = 0;
		int WeaponIndex = 0;

		/* ' Es bandido? */
		if (Declaraciones.UserList[UserIndex].clase != eClass.Bandit) {
			return;
		}

		WeaponIndex = Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex;

		/* ' Es una espada vikinga? */
		if (WeaponIndex != Declaraciones.ESPADA_VIKINGA) {
			return;
		}

		Skill = Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Wrestling];

		Suerte = vb6.Int((((0.00000003 * Skill + 0.000006) * Skill + 0.000107) * Skill + 0.0893) * 100);

		if (Matematicas.RandomNumber(1, 100) <= Suerte) {

			dano = vb6.Int(dano * 0.75);

			if (VictimUserIndex != 0) {

				Declaraciones.UserList[VictimUserIndex].Stats.MinHp = Declaraciones.UserList[VictimUserIndex].Stats.MinHp
						- dano;
				Protocol.WriteConsoleMsg(UserIndex, "Has golpeado críticamente a "
						+ Declaraciones.UserList[VictimUserIndex].Name + " por " + dano + ".",
						FontTypeNames.FONTTYPE_FIGHT);
				Protocol.WriteConsoleMsg(VictimUserIndex,
						Declaraciones.UserList[UserIndex].Name + " te ha golpeado críticamente por " + dano + ".",
						FontTypeNames.FONTTYPE_FIGHT);

			} else {

				Declaraciones.Npclist[VictimNpcIndex].Stats.MinHp = Declaraciones.Npclist[VictimNpcIndex].Stats.MinHp
						- dano;
				Protocol.WriteConsoleMsg(UserIndex, "Has golpeado críticamente a la criatura por " + dano + ".",
						FontTypeNames.FONTTYPE_FIGHT);
				SistemaCombate.CalcularDarExp(UserIndex, VictimNpcIndex, dano);

			}

		}

	}

	static void QuitarSta(int UserIndex, int Cantidad) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		Declaraciones.UserList[UserIndex].Stats.MinSta = Declaraciones.UserList[UserIndex].Stats.MinSta - Cantidad;
		if (Declaraciones.UserList[UserIndex].Stats.MinSta < 0) {
			Declaraciones.UserList[UserIndex].Stats.MinSta = 0;
		}
		Protocol.WriteUpdateSta(UserIndex);

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en QuitarSta. Error " + Err.Number + " : " + Err.description);

	}

	static void DoTalar(int UserIndex) {
		DoTalar(UserIndex, false);
	}

	static void DoTalar(int UserIndex, boolean DarMaderaElfica) {
		/* '*************************************************** */
		/* 'Autor: Unknown */
		/* 'Last Modification: 28/05/2010 */
		/* '16/11/2009: ZaMa - Ahora Se puede dar madera elfica. */
		/* '16/11/2009: ZaMa - Implementado nuevo sistema de extraccion. */
		/*
		 * '11/05/2010: ZaMa - Arreglo formula de maximo de items
		 * contruibles/extraibles.
		 */
		/*
		 * '05/13/2010: Pato - Refix a la formula de maximo de items
		 * construibles/extraibles.
		 */
		/* '22/05/2010: ZaMa - Los caos ya no suben plebe al trabajar. */
		/* '28/05/2010: ZaMa - Los pks no suben plebe al trabajar. */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		int Suerte = 0;
		int res = 0;
		int CantidadItems = 0;
		int Skill = 0;

		if (Declaraciones.UserList[UserIndex].clase == eClass.Worker) {
			QuitarSta(UserIndex, Declaraciones.EsfuerzoTalarLenador);
		} else {
			QuitarSta(UserIndex, Declaraciones.EsfuerzoTalarGeneral);
		}

		Skill = Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Talar];
		Suerte = vb6.Int(-0.00125 * Skill * Skill - 0.3 * Skill + 49);

		res = Matematicas.RandomNumber(1, Suerte);

		if (res <= 6) {
			Declaraciones.Obj MiObj;

			if (Declaraciones.UserList[UserIndex].clase == eClass.Worker) {
				CantidadItems = MaxItemsExtraibles(Declaraciones.UserList[UserIndex].Stats.ELV);

				MiObj.Amount = Matematicas.RandomNumber(1, CantidadItems);
			} else {
				MiObj.Amount = 1;
			}

			MiObj.ObjIndex = vb6.IIf(DarMaderaElfica, Declaraciones.LenaElfica, Declaraciones.Lena);

			if (!InvUsuario.MeterItemEnInventario(UserIndex, MiObj)) {
				InvNpc.TirarItemAlPiso(Declaraciones.UserList[UserIndex].Pos, MiObj);
			}

			Protocol.WriteConsoleMsg(UserIndex, "¡Has conseguido algo de lena!", FontTypeNames.FONTTYPE_INFO);

			UsUaRiOs.SubirSkill(UserIndex, eSkill.Talar, true);
		} else {
			/* '[CDT 17-02-2004] */
			if (!Declaraciones.UserList[UserIndex].flags.UltimoMensaje == 8) {
				Protocol.WriteConsoleMsg(UserIndex, "¡No has obtenido lena!", FontTypeNames.FONTTYPE_INFO);
				Declaraciones.UserList[UserIndex].flags.UltimoMensaje = 8;
			}
			/* '[/CDT] */
			UsUaRiOs.SubirSkill(UserIndex, eSkill.Talar, false);
		}

		if (!ES.criminal(UserIndex)) {
			Declaraciones.UserList[UserIndex].Reputacion.PlebeRep = Declaraciones.UserList[UserIndex].Reputacion.PlebeRep
					+ Declaraciones.vlProleta;
			if (Declaraciones.UserList[UserIndex].Reputacion.PlebeRep > Declaraciones.MAXREP) {
				Declaraciones.UserList[UserIndex].Reputacion.PlebeRep = Declaraciones.MAXREP;
			}
		}

		Declaraciones.UserList[UserIndex].Counters.Trabajando = Declaraciones.UserList[UserIndex].Counters.Trabajando
				+ 1;

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en DoTalar");

	}

	static void DoMineria(int UserIndex) {
		/* '*************************************************** */
		/* 'Autor: Unknown */
		/* 'Last Modification: 28/05/2010 */
		/* '16/11/2009: ZaMa - Implementado nuevo sistema de extraccion. */
		/*
		 * '11/05/2010: ZaMa - Arreglo formula de maximo de items
		 * contruibles/extraibles.
		 */
		/*
		 * '05/13/2010: Pato - Refix a la formula de maximo de items
		 * construibles/extraibles.
		 */
		/* '22/05/2010: ZaMa - Los caos ya no suben plebe al trabajar. */
		/* '28/05/2010: ZaMa - Los pks no suben plebe al trabajar. */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		int Suerte = 0;
		int res = 0;
		int CantidadItems = 0;

		if (Declaraciones.UserList[UserIndex].clase == eClass.Worker) {
			QuitarSta(UserIndex, Declaraciones.EsfuerzoExcavarMinero);
		} else {
			QuitarSta(UserIndex, Declaraciones.EsfuerzoExcavarGeneral);
		}

		int Skill = 0;
		Skill = Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Mineria];
		Suerte = vb6.Int(-0.00125 * Skill * Skill - 0.3 * Skill + 49);

		res = Matematicas.RandomNumber(1, Suerte);

		if (res <= 5) {
			Declaraciones.Obj MiObj;

			if (Declaraciones.UserList[UserIndex].flags.TargetObj == 0) {
				return;
			}

			MiObj.ObjIndex = Declaraciones.ObjData[Declaraciones.UserList[UserIndex].flags.TargetObj].MineralIndex;

			if (Declaraciones.UserList[UserIndex].clase == eClass.Worker) {
				CantidadItems = MaxItemsExtraibles(Declaraciones.UserList[UserIndex].Stats.ELV);

				MiObj.Amount = Matematicas.RandomNumber(1, CantidadItems);
			} else {
				MiObj.Amount = 1;
			}

			if (!InvUsuario.MeterItemEnInventario(UserIndex, MiObj)) {
				InvNpc.TirarItemAlPiso(Declaraciones.UserList[UserIndex].Pos, MiObj);
			}

			Protocol.WriteConsoleMsg(UserIndex, "¡Has extraido algunos minerales!", FontTypeNames.FONTTYPE_INFO);

			UsUaRiOs.SubirSkill(UserIndex, eSkill.Mineria, true);
		} else {
			/* '[CDT 17-02-2004] */
			if (!Declaraciones.UserList[UserIndex].flags.UltimoMensaje == 9) {
				Protocol.WriteConsoleMsg(UserIndex, "¡No has conseguido nada!", FontTypeNames.FONTTYPE_INFO);
				Declaraciones.UserList[UserIndex].flags.UltimoMensaje = 9;
			}
			/* '[/CDT] */
			UsUaRiOs.SubirSkill(UserIndex, eSkill.Mineria, false);
		}

		if (!ES.criminal(UserIndex)) {
			Declaraciones.UserList[UserIndex].Reputacion.PlebeRep = Declaraciones.UserList[UserIndex].Reputacion.PlebeRep
					+ Declaraciones.vlProleta;
			if (Declaraciones.UserList[UserIndex].Reputacion.PlebeRep > Declaraciones.MAXREP) {
				Declaraciones.UserList[UserIndex].Reputacion.PlebeRep = Declaraciones.MAXREP;
			}
		}

		Declaraciones.UserList[UserIndex].Counters.Trabajando = Declaraciones.UserList[UserIndex].Counters.Trabajando
				+ 1;

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en Sub DoMineria");

	}

	static void DoMeditar(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		Declaraciones.UserList[UserIndex].Counters.IdleCount = 0;

		int Suerte = 0;
		int res = 0;
		int cant = 0;
		int MeditarSkill = 0;

		/* 'Barrin 3/10/03 */
		/* 'Esperamos a que se termine de concentrar */
		int TActual = 0;
		TActual = Declaraciones.GetTickCount() && 0x7FFFFFFF;

		int iInterval = 0;
		iInterval = vb6.Int(Declaraciones.UserList[UserIndex].Stats.ELV / 17) * 1000;
		/* ' [TEMPORAL] TIEMPO_INICIOMEDITAR Then */
		if (modNuevoTimer.getInterval(TActual, Declaraciones.UserList[UserIndex].Counters.tInicioMeditar) < iInterval) {
			return;
		}

		if (Declaraciones.UserList[UserIndex].Counters.bPuedeMeditar == false) {
			Declaraciones.UserList[UserIndex].Counters.bPuedeMeditar = true;
		}

		if (Declaraciones.UserList[UserIndex].Stats.MinMAN >= Declaraciones.UserList[UserIndex].Stats.MaxMAN) {
			Protocol.WriteConsoleMsg(UserIndex, "Has terminado de meditar.", FontTypeNames.FONTTYPE_INFO);
			Protocol.WriteMeditateToggle(UserIndex);
			Declaraciones.UserList[UserIndex].flags.Meditando = false;
			Declaraciones.UserList[UserIndex].Char.FX = 0;
			Declaraciones.UserList[UserIndex].Char.loops = 0;
			modSendData.SendData(SendTarget.ToPCArea, UserIndex,
					Protocol.PrepareMessageCreateFX(Declaraciones.UserList[UserIndex].Char.CharIndex, 0, 0));
			return;
		}

		MeditarSkill = Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Meditar];

		if (MeditarSkill <= 10) {
			Suerte = 35;
		} else if (MeditarSkill <= 20) {
			Suerte = 30;
		} else if (MeditarSkill <= 30) {
			Suerte = 28;
		} else if (MeditarSkill <= 40) {
			Suerte = 24;
		} else if (MeditarSkill <= 50) {
			Suerte = 22;
		} else if (MeditarSkill <= 60) {
			Suerte = 20;
		} else if (MeditarSkill <= 70) {
			Suerte = 18;
		} else if (MeditarSkill <= 80) {
			Suerte = 15;
		} else if (MeditarSkill <= 90) {
			Suerte = 10;
		} else if (MeditarSkill < 100) {
			Suerte = 7;
		} else {
			Suerte = 5;
		}
		res = Matematicas.RandomNumber(1, Suerte);

		if (res == 1) {

			cant = Matematicas.Porcentaje(Declaraciones.UserList[UserIndex].Stats.MaxMAN, Admin.PorcentajeRecuperoMana);
			if (cant <= 0) {
				cant = 1;
			}
			Declaraciones.UserList[UserIndex].Stats.MinMAN = Declaraciones.UserList[UserIndex].Stats.MinMAN + cant;
			if (Declaraciones.UserList[UserIndex].Stats.MinMAN > Declaraciones.UserList[UserIndex].Stats.MaxMAN) {
				Declaraciones.UserList[UserIndex].Stats.MinMAN = Declaraciones.UserList[UserIndex].Stats.MaxMAN;
			}

			if (!Declaraciones.UserList[UserIndex].flags.UltimoMensaje == 22) {
				Protocol.WriteConsoleMsg(UserIndex, "¡Has recuperado " + cant + " puntos de maná!",
						FontTypeNames.FONTTYPE_INFO);
				Declaraciones.UserList[UserIndex].flags.UltimoMensaje = 22;
			}

			Protocol.WriteUpdateMana(UserIndex);
			UsUaRiOs.SubirSkill(UserIndex, eSkill.Meditar, true);
		} else {
			UsUaRiOs.SubirSkill(UserIndex, eSkill.Meditar, false);
		}
	}

	static void DoDesequipar(int UserIndex, int VictimIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modif: 15/04/2010 */
		/* 'Unequips either shield, weapon or helmet from target user. */
		/* '*************************************************** */

		int Probabilidad = 0;
		int Resultado = 0;
		int WrestlingSkill = 0;
		boolean AlgoEquipado = false;

		/* ' Si no tiene guantes de hurto no desequipa. */
		if (Declaraciones.UserList[UserIndex].Invent.AnilloEqpObjIndex != Declaraciones.GUANTE_HURTO) {
			return;
		}

		/* ' Si no esta solo con manos, no desequipa tampoco. */
		if (Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex > 0) {
			return;
		}

		WrestlingSkill = Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Wrestling];

		Probabilidad = WrestlingSkill * 0.2 + Declaraciones.UserList[UserIndex].Stats.ELV * 0.66;

		/* ' Si tiene escudo, intenta desequiparlo */
		if (Declaraciones.UserList[VictimIndex].Invent.EscudoEqpObjIndex > 0) {

			Resultado = Matematicas.RandomNumber(1, 100);

			if (Resultado <= Probabilidad) {
				/* ' Se lo desequipo */
				InvUsuario.Desequipar(VictimIndex, Declaraciones.UserList[VictimIndex].Invent.EscudoEqpSlot, true);

				Protocol.WriteConsoleMsg(UserIndex, "Has logrado desequipar el escudo de tu oponente!",
						FontTypeNames.FONTTYPE_FIGHT);

				if (Declaraciones.UserList[VictimIndex].Stats.ELV < 20) {
					Protocol.WriteConsoleMsg(VictimIndex, "¡Tu oponente te ha desequipado el escudo!",
							FontTypeNames.FONTTYPE_FIGHT);
				}

				Protocol.FlushBuffer(VictimIndex);

				return;
			}

			AlgoEquipado = true;
		}

		/*
		 * ' No tiene escudo, o fallo desequiparlo, entonces trata de desequipar
		 * arma
		 */
		if (Declaraciones.UserList[VictimIndex].Invent.WeaponEqpObjIndex > 0) {

			Resultado = Matematicas.RandomNumber(1, 100);

			if (Resultado <= Probabilidad) {
				/* ' Se lo desequipo */
				InvUsuario.Desequipar(VictimIndex, Declaraciones.UserList[VictimIndex].Invent.WeaponEqpSlot, true);

				Protocol.WriteConsoleMsg(UserIndex, "Has logrado desarmar a tu oponente!",
						FontTypeNames.FONTTYPE_FIGHT);

				if (Declaraciones.UserList[VictimIndex].Stats.ELV < 20) {
					Protocol.WriteConsoleMsg(VictimIndex, "¡Tu oponente te ha desarmado!",
							FontTypeNames.FONTTYPE_FIGHT);
				}

				Protocol.FlushBuffer(VictimIndex);

				return;
			}

			AlgoEquipado = true;
		}

		/*
		 * ' No tiene arma, o fallo desequiparla, entonces trata de desequipar
		 * casco
		 */
		if (Declaraciones.UserList[VictimIndex].Invent.CascoEqpObjIndex > 0) {

			Resultado = Matematicas.RandomNumber(1, 100);

			if (Resultado <= Probabilidad) {
				/* ' Se lo desequipo */
				InvUsuario.Desequipar(VictimIndex, Declaraciones.UserList[VictimIndex].Invent.CascoEqpSlot, true);

				Protocol.WriteConsoleMsg(UserIndex, "Has logrado desequipar el casco de tu oponente!",
						FontTypeNames.FONTTYPE_FIGHT);

				if (Declaraciones.UserList[VictimIndex].Stats.ELV < 20) {
					Protocol.WriteConsoleMsg(VictimIndex, "¡Tu oponente te ha desequipado el casco!",
							FontTypeNames.FONTTYPE_FIGHT);
				}

				Protocol.FlushBuffer(VictimIndex);

				return;
			}

			AlgoEquipado = true;
		}

		if (AlgoEquipado) {
			Protocol.WriteConsoleMsg(UserIndex, "Tu oponente no tiene equipado items!", FontTypeNames.FONTTYPE_FIGHT);
		} else {
			Protocol.WriteConsoleMsg(UserIndex, "No has logrado desequipar ningún item a tu oponente!",
					FontTypeNames.FONTTYPE_FIGHT);
		}

	}

	static void DoHurtar(int UserIndex, int VictimaIndex) {
		/* '*************************************************** */
		/* 'Author: Pablo (ToxicWaste) */
		/* 'Last Modif: 03/03/2010 */
		/* 'Implements the pick pocket skill of the Bandit :) */
		/*
		 * '03/03/2010 - Pato: Sólo se puede hurtar si no está en trigger 6 :)
		 */
		/* '*************************************************** */
		int OtroUserIndex = 0;

		if (SistemaCombate.TriggerZonaPelea(UserIndex, VictimaIndex) != TRIGGER6_AUSENTE) {
			return;
		}

		if (Declaraciones.UserList[UserIndex].clase != eClass.Bandit) {
			return;
		}
		/*
		 * 'Esto es precario y feo, pero por ahora no se me ocurrió nada mejor.
		 */
		/* 'Uso el slot de los anillos para "equipar" los guantes. */
		/* 'Y los reconozco porque les puse DefensaMagicaMin y Max = 0 */
		if (Declaraciones.UserList[UserIndex].Invent.AnilloEqpObjIndex != Declaraciones.GUANTE_HURTO) {
			return;
		}

		int res = 0;
		res = Matematicas.RandomNumber(1, 100);
		if ((res < 20)) {
			if (InvUsuario.TieneObjetosRobables(VictimaIndex)) {

				if (Declaraciones.UserList[VictimaIndex].flags.Comerciando) {
					OtroUserIndex = Declaraciones.UserList[VictimaIndex].ComUsu.DestUsu;

					if (OtroUserIndex > 0 && OtroUserIndex <= Declaraciones.MaxUsers) {
						Protocol.WriteConsoleMsg(VictimaIndex, "¡¡Comercio cancelado, te están robando!!",
								FontTypeNames.FONTTYPE_TALK);
						Protocol.WriteConsoleMsg(OtroUserIndex, "¡¡Comercio cancelado por el otro usuario!!",
								FontTypeNames.FONTTYPE_TALK);

						TCP.LimpiarComercioSeguro(VictimaIndex);
						Protocol.FlushBuffer(OtroUserIndex);
					}
				}

				RobarObjeto(UserIndex, VictimaIndex);
				Protocol.WriteConsoleMsg(VictimaIndex, "¡" + Declaraciones.UserList[UserIndex].Name + " es un Bandido!",
						FontTypeNames.FONTTYPE_INFO);
			} else {
				Protocol.WriteConsoleMsg(UserIndex, Declaraciones.UserList[VictimaIndex].Name + " no tiene objetos.",
						FontTypeNames.FONTTYPE_INFO);
			}
		}

	}

	static void DoHandInmo(int UserIndex, int VictimaIndex) {
		/* '*************************************************** */
		/* 'Author: Pablo (ToxicWaste) */
		/* 'Last Modif: 17/02/2007 */
		/* 'Implements the special Skill of the Thief */
		/* '*************************************************** */
		if (Declaraciones.UserList[VictimaIndex].flags.Paralizado == 1) {
			return;
		}
		if (Declaraciones.UserList[UserIndex].clase != eClass.Thief) {
			return;
		}

		if (Declaraciones.UserList[UserIndex].Invent.AnilloEqpObjIndex != Declaraciones.GUANTE_HURTO) {
			return;
		}

		int res = 0;
		res = Matematicas.RandomNumber(0, 100);
		if (res < (Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Wrestling] / 4)) {
			Declaraciones.UserList[VictimaIndex].flags.Paralizado = 1;
			Declaraciones.UserList[VictimaIndex].Counters.Paralisis = Admin.IntervaloParalizado / 2;

			Declaraciones.UserList[VictimaIndex].flags.ParalizedByIndex = UserIndex;
			Declaraciones.UserList[VictimaIndex].flags.ParalizedBy = Declaraciones.UserList[UserIndex].Name;

			Protocol.WriteParalizeOK(VictimaIndex);
			Protocol.WriteConsoleMsg(UserIndex, "Tu golpe ha dejado inmóvil a tu oponente",
					FontTypeNames.FONTTYPE_INFO);
			Protocol.WriteConsoleMsg(VictimaIndex, "¡El golpe te ha dejado inmóvil!", FontTypeNames.FONTTYPE_INFO);
		}

	}

	static void Desarmar(int UserIndex, int VictimIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 02/04/2010 (ZaMa) */
		/* '02/04/2010: ZaMa - Nueva formula para desarmar. */
		/* '*************************************************** */

		int Probabilidad = 0;
		int Resultado = 0;
		int WrestlingSkill = 0;

		WrestlingSkill = Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Wrestling];

		Probabilidad = WrestlingSkill * 0.2 + Declaraciones.UserList[UserIndex].Stats.ELV * 0.66;

		Resultado = Matematicas.RandomNumber(1, 100);

		if (Resultado <= Probabilidad) {
			InvUsuario.Desequipar(VictimIndex, Declaraciones.UserList[VictimIndex].Invent.WeaponEqpSlot, true);
			Protocol.WriteConsoleMsg(UserIndex, "Has logrado desarmar a tu oponente!", FontTypeNames.FONTTYPE_FIGHT);
			if (Declaraciones.UserList[VictimIndex].Stats.ELV < 20) {
				Protocol.WriteConsoleMsg(VictimIndex, "¡Tu oponente te ha desarmado!", FontTypeNames.FONTTYPE_FIGHT);
			}
			Protocol.FlushBuffer(VictimIndex);
		}

	}

	static int MaxItemsConstruibles(int UserIndex) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 29/01/2010 */
		/*
		 * '11/05/2010: ZaMa - Arreglo formula de maximo de items
		 * contruibles/extraibles.
		 */
		/*
		 * '05/13/2010: Pato - Refix a la formula de maximo de items
		 * construibles/extraibles.
		 */
		/* '*************************************************** */

		if (Declaraciones.UserList[UserIndex].clase == eClass.Worker) {
			retval = SistemaCombate.MaximoInt(1, vb6.CInt((Declaraciones.UserList[UserIndex].Stats.ELV - 2) * 0.2));
		} else {
			retval = 1;
		}
		return retval;
	}

	static int MaxItemsExtraibles(int UserLevel) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 14/05/2010 */
		/* '*************************************************** */
		retval = SistemaCombate.MaximoInt(1, vb6.CInt((UserLevel - 2) * 0.2)) + 1;
		return retval;
	}

	static void ImitateNpc(int UserIndex, int NpcIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 20/11/2010 */
		/* 'Copies body, head and desc from previously clicked npc. */
		/* '*************************************************** */

		/* ' Copy desc */
		Declaraciones.UserList[UserIndex].DescRM = Declaraciones.Npclist[NpcIndex].Name;

		/* ' Remove Anims (Npcs don't use equipment anims yet) */
		Declaraciones.UserList[UserIndex].Char.CascoAnim = Declaraciones.NingunCasco;
		Declaraciones.UserList[UserIndex].Char.ShieldAnim = Declaraciones.NingunEscudo;
		Declaraciones.UserList[UserIndex].Char.WeaponAnim = Declaraciones.NingunArma;

		/* ' If admin is invisible the store it in old char */
		if (Declaraciones.UserList[UserIndex].flags.AdminInvisible == 1
				|| Declaraciones.UserList[UserIndex].flags.invisible == 1
				|| Declaraciones.UserList[UserIndex].flags.Oculto == 1) {

			Declaraciones.UserList[UserIndex].flags.OldBody = Declaraciones.Npclist[NpcIndex].Char.body;
			Declaraciones.UserList[UserIndex].flags.OldHead = Declaraciones.Npclist[NpcIndex].Char.Head;
		} else {
			Declaraciones.UserList[UserIndex].Char.body = Declaraciones.Npclist[NpcIndex].Char.body;
			Declaraciones.UserList[UserIndex].Char.Head = Declaraciones.Npclist[NpcIndex].Char.Head;

			UsUaRiOs.ChangeUserChar(UserIndex, Declaraciones.UserList[UserIndex].Char.body,
					Declaraciones.UserList[UserIndex].Char.Head, Declaraciones.UserList[UserIndex].Char.heading,
					Declaraciones.UserList[UserIndex].Char.WeaponAnim,
					Declaraciones.UserList[UserIndex].Char.ShieldAnim,
					Declaraciones.UserList[UserIndex].Char.CascoAnim);
		}

	}

}