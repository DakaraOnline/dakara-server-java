/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"modCentinela"')] */
/* '***************************************************************** */
/* 'modCentinela.bas - ImperiumAO - v1.2 */
/* ' */
/* 'Funciónes de control para usuarios que se encuentran trabajando */
/* ' */
/* '***************************************************************** */
/* 'Respective portions copyrighted by contributors listed below. */
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

/* '***************************************************************** */
/* 'Augusto Rando(barrin@imperiumao.com.ar) */
/* '   ImperiumAO 1.2 */
/* '   - First Relase */
/* ' */
/* 'Juan Martín Sotuyo Dodero (juansotuyo@gmail.com) */
/* '   Alkon AO 0.11.5 */
/* '   - Small improvements and added logs to detect possible cheaters */
/* ' */
/* 'Juan Martín Sotuyo Dodero (juansotuyo@gmail.com) */
/* '   Alkon AO 0.12.0 */
/* '   - Added several messages to spam users until they reply */
/* ' */
/* 'ZaMa */
/* '   Alkon AO 0.13.0 */
/* '   - Added several paralel checks */
/* '***************************************************************** */

import enums.*;

public class modCentinela {

	/* 'Índice del NPC en el .dat */
	static final int NPC_CENTINELA = 16;

	/*
	 * 'Tiempo inicial en minutos. No reducir sin antes revisar el timer que
	 * maneja estos datos.
	 */
	static final int TIEMPO_INICIAL = 2;
	/* 'Tiempo minimo fijo para volver a pasar */
	static final int TIEMPO_PASAR_BASE = 20;
	/* 'Tiempo máximo para el random para que el centinela vuelva a pasar */
	static final int TIEMPO_PASAR_RANDOM = 10;

	static public class tCentinela {
		public int NpcIndex;
		public int RevisandoUserIndex;
		public int TiempoRestante;
		public int clave;
		public int SpawnTime;
		public boolean Activo;
	}

	public static boolean centinelaActivado;

	/* 'Guardo cuando voy a resetear a la lista de usuarios del centinela */
	private static int centinelaStartTime;
	private static int centinelaInterval;

	private static boolean DetenerAsignacion;

	static final int NRO_CENTINELA = 5;
	public static tCentinela[] Centinela = new tCentinela[1 + modCentinela.NRO_CENTINELA];

	static void CallUserAttention() {
		/* '************************************************* */
		/* 'Author: Unknown */
		/* 'Last modified: 03/10/2010 */
		/* 'Makes noise and FX to call the user's attention. */
		/*
		 * '03/10/2010: ZaMa - Adaptado para que funcione mas de un centinela en
		 * paralelo.
		 */
		/* '************************************************* */

		/* 'Esta el sistema habilitado? */
		if (!modCentinela.centinelaActivado) {
			return;
		}

		int index = 0;
		int UserIndex = 0;

		int TActual = 0;
		TActual = (Declaraciones.GetTickCount() && 0x7FFFFFFF);

		/* ' Chequea todos los centinelas */
		for (index = (1); index <= (modCentinela.NRO_CENTINELA); index++) {

			/* ' Centinela activo? */
			if (modCentinela.Centinela[index].Activo) {

				UserIndex = modCentinela.Centinela[index].RevisandoUserIndex;

				/* ' Esta revisando un usuario? */
				if (UserIndex != 0) {

					if (modNuevoTimer.getInterval(TActual, modCentinela.Centinela[index].SpawnTime) >= 5000) {

						if (!Declaraciones.UserList[UserIndex].flags.CentinelaOK) {
							Protocol.WritePlayWave(UserIndex, Declaraciones.SND_WARP,
									Declaraciones.Npclist[modCentinela.Centinela[index].NpcIndex].Pos.X,
									Declaraciones.Npclist[modCentinela.Centinela[index].NpcIndex].Pos.Y);
							Protocol.WriteCreateFX(UserIndex,
									Declaraciones.Npclist[modCentinela.Centinela[index].NpcIndex].Char.CharIndex,
									FXIDs.FXWARP, 0);

							/* 'Resend the key */
							CentinelaSendClave(UserIndex, index);

							Protocol.FlushBuffer(UserIndex);
						}
					}
				}
			}

		}
	}

	static void GoToNextWorkingChar() {
		/* '************************************************* */
		/* 'Author: Unknown */
		/* 'Last modified: 03/10/2010 */
		/* 'Va al siguiente usuario que se encuentre trabajando */
		/*
		 * '09/27/2010: C4b3z0n - Ahora una vez que termina la lista de
		 * usuarios, si se cumplio el tiempo de reset, resetea la info y asigna
		 * un nuevo tiempo.
		 */
		/*
		 * '03/10/2010: ZaMa - Adaptado para que funcione mas de un centinela en
		 * paralelo.
		 */
		/* '************************************************* */

		int LoopC = 0;
		int CentinelaIndex = 0;

		CentinelaIndex = GetIdleCentinela(1);

		for (LoopC = (1); LoopC <= (Declaraciones.LastUser); LoopC++) {

			/* ' Usuario trabajando y no revisado? */
			if (Declaraciones.UserList[LoopC].flags.UserLogged && Declaraciones.UserList[LoopC].Counters.Trabajando > 0
					&& (Declaraciones.UserList[LoopC].flags.Privilegios && PlayerType.User)) {
				if (!Declaraciones.UserList[LoopC].flags.CentinelaOK
						&& Declaraciones.UserList[LoopC].flags.CentinelaIndex == 0) {
					/* 'Inicializamos */
					modCentinela.Centinela[CentinelaIndex].RevisandoUserIndex = LoopC;
					modCentinela.Centinela[CentinelaIndex].TiempoRestante = modCentinela.TIEMPO_INICIAL;
					modCentinela.Centinela[CentinelaIndex].clave = Matematicas.RandomNumber(1, 32000);
					modCentinela.Centinela[CentinelaIndex].SpawnTime = Declaraciones.GetTickCount() && 0x7FFFFFFF;
					modCentinela.Centinela[CentinelaIndex].Activo = true;

					/* 'Ponemos al centinela en posición */
					WarpCentinela(LoopC, CentinelaIndex);

					/* ' Spawneo? */
					if (modCentinela.Centinela[CentinelaIndex].NpcIndex != 0) {
						/*
						 * 'Mandamos el mensaje (el centinela habla y aparece en
						 * consola para que no haya dudas)
						 */
						Protocol.WriteChatOverHead(LoopC,
								"Saludos " + Declaraciones.UserList[LoopC].Name
										+ ", soy el Centinela de estas tierras. Me gustaría que escribas /CENTINELA "
										+ modCentinela.Centinela[CentinelaIndex].clave + " en no más de dos minutos.",
								vb6.CStr(
										Declaraciones.Npclist[modCentinela.Centinela[CentinelaIndex].NpcIndex].Char.CharIndex),
								vbGreen);
						Protocol.WriteConsoleMsg(LoopC, "El centinela intenta llamar tu atención. ¡Respóndele rápido!",
								FontTypeNames.FONTTYPE_CENTINELA);
						Protocol.FlushBuffer(LoopC);

						/* ' Guardo el indice del centinela */
						Declaraciones.UserList[LoopC].flags.CentinelaIndex = CentinelaIndex;
					}

					/* ' Si ya se asigno un usuario a cada centinela, me voy */
					CentinelaIndex = CentinelaIndex + 1;
					if (CentinelaIndex > modCentinela.NRO_CENTINELA) {
						return;
					}

					/* ' Si no queda nadie inactivo, me voy */
					CentinelaIndex = GetIdleCentinela(CentinelaIndex);
					if (CentinelaIndex == 0) {
						return;
					}

				}
			}

		}

	}

	static int GetIdleCentinela(int StartCheckIndex) {
		int retval = 0;
		/* '************************************************* */
		/* 'Author: ZaMa */
		/* 'Last modified: 07/10/2010 */
		/*
		 * 'Returns the index of the first idle centinela found, starting from a
		 * given index.
		 */
		/* '************************************************* */
		int index = 0;

		for (index = (StartCheckIndex); index <= (modCentinela.NRO_CENTINELA); index++) {

			if (!modCentinela.Centinela[index].Activo) {
				retval = index;
				return retval;
			}

		}

		return retval;
	}

	static void CentinelaFinalCheck(int CentiIndex) {
		/* '************************************************* */
		/* 'Author: Unknown */
		/* 'Last modified: 02/10/2010 */
		/*
		 * 'Al finalizar el tiempo, se retira y realiza la acción pertinente
		 * dependiendo del caso
		 */
		/*
		 * '03/10/2010: ZaMa - Adaptado para que funcione mas de un centinela en
		 * paralelo.
		 */
		/* '************************************************* */

		/* FIXME: ON ERROR GOTO Error_Handler */

		int UserIndex = 0;
		String UserName;

		UserIndex = modCentinela.Centinela[CentiIndex].RevisandoUserIndex;

		if (!Declaraciones.UserList[UserIndex].flags.CentinelaOK) {

			UserName = Declaraciones.UserList[UserIndex].Name;

			/* 'Logueamos el evento */
			LogCentinela("Centinela ejecuto y echó a " + UserName + " por uso de macro inasistido.");

			/* 'Avisamos a los admins */
			modSendData.SendData(SendTarget.ToAdmins, 0,
					Protocol.PrepareMessageConsoleMsg(
							"Servidor> El centinela ha ejecutado a " + UserName + " y lo echó del juego.",
							FontTypeNames.FONTTYPE_SERVER));

			/* ' Evitamos loguear el logout */
			modCentinela.Centinela[CentiIndex].RevisandoUserIndex = 0;

			Protocol.WriteShowMessageBox(UserIndex, "Has sido ejecutado por macro inasistido y echado del juego.");
			UsUaRiOs.UserDie(UserIndex);
			Protocol.FlushBuffer(UserIndex);
			TCP.CloseSocket(UserIndex);
		}

		modCentinela.Centinela[CentiIndex].clave = 0;
		modCentinela.Centinela[CentiIndex].TiempoRestante = 0;
		modCentinela.Centinela[CentiIndex].RevisandoUserIndex = 0;
		modCentinela.Centinela[CentiIndex].Activo = false;

		if (modCentinela.Centinela[CentiIndex].NpcIndex != 0) {
			NPCs.QuitarNPC(modCentinela.Centinela[CentiIndex].NpcIndex);
			modCentinela.Centinela[CentiIndex].NpcIndex = 0;
		}

		return;

		/* FIXME: Error_Handler : */

		modCentinela.Centinela[CentiIndex].clave = 0;
		modCentinela.Centinela[CentiIndex].TiempoRestante = 0;
		modCentinela.Centinela[CentiIndex].RevisandoUserIndex = 0;
		modCentinela.Centinela[CentiIndex].Activo = false;

		if (modCentinela.Centinela[CentiIndex].NpcIndex) {
			NPCs.QuitarNPC(modCentinela.Centinela[CentiIndex].NpcIndex);
			modCentinela.Centinela[CentiIndex].NpcIndex = 0;
		}

		General.LogError("Error en el checkeo del centinela: " + Err.description);
	}

	static void CentinelaCheckClave(int UserIndex, int clave) {
		/* '************************************************* */
		/* 'Author: Unknown */
		/* 'Last modified: 02/10/2010 */
		/* 'Corrobora la clave que le envia el usuario */
		/*
		 * '02/10/2010: ZaMa - Adaptado para que funcione mas de un centinela en
		 * paralelo.
		 */
		/* '08/10/2010: ZaMa - Agrego algunos logueos mas coherentes. */
		/* '************************************************* */

		int CentinelaIndex = 0;

		CentinelaIndex = Declaraciones.UserList[UserIndex].flags.CentinelaIndex;

		/* ' No esta siendo revisado por ningun centinela? Clickeo a alguno? */
		if (CentinelaIndex == 0) {

			/*
			 * ' Si no clickeo a ninguno, simplemente logueo el evento (Sino
			 * hago hablar al centi)
			 */
			CentinelaIndex = EsCentinela(Declaraciones.UserList[UserIndex].flags.TargetNPC);
			if (CentinelaIndex == 0) {
				LogCentinela("El usuario " + Declaraciones.UserList[UserIndex].Name
						+ " respondió aunque no se le hablaba a él..");
				return;
			}

		}

		if (clave == modCentinela.Centinela[CentinelaIndex].clave
				&& UserIndex == modCentinela.Centinela[CentinelaIndex].RevisandoUserIndex) {

			if (!Declaraciones.UserList[UserIndex].flags.CentinelaOK) {

				Declaraciones.UserList[UserIndex].flags.CentinelaOK = true;
				Protocol.WriteChatOverHead(UserIndex,
						"¡Muchas gracias " + Declaraciones.UserList[UserIndex].Name
								+ "! Espero no haber sido una molestia.",
						Declaraciones.Npclist[modCentinela.Centinela[CentinelaIndex].NpcIndex].Char.CharIndex,
						0x00ffffff);

				modCentinela.Centinela[CentinelaIndex].Activo = false;
				Protocol.FlushBuffer(UserIndex);

			} else {
				/* 'Logueamos el evento */
				LogCentinela("El usuario " + Declaraciones.UserList[UserIndex].Name
						+ " respondió más de una vez la contraseña correcta.");
			}

		} else {

			/* 'Logueamos el evento */
			if (UserIndex != modCentinela.Centinela[CentinelaIndex].RevisandoUserIndex) {
				Protocol.WriteChatOverHead(UserIndex, "No es a ti a quien estoy hablando, ¿No ves?",
						Declaraciones.Npclist[modCentinela.Centinela[CentinelaIndex].NpcIndex].Char.CharIndex,
						0x00ffffff);
				LogCentinela("El usuario " + Declaraciones.UserList[UserIndex].Name
						+ " respondió aunque no se le hablaba a él.");
			} else {

				if (!Declaraciones.UserList[UserIndex].flags.CentinelaOK) {
					/* ' Clave incorrecta, la reenvio */
					CentinelaSendClave(UserIndex, CentinelaIndex);
					LogCentinela(
							"El usuario " + Declaraciones.UserList[UserIndex].Name + " respondió una clave incorrecta: "
									+ clave + " - Se esperaba : " + modCentinela.Centinela[CentinelaIndex].clave);
				} else {
					LogCentinela("El usuario " + Declaraciones.UserList[UserIndex].Name
							+ " respondió una clave incorrecta después de haber respondido una clave correcta.");
				}
			}
		}

	}

	static void ResetCentinelaInfo() {
		/* '************************************************* */
		/* 'Author: Unknown */
		/* 'Last modified: 02/10/2010 */
		/* 'Cada determinada cantidad de tiempo, volvemos a revisar */
		/*
		 * '07/10/2010: ZaMa - Adaptado para que funcione mas de un centinela en
		 * paralelo.
		 */
		/* '************************************************* */
		int LoopC = 0;

		for (LoopC = (1); LoopC <= (Declaraciones.LastUser); LoopC++) {

			Declaraciones.UserList[LoopC].flags.CentinelaOK = false;
			Declaraciones.UserList[LoopC].flags.CentinelaIndex = 0;

		}

	}

	static void CentinelaSendClave(int UserIndex, int CentinelaIndex) {
		/* '************************************************* */
		/* 'Author: Unknown */
		/* 'Last modified: 02/10/2010 */
		/* 'Enviamos al usuario la clave vía el personaje centinela */
		/*
		 * '02/10/2010: ZaMa - Adaptado para que funcione mas de un centinela en
		 * paralelo.
		 */
		/* '************************************************* */

		if (modCentinela.Centinela[CentinelaIndex].NpcIndex == 0) {
			return;
		}

		if (modCentinela.Centinela[CentinelaIndex].RevisandoUserIndex == UserIndex) {

			if (!Declaraciones.UserList[UserIndex].flags.CentinelaOK) {
				Protocol.WriteChatOverHead(UserIndex,
						"¡La clave que te he dicho es /CENTINELA " + modCentinela.Centinela[CentinelaIndex].clave
								+ ", escríbelo rápido!",
						Declaraciones.Npclist[modCentinela.Centinela[CentinelaIndex].NpcIndex].Char.CharIndex, vbGreen);
				Protocol.WriteConsoleMsg(UserIndex, "El centinela intenta llamar tu atención. ¡Respondele rápido!",
						FontTypeNames.FONTTYPE_CENTINELA);
			} else {
				Protocol.WriteChatOverHead(UserIndex, "Te agradezco, pero ya me has respondido. Me retiraré pronto.",
						vb6.CStr(Declaraciones.Npclist[modCentinela.Centinela[CentinelaIndex].NpcIndex].Char.CharIndex),
						vbGreen);
			}

		} else {
			Protocol.WriteChatOverHead(UserIndex, "No es a ti a quien estoy hablando, ¿No ves?",
					Declaraciones.Npclist[modCentinela.Centinela[CentinelaIndex].NpcIndex].Char.CharIndex, 0x00ffffff);
		}

	}

	static void PasarMinutoCentinela() {
		/* '************************************************* */
		/* 'Author: Unknown */
		/* 'Last modified: 02/10/2010 */
		/* 'Control del timer. Llamado cada un minuto. */
		/*
		 * '03/10/2010: ZaMa - Adaptado para que funcione mas de un centinela en
		 * paralelo.
		 */
		/* '************************************************* */
		/* FIXME: ON ERROR GOTO ErrHandler */

		int index = 0;
		int UserIndex = 0;
		int IdleCount = 0;

		if (!modCentinela.centinelaActivado) {
			return;
		}

		/* ' Primero reviso los que estan chequeando usuarios */
		for (index = (1); index <= (modCentinela.NRO_CENTINELA); index++) {

			/* ' Esta activo? */
			if (modCentinela.Centinela[index].Activo) {
				modCentinela.Centinela[index].TiempoRestante = modCentinela.Centinela[index].TiempoRestante - 1;

				/* ' Temrino el tiempo de chequeo? */
				if (modCentinela.Centinela[index].TiempoRestante == 0) {
					CentinelaFinalCheck(index);
				} else {

					UserIndex = modCentinela.Centinela[index].RevisandoUserIndex;

					/* 'RECORDamos al user que debe escribir */
					if (Matematicas.Distancia(Declaraciones.Npclist[modCentinela.Centinela[index].NpcIndex].Pos,
							Declaraciones.UserList[UserIndex].Pos) > 5) {
						WarpCentinela(UserIndex, index);
					}

					/*
					 * 'El centinela habla y se manda a consola para que no
					 * quepan dudas
					 */
					Protocol.WriteChatOverHead(UserIndex,
							"¡" + Declaraciones.UserList[UserIndex].Name
									+ ", tienes un minuto más para responder! Debes escribir /CENTINELA "
									+ modCentinela.Centinela[index].clave + ".",
							vb6.CStr(Declaraciones.Npclist[modCentinela.Centinela[index].NpcIndex].Char.CharIndex),
							vbRed);
					Protocol.WriteConsoleMsg(UserIndex,
							"¡" + Declaraciones.UserList[UserIndex].Name + ", tienes un minuto más para responder!",
							FontTypeNames.FONTTYPE_CENTINELA);
					Protocol.FlushBuffer(UserIndex);
				}
			} else {

				/*
				 * ' Lo reseteo aca, para que pueda hablarle al usuario
				 * chequeado aunque haya respondido bien.
				 */
				if (modCentinela.Centinela[index].NpcIndex != 0) {
					if (modCentinela.Centinela[index].RevisandoUserIndex != 0) {
						Declaraciones.UserList[modCentinela.Centinela[index].RevisandoUserIndex].flags.CentinelaIndex = 0;
						modCentinela.Centinela[index].RevisandoUserIndex = 0;
					}
					NPCs.QuitarNPC(modCentinela.Centinela[index].NpcIndex);
					modCentinela.Centinela[index].NpcIndex = 0;
				}

				IdleCount = IdleCount + 1;
			}

		}

		/* 'Verificamos si ya debemos resetear la lista */
		int TActual = 0;
		TActual = Declaraciones.GetTickCount() && 0x7FFFFFFF;

		if (modNuevoTimer.checkInterval(centinelaStartTime, TActual, centinelaInterval)) {
			/* ' Espero a que terminen de controlar todos los centinelas */
			DetenerAsignacion = true;
		}

		/*
		 * ' Si hay algun centinela libre, se fija si no hay trabajadores
		 * disponibles para chequear
		 */
		if (IdleCount != 0) {

			/*
			 * ' Si es tiempo de resetear flags, chequeo que no quede nadie
			 * activo
			 */
			if (DetenerAsignacion) {

				/* ' No se completaron los ultimos chequeos */
				if (IdleCount < modCentinela.NRO_CENTINELA) {
					return;
				}

				/* ' Resetea todos los flags */
				ResetCentinelaInfo();
				DetenerAsignacion = false;

				/* ' Renuevo el contador de reseteo */
				ARenovarResetTimer();

			}

			GoToNextWorkingChar();

		}

		return;
		/* FIXME: ErrHandler : */
		General.LogError("Error en PasarMinutoCentinela. Error: " + Err.Number + " - " + Err.description);
	}

	static void WarpCentinela(int UserIndex, int CentinelaIndex) {
		/* '************************************************* */
		/* 'Author: Unknown */
		/* 'Last modified: 02/10/2010 */
		/* 'Inciamos la revisión del usuario UserIndex */
		/*
		 * '02/10/2010: ZaMa - Adaptado para que funcione mas de un centinela en
		 * paralelo.
		 */
		/* '************************************************* */

		/* 'Evitamos conflictos de índices */
		if (modCentinela.Centinela[CentinelaIndex].NpcIndex != 0) {
			NPCs.QuitarNPC(modCentinela.Centinela[CentinelaIndex].NpcIndex);
			modCentinela.Centinela[CentinelaIndex].NpcIndex = 0;
		}

		/* ' Spawn it */
		modCentinela.Centinela[CentinelaIndex].NpcIndex = NPCs.SpawnNpc(modCentinela.NPC_CENTINELA,
				Declaraciones.UserList[UserIndex].Pos, true, false);

		/* 'Si no pudimos crear el NPC, seguimos esperando a poder hacerlo */
		if (modCentinela.Centinela[CentinelaIndex].NpcIndex == 0) {
			modCentinela.Centinela[CentinelaIndex].RevisandoUserIndex = 0;
			modCentinela.Centinela[CentinelaIndex].Activo = false;
		}

	}

	static void CentinelaUserLogout(int CentinelaIndex) {
		/* '************************************************* */
		/* 'Author: Unknown */
		/* 'Last modified: 02/11/2010 */
		/* 'El usuario al que revisabamos se desconectó */
		/*
		 * '02/10/2010: ZaMa - Adaptado para que funcione mas de un centinela en
		 * paralelo.
		 */
		/*
		 * '02/11/2010: ZaMa - Ahora no loguea que el usuario cerro si puso bien
		 * la clave.
		 */
		/* '************************************************* */

		if (modCentinela.Centinela[CentinelaIndex].RevisandoUserIndex != 0) {

			/* 'Logueamos el evento */
			if (!Declaraciones.UserList[modCentinela.Centinela[CentinelaIndex].RevisandoUserIndex].flags.CentinelaOK) {
				LogCentinela("El usuario "
						+ Declaraciones.UserList[modCentinela.Centinela[CentinelaIndex].RevisandoUserIndex].Name
						+ " se desolgueó al pedirsele la contraseña.");
			}

			/*
			 * 'Reseteamos y esperamos a otro PasarMinuto para ir al siguiente
			 * user
			 */
			modCentinela.Centinela[CentinelaIndex].clave = 0;
			modCentinela.Centinela[CentinelaIndex].TiempoRestante = 0;
			modCentinela.Centinela[CentinelaIndex].RevisandoUserIndex = 0;
			modCentinela.Centinela[CentinelaIndex].Activo = false;

			if (modCentinela.Centinela[CentinelaIndex].NpcIndex != 0) {
				NPCs.QuitarNPC(modCentinela.Centinela[CentinelaIndex].NpcIndex);
				modCentinela.Centinela[CentinelaIndex].NpcIndex = 0;
			}

		}

	}

	static void LogCentinela(String texto) {
		/* '************************************************* */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last modified: 03/15/2006 */
		/* 'Loguea un evento del centinela */
		/* '************************************************* */
		/* FIXME: ON ERROR GOTO ErrHandler */

		int nfile = 0;
		/* ' obtenemos un canal */
		nfile = vb6.FreeFile();

		/*
		 * FIXME: OPEN App . Path & "\\logs\\Centinela.log" FOR Append Shared AS
		 * # nfile
		 */
		/* FIXME: PRINT # nfile , Date & " " & time & " " & texto */
		/* FIXME: CLOSE # nfile */
		return;

		/* FIXME: ErrHandler : */
	}

	static void ResetCentinelas() {
		/* '************************************************* */
		/* 'Author: ZaMa */
		/* 'Last modified: 02/10/2010 */
		/* 'Resetea todos los centinelas */
		/* '************************************************* */
		int index = 0;
		int UserIndex = 0;

		for (index = (vb6.LBound(modCentinela.Centinela)); index <= (vb6.UBound(modCentinela.Centinela)); index++) {

			/* ' Si esta activo, reseteo toda la info y quito el npc */
			if (modCentinela.Centinela[index].Activo) {

				modCentinela.Centinela[index].Activo = false;

				UserIndex = modCentinela.Centinela[index].RevisandoUserIndex;
				if (UserIndex != 0) {
					Declaraciones.UserList[UserIndex].flags.CentinelaIndex = 0;
					Declaraciones.UserList[UserIndex].flags.CentinelaOK = false;
					modCentinela.Centinela[index].RevisandoUserIndex = 0;
				}

				modCentinela.Centinela[index].clave = 0;
				modCentinela.Centinela[index].TiempoRestante = 0;

				if (modCentinela.Centinela[index].NpcIndex != 0) {
					NPCs.QuitarNPC(modCentinela.Centinela[index].NpcIndex);
					modCentinela.Centinela[index].NpcIndex = 0;
				}

			}

		}

		DetenerAsignacion = false;
		ARenovarResetTimer();

	}

	static int EsCentinela(int NpcIndex) {
		int retval = 0;
		/* '************************************************* */
		/* 'Author: ZaMa */
		/* 'Last modified: 07/10/2010 */
		/* 'Devuelve True si el indice pertenece a un centinela. */
		/* '************************************************* */

		int index = 0;

		if (NpcIndex == 0) {
			return retval;
		}

		for (index = (1); index <= (modCentinela.NRO_CENTINELA); index++) {

			if (modCentinela.Centinela[index].NpcIndex == NpcIndex) {
				retval = index;
				return retval;
			}

		}

		return retval;
	}

	static void RenovarResetTimer() {
		/* '************************************************* */
		/* 'Author: ZaMa */
		/* 'Last modified: 07/10/2010 */
		/*
		 * 'Renueva el timer que resetea el flag "CentinelaOk" de todos los
		 * usuarios.
		 */
		/* '************************************************* */
		centinelaInterval = (Matematicas.RandomNumber(0, modCentinela.TIEMPO_PASAR_RANDOM)
				+ modCentinela.TIEMPO_PASAR_BASE) * 60 * 1000;
	}

}