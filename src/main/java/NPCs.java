
/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"NPCs"')] */
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

/* '?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿ */
/* '?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿ */
/* '?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿ */
/* '                        Modulo NPC */
/* '?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿ */
/* '?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿ */
/* '?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿ */
/* 'Contiene todas las rutinas necesarias para cotrolar los */
/* 'NPCs meno la rutina de AI que se encuentra en el modulo */
/* 'AI_NPCs para su mejor comprension. */
/* '?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿ */
/* '?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿ */
/* '?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿ */

import enums.*;

public class NPCs {

	static void QuitarMascota(int UserIndex, int NpcIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int i = 0;

		for (i = (1); i <= (Declaraciones.MAXMASCOTAS); i++) {
			if (Declaraciones.UserList[UserIndex].MascotasIndex[i] == NpcIndex) {
				Declaraciones.UserList[UserIndex].MascotasIndex[i] = 0;
				Declaraciones.UserList[UserIndex].MascotasType[i] = 0;

				Declaraciones.UserList[UserIndex].NroMascotas = Declaraciones.UserList[UserIndex].NroMascotas - 1;
				break; /* FIXME: EXIT FOR */
			}
		}
	}

	static void QuitarMascotaNpc(int Maestro) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		Declaraciones.Npclist[Maestro].Mascotas = Declaraciones.Npclist[Maestro].Mascotas - 1;
	}

	static void MuereNpc(int NpcIndex, int UserIndex) {
		/* '******************************************************** */
		/* 'Author: Unknown */
		/* 'Llamado cuando la vida de un NPC llega a cero. */
		/* 'Last Modify Date: 13/07/2010 */
		/* '22/06/06: (Nacho) Chequeamos si es pretoriano */
		/*
		 * '24/01/2007: Pablo (ToxicWaste): Agrego para actualización de tag si
		 * cambia de status.
		 */
		/*
		 * '22/05/2010: ZaMa - Los caos ya no suben nobleza ni plebe al atacar
		 * npcs.
		 */
		/* '23/05/2010: ZaMa - El usuario pierde la pertenencia del npc. */
		/*
		 * '13/07/2010: ZaMa - Optimizaciones de logica en la seleccion de
		 * pretoriano, y el posible cambio de alencion del usuario.
		 */
		/* '******************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		Declaraciones.npc MiNPC;
		MiNPC = Declaraciones.Npclist[NpcIndex];
		boolean EraCriminal = false;
		int PretorianoIndex = 0;

		/* ' Es pretoriano? */
		if (MiNPC.NPCtype == eNPCType.Pretoriano) {
			Declaraciones.ClanPretoriano[MiNPC.ClanIndex].MuerePretoriano(NpcIndex);
		}

		/* 'Quitamos el npc */
		QuitarNPC(NpcIndex);

		/* ' Lo mato un usuario? */
		if (UserIndex > 0) {

			if (MiNPC.flags.Snd3 > 0) {
				modSendData.SendData(SendTarget.ToPCArea, UserIndex,
						Protocol.PrepareMessagePlayWave(MiNPC.flags.Snd3, MiNPC.Pos.X, MiNPC.Pos.Y));
			}
			Declaraciones.UserList[UserIndex].flags.TargetNPC = 0;
			Declaraciones.UserList[UserIndex].flags.TargetNpcTipo = eNPCType.Comun;

			/* 'El user que lo mato tiene mascotas? */
			if (Declaraciones.UserList[UserIndex].NroMascotas > 0) {
				int T = 0;
				for (T = (1); T <= (Declaraciones.MAXMASCOTAS); T++) {
					if (Declaraciones.UserList[UserIndex].MascotasIndex[T] > 0) {
						if (Declaraciones.Npclist[Declaraciones.UserList[UserIndex].MascotasIndex[T]].TargetNPC == NpcIndex) {
							FollowAmo(Declaraciones.UserList[UserIndex].MascotasIndex[T]);
						}
					}
				}
			}

			/* '[KEVIN] */
			if (MiNPC.flags.ExpCount > 0) {
				if (Declaraciones.UserList[UserIndex].PartyIndex > 0) {
					mdParty.ObtenerExito(UserIndex, MiNPC.flags.ExpCount, MiNPC.Pos.Map, MiNPC.Pos.X, MiNPC.Pos.Y);
				} else {
					Declaraciones.UserList[UserIndex].Stats.Exp = Declaraciones.UserList[UserIndex].Stats.Exp
							+ MiNPC.flags.ExpCount;
					if (Declaraciones.UserList[UserIndex].Stats.Exp > Declaraciones.MAXEXP) {
						Declaraciones.UserList[UserIndex].Stats.Exp = Declaraciones.MAXEXP;
					}
					Protocol.WriteConsoleMsg(UserIndex,
							"Has ganado " + MiNPC.flags.ExpCount + " puntos de experiencia.",
							FontTypeNames.FONTTYPE_FIGHT);
				}
				MiNPC.flags.ExpCount = 0;
			}

			/* '[/KEVIN] */
			Protocol.WriteConsoleMsg(UserIndex, "¡Has matado a la criatura!", FontTypeNames.FONTTYPE_FIGHT);
			if (Declaraciones.UserList[UserIndex].Stats.NPCsMuertos < 32000) {
				Declaraciones.UserList[UserIndex].Stats.NPCsMuertos = Declaraciones.UserList[UserIndex].Stats.NPCsMuertos
						+ 1;
			}

			EraCriminal = ES.criminal(UserIndex);

			if (MiNPC.Stats.Alineacion == 0) {

				if (MiNPC.Numero == Declaraciones.Guardias) {
					Declaraciones.UserList[UserIndex].Reputacion.NobleRep = 0;
					Declaraciones.UserList[UserIndex].Reputacion.PlebeRep = 0;
					Declaraciones.UserList[UserIndex].Reputacion.AsesinoRep = Declaraciones.UserList[UserIndex].Reputacion.AsesinoRep
							+ 500;
					if (Declaraciones.UserList[UserIndex].Reputacion.AsesinoRep > Declaraciones.MAXREP) {
						Declaraciones.UserList[UserIndex].Reputacion.AsesinoRep = Declaraciones.MAXREP;
					}
				}

				if (MiNPC.MaestroUser == 0) {
					Declaraciones.UserList[UserIndex].Reputacion.AsesinoRep = Declaraciones.UserList[UserIndex].Reputacion.AsesinoRep
							+ Declaraciones.vlASESINO;
					if (Declaraciones.UserList[UserIndex].Reputacion.AsesinoRep > Declaraciones.MAXREP) {
						Declaraciones.UserList[UserIndex].Reputacion.AsesinoRep = Declaraciones.MAXREP;
					}
				}

			} else if (!Extra.esCaos(UserIndex)) {
				if (MiNPC.Stats.Alineacion == 1) {
					Declaraciones.UserList[UserIndex].Reputacion.PlebeRep = Declaraciones.UserList[UserIndex].Reputacion.PlebeRep
							+ Declaraciones.vlCAZADOR;
					if (Declaraciones.UserList[UserIndex].Reputacion.PlebeRep > Declaraciones.MAXREP) {
						Declaraciones.UserList[UserIndex].Reputacion.PlebeRep = Declaraciones.MAXREP;
					}

				} else if (MiNPC.Stats.Alineacion == 2) {
					Declaraciones.UserList[UserIndex].Reputacion.NobleRep = Declaraciones.UserList[UserIndex].Reputacion.NobleRep
							+ Declaraciones.vlASESINO / (double) 2;
					if (Declaraciones.UserList[UserIndex].Reputacion.NobleRep > Declaraciones.MAXREP) {
						Declaraciones.UserList[UserIndex].Reputacion.NobleRep = Declaraciones.MAXREP;
					}

				} else if (MiNPC.Stats.Alineacion == 4) {
					Declaraciones.UserList[UserIndex].Reputacion.PlebeRep = Declaraciones.UserList[UserIndex].Reputacion.PlebeRep
							+ Declaraciones.vlCAZADOR;
					if (Declaraciones.UserList[UserIndex].Reputacion.PlebeRep > Declaraciones.MAXREP) {
						Declaraciones.UserList[UserIndex].Reputacion.PlebeRep = Declaraciones.MAXREP;
					}

				}
			}

			boolean EsCriminal = false;
			EsCriminal = ES.criminal(UserIndex);

			/* ' Cambio de alienacion? */
			if (EraCriminal != EsCriminal) {

				/* ' Se volvio pk? */
				if (EsCriminal) {
					if (Extra.esArmada(UserIndex)) {
						ModFacciones.ExpulsarFaccionReal(UserIndex);
					}

					/* ' Se volvio ciuda */
				} else {
					if (Extra.esCaos(UserIndex)) {
						ModFacciones.ExpulsarFaccionCaos(UserIndex);
					}
				}

				UsUaRiOs.RefreshCharStatus(UserIndex);
			}

			UsUaRiOs.CheckUserLevel(UserIndex);

			if (NpcIndex == Declaraciones.UserList[UserIndex].flags.ParalizedByNpcIndex) {
				General.RemoveParalisis(UserIndex);
			}

			/* ' Userindex > 0 */
		}

		if (MiNPC.MaestroUser == 0) {
			/* 'Tiramos el inventario */
			InvNpc.NPC_TIRAR_ITEMS(MiNPC, MiNPC.NPCtype == eNPCType.Pretoriano);
			/* 'ReSpawn o no */
			ReSpawnNpc(MiNPC);
		}

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en MuereNpc - Error: " + Err.Number + " - Desc: " + Err.description);
	}

	static void ResetNpcFlags(int NpcIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* 'Clear the npc's flags */

		Declaraciones.Npclist[NpcIndex].flags.AfectaParalisis = 0;
		Declaraciones.Npclist[NpcIndex].flags.AguaValida = 0;
		Declaraciones.Npclist[NpcIndex].flags.AttackedBy = "";
		Declaraciones.Npclist[NpcIndex].flags.AttackedFirstBy = "";
		Declaraciones.Npclist[NpcIndex].flags.BackUp = 0;
		Declaraciones.Npclist[NpcIndex].flags.Bendicion = 0;
		Declaraciones.Npclist[NpcIndex].flags.Domable = 0;
		Declaraciones.Npclist[NpcIndex].flags.Envenenado = 0;
		Declaraciones.Npclist[NpcIndex].flags.Faccion = 0;
		Declaraciones.Npclist[NpcIndex].flags.Follow = false;
		Declaraciones.Npclist[NpcIndex].flags.AtacaDoble = 0;
		Declaraciones.Npclist[NpcIndex].flags.LanzaSpells = 0;
		Declaraciones.Npclist[NpcIndex].flags.invisible = 0;
		Declaraciones.Npclist[NpcIndex].flags.Maldicion = 0;
		Declaraciones.Npclist[NpcIndex].flags.OldHostil = 0;
		Declaraciones.Npclist[NpcIndex].flags.OldMovement = 0;
		Declaraciones.Npclist[NpcIndex].flags.Paralizado = 0;
		Declaraciones.Npclist[NpcIndex].flags.Inmovilizado = 0;
		Declaraciones.Npclist[NpcIndex].flags.Respawn = 0;
		Declaraciones.Npclist[NpcIndex].flags.RespawnOrigPos = 0;
		Declaraciones.Npclist[NpcIndex].flags.Snd1 = 0;
		Declaraciones.Npclist[NpcIndex].flags.Snd2 = 0;
		Declaraciones.Npclist[NpcIndex].flags.Snd3 = 0;
		Declaraciones.Npclist[NpcIndex].flags.TierraInvalida = 0;
	}

	static void ResetNpcCounters(int NpcIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		Declaraciones.Npclist[NpcIndex].Contadores.Paralisis = 0;
		Declaraciones.Npclist[NpcIndex].Contadores.TiempoExistencia = 0;
	}

	static void ResetNpcCharInfo(int NpcIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		Declaraciones.Npclist[NpcIndex].Char.body = 0;
		Declaraciones.Npclist[NpcIndex].Char.CascoAnim = 0;
		Declaraciones.Npclist[NpcIndex].Char.CharIndex = 0;
		Declaraciones.Npclist[NpcIndex].Char.FX = 0;
		Declaraciones.Npclist[NpcIndex].Char.Head = 0;
		Declaraciones.Npclist[NpcIndex].Char.heading = 0;
		Declaraciones.Npclist[NpcIndex].Char.loops = 0;
		Declaraciones.Npclist[NpcIndex].Char.ShieldAnim = 0;
		Declaraciones.Npclist[NpcIndex].Char.WeaponAnim = 0;
	}

	static void ResetNpcCriatures(int NpcIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int j = 0;

		for (j = (1); j <= (Declaraciones.Npclist[NpcIndex].NroCriaturas); j++) {
			Declaraciones.Npclist[NpcIndex].Criaturas[j].NpcIndex = 0;
			Declaraciones.Npclist[NpcIndex].Criaturas[j].NpcName = "";
		}

		Declaraciones.Npclist[NpcIndex].NroCriaturas = 0;
	}

	static void ResetExpresiones(int NpcIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int j = 0;

		for (j = (1); j <= (Declaraciones.Npclist[NpcIndex].NroExpresiones); j++) {
			Declaraciones.Npclist[NpcIndex].Expresiones[j] = "";
		}

		Declaraciones.Npclist[NpcIndex].NroExpresiones = 0;
	}

	static void ResetNpcMainInfo(int NpcIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* '22/05/2010: ZaMa - Ahora se resetea el dueño del npc también. */
		/* '*************************************************** */

		Declaraciones.Npclist[NpcIndex].Attackable = 0;
		Declaraciones.Npclist[NpcIndex].CanAttack = 0;
		Declaraciones.Npclist[NpcIndex].Comercia = 0;
		Declaraciones.Npclist[NpcIndex].GiveEXP = 0;
		Declaraciones.Npclist[NpcIndex].GiveGLD = 0;
		Declaraciones.Npclist[NpcIndex].Hostile = 0;
		Declaraciones.Npclist[NpcIndex].InvReSpawn = 0;

		if (Declaraciones.Npclist[NpcIndex].MaestroUser > 0) {
			QuitarMascota(Declaraciones.Npclist[NpcIndex].MaestroUser, NpcIndex);
		}
		if (Declaraciones.Npclist[NpcIndex].MaestroNpc > 0) {
			QuitarMascotaNpc(Declaraciones.Npclist[NpcIndex].MaestroNpc);
		}
		if (Declaraciones.Npclist[NpcIndex].Owner > 0) {
			UsUaRiOs.PerdioNpc(Declaraciones.Npclist[NpcIndex].Owner);
		}

		Declaraciones.Npclist[NpcIndex].MaestroUser = 0;
		Declaraciones.Npclist[NpcIndex].MaestroNpc = 0;
		Declaraciones.Npclist[NpcIndex].Owner = 0;

		Declaraciones.Npclist[NpcIndex].Mascotas = 0;
		Declaraciones.Npclist[NpcIndex].Movement = 0;
		Declaraciones.Npclist[NpcIndex].Name = "";
		Declaraciones.Npclist[NpcIndex].NPCtype = 0;
		Declaraciones.Npclist[NpcIndex].Numero = 0;
		Declaraciones.Npclist[NpcIndex].Orig.Map = 0;
		Declaraciones.Npclist[NpcIndex].Orig.X = 0;
		Declaraciones.Npclist[NpcIndex].Orig.Y = 0;
		Declaraciones.Npclist[NpcIndex].PoderAtaque = 0;
		Declaraciones.Npclist[NpcIndex].PoderEvasion = 0;
		Declaraciones.Npclist[NpcIndex].Pos.Map = 0;
		Declaraciones.Npclist[NpcIndex].Pos.X = 0;
		Declaraciones.Npclist[NpcIndex].Pos.Y = 0;
		Declaraciones.Npclist[NpcIndex].SkillDomar = 0;
		Declaraciones.Npclist[NpcIndex].Target = 0;
		Declaraciones.Npclist[NpcIndex].TargetNPC = 0;
		Declaraciones.Npclist[NpcIndex].TipoItems = 0;
		Declaraciones.Npclist[NpcIndex].Veneno = 0;
		Declaraciones.Npclist[NpcIndex].desc = "";

		Declaraciones.Npclist[NpcIndex].ClanIndex = 0;

		int j = 0;
		for (j = (1); j <= (Declaraciones.Npclist[NpcIndex].NroSpells); j++) {
			Declaraciones.Npclist[NpcIndex].Spells[j] = 0;
		}

		ResetNpcCharInfo(NpcIndex);
		ResetNpcCriatures(NpcIndex);
		ResetExpresiones(NpcIndex);
	}

	static void QuitarNPC(int NpcIndex) {
		/* '*************************************************** */
		/* 'Autor: Unknown (orginal version) */
		/* 'Last Modification: 16/11/2009 */
		/* '16/11/2009: ZaMa - Now npcs lose their owner */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		Declaraciones.Npclist[NpcIndex].flags.NPCActive = false;

		if (Extra.InMapBounds(Declaraciones.Npclist[NpcIndex].Pos.Map, Declaraciones.Npclist[NpcIndex].Pos.X,
				Declaraciones.Npclist[NpcIndex].Pos.Y)) {
			EraseNPCChar(NpcIndex);
		}

		/* 'Nos aseguramos de que el inventario sea removido... */
		/* 'asi los lobos no volveran a tirar armaduras ;)) */
		InvNpc.ResetNpcInv(NpcIndex);
		ResetNpcFlags(NpcIndex);
		ResetNpcCounters(NpcIndex);

		ResetNpcMainInfo(NpcIndex);

		if (NpcIndex == Declaraciones.LastNPC) {
			while (!(Declaraciones.Npclist[Declaraciones.LastNPC].flags.NPCActive)) {
				Declaraciones.LastNPC = Declaraciones.LastNPC - 1;
				if (Declaraciones.LastNPC < 1) {
					break; /* FIXME: EXIT DO */
				}
			}
		}

		if (Declaraciones.NumNPCs != 0) {
			Declaraciones.NumNPCs = Declaraciones.NumNPCs - 1;
		}
		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en QuitarNPC");
	}

	static void QuitarPet(int UserIndex, int NpcIndex) {
		/* '*************************************************** */
		/* 'Autor: ZaMa */
		/* 'Last Modification: 18/11/2009 */
		/* 'Kills a pet */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		int i = 0;
		int PetIndex = 0;

		/* ' Busco el indice de la mascota */
		for (i = (1); i <= (Declaraciones.MAXMASCOTAS); i++) {
			if (Declaraciones.UserList[UserIndex].MascotasIndex[i] == NpcIndex) {
				PetIndex = i;
			}
		}

		/* ' Poco probable que pase, pero por las dudas.. */
		if (PetIndex == 0) {
			return;
		}

		/* ' Limpio el slot de la mascota */
		Declaraciones.UserList[UserIndex].NroMascotas = Declaraciones.UserList[UserIndex].NroMascotas - 1;
		Declaraciones.UserList[UserIndex].MascotasIndex[PetIndex] = 0;
		Declaraciones.UserList[UserIndex].MascotasType[PetIndex] = 0;

		/* ' Elimino la mascota */
		QuitarNPC(NpcIndex);

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en QuitarPet. Error: " + Err.Number + " Desc: " + Err.description + " NpcIndex: "
				+ NpcIndex + " UserIndex: " + UserIndex + " PetIndex: " + PetIndex);
	}

	static boolean TestSpawnTrigger(Declaraciones.WorldPos Pos) {
		return TestSpawnTrigger(Pos, false);
	}

	static boolean TestSpawnTrigger(Declaraciones.WorldPos Pos, boolean PuedeAgua) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		if (Extra.LegalPos(Pos.Map, Pos.X, Pos.Y, PuedeAgua)) {
			retval = Declaraciones.MapData[Pos.Map][Pos.X][Pos.Y].trigger != 3
					&& Declaraciones.MapData[Pos.Map][Pos.X][Pos.Y].trigger != 2
					&& Declaraciones.MapData[Pos.Map][Pos.X][Pos.Y].trigger != 1;
		}

		return retval;
	}

	static int CrearNPC(int NroNPC, int mapa, Declaraciones.WorldPos OrigPos) {
 return  CrearNPC(NroNPC, mapa, OrigPos, int());
 }

	static int CrearNPC(int NroNPC, int mapa, Declaraciones.WorldPos OrigPos, int CustomHead) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* 'Crea un NPC del tipo NRONPC */

		Declaraciones.WorldPos Pos;
		Declaraciones.WorldPos newpos;
		Declaraciones.WorldPos altpos;
		int nIndex = 0;
		boolean PosicionValida = false;
		int Iteraciones = 0;
		boolean PuedeAgua = false;
		boolean PuedeTierra = false;

		int Map = 0;
		int X = 0;
		int Y = 0;

		/* 'Conseguimos un indice */
		nIndex = OpenNPC(NroNPC);

		if (nIndex > Declaraciones.MAXNPCS) {
			return retval;
		}

		/* ' Cabeza customizada */
		if (CustomHead != 0) {
			Declaraciones.Npclist[nIndex].Char.Head = CustomHead;
		}

		PuedeAgua = Declaraciones.Npclist[nIndex].flags.AguaValida;
		PuedeTierra = vb6.IIf(Declaraciones.Npclist[nIndex].flags.TierraInvalida == 1, false, true);

		/* 'Necesita ser respawned en un lugar especifico */
		if (Extra.InMapBounds(OrigPos.Map, OrigPos.X, OrigPos.Y)) {

			Map = OrigPos.Map;
			X = OrigPos.X;
			Y = OrigPos.Y;
			Declaraciones.Npclist[nIndex].Orig = OrigPos;
			Declaraciones.Npclist[nIndex].Pos = OrigPos;

		} else {

			/* 'mapa */
			Pos.Map = mapa;
			altpos.Map = mapa;

			while (!PosicionValida) {
				/* 'Obtenemos posicion al azar en x */
				Pos.X = Matematicas.RandomNumber(Declaraciones.MinXBorder, Declaraciones.MaxXBorder);
				/* 'Obtenemos posicion al azar en y */
				Pos.Y = Matematicas.RandomNumber(Declaraciones.MinYBorder, Declaraciones.MaxYBorder);

				/* 'Nos devuelve la posicion valida mas cercana */
				Extra.ClosestLegalPos(Pos, newpos, PuedeAgua, PuedeTierra);
				if (newpos.X != 0 && newpos.Y != 0) {
					altpos.X = newpos.X;
					/*
					 * 'posicion alternativa (para evitar el anti respawn, pero
					 * intentando qeu si tenía que ser en el agua, sea en el
					 * agua.)
					 */
					altpos.Y = newpos.Y;
				} else {
					Extra.ClosestLegalPos(Pos, newpos, PuedeAgua);
					if (newpos.X != 0 && newpos.Y != 0) {
						altpos.X = newpos.X;
						/*
						 * 'posicion alternativa (para evitar el anti respawn)
						 */
						altpos.Y = newpos.Y;
					}
				}
				/*
				 * 'Si X e Y son iguales a 0 significa que no se encontro
				 * posicion valida
				 */
				if (Extra.LegalPosNPC(newpos.Map, newpos.X, newpos.Y, PuedeAgua) && !TCP.HayPCarea(newpos)
						&& TestSpawnTrigger(newpos, PuedeAgua)) {
					/* 'Asignamos las nuevas coordenas solo si son validas */
					Declaraciones.Npclist[nIndex].Pos.Map = newpos.Map;
					Declaraciones.Npclist[nIndex].Pos.X = newpos.X;
					Declaraciones.Npclist[nIndex].Pos.Y = newpos.Y;
					PosicionValida = true;
				} else {
					newpos.X = 0;
					newpos.Y = 0;

				}

				/* 'for debug */
				Iteraciones = Iteraciones + 1;
				if (Iteraciones > Declaraciones.MAXSPAWNATTEMPS) {
					if (altpos.X != 0 && altpos.Y != 0) {
						Map = altpos.Map;
						X = altpos.X;
						Y = altpos.Y;
						Declaraciones.Npclist[nIndex].Pos.Map = Map;
						Declaraciones.Npclist[nIndex].Pos.X = X;
						Declaraciones.Npclist[nIndex].Pos.Y = Y;
						MakeNPCChar(true, Map, nIndex, Map, X, Y);
						return retval;
					} else {
						altpos.X = 50;
						altpos.Y = 50;
						Extra.ClosestLegalPos(altpos, newpos);
						if (newpos.X != 0 && newpos.Y != 0) {
							Declaraciones.Npclist[nIndex].Pos.Map = newpos.Map;
							Declaraciones.Npclist[nIndex].Pos.X = newpos.X;
							Declaraciones.Npclist[nIndex].Pos.Y = newpos.Y;
							MakeNPCChar(true, newpos.Map, nIndex, newpos.Map, newpos.X, newpos.Y);
							return retval;
						} else {
							QuitarNPC(nIndex);
							General.LogError(Declaraciones.MAXSPAWNATTEMPS + " iteraciones en CrearNpc Mapa:" + mapa
									+ " NroNpc:" + NroNPC);
							return retval;
						}
					}
				}
			}

			/* 'asignamos las nuevas coordenas */
			Map = newpos.Map;
			X = Declaraciones.Npclist[nIndex].Pos.X;
			Y = Declaraciones.Npclist[nIndex].Pos.Y;
		}

		/* 'Crea el NPC */
		MakeNPCChar(true, Map, nIndex, Map, X, Y);

		retval = nIndex;

		return retval;
	}

	static void MakeNPCChar(boolean toMap, int sndIndex, int NpcIndex, int Map, int X, int Y) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int CharIndex = 0;

		if (Declaraciones.Npclist[NpcIndex].Char.CharIndex == 0) {
			CharIndex = UsUaRiOs.NextOpenCharIndex();
			Declaraciones.Npclist[NpcIndex].Char.CharIndex = CharIndex;
			Declaraciones.CharList[CharIndex] = NpcIndex;
		}

		Declaraciones.MapData[Map][X][Y].NpcIndex = NpcIndex;

		if (!toMap) {
			Protocol.WriteCharacterCreate(sndIndex, Declaraciones.Npclist[NpcIndex].Char.body,
					Declaraciones.Npclist[NpcIndex].Char.Head, Declaraciones.Npclist[NpcIndex].Char.heading,
					Declaraciones.Npclist[NpcIndex].Char.CharIndex, X, Y, 0, 0, 0, 0, 0, "", 0, 0);
			Protocol.FlushBuffer(sndIndex);
		} else {
			ModAreas.AgregarNpc(NpcIndex);
		}
	}

	static void ChangeNPCChar(int NpcIndex, int body, int Head, eHeading heading) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		if (NpcIndex > 0) {
			Declaraciones.Npclist[NpcIndex].Char.body = body;
			Declaraciones.Npclist[NpcIndex].Char.Head = Head;
			Declaraciones.Npclist[NpcIndex].Char.heading = heading;

			modSendData.SendData(SendTarget.ToNPCArea, NpcIndex, Protocol.PrepareMessageCharacterChange(body, Head,
					heading, Declaraciones.Npclist[NpcIndex].Char.CharIndex, 0, 0, 0, 0, 0));
		}
	}

	static void EraseNPCChar(int NpcIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		if (Declaraciones.Npclist[NpcIndex].Char.CharIndex != 0) {
			Declaraciones.CharList[Declaraciones.Npclist[NpcIndex].Char.CharIndex] = 0;
		}

		if (Declaraciones.Npclist[NpcIndex].Char.CharIndex == Declaraciones.LastChar) {
			while (!(Declaraciones.CharList[Declaraciones.LastChar] > 0)) {
				Declaraciones.LastChar = Declaraciones.LastChar - 1;
				if (Declaraciones.LastChar <= 1) {
					break; /* FIXME: EXIT DO */
				}
			}
		}

		/* 'Quitamos del mapa */
		Declaraciones.MapData[Declaraciones.Npclist[NpcIndex].Pos.Map][Declaraciones.Npclist[NpcIndex].Pos.X][Declaraciones.Npclist[NpcIndex].Pos.Y].NpcIndex = 0;

		/* 'Actualizamos los clientes */
		modSendData.SendData(SendTarget.ToNPCArea, NpcIndex,
				Protocol.PrepareMessageCharacterRemove(Declaraciones.Npclist[NpcIndex].Char.CharIndex));

		/* 'Update la lista npc */
		Declaraciones.Npclist[NpcIndex].Char.CharIndex = 0;

		/* 'update NumChars */
		Declaraciones.NumChars = Declaraciones.NumChars - 1;

	}

	static boolean MoveNPCChar(int NpcIndex, int nHeading) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Autor: Unknown (orginal version) */
		/* 'Last Modification: 06/04/2009 */
		/*
		 * '06/04/2009: ZaMa - Now npcs can force to change position with dead
		 * character
		 */
		/*
		 * '01/08/2009: ZaMa - Now npcs can't force to chance position with a
		 * dead character if that means to change the terrain the character is
		 * in
		 */
		/*
		 * '26/09/2010: ZaMa - Turn sub into function to know if npc has moved
		 * or not.
		 */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO errh */

		Declaraciones.WorldPos nPos;
		int UserIndex = 0;

		nPos = Declaraciones.Npclist[NpcIndex].Pos;
		Extra.HeadtoPos(nHeading, nPos);

		/* ' es una posicion legal */
		if (Extra.LegalPosNPC(nPos.Map, nPos.X, nPos.Y, Declaraciones.Npclist[NpcIndex].flags.AguaValida == 1,
				Declaraciones.Npclist[NpcIndex].MaestroUser != 0)) {

			if (Declaraciones.Npclist[NpcIndex].flags.AguaValida == 0
					&& General.HayAgua(Declaraciones.Npclist[NpcIndex].Pos.Map, nPos.X, nPos.Y)) {
				return retval;
			}
			if (Declaraciones.Npclist[NpcIndex].flags.TierraInvalida == 1
					&& !General.HayAgua(Declaraciones.Npclist[NpcIndex].Pos.Map, nPos.X, nPos.Y)) {
				return retval;
			}

			UserIndex = Declaraciones.MapData[Declaraciones.Npclist[NpcIndex].Pos.Map][nPos.X][nPos.Y].UserIndex;
			/*
			 * ' Si hay un usuario a donde se mueve el npc, entonces esta muerto
			 */
			if (UserIndex > 0) {

				/* ' No se traslada caspers de agua a tierra */
				if (General.HayAgua(Declaraciones.Npclist[NpcIndex].Pos.Map, nPos.X, nPos.Y)
						&& !General.HayAgua(Declaraciones.Npclist[NpcIndex].Pos.Map,
								Declaraciones.Npclist[NpcIndex].Pos.X, Declaraciones.Npclist[NpcIndex].Pos.Y)) {
					return retval;
				}
				/* ' No se traslada caspers de tierra a agua */
				if (!General.HayAgua(Declaraciones.Npclist[NpcIndex].Pos.Map, nPos.X, nPos.Y)
						&& General.HayAgua(Declaraciones.Npclist[NpcIndex].Pos.Map,
								Declaraciones.Npclist[NpcIndex].Pos.X, Declaraciones.Npclist[NpcIndex].Pos.Y)) {
					return retval;
				}

				/* ' Actualizamos posicion y mapa */
				Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].UserIndex = 0;
				Declaraciones.UserList[UserIndex].Pos.X = Declaraciones.Npclist[NpcIndex].Pos.X;
				Declaraciones.UserList[UserIndex].Pos.Y = Declaraciones.Npclist[NpcIndex].Pos.Y;
				Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].UserIndex = UserIndex;

				/*
				 * ' Avisamos a los usuarios del area, y al propio usuario lo
				 * forzamos a moverse
				 */
				modSendData.SendData(SendTarget.ToPCAreaButIndex, UserIndex,
						Protocol.PrepareMessageCharacterMove(Declaraciones.UserList[UserIndex].Char.CharIndex,
								Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y));
				Protocol.WriteForceCharMove(UserIndex, UsUaRiOs.InvertHeading(nHeading));
			}

			modSendData.SendData(SendTarget.ToNPCArea, NpcIndex, Protocol
					.PrepareMessageCharacterMove(Declaraciones.Npclist[NpcIndex].Char.CharIndex, nPos.X, nPos.Y));

			/* 'Update map and user pos */
			Declaraciones.MapData[Declaraciones.Npclist[NpcIndex].Pos.Map][Declaraciones.Npclist[NpcIndex].Pos.X][Declaraciones.Npclist[NpcIndex].Pos.Y].NpcIndex = 0;
			Declaraciones.Npclist[NpcIndex].Pos = nPos;
			Declaraciones.Npclist[NpcIndex].Char.heading = nHeading;
			Declaraciones.MapData[Declaraciones.Npclist[NpcIndex].Pos.Map][nPos.X][nPos.Y].NpcIndex = NpcIndex;
			ModAreas.CheckUpdateNeededNpc(NpcIndex, nHeading);

			/* ' Npc has moved */
			retval = true;

		} else if (Declaraciones.Npclist[NpcIndex].MaestroUser == 0) {
			if (Declaraciones.Npclist[NpcIndex].Movement == TipoAI.NpcPathfinding) {
				/*
				 * 'Someone has blocked the npc's way, we must to seek a new
				 * path!
				 */
				Declaraciones.Npclist[NpcIndex].PFINFO.PathLenght = 0;
			}
		}

		return retval;

		/* FIXME: errh : */
		General.LogError("Error en move npc " + NpcIndex + ". Error: " + Err.Number + " - " + Err.description);
		return retval;
	}

	static int NextOpenNPC() {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */
		int LoopC = 0;

		for (LoopC = (1); LoopC <= (Declaraciones.MAXNPCS + 1); LoopC++) {
			if (LoopC > Declaraciones.MAXNPCS) {
				break; /* FIXME: EXIT FOR */
			}
			if (!Declaraciones.Npclist[LoopC].flags.NPCActive) {
				break; /* FIXME: EXIT FOR */
			}
		}

		retval = LoopC;
		return retval;

		/* FIXME: ErrHandler : */
		General.LogError("Error en NextOpenNPC");
		return retval;
	}

	static void NpcEnvenenarUser(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 10/07/2010 */
		/* '10/07/2010: ZaMa - Now npcs can't poison dead users. */
		/* '*************************************************** */

		int N = 0;

		if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
			return;
		}

		N = Matematicas.RandomNumber(1, 100);
		if (N < 30) {
			Declaraciones.UserList[UserIndex].flags.Envenenado = 1;
			Protocol.WriteConsoleMsg(UserIndex, "¡¡La criatura te ha envenenado!!", FontTypeNames.FONTTYPE_FIGHT);
		}

	}

	static int SpawnNpc(int NpcIndex, Declaraciones.WorldPos Pos, boolean FX, boolean Respawn) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Autor: Unknown (orginal version) */
		/* 'Last Modification: 06/15/2008 */
		/*
		 * '23/01/2007 -> Pablo (ToxicWaste): Creates an NPC of the type
		 * Npcindex
		 */
		/* '06/15/2008 -> Optimizé el codigo. (NicoNZ) */
		/* '*************************************************** */
		Declaraciones.WorldPos newpos;
		Declaraciones.WorldPos altpos;
		int nIndex = 0;
		boolean PosicionValida = false;
		boolean PuedeAgua = false;
		boolean PuedeTierra = false;

		int Map = 0;
		int X = 0;
		int Y = 0;

		/* 'Conseguimos un indice */
		nIndex = OpenNPC(NpcIndex, Respawn);

		if (nIndex > Declaraciones.MAXNPCS) {
			retval = 0;
			return retval;
		}

		PuedeAgua = Declaraciones.Npclist[nIndex].flags.AguaValida;
		PuedeTierra = !Declaraciones.Npclist[nIndex].flags.TierraInvalida == 1;

		/* 'Nos devuelve la posicion valida mas cercana */
		Extra.ClosestLegalPos(Pos, newpos, PuedeAgua, PuedeTierra);
		Extra.ClosestLegalPos(Pos, altpos, PuedeAgua);
		/*
		 * 'Si X e Y son iguales a 0 significa que no se encontro posicion
		 * valida
		 */

		if (newpos.X != 0 && newpos.Y != 0) {
			/* 'Asignamos las nuevas coordenas solo si son validas */
			Declaraciones.Npclist[nIndex].Pos.Map = newpos.Map;
			Declaraciones.Npclist[nIndex].Pos.X = newpos.X;
			Declaraciones.Npclist[nIndex].Pos.Y = newpos.Y;
			PosicionValida = true;
		} else {
			if (altpos.X != 0 && altpos.Y != 0) {
				Declaraciones.Npclist[nIndex].Pos.Map = altpos.Map;
				Declaraciones.Npclist[nIndex].Pos.X = altpos.X;
				Declaraciones.Npclist[nIndex].Pos.Y = altpos.Y;
				PosicionValida = true;
			} else {
				PosicionValida = false;
			}
		}

		if (!PosicionValida) {
			QuitarNPC(nIndex);
			retval = 0;
			return retval;
		}

		/* 'asignamos las nuevas coordenas */
		Map = newpos.Map;
		X = Declaraciones.Npclist[nIndex].Pos.X;
		Y = Declaraciones.Npclist[nIndex].Pos.Y;

		/* 'Crea el NPC */
		MakeNPCChar(true, Map, nIndex, Map, X, Y);

		if (FX) {
			modSendData.SendData(SendTarget.ToNPCArea, nIndex,
					Protocol.PrepareMessagePlayWave(Declaraciones.SND_WARP, X, Y));
			modSendData.SendData(SendTarget.ToNPCArea, nIndex,
					Protocol.PrepareMessageCreateFX(Declaraciones.Npclist[nIndex].Char.CharIndex, FXIDs.FXWARP, 0));
		}

		retval = nIndex;

		return retval;
	}

	static void ReSpawnNpc(Declaraciones.npc MiNPC) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		if ((MiNPC.flags.Respawn == 0)) {
			CrearNPC(MiNPC.Numero, MiNPC.Pos.Map, MiNPC.Orig);
		}

	}

	static void NPCTirarOro(Declaraciones.npc /* FIXME BYREF!! */ MiNPC) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* 'SI EL NPC TIENE ORO LO TIRAMOS */
		if (MiNPC.GiveGLD > 0) {
			Declaraciones.Obj MiObj;
			int MiAux = 0;
			MiAux = MiNPC.GiveGLD;
			while (MiAux > Declaraciones.MAX_INVENTORY_OBJS) {
				MiObj.Amount = Declaraciones.MAX_INVENTORY_OBJS;
				MiObj.ObjIndex = Declaraciones.iORO;
				InvNpc.TirarItemAlPiso(MiNPC.Pos, MiObj);
				MiAux = MiAux - Declaraciones.MAX_INVENTORY_OBJS;
			}
			if (MiAux > 0) {
				MiObj.Amount = MiAux;
				MiObj.ObjIndex = Declaraciones.iORO;
				InvNpc.TirarItemAlPiso(MiNPC.Pos, MiObj);
			}
		}
	}

	static int OpenNPC(int NpcNumber) {
		return OpenNPC(NpcNumber, true);
	}

	static int OpenNPC(int NpcNumber, Object Respawn) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* '################################################### */
		/* '# ATENCION PELIGRO # */
		/* '################################################### */
		/* ' */
		/* ' ¡¡¡¡ NO USAR GetVar PARA LEER LOS NPCS !!!! */
		/* ' */
		/* 'El que ose desafiar esta LEY, se las tendrá que ver */
		/* 'conmigo. Para leer los NPCS se deberá usar la */
		/* 'nueva clase clsIniManager. */
		/* ' */
		/* 'Alejo */
		/* ' */
		/* '################################################### */
		int NpcIndex = 0;
		clsIniManager Leer;
		int LoopC = 0;
		String ln;

		Leer = General.LeerNPCs;

		/* 'If requested index is invalid, abort */
		if (!Leer.KeyExists("NPC" + NpcNumber)) {
			retval = Declaraciones.MAXNPCS + 1;
			return retval;
		}

		NpcIndex = NextOpenNPC();

		/* 'Limite de npcs */
		if (NpcIndex > Declaraciones.MAXNPCS) {
			retval = NpcIndex;
			return retval;
		}

		Declaraciones.Npclist[NpcIndex].Numero = NpcNumber;
		Declaraciones.Npclist[NpcIndex].Name = Leer.GetValue("NPC" + NpcNumber, "Name");
		Declaraciones.Npclist[NpcIndex].desc = Leer.GetValue("NPC" + NpcNumber, "Desc");

		Declaraciones.Npclist[NpcIndex].Movement = vb6.val(Leer.GetValue("NPC" + NpcNumber, "Movement"));
		Declaraciones.Npclist[NpcIndex].flags.OldMovement = Declaraciones.Npclist[NpcIndex].Movement;

		Declaraciones.Npclist[NpcIndex].flags.AguaValida = vb6.val(Leer.GetValue("NPC" + NpcNumber, "AguaValida"));
		Declaraciones.Npclist[NpcIndex].flags.TierraInvalida = vb6
				.val(Leer.GetValue("NPC" + NpcNumber, "TierraInValida"));
		Declaraciones.Npclist[NpcIndex].flags.Faccion = vb6.val(Leer.GetValue("NPC" + NpcNumber, "Faccion"));
		Declaraciones.Npclist[NpcIndex].flags.AtacaDoble = vb6.val(Leer.GetValue("NPC" + NpcNumber, "AtacaDoble"));

		Declaraciones.Npclist[NpcIndex].NPCtype = vb6.val(Leer.GetValue("NPC" + NpcNumber, "NpcType"));

		Declaraciones.Npclist[NpcIndex].Char.body = vb6.val(Leer.GetValue("NPC" + NpcNumber, "Body"));
		Declaraciones.Npclist[NpcIndex].Char.Head = vb6.val(Leer.GetValue("NPC" + NpcNumber, "Head"));
		Declaraciones.Npclist[NpcIndex].Char.heading = vb6.val(Leer.GetValue("NPC" + NpcNumber, "Heading"));

		Declaraciones.Npclist[NpcIndex].Attackable = vb6.val(Leer.GetValue("NPC" + NpcNumber, "Attackable"));
		Declaraciones.Npclist[NpcIndex].Comercia = vb6.val(Leer.GetValue("NPC" + NpcNumber, "Comercia"));
		Declaraciones.Npclist[NpcIndex].Hostile = vb6.val(Leer.GetValue("NPC" + NpcNumber, "Hostile"));
		Declaraciones.Npclist[NpcIndex].flags.OldHostil = Declaraciones.Npclist[NpcIndex].Hostile;

		Declaraciones.Npclist[NpcIndex].GiveEXP = vb6.val(Leer.GetValue("NPC" + NpcNumber, "GiveEXP"));
		if (Declaraciones.HappyHourActivated && (Declaraciones.HappyHour != 0)) {
			Declaraciones.Npclist[NpcIndex].GiveEXP = Declaraciones.Npclist[NpcIndex].GiveEXP * Declaraciones.HappyHour;
		}

		Declaraciones.Npclist[NpcIndex].flags.ExpCount = Declaraciones.Npclist[NpcIndex].GiveEXP;

		Declaraciones.Npclist[NpcIndex].Veneno = vb6.val(Leer.GetValue("NPC" + NpcNumber, "Veneno"));

		Declaraciones.Npclist[NpcIndex].flags.Domable = vb6.val(Leer.GetValue("NPC" + NpcNumber, "Domable"));

		Declaraciones.Npclist[NpcIndex].GiveGLD = vb6.val(Leer.GetValue("NPC" + NpcNumber, "GiveGLD"));

		Declaraciones.Npclist[NpcIndex].PoderAtaque = vb6.val(Leer.GetValue("NPC" + NpcNumber, "PoderAtaque"));
		Declaraciones.Npclist[NpcIndex].PoderEvasion = vb6.val(Leer.GetValue("NPC" + NpcNumber, "PoderEvasion"));

		Declaraciones.Npclist[NpcIndex].InvReSpawn = vb6.val(Leer.GetValue("NPC" + NpcNumber, "InvReSpawn"));

		Declaraciones.Npclist[NpcIndex].Stats.MaxHp = vb6.val(Leer.GetValue("NPC" + NpcNumber, "MaxHP"));
		Declaraciones.Npclist[NpcIndex].Stats.MinHp = vb6.val(Leer.GetValue("NPC" + NpcNumber, "MinHP"));
		Declaraciones.Npclist[NpcIndex].Stats.MaxHIT = vb6.val(Leer.GetValue("NPC" + NpcNumber, "MaxHIT"));
		Declaraciones.Npclist[NpcIndex].Stats.MinHIT = vb6.val(Leer.GetValue("NPC" + NpcNumber, "MinHIT"));
		Declaraciones.Npclist[NpcIndex].Stats.def = vb6.val(Leer.GetValue("NPC" + NpcNumber, "DEF"));
		Declaraciones.Npclist[NpcIndex].Stats.defM = vb6.val(Leer.GetValue("NPC" + NpcNumber, "DEFm"));
		Declaraciones.Npclist[NpcIndex].Stats.Alineacion = vb6.val(Leer.GetValue("NPC" + NpcNumber, "Alineacion"));

		Declaraciones.Npclist[NpcIndex].Invent.NroItems = vb6.val(Leer.GetValue("NPC" + NpcNumber, "NROITEMS"));
		for (LoopC = (1); LoopC <= (Declaraciones.Npclist[NpcIndex].Invent.NroItems); LoopC++) {
			ln = Leer.GetValue("NPC" + NpcNumber, "Obj" + LoopC);
			Declaraciones.Npclist[NpcIndex].Invent.Object[LoopC].ObjIndex = vb6.val(General.ReadField(1, ln, 45));
			Declaraciones.Npclist[NpcIndex].Invent.Object[LoopC].Amount = vb6.val(General.ReadField(2, ln, 45));
		}

		for (LoopC = (1); LoopC <= (Declaraciones.MAX_NPC_DROPS); LoopC++) {
			ln = Leer.GetValue("NPC" + NpcNumber, "Drop" + LoopC);
			Declaraciones.Npclist[NpcIndex].Drop[LoopC].ObjIndex = vb6.val(General.ReadField(1, ln, 45));
			Declaraciones.Npclist[NpcIndex].Drop[LoopC].Amount = vb6.val(General.ReadField(2, ln, 45));
		}

		Declaraciones.Npclist[NpcIndex].flags.LanzaSpells = vb6.val(Leer.GetValue("NPC" + NpcNumber, "LanzaSpells"));
		if (Declaraciones.Npclist[NpcIndex].flags.LanzaSpells > 0) {
			Declaraciones.Npclist[NpcIndex].Spells = new None[0];
			Declaraciones.Npclist[NpcIndex].Spells = (Declaraciones.Npclist[NpcIndex].Spells == null)
					? new None[1 + Declaraciones.Npclist[NpcIndex].flags.LanzaSpells]
					: java.util.Arrays.copyOf(Declaraciones.Npclist[NpcIndex].Spells,
							1 + Declaraciones.Npclist[NpcIndex].flags.LanzaSpells);
		}
		for (LoopC = (1); LoopC <= (Declaraciones.Npclist[NpcIndex].flags.LanzaSpells); LoopC++) {
			Declaraciones.Npclist[NpcIndex].Spells[LoopC] = vb6.val(Leer.GetValue("NPC" + NpcNumber, "Sp" + LoopC));
		}

		if (Declaraciones.Npclist[NpcIndex].NPCtype == eNPCType.Entrenador) {
			Declaraciones.Npclist[NpcIndex].NroCriaturas = vb6.val(Leer.GetValue("NPC" + NpcNumber, "NroCriaturas"));
			Declaraciones.Npclist[NpcIndex].Criaturas = new tCriaturasEntrenador[0];
			Declaraciones.Npclist[NpcIndex].Criaturas = (Declaraciones.Npclist[NpcIndex].Criaturas == null)
					? new tCriaturasEntrenador[1 + Declaraciones.Npclist[NpcIndex].NroCriaturas]
					: java.util.Arrays.copyOf(Declaraciones.Npclist[NpcIndex].Criaturas,
							1 + Declaraciones.Npclist[NpcIndex].NroCriaturas);
			for (LoopC = (1); LoopC <= (Declaraciones.Npclist[NpcIndex].NroCriaturas); LoopC++) {
				Declaraciones.Npclist[NpcIndex].Criaturas[LoopC].NpcIndex = Leer.GetValue("NPC" + NpcNumber,
						"CI" + LoopC);
				Declaraciones.Npclist[NpcIndex].Criaturas[LoopC].NpcName = Leer.GetValue("NPC" + NpcNumber,
						"CN" + LoopC);
			}
		}

		Declaraciones.Npclist[NpcIndex].flags.NPCActive = true;

		if (Respawn) {
			Declaraciones.Npclist[NpcIndex].flags.Respawn = vb6.val(Leer.GetValue("NPC" + NpcNumber, "ReSpawn"));
		} else {
			Declaraciones.Npclist[NpcIndex].flags.Respawn = 1;
		}

		Declaraciones.Npclist[NpcIndex].flags.BackUp = vb6.val(Leer.GetValue("NPC" + NpcNumber, "BackUp"));
		Declaraciones.Npclist[NpcIndex].flags.RespawnOrigPos = vb6.val(Leer.GetValue("NPC" + NpcNumber, "OrigPos"));
		Declaraciones.Npclist[NpcIndex].flags.AfectaParalisis = vb6
				.val(Leer.GetValue("NPC" + NpcNumber, "AfectaParalisis"));

		Declaraciones.Npclist[NpcIndex].flags.Snd1 = vb6.val(Leer.GetValue("NPC" + NpcNumber, "Snd1"));
		Declaraciones.Npclist[NpcIndex].flags.Snd2 = vb6.val(Leer.GetValue("NPC" + NpcNumber, "Snd2"));
		Declaraciones.Npclist[NpcIndex].flags.Snd3 = vb6.val(Leer.GetValue("NPC" + NpcNumber, "Snd3"));

		/* '<<<<<<<<<<<<<< Expresiones >>>>>>>>>>>>>>>> */
		Declaraciones.Npclist[NpcIndex].NroExpresiones = vb6.val(Leer.GetValue("NPC" + NpcNumber, "NROEXP"));
		if (Declaraciones.Npclist[NpcIndex].NroExpresiones > 0) {
			Declaraciones.Npclist[NpcIndex].Expresiones = new String[0];
			Declaraciones.Npclist[NpcIndex].Expresiones = (Declaraciones.Npclist[NpcIndex].Expresiones == null)
					? new String[1 + Declaraciones.Npclist[NpcIndex].NroExpresiones]
					: java.util.Arrays.copyOf(Declaraciones.Npclist[NpcIndex].Expresiones,
							1 + Declaraciones.Npclist[NpcIndex].NroExpresiones);
		}
		for (LoopC = (1); LoopC <= (Declaraciones.Npclist[NpcIndex].NroExpresiones); LoopC++) {
			Declaraciones.Npclist[NpcIndex].Expresiones[LoopC] = Leer.GetValue("NPC" + NpcNumber, "Exp" + LoopC);
		}
		/* '<<<<<<<<<<<<<< Expresiones >>>>>>>>>>>>>>>> */

		/* 'Tipo de items con los que comercia */
		Declaraciones.Npclist[NpcIndex].TipoItems = vb6.val(Leer.GetValue("NPC" + NpcNumber, "TipoItems"));

		Declaraciones.Npclist[NpcIndex].Ciudad = vb6.val(Leer.GetValue("NPC" + NpcNumber, "Ciudad"));

		/* 'Update contadores de NPCs */
		if (NpcIndex > Declaraciones.LastNPC) {
			Declaraciones.LastNPC = NpcIndex;
		}
		Declaraciones.NumNPCs = Declaraciones.NumNPCs + 1;

		/* 'Devuelve el nuevo Indice */
		retval = NpcIndex;
		return retval;
	}

	static void DoFollow(int NpcIndex, String UserName) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		if (Declaraciones.Npclist[NpcIndex].flags.Follow) {
			Declaraciones.Npclist[NpcIndex].flags.AttackedBy = "";
			Declaraciones.Npclist[NpcIndex].flags.Follow = false;
			Declaraciones.Npclist[NpcIndex].Movement = Declaraciones.Npclist[NpcIndex].flags.OldMovement;
			Declaraciones.Npclist[NpcIndex].Hostile = Declaraciones.Npclist[NpcIndex].flags.OldHostil;
		} else {
			Declaraciones.Npclist[NpcIndex].flags.AttackedBy = UserName;
			Declaraciones.Npclist[NpcIndex].flags.Follow = true;
			Declaraciones.Npclist[NpcIndex].Movement = TipoAI.NPCDEFENSA;
			Declaraciones.Npclist[NpcIndex].Hostile = 0;
		}
	}

	static void FollowAmo(int NpcIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		Declaraciones.Npclist[NpcIndex].flags.Follow = true;
		Declaraciones.Npclist[NpcIndex].Movement = TipoAI.SigueAmo;
		Declaraciones.Npclist[NpcIndex].Hostile = 0;
		Declaraciones.Npclist[NpcIndex].Target = 0;
		Declaraciones.Npclist[NpcIndex].TargetNPC = 0;
	}

	static void ValidarPermanenciaNpc(int NpcIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* 'Chequea si el npc continua perteneciendo a algún usuario */
		/* '*************************************************** */

		if (modNuevoTimer.IntervaloPerdioNpc(Declaraciones.Npclist[NpcIndex].Owner)) {
			UsUaRiOs.PerdioNpc(Declaraciones.Npclist[NpcIndex].Owner);
		}
	}

}