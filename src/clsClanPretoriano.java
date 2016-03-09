/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"clsClanPretoriano"')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_GlobalNameSpace'), (5, '='), (1, 'False')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Creatable'), (5, '='), (1, 'True')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_PredeclaredId'), (5, '='), (1, 'False')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Exposed'), (5, '='), (1, 'False')] */
/* '************************************************************** */
/* ' clsClanPretoriano - Handles the Praeorians NPCs. */
/* ' */
/* ' Designed (original version) by Mariano Barrou (El Oso) */
/* ' Redesigned by ZaMa */
/* '************************************************************** */

/* '************************************************************************** */
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
/* '************************************************************************** */
import enums.*;

public class clsClanPretoriano {

	/* '#################### */
	/* ' Pretorians Map Vars (Only used for the default map) */
	private Declaraciones.WorldPos LeftSpawnPos;
	private Declaraciones.WorldPos RightSpawnPos;

	/* '#################### */
	/* 'Actions and spells */

	/* ' King */

	/* ' Healer */

	/* ' SpellCaster */

	/* ' Thief */

	/* ' Indicates wether the clan is active or not */
	private boolean ClanActive;

	/* ' Max distance for the thief to steal */
	static final int THIEF_STEAL_DISTANCE = 2;

	/* ' Countdown to break the SpellCaster's wand */
	private int WandBreakCounter;
	/* ' Number from wich starts countdown */
	static final int MAX_WAND_BREAK_VALUE = 6;
	/* ' Min HP needed for SpellCaster to start countdown */
	static final int START_COUNTDOWN_MIN_HP = 750;

	/* ' Time in which the king hasn't done anything */
	private int KingIdleTime;
	/* ' Time needed for respawning an ally (Aprox 5 min) */
	static final int ALLY_RESPAWN_TIME = 2000;

	/* ' Max distance to follow a user, or separate from team */
	static final int MAX_DISTANCE = 14;
	/* ' Number of total clan members */
	static final int NRO_PRETORIANOS = 9;

	/* ' Indicates clan map and king position */
	private Declaraciones.WorldPos CenterPos;
	/* ' Indicates if the clan is spawned after being defeated */
	private boolean RespawnClan;
	/* ' The number of clan members still alive */
	private int ClanMembersAlive;

	static public class tPretorianData {
		public int NpcIndex;
		public ePretorianAI NPCAI;
	}

	/* ' Indice del clan pretoriano */
	private int ClanIndex;

	/* ' Contiene los index de los miembros del clan. */
	private tPretorianData[] Pretorianos = new tPretorianData[0];

	boolean SpawnClan(int mapa, int X, int Y, int PretoClanIndex) {
		return SpawnClan(mapa, X, Y, PretoClanIndex, false);
	}

	boolean SpawnClan(int mapa, int X, int Y, int PretoClanIndex, boolean Respawning) {
		boolean retval = false;
		/* '******************************************************** */
		/* 'Author: EL OSO */
		/* 'Inicializa el clan Pretoriano. */
		/* 'Last Modify Date: 21/09/2010 */
		/* '22/06/2006: (Nacho) - Seteamos cuantos NPCs creamos */
		/*
		 * '21/09/2010: ZaMa - Reescribi la funcion, ahora se le pasa las
		 * coordenadas de respawn
		 */
		/* '******************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		Declaraciones.WorldPos nPos;
		int PretoIndex = 0;

		/* ' Initial pos */
		CenterPos.Map = mapa;
		CenterPos.X = X;
		CenterPos.Y = Y;

		Pretorianos = new tPretorianData[0];
		Pretorianos = (Pretorianos == null) ? new tPretorianData[1 + NRO_PRETORIANOS]
				: java.util.Arrays.copyOf(Pretorianos, 1 + NRO_PRETORIANOS);

		ClanIndex = PretoClanIndex;

		/* ' Check valid spawn place only if it's the first time */
		if (!Respawning) {
			/* ' The clan only respawns on pretorian map */
			if (mapa == PraetoriansCoopNPC.MAPA_PRETORIANO) {
				RespawnClan = true;
				InitializePretoriansVars();

				/* ' Default coordinates, override admin choise */
				CenterPos = LeftSpawnPos;

				/* ' Check if it's a valid area to respawn */
			} else {
				if (!IsValidSpawnArea()) {
					return retval;
				}
			}
		}

		nPos = CenterPos;

		/* ' Spawn clan members */
		ClanMembersAlive = NRO_PRETORIANOS;

		/* ' Spawn King */
		PretoIndex = PretoIndex + 1;
		SpawnPretorian(nPos, ePretorianAI.King, PretoIndex, true);

		/* ' Spawn 2 Healers */
		PretoIndex = PretoIndex + 1;
		nPos.X = nPos.X + 3;
		SpawnPretorian(nPos, ePretorianAI.Healer, PretoIndex);

		PretoIndex = PretoIndex + 1;
		nPos.X = nPos.X - 6;
		SpawnPretorian(nPos, ePretorianAI.Healer, PretoIndex);

		/* ' Spawn 3 Sword Masters */
		PretoIndex = PretoIndex + 1;
		nPos.Y = nPos.Y + 3;
		SpawnPretorian(nPos, ePretorianAI.SwordMaster, PretoIndex);

		PretoIndex = PretoIndex + 1;
		nPos.X = nPos.X + 3;
		SpawnPretorian(nPos, ePretorianAI.SwordMaster, PretoIndex);

		PretoIndex = PretoIndex + 1;
		nPos.X = nPos.X + 3;
		SpawnPretorian(nPos, ePretorianAI.SwordMaster, PretoIndex);

		/* ' Sapwn 1 Shooter */
		PretoIndex = PretoIndex + 1;
		nPos.Y = nPos.Y - 6;
		nPos.X = nPos.X - 1;
		SpawnPretorian(nPos, ePretorianAI.Shooter, PretoIndex);

		/* ' Spawn 1 SpellCaster */
		PretoIndex = PretoIndex + 1;
		nPos.X = nPos.X - 4;
		SpawnPretorian(nPos, ePretorianAI.SpellCaster, PretoIndex);

		/* ' Spawn 1 Thief */
		PretoIndex = PretoIndex + 1;
		SpawnPretorian(nPos, ePretorianAI.Thief, PretoIndex);

		/* ' Reset wand break counter */
		WandBreakCounter = MAX_WAND_BREAK_VALUE;

		/* ' Now is active */
		ClanActive = true;

		/* ' Clan creation success */
		retval = true;

		return retval;

		/* FIXME: ErrHandler : */
		General.LogError("Error en CrearClanPretoriano. Error: " + Err.Number + " - " + Err.description);
		return retval;
	}

	boolean IsValidSpawnArea() {
		boolean retval = false;
		/* '******************************************************** */
		/* 'Author: ZaMa */
		/* 'Checks if it's a clean area to respawn the clan. */
		/* 'Required conditions: */
		/* '-> A 12x12 square free of blocks and in map bounds. */
		/* '-> Must be a non-water terrain */
		/* 'Last Modify Date: 21/09/2010 */
		/* '******************************************************** */

		int loopX = 0;
		int LoopY = 0;

		for (loopX = (CenterPos.X - 6); loopX <= (CenterPos.X + 6); loopX++) {
			for (LoopY = (CenterPos.Y - 6); LoopY <= (CenterPos.Y + 6); LoopY++) {

				if (!Extra.InMapBounds(CenterPos.Map, loopX, LoopY)) {
					return retval;
				}

				if (General.HayAgua(CenterPos.Map, loopX, LoopY)) {
					return retval;
				}

			}
		}

		retval = true;

		return retval;
	}

	void InitializePretoriansVars() {
		/* '******************************************************** */
		/* 'Author: ZaMa */
		/* 'Initialize the variables/const used in pretorian map */
		/* 'Last Modify Date: 21/09/2010 */
		/* '******************************************************** */

		LeftSpawnPos.Map = PraetoriansCoopNPC.MAPA_PRETORIANO;
		LeftSpawnPos.X = 35;
		LeftSpawnPos.Y = 25;

		RightSpawnPos.Map = PraetoriansCoopNPC.MAPA_PRETORIANO;
		RightSpawnPos.X = 67;
		RightSpawnPos.Y = 25;

	}

	void SpawnPretorian(Declaraciones.WorldPos /* FIXME BYREF!! */ SpawnPos, int PretorianAI, int PretoIndex) {
		SpawnPretorian(SpawnPos, PretorianAI, PretoIndex, false);
	}

	void SpawnPretorian(Declaraciones.WorldPos /* FIXME BYREF!! */ SpawnPos, int PretorianAI, int PretoIndex,
			boolean IsKing) {
		/* '******************************************************** */
		/* 'Author: ZaMa */
		/* 'Spawns the Pretorian in the given position. */
		/* 'Last Modify Date: 21/09/2010 */
		/* '******************************************************** */

		Declaraciones.WorldPos FinalPos;
		int NpcIndex = 0;
		int Head = 0;

		int PretoDatNumber = 0;
		PretoDatNumber = Matematicas.RandomNumber(PraetoriansCoopNPC.PretorianAIOffset[PretorianAI],
				PraetoriansCoopNPC.PretorianAIOffset[PretorianAI + 1] - 1);

		/* ' Odd numbers are tall races */
		if ((PretoDatNumber && 1) == 1) {
			Head = RandomTallHead();
		} else {
			Head = RandomShortHead();
		}

		/* ' The King has to spawn always in the given position */
		if (IsKing) {

			/* ' Check if there's any npc in the king's position */
			int OtherNpcIndex = 0;
			OtherNpcIndex = Declaraciones.MapData[SpawnPos.Map][SpawnPos.X][SpawnPos.Y].NpcIndex;

			/* ' Found one */
			if (OtherNpcIndex != 0) {
				/* ' Move it to the closest legal position */
				Extra.ClosestLegalPos(SpawnPos, FinalPos);
				if (FinalPos.X != 0 && FinalPos.Y != 0) {
					modSendData.SendData(SendTarget.ToNPCArea, OtherNpcIndex, Protocol.PrepareMessageCharacterMove(
							Declaraciones.Npclist[OtherNpcIndex].Char.CharIndex, FinalPos.X, FinalPos.Y));
					/* ' Update map and npc pos */
					Declaraciones.MapData[SpawnPos.Map][SpawnPos.X][SpawnPos.Y].NpcIndex = 0;
					Declaraciones.Npclist[OtherNpcIndex].Pos = FinalPos;
					Declaraciones.MapData[FinalPos.Map][FinalPos.X][FinalPos.Y].NpcIndex = OtherNpcIndex;

					/* ' King's position is now empty */
					FinalPos = SpawnPos;

					/* ' Couldn't find a legal pos => Remove it */
				} else {
					NPCs.QuitarNPC(OtherNpcIndex);
				}
			} else {
				FinalPos = SpawnPos;
			}

			/*
			 * ' Other clan member doesn't have to spawn in the exact position
			 */
		} else {
			Extra.ClosestLegalPos(SpawnPos, FinalPos, false, true);
		}

		/* ' Create and Spawn npc */
		NpcIndex = NPCs.CrearNPC(PraetoriansCoopNPC.PretorianDatNumbers[PretoDatNumber], FinalPos.Map, FinalPos, Head);

		/* ' Store Index */
		Pretorianos[PretoIndex].NpcIndex = NpcIndex;
		Pretorianos[PretoIndex].NPCAI = PretorianAI;

		/* ' Asign ClanIndex */
		Declaraciones.Npclist[NpcIndex].ClanIndex = ClanIndex;

	}

	int RandomTallHead() {
		int retval = 0;
		/* '******************************************************** */
		/* 'Author: ZaMa */
		/* 'Gets a random tall race's Head. */
		/* 'Last Modify Date: 21/09/2010 */
		/* '******************************************************** */

		/* ' 3 tall races */
		int raza = 0;
		raza = Matematicas.RandomNumber(1, 3);

		/* ' Male */
		if ((Matematicas.RandomNumber(0, 1) == 1)) {

			/* ' Human */
			if (raza == 1) {
				retval = Matematicas.RandomNumber(Declaraciones.HUMANO_H_PRIMER_CABEZA,
						Declaraciones.HUMANO_H_ULTIMA_CABEZA);
				/* ' Elf */
			} else if (raza == 2) {
				retval = Matematicas.RandomNumber(Declaraciones.ELFO_H_PRIMER_CABEZA,
						Declaraciones.ELFO_H_ULTIMA_CABEZA);
				/* ' Drow */
			} else {
				retval = Matematicas.RandomNumber(Declaraciones.DROW_H_PRIMER_CABEZA,
						Declaraciones.DROW_H_ULTIMA_CABEZA);
			}

			/* ' Female */
		} else {

			/* ' Human */
			if (raza == 1) {
				retval = Matematicas.RandomNumber(Declaraciones.HUMANO_M_PRIMER_CABEZA,
						Declaraciones.HUMANO_M_ULTIMA_CABEZA);
				/* ' Elf */
			} else if (raza == 2) {
				retval = Matematicas.RandomNumber(Declaraciones.ELFO_M_PRIMER_CABEZA,
						Declaraciones.ELFO_M_ULTIMA_CABEZA);
				/* ' Drow */
			} else {
				retval = Matematicas.RandomNumber(Declaraciones.DROW_M_PRIMER_CABEZA,
						Declaraciones.DROW_M_ULTIMA_CABEZA);
			}

		}

		return retval;
	}

	int RandomShortHead() {
		int retval = 0;
		/* '******************************************************** */
		/* 'Author: ZaMa */
		/* 'Gets a random short race's Head. */
		/* 'Last Modify Date: 21/09/2010 */
		/* '******************************************************** */

		/* ' 2 short races */
		int raza = 0;
		raza = Matematicas.RandomNumber(1, 2);

		/* ' Male */
		if ((Matematicas.RandomNumber(0, 1) == 1)) {

			/* ' Dwarf */
			if (raza == 1) {
				retval = Matematicas.RandomNumber(Declaraciones.ENANO_H_PRIMER_CABEZA,
						Declaraciones.ENANO_H_ULTIMA_CABEZA);
				/* ' Gnome */
			} else {
				retval = Matematicas.RandomNumber(Declaraciones.GNOMO_H_PRIMER_CABEZA,
						Declaraciones.GNOMO_H_ULTIMA_CABEZA);
			}

			/* ' Female */
		} else {

			/* ' Dwarf */
			if (raza == 1) {
				retval = Matematicas.RandomNumber(Declaraciones.ENANO_M_PRIMER_CABEZA,
						Declaraciones.ENANO_M_ULTIMA_CABEZA);
				/* ' Gnome */
			} else {
				retval = Matematicas.RandomNumber(Declaraciones.GNOMO_M_PRIMER_CABEZA,
						Declaraciones.GNOMO_M_ULTIMA_CABEZA);
			}

		}

		return retval;
	}

	void PerformPretorianAI(int NpcIndex) {
		/* '******************************************************** */
		/* 'Author: ZaMa */
		/* 'Performs Pretorian's AI. */
		/* 'Last Modify Date: 21/09/2010 */
		/* '******************************************************** */

		int PretorianAI = 0;
		PretorianAI = GetPretorianAI(NpcIndex);

		switch (PretorianAI) {

		case King:
			AI_King(NpcIndex);

			break;

		case Healer:
			AI_Healer(NpcIndex);

			break;

		case SpellCaster:
			AI_SpellCaster(NpcIndex);

			break;

		case SwordMaster:
			AI_SwordMaster(NpcIndex);

			break;

		case Shooter:
			AI_Shooter(NpcIndex);

			break;

		case Thief:
			AI_Thief(NpcIndex);

			break;
		}

	}

	int GetPretorianAI(int NpcIndex) {
		int retval = 0;
		/* '******************************************************** */
		/* 'Author: ZaMa */
		/* 'Returns the Pretorian's AI for the given Npc. */
		/* 'Last Modify Date: 21/09/2010 */
		/* '******************************************************** */

		int Counter = 0;

		for (Counter = (1); Counter <= (NRO_PRETORIANOS); Counter++) {
			if (Pretorianos[Counter].NpcIndex == NpcIndex) {
				retval = Pretorianos[Counter].NPCAI;
				return retval;
			}
		}

		return retval;
	}

	void AI_King(int NpcIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 19/09/2010 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		int BestTarget = 0;
		int Action = 0;

		BestTarget = KingBestTarget(NpcIndex, Action);

		KingPerformAction(NpcIndex, BestTarget, Action);

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en AI_King. Error: " + Err.Number + " - " + Err.description);
	}

	int KingBestTarget(int NpcIndex, int /* FIXME BYREF!! */ Accion) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 05/07/2010 */
		/* 'Pick the best target according to the following criteria: */
		/*
		 * '1) First try to heal status effects on allies (paralisis, poison,
		 * damaged) if any is alive
		 */
		/* '2) If no ally is alive, then start a melee atack to near users */
		/* '3) If a user tries to run, then blind and make him stupid */
		/* '4) if no one is near, then heal himself. */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		int BestTarget = 0;

		/* ' There should be more than one in order to support */
		if (ClanMembersAlive != 1) {

			/* ' Chooses target only if can attack */
			if (Declaraciones.Npclist[NpcIndex].CanAttack == 1) {
				/*
				 * ' First checks if any ally is paralized in order to remove
				 * its paralizis
				 */
				BestTarget = AllyParalyzed(NpcIndex, false);
				if (BestTarget != 0) {
					/* ' 20% of probability of doing it */
					if (Matematicas.RandomNumber(1, 100) < 21) {
						Accion = eKingAction.HealAllyParalisis;
					}
				} else {
					/*
					 * ' Now checks if any ally is poisoned in order to remove
					 * its poison
					 */
					BestTarget = AllyPoisoned(NpcIndex);
					if (BestTarget != 0) {
						Accion = eKingAction.HealAllyPoison;
					} else {
						/*
						 * ' If no target found, then try to heal injured allies
						 */
						BestTarget = AllyInjured(NpcIndex);
						if (BestTarget != 0) {
							Accion = eKingAction.HealAlly;
						}
					}
				}
			}

			/* ' King's alone */
		} else {

			int mapa = 0;
			int NPCPosX = 0;
			int NPCPosY = 0;

			int UserIndex = 0;
			int Counter = 0;

			int BestTargetDistance = 0;
			int Distance = 0;

			mapa = Declaraciones.Npclist[NpcIndex].Pos.Map;
			NPCPosX = Declaraciones.Npclist[NpcIndex].Pos.X;
			NPCPosY = Declaraciones.Npclist[NpcIndex].Pos.Y;

			int CounterStart = 0;
			int CounterEnd = 0;
			int CounterStep = 0;

			/* ' To avoid that all attack the same target */
			CounterStep = Matematicas.RandomNumber(0, 1);
			if (CounterStep == 1) {
				CounterStart = 1;
				CounterEnd = ModAreas.ConnGroups[mapa].CountEntrys;
			} else {
				CounterStart = ModAreas.ConnGroups[mapa].CountEntrys;
				CounterEnd = 1;
				CounterStep = -1;
			}

			/* ' Search for the best user target */
			/* FIXME WEIRD FOR */
			for (Counter = (CounterStart); ((CounterStep) > 0) ? (Counter <= (CounterEnd))
					: (Counter >= (CounterEnd)); Counter = Counter + (CounterStep)) {

				UserIndex = ModAreas.ConnGroups[mapa].UserEntrys[Counter];

				/* 'Is it in it's range of vision?? */
				if (InVisionRange(UserIndex, NPCPosX, NPCPosY)) {
					/* ' Can be atacked? If it's blinded, doesn't count. */
					if (UserAtacable(UserIndex) && Declaraciones.UserList[UserIndex].flags.Ceguera == 0) {
						/* ' if previous user choosen, select the better */
						if (BestTarget != 0) {
							/* ' Has priority to attack the nearest */
							Distance = UserDistance(UserIndex, NPCPosX, NPCPosY);

							if (Distance < BestTargetDistance) {
								BestTarget = UserIndex;
								BestTargetDistance = Distance;
							}
						} else {
							BestTarget = UserIndex;
							BestTargetDistance = UserDistance(UserIndex, NPCPosX, NPCPosY);
						}
					}
				}

			}

			/* ' Any target found? */
			if (BestTarget != 0) {
				/* ' Is reachable? */
				if (UserReachable(NpcIndex, BestTarget)) {
					/* ' Chase it */
					Accion = eKingAction.ChaseTarget;

					/*
					 * ' The target is trying to escape => Stop him with a
					 * massive status attack
					 */
				} else {
					Accion = eKingAction.MassiveAttack;
				}

				/* ' No target found => Retreat and heal */
			} else {
				Accion = eKingAction.RetreatAndHeal;
			}
		}

		retval = BestTarget;

		return retval;

		/* FIXME: ErrHandler : */
		General.LogError("Error en KingBestTarget. Error: " + Err.Number + " - " + Err.description);
		return retval;
	}

	void KingPerformAction(int NpcIndex, int BestTarget, int Accion) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 05/07/2010 */
		/* 'Performs the required action. */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		int IdleTime = 0;

		switch (Accion) {

		case HealAllyParalisis:
			modHechizos.NpcLanzaSpellSobreNpc(NpcIndex, BestTarget,
					Declaraciones.Npclist[NpcIndex].Spells[eKingSpells.RemoveParalisis], true);

			break;

		case HealAllyPoison:
			modHechizos.NpcLanzaSpellSobreNpc(NpcIndex, BestTarget,
					Declaraciones.Npclist[NpcIndex].Spells[eKingSpells.HealPoison], true);

			break;

		case HealAlly:
			modHechizos.NpcLanzaSpellSobreNpc(NpcIndex, BestTarget,
					Declaraciones.Npclist[NpcIndex].Spells[eKingSpells.LittleHeal], true);

			/* ' If time for respawning an ally has passed, then try to do it */
			if (KingIdleTime > ALLY_RESPAWN_TIME) {
				KingReviveAlly(NpcIndex);
			}
			IdleTime = KingIdleTime + 1;

			break;

		case ChaseTarget:
		case eKingAction.MassiveAttack:

			if (Accion == eKingAction.ChaseTarget) {
				GreedyWalkTo(NpcIndex, Declaraciones.UserList[BestTarget].Pos.Map,
						Declaraciones.UserList[BestTarget].Pos.X, Declaraciones.UserList[BestTarget].Pos.Y);
			} else {
				modHechizos.NpcLanzaSpellSobreUser(NpcIndex, BestTarget,
						Declaraciones.Npclist[NpcIndex].Spells[eKingSpells.StupidityAttack], true, true);
				/* ' So it can make double attack */
				Declaraciones.Npclist[NpcIndex].CanAttack = 1;
				modHechizos.NpcLanzaSpellSobreUser(NpcIndex, BestTarget,
						Declaraciones.Npclist[NpcIndex].Spells[eKingSpells.BlindAttack], true, true);

				/* ' King Message */
				Protocol.WriteConsoleMsg(BestTarget, "El rey pretoriano te ha vuelto estúpido.",
						FontTypeNames.FONTTYPE_FIGHT);
				Protocol.WriteConsoleMsg(BestTarget, "El rey pretoriano te ha vuelto ciego ",
						FontTypeNames.FONTTYPE_FIGHT);
				Protocol.WriteConsoleMsg(BestTarget,
						"A la distancia escuchas las siguientes palabras: ¡Cobarde, no eres digno de luchar conmigo si escapas! ",
						FontTypeNames.FONTTYPE_VENENO);
			}

			KingMeleeAttack(NpcIndex);

			break;

		case RetreatAndHeal:
			ReturnToCenter(NpcIndex);
			if (Declaraciones.Npclist[NpcIndex].Stats.MinHp != Declaraciones.Npclist[NpcIndex].Stats.MaxHp) {
				modHechizos.NpcLanzaSpellSobreNpc(NpcIndex, NpcIndex,
						Declaraciones.Npclist[NpcIndex].Spells[eKingSpells.LittleHeal], true);
			}

			/* ' If time for respawning an ally has passed, then try to do it */
			if (KingIdleTime > ALLY_RESPAWN_TIME) {
				KingReviveAlly(NpcIndex);
			}
			IdleTime = KingIdleTime + 1;

			break;
		}

		/* ' Resets if anyone is around, else increases */
		KingIdleTime = IdleTime;
		Debug.PRINTKingIdleTime();
		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en KingPerformAction. Error: " + Err.Number + " - " + Err.description);
	}

	void KingMeleeAttack(Object NpcIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 19/09/2010 */
		/* 'King performes a melee attack ignoring attack intervals */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		int headingloop = 0;
		Declaraciones.WorldPos nPos;

		int UserIndex = 0;

		/* ' Check the four directions */
		for (headingloop = (eHeading.NORTH); headingloop <= (eHeading.WEST); headingloop++) {

			nPos = Declaraciones.Npclist[NpcIndex].Pos;

			Extra.HeadtoPos(headingloop, nPos);

			/* ' In map Limits? */
			if (Extra.InMapBounds(nPos.Map, nPos.X, nPos.Y)) {

				UserIndex = Declaraciones.MapData[nPos.Map][nPos.X][nPos.Y].UserIndex;
				if (UserIndex > 0) {
					if (UserAtacable(UserIndex, false)) {
						if (SistemaCombate.NpcAtacaUser(NpcIndex, UserIndex)) {
							NPCs.ChangeNPCChar(NpcIndex, Declaraciones.Npclist[NpcIndex].Char.body,
									Declaraciones.Npclist[NpcIndex].Char.Head, headingloop);
						}

						/*
						 * ''special speed ability for praetorian king (Ignores
						 * intervals)
						 */
						Declaraciones.Npclist[NpcIndex].CanAttack = 1;
					}
				}
			}
		}

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en KingMeleeAttack. Error: " + Err.Number + " - " + Err.description);
	}

	void KingReviveAlly(int NpcIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 26/09/2010 */
		/* 'King tries to revive an ally. */
		/* '*************************************************** */

		int AllyIndex = 0;
		Declaraciones.WorldPos nPos;

		/* ' Has to be in the center to revive */
		if (Declaraciones.Npclist[NpcIndex].Pos.X == CenterPos.X) {
			if (Declaraciones.Npclist[NpcIndex].Pos.Y == CenterPos.Y) {

				/* ' Any ally dead? */
				AllyIndex = AllyDead();
				if (AllyIndex != 0) {
					nPos = CenterPos;
					nPos.X = nPos.X + 1;
					SpawnPretorian(nPos, Pretorianos[AllyIndex].NPCAI, AllyIndex);
					ClanMembersAlive = ClanMembersAlive + 1;
				}

				/* ' Reset iddle time */
				KingIdleTime = 0;

			}
		}

	}

	void AI_Healer(int NpcIndex) {
		/* '*************************************************** */
		/* 'Author: Unkown */
		/* 'Last Modification: - */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		int BestTarget = 0;
		int Accion = 0;

		/* ' Chooses target only if can attack */
		if (Declaraciones.Npclist[NpcIndex].CanAttack == 1) {
			/*
			 * ' First checks if any ally is paralized in order to remove its
			 * paralizis
			 */
			BestTarget = AllyParalyzed(NpcIndex, false);
			if (BestTarget != 0) {
				Accion = eHealerAction.HealAllyParalisis;
			} else {
				/* ' Now picks the best target to attack, either user or pet */
				BestTarget = HealerBestTarget(NpcIndex, Accion);

				/* ' If no target found, then try to heal injured allies */
				if (BestTarget == 0) {
					BestTarget = AllyInjured(NpcIndex);

					/* ' Found an injured Ally */
					if (BestTarget != 0) {
						Accion = eHealerAction.HealAlly;
					}

				}
			}
		}

		/* ' Search for far paralized allies, if not better choise selected */
		if (BestTarget == 0) {

			/* 'if it's paralized, it has no sense doing it */
			if (Declaraciones.Npclist[NpcIndex].flags.Paralizado == 0) {

				BestTarget = AllyParalyzed(NpcIndex, true);
				if (BestTarget != 0) {
					Accion = eHealerAction.RescueFarAlly;
				}

			}

		}

		boolean CanMove = false;

		/* ' Performes the best action acording to the chosen target */
		HealerPerformAction(NpcIndex, BestTarget, Accion, CanMove);

		/* ' Moves only if in danger */
		if (CanMove) {
			HealerMove(NpcIndex);
		}

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en AI_Healer. Error: " + Err.Number + " - " + Err.description);
	}

	int HealerBestTarget(int NpcIndex, int /* FIXME BYREF!! */ Accion) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 09/07/2010 */
		/* 'Pick the best target according to the following criteria: */
		/* '1) "hoaxed" friends MUST be released */
		/* '2) enemy shall be annihilated no matter what */
		/* '3) party healing if no threats */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int UserIndex = 0;
		int Counter = 0;

		int PetIndex = 0;

		int BestTarget = 0;
		boolean BestTargetInvisible = false;

		int NpcX = 0;
		int NpcY = 0;

		NpcX = Declaraciones.Npclist[NpcIndex].Pos.X;
		NpcY = Declaraciones.Npclist[NpcIndex].Pos.Y;

		int CounterStart = 0;
		int CounterEnd = 0;
		int CounterStep = 0;

		/* ' To avoid that all attack the same target */
		CounterStep = Matematicas.RandomNumber(0, 1);
		if (CounterStep == 1) {
			CounterStart = 1;
			CounterEnd = ModAreas.ConnGroups[CenterPos.Map].CountEntrys;
		} else {
			CounterStart = ModAreas.ConnGroups[CenterPos.Map].CountEntrys;
			CounterEnd = 1;
			CounterStep = -1;
		}

		/* FIXME WEIRD FOR */
		for (Counter = (CounterStart); ((CounterStep) > 0) ? (Counter <= (CounterEnd))
				: (Counter >= (CounterEnd)); Counter = Counter + (CounterStep)) {

			UserIndex = ModAreas.ConnGroups[CenterPos.Map].UserEntrys[Counter];

			/* ' Can be atacked (doesn't matter if invisible or hidden)? */
			if (UserAtacable(UserIndex, false)) {

				/*
				 * ' Checks if ther's a non-paralized user pet in range. If so,
				 * then paralize it.
				 */
				PetIndex = CheckNearUserPets(NpcIndex, UserIndex);
				if (PetIndex != 0) {
					BestTarget = PetIndex;
					Accion = eHealerAction.ParalizePet;
					break; /* FIXME: EXIT FOR */
				}

				/* 'Is it in it's range of vision?? */
				if (InVisionRange(UserIndex, NpcX, NpcY)) {

					/* ' It's Paralized? */
					if (Declaraciones.UserList[UserIndex].flags.Paralizado == 0) {

						/* ' It's visible? */
						if ((Declaraciones.UserList[UserIndex].flags.invisible == 0)
								&& (Declaraciones.UserList[UserIndex].flags.Oculto == 0)) {

							BestTarget = UserIndex;
							Accion = eHealerAction.ParalizeUser;

						}

						/*
						 * ' User Paralized, if not better target found then
						 * attack it
						 */
					} else if (BestTarget == 0) {

						BestTarget = UserIndex;
						Accion = eHealerAction.AttackUser;

					}

				}

			}

		}

		retval = BestTarget;

		return retval;

		/* FIXME: ErrHandler : */
		General.LogError("Error en HealerBestTarget. Error: " + Err.Number + " - " + Err.description);
		return retval;
	}

	void HealerPerformAction(int NpcIndex, int BestTarget, int Accion,
			boolean /* FIXME BYREF!! */ CanMove) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 09/07/2010 */
		/*
		 * 'Performs the required action. Determines if the npc can move after
		 * perfoming the selected action.
		 */
		/* '*************************************************** */

		switch (Accion) {

		case HealAllyParalisis:
			modHechizos.NpcLanzaSpellSobreNpc(NpcIndex, BestTarget,
					Declaraciones.Npclist[NpcIndex].Spells[eHealerSpells.RemoveParalisis], true);
			CanMove = false;

			break;

		case ParalizePet:
			modHechizos.NpcLanzaSpellSobreNpc(NpcIndex, BestTarget,
					Declaraciones.Npclist[NpcIndex].Spells[eHealerSpells.ParalizeNpc], true);
			CanMove = true;

			break;

		case ParalizeUser:
			modSendData.SendData(SendTarget.ToNPCArea, NpcIndex,
					Protocol.PrepareMessageChatOverHead(
							Declaraciones.Hechizos[Declaraciones.Npclist[NpcIndex].Spells[eHealerSpells.ParalizeUser]].PalabrasMagicas,
							Declaraciones.Npclist[NpcIndex].Char.CharIndex, vbCyan));
			modHechizos.NpcLanzaSpellSobreUser(NpcIndex, BestTarget,
					Declaraciones.Npclist[NpcIndex].Spells[eHealerSpells.ParalizeUser], true);
			CanMove = false;

			break;

		case AttackUser:
			modHechizos.NpcLanzaSpellSobreUser(NpcIndex, BestTarget,
					Declaraciones.Npclist[NpcIndex].Spells[eHealerSpells.Tormenta], true, true);
			CanMove = false;

			break;

		case HealAlly:
			modHechizos.NpcLanzaSpellSobreNpc(NpcIndex, BestTarget,
					Declaraciones.Npclist[NpcIndex].Spells[eHealerSpells.Heal], true);
			CanMove = true;

			break;

		case RescueFarAlly:
			GreedyWalkTo(NpcIndex, Declaraciones.Npclist[NpcIndex].Pos.Map, Declaraciones.Npclist[BestTarget].Pos.X,
					Declaraciones.Npclist[BestTarget].Pos.Y);
			CanMove = false;

			break;

		default:
			CanMove = true;

			break;
		}

	}

	void HealerMove(int NpcIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 09/07/2010 */
		/*
		 * 'Doesn't move after casting some spells. Try to avoid swordmasters.
		 */
		/* '*************************************************** */

		/* ' Can't move if paralized */
		if (Declaraciones.Npclist[NpcIndex].flags.Paralizado == 1) {
			return;
		}

		/* ' Only applies to clan map */
		if (Declaraciones.Npclist[NpcIndex].Pos.Map != CenterPos.Map) {
			return;
		}

		/* ' if nobody's near or it's idle then returns near the king */
		ReturnToCenter(NpcIndex);

		/* ' Runs away from close users */
		if (TargetClose(Declaraciones.Npclist[NpcIndex].Pos)) {
			ReturnToCenter(NpcIndex);
		}

	}

	void AI_SpellCaster(int NpcIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: - */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		int BestTarget = 0;
		int Accion = 0;

		/* ' It sacrifices itself giving a fatal blow if close to death */
		if (SpellCasterSacrifice(NpcIndex)) {
			return;
		}

		/* ' If healthy then choose best target */
		BestTarget = SpellCasterBestTarget(NpcIndex, Accion);

		/* ' Performes the best action acoording to the chosen target */
		if (BestTarget != 0) {
			SpellCasterPerformAction(NpcIndex, BestTarget, Accion);
		}

		/* ' Moves only if in danger */
		SpellCasterMove(NpcIndex, BestTarget);

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en AI_SpellCaster. Error: " + Err.Number + " - " + Err.description);
	}

	boolean SpellCasterSacrifice(int NpcIndex) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 05/07/2010 */
		/*
		 * 'Checks if the required conditions to sacrify are fullfiled, if so
		 * then does it.
		 */
		/*
		 * 'The wand break counter is stored in the boat slot, asuming it'd
		 * never be used for any other purpose.
		 */
		/* '*************************************************** */

		if (Declaraciones.Npclist[NpcIndex].Stats.MinHp <= START_COUNTDOWN_MIN_HP) {
			modSendData.SendData(SendTarget.ToNPCArea, NpcIndex,
					Protocol.PrepareMessageCreateFX(Declaraciones.Npclist[NpcIndex].Char.CharIndex,
							FXIDs.FXMEDITARGRANDE, Declaraciones.INFINITE_LOOPS));

			if (Declaraciones.Npclist[NpcIndex].CanAttack == 1) {

				Declaraciones.Npclist[NpcIndex].CanAttack = 0;

				WandBreakCounter = WandBreakCounter - 1;
				SpellCasterDestroyWand(NpcIndex, eSpellCasterSpells.Apocalipsis);

				/* ' Wand broken => dies */
				if (WandBreakCounter == 0) {
					NPCs.MuereNpc(NpcIndex, 0);
				}

			}

			retval = true;

		} else {
			/* 'Restore wand break counter */
			if (WandBreakCounter != MAX_WAND_BREAK_VALUE) {
				WandBreakCounter = MAX_WAND_BREAK_VALUE;
				modSendData.SendData(SendTarget.ToNPCArea, NpcIndex,
						Protocol.PrepareMessageCreateFX(Declaraciones.Npclist[NpcIndex].Char.CharIndex, 0, 0));
			}
		}

		return retval;
	}

	int SpellCasterBestTarget(int NpcIndex, int /* FIXME BYREF!! */ Accion) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 05/07/2010 */
		/* 'Pick the best target according to the following criteria: */
		/* '1) Invisible enemies can be detected sometimes */
		/* '2) A wizard's mission is background spellcasting attack */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int UserIndex = 0;
		int Counter = 0;

		int BestTarget = 0;
		int NpcX = 0;
		int NpcY = 0;
		boolean BestTargetInvisible = false;

		NpcX = Declaraciones.Npclist[NpcIndex].Pos.X;
		NpcY = Declaraciones.Npclist[NpcIndex].Pos.Y;

		int CounterStart = 0;
		int CounterEnd = 0;
		int CounterStep = 0;

		/* ' To avoid that all attack the same target */
		CounterStep = Matematicas.RandomNumber(0, 1);
		if (CounterStep == 1) {
			CounterStart = 1;
			CounterEnd = ModAreas.ConnGroups[CenterPos.Map].CountEntrys;
		} else {
			CounterStart = ModAreas.ConnGroups[CenterPos.Map].CountEntrys;
			CounterEnd = 1;
			CounterStep = -1;
		}

		/* FIXME WEIRD FOR */
		for (Counter = (CounterStart); ((CounterStep) > 0) ? (Counter <= (CounterEnd))
				: (Counter >= (CounterEnd)); Counter = Counter + (CounterStep)) {

			UserIndex = ModAreas.ConnGroups[CenterPos.Map].UserEntrys[Counter];

			/* 'Is it in it's range of vision?? */
			if (InVisionRange(UserIndex, NpcX, NpcY)) {

				/* ' Can be atacked? */
				if (UserAtacable(UserIndex, false)) {

					/* ' It's invisible? */
					if ((Declaraciones.UserList[UserIndex].flags.invisible == 1)
							|| (Declaraciones.UserList[UserIndex].flags.Oculto == 1)) {

						/* ' Try to remove invisibility */
						if ((Matematicas.RandomNumber(1, 100) <= 35)) {
							BestTarget = UserIndex;
							Accion = eSpellCasterAction.RemoveInvi;
							break; /* FIXME: EXIT FOR */
						}

						/* ' Paralized invisible users are good target! */
						if (Declaraciones.UserList[UserIndex].flags.Paralizado == 1) {
							BestTarget = UserIndex;
							BestTargetInvisible = true;
							Accion = eSpellCasterAction.Attack;
						}

						/* ' Visible but paralized? */
					} else if ((Declaraciones.UserList[UserIndex].flags.Paralizado == 1)) {

						/*
						 * ' If not found an invisible and paralized target,
						 * then it's a good one
						 */
						if (!BestTargetInvisible || BestTarget == 0) {
							BestTarget = UserIndex;
							Accion = eSpellCasterAction.Attack;
						}

						/*
						 * ' Visible and can move, if not better found then
						 * choose it
						 */
					} else if (BestTarget == 0) {

						BestTarget = UserIndex;
						Accion = eSpellCasterAction.Attack;

					}

				}

			}

		}

		retval = BestTarget;

		return retval;

		/* FIXME: ErrHandler : */
		General.LogError("Error en SpellCasterBestTarget. Error: " + Err.Number + " - " + Err.description);
		return retval;
	}

	void SpellCasterPerformAction(int NpcIndex, int BestTarget, int Accion) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 05/07/2010 */
		/* 'Performs the required action. */
		/* '*************************************************** */

		switch (Accion) {

		case Attack:
			modHechizos.NpcLanzaSpellSobreUser(NpcIndex, BestTarget,
					Declaraciones.Npclist[NpcIndex].Spells[eSpellCasterSpells.Apocalipsis], true, true);

			break;

		case RemoveInvi:
			modHechizos.NpcLanzaSpellSobreUser(NpcIndex, BestTarget,
					Declaraciones.Npclist[NpcIndex].Spells[eSpellCasterSpells.RemoInvi], true, true);

			break;
		}

	}

	void SpellCasterMove(int NpcIndex, int BestTarget) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 05/07/2010 */
		/* 'Doesn't move unless nobody is near or there is no best target. */
		/* '*************************************************** */

		/* ' Can't move if paralized */
		if (Declaraciones.Npclist[NpcIndex].flags.Paralizado == 1) {
			return;
		}

		/* ' Only applies to clan map */
		if (Declaraciones.Npclist[NpcIndex].Pos.Map != CenterPos.Map) {
			return;
		}

		/* ' if nobody's near or it's idle then returns near the king */
		if ((BestTarget == 0) && (Declaraciones.Npclist[NpcIndex].CanAttack == 1)) {
			ReturnToCenter(NpcIndex);
		}

		/* ' Runs away from close users */
		if (TargetClose(Declaraciones.Npclist[NpcIndex].Pos)) {
			ReturnToCenter(NpcIndex);
		}

	}

	void SpellCasterDestroyWand(int NpcIndex, int SpellIndex) {
		/* '*************************************************** */
		/* 'Author: Unkown */
		/* 'Last Modification: - */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * ''sonidos: 30 y 32, y no los cambien sino termina siendo muy
		 * chistoso...
		 */
		/* ''Para el FX utiliza el del hechizos(SpellIndex) */

		switch (WandBreakCounter) {
		case 5:
			modSendData.SendData(SendTarget.ToNPCArea, NpcIndex, Protocol.PrepareMessageChatOverHead("Rahma",
					Declaraciones.Npclist[NpcIndex].Char.CharIndex, vbGreen));
			modSendData.SendData(SendTarget.ToNPCArea, NpcIndex,
					Protocol.PrepareMessagePlayWave(PraetoriansCoopNPC.SONIDO_DRAGON_VIVO,
							Declaraciones.Npclist[NpcIndex].Pos.X, Declaraciones.Npclist[NpcIndex].Pos.Y));
			break;

		case 4:
			modSendData.SendData(SendTarget.ToNPCArea, NpcIndex, Protocol.PrepareMessageChatOverHead("vôrtax",
					Declaraciones.Npclist[NpcIndex].Char.CharIndex, vbGreen));
			modSendData.SendData(SendTarget.ToNPCArea, NpcIndex,
					Protocol.PrepareMessagePlayWave(PraetoriansCoopNPC.SONIDO_DRAGON_VIVO,
							Declaraciones.Npclist[NpcIndex].Pos.X, Declaraciones.Npclist[NpcIndex].Pos.Y));
			break;

		case 3:
			modSendData.SendData(SendTarget.ToNPCArea, NpcIndex, Protocol.PrepareMessageChatOverHead("Zill",
					Declaraciones.Npclist[NpcIndex].Char.CharIndex, vbGreen));
			modSendData.SendData(SendTarget.ToNPCArea, NpcIndex,
					Protocol.PrepareMessagePlayWave(PraetoriansCoopNPC.SONIDO_DRAGON_VIVO,
							Declaraciones.Npclist[NpcIndex].Pos.X, Declaraciones.Npclist[NpcIndex].Pos.Y));
			break;

		case 2:
			modSendData.SendData(SendTarget.ToNPCArea, NpcIndex, Protocol.PrepareMessageChatOverHead("yäkà E'nta",
					Declaraciones.Npclist[NpcIndex].Char.CharIndex, vbGreen));
			modSendData.SendData(SendTarget.ToNPCArea, NpcIndex,
					Protocol.PrepareMessagePlayWave(PraetoriansCoopNPC.SONIDO_DRAGON_VIVO,
							Declaraciones.Npclist[NpcIndex].Pos.X, Declaraciones.Npclist[NpcIndex].Pos.Y));
			break;

		case 1:
			modSendData.SendData(SendTarget.ToNPCArea, NpcIndex, Protocol.PrepareMessageChatOverHead("¡¡Koràtá!!",
					Declaraciones.Npclist[NpcIndex].Char.CharIndex, vbGreen));
			modSendData.SendData(SendTarget.ToNPCArea, NpcIndex,
					Protocol.PrepareMessagePlayWave(PraetoriansCoopNPC.SONIDO_DRAGON_VIVO,
							Declaraciones.Npclist[NpcIndex].Pos.X, Declaraciones.Npclist[NpcIndex].Pos.Y));
			break;

		case 0:
			modSendData.SendData(SendTarget.ToNPCArea, NpcIndex,
					Protocol.PrepareMessageChatOverHead("", Declaraciones.Npclist[NpcIndex].Char.CharIndex, vbGreen));
			modSendData.SendData(SendTarget.ToNPCArea, NpcIndex,
					Protocol.PrepareMessagePlayWave(PraetoriansCoopNPC.SONIDO_DRAGON_VIVO,
							Declaraciones.Npclist[NpcIndex].Pos.X, Declaraciones.Npclist[NpcIndex].Pos.Y));

			DealWandDamage(NpcIndex, Declaraciones.Npclist[NpcIndex].Spells[SpellIndex]);
			break;
		}

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en SpellCasterDestroyWand. Error: " + Err.Number + " - " + Err.description);
	}

	void DealWandDamage(int NpcIndex, int SpellIndex) {
 /* '*************************************************** */
 /* 'Author: ZaMa */
 /* 'Last Modification: 17/06/2011 */
 /* 'Description: Deals massive wand damage. */
 /* '17/06/2011: Amraphen - User's hp is updated once he gets hit by wand damage. */
 /* '*************************************************** */
 /* FIXME: ON ERROR GOTO ErrHandler */
 
 int UserIndex = 0;
 int Counter = 0;
 
 int Distancia = 0;
 int Danio = 0;
 
 int X = 0;
 int Y = 0;
 
  X = Declaraciones.Npclist[NpcIndex].Pos.X;
  Y = Declaraciones.Npclist[NpcIndex].Pos.Y;
  
   for (Counter = (1); Counter <= (ModAreas.ConnGroups[CenterPos.Map].CountEntrys); Counter++) {
   
   UserIndex = ModAreas.ConnGroups[CenterPos.Map].UserEntrys[Counter];
   
    if (UserIndex != 0) {
    
      if (UserAtacable(UserIndex, false, false)) {
      
      Distancia = UserDistance(UserIndex, X, Y);
      Danio = vb6.Abs(vb6.Int(880 / (double) (Distancia $ (3 / (double) 7))));
      
      Declaraciones.UserList[UserIndex].Stats.MinHp = Declaraciones.UserList[UserIndex].Stats.MinHp-Danio;
      
      Protocol.WriteConsoleMsg(UserIndex, Declaraciones.Npclist[NpcIndex].Name + " te ha quitado " + Danio + " puntos de vida al romper su vara.", FontTypeNames.FONTTYPE_FIGHT);
      modSendData.SendData(SendTarget.ToPCArea, UserIndex, Protocol.PrepareMessagePlayWave(Declaraciones.Hechizos[SpellIndex].WAV, Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y));
      modSendData.SendData(SendTarget.ToPCArea, UserIndex, Protocol.PrepareMessageCreateFX(Declaraciones.UserList[UserIndex].Char.CharIndex, Declaraciones.Hechizos[SpellIndex].FXgrh, Declaraciones.Hechizos[SpellIndex].loops));
      
       if (Declaraciones.UserList[UserIndex].Stats.MinHp<1) {
       Declaraciones.UserList[UserIndex].Stats.MinHp = 0;
       UsUaRiOs.UserDie(UserIndex);
       } else {
       /* 'Updates user's hp: */
       Protocol.WriteUpdateHP(UserIndex);
       /* ' Also hit pets */
       if (Declaraciones.UserList[UserIndex].NroMascotas>0) {
       DealWandDamageToPets(NpcIndex, UserIndex, SpellIndex);
       }
       
      }
      
     }
     
   }
   
  }
  
 return;
 
 /* FIXME: ErrHandler : */
 General.LogError("Error en DealWandDamage. Error: " + Err.Number + " - " + Err.description);
}

	void DealWandDamageToPets(int NpcIndex, int UserIndex, int SpellIndex) {
 /* '*************************************************** */
 /* 'Author: ZaMa */
 /* 'Last Modification: 26/09/2010 */
 /* 'Deals massive wand damage to user pets. */
 /* '*************************************************** */
 /* FIXME: ON ERROR GOTO ErrHandler */
 
 int PetIndex = 0;
 int PetCounter = 0;
 
 int Distancia = 0;
 int Danio = 0;
 
 int NpcX = 0;
 int NpcY = 0;
 
 NpcX = Declaraciones.Npclist[NpcIndex].Pos.X;
 NpcY = Declaraciones.Npclist[NpcIndex].Pos.Y;
 
  for (PetCounter = (1); PetCounter <= (Declaraciones.MAXMASCOTAS); PetCounter++) {
  
  PetIndex = Declaraciones.UserList[UserIndex].MascotasIndex[PetCounter];
   if (PetIndex>0) {
    
    Distancia = NpcDistance(PetIndex, NpcX, NpcY);
    Danio = vb6.Abs(vb6.Int(880 / (double) (Distancia $ (3 / (double) 7))));
    
    Declaraciones.Npclist[PetIndex].Stats.MinHp = Declaraciones.Npclist[PetIndex].Stats.MinHp-Danio;
    
    modSendData.SendData(SendTarget.ToNPCArea, PetIndex, Protocol.PrepareMessagePlayWave(Declaraciones.Hechizos[SpellIndex].WAV, Declaraciones.Npclist[PetIndex].Pos.X, Declaraciones.Npclist[PetIndex].Pos.Y));
    modSendData.SendData(SendTarget.ToNPCArea, PetIndex, Protocol.PrepareMessageCreateFX(Declaraciones.Npclist[PetIndex].Char.CharIndex, Declaraciones.Hechizos[SpellIndex].FXgrh, Declaraciones.Hechizos[SpellIndex].loops));
    
     if (Declaraciones.Npclist[PetIndex].Stats.MinHp<1) {
     Declaraciones.Npclist[PetIndex].Stats.MinHp = 0;
     NPCs.MuereNpc(PetIndex, 0);
    }
    
  }
  
 }
 
 return;
 
 /* FIXME: ErrHandler : */
 General.LogError("Error en DealWandDamageToPets. Error: " + Err.Number + " - " + Err.description);
}

	void AI_SwordMaster(int NpcIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: - */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		int BestTarget = 0;

		/* ' First choose best target */
		BestTarget = SwordMasterBestTarget(NpcIndex);

		/* ' Then moves, close to target user if in range */
		SwordMasterMove(NpcIndex, BestTarget);

		/* ' Finally, attacks sorrounding targets */
		SwordMasterAttack(NpcIndex);

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en AI_SwordMaster. Error: " + Err.Number + " - " + Err.description);
	}

	int SwordMasterBestTarget(int NpcIndex) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 26/06/2010 */
		/* 'Picks the best target according to the following criteria: */
		/* '1) The nearest enemy will be attacked. */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int UserIndex = 0;
		int Counter = 0;

		int BestTarget = 0;
		int BestTargetDistance = 0;

		int NpcX = 0;
		int NpcY = 0;
		int Distance = 0;

		NpcX = Declaraciones.Npclist[NpcIndex].Pos.X;
		NpcY = Declaraciones.Npclist[NpcIndex].Pos.Y;

		int CounterStart = 0;
		int CounterEnd = 0;
		int CounterStep = 0;

		/* ' To avoid that all attack the same target */
		CounterStep = Matematicas.RandomNumber(0, 1);
		if (CounterStep == 1) {
			CounterStart = 1;
			CounterEnd = ModAreas.ConnGroups[CenterPos.Map].CountEntrys;
		} else {
			CounterStart = ModAreas.ConnGroups[CenterPos.Map].CountEntrys;
			CounterEnd = 1;
			CounterStep = -1;
		}

		/* FIXME WEIRD FOR */
		for (Counter = (CounterStart); ((CounterStep) > 0) ? (Counter <= (CounterEnd))
				: (Counter >= (CounterEnd)); Counter = Counter + (CounterStep)) {

			UserIndex = ModAreas.ConnGroups[CenterPos.Map].UserEntrys[Counter];

			/* 'Is it in it's range of vision?? */
			if (InVisionRange(UserIndex, NpcX, NpcY)) {

				/* ' Can be atacked? */
				if (UserAtacable(UserIndex)) {
					/*
					 * ' Checks limits in order to avoid to separate it from the
					 * rest of the clan
					 */
					if (UserReachable(NpcIndex, UserIndex)) {

						if (BestTarget != 0) {

							/* ' Has priority to attack the nearest */
							Distance = UserDistance(UserIndex, NpcX, NpcY);

							if (Distance < BestTargetDistance) {
								BestTarget = UserIndex;
								BestTargetDistance = Distance;
							}

						} else {
							BestTarget = UserIndex;
							BestTargetDistance = UserDistance(UserIndex, NpcX, NpcY);
						}

					}

				}

			}

		}

		retval = BestTarget;

		return retval;

		/* FIXME: ErrHandler : */
		General.LogError("Error en SwordMasterBestTarget. Error: " + Err.Number + " - " + Err.description);
		return retval;
	}

	void SwordMasterMove(int NpcIndex, int BestTarget) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 24/06/2010 */
		/* 'La idea es que no lo "alejen" del rey y despues queden */
		/* 'lejos de la batalla cuando matan a un enemigo o este */
		/* 'sale del area de combate (tipica forma de separar un clan) */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		/* ' If paralized can't move */
		if (Declaraciones.Npclist[NpcIndex].flags.Paralizado == 1) {
			return;
		}

		/* ' Only applies to clan map */
		if (Declaraciones.Npclist[NpcIndex].Pos.Map != CenterPos.Map) {
			return;
		}

		/* ' If it's far from protection then returns to center */
		if (FarFromTeam(NpcIndex)) {
			ReturnToCenter(NpcIndex);
			return;
		}

		/* ' If someone's in range, then go close to him */
		if (BestTarget > 0) {

			GreedyWalkTo(NpcIndex, CenterPos.Map, Declaraciones.UserList[BestTarget].Pos.X,
					Declaraciones.UserList[BestTarget].Pos.Y);

			/* 'Returns to center if no target found */
		} else {
			ReturnToCenter(NpcIndex);
		}

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en SwordMasterMove. Error: " + Err.Number + " - " + Err.description);
	}

	void SwordMasterAttack(int NpcIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 26/06/2010 */
		/* 'Searchs and Attacks the sorrounding target */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int headingloop = 0;
		Declaraciones.WorldPos NpcPos;
		int UserIndex = 0;

		/* ' Check the four directions */
		for (headingloop = (eHeading.NORTH); headingloop <= (eHeading.WEST); headingloop++) {

			NpcPos = Declaraciones.Npclist[NpcIndex].Pos;
			Extra.HeadtoPos(headingloop, NpcPos);

			UserIndex = Declaraciones.MapData[NpcPos.Map][NpcPos.X][NpcPos.Y].UserIndex;

			if (UserIndex > 0) {
				if (UserAtacable(UserIndex, false)) {
					if (SistemaCombate.NpcAtacaUser(NpcIndex, UserIndex)) {
						NPCs.ChangeNPCChar(NpcIndex, Declaraciones.Npclist[NpcIndex].Char.body,
								Declaraciones.Npclist[NpcIndex].Char.Head, headingloop);
						break; /* FIXME: EXIT FOR */
					}
				}
			}

		}

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en SwordMasterAttack. Error: " + Err.Number + " - " + Err.description);
	}

	void AI_Shooter(int NpcIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: - */
		/* '*************************************************** */
		int BestTarget = 0;

		/* ' First choose best target */
		BestTarget = ShooterBestTarget(NpcIndex);

		/* ' Found a target => attack! */
		if (BestTarget > 0) {
			/* ' Attack with arrow */
			modHechizos.NpcLanzaSpellSobreUser(NpcIndex, BestTarget, Declaraciones.Npclist[NpcIndex].Spells[1]);
		}

		/* ' Then moves, close to target user if attacking one */
		ShooterMove(NpcIndex, BestTarget);

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en AI_Shooter. Error: " + Err.Number + " - " + Err.description);
	}

	int ShooterBestTarget(int NpcIndex) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 24/06/2010 */
		/* 'Picks the best target according to the following criteria: */
		/* '1) Magic clases ARE dangerous, but they are weak too, they're */
		/* ' our primary target */
		/* '2) in any other case, our nearest enemy will be attacked */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int UserIndex = 0;
		int Counter = 0;

		int BestTarget = 0;
		int BestTargetDistance = 0;

		int NpcX = 0;
		int NpcY = 0;
		int Distance = 0;

		NpcX = Declaraciones.Npclist[NpcIndex].Pos.X;
		NpcY = Declaraciones.Npclist[NpcIndex].Pos.Y;

		int CounterStart = 0;
		int CounterEnd = 0;
		int CounterStep = 0;

		/* ' To avoid that all attack the same target */
		CounterStep = Matematicas.RandomNumber(0, 1);
		if (CounterStep == 1) {
			CounterStart = 1;
			CounterEnd = ModAreas.ConnGroups[CenterPos.Map].CountEntrys;
		} else {
			CounterStart = ModAreas.ConnGroups[CenterPos.Map].CountEntrys;
			CounterEnd = 1;
			CounterStep = -1;
		}

		/* FIXME WEIRD FOR */
		for (Counter = (CounterStart); ((CounterStep) > 0) ? (Counter <= (CounterEnd))
				: (Counter >= (CounterEnd)); Counter = Counter + (CounterStep)) {

			UserIndex = ModAreas.ConnGroups[CenterPos.Map].UserEntrys[Counter];

			/* 'Is it in it's range of vision?? */
			if (InVisionRange(UserIndex, NpcX, NpcY)) {

				/* ' Can be atacked? */
				if (UserAtacable(UserIndex)) {

					/* ' Has Priority to attack magic clases */
					if (EsClaseMagica(UserIndex)) {
						BestTarget = UserIndex;
						break; /* FIXME: EXIT FOR */
					} else {
						if (BestTarget != 0) {

							/* ' Has priority to attack the nearest */
							Distance = UserDistance(UserIndex, NpcX, NpcY);

							if (Distance < BestTargetDistance) {
								BestTarget = UserIndex;
								BestTargetDistance = Distance;
							}

						} else {
							BestTarget = UserIndex;
							BestTargetDistance = UserDistance(UserIndex, NpcX, NpcY);
						}

					}

				}

			}

		}

		retval = BestTarget;

		return retval;

		/* FIXME: ErrHandler : */
		General.LogError("Error en ShooterBestTarget. Error: " + Err.Number + " - " + Err.description);
		return retval;
	}

	void ShooterMove(int NpcIndex, int BestTarget) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 24/06/2010 */
		/* 'Vamos a setear el hold on del cazador en el medio entre el rey */
		/*
		 * 'y el atacante. De esta manera se lo podra atacar aun asi está lejos
		 */
		/* 'pero sin alejarse del rango de los an hoax vorps de los */
		/* 'clerigos o rey. A menos q este paralizado, claro */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		/* ' If paralized can't move */
		if (Declaraciones.Npclist[NpcIndex].flags.Paralizado == 1) {
			return;
		}

		/* ' Only applies to clan map */
		if (Declaraciones.Npclist[NpcIndex].Pos.Map != CenterPos.Map) {
			return;
		}

		/* ' If it's far from protection then returns to center */
		if (FarFromTeam(NpcIndex)) {
			ReturnToCenter(NpcIndex);
			return;
		}

		/* ' Don't go too far from center */
		if (vb6.Abs(CenterPos.X - Declaraciones.Npclist[NpcIndex].Pos.X) > 4
				|| vb6.Abs(CenterPos.Y - Declaraciones.Npclist[NpcIndex].Pos.Y) > 4) {
			ReturnToCenter(NpcIndex);
			return;
		}

		/* ' If has attacked someone, then go close to him */
		if (BestTarget > 0) {

			GreedyWalkTo(NpcIndex, CenterPos.Map,
					CenterPos.X + ((Declaraciones.UserList[BestTarget].Pos.X - CenterPos.X) / (double) 2),
					CenterPos.Y + ((Declaraciones.UserList[BestTarget].Pos.Y - CenterPos.Y) / (double) 2));
			return;
		} else {

			/* ' Search for aproaching enemies */
			int Counter = 0;
			int UserIndex = 0;

			for (Counter = (1); Counter <= (ModAreas.ConnGroups[CenterPos.Map].CountEntrys); Counter++) {

				UserIndex = ModAreas.ConnGroups[CenterPos.Map].UserEntrys[Counter];

				/* 'Is it in extended range of vision from center position?? */
				if (InVisionRange(UserIndex, CenterPos.X, CenterPos.Y, true)) {

					/* ' Can be atacked? */
					if (UserAtacable(UserIndex)) {

						GreedyWalkTo(NpcIndex, CenterPos.Map, Declaraciones.UserList[UserIndex].Pos.X,
								Declaraciones.UserList[UserIndex].Pos.Y);

						return;
					}

				}
			}
		}

		/* 'Returns to center if no target found */
		if (Declaraciones.Npclist[NpcIndex].CanAttack == 1) {
			ReturnToCenter(NpcIndex);
		}

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en ShooterMove. Error: " + Err.Number + " - " + Err.description);
	}

	void AI_Thief(int NpcIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: - */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		int BestTarget = 0;
		int Action = 0;

		/* ' First choose best target */
		BestTarget = ThiefBestTarget(NpcIndex, Action);

		/* ' Perform's thief action acording to its priorities */
		ThiefPerfomAction(NpcIndex, BestTarget, Action);

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en AI_Thief. Error: " + Err.Number + " - " + Err.description);
	}

	int ThiefBestTarget(int NpcIndex, int /* FIXME BYREF!! */ Action) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 26/06/2010 */
		/* 'Picks the best target according to the following criteria: */
		/* '1) Has priority to attack hunters. */
		/* '2) Targets the nearer user. */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int UserIndex = 0;
		int Counter = 0;

		int BestTarget = 0;
		int BestTargetDistance = 0;

		int NpcX = 0;
		int NpcY = 0;
		int Distance = 0;

		NpcX = Declaraciones.Npclist[NpcIndex].Pos.X;
		NpcY = Declaraciones.Npclist[NpcIndex].Pos.Y;

		int CounterStart = 0;
		int CounterEnd = 0;
		int CounterStep = 0;

		/* ' To avoid that all attack the same target */
		CounterStep = Matematicas.RandomNumber(0, 1);
		if (CounterStep == 1) {
			CounterStart = 1;
			CounterEnd = ModAreas.ConnGroups[CenterPos.Map].CountEntrys;
		} else {
			CounterStart = ModAreas.ConnGroups[CenterPos.Map].CountEntrys;
			CounterEnd = 1;
			CounterStep = -1;
		}

		/* FIXME WEIRD FOR */
		for (Counter = (CounterStart); ((CounterStep) > 0) ? (Counter <= (CounterEnd))
				: (Counter >= (CounterEnd)); Counter = Counter + (CounterStep)) {

			UserIndex = ModAreas.ConnGroups[CenterPos.Map].UserEntrys[Counter];

			/* 'Is it in it's range of vision?? */
			if (InVisionRange(UserIndex, NpcX, NpcY)) {

				/* ' Can be atacked? */
				if (UserAtacable(UserIndex, false)) {
					/*
					 * ' Checks limits in order to avoid to separate it from the
					 * rest of the clan
					 */
					if (UserReachable(NpcIndex, UserIndex)) {

						/* ' Is it a hunter? => Has priority */
						if (Declaraciones.UserList[UserIndex].clase == eClass.Hunter) {
							BestTarget = UserIndex;
							BestTargetDistance = UserDistance(UserIndex, NpcX, NpcY);
							break; /* FIXME: EXIT FOR */
						}

						if (BestTarget != 0) {

							/* ' Has priority to attack the nearest */
							Distance = UserDistance(UserIndex, NpcX, NpcY);

							if (Distance < BestTargetDistance) {
								BestTarget = UserIndex;
								BestTargetDistance = Distance;
							}

						} else {
							BestTarget = UserIndex;
							BestTargetDistance = UserDistance(UserIndex, NpcX, NpcY);
						}

					}

				}

			}

		}

		/* ' Couldn't find anyone */
		if (BestTarget == 0) {
			Action = eThiefAction.None;
		} else {
			/* ' if close to target => Snatch/Steal */
			if (BestTargetDistance <= THIEF_STEAL_DISTANCE) {
				Action = eThiefAction.Steal;

				/* ' Too far => Attack while aproaching */
			} else {
				Action = eThiefAction.Attack;
			}
		}

		retval = BestTarget;

		return retval;

		/* FIXME: ErrHandler : */
		General.LogError("Error en ThiefBestTarget. Error: " + Err.Number + " - " + Err.description);
		return retval;
	}

	void ThiefPerfomAction(int NpcIndex, int BestTarget, int Action) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 28/10/2010 */
		/* '28/10/2010: ZaMa - Now thief doesn't steal/snatch admins. */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		int WeaponEqpSlot = 0;
		int MunicionEqpSlot = 0;

		switch (Action) {

		case Attack:
		case eThiefAction.Steal:

			/* ' Can't do anything */
			if (Declaraciones.Npclist[NpcIndex].CanAttack == 1) {

				/* ' Attack with arrow */
				if (Action == eThiefAction.Attack) {
					modHechizos.NpcLanzaSpellSobreUser(NpcIndex, BestTarget,
							Declaraciones.Npclist[NpcIndex].Spells[eThiefSpells.Arrow], false, true);

					/* ' Can paralize */
					if (Matematicas.RandomNumber(1, 100) < 10) {
						modHechizos.NpcLanzaSpellSobreUser(NpcIndex, BestTarget,
								Declaraciones.Npclist[NpcIndex].Spells[eThiefSpells.Paralisis], false, true);
						Protocol.WriteConsoleMsg(BestTarget,
								"¡¡" + Declaraciones.Npclist[NpcIndex].Name + " te ha paralizado con su golpe!!",
								FontTypeNames.FONTTYPE_VENENO);

						/* ' Can unequip weapon */
					} else if (Matematicas.RandomNumber(1, 100) < 22) {

						WeaponEqpSlot = Declaraciones.UserList[BestTarget].Invent.WeaponEqpSlot;
						if (WeaponEqpSlot != 0) {
							InvUsuario.Desequipar(BestTarget, WeaponEqpSlot, true);
						}

					}

					/* ' Try to snatch/Steal (except for admins) */
				} else if (!Extra.EsGm(BestTarget)) {

					/* ' Can snatch weapon or arrows */
					if (Matematicas.RandomNumber(1, 100) < 16) {

						WeaponEqpSlot = Declaraciones.UserList[BestTarget].Invent.WeaponEqpSlot;
						if (WeaponEqpSlot != 0) {

							if (SnatchItem(NpcIndex, BestTarget, WeaponEqpSlot)) {
								Protocol.WriteConsoleMsg(BestTarget,
										"¡¡" + Declaraciones.Npclist[NpcIndex].Name + " te ha arrebatado tu arma!!",
										FontTypeNames.FONTTYPE_VENENO);
							}

						} else {
							MunicionEqpSlot = Declaraciones.UserList[BestTarget].Invent.MunicionEqpSlot;
							if (MunicionEqpSlot != 0) {

								if (SnatchItem(NpcIndex, BestTarget, MunicionEqpSlot)) {
									Protocol.WriteConsoleMsg(BestTarget,
											"¡¡" + Declaraciones.Npclist[NpcIndex].Name
													+ " te ha arrebatado tus municiones!!",
											FontTypeNames.FONTTYPE_VENENO);
								}

							}
						}

						/* ' Can steal the items of a slot */
					} else if (Matematicas.RandomNumber(1, 100) < 16) {

						int Slot = 0;
						Slot = Matematicas.RandomNumber(1, Declaraciones.UserList[BestTarget].CurrentInventorySlots);

						if (ThiefStealITem(NpcIndex, BestTarget, Slot)) {
							Protocol.WriteConsoleMsg(BestTarget,
									"¡¡" + Declaraciones.Npclist[NpcIndex].Name + " te está robando!!",
									FontTypeNames.FONTTYPE_VENENO);
						}

					}
				}

			}

			/* ' Has chances of becoming invisible (if visible) */
			if (Declaraciones.Npclist[NpcIndex].flags.invisible == 0) {

				if (Matematicas.RandomNumber(1, 100) < 25) {
					ThiefTurnInvisible(NpcIndex, true);
				}

				/* ' Is invisible, can turn visible. */
			} else {
				if (Matematicas.RandomNumber(1, 100) < 13) {
					ThiefTurnInvisible(NpcIndex, false);
				}
			}

			/* ' Get closer to target */
			GreedyWalkTo(NpcIndex, CenterPos.Map, Declaraciones.UserList[BestTarget].Pos.X,
					Declaraciones.UserList[BestTarget].Pos.Y);

			break;

		case None:

			/* ' Turns visible if no threat */
			if (Declaraciones.Npclist[NpcIndex].flags.invisible == 1) {
				ThiefTurnInvisible(NpcIndex, false);
			}

			ReturnToCenter(NpcIndex);

			break;
		}

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en ThiefPerfomAction. Error: " + Err.Number + " - " + Err.description);
	}

	boolean SnatchItem(int NpcIndex, int TargetIndex, int Slot) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 25/09/2010 */
		/*
		 * 'Snatchs slot items from user. No validations are made bacause it's
		 * either weapon or munition.
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		Declaraciones.Obj DropObj;
		Declaraciones.WorldPos nPos;

		DropObj.Amount = Declaraciones.UserList[TargetIndex].Invent.Object[Slot].Amount;
		DropObj.ObjIndex = Declaraciones.UserList[TargetIndex].Invent.Object[Slot].ObjIndex;

		/* ' Search for suitable place to drop item */
		UsUaRiOs.Tilelibre(Declaraciones.UserList[TargetIndex].Pos, nPos, DropObj, false, true);

		/* ' Found any? */
		if (nPos.X != 0 && nPos.Y != 0) {
			/* ' Drop it */
			InvUsuario.MakeObj(DropObj, nPos.Map, nPos.X, nPos.Y);
			InvUsuario.QuitarUserInvItem(TargetIndex, Slot, DropObj.Amount);
			InvUsuario.UpdateUserInv(false, TargetIndex, Slot);

			/* ' Suceed */
			retval = true;
		}

		return retval;

		/* FIXME: ErrHandler : */
		General.LogError("Error en SnatchItem. Error: " + Err.Number + " - " + Err.description);
		return retval;
	}

	boolean ThiefStealITem(int NpcIndex, int TargetIndex, int Slot) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 25/09/2010 */
		/*
		 * 'Steal slot items from user. If no space in thief's inventory, drop
		 * it on floor.
		 */
		/*
		 * '22/10/2010: ZaMa - Now thief doesn't try to stel slots with no items
		 * in it.
		 */
		/* '*************************************************** */
		Declaraciones.Obj StolenObj;
		int NroItems = 0;

		/* ' If doesn't have any item, the doesn't do anything */
		if (Declaraciones.UserList[TargetIndex].Invent.Object[Slot].ObjIndex == 0) {
			return retval;
		}

		/* ' If not stealable, then don't do anything */
		if (!Trabajo.ObjEsRobable(TargetIndex, Slot)) {
			return retval;
		}

		/* ' If thief's inventory is full, then snatch it */
		NroItems = Declaraciones.Npclist[NpcIndex].Invent.NroItems;
		if (NroItems == 20) {

			retval = SnatchItem(NpcIndex, TargetIndex, Slot);

			/* ' Steal it */
		} else {

			StolenObj.Amount = Declaraciones.UserList[TargetIndex].Invent.Object[Slot].Amount;
			StolenObj.ObjIndex = Declaraciones.UserList[TargetIndex].Invent.Object[Slot].ObjIndex;

			/* ' Add it to Thief inventory */
			NroItems = NroItems + 1;
			Declaraciones.Npclist[NpcIndex].Invent.Object[NroItems].Amount = StolenObj.Amount;
			Declaraciones.Npclist[NpcIndex].Invent.Object[NroItems].ObjIndex = StolenObj.ObjIndex;
			Declaraciones.Npclist[NpcIndex].Invent.NroItems = NroItems;

			/* ' Take it from user inventory */
			InvUsuario.QuitarUserInvItem(TargetIndex, Slot, StolenObj.Amount);
			InvUsuario.UpdateUserInv(false, TargetIndex, Slot);

			retval = true;

		}

		return retval;
	}

	void ThiefTurnInvisible(int NpcIndex, boolean TurnVisible) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 26/09/2010 */
		/* 'Turn thief visible/Invisible. */
		/* '*************************************************** */

		if (TurnVisible) {
			Declaraciones.Npclist[NpcIndex].flags.invisible = 1;
		} else {
			Declaraciones.Npclist[NpcIndex].flags.invisible = 0;
		}

		modSendData.SendData(SendTarget.ToNPCArea, NpcIndex,
				Protocol.PrepareMessageSetInvisible(Declaraciones.Npclist[NpcIndex].Char.CharIndex, TurnVisible));

	}

	boolean TargetClose(Declaraciones.WorldPos /* FIXME BYREF!! */ Pos) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 05/07/2010 */
		/*
		 * 'Checks if there is any attackable user sorrounding the given
		 * position.
		 */
		/* '*************************************************** */
		int UserIndex = 0;

		Declaraciones.WorldPos nPos;
		int headingloop = 0;

		for (headingloop = (eHeading.NORTH); headingloop <= (eHeading.WEST); headingloop++) {

			nPos = Pos;
			Extra.HeadtoPos(headingloop, nPos);

			UserIndex = Declaraciones.MapData[nPos.Map][nPos.X][nPos.Y].UserIndex;

			if (UserIndex != 0) {
				if (UserAtacable(UserIndex)) {
					retval = true;
					return retval;
				}
			}
		}

		return retval;
	}

	int CheckNearUserPets(int NpcIndex, int UserIndex) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 09/07/2010 */
		/*
		 * 'Checks if there is a non-paralized user pet in cleric vision range.
		 */
		/* '*************************************************** */
		int PetCounter = 0;
		int PetIndex = 0;

		if (Declaraciones.UserList[UserIndex].NroMascotas == 0) {
			return retval;
		}

		for (PetCounter = (1); PetCounter <= (Declaraciones.MAXMASCOTAS); PetCounter++) {

			PetIndex = Declaraciones.UserList[UserIndex].MascotasIndex[PetCounter];
			if (PetIndex > 0) {

				/* ' If it is visible for the Pretorian */
				if (InVisionRangeNpc(NpcIndex, Declaraciones.Npclist[PetIndex].Pos.X,
						Declaraciones.Npclist[PetIndex].Pos.Y)) {

					/* ' If not paralized then it's a suitable target */
					if (Declaraciones.Npclist[PetIndex].flags.Paralizado == 0) {
						retval = PetIndex;
						return retval;
					}

				}

			}

		}

		return retval;
	}

	boolean EsClaseMagica(int UserIndex) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unkown */
		/* 'Last Modification: - */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		retval = Declaraciones.UserList[UserIndex].clase == eClass.Mage
				|| Declaraciones.UserList[UserIndex].clase == eClass.Cleric
				|| Declaraciones.UserList[UserIndex].clase == eClass.Druid
				|| Declaraciones.UserList[UserIndex].clase == eClass.Bard;

		return retval;

		/* FIXME: ErrHandler : */
		General.LogError("Error en EsClaseMagica. Error: " + Err.Number + " - " + Err.description);
		return retval;
	}

	void GreedyWalkTo(int NpcIndex, int TargetMap, int TargetX, int TargetY) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* ' Este procedimiento es llamado cada vez que un NPC deba ir */
		/* ' a otro lugar en el mismo TargetMap. Utiliza una técnica */
		/* ' de programación greedy no determinística. */
		/* ' Cada paso azaroso que me acerque al destino, es un buen paso. */
		/*
		 * ' Si no hay mejor paso válido, entonces hay que volver atrás y
		 * reintentar.
		 */
		/* ' Si no puedo moverme, me considero piketeado */
		/*
		 * ' La funcion es larga, pero es O(1) - orden algorítmico temporal
		 * constante
		 */
		/* 'Last Modification: 26/09/2010 */
		/* 'Rapsodius - Changed Mod by And for speed */
		/*
		 * '26/09/2010: ZaMa - Now make movements as normal npcs, which allows
		 * to kick caspers and invisible admins.
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		int NpcX = 0;
		int NpcY = 0;
		int RandomDir = 0;

		if (Declaraciones.Npclist[NpcIndex].Pos.Map != TargetMap) {
			return;
		}

		NpcX = Declaraciones.Npclist[NpcIndex].Pos.X;
		NpcY = Declaraciones.Npclist[NpcIndex].Pos.Y;

		/* ' Arrived to destination */
		if ((NpcX == TargetX && NpcY == TargetY)) {
			return;
		}

		/* ' Try to reach target */
		if ((NpcX > TargetX)) {

			/* ' Target is Down-Left */
			if ((NpcY < TargetY)) {

				RandomDir = Matematicas.RandomNumber(0, 9);
				/* ''move down */
				if (((RandomDir && 1) == 0)) {

					if (NPCs.MoveNPCChar(NpcIndex, eHeading.SOUTH)) {
						return;
					} else if (NPCs.MoveNPCChar(NpcIndex, eHeading.WEST)) {
						return;
					} else if (NPCs.MoveNPCChar(NpcIndex, eHeading.EAST)) {
						return;
					} else if (NPCs.MoveNPCChar(NpcIndex, eHeading.NORTH)) {
						return;
					}

				} else {
					/* ''random first move */
					if (NPCs.MoveNPCChar(NpcIndex, eHeading.WEST)) {
						return;
					} else if (NPCs.MoveNPCChar(NpcIndex, eHeading.SOUTH)) {
						return;
					} else if (NPCs.MoveNPCChar(NpcIndex, eHeading.EAST)) {
						return;
					} else if (NPCs.MoveNPCChar(NpcIndex, eHeading.NORTH)) {
						return;
					}

				}

				/* ' Target is Up-Left */
			} else if ((NpcY > TargetY)) {

				RandomDir = Matematicas.RandomNumber(0, 9);
				/* ''move up */
				if (((RandomDir && 1) == 0)) {

					if (NPCs.MoveNPCChar(NpcIndex, eHeading.NORTH)) {
						return;
					} else if (NPCs.MoveNPCChar(NpcIndex, eHeading.WEST)) {
						return;
					} else if (NPCs.MoveNPCChar(NpcIndex, eHeading.SOUTH)) {
						return;
					} else if (NPCs.MoveNPCChar(NpcIndex, eHeading.EAST)) {
						return;
					}

				} else {
					/* ''random first move */
					if (NPCs.MoveNPCChar(NpcIndex, eHeading.WEST)) {
						return;
					} else if (NPCs.MoveNPCChar(NpcIndex, eHeading.NORTH)) {
						return;
					} else if (NPCs.MoveNPCChar(NpcIndex, eHeading.SOUTH)) {
						return;
					} else if (NPCs.MoveNPCChar(NpcIndex, eHeading.EAST)) {
						return;
					}

				}

				/* ' Target is Straight Left */
			} else {

				if (NPCs.MoveNPCChar(NpcIndex, eHeading.WEST)) {
					return;
				} else if (NPCs.MoveNPCChar(NpcIndex, eHeading.SOUTH)) {
					return;
				} else if (NPCs.MoveNPCChar(NpcIndex, eHeading.NORTH)) {
					return;
				} else {
					/*
					 * ' If moves to east, algorithm fails because starts a loop
					 */
					MoveFailed(NpcIndex);
				}

			}

		} else if ((NpcX < TargetX)) {

			/* ' Target is Down-Right */
			if ((NpcY < TargetY)) {

				RandomDir = Matematicas.RandomNumber(0, 9);
				/* ''move down */
				if (((RandomDir && 1) == 0)) {

					if (NPCs.MoveNPCChar(NpcIndex, eHeading.SOUTH)) {
						return;
					} else if (NPCs.MoveNPCChar(NpcIndex, eHeading.EAST)) {
						return;
					} else if (NPCs.MoveNPCChar(NpcIndex, eHeading.WEST)) {
						return;
					} else if (NPCs.MoveNPCChar(NpcIndex, eHeading.NORTH)) {
						return;
					}

					/* ''random first move */
				} else {

					if (NPCs.MoveNPCChar(NpcIndex, eHeading.EAST)) {
						return;
					} else if (NPCs.MoveNPCChar(NpcIndex, eHeading.SOUTH)) {
						return;
					} else if (NPCs.MoveNPCChar(NpcIndex, eHeading.WEST)) {
						return;
					} else if (NPCs.MoveNPCChar(NpcIndex, eHeading.NORTH)) {
						return;
					}

				}

				/* ' Target is Up-Right */
			} else if ((NpcY > TargetY)) {

				RandomDir = Matematicas.RandomNumber(0, 9);
				/* ''move up */
				if (((RandomDir && 1) == 0)) {

					if (NPCs.MoveNPCChar(NpcIndex, eHeading.NORTH)) {
						return;
					} else if (NPCs.MoveNPCChar(NpcIndex, eHeading.EAST)) {
						return;
					} else if (NPCs.MoveNPCChar(NpcIndex, eHeading.WEST)) {
						return;
					} else if (NPCs.MoveNPCChar(NpcIndex, eHeading.SOUTH)) {
						return;
					}

				} else {

					if (NPCs.MoveNPCChar(NpcIndex, eHeading.EAST)) {
						return;
					} else if (NPCs.MoveNPCChar(NpcIndex, eHeading.NORTH)) {
						return;
					} else if (NPCs.MoveNPCChar(NpcIndex, eHeading.SOUTH)) {
						return;
					} else if (NPCs.MoveNPCChar(NpcIndex, eHeading.WEST)) {
						return;
					}

				}

				/* ' Target is Straight Right */
			} else {

				if (NPCs.MoveNPCChar(NpcIndex, eHeading.EAST)) {
					return;
				} else if (NPCs.MoveNPCChar(NpcIndex, eHeading.SOUTH)) {
					return;
				} else if (NPCs.MoveNPCChar(NpcIndex, eHeading.NORTH)) {
					return;
				} else {
					/*
					 * ' If moves to west, algorithm fails because starts a loop
					 */
					MoveFailed(NpcIndex);
				}

			}

			/* ' Target is straight Up/Down */
		} else {

			/* ' Target is straight Up */
			if ((NpcY > TargetY)) {

				if (NPCs.MoveNPCChar(NpcIndex, eHeading.NORTH)) {
					return;
				} else if (NPCs.MoveNPCChar(NpcIndex, eHeading.EAST)) {
					return;
				} else if (NPCs.MoveNPCChar(NpcIndex, eHeading.WEST)) {
					return;
				} else {
					/*
					 * ' If moves to south, algorithm fails because starts a
					 * loop
					 */
					MoveFailed(NpcIndex);
				}

				/* ' Target is straight Down */
			} else {

				if (NPCs.MoveNPCChar(NpcIndex, eHeading.SOUTH)) {
					return;
				} else if (NPCs.MoveNPCChar(NpcIndex, eHeading.EAST)) {
					return;
				} else if (NPCs.MoveNPCChar(NpcIndex, eHeading.WEST)) {
					return;
				} else {
					/*
					 * ' If moves to north, algorithm fails because starts a
					 * loop
					 */
					MoveFailed(NpcIndex);
				}

			}

		}

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en GreedyWalkTo. Error: " + Err.Number + " - " + Err.description);
	}

	void MoveFailed(int NpcIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 26/06/2010 */
		/*
		 * 'Npc shouts a message overhead because cannot move (to avoid a loop)
		 */
		/* '*************************************************** */

		if (Declaraciones.Npclist[NpcIndex].CanAttack == 1) {
			if ((Matematicas.RandomNumber(1, 100) > 95)) {
				modSendData.SendData(SendTarget.ToNPCArea, NpcIndex, Protocol.PrepareMessageChatOverHead(
						"Maldito bastardo, ¡Ven aquí!", Declaraciones.Npclist[NpcIndex].Char.CharIndex, vbYellow));
				Declaraciones.Npclist[NpcIndex].CanAttack = 0;
			}
		}

	}

	void ReturnToCenter(int NpcIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 19/09/2010 */
		/* 'Returns to the center, where the king is. */
		/* '*************************************************** */

		GreedyWalkTo(NpcIndex, CenterPos.Map, CenterPos.X, CenterPos.Y);

	}

	boolean FarFromTeam(Object NpcIndex) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown & ZaMa */
		/* 'Last Modification: 19/09/2010 */
		/* 'Checks if Npc is far from cleric protection ring */
		/* '*************************************************** */

		if (Declaraciones.Npclist[NpcIndex].Pos.Map != CenterPos.Map) {
			return retval;
		}

		retval = (vb6.Abs(Declaraciones.Npclist[NpcIndex].Pos.Y - CenterPos.Y) > MAX_DISTANCE)
				|| (vb6.Abs(Declaraciones.Npclist[NpcIndex].Pos.X - CenterPos.X) > MAX_DISTANCE);

		return retval;

		/* FIXME: ErrHandler : */
		General.LogError("Error en FarFromTeam. Error: " + Err.Number + " - " + Err.description);
		return retval;
	}

	boolean UserReachable(int NpcIndex, int UserIndex) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 19/09/2010 */
		/*
		 * 'Ignores users who are too far in order to avoid being separated from
		 * the rest of the team.
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		if (Declaraciones.Npclist[NpcIndex].Pos.Map != CenterPos.Map) {
			return retval;
		}

		retval = (vb6.Abs(Declaraciones.UserList[UserIndex].Pos.X - CenterPos.X) < MAX_DISTANCE)
				&& (vb6.Abs(Declaraciones.UserList[UserIndex].Pos.Y - CenterPos.Y) < MAX_DISTANCE);

		return retval;

		/* FIXME: ErrHandler : */
		General.LogError("Error en UserReachable. Error: " + Err.Number + " - " + Err.description);
		return retval;
	}

	boolean InVisionRange(int UserIndex, int X, int Y) {
		return InVisionRange(UserIndex, X, Y, false);
	}

	boolean InVisionRange(int UserIndex, int X, int Y, boolean ExtendedRange) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 24/06/2010 */
		/* 'Checks whether user is in vision range or not */
		/* '*************************************************** */

		int Rango = 0;
		Rango = vb6.val(vb6.IIf(ExtendedRange, 2, 1));

		retval = vb6.Abs(Declaraciones.UserList[UserIndex].Pos.X - X) <= AI.RANGO_VISION_X * Rango
				&& vb6.Abs(Declaraciones.UserList[UserIndex].Pos.Y - Y) <= AI.RANGO_VISION_Y * Rango;

		return retval;
	}

	boolean InVisionRangeNpc(int NpcIndex, int X, int Y) {
		return InVisionRangeNpc(NpcIndex, X, Y, false);
	}

	boolean InVisionRangeNpc(int NpcIndex, int X, int Y, boolean ExtendedRange) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 24/06/2010 */
		/* 'Checks whether npc is in vision range or not */
		/* '*************************************************** */

		int Rango = 0;
		Rango = vb6.val(vb6.IIf(ExtendedRange, 2, 1));

		retval = vb6.Abs(Declaraciones.Npclist[NpcIndex].Pos.X - X) <= AI.RANGO_VISION_X * Rango
				&& vb6.Abs(Declaraciones.Npclist[NpcIndex].Pos.Y - Y) <= AI.RANGO_VISION_Y * Rango;

		return retval;
	}

	boolean UserAtacable(int UserIndex) {
		return UserAtacable(UserIndex, true, true);
	}

	boolean UserAtacable(int UserIndex, boolean CheckVisibility, boolean AttackAdmin) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 05/10/2010 */
		/* 'DEtermines whether the user can be atacked or not */
		/* '05/10/2010: ZaMa - Now doesn't allow to attack admins sometimes. */
		/* '*************************************************** */

		retval = !Declaraciones.UserList[UserIndex].flags.EnConsulta
				&& Declaraciones.UserList[UserIndex].flags.AdminInvisible == 0
				&& Declaraciones.UserList[UserIndex].flags.AdminPerseguible
				&& Declaraciones.UserList[UserIndex].flags.Muerto == 0;

		if (CheckVisibility) {
			retval = retval && Declaraciones.UserList[UserIndex].flags.Oculto == 0
					&& Declaraciones.UserList[UserIndex].flags.invisible == 0;
		}

		if (!AttackAdmin) {
			retval = retval && (!Extra.EsGm(UserIndex));
		}

		return retval;
	}

	int UserDistance(int UserIndex, int X, int Y) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 24/06/2010 */
		/* 'Calculates the user distance according to the given coords. */
		/* '*************************************************** */

		retval = vb6.Abs(X - Declaraciones.UserList[UserIndex].Pos.X)
				+ vb6.Abs(Y - Declaraciones.UserList[UserIndex].Pos.Y);

		return retval;
	}

	int NpcDistance(int NpcIndex, int X, int Y) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 24/06/2010 */
		/* 'Calculates the npc distance according to the given coords. */
		/* '*************************************************** */

		retval = vb6.Abs(X - Declaraciones.Npclist[NpcIndex].Pos.X)
				+ vb6.Abs(Y - Declaraciones.Npclist[NpcIndex].Pos.Y);

		return retval;
	}

	void MuerePretoriano(int NpcIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 27/06/2010 */
		/*
		 * 'Eliminates the pretorian from the current alive ones, and respawn
		 * the clan if in pretorian's default map.
		 */
		/* '*************************************************** */

		int PretoIndex = 0;

		for (PretoIndex = (1); PretoIndex <= (NRO_PRETORIANOS); PretoIndex++) {
			/* ' Remove npc index */
			if (Pretorianos[PretoIndex].NpcIndex == NpcIndex) {
				Pretorianos[PretoIndex].NpcIndex = 0;
				ClanMembersAlive = ClanMembersAlive - 1;

				/* ' Entire clan has been defeated */
				if (ClanMembersAlive == 0) {
					/* ' Respawn it? (Only allowed in pretorian default map) */
					if (RespawnClan) {

						Declaraciones.WorldPos NewSpawnPos;

						/*
						 * ' Switch respawn place (alternate between two places)
						 */
						if (CenterPos.X == LeftSpawnPos.X) {
							NewSpawnPos = RightSpawnPos;
						} else {
							NewSpawnPos = LeftSpawnPos;
						}

						/* ' Spawn it */
						SpawnClan(NewSpawnPos.Map, NewSpawnPos.X, NewSpawnPos.Y, ClanIndex, true);
					} else {
						ClanActive = false;
					}
				}

				return;
			}
		}

	}

	int AllyParalyzed(int NpcIndex, boolean ExtendedRange) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 27/06/2010 */
		/* 'Returns the index of the paralized ally if exists one. */
		/* '*************************************************** */

		int PretoIndex = 0;
		int AllyIndex = 0;

		for (PretoIndex = (1); PretoIndex <= (NRO_PRETORIANOS); PretoIndex++) {

			AllyIndex = Pretorianos[PretoIndex].NpcIndex;
			if (AllyIndex != 0) {
				if (Declaraciones.Npclist[AllyIndex].flags.Paralizado == 1) {
					if (InVisionRangeNpc(AllyIndex, Declaraciones.Npclist[NpcIndex].Pos.X,
							Declaraciones.Npclist[NpcIndex].Pos.Y, ExtendedRange)) {
						retval = AllyIndex;
						return retval;
					}
				}
			}

		}

		return retval;
	}

	int AllyInjured(int NpcIndex) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 09/07/2010 */
		/* 'Returns the index of the first found injured ally if exists one. */
		/* '*************************************************** */

		int PretoIndex = 0;
		int AllyIndex = 0;

		for (PretoIndex = (1); PretoIndex <= (NRO_PRETORIANOS); PretoIndex++) {

			AllyIndex = Pretorianos[PretoIndex].NpcIndex;
			if (AllyIndex != 0) {
				if (Declaraciones.Npclist[AllyIndex].Stats.MinHp < Declaraciones.Npclist[AllyIndex].Stats.MaxHp) {
					if (InVisionRangeNpc(NpcIndex, Declaraciones.Npclist[AllyIndex].Pos.X,
							Declaraciones.Npclist[AllyIndex].Pos.Y, false)) {
						retval = AllyIndex;
						return retval;
					}
				}
			}

		}

		return retval;
	}

	int AllyPoisoned(int NpcIndex) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 19/09/2010 */
		/* 'Returns the index of the poisoned ally if exists one. */
		/* '*************************************************** */

		int PretoIndex = 0;
		int AllyIndex = 0;

		for (PretoIndex = (1); PretoIndex <= (NRO_PRETORIANOS); PretoIndex++) {

			AllyIndex = Pretorianos[PretoIndex].NpcIndex;
			if (AllyIndex != 0) {
				if (Declaraciones.Npclist[AllyIndex].flags.Envenenado == 1) {
					if (InVisionRangeNpc(AllyIndex, Declaraciones.Npclist[NpcIndex].Pos.X,
							Declaraciones.Npclist[NpcIndex].Pos.Y)) {
						retval = AllyIndex;
						return retval;
					}
				}
			}

		}

		return retval;
	}

	int AllyDead() {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 19/09/2010 */
		/* 'Returns the Index of the first dead member found, if exists. */
		/* '*************************************************** */

		int PretoIndex = 0;
		int AllyIndex = 0;

		for (PretoIndex = (1); PretoIndex <= (NRO_PRETORIANOS); PretoIndex++) {

			if (Pretorianos[PretoIndex].NpcIndex == 0) {
				retval = PretoIndex;
				return retval;
			}

		}

		return retval;
	}

	boolean CanAtackMember(int NpcIndex) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 26/09/2010 */
		/*
		 * 'Returns true if given NpcIndex belongs to a normal clan member, o if
		 * king's alone.
		 */
		/* '*************************************************** */

		/* ' King? */
		if (Pretorianos[1].NpcIndex == NpcIndex) {
			/* ' any member left? => Can't attack */
			if (ClanMembersAlive != 1) {
				return retval;
			}
		}

		retval = true;

		return retval;
	}

	int ClanMap() {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 29/10/2010 */
		/* 'Returns the clan map */
		/* '*************************************************** */
		retval = CenterPos.Map;
		return retval;
	}

	boolean Active() {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 29/10/2010 */
		/* 'Returns true if clan is active. */
		/* '*************************************************** */
		retval = ClanActive;
		return retval;
	}

	void DeleteClan() {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 29/10/2010 */
		/* 'Kill all alive members. */
		/* '*************************************************** */

		int PretoIndex = 0;
		int NpcIndex = 0;

		for (PretoIndex = (1); PretoIndex <= (NRO_PRETORIANOS); PretoIndex++) {

			NpcIndex = Pretorianos[PretoIndex].NpcIndex;
			if (NpcIndex != 0) {
				NPCs.QuitarNPC(NpcIndex);
			}

			Pretorianos[PretoIndex].NpcIndex = 0;

		}

		ClanActive = false;

	}

}