
/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"modNuevoTimer"')] */
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

public class modNuevoTimer {

	/* ' */
	/* ' Las siguientes funciones devuelven TRUE o FALSE si el intervalo */
	/* ' permite hacerlo. Si devuelve TRUE, setean automaticamente el */
	/* ' timer para que no se pueda hacer la accion hasta el nuevo ciclo. */
	/* ' */

	/* ' CASTING DE HECHIZOS */
	static boolean IntervaloPermiteLanzarSpell(int UserIndex) {
		return IntervaloPermiteLanzarSpell(UserIndex, true);
	}

	static boolean IntervaloPermiteLanzarSpell(int UserIndex, boolean Actualizar) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int TActual;

		TActual = Declaraciones.GetTickCount() && 0x7FFFFFFF;

		if (getInterval(TActual,
				Declaraciones.UserList[UserIndex].Counters.TimerLanzarSpell) >= Admin.IntervaloUserPuedeCastear) {
			if (Actualizar) {
				Declaraciones.UserList[UserIndex].Counters.TimerLanzarSpell = TActual;
			}
			retval = true;
		} else {
			retval = false;
		}

		return retval;
	}

	static boolean IntervaloPermiteAtacar(int UserIndex) {
		return IntervaloPermiteAtacar(UserIndex, true);
	}

	static boolean IntervaloPermiteAtacar(int UserIndex, boolean Actualizar) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int TActual;

		TActual = Declaraciones.GetTickCount() && 0x7FFFFFFF;

		if (getInterval(TActual,
				Declaraciones.UserList[UserIndex].Counters.TimerPuedeAtacar) >= Admin.IntervaloUserPuedeAtacar) {
			if (Actualizar) {
				Declaraciones.UserList[UserIndex].Counters.TimerPuedeAtacar = TActual;
				Declaraciones.UserList[UserIndex].Counters.TimerGolpeUsar = TActual;
			}
			retval = true;
		} else {
			retval = false;
		}
		return retval;
	}

	static boolean IntervaloPermiteGolpeUsar(int UserIndex) {
		return IntervaloPermiteGolpeUsar(UserIndex, true);
	}

	static boolean IntervaloPermiteGolpeUsar(int UserIndex, boolean Actualizar) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/*
		 * 'Checks if the time that passed from the last hit is enough for the
		 * user to use a potion.
		 */
		/* 'Last Modification: 06/04/2009 */
		/* '*************************************************** */

		int TActual;

		TActual = Declaraciones.GetTickCount() && 0x7FFFFFFF;

		if (getInterval(TActual,
				Declaraciones.UserList[UserIndex].Counters.TimerGolpeUsar) >= Admin.IntervaloGolpeUsar) {
			if (Actualizar) {
				Declaraciones.UserList[UserIndex].Counters.TimerGolpeUsar = TActual;
			}
			retval = true;
		} else {
			retval = false;
		}
		return retval;
	}

	static boolean IntervaloPermiteMagiaGolpe(int UserIndex) {
		return IntervaloPermiteMagiaGolpe(UserIndex, true);
	}

	static boolean IntervaloPermiteMagiaGolpe(int UserIndex, boolean Actualizar) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */
		int TActual;

		if (Declaraciones.UserList[UserIndex].Counters.TimerMagiaGolpe > Declaraciones.UserList[UserIndex].Counters.TimerLanzarSpell) {
			return retval;
		}

		TActual = Declaraciones.GetTickCount() && 0x7FFFFFFF;

		if (getInterval(TActual,
				Declaraciones.UserList[UserIndex].Counters.TimerLanzarSpell) >= Admin.IntervaloMagiaGolpe) {
			if (Actualizar) {
				Declaraciones.UserList[UserIndex].Counters.TimerMagiaGolpe = TActual;
				Declaraciones.UserList[UserIndex].Counters.TimerPuedeAtacar = TActual;
				Declaraciones.UserList[UserIndex].Counters.TimerGolpeUsar = TActual;
			}
			retval = true;
		} else {
			retval = false;
		}
		return retval;
	}

	static boolean IntervaloPermiteGolpeMagia(int UserIndex) {
		return IntervaloPermiteGolpeMagia(UserIndex, true);
	}

	static boolean IntervaloPermiteGolpeMagia(int UserIndex, boolean Actualizar) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int TActual;

		if (Declaraciones.UserList[UserIndex].Counters.TimerGolpeMagia > Declaraciones.UserList[UserIndex].Counters.TimerPuedeAtacar) {
			return retval;
		}

		TActual = Declaraciones.GetTickCount() && 0x7FFFFFFF;

		if (getInterval(TActual,
				Declaraciones.UserList[UserIndex].Counters.TimerPuedeAtacar) >= Admin.IntervaloGolpeMagia) {
			if (Actualizar) {
				Declaraciones.UserList[UserIndex].Counters.TimerGolpeMagia = TActual;
				Declaraciones.UserList[UserIndex].Counters.TimerLanzarSpell = TActual;
			}
			retval = true;
		} else {
			retval = false;
		}
		return retval;
	}

	/* ' ATAQUE CUERPO A CUERPO */
	/*
	 * 'Public Function IntervaloPermiteAtacar(ByVal UserIndex As Integer,
	 * Optional ByVal Actualizar As Boolean = True) As Boolean
	 */
	/* 'Dim TActual As Long */
	/* ' */
	/* 'TActual = GetTickCount() And &H7FFFFFFF'' */
	/* ' */
	/*
	 * 'If TActual - UserList(UserIndex).Counters.TimerPuedeAtacar >=
	 * IntervaloUserPuedeAtacar Then
	 */
	/*
	 * ' If Actualizar Then UserList(UserIndex).Counters.TimerPuedeAtacar =
	 * TActual
	 */
	/* ' IntervaloPermiteAtacar = True */
	/* 'Else */
	/* ' IntervaloPermiteAtacar = False */
	/* 'End If */
	/* 'End Function */

	/* ' TRABAJO */
	static boolean IntervaloPermiteTrabajar(int UserIndex) {
		return IntervaloPermiteTrabajar(UserIndex, true);
	}

	static boolean IntervaloPermiteTrabajar(int UserIndex, boolean Actualizar) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int TActual;

		TActual = Declaraciones.GetTickCount() && 0x7FFFFFFF;

		if (getInterval(TActual,
				Declaraciones.UserList[UserIndex].Counters.TimerPuedeTrabajar) >= Admin.IntervaloUserPuedeTrabajar) {
			if (Actualizar) {
				Declaraciones.UserList[UserIndex].Counters.TimerPuedeTrabajar = TActual;
			}
			retval = true;
		} else {
			retval = false;
		}
		return retval;
	}

	/* ' USAR OBJETOS */
	static boolean IntervaloPermiteUsar(int UserIndex) {
		return IntervaloPermiteUsar(UserIndex, true);
	}

	static boolean IntervaloPermiteUsar(int UserIndex, boolean Actualizar) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 25/01/2010 (ZaMa) */
		/* '25/01/2010: ZaMa - General adjustments. */
		/* '*************************************************** */

		int TActual;

		TActual = Declaraciones.GetTickCount() && 0x7FFFFFFF;

		if (getInterval(TActual,
				Declaraciones.UserList[UserIndex].Counters.TimerUsar) >= Admin.IntervaloUserPuedeUsar) {
			if (Actualizar) {
				Declaraciones.UserList[UserIndex].Counters.TimerUsar = TActual;
				/* 'UserList(UserIndex).Counters.failedUsageAttempts = 0 */
			}
			retval = true;
		} else {
			retval = false;

			/*
			 * 'UserList(UserIndex).Counters.failedUsageAttempts =
			 * UserList(UserIndex).Counters.failedUsageAttempts + 1
			 */

			/*
			 * 'Tolerancia arbitraria - 20 es MUY alta, la está chiteando
			 * zarpado
			 */
			/* 'If UserList(UserIndex).Counters.failedUsageAttempts = 20 Then */
			/*
			 * 'Call SendData(SendTarget.ToAdmins, 0,
			 * PrepareMessageConsoleMsg(UserList(UserIndex).name &
			 * " kicked by the server por posible modificación de intervalos.",
			 * FontTypeNames.FONTTYPE_FIGHT))
			 */
			/* 'Call CloseSocket(UserIndex) */
			/* 'End If */
		}

		return retval;
	}

	static boolean IntervaloPermiteUsarArcos(int UserIndex) {
		return IntervaloPermiteUsarArcos(UserIndex, true);
	}

	static boolean IntervaloPermiteUsarArcos(int UserIndex, boolean Actualizar) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int TActual;

		TActual = Declaraciones.GetTickCount() && 0x7FFFFFFF;

		if (getInterval(TActual,
				Declaraciones.UserList[UserIndex].Counters.TimerPuedeUsarArco) >= Admin.IntervaloFlechasCazadores) {
			if (Actualizar) {
				Declaraciones.UserList[UserIndex].Counters.TimerPuedeUsarArco = TActual;
			}
			retval = true;
		} else {
			retval = false;
		}

		return retval;
	}

	static boolean IntervaloPermiteSerAtacado(int UserIndex) {
		return IntervaloPermiteSerAtacado(UserIndex, false);
	}

	static boolean IntervaloPermiteSerAtacado(int UserIndex, boolean Actualizar) {
		boolean retval;
		/* '************************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modify by: ZaMa */
		/* 'Last Modify Date: 13/11/2009 */
		/*
		 * '13/11/2009: ZaMa - Add the Timer which determines wether the user
		 * can be atacked by a NPc or not
		 */
		/* '************************************************************** */
		int TActual;

		TActual = Declaraciones.GetTickCount() && 0x7FFFFFFF;

		/* ' Inicializa el timer */
		if (Actualizar) {
			Declaraciones.UserList[UserIndex].Counters.TimerPuedeSerAtacado = TActual;
			Declaraciones.UserList[UserIndex].flags.NoPuedeSerAtacado = true;
			retval = false;
		} else {
			if (getInterval(TActual,
					Declaraciones.UserList[UserIndex].Counters.TimerPuedeSerAtacado) >= Admin.IntervaloPuedeSerAtacado) {
				Declaraciones.UserList[UserIndex].flags.NoPuedeSerAtacado = false;
				retval = true;
			} else {
				retval = false;
			}
		}

		return retval;
	}

	static boolean IntervaloPerdioNpc(int UserIndex) {
		return IntervaloPerdioNpc(UserIndex, false);
	}

	static boolean IntervaloPerdioNpc(int UserIndex, boolean Actualizar) {
		boolean retval;
		/* '************************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modify by: ZaMa */
		/* 'Last Modify Date: 13/11/2009 */
		/*
		 * '13/11/2009: ZaMa - Add the Timer which determines wether the user
		 * still owns a Npc or not
		 */
		/* '************************************************************** */
		int TActual;

		TActual = Declaraciones.GetTickCount() && 0x7FFFFFFF;

		/* ' Inicializa el timer */
		if (Actualizar) {
			Declaraciones.UserList[UserIndex].Counters.TimerPerteneceNpc = TActual;
			retval = false;
		} else {
			if (getInterval(TActual,
					Declaraciones.UserList[UserIndex].Counters.TimerPerteneceNpc) >= Admin.IntervaloOwnedNpc) {
				retval = true;
			} else {
				retval = false;
			}
		}

		return retval;
	}

	static boolean IntervaloEstadoAtacable(int UserIndex) {
		return IntervaloEstadoAtacable(UserIndex, false);
	}

	static boolean IntervaloEstadoAtacable(int UserIndex, boolean Actualizar) {
		boolean retval;
		/* '************************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modify by: ZaMa */
		/* 'Last Modify Date: 13/01/2010 */
		/*
		 * '13/01/2010: ZaMa - Add the Timer which determines wether the user
		 * can be atacked by an user or not
		 */
		/* '************************************************************** */
		int TActual;

		TActual = Declaraciones.GetTickCount() && 0x7FFFFFFF;

		/* ' Inicializa el timer */
		if (Actualizar) {
			Declaraciones.UserList[UserIndex].Counters.TimerEstadoAtacable = TActual;
			retval = true;
		} else {
			if (getInterval(TActual,
					Declaraciones.UserList[UserIndex].Counters.TimerEstadoAtacable) >= Admin.IntervaloAtacable) {
				retval = false;
			} else {
				retval = true;
			}
		}

		return retval;
	}

	static boolean IntervaloGoHome(int UserIndex) {
 return  IntervaloGoHome(UserIndex, int(), false);
 }

	static boolean IntervaloGoHome(int UserIndex, int TimeInterval, boolean Actualizar) {
		boolean retval;
		/* '************************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modify by: ZaMa */
		/* 'Last Modify Date: 01/06/2010 */
		/*
		 * '01/06/2010: ZaMa - Add the Timer which determines wether the user
		 * can be teleported to its home or not
		 */
		/* '************************************************************** */
		int TActual;

		TActual = Declaraciones.GetTickCount() && 0x7FFFFFFF;

		/* ' Inicializa el timer */
		if (Actualizar) {
			Declaraciones.UserList[UserIndex].flags.Traveling = 1;
			Declaraciones.UserList[UserIndex].Counters.goHome = TActual + TimeInterval;
		} else {
			if (TActual >= Declaraciones.UserList[UserIndex].Counters.goHome) {
				retval = true;
			}
		}

		return retval;
	}

	static boolean checkInterval(int /* FIXME BYREF!! */ startTime, int timeNow, int interval) {
		boolean retval;
		int lInterval;

		if (timeNow < startTime) {
			lInterval = 0x7FFFFFFF - startTime + timeNow + 1;
		} else {
			lInterval = timeNow - startTime;
		}

		if (lInterval >= interval) {
			startTime = timeNow;
			retval = true;
		} else {
			retval = false;
		}
		return retval;
	}

	static int getInterval(int timeNow, int startTime) {
		int retval;
		if (timeNow < startTime) {
			retval = 0x7FFFFFFF - startTime + timeNow + 1;
		} else {
			retval = timeNow - startTime;
		}
		return retval;
	}

}