
/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"ES"')] */
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

public class ES {

	static void CargarSpawnList() {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int N;
		int LoopC;
		N = vb6.val(GetVar(vb6.App.Instance().Path + "\\Dat\\Invokar.dat", "INIT", "NumNPCs"));
		Declaraciones.SpawnList = new tCriaturasEntrenador[0];
		Declaraciones.SpawnList = (Declaraciones.SpawnList == null) ? new tCriaturasEntrenador[N]
				: java.util.Arrays.copyOf(Declaraciones.SpawnList, N);
		for (LoopC = (1); LoopC <= (N); LoopC++) {
			Declaraciones.SpawnList[LoopC].NpcIndex = vb6
					.val(GetVar(vb6.App.Instance().Path + "\\Dat\\Invokar.dat", "LIST", "NI" + LoopC));
			Declaraciones.SpawnList[LoopC].NpcName = GetVar(vb6.App.Instance().Path + "\\Dat\\Invokar.dat", "LIST",
					"NN" + LoopC);
		}

	}

	static boolean EsAdmin(String /* FIXME BYREF!! */ Name) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 27/03/2011 */
		/* '27/03/2011 - ZaMa: Utilizo la clase para saber los datos. */
		/* '*************************************************** */
		retval = (vb6.val(Declaraciones.Administradores.GetValue("Admin", vb6.Replace(Name, "+", " "))) == 1);
		return retval;
	}

	static boolean EsDios(String /* FIXME BYREF!! */ Name) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 27/03/2011 */
		/* '27/03/2011 - ZaMa: Utilizo la clase para saber los datos. */
		/* '*************************************************** */
		retval = (vb6.val(Declaraciones.Administradores.GetValue("Dios", vb6.Replace(Name, "+", " "))) == 1);
		return retval;
	}

	static boolean EsSemiDios(String /* FIXME BYREF!! */ Name) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 27/03/2011 */
		/* '27/03/2011 - ZaMa: Utilizo la clase para saber los datos. */
		/* '*************************************************** */
		retval = (vb6.val(Declaraciones.Administradores.GetValue("SemiDios", vb6.Replace(Name, "+", " "))) == 1);
		return retval;
	}

	static boolean EsGmEspecial(String /* FIXME BYREF!! */ Name) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 27/03/2011 */
		/* '27/03/2011 - ZaMa: Utilizo la clase para saber los datos. */
		/* '*************************************************** */
		retval = (vb6.val(Declaraciones.Administradores.GetValue("Especial", vb6.Replace(Name, "+", " "))) == 1);
		return retval;
	}

	static boolean EsConsejero(String /* FIXME BYREF!! */ Name) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 27/03/2011 */
		/* '27/03/2011 - ZaMa: Utilizo la clase para saber los datos. */
		/* '*************************************************** */
		retval = (vb6.val(Declaraciones.Administradores.GetValue("Consejero", vb6.Replace(Name, "+", " "))) == 1);
		return retval;
	}

	static boolean EsRolesMaster(String /* FIXME BYREF!! */ Name) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 27/03/2011 */
		/* '27/03/2011 - ZaMa: Utilizo la clase para saber los datos. */
		/* '*************************************************** */
		retval = (vb6.val(Declaraciones.Administradores.GetValue("RM", vb6.Replace(Name, "+", " "))) == 1);
		return retval;
	}

	static boolean EsGmChar(String /* FIXME BYREF!! */ Name) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 27/03/2011 */
		/* 'Returns true if char is administrative user. */
		/* '*************************************************** */

		boolean EsGm;

		/* ' Admin? */
		EsGm = EsAdmin(Name);
		/* ' Dios? */
		if (!EsGm) {
			EsGm = EsDios(Name);
		}
		/* ' Semidios? */
		if (!EsGm) {
			EsGm = EsSemiDios(Name);
		}
		/* ' Consejero? */
		if (!EsGm) {
			EsGm = EsConsejero(Name);
		}

		retval = EsGm;

		return retval;
	}

	static void loadAdministrativeUsers() {
		/* 'Admines => Admin */
		/* 'Dioses => Dios */
		/* 'SemiDioses => SemiDios */
		/* 'Especiales => Especial */
		/* 'Consejeros => Consejero */
		/* 'RoleMasters => RM */

		/*
		 * 'Si esta mierda tuviese array asociativos el código sería tan lindo.
		 */
		int buf;
		int i;
		String Name;

		/* ' Public container */
		Declaraciones.Administradores = new clsIniManager();

		/* ' Server ini info file */
		clsIniManager ServerIni;
		ServerIni = new clsIniManager();

		ServerIni.Initialize(Declaraciones.IniPath + "Server.ini");

		/* ' Admines */
		buf = vb6.val(ServerIni.GetValue("INIT", "Admines"));

		for (i = (1); i <= (buf); i++) {
			Name = vb6.UCase(ServerIni.GetValue("Admines", "Admin" + i));

			if (vb6.Left(Name, 1) == "*" || vb6.Left(Name, 1) == "+") {
				Name = vb6.Right(Name, vb6.Len(Name) - 1);
			}

			/* ' Add key */
			Declaraciones.Administradores.ChangeValue("Admin", Name, "1");

		}

		/* ' Dioses */
		buf = vb6.val(ServerIni.GetValue("INIT", "Dioses"));

		for (i = (1); i <= (buf); i++) {
			Name = vb6.UCase(ServerIni.GetValue("Dioses", "Dios" + i));

			if (vb6.Left(Name, 1) == "*" || vb6.Left(Name, 1) == "+") {
				Name = vb6.Right(Name, vb6.Len(Name) - 1);
			}

			/* ' Add key */
			Declaraciones.Administradores.ChangeValue("Dios", Name, "1");

		}

		/* ' Especiales */
		buf = vb6.val(ServerIni.GetValue("INIT", "Especiales"));

		for (i = (1); i <= (buf); i++) {
			Name = vb6.UCase(ServerIni.GetValue("Especiales", "Especial" + i));

			if (vb6.Left(Name, 1) == "*" || vb6.Left(Name, 1) == "+") {
				Name = vb6.Right(Name, vb6.Len(Name) - 1);
			}

			/* ' Add key */
			Declaraciones.Administradores.ChangeValue("Especial", Name, "1");

		}

		/* ' SemiDioses */
		buf = vb6.val(ServerIni.GetValue("INIT", "SemiDioses"));

		for (i = (1); i <= (buf); i++) {
			Name = vb6.UCase(ServerIni.GetValue("SemiDioses", "SemiDios" + i));

			if (vb6.Left(Name, 1) == "*" || vb6.Left(Name, 1) == "+") {
				Name = vb6.Right(Name, vb6.Len(Name) - 1);
			}

			/* ' Add key */
			Declaraciones.Administradores.ChangeValue("SemiDios", Name, "1");

		}

		/* ' Consejeros */
		buf = vb6.val(ServerIni.GetValue("INIT", "Consejeros"));

		for (i = (1); i <= (buf); i++) {
			Name = vb6.UCase(ServerIni.GetValue("Consejeros", "Consejero" + i));

			if (vb6.Left(Name, 1) == "*" || vb6.Left(Name, 1) == "+") {
				Name = vb6.Right(Name, vb6.Len(Name) - 1);
			}

			/* ' Add key */
			Declaraciones.Administradores.ChangeValue("Consejero", Name, "1");

		}

		/* ' RolesMasters */
		buf = vb6.val(ServerIni.GetValue("INIT", "RolesMasters"));

		for (i = (1); i <= (buf); i++) {
			Name = vb6.UCase(ServerIni.GetValue("RolesMasters", "RM" + i));

			if (vb6.Left(Name, 1) == "*" || vb6.Left(Name, 1) == "+") {
				Name = vb6.Right(Name, vb6.Len(Name) - 1);
			}

			/* ' Add key */
			Declaraciones.Administradores.ChangeValue("RM", Name, "1");
		}

		ServerIni = null;

	}

	static PlayerType GetCharPrivs(String /* FIXME BYREF!! */ UserName) {
		PlayerType retval;
		/* '**************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 18/11/2010 */
		/* 'Reads the user's charfile and retrieves its privs. */
		/* '*************************************************** */

		PlayerType Privs;

		if (EsAdmin(UserName)) {
			Privs = PlayerType.Admin;

		} else if (EsDios(UserName)) {
			Privs = PlayerType.Dios;

		} else if (EsSemiDios(UserName)) {
			Privs = PlayerType.SemiDios;

		} else if (EsConsejero(UserName)) {
			Privs = PlayerType.Consejero;

		} else {
			Privs = PlayerType.User;
		}

		retval = Privs;

		return retval;
	}

	static int TxtDimension(String Name) {
		int retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int N;
		String cad;
		int Tam;
		N = vb6.FreeFile(1);
		/* FIXME: OPEN Name FOR INPUT AS # N */
		Tam = 0;
		while (!clsByteBuffer.Eof(N)) {
			Tam = Tam + 1;
			/* FIXME: LINE INPUT # N , cad */
		}
		/* FIXME: CLOSE N */
		retval = Tam;
		return retval;
	}

	static void CargarForbidenWords() {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		Declaraciones.ForbidenNames = new None[0];
		Declaraciones.ForbidenNames = (Declaraciones.ForbidenNames == null)
				? new None[1 + TxtDimension(Declaraciones.DatPath + "NombresInvalidos.txt")]
				: java.util.Arrays.copyOf(Declaraciones.ForbidenNames,
						1 + TxtDimension(Declaraciones.DatPath + "NombresInvalidos.txt"));
		int N;
		int i;
		N = vb6.FreeFile(1);
		/* FIXME: OPEN DatPath & "NombresInvalidos.txt" FOR INPUT AS # N */

		for (i = (1); i <= (vb6.UBound(Declaraciones.ForbidenNames)); i++) {
			/* FIXME: LINE INPUT # N , ForbidenNames ( i ) */
		}

		/* FIXME: CLOSE N */

	}

	static void CargarHechizos() {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* '################################################### */
		/* '# ATENCION PELIGRO # */
		/* '################################################### */
		/* ' */
		/* ' ¡¡¡¡ NO USAR GetVar PARA LEER Hechizos.dat !!!! */
		/* ' */
		/* 'El que ose desafiar esta LEY, se las tendrá que ver */
		/* 'con migo. Para leer Hechizos.dat se deberá usar */
		/* 'la nueva clase clsLeerInis. */
		/* ' */
		/* 'Alejo */
		/* ' */
		/* '################################################### */

		/* FIXME: ON ERROR GOTO ErrHandler */

		if (frmMain.Visible) {
			frmMain.txStatus.Caption = "Cargando Hechizos.";
		}

		int Hechizo;
		clsIniManager Leer;
		Leer = new clsIniManager();

		Leer.Initialize(Declaraciones.DatPath + "Hechizos.dat");

		/* 'obtiene el numero de hechizos */
		Declaraciones.NumeroHechizos = vb6.val(Leer.GetValue("INIT", "NumeroHechizos"));

		Declaraciones.Hechizos = new tHechizo[0];
		Declaraciones.Hechizos = (Declaraciones.Hechizos == null) ? new tHechizo[1 + Declaraciones.NumeroHechizos]
				: java.util.Arrays.copyOf(Declaraciones.Hechizos, 1 + Declaraciones.NumeroHechizos);

		frmCargando.cargar.min = 0;
		frmCargando.cargar.max = Declaraciones.NumeroHechizos;
		frmCargando.cargar.value = 0;

		/* 'Llena la lista */
		for (Hechizo = (1); Hechizo <= (Declaraciones.NumeroHechizos); Hechizo++) {
			Declaraciones.Hechizos[Hechizo].Nombre = Leer.GetValue("Hechizo" + Hechizo, "Nombre");
			Declaraciones.Hechizos[Hechizo].desc = Leer.GetValue("Hechizo" + Hechizo, "Desc");
			Declaraciones.Hechizos[Hechizo].PalabrasMagicas = Leer.GetValue("Hechizo" + Hechizo, "PalabrasMagicas");

			Declaraciones.Hechizos[Hechizo].HechizeroMsg = Leer.GetValue("Hechizo" + Hechizo, "HechizeroMsg");
			Declaraciones.Hechizos[Hechizo].TargetMsg = Leer.GetValue("Hechizo" + Hechizo, "TargetMsg");
			Declaraciones.Hechizos[Hechizo].PropioMsg = Leer.GetValue("Hechizo" + Hechizo, "PropioMsg");

			Declaraciones.Hechizos[Hechizo].Tipo = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "Tipo"));
			Declaraciones.Hechizos[Hechizo].WAV = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "WAV"));
			Declaraciones.Hechizos[Hechizo].FXgrh = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "Fxgrh"));

			Declaraciones.Hechizos[Hechizo].loops = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "Loops"));

			/* ' .Resis = val(Leer.GetValue("Hechizo" & Hechizo, "Resis")) */

			Declaraciones.Hechizos[Hechizo].SubeHP = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "SubeHP"));
			Declaraciones.Hechizos[Hechizo].MinHp = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "MinHP"));
			Declaraciones.Hechizos[Hechizo].MaxHp = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "MaxHP"));

			Declaraciones.Hechizos[Hechizo].SubeMana = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "SubeMana"));
			Declaraciones.Hechizos[Hechizo].MiMana = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "MinMana"));
			Declaraciones.Hechizos[Hechizo].MaMana = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "MaxMana"));

			Declaraciones.Hechizos[Hechizo].SubeSta = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "SubeSta"));
			Declaraciones.Hechizos[Hechizo].MinSta = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "MinSta"));
			Declaraciones.Hechizos[Hechizo].MaxSta = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "MaxSta"));

			Declaraciones.Hechizos[Hechizo].SubeHam = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "SubeHam"));
			Declaraciones.Hechizos[Hechizo].MinHam = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "MinHam"));
			Declaraciones.Hechizos[Hechizo].MaxHam = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "MaxHam"));

			Declaraciones.Hechizos[Hechizo].SubeSed = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "SubeSed"));
			Declaraciones.Hechizos[Hechizo].MinSed = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "MinSed"));
			Declaraciones.Hechizos[Hechizo].MaxSed = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "MaxSed"));

			Declaraciones.Hechizos[Hechizo].SubeAgilidad = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "SubeAG"));
			Declaraciones.Hechizos[Hechizo].MinAgilidad = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "MinAG"));
			Declaraciones.Hechizos[Hechizo].MaxAgilidad = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "MaxAG"));

			Declaraciones.Hechizos[Hechizo].SubeFuerza = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "SubeFU"));
			Declaraciones.Hechizos[Hechizo].MinFuerza = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "MinFU"));
			Declaraciones.Hechizos[Hechizo].MaxFuerza = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "MaxFU"));

			Declaraciones.Hechizos[Hechizo].SubeCarisma = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "SubeCA"));
			Declaraciones.Hechizos[Hechizo].MinCarisma = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "MinCA"));
			Declaraciones.Hechizos[Hechizo].MaxCarisma = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "MaxCA"));

			Declaraciones.Hechizos[Hechizo].Invisibilidad = vb6
					.val(Leer.GetValue("Hechizo" + Hechizo, "Invisibilidad"));
			Declaraciones.Hechizos[Hechizo].Paraliza = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "Paraliza"));
			Declaraciones.Hechizos[Hechizo].Inmoviliza = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "Inmoviliza"));
			Declaraciones.Hechizos[Hechizo].RemoverParalisis = vb6
					.val(Leer.GetValue("Hechizo" + Hechizo, "RemoverParalisis"));
			Declaraciones.Hechizos[Hechizo].RemoverEstupidez = vb6
					.val(Leer.GetValue("Hechizo" + Hechizo, "RemoverEstupidez"));
			Declaraciones.Hechizos[Hechizo].RemueveInvisibilidadParcial = vb6
					.val(Leer.GetValue("Hechizo" + Hechizo, "RemueveInvisibilidadParcial"));

			Declaraciones.Hechizos[Hechizo].CuraVeneno = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "CuraVeneno"));
			Declaraciones.Hechizos[Hechizo].Envenena = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "Envenena"));
			Declaraciones.Hechizos[Hechizo].Maldicion = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "Maldicion"));
			Declaraciones.Hechizos[Hechizo].RemoverMaldicion = vb6
					.val(Leer.GetValue("Hechizo" + Hechizo, "RemoverMaldicion"));
			Declaraciones.Hechizos[Hechizo].Bendicion = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "Bendicion"));
			Declaraciones.Hechizos[Hechizo].Revivir = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "Revivir"));

			Declaraciones.Hechizos[Hechizo].Ceguera = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "Ceguera"));
			Declaraciones.Hechizos[Hechizo].Estupidez = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "Estupidez"));

			Declaraciones.Hechizos[Hechizo].Warp = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "Warp"));

			Declaraciones.Hechizos[Hechizo].Invoca = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "Invoca"));
			Declaraciones.Hechizos[Hechizo].NumNpc = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "NumNpc"));
			Declaraciones.Hechizos[Hechizo].cant = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "Cant"));
			Declaraciones.Hechizos[Hechizo].Mimetiza = vb6.val(Leer.GetValue("hechizo" + Hechizo, "Mimetiza"));

			/*
			 * ' .Materializa = val(Leer.GetValue("Hechizo" & Hechizo,
			 * "Materializa"))
			 */
			/*
			 * ' .ItemIndex = val(Leer.GetValue("Hechizo" & Hechizo,
			 * "ItemIndex"))
			 */

			Declaraciones.Hechizos[Hechizo].MinSkill = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "MinSkill"));
			Declaraciones.Hechizos[Hechizo].ManaRequerido = vb6
					.val(Leer.GetValue("Hechizo" + Hechizo, "ManaRequerido"));

			/* 'Barrin 30/9/03 */
			Declaraciones.Hechizos[Hechizo].StaRequerido = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "StaRequerido"));

			Declaraciones.Hechizos[Hechizo].Target = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "Target"));
			frmCargando.cargar.value = frmCargando.cargar.value + 1;

			Declaraciones.Hechizos[Hechizo].NeedStaff = vb6.val(Leer.GetValue("Hechizo" + Hechizo, "NeedStaff"));
			Declaraciones.Hechizos[Hechizo].StaffAffected = vb6
					.CBool(vb6.val(Leer.GetValue("Hechizo" + Hechizo, "StaffAffected")));
		}

		Leer = null;

		return;

		/* FIXME: ErrHandler : */
		vb6.MsgBox("Error cargando hechizos.dat " + Err.Number + ": " + Err.description);

	}

	static void LoadMotd() {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int i;

		Admin.MaxLines = vb6.val(GetVar(vb6.App.Instance().Path + "\\Dat\\Motd.ini", "INIT", "NumLines"));

		Admin.MOTD = new None[0];
		Admin.MOTD = (Admin.MOTD == null) ? new None[1 + Admin.MaxLines]
				: java.util.Arrays.copyOf(Admin.MOTD, 1 + Admin.MaxLines);
		for (i = (1); i <= (Admin.MaxLines); i++) {
			Admin.MOTD[i].texto = GetVar(vb6.App.Instance().Path + "\\Dat\\Motd.ini", "Motd", "Line" + i);
			Admin.MOTD[i].Formato = "";
		}

	}

	static void DoBackUp() {
 /* '*************************************************** */
 /* 'Author: Unknown */
 /* 'Last Modification: - */
 /* ' */
 /* '*************************************************** */
 
 Declaraciones.haciendoBK = true;
 
 /* ' Lo saco porque elimina elementales y mascotas - Maraxus */
 /* ''''''''''''''lo pongo aca x sugernecia del yind */
 /* 'For i = 1 To LastNPC */
 /* '    If Npclist(i).flags.NPCActive Then */
 /* '        If Npclist(i).Contadores.TiempoExistencia > 0 Then */
 /* '            Call MuereNpc(i, 0) */
 /* '        End If */
 /* '    End If */
 /* 'Next i */
 /* '''''''''''/'lo pongo aca x sugernecia del yind */
 
 modSendData.SendData(SendTarget.ToAll, 0, Protocol.PrepareMessagePauseToggle());
 
 General.LimpiarMundo();
 Admin.WorldSave();
 modGuilds.v_RutinaElecciones;
 /* 'Call ResetCentinelaInfo     'Reseteamos al centinela 'Lo saco porque ahora el reset lo maneja el modCentinela [C4b3z0n] */
 
 modSendData.SendData(SendTarget.ToAll, 0, Protocol.PrepareMessagePauseToggle());
 
 /* 'Call EstadisticasWeb.Informar(EVENTO_NUEVO_CLAN, 0) */
 
 Declaraciones.haciendoBK = false;
 
 /* 'Log */
 /* FIXME: ON ERROR RESUME NEXT */
 int nfile;
 /* ' obtenemos un canal */
 nfile = vb6.FreeFile();
 /* FIXME: OPEN App . Path & "\\logs\\BackUps.log" FOR Append Shared AS # nfile */
 /* FIXME: PRINT # nfile , Date & " " & time */
 /* FIXME: CLOSE # nfile */
}

	static void GrabarMapa(int Map, String /* FIXME BYREF!! */ MAPFILE) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 12/01/2011 */
		/*
		 * '10/08/2010 - Pato: Implemento el clsByteBuffer para el grabado de
		 * mapas
		 */
		/* '28/10/2010:ZaMa - Ahora no se hace backup de los pretorianos. */
		/*
		 * '12/01/2011 - Amraphen: Ahora no se hace backup de NPCs prohibidos
		 * (Pretorianos, Mascotas, Invocados y Centinela)
		 */
		/* '*************************************************** */

		/* FIXME: ON ERROR RESUME NEXT */
		int FreeFileMap;
		int FreeFileInf;
		int Y;
		int X;
		int ByFlags;
		int LoopC;
		clsByteBuffer MapWriter;
		clsByteBuffer InfWriter;
		clsIniManager IniManager;
		boolean NpcInvalido;

		MapWriter = new clsByteBuffer();
		InfWriter = new clsByteBuffer();
		IniManager = new clsIniManager();

		if (General.FileExist(MAPFILE + ".map", 0)) {
			/* FIXME: KILL MAPFILE & ".map" */
		}

		if (General.FileExist(MAPFILE + ".inf", 0)) {
			/* FIXME: KILL MAPFILE & ".inf" */
		}

		/* 'Open .map file */
		FreeFileMap = vb6.FreeFile();
		/* FIXME: OPEN MAPFILE & ".Map" FOR Binary AS FreeFileMap */

		MapWriter.initializeWriter(FreeFileMap);

		/* 'Open .inf file */
		FreeFileInf = vb6.FreeFile();
		/* FIXME: OPEN MAPFILE & ".Inf" FOR Binary AS FreeFileInf */

		InfWriter.initializeWriter(FreeFileInf);

		/* 'map Header */
		MapWriter.putInteger(Declaraciones.MapInfo[Map].MapVersion);

		MapWriter.putString(Declaraciones.MiCabecera.desc, false);
		MapWriter.putLong(Declaraciones.MiCabecera.crc);
		MapWriter.putLong(Declaraciones.MiCabecera.MagicWord);

		MapWriter.putDouble(0);

		/* 'inf Header */
		InfWriter.putDouble(0);
		InfWriter.putInteger(0);

		/* 'Write .map file */
		for (Y = (Declaraciones.YMinMapSize); Y <= (Declaraciones.YMaxMapSize); Y++) {
			for (X = (Declaraciones.XMinMapSize); X <= (Declaraciones.XMaxMapSize); X++) {
				ByFlags = 0;

				if (Declaraciones.MapData[Map][X][Y].Blocked) {
					ByFlags = ByFlags || 1;
				}
				if (Declaraciones.MapData[Map][X][Y].Graphic[2]) {
					ByFlags = ByFlags || 2;
				}
				if (Declaraciones.MapData[Map][X][Y].Graphic[3]) {
					ByFlags = ByFlags || 4;
				}
				if (Declaraciones.MapData[Map][X][Y].Graphic[4]) {
					ByFlags = ByFlags || 8;
				}
				if (Declaraciones.MapData[Map][X][Y].trigger) {
					ByFlags = ByFlags || 16;
				}

				MapWriter.putByte(ByFlags);

				MapWriter.putInteger(Declaraciones.MapData[Map][X][Y].Graphic[1]);

				for (LoopC = (2); LoopC <= (4); LoopC++) {
					if (Declaraciones.MapData[Map][X][Y].Graphic[LoopC]) {
						MapWriter.putInteger(Declaraciones.MapData[Map][X][Y].Graphic[LoopC]);
					}
				}

				if (Declaraciones.MapData[Map][X][Y].trigger) {
					MapWriter.putInteger(vb6.CInt(Declaraciones.MapData[Map][X][Y].trigger));
				}

				/* '.inf file */
				ByFlags = 0;

				if (Declaraciones.MapData[Map][X][Y].ObjInfo.ObjIndex > 0) {
					if (Declaraciones.ObjData[Declaraciones.MapData[Map][X][Y].ObjInfo.ObjIndex].OBJType == eOBJType.otFogata) {
						Declaraciones.MapData[Map][X][Y].ObjInfo.ObjIndex = 0;
						Declaraciones.MapData[Map][X][Y].ObjInfo.Amount = 0;
					}
				}

				if (Declaraciones.MapData[Map][X][Y].TileExit.Map) {
					ByFlags = ByFlags || 1;
				}

				/*
				 * ' No hacer backup de los NPCs inválidos (Pretorianos,
				 * Mascotas, Invocados y Centinela)
				 */
				if (Declaraciones.MapData[Map][X][Y].NpcIndex) {
					NpcInvalido = (Declaraciones.Npclist[Declaraciones.MapData[Map][X][Y].NpcIndex].NPCtype == eNPCType.Pretoriano)
							|| (Declaraciones.Npclist[Declaraciones.MapData[Map][X][Y].NpcIndex].MaestroUser > 0)
							|| modCentinela.EsCentinela(Declaraciones.MapData[Map][X][Y].NpcIndex);

					if (!NpcInvalido) {
						ByFlags = ByFlags || 2;
					}
				}

				if (Declaraciones.MapData[Map][X][Y].ObjInfo.ObjIndex) {
					ByFlags = ByFlags || 4;
				}

				InfWriter.putByte(ByFlags);

				if (Declaraciones.MapData[Map][X][Y].TileExit.Map) {
					InfWriter.putInteger(Declaraciones.MapData[Map][X][Y].TileExit.Map);
					InfWriter.putInteger(Declaraciones.MapData[Map][X][Y].TileExit.X);
					InfWriter.putInteger(Declaraciones.MapData[Map][X][Y].TileExit.Y);
				}

				if (Declaraciones.MapData[Map][X][Y].NpcIndex && !NpcInvalido) {
					InfWriter.putInteger(Declaraciones.Npclist[Declaraciones.MapData[Map][X][Y].NpcIndex].Numero);
				}

				if (Declaraciones.MapData[Map][X][Y].ObjInfo.ObjIndex) {
					InfWriter.putInteger(Declaraciones.MapData[Map][X][Y].ObjInfo.ObjIndex);
					InfWriter.putInteger(Declaraciones.MapData[Map][X][Y].ObjInfo.Amount);
				}

				NpcInvalido = false;
			}
		}

		MapWriter.saveBuffer();
		InfWriter.saveBuffer();

		/* 'Close .map file */
		/* FIXME: CLOSE FreeFileMap */

		/* 'Close .inf file */
		/* FIXME: CLOSE FreeFileInf */

		MapWriter = null;
		InfWriter = null;

		/* 'write .dat file */
		IniManager.ChangeValue("Mapa" + Map, "Name", Declaraciones.MapInfo[Map].Name);
		IniManager.ChangeValue("Mapa" + Map, "MusicNum", Declaraciones.MapInfo[Map].Music);
		IniManager.ChangeValue("Mapa" + Map, "MagiaSinefecto", Declaraciones.MapInfo[Map].MagiaSinEfecto);
		IniManager.ChangeValue("Mapa" + Map, "InviSinEfecto", Declaraciones.MapInfo[Map].InviSinEfecto);
		IniManager.ChangeValue("Mapa" + Map, "ResuSinEfecto", Declaraciones.MapInfo[Map].ResuSinEfecto);
		IniManager.ChangeValue("Mapa" + Map, "StartPos", Declaraciones.MapInfo[Map].StartPos.Map + "-"
				+ Declaraciones.MapInfo[Map].StartPos.X + "-" + Declaraciones.MapInfo[Map].StartPos.Y);
		IniManager.ChangeValue("Mapa" + Map, "OnDeathGoTo", Declaraciones.MapInfo[Map].OnDeathGoTo.Map + "-"
				+ Declaraciones.MapInfo[Map].OnDeathGoTo.X + "-" + Declaraciones.MapInfo[Map].OnDeathGoTo.Y);

		IniManager.ChangeValue("Mapa" + Map, "Terreno", Extra.TerrainByteToString(Declaraciones.MapInfo[Map].Terreno));
		IniManager.ChangeValue("Mapa" + Map, "Zona", Declaraciones.MapInfo[Map].Zona);
		IniManager.ChangeValue("Mapa" + Map, "Restringir",
				Extra.RestrictByteToString(Declaraciones.MapInfo[Map].Restringir));
		IniManager.ChangeValue("Mapa" + Map, "BackUp", vb6.str(Declaraciones.MapInfo[Map].BackUp));

		if (Declaraciones.MapInfo[Map].Pk) {
			IniManager.ChangeValue("Mapa" + Map, "Pk", "0");
		} else {
			IniManager.ChangeValue("Mapa" + Map, "Pk", "1");
		}

		IniManager.ChangeValue("Mapa" + Map, "OcultarSinEfecto", Declaraciones.MapInfo[Map].OcultarSinEfecto);
		IniManager.ChangeValue("Mapa" + Map, "InvocarSinEfecto", Declaraciones.MapInfo[Map].InvocarSinEfecto);
		IniManager.ChangeValue("Mapa" + Map, "NoEncriptarMP", Declaraciones.MapInfo[Map].NoEncriptarMP);
		IniManager.ChangeValue("Mapa" + Map, "RoboNpcsPermitido", Declaraciones.MapInfo[Map].RoboNpcsPermitido);

		IniManager.DumpFile(MAPFILE + ".dat");

		IniManager = null;
	}

	static void LoadArmasHerreria() {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int N;
		int lc;

		N = vb6.val(GetVar(Declaraciones.DatPath + "ArmasHerrero.dat", "INIT", "NumArmas"));

		Declaraciones.ArmasHerrero = (Declaraciones.ArmasHerrero == null) ? new Integer[1 + N]
				: java.util.Arrays.copyOf(Declaraciones.ArmasHerrero, 1 + N);

		for (lc = (1); lc <= (N); lc++) {
			Declaraciones.ArmasHerrero[lc] = vb6
					.val(GetVar(Declaraciones.DatPath + "ArmasHerrero.dat", "Arma" + lc, "Index"));
		}

	}

	static void LoadArmadurasHerreria() {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int N;
		int lc;

		N = vb6.val(GetVar(Declaraciones.DatPath + "ArmadurasHerrero.dat", "INIT", "NumArmaduras"));

		Declaraciones.ArmadurasHerrero = (Declaraciones.ArmadurasHerrero == null) ? new Integer[1 + N]
				: java.util.Arrays.copyOf(Declaraciones.ArmadurasHerrero, 1 + N);

		for (lc = (1); lc <= (N); lc++) {
			Declaraciones.ArmadurasHerrero[lc] = vb6
					.val(GetVar(Declaraciones.DatPath + "ArmadurasHerrero.dat", "Armadura" + lc, "Index"));
		}

	}

	static void LoadBalance() {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 15/04/2010 */
		/* '15/04/2010: ZaMa - Agrego recompensas faccionarias. */
		/* '*************************************************** */

		int i;

		/* 'Modificadores de Clase */
		for (i = (1); i <= (Declaraciones.NUMCLASES); i++) {
			Declaraciones.ModClase[i].Evasion = vb6
					.val(GetVar(Declaraciones.DatPath + "Balance.dat", "MODEVASION", Declaraciones.ListaClases[i]));
			Declaraciones.ModClase[i].AtaqueArmas = vb6
					.val(GetVar(Declaraciones.DatPath + "Balance.dat", "MODATAQUEARMAS", Declaraciones.ListaClases[i]));
			Declaraciones.ModClase[i].AtaqueProyectiles = vb6.val(GetVar(Declaraciones.DatPath + "Balance.dat",
					"MODATAQUEPROYECTILES", Declaraciones.ListaClases[i]));
			Declaraciones.ModClase[i].AtaqueWrestling = vb6.val(
					GetVar(Declaraciones.DatPath + "Balance.dat", "MODATAQUEWRESTLING", Declaraciones.ListaClases[i]));
			Declaraciones.ModClase[i].DanoArmas = vb6
					.val(GetVar(Declaraciones.DatPath + "Balance.dat", "MODDANOARMAS", Declaraciones.ListaClases[i]));
			Declaraciones.ModClase[i].DanoProyectiles = vb6.val(
					GetVar(Declaraciones.DatPath + "Balance.dat", "MODDANOPROYECTILES", Declaraciones.ListaClases[i]));
			Declaraciones.ModClase[i].DanoWrestling = vb6.val(
					GetVar(Declaraciones.DatPath + "Balance.dat", "MODDANOWRESTLING", Declaraciones.ListaClases[i]));
			Declaraciones.ModClase[i].Escudo = vb6
					.val(GetVar(Declaraciones.DatPath + "Balance.dat", "MODESCUDO", Declaraciones.ListaClases[i]));
		}

		/* 'Modificadores de Raza */
		for (i = (1); i <= (Declaraciones.NUMRAZAS); i++) {
			Declaraciones.ModRaza[i].Fuerza = vb6.val(
					GetVar(Declaraciones.DatPath + "Balance.dat", "MODRAZA", Declaraciones.ListaRazas[i] + "Fuerza"));
			Declaraciones.ModRaza[i].Agilidad = vb6.val(
					GetVar(Declaraciones.DatPath + "Balance.dat", "MODRAZA", Declaraciones.ListaRazas[i] + "Agilidad"));
			Declaraciones.ModRaza[i].Inteligencia = vb6.val(GetVar(Declaraciones.DatPath + "Balance.dat", "MODRAZA",
					Declaraciones.ListaRazas[i] + "Inteligencia"));
			Declaraciones.ModRaza[i].Carisma = vb6.val(
					GetVar(Declaraciones.DatPath + "Balance.dat", "MODRAZA", Declaraciones.ListaRazas[i] + "Carisma"));
			Declaraciones.ModRaza[i].Constitucion = vb6.val(GetVar(Declaraciones.DatPath + "Balance.dat", "MODRAZA",
					Declaraciones.ListaRazas[i] + "Constitucion"));
		}

		/* 'Modificadores de Vida */
		for (i = (1); i <= (Declaraciones.NUMCLASES); i++) {
			Declaraciones.ModVida[i] = vb6
					.val(GetVar(Declaraciones.DatPath + "Balance.dat", "MODVIDA", Declaraciones.ListaClases[i]));
		}

		/* 'Distribución de Vida */
		for (i = (1); i <= (5); i++) {
			Declaraciones.DistribucionEnteraVida[i] = vb6
					.val(GetVar(Declaraciones.DatPath + "Balance.dat", "DISTRIBUCION", "E" + vb6.CStr(i)));
		}
		for (i = (1); i <= (4); i++) {
			Declaraciones.DistribucionSemienteraVida[i] = vb6
					.val(GetVar(Declaraciones.DatPath + "Balance.dat", "DISTRIBUCION", "S" + vb6.CStr(i)));
		}

		/* 'Extra */
		Admin.PorcentajeRecuperoMana = vb6
				.val(GetVar(Declaraciones.DatPath + "Balance.dat", "EXTRA", "PorcentajeRecuperoMana"));

		/* 'Party */
		mdParty.ExponenteNivelParty = vb6
				.val(GetVar(Declaraciones.DatPath + "Balance.dat", "PARTY", "ExponenteNivelParty"));

		/* ' Recompensas faccionarias */
		for (i = (1); i <= (ModFacciones.NUM_RANGOS_FACCION); i++) {
			ModFacciones.RecompensaFacciones[i - 1] = vb6
					.val(GetVar(Declaraciones.DatPath + "Balance.dat", "RECOMPENSAFACCION", "Rango" + i));
		}

	}

	static void LoadObjCarpintero() {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int N;
		int lc;

		N = vb6.val(GetVar(Declaraciones.DatPath + "ObjCarpintero.dat", "INIT", "NumObjs"));

		Declaraciones.ObjCarpintero = (Declaraciones.ObjCarpintero == null) ? new Integer[1 + N]
				: java.util.Arrays.copyOf(Declaraciones.ObjCarpintero, 1 + N);

		for (lc = (1); lc <= (N); lc++) {
			Declaraciones.ObjCarpintero[lc] = vb6
					.val(GetVar(Declaraciones.DatPath + "ObjCarpintero.dat", "Obj" + lc, "Index"));
		}

	}

	static void LoadOBJData() {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* '################################################### */
		/* '# ATENCION PELIGRO # */
		/* '################################################### */
		/* ' */
		/* '¡¡¡¡ NO USAR GetVar PARA LEER DESDE EL OBJ.DAT !!!! */
		/* ' */
		/* 'El que ose desafiar esta LEY, se las tendrá que ver */
		/* 'con migo. Para leer desde el OBJ.DAT se deberá usar */
		/* 'la nueva clase clsLeerInis. */
		/* ' */
		/* 'Alejo */
		/* ' */
		/* '################################################### */

		/* 'Call LogTarea("Sub LoadOBJData") */

		/* FIXME: ON ERROR GOTO ErrHandler */

		if (frmMain.Visible) {
			frmMain.txStatus.Caption = "Cargando base de datos de los objetos.";
		}

		/* '***************************************************************** */
		/* 'Carga la lista de objetos */
		/* '***************************************************************** */
		int Object;
		clsIniManager Leer;
		Leer = new clsIniManager();

		Leer.Initialize(Declaraciones.DatPath + "Obj.dat");

		/* 'obtiene el numero de obj */
		Declaraciones.NumObjDatas = vb6.val(Leer.GetValue("INIT", "NumObjs"));

		frmCargando.cargar.min = 0;
		frmCargando.cargar.max = Declaraciones.NumObjDatas;
		frmCargando.cargar.value = 0;

		Declaraciones.ObjData = (Declaraciones.ObjData == null) ? new ObjData[1 + Declaraciones.NumObjDatas]
				: java.util.Arrays.copyOf(Declaraciones.ObjData, 1 + Declaraciones.NumObjDatas);

		/* 'Llena la lista */
		for (Object = (1); Object <= (Declaraciones.NumObjDatas); Object++) {
			Declaraciones.ObjData[Object].Name = Leer.GetValue("OBJ" + Object, "Name");

			/* 'Pablo (ToxicWaste) Log de Objetos. */
			Declaraciones.ObjData[Object].Log = vb6.val(Leer.GetValue("OBJ" + Object, "Log"));
			Declaraciones.ObjData[Object].NoLog = vb6.val(Leer.GetValue("OBJ" + Object, "NoLog"));
			/* '07/09/07 */

			Declaraciones.ObjData[Object].GrhIndex = vb6.val(Leer.GetValue("OBJ" + Object, "GrhIndex"));
			if (Declaraciones.ObjData[Object].GrhIndex == 0) {
				Declaraciones.ObjData[Object].GrhIndex = Declaraciones.ObjData[Object].GrhIndex;
			}

			Declaraciones.ObjData[Object].OBJType = vb6.val(Leer.GetValue("OBJ" + Object, "ObjType"));

			Declaraciones.ObjData[Object].Newbie = vb6.val(Leer.GetValue("OBJ" + Object, "Newbie"));

			switch (Declaraciones.ObjData[Object].OBJType) {
			case otArmadura:
				Declaraciones.ObjData[Object].Real = vb6.val(Leer.GetValue("OBJ" + Object, "Real"));
				Declaraciones.ObjData[Object].Caos = vb6.val(Leer.GetValue("OBJ" + Object, "Caos"));
				Declaraciones.ObjData[Object].LingH = vb6.val(Leer.GetValue("OBJ" + Object, "LingH"));
				Declaraciones.ObjData[Object].LingP = vb6.val(Leer.GetValue("OBJ" + Object, "LingP"));
				Declaraciones.ObjData[Object].LingO = vb6.val(Leer.GetValue("OBJ" + Object, "LingO"));
				Declaraciones.ObjData[Object].SkHerreria = vb6.val(Leer.GetValue("OBJ" + Object, "SkHerreria"));

				break;

			case otESCUDO:
				Declaraciones.ObjData[Object].ShieldAnim = vb6.val(Leer.GetValue("OBJ" + Object, "Anim"));
				Declaraciones.ObjData[Object].LingH = vb6.val(Leer.GetValue("OBJ" + Object, "LingH"));
				Declaraciones.ObjData[Object].LingP = vb6.val(Leer.GetValue("OBJ" + Object, "LingP"));
				Declaraciones.ObjData[Object].LingO = vb6.val(Leer.GetValue("OBJ" + Object, "LingO"));
				Declaraciones.ObjData[Object].SkHerreria = vb6.val(Leer.GetValue("OBJ" + Object, "SkHerreria"));
				Declaraciones.ObjData[Object].Real = vb6.val(Leer.GetValue("OBJ" + Object, "Real"));
				Declaraciones.ObjData[Object].Caos = vb6.val(Leer.GetValue("OBJ" + Object, "Caos"));

				break;

			case otCASCO:
				Declaraciones.ObjData[Object].CascoAnim = vb6.val(Leer.GetValue("OBJ" + Object, "Anim"));
				Declaraciones.ObjData[Object].LingH = vb6.val(Leer.GetValue("OBJ" + Object, "LingH"));
				Declaraciones.ObjData[Object].LingP = vb6.val(Leer.GetValue("OBJ" + Object, "LingP"));
				Declaraciones.ObjData[Object].LingO = vb6.val(Leer.GetValue("OBJ" + Object, "LingO"));
				Declaraciones.ObjData[Object].SkHerreria = vb6.val(Leer.GetValue("OBJ" + Object, "SkHerreria"));
				Declaraciones.ObjData[Object].Real = vb6.val(Leer.GetValue("OBJ" + Object, "Real"));
				Declaraciones.ObjData[Object].Caos = vb6.val(Leer.GetValue("OBJ" + Object, "Caos"));

				break;

			case otWeapon:
				Declaraciones.ObjData[Object].WeaponAnim = vb6.val(Leer.GetValue("OBJ" + Object, "Anim"));
				Declaraciones.ObjData[Object].Apunala = vb6.val(Leer.GetValue("OBJ" + Object, "Apunala"));
				Declaraciones.ObjData[Object].Envenena = vb6.val(Leer.GetValue("OBJ" + Object, "Envenena"));
				Declaraciones.ObjData[Object].MaxHIT = vb6.val(Leer.GetValue("OBJ" + Object, "MaxHIT"));
				Declaraciones.ObjData[Object].MinHIT = vb6.val(Leer.GetValue("OBJ" + Object, "MinHIT"));
				Declaraciones.ObjData[Object].proyectil = vb6.val(Leer.GetValue("OBJ" + Object, "Proyectil"));
				Declaraciones.ObjData[Object].Municion = vb6.val(Leer.GetValue("OBJ" + Object, "Municiones"));
				Declaraciones.ObjData[Object].StaffPower = vb6.val(Leer.GetValue("OBJ" + Object, "StaffPower"));
				Declaraciones.ObjData[Object].StaffDamageBonus = vb6
						.val(Leer.GetValue("OBJ" + Object, "StaffDamageBonus"));
				Declaraciones.ObjData[Object].Refuerzo = vb6.val(Leer.GetValue("OBJ" + Object, "Refuerzo"));

				Declaraciones.ObjData[Object].LingH = vb6.val(Leer.GetValue("OBJ" + Object, "LingH"));
				Declaraciones.ObjData[Object].LingP = vb6.val(Leer.GetValue("OBJ" + Object, "LingP"));
				Declaraciones.ObjData[Object].LingO = vb6.val(Leer.GetValue("OBJ" + Object, "LingO"));
				Declaraciones.ObjData[Object].SkHerreria = vb6.val(Leer.GetValue("OBJ" + Object, "SkHerreria"));
				Declaraciones.ObjData[Object].Real = vb6.val(Leer.GetValue("OBJ" + Object, "Real"));
				Declaraciones.ObjData[Object].Caos = vb6.val(Leer.GetValue("OBJ" + Object, "Caos"));

				Declaraciones.ObjData[Object].WeaponRazaEnanaAnim = vb6
						.val(Leer.GetValue("OBJ" + Object, "RazaEnanaAnim"));

				break;

			case otInstrumentos:
				Declaraciones.ObjData[Object].Snd1 = vb6.val(Leer.GetValue("OBJ" + Object, "SND1"));
				Declaraciones.ObjData[Object].Snd2 = vb6.val(Leer.GetValue("OBJ" + Object, "SND2"));
				Declaraciones.ObjData[Object].Snd3 = vb6.val(Leer.GetValue("OBJ" + Object, "SND3"));
				/* 'Pablo (ToxicWaste) */
				Declaraciones.ObjData[Object].Real = vb6.val(Leer.GetValue("OBJ" + Object, "Real"));
				Declaraciones.ObjData[Object].Caos = vb6.val(Leer.GetValue("OBJ" + Object, "Caos"));

				break;

			case otMinerales:
				Declaraciones.ObjData[Object].MinSkill = vb6.val(Leer.GetValue("OBJ" + Object, "MinSkill"));

				break;

			case otPuertas:
			case eOBJType.otBotellaVacia:
			case eOBJType.otBotellaLlena:
				Declaraciones.ObjData[Object].IndexAbierta = vb6.val(Leer.GetValue("OBJ" + Object, "IndexAbierta"));
				Declaraciones.ObjData[Object].IndexCerrada = vb6.val(Leer.GetValue("OBJ" + Object, "IndexCerrada"));
				Declaraciones.ObjData[Object].IndexCerradaLlave = vb6
						.val(Leer.GetValue("OBJ" + Object, "IndexCerradaLlave"));

				break;

			case otPociones:
				Declaraciones.ObjData[Object].TipoPocion = vb6.val(Leer.GetValue("OBJ" + Object, "TipoPocion"));
				Declaraciones.ObjData[Object].MaxModificador = vb6.val(Leer.GetValue("OBJ" + Object, "MaxModificador"));
				Declaraciones.ObjData[Object].MinModificador = vb6.val(Leer.GetValue("OBJ" + Object, "MinModificador"));
				Declaraciones.ObjData[Object].DuracionEfecto = vb6.val(Leer.GetValue("OBJ" + Object, "DuracionEfecto"));

				break;

			case otBarcos:
				Declaraciones.ObjData[Object].MinSkill = vb6.val(Leer.GetValue("OBJ" + Object, "MinSkill"));
				Declaraciones.ObjData[Object].MaxHIT = vb6.val(Leer.GetValue("OBJ" + Object, "MaxHIT"));
				Declaraciones.ObjData[Object].MinHIT = vb6.val(Leer.GetValue("OBJ" + Object, "MinHIT"));

				break;

			case otFlechas:
				Declaraciones.ObjData[Object].MaxHIT = vb6.val(Leer.GetValue("OBJ" + Object, "MaxHIT"));
				Declaraciones.ObjData[Object].MinHIT = vb6.val(Leer.GetValue("OBJ" + Object, "MinHIT"));
				Declaraciones.ObjData[Object].Envenena = vb6.val(Leer.GetValue("OBJ" + Object, "Envenena"));
				Declaraciones.ObjData[Object].Paraliza = vb6.val(Leer.GetValue("OBJ" + Object, "Paraliza"));

				/* 'Pablo (ToxicWaste) */
				break;

			case otAnillo:
				Declaraciones.ObjData[Object].LingH = vb6.val(Leer.GetValue("OBJ" + Object, "LingH"));
				Declaraciones.ObjData[Object].LingP = vb6.val(Leer.GetValue("OBJ" + Object, "LingP"));
				Declaraciones.ObjData[Object].LingO = vb6.val(Leer.GetValue("OBJ" + Object, "LingO"));
				Declaraciones.ObjData[Object].SkHerreria = vb6.val(Leer.GetValue("OBJ" + Object, "SkHerreria"));
				Declaraciones.ObjData[Object].MaxHIT = vb6.val(Leer.GetValue("OBJ" + Object, "MaxHIT"));
				Declaraciones.ObjData[Object].MinHIT = vb6.val(Leer.GetValue("OBJ" + Object, "MinHIT"));

				break;

			case otTeleport:
				Declaraciones.ObjData[Object].Radio = vb6.val(Leer.GetValue("OBJ" + Object, "Radio"));

				break;

			case otMochilas:
				Declaraciones.ObjData[Object].MochilaType = vb6.val(Leer.GetValue("OBJ" + Object, "MochilaType"));

				break;

			case otForos:
				modForum.AddForum(Leer.GetValue("OBJ" + Object, "ID"));

				break;
			}

			Declaraciones.ObjData[Object].Ropaje = vb6.val(Leer.GetValue("OBJ" + Object, "NumRopaje"));
			Declaraciones.ObjData[Object].HechizoIndex = vb6.val(Leer.GetValue("OBJ" + Object, "HechizoIndex"));

			Declaraciones.ObjData[Object].LingoteIndex = vb6.val(Leer.GetValue("OBJ" + Object, "LingoteIndex"));

			Declaraciones.ObjData[Object].MineralIndex = vb6.val(Leer.GetValue("OBJ" + Object, "MineralIndex"));

			Declaraciones.ObjData[Object].MaxHp = vb6.val(Leer.GetValue("OBJ" + Object, "MaxHP"));
			Declaraciones.ObjData[Object].MinHp = vb6.val(Leer.GetValue("OBJ" + Object, "MinHP"));

			Declaraciones.ObjData[Object].Mujer = vb6.val(Leer.GetValue("OBJ" + Object, "Mujer"));
			Declaraciones.ObjData[Object].Hombre = vb6.val(Leer.GetValue("OBJ" + Object, "Hombre"));

			Declaraciones.ObjData[Object].MinHam = vb6.val(Leer.GetValue("OBJ" + Object, "MinHam"));
			Declaraciones.ObjData[Object].MinSed = vb6.val(Leer.GetValue("OBJ" + Object, "MinAgu"));

			Declaraciones.ObjData[Object].MinDef = vb6.val(Leer.GetValue("OBJ" + Object, "MINDEF"));
			Declaraciones.ObjData[Object].MaxDef = vb6.val(Leer.GetValue("OBJ" + Object, "MAXDEF"));
			Declaraciones.ObjData[Object].def = (Declaraciones.ObjData[Object].MinDef
					+ Declaraciones.ObjData[Object].MaxDef) / 2;

			Declaraciones.ObjData[Object].RazaEnana = vb6.val(Leer.GetValue("OBJ" + Object, "RazaEnana"));
			Declaraciones.ObjData[Object].RazaDrow = vb6.val(Leer.GetValue("OBJ" + Object, "RazaDrow"));
			Declaraciones.ObjData[Object].RazaElfa = vb6.val(Leer.GetValue("OBJ" + Object, "RazaElfa"));
			Declaraciones.ObjData[Object].RazaGnoma = vb6.val(Leer.GetValue("OBJ" + Object, "RazaGnoma"));
			Declaraciones.ObjData[Object].RazaHumana = vb6.val(Leer.GetValue("OBJ" + Object, "RazaHumana"));

			Declaraciones.ObjData[Object].Valor = vb6.val(Leer.GetValue("OBJ" + Object, "Valor"));

			Declaraciones.ObjData[Object].Crucial = vb6.val(Leer.GetValue("OBJ" + Object, "Crucial"));

			Declaraciones.ObjData[Object].Cerrada = vb6.val(Leer.GetValue("OBJ" + Object, "abierta"));
			if (Declaraciones.ObjData[Object].Cerrada == 1) {
				Declaraciones.ObjData[Object].Llave = vb6.val(Leer.GetValue("OBJ" + Object, "Llave"));
				Declaraciones.ObjData[Object].clave = vb6.val(Leer.GetValue("OBJ" + Object, "Clave"));
			}

			/* 'Puertas y llaves */
			Declaraciones.ObjData[Object].clave = vb6.val(Leer.GetValue("OBJ" + Object, "Clave"));

			Declaraciones.ObjData[Object].texto = Leer.GetValue("OBJ" + Object, "Texto");
			Declaraciones.ObjData[Object].GrhSecundario = vb6.val(Leer.GetValue("OBJ" + Object, "VGrande"));

			Declaraciones.ObjData[Object].Agarrable = vb6.val(Leer.GetValue("OBJ" + Object, "Agarrable"));
			Declaraciones.ObjData[Object].ForoID = Leer.GetValue("OBJ" + Object, "ID");

			Declaraciones.ObjData[Object].Acuchilla = vb6.val(Leer.GetValue("OBJ" + Object, "Acuchilla"));

			Declaraciones.ObjData[Object].Guante = vb6.val(Leer.GetValue("OBJ" + Object, "Guante"));

			/*
			 * 'CHECK: !!! Esto es provisorio hasta que los de Dateo cambien los
			 * valores de string a numerico
			 */
			int i;
			int N;
			String S;
			for (i = (1); i <= (Declaraciones.NUMCLASES); i++) {
				S = vb6.UCase(Leer.GetValue("OBJ" + Object, "CP" + i));
				N = 1;
				while (vb6.LenB(S) > 0 && vb6.UCase(Declaraciones.ListaClases[N]) != S) {
					N = N + 1;
				}
				Declaraciones.ObjData[Object].ClaseProhibida[i] = vb6.IIf(vb6.LenB(S) > 0, N, 0);
			}

			Declaraciones.ObjData[Object].DefensaMagicaMax = vb6.val(Leer.GetValue("OBJ" + Object, "DefensaMagicaMax"));
			Declaraciones.ObjData[Object].DefensaMagicaMin = vb6.val(Leer.GetValue("OBJ" + Object, "DefensaMagicaMin"));

			Declaraciones.ObjData[Object].SkCarpinteria = vb6.val(Leer.GetValue("OBJ" + Object, "SkCarpinteria"));

			if (Declaraciones.ObjData[Object].SkCarpinteria > 0) {
				Declaraciones.ObjData[Object].Madera = vb6.val(Leer.GetValue("OBJ" + Object, "Madera"));
			}
			Declaraciones.ObjData[Object].MaderaElfica = vb6.val(Leer.GetValue("OBJ" + Object, "MaderaElfica"));

			/* 'Bebidas */
			Declaraciones.ObjData[Object].MinSta = vb6.val(Leer.GetValue("OBJ" + Object, "MinST"));

			Declaraciones.ObjData[Object].NoSeCae = vb6.val(Leer.GetValue("OBJ" + Object, "NoSeCae"));
			Declaraciones.ObjData[Object].NoSeTira = vb6.val(Leer.GetValue("OBJ" + Object, "NoSeTira"));
			Declaraciones.ObjData[Object].NoRobable = vb6.val(Leer.GetValue("OBJ" + Object, "NoRobable"));
			Declaraciones.ObjData[Object].NoComerciable = vb6.val(Leer.GetValue("OBJ" + Object, "NoComerciable"));
			Declaraciones.ObjData[Object].Intransferible = vb6.val(Leer.GetValue("OBJ" + Object, "Intransferible"));

			Declaraciones.ObjData[Object].ImpideParalizar = vb6
					.CByte(vb6.val(Leer.GetValue("OBJ" + Object, "ImpideParalizar")));
			Declaraciones.ObjData[Object].ImpideInmobilizar = vb6
					.CByte(vb6.val(Leer.GetValue("OBJ" + Object, "ImpideInmobilizar")));
			Declaraciones.ObjData[Object].ImpideAturdir = vb6
					.CByte(vb6.val(Leer.GetValue("OBJ" + Object, "ImpideAturdir")));
			Declaraciones.ObjData[Object].ImpideCegar = vb6
					.CByte(vb6.val(Leer.GetValue("OBJ" + Object, "ImpideCegar")));

			Declaraciones.ObjData[Object].Upgrade = vb6.val(Leer.GetValue("OBJ" + Object, "Upgrade"));

			frmCargando.cargar.value = frmCargando.cargar.value + 1;
		}

		Leer = null;

		/* ' Inicializo los foros faccionarios */
		modForum.AddForum(modForum.FORO_CAOS_ID);
		modForum.AddForum(modForum.FORO_REAL_ID);

		return;

		/* FIXME: ErrHandler : */
		vb6.MsgBox("error cargando objetos " + Err.Number + ": " + Err.description);

	}

	static void LoadUserStats(int UserIndex, clsIniManager /* FIXME BYREF!! */ UserFile) {
		/* '************************************************* */
		/* 'Author: Unknown */
		/* 'Last modified: 11/19/2009 */
		/* '11/19/2009: Pato - Load the EluSkills and ExpSkills */
		/* '************************************************* */
		int LoopC;

		for (LoopC = (1); LoopC <= (Declaraciones.NUMATRIBUTOS); LoopC++) {
			Declaraciones.UserList[UserIndex].Stats.UserAtributos[LoopC] = vb6
					.CInt(UserFile.GetValue("ATRIBUTOS", "AT" + LoopC));
			Declaraciones.UserList[UserIndex].Stats.UserAtributosBackUP[LoopC] = Declaraciones.UserList[UserIndex].Stats.UserAtributos[LoopC];
		}

		for (LoopC = (1); LoopC <= (Declaraciones.NUMSKILLS); LoopC++) {
			Declaraciones.UserList[UserIndex].Stats.UserSkills[LoopC] = vb6
					.val(UserFile.GetValue("SKILLS", "SK" + LoopC));
			Declaraciones.UserList[UserIndex].Stats.EluSkills[LoopC] = vb6
					.val(UserFile.GetValue("SKILLS", "ELUSK" + LoopC));
			Declaraciones.UserList[UserIndex].Stats.ExpSkills[LoopC] = vb6
					.val(UserFile.GetValue("SKILLS", "EXPSK" + LoopC));
		}

		for (LoopC = (1); LoopC <= (Declaraciones.MAXUSERHECHIZOS); LoopC++) {
			Declaraciones.UserList[UserIndex].Stats.UserHechizos[LoopC] = vb6
					.val(UserFile.GetValue("Hechizos", "H" + LoopC));
		}

		Declaraciones.UserList[UserIndex].Stats.GLD = vb6.CLng(UserFile.GetValue("STATS", "GLD"));
		Declaraciones.UserList[UserIndex].Stats.Banco = vb6.CLng(UserFile.GetValue("STATS", "BANCO"));

		Declaraciones.UserList[UserIndex].Stats.MaxHp = vb6.CInt(UserFile.GetValue("STATS", "MaxHP"));
		Declaraciones.UserList[UserIndex].Stats.MinHp = vb6.CInt(UserFile.GetValue("STATS", "MinHP"));

		Declaraciones.UserList[UserIndex].Stats.MinSta = vb6.CInt(UserFile.GetValue("STATS", "MinSTA"));
		Declaraciones.UserList[UserIndex].Stats.MaxSta = vb6.CInt(UserFile.GetValue("STATS", "MaxSTA"));

		Declaraciones.UserList[UserIndex].Stats.MaxMAN = vb6.CInt(UserFile.GetValue("STATS", "MaxMAN"));
		Declaraciones.UserList[UserIndex].Stats.MinMAN = vb6.CInt(UserFile.GetValue("STATS", "MinMAN"));

		Declaraciones.UserList[UserIndex].Stats.MaxHIT = vb6.CInt(UserFile.GetValue("STATS", "MaxHIT"));
		Declaraciones.UserList[UserIndex].Stats.MinHIT = vb6.CInt(UserFile.GetValue("STATS", "MinHIT"));

		Declaraciones.UserList[UserIndex].Stats.MaxAGU = vb6.CByte(UserFile.GetValue("STATS", "MaxAGU"));
		Declaraciones.UserList[UserIndex].Stats.MinAGU = vb6.CByte(UserFile.GetValue("STATS", "MinAGU"));

		Declaraciones.UserList[UserIndex].Stats.MaxHam = vb6.CByte(UserFile.GetValue("STATS", "MaxHAM"));
		Declaraciones.UserList[UserIndex].Stats.MinHam = vb6.CByte(UserFile.GetValue("STATS", "MinHAM"));

		Declaraciones.UserList[UserIndex].Stats.SkillPts = vb6.CInt(UserFile.GetValue("STATS", "SkillPtsLibres"));

		Declaraciones.UserList[UserIndex].Stats.Exp = vb6.CDbl(UserFile.GetValue("STATS", "EXP"));
		Declaraciones.UserList[UserIndex].Stats.ELU = vb6.CLng(UserFile.GetValue("STATS", "ELU"));
		Declaraciones.UserList[UserIndex].Stats.ELV = vb6.CByte(UserFile.GetValue("STATS", "ELV"));

		Declaraciones.UserList[UserIndex].Stats.UsuariosMatados = vb6.CLng(UserFile.GetValue("MUERTES", "UserMuertes"));
		Declaraciones.UserList[UserIndex].Stats.NPCsMuertos = vb6.CInt(UserFile.GetValue("MUERTES", "NpcsMuertes"));

		if (vb6.CByte(UserFile.GetValue("CONSEJO", "PERTENECE"))) {
			Declaraciones.UserList[UserIndex].flags.Privilegios = Declaraciones.UserList[UserIndex].flags.Privilegios
					|| PlayerType.RoyalCouncil;
		}

		if (vb6.CByte(UserFile.GetValue("CONSEJO", "PERTENECECAOS"))) {
			Declaraciones.UserList[UserIndex].flags.Privilegios = Declaraciones.UserList[UserIndex].flags.Privilegios
					|| PlayerType.ChaosCouncil;
		}
	}

	static void LoadUserReputacion(int UserIndex,
			clsIniManager /* FIXME BYREF!! */ UserFile) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		Declaraciones.UserList[UserIndex].Reputacion.AsesinoRep = vb6.val(UserFile.GetValue("REP", "Asesino"));
		Declaraciones.UserList[UserIndex].Reputacion.BandidoRep = vb6.val(UserFile.GetValue("REP", "Bandido"));
		Declaraciones.UserList[UserIndex].Reputacion.BurguesRep = vb6.val(UserFile.GetValue("REP", "Burguesia"));
		Declaraciones.UserList[UserIndex].Reputacion.LadronesRep = vb6.val(UserFile.GetValue("REP", "Ladrones"));
		Declaraciones.UserList[UserIndex].Reputacion.NobleRep = vb6.val(UserFile.GetValue("REP", "Nobles"));
		Declaraciones.UserList[UserIndex].Reputacion.PlebeRep = vb6.val(UserFile.GetValue("REP", "Plebe"));
		Declaraciones.UserList[UserIndex].Reputacion.Promedio = vb6.val(UserFile.GetValue("REP", "Promedio"));

	}

	static void LoadUserInit(int UserIndex, clsIniManager /* FIXME BYREF!! */ UserFile) {
		/* '************************************************* */
		/* 'Author: Unknown */
		/* 'Last modified: 19/11/2006 */
		/* 'Loads the Users RECORDs */
		/*
		 * '23/01/2007 Pablo (ToxicWaste) - Agrego NivelIngreso, FechaIngreso,
		 * MatadosIngreso y NextRecompensa.
		 */
		/*
		 * '23/01/2007 Pablo (ToxicWaste) - Quito CriminalesMatados de Stats
		 * porque era redundante.
		 */
		/* '************************************************* */
		int LoopC;
		String ln;

		Declaraciones.UserList[UserIndex].Faccion.ArmadaReal = vb6
				.CByte(UserFile.GetValue("FACCIONES", "EjercitoReal"));
		Declaraciones.UserList[UserIndex].Faccion.FuerzasCaos = vb6
				.CByte(UserFile.GetValue("FACCIONES", "EjercitoCaos"));
		Declaraciones.UserList[UserIndex].Faccion.CiudadanosMatados = vb6
				.CLng(UserFile.GetValue("FACCIONES", "CiudMatados"));
		Declaraciones.UserList[UserIndex].Faccion.CriminalesMatados = vb6
				.CLng(UserFile.GetValue("FACCIONES", "CrimMatados"));
		Declaraciones.UserList[UserIndex].Faccion.RecibioArmaduraCaos = vb6
				.CByte(UserFile.GetValue("FACCIONES", "rArCaos"));
		Declaraciones.UserList[UserIndex].Faccion.RecibioArmaduraReal = vb6
				.CByte(UserFile.GetValue("FACCIONES", "rArReal"));
		Declaraciones.UserList[UserIndex].Faccion.RecibioExpInicialCaos = vb6
				.CByte(UserFile.GetValue("FACCIONES", "rExCaos"));
		Declaraciones.UserList[UserIndex].Faccion.RecibioExpInicialReal = vb6
				.CByte(UserFile.GetValue("FACCIONES", "rExReal"));
		Declaraciones.UserList[UserIndex].Faccion.RecompensasCaos = vb6.CLng(UserFile.GetValue("FACCIONES", "recCaos"));
		Declaraciones.UserList[UserIndex].Faccion.RecompensasReal = vb6.CLng(UserFile.GetValue("FACCIONES", "recReal"));
		Declaraciones.UserList[UserIndex].Faccion.Reenlistadas = vb6
				.CByte(UserFile.GetValue("FACCIONES", "Reenlistadas"));
		Declaraciones.UserList[UserIndex].Faccion.NivelIngreso = vb6
				.CInt(UserFile.GetValue("FACCIONES", "NivelIngreso"));
		Declaraciones.UserList[UserIndex].Faccion.FechaIngreso = UserFile.GetValue("FACCIONES", "FechaIngreso");
		Declaraciones.UserList[UserIndex].Faccion.MatadosIngreso = vb6
				.CInt(UserFile.GetValue("FACCIONES", "MatadosIngreso"));
		Declaraciones.UserList[UserIndex].Faccion.NextRecompensa = vb6
				.CInt(UserFile.GetValue("FACCIONES", "NextRecompensa"));

		Declaraciones.UserList[UserIndex].flags.Muerto = vb6.CByte(UserFile.GetValue("FLAGS", "Muerto"));
		Declaraciones.UserList[UserIndex].flags.Escondido = vb6.CByte(UserFile.GetValue("FLAGS", "Escondido"));

		Declaraciones.UserList[UserIndex].flags.Hambre = vb6.CByte(UserFile.GetValue("FLAGS", "Hambre"));
		Declaraciones.UserList[UserIndex].flags.Sed = vb6.CByte(UserFile.GetValue("FLAGS", "Sed"));
		Declaraciones.UserList[UserIndex].flags.Desnudo = vb6.CByte(UserFile.GetValue("FLAGS", "Desnudo"));
		Declaraciones.UserList[UserIndex].flags.Navegando = vb6.CByte(UserFile.GetValue("FLAGS", "Navegando"));
		Declaraciones.UserList[UserIndex].flags.Envenenado = vb6.CByte(UserFile.GetValue("FLAGS", "Envenenado"));
		Declaraciones.UserList[UserIndex].flags.Paralizado = vb6.CByte(UserFile.GetValue("FLAGS", "Paralizado"));

		/* 'Matrix */
		Declaraciones.UserList[UserIndex].flags.lastMap = vb6.val(UserFile.GetValue("FLAGS", "LastMap"));

		if (Declaraciones.UserList[UserIndex].flags.Paralizado == 1) {
			Declaraciones.UserList[UserIndex].Counters.Paralisis = Admin.IntervaloParalizado;
		}

		Declaraciones.UserList[UserIndex].Counters.Pena = vb6.CLng(UserFile.GetValue("COUNTERS", "Pena"));
		Declaraciones.UserList[UserIndex].Counters.AsignedSkills = vb6
				.CByte(vb6.val(UserFile.GetValue("COUNTERS", "SkillsAsignados")));

		Declaraciones.UserList[UserIndex].email = UserFile.GetValue("CONTACTO", "Email");

		Declaraciones.UserList[UserIndex].Genero = UserFile.GetValue("INIT", "Genero");
		Declaraciones.UserList[UserIndex].clase = UserFile.GetValue("INIT", "Clase");
		Declaraciones.UserList[UserIndex].raza = UserFile.GetValue("INIT", "Raza");
		Declaraciones.UserList[UserIndex].Hogar = UserFile.GetValue("INIT", "Hogar");
		Declaraciones.UserList[UserIndex].Char.heading = vb6.CInt(UserFile.GetValue("INIT", "Heading"));

		Declaraciones.UserList[UserIndex].OrigChar.Head = vb6.CInt(UserFile.GetValue("INIT", "Head"));
		Declaraciones.UserList[UserIndex].OrigChar.body = vb6.CInt(UserFile.GetValue("INIT", "Body"));
		Declaraciones.UserList[UserIndex].OrigChar.WeaponAnim = vb6.CInt(UserFile.GetValue("INIT", "Arma"));
		Declaraciones.UserList[UserIndex].OrigChar.ShieldAnim = vb6.CInt(UserFile.GetValue("INIT", "Escudo"));
		Declaraciones.UserList[UserIndex].OrigChar.CascoAnim = vb6.CInt(UserFile.GetValue("INIT", "Casco"));

		Declaraciones.UserList[UserIndex].OrigChar.heading = eHeading.SOUTH;

		/* # IF ConUpTime THEN */
		Declaraciones.UserList[UserIndex].UpTime = vb6.CLng(UserFile.GetValue("INIT", "UpTime"));
		/* # END IF */

		if (Declaraciones.UserList[UserIndex].flags.Muerto == 0) {
			Declaraciones.UserList[UserIndex].Char = Declaraciones.UserList[UserIndex].OrigChar;
		} else {
			Declaraciones.UserList[UserIndex].Char.body = Declaraciones.iCuerpoMuerto;
			Declaraciones.UserList[UserIndex].Char.Head = Declaraciones.iCabezaMuerto;
			Declaraciones.UserList[UserIndex].Char.WeaponAnim = Declaraciones.NingunArma;
			Declaraciones.UserList[UserIndex].Char.ShieldAnim = Declaraciones.NingunEscudo;
			Declaraciones.UserList[UserIndex].Char.CascoAnim = Declaraciones.NingunCasco;
		}

		Declaraciones.UserList[UserIndex].desc = UserFile.GetValue("INIT", "Desc");

		Declaraciones.UserList[UserIndex].Pos.Map = vb6
				.CInt(General.ReadField(1, UserFile.GetValue("INIT", "Position"), 45));
		Declaraciones.UserList[UserIndex].Pos.X = vb6
				.CInt(General.ReadField(2, UserFile.GetValue("INIT", "Position"), 45));
		Declaraciones.UserList[UserIndex].Pos.Y = vb6
				.CInt(General.ReadField(3, UserFile.GetValue("INIT", "Position"), 45));

		Declaraciones.UserList[UserIndex].Invent.NroItems = vb6.CInt(UserFile.GetValue("Inventory", "CantidadItems"));

		/*
		 * '[KEVIN]-------------------------------------------------------------
		 * -------
		 */
		/*
		 * '********************************************************************
		 * ***************
		 */
		Declaraciones.UserList[UserIndex].BancoInvent.NroItems = vb6
				.CInt(UserFile.GetValue("BancoInventory", "CantidadItems"));
		/* 'Lista de objetos del banco */
		for (LoopC = (1); LoopC <= (Declaraciones.MAX_BANCOINVENTORY_SLOTS); LoopC++) {
			ln = UserFile.GetValue("BancoInventory", "Obj" + LoopC);
			Declaraciones.UserList[UserIndex].BancoInvent.Object[LoopC].ObjIndex = vb6
					.CInt(General.ReadField(1, ln, 45));
			Declaraciones.UserList[UserIndex].BancoInvent.Object[LoopC].Amount = vb6.CInt(General.ReadField(2, ln, 45));
		}
		/*
		 * '--------------------------------------------------------------------
		 * ----------------
		 */
		/*
		 * '[/KEVIN]************************************************************
		 * *****************
		 */

		/* 'Lista de objetos */
		for (LoopC = (1); LoopC <= (Declaraciones.MAX_INVENTORY_SLOTS); LoopC++) {
			ln = UserFile.GetValue("Inventory", "Obj" + LoopC);
			Declaraciones.UserList[UserIndex].Invent.Object[LoopC].ObjIndex = vb6.val(General.ReadField(1, ln, 45));
			Declaraciones.UserList[UserIndex].Invent.Object[LoopC].Amount = vb6.val(General.ReadField(2, ln, 45));
			Declaraciones.UserList[UserIndex].Invent.Object[LoopC].Equipped = vb6.val(General.ReadField(3, ln, 45));
		}

		/* 'Obtiene el indice-objeto del arma */
		Declaraciones.UserList[UserIndex].Invent.WeaponEqpSlot = vb6
				.CByte(UserFile.GetValue("Inventory", "WeaponEqpSlot"));
		if (Declaraciones.UserList[UserIndex].Invent.WeaponEqpSlot > 0) {
			Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex = Declaraciones.UserList[UserIndex].Invent.Object[Declaraciones.UserList[UserIndex].Invent.WeaponEqpSlot].ObjIndex;
		}

		/* 'Obtiene el indice-objeto del armadura */
		Declaraciones.UserList[UserIndex].Invent.ArmourEqpSlot = vb6
				.CByte(UserFile.GetValue("Inventory", "ArmourEqpSlot"));
		if (Declaraciones.UserList[UserIndex].Invent.ArmourEqpSlot > 0) {
			Declaraciones.UserList[UserIndex].Invent.ArmourEqpObjIndex = Declaraciones.UserList[UserIndex].Invent.Object[Declaraciones.UserList[UserIndex].Invent.ArmourEqpSlot].ObjIndex;
			Declaraciones.UserList[UserIndex].flags.Desnudo = 0;
		} else {
			Declaraciones.UserList[UserIndex].flags.Desnudo = 1;
		}

		/* 'Obtiene el indice-objeto del escudo */
		Declaraciones.UserList[UserIndex].Invent.EscudoEqpSlot = vb6
				.CByte(UserFile.GetValue("Inventory", "EscudoEqpSlot"));
		if (Declaraciones.UserList[UserIndex].Invent.EscudoEqpSlot > 0) {
			Declaraciones.UserList[UserIndex].Invent.EscudoEqpObjIndex = Declaraciones.UserList[UserIndex].Invent.Object[Declaraciones.UserList[UserIndex].Invent.EscudoEqpSlot].ObjIndex;
		}

		/* 'Obtiene el indice-objeto del casco */
		Declaraciones.UserList[UserIndex].Invent.CascoEqpSlot = vb6
				.CByte(UserFile.GetValue("Inventory", "CascoEqpSlot"));
		if (Declaraciones.UserList[UserIndex].Invent.CascoEqpSlot > 0) {
			Declaraciones.UserList[UserIndex].Invent.CascoEqpObjIndex = Declaraciones.UserList[UserIndex].Invent.Object[Declaraciones.UserList[UserIndex].Invent.CascoEqpSlot].ObjIndex;
		}

		/* 'Obtiene el indice-objeto barco */
		Declaraciones.UserList[UserIndex].Invent.BarcoSlot = vb6.CByte(UserFile.GetValue("Inventory", "BarcoSlot"));
		if (Declaraciones.UserList[UserIndex].Invent.BarcoSlot > 0) {
			Declaraciones.UserList[UserIndex].Invent.BarcoObjIndex = Declaraciones.UserList[UserIndex].Invent.Object[Declaraciones.UserList[UserIndex].Invent.BarcoSlot].ObjIndex;
		}

		/* 'Obtiene el indice-objeto municion */
		Declaraciones.UserList[UserIndex].Invent.MunicionEqpSlot = vb6
				.CByte(UserFile.GetValue("Inventory", "MunicionSlot"));
		if (Declaraciones.UserList[UserIndex].Invent.MunicionEqpSlot > 0) {
			Declaraciones.UserList[UserIndex].Invent.MunicionEqpObjIndex = Declaraciones.UserList[UserIndex].Invent.Object[Declaraciones.UserList[UserIndex].Invent.MunicionEqpSlot].ObjIndex;
		}

		/* '[Alejo] */
		/* 'Obtiene el indice-objeto anilo */
		Declaraciones.UserList[UserIndex].Invent.AnilloEqpSlot = vb6
				.CByte(UserFile.GetValue("Inventory", "AnilloSlot"));
		if (Declaraciones.UserList[UserIndex].Invent.AnilloEqpSlot > 0) {
			Declaraciones.UserList[UserIndex].Invent.AnilloEqpObjIndex = Declaraciones.UserList[UserIndex].Invent.Object[Declaraciones.UserList[UserIndex].Invent.AnilloEqpSlot].ObjIndex;
		}

		Declaraciones.UserList[UserIndex].Invent.MochilaEqpSlot = vb6
				.val(UserFile.GetValue("Inventory", "MochilaSlot"));
		if (Declaraciones.UserList[UserIndex].Invent.MochilaEqpSlot > 0) {
			Declaraciones.UserList[UserIndex].Invent.MochilaEqpObjIndex = Declaraciones.UserList[UserIndex].Invent.Object[Declaraciones.UserList[UserIndex].Invent.MochilaEqpSlot].ObjIndex;
		}

		Declaraciones.UserList[UserIndex].NroMascotas = vb6.CInt(UserFile.GetValue("MASCOTAS", "NroMascotas"));
		for (LoopC = (1); LoopC <= (Declaraciones.MAXMASCOTAS); LoopC++) {
			Declaraciones.UserList[UserIndex].MascotasType[LoopC] = vb6
					.val(UserFile.GetValue("MASCOTAS", "MAS" + LoopC));
		}

		ln = UserFile.GetValue("Guild", "GUILDINDEX");
		if (vb6.IsNumeric(ln)) {
			Declaraciones.UserList[UserIndex].GuildIndex = vb6.CInt(ln);
		} else {
			Declaraciones.UserList[UserIndex].GuildIndex = 0;
		}

	}

	static String GetVar(String File, String Main, String Var) {
		return GetVar(File, Main, Var, 1024);
	}

	static String GetVar(String File, String Main, String Var, int EmptySpaces) {
		String retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* ' This will hold the input that the program will retrieve */
		String sSpaces;
		/* ' This will be the defaul value if the string is not found */
		String szReturn;

		szReturn = "";

		/* ' This tells the computer how long the longest string can be */
		sSpaces = vb6.Space(EmptySpaces);

		Declaraciones.GetPrivateProfileString(Main, Var, szReturn, sSpaces, EmptySpaces, File);

		retval = vb6.RTrim(sSpaces);
		retval = vb6.Left(retval, vb6.Len(retval) - 1);

		return retval;
	}

	static void CargarBackUp() {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		if (frmMain.Visible) {
			frmMain.txStatus.Caption = "Cargando backup.";
		}

		int Map;
		String tFileName;

		/* FIXME: ON ERROR GOTO man */

		Declaraciones.NumMaps = vb6.val(GetVar(Declaraciones.DatPath + "Map.dat", "INIT", "NumMaps"));
		ModAreas.InitAreas();

		frmCargando.cargar.min = 0;
		frmCargando.cargar.max = Declaraciones.NumMaps;
		frmCargando.cargar.value = 0;

		Declaraciones.MapPath = GetVar(Declaraciones.DatPath + "Map.dat", "INIT", "MapPath");

		Declaraciones.MapData = new MapBlock[0];
		/*
		 * FIXME: REDIM MULTIDIMENSIONAL: REDIM MapData ( 1 TO NumMaps ,
		 * XMinMapSize TO XMaxMapSize , YMinMapSize TO YMaxMapSize ) AS MapBlock
		 */
		Declaraciones.MapInfo = new MapInfo[0];
		Declaraciones.MapInfo = (Declaraciones.MapInfo == null) ? new MapInfo[1 + Declaraciones.NumMaps]
				: java.util.Arrays.copyOf(Declaraciones.MapInfo, 1 + Declaraciones.NumMaps);

		for (Map = (1); Map <= (Declaraciones.NumMaps); Map++) {
			if (vb6.val(GetVar(vb6.App.Instance().Path + Declaraciones.MapPath + "Mapa" + Map + ".Dat", "Mapa" + Map,
					"BackUp")) != 0) {
				tFileName = vb6.App.Instance().Path + "\\WorldBackUp\\Mapa" + Map;

				/*
				 * 'Miramos que exista al menos uno de los 3 archivos, sino lo
				 * cargamos de la carpeta de los mapas
				 */
				if (!General.FileExist(tFileName + ".*")) {
					tFileName = vb6.App.Instance().Path + Declaraciones.MapPath + "Mapa" + Map;
				}
			} else {
				tFileName = vb6.App.Instance().Path + Declaraciones.MapPath + "Mapa" + Map;
			}

			CargarMapa(Map, tFileName);

			frmCargando.cargar.value = frmCargando.cargar.value + 1;
			/* FIXME: DOEVENTS */
		}

		return;

		/* FIXME: man : */
		vb6.MsgBox("Error durante la carga de mapas, el mapa " + Map + " contiene errores");
		General.LogError(Date + " " + Err.description + " " + Err.HelpContext + " " + Err.HelpFile + " " + Err.source);

	}

	static void LoadMapData() {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		if (frmMain.Visible) {
			frmMain.txStatus.Caption = "Cargando mapas...";
		}

		int Map;
		String tFileName;

		/* FIXME: ON ERROR GOTO man */

		Declaraciones.NumMaps = vb6.val(GetVar(Declaraciones.DatPath + "Map.dat", "INIT", "NumMaps"));
		ModAreas.InitAreas();

		frmCargando.cargar.min = 0;
		frmCargando.cargar.max = Declaraciones.NumMaps;
		frmCargando.cargar.value = 0;

		Declaraciones.MapPath = GetVar(Declaraciones.DatPath + "Map.dat", "INIT", "MapPath");

		Declaraciones.MapData = new MapBlock[0];
		/*
		 * FIXME: REDIM MULTIDIMENSIONAL: REDIM MapData ( 1 TO NumMaps ,
		 * XMinMapSize TO XMaxMapSize , YMinMapSize TO YMaxMapSize ) AS MapBlock
		 */
		Declaraciones.MapInfo = new MapInfo[0];
		Declaraciones.MapInfo = (Declaraciones.MapInfo == null) ? new MapInfo[1 + Declaraciones.NumMaps]
				: java.util.Arrays.copyOf(Declaraciones.MapInfo, 1 + Declaraciones.NumMaps);

		for (Map = (1); Map <= (Declaraciones.NumMaps); Map++) {

			tFileName = vb6.App.Instance().Path + Declaraciones.MapPath + "Mapa" + Map;
			CargarMapa(Map, tFileName);

			frmCargando.cargar.value = frmCargando.cargar.value + 1;
			/* FIXME: DOEVENTS */
		}

		return;

		/* FIXME: man : */
		vb6.MsgBox("Error durante la carga de mapas, el mapa " + Map + " contiene errores");
		General.LogError(Date + " " + Err.description + " " + Err.HelpContext + " " + Err.HelpFile + " " + Err.source);

	}

	static void CargarMapa(int Map, String /* FIXME BYREF!! */ MAPFl) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 10/08/2010 */
		/*
		 * '10/08/2010 - Pato: Implemento el clsByteBuffer y el clsIniManager
		 * para la carga de mapa
		 */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO errh */
		int hFile;
		int X;
		int Y;
		int ByFlags;
		String npcfile;
		clsIniManager Leer;
		clsByteBuffer MapReader;
		clsByteBuffer InfReader;
		int[] Buff;

		MapReader = new clsByteBuffer();
		InfReader = new clsByteBuffer();
		Leer = new clsIniManager();

		npcfile = Declaraciones.DatPath + "NPCs.dat";

		hFile = vb6.FreeFile();

		/* FIXME: OPEN MAPFl & ".map" FOR Binary AS # hFile */
		/* FIXME: SEEK hFile , 1 */

		Buff = new Byte[0];
		Buff = (Buff == null) ? new Byte[vb6.LOF(hFile) - 1] : java.util.Arrays.copyOf(Buff, vb6.LOF(hFile) - 1);

		/* FIXME: GET # hFile , , Buff */
		/* FIXME: CLOSE hFile */

		MapReader.initializeReader(Buff);

		/* 'inf */
		/* FIXME: OPEN MAPFl & ".inf" FOR Binary AS # hFile */
		/* FIXME: SEEK hFile , 1 */

		Buff = new Byte[0];
		Buff = (Buff == null) ? new Byte[vb6.LOF(hFile) - 1] : java.util.Arrays.copyOf(Buff, vb6.LOF(hFile) - 1);

		/* FIXME: GET # hFile , , Buff */
		/* FIXME: CLOSE hFile */

		InfReader.initializeReader(Buff);

		/* 'map Header */
		Declaraciones.MapInfo[Map].MapVersion = MapReader.getInteger();

		Declaraciones.MiCabecera.desc = MapReader.getString(vb6.Len(Declaraciones.MiCabecera.desc));
		Declaraciones.MiCabecera.crc = MapReader.getLong();
		Declaraciones.MiCabecera.MagicWord = MapReader.getLong();

		MapReader.getDouble();

		/* 'inf Header */
		InfReader.getDouble();
		InfReader.getInteger();

		for (Y = (Declaraciones.YMinMapSize); Y <= (Declaraciones.YMaxMapSize); Y++) {
			for (X = (Declaraciones.XMinMapSize); X <= (Declaraciones.XMaxMapSize); X++) {
				/* '.map file */
				ByFlags = MapReader.getByte();

				if (ByFlags && 1) {
					Declaraciones.MapData[Map][X][Y].Blocked = 1;
				}

				Declaraciones.MapData[Map][X][Y].Graphic[1] = MapReader.getInteger();

				/* 'Layer 2 used? */
				if (ByFlags && 2) {
					Declaraciones.MapData[Map][X][Y].Graphic[2] = MapReader.getInteger();
				}

				/* 'Layer 3 used? */
				if (ByFlags && 4) {
					Declaraciones.MapData[Map][X][Y].Graphic[3] = MapReader.getInteger();
				}

				/* 'Layer 4 used? */
				if (ByFlags && 8) {
					Declaraciones.MapData[Map][X][Y].Graphic[4] = MapReader.getInteger();
				}

				/* 'Trigger used? */
				if (ByFlags && 16) {
					Declaraciones.MapData[Map][X][Y].trigger = MapReader.getInteger();
				}

				/* '.inf file */
				ByFlags = InfReader.getByte();

				if (ByFlags && 1) {
					Declaraciones.MapData[Map][X][Y].TileExit.Map = InfReader.getInteger();
					Declaraciones.MapData[Map][X][Y].TileExit.X = InfReader.getInteger();
					Declaraciones.MapData[Map][X][Y].TileExit.Y = InfReader.getInteger();
				}

				if (ByFlags && 2) {
					/* 'Get and make NPC */
					Declaraciones.MapData[Map][X][Y].NpcIndex = InfReader.getInteger();

					if (Declaraciones.MapData[Map][X][Y].NpcIndex > 0) {
						/* 'Si el npc debe hacer respawn en la pos */
						/* 'original la guardamos */
						if (vb6.val(
								GetVar(npcfile, "NPC" + Declaraciones.MapData[Map][X][Y].NpcIndex, "PosOrig")) == 1) {
							Declaraciones.MapData[Map][X][Y].NpcIndex = NPCs
									.OpenNPC(Declaraciones.MapData[Map][X][Y].NpcIndex);
							Declaraciones.Npclist[Declaraciones.MapData[Map][X][Y].NpcIndex].Orig.Map = Map;
							Declaraciones.Npclist[Declaraciones.MapData[Map][X][Y].NpcIndex].Orig.X = X;
							Declaraciones.Npclist[Declaraciones.MapData[Map][X][Y].NpcIndex].Orig.Y = Y;
						} else {
							Declaraciones.MapData[Map][X][Y].NpcIndex = NPCs
									.OpenNPC(Declaraciones.MapData[Map][X][Y].NpcIndex);
						}

						Declaraciones.Npclist[Declaraciones.MapData[Map][X][Y].NpcIndex].Pos.Map = Map;
						Declaraciones.Npclist[Declaraciones.MapData[Map][X][Y].NpcIndex].Pos.X = X;
						Declaraciones.Npclist[Declaraciones.MapData[Map][X][Y].NpcIndex].Pos.Y = Y;

						NPCs.MakeNPCChar(true, 0, Declaraciones.MapData[Map][X][Y].NpcIndex, Map, X, Y);
					}
				}

				if (ByFlags && 4) {
					/* 'Get and make Object */
					Declaraciones.MapData[Map][X][Y].ObjInfo.ObjIndex = InfReader.getInteger();
					Declaraciones.MapData[Map][X][Y].ObjInfo.Amount = InfReader.getInteger();
				}
			}
		}

		Leer.Initialize(MAPFl + ".dat");

		Declaraciones.MapInfo[Map].Name = Leer.GetValue("Mapa" + Map, "Name");
		Declaraciones.MapInfo[Map].Music = Leer.GetValue("Mapa" + Map, "MusicNum");
		Declaraciones.MapInfo[Map].StartPos.Map = vb6
				.val(General.ReadField(1, Leer.GetValue("Mapa" + Map, "StartPos"), vb6.Asc("-")));
		Declaraciones.MapInfo[Map].StartPos.X = vb6
				.val(General.ReadField(2, Leer.GetValue("Mapa" + Map, "StartPos"), vb6.Asc("-")));
		Declaraciones.MapInfo[Map].StartPos.Y = vb6
				.val(General.ReadField(3, Leer.GetValue("Mapa" + Map, "StartPos"), vb6.Asc("-")));

		Declaraciones.MapInfo[Map].OnDeathGoTo.Map = vb6
				.val(General.ReadField(1, Leer.GetValue("Mapa" + Map, "OnDeathGoTo"), vb6.Asc("-")));
		Declaraciones.MapInfo[Map].OnDeathGoTo.X = vb6
				.val(General.ReadField(2, Leer.GetValue("Mapa" + Map, "OnDeathGoTo"), vb6.Asc("-")));
		Declaraciones.MapInfo[Map].OnDeathGoTo.Y = vb6
				.val(General.ReadField(3, Leer.GetValue("Mapa" + Map, "OnDeathGoTo"), vb6.Asc("-")));

		Declaraciones.MapInfo[Map].MagiaSinEfecto = vb6.val(Leer.GetValue("Mapa" + Map, "MagiaSinEfecto"));
		Declaraciones.MapInfo[Map].InviSinEfecto = vb6.val(Leer.GetValue("Mapa" + Map, "InviSinEfecto"));
		Declaraciones.MapInfo[Map].ResuSinEfecto = vb6.val(Leer.GetValue("Mapa" + Map, "ResuSinEfecto"));
		Declaraciones.MapInfo[Map].OcultarSinEfecto = vb6.val(Leer.GetValue("Mapa" + Map, "OcultarSinEfecto"));
		Declaraciones.MapInfo[Map].InvocarSinEfecto = vb6.val(Leer.GetValue("Mapa" + Map, "InvocarSinEfecto"));

		Declaraciones.MapInfo[Map].NoEncriptarMP = vb6.val(Leer.GetValue("Mapa" + Map, "NoEncriptarMP"));

		Declaraciones.MapInfo[Map].RoboNpcsPermitido = vb6.val(Leer.GetValue("Mapa" + Map, "RoboNpcsPermitido"));

		if (vb6.val(Leer.GetValue("Mapa" + Map, "Pk")) == 0) {
			Declaraciones.MapInfo[Map].Pk = true;
		} else {
			Declaraciones.MapInfo[Map].Pk = false;
		}

		Declaraciones.MapInfo[Map].Terreno = Extra.TerrainStringToByte(Leer.GetValue("Mapa" + Map, "Terreno"));
		Declaraciones.MapInfo[Map].Zona = Leer.GetValue("Mapa" + Map, "Zona");
		Declaraciones.MapInfo[Map].Restringir = Extra.RestrictStringToByte(Leer.GetValue("Mapa" + Map, "Restringir"));
		Declaraciones.MapInfo[Map].BackUp = vb6.val(Leer.GetValue("Mapa" + Map, "BACKUP"));

		MapReader = null;
		InfReader = null;
		Leer = null;

		/* FIXME: ERASE Buff */
		return;

		/* FIXME: errh : */
		General.LogError("Error cargando mapa: " + Map + " - Pos: " + X + "," + Y + "." + Err.description);

		MapReader = null;
		InfReader = null;
		Leer = null;
	}

	static void LoadSini() {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int Temporal;

		if (frmMain.Visible) {
			frmMain.txStatus.Caption = "Cargando info de inicio del server.";
		}

		Admin.BootDelBackUp = vb6.val(GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "IniciarDesdeBackUp"));

		/* 'Misc */
		/* # IF SeguridadAlkon THEN */

		/* # END IF */

		/* 'TODO:Esto es temporal, hay que volarlo!! */
		Declaraciones.GUILDPATH = vb6.App.Instance().Path + "\\GUILDS\\";
		Declaraciones.GUILDINFOFILE = Declaraciones.GUILDPATH + "guildsinfo.inf";

		Admin.Puerto = vb6.val(GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "StartPort"));
		Declaraciones.HideMe = vb6.val(GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "Hide"));
		Declaraciones.AllowMultiLogins = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "AllowMultiLogins"));
		Declaraciones.IdleLimit = vb6.val(GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "IdleLimit"));
		/* 'Lee la version correcta del cliente */
		Declaraciones.ULTIMAVERSION = GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "Version");

		Declaraciones.PuedeCrearPersonajes = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "PuedeCrearPersonajes"));
		Declaraciones.ServerSoloGMs = vb6.val(GetVar(Declaraciones.IniPath + "Server.ini", "init", "ServerSoloGMs"));

		/* ' HappyHour */
		int lDay;
		for (lDay = (1); lDay <= (7); lDay++) {
			Declaraciones.HappyHourDays[lDay] = vb6
					.val(GetVar(Declaraciones.IniPath + "Server.ini", "HappyHour", "Day" + lDay));
			if (Declaraciones.HappyHourDays[lDay] <= 0) {
				Declaraciones.HappyHourDays[lDay] = 1;
			}
		}

		ModFacciones.ArmaduraImperial1 = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "ArmaduraImperial1"));
		ModFacciones.ArmaduraImperial2 = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "ArmaduraImperial2"));
		ModFacciones.ArmaduraImperial3 = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "ArmaduraImperial3"));
		ModFacciones.TunicaMagoImperial = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "TunicaMagoImperial"));
		ModFacciones.TunicaMagoImperialEnanos = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "TunicaMagoImperialEnanos"));
		ModFacciones.ArmaduraCaos1 = vb6.val(GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "ArmaduraCaos1"));
		ModFacciones.ArmaduraCaos2 = vb6.val(GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "ArmaduraCaos2"));
		ModFacciones.ArmaduraCaos3 = vb6.val(GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "ArmaduraCaos3"));
		ModFacciones.TunicaMagoCaos = vb6.val(GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "TunicaMagoCaos"));
		ModFacciones.TunicaMagoCaosEnanos = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "TunicaMagoCaosEnanos"));

		ModFacciones.VestimentaImperialHumano = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "VestimentaImperialHumano"));
		ModFacciones.VestimentaImperialEnano = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "VestimentaImperialEnano"));
		ModFacciones.TunicaConspicuaHumano = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "TunicaConspicuaHumano"));
		ModFacciones.TunicaConspicuaEnano = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "TunicaConspicuaEnano"));
		ModFacciones.ArmaduraNobilisimaHumano = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "ArmaduraNobilisimaHumano"));
		ModFacciones.ArmaduraNobilisimaEnano = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "ArmaduraNobilisimaEnano"));
		ModFacciones.ArmaduraGranSacerdote = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "ArmaduraGranSacerdote"));

		ModFacciones.VestimentaLegionHumano = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "VestimentaLegionHumano"));
		ModFacciones.VestimentaLegionEnano = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "VestimentaLegionEnano"));
		ModFacciones.TunicaLobregaHumano = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "TunicaLobregaHumano"));
		ModFacciones.TunicaLobregaEnano = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "TunicaLobregaEnano"));
		ModFacciones.TunicaEgregiaHumano = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "TunicaEgregiaHumano"));
		ModFacciones.TunicaEgregiaEnano = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "TunicaEgregiaEnano"));
		ModFacciones.SacerdoteDemoniaco = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "SacerdoteDemoniaco"));

		PraetoriansCoopNPC.MAPA_PRETORIANO = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "MapaPretoriano"));

		Declaraciones.EnTesting = vb6.val(GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "Testing"));

		/* 'Intervalos */
		Admin.SanaIntervaloSinDescansar = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "SanaIntervaloSinDescansar"));
		FrmInterv.txtSanaIntervaloSinDescansar.Text = Admin.SanaIntervaloSinDescansar;

		Admin.StaminaIntervaloSinDescansar = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "StaminaIntervaloSinDescansar"));
		FrmInterv.txtStaminaIntervaloSinDescansar.Text = Admin.StaminaIntervaloSinDescansar;

		Admin.SanaIntervaloDescansar = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "SanaIntervaloDescansar"));
		FrmInterv.txtSanaIntervaloDescansar.Text = Admin.SanaIntervaloDescansar;

		Admin.StaminaIntervaloDescansar = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "StaminaIntervaloDescansar"));
		FrmInterv.txtStaminaIntervaloDescansar.Text = Admin.StaminaIntervaloDescansar;

		Admin.IntervaloSed = vb6.val(GetVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloSed"));
		FrmInterv.txtIntervaloSed.Text = Admin.IntervaloSed;

		Admin.IntervaloHambre = vb6.val(GetVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloHambre"));
		FrmInterv.txtIntervaloHambre.Text = Admin.IntervaloHambre;

		Admin.IntervaloVeneno = vb6.val(GetVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloVeneno"));
		FrmInterv.txtIntervaloVeneno.Text = Admin.IntervaloVeneno;

		Admin.IntervaloParalizado = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloParalizado"));
		FrmInterv.txtIntervaloParalizado.Text = Admin.IntervaloParalizado;

		Admin.IntervaloInvisible = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloInvisible"));
		FrmInterv.txtIntervaloInvisible.Text = Admin.IntervaloInvisible;

		Admin.IntervaloFrio = vb6.val(GetVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloFrio"));
		FrmInterv.txtIntervaloFrio.Text = Admin.IntervaloFrio;

		Admin.IntervaloWavFx = vb6.val(GetVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloWAVFX"));
		FrmInterv.txtIntervaloWAVFX.Text = Admin.IntervaloWavFx;

		Admin.IntervaloInvocacion = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloInvocacion"));
		FrmInterv.txtInvocacion.Text = Admin.IntervaloInvocacion;

		Admin.IntervaloParaConexion = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloParaConexion"));
		FrmInterv.txtIntervaloParaConexion.Text = Admin.IntervaloParaConexion;

		/* '&&&&&&&&&&&&&&&&&&&&& TIMERS &&&&&&&&&&&&&&&&&&&&&&& */

		/* ' Cargar desde balance.dat */
		Admin.IntervaloPuedeSerAtacado = 5000;
		/* ' Cargar desde balance.dat */
		Admin.IntervaloAtacable = 60000;
		/* ' Cargar desde balance.dat */
		Admin.IntervaloOwnedNpc = 18000;

		Admin.IntervaloUserPuedeCastear = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloLanzaHechizo"));
		FrmInterv.txtIntervaloLanzaHechizo.Text = Admin.IntervaloUserPuedeCastear;

		frmMain.TIMER_AI.interval = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloNpcAI"));
		FrmInterv.txtAI.Text = frmMain.TIMER_AI.interval;

		frmMain.npcataca.interval = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloNpcPuedeAtacar"));
		FrmInterv.txtNPCPuedeAtacar.Text = frmMain.npcataca.interval;

		Admin.IntervaloUserPuedeTrabajar = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloTrabajo"));
		FrmInterv.txtTrabajo.Text = Admin.IntervaloUserPuedeTrabajar;

		Admin.IntervaloUserPuedeAtacar = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloUserPuedeAtacar"));
		FrmInterv.txtPuedeAtacar.Text = Admin.IntervaloUserPuedeAtacar;

		/* 'TODO : Agregar estos intervalos al form!!! */
		Admin.IntervaloMagiaGolpe = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloMagiaGolpe"));
		Admin.IntervaloGolpeMagia = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloGolpeMagia"));
		Admin.IntervaloGolpeUsar = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloGolpeUsar"));

		frmMain.tLluvia.interval = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloPerdidaStaminaLluvia"));
		FrmInterv.txtIntervaloPerdidaStaminaLluvia.Text = frmMain.tLluvia.interval;

		Admin.MinutosWs = vb6.val(GetVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloWS"));
		if (Admin.MinutosWs < 60) {
			Admin.MinutosWs = 180;
		}

		Admin.MinutosMotd = vb6.val(GetVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "MinutosMotd"));
		if (Admin.MinutosMotd < 20) {
			Admin.MinutosMotd = 20;
		}

		Admin.MinutosGuardarUsuarios = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloGuardarUsuarios"));

		Admin.IntervaloCerrarConexion = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloCerrarConexion"));
		Admin.IntervaloUserPuedeUsar = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloUserPuedeUsar"));
		Admin.IntervaloFlechasCazadores = vb6
				.val(GetVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloFlechasCazadores"));

		Admin.IntervaloOculto = vb6.val(GetVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloOculto"));

		/* '&&&&&&&&&&&&&&&&&&&&& FIN TIMERS &&&&&&&&&&&&&&&&&&&&&&& */

		Declaraciones.RECORDusuarios = vb6.val(GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "RECORD"));

		/* 'Max users */
		Temporal = vb6.val(GetVar(Declaraciones.IniPath + "Server.ini", "INIT", "MaxUsers"));
		if (Declaraciones.MaxUsers == 0) {
			Declaraciones.MaxUsers = Temporal;
			Declaraciones.UserList = new User[0];
			Declaraciones.UserList = (Declaraciones.UserList == null) ? new User[1 + Declaraciones.MaxUsers]
					: java.util.Arrays.copyOf(Declaraciones.UserList, 1 + Declaraciones.MaxUsers);
		}

		/* '&&&&&&&&&&&&&&&&&&&&& BALANCE &&&&&&&&&&&&&&&&&&&&&&& */
		/* 'Se agregó en LoadBalance y en el Balance.dat */
		/*
		 * 'PorcentajeRecuperoMana = val(GetVar(IniPath & "Server.ini",
		 * "BALANCE", "PorcentajeRecuperoMana"))
		 */

		/* ''&&&&&&&&&&&&&&&&&&&&& FIN BALANCE &&&&&&&&&&&&&&&&&&&&&&& */
		Statistics.Initialize();

		Declaraciones.Ullathorpe.Map = GetVar(Declaraciones.DatPath + "Ciudades.dat", "Ullathorpe", "Mapa");
		Declaraciones.Ullathorpe.X = GetVar(Declaraciones.DatPath + "Ciudades.dat", "Ullathorpe", "X");
		Declaraciones.Ullathorpe.Y = GetVar(Declaraciones.DatPath + "Ciudades.dat", "Ullathorpe", "Y");

		Declaraciones.Nix.Map = GetVar(Declaraciones.DatPath + "Ciudades.dat", "Nix", "Mapa");
		Declaraciones.Nix.X = GetVar(Declaraciones.DatPath + "Ciudades.dat", "Nix", "X");
		Declaraciones.Nix.Y = GetVar(Declaraciones.DatPath + "Ciudades.dat", "Nix", "Y");

		Declaraciones.Banderbill.Map = GetVar(Declaraciones.DatPath + "Ciudades.dat", "Banderbill", "Mapa");
		Declaraciones.Banderbill.X = GetVar(Declaraciones.DatPath + "Ciudades.dat", "Banderbill", "X");
		Declaraciones.Banderbill.Y = GetVar(Declaraciones.DatPath + "Ciudades.dat", "Banderbill", "Y");

		Declaraciones.Lindos.Map = GetVar(Declaraciones.DatPath + "Ciudades.dat", "Lindos", "Mapa");
		Declaraciones.Lindos.X = GetVar(Declaraciones.DatPath + "Ciudades.dat", "Lindos", "X");
		Declaraciones.Lindos.Y = GetVar(Declaraciones.DatPath + "Ciudades.dat", "Lindos", "Y");

		Declaraciones.Arghal.Map = GetVar(Declaraciones.DatPath + "Ciudades.dat", "Arghal", "Mapa");
		Declaraciones.Arghal.X = GetVar(Declaraciones.DatPath + "Ciudades.dat", "Arghal", "X");
		Declaraciones.Arghal.Y = GetVar(Declaraciones.DatPath + "Ciudades.dat", "Arghal", "Y");

		Declaraciones.Arkhein.Map = GetVar(Declaraciones.DatPath + "Ciudades.dat", "Arkhein", "Mapa");
		Declaraciones.Arkhein.X = GetVar(Declaraciones.DatPath + "Ciudades.dat", "Arkhein", "X");
		Declaraciones.Arkhein.Y = GetVar(Declaraciones.DatPath + "Ciudades.dat", "Arkhein", "Y");

		Declaraciones.Nemahuak.Map = GetVar(Declaraciones.DatPath + "Ciudades.dat", "Nemahuak", "Mapa");
		Declaraciones.Nemahuak.X = GetVar(Declaraciones.DatPath + "Ciudades.dat", "Nemahuak", "X");
		Declaraciones.Nemahuak.Y = GetVar(Declaraciones.DatPath + "Ciudades.dat", "Nemahuak", "Y");

		Declaraciones.Ciudades[eCiudad.cUllathorpe] = Declaraciones.Ullathorpe;
		Declaraciones.Ciudades[eCiudad.cNix] = Declaraciones.Nix;
		Declaraciones.Ciudades[eCiudad.cBanderbill] = Declaraciones.Banderbill;
		Declaraciones.Ciudades[eCiudad.cLindos] = Declaraciones.Lindos;
		Declaraciones.Ciudades[eCiudad.cArghal] = Declaraciones.Arghal;
		Declaraciones.Ciudades[eCiudad.cArkhein] = Declaraciones.Arkhein;

		Admin.MD5sCarga();

		Declaraciones.ConsultaPopular = new ConsultasPopulares();
		Declaraciones.ConsultaPopular.LoadData();

		/* # IF SeguridadAlkon THEN */
		/* # END IF */

		/* ' Admins */
		loadAdministrativeUsers();

	}

	static void WriteVar(String File, String Main, String Var, String value) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* 'Escribe VAR en un archivo */
		/* '*************************************************** */

		Declaraciones.writeprivateprofilestring(Main, Var, value, File);

	}

	static void SaveUser(int UserIndex, String UserFile) {
		SaveUser(UserIndex, UserFile, true);
	}

	static void SaveUser(int UserIndex, String UserFile, boolean SaveTimeOnline) {
		/* '************************************************* */
		/* 'Author: Unknown */
		/* 'Last modified: 10/10/2010 (Pato) */
		/* 'Saves the Users RECORDs */
		/*
		 * '23/01/2007 Pablo (ToxicWaste) - Agrego NivelIngreso, FechaIngreso,
		 * MatadosIngreso y NextRecompensa.
		 */
		/* '11/19/2009: Pato - Save the EluSkills and ExpSkills */
		/*
		 * '12/01/2010: ZaMa - Los druidas pierden la inmunidad de ser atacados
		 * cuando pierden el efecto del mimetismo.
		 */
		/*
		 * '10/10/2010: Pato - Saco el WriteVar e implemento la clase
		 * clsIniManager
		 */
		/* '************************************************* */

		/* FIXME: ON ERROR GOTO ErrHandler */

		clsIniManager Manager;
		boolean Existe;

		/*
		 * 'ESTO TIENE QUE EVITAR ESE BUGAZO QUE NO SE POR QUE GRABA USUARIOS
		 * NULOS
		 */
		/* 'clase=0 es el error, porq el enum empieza de 1!! */
		if (Declaraciones.UserList[UserIndex].clase == 0 || Declaraciones.UserList[UserIndex].Stats.ELV == 0) {
			General.LogCriticEvent(
					"Estoy intentantdo guardar un usuario nulo de nombre: " + Declaraciones.UserList[UserIndex].Name);
			return;
		}

		Manager = new clsIniManager();

		if (General.FileExist(UserFile)) {
			Manager.Initialize(UserFile);

			if (General.FileExist(UserFile + ".bk")) {
				KILL(UserFile + ".bk");
			}
			Name(UserFileASUserFile + ".bk");

			Existe = true;
		}

		if (Declaraciones.UserList[UserIndex].flags.Mimetizado == 1) {
			Declaraciones.UserList[UserIndex].Char.body = Declaraciones.UserList[UserIndex].CharMimetizado.body;
			Declaraciones.UserList[UserIndex].Char.Head = Declaraciones.UserList[UserIndex].CharMimetizado.Head;
			Declaraciones.UserList[UserIndex].Char.CascoAnim = Declaraciones.UserList[UserIndex].CharMimetizado.CascoAnim;
			Declaraciones.UserList[UserIndex].Char.ShieldAnim = Declaraciones.UserList[UserIndex].CharMimetizado.ShieldAnim;
			Declaraciones.UserList[UserIndex].Char.WeaponAnim = Declaraciones.UserList[UserIndex].CharMimetizado.WeaponAnim;
			Declaraciones.UserList[UserIndex].Counters.Mimetismo = 0;
			Declaraciones.UserList[UserIndex].flags.Mimetizado = 0;
			/* ' Se fue el efecto del mimetismo, puede ser atacado por npcs */
			Declaraciones.UserList[UserIndex].flags.Ignorado = false;
		}

		int LoopC;

		Manager.ChangeValue("FLAGS", "Muerto", vb6.CStr(Declaraciones.UserList[UserIndex].flags.Muerto));
		Manager.ChangeValue("FLAGS", "Escondido", vb6.CStr(Declaraciones.UserList[UserIndex].flags.Escondido));
		Manager.ChangeValue("FLAGS", "Hambre", vb6.CStr(Declaraciones.UserList[UserIndex].flags.Hambre));
		Manager.ChangeValue("FLAGS", "Sed", vb6.CStr(Declaraciones.UserList[UserIndex].flags.Sed));
		Manager.ChangeValue("FLAGS", "Desnudo", vb6.CStr(Declaraciones.UserList[UserIndex].flags.Desnudo));
		Manager.ChangeValue("FLAGS", "Ban", vb6.CStr(Declaraciones.UserList[UserIndex].flags.Ban));
		Manager.ChangeValue("FLAGS", "Navegando", vb6.CStr(Declaraciones.UserList[UserIndex].flags.Navegando));
		Manager.ChangeValue("FLAGS", "Envenenado", vb6.CStr(Declaraciones.UserList[UserIndex].flags.Envenenado));
		Manager.ChangeValue("FLAGS", "Paralizado", vb6.CStr(Declaraciones.UserList[UserIndex].flags.Paralizado));
		/* 'Matrix */
		Manager.ChangeValue("FLAGS", "LastMap", vb6.CStr(Declaraciones.UserList[UserIndex].flags.lastMap));

		Manager.ChangeValue("CONSEJO", "PERTENECE",
				vb6.IIf(Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoyalCouncil, "1", "0"));
		Manager.ChangeValue("CONSEJO", "PERTENECECAOS",
				vb6.IIf(Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.ChaosCouncil, "1", "0"));

		Manager.ChangeValue("COUNTERS", "Pena", vb6.CStr(Declaraciones.UserList[UserIndex].Counters.Pena));
		Manager.ChangeValue("COUNTERS", "SkillsAsignados",
				vb6.CStr(Declaraciones.UserList[UserIndex].Counters.AsignedSkills));

		Manager.ChangeValue("FACCIONES", "EjercitoReal",
				vb6.CStr(Declaraciones.UserList[UserIndex].Faccion.ArmadaReal));
		Manager.ChangeValue("FACCIONES", "EjercitoCaos",
				vb6.CStr(Declaraciones.UserList[UserIndex].Faccion.FuerzasCaos));
		Manager.ChangeValue("FACCIONES", "CiudMatados",
				vb6.CStr(Declaraciones.UserList[UserIndex].Faccion.CiudadanosMatados));
		Manager.ChangeValue("FACCIONES", "CrimMatados",
				vb6.CStr(Declaraciones.UserList[UserIndex].Faccion.CriminalesMatados));
		Manager.ChangeValue("FACCIONES", "rArCaos",
				vb6.CStr(Declaraciones.UserList[UserIndex].Faccion.RecibioArmaduraCaos));
		Manager.ChangeValue("FACCIONES", "rArReal",
				vb6.CStr(Declaraciones.UserList[UserIndex].Faccion.RecibioArmaduraReal));
		Manager.ChangeValue("FACCIONES", "rExCaos",
				vb6.CStr(Declaraciones.UserList[UserIndex].Faccion.RecibioExpInicialCaos));
		Manager.ChangeValue("FACCIONES", "rExReal",
				vb6.CStr(Declaraciones.UserList[UserIndex].Faccion.RecibioExpInicialReal));
		Manager.ChangeValue("FACCIONES", "recCaos",
				vb6.CStr(Declaraciones.UserList[UserIndex].Faccion.RecompensasCaos));
		Manager.ChangeValue("FACCIONES", "recReal",
				vb6.CStr(Declaraciones.UserList[UserIndex].Faccion.RecompensasReal));
		Manager.ChangeValue("FACCIONES", "Reenlistadas",
				vb6.CStr(Declaraciones.UserList[UserIndex].Faccion.Reenlistadas));
		Manager.ChangeValue("FACCIONES", "NivelIngreso",
				vb6.CStr(Declaraciones.UserList[UserIndex].Faccion.NivelIngreso));
		Manager.ChangeValue("FACCIONES", "FechaIngreso", Declaraciones.UserList[UserIndex].Faccion.FechaIngreso);
		Manager.ChangeValue("FACCIONES", "MatadosIngreso",
				vb6.CStr(Declaraciones.UserList[UserIndex].Faccion.MatadosIngreso));
		Manager.ChangeValue("FACCIONES", "NextRecompensa",
				vb6.CStr(Declaraciones.UserList[UserIndex].Faccion.NextRecompensa));

		/* '¿Fueron modificados los atributos del usuario? */
		if (!Declaraciones.UserList[UserIndex].flags.TomoPocion) {
			for (LoopC = (1); LoopC <= (vb6.UBound(Declaraciones.UserList[UserIndex].Stats.UserAtributos)); LoopC++) {
				Manager.ChangeValue("ATRIBUTOS", "AT" + LoopC,
						vb6.CStr(Declaraciones.UserList[UserIndex].Stats.UserAtributos[LoopC]));
			}
		} else {
			for (LoopC = (1); LoopC <= (vb6.UBound(Declaraciones.UserList[UserIndex].Stats.UserAtributos)); LoopC++) {
				/*
				 * '.Stats.UserAtributos(LoopC) =
				 * .Stats.UserAtributosBackUP(LoopC)
				 */
				Manager.ChangeValue("ATRIBUTOS", "AT" + LoopC,
						vb6.CStr(Declaraciones.UserList[UserIndex].Stats.UserAtributosBackUP[LoopC]));
			}
		}

		for (LoopC = (1); LoopC <= (vb6.UBound(Declaraciones.UserList[UserIndex].Stats.UserSkills)); LoopC++) {
			Manager.ChangeValue("SKILLS", "SK" + LoopC,
					vb6.CStr(Declaraciones.UserList[UserIndex].Stats.UserSkills[LoopC]));
			Manager.ChangeValue("SKILLS", "ELUSK" + LoopC,
					vb6.CStr(Declaraciones.UserList[UserIndex].Stats.EluSkills[LoopC]));
			Manager.ChangeValue("SKILLS", "EXPSK" + LoopC,
					vb6.CStr(Declaraciones.UserList[UserIndex].Stats.ExpSkills[LoopC]));
		}

		Manager.ChangeValue("CONTACTO", "Email", Declaraciones.UserList[UserIndex].email);

		Manager.ChangeValue("INIT", "Genero", Declaraciones.UserList[UserIndex].Genero);
		Manager.ChangeValue("INIT", "Raza", Declaraciones.UserList[UserIndex].raza);
		Manager.ChangeValue("INIT", "Hogar", Declaraciones.UserList[UserIndex].Hogar);
		Manager.ChangeValue("INIT", "Clase", Declaraciones.UserList[UserIndex].clase);
		Manager.ChangeValue("INIT", "Desc", Declaraciones.UserList[UserIndex].desc);

		Manager.ChangeValue("INIT", "Heading", vb6.CStr(Declaraciones.UserList[UserIndex].Char.heading));
		Manager.ChangeValue("INIT", "Head", vb6.CStr(Declaraciones.UserList[UserIndex].OrigChar.Head));

		if (Declaraciones.UserList[UserIndex].flags.Muerto == 0) {
			if (Declaraciones.UserList[UserIndex].Char.body != 0) {
				Manager.ChangeValue("INIT", "Body", vb6.CStr(Declaraciones.UserList[UserIndex].Char.body));
			}
		}

		Manager.ChangeValue("INIT", "Arma", vb6.CStr(Declaraciones.UserList[UserIndex].Char.WeaponAnim));
		Manager.ChangeValue("INIT", "Escudo", vb6.CStr(Declaraciones.UserList[UserIndex].Char.ShieldAnim));
		Manager.ChangeValue("INIT", "Casco", vb6.CStr(Declaraciones.UserList[UserIndex].Char.CascoAnim));

		/* # IF ConUpTime THEN */

		if (SaveTimeOnline) {
			vb6.Date TempDate;
			TempDate = vb6.Now() - Declaraciones.UserList[UserIndex].LogOnTime;
			Declaraciones.UserList[UserIndex].LogOnTime = vb6.Now();
			Declaraciones.UserList[UserIndex].UpTime = Declaraciones.UserList[UserIndex].UpTime
					+ (vb6.Abs(vb6.Day(TempDate) - 30) * 24 * 3600) + vb6.Hour(TempDate) * 3600
					+ vb6.Minute(TempDate) * 60 + vb6.Second(TempDate);
			Declaraciones.UserList[UserIndex].UpTime = Declaraciones.UserList[UserIndex].UpTime;
			Manager.ChangeValue("INIT", "UpTime", Declaraciones.UserList[UserIndex].UpTime);
		}
		/* # END IF */

		/* 'First time around? */
		if (Manager.GetValue("INIT", "LastIP1") == "") {
			Manager.ChangeValue("INIT", "LastIP1",
					Declaraciones.UserList[UserIndex].ip + " - " + Date + ":" + vb6.time());
			/* 'Is it a different ip from last time? */
		} else if (Declaraciones.UserList[UserIndex].ip != vb6.Left(Manager.GetValue("INIT", "LastIP1"),
				vb6.InStr(1, Manager.GetValue("INIT", "LastIP1"), " ") - 1)) {
			int i;
			/* FIXME WEIRD FOR */
			for (i = (5); ((-1) > 0) ? (i <= (2)) : (i >= (2)); i = i + (-1)) {
				Manager.ChangeValue("INIT", "LastIP" + i, Manager.GetValue("INIT", "LastIP" + vb6.CStr(i - 1)));
			}
			Manager.ChangeValue("INIT", "LastIP1",
					Declaraciones.UserList[UserIndex].ip + " - " + Date + ":" + vb6.time());
			/* 'Same ip, just update the date */
		} else {
			Manager.ChangeValue("INIT", "LastIP1",
					Declaraciones.UserList[UserIndex].ip + " - " + Date + ":" + vb6.time());
		}

		Manager.ChangeValue("INIT", "Position", Declaraciones.UserList[UserIndex].Pos.Map + "-"
				+ Declaraciones.UserList[UserIndex].Pos.X + "-" + Declaraciones.UserList[UserIndex].Pos.Y);

		Manager.ChangeValue("STATS", "GLD", vb6.CStr(Declaraciones.UserList[UserIndex].Stats.GLD));
		Manager.ChangeValue("STATS", "BANCO", vb6.CStr(Declaraciones.UserList[UserIndex].Stats.Banco));

		Manager.ChangeValue("STATS", "MaxHP", vb6.CStr(Declaraciones.UserList[UserIndex].Stats.MaxHp));
		Manager.ChangeValue("STATS", "MinHP", vb6.CStr(Declaraciones.UserList[UserIndex].Stats.MinHp));

		Manager.ChangeValue("STATS", "MaxSTA", vb6.CStr(Declaraciones.UserList[UserIndex].Stats.MaxSta));
		Manager.ChangeValue("STATS", "MinSTA", vb6.CStr(Declaraciones.UserList[UserIndex].Stats.MinSta));

		Manager.ChangeValue("STATS", "MaxMAN", vb6.CStr(Declaraciones.UserList[UserIndex].Stats.MaxMAN));
		Manager.ChangeValue("STATS", "MinMAN", vb6.CStr(Declaraciones.UserList[UserIndex].Stats.MinMAN));

		Manager.ChangeValue("STATS", "MaxHIT", vb6.CStr(Declaraciones.UserList[UserIndex].Stats.MaxHIT));
		Manager.ChangeValue("STATS", "MinHIT", vb6.CStr(Declaraciones.UserList[UserIndex].Stats.MinHIT));

		Manager.ChangeValue("STATS", "MaxAGU", vb6.CStr(Declaraciones.UserList[UserIndex].Stats.MaxAGU));
		Manager.ChangeValue("STATS", "MinAGU", vb6.CStr(Declaraciones.UserList[UserIndex].Stats.MinAGU));

		Manager.ChangeValue("STATS", "MaxHAM", vb6.CStr(Declaraciones.UserList[UserIndex].Stats.MaxHam));
		Manager.ChangeValue("STATS", "MinHAM", vb6.CStr(Declaraciones.UserList[UserIndex].Stats.MinHam));

		Manager.ChangeValue("STATS", "SkillPtsLibres", vb6.CStr(Declaraciones.UserList[UserIndex].Stats.SkillPts));

		Manager.ChangeValue("STATS", "EXP", vb6.CStr(Declaraciones.UserList[UserIndex].Stats.Exp));
		Manager.ChangeValue("STATS", "ELV", vb6.CStr(Declaraciones.UserList[UserIndex].Stats.ELV));

		Manager.ChangeValue("STATS", "ELU", vb6.CStr(Declaraciones.UserList[UserIndex].Stats.ELU));
		Manager.ChangeValue("MUERTES", "UserMuertes",
				vb6.CStr(Declaraciones.UserList[UserIndex].Stats.UsuariosMatados));
		/*
		 * 'Call Manager.ChangeValue( "MUERTES", "CrimMuertes",
		 * CStr(.Stats.CriminalesMatados))
		 */
		Manager.ChangeValue("MUERTES", "NpcsMuertes", vb6.CStr(Declaraciones.UserList[UserIndex].Stats.NPCsMuertos));

		/*
		 * '[KEVIN]-------------------------------------------------------------
		 * ---------------
		 */
		/*
		 * '********************************************************************
		 * ***********************
		 */
		Manager.ChangeValue("BancoInventory", "CantidadItems",
				vb6.val(Declaraciones.UserList[UserIndex].BancoInvent.NroItems));
		int loopd;
		for (loopd = (1); loopd <= (Declaraciones.MAX_BANCOINVENTORY_SLOTS); loopd++) {
			Manager.ChangeValue("BancoInventory", "Obj" + loopd,
					Declaraciones.UserList[UserIndex].BancoInvent.Object[loopd].ObjIndex + "-"
							+ Declaraciones.UserList[UserIndex].BancoInvent.Object[loopd].Amount);
		}
		/*
		 * '********************************************************************
		 * ***********************
		 */
		/* '[/KEVIN]----------- */

		/* 'Save Inv */
		Manager.ChangeValue("Inventory", "CantidadItems", vb6.val(Declaraciones.UserList[UserIndex].Invent.NroItems));

		for (LoopC = (1); LoopC <= (Declaraciones.MAX_INVENTORY_SLOTS); LoopC++) {
			Manager.ChangeValue("Inventory", "Obj" + LoopC,
					Declaraciones.UserList[UserIndex].Invent.Object[LoopC].ObjIndex + "-"
							+ Declaraciones.UserList[UserIndex].Invent.Object[LoopC].Amount + "-"
							+ Declaraciones.UserList[UserIndex].Invent.Object[LoopC].Equipped);
		}

		Manager.ChangeValue("Inventory", "WeaponEqpSlot",
				vb6.CStr(Declaraciones.UserList[UserIndex].Invent.WeaponEqpSlot));
		Manager.ChangeValue("Inventory", "ArmourEqpSlot",
				vb6.CStr(Declaraciones.UserList[UserIndex].Invent.ArmourEqpSlot));
		Manager.ChangeValue("Inventory", "CascoEqpSlot",
				vb6.CStr(Declaraciones.UserList[UserIndex].Invent.CascoEqpSlot));
		Manager.ChangeValue("Inventory", "EscudoEqpSlot",
				vb6.CStr(Declaraciones.UserList[UserIndex].Invent.EscudoEqpSlot));
		Manager.ChangeValue("Inventory", "BarcoSlot", vb6.CStr(Declaraciones.UserList[UserIndex].Invent.BarcoSlot));
		Manager.ChangeValue("Inventory", "MunicionSlot",
				vb6.CStr(Declaraciones.UserList[UserIndex].Invent.MunicionEqpSlot));
		Manager.ChangeValue("Inventory", "MochilaSlot",
				vb6.CStr(Declaraciones.UserList[UserIndex].Invent.MochilaEqpSlot));
		/* '/Nacho */

		Manager.ChangeValue("Inventory", "AnilloSlot",
				vb6.CStr(Declaraciones.UserList[UserIndex].Invent.AnilloEqpSlot));

		/* 'Reputacion */
		Manager.ChangeValue("REP", "Asesino", vb6.CStr(Declaraciones.UserList[UserIndex].Reputacion.AsesinoRep));
		Manager.ChangeValue("REP", "Bandido", vb6.CStr(Declaraciones.UserList[UserIndex].Reputacion.BandidoRep));
		Manager.ChangeValue("REP", "Burguesia", vb6.CStr(Declaraciones.UserList[UserIndex].Reputacion.BurguesRep));
		Manager.ChangeValue("REP", "Ladrones", vb6.CStr(Declaraciones.UserList[UserIndex].Reputacion.LadronesRep));
		Manager.ChangeValue("REP", "Nobles", vb6.CStr(Declaraciones.UserList[UserIndex].Reputacion.NobleRep));
		Manager.ChangeValue("REP", "Plebe", vb6.CStr(Declaraciones.UserList[UserIndex].Reputacion.PlebeRep));

		int L;
		L = (-Declaraciones.UserList[UserIndex].Reputacion.AsesinoRep)
				+ (-Declaraciones.UserList[UserIndex].Reputacion.BandidoRep)
				+ Declaraciones.UserList[UserIndex].Reputacion.BurguesRep
				+ (-Declaraciones.UserList[UserIndex].Reputacion.LadronesRep)
				+ Declaraciones.UserList[UserIndex].Reputacion.NobleRep
				+ Declaraciones.UserList[UserIndex].Reputacion.PlebeRep;
		L = L / 6;
		Manager.ChangeValue("REP", "Promedio", vb6.CStr(L));

		String cad;

		for (LoopC = (1); LoopC <= (Declaraciones.MAXUSERHECHIZOS); LoopC++) {
			cad = Declaraciones.UserList[UserIndex].Stats.UserHechizos[LoopC];
			Manager.ChangeValue("HECHIZOS", "H" + LoopC, cad);
		}

		int NroMascotas;
		NroMascotas = Declaraciones.UserList[UserIndex].NroMascotas;

		for (LoopC = (1); LoopC <= (Declaraciones.MAXMASCOTAS); LoopC++) {
			/* ' Mascota valida? */
			if (Declaraciones.UserList[UserIndex].MascotasIndex[LoopC] > 0) {
				/* ' Nos aseguramos que la criatura no fue invocada */
				if (Declaraciones.Npclist[Declaraciones.UserList[UserIndex].MascotasIndex[LoopC]].Contadores.TiempoExistencia == 0) {
					cad = Declaraciones.UserList[UserIndex].MascotasType[LoopC];
					/* 'Si fue invocada no la guardamos */
				} else {
					cad = "0";
					NroMascotas = NroMascotas - 1;
				}
				Manager.ChangeValue("MASCOTAS", "MAS" + LoopC, cad);
			} else {
				cad = Declaraciones.UserList[UserIndex].MascotasType[LoopC];
				Manager.ChangeValue("MASCOTAS", "MAS" + LoopC, cad);
			}

		}

		Manager.ChangeValue("MASCOTAS", "NroMascotas", vb6.CStr(NroMascotas));

		/* 'Devuelve el head de muerto */
		if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
			Declaraciones.UserList[UserIndex].Char.Head = Declaraciones.iCabezaMuerto;
		}

		Manager.DumpFile(UserFile);

		Manager = null;

		if (Existe) {
			KILL(UserFile + ".bk");
		}

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en SaveUser");
		Manager = null;

	}

	static boolean criminal(int UserIndex) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int L;

		L = (-Declaraciones.UserList[UserIndex].Reputacion.AsesinoRep)
				+ (-Declaraciones.UserList[UserIndex].Reputacion.BandidoRep)
				+ Declaraciones.UserList[UserIndex].Reputacion.BurguesRep
				+ (-Declaraciones.UserList[UserIndex].Reputacion.LadronesRep)
				+ Declaraciones.UserList[UserIndex].Reputacion.NobleRep
				+ Declaraciones.UserList[UserIndex].Reputacion.PlebeRep;
		L = L / 6;
		retval = (L < 0);

		return retval;
	}

	static void BackUPnPc(int NpcIndex, int hFile) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 10/09/2010 */
		/* '10/09/2010 - Pato: Optimice el BackUp de NPCs */
		/* '*************************************************** */

		int LoopC;

		/*
		 * FIXME: PRINT # hFile , "[NPC" & Npclist ( NpcIndex ) . Numero & "]"
		 */

		/* 'General */
		/* FIXME: PRINT # hFile , "Name=" & . Name */
		/* FIXME: PRINT # hFile , "Desc=" & . desc */
		/* FIXME: PRINT # hFile , "Head=" & val ( . Char . Head ) */
		/* FIXME: PRINT # hFile , "Body=" & val ( . Char . body ) */
		/* FIXME: PRINT # hFile , "Heading=" & val ( . Char . heading ) */
		/* FIXME: PRINT # hFile , "Movement=" & val ( . Movement ) */
		/* FIXME: PRINT # hFile , "Attackable=" & val ( . Attackable ) */
		/* FIXME: PRINT # hFile , "Comercia=" & val ( . Comercia ) */
		/* FIXME: PRINT # hFile , "TipoItems=" & val ( . TipoItems ) */
		/* FIXME: PRINT # hFile , "Hostil=" & val ( . Hostile ) */
		/* FIXME: PRINT # hFile , "GiveEXP=" & val ( . GiveEXP ) */
		/* FIXME: PRINT # hFile , "GiveGLD=" & val ( . GiveGLD ) */
		/* FIXME: PRINT # hFile , "InvReSpawn=" & val ( . InvReSpawn ) */
		/* FIXME: PRINT # hFile , "NpcType=" & val ( . NPCtype ) */

		/* 'Stats */
		/*
		 * FIXME: PRINT # hFile , "Alineacion=" & val ( . Stats . Alineacion )
		 */
		/* FIXME: PRINT # hFile , "DEF=" & val ( . Stats . def ) */
		/* FIXME: PRINT # hFile , "MaxHit=" & val ( . Stats . MaxHIT ) */
		/* FIXME: PRINT # hFile , "MaxHp=" & val ( . Stats . MaxHp ) */
		/* FIXME: PRINT # hFile , "MinHit=" & val ( . Stats . MinHIT ) */
		/* FIXME: PRINT # hFile , "MinHp=" & val ( . Stats . MinHp ) */

		/* 'Flags */
		/* FIXME: PRINT # hFile , "ReSpawn=" & val ( . flags . Respawn ) */
		/* FIXME: PRINT # hFile , "BackUp=" & val ( . flags . BackUp ) */
		/* FIXME: PRINT # hFile , "Domable=" & val ( . flags . Domable ) */

		/* 'Inventario */
		/* FIXME: PRINT # hFile , "NroItems=" & val ( . Invent . NroItems ) */
		if (Declaraciones.Npclist[NpcIndex].Invent.NroItems > 0) {
			for (LoopC = (1); LoopC <= (Declaraciones.Npclist[NpcIndex].Invent.NroItems); LoopC++) {
				/*
				 * FIXME: PRINT # hFile , "Obj" & LoopC & "=" & . Invent .
				 * Object ( LoopC ) . ObjIndex & "-" & . Invent . Object ( LoopC
				 * ) . Amount
				 */
			}
		}

		/* FIXME: PRINT # hFile , "" */

	}

	static void CargarNpcBackUp(int NpcIndex, int NpcNumber) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* 'Status */
		if (frmMain.Visible) {
			frmMain.txStatus.Caption = "Cargando backup Npc";
		}

		String npcfile;

		/* 'If NpcNumber > 499 Then */
		/* ' npcfile = DatPath & "bkNPCs-HOSTILES.dat" */
		/* 'Else */
		npcfile = Declaraciones.DatPath + "bkNPCs.dat";
		/* 'End If */

		Declaraciones.Npclist[NpcIndex].Numero = NpcNumber;
		Declaraciones.Npclist[NpcIndex].Name = GetVar(npcfile, "NPC" + NpcNumber, "Name");
		Declaraciones.Npclist[NpcIndex].desc = GetVar(npcfile, "NPC" + NpcNumber, "Desc");
		Declaraciones.Npclist[NpcIndex].Movement = vb6.val(GetVar(npcfile, "NPC" + NpcNumber, "Movement"));
		Declaraciones.Npclist[NpcIndex].NPCtype = vb6.val(GetVar(npcfile, "NPC" + NpcNumber, "NpcType"));

		Declaraciones.Npclist[NpcIndex].Char.body = vb6.val(GetVar(npcfile, "NPC" + NpcNumber, "Body"));
		Declaraciones.Npclist[NpcIndex].Char.Head = vb6.val(GetVar(npcfile, "NPC" + NpcNumber, "Head"));
		Declaraciones.Npclist[NpcIndex].Char.heading = vb6.val(GetVar(npcfile, "NPC" + NpcNumber, "Heading"));

		Declaraciones.Npclist[NpcIndex].Attackable = vb6.val(GetVar(npcfile, "NPC" + NpcNumber, "Attackable"));
		Declaraciones.Npclist[NpcIndex].Comercia = vb6.val(GetVar(npcfile, "NPC" + NpcNumber, "Comercia"));
		Declaraciones.Npclist[NpcIndex].Hostile = vb6.val(GetVar(npcfile, "NPC" + NpcNumber, "Hostile"));
		Declaraciones.Npclist[NpcIndex].GiveEXP = vb6.val(GetVar(npcfile, "NPC" + NpcNumber, "GiveEXP"));

		Declaraciones.Npclist[NpcIndex].GiveGLD = vb6.val(GetVar(npcfile, "NPC" + NpcNumber, "GiveGLD"));

		Declaraciones.Npclist[NpcIndex].InvReSpawn = vb6.val(GetVar(npcfile, "NPC" + NpcNumber, "InvReSpawn"));

		Declaraciones.Npclist[NpcIndex].Stats.MaxHp = vb6.val(GetVar(npcfile, "NPC" + NpcNumber, "MaxHP"));
		Declaraciones.Npclist[NpcIndex].Stats.MinHp = vb6.val(GetVar(npcfile, "NPC" + NpcNumber, "MinHP"));
		Declaraciones.Npclist[NpcIndex].Stats.MaxHIT = vb6.val(GetVar(npcfile, "NPC" + NpcNumber, "MaxHIT"));
		Declaraciones.Npclist[NpcIndex].Stats.MinHIT = vb6.val(GetVar(npcfile, "NPC" + NpcNumber, "MinHIT"));
		Declaraciones.Npclist[NpcIndex].Stats.def = vb6.val(GetVar(npcfile, "NPC" + NpcNumber, "DEF"));
		Declaraciones.Npclist[NpcIndex].Stats.Alineacion = vb6.val(GetVar(npcfile, "NPC" + NpcNumber, "Alineacion"));

		int LoopC;
		String ln;
		Declaraciones.Npclist[NpcIndex].Invent.NroItems = vb6.val(GetVar(npcfile, "NPC" + NpcNumber, "NROITEMS"));
		if (Declaraciones.Npclist[NpcIndex].Invent.NroItems > 0) {
			for (LoopC = (1); LoopC <= (Declaraciones.MAX_INVENTORY_SLOTS); LoopC++) {
				ln = GetVar(npcfile, "NPC" + NpcNumber, "Obj" + LoopC);
				Declaraciones.Npclist[NpcIndex].Invent.Object[LoopC].ObjIndex = vb6.val(General.ReadField(1, ln, 45));
				Declaraciones.Npclist[NpcIndex].Invent.Object[LoopC].Amount = vb6.val(General.ReadField(2, ln, 45));

			}
		} else {
			for (LoopC = (1); LoopC <= (Declaraciones.MAX_INVENTORY_SLOTS); LoopC++) {
				Declaraciones.Npclist[NpcIndex].Invent.Object[LoopC].ObjIndex = 0;
				Declaraciones.Npclist[NpcIndex].Invent.Object[LoopC].Amount = 0;
			}
		}

		for (LoopC = (1); LoopC <= (Declaraciones.MAX_NPC_DROPS); LoopC++) {
			ln = GetVar(npcfile, "NPC" + NpcNumber, "Drop" + LoopC);
			Declaraciones.Npclist[NpcIndex].Drop[LoopC].ObjIndex = vb6.val(General.ReadField(1, ln, 45));
			Declaraciones.Npclist[NpcIndex].Drop[LoopC].Amount = vb6.val(General.ReadField(2, ln, 45));
		}

		Declaraciones.Npclist[NpcIndex].flags.NPCActive = true;
		Declaraciones.Npclist[NpcIndex].flags.Respawn = vb6.val(GetVar(npcfile, "NPC" + NpcNumber, "ReSpawn"));
		Declaraciones.Npclist[NpcIndex].flags.BackUp = vb6.val(GetVar(npcfile, "NPC" + NpcNumber, "BackUp"));
		Declaraciones.Npclist[NpcIndex].flags.Domable = vb6.val(GetVar(npcfile, "NPC" + NpcNumber, "Domable"));
		Declaraciones.Npclist[NpcIndex].flags.RespawnOrigPos = vb6.val(GetVar(npcfile, "NPC" + NpcNumber, "OrigPos"));

		/* 'Tipo de items con los que comercia */
		Declaraciones.Npclist[NpcIndex].TipoItems = vb6.val(GetVar(npcfile, "NPC" + NpcNumber, "TipoItems"));

	}

	static void LogBan(int BannedIndex, int UserIndex, String Motivo) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		WriteVar(vb6.App.Instance().Path + "\\logs\\" + "BanDetail.log", Declaraciones.UserList[BannedIndex].Name,
				"BannedBy", Declaraciones.UserList[UserIndex].Name);
		WriteVar(vb6.App.Instance().Path + "\\logs\\" + "BanDetail.log", Declaraciones.UserList[BannedIndex].Name,
				"Reason", Motivo);

		/*
		 * 'Log interno del servidor, lo usa para hacer un UNBAN general de toda
		 * la gente banned
		 */
		int mifile;
		mifile = vb6.FreeFile();
		/*
		 * FIXME: OPEN App . Path & "\\logs\\GenteBanned.log" FOR Append Shared
		 * AS # mifile
		 */
		/* FIXME: PRINT # mifile , UserList ( BannedIndex ) . Name */
		/* FIXME: CLOSE # mifile */

	}

	static void LogBanFromName(String BannedName, int UserIndex, String Motivo) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		WriteVar(vb6.App.Instance().Path + "\\logs\\" + "BanDetail.dat", BannedName, "BannedBy",
				Declaraciones.UserList[UserIndex].Name);
		WriteVar(vb6.App.Instance().Path + "\\logs\\" + "BanDetail.dat", BannedName, "Reason", Motivo);

		/*
		 * 'Log interno del servidor, lo usa para hacer un UNBAN general de toda
		 * la gente banned
		 */
		int mifile;
		mifile = vb6.FreeFile();
		/*
		 * FIXME: OPEN App . Path & "\\logs\\GenteBanned.log" FOR Append Shared
		 * AS # mifile
		 */
		/* FIXME: PRINT # mifile , BannedName */
		/* FIXME: CLOSE # mifile */

	}

	static void Ban(String BannedName, String Baneador, String Motivo) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		WriteVar(vb6.App.Instance().Path + "\\logs\\" + "BanDetail.dat", BannedName, "BannedBy", Baneador);
		WriteVar(vb6.App.Instance().Path + "\\logs\\" + "BanDetail.dat", BannedName, "Reason", Motivo);

		/*
		 * 'Log interno del servidor, lo usa para hacer un UNBAN general de toda
		 * la gente banned
		 */
		int mifile;
		mifile = vb6.FreeFile();
		/*
		 * FIXME: OPEN App . Path & "\\logs\\GenteBanned.log" FOR Append Shared
		 * AS # mifile
		 */
		/* FIXME: PRINT # mifile , BannedName */
		/* FIXME: CLOSE # mifile */

	}

	static void CargaApuestas() {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		Admin.Apuestas.Ganancias = vb6.val(GetVar(Declaraciones.DatPath + "apuestas.dat", "Main", "Ganancias"));
		Admin.Apuestas.Perdidas = vb6.val(GetVar(Declaraciones.DatPath + "apuestas.dat", "Main", "Perdidas"));
		Admin.Apuestas.Jugadas = vb6.val(GetVar(Declaraciones.DatPath + "apuestas.dat", "Main", "Jugadas"));

	}

	static void generateMatrix(int mapa) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int i;
		int j;

		Declaraciones.distanceToCities = new HomeDistance[0];
		Declaraciones.distanceToCities = (Declaraciones.distanceToCities == null)
				? new HomeDistance[1 + Declaraciones.NumMaps]
				: java.util.Arrays.copyOf(Declaraciones.distanceToCities, 1 + Declaraciones.NumMaps);

		for (j = (1); j <= (Declaraciones.NUMCIUDADES); j++) {
			for (i = (1); i <= (Declaraciones.NumMaps); i++) {
				Declaraciones.distanceToCities[i].distanceToCity[j] = -1;
			}
		}

		for (j = (1); j <= (Declaraciones.NUMCIUDADES); j++) {
			for (i = (1); i <= (4); i++) {
				switch (i) {
				case NORTH:
					setDistance(getLimit(Declaraciones.Ciudades[j].Map, eHeading.NORTH), j, i, 0, 1);
					break;

				case EAST:
					setDistance(getLimit(Declaraciones.Ciudades[j].Map, eHeading.EAST), j, i, 1, 0);
					break;

				case SOUTH:
					setDistance(getLimit(Declaraciones.Ciudades[j].Map, eHeading.SOUTH), j, i, 0, 1);
					break;

				case WEST:
					setDistance(getLimit(Declaraciones.Ciudades[j].Map, eHeading.WEST), j, i, -1, 0);
					break;
				}
			}
		}

	}

	static void setDistance(int mapa, int city, int side) {
		setDistance(mapa, city, side, 0, 0);
	}

	static void setDistance(int mapa, int city, int side, int X, int Y) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int i;
		int lim;

		if (mapa <= 0 || mapa > Declaraciones.NumMaps) {
			return;
		}

		if (Declaraciones.distanceToCities[mapa].distanceToCity[city] >= 0) {
			return;
		}

		if (mapa == Declaraciones.Ciudades[city].Map) {
			Declaraciones.distanceToCities[mapa].distanceToCity[city] = 0;
		} else {
			Declaraciones.distanceToCities[mapa].distanceToCity[city] = vb6.Abs(X) + vb6.Abs(Y);
		}

		for (i = (1); i <= (4); i++) {
			lim = getLimit(mapa, i);
			if (lim > 0) {
				switch (i) {
				case NORTH:
					setDistance(lim, city, i, X, Y + 1);
					break;

				case EAST:
					setDistance(lim, city, i, X + 1, Y);
					break;

				case SOUTH:
					setDistance(lim, city, i, X, Y - 1);
					break;

				case WEST:
					setDistance(lim, city, i, X - 1, Y);
					break;
				}
			}
		}
	}

	static int getLimit(int mapa, int side) {
		int retval;
		/* '*************************************************** */
		/* 'Author: Budi */
		/* 'Last Modification: 31/01/2010 */
		/* 'Retrieves the limit in the given side in the given map. */
		/* 'TODO: This should be set in the .inf map file. */
		/* '*************************************************** */
		int X;
		int Y;

		if (mapa <= 0) {
			return retval;
		}

		for (X = (15); X <= (87); X++) {
			for (Y = (0); Y <= (3); Y++) {
				switch (side) {
				case NORTH:
					retval = Declaraciones.MapData[mapa][X][7 + Y].TileExit.Map;
					break;

				case EAST:
					retval = Declaraciones.MapData[mapa][92 - Y][X].TileExit.Map;
					break;

				case SOUTH:
					retval = Declaraciones.MapData[mapa][X][94 - Y].TileExit.Map;
					break;

				case WEST:
					retval = Declaraciones.MapData[mapa][9 + Y][X].TileExit.Map;
					break;
				}
				if (retval > 0) {
					return retval;
				}
			}
		}
		return retval;
	}

	static void LoadArmadurasFaccion() {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 15/04/2010 */
		/* ' */
		/* '*************************************************** */
		int ClassIndex;

		int ArmaduraIndex;

		for (ClassIndex = (1); ClassIndex <= (Declaraciones.NUMCLASES); ClassIndex++) {

			/* ' Defensa minima para armadas altos */
			ArmaduraIndex = vb6.val(GetVar(Declaraciones.DatPath + "ArmadurasFaccionarias.dat", "CLASE" + ClassIndex,
					"DefMinArmyAlto"));

			ModFacciones.ArmadurasFaccion[ClassIndex][eRaza.Drow].Armada[eTipoDefArmors.ieBaja] = ArmaduraIndex;
			ModFacciones.ArmadurasFaccion[ClassIndex][eRaza.Elfo].Armada[eTipoDefArmors.ieBaja] = ArmaduraIndex;
			ModFacciones.ArmadurasFaccion[ClassIndex][eRaza.Humano].Armada[eTipoDefArmors.ieBaja] = ArmaduraIndex;

			/* ' Defensa minima para armadas bajos */
			ArmaduraIndex = vb6.val(GetVar(Declaraciones.DatPath + "ArmadurasFaccionarias.dat", "CLASE" + ClassIndex,
					"DefMinArmyBajo"));

			ModFacciones.ArmadurasFaccion[ClassIndex][eRaza.Enano].Armada[eTipoDefArmors.ieBaja] = ArmaduraIndex;
			ModFacciones.ArmadurasFaccion[ClassIndex][eRaza.Gnomo].Armada[eTipoDefArmors.ieBaja] = ArmaduraIndex;

			/* ' Defensa minima para caos altos */
			ArmaduraIndex = vb6.val(GetVar(Declaraciones.DatPath + "ArmadurasFaccionarias.dat", "CLASE" + ClassIndex,
					"DefMinCaosAlto"));

			ModFacciones.ArmadurasFaccion[ClassIndex][eRaza.Drow].Caos[eTipoDefArmors.ieBaja] = ArmaduraIndex;
			ModFacciones.ArmadurasFaccion[ClassIndex][eRaza.Elfo].Caos[eTipoDefArmors.ieBaja] = ArmaduraIndex;
			ModFacciones.ArmadurasFaccion[ClassIndex][eRaza.Humano].Caos[eTipoDefArmors.ieBaja] = ArmaduraIndex;

			/* ' Defensa minima para caos bajos */
			ArmaduraIndex = vb6.val(GetVar(Declaraciones.DatPath + "ArmadurasFaccionarias.dat", "CLASE" + ClassIndex,
					"DefMinCaosBajo"));

			ModFacciones.ArmadurasFaccion[ClassIndex][eRaza.Enano].Caos[eTipoDefArmors.ieBaja] = ArmaduraIndex;
			ModFacciones.ArmadurasFaccion[ClassIndex][eRaza.Gnomo].Caos[eTipoDefArmors.ieBaja] = ArmaduraIndex;

			/* ' Defensa media para armadas altos */
			ArmaduraIndex = vb6.val(GetVar(Declaraciones.DatPath + "ArmadurasFaccionarias.dat", "CLASE" + ClassIndex,
					"DefMedArmyAlto"));

			ModFacciones.ArmadurasFaccion[ClassIndex][eRaza.Drow].Armada[eTipoDefArmors.ieMedia] = ArmaduraIndex;
			ModFacciones.ArmadurasFaccion[ClassIndex][eRaza.Elfo].Armada[eTipoDefArmors.ieMedia] = ArmaduraIndex;
			ModFacciones.ArmadurasFaccion[ClassIndex][eRaza.Humano].Armada[eTipoDefArmors.ieMedia] = ArmaduraIndex;

			/* ' Defensa media para armadas bajos */
			ArmaduraIndex = vb6.val(GetVar(Declaraciones.DatPath + "ArmadurasFaccionarias.dat", "CLASE" + ClassIndex,
					"DefMedArmyBajo"));

			ModFacciones.ArmadurasFaccion[ClassIndex][eRaza.Enano].Armada[eTipoDefArmors.ieMedia] = ArmaduraIndex;
			ModFacciones.ArmadurasFaccion[ClassIndex][eRaza.Gnomo].Armada[eTipoDefArmors.ieMedia] = ArmaduraIndex;

			/* ' Defensa media para caos altos */
			ArmaduraIndex = vb6.val(GetVar(Declaraciones.DatPath + "ArmadurasFaccionarias.dat", "CLASE" + ClassIndex,
					"DefMedCaosAlto"));

			ModFacciones.ArmadurasFaccion[ClassIndex][eRaza.Drow].Caos[eTipoDefArmors.ieMedia] = ArmaduraIndex;
			ModFacciones.ArmadurasFaccion[ClassIndex][eRaza.Elfo].Caos[eTipoDefArmors.ieMedia] = ArmaduraIndex;
			ModFacciones.ArmadurasFaccion[ClassIndex][eRaza.Humano].Caos[eTipoDefArmors.ieMedia] = ArmaduraIndex;

			/* ' Defensa media para caos bajos */
			ArmaduraIndex = vb6.val(GetVar(Declaraciones.DatPath + "ArmadurasFaccionarias.dat", "CLASE" + ClassIndex,
					"DefMedCaosBajo"));

			ModFacciones.ArmadurasFaccion[ClassIndex][eRaza.Enano].Caos[eTipoDefArmors.ieMedia] = ArmaduraIndex;
			ModFacciones.ArmadurasFaccion[ClassIndex][eRaza.Gnomo].Caos[eTipoDefArmors.ieMedia] = ArmaduraIndex;

			/* ' Defensa alta para armadas altos */
			ArmaduraIndex = vb6.val(GetVar(Declaraciones.DatPath + "ArmadurasFaccionarias.dat", "CLASE" + ClassIndex,
					"DefAltaArmyAlto"));

			ModFacciones.ArmadurasFaccion[ClassIndex][eRaza.Drow].Armada[eTipoDefArmors.ieAlta] = ArmaduraIndex;
			ModFacciones.ArmadurasFaccion[ClassIndex][eRaza.Elfo].Armada[eTipoDefArmors.ieAlta] = ArmaduraIndex;
			ModFacciones.ArmadurasFaccion[ClassIndex][eRaza.Humano].Armada[eTipoDefArmors.ieAlta] = ArmaduraIndex;

			/* ' Defensa alta para armadas bajos */
			ArmaduraIndex = vb6.val(GetVar(Declaraciones.DatPath + "ArmadurasFaccionarias.dat", "CLASE" + ClassIndex,
					"DefAltaArmyBajo"));

			ModFacciones.ArmadurasFaccion[ClassIndex][eRaza.Enano].Armada[eTipoDefArmors.ieAlta] = ArmaduraIndex;
			ModFacciones.ArmadurasFaccion[ClassIndex][eRaza.Gnomo].Armada[eTipoDefArmors.ieAlta] = ArmaduraIndex;

			/* ' Defensa alta para caos altos */
			ArmaduraIndex = vb6.val(GetVar(Declaraciones.DatPath + "ArmadurasFaccionarias.dat", "CLASE" + ClassIndex,
					"DefAltaCaosAlto"));

			ModFacciones.ArmadurasFaccion[ClassIndex][eRaza.Drow].Caos[eTipoDefArmors.ieAlta] = ArmaduraIndex;
			ModFacciones.ArmadurasFaccion[ClassIndex][eRaza.Elfo].Caos[eTipoDefArmors.ieAlta] = ArmaduraIndex;
			ModFacciones.ArmadurasFaccion[ClassIndex][eRaza.Humano].Caos[eTipoDefArmors.ieAlta] = ArmaduraIndex;

			/* ' Defensa alta para caos bajos */
			ArmaduraIndex = vb6.val(GetVar(Declaraciones.DatPath + "ArmadurasFaccionarias.dat", "CLASE" + ClassIndex,
					"DefAltaCaosBajo"));

			ModFacciones.ArmadurasFaccion[ClassIndex][eRaza.Enano].Caos[eTipoDefArmors.ieAlta] = ArmaduraIndex;
			ModFacciones.ArmadurasFaccion[ClassIndex][eRaza.Gnomo].Caos[eTipoDefArmors.ieAlta] = ArmaduraIndex;

		}

	}

}