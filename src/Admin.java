
/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"Admin"')] */
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

public class Admin {

	static public class tMotd {
		String texto;
		String Formato;
	}

	public static int MaxLines;
	public static tMotd[] MOTD = new tMotd[0]; /* XXX */

	static public class tAPuestas {
		int Ganancias;
		int Perdidas;
		int Jugadas;
	}

	public static tAPuestas Apuestas;

	public static int tInicioServer;
	public static clsEstadisticasIPC EstadisticasWeb;

	/* 'INTERVALOS */
	public static int SanaIntervaloSinDescansar;
	public static int StaminaIntervaloSinDescansar;
	public static int SanaIntervaloDescansar;
	public static int StaminaIntervaloDescansar;
	public static int IntervaloSed;
	public static int IntervaloHambre;
	public static int IntervaloVeneno;
	public static int IntervaloParalizado;
	static final int IntervaloParalizadoReducido = 37;
	public static int IntervaloInvisible;
	public static int IntervaloFrio;
	public static int IntervaloWavFx;
	public static int IntervaloLanzaHechizo;
	public static int IntervaloNPCPuedeAtacar;
	public static int IntervaloNPCAI;
	public static int IntervaloInvocacion;
	/* '[Nacho] */
	public static int IntervaloOculto;
	public static int IntervaloUserPuedeAtacar;
	public static int IntervaloGolpeUsar;
	public static int IntervaloMagiaGolpe;
	public static int IntervaloGolpeMagia;
	public static int IntervaloUserPuedeCastear;
	public static int IntervaloUserPuedeTrabajar;
	public static int IntervaloParaConexion;
	/* '[Gonzalo] */
	public static int IntervaloCerrarConexion;
	public static int IntervaloUserPuedeUsar;
	public static int IntervaloFlechasCazadores;
	public static int IntervaloPuedeSerAtacado;
	public static int IntervaloAtacable;
	public static int IntervaloOwnedNpc;

	/* 'BALANCE */

	public static int PorcentajeRecuperoMana;

	public static int MinutosWs;
	public static int MinutosGuardarUsuarios;
	public static int MinutosMotd;
	public static int Puerto;

	public static int BootDelBackUp;
	public static boolean Lloviendo;
	public static boolean DeNoche;

	static boolean VersionOK(String Ver) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		retval = (Ver == Declaraciones.ULTIMAVERSION);
		return retval;
	}

	static void ReSpawnOrigPosNpcs() {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR RESUME NEXT */

		int i;
		Declaraciones.npc MiNPC;

		for (i = (1); i <= (Declaraciones.LastNPC); i++) {
			/* 'OJO */
			if (Declaraciones.Npclist[i].flags.NPCActive) {

				if (Extra.InMapBounds(Declaraciones.Npclist[i].Orig.Map, Declaraciones.Npclist[i].Orig.X,
						Declaraciones.Npclist[i].Orig.Y) && Declaraciones.Npclist[i].Numero == Declaraciones.Guardias) {
					MiNPC = Declaraciones.Npclist[i];
					NPCs.QuitarNPC(i);
					NPCs.ReSpawnNpc(MiNPC);
				}

				/* 'tildada por sugerencia de yind */
				/* 'If Npclist(i).Contadores.TiempoExistencia > 0 Then */
				/* ' Call MuereNpc(i, 0) */
				/* 'End If */
			}

		}

	}

	static void WorldSave() {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR RESUME NEXT */

		int loopX;
		int hFile;

		modSendData.SendData(SendTarget.ToAll, 0,
				Protocol.PrepareMessageConsoleMsg("Servidor> Iniciando WorldSave", FontTypeNames.FONTTYPE_SERVER));

		/* # IF SeguridadAlkon THEN */
		/* # END IF */

		/* 'respawn de los guardias en las pos originales */
		ReSpawnOrigPosNpcs();

		int j;
		int k;

		for (j = (1); j <= (Declaraciones.NumMaps); j++) {
			if (Declaraciones.MapInfo[j].BackUp == 1) {
				k = k + 1;
			}
		}

		FrmStat.ProgressBar1.min = 0;
		FrmStat.ProgressBar1.max = k;
		FrmStat.ProgressBar1.value = 0;

		for (loopX = (1); loopX <= (Declaraciones.NumMaps); loopX++) {
			/* 'DoEvents */

			if (Declaraciones.MapInfo[loopX].BackUp == 1) {
				ES.GrabarMapa(loopX, vb6.App.Instance().Path + "\\WorldBackUp\\Mapa" + loopX);
				FrmStat.ProgressBar1.value = FrmStat.ProgressBar1.value + 1;
			}

		}

		FrmStat.Visible = false;

		if (General.FileExist(Declaraciones.DatPath + "\\bkNpcs.dat")) {
			/* FIXME: KILL ( DatPath & "bkNpcs.dat" ) */
		}

		hFile = vb6.FreeFile();

		/* FIXME: OPEN DatPath & "\\bkNpcs.dat" FOR OUTPUT AS hFile */

		for (loopX = (1); loopX <= (Declaraciones.LastNPC); loopX++) {
			if (Declaraciones.Npclist[loopX].flags.BackUp == 1) {
				ES.BackUPnPc(loopX, hFile);
			}
		}

		/* FIXME: CLOSE hFile */

		modForum.SaveForums();

		modSendData.SendData(SendTarget.ToAll, 0,
				Protocol.PrepareMessageConsoleMsg("Servidor> WorldSave ha concluído.", FontTypeNames.FONTTYPE_SERVER));
	}

	static void PurgarPenas() {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int i;

		for (i = (1); i <= (Declaraciones.LastUser); i++) {
			if (Declaraciones.UserList[i].flags.UserLogged) {
				if (Declaraciones.UserList[i].Counters.Pena > 0) {
					Declaraciones.UserList[i].Counters.Pena = Declaraciones.UserList[i].Counters.Pena - 1;

					if (Declaraciones.UserList[i].Counters.Pena < 1) {
						Declaraciones.UserList[i].Counters.Pena = 0;
						UsUaRiOs.WarpUserChar(i, Declaraciones.Libertad.Map, Declaraciones.Libertad.X,
								Declaraciones.Libertad.Y, true);
						Protocol.WriteConsoleMsg(i, "¡Has sido liberado!", FontTypeNames.FONTTYPE_INFO);

						Protocol.FlushBuffer(i);
					}
				}
			}
		}
	}

	static void Encarcelar(int UserIndex, int Minutos) {
		Encarcelar(UserIndex, Minutos, "");
	}

	static void Encarcelar(int UserIndex, int Minutos, String GmName) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		Declaraciones.UserList[UserIndex].Counters.Pena = Minutos;

		UsUaRiOs.WarpUserChar(UserIndex, Declaraciones.Prision.Map, Declaraciones.Prision.X, Declaraciones.Prision.Y,
				true);

		if (vb6.LenB(GmName) == 0) {
			Protocol.WriteConsoleMsg(UserIndex,
					"Has sido encarcelado, deberás permanecer en la cárcel " + Minutos + " minutos.",
					FontTypeNames.FONTTYPE_INFO);
		} else {
			Protocol.WriteConsoleMsg(UserIndex,
					GmName + " te ha encarcelado, deberás permanecer en la cárcel " + Minutos + " minutos.",
					FontTypeNames.FONTTYPE_INFO);
		}
		if (Declaraciones.UserList[UserIndex].flags.Traveling == 1) {
			Declaraciones.UserList[UserIndex].flags.Traveling = 0;
			Declaraciones.UserList[UserIndex].Counters.goHome = 0;
			Protocol.WriteMultiMessage(UserIndex, eMessages.CancelHome);
		}
	}

	static void BorrarUsuario(String UserName) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR RESUME NEXT */
		if (General.FileExist(Declaraciones.CharPath + vb6.UCase(UserName) + ".chr", 0)) {
			/* FIXME: KILL CharPath & UCase $ ( UserName ) & ".chr" */
		}
	}

	static boolean BANCheck(String Name) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		retval = (vb6.val(ES.GetVar(vb6.App.Instance().Path + "\\charfile\\" + Name + ".chr", "FLAGS", "Ban")) == 1);

		return retval;
	}

	static boolean PersonajeExiste(String Name) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		retval = General.FileExist(Declaraciones.CharPath + vb6.UCase(Name) + ".chr", 0);

		return retval;
	}

	static boolean UnBan(String Name) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* 'Unban the character */
		ES.WriteVar(vb6.App.Instance().Path + "\\charfile\\" + Name + ".chr", "FLAGS", "Ban", "0");

		/* 'Remove it from the banned people database */
		ES.WriteVar(vb6.App.Instance().Path + "\\logs\\" + "BanDetail.dat", Name, "BannedBy", "NOBODY");
		ES.WriteVar(vb6.App.Instance().Path + "\\logs\\" + "BanDetail.dat", Name, "Reason", "NO REASON");
		return retval;
	}

	static boolean MD5ok(String md5formateado) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int i;

		if (Declaraciones.MD5ClientesActivado == 1) {
			for (i = (0); i <= (vb6.UBound(Declaraciones.MD5s)); i++) {
				if ((md5formateado == Declaraciones.MD5s[i])) {
					retval = true;
					return retval;
				}
			}
			retval = false;
		} else {
			retval = true;
		}

		return retval;
	}

	static void MD5sCarga() {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int LoopC;

		Declaraciones.MD5ClientesActivado = vb6
				.val(ES.GetVar(Declaraciones.IniPath + "Server.ini", "MD5Hush", "Activado"));

		if (Declaraciones.MD5ClientesActivado == 1) {
			Declaraciones.MD5s = new None[0];
			Declaraciones.MD5s = (Declaraciones.MD5s == null)
					? new None[vb6.val(ES.GetVar(Declaraciones.IniPath + "Server.ini", "MD5Hush", "MD5Aceptados"))]
					: java.util.Arrays.copyOf(Declaraciones.MD5s,
							vb6.val(ES.GetVar(Declaraciones.IniPath + "Server.ini", "MD5Hush", "MD5Aceptados")));
			for (LoopC = (0); LoopC <= (vb6.UBound(Declaraciones.MD5s)); LoopC++) {
				Declaraciones.MD5s[LoopC] = ES.GetVar(Declaraciones.IniPath + "Server.ini", "MD5Hush",
						"MD5Aceptado" + (LoopC + 1));
				Declaraciones.MD5s[LoopC] = modHexaStrings
						.txtOffset(modHexaStrings.hexMd52Asc(Declaraciones.MD5s[LoopC]), 55);
			}
		}

	}

	static void BanIpAgrega(String ip) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		Declaraciones.BanIps.Add(ip);

		BanIpGuardar();
	}

	static int BanIpBuscar(String ip) {
		int retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		boolean Dale;
		int LoopC;

		Dale = true;
		LoopC = 1;
		while (LoopC <= Declaraciones.BanIps.Count && Dale) {
			Dale = (Declaraciones.BanIps.Item[LoopC] != ip);
			LoopC = LoopC + 1;
		}

		if (Dale) {
			retval = 0;
		} else {
			retval = LoopC - 1;
		}
		return retval;
	}

	static boolean BanIpQuita(String ip) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR RESUME NEXT */

		int N;

		N = BanIpBuscar(ip);
		if (N > 0) {
			Declaraciones.BanIps.Remove(N);
			ABanIpGuardar();
			retval = true;
		} else {
			retval = false;
		}

		return retval;
	}

	static void BanIpGuardar() {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		String ArchivoBanIp;
		int ArchN;
		int LoopC;

		ArchivoBanIp = vb6.App.Instance().Path + "\\Dat\\BanIps.dat";

		ArchN = vb6.FreeFile();
		/* FIXME: OPEN ArchivoBanIp FOR OUTPUT AS # ArchN */

		for (LoopC = (1); LoopC <= (Declaraciones.BanIps.Count); LoopC++) {
			/* FIXME: PRINT # ArchN , BanIps . Item ( LoopC ) */
		}

		/* FIXME: CLOSE # ArchN */

	}

	static void BanIpCargar() {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int ArchN;
		String Tmp;
		String ArchivoBanIp;

		ArchivoBanIp = vb6.App.Instance().Path + "\\Dat\\BanIps.dat";

		Declaraciones.BanIps = new Collection();

		ArchN = vb6.FreeFile();
		/* FIXME: OPEN ArchivoBanIp FOR INPUT AS # ArchN */

		while (!clsByteBuffer.Eof(ArchN)) {
			/* FIXME: LINE INPUT # ArchN , Tmp */
			Declaraciones.BanIps.Add(Tmp);
		}

		/* FIXME: CLOSE # ArchN */

	}

	static void ActualizaEstadisticasWeb() {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		Static(AndandoASBoolean);
		Static(ContadorASLong);
		boolean Tmp;

		Contador = Contador + 1;

		if (Contador >= 10) {
			Contador = 0;
			Tmp = Admin.EstadisticasWeb.EstadisticasAndando();

			if (Andando == false && Tmp == true) {
				General.InicializaEstadisticas();
			}

			Andando = Tmp;
		}

	}

	static PlayerType UserDarPrivilegioLevel(String Name) {
		PlayerType retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 03/02/07 */
		/* 'Last Modified By: Juan Martín Sotuyo Dodero (Maraxus) */
		/* '*************************************************** */

		if (ES.EsAdmin(Name)) {
			retval = PlayerType.Admin;
		} else if (ES.EsDios(Name)) {
			retval = PlayerType.Dios;
		} else if (ES.EsSemiDios(Name)) {
			retval = PlayerType.SemiDios;
		} else if (ES.EsConsejero(Name)) {
			retval = PlayerType.Consejero;
		} else {
			retval = PlayerType.User;
		}
		return retval;
	}

	static void BanCharacter(int bannerUserIndex, String UserName, String Reason) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 03/02/07 */
		/*
		 * '22/05/2010: Ya no se peude banear admins de mayor rango si estan
		 * online.
		 */
		/* '*************************************************** */

		int tUser;
		int UserPriv;
		int cantPenas;
		int rank;

		if (vb6.InStrB(UserName, "+")) {
			UserName = vb6.Replace(UserName, "+", " ");
		}

		tUser = Extra.NameIndex(UserName);

		rank = PlayerType.Admin || PlayerType.Dios || PlayerType.SemiDios || PlayerType.Consejero;

		if (tUser <= 0) {
			Protocol.WriteConsoleMsg(bannerUserIndex, "El usuario no está online.", FontTypeNames.FONTTYPE_TALK);

			if (General.FileExist(Declaraciones.CharPath + UserName + ".chr", 0)) {
				UserPriv = UserDarPrivilegioLevel(UserName);

				if ((UserPriv && rank) > (Declaraciones.UserList[bannerUserIndex].flags.Privilegios && rank)) {
					Protocol.WriteConsoleMsg(bannerUserIndex, "No puedes banear a al alguien de mayor jerarquía.",
							FontTypeNames.FONTTYPE_INFO);
				} else {
					if (ES.GetVar(Declaraciones.CharPath + UserName + ".chr", "FLAGS", "Ban") != "0") {
						Protocol.WriteConsoleMsg(bannerUserIndex, "El personaje ya se encuentra baneado.",
								FontTypeNames.FONTTYPE_INFO);
					} else {
						ES.LogBanFromName(UserName, bannerUserIndex, Reason);
						modSendData
								.SendData(SendTarget.ToAdminsButCounselorsAndRms, 0,
										Protocol.PrepareMessageConsoleMsg(
												"Servidor> " + Declaraciones.UserList[bannerUserIndex].Name
														+ " ha baneado a " + UserName + ".",
												FontTypeNames.FONTTYPE_SERVER));

						/* 'ponemos el flag de ban a 1 */
						ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "FLAGS", "Ban", "1");
						/* 'ponemos la pena */
						cantPenas = vb6.val(ES.GetVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "Cant"));
						ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "Cant", cantPenas + 1);
						ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "P" + cantPenas + 1,
								vb6.LCase(Declaraciones.UserList[bannerUserIndex].Name) + ": BAN POR "
										+ vb6.LCase(Reason) + " " + Date + " " + vb6.time());

						if ((UserPriv && rank) == (Declaraciones.UserList[bannerUserIndex].flags.Privilegios && rank)) {
							Declaraciones.UserList[bannerUserIndex].flags.Ban = 1;
							modSendData.SendData(SendTarget.ToAdmins, 0,
									Protocol.PrepareMessageConsoleMsg(
											Declaraciones.UserList[bannerUserIndex].Name
													+ " banned by the server por bannear un Administrador.",
											FontTypeNames.FONTTYPE_FIGHT));
							TCP.CloseSocket(bannerUserIndex);
						}

						General.LogGM(Declaraciones.UserList[bannerUserIndex].Name,
								"BAN a " + UserName + ". Razón: " + Reason);
					}
				}
			} else {
				Protocol.WriteConsoleMsg(bannerUserIndex, "El pj " + UserName + " no existe.",
						FontTypeNames.FONTTYPE_INFO);
			}
		} else {
			if ((Declaraciones.UserList[tUser].flags.Privilegios
					&& rank) > (Declaraciones.UserList[bannerUserIndex].flags.Privilegios && rank)) {
				Protocol.WriteConsoleMsg(bannerUserIndex, "No puedes banear a al alguien de mayor jerarquía.",
						FontTypeNames.FONTTYPE_INFO);
			} else {

				ES.LogBan(tUser, bannerUserIndex, Reason);
				modSendData.SendData(SendTarget.ToAdminsButCounselorsAndRms, 0,
						Protocol.PrepareMessageConsoleMsg("Servidor> " + Declaraciones.UserList[bannerUserIndex].Name
								+ " ha baneado a " + Declaraciones.UserList[tUser].Name + ".",
								FontTypeNames.FONTTYPE_SERVER));

				/* 'Ponemos el flag de ban a 1 */
				Declaraciones.UserList[tUser].flags.Ban = 1;

				if ((Declaraciones.UserList[tUser].flags.Privilegios
						&& rank) == (Declaraciones.UserList[bannerUserIndex].flags.Privilegios && rank)) {
					Declaraciones.UserList[bannerUserIndex].flags.Ban = 1;
					modSendData.SendData(SendTarget.ToAdminsButCounselorsAndRms, 0,
							Protocol.PrepareMessageConsoleMsg(
									Declaraciones.UserList[bannerUserIndex].Name
											+ " banned by the server por bannear un Administrador.",
									FontTypeNames.FONTTYPE_FIGHT));
					TCP.CloseSocket(bannerUserIndex);
				}

				General.LogGM(Declaraciones.UserList[bannerUserIndex].Name, "BAN a " + UserName + ". Razón: " + Reason);

				/* 'ponemos el flag de ban a 1 */
				ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "FLAGS", "Ban", "1");
				/* 'ponemos la pena */
				cantPenas = vb6.val(ES.GetVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "Cant"));
				ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "Cant", cantPenas + 1);
				ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "P" + cantPenas + 1,
						vb6.LCase(Declaraciones.UserList[bannerUserIndex].Name) + ": BAN POR " + vb6.LCase(Reason) + " "
								+ Date + " " + vb6.time());

				TCP.CloseSocket(tUser);
			}
		}
	}

}