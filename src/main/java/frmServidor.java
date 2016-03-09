

/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"frmServidor"')] */
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

public class frmServidor {

	static void Form_Load() {

		/* # IF UsarQueSocket = 1 THEN */
		cmdResetSockets.Visible = true;
		cmdResetListen.Visible = true;
		/* # ELSEIF UsarQueSocket = 0 THEN */
		/* # ELSEIF UsarQueSocket = 2 THEN */
		/* # END IF */

	}

	static void cmdApagarServer_Click() {
 
 if (vb6.MsgBox("¿Está seguro que desea hacer WorldSave, guardar pjs y cerrar?", vbYesNo, "Apagar Magicamente") == vbNo) {
 return;
 }
 
 Me.MousePointer = 11;
 
 FrmStat.Show();
 
 /* 'WorldSave */
 ES.DoBackUp;
 
 /* 'commit experiencia */
 mdParty.ActualizaExperiencias;
 
 /* 'Guardar Pjs */
 General.GuardarUsuarios();
 
 /* 'Chauuu */
 Unload(frmMain);
 
}

	static void cmdCerrar_Click() {
		frmServidor.Visible = false;
	}

	static void cmdCharBackup_Click() {
 Me.MousePointer = 11;
 mdParty.ActualizaExperiencias;
 General.GuardarUsuarios();
 Me.MousePointer = 0;
 vb6.MsgBox("Grabado de personajes OK!");
}

	static void cmdConfigIntervalos_Click() {
		FrmInterv.Show();
	}

	static void cmdDebugNpcs_Click() {
		frmDebugNpc.Show();
	}

	static void cmdDebugUserlist_Click() {
		frmUserList.Show();
	}

	static void cmdLoadWorldBackup_Click() {
 /* 'Se asegura de que los sockets estan cerrados e ignora cualquier err */
 /* FIXME: ON ERROR RESUME NEXT */
 
 if (ffrmMain.nline.dakaraserver.txStatus.Caption = "Reiniciando.";
 }
 
 FrmStat.Show();
 
 if (General.FileExist(vb6.App.Instance().Path + "\\logs\\errores.log", 0)) {
 /* FIXME: KILL App . Path & "\\logs\\errores.log" */
 }
 if (General.FileExist(vb6.App.Instance().Path + "\\logs\\connect.log", 0)) {
 /* FIXME: KILL App . Path & "\\logs\\Connect.log" */
 }
 if (General.FileExist(vb6.App.Instance().Path + "\\logs\\HackAttemps.log", 0)) {
 /* FIXME: KILL App . Path & "\\logs\\HackAttemps.log" */
 }
 if (General.FileExist(vb6.App.Instance().Path + "\\logs\\Asesinatos.log", 0)) {
 /* FIXME: KILL App . Path & "\\logs\\Asesinatos.log" */
 }
 if (General.FileExist(vb6.App.Instance().Path + "\\logs\\Resurrecciones.log", 0)) {
 /* FIXME: KILL App . Path & "\\logs\\Resurrecciones.log" */
 }
 if (General.FileExist(vb6.App.Instance().Path + "\\logs\\Teleports.Log", 0)) {
 /* FIXME: KILL App . Path & "\\logs\\Teleports.Log" */
 }
 
 /* # IF UsarQueSocket = 1 THEN */
 apiclosesocket[wskapiAO.SockListen];
 /* # ELSEIF UsarQueSocket = 0 THEN */
 /* # ELSEIF UsarQueSocket = 2 THEN */
 /* # END IF */
 
 int LoopC = 0;
 
  for (LoopC = (1); LoopC <= (Declaraciones.MaxUsers); LoopC++) {
  TCP.CloseSocket(LoopC);
 }
 
 Declaraciones.LastUser = 0;
 Declaraciones.NumUsers = 0;
 
 General.FreeNPCs();
 General.FreeCharIndexes();
 
 ES.LoadSini();
 ES.CargarBackUp();
 ES.LoadOBJData();
 
 /* # IF UsarQueSocket = 1 THEN */
 wskapiAO.SockListen = ListenForConnect[Admin.Puerto][wskapiAO.hWndMsg][""];
 
 /* # ELSEIF UsarQueSocket = 0 THEN */
 
 /* # END IF */
 
 if (ffrmMain.nline.dakaraserver.txStatus.Caption = "Escuchando conexiones entrantes ...";
 }
 
}

	static void cmdPausarServidor_Click() {
		if (Declaraciones.EnPausa == false) {
			Declaraciones.EnPausa = true;
			modSendData.SendData(SendTarget.ToAll, 0, Protocol.PrepareMessagePauseToggle());
			cmdPausarServidor.Caption = "Reanudar el servidor";
		} else {
			Declaraciones.EnPausa = false;
			modSendData.SendData(SendTarget.ToAll, 0, Protocol.PrepareMessagePauseToggle());
			cmdPausarServidor.Caption = "Pausar el servidor";
		}
	}

	static void cmdRecargarBalance_Click() {
		ES.LoadBalance();
	}

	static void cmdRecargarGuardiasPosOrig_Click() {
		Admin.ReSpawnOrigPosNpcs();
	}

	static void cmdRecargarHechizos_Click() {
		ES.CargarHechizos();
	}

	static void cmdRecargarMD5s_Click() {
		Admin.MD5sCarga();
	}

	static void cmdRecargarMOTD_Click() {
		ES.LoadMotd();
	}

	static void cmdRecargarNombresInvalidos_Click() {
		ES.CargarForbidenWords();
	}

	static void cmdRecargarNPCs_Click() {
		General.CargaNpcsDat();
	}

	static void cmdRecargarObjetos_Click() {
		modForum.ResetForums();
		ES.LoadOBJData();
	}

	static void cmdRecargarServerIni_Click() {
		ES.LoadSini();
	}

	static void cmdReiniciar_Click() {
 
 if (vb6.MsgBox("¡¡Atencion!! Si reinicia el servidor puede provocar la pérdida de datos de los usarios. " + "¿Desea reiniciar el servidor de todas maneras?", vbYesNo) == vbNo) {
 return;
 }
 
 Me.Visible = false;
 General.Restart;
 
}

	static void cmdResetListen_Click() {
 
 /* # IF UsarQueSocket = 1 THEN */
 /* 'Cierra el socket de escucha */
 if (wskapiAO.SockListen>=0) {
 apiclosesocket[wskapiAO.SockListen];
 }
 
 /* 'Inicia el socket de escucha */
 wskapiAO.SockListen = ListenForConnect[Admin.Puerto][wskapiAO.hWndMsg][""];
 /* # END IF */
 
}

	static void cmdResetSockets_Click() {

		/* # IF UsarQueSocket = 1 THEN */

		if (vb6.MsgBox("¿Está seguro que desea reiniciar los sockets? Se cerrarán todas las conexiones activas.",
				vbYesNo, "Reiniciar Sockets") == vbYes) {
			wskapiAO.WSApiReiniciarSockets();
		}

		/* # ELSEIF UsarQueSocket = 2 THEN */

		/* # END IF */

	}

	static void cmdStatsSlots_Click() {
		frmConID.Show();
	}

	static void cmdUnbanAll_Click() {
 /* FIXME: ON ERROR RESUME NEXT */
 
 String Fn;
 String cad;
 int N = 0;
 int k = 0;
 
 String sENtrada;
 
 sENtrada = InputBox["Escribe ""estoy DE acuerdo"" entre comillas y con distinción de mayúsculas minúsculas para desbanear a todos los personajes."]["UnBan"]["hola"];
  if (sENtrada == "estoy DE acuerdo") {
  
  Fn = vb6.App.Instance().Path + "\\logs\\GenteBanned.log";
  
   if (General.FileExist(Fn, 0)) {
   N = vb6.FreeFile();
   /* FIXME: OPEN Fn FOR INPUT Shared AS # N */
    while (!clsByteBuffer.Eof(N)) {
    k = k+1;
    /* FIXME: INPUT # N , cad $ */
    Admin.UnBan(cad$);
    
   }
   /* FIXME: CLOSE # N */
   vb6.MsgBox("Se han habilitado " + k + " personajes.");
   /* FIXME: KILL Fn */
  }
 }
}

	static void cmdUnbanAllIps_Click() {
 int i = 0;
 int N = 0;
 
 String sENtrada;
 
 sENtrada = InputBox["Escribe ""estoy DE acuerdo"" sin comillas y con distinción de mayúsculas minúsculas para desbanear a todos los personajes"]["UnBan"]["hola"];
  if (sENtrada == "estoy DE acDeclaraciones.nline.dakaraserver.BanIps.Count;
   fDeclaraciones.nline.dakaraserver.BanIps.Count); i++) {
   Declaraciones.BanIps.Remove(1);
  }
  
  vb6.MsgBox("Se han habilitado " + N + " ipes");
 }
}

	static void cmdVerTrafico_Click() {
		frmTrafic.Show();
	}

	static void cmdWorldBackup_Click() {
 /* FIXME: ON ERROR GOTO ErrHandler */
 
 Me.MousePointer = 11;
 FrmStat.Show();
 ES.DoBackUp;
 Me.MousePointer = 0;
 vb6.MsgBox("WORLDSAVE OK!!");
 
 return;
 
 /* FIXME: ErrHandler : */
 General.LogError("Error en WORLDSAVE");
}

	static void Form_Deactivate() {
		frmServidor.Visible = false;
	}

	static void frmAdministracion_Click() {
		Me.Visible = false;
		frmAdmin.Show();
	}

	static void cmdRecargarAdministradores_Click() {
		AES.loadAdministrativeUsers();
	}

}