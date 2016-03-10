
/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"UsUaRiOs"')] */
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

public class UsUaRiOs {

	/* '?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿ */
	/* ' Modulo Usuarios */
	/* '?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿ */
	/* 'Rutinas de los usuarios */
	/* '?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿ */

	static void ActStats(int VictimIndex, int AttackerIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 11/03/2010 */
		/*
		 * '11/03/2010: ZaMa - Ahora no te vuelve cirminal por matar un atacable
		 */
		/* '*************************************************** */

		int DaExp = 0;
		boolean EraCriminal = false;

		DaExp = vb6.CInt(Declaraciones.UserList[VictimIndex].Stats.ELV) * 2;

		Declaraciones.UserList[AttackerIndex].Stats.Exp = Declaraciones.UserList[AttackerIndex].Stats.Exp + DaExp;
		if (Declaraciones.UserList[AttackerIndex].Stats.Exp > Declaraciones.MAXEXP) {
			Declaraciones.UserList[AttackerIndex].Stats.Exp = Declaraciones.MAXEXP;
		}

		if (SistemaCombate.TriggerZonaPelea(VictimIndex, AttackerIndex) != TRIGGER6_PERMITE) {

			/* ' Es legal matarlo si estaba en atacable */
			if (Declaraciones.UserList[VictimIndex].flags.AtacablePor != AttackerIndex) {
				EraCriminal = ES.criminal(AttackerIndex);

				if (!ES.criminal(VictimIndex)) {
					Declaraciones.UserList[AttackerIndex].Reputacion.AsesinoRep = Declaraciones.UserList[AttackerIndex].Reputacion.AsesinoRep
							+ Declaraciones.vlASESINO * 2;
					if (Declaraciones.UserList[AttackerIndex].Reputacion.AsesinoRep > Declaraciones.MAXREP) {
						Declaraciones.UserList[AttackerIndex].Reputacion.AsesinoRep = Declaraciones.MAXREP;
					}
					Declaraciones.UserList[AttackerIndex].Reputacion.BurguesRep = 0;
					Declaraciones.UserList[AttackerIndex].Reputacion.NobleRep = 0;
					Declaraciones.UserList[AttackerIndex].Reputacion.PlebeRep = 0;
				} else {
					Declaraciones.UserList[AttackerIndex].Reputacion.NobleRep = Declaraciones.UserList[AttackerIndex].Reputacion.NobleRep
							+ Declaraciones.vlNoble;
					if (Declaraciones.UserList[AttackerIndex].Reputacion.NobleRep > Declaraciones.MAXREP) {
						Declaraciones.UserList[AttackerIndex].Reputacion.NobleRep = Declaraciones.MAXREP;
					}
				}

				boolean EsCriminal = false;
				EsCriminal = ES.criminal(AttackerIndex);

				if (EraCriminal != EsCriminal) {
					RefreshCharStatus(AttackerIndex);
				}

			}
		}

		/* 'Lo mata */
		Protocol.WriteMultiMessage(AttackerIndex, eMessages.HaveKilledUser, VictimIndex, DaExp);
		Protocol.WriteMultiMessage(VictimIndex, eMessages.UserKill, AttackerIndex);

		Protocol.FlushBuffer(VictimIndex);

		/* 'Log */
		General.LogAsesinato(
				Declaraciones.UserList[AttackerIndex].Name + " asesino a " + Declaraciones.UserList[VictimIndex].Name);
	}

	static void RevivirUsuario(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		Declaraciones.UserList[UserIndex].flags.Muerto = 0;
		Declaraciones.UserList[UserIndex].Stats.MinHp = Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Constitucion];

		if (Declaraciones.UserList[UserIndex].Stats.MinHp > Declaraciones.UserList[UserIndex].Stats.MaxHp) {
			Declaraciones.UserList[UserIndex].Stats.MinHp = Declaraciones.UserList[UserIndex].Stats.MaxHp;
		}

		if (Declaraciones.UserList[UserIndex].flags.Navegando == 1) {
			ToggleBoatBody(UserIndex);
		} else {
			General.DarCuerpoDesnudo(UserIndex);

			Declaraciones.UserList[UserIndex].Char.Head = Declaraciones.UserList[UserIndex].OrigChar.Head;
		}

		if (Declaraciones.UserList[UserIndex].flags.Traveling) {
			Declaraciones.UserList[UserIndex].flags.Traveling = 0;
			Declaraciones.UserList[UserIndex].Counters.goHome = 0;
			Protocol.WriteMultiMessage(UserIndex, eMessages.CancelHome);
		}

		ChangeUserChar(UserIndex, Declaraciones.UserList[UserIndex].Char.body,
				Declaraciones.UserList[UserIndex].Char.Head, Declaraciones.UserList[UserIndex].Char.heading,
				Declaraciones.UserList[UserIndex].Char.WeaponAnim, Declaraciones.UserList[UserIndex].Char.ShieldAnim,
				Declaraciones.UserList[UserIndex].Char.CascoAnim);
		Protocol.WriteUpdateUserStats(UserIndex);
	}

	static void ToggleBoatBody(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 25/07/2010 */
		/* 'Gives boat body depending on user alignment. */
		/*
		 * '25/07/2010: ZaMa - Now makes difference depending on faccion and
		 * atacable status.
		 */
		/* '*************************************************** */

		int Ropaje = 0;
		boolean EsFaccionario = false;
		int NewBody = 0;

		Declaraciones.UserList[UserIndex].Char.Head = 0;
		if (Declaraciones.UserList[UserIndex].Invent.BarcoObjIndex == 0) {
			return;
		}

		Ropaje = Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.BarcoObjIndex].Ropaje;

		/* ' Criminales y caos */
		if (ES.criminal(UserIndex)) {

			EsFaccionario = Extra.esCaos(UserIndex);

			switch (Ropaje) {
			case Declaraciones.iBarca:
				if (EsFaccionario) {
					NewBody = Declaraciones.iBarcaCaos;
				} else {
					NewBody = Declaraciones.iBarcaPk;
				}

				break;

			case Declaraciones.iGalera:
				if (EsFaccionario) {
					NewBody = Declaraciones.iGaleraCaos;
				} else {
					NewBody = Declaraciones.iGaleraPk;
				}

				break;

			case Declaraciones.iGaleon:
				if (EsFaccionario) {
					NewBody = Declaraciones.iGaleonCaos;
				} else {
					NewBody = Declaraciones.iGaleonPk;
				}
				break;
			}

			/* ' Ciudas y Armadas */
		} else {

			EsFaccionario = Extra.esArmada(UserIndex);

			/* ' Atacable */
			if (Declaraciones.UserList[UserIndex].flags.AtacablePor != 0) {

				switch (Ropaje) {
				case Declaraciones.iBarca:
					if (EsFaccionario) {
						NewBody = Declaraciones.iBarcaRealAtacable;
					} else {
						NewBody = Declaraciones.iBarcaCiudaAtacable;
					}

					break;

				case Declaraciones.iGalera:
					if (EsFaccionario) {
						NewBody = Declaraciones.iGaleraRealAtacable;
					} else {
						NewBody = Declaraciones.iGaleraCiudaAtacable;
					}

					break;

				case Declaraciones.iGaleon:
					if (EsFaccionario) {
						NewBody = Declaraciones.iGaleonRealAtacable;
					} else {
						NewBody = Declaraciones.iGaleonCiudaAtacable;
					}
					break;
				}

				/* ' Normal */
			} else {

				switch (Ropaje) {
				case Declaraciones.iBarca:
					if (EsFaccionario) {
						NewBody = Declaraciones.iBarcaReal;
					} else {
						NewBody = Declaraciones.iBarcaCiuda;
					}

					break;

				case Declaraciones.iGalera:
					if (EsFaccionario) {
						NewBody = Declaraciones.iGaleraReal;
					} else {
						NewBody = Declaraciones.iGaleraCiuda;
					}

					break;

				case Declaraciones.iGaleon:
					if (EsFaccionario) {
						NewBody = Declaraciones.iGaleonReal;
					} else {
						NewBody = Declaraciones.iGaleonCiuda;
					}
					break;
				}

			}

		}

		Declaraciones.UserList[UserIndex].Char.body = NewBody;
		Declaraciones.UserList[UserIndex].Char.ShieldAnim = Declaraciones.NingunEscudo;
		Declaraciones.UserList[UserIndex].Char.WeaponAnim = Declaraciones.NingunArma;
		Declaraciones.UserList[UserIndex].Char.CascoAnim = Declaraciones.NingunCasco;

	}

	static void ChangeUserChar(int UserIndex, int body, int Head, int heading, int Arma, int Escudo, int casco) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */
		Declaraciones.UserList[UserIndex].Char.body = body;
		Declaraciones.UserList[UserIndex].Char.Head = Head;
		Declaraciones.UserList[UserIndex].Char.heading = heading;
		Declaraciones.UserList[UserIndex].Char.WeaponAnim = Arma;
		Declaraciones.UserList[UserIndex].Char.ShieldAnim = Escudo;
		Declaraciones.UserList[UserIndex].Char.CascoAnim = casco;

		modSendData.SendData(SendTarget.ToPCArea, UserIndex, Protocol.PrepareMessageCharacterChange(body, Head, heading,
				Declaraciones.UserList[UserIndex].Char.CharIndex, Arma, Escudo,
				Declaraciones.UserList[UserIndex].Char.FX, Declaraciones.UserList[UserIndex].Char.loops, casco));
	}

	static int GetWeaponAnim(int UserIndex, int ObjIndex) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: Torres Patricio (Pato) */
		/* 'Last Modification: 03/29/10 */
		/* ' */
		/* '*************************************************** */
		int Tmp = 0;

		Tmp = Declaraciones.ObjData[ObjIndex].WeaponRazaEnanaAnim;

		if (Tmp > 0) {
			if (Declaraciones.UserList[UserIndex].raza == eRaza.Enano
					|| Declaraciones.UserList[UserIndex].raza == eRaza.Gnomo) {
				retval = Tmp;
				return retval;
			}
		}

		retval = Declaraciones.ObjData[ObjIndex].WeaponAnim;
		return retval;
	}

	static void EnviarFama(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int L = 0;

		L = (-Declaraciones.UserList[UserIndex].Reputacion.AsesinoRep)
				+ (-Declaraciones.UserList[UserIndex].Reputacion.BandidoRep)
				+ Declaraciones.UserList[UserIndex].Reputacion.BurguesRep
				+ (-Declaraciones.UserList[UserIndex].Reputacion.LadronesRep)
				+ Declaraciones.UserList[UserIndex].Reputacion.NobleRep
				+ Declaraciones.UserList[UserIndex].Reputacion.PlebeRep;
		L = vb6.Round(L / (double) 6);

		Declaraciones.UserList[UserIndex].Reputacion.Promedio = L;

		Protocol.WriteFame(UserIndex);
	}

	static void EraseUserChar(int UserIndex, boolean IsAdminInvisible) {
		/* '************************************************* */
		/* 'Author: Unknown */
		/* 'Last modified: 08/01/2009 */
		/*
		 * '08/01/2009: ZaMa - No se borra el char de un admin invisible en
		 * todos los clientes excepto en su mismo cliente.
		 */
		/* '************************************************* */

		/* FIXME: ON ERROR GOTO ErrorHandler */

		if (Declaraciones.UserList[UserIndex].Char.CharIndex > 0
				&& Declaraciones.UserList[UserIndex].Char.CharIndex <= Declaraciones.LastChar) {

			Declaraciones.CharList[Declaraciones.UserList[UserIndex].Char.CharIndex] = 0;

			if (Declaraciones.UserList[UserIndex].Char.CharIndex == Declaraciones.LastChar) {
				while (!(Declaraciones.CharList[Declaraciones.LastChar] > 0)) {
					Declaraciones.LastChar = Declaraciones.LastChar - 1;
					if (Declaraciones.LastChar <= 1) {
						break; /* FIXME: EXIT DO */
					}
				}
			}

			/*
			 * ' Si esta invisible, solo el sabe de su propia existencia, es
			 * innecesario borrarlo en los demas clientes
			 */
			if (IsAdminInvisible) {
				TCP.EnviarDatosASlot(UserIndex,
						Protocol.PrepareMessageCharacterRemove(Declaraciones.UserList[UserIndex].Char.CharIndex));
			} else {
				/*
				 * 'Le mandamos el mensaje para que borre el personaje a los
				 * clientes que estén cerca
				 */
				modSendData.SendData(SendTarget.ToPCArea, UserIndex,
						Protocol.PrepareMessageCharacterRemove(Declaraciones.UserList[UserIndex].Char.CharIndex));
			}
		}

		if (General.MapaValido(Declaraciones.UserList[UserIndex].Pos.Map)) {
			ModAreas.QuitarUser(UserIndex, Declaraciones.UserList[UserIndex].Pos.Map);

			Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].UserIndex = 0;
		}

		Declaraciones.UserList[UserIndex].Char.CharIndex = 0;

		Declaraciones.NumChars = Declaraciones.NumChars - 1;
		return;

		/* FIXME: ErrorHandler : */

		String UserName;
		int CharIndex = 0;

		if (UserIndex > 0) {
			UserName = Declaraciones.UserList[UserIndex].Name;
			CharIndex = Declaraciones.UserList[UserIndex].Char.CharIndex;
		}

		General.LogError("Error en EraseUserchar " + Err.Number + ": " + Err.description + ". User: " + UserName
				+ "(UI: " + UserIndex + " - CI: " + CharIndex + ")");
	}

	static void RefreshCharStatus(int UserIndex) {
		/* '************************************************* */
		/* 'Author: Tararira */
		/* 'Last modified: 04/07/2009 */
		/* 'Refreshes the status and tag of UserIndex. */
		/*
		 * '04/07/2009: ZaMa - Ahora mantenes la fragata fantasmal si estas
		 * muerto.
		 */
		/* '************************************************* */
		String ClanTag;
		int NickColor = 0;

		if (Declaraciones.UserList[UserIndex].GuildIndex > 0) {
			ClanTag = modGuilds.GuildName(Declaraciones.UserList[UserIndex].GuildIndex);
			ClanTag = " <" + ClanTag + ">";
		}

		NickColor = GetNickColor(UserIndex);

		if (Declaraciones.UserList[UserIndex].showName) {
			modSendData.SendData(SendTarget.ToPCArea, UserIndex, Protocol.PrepareMessageUpdateTagAndStatus(UserIndex,
					NickColor, Declaraciones.UserList[UserIndex].Name + ClanTag));
		} else {
			modSendData.SendData(SendTarget.ToPCArea, UserIndex,
					Protocol.PrepareMessageUpdateTagAndStatus(UserIndex, NickColor, ""));
		}

		/* 'Si esta navengando, se cambia la barca. */
		if (Declaraciones.UserList[UserIndex].flags.Navegando) {
			if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
				Declaraciones.UserList[UserIndex].Char.body = Declaraciones.iFragataFantasmal;
			} else {
				ToggleBoatBody(UserIndex);
			}

			ChangeUserChar(UserIndex, Declaraciones.UserList[UserIndex].Char.body,
					Declaraciones.UserList[UserIndex].Char.Head, Declaraciones.UserList[UserIndex].Char.heading,
					Declaraciones.UserList[UserIndex].Char.WeaponAnim,
					Declaraciones.UserList[UserIndex].Char.ShieldAnim,
					Declaraciones.UserList[UserIndex].Char.CascoAnim);
		}
	}

	static int GetNickColor(int UserIndex) {
		int retval = 0;
		/* '************************************************* */
		/* 'Author: ZaMa */
		/* 'Last modified: 15/01/2010 */
		/* ' */
		/* '************************************************* */

		if (ES.criminal(UserIndex)) {
			retval = eNickColor.ieCriminal;
		} else {
			retval = eNickColor.ieCiudadano;
		}

		if (Declaraciones.UserList[UserIndex].flags.AtacablePor > 0) {
			retval = retval || eNickColor.ieAtacable;
		}

		return retval;
	}

	static boolean MakeUserChar(boolean toMap, int sndIndex, int UserIndex, int Map, int X, int Y) {
		return MakeUserChar(toMap, sndIndex, UserIndex, Map, X, Y, false);
	}

	static boolean MakeUserChar(boolean toMap, int sndIndex, int UserIndex, int Map, int X, int Y, boolean ButIndex) {
		boolean retval = false;
		/* '************************************************* */
		/* 'Author: Unknown */
		/* 'Last modified: 15/01/2010 */
		/* '23/07/2009: Budi - Ahora se envía el nick */
		/* '15/01/2010: ZaMa - Ahora se envia el color del nick. */
		/* '************************************************* */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int CharIndex = 0;
		String ClanTag;
		int NickColor = 0;
		String UserName;
		int Privileges = 0;

		if (Extra.InMapBounds(Map, X, Y)) {
			/* 'If needed make a new character in list */
			if (Declaraciones.UserList[UserIndex].Char.CharIndex == 0) {
				CharIndex = NextOpenCharIndex();
				Declaraciones.UserList[UserIndex].Char.CharIndex = CharIndex;
				Declaraciones.CharList[CharIndex] = UserIndex;
			}

			/* 'Place character on map if needed */
			if (toMap) {
				Declaraciones.MapData[Map][X][Y].UserIndex = UserIndex;
			}

			/* 'Send make character command to clients */
			if (!toMap) {
				if (Declaraciones.UserList[UserIndex].GuildIndex > 0) {
					ClanTag = modGuilds.GuildName(Declaraciones.UserList[UserIndex].GuildIndex);
				}

				NickColor = GetNickColor(UserIndex);
				Privileges = Declaraciones.UserList[UserIndex].flags.Privilegios;

				/* 'Preparo el nick */
				if (Declaraciones.UserList[UserIndex].showName) {
					UserName = Declaraciones.UserList[UserIndex].Name;

					if (Declaraciones.UserList[UserIndex].flags.EnConsulta) {
						UserName = UserName + " " + Declaraciones.TAG_CONSULT_MODE;
					} else {
						if (Declaraciones.UserList[sndIndex].flags.Privilegios
								&& (PlayerType.User || PlayerType.Consejero || PlayerType.RoleMaster)) {
							if (vb6.LenB(ClanTag) != 0) {
								UserName = UserName + " <" + ClanTag + ">";
							}
						} else {
							if ((Declaraciones.UserList[UserIndex].flags.invisible
									|| Declaraciones.UserList[UserIndex].flags.Oculto)
									&& (!Declaraciones.UserList[UserIndex].flags.AdminInvisible == 1)) {
								UserName = UserName + " " + Declaraciones.TAG_USER_INVISIBLE;
							} else {
								if (vb6.LenB(ClanTag) != 0) {
									UserName = UserName + " <" + ClanTag + ">";
								}
							}
						}
					}
				}

				Protocol.WriteCharacterCreate(sndIndex, Declaraciones.UserList[UserIndex].Char.body,
						Declaraciones.UserList[UserIndex].Char.Head, Declaraciones.UserList[UserIndex].Char.heading,
						Declaraciones.UserList[UserIndex].Char.CharIndex, X, Y,
						Declaraciones.UserList[UserIndex].Char.WeaponAnim,
						Declaraciones.UserList[UserIndex].Char.ShieldAnim, Declaraciones.UserList[UserIndex].Char.FX,
						999, Declaraciones.UserList[UserIndex].Char.CascoAnim, UserName, NickColor, Privileges);
			} else {
				/* 'Hide the name and clan - set privs as normal user */
				ModAreas.AgregarUser(UserIndex, Declaraciones.UserList[UserIndex].Pos.Map, ButIndex);
			}
		}

		retval = true;

		return retval;

		/* FIXME: ErrHandler : */

		String UserErrName;
		int UserMap = 0;
		if (UserIndex > 0) {
			UserErrName = Declaraciones.UserList[UserIndex].Name;
			UserMap = Declaraciones.UserList[UserIndex].Pos.Map;
		}

		String sError;
		sError = "MakeUserChar: num: " + Err.Number + " desc: " + Err.description + ".User: " + UserErrName + "("
				+ UserIndex + "). UserMap: " + UserMap + ". Coor: " + Map + "," + X + "," + Y + ". toMap: " + toMap
				+ ". sndIndex: " + sndIndex + ". CharIndex: " + CharIndex + ". ButIndex: " + ButIndex;

		/* 'Resume Next */
		TCP.CloseSocket(UserIndex);

		/* 'Para ver si clona.. */
		sError = sError + ". MapUserIndex: " + Declaraciones.MapData[Map][X][Y].UserIndex;
		General.LogError(sError);

		return retval;
	}

	/* '' */
	/* ' Checks if the user gets the next level. */
	/* ' */
	/* ' @param UserIndex Specifies reference to user */

	static void CheckUserLevel(int UserIndex) {
		/* '************************************************* */
		/* 'Author: Unknown */
		/* 'Last modified: 08/04/2011 */
		/* 'Chequea que el usuario no halla alcanzado el siguiente nivel, */
		/* 'de lo contrario le da la vida, mana, etc, correspodiente. */
		/* '07/08/2006 Integer - Modificacion de los valores */
		/* '01/10/2007 Tavo - Corregido el BUG de STAT_MAXELV */
		/*
		 * '24/01/2007 Pablo (ToxicWaste) - Agrego modificaciones en ELU al
		 * subir de nivel.
		 */
		/*
		 * '24/01/2007 Pablo (ToxicWaste) - Agrego modificaciones de la subida
		 * de mana de los magos por lvl.
		 */
		/*
		 * '13/03/2007 Pablo (ToxicWaste) - Agrego diferencias entre el 18 y el
		 * 19 en Constitución.
		 */
		/*
		 * '09/01/2008 Pablo (ToxicWaste) - Ahora el incremento de vida por
		 * Consitución se controla desde Balance.dat
		 */
		/*
		 * '12/09/2008 Marco Vanotti (Marco) - Ahora si se llega a nivel 25 y
		 * está en un clan, se lo expulsa para no sumar antifacción
		 */
		/*
		 * '02/03/2009 ZaMa - Arreglada la validacion de expulsion para miembros
		 * de clanes faccionarios que llegan a 25.
		 */
		/*
		 * '11/19/2009 Pato - Modifico la nueva fórmula de maná ganada para el
		 * bandido y se la limito a 499
		 */
		/*
		 * '02/04/2010: ZaMa - Modifico la ganancia de hit por nivel del ladron.
		 */
		/*
		 * '08/04/2011: Amraphen - Arreglada la distribución de probabilidades
		 * para la vida en el caso de promedio entero.
		 */
		/* '************************************************* */
		int Pts = 0;
		int AumentoHIT = 0;
		int AumentoMANA = 0;
		int AumentoSTA = 0;
		int AumentoHP = 0;
		boolean WasNewbie = false;
		double Promedio = 0.0;
		int aux = 0;
		int[] DistVida;
		/* 'Guild Index */
		int GI = 0;

		/* FIXME: ON ERROR GOTO ErrHandler */

		WasNewbie = Extra.EsNewbie(UserIndex);

		while (Declaraciones.UserList[UserIndex].Stats.Exp >= Declaraciones.UserList[UserIndex].Stats.ELU) {

			/* 'Checkea si alcanzó el máximo nivel */
			if (Declaraciones.UserList[UserIndex].Stats.ELV >= Declaraciones.STAT_MAXELV) {
				Declaraciones.UserList[UserIndex].Stats.Exp = 0;
				Declaraciones.UserList[UserIndex].Stats.ELU = 0;
				return;
			}

			/* 'Store it! */
			Statistics.UserLevelUp(UserIndex);

			modSendData.SendData(SendTarget.ToPCArea, UserIndex,
					Protocol.PrepareMessagePlayWave(Declaraciones.SND_NIVEL, Declaraciones.UserList[UserIndex].Pos.X,
							Declaraciones.UserList[UserIndex].Pos.Y));
			Protocol.WriteConsoleMsg(UserIndex, "¡Has subido de nivel!", FontTypeNames.FONTTYPE_INFO);

			if (Declaraciones.UserList[UserIndex].Stats.ELV == 1) {
				Pts = 10;
			} else {
				/* 'For multiple levels being rised at once */
				Pts = Pts + 5;
			}

			Declaraciones.UserList[UserIndex].Stats.ELV = Declaraciones.UserList[UserIndex].Stats.ELV + 1;

			Declaraciones.UserList[UserIndex].Stats.Exp = Declaraciones.UserList[UserIndex].Stats.Exp
					- Declaraciones.UserList[UserIndex].Stats.ELU;

			/* 'Nueva subida de exp x lvl. Pablo (ToxicWaste) */
			if (Declaraciones.UserList[UserIndex].Stats.ELV < 15) {
				Declaraciones.UserList[UserIndex].Stats.ELU = Declaraciones.UserList[UserIndex].Stats.ELU * 1.4;
			} else if (Declaraciones.UserList[UserIndex].Stats.ELV < 21) {
				Declaraciones.UserList[UserIndex].Stats.ELU = Declaraciones.UserList[UserIndex].Stats.ELU * 1.35;
			} else if (Declaraciones.UserList[UserIndex].Stats.ELV < 26) {
				Declaraciones.UserList[UserIndex].Stats.ELU = Declaraciones.UserList[UserIndex].Stats.ELU * 1.3;
			} else if (Declaraciones.UserList[UserIndex].Stats.ELV < 35) {
				Declaraciones.UserList[UserIndex].Stats.ELU = Declaraciones.UserList[UserIndex].Stats.ELU * 1.2;
			} else if (Declaraciones.UserList[UserIndex].Stats.ELV < 40) {
				Declaraciones.UserList[UserIndex].Stats.ELU = Declaraciones.UserList[UserIndex].Stats.ELU * 1.3;
			} else {
				Declaraciones.UserList[UserIndex].Stats.ELU = Declaraciones.UserList[UserIndex].Stats.ELU * 1.375;
			}

			/* 'Calculo subida de vida */
			Promedio = Declaraciones.ModVida[Declaraciones.UserList[UserIndex].clase]
					- (21 - Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Constitucion]) * 0.5;
			aux = Matematicas.RandomNumber(0, 100);

			if (Promedio - vb6.Int(Promedio) == 0.5) {
				/* 'Es promedio semientero */
				DistVida[1] = Declaraciones.DistribucionSemienteraVida[1];
				DistVida[2] = DistVida[1] + Declaraciones.DistribucionSemienteraVida[2];
				DistVida[3] = DistVida[2] + Declaraciones.DistribucionSemienteraVida[3];
				DistVida[4] = DistVida[3] + Declaraciones.DistribucionSemienteraVida[4];

				if (aux <= DistVida[1]) {
					AumentoHP = Promedio + 1.5;
				} else if (aux <= DistVida[2]) {
					AumentoHP = Promedio + 0.5;
				} else if (aux <= DistVida[3]) {
					AumentoHP = Promedio - 0.5;
				} else {
					AumentoHP = Promedio - 1.5;
				}
			} else {
				/* 'Es promedio entero */
				DistVida[1] = Declaraciones.DistribucionEnteraVida[1];
				DistVida[2] = DistVida[1] + Declaraciones.DistribucionEnteraVida[2];
				DistVida[3] = DistVida[2] + Declaraciones.DistribucionEnteraVida[3];
				DistVida[4] = DistVida[3] + Declaraciones.DistribucionEnteraVida[4];
				DistVida[5] = DistVida[4] + Declaraciones.DistribucionEnteraVida[5];

				if (aux <= DistVida[1]) {
					AumentoHP = Promedio + 2;
				} else if (aux <= DistVida[2]) {
					AumentoHP = Promedio + 1;
				} else if (aux <= DistVida[3]) {
					AumentoHP = Promedio;
				} else if (aux <= DistVida[4]) {
					AumentoHP = Promedio - 1;
				} else {
					AumentoHP = Promedio - 2;
				}

			}

			switch (Declaraciones.UserList[UserIndex].clase) {
			case Warrior:
				AumentoHIT = vb6.IIf(Declaraciones.UserList[UserIndex].Stats.ELV > 35, 2, 3);
				AumentoSTA = Declaraciones.AumentoSTDef;

				break;

			case Hunter:
				AumentoHIT = vb6.IIf(Declaraciones.UserList[UserIndex].Stats.ELV > 35, 2, 3);
				AumentoSTA = Declaraciones.AumentoSTDef;

				break;

			case Pirat:
				AumentoHIT = 3;
				AumentoSTA = Declaraciones.AumentoSTDef;

				break;

			case Paladin:
				AumentoHIT = vb6.IIf(Declaraciones.UserList[UserIndex].Stats.ELV > 35, 1, 3);
				AumentoMANA = Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Inteligencia];
				AumentoSTA = Declaraciones.AumentoSTDef;

				break;

			case Thief:
				AumentoHIT = 2;
				AumentoSTA = Declaraciones.AumentoSTLadron;

				break;

			case Mage:
				AumentoHIT = 1;
				AumentoMANA = 2.8 * Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Inteligencia];
				AumentoSTA = Declaraciones.AumentoSTMago;

				break;

			case Worker:
				AumentoHIT = 2;
				AumentoSTA = Declaraciones.AumentoSTTrabajador;

				break;

			case Cleric:
				AumentoHIT = 2;
				AumentoMANA = 2 * Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Inteligencia];
				AumentoSTA = Declaraciones.AumentoSTDef;

				break;

			case Druid:
				AumentoHIT = 2;
				AumentoMANA = 2 * Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Inteligencia];
				AumentoSTA = Declaraciones.AumentoSTDef;

				break;

			case Assasin:
				AumentoHIT = vb6.IIf(Declaraciones.UserList[UserIndex].Stats.ELV > 35, 1, 3);
				AumentoMANA = Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Inteligencia];
				AumentoSTA = Declaraciones.AumentoSTDef;

				break;

			case Bard:
				AumentoHIT = 2;
				AumentoMANA = 2 * Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Inteligencia];
				AumentoSTA = Declaraciones.AumentoSTDef;

				break;

			case Bandit:
				AumentoHIT = vb6.IIf(Declaraciones.UserList[UserIndex].Stats.ELV > 35, 1, 3);
				AumentoMANA = Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Inteligencia]
						/ (double) 3 * 2;
				AumentoSTA = Declaraciones.AumentoStBandido;

				break;

			default:
				AumentoHIT = 2;
				AumentoSTA = Declaraciones.AumentoSTDef;
				break;
			}

			/* 'Actualizamos HitPoints */
			Declaraciones.UserList[UserIndex].Stats.MaxHp = Declaraciones.UserList[UserIndex].Stats.MaxHp + AumentoHP;
			if (Declaraciones.UserList[UserIndex].Stats.MaxHp > Declaraciones.STAT_MAXHP) {
				Declaraciones.UserList[UserIndex].Stats.MaxHp = Declaraciones.STAT_MAXHP;
			}

			/* 'Actualizamos Stamina */
			Declaraciones.UserList[UserIndex].Stats.MaxSta = Declaraciones.UserList[UserIndex].Stats.MaxSta
					+ AumentoSTA;
			if (Declaraciones.UserList[UserIndex].Stats.MaxSta > Declaraciones.STAT_MAXSTA) {
				Declaraciones.UserList[UserIndex].Stats.MaxSta = Declaraciones.STAT_MAXSTA;
			}

			/* 'Actualizamos Mana */
			Declaraciones.UserList[UserIndex].Stats.MaxMAN = Declaraciones.UserList[UserIndex].Stats.MaxMAN
					+ AumentoMANA;
			if (Declaraciones.UserList[UserIndex].Stats.MaxMAN > Declaraciones.STAT_MAXMAN) {
				Declaraciones.UserList[UserIndex].Stats.MaxMAN = Declaraciones.STAT_MAXMAN;
			}

			/* 'Actualizamos Golpe Máximo */
			Declaraciones.UserList[UserIndex].Stats.MaxHIT = Declaraciones.UserList[UserIndex].Stats.MaxHIT
					+ AumentoHIT;
			if (Declaraciones.UserList[UserIndex].Stats.ELV < 36) {
				if (Declaraciones.UserList[UserIndex].Stats.MaxHIT > Declaraciones.STAT_MAXHIT_UNDER36) {
					Declaraciones.UserList[UserIndex].Stats.MaxHIT = Declaraciones.STAT_MAXHIT_UNDER36;
				}
			} else {
				if (Declaraciones.UserList[UserIndex].Stats.MaxHIT > Declaraciones.STAT_MAXHIT_OVER36) {
					Declaraciones.UserList[UserIndex].Stats.MaxHIT = Declaraciones.STAT_MAXHIT_OVER36;
				}
			}

			/* 'Actualizamos Golpe Mínimo */
			Declaraciones.UserList[UserIndex].Stats.MinHIT = Declaraciones.UserList[UserIndex].Stats.MinHIT
					+ AumentoHIT;
			if (Declaraciones.UserList[UserIndex].Stats.ELV < 36) {
				if (Declaraciones.UserList[UserIndex].Stats.MinHIT > Declaraciones.STAT_MAXHIT_UNDER36) {
					Declaraciones.UserList[UserIndex].Stats.MinHIT = Declaraciones.STAT_MAXHIT_UNDER36;
				}
			} else {
				if (Declaraciones.UserList[UserIndex].Stats.MinHIT > Declaraciones.STAT_MAXHIT_OVER36) {
					Declaraciones.UserList[UserIndex].Stats.MinHIT = Declaraciones.STAT_MAXHIT_OVER36;
				}
			}

			/* 'Notificamos al user */
			if (AumentoHP > 0) {
				Protocol.WriteConsoleMsg(UserIndex, "Has ganado " + AumentoHP + " puntos de vida.",
						FontTypeNames.FONTTYPE_INFO);
			}
			if (AumentoSTA > 0) {
				Protocol.WriteConsoleMsg(UserIndex, "Has ganado " + AumentoSTA + " puntos de energía.",
						FontTypeNames.FONTTYPE_INFO);
			}
			if (AumentoMANA > 0) {
				Protocol.WriteConsoleMsg(UserIndex, "Has ganado " + AumentoMANA + " puntos de maná.",
						FontTypeNames.FONTTYPE_INFO);
			}
			if (AumentoHIT > 0) {
				Protocol.WriteConsoleMsg(UserIndex, "Tu golpe máximo aumentó en " + AumentoHIT + " puntos.",
						FontTypeNames.FONTTYPE_INFO);
				Protocol.WriteConsoleMsg(UserIndex, "Tu golpe mínimo aumentó en " + AumentoHIT + " puntos.",
						FontTypeNames.FONTTYPE_INFO);
			}

			General.LogDesarrollo(Declaraciones.UserList[UserIndex].Name + " paso a nivel "
					+ Declaraciones.UserList[UserIndex].Stats.ELV + " gano HP: " + AumentoHP);

			Declaraciones.UserList[UserIndex].Stats.MinHp = Declaraciones.UserList[UserIndex].Stats.MaxHp;

			/*
			 * 'If user is in a party, we modify the variable
			 * p_sumaniveleselevados
			 */
			mdParty.ActualizarSumaNivelesElevados(UserIndex);
			/*
			 * 'If user reaches lvl 25 and he is in a guild, we check the
			 * guild's alignment and expulses the user if guild has factionary
			 * alignment
			 */

			if (Declaraciones.UserList[UserIndex].Stats.ELV == 25) {
				GI = Declaraciones.UserList[UserIndex].GuildIndex;
				if (GI > 0) {
					if (modGuilds.GuildAlignment(GI) == "Del Mal" || modGuilds.GuildAlignment(GI) == "Real") {
						/*
						 * 'We get here, so guild has factionary alignment, we
						 * have to expulse the user
						 */
						modGuilds.m_EcharMiembroDeClan(-1, Declaraciones.UserList[UserIndex].Name);
						modSendData.SendData(SendTarget.ToGuildMembers, GI,
								Protocol.PrepareMessageConsoleMsg(
										Declaraciones.UserList[UserIndex].Name + " deja el clan.",
										FontTypeNames.FONTTYPE_GUILD));
						Protocol.WriteConsoleMsg(UserIndex,
								"¡Ya tienes la madurez suficiente como para decidir bajo que estandarte pelearás! Por esta razón, hasta tanto no te enlistes en la facción bajo la cual tu clan está alineado, estarás excluído del mismo.",
								FontTypeNames.FONTTYPE_GUILD);
					}
				}
			}

		}

		/*
		 * 'If it ceased to be a newbie, remove newbie items and get char away
		 * from newbie dungeon
		 */
		if (!Extra.EsNewbie(UserIndex) && WasNewbie) {
			InvUsuario.QuitarNewbieObj(UserIndex);
			if (Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].Restringir == eRestrict.restrict_newbie) {
				WarpUserChar(UserIndex, 1, 50, 50, true);
				Protocol.WriteConsoleMsg(UserIndex, "Debes abandonar el Dungeon Newbie.", FontTypeNames.FONTTYPE_INFO);
			}
		}

		/* 'Send all gained skill points at once (if any) */
		if (Pts > 0) {
			Protocol.WriteLevelUp(UserIndex, Pts);

			Declaraciones.UserList[UserIndex].Stats.SkillPts = Declaraciones.UserList[UserIndex].Stats.SkillPts + Pts;

			Protocol.WriteConsoleMsg(UserIndex, "Has ganado un total de " + Pts + " skillpoints.",
					FontTypeNames.FONTTYPE_INFO);
		}

		Protocol.WriteUpdateUserStats(UserIndex);
		return;

		/* FIXME: ErrHandler : */
		String UserName;
		int UserMap = 0;

		if (UserIndex > 0) {
			UserName = Declaraciones.UserList[UserIndex].Name;
			UserMap = Declaraciones.UserList[UserIndex].Pos.Map;
		}

		General.LogError("Error en la subrutina CheckUserLevel - Error : " + Err.Number + " - Description : "
				+ Err.description + ". User: " + UserName + "(" + UserIndex + "). Map: " + UserMap);

	}

	static boolean PuedeAtravesarAgua(int UserIndex) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		retval = Declaraciones.UserList[UserIndex].flags.Navegando == 1
				|| Declaraciones.UserList[UserIndex].flags.Vuela == 1;
		return retval;
	}

	static void MoveUserChar(int UserIndex, eHeading nHeading) {
		/* '************************************************* */
		/* 'Author: Unknown */
		/* 'Last modified: 13/07/2009 */
		/* 'Moves the char, sending the message to everyone in range. */
		/*
		 * '30/03/2009: ZaMa - Now it's legal to move where a casper is,
		 * changing its pos to where the moving char was.
		 */
		/*
		 * '28/05/2009: ZaMa - When you are moved out of an Arena, the
		 * resurrection safe is activated.
		 */
		/*
		 * '13/07/2009: ZaMa - Now all the clients don't know when an invisible
		 * admin moves, they force the admin to move.
		 */
		/*
		 * '13/07/2009: ZaMa - Invisible admins aren't allowed to force dead
		 * characater to move
		 */
		/* '************************************************* */
		Declaraciones.WorldPos nPos;
		boolean sailing = false;
		int CasperIndex = 0;
		eHeading CasperHeading;
		boolean isAdminInvi = false;

		sailing = PuedeAtravesarAgua(UserIndex);
		nPos = Declaraciones.UserList[UserIndex].Pos;
		Extra.HeadtoPos(nHeading, nPos);

		isAdminInvi = (Declaraciones.UserList[UserIndex].flags.AdminInvisible == 1);

		if (Extra.MoveToLegalPos(Declaraciones.UserList[UserIndex].Pos.Map, nPos.X, nPos.Y, sailing, !sailing)) {
			/* 'si no estoy solo en el mapa... */
			if (Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].NumUsers > 1) {

				CasperIndex = Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][nPos.X][nPos.Y].UserIndex;
				/*
				 * 'Si hay un usuario, y paso la validacion, entonces es un
				 * casper
				 */
				if (CasperIndex > 0) {
					/* ' Los admins invisibles no pueden patear caspers */
					if (!isAdminInvi) {

						if (SistemaCombate.TriggerZonaPelea(UserIndex, CasperIndex) == TRIGGER6_PROHIBE) {
							if (Declaraciones.UserList[CasperIndex].flags.SeguroResu == false) {
								Declaraciones.UserList[CasperIndex].flags.SeguroResu = true;
								Protocol.WriteMultiMessage(CasperIndex, eMessages.ResuscitationSafeOn);
							}
						}

						CasperHeading = InvertHeading(nHeading);
						Extra.HeadtoPos(CasperHeading, Declaraciones.UserList[CasperIndex].Pos);

						/*
						 * ' Si es un admin invisible, no se avisa a los demas
						 * clientes
						 */
						if (!Declaraciones.UserList[CasperIndex].flags.AdminInvisible == 1) {
							modSendData.SendData(SendTarget.ToPCAreaButIndex, CasperIndex,
									Protocol.PrepareMessageCharacterMove(
											Declaraciones.UserList[CasperIndex].Char.CharIndex,
											Declaraciones.UserList[CasperIndex].Pos.X,
											Declaraciones.UserList[CasperIndex].Pos.Y));
						}

						Protocol.WriteForceCharMove(CasperIndex, CasperHeading);

						/* 'Update map and char */
						Declaraciones.UserList[CasperIndex].Char.heading = CasperHeading;
						Declaraciones.MapData[Declaraciones.UserList[CasperIndex].Pos.Map][Declaraciones.UserList[CasperIndex].Pos.X][Declaraciones.UserList[CasperIndex].Pos.Y].UserIndex = CasperIndex;

						/* 'Actualizamos las áreas de ser necesario */
						ModAreas.CheckUpdateNeededUser(CasperIndex, CasperHeading);
					}
				}

				/*
				 * ' Si es un admin invisible, no se avisa a los demas clientes
				 */
				if (!isAdminInvi) {
					modSendData.SendData(SendTarget.ToPCAreaButIndex, UserIndex, Protocol.PrepareMessageCharacterMove(
							Declaraciones.UserList[UserIndex].Char.CharIndex, nPos.X, nPos.Y));
				}

			}

			/* ' Los admins invisibles no pueden patear caspers */
			if (!(isAdminInvi && (CasperIndex != 0))) {
				int oldUserIndex = 0;

				oldUserIndex = Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].UserIndex;

				/* ' Si no hay intercambio de pos con nadie */
				if (oldUserIndex == UserIndex) {
					Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].UserIndex = 0;
				}

				Declaraciones.UserList[UserIndex].Pos = nPos;
				Declaraciones.UserList[UserIndex].Char.heading = nHeading;
				Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].UserIndex = UserIndex;

				Extra.DoTileEvents(UserIndex, Declaraciones.UserList[UserIndex].Pos.Map,
						Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y);

				/* 'Actualizamos las áreas de ser necesario */
				ModAreas.CheckUpdateNeededUser(UserIndex, nHeading);
			} else {
				Protocol.WritePosUpdate(UserIndex);
			}
		} else {
			Protocol.WritePosUpdate(UserIndex);
		}

		if (Declaraciones.UserList[UserIndex].Counters.Trabajando) {
			Declaraciones.UserList[UserIndex].Counters.Trabajando = Declaraciones.UserList[UserIndex].Counters.Trabajando
					- 1;
		}

		if (Declaraciones.UserList[UserIndex].Counters.Ocultando) {
			Declaraciones.UserList[UserIndex].Counters.Ocultando = Declaraciones.UserList[UserIndex].Counters.Ocultando
					- 1;
		}
	}

	static eHeading InvertHeading(eHeading nHeading) {
		eHeading retval;
		/* '************************************************* */
		/* 'Author: ZaMa */
		/* 'Last modified: 30/03/2009 */
		/* 'Returns the heading opposite to the one passed by val. */
		/* '************************************************* */
		switch (nHeading) {
		case EAST:
			retval = WEST;
			break;

		case WEST:
			retval = EAST;
			break;

		case SOUTH:
			retval = NORTH;
			break;

		case NORTH:
			retval = SOUTH;
			break;
		}
		return retval;
	}

	static void ChangeUserInv(int UserIndex, int Slot,
			Declaraciones.UserOBJ /* FIXME BYREF!! */ Object) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		Declaraciones.UserList[UserIndex].Invent.Object[Slot] = Object;
		Protocol.WriteChangeInventorySlot(UserIndex, Slot);
	}

	static int NextOpenCharIndex() {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int LoopC = 0;

		for (LoopC = (1); LoopC <= (Declaraciones.MAXCHARS); LoopC++) {
			if (Declaraciones.CharList[LoopC] == 0) {
				retval = LoopC;
				Declaraciones.NumChars = Declaraciones.NumChars + 1;

				if (LoopC > Declaraciones.LastChar) {
					Declaraciones.LastChar = LoopC;
				}

				return retval;
			}
		}
		return retval;
	}

	static int NextOpenUser() {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int LoopC = 0;

		for (LoopC = (1); LoopC <= (Declaraciones.MaxUsers + 1); LoopC++) {
			if (LoopC > Declaraciones.MaxUsers) {
				break; /* FIXME: EXIT FOR */
			}
			if ((Declaraciones.UserList[LoopC].ConnID == -1
					&& Declaraciones.UserList[LoopC].flags.UserLogged == false)) {
				break; /* FIXME: EXIT FOR */
			}
		}

		retval = LoopC;
		return retval;
	}

	static void SendUserStatsTxt(int sendIndex, int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int GuildI = 0;

		Protocol.WriteConsoleMsg(sendIndex, "Estadísticas de: " + Declaraciones.UserList[UserIndex].Name,
				FontTypeNames.FONTTYPE_INFO);
		Protocol.WriteConsoleMsg(sendIndex,
				"Nivel: " + Declaraciones.UserList[UserIndex].Stats.ELV + "  EXP: "
						+ Declaraciones.UserList[UserIndex].Stats.Exp + "/"
						+ Declaraciones.UserList[UserIndex].Stats.ELU,
				FontTypeNames.FONTTYPE_INFO);
		Protocol.WriteConsoleMsg(sendIndex, "Salud: " + Declaraciones.UserList[UserIndex].Stats.MinHp + "/"
				+ Declaraciones.UserList[UserIndex].Stats.MaxHp + "  Maná: "
				+ Declaraciones.UserList[UserIndex].Stats.MinMAN + "/" + Declaraciones.UserList[UserIndex].Stats.MaxMAN
				+ "  Energía: " + Declaraciones.UserList[UserIndex].Stats.MinSta + "/"
				+ Declaraciones.UserList[UserIndex].Stats.MaxSta, FontTypeNames.FONTTYPE_INFO);

		if (Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex > 0) {
			Protocol.WriteConsoleMsg(sendIndex,
					"Menor Golpe/Mayor Golpe: " + Declaraciones.UserList[UserIndex].Stats.MinHIT + "/"
							+ Declaraciones.UserList[UserIndex].Stats.MaxHIT + " ("
							+ Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex].MinHIT
							+ "/"
							+ Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex].MaxHIT
							+ ")",
					FontTypeNames.FONTTYPE_INFO);
		} else {
			Protocol.WriteConsoleMsg(sendIndex,
					"Menor Golpe/Mayor Golpe: " + Declaraciones.UserList[UserIndex].Stats.MinHIT + "/"
							+ Declaraciones.UserList[UserIndex].Stats.MaxHIT,
					FontTypeNames.FONTTYPE_INFO);
		}

		if (Declaraciones.UserList[UserIndex].Invent.ArmourEqpObjIndex > 0) {
			if (Declaraciones.UserList[UserIndex].Invent.EscudoEqpObjIndex > 0) {
				Protocol.WriteConsoleMsg(sendIndex, "(CUERPO) Mín Def/Máx Def: "
						+ Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.ArmourEqpObjIndex].MinDef
						+ Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.EscudoEqpObjIndex].MinDef + "/"
						+ Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.ArmourEqpObjIndex].MaxDef
						+ Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.EscudoEqpObjIndex].MaxDef,
						FontTypeNames.FONTTYPE_INFO);
			} else {
				Protocol.WriteConsoleMsg(sendIndex, "(CUERPO) Mín Def/Máx Def: "
						+ Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.ArmourEqpObjIndex].MinDef + "/"
						+ Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.ArmourEqpObjIndex].MaxDef,
						FontTypeNames.FONTTYPE_INFO);
			}
		} else {
			Protocol.WriteConsoleMsg(sendIndex, "(CUERPO) Mín Def/Máx Def: 0", FontTypeNames.FONTTYPE_INFO);
		}

		if (Declaraciones.UserList[UserIndex].Invent.CascoEqpObjIndex > 0) {
			Protocol.WriteConsoleMsg(sendIndex, "(CABEZA) Mín Def/Máx Def: "
					+ Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.CascoEqpObjIndex].MinDef + "/"
					+ Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.CascoEqpObjIndex].MaxDef,
					FontTypeNames.FONTTYPE_INFO);
		} else {
			Protocol.WriteConsoleMsg(sendIndex, "(CABEZA) Mín Def/Máx Def: 0", FontTypeNames.FONTTYPE_INFO);
		}

		GuildI = Declaraciones.UserList[UserIndex].GuildIndex;
		if (GuildI > 0) {
			Protocol.WriteConsoleMsg(sendIndex, "Clan: " + modGuilds.GuildName(GuildI), FontTypeNames.FONTTYPE_INFO);
			if (vb6.UCase(modGuilds.GuildLeader(GuildI)) == vb6.UCase(Declaraciones.UserList[UserIndex].Name)) {
				Protocol.WriteConsoleMsg(sendIndex, "Status: Líder", FontTypeNames.FONTTYPE_INFO);
			}
			/* 'guildpts no tienen objeto */
		}

		/* # IF ConUpTime THEN */
		vb6.Date TempDate;
		int TempSecs = 0;
		String TempStr;
		TempDate = vb6.Now() - Declaraciones.UserList[UserIndex].LogOnTime;
		TempSecs = (Declaraciones.UserList[UserIndex].UpTime + (vb6.Abs(vb6.Day(TempDate) - 30) * 24 * 3600)
				+ (vb6.Hour(TempDate) * 3600) + (vb6.Minute(TempDate) * 60) + vb6.Second(TempDate));
		TempStr = (TempSecs / 86400) + " Dias, " + ((TempSecs % 86400) / 3600) + " Horas, "
				+ ((TempSecs % 86400) % 3600) / 60 + " Minutos, " + (((TempSecs % 86400) % 3600) % 60) + " Segundos.";
		Protocol.WriteConsoleMsg(sendIndex,
				"Logeado hace: " + vb6.Hour(TempDate) + ":" + vb6.Minute(TempDate) + ":" + vb6.Second(TempDate),
				FontTypeNames.FONTTYPE_INFO);
		Protocol.WriteConsoleMsg(sendIndex, "Total: " + TempStr, FontTypeNames.FONTTYPE_INFO);
		/* # END IF */
		if (Declaraciones.UserList[UserIndex].flags.Traveling == 1) {
			Protocol.WriteConsoleMsg(sendIndex,
					"Tiempo restante para llegar a tu hogar: " + GetHomeArrivalTime(UserIndex) + " segundos.",
					FontTypeNames.FONTTYPE_INFO);
		}

		Protocol.WriteConsoleMsg(sendIndex,
				"Oro: " + Declaraciones.UserList[UserIndex].Stats.GLD + "  Posición: "
						+ Declaraciones.UserList[UserIndex].Pos.X + "," + Declaraciones.UserList[UserIndex].Pos.Y
						+ " en mapa " + Declaraciones.UserList[UserIndex].Pos.Map,
				FontTypeNames.FONTTYPE_INFO);
		Protocol.WriteConsoleMsg(sendIndex,
				"Dados: " + Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Fuerza] + ", "
						+ Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Agilidad] + ", "
						+ Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Inteligencia] + ", "
						+ Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Carisma] + ", "
						+ Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Constitucion],
				FontTypeNames.FONTTYPE_INFO);
	}

	static void SendUserMiniStatsTxt(int sendIndex, int UserIndex) {
		/* '************************************************* */
		/* 'Author: Unknown */
		/* 'Last modified: 23/01/2007 */
		/* 'Shows the users Stats when the user is online. */
		/*
		 * '23/01/2007 Pablo (ToxicWaste) - Agrego de funciones y mejora de
		 * distribución de parámetros.
		 */
		/* '************************************************* */
		Protocol.WriteConsoleMsg(sendIndex, "Pj: " + Declaraciones.UserList[UserIndex].Name,
				FontTypeNames.FONTTYPE_INFO);
		Protocol.WriteConsoleMsg(sendIndex,
				"Ciudadanos matados: " + Declaraciones.UserList[UserIndex].Faccion.CiudadanosMatados
						+ " Criminales matados: " + Declaraciones.UserList[UserIndex].Faccion.CriminalesMatados
						+ " usuarios matados: " + Declaraciones.UserList[UserIndex].Stats.UsuariosMatados,
				FontTypeNames.FONTTYPE_INFO);
		Protocol.WriteConsoleMsg(sendIndex, "NPCs muertos: " + Declaraciones.UserList[UserIndex].Stats.NPCsMuertos,
				FontTypeNames.FONTTYPE_INFO);
		Protocol.WriteConsoleMsg(sendIndex,
				"Clase: " + Declaraciones.ListaClases[Declaraciones.UserList[UserIndex].clase],
				FontTypeNames.FONTTYPE_INFO);
		Protocol.WriteConsoleMsg(sendIndex, "Pena: " + Declaraciones.UserList[UserIndex].Counters.Pena,
				FontTypeNames.FONTTYPE_INFO);

		if (Declaraciones.UserList[UserIndex].Faccion.ArmadaReal == 1) {
			Protocol.WriteConsoleMsg(sendIndex,
					"Ejército real desde: " + Declaraciones.UserList[UserIndex].Faccion.FechaIngreso,
					FontTypeNames.FONTTYPE_INFO);
			Protocol.WriteConsoleMsg(sendIndex,
					"Ingresó en nivel: " + Declaraciones.UserList[UserIndex].Faccion.NivelIngreso + " con "
							+ Declaraciones.UserList[UserIndex].Faccion.MatadosIngreso + " ciudadanos matados.",
					FontTypeNames.FONTTYPE_INFO);
			Protocol.WriteConsoleMsg(sendIndex,
					"Veces que ingresó: " + Declaraciones.UserList[UserIndex].Faccion.Reenlistadas,
					FontTypeNames.FONTTYPE_INFO);

		} else if (Declaraciones.UserList[UserIndex].Faccion.FuerzasCaos == 1) {
			Protocol.WriteConsoleMsg(sendIndex,
					"Legión oscura desde: " + Declaraciones.UserList[UserIndex].Faccion.FechaIngreso,
					FontTypeNames.FONTTYPE_INFO);
			Protocol.WriteConsoleMsg(sendIndex,
					"Ingresó en nivel: " + Declaraciones.UserList[UserIndex].Faccion.NivelIngreso,
					FontTypeNames.FONTTYPE_INFO);
			Protocol.WriteConsoleMsg(sendIndex,
					"Veces que ingresó: " + Declaraciones.UserList[UserIndex].Faccion.Reenlistadas,
					FontTypeNames.FONTTYPE_INFO);

		} else if (Declaraciones.UserList[UserIndex].Faccion.RecibioExpInicialReal == 1) {
			Protocol.WriteConsoleMsg(sendIndex, "Fue ejército real", FontTypeNames.FONTTYPE_INFO);
			Protocol.WriteConsoleMsg(sendIndex,
					"Veces que ingresó: " + Declaraciones.UserList[UserIndex].Faccion.Reenlistadas,
					FontTypeNames.FONTTYPE_INFO);

		} else if (Declaraciones.UserList[UserIndex].Faccion.RecibioExpInicialCaos == 1) {
			Protocol.WriteConsoleMsg(sendIndex, "Fue legión oscura", FontTypeNames.FONTTYPE_INFO);
			Protocol.WriteConsoleMsg(sendIndex,
					"Veces que ingresó: " + Declaraciones.UserList[UserIndex].Faccion.Reenlistadas,
					FontTypeNames.FONTTYPE_INFO);
		}

		Protocol.WriteConsoleMsg(sendIndex, "Asesino: " + Declaraciones.UserList[UserIndex].Reputacion.AsesinoRep,
				FontTypeNames.FONTTYPE_INFO);
		Protocol.WriteConsoleMsg(sendIndex, "Noble: " + Declaraciones.UserList[UserIndex].Reputacion.NobleRep,
				FontTypeNames.FONTTYPE_INFO);
		Protocol.WriteConsoleMsg(sendIndex, "Bandido: " + Declaraciones.UserList[UserIndex].Reputacion.BandidoRep,
				FontTypeNames.FONTTYPE_INFO);

		if (Declaraciones.UserList[UserIndex].GuildIndex > 0) {
			Protocol.WriteConsoleMsg(sendIndex,
					"Clan: " + modGuilds.GuildName(Declaraciones.UserList[UserIndex].GuildIndex),
					FontTypeNames.FONTTYPE_INFO);
		}
	}

	static void SendUserMiniStatsTxtFromChar(int sendIndex, String charName) {
		/* '************************************************* */
		/* 'Author: Unknown */
		/* 'Last modified: 23/01/2007 */
		/* 'Shows the users Stats when the user is offline. */
		/*
		 * '23/01/2007 Pablo (ToxicWaste) - Agrego de funciones y mejora de
		 * distribución de parámetros.
		 */
		/* '************************************************* */
		String CharFile;
		String Ban;
		String BanDetailPath;

		BanDetailPath = vb6.App.Instance().Path + "\\logs\\" + "BanDetail.dat";
		CharFile = Declaraciones.CharPath + charName + ".chr";

		if (General.FileExist(CharFile)) {
			Protocol.WriteConsoleMsg(sendIndex, "Pj: " + charName, FontTypeNames.FONTTYPE_INFO);
			Protocol.WriteConsoleMsg(sendIndex,
					"Ciudadanos matados: " + ES.GetVar(CharFile, "FACCIONES", "CiudMatados") + " CriminalesMatados: "
							+ ES.GetVar(CharFile, "FACCIONES", "CrimMatados") + " usuarios matados: "
							+ ES.GetVar(CharFile, "MUERTES", "UserMuertes"),
					FontTypeNames.FONTTYPE_INFO);
			Protocol.WriteConsoleMsg(sendIndex, "NPCs muertos: " + ES.GetVar(CharFile, "MUERTES", "NpcsMuertes"),
					FontTypeNames.FONTTYPE_INFO);
			Protocol.WriteConsoleMsg(sendIndex,
					"Clase: " + Declaraciones.ListaClases[ES.GetVar(CharFile, "INIT", "Clase")],
					FontTypeNames.FONTTYPE_INFO);
			Protocol.WriteConsoleMsg(sendIndex, "Pena: " + ES.GetVar(CharFile, "COUNTERS", "PENA"),
					FontTypeNames.FONTTYPE_INFO);

			if (vb6.CByte(ES.GetVar(CharFile, "FACCIONES", "EjercitoReal")) == 1) {
				Protocol.WriteConsoleMsg(sendIndex,
						"Ejército real desde: " + ES.GetVar(CharFile, "FACCIONES", "FechaIngreso"),
						FontTypeNames.FONTTYPE_INFO);
				Protocol.WriteConsoleMsg(sendIndex,
						"Ingresó en nivel: " + vb6.CInt(ES.GetVar(CharFile, "FACCIONES", "NivelIngreso")) + " con "
								+ vb6.CInt(ES.GetVar(CharFile, "FACCIONES", "MatadosIngreso")) + " ciudadanos matados.",
						FontTypeNames.FONTTYPE_INFO);
				Protocol.WriteConsoleMsg(sendIndex,
						"Veces que ingresó: " + vb6.CByte(ES.GetVar(CharFile, "FACCIONES", "Reenlistadas")),
						FontTypeNames.FONTTYPE_INFO);

			} else if (vb6.CByte(ES.GetVar(CharFile, "FACCIONES", "EjercitoCaos")) == 1) {
				Protocol.WriteConsoleMsg(sendIndex,
						"Legión oscura desde: " + ES.GetVar(CharFile, "FACCIONES", "FechaIngreso"),
						FontTypeNames.FONTTYPE_INFO);
				Protocol.WriteConsoleMsg(sendIndex,
						"Ingresó en nivel: " + vb6.CInt(ES.GetVar(CharFile, "FACCIONES", "NivelIngreso")),
						FontTypeNames.FONTTYPE_INFO);
				Protocol.WriteConsoleMsg(sendIndex,
						"Veces que ingresó: " + vb6.CByte(ES.GetVar(CharFile, "FACCIONES", "Reenlistadas")),
						FontTypeNames.FONTTYPE_INFO);

			} else if (vb6.CByte(ES.GetVar(CharFile, "FACCIONES", "rExReal")) == 1) {
				Protocol.WriteConsoleMsg(sendIndex, "Fue ejército real", FontTypeNames.FONTTYPE_INFO);
				Protocol.WriteConsoleMsg(sendIndex,
						"Veces que ingresó: " + vb6.CByte(ES.GetVar(CharFile, "FACCIONES", "Reenlistadas")),
						FontTypeNames.FONTTYPE_INFO);

			} else if (vb6.CByte(ES.GetVar(CharFile, "FACCIONES", "rExCaos")) == 1) {
				Protocol.WriteConsoleMsg(sendIndex, "Fue legión oscura", FontTypeNames.FONTTYPE_INFO);
				Protocol.WriteConsoleMsg(sendIndex,
						"Veces que ingresó: " + vb6.CByte(ES.GetVar(CharFile, "FACCIONES", "Reenlistadas")),
						FontTypeNames.FONTTYPE_INFO);
			}

			Protocol.WriteConsoleMsg(sendIndex, "Asesino: " + vb6.CLng(ES.GetVar(CharFile, "REP", "Asesino")),
					FontTypeNames.FONTTYPE_INFO);
			Protocol.WriteConsoleMsg(sendIndex, "Noble: " + vb6.CLng(ES.GetVar(CharFile, "REP", "Nobles")),
					FontTypeNames.FONTTYPE_INFO);
			Protocol.WriteConsoleMsg(sendIndex, "Bandido: " + vb6.CLng(ES.GetVar(CharFile, "REP", "BANDIDO")),
					FontTypeNames.FONTTYPE_INFO);

			if (vb6.IsNumeric(ES.GetVar(CharFile, "Guild", "GUILDINDEX"))) {
				Protocol.WriteConsoleMsg(sendIndex,
						"Clan: " + modGuilds.GuildName(vb6.CInt(ES.GetVar(CharFile, "Guild", "GUILDINDEX"))),
						FontTypeNames.FONTTYPE_INFO);
			}

			Ban = ES.GetVar(CharFile, "FLAGS", "Ban");
			Protocol.WriteConsoleMsg(sendIndex, "Ban: " + Ban, FontTypeNames.FONTTYPE_INFO);

			if (Ban == "1") {
				Protocol.WriteConsoleMsg(sendIndex, "Ban por: " + ES.GetVar(CharFile, charName, "BannedBy")
						+ " Motivo: " + ES.GetVar(BanDetailPath, charName, "Reason"), FontTypeNames.FONTTYPE_INFO);
			}
		} else {
			Protocol.WriteConsoleMsg(sendIndex, "El pj no existe: " + charName, FontTypeNames.FONTTYPE_INFO);
		}
	}

	static void SendUserInvTxt(int sendIndex, int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR RESUME NEXT */

		int j = 0;

		Protocol.WriteConsoleMsg(sendIndex, Declaraciones.UserList[UserIndex].Name, FontTypeNames.FONTTYPE_INFO);
		Protocol.WriteConsoleMsg(sendIndex, "Tiene " + Declaraciones.UserList[UserIndex].Invent.NroItems + " objetos.",
				FontTypeNames.FONTTYPE_INFO);

		for (j = (1); j <= (Declaraciones.UserList[UserIndex].CurrentInventorySlots); j++) {
			if (Declaraciones.UserList[UserIndex].Invent.Object[j].ObjIndex > 0) {
				Protocol.WriteConsoleMsg(sendIndex,
						"Objeto " + j + " "
								+ Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.Object[j].ObjIndex].Name
								+ " Cantidad:" + Declaraciones.UserList[UserIndex].Invent.Object[j].Amount,
						FontTypeNames.FONTTYPE_INFO);
			}
		}
	}

	static void SendUserInvTxtFromChar(int sendIndex, String charName) {
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
					"Tiene " + ES.GetVar(CharFile, "Inventory", "CantidadItems") + " objetos.",
					FontTypeNames.FONTTYPE_INFO);

			for (j = (1); j <= (Declaraciones.MAX_INVENTORY_SLOTS); j++) {
				Tmp = ES.GetVar(CharFile, "Inventory", "Obj" + j);
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

	static void SendUserSkillsTxt(int sendIndex, int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR RESUME NEXT */
		int j = 0;

		Protocol.WriteConsoleMsg(sendIndex, Declaraciones.UserList[UserIndex].Name, FontTypeNames.FONTTYPE_INFO);

		for (j = (1); j <= (Declaraciones.NUMSKILLS); j++) {
			Protocol.WriteConsoleMsg(sendIndex,
					Declaraciones.SkillsNames[j] + " = " + Declaraciones.UserList[UserIndex].Stats.UserSkills[j],
					FontTypeNames.FONTTYPE_INFO);
		}

		Protocol.WriteConsoleMsg(sendIndex, "SkillLibres:" + Declaraciones.UserList[UserIndex].Stats.SkillPts,
				FontTypeNames.FONTTYPE_INFO);
	}

	static boolean EsMascotaCiudadano(int NpcIndex, int UserIndex) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		if (Declaraciones.Npclist[NpcIndex].MaestroUser > 0) {
			retval = !ES.criminal(Declaraciones.Npclist[NpcIndex].MaestroUser);
			if (retval) {
				Protocol.WriteConsoleMsg(Declaraciones.Npclist[NpcIndex].MaestroUser,
						"¡¡" + Declaraciones.UserList[UserIndex].Name + " esta atacando tu mascota!!",
						FontTypeNames.FONTTYPE_INFO);
			}
		}
		return retval;
	}

	static void NPCAtacado(int NpcIndex, int UserIndex) {
		/* '********************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 02/04/2010 */
		/*
		 * '24/01/2007 -> Pablo (ToxicWaste): Agrego para que se actualize el
		 * tag si corresponde.
		 */
		/*
		 * '24/07/2007 -> Pablo (ToxicWaste): Guardar primero que ataca NPC y el
		 * que atacas ahora.
		 */
		/*
		 * '06/28/2008 -> NicoNZ: Los elementales al atacarlos por su amo no se
		 * paran más al lado de él sin hacer nada.
		 */
		/*
		 * '02/04/2010: ZaMa: Un ciuda no se vuelve mas criminal al atacar un
		 * npc no hostil.
		 */
		/* '********************************************** */
		boolean EraCriminal = false;

		/* 'Guardamos el usuario que ataco el npc. */
		Declaraciones.Npclist[NpcIndex].flags.AttackedBy = Declaraciones.UserList[UserIndex].Name;

		/* 'Npc que estabas atacando. */
		int LastNpcHit = 0;
		LastNpcHit = Declaraciones.UserList[UserIndex].flags.retval;
		/* 'Guarda el NPC que estas atacando ahora. */
		Declaraciones.UserList[UserIndex].flags.retval = NpcIndex;

		/* 'Revisamos robo de npc. */
		/* 'Guarda el primer nick que lo ataca. */
		if (Declaraciones.Npclist[NpcIndex].flags.AttackedFirstBy == "") {
			/* 'El que le pegabas antes ya no es tuyo */
			if (LastNpcHit != 0) {
				if (Declaraciones.Npclist[LastNpcHit].flags.AttackedFirstBy == Declaraciones.UserList[UserIndex].Name) {
					Declaraciones.Npclist[LastNpcHit].flags.AttackedFirstBy = "";
				}
			}
			Declaraciones.Npclist[NpcIndex].flags.AttackedFirstBy = Declaraciones.UserList[UserIndex].Name;
		} else if (Declaraciones.Npclist[NpcIndex].flags.AttackedFirstBy != Declaraciones.UserList[UserIndex].Name) {
			/* 'Estas robando NPC */
			/* 'El que le pegabas antes ya no es tuyo */
			if (LastNpcHit != 0) {
				if (Declaraciones.Npclist[LastNpcHit].flags.AttackedFirstBy == Declaraciones.UserList[UserIndex].Name) {
					Declaraciones.Npclist[LastNpcHit].flags.AttackedFirstBy = "";
				}
			}
		}

		if (Declaraciones.Npclist[NpcIndex].MaestroUser > 0) {
			if (Declaraciones.Npclist[NpcIndex].MaestroUser != UserIndex) {
				SistemaCombate.AllMascotasAtacanUser(UserIndex, Declaraciones.Npclist[NpcIndex].MaestroUser);
			}
		}

		if (EsMascotaCiudadano(NpcIndex, UserIndex)) {
			VolverCriminal(UserIndex);
			Declaraciones.Npclist[NpcIndex].Movement = TipoAI.NPCDEFENSA;
			Declaraciones.Npclist[NpcIndex].Hostile = 1;
		} else {
			EraCriminal = ES.criminal(UserIndex);

			/* 'Reputacion */
			if (Declaraciones.Npclist[NpcIndex].Stats.Alineacion == 0) {
				if (Declaraciones.Npclist[NpcIndex].NPCtype == eNPCType.GuardiaReal) {
					VolverCriminal(UserIndex);
				}

			} else if (Declaraciones.Npclist[NpcIndex].Stats.Alineacion == 1) {
				Declaraciones.UserList[UserIndex].Reputacion.PlebeRep = Declaraciones.UserList[UserIndex].Reputacion.PlebeRep
						+ Declaraciones.vlCAZADOR / (double) 2;
				if (Declaraciones.UserList[UserIndex].Reputacion.PlebeRep > Declaraciones.MAXREP) {
					Declaraciones.UserList[UserIndex].Reputacion.PlebeRep = Declaraciones.MAXREP;
				}
			}

			if (Declaraciones.Npclist[NpcIndex].MaestroUser != UserIndex) {
				/* 'hacemos que el npc se defienda */
				Declaraciones.Npclist[NpcIndex].Movement = TipoAI.NPCDEFENSA;
				Declaraciones.Npclist[NpcIndex].Hostile = 1;
			}

			if (EraCriminal && !ES.criminal(UserIndex)) {
				VolverCiudadano(UserIndex);
			}
		}
	}

	static boolean PuedeApunalar(int UserIndex) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int WeaponIndex = 0;

		WeaponIndex = Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex;

		if (WeaponIndex > 0) {
			if (Declaraciones.ObjData[WeaponIndex].Apunala == 1) {
				retval = Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Apunalar] >= Declaraciones.MIN_APUNALAR
						|| Declaraciones.UserList[UserIndex].clase == eClass.Assasin;
			}
		}

		return retval;
	}

	static boolean PuedeAcuchillar(int UserIndex) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 25/01/2010 (ZaMa) */
		/* ' */
		/* '*************************************************** */

		int WeaponIndex = 0;

		if (Declaraciones.UserList[UserIndex].clase == eClass.Pirat) {

			WeaponIndex = Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex;
			if (WeaponIndex > 0) {
				retval = (Declaraciones.ObjData[WeaponIndex].Acuchilla == 1);
			}
		}

		return retval;
	}

	static void SubirSkill(int UserIndex, eSkill Skill, boolean Acerto) {
		/* '************************************************* */
		/* 'Author: Unknown */
		/* 'Last modified: 11/19/2009 */
		/* '11/19/2009 Pato - Implement the new system to train the skills. */
		/* '************************************************* */
		if (Declaraciones.UserList[UserIndex].flags.Hambre == 0 && Declaraciones.UserList[UserIndex].flags.Sed == 0) {
			if (Declaraciones.UserList[UserIndex].Counters.AsignedSkills < 10) {
				if (Declaraciones.UserList[UserIndex].flags.UltimoMensaje != 7) {
					Protocol.WriteConsoleMsg(UserIndex,
							"Para poder entrenar un skill debes asignar los 10 skills iniciales.",
							FontTypeNames.FONTTYPE_INFO);
					Declaraciones.UserList[UserIndex].flags.UltimoMensaje = 7;
				}

				return;
			}

			if (Declaraciones.UserList[UserIndex].Stats.UserSkills[Skill.toInteger()] == Declaraciones.MAXSKILLPOINTS) {
				return;
			}

			int Lvl = 0;
			Lvl = Declaraciones.UserList[UserIndex].Stats.ELV;

			if (Lvl >= Declaraciones.LevelSkill.length) {
				Lvl = Declaraciones.LevelSkill.length - 1;
			}

			if (Declaraciones.UserList[UserIndex].Stats.UserSkills[Skill
					.toInteger()] >= Declaraciones.LevelSkill[Lvl].LevelValue) {
				return;
			}

			if (Acerto) {
				Declaraciones.UserList[UserIndex].Stats.ExpSkills[Skill.toInteger()] += Declaraciones.EXP_ACIERTO_SKILL;
			} else {
				Declaraciones.UserList[UserIndex].Stats.ExpSkills[Skill.toInteger()] += Declaraciones.EXP_FALLO_SKILL;
			}

			if (Declaraciones.UserList[UserIndex].Stats.ExpSkills[Skill
					.toInteger()] >= Declaraciones.UserList[UserIndex].Stats.EluSkills[Skill.toInteger()]) {
				Declaraciones.UserList[UserIndex].Stats.UserSkills[Skill
						.toInteger()] = Declaraciones.UserList[UserIndex].Stats.UserSkills[Skill.toInteger()] + 1;
				Protocol.WriteConsoleMsg(UserIndex,
						"¡Has mejorado tu skill " + Declaraciones.SkillsNames[Skill.toInteger()]
								+ " en un punto! Ahora tienes "
								+ Declaraciones.UserList[UserIndex].Stats.UserSkills[Skill.toInteger()] + " pts.",
						FontTypeNames.FONTTYPE_INFO);

				Declaraciones.UserList[UserIndex].Stats.Exp = Declaraciones.UserList[UserIndex].Stats.Exp + 50;
				if (Declaraciones.UserList[UserIndex].Stats.Exp > Declaraciones.MAXEXP) {
					Declaraciones.UserList[UserIndex].Stats.Exp = Declaraciones.MAXEXP;
				}

				Protocol.WriteConsoleMsg(UserIndex, "¡Has ganado 50 puntos de experiencia!",
						FontTypeNames.FONTTYPE_FIGHT);

				Protocol.WriteUpdateExp(UserIndex);
				CheckUserLevel(UserIndex);
				CheckEluSkill(UserIndex, Skill, false);
			}
		}
	}

	/* '' */
	/* ' Muere un usuario */
	/* ' */
	/* ' @param UserIndex Indice del usuario que muere */
	/* ' */

	static void UserDie(int UserIndex) {
		/* '************************************************ */
		/* 'Author: Uknown */
		/* 'Last Modified: 12/01/2010 (ZaMa) */
		/* '04/15/2008: NicoNZ - Ahora se resetea el counter del invi */
		/*
		 * '13/02/2009: ZaMa - Ahora se borran las mascotas cuando moris en
		 * agua.
		 */
		/*
		 * '27/05/2009: ZaMa - El seguro de resu no se activa si estas en una
		 * arena.
		 */
		/* '21/07/2009: Marco - Al morir se desactiva el comercio seguro. */
		/* '16/11/2009: ZaMa - Al morir perdes la criatura que te pertenecia. */
		/* '27/11/2009: Budi - Al morir envia los atributos originales. */
		/*
		 * '12/01/2010: ZaMa - Los druidas pierden la inmunidad de ser atacados
		 * cuando mueren.
		 */
		/* '************************************************ */
		/* FIXME: ON ERROR GOTO ErrorHandler */
		int i = 0;
		int aN = 0;

		int iSoundDeath = 0;

		/* 'Sonido */
		if (Declaraciones.UserList[UserIndex].Genero == eGenero.Mujer) {
			if (General.HayAgua(Declaraciones.UserList[UserIndex].Pos.Map, Declaraciones.UserList[UserIndex].Pos.X,
					Declaraciones.UserList[UserIndex].Pos.Y)) {
				iSoundDeath = e_SoundIndex.MUERTE_MUJER_AGUA;
			} else {
				iSoundDeath = e_SoundIndex.MUERTE_MUJER;
			}
		} else {
			if (General.HayAgua(Declaraciones.UserList[UserIndex].Pos.Map, Declaraciones.UserList[UserIndex].Pos.X,
					Declaraciones.UserList[UserIndex].Pos.Y)) {
				iSoundDeath = e_SoundIndex.MUERTE_HOMBRE_AGUA;
			} else {
				iSoundDeath = e_SoundIndex.MUERTE_HOMBRE;
			}
		}

		Declaraciones.SonidosMapas.ReproducirSonido(SendTarget.ToPCArea, UserIndex, iSoundDeath);

		/* 'Quitar el dialogo del user muerto */
		modSendData.SendData(SendTarget.ToPCArea, UserIndex,
				Protocol.PrepareMessageRemoveCharDialog(Declaraciones.UserList[UserIndex].Char.CharIndex));

		Declaraciones.UserList[UserIndex].Stats.MinHp = 0;
		Declaraciones.UserList[UserIndex].Stats.MinSta = 0;
		Declaraciones.UserList[UserIndex].flags.AtacadoPorUser = 0;
		Declaraciones.UserList[UserIndex].flags.Envenenado = 0;
		Declaraciones.UserList[UserIndex].flags.Muerto = 1;

		Declaraciones.UserList[UserIndex].Counters.Trabajando = 0;

		/* ' No se activa en arenas */
		if (SistemaCombate.TriggerZonaPelea(UserIndex, UserIndex) != TRIGGER6_PERMITE) {
			Declaraciones.UserList[UserIndex].flags.SeguroResu = true;
			/* 'Call WriteResuscitationSafeOn(UserIndex) */
			Protocol.WriteMultiMessage(UserIndex, eMessages.ResuscitationSafeOn);
		} else {
			Declaraciones.UserList[UserIndex].flags.SeguroResu = false;
			/* 'Call WriteResuscitationSafeOff(UserIndex) */
			Protocol.WriteMultiMessage(UserIndex, eMessages.ResuscitationSafeOff);
		}

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

		PerdioNpc(UserIndex, false);

		/* '<<<< Atacable >>>> */
		if (Declaraciones.UserList[UserIndex].flags.AtacablePor > 0) {
			Declaraciones.UserList[UserIndex].flags.AtacablePor = 0;
			RefreshCharStatus(UserIndex);
		}

		/* '<<<< Paralisis >>>> */
		if (Declaraciones.UserList[UserIndex].flags.Paralizado == 1) {
			Declaraciones.UserList[UserIndex].flags.Paralizado = 0;
			Protocol.WriteParalizeOK(UserIndex);
		}

		/* '<<< Estupidez >>> */
		if (Declaraciones.UserList[UserIndex].flags.Estupidez == 1) {
			Declaraciones.UserList[UserIndex].flags.Estupidez = 0;
			Protocol.WriteDumbNoMore(UserIndex);
		}

		/* '<<<< Descansando >>>> */
		if (Declaraciones.UserList[UserIndex].flags.Descansar) {
			Declaraciones.UserList[UserIndex].flags.Descansar = false;
			Protocol.WriteRestOK(UserIndex);
		}

		/* '<<<< Meditando >>>> */
		if (Declaraciones.UserList[UserIndex].flags.Meditando) {
			Declaraciones.UserList[UserIndex].flags.Meditando = false;
			Protocol.WriteMeditateToggle(UserIndex);
		}

		/* '<<<< Invisible >>>> */
		if (Declaraciones.UserList[UserIndex].flags.invisible == 1
				|| Declaraciones.UserList[UserIndex].flags.Oculto == 1) {
			Declaraciones.UserList[UserIndex].flags.Oculto = 0;
			Declaraciones.UserList[UserIndex].flags.invisible = 0;
			Declaraciones.UserList[UserIndex].Counters.TiempoOculto = 0;
			Declaraciones.UserList[UserIndex].Counters.Invisibilidad = 0;

			/*
			 * 'Call SendData(SendTarget.ToPCArea, UserIndex,
			 * PrepareMessageSetInvisible(.Char.CharIndex, False))
			 */
			UsUaRiOs.SetInvisible(UserIndex, Declaraciones.UserList[UserIndex].Char.CharIndex, false);
		}

		if (SistemaCombate.TriggerZonaPelea(UserIndex, UserIndex) != eTrigger6.TRIGGER6_PERMITE) {
			/* ' << Si es newbie no pierde el inventario >> */
			if (!Extra.EsNewbie(UserIndex)) {
				InvUsuario.TirarTodo(UserIndex);
			} else {
				InvUsuario.TirarTodosLosItemsNoNewbies(UserIndex);
			}
		}

		/* ' DESEQUIPA TODOS LOS OBJETOS */
		/* 'desequipar armadura */
		if (Declaraciones.UserList[UserIndex].Invent.ArmourEqpObjIndex > 0) {
			InvUsuario.Desequipar(UserIndex, Declaraciones.UserList[UserIndex].Invent.ArmourEqpSlot, false);
		}

		/* 'desequipar arma */
		if (Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex > 0) {
			InvUsuario.Desequipar(UserIndex, Declaraciones.UserList[UserIndex].Invent.WeaponEqpSlot, false);
		}

		/* 'desequipar casco */
		if (Declaraciones.UserList[UserIndex].Invent.CascoEqpObjIndex > 0) {
			InvUsuario.Desequipar(UserIndex, Declaraciones.UserList[UserIndex].Invent.CascoEqpSlot, false);
		}

		/* 'desequipar herramienta */
		if (Declaraciones.UserList[UserIndex].Invent.AnilloEqpSlot > 0) {
			InvUsuario.Desequipar(UserIndex, Declaraciones.UserList[UserIndex].Invent.AnilloEqpSlot, false);
		}

		/* 'desequipar municiones */
		if (Declaraciones.UserList[UserIndex].Invent.MunicionEqpObjIndex > 0) {
			InvUsuario.Desequipar(UserIndex, Declaraciones.UserList[UserIndex].Invent.MunicionEqpSlot, false);
		}

		/* 'desequipar escudo */
		if (Declaraciones.UserList[UserIndex].Invent.EscudoEqpObjIndex > 0) {
			InvUsuario.Desequipar(UserIndex, Declaraciones.UserList[UserIndex].Invent.EscudoEqpSlot, false);
		}

		/* ' << Reseteamos los posibles FX sobre el personaje >> */
		if (Declaraciones.UserList[UserIndex].Char.loops == Declaraciones.INFINITE_LOOPS) {
			Declaraciones.UserList[UserIndex].Char.FX = 0;
			Declaraciones.UserList[UserIndex].Char.loops = 0;
		}

		/* ' << Restauramos el mimetismo */
		if (Declaraciones.UserList[UserIndex].flags.Mimetizado == 1) {
			Declaraciones.UserList[UserIndex].Char.body = Declaraciones.UserList[UserIndex].CharMimetizado.body;
			Declaraciones.UserList[UserIndex].Char.Head = Declaraciones.UserList[UserIndex].CharMimetizado.Head;
			Declaraciones.UserList[UserIndex].Char.CascoAnim = Declaraciones.UserList[UserIndex].CharMimetizado.CascoAnim;
			Declaraciones.UserList[UserIndex].Char.ShieldAnim = Declaraciones.UserList[UserIndex].CharMimetizado.ShieldAnim;
			Declaraciones.UserList[UserIndex].Char.WeaponAnim = Declaraciones.UserList[UserIndex].CharMimetizado.WeaponAnim;
			Declaraciones.UserList[UserIndex].Counters.Mimetismo = 0;
			Declaraciones.UserList[UserIndex].flags.Mimetizado = 0;
			/* ' Puede ser atacado por npcs (cuando resucite) */
			Declaraciones.UserList[UserIndex].flags.Ignorado = false;
		}

		/* ' << Restauramos los atributos >> */
		if (Declaraciones.UserList[UserIndex].flags.TomoPocion == true) {
			for (i = (1); i <= (5); i++) {
				Declaraciones.UserList[UserIndex].Stats.UserAtributos[i] = Declaraciones.UserList[UserIndex].Stats.UserAtributosBackUP[i];
			}
		}

		/* '<< Cambiamos la apariencia del char >> */
		if (Declaraciones.UserList[UserIndex].flags.Navegando == 0) {
			Declaraciones.UserList[UserIndex].Char.body = Declaraciones.iCuerpoMuerto;
			Declaraciones.UserList[UserIndex].Char.Head = Declaraciones.iCabezaMuerto;
			Declaraciones.UserList[UserIndex].Char.ShieldAnim = Declaraciones.NingunEscudo;
			Declaraciones.UserList[UserIndex].Char.WeaponAnim = Declaraciones.NingunArma;
			Declaraciones.UserList[UserIndex].Char.CascoAnim = Declaraciones.NingunCasco;
		} else {
			Declaraciones.UserList[UserIndex].Char.body = Declaraciones.iFragataFantasmal;
		}

		for (i = (1); i <= (Declaraciones.MAXMASCOTAS); i++) {
			if (Declaraciones.UserList[UserIndex].MascotasIndex[i] > 0) {
				NPCs.MuereNpc(Declaraciones.UserList[UserIndex].MascotasIndex[i], 0);
				/* ' Si estan en agua o zona segura */
			} else {
				Declaraciones.UserList[UserIndex].MascotasType[i] = 0;
			}
		}

		Declaraciones.UserList[UserIndex].NroMascotas = 0;

		/* '<< Actualizamos clientes >> */
		ChangeUserChar(UserIndex, Declaraciones.UserList[UserIndex].Char.body,
				Declaraciones.UserList[UserIndex].Char.Head, Declaraciones.UserList[UserIndex].Char.heading,
				Declaraciones.NingunArma, Declaraciones.NingunEscudo, Declaraciones.NingunCasco);
		Protocol.WriteUpdateUserStats(UserIndex);
		Protocol.WriteUpdateStrenghtAndDexterity(UserIndex);
		/* '<<Castigos por party>> */
		if (Declaraciones.UserList[UserIndex].PartyIndex > 0) {
			mdParty.ObtenerExito(UserIndex,
					Declaraciones.UserList[UserIndex].Stats.ELV * -10 * mdParty.CantMiembros(UserIndex),
					Declaraciones.UserList[UserIndex].Pos.Map, Declaraciones.UserList[UserIndex].Pos.X,
					Declaraciones.UserList[UserIndex].Pos.Y);
		}

		/* '<<Cerramos comercio seguro>> */
		TCP.LimpiarComercioSeguro(UserIndex);

		/* ' Hay que teletransportar? */
		int mapa = 0;
		mapa = Declaraciones.UserList[UserIndex].Pos.Map;
		int MapaTelep = 0;
		MapaTelep = Declaraciones.MapInfo[mapa].OnDeathGoTo.Map;

		if (MapaTelep != 0) {
			Protocol.WriteConsoleMsg(UserIndex, "¡¡¡Tu estado no te permite permanecer en el mapa!!!",
					FontTypeNames.FONTTYPE_INFOBOLD);
			WarpUserChar(UserIndex, MapaTelep, Declaraciones.MapInfo[mapa].OnDeathGoTo.X,
					Declaraciones.MapInfo[mapa].OnDeathGoTo.Y, true, true);
		}

		return;

		/* FIXME: ErrorHandler : */
		General.LogError("Error en SUB USERDIE. Error: " + Err.Number + " Descripción: " + Err.description);
	}

	static void ContarMuerte(int Muerto, int Atacante) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 13/07/2010 */
		/*
		 * '13/07/2010: ZaMa - Los matados en estado atacable ya no suman frag.
		 */
		/* '*************************************************** */

		if (Extra.EsNewbie(Muerto)) {
			return;
		}

		if (SistemaCombate.TriggerZonaPelea(Muerto, Atacante) == TRIGGER6_PERMITE) {
			return;
		}

		if (ES.criminal(Muerto)) {
			if (Declaraciones.UserList[Atacante].flags.LastCrimMatado != Declaraciones.UserList[Muerto].Name) {
				Declaraciones.UserList[Atacante].flags.LastCrimMatado = Declaraciones.UserList[Muerto].Name;
				if (Declaraciones.UserList[Atacante].Faccion.CriminalesMatados < Declaraciones.MAXUSERMATADOS) {
					Declaraciones.UserList[Atacante].Faccion.CriminalesMatados = Declaraciones.UserList[Atacante].Faccion.CriminalesMatados
							+ 1;
				}
			}

			if (Declaraciones.UserList[Atacante].Faccion.RecibioExpInicialCaos == 1
					&& Declaraciones.UserList[Muerto].Faccion.FuerzasCaos == 1) {
				/* 'jaja que trucho */
				Declaraciones.UserList[Atacante].Faccion.Reenlistadas = 200;

				/* 'con esto evitamos que se vuelva a reenlistar */
			}
		} else {
			if (Declaraciones.UserList[Atacante].flags.LastCiudMatado != Declaraciones.UserList[Muerto].Name) {
				Declaraciones.UserList[Atacante].flags.LastCiudMatado = Declaraciones.UserList[Muerto].Name;
				if (Declaraciones.UserList[Atacante].Faccion.CiudadanosMatados < Declaraciones.MAXUSERMATADOS) {
					Declaraciones.UserList[Atacante].Faccion.CiudadanosMatados = Declaraciones.UserList[Atacante].Faccion.CiudadanosMatados
							+ 1;
				}
			}
		}

		if (Declaraciones.UserList[Atacante].Stats.UsuariosMatados < Declaraciones.MAXUSERMATADOS) {
			Declaraciones.UserList[Atacante].Stats.UsuariosMatados = Declaraciones.UserList[Atacante].Stats.UsuariosMatados
					+ 1;
		}
	}

	static void Tilelibre(Declaraciones.WorldPos /* FIXME BYREF!! */ Pos,
			Declaraciones.WorldPos /* FIXME BYREF!! */ nPos,
			Declaraciones.Obj /* FIXME BYREF!! */ Obj,
			boolean /* FIXME BYREF!! */ PuedeAgua, boolean /* FIXME BYREF!! */ PuedeTierra) {
		/* '************************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modify Date: 18/09/2010 */
		/*
		 * '23/01/2007 -> Pablo (ToxicWaste): El agua es ahora un TileLibre
		 * agregando las condiciones necesarias.
		 */
		/*
		 * '18/09/2010: ZaMa - Aplico optimizacion de busqueda de tile libre en
		 * forma de rombo.
		 */
		/* '************************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		boolean Found = false;
		int LoopC = 0;
		int tX = 0;
		int tY = 0;

		nPos = Pos;
		tX = Pos.X;
		tY = Pos.Y;

		LoopC = 1;

		/* ' La primera posicion es valida? */
		if (Extra.LegalPos(Pos.Map, nPos.X, nPos.Y, PuedeAgua, PuedeTierra, true)) {

			if (!Extra.HayObjeto(Pos.Map, nPos.X, nPos.Y, Obj.ObjIndex, Obj.Amount)) {
				Found = true;
			}

		}

		/* ' Busca en las demas posiciones, en forma de "rombo" */
		if (!Found) {
			while ((!Found) && LoopC <= 16) {
				if (Extra.RhombLegalTilePos(Pos, tX, tY, LoopC, Obj.ObjIndex, Obj.Amount, PuedeAgua, PuedeTierra)) {
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

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en Tilelibre. Error: " + Err.Number + " - " + Err.description);
	}

	static void WarpUserChar(int UserIndex, int Map, int X, int Y, boolean FX) {
 WarpUserChar(UserIndex, Map, X, Y, FX, boolean());
 }

	static void WarpUserChar(int UserIndex, int Map, int X, int Y, boolean FX, boolean Teletransported) {
		/* '************************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modify Date: 11/23/2010 */
		/*
		 * '15/07/2009 - ZaMa: Automatic toogle navigate after warping to water.
		 */
		/*
		 * '13/11/2009 - ZaMa: Now it's activated the timer which determines if
		 * the npc can atacak the user.
		 */
		/*
		 * '16/09/2010 - ZaMa: No se pierde la visibilidad al cambiar de mapa al
		 * estar navegando invisible.
		 */
		/*
		 * '11/23/2010 - C4b3z0n: Ahora si no se permite Invi o Ocultar en el
		 * mapa al que cambias, te lo saca
		 */
		/* '************************************************************** */
		int OldMap = 0;
		int OldX = 0;
		int OldY = 0;

		/* 'Quitar el dialogo */
		modSendData.SendData(SendTarget.ToPCArea, UserIndex,
				Protocol.PrepareMessageRemoveCharDialog(Declaraciones.UserList[UserIndex].Char.CharIndex));

		OldMap = Declaraciones.UserList[UserIndex].Pos.Map;
		OldX = Declaraciones.UserList[UserIndex].Pos.X;
		OldY = Declaraciones.UserList[UserIndex].Pos.Y;

		EraseUserChar(UserIndex, Declaraciones.UserList[UserIndex].flags.AdminInvisible == 1);

		if (OldMap != Map) {
			Protocol.WriteChangeMap(UserIndex, Map,
					Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].MapVersion);

			/* 'El chequeo de invi/ocultar solo afecta a Usuarios (C4b3z0n) */
			if (Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) {
				/* 'Para enviar el mensaje de invi y hacer visible (C4b3z0n) */
				boolean AhoraVisible = false;
				boolean WasInvi = false;
				/* 'Chequeo de flags de mapa por invisibilidad (C4b3z0n) */
				if (Declaraciones.MapInfo[Map].InviSinEfecto > 0
						&& Declaraciones.UserList[UserIndex].flags.invisible == 1) {
					Declaraciones.UserList[UserIndex].flags.invisible = 0;
					Declaraciones.UserList[UserIndex].Counters.Invisibilidad = 0;
					AhoraVisible = true;
					/* 'si era invi, para el string */
					WasInvi = true;
				}
				/* 'Chequeo de flags de mapa por ocultar (C4b3z0n) */
				if (Declaraciones.MapInfo[Map].OcultarSinEfecto > 0
						&& Declaraciones.UserList[UserIndex].flags.Oculto == 1) {
					AhoraVisible = true;
					Declaraciones.UserList[UserIndex].flags.Oculto = 0;
					Declaraciones.UserList[UserIndex].Counters.TiempoOculto = 0;
				}

				/* 'Si no era visible y ahora es, le avisa. (C4b3z0n) */
				if (AhoraVisible) {
					UsUaRiOs.SetInvisible(UserIndex, Declaraciones.UserList[UserIndex].Char.CharIndex, false);
					/* 'era invi */
					if (WasInvi) {
						Protocol.WriteConsoleMsg(UserIndex,
								"Has vuelto a ser visible ya que no esta permitida la invisibilidad en este mapa.",
								FontTypeNames.FONTTYPE_INFO);
						/* 'estaba oculto */
					} else {
						Protocol.WriteConsoleMsg(UserIndex,
								"Has vuelto a ser visible ya que no esta permitido ocultarse en este mapa.",
								FontTypeNames.FONTTYPE_INFO);
					}
				}
			}

			Protocol.WritePlayMidi(UserIndex, vb6.val(General.ReadField(1, Declaraciones.MapInfo[Map].Music, 45)));

			/* 'Update new Map Users */
			Declaraciones.MapInfo[Map].NumUsers = Declaraciones.MapInfo[Map].NumUsers + 1;

			/* 'Update old Map Users */
			Declaraciones.MapInfo[OldMap].NumUsers = Declaraciones.MapInfo[OldMap].NumUsers - 1;
			if (Declaraciones.MapInfo[OldMap].NumUsers < 0) {
				Declaraciones.MapInfo[OldMap].NumUsers = 0;
			}

			/*
			 * 'Si el mapa al que entro NO ES superficial AND en el que estaba
			 * TAMPOCO ES superficial, ENTONCES
			 */
			boolean nextMap = false;
			boolean previousMap = false;

			nextMap = vb6.IIf(
					Declaraciones.distanceToCities[Map].distanceToCity[Declaraciones.UserList[UserIndex].Hogar] >= 0,
					true, false);
			previousMap = vb6.IIf(
					Declaraciones.distanceToCities[Declaraciones.UserList[UserIndex].Pos.Map].distanceToCity[Declaraciones.UserList[UserIndex].Hogar] >= 0,
					true, false);

			/* '138 => 139 (Ambos superficiales, no tiene que pasar nada) */
			if (previousMap && nextMap) {
				/* 'NO PASA NADA PORQUE NO ENTRO A UN DUNGEON. */
				/*
				 * '139 => 140 (139 es superficial, 140 no. Por lo tanto 139 es
				 * el ultimo mapa superficial)
				 */
			} else if (previousMap && !nextMap) {
				Declaraciones.UserList[UserIndex].flags.lastMap = Declaraciones.UserList[UserIndex].Pos.Map;
				/*
				 * '140 => 139 (140 es no es superficial, 139 si. Por lo tanto,
				 * el último mapa es 0 ya que no esta en un dungeon)
				 */
			} else if (!previousMap && nextMap) {
				Declaraciones.UserList[UserIndex].flags.lastMap = 0;
				/*
				 * '140 => 141 (Ninguno es superficial, el ultimo mapa es el
				 * mismo de antes)
				 */
			} else if (!previousMap && !nextMap) {
				Declaraciones.UserList[UserIndex].flags.lastMap = Declaraciones.UserList[UserIndex].flags.lastMap;
			}

			Protocol.WriteRemoveAllDialogs(UserIndex);
		}

		Declaraciones.UserList[UserIndex].Pos.X = X;
		Declaraciones.UserList[UserIndex].Pos.Y = Y;
		Declaraciones.UserList[UserIndex].Pos.Map = Map;

		if (!MakeUserChar(true, Map, UserIndex, Map, X, Y)) {
			return;
		}

		Protocol.WriteUserCharIndexInServer(UserIndex);

		Extra.DoTileEvents(UserIndex, Map, X, Y);

		/*
		 * 'Force a flush, so user index is in there before it's destroyed for
		 * teleporting
		 */
		Protocol.FlushBuffer(UserIndex);

		/* 'Seguis invisible al pasar de mapa */
		if ((Declaraciones.UserList[UserIndex].flags.invisible == 1
				|| Declaraciones.UserList[UserIndex].flags.Oculto == 1)
				&& (!Declaraciones.UserList[UserIndex].flags.AdminInvisible == 1)) {

			/* ' No si estas navegando */
			if (Declaraciones.UserList[UserIndex].flags.Navegando == 0) {
				UsUaRiOs.SetInvisible(UserIndex, Declaraciones.UserList[UserIndex].Char.CharIndex, true);
			}
		}

		if (Teletransported) {
			if (Declaraciones.UserList[UserIndex].flags.Traveling == 1) {
				Declaraciones.UserList[UserIndex].flags.Traveling = 0;
				Declaraciones.UserList[UserIndex].Counters.goHome = 0;
				Protocol.WriteMultiMessage(UserIndex, eMessages.CancelHome);
			}
		}

		/* 'FX */
		if (FX && Declaraciones.UserList[UserIndex].flags.AdminInvisible == 0) {
			modSendData.SendData(SendTarget.ToPCArea, UserIndex,
					Protocol.PrepareMessagePlayWave(Declaraciones.SND_WARP, X, Y));
			modSendData.SendData(SendTarget.ToPCArea, UserIndex,
					Protocol.PrepareMessageCreateFX(Declaraciones.UserList[UserIndex].Char.CharIndex, FXIDs.FXWARP, 0));
		}

		if (Declaraciones.UserList[UserIndex].NroMascotas) {
			WarpMascotas(UserIndex);
		}

		/* ' No puede ser atacado cuando cambia de mapa, por cierto tiempo */
		modNuevoTimer.IntervaloPermiteSerAtacado(UserIndex, true);

		/* ' Perdes el npc al cambiar de mapa */
		PerdioNpc(UserIndex, false);

		/* ' Automatic toogle navigate */
		if ((Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.User)) == 0) {
			if (General.HayAgua(Declaraciones.UserList[UserIndex].Pos.Map, Declaraciones.UserList[UserIndex].Pos.X,
					Declaraciones.UserList[UserIndex].Pos.Y)) {
				if (Declaraciones.UserList[UserIndex].flags.Navegando == 0) {
					Declaraciones.UserList[UserIndex].flags.Navegando = 1;

					/* 'Tell the client that we are navigating. */
					Protocol.WriteNavigateToggle(UserIndex);
				}
			} else {
				if (Declaraciones.UserList[UserIndex].flags.Navegando == 1) {
					Declaraciones.UserList[UserIndex].flags.Navegando = 0;

					/* 'Tell the client that we are navigating. */
					Protocol.WriteNavigateToggle(UserIndex);
				}
			}
		}

	}

	static void WarpMascotas(int UserIndex) {
		/* '************************************************ */
		/* 'Author: Uknown */
		/* 'Last Modified: 26/10/2010 */
		/*
		 * '13/02/2009: ZaMa - Arreglado respawn de mascotas al cambiar de mapa.
		 */
		/*
		 * '13/02/2009: ZaMa - Las mascotas no regeneran su vida al cambiar de
		 * mapa (Solo entre mapas inseguros).
		 */
		/*
		 * '11/05/2009: ZaMa - Chequeo si la mascota pueden spwnear para
		 * asiganrle los stats.
		 */
		/*
		 * '26/10/2010: ZaMa - Ahora las mascotas rapswnean de forma aleatoria.
		 */
		/* '************************************************ */
		int i = 0;
		int petType = 0;
		boolean PetRespawn = false;
		int PetTiempoDeVida = 0;
		int NroPets = 0;
		int InvocadosMatados = 0;
		boolean canWarp = false;
		int index = 0;
		int iMinHP = 0;

		NroPets = Declaraciones.UserList[UserIndex].NroMascotas;
		canWarp = (Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].Pk == true);

		for (i = (1); i <= (Declaraciones.MAXMASCOTAS); i++) {
			index = Declaraciones.UserList[UserIndex].MascotasIndex[i];

			if (index > 0) {
				/*
				 * ' si la mascota tiene tiempo de vida > 0 significa q fue
				 * invocada => we kill it
				 */
				if (Declaraciones.Npclist[index].Contadores.TiempoExistencia > 0) {
					NPCs.QuitarNPC(index);
					Declaraciones.UserList[UserIndex].MascotasIndex[i] = 0;
					InvocadosMatados = InvocadosMatados + 1;
					NroPets = NroPets - 1;

					petType = 0;
				} else {
					/* 'Store data and remove NPC to recreate it after warp */
					/* 'PetRespawn = Npclist(index).flags.Respawn = 0 */
					petType = Declaraciones.UserList[UserIndex].MascotasType[i];
					/*
					 * 'PetTiempoDeVida =
					 * Npclist(index).Contadores.TiempoExistencia
					 */

					/*
					 * ' Guardamos el hp, para restaurarlo uando se cree el npc
					 */
					iMinHP = Declaraciones.Npclist[index].Stats.MinHp;

					NPCs.QuitarNPC(index);

					/* ' Restauramos el valor de la variable */
					Declaraciones.UserList[UserIndex].MascotasType[i] = petType;

				}
			} else if (Declaraciones.UserList[UserIndex].MascotasType[i] > 0) {
				/* 'Store data and remove NPC to recreate it after warp */
				PetRespawn = true;
				petType = Declaraciones.UserList[UserIndex].MascotasType[i];
				PetTiempoDeVida = 0;
			} else {
				petType = 0;
			}

			if (petType > 0 && canWarp) {

				Declaraciones.WorldPos SpawnPos;

				SpawnPos.Map = Declaraciones.UserList[UserIndex].Pos.Map;
				SpawnPos.X = Declaraciones.UserList[UserIndex].Pos.X + Matematicas.RandomNumber(-3, 3);
				SpawnPos.Y = Declaraciones.UserList[UserIndex].Pos.Y + Matematicas.RandomNumber(-3, 3);

				index = NPCs.SpawnNpc(petType, SpawnPos, false, PetRespawn);

				/*
				 * 'Controlamos que se sumoneo OK - should never happen.
				 * Continue to allow removal of other pets if not alone
				 */
				/* ' Exception: Pets don't spawn in water if they can't swim */
				if (index == 0) {
					Protocol.WriteConsoleMsg(UserIndex, "Tus mascotas no pueden transitar este mapa.",
							FontTypeNames.FONTTYPE_INFO);
				} else {
					Declaraciones.UserList[UserIndex].MascotasIndex[i] = index;

					/*
					 * ' Nos aseguramos de que conserve el hp, si estaba dañado
					 */
					Declaraciones.Npclist[index].Stats.MinHp = vb6.IIf(iMinHP == 0,
							Declaraciones.Npclist[index].Stats.MinHp, iMinHP);

					Declaraciones.Npclist[index].MaestroUser = UserIndex;
					Declaraciones.Npclist[index].Contadores.TiempoExistencia = PetTiempoDeVida;
					NPCs.FollowAmo(index);
				}
			}
		}

		if (InvocadosMatados > 0) {
			Protocol.WriteConsoleMsg(UserIndex, "Pierdes el control de tus mascotas invocadas.",
					FontTypeNames.FONTTYPE_INFO);
		}

		if (!canWarp) {
			Protocol.WriteConsoleMsg(UserIndex, "No se permiten mascotas en zona segura. Éstas te esperarán afuera.",
					FontTypeNames.FONTTYPE_INFO);
		}

		Declaraciones.UserList[UserIndex].NroMascotas = NroPets;
	}

	static void WarpMascota(int UserIndex, int PetIndex) {
		/* '************************************************ */
		/* 'Author: ZaMa */
		/* 'Last Modified: 18/11/2009 */
		/* 'Warps a pet without changing its stats */
		/* '************************************************ */
		int petType = 0;
		int NpcIndex = 0;
		int iMinHP = 0;
		Declaraciones.WorldPos TargetPos;

		TargetPos.Map = Declaraciones.UserList[UserIndex].flags.TargetMap;
		TargetPos.X = Declaraciones.UserList[UserIndex].flags.TargetX;
		TargetPos.Y = Declaraciones.UserList[UserIndex].flags.TargetY;

		NpcIndex = Declaraciones.UserList[UserIndex].MascotasIndex[PetIndex];

		/* 'Store data and remove NPC to recreate it after warp */
		petType = Declaraciones.UserList[UserIndex].MascotasType[PetIndex];

		/* ' Guardamos el hp, para restaurarlo cuando se cree el npc */
		iMinHP = Declaraciones.Npclist[NpcIndex].Stats.MinHp;

		NPCs.QuitarNPC(NpcIndex);

		/* ' Restauramos el valor de la variable */
		Declaraciones.UserList[UserIndex].MascotasType[PetIndex] = petType;
		Declaraciones.UserList[UserIndex].NroMascotas = Declaraciones.UserList[UserIndex].NroMascotas + 1;
		NpcIndex = NPCs.SpawnNpc(petType, TargetPos, false, false);

		/*
		 * 'Controlamos que se sumoneo OK - should never happen. Continue to
		 * allow removal of other pets if not alone
		 */
		/* ' Exception: Pets don't spawn in water if they can't swim */
		if (NpcIndex == 0) {
			Protocol.WriteConsoleMsg(UserIndex,
					"Tu mascota no pueden transitar este sector del mapa, intenta invocarla en otra parte.",
					FontTypeNames.FONTTYPE_INFO);
		} else {
			Declaraciones.UserList[UserIndex].MascotasIndex[PetIndex] = NpcIndex;

			/* ' Nos aseguramos de que conserve el hp, si estaba dañado */
			Declaraciones.Npclist[NpcIndex].Stats.MinHp = vb6.IIf(iMinHP == 0,
					Declaraciones.Npclist[NpcIndex].Stats.MinHp, iMinHP);

			Declaraciones.Npclist[NpcIndex].MaestroUser = UserIndex;
			Declaraciones.Npclist[NpcIndex].Movement = TipoAI.SigueAmo;
			Declaraciones.Npclist[NpcIndex].Target = 0;
			Declaraciones.Npclist[NpcIndex].TargetNPC = 0;

			NPCs.FollowAmo(NpcIndex);
		}
	}

	/* '' */
	/* ' Se inicia la salida de un usuario. */
	/* ' */
	/* ' @param UserIndex El index del usuario que va a salir */

	static void Cerrar_Usuario(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 16/09/2010 */
		/*
		 * '16/09/2010 - ZaMa: Cuando se va el invi estando navegando, no se
		 * saca el invi (ya esta visible).
		 */
		/* '*************************************************** */
		boolean isNotVisible = false;
		boolean HiddenPirat = false;

		if (Declaraciones.UserList[UserIndex].flags.UserLogged
				&& !Declaraciones.UserList[UserIndex].Counters.Saliendo) {
			Declaraciones.UserList[UserIndex].Counters.Saliendo = true;
			Declaraciones.UserList[UserIndex].Counters.Salir = vb6.IIf(
					(Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User)
							&& Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].Pk,
					Admin.IntervaloCerrarConexion, 0);

			isNotVisible = (Declaraciones.UserList[UserIndex].flags.Oculto
					|| Declaraciones.UserList[UserIndex].flags.invisible);
			if (isNotVisible) {
				Declaraciones.UserList[UserIndex].flags.invisible = 0;

				if (Declaraciones.UserList[UserIndex].flags.Oculto) {
					if (Declaraciones.UserList[UserIndex].flags.Navegando == 1) {
						if (Declaraciones.UserList[UserIndex].clase == eClass.Pirat) {
							/* ' Pierde la apariencia de fragata fantasmal */
							ToggleBoatBody(UserIndex);
							Protocol.WriteConsoleMsg(UserIndex, "¡Has recuperado tu apariencia normal!",
									FontTypeNames.FONTTYPE_INFO);
							ChangeUserChar(UserIndex, Declaraciones.UserList[UserIndex].Char.body,
									Declaraciones.UserList[UserIndex].Char.Head,
									Declaraciones.UserList[UserIndex].Char.heading, Declaraciones.NingunArma,
									Declaraciones.NingunEscudo, Declaraciones.NingunCasco);
							HiddenPirat = true;
						}
					}
				}

				Declaraciones.UserList[UserIndex].flags.Oculto = 0;

				/* ' Para no repetir mensajes */
				if (!HiddenPirat) {
					Protocol.WriteConsoleMsg(UserIndex, "Has vuelto a ser visible.", FontTypeNames.FONTTYPE_INFO);
				}

				/* ' Si esta navegando ya esta visible */
				if (Declaraciones.UserList[UserIndex].flags.Navegando == 0) {
					UsUaRiOs.SetInvisible(UserIndex, Declaraciones.UserList[UserIndex].Char.CharIndex, false);
				}
			}

			if (Declaraciones.UserList[UserIndex].flags.Traveling == 1) {
				Protocol.WriteMultiMessage(UserIndex, eMessages.CancelHome);
				Declaraciones.UserList[UserIndex].flags.Traveling = 0;
				Declaraciones.UserList[UserIndex].Counters.goHome = 0;
			}

			Protocol.WriteConsoleMsg(UserIndex, "Cerrando...Se cerrará el juego en "
					+ Declaraciones.UserList[UserIndex].Counters.Salir + " segundos...", FontTypeNames.FONTTYPE_INFO);
		}
	}

	/* '' */
	/* ' Cancels the exit of a user. If it's disconnected it's reset. */
	/* ' */
	/* ' @param UserIndex The index of the user whose exit is being reset. */

	static void CancelExit(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 04/02/08 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].Counters.Saliendo) {
			/* ' Is the user still connected? */
			if (Declaraciones.UserList[UserIndex].ConnIDValida) {
				Declaraciones.UserList[UserIndex].Counters.Saliendo = false;
				Declaraciones.UserList[UserIndex].Counters.Salir = 0;
				Protocol.WriteConsoleMsg(UserIndex, "/salir cancelado.", FontTypeNames.FONTTYPE_WARNING);
			} else {
				/* 'Simply reset */
				Declaraciones.UserList[UserIndex].Counters.Salir = vb6.IIf(
						(Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User)
								&& Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].Pk,
						Admin.IntervaloCerrarConexion, 0);
			}
		}
	}

	/* 'CambiarNick: Cambia el Nick de un slot. */
	/* ' */
	/* 'UserIndex: Quien ejecutó la orden */
	/* 'UserIndexDestino: SLot del usuario destino, a quien cambiarle el nick */
	/* 'NuevoNick: Nuevo nick de UserIndexDestino */
	static void CambiarNick(int UserIndex, int UserIndexDestino, String NuevoNick) {
 /* '*************************************************** */
 /* 'Author: Unknown */
 /* 'Last Modification: - */
 /* ' */
 /* '*************************************************** */
 
 String ViejoNick;
 String ViejoCharBackup;
 
 if (Declaraciones.UserList[UserIndexDestino].flags.UserLogged == false) {
 return;
 }
 ViejoNick = Declaraciones.UserList[UserIndexDestino].Name;
 
  if (General.FileExist(Declaraciones.CharPath + ViejoNick + ".chr", 0)) {
  /* 'hace un backup del char */
  ViejoCharBackup = Declaraciones.CharPath + ViejoNick + ".chr.old-";
  Name(Declaraciones.CharPath + ViejoNick + ".chr"ASViejoCharBackup);
 }
}

	static void SendUserStatsTxtOFF(int sendIndex, String Nombre) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		if (General.FileExist(Declaraciones.CharPath + Nombre + ".chr", vbArchive) == false) {
			Protocol.WriteConsoleMsg(sendIndex, "Pj Inexistente", FontTypeNames.FONTTYPE_INFO);
		} else {
			Protocol.WriteConsoleMsg(sendIndex, "Estadísticas de: " + Nombre, FontTypeNames.FONTTYPE_INFO);
			Protocol.WriteConsoleMsg(sendIndex,
					"Nivel: " + ES.GetVar(Declaraciones.CharPath + Nombre + ".chr", "stats", "elv") + "  EXP: "
							+ ES.GetVar(Declaraciones.CharPath + Nombre + ".chr", "stats", "Exp") + "/"
							+ ES.GetVar(Declaraciones.CharPath + Nombre + ".chr", "stats", "elu"),
					FontTypeNames.FONTTYPE_INFO);
			Protocol.WriteConsoleMsg(sendIndex,
					"Energía: " + ES.GetVar(Declaraciones.CharPath + Nombre + ".chr", "stats", "minsta") + "/"
							+ ES.GetVar(Declaraciones.CharPath + Nombre + ".chr", "stats", "maxSta"),
					FontTypeNames.FONTTYPE_INFO);
			Protocol.WriteConsoleMsg(sendIndex,
					"Salud: " + ES.GetVar(Declaraciones.CharPath + Nombre + ".chr", "stats", "MinHP") + "/"
							+ ES.GetVar(Declaraciones.CharPath + Nombre + ".chr", "Stats", "MaxHP") + "  Maná: "
							+ ES.GetVar(Declaraciones.CharPath + Nombre + ".chr", "Stats", "MinMAN") + "/"
							+ ES.GetVar(Declaraciones.CharPath + Nombre + ".chr", "Stats", "MaxMAN"),
					FontTypeNames.FONTTYPE_INFO);

			Protocol.WriteConsoleMsg(sendIndex,
					"Menor Golpe/Mayor Golpe: "
							+ ES.GetVar(Declaraciones.CharPath + Nombre + ".chr", "stats", "MaxHIT"),
					FontTypeNames.FONTTYPE_INFO);

			Protocol.WriteConsoleMsg(sendIndex,
					"Oro: " + ES.GetVar(Declaraciones.CharPath + Nombre + ".chr", "stats", "GLD"),
					FontTypeNames.FONTTYPE_INFO);

			/* # IF ConUpTime THEN */
			int TempSecs = 0;
			String TempStr;
			TempSecs = ES.GetVar(Declaraciones.CharPath + Nombre + ".chr", "INIT", "UpTime");
			TempStr = (TempSecs / 86400) + " Días, " + ((TempSecs % 86400) / 3600) + " Horas, "
					+ ((TempSecs % 86400) % 3600) / 60 + " Minutos, " + (((TempSecs % 86400) % 3600) % 60)
					+ " Segundos.";
			Protocol.WriteConsoleMsg(sendIndex, "Tiempo Logeado: " + TempStr, FontTypeNames.FONTTYPE_INFO);
			/* # END IF */

			Protocol.WriteConsoleMsg(sendIndex,
					"Dados: " + ES.GetVar(Declaraciones.CharPath + Nombre + ".chr", "ATRIBUTOS", "AT1") + ", "
							+ ES.GetVar(Declaraciones.CharPath + Nombre + ".chr", "ATRIBUTOS", "AT2") + ", "
							+ ES.GetVar(Declaraciones.CharPath + Nombre + ".chr", "ATRIBUTOS", "AT3") + ", "
							+ ES.GetVar(Declaraciones.CharPath + Nombre + ".chr", "ATRIBUTOS", "AT4") + ", "
							+ ES.GetVar(Declaraciones.CharPath + Nombre + ".chr", "ATRIBUTOS", "AT5"),
					FontTypeNames.FONTTYPE_INFO);
		}
	}

	static void SendUserOROTxtFromChar(int sendIndex, String charName) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		String CharFile;

		/* FIXME: ON ERROR RESUME NEXT */
		CharFile = Declaraciones.CharPath + charName + ".chr";

		if (General.FileExist(CharFile, 0)) {
			Protocol.WriteConsoleMsg(sendIndex, charName, FontTypeNames.FONTTYPE_INFO);
			Protocol.WriteConsoleMsg(sendIndex, "Tiene " + ES.GetVar(CharFile, "STATS", "BANCO") + " en el banco.",
					FontTypeNames.FONTTYPE_INFO);
		} else {
			Protocol.WriteConsoleMsg(sendIndex, "Usuario inexistente: " + charName, FontTypeNames.FONTTYPE_INFO);
		}
	}

	static void VolverCriminal(int UserIndex) {
		/* '************************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modify Date: 21/02/2010 */
		/* 'Nacho: Actualiza el tag al cliente */
		/*
		 * '21/02/2010: ZaMa - Ahora deja de ser atacable si se hace criminal.
		 */
		/* '************************************************************** */
		if (Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].trigger == eTrigger.ZONAPELEA) {
			return;
		}

		if (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.User || PlayerType.Consejero)) {
			Declaraciones.UserList[UserIndex].Reputacion.BurguesRep = 0;
			Declaraciones.UserList[UserIndex].Reputacion.NobleRep = 0;
			Declaraciones.UserList[UserIndex].Reputacion.PlebeRep = 0;
			Declaraciones.UserList[UserIndex].Reputacion.BandidoRep = Declaraciones.UserList[UserIndex].Reputacion.BandidoRep
					+ Declaraciones.vlASALTO;
			if (Declaraciones.UserList[UserIndex].Reputacion.BandidoRep > Declaraciones.MAXREP) {
				Declaraciones.UserList[UserIndex].Reputacion.BandidoRep = Declaraciones.MAXREP;
			}
			if (Declaraciones.UserList[UserIndex].Faccion.ArmadaReal == 1) {
				ModFacciones.ExpulsarFaccionReal(UserIndex);
			}

			if (Declaraciones.UserList[UserIndex].flags.AtacablePor > 0) {
				Declaraciones.UserList[UserIndex].flags.AtacablePor = 0;
			}

		}

		RefreshCharStatus(UserIndex);
	}

	static void VolverCiudadano(int UserIndex) {
		/* '************************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modify Date: 21/06/2006 */
		/* 'Nacho: Actualiza el tag al cliente. */
		/* '************************************************************** */
		if (Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].trigger == 6) {
			return;
		}

		Declaraciones.UserList[UserIndex].Reputacion.LadronesRep = 0;
		Declaraciones.UserList[UserIndex].Reputacion.BandidoRep = 0;
		Declaraciones.UserList[UserIndex].Reputacion.AsesinoRep = 0;
		Declaraciones.UserList[UserIndex].Reputacion.PlebeRep = Declaraciones.UserList[UserIndex].Reputacion.PlebeRep
				+ Declaraciones.vlASALTO;
		if (Declaraciones.UserList[UserIndex].Reputacion.PlebeRep > Declaraciones.MAXREP) {
			Declaraciones.UserList[UserIndex].Reputacion.PlebeRep = Declaraciones.MAXREP;
		}

		RefreshCharStatus(UserIndex);
	}

	/* '' */
	/* 'Checks if a given body index is a boat or not. */
	/* ' */
	/* '@param body The body index to bechecked. */
	/* '@return True if the body is a boat, false otherwise. */

	static boolean BodyIsBoat(int body) {
		boolean retval = false;
		/* '************************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modify Date: 10/07/2008 */
		/* 'Checks if a given body index is a boat */
		/* '************************************************************** */
		/* 'TODO : This should be checked somehow else. This is nasty.... */
		if (body == Declaraciones.iFragataReal || body == Declaraciones.iFragataCaos || body == Declaraciones.iBarcaPk
				|| body == Declaraciones.iGaleraPk || body == Declaraciones.iGaleonPk
				|| body == Declaraciones.iBarcaCiuda || body == Declaraciones.iGaleraCiuda
				|| body == Declaraciones.iGaleonCiuda || body == Declaraciones.iFragataFantasmal) {
			retval = true;
		}
		return retval;
	}

	static void SetInvisible(int UserIndex, int userCharIndex, boolean invisible) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		String sndNick;

		modSendData.SendData(SendTarget.ToUsersAndRmsAndCounselorsAreaButGMs, UserIndex,
				Protocol.PrepareMessageSetInvisible(userCharIndex, invisible));

		sndNick = Declaraciones.UserList[UserIndex].Name;

		if (invisible) {
			sndNick = sndNick + " " + Declaraciones.TAG_USER_INVISIBLE;
		} else {
			if (Declaraciones.UserList[UserIndex].GuildIndex > 0) {
				sndNick = sndNick + " <" + modGuilds.GuildName(Declaraciones.UserList[UserIndex].GuildIndex) + ">";
			}
		}

		modSendData.SendData(SendTarget.ToGMsAreaButRmsOrCounselors, UserIndex,
				Protocol.PrepareMessageCharacterChangeNick(userCharIndex, sndNick));
	}

	static void SetConsulatMode(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Torres Patricio (Pato) */
		/* 'Last Modification: 05/06/10 */
		/* ' */
		/* '*************************************************** */

		String sndNick;

		sndNick = Declaraciones.UserList[UserIndex].Name;

		if (Declaraciones.UserList[UserIndex].flags.EnConsulta) {
			sndNick = sndNick + " " + Declaraciones.TAG_CONSULT_MODE;
		} else {
			if (Declaraciones.UserList[UserIndex].GuildIndex > 0) {
				sndNick = sndNick + " <" + modGuilds.GuildName(Declaraciones.UserList[UserIndex].GuildIndex) + ">";
			}
		}

		modSendData.SendData(SendTarget.ToPCArea, UserIndex,
				Protocol.PrepareMessageCharacterChangeNick(Declaraciones.UserList[UserIndex].Char.CharIndex, sndNick));
	}

	static boolean IsArena(int UserIndex) {
		boolean retval = false;
		/* '************************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modify Date: 10/11/2009 */
		/* 'Returns true if the user is in an Arena */
		/* '************************************************************** */
		retval = (SistemaCombate.TriggerZonaPelea(UserIndex, UserIndex) == TRIGGER6_PERMITE);
		return retval;
	}

	static void PerdioNpc(int UserIndex) {
		PerdioNpc(UserIndex, true);
	}

	static void PerdioNpc(int UserIndex, boolean CheckPets) {
		/* '************************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modify Date: 11/07/2010 (ZaMa) */
		/* 'The user loses his owned npc */
		/*
		 * '18/01/2010: ZaMa - Las mascotas dejan de atacar al npc que se
		 * perdió.
		 */
		/*
		 * '11/07/2010: ZaMa - Coloco el indice correcto de las mascotas y ahora
		 * siguen al amo si existen.
		 */
		/*
		 * '13/07/2010: ZaMa - Ahora solo dejan de atacar las mascotas si estan
		 * atacando al npc que pierde su amo.
		 */
		/* '************************************************************** */

		int PetCounter = 0;
		int PetIndex = 0;
		int NpcIndex = 0;

		NpcIndex = Declaraciones.UserList[UserIndex].flags.OwnedNpc;
		if (NpcIndex > 0) {

			if (CheckPets) {
				/* ' Dejan de atacar las mascotas */
				if (Declaraciones.UserList[UserIndex].NroMascotas > 0) {
					for (PetCounter = (1); PetCounter <= (Declaraciones.MAXMASCOTAS); PetCounter++) {

						PetIndex = Declaraciones.UserList[UserIndex].MascotasIndex[PetCounter];

						if (PetIndex > 0) {
							/* ' Si esta atacando al npc deja de hacerlo */
							if (Declaraciones.Npclist[PetIndex].TargetNPC == NpcIndex) {
								NPCs.FollowAmo(PetIndex);
							}
						}

					}
				}
			}

			/* ' Reset flags */
			Declaraciones.Npclist[NpcIndex].Owner = 0;
			Declaraciones.UserList[UserIndex].flags.OwnedNpc = 0;

		}
	}

	static void ApropioNpc(int UserIndex, int NpcIndex) {
		/* '************************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modify Date: 27/07/2010 (zaMa) */
		/* 'The user owns a new npc */
		/* '18/01/2010: ZaMa - El sistema no aplica a zonas seguras. */
		/*
		 * '19/04/2010: ZaMa - Ahora los admins no se pueden apropiar de npcs.
		 */
		/* '27/07/2010: ZaMa - El sistema no aplica a mapas seguros. */
		/* '************************************************************** */

		/* ' Los admins no se pueden apropiar de npcs */
		if (Extra.EsGm(UserIndex)) {
			return;
		}

		int mapa = 0;
		mapa = Declaraciones.UserList[UserIndex].Pos.Map;

		/* ' No aplica a triggers seguras */
		if (Declaraciones.MapData[mapa][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].trigger == eTrigger.ZONASEGURA) {
			return;
		}

		/* ' No se aplica a mapas seguros */
		if (Declaraciones.MapInfo[mapa].Pk == false) {
			return;
		}

		/* ' No aplica a algunos mapas que permiten el robo de npcs */
		if (Declaraciones.MapInfo[mapa].RoboNpcsPermitido == 1) {
			return;
		}

		/* ' Pierde el npc anterior */
		if (Declaraciones.UserList[UserIndex].flags.OwnedNpc > 0) {
			Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.OwnedNpc].Owner = 0;
		}

		/* ' Si tenia otro dueño, lo perdio aca */
		Declaraciones.Npclist[NpcIndex].Owner = UserIndex;
		Declaraciones.UserList[UserIndex].flags.OwnedNpc = NpcIndex;

		/* ' Inicializo o actualizo el timer de pertenencia */
		modNuevoTimer.IntervaloPerdioNpc(UserIndex, true);
	}

	static String GetDireccion(int UserIndex, int OtherUserIndex) {
		String retval;
		/* '************************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modify Date: 17/11/2009 */
		/* 'Devuelve la direccion hacia donde esta el usuario */
		/* '************************************************************** */
		int X = 0;
		int Y = 0;

		X = Declaraciones.UserList[UserIndex].Pos.X - Declaraciones.UserList[OtherUserIndex].Pos.X;
		Y = Declaraciones.UserList[UserIndex].Pos.Y - Declaraciones.UserList[OtherUserIndex].Pos.Y;

		if (X == 0 && Y > 0) {
			retval = "Sur";
		} else if (X == 0 && Y < 0) {
			retval = "Norte";
		} else if (X > 0 && Y == 0) {
			retval = "Este";
		} else if (X < 0 && Y == 0) {
			retval = "Oeste";
		} else if (X > 0 && Y < 0) {
			retval = "NorEste";
		} else if (X < 0 && Y < 0) {
			retval = "NorOeste";
		} else if (X > 0 && Y > 0) {
			retval = "SurEste";
		} else if (X < 0 && Y > 0) {
			retval = "SurOeste";
		}

		return retval;
	}

	static boolean SameFaccion(int UserIndex, int OtherUserIndex) {
		boolean retval = false;
		/* '************************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modify Date: 17/11/2009 */
		/* 'Devuelve True si son de la misma faccion */
		/* '************************************************************** */
		retval = (Extra.esCaos(UserIndex) && Extra.esCaos(OtherUserIndex))
				|| (Extra.esArmada(UserIndex) && Extra.esArmada(OtherUserIndex));
		return retval;
	}

	static int FarthestPet(int UserIndex) {
		int retval = 0;
		/* '************************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modify Date: 18/11/2009 */
		/* 'Devuelve el indice de la mascota mas lejana. */
		/* '************************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		int PetIndex = 0;
		int Distancia = 0;
		int OtraDistancia = 0;

		if (Declaraciones.UserList[UserIndex].NroMascotas == 0) {
			return retval;
		}

		for (PetIndex = (1); PetIndex <= (Declaraciones.MAXMASCOTAS); PetIndex++) {
			/* ' Solo pos invocar criaturas que exitan! */
			if (Declaraciones.UserList[UserIndex].MascotasIndex[PetIndex] > 0) {
				/* ' Solo aplica a mascota, nada de elementales.. */
				if (Declaraciones.Npclist[Declaraciones.UserList[UserIndex].MascotasIndex[PetIndex]].Contadores.TiempoExistencia == 0) {
					if (retval == 0) {
						/* ' Por si tiene 1 sola mascota */
						retval = PetIndex;
						Distancia = vb6
								.Abs(Declaraciones.UserList[UserIndex].Pos.X
										- Declaraciones.Npclist[Declaraciones.UserList[UserIndex].MascotasIndex[PetIndex]].Pos.X)
								+ vb6.Abs(Declaraciones.UserList[UserIndex].Pos.Y
										- Declaraciones.Npclist[Declaraciones.UserList[UserIndex].MascotasIndex[PetIndex]].Pos.Y);
					} else {
						/* ' La distancia de la proxima mascota */
						OtraDistancia = vb6
								.Abs(Declaraciones.UserList[UserIndex].Pos.X
										- Declaraciones.Npclist[Declaraciones.UserList[UserIndex].MascotasIndex[PetIndex]].Pos.X)
								+ vb6.Abs(Declaraciones.UserList[UserIndex].Pos.Y
										- Declaraciones.Npclist[Declaraciones.UserList[UserIndex].MascotasIndex[PetIndex]].Pos.Y);
						/* ' Esta mas lejos? */
						if (OtraDistancia > Distancia) {
							Distancia = OtraDistancia;
							retval = PetIndex;
						}
					}
				}
			}
		}

		return retval;

		/* FIXME: ErrHandler : */
		General.LogError("Error en FarthestPet");
		return retval;
	}

	/* '' */
	/* ' Set the EluSkill value at the skill. */
	/* ' */
	/* ' @param UserIndex Specifies reference to user */
	/* ' @param Skill Number of the skill to check */
	/*
	 * ' @param Allocation True If the motive of the modification is the
	 * allocation, False if the skill increase by training
	 */

	static void CheckEluSkill(int UserIndex, eSkill Skill, boolean Allocation) {
		/* '************************************************* */
		/* 'Author: Torres Patricio (Pato) */
		/* 'Last modified: 11/20/2009 */
		/* ' */
		/* '************************************************* */

		if (Declaraciones.UserList[UserIndex].Stats.UserSkills[Skill] < Declaraciones.MAXSKILLPOINTS) {
			if (Allocation) {
				Declaraciones.UserList[UserIndex].Stats.ExpSkills[Skill] = 0;
			} else {
				Declaraciones.UserList[UserIndex].Stats.ExpSkills[Skill] = Declaraciones.UserList[UserIndex].Stats.ExpSkills[Skill]
						- Declaraciones.UserList[UserIndex].Stats.EluSkills[Skill];
			}

			Declaraciones.UserList[UserIndex].Stats.EluSkills[Skill] = (int) (Declaraciones.ELU_SKILL_INICIAL
					* Math.pow(1.05, Declaraciones.UserList[UserIndex].Stats.UserSkills[Skill]));
		} else {
			Declaraciones.UserList[UserIndex].Stats.ExpSkills[Skill] = 0;
			Declaraciones.UserList[UserIndex].Stats.EluSkills[Skill] = 0;
		}

	}

	static boolean HasEnoughItems(int UserIndex, int ObjIndex, int Amount) {
		boolean retval = false;
		/* '************************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modify Date: 25/11/2009 */
		/*
		 * 'Cheks Wether the user has the required amount of items in the
		 * inventory or not
		 */
		/* '************************************************************** */

		int Slot = 0;
		int ItemInvAmount = 0;

		for (Slot = (1); Slot <= (Declaraciones.UserList[UserIndex].CurrentInventorySlots); Slot++) {
			/* ' Si es el item que busco */
			if (Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex == ObjIndex) {
				/* ' Lo sumo a la cantidad total */
				ItemInvAmount = ItemInvAmount + Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount;
			}
		}

		retval = Amount <= ItemInvAmount;
		return retval;
	}

	static int TotalOfferItems(int ObjIndex, int UserIndex) {
		int retval = 0;
		/* '************************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modify Date: 25/11/2009 */
		/* 'Cheks the amount of items the user has in offerSlots. */
		/* '************************************************************** */
		int Slot = 0;

		for (Slot = (1); Slot <= (mdlCOmercioConUsuario.MAX_OFFER_SLOTS); Slot++) {
			/* ' Si es el item que busco */
			if (Declaraciones.UserList[UserIndex].ComUsu.Objeto[Slot] == ObjIndex) {
				/* ' Lo sumo a la cantidad total */
				retval = retval + Declaraciones.UserList[UserIndex].ComUsu.cant[Slot];
			}
		}

		return retval;
	}

	static int getMaxInventorySlots(int UserIndex) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		if (Declaraciones.UserList[UserIndex].Invent.MochilaEqpObjIndex > 0) {
			/* '5=slots por fila, hacer constante */
			retval = Declaraciones.MAX_NORMAL_INVENTORY_SLOTS
					+ Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.MochilaEqpObjIndex].MochilaType
							* 5;
		} else {
			retval = Declaraciones.MAX_NORMAL_INVENTORY_SLOTS;
		}
		return retval;
	}

	static void goHome(int UserIndex) {
 /* '*************************************************** */
 /* 'Author: Budi */
 /* 'Last Modification: 01/06/2010 */
 /* '01/06/2010: ZaMa - Ahora usa otro tipo de intervalo (lo saque de tPiquetec) */
 /* '*************************************************** */
 
 int Distance = 0;
 int Tiempo = 0;
 
   if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
    if (Declaraciones.UserList[UserIndex].flags.lastMap == 0) {
    Distance = Declaraciones.distanceToCities[Declaraciones.UserList[UserIndex].Pos.Map].distanceToCity[Declaraciones.UserList[UserIndex].Hogar];
    } else {
    Distance = Declaraciones.distanceToCities[Declaraciones.UserList[UserIndex].flags.lastMap].distanceToCity[Declaraciones.UserList[UserIndex].Hogar]+Declaraciones.GOHOME_PENALTY;
   }
   
   /* ' [TEMPORAL]  30 'seg */
   Tiempo = (Distance+1)*SistemaCombate.MaximoInt((Declaraciones.UserList[UserIndex].Stats.ELV-25), 0)*1.5;
   
   modNuevoTimer.IntervaloGoHome(UserIndex, Tiempo*1000, true);
   
   Protocol.WriteMultiMessage(UserIndex, eMessages.Home, Distance, Tiempo, , Declaraciones.MapInfo[Declaraciones.Ciudades[Declaraciones.UserList[UserIndex].Hogar].Map].Name);
   } else {
   Protocol.WriteConsoleMsg(UserIndex, "Debes estar muerto para poder utilizar este comando.", FontTypeNames.FONTTYPE_FIGHT);
  }
  
}

	static boolean ToogleToAtackable(int UserIndex, int OwnerIndex) {
		return ToogleToAtackable(UserIndex, OwnerIndex, true);
	}

	static boolean ToogleToAtackable(int UserIndex, int OwnerIndex, boolean StealingNpc) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 15/01/2010 */
		/* 'Change to Atackable mode. */
		/* '*************************************************** */

		int AtacablePor = 0;

		if (Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].Pk == false) {
			Protocol.WriteConsoleMsg(UserIndex, "No puedes robar npcs en zonas seguras.", FontTypeNames.FONTTYPE_INFO);
			return retval;
		}

		AtacablePor = Declaraciones.UserList[UserIndex].flags.AtacablePor;

		if (AtacablePor > 0) {
			/* ' Intenta robar un npc */
			if (StealingNpc) {
				/*
				 * ' Puede atacar el mismo npc que ya estaba robando, pero no
				 * una nuevo.
				 */
				if (AtacablePor != OwnerIndex) {
					Protocol.WriteConsoleMsg(UserIndex,
							"No puedes atacar otra criatura con dueño hasta que haya terminado tu castigo.",
							FontTypeNames.FONTTYPE_INFO);
					return retval;
				}
				/*
				 * ' Esta atacando a alguien en estado atacable => Se renueva el
				 * timer de atacable
				 */
			} else {
				/* ' Renovar el timer */
				modNuevoTimer.IntervaloEstadoAtacable(UserIndex, true);
				retval = true;
				return retval;
			}
		}

		Declaraciones.UserList[UserIndex].flags.AtacablePor = OwnerIndex;

		/* ' Actualizar clientes */
		RefreshCharStatus(UserIndex);

		/* ' Inicializar el timer */
		modNuevoTimer.IntervaloEstadoAtacable(UserIndex, true);

		retval = true;

		return retval;
	}

	static void setHome(int UserIndex, eCiudad newHome, int NpcIndex) {
		/* '*************************************************** */
		/* 'Author: Budi */
		/* 'Last Modification: 01/06/2010 */
		/* '30/04/2010: ZaMa - Ahora el npc avisa que se cambio de hogar. */
		/* '01/06/2010: ZaMa - Ahora te avisa si ya tenes ese hogar. */
		/* '*************************************************** */
		if (newHome < eCiudad.cUllathorpe || newHome > eCiudad.cLastCity - 1) {
			return;
		}

		if (Declaraciones.UserList[UserIndex].Hogar != newHome) {
			Declaraciones.UserList[UserIndex].Hogar = newHome;

			Protocol.WriteChatOverHead(UserIndex,
					"¡¡¡Bienvenido a nuestra humilde comunidad, este es ahora tu nuevo hogar!!!",
					Declaraciones.Npclist[NpcIndex].Char.CharIndex, 0x00ffffff);
		} else {
			Protocol.WriteChatOverHead(UserIndex, "¡¡¡Ya eres miembro de nuestra humilde comunidad!!!",
					Declaraciones.Npclist[NpcIndex].Char.CharIndex, 0x00ffffff);
		}

	}

	static int GetHomeArrivalTime(int UserIndex) {
		int retval = 0;
		/* '************************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modify by: ZaMa */
		/* 'Last Modify Date: 01/06/2010 */
		/* 'Calculates the time left to arrive home. */
		/* '************************************************************** */
		int TActual = 0;

		TActual = Declaraciones.GetTickCount() && 0x7FFFFFFF;

		retval = modNuevoTimer.getInterval(Declaraciones.UserList[UserIndex].Counters.goHome, TActual) * 0.001;

		return retval;
	}

	static void HomeArrival(int UserIndex) {
		/* '************************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modify by: ZaMa */
		/* 'Last Modify Date: 01/06/2010 */
		/* 'Teleports user to its home. */
		/* '************************************************************** */

		int tX = 0;
		int tY = 0;
		int tMap = 0;

		/*
		 * 'Antes de que el pj llegue a la ciudad, lo hacemos dejar de navegar
		 * para que no se buguee.
		 */
		if (Declaraciones.UserList[UserIndex].flags.Navegando == 1) {
			Declaraciones.UserList[UserIndex].Char.body = Declaraciones.iCuerpoMuerto;
			Declaraciones.UserList[UserIndex].Char.Head = Declaraciones.iCabezaMuerto;
			Declaraciones.UserList[UserIndex].Char.ShieldAnim = Declaraciones.NingunEscudo;
			Declaraciones.UserList[UserIndex].Char.WeaponAnim = Declaraciones.NingunArma;
			Declaraciones.UserList[UserIndex].Char.CascoAnim = Declaraciones.NingunCasco;

			Declaraciones.UserList[UserIndex].flags.Navegando = 0;

			Protocol.WriteNavigateToggle(UserIndex);
			/*
			 * 'Le sacamos el navegando, pero no le mostramos a los demás porque
			 * va a ser sumoneado hasta ulla.
			 */
		}

		tX = Declaraciones.Ciudades[Declaraciones.UserList[UserIndex].Hogar].X;
		tY = Declaraciones.Ciudades[Declaraciones.UserList[UserIndex].Hogar].Y;
		tMap = Declaraciones.Ciudades[Declaraciones.UserList[UserIndex].Hogar].Map;

		Extra.FindLegalPos(UserIndex, tMap, tX, tY);
		WarpUserChar(UserIndex, tMap, tX, tY, true);

		Protocol.WriteMultiMessage(UserIndex, eMessages.FinishHome);

		Declaraciones.UserList[UserIndex].flags.Traveling = 0;
		Declaraciones.UserList[UserIndex].Counters.goHome = 0;

	}

}