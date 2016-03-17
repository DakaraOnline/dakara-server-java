/*  AUTOMATICALLY CONVERTED FILE  */

/* 
 * Este archivo fue convertido automaticamente, por un script, desde el 
 * código fuente original de Visual Basic 6.
 */

/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"frmMain"')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_GlobalNameSpace'), (5, '='), (1, 'False')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Creatable'), (5, '='), (1, 'False')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_PredeclaredId'), (5, '='), (1, 'True')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Exposed'), (5, '='), (1, 'False')] */
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

public class frmMain {

	public static int ESCUCHADAS;

	static public class NOTIFYICONDATA {
		public int cbSize;
		public int hWnd;
		public int uID;
		public int uFlags;
		public int uCallbackMessage;
		public int hIcon;
		public String szTip;
	}

	private static int iDay;

	static final int NIM_ADD = 0;
	static final int NIM_DELETE = 2;
	static final int NIF_MESSAGE = 1;
	static final int NIF_ICON = 2;
	static final int NIF_TIP = 4;

	static final int WM_MOUSEMOVE = 0x200;
	static final int WM_LBUTTONDBLCLK = 0x203;
	static final int WM_RBUTTONUP = 0x205;

	public static NOTIFYICONDATA setNOTIFYICONDATA(int hWnd, int ID, int flags, int CallbackMessage, int Icon,
			String Tip) {
		NOTIFYICONDATA retval;
		NOTIFYICONDATA nidTemp;

		nidTemp.cbSize = vb6.Len(nidTemp);
		nidTemp.hWnd = hWnd;
		nidTemp.uID = ID;
		nidTemp.uFlags = flags;
		nidTemp.uCallbackMessage = CallbackMessage;
		nidTemp.hIcon = Icon;
		nidTemp.szTip = Tip + vb6.Chr(0);

		retval = nidTemp;
		return retval;
	}

	public static void CheckIdleUser() {
		int iUserIndex = 0;

		for (iUserIndex = (1); iUserIndex <= (Declaraciones.MaxUsers); iUserIndex++) {
			/* 'Conexion activa? y es un usuario loggeado? */
			if (Declaraciones.UserList[iUserIndex].ConnID != -1
					&& Declaraciones.UserList[iUserIndex].flags.UserLogged) {
				/* 'Actualiza el contador de inactividad */
				if (Declaraciones.UserList[iUserIndex].flags.Traveling == 0) {
					Declaraciones.UserList[iUserIndex].Counters.IdleCount = Declaraciones.UserList[iUserIndex].Counters.IdleCount
							+ 1;
				}

				if (Declaraciones.UserList[iUserIndex].Counters.IdleCount >= Declaraciones.IdleLimit) {
					Protocol.WriteShowMessageBox(iUserIndex, "Demasiado tiempo inactivo. Has sido desconectado.");
					/* 'mato los comercios seguros */
					if (Declaraciones.UserList[iUserIndex].ComUsu.DestUsu > 0) {
						if (Declaraciones.UserList[Declaraciones.UserList[iUserIndex].ComUsu.DestUsu].flags.UserLogged) {
							if (Declaraciones.UserList[Declaraciones.UserList[iUserIndex].ComUsu.DestUsu].ComUsu.DestUsu == iUserIndex) {
								Protocol.WriteConsoleMsg(Declaraciones.UserList[iUserIndex].ComUsu.DestUsu,
										"Comercio cancelado por el otro usuario.", FontTypeNames.FONTTYPE_TALK);
								mdlCOmercioConUsuario
										.FinComerciarUsu(Declaraciones.UserList[iUserIndex].ComUsu.DestUsu);
								/*
								 * 'flush the buffer to send the message right
								 * away
								 */
								Protocol.FlushBuffer(Declaraciones.UserList[iUserIndex].ComUsu.DestUsu);
							}
						}
						mdlCOmercioConUsuario.FinComerciarUsu(iUserIndex);
					}
					UsUaRiOs.Cerrar_Usuario(iUserIndex);
				}
			}
		}
	}

	public static void Auditoria_Timer() {
 /* FIXME: ON ERROR GOTO errhand */
 Static(centinelSecsASByte);
 
 centinelSecs = centinelSecs+1;
 
  if (centinelSecs == 5) {
  /* 'Every 5 seconds, we try to call the player's attention so it will report the code. */
  modCentinela.CallUserAttention;
  
  centinelSecs = 0;
 }
 
 /* 'sistema de desconexion de 10 segs */
 General.PasarSegundo();
 
 Admin.ActualizaEstadisticasWeb();
 
 return;
 
 /* FIXME: errhand : */
 
 General.LogError("Error en Timer Auditoria. Err: " + Err.description + " - " + Err.Number);
 /* FIXME: RESUME NEXT */
 
}

	public static void UpdateNpcsExp(float Multiplicador) {

		int NpcIndex = 0;
		for (NpcIndex = (1); NpcIndex <= (Declaraciones.LastNPC); NpcIndex++) {
			Declaraciones.Npclist[NpcIndex].GiveEXP = Declaraciones.Npclist[NpcIndex].GiveEXP * Multiplicador;
			Declaraciones.Npclist[NpcIndex].flags.ExpCount = Declaraciones.Npclist[NpcIndex].flags.ExpCount
					* Multiplicador;
		}
	}

	public static void AutoSave_Timer() {
 
 /* FIXME: ON ERROR GOTO ErrHandler */
 /* 'fired every minute */
 Static(Declaraciones.MinutosASLong);
 Static(MinutosLatsCleanASLong);
 Static(MinsPjesSaveASLong);
 Static(MinsSendMotdASLong);
 
 Declaraciones.Minutos = Declaraciones.Minutos+1;
 MinsPjesSave = MinsPjesSave+1;
 MinsSendMotd = MinsSendMotd+1;
 
 double tmpHappyHour = 0.0;
 
 /* ' HappyHour */
 iDay = vb6.Weekday(Date);
 tmpHappyHour = Declaraciones.HappyHourDays[iDay];
 
  if (tmpHappyHour != Declaraciones.HappyHour) {
  
   if (Declaraciones.HappyHourActivated) {
   /* ' Reestablece la exp de los npcs */
   if (Declaraciones.HappyHour != 0) {
   UpdateNpcsExp(1 / (double) Declaraciones.HappyHour);
   }
  }
  
  /* ' Desactiva */
   if (tmpHappyHour == 1) {
   modSendData.SendData(SendTarget.ToAll, 0, Protocol.PrepareMessageConsoleMsg("¡Ha concluido la Happy Hour!", FontTypeNames.FONTTYPE_DIOS));
   Declaraciones.HappyHourActivated = false;
   
   /* ' Activa */
   } else {
   UpdateNpcsExp(tmpHappyHour);
   
    if (Declaraciones.HappyHour != 1) {
    modSendData.SendData(SendTarget.ToAll, 0, Protocol.PrepareMessageConsoleMsg("Se ha modificado la Happy Hour, a partir de ahora las criaturas aumentan su experiencia en un " + vb6.Round((tmpHappyHour-1)*100, 2) + "%", FontTypeNames.FONTTYPE_DIOS));
    } else {
    modSendData.SendData(SendTarget.ToAll, 0, Protocol.PrepareMessageConsoleMsg("¡Ha comenzado la Happy Hour! ¡Las criaturas aumentan su experiencia en un " + vb6.Round((tmpHappyHour-1)*100, 2) + "%!", FontTypeNames.FONTTYPE_DIOS));
   }
   
   Declaraciones.HappyHourActivated = true;
  }
  
  Declaraciones.HappyHour = tmpHappyHour;
 }
 
 /* '¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿ */
 ModAreas.AreasOptimizacion;
 /* '¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿ */
 
 /* 'Actualizamos el centinela */
 modCentinela.PasarMinutoCentinela;
 
  if (Declaraciones.Minutos == Admin.MinutosWs-1) {
  modSendData.SendData(SendTarget.ToAll, 0, Protocol.PrepareMessageConsoleMsg("Worldsave en 1 minuto ...", FontTypeNames.FONTTYPE_VENENO));
 }
 
  if (Declaraciones.Minutos>=Admin.MinutosWs) {
  ES.DoBackUp;
  Declaraciones.aClon.VaciarColeccion();
  Declaraciones.Minutos = 0;
 }
 
  if (MinsPjesSave == Admin.MinutosGuardarUsuarios-1) {
  modSendData.SendData(SendTarget.ToAll, 0, Protocol.PrepareMessageConsoleMsg("CharSave en 1 minuto ...", FontTypeNames.FONTTYPE_VENENO));
  } else if (MinsPjesSave>=Admin.MinutosGuardarUsuarios) {
  mdParty.ActualizaExperiencias;
  General.GuardarUsuarios();
  MinsPjesSave = 0;
 }
 
  if (MinutosLatsClean>=15) {
  MinutosLatsClean = 0;
  /* 'respawn de los guardias en las pos originales */
  Admin.ReSpawnOrigPosNpcs();
  General.LimpiarMundo();
  } else {
  MinutosLatsClean = MinutosLatsClean+1;
 }
 
  if (MinsSendMotd>=Admin.MinutosMotd) {
  int i = 0;
   for (i = (1); i <= (Declaraciones.LastUser); i++) {
    if (Declaraciones.UserList[i].flags.UserLogged) {
    TCP.SendMOTD(i);
   }
  }
  MinsSendMotd = 0;
 }
 
 Admin.PurgarPenas();
 CheckIdleUser();
 
 /* '<<<<<-------- Log the number of users online ------>>> */
 int N = 0;
 N = vb6.FreeFile();
 /* FIXME: OPEN App . Path & "\\logs\\numusers.log" FOR OUTPUT Shared AS N */
 /* FIXME: PRINT # N , NumUsers */
 /* FIXME: CLOSE # N */
 /* '<<<<<-------- Log the number of users online ------>>> */
 
 return;
 /* FIXME: ErrHandler : */
 General.LogError("Error en TimerAutoSave " + Err.Number + ": " + Err.description);
 /* FIXME: RESUME NEXT */
}

	public static void chkServerHabilitado_Click() {
		Declaraciones.ServerSoloGMs = chkServerHabilitado.value;
	}

	public static void cmdCerrarServer_Click() {
  if (vb6.MsgBox("¡¡Atencion!! Si cierra el servidor puede provocar la perdida de datos. " + "¿Desea hacerlo de todas maneras?", vbYesNo) == vbYes) {
  
  Object f;
   for (f : Forms) {
   Unload(f);
  }
 }
}

	public static void cmdConfiguracion_Click() {
		frmServidor.Visible = true;
	}

	public static void CMDDUMP_Click() {
		/* FIXME: ON ERROR RESUME NEXT */

		int i = 0;
		for (i = (1); i <= (Declaraciones.MaxUsers); i++) {
			General.LogCriticEvent(i + ") ConnID: " + Declaraciones.UserList[i].ConnID + ". ConnidValida: "
					+ Declaraciones.UserList[i].ConnIDValida + " Name: " + Declaraciones.UserList[i].Name
					+ " UserLogged: " + Declaraciones.UserList[i].flags.UserLogged);
		}

		General.LogCriticEvent("Lastuser: " + Declaraciones.LastUser + " NextOpenUser: " + UsUaRiOs.NextOpenUser());

	}

	public static void cmdSystray_Click() {
		ASetSystray();
	}

	public static void Command1_Click() {
		modSendData.SendData(SendTarget.ToAll, 0, Protocol.PrepareMessageShowMessageBox(BroadMsg.Text));
		/* ''''''''''''''''SOLO PARA EL TESTEO''''''' */
		/* ''''''''''SE USA PARA COMUNICARSE CON EL SERVER''''''''''' */
		txtChat.Text = txtChat.Text + vbNewLine + "Servidor> " + BroadMsg.Text;
	}

	public static void InitMain(int f) {

		if (f == 1) {
			SetSystray();
		} else {
			frmMain.Show();
		}

	}

	public static void Command2_Click() {
		modSendData.SendData(SendTarget.ToAll, 0,
				Protocol.PrepareMessageConsoleMsg("Servidor> " + BroadMsg.Text, FontTypeNames.FONTTYPE_SERVER));
		/* ''''''''''''''''SOLO PARA EL TESTEO''''''' */
		/* ''''''''''SE USA PARA COMUNICARSE CON EL SERVER''''''''''' */
		txtChat.Text = txtChat.Text + vbNewLine + "Servidor> " + BroadMsg.Text;
	}

	public static void Form_MouseMove(int Button, int Shift, float X, float Y) {
		/* FIXME: ON ERROR RESUME NEXT */

		if (! /* FIXME */Visible) {
			switch (X / Screen.TwipsPerPixelX) {

			case WM_LBUTTONDBLCLK:
				WindowState = 0;
				Visible = true;
				int hProcess = 0;
				GetWindowThreadProcessId(hWnd, hProcess);
				AppActivate(hProcess);
				break;

			case WM_RBUTTONUP:
				SysTray.hHook = SysTray.SetWindowsHookEx(SysTray.WH_CALLWNDPROC, AddressOfSysTray.AppHook(),
						vb6.App.Instance().hInstance, vb6.App.Instance().ThreadID);
				PopupMenu(mnuPopUp);
				if (SysTray.hHook) {
					SysTray.UnhookWindowsHookEx(SysTray.hHook);
				}
				SysTray.hHook = 0;
				break;
			}
		}

	}

	public static void QuitarIconoSystray() {
		/* FIXME: ON ERROR RESUME NEXT */

		/* 'Borramos el icono del systray */
		int i = 0;
		NOTIFYICONDATA nid;

		nid = setNOTIFYICONDATA(frmMain.hWnd, vbNull, NIF_MESSAGE || NIF_ICON || NIF_TIP, vbNull, frmMain.Icon, "");

		i = Shell_NotifyIconA(NIM_DELETE, nid);

	}

	public static void Form_Unload(int Cancel) {
 /* FIXME: ON ERROR RESUME NEXT */
 
 /* 'Save stats!!! */
 Statistics.DumpStatistics;
 
 QuitarIconoSystray();
 
 /* # IF UsarQueSocket = 1 THEN */
 wskapiAO.LimpiaWsApi();
 /* # ELSEIF UsarQueSocket = 0 THEN */
 /* # ELSEIF UsarQueSocket = 2 THEN */
 /* # END IF */
 
 int LoopC = 0;
 
  for (LoopC = (1); LoopC <= (Declaraciones.MaxUsers); LoopC++) {
  if (Declaraciones.UserList[LoopC].ConnID != -1) {
  TCP.CloseSocket(LoopC);
  }
 }
 
 /* 'Log */
 int N = 0;
 N = vb6.FreeFile();
 /* FIXME: OPEN App . Path & "\\logs\\Main.log" FOR Append Shared AS # N */
 /* FIXME: PRINT # N , Date & " " & time & " server cerrado." */
 /* FIXME: CLOSE # N */
 
 /* FIXME: END */
 
 Declaraciones.SonidosMapas = null;
 
}

	public static void FX_Timer() {
		/* FIXME: ON ERROR GOTO hayerror */

		Declaraciones.SonidosMapas.ReproducirSonidosDeMapas();

		return;
		/* FIXME: hayerror : */

	}

	public static void GameTimer_Timer() {
		/* '******************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modify Date: - */
		/* '******************************************************** */
		int iUserIndex = 0;
		boolean bEnviarStats = false;
		boolean bEnviarAyS = false;

		/* FIXME: ON ERROR GOTO hayerror */

		/* '<<<<<< Procesa eventos de los usuarios >>>>>> */
		/* 'LastUser */
		for (iUserIndex = (1); iUserIndex <= (Declaraciones.MaxUsers); iUserIndex++) {
			/* 'Conexion activa? */
			if (Declaraciones.UserList[iUserIndex].ConnID != -1) {
				/* '¿User valido? */

				if (Declaraciones.UserList[iUserIndex].ConnIDValida
						&& Declaraciones.UserList[iUserIndex].flags.UserLogged) {

					/* '[Alejo-18-5] */
					bEnviarStats = false;
					bEnviarAyS = false;

					if (Declaraciones.UserList[iUserIndex].flags.Paralizado == 1) {
						General.EfectoParalisisUser(iUserIndex);
					}
					if (Declaraciones.UserList[iUserIndex].flags.Ceguera == 1
							|| Declaraciones.UserList[iUserIndex].flags.Estupidez) {
						General.EfectoCegueEstu(iUserIndex);
					}

					if (Declaraciones.UserList[iUserIndex].flags.Muerto == 0) {

						/* '[Consejeros] */
						if ((Declaraciones.UserList[iUserIndex].flags.Privilegios && PlayerType.User)) {
							General.EfectoLava(iUserIndex);
						}

						if (Declaraciones.UserList[iUserIndex].flags.Desnudo != 0
								&& (Declaraciones.UserList[iUserIndex].flags.Privilegios && PlayerType.User) != 0) {
							General.EfectoFrio(iUserIndex);
						}

						if (Declaraciones.UserList[iUserIndex].flags.Meditando) {
							Trabajo.DoMeditar(iUserIndex);
						}

						if (Declaraciones.UserList[iUserIndex].flags.Envenenado != 0
								&& (Declaraciones.UserList[iUserIndex].flags.Privilegios && PlayerType.User) != 0) {
							General.EfectoVeneno(iUserIndex);
						}

						if (Declaraciones.UserList[iUserIndex].flags.AdminInvisible != 1) {
							if (Declaraciones.UserList[iUserIndex].flags.invisible == 1) {
								General.EfectoInvisibilidad(iUserIndex);
							}
							if (Declaraciones.UserList[iUserIndex].flags.Oculto == 1) {
								Trabajo.DoPermanecerOculto(iUserIndex);
							}
						}

						if (Declaraciones.UserList[iUserIndex].flags.Mimetizado == 1) {
							General.EfectoMimetismo(iUserIndex);
						}

						if (Declaraciones.UserList[iUserIndex].flags.AtacablePor != 0) {
							General.EfectoEstadoAtacable(iUserIndex);
						}

						General.DuracionPociones(iUserIndex);

						General.HambreYSed(iUserIndex, bEnviarAyS);

						if (Declaraciones.UserList[iUserIndex].flags.Hambre == 0
								&& Declaraciones.UserList[iUserIndex].flags.Sed == 0) {
							if (Admin.Lloviendo) {
								if (! /* FIXME */General.Intemperie(iUserIndex)) {
									if (! /* FIXME */Declaraciones.UserList[iUserIndex].flags.Descansar) {
										/* 'No esta descansando */
										General.Sanar(iUserIndex, bEnviarStats, Admin.SanaIntervaloSinDescansar);
										if (bEnviarStats) {
											Protocol.WriteUpdateHP(iUserIndex);
											bEnviarStats = false;
										}
										General.RecStamina(iUserIndex, bEnviarStats,
												Admin.StaminaIntervaloSinDescansar);
										if (bEnviarStats) {
											Protocol.WriteUpdateSta(iUserIndex);
											bEnviarStats = false;
										}
									} else {
										/* 'esta descansando */
										General.Sanar(iUserIndex, bEnviarStats, Admin.SanaIntervaloDescansar);
										if (bEnviarStats) {
											Protocol.WriteUpdateHP(iUserIndex);
											bEnviarStats = false;
										}
										General.RecStamina(iUserIndex, bEnviarStats, Admin.StaminaIntervaloDescansar);
										if (bEnviarStats) {
											Protocol.WriteUpdateSta(iUserIndex);
											bEnviarStats = false;
										}
										/*
										 * 'termina de descansar automaticamente
										 */
										if (Declaraciones.UserList[iUserIndex].Stats.MaxHp == Declaraciones.UserList[iUserIndex].Stats.MinHp
												&& Declaraciones.UserList[iUserIndex].Stats.MaxSta == Declaraciones.UserList[iUserIndex].Stats.MinSta) {
											Protocol.WriteRestOK(iUserIndex);
											Protocol.WriteConsoleMsg(iUserIndex, "Has terminado de descansar.",
													FontTypeNames.FONTTYPE_INFO);
											Declaraciones.UserList[iUserIndex].flags.Descansar = false;
										}

									}
								}
							} else {
								if (! /* FIXME */Declaraciones.UserList[iUserIndex].flags.Descansar) {
									/* 'No esta descansando */

									General.Sanar(iUserIndex, bEnviarStats, Admin.SanaIntervaloSinDescansar);
									if (bEnviarStats) {
										Protocol.WriteUpdateHP(iUserIndex);
										bEnviarStats = false;
									}
									General.RecStamina(iUserIndex, bEnviarStats, Admin.StaminaIntervaloSinDescansar);
									if (bEnviarStats) {
										Protocol.WriteUpdateSta(iUserIndex);
										bEnviarStats = false;
									}

								} else {
									/* 'esta descansando */

									General.Sanar(iUserIndex, bEnviarStats, Admin.SanaIntervaloDescansar);
									if (bEnviarStats) {
										Protocol.WriteUpdateHP(iUserIndex);
										bEnviarStats = false;
									}
									General.RecStamina(iUserIndex, bEnviarStats, Admin.StaminaIntervaloDescansar);
									if (bEnviarStats) {
										Protocol.WriteUpdateSta(iUserIndex);
										bEnviarStats = false;
									}
									/* 'termina de descansar automaticamente */
									if (Declaraciones.UserList[iUserIndex].Stats.MaxHp == Declaraciones.UserList[iUserIndex].Stats.MinHp
											&& Declaraciones.UserList[iUserIndex].Stats.MaxSta == Declaraciones.UserList[iUserIndex].Stats.MinSta) {
										Protocol.WriteRestOK(iUserIndex);
										Protocol.WriteConsoleMsg(iUserIndex, "Has terminado de descansar.",
												FontTypeNames.FONTTYPE_INFO);
										Declaraciones.UserList[iUserIndex].flags.Descansar = false;
									}

								}
							}
						}

						if (bEnviarAyS) {
							Protocol.WriteUpdateHungerAndThirst(iUserIndex);
						}

						if (Declaraciones.UserList[iUserIndex].NroMascotas > 0) {
							General.TiempoInvocacion(iUserIndex);
						}
					} else {
						if (Declaraciones.UserList[iUserIndex].flags.Traveling != 0) {
							General.TravelingEffect(iUserIndex);
						}
						/* 'Muerto */
					}
					/* 'no esta logeado? */
				} else {
					/* 'Inactive players will be removed! */
					Declaraciones.UserList[iUserIndex].Counters.IdleCount = Declaraciones.UserList[iUserIndex].Counters.IdleCount
							+ 1;
					if (Declaraciones.UserList[iUserIndex].Counters.IdleCount > Admin.IntervaloParaConexion) {
						Declaraciones.UserList[iUserIndex].Counters.IdleCount = 0;
						TCP.CloseSocket(iUserIndex);
					}
					/* 'UserLogged */
				}

				/* 'If there is anything to be sent, we send it */
				Protocol.FlushBuffer(iUserIndex);
			}
		}
		return;

		/* FIXME: hayerror : */
		General.LogError("Error en GameTimer: " + Err.description + " UserIndex = " + iUserIndex);
	}

	public static void mnusalir_Click() {
		cmdCerrarServer_Click();
	}

	public static void mnuMostrar_Click() {
		/* FIXME: ON ERROR RESUME NEXT */
		WindowState = 0;
		Form_MouseMove(0, 0, 7725, 0);
	}

	public static void KillLog_Timer() {
		/* FIXME: ON ERROR RESUME NEXT */
		if (General.FileExist(vb6.App.Instance().Path + "\\logs\\connect.log", 0)) {
			/* FIXME: KILL App . Path & "\\logs\\connect.log" */
		}
		if (General.FileExist(vb6.App.Instance().Path + "\\logs\\haciendo.log", 0)) {
			/* FIXME: KILL App . Path & "\\logs\\haciendo.log" */
		}
		if (General.FileExist(vb6.App.Instance().Path + "\\logs\\stats.log", 0)) {
			/* FIXME: KILL App . Path & "\\logs\\stats.log" */
		}
		if (General.FileExist(vb6.App.Instance().Path + "\\logs\\Asesinatos.log", 0)) {
			/* FIXME: KILL App . Path & "\\logs\\Asesinatos.log" */
		}
		if (General.FileExist(vb6.App.Instance().Path + "\\logs\\HackAttemps.log", 0)) {
			/* FIXME: KILL App . Path & "\\logs\\HackAttemps.log" */
		}
		if (! /* FIXME */General.FileExist(vb6.App.Instance().Path + "\\logs\\nokillwsapi.txt")) {
			if (General.FileExist(vb6.App.Instance().Path + "\\logs\\wsapi.log", 0)) {
				/* FIXME: KILL App . Path & "\\logs\\wsapi.log" */
			}
		}

	}

	public static void SetSystray() {

		int i = 0;
		String S;
		NOTIFYICONDATA nid;

		S = "ARGENTUM-ONLINE";
		nid = setNOTIFYICONDATA(frmMain.hWnd, vbNull, NIF_MESSAGE || NIF_ICON || NIF_TIP, WM_MOUSEMOVE, frmMain.Icon,
				S);
		i = Shell_NotifyIconA(NIM_ADD, nid);

		if (WindowState != vbMinimized) {
			WindowState = vbMinimized;
		}
		Visible = false;

	}

	public static void npcataca_Timer() {

		/* FIXME: ON ERROR RESUME NEXT */
		int npc = 0;

		for (npc = (1); npc <= (Declaraciones.LastNPC); npc++) {
			Declaraciones.Npclist[npc].CanAttack = 1;
		}

	}

	public static void packetResend_Timer() {
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 04/01/07 */
		/* 'Attempts to resend to the user all data that may be enqueued. */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		int i = 0;

		for (i = (1); i <= (Declaraciones.MaxUsers); i++) {
			if (Declaraciones.UserList[i].ConnIDValida) {
				if (Declaraciones.UserList[i].outgoingData.length > 0) {
					TCP.EnviarDatosASlot(i, Declaraciones.UserList[i].outgoingData
							.ReadASCIIStringFixed(Declaraciones.UserList[i].outgoingData.length));
				}
			}
		}

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en packetResend - Error: " + Err.Number + " - Desc: " + Err.description);
		/* FIXME: RESUME NEXT */
	}

	public static void securityTimer_Timer() {

		/* # IF SeguridadAlkon THEN */
		/* # END IF */

	}

	public static void TIMER_AI_Timer() {

		/* FIXME: ON ERROR GOTO ErrorHandler */
		int NpcIndex = 0;
		int mapa = 0;
		int e_p = 0;

		/* 'Barrin 29/9/03 */
		if (! /* FIXME */Declaraciones.haciendoBK && ! /* FIXME */Declaraciones.EnPausa) {
			/* 'Update NPCs */
			for (NpcIndex = (1); NpcIndex <= (Declaraciones.LastNPC); NpcIndex++) {

				/* 'Nos aseguramos que sea INTELIGENTE! */
				if (Declaraciones.Npclist[NpcIndex].flags.NPCActive) {

					/* ' Chequea si contiua teniendo dueño */
					if (Declaraciones.Npclist[NpcIndex].Owner > 0) {
						NPCs.ValidarPermanenciaNpc(NpcIndex);
					}

					if (Declaraciones.Npclist[NpcIndex].flags.Paralizado == 1) {
						General.EfectoParalisisNpc(NpcIndex);
					} else {
						/* ' Preto? Tienen ai especial */
						if (Declaraciones.Npclist[NpcIndex].NPCtype == eNPCType.Pretoriano) {
							Declaraciones.ClanPretoriano[Declaraciones.Npclist[NpcIndex].ClanIndex]
									.PerformPretorianAI(NpcIndex);
						} else {
							/* 'Usamos AI si hay algun user en el mapa */
							if (Declaraciones.Npclist[NpcIndex].flags.Inmovilizado == 1) {
								General.EfectoParalisisNpc(NpcIndex);
							}

							mapa = Declaraciones.Npclist[NpcIndex].Pos.Map;

							if (mapa > 0) {
								if (Declaraciones.MapInfo[mapa].NumUsers > 0) {
									if (Declaraciones.Npclist[NpcIndex].Movement != TipoAI.ESTATICO) {
										AI.NPCAI(NpcIndex);
									}
								}
							}
						}
					}
				}
			}
		}

		return;

		/* FIXME: ErrorHandler : */
		General.LogError("Error en TIMER_AI_Timer " + Declaraciones.Npclist[NpcIndex].Name + " mapa:"
				+ Declaraciones.Npclist[NpcIndex].Pos.Map);
		NPCs.MuereNpc(NpcIndex, 0);
	}

	public static void tLluvia_Timer() {
		/* FIXME: ON ERROR GOTO ErrHandler */

		/* ' [TEMPORAL] */
		return;

		int iCount = 0;
		if (Admin.Lloviendo) {
			for (iCount = (1); iCount <= (Declaraciones.LastUser); iCount++) {
				General.EfectoLluvia(iCount);
			}
		}

		return;
		/* FIXME: ErrHandler : */
		General.LogError("tLluvia " + Err.Number + ": " + Err.description);
	}

	public static void tLluviaEvent_Timer() {

		/* FIXME: ON ERROR GOTO ErrorHandler */
		Static(MinutosLloviendoASLong);
		Static(MinutosSinLluviaASLong);

		if (! /* FIXME */Admin.Lloviendo) {
			MinutosSinLluvia = MinutosSinLluvia + 1;
			if (MinutosSinLluvia >= 15 && MinutosSinLluvia < 1440) {
				if (Matematicas.RandomNumber(1, 100) <= 2) {
					Admin.Lloviendo = true;
					MinutosSinLluvia = 0;
					modSendData.SendData(SendTarget.ToAll, 0, Protocol.PrepareMessageRainToggle());
				}
			} else if (MinutosSinLluvia >= 1440) {
				Admin.Lloviendo = true;
				MinutosSinLluvia = 0;
				modSendData.SendData(SendTarget.ToAll, 0, Protocol.PrepareMessageRainToggle());
			}
		} else {
			MinutosLloviendo = MinutosLloviendo + 1;
			if (MinutosLloviendo >= 5) {
				Admin.Lloviendo = false;
				modSendData.SendData(SendTarget.ToAll, 0, Protocol.PrepareMessageRainToggle());
				MinutosLloviendo = 0;
			} else {
				if (Matematicas.RandomNumber(1, 100) <= 2) {
					Admin.Lloviendo = false;
					MinutosLloviendo = 0;
					modSendData.SendData(SendTarget.ToAll, 0, Protocol.PrepareMessageRainToggle());
				}
			}
		}

		return;
		/* FIXME: ErrorHandler : */
		General.LogError("Error tLluviaTimer");

	}

	public static void tPiqueteC_Timer() {
		boolean NuevaA = false;
		/* ' Dim NuevoL As Boolean */
		int GI = 0;
		int i = 0;

		/* FIXME: ON ERROR GOTO ErrHandler */
		for (i = (1); i <= (Declaraciones.LastUser); i++) {
			if (Declaraciones.UserList[i].flags.UserLogged) {
				if (Declaraciones.MapData[Declaraciones.UserList[i].Pos.Map][Declaraciones.UserList[i].Pos.X][Declaraciones.UserList[i].Pos.Y].trigger == eTrigger.ANTIPIQUETE) {
					if (Declaraciones.UserList[i].flags.Muerto == 0) {
						Declaraciones.UserList[i].Counters.PiqueteC = Declaraciones.UserList[i].Counters.PiqueteC + 1;
						Protocol.WriteConsoleMsg(i,
								"¡¡¡Estás obstruyendo la vía pública, muévete o serás encarcelado!!!",
								FontTypeNames.FONTTYPE_INFO);

						if (Declaraciones.UserList[i].Counters.PiqueteC > 23) {
							Declaraciones.UserList[i].Counters.PiqueteC = 0;
							Admin.Encarcelar(i, Declaraciones.TIEMPO_CARCEL_PIQUETE);
						}
					} else {
						Declaraciones.UserList[i].Counters.PiqueteC = 0;
					}
				} else {
					Declaraciones.UserList[i].Counters.PiqueteC = 0;
				}

				/* 'ustedes se preguntaran que hace esto aca? */
				/*
				 * 'bueno la respuesta es simple: el codigo de AO es una mierda
				 * y encontrar
				 */
				/*
				 * 'todos los puntos en los cuales la alineacion puede cambiar
				 * es un dolor de
				 */
				/*
				 * 'huevos, asi que lo controlo aca, cada 6 segundos, lo cual es
				 * razonable
				 */

				GI = Declaraciones.UserList[i].GuildIndex;
				if (GI > 0) {
					NuevaA = false;
					/* ' NuevoL = False */
					if (! /* FIXME */modGuilds.m_ValidarPermanencia(i, true, NuevaA)) {
						Protocol.WriteConsoleMsg(i,
								"Has sido expulsado del clan. ¡El clan ha sumado un punto de antifacción!",
								FontTypeNames.FONTTYPE_GUILD);
					}
					if (NuevaA) {
						modSendData.SendData(SendTarget.ToGuildMembers, GI,
								Protocol.PrepareMessageConsoleMsg(
										"¡El clan ha pasado a tener alineación " + modGuilds.GuildAlignment(GI) + "!",
										FontTypeNames.FONTTYPE_GUILD));
						General.LogClanes("¡El clan cambio de alineación!");
					}
					/* ' If NuevoL Then */
					/*
					 * ' Call SendData(SendTarget.ToGuildMembers, GI,
					 * PrepareMessageConsoleMsg("¡El clan tiene un nuevo líder!"
					 * , FontTypeNames.FONTTYPE_GUILD))
					 */
					/* ' Call LogClanes("¡El clan tiene nuevo lider!") */
					/* ' End If */
				}

				Protocol.FlushBuffer(i);
			}
		}
		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en tPiqueteC_Timer " + Err.Number + ": " + Err.description);
	}

	/* ''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''' */
	/* '''''''''''''''''USO DEL CONTROL TCPSERV''''''''''''''''''''''''''' */
	/* '''''''''''''Compilar con UsarQueSocket = 3'''''''''''''''''''''''' */
	/* ''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''' */

	/* # IF UsarQueSocket = 3 THEN */

	/* # END IF */
	/* ''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''' */
	/* ''''''''''''''FIN USO DEL CONTROL TCPSERV''''''''''''''''''''''''' */
	/* '''''''''''''Compilar con UsarQueSocket = 3'''''''''''''''''''''''' */
	/* ''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''' */

}