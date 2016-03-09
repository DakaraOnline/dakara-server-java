
/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"TCP"')] */
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

public class TCP {

	/* # IF UsarQueSocket = 0 THEN */

	/* # END IF */

	static void DarCuerpo(int UserIndex) {
		/* '************************************************* */
		/* 'Author: Nacho (Integer) */
		/* 'Last modified: 14/03/2007 */
		/* 'Elije una cabeza para el usuario y le da un body */
		/* '************************************************* */
		int NewBody = 0;
		int UserRaza = 0;
		int UserGenero = 0;

		UserGenero = Declaraciones.UserList[UserIndex].Genero;
		UserRaza = Declaraciones.UserList[UserIndex].raza;

		switch (UserGenero) {
		case Hombre:
			switch (UserRaza) {
			case Humano:
				NewBody = 1;
				break;

			case Elfo:
				NewBody = 2;
				break;

			case Drow:
				NewBody = 3;
				break;

			case Enano:
				NewBody = 300;
				break;

			case Gnomo:
				NewBody = 300;
				break;
			}
		case Mujer:
			switch (UserRaza) {
			case Humano:
				NewBody = 1;
				break;

			case Elfo:
				NewBody = 2;
				break;

			case Drow:
				NewBody = 3;
				break;

			case Gnomo:
				NewBody = 300;
				break;

			case Enano:
				NewBody = 300;
				break;
			}
		}

		Declaraciones.UserList[UserIndex].Char.body = NewBody;
	}

	static boolean ValidarCabeza(int UserRaza, int UserGenero, int Head) {
		boolean retval = false;

		switch (UserGenero) {
		case Hombre:
			switch (UserRaza) {
			case Humano:
				retval = (Head >= Declaraciones.HUMANO_H_PRIMER_CABEZA && Head <= Declaraciones.HUMANO_H_ULTIMA_CABEZA);
				break;

			case Elfo:
				retval = (Head >= Declaraciones.ELFO_H_PRIMER_CABEZA && Head <= Declaraciones.ELFO_H_ULTIMA_CABEZA);
				break;

			case Drow:
				retval = (Head >= Declaraciones.DROW_H_PRIMER_CABEZA && Head <= Declaraciones.DROW_H_ULTIMA_CABEZA);
				break;

			case Enano:
				retval = (Head >= Declaraciones.ENANO_H_PRIMER_CABEZA && Head <= Declaraciones.ENANO_H_ULTIMA_CABEZA);
				break;

			case Gnomo:
				retval = (Head >= Declaraciones.GNOMO_H_PRIMER_CABEZA && Head <= Declaraciones.GNOMO_H_ULTIMA_CABEZA);
				break;
			}

		case Mujer:
			switch (UserRaza) {
			case Humano:
				retval = (Head >= Declaraciones.HUMANO_M_PRIMER_CABEZA && Head <= Declaraciones.HUMANO_M_ULTIMA_CABEZA);
				break;

			case Elfo:
				retval = (Head >= Declaraciones.ELFO_M_PRIMER_CABEZA && Head <= Declaraciones.ELFO_M_ULTIMA_CABEZA);
				break;

			case Drow:
				retval = (Head >= Declaraciones.DROW_M_PRIMER_CABEZA && Head <= Declaraciones.DROW_M_ULTIMA_CABEZA);
				break;

			case Enano:
				retval = (Head >= Declaraciones.ENANO_M_PRIMER_CABEZA && Head <= Declaraciones.ENANO_M_ULTIMA_CABEZA);
				break;

			case Gnomo:
				retval = (Head >= Declaraciones.GNOMO_M_PRIMER_CABEZA && Head <= Declaraciones.GNOMO_M_ULTIMA_CABEZA);
				break;
			}
		}

		return retval;
	}

	static boolean AsciiValidos(String cad) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int car = 0;
		int i = 0;

		cad = vb6.LCase(cad);

		for (i = (1); i <= (vb6.Len(cad)); i++) {
			car = vb6.Asc(vb6.mid(cad, i, 1));

			if ((car < 97 || car > 122) && (car != 255) && (car != 32)) {
				retval = false;
				return retval;
			}

		}

		retval = true;

		return retval;
	}

	static boolean Numeric(String cad) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int car = 0;
		int i = 0;

		cad = vb6.LCase(cad);

		for (i = (1); i <= (vb6.Len(cad)); i++) {
			car = vb6.Asc(vb6.mid(cad, i, 1));

			if ((car < 48 || car > 57)) {
				retval = false;
				return retval;
			}

		}

		retval = true;

		return retval;
	}

	static boolean NombrePermitido(String Nombre) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int i = 0;

		for (i = (1); i <= (vb6.UBound(Declaraciones.ForbidenNames)); i++) {
			if (vb6.InStr(Nombre, Declaraciones.ForbidenNames[i])) {
				retval = false;
				return retval;
			}
		}

		retval = true;

		return retval;
	}

	static boolean ValidateSkills(int UserIndex) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int LoopC = 0;

		for (LoopC = (1); LoopC <= (Declaraciones.NUMSKILLS); LoopC++) {
			if (Declaraciones.UserList[UserIndex].Stats.UserSkills[LoopC] < 0) {
				return retval;
				if (Declaraciones.UserList[UserIndex].Stats.UserSkills[LoopC] > 100) {
					Declaraciones.UserList[UserIndex].Stats.UserSkills[LoopC] = 100;
				}
			}
		}

		retval = true;

		return retval;
	}

	static void ConnectNewUser(int UserIndex, String /* FIXME BYREF!! */ Name,
			String /* FIXME BYREF!! */ Password, eRaza UserRaza, eGenero UserSexo, eClass UserClase,
			String /* FIXME BYREF!! */ UserEmail, eCiudad Hogar, int Head) {
		/* '************************************************* */
		/* 'Author: Unknown */
		/* 'Last modified: 3/12/2009 */
		/* 'Conecta un nuevo Usuario */
		/*
		 * '23/01/2007 Pablo (ToxicWaste) - Agregué ResetFaccion al crear
		 * usuario
		 */
		/*
		 * '24/01/2007 Pablo (ToxicWaste) - Agregué el nuevo mana inicial de los
		 * magos.
		 */
		/* '12/02/2007 Pablo (ToxicWaste) - Puse + 1 de const al Elfo normal. */
		/* '20/04/2007 Pablo (ToxicWaste) - Puse -1 de fuerza al Elfo. */
		/*
		 * '09/01/2008 Pablo (ToxicWaste) - Ahora los modificadores de Raza se
		 * controlan desde Balance.dat
		 */
		/* '11/19/2009: Pato - Modifico la maná inicial del bandido. */
		/*
		 * '11/19/2009: Pato - Asigno los valores iniciales de ExpSkills y
		 * EluSkills.
		 */
		/* '03/12/2009: Budi - Optimización del código. */
		/* '************************************************* */
		int i = 0;

		if (!AsciiValidos(Name) || vb6.LenB(Name) == 0) {
			Protocol.WriteErrorMsg(UserIndex, "Nombre inválido.");
			return;
		}

		if (Declaraciones.UserList[UserIndex].flags.UserLogged) {
			General.LogCheating("El usuario " + Declaraciones.UserList[UserIndex].Name + " ha intentado crear a " + Name
					+ " desde la IP " + Declaraciones.UserList[UserIndex].ip);

			/* 'Kick player ( and leave character inside :D )! */
			CloseSocketSL(UserIndex);
			UsUaRiOs.Cerrar_Usuario(UserIndex);

			return;
		}

		/* '¿Existe el personaje? */
		if (General.FileExist(Declaraciones.CharPath + vb6.UCase(Name) + ".chr", 0) == true) {
			Protocol.WriteErrorMsg(UserIndex, "Ya existe el personaje.");
			return;
		}

		/* 'Tiró los dados antes de llegar acá?? */
		if (Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Fuerza] == 0) {
			Protocol.WriteErrorMsg(UserIndex, "Debe tirar los dados antes de poder crear un personaje.");
			return;
		}

		if (!ValidarCabeza(UserRaza, UserSexo, Head)) {
			General.LogCheating("El usuario " + Name + " ha seleccionado la cabeza " + Head + " desde la IP "
					+ Declaraciones.UserList[UserIndex].ip);

			Protocol.WriteErrorMsg(UserIndex, "Cabeza inválida, elija una cabeza seleccionable.");
			return;
		}

		Declaraciones.UserList[UserIndex].flags.Muerto = 0;
		Declaraciones.UserList[UserIndex].flags.Escondido = 0;

		Declaraciones.UserList[UserIndex].Reputacion.AsesinoRep = 0;
		Declaraciones.UserList[UserIndex].Reputacion.BandidoRep = 0;
		Declaraciones.UserList[UserIndex].Reputacion.BurguesRep = 0;
		Declaraciones.UserList[UserIndex].Reputacion.LadronesRep = 0;
		Declaraciones.UserList[UserIndex].Reputacion.NobleRep = 1000;
		Declaraciones.UserList[UserIndex].Reputacion.PlebeRep = 30;

		Declaraciones.UserList[UserIndex].Reputacion.Promedio = 30 / (double) 6;

		Declaraciones.UserList[UserIndex].Name = Name;
		Declaraciones.UserList[UserIndex].clase = UserClase;
		Declaraciones.UserList[UserIndex].raza = UserRaza;
		Declaraciones.UserList[UserIndex].Genero = UserSexo;
		Declaraciones.UserList[UserIndex].email = UserEmail;
		Declaraciones.UserList[UserIndex].Hogar = Hogar;

		/* '[Pablo (Toxic Waste) 9/01/08] */
		Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Fuerza] = Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Fuerza]
				+ Declaraciones.ModRaza[UserRaza].Fuerza;
		Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Agilidad] = Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Agilidad]
				+ Declaraciones.ModRaza[UserRaza].Agilidad;
		Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Inteligencia] = Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Inteligencia]
				+ Declaraciones.ModRaza[UserRaza].Inteligencia;
		Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Carisma] = Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Carisma]
				+ Declaraciones.ModRaza[UserRaza].Carisma;
		Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Constitucion] = Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Constitucion]
				+ Declaraciones.ModRaza[UserRaza].Constitucion;
		/* '[/Pablo (Toxic Waste)] */

		for (i = (1); i <= (Declaraciones.NUMSKILLS); i++) {
			Declaraciones.UserList[UserIndex].Stats.UserSkills[i] = 0;
			UsUaRiOs.CheckEluSkill(UserIndex, i, true);
		}

		Declaraciones.UserList[UserIndex].Stats.SkillPts = 10;

		Declaraciones.UserList[UserIndex].Char.heading = eHeading.SOUTH;

		DarCuerpo(UserIndex);
		Declaraciones.UserList[UserIndex].Char.Head = Head;

		Declaraciones.UserList[UserIndex].OrigChar = Declaraciones.UserList[UserIndex].Char;

		int MiInt = 0;
		MiInt = Matematicas.RandomNumber(1,
				Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Constitucion] / 3);

		Declaraciones.UserList[UserIndex].Stats.MaxHp = 15 + MiInt;
		Declaraciones.UserList[UserIndex].Stats.MinHp = 15 + MiInt;

		MiInt = Matematicas.RandomNumber(1,
				Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Agilidad] / 6);
		if (MiInt == 1) {
			MiInt = 2;
		}

		Declaraciones.UserList[UserIndex].Stats.MaxSta = 20 * MiInt;
		Declaraciones.UserList[UserIndex].Stats.MinSta = 20 * MiInt;

		Declaraciones.UserList[UserIndex].Stats.MaxAGU = 100;
		Declaraciones.UserList[UserIndex].Stats.MinAGU = 100;

		Declaraciones.UserList[UserIndex].Stats.MaxHam = 100;
		Declaraciones.UserList[UserIndex].Stats.MinHam = 100;

		/* '<-----------------MANA-----------------------> */
		/* 'Cambio en mana inicial (ToxicWaste) */
		if (UserClase == eClass.Mage) {
			MiInt = Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Inteligencia] * 3;
			Declaraciones.UserList[UserIndex].Stats.MaxMAN = MiInt;
			Declaraciones.UserList[UserIndex].Stats.MinMAN = MiInt;
		} else if (UserClase == eClass.Cleric || UserClase == eClass.Druid || UserClase == eClass.Bard
				|| UserClase == eClass.Assasin) {
			Declaraciones.UserList[UserIndex].Stats.MaxMAN = 50;
			Declaraciones.UserList[UserIndex].Stats.MinMAN = 50;
			/* 'Mana Inicial del Bandido (ToxicWaste) */
		} else if (UserClase == eClass.Bandit) {
			Declaraciones.UserList[UserIndex].Stats.MaxMAN = 50;
			Declaraciones.UserList[UserIndex].Stats.MinMAN = 50;
		} else {
			Declaraciones.UserList[UserIndex].Stats.MaxMAN = 0;
			Declaraciones.UserList[UserIndex].Stats.MinMAN = 0;
		}

		if (UserClase == eClass.Mage || UserClase == eClass.Cleric || UserClase == eClass.Druid
				|| UserClase == eClass.Bard || UserClase == eClass.Assasin) {
			Declaraciones.UserList[UserIndex].Stats.UserHechizos[1] = 2;

			if (UserClase == eClass.Druid) {
				Declaraciones.UserList[UserIndex].Stats.UserHechizos[2] = 46;
			}
		}

		Declaraciones.UserList[UserIndex].Stats.MaxHIT = 2;
		Declaraciones.UserList[UserIndex].Stats.MinHIT = 1;

		Declaraciones.UserList[UserIndex].Stats.GLD = 0;

		Declaraciones.UserList[UserIndex].Stats.Exp = 0;
		Declaraciones.UserList[UserIndex].Stats.ELU = 300;
		Declaraciones.UserList[UserIndex].Stats.ELV = 1;

		/* '???????????????? INVENTARIO ¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿ */
		int Slot = 0;
		boolean IsPaladin = false;

		IsPaladin = UserClase == eClass.Paladin;

		/* 'Pociones Rojas (Newbie) */
		Slot = 1;
		Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex = 857;
		Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount = 200;

		/* 'Pociones azules (Newbie) */
		if (Declaraciones.UserList[UserIndex].Stats.MaxMAN > 0 || IsPaladin) {
			Slot = Slot + 1;
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex = 856;
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount = 200;

		} else {
			/* 'Pociones amarillas (Newbie) */
			Slot = Slot + 1;
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex = 855;
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount = 100;

			/* 'Pociones verdes (Newbie) */
			Slot = Slot + 1;
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex = 858;
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount = 50;

		}

		/* ' Ropa (Newbie) */
		Slot = Slot + 1;
		switch (UserRaza) {
		case Humano:
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex = 463;
			break;

		case Elfo:
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex = 464;
			break;

		case Drow:
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex = 465;
			break;

		case Enano:
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex = 466;
			break;

		case Gnomo:
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex = 466;
			break;
		}

		/* ' Equipo ropa */
		Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount = 1;
		Declaraciones.UserList[UserIndex].Invent.Object[Slot].Equipped = 1;

		Declaraciones.UserList[UserIndex].Invent.ArmourEqpSlot = Slot;
		Declaraciones.UserList[UserIndex].Invent.ArmourEqpObjIndex = Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex;

		/* 'Arma (Newbie) */
		Slot = Slot + 1;
		switch (UserClase) {
		case Hunter:
			/* ' Arco (Newbie) */
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex = 859;
			break;

		case Worker:
			/* ' Herramienta (Newbie) */
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex = Matematicas.RandomNumber(561, 565);
			break;

		default:
			/* ' Daga (Newbie) */
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex = 460;
			break;
		}

		/* ' Equipo arma */
		Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount = 1;
		Declaraciones.UserList[UserIndex].Invent.Object[Slot].Equipped = 1;

		Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex = Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex;
		Declaraciones.UserList[UserIndex].Invent.WeaponEqpSlot = Slot;

		Declaraciones.UserList[UserIndex].Char.WeaponAnim = UsUaRiOs.GetWeaponAnim(UserIndex,
				Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex);

		/* ' Municiones (Newbie) */
		if (UserClase == eClass.Hunter) {
			Slot = Slot + 1;
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex = 860;
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount = 150;

			/* ' Equipo flechas */
			Declaraciones.UserList[UserIndex].Invent.Object[Slot].Equipped = 1;
			Declaraciones.UserList[UserIndex].Invent.MunicionEqpSlot = Slot;
			Declaraciones.UserList[UserIndex].Invent.MunicionEqpObjIndex = 860;
		}

		/* ' Manzanas (Newbie) */
		Slot = Slot + 1;
		Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex = 467;
		Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount = 100;

		/* ' Jugos (Nwbie) */
		Slot = Slot + 1;
		Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex = 468;
		Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount = 100;

		/* ' Sin casco y escudo */
		Declaraciones.UserList[UserIndex].Char.ShieldAnim = Declaraciones.NingunEscudo;
		Declaraciones.UserList[UserIndex].Char.CascoAnim = Declaraciones.NingunCasco;

		/* ' Total Items */
		Declaraciones.UserList[UserIndex].Invent.NroItems = Slot;

		/* # IF ConUpTime THEN */
		Declaraciones.UserList[UserIndex].LogOnTime = vb6.Now();
		Declaraciones.UserList[UserIndex].UpTime = 0;
		/* # END IF */

		/* 'Valores Default de facciones al Activar nuevo usuario */
		ResetFacciones(UserIndex);

		/*
		 * 'grabamos el password aqui afuera, para no mantenerlo cargado en
		 * memoria
		 */
		ES.WriteVar(Declaraciones.CharPath + vb6.UCase(Name) + ".chr", "INIT", "Password", Password);

		ES.SaveUser(UserIndex, Declaraciones.CharPath + vb6.UCase(Name) + ".chr");

		/* 'Open User */
		ConnectUser(UserIndex, Name, Password);

	}

	/* # IF UsarQueSocket = 1 OR UsarQueSocket = 2 THEN */

	static void CloseSocket(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */
		SecurityIp.IpRestarConexion(GetLongIp[Declaraciones.UserList[UserIndex].ip]);

		if (Declaraciones.UserList[UserIndex].ConnID != -1) {
			CloseSocketSL(UserIndex);
		}

		/* 'Es el mismo user al que está revisando el centinela?? */
		/*
		 * 'IMPORTANTE!!! hacerlo antes de resetear así todavía sabemos el
		 * nombre del user
		 */
		/* ' y lo podemos loguear */
		int CentinelaIndex = 0;
		CentinelaIndex = Declaraciones.UserList[UserIndex].flags.CentinelaIndex;

		if (CentinelaIndex != 0) {
			modCentinela.CentinelaUserLogout(CentinelaIndex);
		}

		/* 'mato los comercios seguros */
		if (Declaraciones.UserList[UserIndex].ComUsu.DestUsu > 0) {
			if (Declaraciones.UserList[Declaraciones.UserList[UserIndex].ComUsu.DestUsu].flags.UserLogged) {
				if (Declaraciones.UserList[Declaraciones.UserList[UserIndex].ComUsu.DestUsu].ComUsu.DestUsu == UserIndex) {
					Protocol.WriteConsoleMsg(Declaraciones.UserList[UserIndex].ComUsu.DestUsu,
							"Comercio cancelado por el otro usuario", FontTypeNames.FONTTYPE_TALK);
					mdlCOmercioConUsuario.FinComerciarUsu(Declaraciones.UserList[UserIndex].ComUsu.DestUsu);
					Protocol.FlushBuffer(Declaraciones.UserList[UserIndex].ComUsu.DestUsu);
				}
			}
		}

		/* 'Empty buffer for reuse */
		Declaraciones.UserList[UserIndex].incomingData
				.ReadASCIIStringFixed(Declaraciones.UserList[UserIndex].incomingData.length);

		if (Declaraciones.UserList[UserIndex].flags.UserLogged) {
			if (Declaraciones.NumUsers > 0) {
				Declaraciones.NumUsers = Declaraciones.NumUsers - 1;
			}
			CloseUser(UserIndex);

			Admin.EstadisticasWeb.Informar(CANTIDAD_ONLINE, Declaraciones.NumUsers);
		} else {
			ResetUserSlot(UserIndex);
		}

		Declaraciones.UserList[UserIndex].ConnID = -1;
		Declaraciones.UserList[UserIndex].ConnIDValida = false;

		if (UserIndex == Declaraciones.LastUser) {
			while (!(Declaraciones.UserList[Declaraciones.LastUser].ConnID != -1)) {
				Declaraciones.LastUser = Declaraciones.LastUser - 1;
				if (Declaraciones.LastUser < 1) {
					break; /* FIXME: EXIT DO */
				}
			}
		}
		return;

		/* FIXME: ErrHandler : */
		Declaraciones.UserList[UserIndex].ConnID = -1;
		Declaraciones.UserList[UserIndex].ConnIDValida = false;
		ResetUserSlot(UserIndex);

		if (UserIndex == Declaraciones.LastUser) {
			while (!(Declaraciones.UserList[Declaraciones.LastUser].ConnID != -1)) {
				Declaraciones.LastUser = Declaraciones.LastUser - 1;
				if (Declaraciones.LastUser < 1) {
					break; /* FIXME: EXIT DO */
				}
			}
		}

		General.LogError("CloseSocket - Error = " + Err.Number + " - Descripción = " + Err.description
				+ " - UserIndex = " + UserIndex);
	}

	/* # ELSEIF UsarQueSocket = 0 THEN */

	/* # ELSEIF UsarQueSocket = 3 THEN */

	/* # END IF */

	/* '[Alejo-21-5]: Cierra un socket sin limpiar el slot */
	static void CloseSocketSL(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* # IF UsarQueSocket = 1 THEN */

		if (Declaraciones.UserList[UserIndex].ConnID != -1 && Declaraciones.UserList[UserIndex].ConnIDValida) {
			wskapiAO.BorraSlotSock(Declaraciones.UserList[UserIndex].ConnID);
			wskapiAO.WSApiCloseSocket(Declaraciones.UserList[UserIndex].ConnID);
			Declaraciones.UserList[UserIndex].ConnIDValida = false;
		}

		/* # ELSEIF UsarQueSocket = 0 THEN */

		/* # ELSEIF UsarQueSocket = 2 THEN */

		/* # END IF */
	}

	/* '' */
	/* ' Send an string to a Slot */
	/* ' */
	/* ' @param userIndex The index of the User */
	/* ' @param Datos The string that will be send */
	/* ' @remarks If UsarQueSocket is 3 it won`t use the clsByteQueue */

	static int EnviarDatosASlot(int UserIndex, String /* FIXME BYREF!! */ Datos) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 01/10/07 */
		/* 'Last Modified By: Lucas Tavolaro Ortiz (Tavo) */
		/*
		 * 'Now it uses the clsByteQueue class and don`t make a FIFO Queue of
		 * String
		 */
		/* '*************************************************** */

		/* '********************************************** */
		/* # IF UsarQueSocket = 1 THEN */
		/* FIXME: ON ERROR GOTO Err */

		int ret = 0;

		ret = wskapiAO.WsApiEnviar(UserIndex, Datos);

		if (ret != 0 && ret != TCP.WSAEWOULDBLOCK) {
			/* ' Close the socket avoiding any critical error */
			CloseSocketSL(UserIndex);
			UsUaRiOs.Cerrar_Usuario(UserIndex);
		}
		return retval;

		/* FIXME: Err : */

		/* '********************************************** */
		/* # ELSEIF UsarQueSocket = 0 THEN */

		/* '********************************************** */
		/* # ELSEIF UsarQueSocket = 2 THEN */

		/* # ELSEIF UsarQueSocket = 3 THEN */

		/* '********************************************** */
		/* # END IF */

		return retval;
	}

	static boolean EstaPCarea(int index, int Index2) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int X = 0;
		int Y = 0;
		for (Y = (Declaraciones.UserList[index].Pos.Y - Declaraciones.MinYBorder
				+ 1); Y <= (Declaraciones.UserList[index].Pos.Y + Declaraciones.MinYBorder - 1); Y++) {
			for (X = (Declaraciones.UserList[index].Pos.X - Declaraciones.MinXBorder
					+ 1); X <= (Declaraciones.UserList[index].Pos.X + Declaraciones.MinXBorder - 1); X++) {

				if (Declaraciones.MapData[Declaraciones.UserList[index].Pos.Map][X][Y].UserIndex == Index2) {
					retval = true;
					return retval;
				}

			}
		}
		retval = false;
		return retval;
	}

	static boolean HayPCarea(Declaraciones.WorldPos Pos) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int X = 0;
		int Y = 0;
		for (Y = (Pos.Y - Declaraciones.MinYBorder + 1); Y <= (Pos.Y + Declaraciones.MinYBorder - 1); Y++) {
			for (X = (Pos.X - Declaraciones.MinXBorder + 1); X <= (Pos.X + Declaraciones.MinXBorder - 1); X++) {
				if (X > 0 && Y > 0 && X < 101 && Y < 101) {
					if (Declaraciones.MapData[Pos.Map][X][Y].UserIndex > 0) {
						retval = true;
						return retval;
					}
				}
			}
		}
		retval = false;
		return retval;
	}

	static boolean HayOBJarea(Declaraciones.WorldPos Pos, int ObjIndex) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int X = 0;
		int Y = 0;
		for (Y = (Pos.Y - Declaraciones.MinYBorder + 1); Y <= (Pos.Y + Declaraciones.MinYBorder - 1); Y++) {
			for (X = (Pos.X - Declaraciones.MinXBorder + 1); X <= (Pos.X + Declaraciones.MinXBorder - 1); X++) {
				if (Declaraciones.MapData[Pos.Map][X][Y].ObjInfo.ObjIndex == ObjIndex) {
					retval = true;
					return retval;
				}

			}
		}
		retval = false;
		return retval;
	}

	static boolean ValidateChr(int UserIndex) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		retval = Declaraciones.UserList[UserIndex].Char.Head != 0 && Declaraciones.UserList[UserIndex].Char.body != 0
				&& ValidateSkills(UserIndex);

		return retval;
	}

	static boolean ConnectUser(int UserIndex, String /* FIXME BYREF!! */ Name,
			String /* FIXME BYREF!! */ Password) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Autor: Unknown (orginal version) */
		/* 'Last Modification: 24/07/2010 (ZaMa) */
		/*
		 * '26/03/2009: ZaMa - Agrego por default que el color de dialogo de los
		 * dioses, sea como el de su nick.
		 */
		/* '12/06/2009: ZaMa - Agrego chequeo de nivel al loguear */
		/*
		 * '14/09/2009: ZaMa - Ahora el usuario esta protegido del ataque de
		 * npcs al loguear
		 */
		/*
		 * '11/27/2009: Budi - Se envian los InvStats del personaje y su Fuerza
		 * y Agilidad
		 */
		/* '03/12/2009: Budi - Optimización del código */
		/*
		 * '24/07/2010: ZaMa - La posicion de comienzo es namehuak, como se
		 * habia definido inicialmente.
		 */
		/* '*************************************************** */
		int N = 0;
		String tStr;

		if (Declaraciones.UserList[UserIndex].flags.UserLogged) {
			General.LogCheating("El usuario " + Declaraciones.UserList[UserIndex].Name + " ha intentado loguear a "
					+ Name + " desde la IP " + Declaraciones.UserList[UserIndex].ip);
			/* 'Kick player ( and leave character inside :D )! */
			CloseSocketSL(UserIndex);
			UsUaRiOs.Cerrar_Usuario(UserIndex);
			return retval;
		}

		/* 'Reseteamos los FLAGS */
		Declaraciones.UserList[UserIndex].flags.Escondido = 0;
		Declaraciones.UserList[UserIndex].flags.TargetNPC = 0;
		Declaraciones.UserList[UserIndex].flags.TargetNpcTipo = eNPCType.Comun;
		Declaraciones.UserList[UserIndex].flags.TargetObj = 0;
		Declaraciones.UserList[UserIndex].flags.TargetUser = 0;
		Declaraciones.UserList[UserIndex].Char.FX = 0;

		/* 'Controlamos no pasar el maximo de usuarios */
		if (Declaraciones.NumUsers >= Declaraciones.MaxUsers) {
			Protocol.WriteErrorMsg(UserIndex,
					"El servidor ha alcanzado el máximo de usuarios soportado, por favor vuelva a intertarlo más tarde.");
			Protocol.FlushBuffer(UserIndex);
			CloseSocket(UserIndex);
			return retval;
		}

		/* '¿Este IP ya esta conectado? */
		if (Declaraciones.AllowMultiLogins == 0) {
			if (Extra.CheckForSameIP(UserIndex, Declaraciones.UserList[UserIndex].ip) == true) {
				Protocol.WriteErrorMsg(UserIndex, "No es posible usar más de un personaje al mismo tiempo.");
				Protocol.FlushBuffer(UserIndex);
				CloseSocket(UserIndex);
				return retval;
			}
		}

		/* '¿Existe el personaje? */
		if (!General.FileExist(Declaraciones.CharPath + vb6.UCase(Name) + ".chr", 0)) {
			Protocol.WriteErrorMsg(UserIndex, "El personaje no existe.");
			Protocol.FlushBuffer(UserIndex);
			CloseSocket(UserIndex);
			return retval;
		}

		/* '¿Es el passwd valido? */
		if (vb6.UCase(Password) != vb6
				.UCase(ES.GetVar(Declaraciones.CharPath + vb6.UCase(Name) + ".chr", "INIT", "Password"))) {
			Protocol.WriteErrorMsg(UserIndex, "Password incorrecto.");
			Protocol.FlushBuffer(UserIndex);
			CloseSocket(UserIndex);
			return retval;
		}

		/* '¿Ya esta conectado el personaje? */
		if (Extra.CheckForSameName(Name)) {
			if (Declaraciones.UserList[Extra.NameIndex(Name)].Counters.Saliendo) {
				Protocol.WriteErrorMsg(UserIndex, "El usuario está saliendo.");
			} else {
				Protocol.WriteErrorMsg(UserIndex, "Perdón, un usuario con el mismo nombre se ha logueado.");
			}
			Protocol.FlushBuffer(UserIndex);
			CloseSocket(UserIndex);
			return retval;
		}

		/* 'Reseteamos los privilegios */
		Declaraciones.UserList[UserIndex].flags.Privilegios = 0;

		/*
		 * 'Vemos que clase de user es (se lo usa para setear los privilegios al
		 * loguear el PJ)
		 */
		if (ES.EsAdmin(Name)) {
			Declaraciones.UserList[UserIndex].flags.Privilegios = Declaraciones.UserList[UserIndex].flags.Privilegios
					|| PlayerType.Admin;
			General.LogGM(Name, "Se conecto con ip:" + Declaraciones.UserList[UserIndex].ip);
		} else if (ES.EsDios(Name)) {
			Declaraciones.UserList[UserIndex].flags.Privilegios = Declaraciones.UserList[UserIndex].flags.Privilegios
					|| PlayerType.Dios;
			General.LogGM(Name, "Se conecto con ip:" + Declaraciones.UserList[UserIndex].ip);
		} else if (ES.EsSemiDios(Name)) {
			Declaraciones.UserList[UserIndex].flags.Privilegios = Declaraciones.UserList[UserIndex].flags.Privilegios
					|| PlayerType.SemiDios;

			Declaraciones.UserList[UserIndex].flags.PrivEspecial = ES.EsGmEspecial(Name);

			General.LogGM(Name, "Se conecto con ip:" + Declaraciones.UserList[UserIndex].ip);
		} else if (ES.EsConsejero(Name)) {
			Declaraciones.UserList[UserIndex].flags.Privilegios = Declaraciones.UserList[UserIndex].flags.Privilegios
					|| PlayerType.Consejero;
			General.LogGM(Name, "Se conecto con ip:" + Declaraciones.UserList[UserIndex].ip);
		} else {
			Declaraciones.UserList[UserIndex].flags.Privilegios = Declaraciones.UserList[UserIndex].flags.Privilegios
					|| PlayerType.User;
			Declaraciones.UserList[UserIndex].flags.AdminPerseguible = true;
		}

		/* 'Add RM flag if needed */
		if (ES.EsRolesMaster(Name)) {
			Declaraciones.UserList[UserIndex].flags.Privilegios = Declaraciones.UserList[UserIndex].flags.Privilegios
					|| PlayerType.RoleMaster;
		}

		if (Declaraciones.ServerSoloGMs > 0) {
			if ((Declaraciones.UserList[UserIndex].flags.Privilegios
					&& (PlayerType.Admin || PlayerType.Dios || PlayerType.SemiDios || PlayerType.Consejero)) == 0) {
				Protocol.WriteErrorMsg(UserIndex,
						"Servidor restringido a administradores. Por favor reintente en unos momentos.");
				Protocol.FlushBuffer(UserIndex);
				CloseSocket(UserIndex);
				return retval;
			}
		}

		/* 'Cargamos el personaje */
		clsIniManager Leer;
		Leer = new clsIniManager();

		Leer.Initialize(Declaraciones.CharPath + vb6.UCase(Name) + ".chr");

		/* 'Cargamos los datos del personaje */
		ES.LoadUserInit(UserIndex, Leer);

		ES.LoadUserStats(UserIndex, Leer);

		if (!ValidateChr(UserIndex)) {
			Protocol.WriteErrorMsg(UserIndex, "Error en el personaje.");
			CloseSocket(UserIndex);
			return retval;
		}

		ES.LoadUserReputacion(UserIndex, Leer);

		Leer = null;

		if (Declaraciones.UserList[UserIndex].Invent.EscudoEqpSlot == 0) {
			Declaraciones.UserList[UserIndex].Char.ShieldAnim = Declaraciones.NingunEscudo;
		}
		if (Declaraciones.UserList[UserIndex].Invent.CascoEqpSlot == 0) {
			Declaraciones.UserList[UserIndex].Char.CascoAnim = Declaraciones.NingunCasco;
		}
		if (Declaraciones.UserList[UserIndex].Invent.WeaponEqpSlot == 0) {
			Declaraciones.UserList[UserIndex].Char.WeaponAnim = Declaraciones.NingunArma;
		}

		if (Declaraciones.UserList[UserIndex].Invent.MochilaEqpSlot > 0) {
			Declaraciones.UserList[UserIndex].CurrentInventorySlots = Declaraciones.MAX_NORMAL_INVENTORY_SLOTS
					+ Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.Object[Declaraciones.UserList[UserIndex].Invent.MochilaEqpSlot].ObjIndex].MochilaType
							* 5;
		} else {
			Declaraciones.UserList[UserIndex].CurrentInventorySlots = Declaraciones.MAX_NORMAL_INVENTORY_SLOTS;
		}
		if ((Declaraciones.UserList[UserIndex].flags.Muerto == 0)) {
			Declaraciones.UserList[UserIndex].flags.SeguroResu = false;
			Protocol.WriteMultiMessage(UserIndex, eMessages.ResuscitationSafeOff);
		} else {
			Declaraciones.UserList[UserIndex].flags.SeguroResu = true;
			Protocol.WriteMultiMessage(UserIndex, eMessages.ResuscitationSafeOn);
		}

		InvUsuario.UpdateUserInv(true, UserIndex, 0);
		modHechizos.UpdateUserHechizos(true, UserIndex, 0);

		if (Declaraciones.UserList[UserIndex].flags.Paralizado) {
			Protocol.WriteParalizeOK(UserIndex);
		}

		int mapa = 0;
		mapa = Declaraciones.UserList[UserIndex].Pos.Map;

		/* 'Posicion de comienzo */
		if (mapa == 0) {
			Declaraciones.UserList[UserIndex].Pos = Declaraciones.Nemahuak;
			mapa = Declaraciones.Nemahuak.Map;
		} else {

			if (!General.MapaValido(mapa)) {
				Protocol.WriteErrorMsg(UserIndex, "El PJ se encuenta en un mapa inválido.");
				CloseSocket(UserIndex);
				return retval;
			}

			/* ' If map has different initial coords, update it */
			int StartMap = 0;
			StartMap = Declaraciones.MapInfo[mapa].StartPos.Map;
			if (StartMap != 0) {
				if (General.MapaValido(StartMap)) {
					Declaraciones.UserList[UserIndex].Pos = Declaraciones.MapInfo[mapa].StartPos;
					mapa = StartMap;
				}
			}

		}

		/*
		 * 'Tratamos de evitar en lo posible el "Telefrag". Solo 1 intento de
		 * loguear en pos adjacentes.
		 */
		/*
		 * 'Codigo por Pablo (ToxicWaste) y revisado por Nacho (Integer),
		 * corregido para que realmetne ande y no tire el server por Juan Martín
		 * Sotuyo Dodero (Maraxus)
		 */
		if (Declaraciones.MapData[mapa][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].UserIndex != 0
				|| Declaraciones.MapData[mapa][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].NpcIndex != 0) {
			boolean FoundPlace = false;
			boolean esAgua = false;
			int tX = 0;
			int tY = 0;

			FoundPlace = false;
			esAgua = General.HayAgua(mapa, Declaraciones.UserList[UserIndex].Pos.X,
					Declaraciones.UserList[UserIndex].Pos.Y);

			for (tY = (Declaraciones.UserList[UserIndex].Pos.Y - 1); tY <= (Declaraciones.UserList[UserIndex].Pos.Y
					+ 1); tY++) {
				for (tX = (Declaraciones.UserList[UserIndex].Pos.X - 1); tX <= (Declaraciones.UserList[UserIndex].Pos.X
						+ 1); tX++) {
					if (esAgua) {
						/*
						 * 'reviso que sea pos legal en agua, que no haya User
						 * ni NPC para poder loguear.
						 */
						if (Extra.LegalPos(mapa, tX, tY, true, false)) {
							FoundPlace = true;
							break; /* FIXME: EXIT FOR */
						}
					} else {
						/*
						 * 'reviso que sea pos legal en tierra, que no haya User
						 * ni NPC para poder loguear.
						 */
						if (Extra.LegalPos(mapa, tX, tY, false, true)) {
							FoundPlace = true;
							break; /* FIXME: EXIT FOR */
						}
					}
				}

				if (FoundPlace) {
					break; /* FIXME: EXIT FOR */
				}
			}

			/* 'Si encontramos un lugar, listo, nos quedamos ahi */
			if (FoundPlace) {
				Declaraciones.UserList[UserIndex].Pos.X = tX;
				Declaraciones.UserList[UserIndex].Pos.Y = tY;
			} else {
				/*
				 * 'Si no encontramos un lugar, sacamos al usuario que tenemos
				 * abajo, y si es un NPC, lo pisamos.
				 */
				if (Declaraciones.MapData[mapa][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].UserIndex != 0) {
					/*
					 * 'Si no encontramos lugar, y abajo teniamos a un usuario,
					 * lo pisamos y cerramos su comercio seguro
					 */
					if (Declaraciones.UserList[Declaraciones.MapData[mapa][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].UserIndex].ComUsu.DestUsu > 0) {
						/*
						 * 'Le avisamos al que estaba comerciando que se tuvo
						 * que ir.
						 */
						if (Declaraciones.UserList[Declaraciones.UserList[Declaraciones.MapData[mapa][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].UserIndex].ComUsu.DestUsu].flags.UserLogged) {
							mdlCOmercioConUsuario.FinComerciarUsu(
									Declaraciones.UserList[Declaraciones.MapData[mapa][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].UserIndex].ComUsu.DestUsu);
							Protocol.WriteConsoleMsg(
									Declaraciones.UserList[Declaraciones.MapData[mapa][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].UserIndex].ComUsu.DestUsu,
									"Comercio cancelado. El otro usuario se ha desconectado.",
									FontTypeNames.FONTTYPE_TALK);
							Protocol.FlushBuffer(
									Declaraciones.UserList[Declaraciones.MapData[mapa][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].UserIndex].ComUsu.DestUsu);
						}
						/* 'Lo sacamos. */
						if (Declaraciones.UserList[Declaraciones.MapData[mapa][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].UserIndex].flags.UserLogged) {
							mdlCOmercioConUsuario.FinComerciarUsu(
									Declaraciones.MapData[mapa][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].UserIndex);
							Protocol.WriteErrorMsg(
									Declaraciones.MapData[mapa][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].UserIndex,
									"Alguien se ha conectado donde te encontrabas, por favor reconéctate...");
							Protocol.FlushBuffer(
									Declaraciones.MapData[mapa][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].UserIndex);
						}
					}

					CloseSocket(
							Declaraciones.MapData[mapa][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].UserIndex);
				}
			}
		}

		/* 'Nombre de sistema */
		Declaraciones.UserList[UserIndex].Name = Name;

		/* 'Por default los nombres son visibles */
		Declaraciones.UserList[UserIndex].showName = true;

		/* 'If in the water, and has a boat, equip it! */
		if (Declaraciones.UserList[UserIndex].Invent.BarcoObjIndex > 0 && (General.HayAgua(mapa,
				Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y)
				|| UsUaRiOs.BodyIsBoat(Declaraciones.UserList[UserIndex].Char.body))) {

			Declaraciones.UserList[UserIndex].Char.Head = 0;
			if (Declaraciones.UserList[UserIndex].flags.Muerto == 0) {
				UsUaRiOs.ToggleBoatBody(UserIndex);
			} else {
				Declaraciones.UserList[UserIndex].Char.body = Declaraciones.iFragataFantasmal;
				Declaraciones.UserList[UserIndex].Char.ShieldAnim = Declaraciones.NingunEscudo;
				Declaraciones.UserList[UserIndex].Char.WeaponAnim = Declaraciones.NingunArma;
				Declaraciones.UserList[UserIndex].Char.CascoAnim = Declaraciones.NingunCasco;
			}

			Declaraciones.UserList[UserIndex].flags.Navegando = 1;
		}

		/* 'Info */
		/* 'Enviamos el User index */
		Protocol.WriteUserIndexInServer(UserIndex);
		/* 'Carga el mapa */
		Protocol.WriteChangeMap(UserIndex, Declaraciones.UserList[UserIndex].Pos.Map,
				Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].MapVersion);
		Protocol.WritePlayMidi(UserIndex, vb6
				.val(General.ReadField(1, Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].Music, 45)));

		if (Declaraciones.UserList[UserIndex].flags.Privilegios == PlayerType.Dios) {
			Declaraciones.UserList[UserIndex].flags.ChatColor = vb6.RGB(250, 250, 150);
		} else if (Declaraciones.UserList[UserIndex].flags.Privilegios != PlayerType.User
				&& Declaraciones.UserList[UserIndex].flags.Privilegios != (PlayerType.User || PlayerType.ChaosCouncil)
				&& Declaraciones.UserList[UserIndex].flags.Privilegios != (PlayerType.User
						|| PlayerType.RoyalCouncil)) {
			Declaraciones.UserList[UserIndex].flags.ChatColor = vb6.RGB(0, 255, 0);
		} else if (Declaraciones.UserList[UserIndex].flags.Privilegios == (PlayerType.User
				|| PlayerType.RoyalCouncil)) {
			Declaraciones.UserList[UserIndex].flags.ChatColor = vb6.RGB(0, 255, 255);
		} else if (Declaraciones.UserList[UserIndex].flags.Privilegios == (PlayerType.User
				|| PlayerType.ChaosCouncil)) {
			Declaraciones.UserList[UserIndex].flags.ChatColor = vb6.RGB(255, 128, 64);
		} else {
			Declaraciones.UserList[UserIndex].flags.ChatColor = 0x00ffffff;
		}

		/* ''[EL OSO]: TRAIGO ESTO ACA ARRIBA PARA DARLE EL IP! */
		/* # IF ConUpTime THEN */
		Declaraciones.UserList[UserIndex].LogOnTime = vb6.Now();
		/* # END IF */

		/* 'Crea el personaje del usuario (hubo algun error) */
		if (!UsUaRiOs.MakeUserChar(true, Declaraciones.UserList[UserIndex].Pos.Map, UserIndex,
				Declaraciones.UserList[UserIndex].Pos.Map, Declaraciones.UserList[UserIndex].Pos.X,
				Declaraciones.UserList[UserIndex].Pos.Y)) {
			return retval;
		}

		if ((Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.User || PlayerType.RoleMaster)) == 0) {
			Trabajo.DoAdminInvisible(UserIndex);
			Declaraciones.UserList[UserIndex].flags.SendDenounces = true;
		}

		Protocol.WriteUserCharIndexInServer(UserIndex);
		/* ''[/el oso] */

		Extra.DoTileEvents(UserIndex, Declaraciones.UserList[UserIndex].Pos.Map,
				Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y);

		UsUaRiOs.CheckUserLevel(UserIndex);
		Protocol.WriteUpdateUserStats(UserIndex);

		Protocol.WriteUpdateHungerAndThirst(UserIndex);
		Protocol.WriteUpdateStrenghtAndDexterity(UserIndex);

		SendMOTD(UserIndex);

		if (Declaraciones.haciendoBK) {
			Protocol.WritePauseToggle(UserIndex);
			Protocol.WriteConsoleMsg(UserIndex,
					"Servidor> Por favor espera algunos segundos, el WorldSave está ejecutándose.",
					FontTypeNames.FONTTYPE_SERVER);
		}

		if (Declaraciones.EnPausa) {
			Protocol.WritePauseToggle(UserIndex);
			Protocol.WriteConsoleMsg(UserIndex,
					"Servidor> Lo sentimos mucho pero el servidor se encuentra actualmente detenido. Intenta ingresar más tarde.",
					FontTypeNames.FONTTYPE_SERVER);
		}

		if (Declaraciones.EnTesting && Declaraciones.UserList[UserIndex].Stats.ELV >= 18) {
			Protocol.WriteErrorMsg(UserIndex,
					"Servidor en Testing por unos minutos, conectese con PJs de nivel menor a 18. No se conecte con Pjs que puedan resultar importantes por ahora pues pueden arruinarse.");
			Protocol.FlushBuffer(UserIndex);
			CloseSocket(UserIndex);
			return retval;
		}

		/* 'Actualiza el Num de usuarios */
		/* 'DE ACA EN ADELANTE GRABA EL CHARFILE, OJO! */
		Declaraciones.NumUsers = Declaraciones.NumUsers + 1;
		Declaraciones.UserList[UserIndex].flags.UserLogged = true;

		/* 'usado para borrar Pjs */
		ES.WriteVar(Declaraciones.CharPath + Declaraciones.UserList[UserIndex].Name + ".chr", "INIT", "Logged", "1");

		Admin.EstadisticasWeb.Informar(CANTIDAD_ONLINE, Declaraciones.NumUsers);

		Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].NumUsers = Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].NumUsers
				+ 1;

		if (Declaraciones.UserList[UserIndex].Stats.SkillPts > 0) {
			Protocol.WriteSendSkills(UserIndex);
			Protocol.WriteLevelUp(UserIndex, Declaraciones.UserList[UserIndex].Stats.SkillPts);
		}

		if (Declaraciones.NumUsers > Declaraciones.RECORDusuarios) {
			modSendData.SendData(SendTarget.ToAll, 0, Protocol.PrepareMessageConsoleMsg(
					"RECORD de usuarios conectados simultaneamente." + "Hay " + Declaraciones.NumUsers + " usuarios.",
					FontTypeNames.FONTTYPE_INFO));
			Declaraciones.RECORDusuarios = Declaraciones.NumUsers;
			ES.WriteVar(Declaraciones.IniPath + "Server.ini", "INIT", "RECORD", vb6.str(Declaraciones.RECORDusuarios));

			Admin.EstadisticasWeb.Informar(RECORD_USUARIOS, Declaraciones.RECORDusuarios);
		}

		if (Declaraciones.UserList[UserIndex].NroMascotas > 0
				&& Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].Pk) {
			int i = 0;
			for (i = (1); i <= (Declaraciones.MAXMASCOTAS); i++) {
				if (Declaraciones.UserList[UserIndex].MascotasType[i] > 0) {
					Declaraciones.UserList[UserIndex].MascotasIndex[i] = NPCs.SpawnNpc(
							Declaraciones.UserList[UserIndex].MascotasType[i], Declaraciones.UserList[UserIndex].Pos,
							true, true);

					if (Declaraciones.UserList[UserIndex].MascotasIndex[i] > 0) {
						Declaraciones.Npclist[Declaraciones.UserList[UserIndex].MascotasIndex[i]].MaestroUser = UserIndex;
						NPCs.FollowAmo(Declaraciones.UserList[UserIndex].MascotasIndex[i]);
					} else {
						Declaraciones.UserList[UserIndex].MascotasIndex[i] = 0;
					}
				}
			}
		}

		if (Declaraciones.UserList[UserIndex].flags.Navegando == 1) {
			Protocol.WriteNavigateToggle(UserIndex);
		}

		if (ES.criminal(UserIndex)) {
			/* 'Call WriteSafeModeOff(UserIndex) */
			Protocol.WriteMultiMessage(UserIndex, eMessages.SafeModeOff);
			Declaraciones.UserList[UserIndex].flags.Seguro = false;
		} else {
			Declaraciones.UserList[UserIndex].flags.Seguro = true;
			/* 'Call WriteSafeModeOn(UserIndex) */
			Protocol.WriteMultiMessage(UserIndex, eMessages.SafeModeOn);
		}

		if (Declaraciones.UserList[UserIndex].GuildIndex > 0) {
			/* 'welcome to the show baby... */
			if (!modGuilds.m_ConectarMiembroAClan(UserIndex, Declaraciones.UserList[UserIndex].GuildIndex)) {
				Protocol.WriteConsoleMsg(UserIndex, "Tu estado no te permite entrar al clan.",
						FontTypeNames.FONTTYPE_GUILD);
			}
		}

		modSendData.SendData(SendTarget.ToPCArea, UserIndex,
				Protocol.PrepareMessageCreateFX(Declaraciones.UserList[UserIndex].Char.CharIndex, FXIDs.FXWARP, 0));

		Protocol.WriteLoggedMessage(UserIndex);

		modGuilds.SendGuildNews(UserIndex);

		/*
		 * ' Esta protegido del ataque de npcs por 5 segundos, si no realiza
		 * ninguna accion
		 */
		modNuevoTimer.IntervaloPermiteSerAtacado(UserIndex, true);

		if (Admin.Lloviendo) {
			Protocol.WriteRainToggle(UserIndex);
		}

		tStr = modGuilds.a_ObtenerRechazoDeChar(Declaraciones.UserList[UserIndex].Name);

		if (vb6.LenB(tStr) != 0) {
			Protocol.WriteShowMessageBox(UserIndex,
					"Tu solicitud de ingreso al clan ha sido rechazada. El clan te explica que: " + tStr);
		}

		/* 'Load the user statistics */
		Statistics.UserConnected(UserIndex);

		General.MostrarNumUsers();

		/* # IF SeguridadAlkon THEN */
		/* # END IF */

		N = vb6.FreeFile();
		/* FIXME: OPEN App . Path & "\\logs\\numusers.log" FOR OUTPUT AS N */
		/* FIXME: PRINT # N , NumUsers */
		/* FIXME: CLOSE # N */

		N = vb6.FreeFile();
		/* 'Log */
		/*
		 * FIXME: OPEN App . Path & "\\logs\\Connect.log" FOR Append Shared AS #
		 * N
		 */
		/*
		 * FIXME: PRINT # N , . Name & " ha entrado al juego. UserIndex:" &
		 * UserIndex & " " & time & " " & Date
		 */
		/* FIXME: CLOSE # N */

		retval = true;

		return retval;
	}

	static void SendMOTD(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int j = 0;

		Protocol.WriteGuildChat(UserIndex, "Mensajes de entrada:");
		for (j = (1); j <= (Admin.MaxLines); j++) {
			Protocol.WriteGuildChat(UserIndex, Admin.MOTD[j].texto);
		}
	}

	static void ResetFacciones(int UserIndex) {
		/* '************************************************* */
		/* 'Author: Unknown */
		/* 'Last modified: 23/01/2007 */
		/* 'Resetea todos los valores generales y las stats */
		/*
		 * '03/15/2006 Maraxus - Uso de With para mayor performance y claridad.
		 */
		/*
		 * '23/01/2007 Pablo (ToxicWaste) - Agrego NivelIngreso, FechaIngreso,
		 * MatadosIngreso y NextRecompensa.
		 */
		/* '************************************************* */
		Declaraciones.UserList[UserIndex].Faccion.ArmadaReal = 0;
		Declaraciones.UserList[UserIndex].Faccion.CiudadanosMatados = 0;
		Declaraciones.UserList[UserIndex].Faccion.CriminalesMatados = 0;
		Declaraciones.UserList[UserIndex].Faccion.FuerzasCaos = 0;
		Declaraciones.UserList[UserIndex].Faccion.FechaIngreso = "No ingresó a ninguna Facción";
		Declaraciones.UserList[UserIndex].Faccion.RecibioArmaduraCaos = 0;
		Declaraciones.UserList[UserIndex].Faccion.RecibioArmaduraReal = 0;
		Declaraciones.UserList[UserIndex].Faccion.RecibioExpInicialCaos = 0;
		Declaraciones.UserList[UserIndex].Faccion.RecibioExpInicialReal = 0;
		Declaraciones.UserList[UserIndex].Faccion.RecompensasCaos = 0;
		Declaraciones.UserList[UserIndex].Faccion.RecompensasReal = 0;
		Declaraciones.UserList[UserIndex].Faccion.Reenlistadas = 0;
		Declaraciones.UserList[UserIndex].Faccion.NivelIngreso = 0;
		Declaraciones.UserList[UserIndex].Faccion.MatadosIngreso = 0;
		Declaraciones.UserList[UserIndex].Faccion.NextRecompensa = 0;
	}

	static void ResetContadores(int UserIndex) {
		/* '************************************************* */
		/* 'Author: Unknown */
		/* 'Last modified: 10/07/2010 */
		/* 'Resetea todos los valores generales y las stats */
		/*
		 * '03/15/2006 Maraxus - Uso de With para mayor performance y claridad.
		 */
		/* '05/20/2007 Integer - Agregue todas las variables que faltaban. */
		/* '10/07/2010: ZaMa - Agrego los counters que faltaban. */
		/* '************************************************* */
		Declaraciones.UserList[UserIndex].Counters.AGUACounter = 0;
		Declaraciones.UserList[UserIndex].Counters.AsignedSkills = 0;
		Declaraciones.UserList[UserIndex].Counters.AttackCounter = 0;
		Declaraciones.UserList[UserIndex].Counters.bPuedeMeditar = true;
		Declaraciones.UserList[UserIndex].Counters.Ceguera = 0;
		Declaraciones.UserList[UserIndex].Counters.COMCounter = 0;
		Declaraciones.UserList[UserIndex].Counters.Estupidez = 0;
		Declaraciones.UserList[UserIndex].Counters.failedUsageAttempts = 0;
		Declaraciones.UserList[UserIndex].Counters.Frio = 0;
		Declaraciones.UserList[UserIndex].Counters.goHome = 0;
		Declaraciones.UserList[UserIndex].Counters.HPCounter = 0;
		Declaraciones.UserList[UserIndex].Counters.IdleCount = 0;
		Declaraciones.UserList[UserIndex].Counters.Invisibilidad = 0;
		Declaraciones.UserList[UserIndex].Counters.Lava = 0;
		Declaraciones.UserList[UserIndex].Counters.Mimetismo = 0;
		Declaraciones.UserList[UserIndex].Counters.Ocultando = 0;
		Declaraciones.UserList[UserIndex].Counters.Paralisis = 0;
		Declaraciones.UserList[UserIndex].Counters.Pena = 0;
		Declaraciones.UserList[UserIndex].Counters.PiqueteC = 0;
		Declaraciones.UserList[UserIndex].Counters.Saliendo = false;
		Declaraciones.UserList[UserIndex].Counters.Salir = 0;
		Declaraciones.UserList[UserIndex].Counters.STACounter = 0;
		Declaraciones.UserList[UserIndex].Counters.TiempoOculto = 0;
		Declaraciones.UserList[UserIndex].Counters.TimerEstadoAtacable = 0;
		Declaraciones.UserList[UserIndex].Counters.TimerGolpeMagia = 0;
		Declaraciones.UserList[UserIndex].Counters.TimerGolpeUsar = 0;
		Declaraciones.UserList[UserIndex].Counters.TimerLanzarSpell = 0;
		Declaraciones.UserList[UserIndex].Counters.TimerMagiaGolpe = 0;
		Declaraciones.UserList[UserIndex].Counters.TimerPerteneceNpc = 0;
		Declaraciones.UserList[UserIndex].Counters.TimerPuedeAtacar = 0;
		Declaraciones.UserList[UserIndex].Counters.TimerPuedeSerAtacado = 0;
		Declaraciones.UserList[UserIndex].Counters.TimerPuedeTrabajar = 0;
		Declaraciones.UserList[UserIndex].Counters.TimerPuedeUsarArco = 0;
		Declaraciones.UserList[UserIndex].Counters.TimerUsar = 0;
		Declaraciones.UserList[UserIndex].Counters.Trabajando = 0;
		Declaraciones.UserList[UserIndex].Counters.Veneno = 0;
	}

	static void ResetCharInfo(int UserIndex) {
		/* '************************************************* */
		/* 'Author: Unknown */
		/* 'Last modified: 03/15/2006 */
		/* 'Resetea todos los valores generales y las stats */
		/*
		 * '03/15/2006 Maraxus - Uso de With para mayor performance y claridad.
		 */
		/* '************************************************* */
		Declaraciones.UserList[UserIndex].Char.body = 0;
		Declaraciones.UserList[UserIndex].Char.CascoAnim = 0;
		Declaraciones.UserList[UserIndex].Char.CharIndex = 0;
		Declaraciones.UserList[UserIndex].Char.FX = 0;
		Declaraciones.UserList[UserIndex].Char.Head = 0;
		Declaraciones.UserList[UserIndex].Char.loops = 0;
		Declaraciones.UserList[UserIndex].Char.heading = 0;
		Declaraciones.UserList[UserIndex].Char.loops = 0;
		Declaraciones.UserList[UserIndex].Char.ShieldAnim = 0;
		Declaraciones.UserList[UserIndex].Char.WeaponAnim = 0;
	}

	static void ResetBasicUserInfo(int UserIndex) {
		/* '************************************************* */
		/* 'Author: Unknown */
		/* 'Last modified: 03/15/2006 */
		/* 'Resetea todos los valores generales y las stats */
		/*
		 * '03/15/2006 Maraxus - Uso de With para mayor performance y claridad.
		 */
		/* '************************************************* */
		Declaraciones.UserList[UserIndex].Name = "";
		Declaraciones.UserList[UserIndex].desc = "";
		Declaraciones.UserList[UserIndex].DescRM = "";
		Declaraciones.UserList[UserIndex].Pos.Map = 0;
		Declaraciones.UserList[UserIndex].Pos.X = 0;
		Declaraciones.UserList[UserIndex].Pos.Y = 0;
		Declaraciones.UserList[UserIndex].ip = "";
		Declaraciones.UserList[UserIndex].clase = 0;
		Declaraciones.UserList[UserIndex].email = "";
		Declaraciones.UserList[UserIndex].Genero = 0;
		Declaraciones.UserList[UserIndex].Hogar = 0;
		Declaraciones.UserList[UserIndex].raza = 0;

		Declaraciones.UserList[UserIndex].PartyIndex = 0;
		Declaraciones.UserList[UserIndex].PartySolicitud = 0;

		Declaraciones.UserList[UserIndex].Stats.Banco = 0;
		Declaraciones.UserList[UserIndex].Stats.ELV = 0;
		Declaraciones.UserList[UserIndex].Stats.ELU = 0;
		Declaraciones.UserList[UserIndex].Stats.Exp = 0;
		Declaraciones.UserList[UserIndex].Stats.def = 0;
		/* '.CriminalesMatados = 0 */
		Declaraciones.UserList[UserIndex].Stats.NPCsMuertos = 0;
		Declaraciones.UserList[UserIndex].Stats.UsuariosMatados = 0;
		Declaraciones.UserList[UserIndex].Stats.SkillPts = 0;
		Declaraciones.UserList[UserIndex].Stats.GLD = 0;
		Declaraciones.UserList[UserIndex].Stats.UserAtributos[1] = 0;
		Declaraciones.UserList[UserIndex].Stats.UserAtributos[2] = 0;
		Declaraciones.UserList[UserIndex].Stats.UserAtributos[3] = 0;
		Declaraciones.UserList[UserIndex].Stats.UserAtributos[4] = 0;
		Declaraciones.UserList[UserIndex].Stats.UserAtributos[5] = 0;
		Declaraciones.UserList[UserIndex].Stats.UserAtributosBackUP[1] = 0;
		Declaraciones.UserList[UserIndex].Stats.UserAtributosBackUP[2] = 0;
		Declaraciones.UserList[UserIndex].Stats.UserAtributosBackUP[3] = 0;
		Declaraciones.UserList[UserIndex].Stats.UserAtributosBackUP[4] = 0;
		Declaraciones.UserList[UserIndex].Stats.UserAtributosBackUP[5] = 0;

	}

	static void ResetReputacion(int UserIndex) {
		/* '************************************************* */
		/* 'Author: Unknown */
		/* 'Last modified: 03/15/2006 */
		/* 'Resetea todos los valores generales y las stats */
		/*
		 * '03/15/2006 Maraxus - Uso de With para mayor performance y claridad.
		 */
		/* '************************************************* */
		Declaraciones.UserList[UserIndex].Reputacion.AsesinoRep = 0;
		Declaraciones.UserList[UserIndex].Reputacion.BandidoRep = 0;
		Declaraciones.UserList[UserIndex].Reputacion.BurguesRep = 0;
		Declaraciones.UserList[UserIndex].Reputacion.LadronesRep = 0;
		Declaraciones.UserList[UserIndex].Reputacion.NobleRep = 0;
		Declaraciones.UserList[UserIndex].Reputacion.PlebeRep = 0;
		Declaraciones.UserList[UserIndex].Reputacion.NobleRep = 0;
		Declaraciones.UserList[UserIndex].Reputacion.Promedio = 0;
	}

	static void ResetGuildInfo(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		if (Declaraciones.UserList[UserIndex].EscucheClan > 0) {
			modGuilds.GMDejaDeEscucharClan(UserIndex, Declaraciones.UserList[UserIndex].EscucheClan);
			Declaraciones.UserList[UserIndex].EscucheClan = 0;
		}
		if (Declaraciones.UserList[UserIndex].GuildIndex > 0) {
			modGuilds.m_DesconectarMiembroDelClan(UserIndex, Declaraciones.UserList[UserIndex].GuildIndex);
		}
		Declaraciones.UserList[UserIndex].GuildIndex = 0;
	}

	static void ResetUserFlags(int UserIndex) {
		/* '************************************************* */
		/* 'Author: Unknown */
		/* 'Last modified: 06/28/2008 */
		/* 'Resetea todos los valores generales y las stats */
		/*
		 * '03/15/2006 Maraxus - Uso de With para mayor performance y claridad.
		 */
		/* '03/29/2006 Maraxus - Reseteo el CentinelaOK también. */
		/* '06/28/2008 NicoNZ - Agrego el flag Inmovilizado */
		/* '************************************************* */
		Declaraciones.UserList[UserIndex].flags.Comerciando = false;
		Declaraciones.UserList[UserIndex].flags.Ban = 0;
		Declaraciones.UserList[UserIndex].flags.Escondido = 0;
		Declaraciones.UserList[UserIndex].flags.DuracionEfecto = 0;
		Declaraciones.UserList[UserIndex].flags.NpcInv = 0;
		Declaraciones.UserList[UserIndex].flags.StatsChanged = 0;
		Declaraciones.UserList[UserIndex].flags.TargetNPC = 0;
		Declaraciones.UserList[UserIndex].flags.TargetNpcTipo = eNPCType.Comun;
		Declaraciones.UserList[UserIndex].flags.TargetObj = 0;
		Declaraciones.UserList[UserIndex].flags.TargetObjMap = 0;
		Declaraciones.UserList[UserIndex].flags.TargetObjX = 0;
		Declaraciones.UserList[UserIndex].flags.TargetObjY = 0;
		Declaraciones.UserList[UserIndex].flags.TargetUser = 0;
		Declaraciones.UserList[UserIndex].flags.TipoPocion = 0;
		Declaraciones.UserList[UserIndex].flags.TomoPocion = false;
		Declaraciones.UserList[UserIndex].flags.Descuento = "";
		Declaraciones.UserList[UserIndex].flags.Hambre = 0;
		Declaraciones.UserList[UserIndex].flags.Sed = 0;
		Declaraciones.UserList[UserIndex].flags.Descansar = false;
		Declaraciones.UserList[UserIndex].flags.Vuela = 0;
		Declaraciones.UserList[UserIndex].flags.Navegando = 0;
		Declaraciones.UserList[UserIndex].flags.Oculto = 0;
		Declaraciones.UserList[UserIndex].flags.Envenenado = 0;
		Declaraciones.UserList[UserIndex].flags.invisible = 0;
		Declaraciones.UserList[UserIndex].flags.Paralizado = 0;
		Declaraciones.UserList[UserIndex].flags.Inmovilizado = 0;
		Declaraciones.UserList[UserIndex].flags.Maldicion = 0;
		Declaraciones.UserList[UserIndex].flags.Bendicion = 0;
		Declaraciones.UserList[UserIndex].flags.Meditando = 0;
		Declaraciones.UserList[UserIndex].flags.Privilegios = 0;
		Declaraciones.UserList[UserIndex].flags.PrivEspecial = false;
		Declaraciones.UserList[UserIndex].flags.PuedeMoverse = 0;
		Declaraciones.UserList[UserIndex].flags.OldBody = 0;
		Declaraciones.UserList[UserIndex].flags.OldHead = 0;
		Declaraciones.UserList[UserIndex].flags.AdminInvisible = 0;
		Declaraciones.UserList[UserIndex].flags.ValCoDe = 0;
		Declaraciones.UserList[UserIndex].flags.Hechizo = 0;
		Declaraciones.UserList[UserIndex].flags.TimesWalk = 0;
		Declaraciones.UserList[UserIndex].flags.StartWalk = 0;
		Declaraciones.UserList[UserIndex].flags.CountSH = 0;
		Declaraciones.UserList[UserIndex].flags.Silenciado = 0;
		Declaraciones.UserList[UserIndex].flags.CentinelaOK = false;
		Declaraciones.UserList[UserIndex].flags.CentinelaIndex = 0;
		Declaraciones.UserList[UserIndex].flags.AdminPerseguible = false;
		Declaraciones.UserList[UserIndex].flags.lastMap = 0;
		Declaraciones.UserList[UserIndex].flags.Traveling = 0;
		Declaraciones.UserList[UserIndex].flags.AtacablePor = 0;
		Declaraciones.UserList[UserIndex].flags.AtacadoPorNpc = 0;
		Declaraciones.UserList[UserIndex].flags.AtacadoPorUser = 0;
		Declaraciones.UserList[UserIndex].flags.NoPuedeSerAtacado = false;
		Declaraciones.UserList[UserIndex].flags.ShareNpcWith = 0;
		Declaraciones.UserList[UserIndex].flags.EnConsulta = false;
		Declaraciones.UserList[UserIndex].flags.Ignorado = false;
		Declaraciones.UserList[UserIndex].flags.SendDenounces = false;
		Declaraciones.UserList[UserIndex].flags.ParalizedBy = "";
		Declaraciones.UserList[UserIndex].flags.ParalizedByIndex = 0;
		Declaraciones.UserList[UserIndex].flags.ParalizedByNpcIndex = 0;

		if (Declaraciones.UserList[UserIndex].flags.OwnedNpc != 0) {
			UsUaRiOs.PerdioNpc(UserIndex);
		}

	}

	static void ResetUserSpells(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int LoopC = 0;
		for (LoopC = (1); LoopC <= (Declaraciones.MAXUSERHECHIZOS); LoopC++) {
			Declaraciones.UserList[UserIndex].Stats.UserHechizos[LoopC] = 0;
		}
	}

	static void ResetUserPets(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int LoopC = 0;

		Declaraciones.UserList[UserIndex].NroMascotas = 0;

		for (LoopC = (1); LoopC <= (Declaraciones.MAXMASCOTAS); LoopC++) {
			Declaraciones.UserList[UserIndex].MascotasIndex[LoopC] = 0;
			Declaraciones.UserList[UserIndex].MascotasType[LoopC] = 0;
		}
	}

	static void ResetUserBanco(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int LoopC = 0;

		for (LoopC = (1); LoopC <= (Declaraciones.MAX_BANCOINVENTORY_SLOTS); LoopC++) {
			Declaraciones.UserList[UserIndex].BancoInvent.Object[LoopC].Amount = 0;
			Declaraciones.UserList[UserIndex].BancoInvent.Object[LoopC].Equipped = 0;
			Declaraciones.UserList[UserIndex].BancoInvent.Object[LoopC].ObjIndex = 0;
		}

		Declaraciones.UserList[UserIndex].BancoInvent.NroItems = 0;
	}

	static void LimpiarComercioSeguro(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		if (Declaraciones.UserList[UserIndex].ComUsu.DestUsu > 0) {
			mdlCOmercioConUsuario.FinComerciarUsu(Declaraciones.UserList[UserIndex].ComUsu.DestUsu);
			mdlCOmercioConUsuario.FinComerciarUsu(UserIndex);
		}
	}

	static void ResetUserSlot(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int i = 0;

		Declaraciones.UserList[UserIndex].ConnIDValida = false;
		Declaraciones.UserList[UserIndex].ConnID = -1;

		LimpiarComercioSeguro(UserIndex);
		ResetFacciones(UserIndex);
		ResetContadores(UserIndex);
		ResetGuildInfo(UserIndex);
		ResetCharInfo(UserIndex);
		ResetBasicUserInfo(UserIndex);
		ResetReputacion(UserIndex);
		ResetUserFlags(UserIndex);
		InvUsuario.LimpiarInventario(UserIndex);
		ResetUserSpells(UserIndex);
		ResetUserPets(UserIndex);
		ResetUserBanco(UserIndex);

		Declaraciones.UserList[UserIndex].ComUsu.Acepto = false;

		for (i = (1); i <= (mdlCOmercioConUsuario.MAX_OFFER_SLOTS); i++) {
			Declaraciones.UserList[UserIndex].ComUsu.cant[i] = 0;
			Declaraciones.UserList[UserIndex].ComUsu.Objeto[i] = 0;
		}

		Declaraciones.UserList[UserIndex].ComUsu.GoldAmount = 0;
		Declaraciones.UserList[UserIndex].ComUsu.DestNick = "";
		Declaraciones.UserList[UserIndex].ComUsu.DestUsu = 0;

		/* # IF SeguridadAlkon THEN */
		/* # END IF */
	}

	static void CloseUser(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int N = 0;
		int Map = 0;
		String Name;
		int i = 0;

		int aN = 0;

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

		Map = Declaraciones.UserList[UserIndex].Pos.Map;
		Name = vb6.UCase(Declaraciones.UserList[UserIndex].Name);

		Declaraciones.UserList[UserIndex].Char.FX = 0;
		Declaraciones.UserList[UserIndex].Char.loops = 0;
		modSendData.SendData(SendTarget.ToPCArea, UserIndex,
				Protocol.PrepareMessageCreateFX(Declaraciones.UserList[UserIndex].Char.CharIndex, 0, 0));

		Declaraciones.UserList[UserIndex].flags.UserLogged = false;
		Declaraciones.UserList[UserIndex].Counters.Saliendo = false;

		/* 'Le devolvemos el body y head originales */
		if (Declaraciones.UserList[UserIndex].flags.AdminInvisible == 1) {
			Declaraciones.UserList[UserIndex].Char.body = Declaraciones.UserList[UserIndex].flags.OldBody;
			Declaraciones.UserList[UserIndex].Char.Head = Declaraciones.UserList[UserIndex].flags.OldHead;
			Declaraciones.UserList[UserIndex].flags.AdminInvisible = 0;
		}

		/* 'si esta en party le devolvemos la experiencia */
		if (Declaraciones.UserList[UserIndex].PartyIndex > 0) {
			mdParty.SalirDeParty(UserIndex);
		}

		/* 'Save statistics */
		Statistics.UserDisconnected(UserIndex);

		/* ' Grabamos el personaje del usuario */
		ES.SaveUser(UserIndex, Declaraciones.CharPath + Name + ".chr");

		/* 'usado para borrar Pjs */
		ES.WriteVar(Declaraciones.CharPath + Declaraciones.UserList[UserIndex].Name + ".chr", "INIT", "Logged", "0");

		/* 'Quitar el dialogo */
		/* 'If MapInfo(Map).NumUsers > 0 Then */
		/* ' Call SendToUserArea(UserIndex, "QDL" & .Char.charindex) */
		/* 'End If */

		if (General.MapaValido(Map)) {
			if (Declaraciones.MapInfo[Map].NumUsers > 0) {
				modSendData.SendData(SendTarget.ToPCAreaButIndex, UserIndex,
						Protocol.PrepareMessageRemoveCharDialog(Declaraciones.UserList[UserIndex].Char.CharIndex));
			}

			/* 'Update Map Users */
			Declaraciones.MapInfo[Map].NumUsers = Declaraciones.MapInfo[Map].NumUsers - 1;

			if (Declaraciones.MapInfo[Map].NumUsers < 0) {
				Declaraciones.MapInfo[Map].NumUsers = 0;
			}
		}

		/* 'Borrar el personaje */
		if (Declaraciones.UserList[UserIndex].Char.CharIndex > 0) {
			UsUaRiOs.EraseUserChar(UserIndex, Declaraciones.UserList[UserIndex].flags.AdminInvisible == 1);
		}

		/* 'Borrar mascotas */
		for (i = (1); i <= (Declaraciones.MAXMASCOTAS); i++) {
			if (Declaraciones.UserList[UserIndex].MascotasIndex[i] > 0) {
				if (Declaraciones.Npclist[Declaraciones.UserList[UserIndex].MascotasIndex[i]].flags.NPCActive) {
					NPCs.QuitarNPC(Declaraciones.UserList[UserIndex].MascotasIndex[i]);
				}
			}
		}

		/* ' Si el usuario habia dejado un msg en la gm's queue lo borramos */
		if (Declaraciones.Ayuda.Existe(Declaraciones.UserList[UserIndex].Name)) {
			Declaraciones.Ayuda.Quitar(Declaraciones.UserList[UserIndex].Name);
		}

		ResetUserSlot(UserIndex);

		General.MostrarNumUsers();

		N = vb6.FreeFile(1);
		/*
		 * FIXME: OPEN App . Path & "\\logs\\Connect.log" FOR Append Shared AS #
		 * N
		 */
		/*
		 * FIXME: PRINT # N , Name & " ha dejado el juego. " & "User Index:" &
		 * UserIndex & " " & time & " " & Date
		 */
		/* FIXME: CLOSE # N */

		return;

		/* FIXME: ErrHandler : */
		String UserName;
		if (UserIndex > 0) {
			UserName = Declaraciones.UserList[UserIndex].Name;
		}

		General.LogError("Error en CloseUser. Número " + Err.Number + " Descripción: " + Err.description + ".User: "
				+ UserName + "(" + UserIndex + "). Map: " + Map);

	}

	static void ReloadSokcet() {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */
		/* # IF UsarQueSocket = 1 THEN */

		wskapiAO.LogApiSock("ReloadSokcet() " + Declaraciones.NumUsers + " " + Declaraciones.LastUser + " "
				+ Declaraciones.MaxUsers);

		if (Declaraciones.NumUsers <= 0) {
			wskapiAO.WSApiReiniciarSockets();
		} else {
			/* ' Call apiclosesocket(SockListen) */
			/* ' SockListen = ListenForConnect(Puerto, hWndMsg, "") */
		}

		/* # ELSEIF UsarQueSocket = 0 THEN */

		/* # ELSEIF UsarQueSocket = 2 THEN */

		/* # END IF */

		return;
		/* FIXME: ErrHandler : */
		General.LogError("Error en CheckSocketState " + Err.Number + ": " + Err.description);

	}

	static void EnviarNoche(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		Protocol.WriteSendNight(UserIndex, vb6.IIf(
				Admin.DeNoche
						&& (Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].Zona == Declaraciones.Campo
								|| Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].Zona == Declaraciones.Ciudad),
				true, false));
		Protocol.WriteSendNight(UserIndex, vb6.IIf(Admin.DeNoche, true, false));
	}

	static void EcharPjsNoPrivilegiados() {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int LoopC = 0;

		for (LoopC = (1); LoopC <= (Declaraciones.LastUser); LoopC++) {
			if (Declaraciones.UserList[LoopC].flags.UserLogged && Declaraciones.UserList[LoopC].ConnID >= 0
					&& Declaraciones.UserList[LoopC].ConnIDValida) {
				if (Declaraciones.UserList[LoopC].flags.Privilegios && PlayerType.User) {
					CloseSocket(LoopC);
				}
			}
		}

	}

}