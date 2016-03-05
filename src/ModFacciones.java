
/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"ModFacciones"')] */
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

public class ModFacciones {

	public static int ArmaduraImperial1;
	public static int ArmaduraImperial2;
	public static int ArmaduraImperial3;
	public static int TunicaMagoImperial;
	public static int TunicaMagoImperialEnanos;
	public static int ArmaduraCaos1;
	public static int ArmaduraCaos2;
	public static int ArmaduraCaos3;
	public static int TunicaMagoCaos;
	public static int TunicaMagoCaosEnanos;

	public static int VestimentaImperialHumano;
	public static int VestimentaImperialEnano;
	public static int TunicaConspicuaHumano;
	public static int TunicaConspicuaEnano;
	public static int ArmaduraNobilisimaHumano;
	public static int ArmaduraNobilisimaEnano;
	public static int ArmaduraGranSacerdote;

	public static int VestimentaLegionHumano;
	public static int VestimentaLegionEnano;
	public static int TunicaLobregaHumano;
	public static int TunicaLobregaEnano;
	public static int TunicaEgregiaHumano;
	public static int TunicaEgregiaEnano;
	public static int SacerdoteDemoniaco;

	static final int NUM_RANGOS_FACCION = 15;
	static final int NUM_DEF_FACCION_ARMOURS = 3;

	static public class tFaccionArmaduras {
		public int[] Armada;
		public int[] Caos;
	}

	/*
	 * ' Matriz que contiene las armaduras faccionarias segun raza, clase,
	 * faccion y defensa de armadura
	 */
public static tFaccionArmaduras[] ArmadurasFaccion = new tFaccionArmaduras[[('1', 'Declaraciones.NUMCLASES'), ('1', 'Declaraciones.NUMRAZAS')]]; /* XXX MULTIDIMENSIONAL [('1', 'Declaraciones.NUMCLASES'), ('1', 'Declaraciones.NUMRAZAS')] */

/* ' Contiene la cantidad de exp otorgada cada vez que aumenta el rango */
	public static int[] RecompensaFacciones = new int[0 + ModFacciones.NUM_RANGOS_FACCION];

	static int GetArmourAmount(int Rango, eTipoDefArmors TipoDef) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Autor: ZaMa */
		/* 'Last Modification: 15/04/2010 */
		/*
		 * 'Returns the amount of armours to give, depending on the specified
		 * rank
		 */
		/* '*************************************************** */

		switch (TipoDef) {

		case ieBaja:
			retval = 20 / (Rango + 1);

			break;

		case ieMedia:
			retval = Rango * 2 / SistemaCombate.MaximoInt((Rango - 4), 1);

			break;

		case ieAlta:
			retval = Rango * 1.35;

			break;
		}

		return retval;
	}

	static void GiveFactionArmours(int UserIndex, boolean IsCaos) {
		/* '*************************************************** */
		/* 'Autor: ZaMa */
		/* 'Last Modification: 15/04/2010 */
		/* 'Gives faction armours to user */
		/* '*************************************************** */

		Declaraciones.Obj ObjArmour;
		int Rango = 0;

		Rango = vb6.val(vb6.IIf(IsCaos, Declaraciones.UserList[UserIndex].Faccion.RecompensasCaos,
				Declaraciones.UserList[UserIndex].Faccion.RecompensasReal)) + 1;

		/* ' Entrego armaduras de defensa baja */
		ObjArmour.Amount = GetArmourAmount(Rango, eTipoDefArmors.ieBaja);

		if (IsCaos) {
			ObjArmour.ObjIndex = ModFacciones.ArmadurasFaccion[Declaraciones.UserList[UserIndex].clase][Declaraciones.UserList[UserIndex].raza].Caos[eTipoDefArmors.ieBaja];
		} else {
			ObjArmour.ObjIndex = ModFacciones.ArmadurasFaccion[Declaraciones.UserList[UserIndex].clase][Declaraciones.UserList[UserIndex].raza].Armada[eTipoDefArmors.ieBaja];
		}

		if (!InvUsuario.MeterItemEnInventario(UserIndex, ObjArmour)) {
			InvNpc.TirarItemAlPiso(Declaraciones.UserList[UserIndex].Pos, ObjArmour);
		}

		/* ' Entrego armaduras de defensa media */
		ObjArmour.Amount = GetArmourAmount(Rango, eTipoDefArmors.ieMedia);

		if (IsCaos) {
			ObjArmour.ObjIndex = ModFacciones.ArmadurasFaccion[Declaraciones.UserList[UserIndex].clase][Declaraciones.UserList[UserIndex].raza].Caos[eTipoDefArmors.ieMedia];
		} else {
			ObjArmour.ObjIndex = ModFacciones.ArmadurasFaccion[Declaraciones.UserList[UserIndex].clase][Declaraciones.UserList[UserIndex].raza].Armada[eTipoDefArmors.ieMedia];
		}

		if (!InvUsuario.MeterItemEnInventario(UserIndex, ObjArmour)) {
			InvNpc.TirarItemAlPiso(Declaraciones.UserList[UserIndex].Pos, ObjArmour);
		}

		/* ' Entrego armaduras de defensa alta */
		ObjArmour.Amount = GetArmourAmount(Rango, eTipoDefArmors.ieAlta);

		if (IsCaos) {
			ObjArmour.ObjIndex = ModFacciones.ArmadurasFaccion[Declaraciones.UserList[UserIndex].clase][Declaraciones.UserList[UserIndex].raza].Caos[eTipoDefArmors.ieAlta];
		} else {
			ObjArmour.ObjIndex = ModFacciones.ArmadurasFaccion[Declaraciones.UserList[UserIndex].clase][Declaraciones.UserList[UserIndex].raza].Armada[eTipoDefArmors.ieAlta];
		}

		if (!InvUsuario.MeterItemEnInventario(UserIndex, ObjArmour)) {
			InvNpc.TirarItemAlPiso(Declaraciones.UserList[UserIndex].Pos, ObjArmour);
		}

	}

	static void GiveExpReward(int UserIndex, int Rango) {
		/* '*************************************************** */
		/* 'Autor: ZaMa */
		/* 'Last Modification: 15/04/2010 */
		/* 'Gives reward exp to user */
		/* '*************************************************** */

		int GivenExp = 0;

		GivenExp = ModFacciones.RecompensaFacciones[Rango];

		Declaraciones.UserList[UserIndex].Stats.Exp = Declaraciones.UserList[UserIndex].Stats.Exp + GivenExp;

		if (Declaraciones.UserList[UserIndex].Stats.Exp > Declaraciones.MAXEXP) {
			Declaraciones.UserList[UserIndex].Stats.Exp = Declaraciones.MAXEXP;
		}

		Protocol.WriteConsoleMsg(UserIndex, "Has sido recompensado con " + GivenExp + " puntos de experiencia.",
				FontTypeNames.FONTTYPE_FIGHT);

		UsUaRiOs.CheckUserLevel(UserIndex);

	}

	static void EnlistarArmadaReal(int UserIndex) {
		/* '*************************************************** */
		/* 'Autor: Pablo (ToxicWaste) & Unknown (orginal version) */
		/* 'Last Modification: 15/04/2010 */
		/* 'Handles the entrance of users to the "Armada Real" */
		/*
		 * '15/03/2009: ZaMa - No se puede enlistar el fundador de un clan con
		 * alineación neutral.
		 */
		/*
		 * '27/11/2009: ZaMa - Ahora no se puede enlistar un miembro de un clan
		 * neutro, por ende saque la antifaccion.
		 */
		/* '15/04/2010: ZaMa - Cambio en recompensas iniciales. */
		/* '*************************************************** */

		if (Declaraciones.UserList[UserIndex].Faccion.ArmadaReal == 1) {
			Protocol.WriteChatOverHead(UserIndex, "¡¡¡Ya perteneces a las tropas reales!!! Ve a combatir criminales.",
					vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
					0x00ffffff);
			return;
		}

		if (Declaraciones.UserList[UserIndex].Faccion.FuerzasCaos == 1) {
			Protocol.WriteChatOverHead(UserIndex, "¡¡¡Maldito insolente!!! Vete de aquí seguidor de las sombras.",
					vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
					0x00ffffff);
			return;
		}

		if (ES.criminal(UserIndex)) {
			Protocol.WriteChatOverHead(UserIndex, "¡¡¡No se permiten criminales en el ejército real!!!",
					vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
					0x00ffffff);
			return;
		}

		if (Declaraciones.UserList[UserIndex].Faccion.CriminalesMatados < 30) {
			Protocol.WriteChatOverHead(UserIndex,
					"Para unirte a nuestras fuerzas debes matar al menos 30 criminales, sólo has matado "
							+ Declaraciones.UserList[UserIndex].Faccion.CriminalesMatados + ".",
					vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
					0x00ffffff);
			return;
		}

		if (Declaraciones.UserList[UserIndex].Stats.ELV < 25) {
			Protocol.WriteChatOverHead(UserIndex, "¡¡¡Para unirte a nuestras fuerzas debes ser al menos de nivel 25!!!",
					vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
					0x00ffffff);
			return;
		}

		if (Declaraciones.UserList[UserIndex].Faccion.CiudadanosMatados > 0) {
			Protocol.WriteChatOverHead(UserIndex,
					"¡Has asesinado gente inocente, no aceptamos asesinos en las tropas reales!",
					vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
					0x00ffffff);
			return;
		}

		if (Declaraciones.UserList[UserIndex].Faccion.Reenlistadas > 4) {
			Protocol.WriteChatOverHead(UserIndex, "¡Has sido expulsado de las fuerzas reales demasiadas veces!",
					vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
					0x00ffffff);
			return;
		}

		if (Declaraciones.UserList[UserIndex].Reputacion.NobleRep < 1000000) {
			Protocol.WriteChatOverHead(UserIndex,
					"Necesitas ser aún más noble para integrar el ejército real, sólo tienes "
							+ Declaraciones.UserList[UserIndex].Reputacion.NobleRep + "/1.000.000 puntos de nobleza",
					vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
					0x00ffffff);
			return;
		}

		if (Declaraciones.UserList[UserIndex].GuildIndex > 0) {
			if (modGuilds.GuildAlignment(Declaraciones.UserList[UserIndex].GuildIndex) == "Neutral") {
				Protocol.WriteChatOverHead(UserIndex,
						"¡¡¡Perteneces a un clan neutro, sal de él si quieres unirte a nuestras fuerzas!!!",
						vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
						0x00ffffff);
				return;
			}
		}

		Declaraciones.UserList[UserIndex].Faccion.ArmadaReal = 1;
		Declaraciones.UserList[UserIndex].Faccion.Reenlistadas = Declaraciones.UserList[UserIndex].Faccion.Reenlistadas
				+ 1;

		Protocol.WriteChatOverHead(UserIndex,
				"¡¡¡Bienvenido al ejército real!!! Aquí tienes tus vestimentas. Cumple bien tu labor exterminando criminales y me encargaré de recompensarte.",
				vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
				0x00ffffff);

		/*
		 * ' TODO: Dejo esta variable por ahora, pero con chequear las
		 * reenlistadas deberia ser suficiente :S
		 */
		if (Declaraciones.UserList[UserIndex].Faccion.RecibioArmaduraReal == 0) {

			GiveFactionArmours(UserIndex, false);
			GiveExpReward(UserIndex, 0);

			Declaraciones.UserList[UserIndex].Faccion.RecibioArmaduraReal = 1;
			Declaraciones.UserList[UserIndex].Faccion.NivelIngreso = Declaraciones.UserList[UserIndex].Stats.ELV;
			Declaraciones.UserList[UserIndex].Faccion.FechaIngreso = Date;
			/*
			 * 'Esto por ahora es inútil, siempre va a ser cero, pero bueno,
			 * despues va a servir.
			 */
			Declaraciones.UserList[UserIndex].Faccion.MatadosIngreso = Declaraciones.UserList[UserIndex].Faccion.CiudadanosMatados;

			Declaraciones.UserList[UserIndex].Faccion.RecibioExpInicialReal = 1;
			Declaraciones.UserList[UserIndex].Faccion.RecompensasReal = 0;
			Declaraciones.UserList[UserIndex].Faccion.NextRecompensa = 70;

		}

		/* 'Actualizamos la barca si esta navegando (NicoNZ) */
		if (Declaraciones.UserList[UserIndex].flags.Navegando) {
			UsUaRiOs.RefreshCharStatus(UserIndex);
		}

		General.LogEjercitoReal(Declaraciones.UserList[UserIndex].Name + " ingresó el " + Date + " cuando era nivel "
				+ Declaraciones.UserList[UserIndex].Stats.ELV);

	}

	static void RecompensaArmadaReal(int UserIndex) {
		/* '*************************************************** */
		/* 'Autor: Pablo (ToxicWaste) & Unknown (orginal version) */
		/* 'Last Modification: 15/04/2010 */
		/* 'Handles the way of gaining new ranks in the "Armada Real" */
		/* '15/04/2010: ZaMa - Agrego recompensas de oro y armaduras */
		/* '*************************************************** */
		int Crimis = 0;
		int Lvl = 0;
		int NextRecom = 0;
		int Nobleza = 0;

		Lvl = Declaraciones.UserList[UserIndex].Stats.ELV;
		Crimis = Declaraciones.UserList[UserIndex].Faccion.CriminalesMatados;
		NextRecom = Declaraciones.UserList[UserIndex].Faccion.NextRecompensa;
		Nobleza = Declaraciones.UserList[UserIndex].Reputacion.NobleRep;

		if (Crimis < NextRecom) {
			Protocol.WriteChatOverHead(UserIndex,
					"Mata " + NextRecom - Crimis + " criminales más para recibir la próxima recompensa.",
					vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
					0x00ffffff);
			return;
		}

		switch (NextRecom) {
		case 70:
			Declaraciones.UserList[UserIndex].Faccion.RecompensasReal = 1;
			Declaraciones.UserList[UserIndex].Faccion.NextRecompensa = 130;

			break;

		case 130:
			Declaraciones.UserList[UserIndex].Faccion.RecompensasReal = 2;
			Declaraciones.UserList[UserIndex].Faccion.NextRecompensa = 210;

			break;

		case 210:
			Declaraciones.UserList[UserIndex].Faccion.RecompensasReal = 3;
			Declaraciones.UserList[UserIndex].Faccion.NextRecompensa = 320;

			break;

		case 320:
			Declaraciones.UserList[UserIndex].Faccion.RecompensasReal = 4;
			Declaraciones.UserList[UserIndex].Faccion.NextRecompensa = 460;

			break;

		case 460:
			Declaraciones.UserList[UserIndex].Faccion.RecompensasReal = 5;
			Declaraciones.UserList[UserIndex].Faccion.NextRecompensa = 640;

			break;

		case 640:
			if (Lvl < 27) {
				Protocol.WriteChatOverHead(UserIndex,
						"Mataste suficientes criminales, pero te faltan " + 27 - Lvl
								+ " niveles para poder recibir la próxima recompensa.",
						vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
						0x00ffffff);
				return;
			}
			Declaraciones.UserList[UserIndex].Faccion.RecompensasReal = 6;
			Declaraciones.UserList[UserIndex].Faccion.NextRecompensa = 870;

			break;

		case 870:
			Declaraciones.UserList[UserIndex].Faccion.RecompensasReal = 7;
			Declaraciones.UserList[UserIndex].Faccion.NextRecompensa = 1160;

			break;

		case 1160:
			Declaraciones.UserList[UserIndex].Faccion.RecompensasReal = 8;
			Declaraciones.UserList[UserIndex].Faccion.NextRecompensa = 2000;

			break;

		case 2000:
			if (Lvl < 30) {
				Protocol.WriteChatOverHead(UserIndex,
						"Mataste suficientes criminales, pero te faltan " + 30 - Lvl
								+ " niveles para poder recibir la próxima recompensa.",
						vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
						0x00ffffff);
				return;
			}
			Declaraciones.UserList[UserIndex].Faccion.RecompensasReal = 9;
			Declaraciones.UserList[UserIndex].Faccion.NextRecompensa = 2500;

			break;

		case 2500:
			if (Nobleza < 2000000) {
				Protocol.WriteChatOverHead(UserIndex,
						"Mataste suficientes criminales, pero te faltan " + 2000000 - Nobleza
								+ " puntos de nobleza para poder recibir la próxima recompensa.",
						vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
						0x00ffffff);
				return;
			}
			Declaraciones.UserList[UserIndex].Faccion.RecompensasReal = 10;
			Declaraciones.UserList[UserIndex].Faccion.NextRecompensa = 3000;

			break;

		case 3000:
			if (Nobleza < 3000000) {
				Protocol.WriteChatOverHead(UserIndex,
						"Mataste suficientes criminales, pero te faltan " + 3000000 - Nobleza
								+ " puntos de nobleza para poder recibir la próxima recompensa.",
						vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
						0x00ffffff);
				return;
			}
			Declaraciones.UserList[UserIndex].Faccion.RecompensasReal = 11;
			Declaraciones.UserList[UserIndex].Faccion.NextRecompensa = 3500;

			break;

		case 3500:
			if (Lvl < 35) {
				Protocol.WriteChatOverHead(UserIndex,
						"Mataste suficientes criminales, pero te faltan " + 35 - Lvl
								+ " niveles para poder recibir la próxima recompensa.",
						vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
						0x00ffffff);
				return;
			}
			if (Nobleza < 4000000) {
				Protocol.WriteChatOverHead(UserIndex,
						"Mataste suficientes criminales, pero te faltan " + 4000000 - Nobleza
								+ " puntos de nobleza para poder recibir la próxima recompensa.",
						vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
						0x00ffffff);
				return;
			}
			Declaraciones.UserList[UserIndex].Faccion.RecompensasReal = 12;
			Declaraciones.UserList[UserIndex].Faccion.NextRecompensa = 4000;

			break;

		case 4000:
			if (Lvl < 36) {
				Protocol.WriteChatOverHead(UserIndex,
						"Mataste suficientes criminales, pero te faltan " + 36 - Lvl
								+ " niveles para poder recibir la próxima recompensa.",
						vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
						0x00ffffff);
				return;
			}
			if (Nobleza < 5000000) {
				Protocol.WriteChatOverHead(UserIndex,
						"Mataste suficientes criminales, pero te faltan " + 5000000 - Nobleza
								+ " puntos de nobleza para poder recibir la próxima recompensa.",
						vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
						0x00ffffff);
				return;
			}
			Declaraciones.UserList[UserIndex].Faccion.RecompensasReal = 13;
			Declaraciones.UserList[UserIndex].Faccion.NextRecompensa = 5000;

			break;

		case 5000:
			if (Lvl < 37) {
				Protocol.WriteChatOverHead(UserIndex,
						"Mataste suficientes criminales, pero te faltan " + 37 - Lvl
								+ " niveles para poder recibir la próxima recompensa.",
						vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
						0x00ffffff);
				return;
			}
			if (Nobleza < 6000000) {
				Protocol.WriteChatOverHead(UserIndex,
						"Mataste suficientes criminales, pero te faltan " + 6000000 - Nobleza
								+ " puntos de nobleza para poder recibir la próxima recompensa.",
						vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
						0x00ffffff);
				return;
			}
			Declaraciones.UserList[UserIndex].Faccion.RecompensasReal = 14;
			Declaraciones.UserList[UserIndex].Faccion.NextRecompensa = 10000;

			break;

		case 10000:
			Protocol.WriteChatOverHead(UserIndex,
					"Eres uno de mis mejores soldados. Mataste " + Crimis
							+ " criminales, sigue así. Ya no tengo más recompensa para darte que mi agradecimiento. ¡Felicidades!",
					vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
					0x00ffffff);
			return;

			break;

		default:
			return;
			break;
		}

		Protocol.WriteChatOverHead(UserIndex, "¡¡¡Aquí tienes tu recompensa " + TituloReal(UserIndex) + "!!!",
				vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
				0x00ffffff);

		/* ' Recompensas de armaduras y exp */
		GiveFactionArmours(UserIndex, false);
		GiveExpReward(UserIndex, Declaraciones.UserList[UserIndex].Faccion.RecompensasReal);

	}

	static void ExpulsarFaccionReal(int UserIndex) {
		ExpulsarFaccionReal(UserIndex, true);
	}

	static void ExpulsarFaccionReal(int UserIndex, boolean Expulsado) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/*
		 * ' 09/28/2010 C4b3z0n - Arreglado RT6 Overflow, el Desequipar() del
		 * escudo, ponía de parametro el ObjIndex del escudo en vez del EqpSlot.
		 */
		/* '*************************************************** */

		Declaraciones.UserList[UserIndex].Faccion.ArmadaReal = 0;
		/* 'Call PerderItemsFaccionarios(UserIndex) */
		if (Expulsado) {
			Protocol.WriteConsoleMsg(UserIndex, "¡¡¡Has sido expulsado del ejército real!!!",
					FontTypeNames.FONTTYPE_FIGHT);
		} else {
			Protocol.WriteConsoleMsg(UserIndex, "¡¡¡Te has retirado del ejército real!!!",
					FontTypeNames.FONTTYPE_FIGHT);
		}

		boolean bRefresh = false;

		if (Declaraciones.UserList[UserIndex].Invent.ArmourEqpObjIndex != 0) {
			/* 'Desequipamos la armadura real si está equipada */
			if (Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.ArmourEqpObjIndex].Real == 1) {
				InvUsuario.Desequipar(UserIndex, Declaraciones.UserList[UserIndex].Invent.ArmourEqpSlot, false);
				bRefresh = true;
			}
		}

		if (Declaraciones.UserList[UserIndex].Invent.EscudoEqpObjIndex != 0) {
			/* 'Desequipamos el escudo de caos si está equipado */
			if (Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.EscudoEqpObjIndex].Real == 1) {
				InvUsuario.Desequipar(UserIndex, Declaraciones.UserList[UserIndex].Invent.EscudoEqpSlot, false);
				bRefresh = true;
			}
		}

		if (bRefresh) {
			UsUaRiOs.ChangeUserChar(UserIndex, Declaraciones.UserList[UserIndex].Char.body,
					Declaraciones.UserList[UserIndex].Char.Head, Declaraciones.UserList[UserIndex].Char.heading,
					Declaraciones.UserList[UserIndex].Char.WeaponAnim,
					Declaraciones.UserList[UserIndex].Char.ShieldAnim,
					Declaraciones.UserList[UserIndex].Char.CascoAnim);
			Protocol.WriteUpdateUserStats(UserIndex);
		}

		/* 'Actualizamos la barca si esta navegando (NicoNZ) */
		if (Declaraciones.UserList[UserIndex].flags.Navegando) {
			UsUaRiOs.RefreshCharStatus(UserIndex);
		}

	}

	static void ExpulsarFaccionCaos(int UserIndex) {
		ExpulsarFaccionCaos(UserIndex, true);
	}

	static void ExpulsarFaccionCaos(int UserIndex, boolean Expulsado) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/*
		 * ' 09/28/2010 C4b3z0n - Arreglado RT6 Overflow, el Desequipar() del
		 * escudo, ponía de parametro el ObjIndex del escudo en vez del EqpSlot.
		 */
		/* '*************************************************** */

		Declaraciones.UserList[UserIndex].Faccion.FuerzasCaos = 0;
		/* 'Call PerderItemsFaccionarios(UserIndex) */
		if (Expulsado) {
			Protocol.WriteConsoleMsg(UserIndex, "¡¡¡Has sido expulsado de la Legión Oscura!!!",
					FontTypeNames.FONTTYPE_FIGHT);
		} else {
			Protocol.WriteConsoleMsg(UserIndex, "¡¡¡Te has retirado de la Legión Oscura!!!",
					FontTypeNames.FONTTYPE_FIGHT);
		}

		boolean bRefresh = false;

		if (Declaraciones.UserList[UserIndex].Invent.ArmourEqpObjIndex != 0) {
			/* 'Desequipamos la armadura de caos si está equipada */
			if (Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.ArmourEqpObjIndex].Caos == 1) {
				InvUsuario.Desequipar(UserIndex, Declaraciones.UserList[UserIndex].Invent.ArmourEqpSlot, false);
				bRefresh = true;
			}
		}

		if (Declaraciones.UserList[UserIndex].Invent.EscudoEqpObjIndex != 0) {
			/* 'Desequipamos el escudo de caos si está equipado */
			if (Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.EscudoEqpObjIndex].Caos == 1) {
				InvUsuario.Desequipar(UserIndex, Declaraciones.UserList[UserIndex].Invent.EscudoEqpSlot, false);
				bRefresh = true;
			}
		}

		if (bRefresh) {
			UsUaRiOs.ChangeUserChar(UserIndex, Declaraciones.UserList[UserIndex].Char.body,
					Declaraciones.UserList[UserIndex].Char.Head, Declaraciones.UserList[UserIndex].Char.heading,
					Declaraciones.UserList[UserIndex].Char.WeaponAnim,
					Declaraciones.UserList[UserIndex].Char.ShieldAnim,
					Declaraciones.UserList[UserIndex].Char.CascoAnim);
			Protocol.WriteUpdateUserStats(UserIndex);
		}

		/* 'Actualizamos la barca si esta navegando (NicoNZ) */
		if (Declaraciones.UserList[UserIndex].flags.Navegando) {
			UsUaRiOs.RefreshCharStatus(UserIndex);
		}

	}

	static String TituloReal(int UserIndex) {
		String retval;
		/* '*************************************************** */
		/* 'Autor: Unknown */
		/* 'Last Modification: 23/01/2007 Pablo (ToxicWaste) */
		/* 'Handles the titles of the members of the "Armada Real" */
		/* '*************************************************** */

		switch (Declaraciones.UserList[UserIndex].Faccion.RecompensasReal) {
		/* 'Rango 1: Aprendiz (30 Criminales) */
		/* 'Rango 2: Escudero (70 Criminales) */
		/* 'Rango 3: Soldado (130 Criminales) */
		/* 'Rango 4: Sargento (210 Criminales) */
		/* 'Rango 5: Caballero (320 Criminales) */
		/* 'Rango 6: Comandante (460 Criminales) */
		/* 'Rango 7: Capitán (640 Criminales + > lvl 27) */
		/* 'Rango 8: Senescal (870 Criminales) */
		/* 'Rango 9: Mariscal (1160 Criminales) */
		/* 'Rango 10: Condestable (2000 Criminales + > lvl 30) */
		/* 'Rangos de Honor de la Armada Real: (Consejo de Bander) */
		/* 'Rango 11: Ejecutor Imperial (2500 Criminales + 2.000.000 Nobleza) */
		/*
		 * 'Rango 12: Protector del Reino (3000 Criminales + 3.000.000 Nobleza)
		 */
		/*
		 * 'Rango 13: Avatar de la Justicia (3500 Criminales + 4.000.000 Nobleza
		 * + > lvl 35)
		 */
		/*
		 * 'Rango 14: Guardián del Bien (4000 Criminales + 5.000.000 Nobleza + >
		 * lvl 36)
		 */
		/*
		 * 'Rango 15: Campeón de la Luz (5000 Criminales + 6.000.000 Nobleza + >
		 * lvl 37)
		 */

		case 0:
			retval = "Aprendiz";
			break;

		case 1:
			retval = "Escudero";
			break;

		case 2:
			retval = "Soldado";
			break;

		case 3:
			retval = "Sargento";
			break;

		case 4:
			retval = "Teniente";
			break;

		case 5:
			retval = "Comandante";
			break;

		case 6:
			retval = "Capitán";
			break;

		case 7:
			retval = "Senescal";
			break;

		case 8:
			retval = "Mariscal";
			break;

		case 9:
			retval = "Condestable";
			break;

		case 10:
			retval = "Ejecutor Imperial";
			break;

		case 11:
			retval = "Protector del Reino";
			break;

		case 12:
			retval = "Avatar de la Justicia";
			break;

		case 13:
			retval = "Guardián del Bien";
			break;

		default:
			retval = "Campeón de la Luz";
			break;
		}

		return retval;
	}

	static void EnlistarCaos(int UserIndex) {
		/* '*************************************************** */
		/* 'Autor: Pablo (ToxicWaste) & Unknown (orginal version) */
		/* 'Last Modification: 27/11/2009 */
		/*
		 * '15/03/2009: ZaMa - No se puede enlistar el fundador de un clan con
		 * alineación neutral.
		 */
		/*
		 * '27/11/2009: ZaMa - Ahora no se puede enlistar un miembro de un clan
		 * neutro, por ende saque la antifaccion.
		 */
		/* 'Handles the entrance of users to the "Legión Oscura" */
		/* '*************************************************** */

		if (!ES.criminal(UserIndex)) {
			Protocol.WriteChatOverHead(UserIndex, "¡¡¡Lárgate de aquí, bufón!!!",
					vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
					0x00ffffff);
			return;
		}

		if (Declaraciones.UserList[UserIndex].Faccion.FuerzasCaos == 1) {
			Protocol.WriteChatOverHead(UserIndex, "¡¡¡Ya perteneces a la legión oscura!!!",
					vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
					0x00ffffff);
			return;
		}

		if (Declaraciones.UserList[UserIndex].Faccion.ArmadaReal == 1) {
			Protocol.WriteChatOverHead(UserIndex, "Las sombras reinarán en Argentum. ¡¡¡Fuera de aquí insecto real!!!",
					vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
					0x00ffffff);
			return;
		}

		/*
		 * '[Barrin 17-12-03] Si era miembro de la Armada Real no se puede
		 * enlistar
		 */
		/* 'Tomamos el valor de ahí: ¿Recibio la experiencia para entrar? */
		if (Declaraciones.UserList[UserIndex].Faccion.RecibioExpInicialReal == 1) {
			Protocol.WriteChatOverHead(UserIndex, "No permitiré que ningún insecto real ingrese a mis tropas.",
					vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
					0x00ffffff);
			return;
		}
		/* '[/Barrin] */

		if (!ES.criminal(UserIndex)) {
			Protocol.WriteChatOverHead(UserIndex, "¡¡Ja ja ja!! Tú no eres bienvenido aquí asqueroso ciudadano.",
					vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
					0x00ffffff);
			return;
		}

		if (Declaraciones.UserList[UserIndex].Faccion.CiudadanosMatados < 70) {
			Protocol.WriteChatOverHead(UserIndex,
					"Para unirte a nuestras fuerzas debes matar al menos 70 ciudadanos, sólo has matado "
							+ Declaraciones.UserList[UserIndex].Faccion.CiudadanosMatados + ".",
					vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
					0x00ffffff);
			return;
		}

		if (Declaraciones.UserList[UserIndex].Stats.ELV < 25) {
			Protocol.WriteChatOverHead(UserIndex, "¡¡¡Para unirte a nuestras fuerzas debes ser al menos nivel 25!!!",
					vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
					0x00ffffff);
			return;
		}

		if (Declaraciones.UserList[UserIndex].GuildIndex > 0) {
			if (modGuilds.GuildAlignment(Declaraciones.UserList[UserIndex].GuildIndex) == "Neutral") {
				Protocol.WriteChatOverHead(UserIndex,
						"¡¡¡Perteneces a un clan neutro, sal de él si quieres unirte a nuestras fuerzas!!!",
						vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
						0x00ffffff);
				return;
			}
		}

		if (Declaraciones.UserList[UserIndex].Faccion.Reenlistadas > 4) {
			if (Declaraciones.UserList[UserIndex].Faccion.Reenlistadas == 200) {
				Protocol.WriteChatOverHead(UserIndex,
						"Has sido expulsado de las fuerzas oscuras y durante tu rebeldía has atacado a mi ejército. ¡Vete de aquí!",
						vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
						0x00ffffff);
			} else {
				Protocol.WriteChatOverHead(UserIndex, "¡Has sido expulsado de las fuerzas oscuras demasiadas veces!",
						vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
						0x00ffffff);
			}
			return;
		}

		Declaraciones.UserList[UserIndex].Faccion.Reenlistadas = Declaraciones.UserList[UserIndex].Faccion.Reenlistadas
				+ 1;
		Declaraciones.UserList[UserIndex].Faccion.FuerzasCaos = 1;

		Protocol.WriteChatOverHead(UserIndex,
				"¡¡¡Bienvenido al lado oscuro!!! Aquí tienes tus armaduras. Derrama sangre ciudadana y real, y serás recompensado, lo prometo.",
				vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
				0x00ffffff);

		if (Declaraciones.UserList[UserIndex].Faccion.RecibioArmaduraCaos == 0) {

			GiveFactionArmours(UserIndex, true);
			GiveExpReward(UserIndex, 0);

			Declaraciones.UserList[UserIndex].Faccion.RecibioArmaduraCaos = 1;
			Declaraciones.UserList[UserIndex].Faccion.NivelIngreso = Declaraciones.UserList[UserIndex].Stats.ELV;
			Declaraciones.UserList[UserIndex].Faccion.FechaIngreso = Date;

			Declaraciones.UserList[UserIndex].Faccion.RecibioExpInicialCaos = 1;
			Declaraciones.UserList[UserIndex].Faccion.RecompensasCaos = 0;
			Declaraciones.UserList[UserIndex].Faccion.NextRecompensa = 160;
		}

		/* 'Actualizamos la barca si esta navegando (NicoNZ) */
		if (Declaraciones.UserList[UserIndex].flags.Navegando) {
			UsUaRiOs.RefreshCharStatus(UserIndex);
		}

		General.LogEjercitoCaos(Declaraciones.UserList[UserIndex].Name + " ingresó el " + Date + " cuando era nivel "
				+ Declaraciones.UserList[UserIndex].Stats.ELV);

	}

	static void RecompensaCaos(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Pablo (ToxicWaste) & Unknown (orginal version) */
		/* 'Last Modification: 15/04/2010 */
		/* 'Handles the way of gaining new ranks in the "Legión Oscura" */
		/* '15/04/2010: ZaMa - Agrego recompensas de oro y armaduras */
		/* '*************************************************** */
		int Ciudas = 0;
		int Lvl = 0;
		int NextRecom = 0;

		Lvl = Declaraciones.UserList[UserIndex].Stats.ELV;
		Ciudas = Declaraciones.UserList[UserIndex].Faccion.CiudadanosMatados;
		NextRecom = Declaraciones.UserList[UserIndex].Faccion.NextRecompensa;

		if (Ciudas < NextRecom) {
			Protocol.WriteChatOverHead(UserIndex,
					"Mata " + NextRecom - Ciudas + " cuidadanos más para recibir la próxima recompensa.",
					vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
					0x00ffffff);
			return;
		}

		switch (NextRecom) {
		case 160:
			Declaraciones.UserList[UserIndex].Faccion.RecompensasCaos = 1;
			Declaraciones.UserList[UserIndex].Faccion.NextRecompensa = 300;

			break;

		case 300:
			Declaraciones.UserList[UserIndex].Faccion.RecompensasCaos = 2;
			Declaraciones.UserList[UserIndex].Faccion.NextRecompensa = 490;

			break;

		case 490:
			Declaraciones.UserList[UserIndex].Faccion.RecompensasCaos = 3;
			Declaraciones.UserList[UserIndex].Faccion.NextRecompensa = 740;

			break;

		case 740:
			Declaraciones.UserList[UserIndex].Faccion.RecompensasCaos = 4;
			Declaraciones.UserList[UserIndex].Faccion.NextRecompensa = 1100;

			break;

		case 1100:
			Declaraciones.UserList[UserIndex].Faccion.RecompensasCaos = 5;
			Declaraciones.UserList[UserIndex].Faccion.NextRecompensa = 1500;

			break;

		case 1500:
			if (Lvl < 27) {
				Protocol.WriteChatOverHead(UserIndex,
						"Mataste suficientes ciudadanos, pero te faltan " + 27 - Lvl
								+ " niveles para poder recibir la próxima recompensa.",
						vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
						0x00ffffff);
				return;
			}
			Declaraciones.UserList[UserIndex].Faccion.RecompensasCaos = 6;
			Declaraciones.UserList[UserIndex].Faccion.NextRecompensa = 2010;

			break;

		case 2010:
			Declaraciones.UserList[UserIndex].Faccion.RecompensasCaos = 7;
			Declaraciones.UserList[UserIndex].Faccion.NextRecompensa = 2700;

			break;

		case 2700:
			Declaraciones.UserList[UserIndex].Faccion.RecompensasCaos = 8;
			Declaraciones.UserList[UserIndex].Faccion.NextRecompensa = 4600;

			break;

		case 4600:
			if (Lvl < 30) {
				Protocol.WriteChatOverHead(UserIndex,
						"Mataste suficientes ciudadanos, pero te faltan " + 30 - Lvl
								+ " niveles para poder recibir la próxima recompensa.",
						vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
						0x00ffffff);
				return;
			}
			Declaraciones.UserList[UserIndex].Faccion.RecompensasCaos = 9;
			Declaraciones.UserList[UserIndex].Faccion.NextRecompensa = 5800;

			break;

		case 5800:
			if (Lvl < 31) {
				Protocol.WriteChatOverHead(UserIndex,
						"Mataste suficientes ciudadanos, pero te faltan " + 31 - Lvl
								+ " niveles para poder recibir la próxima recompensa.",
						vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
						0x00ffffff);
				return;
			}
			Declaraciones.UserList[UserIndex].Faccion.RecompensasCaos = 10;
			Declaraciones.UserList[UserIndex].Faccion.NextRecompensa = 6990;

			break;

		case 6990:
			if (Lvl < 33) {
				Protocol.WriteChatOverHead(UserIndex,
						"Mataste suficientes ciudadanos, pero te faltan " + 33 - Lvl
								+ " niveles para poder recibir la próxima recompensa.",
						vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
						0x00ffffff);
				return;
			}
			Declaraciones.UserList[UserIndex].Faccion.RecompensasCaos = 11;
			Declaraciones.UserList[UserIndex].Faccion.NextRecompensa = 8100;

			break;

		case 8100:
			if (Lvl < 35) {
				Protocol.WriteChatOverHead(UserIndex,
						"Mataste suficientes ciudadanos, pero te faltan " + 35 - Lvl
								+ " niveles para poder recibir la próxima recompensa.",
						vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
						0x00ffffff);
				return;
			}
			Declaraciones.UserList[UserIndex].Faccion.RecompensasCaos = 12;
			Declaraciones.UserList[UserIndex].Faccion.NextRecompensa = 9300;

			break;

		case 9300:
			if (Lvl < 36) {
				Protocol.WriteChatOverHead(UserIndex,
						"Mataste suficientes ciudadanos, pero te faltan " + 36 - Lvl
								+ " niveles para poder recibir la próxima recompensa.",
						vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
						0x00ffffff);
				return;
			}
			Declaraciones.UserList[UserIndex].Faccion.RecompensasCaos = 13;
			Declaraciones.UserList[UserIndex].Faccion.NextRecompensa = 11500;

			break;

		case 11500:
			if (Lvl < 37) {
				Protocol.WriteChatOverHead(UserIndex,
						"Mataste suficientes ciudadanos, pero te faltan " + 37 - Lvl
								+ " niveles para poder recibir la próxima recompensa.",
						vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
						0x00ffffff);
				return;
			}
			Declaraciones.UserList[UserIndex].Faccion.RecompensasCaos = 14;
			Declaraciones.UserList[UserIndex].Faccion.NextRecompensa = 23000;

			break;

		case 23000:
			Protocol.WriteChatOverHead(UserIndex,
					"Eres uno de mis mejores soldados. Mataste " + Ciudas
							+ " ciudadanos . Tu única recompensa será la sangre derramada. ¡¡Continúa así!!",
					vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
					0x00ffffff);
			return;

			break;

		default:
			return;

			break;
		}

		Protocol.WriteChatOverHead(UserIndex,
				"¡¡¡Bien hecho " + TituloCaos(UserIndex) + ", aquí tienes tu recompensa!!!",
				vb6.str(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex),
				0x00ffffff);

		/* ' Recompensas de armaduras y exp */
		GiveFactionArmours(UserIndex, true);
		GiveExpReward(UserIndex, Declaraciones.UserList[UserIndex].Faccion.RecompensasCaos);

	}

	static String TituloCaos(int UserIndex) {
		String retval;
		/* '*************************************************** */
		/* 'Autor: Unknown (orginal version) */
		/* 'Last Modification: 23/01/2007 Pablo (ToxicWaste) */
		/* 'Handles the titles of the members of the "Legión Oscura" */
		/* '*************************************************** */
		/* 'Rango 1: Acólito (70) */
		/* 'Rango 2: Alma Corrupta (160) */
		/* 'Rango 3: Paria (300) */
		/* 'Rango 4: Condenado (490) */
		/* 'Rango 5: Esbirro (740) */
		/* 'Rango 6: Sanguinario (1100) */
		/* 'Rango 7: Corruptor (1500 + lvl 27) */
		/* 'Rango 8: Heraldo Impio (2010) */
		/* 'Rango 9: Caballero de la Oscuridad (2700) */
		/* 'Rango 10: Senor del Miedo (4600 + lvl 30) */
		/* 'Rango 11: Ejecutor Infernal (5800 + lvl 31) */
		/* 'Rango 12: Protector del Averno (6990 + lvl 33) */
		/* 'Rango 13: Avatar de la Destrucción (8100 + lvl 35) */
		/* 'Rango 14: Guardián del Mal (9300 + lvl 36) */
		/* 'Rango 15: Campeón de la Oscuridad (11500 + lvl 37) */

		switch (Declaraciones.UserList[UserIndex].Faccion.RecompensasCaos) {
		case 0:
			retval = "Acólito";
			break;

		case 1:
			retval = "Alma Corrupta";
			break;

		case 2:
			retval = "Paria";
			break;

		case 3:
			retval = "Condenado";
			break;

		case 4:
			retval = "Esbirro";
			break;

		case 5:
			retval = "Sanguinario";
			break;

		case 6:
			retval = "Corruptor";
			break;

		case 7:
			retval = "Heraldo Impío";
			break;

		case 8:
			retval = "Caballero de la Oscuridad";
			break;

		case 9:
			retval = "Senor del Miedo";
			break;

		case 10:
			retval = "Ejecutor Infernal";
			break;

		case 11:
			retval = "Protector del Averno";
			break;

		case 12:
			retval = "Avatar de la Destrucción";
			break;

		case 13:
			retval = "Guardián del Mal";
			break;

		default:
			retval = "Campeón de la Oscuridad";
			break;
		}

		return retval;
	}

}