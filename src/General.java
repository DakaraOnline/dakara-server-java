
/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"General"')] */
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

public class General {

	public static clsIniManager LeerNPCs;

	static void DarCuerpoDesnudo(int UserIndex) {
		DarCuerpoDesnudo(UserIndex, false);
	}

	static void DarCuerpoDesnudo(int UserIndex, boolean Mimetizado) {
		/* '*************************************************** */
		/* 'Autor: Nacho (Integer) */
		/* 'Last Modification: 03/14/07 */
		/* 'Da cuerpo desnudo a un usuario */
		/* '23/11/2009: ZaMa - Optimizacion de codigo. */
		/* '*************************************************** */

		int CuerpoDesnudo;

		switch (Declaraciones.UserList[UserIndex].Genero) {
		case Hombre:
			switch (Declaraciones.UserList[UserIndex].raza) {
			case Humano:
				CuerpoDesnudo = 21;
				break;

			case Drow:
				CuerpoDesnudo = 32;
				break;

			case Elfo:
				CuerpoDesnudo = 210;
				break;

			case Gnomo:
				CuerpoDesnudo = 222;
				break;

			case Enano:
				CuerpoDesnudo = 53;
				break;
			}
		case Mujer:
			switch (Declaraciones.UserList[UserIndex].raza) {
			case Humano:
				CuerpoDesnudo = 39;
				break;

			case Drow:
				CuerpoDesnudo = 40;
				break;

			case Elfo:
				CuerpoDesnudo = 259;
				break;

			case Gnomo:
				CuerpoDesnudo = 260;
				break;

			case Enano:
				CuerpoDesnudo = 60;
				break;
			}
		}

		if (Mimetizado) {
			Declaraciones.UserList[UserIndex].CharMimetizado.body = CuerpoDesnudo;
		} else {
			Declaraciones.UserList[UserIndex].Char.body = CuerpoDesnudo;
		}

		Declaraciones.UserList[UserIndex].flags.Desnudo = 1;

	}

	static void Bloquear(boolean toMap, int sndIndex, int X, int Y, boolean b) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* 'b ahora es boolean, */
		/* 'b=true bloquea el tile en (x,y) */
		/* 'b=false desbloquea el tile en (x,y) */
		/* 'toMap = true -> Envia los datos a todo el mapa */
		/* 'toMap = false -> Envia los datos al user */
		/*
		 * 'Unifique los tres parametros (sndIndex,sndMap y map) en sndIndex...
		 * pero de todas formas, el mapa jamas se indica.. eso esta bien asi?
		 */
		/*
		 * 'Puede llegar a ser, que se quiera mandar el mapa, habria que agregar
		 * un nuevo parametro y modificar.. lo quite porque no se usaba ni aca
		 * ni en el cliente :s
		 */
		/* '*************************************************** */

		if (toMap) {
			modSendData.SendData(SendTarget.toMap, sndIndex, Protocol.PrepareMessageBlockPosition(X, Y, b));
		} else {
			Protocol.WriteBlockPosition(sndIndex, X, Y, b);
		}

	}

	static boolean HayAgua(int Map, int X, int Y) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		if (Map > 0 && Map < Declaraciones.NumMaps + 1 && X > 0 && X < 101 && Y > 0 && Y < 101) {
			if (((Declaraciones.MapData[Map][X][Y].Graphic[1] >= 1505
					&& Declaraciones.MapData[Map][X][Y].Graphic[1] <= 1520)
					|| (Declaraciones.MapData[Map][X][Y].Graphic[1] >= 5665
							&& Declaraciones.MapData[Map][X][Y].Graphic[1] <= 5680)
					|| (Declaraciones.MapData[Map][X][Y].Graphic[1] >= 13547
							&& Declaraciones.MapData[Map][X][Y].Graphic[1] <= 13562))
					&& Declaraciones.MapData[Map][X][Y].Graphic[2] == 0) {
				retval = true;
			} else {
				retval = false;
			}
		} else {
			retval = false;
		}

		return retval;
	}

	static boolean HayLava(int Map, int X, int Y) {
		boolean retval;
		/* '*************************************************** */
		/* 'Autor: Nacho (Integer) */
		/* 'Last Modification: 03/12/07 */
		/* '*************************************************** */
		if (Map > 0 && Map < Declaraciones.NumMaps + 1 && X > 0 && X < 101 && Y > 0 && Y < 101) {
			if (Declaraciones.MapData[Map][X][Y].Graphic[1] >= 5837
					&& Declaraciones.MapData[Map][X][Y].Graphic[1] <= 5852) {
				retval = true;
			} else {
				retval = false;
			}
		} else {
			retval = false;
		}

		return retval;
	}

	static void LimpiarMundo() {
 /* '*************************************************** */
 /* 'Author: Unknow */
 /* 'Last Modification: 04/15/2008 */
 /* '01/14/2008: Marcos Martinez (ByVal) - La funcion FOR estaba mal. En ves de i habia un 1. */
 /* '04/15/2008: (NicoNZ) - La funcion FOR estaba mal, de la forma que se hacia tiraba error. */
 /* '*************************************************** */
 /* FIXME: ON ERROR GOTO ErrHandler */
 
 int i;
 cGarbage d;
 d = new cGarbage();
 
  /* FIXME WEIRD FOR */
  for (i = (Declaraciones.TrashCollector.Count); ((-1) > 0) ? (i <= (1)) : (i >= (1)); i = i + (-1)) {
  d = Declaraciones.TrashCollector[i];
  InvUsuario.EraseObj(1, d.Map, d.X, d.Y);
  Declaraciones.TrashCollector.Remove[i];
  d = null;
 }
 
 SecurityIp.IpSecurityMantenimientoLista;
 
 return;
 
 /* FIXME: ErrHandler : */
 LogError("Error producido en el sub LimpiarMundo: " + Err.description);
}

	static void EnviarSpawnList(int UserIndex) {
 /* '*************************************************** */
 /* 'Author: Unknown */
 /* 'Last Modification: - */
 /* ' */
 /* '*************************************************** */
 
 int k;
 String[] npcNames;
 
 npcNames = new String[0];
 npcNames = (npcNames == null) ? new String[1 + vb6.UBound(Declaraciones.SpawnList)] : java.util.Arrays.copyOf(npcNames, 1 + vb6.UBound(Declaraciones.SpawnList));
 
  for (k = (1); k <= (vb6.UBound(Declaraciones.SpawnList)); k++) {
  npcNames[k] = Declaraciones.SpawnList[k].NpcName;
 }
 
 Protocol.WriteSpawnList(UserIndex, npcNames[]);
 
}

	static void ConfigListeningSocket(Object /* FIXME BYREF!! */ Obj, int Port) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* # IF UsarQueSocket = 0 THEN */

		/* # END IF */

	}

	static void Main() {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 15/03/2011 */
		/* '15/03/2011: ZaMa - Modularice todo, para que quede mas claro. */
		/* '*************************************************** */

		/* FIXME: ON ERROR RESUME NEXT */

		/* FIXME: CHDIR App . Path */
		/* FIXME: CHDRIVE App . Path */

		ES.LoadMotd();
		Admin.BanIpCargar();

		frmMain.Caption = frmMain.Caption + " V." + vb6.App.Instance().Major + "." + vb6.App.Instance().Minor + "."
				+ vb6.App.Instance().Revision;

		/* ' Start loading.. */
		frmCargando.Show();

		/* ' Constants & vars */
		frmCargando.Label1[2].Caption = "Cargando constantes...";
		LoadConstants();
		/* FIXME: DOEVENTS */

		/* ' Arrays */
		frmCargando.Label1[2].Caption = "Iniciando Arrays...";
		LoadArrays();

		/* ' Server.ini & Apuestas.dat */
		frmCargando.Label1[2].Caption = "Cargando Server.ini";
		ES.LoadSini();
		ES.CargaApuestas();

		/* ' Npcs.dat */
		frmCargando.Label1[2].Caption = "Cargando NPCs.Dat";
		CargaNpcsDat();

		/* ' Obj.dat */
		frmCargando.Label1[2].Caption = "Cargando Obj.Dat";
		ES.LoadOBJData();

		/* ' Hechizos.dat */
		frmCargando.Label1[2].Caption = "Cargando Hechizos.Dat";
		ES.CargarHechizos();

		/* ' Objetos de Herreria */
		frmCargando.Label1[2].Caption = "Cargando Objetos de Herrería";
		ES.LoadArmasHerreria();
		ES.LoadArmadurasHerreria();

		/* ' Objetos de Capinteria */
		frmCargando.Label1[2].Caption = "Cargando Objetos de Carpintería";
		ES.LoadObjCarpintero();

		/* ' Balance.dat */
		frmCargando.Label1[2].Caption = "Cargando Balance.Dat";
		ES.LoadBalance();

		/* ' Armaduras faccionarias */
		frmCargando.Label1[2].Caption = "Cargando ArmadurasFaccionarias.dat";
		ES.LoadArmadurasFaccion();

		/* ' Pretorianos */
		frmCargando.Label1[2].Caption = "Cargando Pretorianos.dat";
		PraetoriansCoopNPC.LoadPretorianData();

		/* ' Mapas */
		if (Admin.BootDelBackUp) {
			frmCargando.Label1[2].Caption = "Cargando BackUp";
			ES.CargarBackUp();
		} else {
			frmCargando.Label1[2].Caption = "Cargando Mapas";
			ES.LoadMapData();
		}

		/* ' Map Sounds */
		Declaraciones.SonidosMapas = new SoundMapInfo();
		Declaraciones.SonidosMapas.LoadSoundMapInfo();

		/* ' Home distance */
		ES.generateMatrix(Declaraciones.MATRIX_INITIAL_MAP);

		/* ' Connections */
		ResetUsersConnections();

		/* ' Timers */
		InitMainTimers();

		/* ' Sockets */
		SocketConfig();

		/* ' End loading.. */
		Unload(frmCargando);

		/* 'Log start time */
		ALogServerStartTime();

		/* 'Ocultar */
		if (Declaraciones.HideMe == 1) {
			frmMain.InitMain(1);
		} else {
			frmMain.InitMain(0);
		}

		Admin.tInicioServer = Declaraciones.GetTickCount() && 0x7FFFFFFF;
		InicializaEstadisticas();

	}

	static void LoadConstants() {
		/* '***************************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modify Date: 15/03/2011 */
		/* 'Loads all constants and general parameters. */
		/* '***************************************************************** */
		/* FIXME: ON ERROR RESUME NEXT */

		Declaraciones.LastBackup = vb6.Format(vb6.Now(), "Short Time");
		Declaraciones.Minutos = vb6.Format(vb6.Now(), "Short Time");

		/* ' Paths */
		Declaraciones.IniPath = vb6.App.Instance().Path + "\\";
		Declaraciones.DatPath = vb6.App.Instance().Path + "\\Dat\\";
		Declaraciones.CharPath = vb6.App.Instance().Path + "\\Charfile\\";

		/* ' Skills by level */
		Declaraciones.LevelSkill[1].LevelValue = 3;
		Declaraciones.LevelSkill[2].LevelValue = 5;
		Declaraciones.LevelSkill[3].LevelValue = 7;
		Declaraciones.LevelSkill[4].LevelValue = 10;
		Declaraciones.LevelSkill[5].LevelValue = 13;
		Declaraciones.LevelSkill[6].LevelValue = 15;
		Declaraciones.LevelSkill[7].LevelValue = 17;
		Declaraciones.LevelSkill[8].LevelValue = 20;
		Declaraciones.LevelSkill[9].LevelValue = 23;
		Declaraciones.LevelSkill[10].LevelValue = 25;
		Declaraciones.LevelSkill[11].LevelValue = 27;
		Declaraciones.LevelSkill[12].LevelValue = 30;
		Declaraciones.LevelSkill[13].LevelValue = 33;
		Declaraciones.LevelSkill[14].LevelValue = 35;
		Declaraciones.LevelSkill[15].LevelValue = 37;
		Declaraciones.LevelSkill[16].LevelValue = 40;
		Declaraciones.LevelSkill[17].LevelValue = 43;
		Declaraciones.LevelSkill[18].LevelValue = 45;
		Declaraciones.LevelSkill[19].LevelValue = 47;
		Declaraciones.LevelSkill[20].LevelValue = 50;
		Declaraciones.LevelSkill[21].LevelValue = 53;
		Declaraciones.LevelSkill[22].LevelValue = 55;
		Declaraciones.LevelSkill[23].LevelValue = 57;
		Declaraciones.LevelSkill[24].LevelValue = 60;
		Declaraciones.LevelSkill[25].LevelValue = 63;
		Declaraciones.LevelSkill[26].LevelValue = 65;
		Declaraciones.LevelSkill[27].LevelValue = 67;
		Declaraciones.LevelSkill[28].LevelValue = 70;
		Declaraciones.LevelSkill[29].LevelValue = 73;
		Declaraciones.LevelSkill[30].LevelValue = 75;
		Declaraciones.LevelSkill[31].LevelValue = 77;
		Declaraciones.LevelSkill[32].LevelValue = 80;
		Declaraciones.LevelSkill[33].LevelValue = 83;
		Declaraciones.LevelSkill[34].LevelValue = 85;
		Declaraciones.LevelSkill[35].LevelValue = 87;
		Declaraciones.LevelSkill[36].LevelValue = 90;
		Declaraciones.LevelSkill[37].LevelValue = 93;
		Declaraciones.LevelSkill[38].LevelValue = 95;
		Declaraciones.LevelSkill[39].LevelValue = 97;
		Declaraciones.LevelSkill[40].LevelValue = 100;
		Declaraciones.LevelSkill[41].LevelValue = 100;
		Declaraciones.LevelSkill[42].LevelValue = 100;
		Declaraciones.LevelSkill[43].LevelValue = 100;
		Declaraciones.LevelSkill[44].LevelValue = 100;
		Declaraciones.LevelSkill[45].LevelValue = 100;
		Declaraciones.LevelSkill[46].LevelValue = 100;
		Declaraciones.LevelSkill[47].LevelValue = 100;
		Declaraciones.LevelSkill[48].LevelValue = 100;
		Declaraciones.LevelSkill[49].LevelValue = 100;
		Declaraciones.LevelSkill[50].LevelValue = 100;

		/* ' Races */
		Declaraciones.ListaRazas[eRaza.Humano] = "Humano";
		Declaraciones.ListaRazas[eRaza.Elfo] = "Elfo";
		Declaraciones.ListaRazas[eRaza.Drow] = "Drow";
		Declaraciones.ListaRazas[eRaza.Gnomo] = "Gnomo";
		Declaraciones.ListaRazas[eRaza.Enano] = "Enano";

		/* ' Classes */
		Declaraciones.ListaClases[eClass.Mage] = "Mago";
		Declaraciones.ListaClases[eClass.Cleric] = "Clerigo";
		Declaraciones.ListaClases[eClass.Warrior] = "Guerrero";
		Declaraciones.ListaClases[eClass.Assasin] = "Asesino";
		Declaraciones.ListaClases[eClass.Thief] = "Ladron";
		Declaraciones.ListaClases[eClass.Bard] = "Bardo";
		Declaraciones.ListaClases[eClass.Druid] = "Druida";
		Declaraciones.ListaClases[eClass.Bandit] = "Bandido";
		Declaraciones.ListaClases[eClass.Paladin] = "Paladin";
		Declaraciones.ListaClases[eClass.Hunter] = "Cazador";
		Declaraciones.ListaClases[eClass.Worker] = "Trabajador";
		Declaraciones.ListaClases[eClass.Pirat] = "Pirata";

		/* ' Skills */
		Declaraciones.SkillsNames[eSkill.Magia] = "Magia";
		Declaraciones.SkillsNames[eSkill.Robar] = "Robar";
		Declaraciones.SkillsNames[eSkill.Tacticas] = "Evasión en combate";
		Declaraciones.SkillsNames[eSkill.Armas] = "Combate con armas";
		Declaraciones.SkillsNames[eSkill.Meditar] = "Meditar";
		Declaraciones.SkillsNames[eSkill.Apunalar] = "Apunalar";
		Declaraciones.SkillsNames[eSkill.Ocultarse] = "Ocultarse";
		Declaraciones.SkillsNames[eSkill.Supervivencia] = "Supervivencia";
		Declaraciones.SkillsNames[eSkill.Talar] = "Talar";
		Declaraciones.SkillsNames[eSkill.Comerciar] = "Comercio";
		Declaraciones.SkillsNames[eSkill.Defensa] = "Defensa con escudos";
		Declaraciones.SkillsNames[eSkill.Pesca] = "Pesca";
		Declaraciones.SkillsNames[eSkill.Mineria] = "Mineria";
		Declaraciones.SkillsNames[eSkill.Carpinteria] = "Carpinteria";
		Declaraciones.SkillsNames[eSkill.Herreria] = "Herreria";
		Declaraciones.SkillsNames[eSkill.Liderazgo] = "Liderazgo";
		Declaraciones.SkillsNames[eSkill.Domar] = "Domar animales";
		Declaraciones.SkillsNames[eSkill.Proyectiles] = "Combate a distancia";
		Declaraciones.SkillsNames[eSkill.Wrestling] = "Combate sin armas";
		Declaraciones.SkillsNames[eSkill.Navegacion] = "Navegacion";

		/* ' Attributes */
		Declaraciones.ListaAtributos[eAtributos.Fuerza] = "Fuerza";
		Declaraciones.ListaAtributos[eAtributos.Agilidad] = "Agilidad";
		Declaraciones.ListaAtributos[eAtributos.Inteligencia] = "Inteligencia";
		Declaraciones.ListaAtributos[eAtributos.Carisma] = "Carisma";
		Declaraciones.ListaAtributos[eAtributos.Constitucion] = "Constitucion";

		/* ' Fishes */
		Declaraciones.ListaPeces[1] = PECES_POSIBLES.PESCADO1;
		Declaraciones.ListaPeces[2] = PECES_POSIBLES.PESCADO2;
		Declaraciones.ListaPeces[3] = PECES_POSIBLES.PESCADO3;
		Declaraciones.ListaPeces[4] = PECES_POSIBLES.PESCADO4;

		/* 'Bordes del mapa */
		Declaraciones.MinXBorder = Declaraciones.XMinMapSize + (Declaraciones.XWindow / 2);
		Declaraciones.MaxXBorder = Declaraciones.XMaxMapSize - (Declaraciones.XWindow / 2);
		Declaraciones.MinYBorder = Declaraciones.YMinMapSize + (Declaraciones.YWindow / 2);
		Declaraciones.MaxYBorder = Declaraciones.YMaxMapSize - (Declaraciones.YWindow / 2);

		Declaraciones.Ayuda = new cCola();
		Declaraciones.Denuncias = new cCola();
		Declaraciones.Denuncias.MaxLenght = Declaraciones.MAX_DENOUNCES;

		Declaraciones.Prision.Map = 66;
		Declaraciones.Prision.X = 75;
		Declaraciones.Prision.Y = 47;

		Declaraciones.Libertad.Map = 66;
		Declaraciones.Libertad.X = 75;
		Declaraciones.Libertad.Y = 65;

		Declaraciones.MaxUsers = 0;

		/* ' Initialize classes */
		wskapiAO.WSAPISock2Usr = new Collection();
		Protocol.InitAuxiliarBuffer();
		/* # IF SeguridadAlkon THEN */
		/* # END IF */

		Declaraciones.aClon = new clsAntiMassClon();
		Declaraciones.TrashCollector = new Collection();

	}

	static void LoadArrays() {
		/* '***************************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modify Date: 15/03/2011 */
		/* 'Loads all arrays */
		/* '***************************************************************** */
		/* FIXME: ON ERROR RESUME NEXT */
		/* ' Load Records */
		modUserRecords.LoadRecords();
		/* ' Load guilds info */
		modGuilds.LoadGuildsDB();
		/* ' Load spawn list */
		ES.CargarSpawnList();
		/* ' Load forbidden words */
		ES.CargarForbidenWords();
	}

	static void ResetUsersConnections() {
		/* '***************************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modify Date: 15/03/2011 */
		/* 'Resets Users Connections. */
		/* '***************************************************************** */
		/* FIXME: ON ERROR RESUME NEXT */

		int LoopC;
		for (LoopC = (1); LoopC <= (Declaraciones.MaxUsers); LoopC++) {
			Declaraciones.UserList[LoopC].ConnID = -1;
			Declaraciones.UserList[LoopC].ConnIDValida = false;
			Declaraciones.UserList[LoopC].incomingData = new clsByteQueue();
			Declaraciones.UserList[LoopC].outgoingData = new clsByteQueue();
		}

	}

	static void InitMainTimers() {
		/* '***************************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modify Date: 15/03/2011 */
		/* 'Initializes Main Timers. */
		/* '***************************************************************** */
		/* FIXME: ON ERROR RESUME NEXT */

		frmMain.AutoSave.Enabled = true;
		/* '.tLluvia.Enabled = True ' [TEMPORAL] */
		frmMain.tPiqueteC.Enabled = true;
		frmMain.GameTimer.Enabled = true;
		/* '.tLluviaEvent.Enabled = True ' [TEMPORAL] */
		frmMain.FX.Enabled = true;
		frmMain.Auditoria.Enabled = true;
		frmMain.KillLog.Enabled = true;
		frmMain.TIMER_AI.Enabled = true;
		frmMain.npcataca.Enabled = true;

		/* # IF SeguridadAlkon THEN */
		/* # END IF */

	}

	static void SocketConfig() {
		/* '***************************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modify Date: 15/03/2011 */
		/* 'Sets socket config. */
		/* '***************************************************************** */
		/* FIXME: ON ERROR RESUME NEXT */

		SecurityIp.InitIpTables(1000);

		/* # IF UsarQueSocket = 1 THEN */

		wskapiAO.IniciaWsApi(frmMain.hWnd);
		wskapiAO.SockListen = ListenForConnect[Admin.Puerto][wskapiAO.hWndMsg][""];

		/* # ELSEIF UsarQueSocket = 0 THEN */

		/* # ELSEIF UsarQueSocket = 2 THEN */

		/* # ELSEIF UsarQueSocket = 3 THEN */

		/* # END IF */

		if (frmMain.Visible) {
			frmMain.txStatus.Caption = "Escuchando conexiones entrantes ...";
		}

	}

	static void LogServerStartTime() {
		/* '***************************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modify Date: 15/03/2011 */
		/* 'Logs Server Start Time. */
		/* '***************************************************************** */
		int N;
		N = vb6.FreeFile();
		/*
		 * FIXME: OPEN App . Path & "\\logs\\Main.log" FOR Append Shared AS # N
		 */
		/*
		 * FIXME: PRINT # N , Date & " " & time & " server iniciado " & App .
		 * Major & "." ; App . Minor & "." & App . Revision
		 */
		/* FIXME: CLOSE # N */

	}

	static boolean FileExist(String File) {
		return FileExist(File, 0);
	}

	static boolean FileExist(String File, int FileType) {
		boolean retval;
		/* '***************************************************************** */
		/* 'Se fija si existe el archivo */
		/* '***************************************************************** */

		retval = vb6.LenB(vb6.dir(File, FileType)) != 0;
		return retval;
	}

	static String ReadField(int Pos, String /* FIXME BYREF!! */ Text, int SepASCII) {
		String retval;
		/* '***************************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modify Date: 11/15/2004 */
		/* 'Gets a field from a delimited string */
		/* '***************************************************************** */

		int i;
		int lastPos;
		int CurrentPos;
		String delimiter;

		delimiter = vb6.Chr(SepASCII);

		for (i = (1); i <= (Pos); i++) {
			lastPos = CurrentPos;
			CurrentPos = vb6.InStr(lastPos + 1, Text, delimiter, vbBinaryCompare);
		}

		if (CurrentPos == 0) {
			retval = vb6.mid(Text, lastPos + 1, vb6.Len(Text) - lastPos);
		} else {
			retval = vb6.mid(Text, lastPos + 1, CurrentPos - lastPos - 1);
		}
		return retval;
	}

	static boolean MapaValido(int Map) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		retval = Map >= 1 && Map <= Declaraciones.NumMaps;
		return retval;
	}

	static void MostrarNumUsers() {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		frmMain.txtNumUsers.Text = Declaraciones.NumUsers;

	}

	static void LogCriticEvent(String desc) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int nfile;
		/* ' obtenemos un canal */
		nfile = vb6.FreeFile();
		/*
		 * FIXME: OPEN App . Path & "\\logs\\Eventos.log" FOR Append Shared AS #
		 * nfile
		 */
		/* FIXME: PRINT # nfile , Date & " " & time & " " & desc */
		/* FIXME: CLOSE # nfile */

		return;

		/* FIXME: ErrHandler : */

	}

	static void LogEjercitoReal(String desc) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int nfile;
		/* ' obtenemos un canal */
		nfile = vb6.FreeFile();
		/*
		 * FIXME: OPEN App . Path & "\\logs\\EjercitoReal.log" FOR Append Shared
		 * AS # nfile
		 */
		/* FIXME: PRINT # nfile , desc */
		/* FIXME: CLOSE # nfile */

		return;

		/* FIXME: ErrHandler : */

	}

	static void LogEjercitoCaos(String desc) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int nfile;
		/* ' obtenemos un canal */
		nfile = vb6.FreeFile();
		/*
		 * FIXME: OPEN App . Path & "\\logs\\EjercitoCaos.log" FOR Append Shared
		 * AS # nfile
		 */
		/* FIXME: PRINT # nfile , desc */
		/* FIXME: CLOSE # nfile */

		return;

		/* FIXME: ErrHandler : */

	}

	static void LogIndex(int index, String desc) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int nfile;
		/* ' obtenemos un canal */
		nfile = vb6.FreeFile();
		/*
		 * FIXME: OPEN App . Path & "\\logs\\" & index & ".log" FOR Append
		 * Shared AS # nfile
		 */
		/* FIXME: PRINT # nfile , Date & " " & time & " " & desc */
		/* FIXME: CLOSE # nfile */

		return;

		/* FIXME: ErrHandler : */

	}

	static void LogError(String desc) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int nfile;
		/* ' obtenemos un canal */
		nfile = vb6.FreeFile();
		/*
		 * FIXME: OPEN App . Path & "\\logs\\errores.log" FOR Append Shared AS #
		 * nfile
		 */
		/* FIXME: PRINT # nfile , Date & " " & time & " " & desc */
		/* FIXME: CLOSE # nfile */

		return;

		/* FIXME: ErrHandler : */

	}

	static void LogStatic(String desc) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int nfile;
		/* ' obtenemos un canal */
		nfile = vb6.FreeFile();
		/*
		 * FIXME: OPEN App . Path & "\\logs\\Stats.log" FOR Append Shared AS #
		 * nfile
		 */
		/* FIXME: PRINT # nfile , Date & " " & time & " " & desc */
		/* FIXME: CLOSE # nfile */

		return;

		/* FIXME: ErrHandler : */

	}

	static void LogTarea(String desc) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int nfile;
		/* ' obtenemos un canal */
		nfile = vb6.FreeFile(1);
		/*
		 * FIXME: OPEN App . Path & "\\logs\\haciendo.log" FOR Append Shared AS
		 * # nfile
		 */
		/* FIXME: PRINT # nfile , Date & " " & time & " " & desc */
		/* FIXME: CLOSE # nfile */

		return;

		/* FIXME: ErrHandler : */

	}

	static void LogClanes(String str) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int nfile;
		/* ' obtenemos un canal */
		nfile = vb6.FreeFile();
		/*
		 * FIXME: OPEN App . Path & "\\logs\\clanes.log" FOR Append Shared AS #
		 * nfile
		 */
		/* FIXME: PRINT # nfile , Date & " " & time & " " & str */
		/* FIXME: CLOSE # nfile */

	}

	static void LogIP(String str) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int nfile;
		/* ' obtenemos un canal */
		nfile = vb6.FreeFile();
		/*
		 * FIXME: OPEN App . Path & "\\logs\\IP.log" FOR Append Shared AS #
		 * nfile
		 */
		/* FIXME: PRINT # nfile , Date & " " & time & " " & str */
		/* FIXME: CLOSE # nfile */

	}

	static void LogDesarrollo(String str) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int nfile;
		/* ' obtenemos un canal */
		nfile = vb6.FreeFile();
		/*
		 * FIXME: OPEN App . Path & "\\logs\\desarrollo" & Month ( Date ) & Year
		 * ( Date ) & ".log" FOR Append Shared AS # nfile
		 */
		/* FIXME: PRINT # nfile , Date & " " & time & " " & str */
		/* FIXME: CLOSE # nfile */

	}

	static void LogGM(String Nombre, String texto) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '***************************************************ç */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int nfile;
		/* ' obtenemos un canal */
		nfile = vb6.FreeFile();
		/* 'Guardamos todo en el mismo lugar. Pablo (ToxicWaste) 18/05/07 */
		/*
		 * FIXME: OPEN App . Path & "\\logs\\" & Nombre & ".log" FOR Append
		 * Shared AS # nfile
		 */
		/* FIXME: PRINT # nfile , Date & " " & time & " " & texto */
		/* FIXME: CLOSE # nfile */

		return;

		/* FIXME: ErrHandler : */

	}

	static void LogAsesinato(String texto) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */
		int nfile;

		/* ' obtenemos un canal */
		nfile = vb6.FreeFile();

		/*
		 * FIXME: OPEN App . Path & "\\logs\\asesinatos.log" FOR Append Shared
		 * AS # nfile
		 */
		/* FIXME: PRINT # nfile , Date & " " & time & " " & texto */
		/* FIXME: CLOSE # nfile */

		return;

		/* FIXME: ErrHandler : */

	}

	static void logVentaCasa(String texto) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int nfile;
		/* ' obtenemos un canal */
		nfile = vb6.FreeFile();

		/*
		 * FIXME: OPEN App . Path & "\\logs\\propiedades.log" FOR Append Shared
		 * AS # nfile
		 */
		/*
		 * FIXME: PRINT # nfile ,
		 * "----------------------------------------------------------"
		 */
		/* FIXME: PRINT # nfile , Date & " " & time & " " & texto */
		/*
		 * FIXME: PRINT # nfile ,
		 * "----------------------------------------------------------"
		 */
		/* FIXME: CLOSE # nfile */

		return;

		/* FIXME: ErrHandler : */

	}

	static void LogHackAttemp(String texto) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int nfile;
		/* ' obtenemos un canal */
		nfile = vb6.FreeFile();
		/*
		 * FIXME: OPEN App . Path & "\\logs\\HackAttemps.log" FOR Append Shared
		 * AS # nfile
		 */
		/*
		 * FIXME: PRINT # nfile ,
		 * "----------------------------------------------------------"
		 */
		/* FIXME: PRINT # nfile , Date & " " & time & " " & texto */
		/*
		 * FIXME: PRINT # nfile ,
		 * "----------------------------------------------------------"
		 */
		/* FIXME: CLOSE # nfile */

		return;

		/* FIXME: ErrHandler : */

	}

	static void LogCheating(String texto) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int nfile;
		/* ' obtenemos un canal */
		nfile = vb6.FreeFile();
		/*
		 * FIXME: OPEN App . Path & "\\logs\\CH.log" FOR Append Shared AS #
		 * nfile
		 */
		/* FIXME: PRINT # nfile , Date & " " & time & " " & texto */
		/* FIXME: CLOSE # nfile */

		return;

		/* FIXME: ErrHandler : */

	}

	static void LogCriticalHackAttemp(String texto) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int nfile;
		/* ' obtenemos un canal */
		nfile = vb6.FreeFile();
		/*
		 * FIXME: OPEN App . Path & "\\logs\\CriticalHackAttemps.log" FOR Append
		 * Shared AS # nfile
		 */
		/*
		 * FIXME: PRINT # nfile ,
		 * "----------------------------------------------------------"
		 */
		/* FIXME: PRINT # nfile , Date & " " & time & " " & texto */
		/*
		 * FIXME: PRINT # nfile ,
		 * "----------------------------------------------------------"
		 */
		/* FIXME: CLOSE # nfile */

		return;

		/* FIXME: ErrHandler : */

	}

	static void LogAntiCheat(String texto) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int nfile;
		/* ' obtenemos un canal */
		nfile = vb6.FreeFile();
		/*
		 * FIXME: OPEN App . Path & "\\logs\\AntiCheat.log" FOR Append Shared AS
		 * # nfile
		 */
		/* FIXME: PRINT # nfile , Date & " " & time & " " & texto */
		/* FIXME: PRINT # nfile , "" */
		/* FIXME: CLOSE # nfile */

		return;

		/* FIXME: ErrHandler : */

	}

	static boolean ValidInputNP(String cad) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		String Arg;
		int i;

		for (i = (1); i <= (33); i++) {

			Arg = ReadField(i, cad, 44);

			if (vb6.LenB(Arg) == 0) {
				return retval;
			}

		}

		retval = true;

		return retval;
	}

	static void Restart() {
 /* '*************************************************** */
 /* 'Author: Unknown */
 /* 'Last Modification: - */
 /* ' */
 /* '*************************************************** */
 
 /* 'Se asegura de que los sockets estan cerrados e ignora cualquier err */
 /* FIXME: ON ERROR RESUME NEXT */
 
 if (frmMain.Visible) {
 frmMain.txStatus.Caption = "Reiniciando.";
 }
 
 int LoopC;
 
 /* # IF UsarQueSocket = 0 THEN */
 
 /* # ELSEIF UsarQueSocket = 1 THEN */
 
 /* 'Cierra el socket de escucha */
 if (wskapiAO.SockListen>=0) {
 apiclosesocket[wskapiAO.SockListen];
 }
 
 /* 'Inicia el socket de escucha */
 wskapiAO.SockListen = ListenForConnect[Admin.Puerto][wskapiAO.hWndMsg][""];
 
 /* # ELSEIF UsarQueSocket = 2 THEN */
 
 /* # END IF */
 
  for (LoopC = (1); LoopC <= (Declaraciones.MaxUsers); LoopC++) {
  TCP.CloseSocket(LoopC);
 }
 
 /* 'Initialize statistics!! */
 Statistics.Initialize();
 
  for (LoopC = (1); LoopC <= (vb6.UBound(Declaraciones.UserList[])); LoopC++) {
  Declaraciones.UserList[LoopC].incomingData = null;
  Declaraciones.UserList[LoopC].outgoingData = null;
 }
 
 Declaraciones.UserList = new User[0];
 Declaraciones.UserList = (Declaraciones.UserList == null) ? new User[1 + Declaraciones.MaxUsers] : java.util.Arrays.copyOf(Declaraciones.UserList, 1 + Declaraciones.MaxUsers);
 
  for (LoopC = (1); LoopC <= (Declaraciones.MaxUsers); LoopC++) {
  Declaraciones.UserList[LoopC].ConnID = -1;
  Declaraciones.UserList[LoopC].ConnIDValida = false;
  Declaraciones.UserList[LoopC].incomingData = new clsByteQueue();
  Declaraciones.UserList[LoopC].outgoingData = new clsByteQueue();
 }
 
 Declaraciones.LastUser = 0;
 Declaraciones.NumUsers = 0;
 
 FreeNPCs();
 FreeCharIndexes();
 
 ES.LoadSini();
 
 modForum.ResetForums();
 ES.LoadOBJData();
 
 ES.LoadMapData();
 
 ES.CargarHechizos();
 
 /* # IF UsarQueSocket = 0 THEN */
 
 /* # ELSEIF UsarQueSocket = 1 THEN */
 
 /* # ELSEIF UsarQueSocket = 2 THEN */
 
 /* # END IF */
 
 if (frmMain.Visible) {
 frmMain.txStatus.Caption = "Escuchando conexiones entrantes ...";
 }
 
 /* 'Log it */
 int N;
 N = vb6.FreeFile();
 /* FIXME: OPEN App . Path & "\\logs\\Main.log" FOR Append Shared AS # N */
 /* FIXME: PRINT # N , Date & " " & time & " servidor reiniciado." */
 /* FIXME: CLOSE # N */
 
 /* 'Ocultar */
 
  if (Declaraciones.HideMe == 1) {
  frmMain.InitMain(1);
  } else {
  frmMain.InitMain(0);
 }
 
}

	static boolean Intemperie(int UserIndex) {
		boolean retval;
		/* '************************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modify Date: 15/11/2009 */
		/* '15/11/2009: ZaMa - La lluvia no quita stamina en las arenas. */
		/* '23/11/2009: ZaMa - Optimizacion de codigo. */
		/* '************************************************************** */

		if (Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].Zona != "DUNGEON") {
			if (Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].trigger != 1
					&& Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].trigger != 2
					&& Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].trigger != 4) {
				retval = true;
			}
		} else {
			retval = false;
		}

		/* 'En las arenas no te afecta la lluvia */
		if (UsUaRiOs.IsArena(UserIndex)) {
			retval = false;
		}
		return retval;
	}

	static void EfectoLluvia(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		if (Declaraciones.UserList[UserIndex].flags.UserLogged) {
			if (Intemperie(UserIndex)) {
				int modifi;
				modifi = Matematicas.Porcentaje(Declaraciones.UserList[UserIndex].Stats.MaxSta, 3);
				Trabajo.QuitarSta(UserIndex, modifi);
				Protocol.FlushBuffer(UserIndex);
			}
		}

		return;
		/* FIXME: ErrHandler : */
		LogError("Error en EfectoLluvia");
	}

	static void TiempoInvocacion(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int i;
		for (i = (1); i <= (Declaraciones.MAXMASCOTAS); i++) {
			if (Declaraciones.UserList[UserIndex].MascotasIndex[i] > 0) {
				if (Declaraciones.Npclist[Declaraciones.UserList[UserIndex].MascotasIndex[i]].Contadores.TiempoExistencia > 0) {
					Declaraciones.Npclist[Declaraciones.UserList[UserIndex].MascotasIndex[i]].Contadores.TiempoExistencia = Declaraciones.Npclist[Declaraciones.UserList[UserIndex].MascotasIndex[i]].Contadores.TiempoExistencia
							- 1;
					if (Declaraciones.Npclist[Declaraciones.UserList[UserIndex].MascotasIndex[i]].Contadores.TiempoExistencia == 0) {
						NPCs.MuereNpc(Declaraciones.UserList[UserIndex].MascotasIndex[i], 0);
					}
				}
			}
		}
	}

	static void EfectoFrio(int UserIndex) {
		/* '*************************************************** */
		/* 'Autor: Unkonwn */
		/* 'Last Modification: 23/11/2009 */
		/*
		 * 'If user is naked and it's in a cold map, take health points from him
		 */
		/* '23/11/2009: ZaMa - Optimizacion de codigo. */
		/* '*************************************************** */
		int modifi;

		if (Declaraciones.UserList[UserIndex].Counters.Frio < Admin.IntervaloFrio) {
			Declaraciones.UserList[UserIndex].Counters.Frio = Declaraciones.UserList[UserIndex].Counters.Frio + 1;
		} else {
			if (Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].Terreno == eTerrain.terrain_nieve) {
				Protocol.WriteConsoleMsg(UserIndex, "¡¡Estás muriendo de frío, abrigate o morirás!!",
						FontTypeNames.FONTTYPE_INFO);
				modifi = SistemaCombate
						.MinimoInt(Matematicas.Porcentaje(Declaraciones.UserList[UserIndex].Stats.MaxHp, 5), 15);
				Declaraciones.UserList[UserIndex].Stats.MinHp = Declaraciones.UserList[UserIndex].Stats.MinHp - modifi;

				if (Declaraciones.UserList[UserIndex].Stats.MinHp < 1) {
					Protocol.WriteConsoleMsg(UserIndex, "¡¡Has muerto de frío!!", FontTypeNames.FONTTYPE_INFO);
					Declaraciones.UserList[UserIndex].Stats.MinHp = 0;
					UsUaRiOs.UserDie(UserIndex);
				}

				Protocol.WriteUpdateHP(UserIndex);
			} else {
				modifi = Matematicas.Porcentaje(Declaraciones.UserList[UserIndex].Stats.MaxSta, 5);
				Trabajo.QuitarSta(UserIndex, modifi);
				Protocol.WriteUpdateSta(UserIndex);
			}

			Declaraciones.UserList[UserIndex].Counters.Frio = 0;
		}
	}

	static void EfectoLava(int UserIndex) {
		/* '*************************************************** */
		/* 'Autor: Nacho (Integer) */
		/* 'Last Modification: 23/11/2009 */
		/* 'If user is standing on lava, take health points from him */
		/* '23/11/2009: ZaMa - Optimizacion de codigo. */
		/* '*************************************************** */
		/* 'Usamos el mismo intervalo que el del frio */
		if (Declaraciones.UserList[UserIndex].Counters.Lava < Admin.IntervaloFrio) {
			Declaraciones.UserList[UserIndex].Counters.Lava = Declaraciones.UserList[UserIndex].Counters.Lava + 1;
		} else {
			if (HayLava(Declaraciones.UserList[UserIndex].Pos.Map, Declaraciones.UserList[UserIndex].Pos.X,
					Declaraciones.UserList[UserIndex].Pos.Y)) {
				Protocol.WriteConsoleMsg(UserIndex, "¡¡Quitate de la lava, te estás quemando!!",
						FontTypeNames.FONTTYPE_INFO);
				Declaraciones.UserList[UserIndex].Stats.MinHp = Declaraciones.UserList[UserIndex].Stats.MinHp
						- Matematicas.Porcentaje(Declaraciones.UserList[UserIndex].Stats.MaxHp, 5);

				if (Declaraciones.UserList[UserIndex].Stats.MinHp < 1) {
					Protocol.WriteConsoleMsg(UserIndex, "¡¡Has muerto quemado!!", FontTypeNames.FONTTYPE_INFO);
					Declaraciones.UserList[UserIndex].Stats.MinHp = 0;
					UsUaRiOs.UserDie(UserIndex);
				}

				Protocol.WriteUpdateHP(UserIndex);

			}

			Declaraciones.UserList[UserIndex].Counters.Lava = 0;
		}
	}

	/* '' */
	/* ' Maneja el efecto del estado atacable */
	/* ' */
	/*
	 * ' @param UserIndex El index del usuario a ser afectado por el estado
	 * atacable
	 */
	/* ' */

	static void EfectoEstadoAtacable(int UserIndex) {
		/* '****************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Update: 18/09/2010 (ZaMa) */
		/*
		 * '18/09/2010: ZaMa - Ahora se activa el seguro cuando dejas de ser
		 * atacable.
		 */
		/* '****************************************************** */

		/* ' Si ya paso el tiempo de penalizacion */
		if (!modNuevoTimer.IntervaloEstadoAtacable(UserIndex)) {
			/* ' Deja de poder ser atacado */
			Declaraciones.UserList[UserIndex].flags.AtacablePor = 0;

			/* ' Activo el seguro si deja de estar atacable */
			if (!Declaraciones.UserList[UserIndex].flags.Seguro) {
				Protocol.WriteMultiMessage(UserIndex, eMessages.SafeModeOn);
			}

			/* ' Send nick normal */
			UsUaRiOs.RefreshCharStatus(UserIndex);
		}

	}

	/* '' */
	/* ' Maneja el tiempo de arrivo al hogar */
	/* ' */
	/* ' @param UserIndex El index del usuario a ser afectado por el /hogar */
	/* ' */

	static void TravelingEffect(int UserIndex) {
		/* '****************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Update: 01/06/2010 (ZaMa) */
		/* '****************************************************** */

		/* ' Si ya paso el tiempo de penalizacion */
		if (modNuevoTimer.IntervaloGoHome(UserIndex)) {
			UsUaRiOs.HomeArrival(UserIndex);
		}

	}

	/* '' */
	/* ' Maneja el tiempo y el efecto del mimetismo */
	/* ' */
	/*
	 * ' @param UserIndex El index del usuario a ser afectado por el mimetismo
	 */
	/* ' */

	static void EfectoMimetismo(int UserIndex) {
		/* '****************************************************** */
		/* 'Author: Unknown */
		/* 'Last Update: 16/09/2010 (ZaMa) */
		/*
		 * '12/01/2010: ZaMa - Los druidas pierden la inmunidad de ser atacados
		 * cuando pierden el efecto del mimetismo.
		 */
		/*
		 * '16/09/2010: ZaMa - Se recupera la apariencia de la barca
		 * correspondiente despues de terminado el mimetismo.
		 */
		/* '****************************************************** */
		Declaraciones.ObjData Barco;

		if (Declaraciones.UserList[UserIndex].Counters.Mimetismo < Admin.IntervaloInvisible) {
			Declaraciones.UserList[UserIndex].Counters.Mimetismo = Declaraciones.UserList[UserIndex].Counters.Mimetismo
					+ 1;
		} else {
			/* 'restore old char */
			Protocol.WriteConsoleMsg(UserIndex, "Recuperas tu apariencia normal.", FontTypeNames.FONTTYPE_INFO);

			if (Declaraciones.UserList[UserIndex].flags.Navegando) {
				if (Declaraciones.UserList[UserIndex].flags.Muerto == 0) {
					UsUaRiOs.ToggleBoatBody(UserIndex);
				} else {
					Declaraciones.UserList[UserIndex].Char.body = Declaraciones.iFragataFantasmal;
					Declaraciones.UserList[UserIndex].Char.ShieldAnim = Declaraciones.NingunEscudo;
					Declaraciones.UserList[UserIndex].Char.WeaponAnim = Declaraciones.NingunArma;
					Declaraciones.UserList[UserIndex].Char.CascoAnim = Declaraciones.NingunCasco;
				}
			} else {
				Declaraciones.UserList[UserIndex].Char.body = Declaraciones.UserList[UserIndex].CharMimetizado.body;
				Declaraciones.UserList[UserIndex].Char.Head = Declaraciones.UserList[UserIndex].CharMimetizado.Head;
				Declaraciones.UserList[UserIndex].Char.CascoAnim = Declaraciones.UserList[UserIndex].CharMimetizado.CascoAnim;
				Declaraciones.UserList[UserIndex].Char.ShieldAnim = Declaraciones.UserList[UserIndex].CharMimetizado.ShieldAnim;
				Declaraciones.UserList[UserIndex].Char.WeaponAnim = Declaraciones.UserList[UserIndex].CharMimetizado.WeaponAnim;
			}

			UsUaRiOs.ChangeUserChar(UserIndex, Declaraciones.UserList[UserIndex].Char.body,
					Declaraciones.UserList[UserIndex].Char.Head, Declaraciones.UserList[UserIndex].Char.heading,
					Declaraciones.UserList[UserIndex].Char.WeaponAnim,
					Declaraciones.UserList[UserIndex].Char.ShieldAnim,
					Declaraciones.UserList[UserIndex].Char.CascoAnim);

			Declaraciones.UserList[UserIndex].Counters.Mimetismo = 0;
			Declaraciones.UserList[UserIndex].flags.Mimetizado = 0;
			/* ' Se fue el efecto del mimetismo, puede ser atacado por npcs */
			Declaraciones.UserList[UserIndex].flags.Ignorado = false;
		}
	}

	static void EfectoInvisibilidad(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 16/09/2010 (ZaMa) */
		/*
		 * '16/09/2010: ZaMa - Al perder el invi cuando navegas, no se manda el
		 * mensaje de sacar invi (ya estas visible).
		 */
		/* '*************************************************** */

		if (Declaraciones.UserList[UserIndex].Counters.Invisibilidad < Admin.IntervaloInvisible) {
			Declaraciones.UserList[UserIndex].Counters.Invisibilidad = Declaraciones.UserList[UserIndex].Counters.Invisibilidad
					+ 1;
		} else {
			/* ' Invi variable :D */
			Declaraciones.UserList[UserIndex].Counters.Invisibilidad = Matematicas.RandomNumber(-100, 100);
			Declaraciones.UserList[UserIndex].flags.invisible = 0;
			if (Declaraciones.UserList[UserIndex].flags.Oculto == 0) {
				Protocol.WriteConsoleMsg(UserIndex, "Has vuelto a ser visible.", FontTypeNames.FONTTYPE_INFO);

				/* ' Si navega ya esta visible.. */
				if (!Declaraciones.UserList[UserIndex].flags.Navegando == 1) {
					UsUaRiOs.SetInvisible(UserIndex, Declaraciones.UserList[UserIndex].Char.CharIndex, false);
				}

			}
		}

	}

	static void EfectoParalisisNpc(int NpcIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		if (Declaraciones.Npclist[NpcIndex].Contadores.Paralisis > 0) {
			Declaraciones.Npclist[NpcIndex].Contadores.Paralisis = Declaraciones.Npclist[NpcIndex].Contadores.Paralisis
					- 1;
		} else {
			Declaraciones.Npclist[NpcIndex].flags.Paralizado = 0;
			Declaraciones.Npclist[NpcIndex].flags.Inmovilizado = 0;
		}

	}

	static void EfectoCegueEstu(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		if (Declaraciones.UserList[UserIndex].Counters.Ceguera > 0) {
			Declaraciones.UserList[UserIndex].Counters.Ceguera = Declaraciones.UserList[UserIndex].Counters.Ceguera - 1;
		} else {
			if (Declaraciones.UserList[UserIndex].flags.Ceguera == 1) {
				Declaraciones.UserList[UserIndex].flags.Ceguera = 0;
				Protocol.WriteBlindNoMore(UserIndex);
			}
			if (Declaraciones.UserList[UserIndex].flags.Estupidez == 1) {
				Declaraciones.UserList[UserIndex].flags.Estupidez = 0;
				Protocol.WriteDumbNoMore(UserIndex);
			}

		}

	}

	static void EfectoParalisisUser(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 02/12/2010 */
		/*
		 * '02/12/2010: ZaMa - Now non-magic clases lose paralisis effect under
		 * certain circunstances.
		 */
		/* '*************************************************** */

		if (Declaraciones.UserList[UserIndex].Counters.Paralisis > 0) {

			int CasterIndex;
			CasterIndex = Declaraciones.UserList[UserIndex].flags.ParalizedByIndex;

			/* ' Only aplies to non-magic clases */
			if (Declaraciones.UserList[UserIndex].Stats.MaxMAN == 0) {
				/* ' Paralized by user? */
				if (CasterIndex != 0) {

					/* ' Close? => Remove Paralisis */
					if (Declaraciones.UserList[CasterIndex].Name != Declaraciones.UserList[UserIndex].flags.ParalizedBy) {
						RemoveParalisis(UserIndex);
						return;

						/* ' Caster dead? => Remove Paralisis */
					} else if (Declaraciones.UserList[CasterIndex].flags.Muerto == 1) {
						RemoveParalisis(UserIndex);
						return;

					} else if (Declaraciones.UserList[UserIndex].Counters.Paralisis > Admin.IntervaloParalizadoReducido) {
						/* ' Out of vision range? => Reduce paralisis counter */
						if (!Extra.InVisionRangeAndMap(UserIndex, Declaraciones.UserList[CasterIndex].Pos)) {
							/* ' Aprox. 1500 ms */
							Declaraciones.UserList[UserIndex].Counters.Paralisis = Admin.IntervaloParalizadoReducido;
							return;
						}
					}

					/* ' Npc? */
				} else {
					CasterIndex = Declaraciones.UserList[UserIndex].flags.ParalizedByNpcIndex;

					/* ' Paralized by npc? */
					if (CasterIndex != 0) {

						if (Declaraciones.UserList[UserIndex].Counters.Paralisis > Admin.IntervaloParalizadoReducido) {
							/*
							 * ' Out of vision range? => Reduce paralisis
							 * counter
							 */
							if (!Extra.InVisionRangeAndMap(UserIndex, Declaraciones.Npclist[CasterIndex].Pos)) {
								/* ' Aprox. 1500 ms */
								Declaraciones.UserList[UserIndex].Counters.Paralisis = Admin.IntervaloParalizadoReducido;
								return;
							}
						}
					}

				}
			}

			Declaraciones.UserList[UserIndex].Counters.Paralisis = Declaraciones.UserList[UserIndex].Counters.Paralisis
					- 1;

		} else {
			RemoveParalisis(UserIndex);
		}

	}

	static void RemoveParalisis(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 20/11/2010 */
		/* 'Removes paralisis effect from user. */
		/* '*************************************************** */
		Declaraciones.UserList[UserIndex].flags.Paralizado = 0;
		Declaraciones.UserList[UserIndex].flags.Inmovilizado = 0;
		Declaraciones.UserList[UserIndex].flags.ParalizedBy = "";
		Declaraciones.UserList[UserIndex].flags.ParalizedByIndex = 0;
		Declaraciones.UserList[UserIndex].flags.ParalizedByNpcIndex = 0;
		Declaraciones.UserList[UserIndex].Counters.Paralisis = 0;
		Protocol.WriteParalizeOK(UserIndex);
	}

	static void RecStamina(int UserIndex, boolean /* FIXME BYREF!! */ EnviarStats, int Intervalo) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		if (Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].trigger == 1
				&& Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].trigger == 2
				&& Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].trigger == 4) {
			return;
		}

		int massta;
		if (Declaraciones.UserList[UserIndex].Stats.MinSta < Declaraciones.UserList[UserIndex].Stats.MaxSta) {
			if (Declaraciones.UserList[UserIndex].Counters.STACounter < Intervalo) {
				Declaraciones.UserList[UserIndex].Counters.STACounter = Declaraciones.UserList[UserIndex].Counters.STACounter
						+ 1;
			} else {
				EnviarStats = true;
				Declaraciones.UserList[UserIndex].Counters.STACounter = 0;
				/* 'Desnudo no sube energía. (ToxicWaste) */
				if (Declaraciones.UserList[UserIndex].flags.Desnudo) {
					return;
				}

				massta = Matematicas.RandomNumber(1,
						Matematicas.Porcentaje(Declaraciones.UserList[UserIndex].Stats.MaxSta, 5));
				Declaraciones.UserList[UserIndex].Stats.MinSta = Declaraciones.UserList[UserIndex].Stats.MinSta
						+ massta;
				if (Declaraciones.UserList[UserIndex].Stats.MinSta > Declaraciones.UserList[UserIndex].Stats.MaxSta) {
					Declaraciones.UserList[UserIndex].Stats.MinSta = Declaraciones.UserList[UserIndex].Stats.MaxSta;
				}
			}
		}

	}

	static void EfectoVeneno(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int N;

		if (Declaraciones.UserList[UserIndex].Counters.Veneno < Admin.IntervaloVeneno) {
			Declaraciones.UserList[UserIndex].Counters.Veneno = Declaraciones.UserList[UserIndex].Counters.Veneno + 1;
		} else {
			Protocol.WriteConsoleMsg(UserIndex, "Estás envenenado, si no te curas morirás.",
					FontTypeNames.FONTTYPE_VENENO);
			Declaraciones.UserList[UserIndex].Counters.Veneno = 0;
			N = Matematicas.RandomNumber(1, 5);
			Declaraciones.UserList[UserIndex].Stats.MinHp = Declaraciones.UserList[UserIndex].Stats.MinHp - N;
			if (Declaraciones.UserList[UserIndex].Stats.MinHp < 1) {
				UsUaRiOs.UserDie(UserIndex);
			}
			Protocol.WriteUpdateHP(UserIndex);
		}

	}

	static void DuracionPociones(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: ?????? */
		/* 'Last Modification: 11/27/09 (Budi) */
		/*
		 * 'Cuando se pierde el efecto de la poción updatea fz y agi (No me
		 * gusta que ambos atributos aunque se haya modificado solo uno, pero
		 * bueno :p)
		 */
		/* '*************************************************** */
		/* 'Controla la duracion de las pociones */
		if (Declaraciones.UserList[UserIndex].flags.DuracionEfecto > 0) {
			Declaraciones.UserList[UserIndex].flags.DuracionEfecto = Declaraciones.UserList[UserIndex].flags.DuracionEfecto
					- 1;
			if (Declaraciones.UserList[UserIndex].flags.DuracionEfecto == 0) {
				Declaraciones.UserList[UserIndex].flags.TomoPocion = false;
				Declaraciones.UserList[UserIndex].flags.TipoPocion = 0;
				/* 'volvemos los atributos al estado normal */
				int loopX;

				for (loopX = (1); loopX <= (Declaraciones.NUMATRIBUTOS); loopX++) {
					Declaraciones.UserList[UserIndex].Stats.UserAtributos[loopX] = Declaraciones.UserList[UserIndex].Stats.UserAtributosBackUP[loopX];
				}

				Protocol.WriteUpdateStrenghtAndDexterity(UserIndex);
			}
		}

	}

	static void HambreYSed(int UserIndex, boolean /* FIXME BYREF!! */ fenviarAyS) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		if (!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) {
			return;
		}

		/* 'Sed */
		if (Declaraciones.UserList[UserIndex].Stats.MinAGU > 0) {
			if (Declaraciones.UserList[UserIndex].Counters.AGUACounter < Admin.IntervaloSed) {
				Declaraciones.UserList[UserIndex].Counters.AGUACounter = Declaraciones.UserList[UserIndex].Counters.AGUACounter
						+ 1;
			} else {
				Declaraciones.UserList[UserIndex].Counters.AGUACounter = 0;
				Declaraciones.UserList[UserIndex].Stats.MinAGU = Declaraciones.UserList[UserIndex].Stats.MinAGU - 10;

				if (Declaraciones.UserList[UserIndex].Stats.MinAGU <= 0) {
					Declaraciones.UserList[UserIndex].Stats.MinAGU = 0;
					Declaraciones.UserList[UserIndex].flags.Sed = 1;
				}

				fenviarAyS = true;
			}
		}

		/* 'hambre */
		if (Declaraciones.UserList[UserIndex].Stats.MinHam > 0) {
			if (Declaraciones.UserList[UserIndex].Counters.COMCounter < Admin.IntervaloHambre) {
				Declaraciones.UserList[UserIndex].Counters.COMCounter = Declaraciones.UserList[UserIndex].Counters.COMCounter
						+ 1;
			} else {
				Declaraciones.UserList[UserIndex].Counters.COMCounter = 0;
				Declaraciones.UserList[UserIndex].Stats.MinHam = Declaraciones.UserList[UserIndex].Stats.MinHam - 10;
				if (Declaraciones.UserList[UserIndex].Stats.MinHam <= 0) {
					Declaraciones.UserList[UserIndex].Stats.MinHam = 0;
					Declaraciones.UserList[UserIndex].flags.Hambre = 1;
				}
				fenviarAyS = true;
			}
		}

	}

	static void Sanar(int UserIndex, boolean /* FIXME BYREF!! */ EnviarStats, int Intervalo) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		if (Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].trigger == 1
				&& Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].trigger == 2
				&& Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].trigger == 4) {
			return;
		}

		int mashit;
		/* 'con el paso del tiempo va sanando....pero muy lentamente ;-) */
		if (Declaraciones.UserList[UserIndex].Stats.MinHp < Declaraciones.UserList[UserIndex].Stats.MaxHp) {
			if (Declaraciones.UserList[UserIndex].Counters.HPCounter < Intervalo) {
				Declaraciones.UserList[UserIndex].Counters.HPCounter = Declaraciones.UserList[UserIndex].Counters.HPCounter
						+ 1;
			} else {
				mashit = Matematicas.RandomNumber(2,
						Matematicas.Porcentaje(Declaraciones.UserList[UserIndex].Stats.MaxSta, 5));

				Declaraciones.UserList[UserIndex].Counters.HPCounter = 0;
				Declaraciones.UserList[UserIndex].Stats.MinHp = Declaraciones.UserList[UserIndex].Stats.MinHp + mashit;
				if (Declaraciones.UserList[UserIndex].Stats.MinHp > Declaraciones.UserList[UserIndex].Stats.MaxHp) {
					Declaraciones.UserList[UserIndex].Stats.MinHp = Declaraciones.UserList[UserIndex].Stats.MaxHp;
				}
				Protocol.WriteConsoleMsg(UserIndex, "Has sanado.", FontTypeNames.FONTTYPE_INFO);
				EnviarStats = true;
			}
		}

	}

	static void CargaNpcsDat() {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		String npcfile;

		npcfile = Declaraciones.DatPath + "NPCs.dat";
		General.LeerNPCs = new clsIniManager();
		General.LeerNPCs.Initialize(npcfile);
	}

	static void PasarSegundo() {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */
		int i;

		for (i = (1); i <= (Declaraciones.LastUser); i++) {
			if (Declaraciones.UserList[i].flags.UserLogged) {
				/* 'Cerrar usuario */
				if (Declaraciones.UserList[i].Counters.Saliendo) {
					Declaraciones.UserList[i].Counters.Salir = Declaraciones.UserList[i].Counters.Salir - 1;
					if (Declaraciones.UserList[i].Counters.Salir <= 0) {
						Protocol.WriteConsoleMsg(i, "Gracias por jugar Argentum Online", FontTypeNames.FONTTYPE_INFO);
						Protocol.WriteDisconnect(i);
						Protocol.FlushBuffer(i);

						TCP.CloseSocket(i);
					}
				}
			}
		}
		return;

		/* FIXME: ErrHandler : */
		LogError("Error en PasarSegundo. Err: " + Err.description + " - " + Err.Number + " - UserIndex: " + i);
		/* FIXME: RESUME NEXT */
	}

	static double ReiniciarAutoUpdate() {
		double retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		retval = vb6.Shell(vb6.App.Instance().Path + "\\autoupdater\\aoau.exe", vbMinimizedNoFocus);

		return retval;
	}

	static void ReiniciarServidor() {
		ReiniciarServidor(true);
	}

	static void ReiniciarServidor(boolean EjecutarLauncher) {
 /* '*************************************************** */
 /* 'Author: Unknown */
 /* 'Last Modification: - */
 /* ' */
 /* '*************************************************** */
 
 /* 'WorldSave */
 ES.DoBackUp;
 
 /* 'commit experiencias */
 mdParty.ActualizaExperiencias;
 
 /* 'Guardar Pjs */
 GuardarUsuarios();
 
 if (EjecutarLauncher) {
 vb6.Shell(vb6.App.Instance().Path + "\\launcher.exe");
 }
 
 /* 'Chauuu */
 Unload(frmMain);
 
}

	static void GuardarUsuarios() {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		Declaraciones.haciendoBK = true;

		modSendData.SendData(SendTarget.ToAll, 0, Protocol.PrepareMessagePauseToggle());
		modSendData.SendData(SendTarget.ToAll, 0,
				Protocol.PrepareMessageConsoleMsg("Servidor> Grabando Personajes", FontTypeNames.FONTTYPE_SERVER));

		int i;
		for (i = (1); i <= (Declaraciones.LastUser); i++) {
			if (Declaraciones.UserList[i].flags.UserLogged) {
				ES.SaveUser(i, Declaraciones.CharPath + vb6.UCase(Declaraciones.UserList[i].Name) + ".chr", false);
			}
		}

		/* 'se guardan los seguimientos */
		modUserRecords.SaveRecords();

		modSendData.SendData(SendTarget.ToAll, 0,
				Protocol.PrepareMessageConsoleMsg("Servidor> Personajes Grabados", FontTypeNames.FONTTYPE_SERVER));
		modSendData.SendData(SendTarget.ToAll, 0, Protocol.PrepareMessagePauseToggle());

		Declaraciones.haciendoBK = false;
	}

	static void InicializaEstadisticas() {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int Ta;
		Ta = Declaraciones.GetTickCount() && 0x7FFFFFFF;

		Admin.EstadisticasWeb = new clsEstadisticasIPC();
		Admin.EstadisticasWeb.Inicializa(frmMain.hWnd);
		Admin.EstadisticasWeb.Informar(CANTIDAD_MAPAS, Declaraciones.NumMaps);
		Admin.EstadisticasWeb.Informar(CANTIDAD_ONLINE, Declaraciones.NumUsers);
		Admin.EstadisticasWeb.Informar(UPTIME_SERVER, modNuevoTimer.getInterval(Ta, Admin.tInicioServer) / 1000);
		Admin.EstadisticasWeb.Informar(RECORD_USUARIOS, Declaraciones.RECORDusuarios);

	}

	static void FreeNPCs() {
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* 'Releases all NPC Indexes */
		/* '*************************************************** */
		int LoopC;

		/* ' Free all NPC indexes */
		for (LoopC = (1); LoopC <= (Declaraciones.MAXNPCS); LoopC++) {
			Declaraciones.Npclist[LoopC].flags.NPCActive = false;
		}
	}

	static void FreeCharIndexes() {
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* 'Releases all char indexes */
		/* '*************************************************** */
		/* ' Free all char indexes (set them all to 0) */
		Declaraciones.ZeroMemory(Declaraciones.CharList[1],
				Declaraciones.MAXCHARS * vb6.Len(Declaraciones.CharList[1]));
	}

}