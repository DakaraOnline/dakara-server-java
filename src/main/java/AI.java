/*  AUTOMATICALLY CONVERTED FILE  */

/* 
 * Este archivo fue convertido automaticamente, por un script, desde el 
 * código fuente original de Visual Basic 6.
 */

/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"AI"')] */
/* 'Argentum Online 0.12.2 */
/* 'Copyright (C) 2002 Mï¿½rquez Pablo Ignacio */
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
/* 'Calle 3 nï¿½mero 983 piso 7 dto A */
/* 'La Plata - Pcia, Buenos Aires - Republica Argentina */
/* 'Cï¿½digo Postal 1900 */
/* 'Pablo Ignacio Mï¿½rquez */
// HOLA Q ASEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
import enums.*;

public class AI {

	static final int ELEMENTALFUEGO = 93;
	static final int ELEMENTALTIERRA = 94;
	static final int ELEMENTALAGUA = 92;

	/* 'Damos a los NPCs el mismo rango de visiï¿½n que un PJ */
	static final int RANGO_VISION_X = 8;
	static final int RANGO_VISION_Y = 6;

	/*
	 * '?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½
	 * ?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½
	 */
	/*
	 * '?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½
	 * ?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½
	 */
	/*
	 * '?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½
	 * ?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½
	 */
	/* ' Modulo AI_NPC */
	/*
	 * '?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½
	 * ?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½
	 */
	/*
	 * '?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½
	 * ?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½
	 */
	/*
	 * '?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½
	 * ?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½
	 */
	/* 'AI de los NPC */
	/*
	 * '?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½
	 * ?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½
	 */
	/*
	 * '?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½
	 * ?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½
	 */
	/*
	 * '?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½
	 * ?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½?ï¿½
	 */

	public static void GuardiasAI(int NpcIndex, boolean DelCaos) {
		/* '*************************************************** */
		/* 'Autor: Unknown (orginal version) */
		/* 'Last Modification: 12/01/2010 (ZaMa) */
		/* '14/09/2009: ZaMa - Now npcs don't atack protected users. */
		/*
		 * '12/01/2010: ZaMa - Los npcs no atacan druidas mimetizados con npcs
		 */
		/* '*************************************************** */
		Declaraciones.WorldPos nPos;
		int headingloop = 0;
		int UI = 0;
		boolean UserProtected = false;

		for (headingloop = (eHeading.NORTH); headingloop <= (eHeading.WEST); headingloop++) {
			nPos = Declaraciones.Npclist[NpcIndex].Pos;
			if (Declaraciones.Npclist[NpcIndex].flags.Inmovilizado == 0
					|| headingloop == Declaraciones.Npclist[NpcIndex].Char.heading) {
				Extra.HeadtoPos(headingloop, nPos);
				if (Extra.InMapBounds(nPos.Map, nPos.X, nPos.Y)) {
					UI = Declaraciones.MapData[nPos.Map][nPos.X][nPos.Y].UserIndex;
					if (UI > 0) {
						UserProtected = ! /* FIXME */modNuevoTimer.IntervaloPermiteSerAtacado(UI)
								&& Declaraciones.UserList[UI].flags.NoPuedeSerAtacado;
						UserProtected = UserProtected || Declaraciones.UserList[UI].flags.Ignorado
								|| Declaraciones.UserList[UI].flags.EnConsulta;

						if (Declaraciones.UserList[UI].flags.Muerto == 0
								&& Declaraciones.UserList[UI].flags.AdminPerseguible && ! /* FIXME */UserProtected) {
							/* 'ï¿½ES CRIMINAL? */
							if (! /* FIXME */DelCaos) {
								if (ES.criminal(UI)) {
									if (SistemaCombate.NpcAtacaUser(NpcIndex, UI)) {
										NPCs.ChangeNPCChar(NpcIndex, Declaraciones.Npclist[NpcIndex].Char.body,
												Declaraciones.Npclist[NpcIndex].Char.Head, headingloop);
									}
									return;
								} else if (Declaraciones.Npclist[NpcIndex].flags.AttackedBy == Declaraciones.UserList[UI].Name
										&& ! /* FIXME */Declaraciones.Npclist[NpcIndex].flags.Follow) {

									if (SistemaCombate.NpcAtacaUser(NpcIndex, UI)) {
										NPCs.ChangeNPCChar(NpcIndex, Declaraciones.Npclist[NpcIndex].Char.body,
												Declaraciones.Npclist[NpcIndex].Char.Head, headingloop);
									}
									return;
								}
							} else {
								if (! /* FIXME */ES.criminal(UI)) {
									if (SistemaCombate.NpcAtacaUser(NpcIndex, UI)) {
										NPCs.ChangeNPCChar(NpcIndex, Declaraciones.Npclist[NpcIndex].Char.body,
												Declaraciones.Npclist[NpcIndex].Char.Head, headingloop);
									}
									return;
								} else if (Declaraciones.Npclist[NpcIndex].flags.AttackedBy == Declaraciones.UserList[UI].Name
										&& ! /* FIXME */Declaraciones.Npclist[NpcIndex].flags.Follow) {

									if (SistemaCombate.NpcAtacaUser(NpcIndex, UI)) {
										NPCs.ChangeNPCChar(NpcIndex, Declaraciones.Npclist[NpcIndex].Char.body,
												Declaraciones.Npclist[NpcIndex].Char.Head, headingloop);
									}
									return;
								}
							}
						}
					}
				}
				/* 'not inmovil */
			}
		}

		RestoreOldMovement(NpcIndex);
	}

	/* '' */
	/* ' Handles the evil npcs' artificial intelligency. */
	/* ' */
	/* ' @param NpcIndex Specifies reference to the npc */
	public static void HostilMalvadoAI(int NpcIndex) {
		/* '************************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modify Date: 12/01/2010 (ZaMa) */
		/*
		 * '28/04/2009: ZaMa - Now those NPCs who doble attack, have 50% of
		 * posibility of casting a spell on user.
		 */
		/* '14/09/200*: ZaMa - Now npcs don't atack protected users. */
		/*
		 * '12/01/2010: ZaMa - Los npcs no atacan druidas mimetizados con npcs
		 */
		/* '************************************************************** */
		Declaraciones.WorldPos nPos;
		int headingloop = 0;
		int UI = 0;
		int NPCI = 0;
		boolean atacoPJ = false;
		boolean UserProtected = false;

		atacoPJ = false;

		for (headingloop = (eHeading.NORTH); headingloop <= (eHeading.WEST); headingloop++) {
			nPos = Declaraciones.Npclist[NpcIndex].Pos;
			if (Declaraciones.Npclist[NpcIndex].flags.Inmovilizado == 0
					|| Declaraciones.Npclist[NpcIndex].Char.heading == headingloop) {
				Extra.HeadtoPos(headingloop, nPos);
				if (Extra.InMapBounds(nPos.Map, nPos.X, nPos.Y)) {
					UI = Declaraciones.MapData[nPos.Map][nPos.X][nPos.Y].UserIndex;
					NPCI = Declaraciones.MapData[nPos.Map][nPos.X][nPos.Y].NpcIndex;
					if (UI > 0 && ! /* FIXME */atacoPJ) {
						UserProtected = ! /* FIXME */modNuevoTimer.IntervaloPermiteSerAtacado(UI)
								&& Declaraciones.UserList[UI].flags.NoPuedeSerAtacado;
						UserProtected = UserProtected || Declaraciones.UserList[UI].flags.Ignorado
								|| Declaraciones.UserList[UI].flags.EnConsulta;

						if (Declaraciones.UserList[UI].flags.Muerto == 0
								&& Declaraciones.UserList[UI].flags.AdminPerseguible && (! /* FIXME */UserProtected)) {

							atacoPJ = true;
							if (Declaraciones.Npclist[NpcIndex].Movement == NpcObjeto) {
								/*
								 * ' Los npc objeto no atacan siempre al mismo
								 * usuario
								 */
								if (Matematicas.RandomNumber(1, 3) == 3) {
									atacoPJ = false;
								}
							}

							if (atacoPJ) {
								if (Declaraciones.Npclist[NpcIndex].flags.LanzaSpells) {
									if (Declaraciones.Npclist[NpcIndex].flags.AtacaDoble) {
										if ((Matematicas.RandomNumber(0, 1))) {
											if (SistemaCombate.NpcAtacaUser(NpcIndex, UI)) {
												NPCs.ChangeNPCChar(NpcIndex, Declaraciones.Npclist[NpcIndex].Char.body,
														Declaraciones.Npclist[NpcIndex].Char.Head, headingloop);
											}
											return;
										}
									}

									NPCs.ChangeNPCChar(NpcIndex, Declaraciones.Npclist[NpcIndex].Char.body,
											Declaraciones.Npclist[NpcIndex].Char.Head, headingloop);
									NpcLanzaUnSpell(NpcIndex, UI);
								}
							}
							if (SistemaCombate.NpcAtacaUser(NpcIndex, UI)) {
								NPCs.ChangeNPCChar(NpcIndex, Declaraciones.Npclist[NpcIndex].Char.body,
										Declaraciones.Npclist[NpcIndex].Char.Head, headingloop);
							}
							return;

						}
					} else if (NPCI > 0) {
						if (Declaraciones.Npclist[NPCI].MaestroUser > 0
								&& Declaraciones.Npclist[NPCI].flags.Paralizado == 0) {
							NPCs.ChangeNPCChar(NpcIndex, Declaraciones.Npclist[NpcIndex].Char.body,
									Declaraciones.Npclist[NpcIndex].Char.Head, headingloop);
							SistemaCombate.NpcAtacaNpc(NpcIndex, NPCI, false);
							return;
						}
					}
				}
				/* 'inmo */
			}
		}

		RestoreOldMovement(NpcIndex);
	}

	public static void HostilBuenoAI(int NpcIndex) {
		/* '*************************************************** */
		/* 'Autor: Unknown (orginal version) */
		/* 'Last Modification: 12/01/2010 (ZaMa) */
		/* '14/09/2009: ZaMa - Now npcs don't atack protected users. */
		/*
		 * '12/01/2010: ZaMa - Los npcs no atacan druidas mimetizados con npcs
		 */
		/* '*************************************************** */
		Declaraciones.WorldPos nPos;
		eHeading headingloop;
		int UI = 0;
		boolean UserProtected = false;

		for (headingloop = (eHeading.NORTH); headingloop <= (eHeading.WEST); headingloop++) {
			nPos = Declaraciones.Npclist[NpcIndex].Pos;
			if (Declaraciones.Npclist[NpcIndex].flags.Inmovilizado == 0
					|| Declaraciones.Npclist[NpcIndex].Char.heading == headingloop) {
				Extra.HeadtoPos(headingloop, nPos);
				if (Extra.InMapBounds(nPos.Map, nPos.X, nPos.Y)) {
					UI = Declaraciones.MapData[nPos.Map][nPos.X][nPos.Y].UserIndex;
					if (UI > 0) {
						if (Declaraciones.UserList[UI].Name == Declaraciones.Npclist[NpcIndex].flags.AttackedBy) {

							UserProtected = ! /* FIXME */modNuevoTimer.IntervaloPermiteSerAtacado(UI)
									&& Declaraciones.UserList[UI].flags.NoPuedeSerAtacado;
							UserProtected = UserProtected || Declaraciones.UserList[UI].flags.Ignorado
									|| Declaraciones.UserList[UI].flags.EnConsulta;

							if (Declaraciones.UserList[UI].flags.Muerto == 0
									&& Declaraciones.UserList[UI].flags.AdminPerseguible
									&& ! /* FIXME */UserProtected) {
								if (Declaraciones.Npclist[NpcIndex].flags.LanzaSpells > 0) {
									NpcLanzaUnSpell(NpcIndex, UI);
								}

								if (SistemaCombate.NpcAtacaUser(NpcIndex, UI)) {
									NPCs.ChangeNPCChar(NpcIndex, Declaraciones.Npclist[NpcIndex].Char.body,
											Declaraciones.Npclist[NpcIndex].Char.Head, headingloop);
								}
								return;
							}
						}
					}
				}
			}
		}

		RestoreOldMovement(NpcIndex);
	}

	public static void IrUsuarioCercano(int NpcIndex) {
		/* '*************************************************** */
		/* 'Autor: Unknown (orginal version) */
		/* 'Last Modification: 25/07/2010 (ZaMa) */
		/* '14/09/2009: ZaMa - Now npcs don't follow protected users. */
		/*
		 * '12/01/2010: ZaMa - Los npcs no atacan druidas mimetizados con npcs
		 */
		/*
		 * '25/07/2010: ZaMa - Agrego una validacion temporal para evitar que
		 * los npcs ataquen a usuarios de mapas difernetes.
		 */
		/* '*************************************************** */
		int tHeading = 0;
		int UserIndex = 0;
		int SignoNS = 0;
		int SignoEO = 0;
		int i = 0;
		boolean UserProtected = false;

		if (Declaraciones.Npclist[NpcIndex].flags.Inmovilizado == 1) {
			switch (Declaraciones.Npclist[NpcIndex].Char.heading) {
			case NORTH:
				SignoNS = -1;
				SignoEO = 0;

				break;

			case EAST:
				SignoNS = 0;
				SignoEO = 1;

				break;

			case SOUTH:
				SignoNS = 1;
				SignoEO = 0;

				break;

			case WEST:
				SignoEO = -1;
				SignoNS = 0;
				break;
			}

			for (i = (1); i <= (ModAreas.ConnGroups[Declaraciones.Npclist[NpcIndex].Pos.Map].CountEntrys); i++) {
				UserIndex = ModAreas.ConnGroups[Declaraciones.Npclist[NpcIndex].Pos.Map].UserEntrys[i];

				/* 'Is it in it's range of vision?? */
				if (vb6.Abs(Declaraciones.UserList[UserIndex].Pos.X
						- Declaraciones.Npclist[NpcIndex].Pos.X) <= AI.RANGO_VISION_X
						&& vb6.Sgn(Declaraciones.UserList[UserIndex].Pos.X
								- Declaraciones.Npclist[NpcIndex].Pos.X) == SignoEO) {
					if (vb6.Abs(Declaraciones.UserList[UserIndex].Pos.Y
							- Declaraciones.Npclist[NpcIndex].Pos.Y) <= AI.RANGO_VISION_Y
							&& vb6.Sgn(Declaraciones.UserList[UserIndex].Pos.Y
									- Declaraciones.Npclist[NpcIndex].Pos.Y) == SignoNS) {

						UserProtected = ! /* FIXME */modNuevoTimer.IntervaloPermiteSerAtacado(UserIndex)
								&& Declaraciones.UserList[UserIndex].flags.NoPuedeSerAtacado;
						UserProtected = UserProtected || Declaraciones.UserList[UserIndex].flags.Ignorado
								|| Declaraciones.UserList[UserIndex].flags.EnConsulta;

						if (Declaraciones.UserList[UserIndex].flags.Muerto == 0) {
							if (! /* FIXME */UserProtected) {
								if (Declaraciones.Npclist[NpcIndex].flags.LanzaSpells != 0) {
									NpcLanzaUnSpell(NpcIndex, UserIndex);
								}
								return;
							}
						}

					}
				}
			}

			/* ' No esta inmobilizado */
		} else {

			/*
			 * ' Tiene prioridad de seguir al usuario al que le pertenece si
			 * esta en el rango de vision
			 */
			int OwnerIndex = 0;

			OwnerIndex = Declaraciones.Npclist[NpcIndex].Owner;
			if (OwnerIndex > 0) {

				/*
				 * ' TODO: Es temporal hatsa reparar un bug que hace que ataquen
				 * a usuarios de otros mapas
				 */
				if (Declaraciones.UserList[OwnerIndex].Pos.Map == Declaraciones.Npclist[NpcIndex].Pos.Map) {

					/* 'Is it in it's range of vision?? */
					if (vb6.Abs(Declaraciones.UserList[OwnerIndex].Pos.X
							- Declaraciones.Npclist[NpcIndex].Pos.X) <= AI.RANGO_VISION_X) {
						if (vb6.Abs(Declaraciones.UserList[OwnerIndex].Pos.Y
								- Declaraciones.Npclist[NpcIndex].Pos.Y) <= AI.RANGO_VISION_Y) {

							/* ' va hacia el si o esta invi ni oculto */
							if (Declaraciones.UserList[OwnerIndex].flags.invisible == 0
									&& Declaraciones.UserList[OwnerIndex].flags.Oculto == 0
									&& ! /* FIXME */Declaraciones.UserList[OwnerIndex].flags.EnConsulta
									&& ! /* FIXME */Declaraciones.UserList[OwnerIndex].flags.Ignorado) {
								if (Declaraciones.Npclist[NpcIndex].flags.LanzaSpells != 0) {
									NpcLanzaUnSpell(NpcIndex, OwnerIndex);
								}

								tHeading = Extra.FindDirection(Declaraciones.Npclist[NpcIndex].Pos,
										Declaraciones.UserList[OwnerIndex].Pos);
								NPCs.MoveNPCChar(NpcIndex, tHeading);
								return;
							}
						}
					}

					/*
					 * ' Esto significa que esta bugueado.. Lo logueo, y
					 * "reparo" el error a mano (Todo temporal)
					 */
				} else {
					General.LogError("El npc: " + Declaraciones.Npclist[NpcIndex].Name + "(" + NpcIndex
							+ "), intenta atacar a " + Declaraciones.UserList[OwnerIndex].Name + "(Index: " + OwnerIndex
							+ ", Mapa: " + Declaraciones.UserList[OwnerIndex].Pos.Map + ") desde el mapa "
							+ Declaraciones.Npclist[NpcIndex].Pos.Map);
					Declaraciones.Npclist[NpcIndex].Owner = 0;
				}

			}

			/*
			 * ' No le pertenece a nadie o el dueño no esta en el rango de
			 * vision, sigue a cualquiera
			 */
			for (i = (1); i <= (ModAreas.ConnGroups[Declaraciones.Npclist[NpcIndex].Pos.Map].CountEntrys); i++) {
				UserIndex = ModAreas.ConnGroups[Declaraciones.Npclist[NpcIndex].Pos.Map].UserEntrys[i];

				/* 'Is it in it's range of vision?? */
				if (vb6.Abs(Declaraciones.UserList[UserIndex].Pos.X
						- Declaraciones.Npclist[NpcIndex].Pos.X) <= AI.RANGO_VISION_X) {
					if (vb6.Abs(Declaraciones.UserList[UserIndex].Pos.Y
							- Declaraciones.Npclist[NpcIndex].Pos.Y) <= AI.RANGO_VISION_Y) {

						UserProtected = ! /* FIXME */modNuevoTimer.IntervaloPermiteSerAtacado(UserIndex)
								&& Declaraciones.UserList[UserIndex].flags.NoPuedeSerAtacado;
						UserProtected = UserProtected || Declaraciones.UserList[UserIndex].flags.Ignorado
								|| Declaraciones.UserList[UserIndex].flags.EnConsulta;

						if (Declaraciones.UserList[UserIndex].flags.Muerto == 0
								&& Declaraciones.UserList[UserIndex].flags.invisible == 0
								&& Declaraciones.UserList[UserIndex].flags.Oculto == 0
								&& Declaraciones.UserList[UserIndex].flags.AdminPerseguible
								&& ! /* FIXME */UserProtected) {

							if (Declaraciones.Npclist[NpcIndex].flags.LanzaSpells != 0) {
								NpcLanzaUnSpell(NpcIndex, UserIndex);
							}

							tHeading = Extra.FindDirection(Declaraciones.Npclist[NpcIndex].Pos,
									Declaraciones.UserList[UserIndex].Pos);
							NPCs.MoveNPCChar(NpcIndex, tHeading);
							return;
						}

					}
				}
			}

			/* 'Si llega aca es que no habï¿½a ningï¿½n usuario cercano vivo. */
			/* 'A bailar. Pablo (ToxicWaste) */
			if (Matematicas.RandomNumber(0, 10) == 0) {
				NPCs.MoveNPCChar(NpcIndex, vb6.CByte(Matematicas.RandomNumber(eHeading.NORTH, eHeading.WEST)));
			}

		}

		RestoreOldMovement(NpcIndex);
	}

	/* '' */
	/* ' Makes a Pet / Summoned Npc to Follow an enemy */
	/* ' */
	/* ' @param NpcIndex Specifies reference to the npc */
	public static void SeguirAgresor(int NpcIndex) {
		/* '************************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modify by: Marco Vanotti (MarKoxX) */
		/* 'Last Modify Date: 08/16/2008 */
		/*
		 * '08/16/2008: MarKoxX - Now pets that do melï¿½ attacks have to be
		 * near the enemy to attack.
		 */
		/* '************************************************************** */
		int tHeading = 0;
		int UI = 0;

		int i = 0;

		int SignoNS = 0;
		int SignoEO = 0;

		if (Declaraciones.Npclist[NpcIndex].flags.Paralizado == 1
				|| Declaraciones.Npclist[NpcIndex].flags.Inmovilizado == 1) {
			switch (Declaraciones.Npclist[NpcIndex].Char.heading) {
			case NORTH:
				SignoNS = -1;
				SignoEO = 0;

				break;

			case EAST:
				SignoNS = 0;
				SignoEO = 1;

				break;

			case SOUTH:
				SignoNS = 1;
				SignoEO = 0;

				break;

			case WEST:
				SignoEO = -1;
				SignoNS = 0;
				break;
			}

			for (i = (1); i <= (ModAreas.ConnGroups[Declaraciones.Npclist[NpcIndex].Pos.Map].CountEntrys); i++) {
				UI = ModAreas.ConnGroups[Declaraciones.Npclist[NpcIndex].Pos.Map].UserEntrys[i];

				/* 'Is it in it's range of vision?? */
				if (vb6.Abs(
						Declaraciones.UserList[UI].Pos.X - Declaraciones.Npclist[NpcIndex].Pos.X) <= AI.RANGO_VISION_X
						&& vb6.Sgn(
								Declaraciones.UserList[UI].Pos.X - Declaraciones.Npclist[NpcIndex].Pos.X) == SignoEO) {
					if (vb6.Abs(Declaraciones.UserList[UI].Pos.Y
							- Declaraciones.Npclist[NpcIndex].Pos.Y) <= AI.RANGO_VISION_Y
							&& vb6.Sgn(Declaraciones.UserList[UI].Pos.Y
									- Declaraciones.Npclist[NpcIndex].Pos.Y) == SignoNS) {

						if (Declaraciones.UserList[UI].Name == Declaraciones.Npclist[NpcIndex].flags.AttackedBy) {
							if (Declaraciones.Npclist[NpcIndex].MaestroUser > 0) {
								if (! /* FIXME */ES.criminal(Declaraciones.Npclist[NpcIndex].MaestroUser)
										&& ! /* FIXME */ES.criminal(UI)
										&& (Declaraciones.UserList[Declaraciones.Npclist[NpcIndex].MaestroUser].flags.Seguro
												|| Declaraciones.UserList[Declaraciones.Npclist[NpcIndex].MaestroUser].Faccion.ArmadaReal == 1)) {
									Protocol.WriteConsoleMsg(Declaraciones.Npclist[NpcIndex].MaestroUser,
											"La mascota no atacará a ciudadanos si eres miembro del ejército real o tienes el seguro activado.",
											FontTypeNames.FONTTYPE_INFO);
									Protocol.FlushBuffer(Declaraciones.Npclist[NpcIndex].MaestroUser);
									Declaraciones.Npclist[NpcIndex].flags.AttackedBy = "";
									return;
								}
							}

							if (Declaraciones.UserList[UI].flags.Muerto == 0
									&& Declaraciones.UserList[UI].flags.invisible == 0
									&& Declaraciones.UserList[UI].flags.Oculto == 0) {
								if (Declaraciones.Npclist[NpcIndex].flags.LanzaSpells > 0) {
									NpcLanzaUnSpell(NpcIndex, UI);
								} else {
									if (Matematicas.Distancia(Declaraciones.UserList[UI].Pos,
											Declaraciones.Npclist[NpcIndex].Pos) <= 1) {
										/*
										 * ' TODO : Set this a separate AI for
										 * Elementals and Druid's pets
										 */
										if (Declaraciones.Npclist[NpcIndex].Numero != 92) {
											SistemaCombate.NpcAtacaUser(NpcIndex, UI);
										}
									}
								}
								return;
							}
						}

					}
				}

			}
		} else {
			for (i = (1); i <= (ModAreas.ConnGroups[Declaraciones.Npclist[NpcIndex].Pos.Map].CountEntrys); i++) {
				UI = ModAreas.ConnGroups[Declaraciones.Npclist[NpcIndex].Pos.Map].UserEntrys[i];

				/* 'Is it in it's range of vision?? */
				if (vb6.Abs(Declaraciones.UserList[UI].Pos.X
						- Declaraciones.Npclist[NpcIndex].Pos.X) <= AI.RANGO_VISION_X) {
					if (vb6.Abs(Declaraciones.UserList[UI].Pos.Y
							- Declaraciones.Npclist[NpcIndex].Pos.Y) <= AI.RANGO_VISION_Y) {

						if (Declaraciones.UserList[UI].Name == Declaraciones.Npclist[NpcIndex].flags.AttackedBy) {
							if (Declaraciones.Npclist[NpcIndex].MaestroUser > 0) {
								if (! /* FIXME */ES.criminal(Declaraciones.Npclist[NpcIndex].MaestroUser)
										&& ! /* FIXME */ES.criminal(UI)
										&& (Declaraciones.UserList[Declaraciones.Npclist[NpcIndex].MaestroUser].flags.Seguro
												|| Declaraciones.UserList[Declaraciones.Npclist[NpcIndex].MaestroUser].Faccion.ArmadaReal == 1)) {
									Protocol.WriteConsoleMsg(Declaraciones.Npclist[NpcIndex].MaestroUser,
											"La mascota no atacará a ciudadanos si eres miembro del ejército real o tienes el seguro activado.",
											FontTypeNames.FONTTYPE_INFO);
									Protocol.FlushBuffer(Declaraciones.Npclist[NpcIndex].MaestroUser);
									Declaraciones.Npclist[NpcIndex].flags.AttackedBy = "";
									NPCs.FollowAmo(NpcIndex);
									return;
								}
							}

							if (Declaraciones.UserList[UI].flags.Muerto == 0
									&& Declaraciones.UserList[UI].flags.invisible == 0
									&& Declaraciones.UserList[UI].flags.Oculto == 0) {
								if (Declaraciones.Npclist[NpcIndex].flags.LanzaSpells > 0) {
									NpcLanzaUnSpell(NpcIndex, UI);
								} else {
									if (Matematicas.Distancia(Declaraciones.UserList[UI].Pos,
											Declaraciones.Npclist[NpcIndex].Pos) <= 1) {
										/*
										 * ' TODO : Set this a separate AI for
										 * Elementals and Druid's pets
										 */
										if (Declaraciones.Npclist[NpcIndex].Numero != 92) {
											SistemaCombate.NpcAtacaUser(NpcIndex, UI);
										}
									}
								}

								tHeading = Extra.FindDirection(Declaraciones.Npclist[NpcIndex].Pos,
										Declaraciones.UserList[UI].Pos);
								NPCs.MoveNPCChar(NpcIndex, tHeading);

								return;
							}
						}

					}
				}

			}
		}

		RestoreOldMovement(NpcIndex);
	}

	public static void RestoreOldMovement(int NpcIndex) {
		if (Declaraciones.Npclist[NpcIndex].MaestroUser == 0) {
			Declaraciones.Npclist[NpcIndex].Movement = Declaraciones.Npclist[NpcIndex].flags.OldMovement;
			Declaraciones.Npclist[NpcIndex].Hostile = Declaraciones.Npclist[NpcIndex].flags.OldHostil;
			Declaraciones.Npclist[NpcIndex].flags.AttackedBy = "";
		}
	}

	public static void PersigueCiudadano(int NpcIndex) {
		/* '*************************************************** */
		/* 'Autor: Unknown (orginal version) */
		/* 'Last Modification: 12/01/2010 (ZaMa) */
		/* '14/09/2009: ZaMa - Now npcs don't follow protected users. */
		/*
		 * '12/01/2010: ZaMa - Los npcs no atacan druidas mimetizados con npcs.
		 */
		/* '*************************************************** */
		int UserIndex = 0;
		int tHeading = 0;
		int i = 0;
		boolean UserProtected = false;

		for (i = (1); i <= (ModAreas.ConnGroups[Declaraciones.Npclist[NpcIndex].Pos.Map].CountEntrys); i++) {
			UserIndex = ModAreas.ConnGroups[Declaraciones.Npclist[NpcIndex].Pos.Map].UserEntrys[i];

			/* 'Is it in it's range of vision?? */
			if (vb6.Abs(Declaraciones.UserList[UserIndex].Pos.X
					- Declaraciones.Npclist[NpcIndex].Pos.X) <= AI.RANGO_VISION_X) {
				if (vb6.Abs(Declaraciones.UserList[UserIndex].Pos.Y
						- Declaraciones.Npclist[NpcIndex].Pos.Y) <= AI.RANGO_VISION_Y) {

					if (! /* FIXME */ES.criminal(UserIndex)) {

						UserProtected = ! /* FIXME */modNuevoTimer.IntervaloPermiteSerAtacado(UserIndex)
								&& Declaraciones.UserList[UserIndex].flags.NoPuedeSerAtacado;
						UserProtected = UserProtected || Declaraciones.UserList[UserIndex].flags.Ignorado
								|| Declaraciones.UserList[UserIndex].flags.EnConsulta;

						if (Declaraciones.UserList[UserIndex].flags.Muerto == 0
								&& Declaraciones.UserList[UserIndex].flags.invisible == 0
								&& Declaraciones.UserList[UserIndex].flags.Oculto == 0
								&& Declaraciones.UserList[UserIndex].flags.AdminPerseguible
								&& ! /* FIXME */UserProtected) {

							if (Declaraciones.Npclist[NpcIndex].flags.LanzaSpells > 0) {
								NpcLanzaUnSpell(NpcIndex, UserIndex);
							}
							tHeading = Extra.FindDirection(Declaraciones.Npclist[NpcIndex].Pos,
									Declaraciones.UserList[UserIndex].Pos);
							NPCs.MoveNPCChar(NpcIndex, tHeading);
							return;
						}
					}

				}
			}

		}

		RestoreOldMovement(NpcIndex);
	}

	public static void PersigueCriminal(int NpcIndex) {
		/* '*************************************************** */
		/* 'Autor: Unknown (orginal version) */
		/* 'Last Modification: 12/01/2010 (ZaMa) */
		/* '14/09/2009: ZaMa - Now npcs don't follow protected users. */
		/*
		 * '12/01/2010: ZaMa - Los npcs no atacan druidas mimetizados con npcs.
		 */
		/* '*************************************************** */
		int UserIndex = 0;
		int tHeading = 0;
		int i = 0;
		int SignoNS = 0;
		int SignoEO = 0;
		boolean UserProtected = false;

		if (Declaraciones.Npclist[NpcIndex].flags.Inmovilizado == 1) {
			switch (Declaraciones.Npclist[NpcIndex].Char.heading) {
			case NORTH:
				SignoNS = -1;
				SignoEO = 0;

				break;

			case EAST:
				SignoNS = 0;
				SignoEO = 1;

				break;

			case SOUTH:
				SignoNS = 1;
				SignoEO = 0;

				break;

			case WEST:
				SignoEO = -1;
				SignoNS = 0;
				break;
			}

			for (i = (1); i <= (ModAreas.ConnGroups[Declaraciones.Npclist[NpcIndex].Pos.Map].CountEntrys); i++) {
				UserIndex = ModAreas.ConnGroups[Declaraciones.Npclist[NpcIndex].Pos.Map].UserEntrys[i];

				/* 'Is it in it's range of vision?? */
				if (vb6.Abs(Declaraciones.UserList[UserIndex].Pos.X
						- Declaraciones.Npclist[NpcIndex].Pos.X) <= AI.RANGO_VISION_X
						&& vb6.Sgn(Declaraciones.UserList[UserIndex].Pos.X
								- Declaraciones.Npclist[NpcIndex].Pos.X) == SignoEO) {
					if (vb6.Abs(Declaraciones.UserList[UserIndex].Pos.Y
							- Declaraciones.Npclist[NpcIndex].Pos.Y) <= AI.RANGO_VISION_Y
							&& vb6.Sgn(Declaraciones.UserList[UserIndex].Pos.Y
									- Declaraciones.Npclist[NpcIndex].Pos.Y) == SignoNS) {

						if (ES.criminal(UserIndex)) {

							UserProtected = ! /* FIXME */modNuevoTimer.IntervaloPermiteSerAtacado(UserIndex)
									&& Declaraciones.UserList[UserIndex].flags.NoPuedeSerAtacado;
							UserProtected = UserProtected || Declaraciones.UserList[UserIndex].flags.Ignorado
									|| Declaraciones.UserList[UserIndex].flags.EnConsulta;

							if (Declaraciones.UserList[UserIndex].flags.Muerto == 0
									&& Declaraciones.UserList[UserIndex].flags.invisible == 0
									&& Declaraciones.UserList[UserIndex].flags.Oculto == 0
									&& Declaraciones.UserList[UserIndex].flags.AdminPerseguible
									&& ! /* FIXME */UserProtected) {

								if (Declaraciones.Npclist[NpcIndex].flags.LanzaSpells > 0) {
									NpcLanzaUnSpell(NpcIndex, UserIndex);
								}
								return;
							}
						}

					}
				}
			}
		} else {
			for (i = (1); i <= (ModAreas.ConnGroups[Declaraciones.Npclist[NpcIndex].Pos.Map].CountEntrys); i++) {
				UserIndex = ModAreas.ConnGroups[Declaraciones.Npclist[NpcIndex].Pos.Map].UserEntrys[i];

				/* 'Is it in it's range of vision?? */
				if (vb6.Abs(Declaraciones.UserList[UserIndex].Pos.X
						- Declaraciones.Npclist[NpcIndex].Pos.X) <= AI.RANGO_VISION_X) {
					if (vb6.Abs(Declaraciones.UserList[UserIndex].Pos.Y
							- Declaraciones.Npclist[NpcIndex].Pos.Y) <= AI.RANGO_VISION_Y) {

						if (ES.criminal(UserIndex)) {

							UserProtected = ! /* FIXME */modNuevoTimer.IntervaloPermiteSerAtacado(UserIndex)
									&& Declaraciones.UserList[UserIndex].flags.NoPuedeSerAtacado;
							UserProtected = UserProtected || Declaraciones.UserList[UserIndex].flags.Ignorado;

							if (Declaraciones.UserList[UserIndex].flags.Muerto == 0
									&& Declaraciones.UserList[UserIndex].flags.invisible == 0
									&& Declaraciones.UserList[UserIndex].flags.Oculto == 0
									&& Declaraciones.UserList[UserIndex].flags.AdminPerseguible
									&& ! /* FIXME */UserProtected) {
								if (Declaraciones.Npclist[NpcIndex].flags.LanzaSpells > 0) {
									NpcLanzaUnSpell(NpcIndex, UserIndex);
								}
								if (Declaraciones.Npclist[NpcIndex].flags.Inmovilizado == 1) {
									return;
								}
								tHeading = Extra.FindDirection(Declaraciones.Npclist[NpcIndex].Pos,
										Declaraciones.UserList[UserIndex].Pos);
								NPCs.MoveNPCChar(NpcIndex, tHeading);
								return;
							}
						}

					}
				}

			}
		}

		RestoreOldMovement(NpcIndex);
	}

	public static void SeguirAmo(int NpcIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int tHeading = 0;
		int UI = 0;

		if (Declaraciones.Npclist[NpcIndex].Target == 0 && Declaraciones.Npclist[NpcIndex].TargetNPC == 0) {
			UI = Declaraciones.Npclist[NpcIndex].MaestroUser;

			if (UI > 0) {
				/* 'Is it in it's range of vision?? */
				if (vb6.Abs(Declaraciones.UserList[UI].Pos.X
						- Declaraciones.Npclist[NpcIndex].Pos.X) <= AI.RANGO_VISION_X) {
					if (vb6.Abs(Declaraciones.UserList[UI].Pos.Y
							- Declaraciones.Npclist[NpcIndex].Pos.Y) <= AI.RANGO_VISION_Y) {
						if (Declaraciones.UserList[UI].flags.Muerto == 0
								&& Declaraciones.UserList[UI].flags.invisible == 0
								&& Declaraciones.UserList[UI].flags.Oculto == 0
								&& Matematicas.Distancia(Declaraciones.Npclist[NpcIndex].Pos,
										Declaraciones.UserList[UI].Pos) > 3) {
							tHeading = Extra.FindDirection(Declaraciones.Npclist[NpcIndex].Pos,
									Declaraciones.UserList[UI].Pos);
							NPCs.MoveNPCChar(NpcIndex, tHeading);
							return;
						}
					}
				}
			}
		}

		RestoreOldMovement(NpcIndex);
	}

	public static void AiNpcAtacaNpc(int NpcIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int tHeading = 0;
		int X = 0;
		int Y = 0;
		int NI = 0;
		boolean bNoEsta = false;

		int SignoNS = 0;
		int SignoEO = 0;

		if (Declaraciones.Npclist[NpcIndex].flags.Inmovilizado == 1) {
			switch (Declaraciones.Npclist[NpcIndex].Char.heading) {
			case NORTH:
				SignoNS = -1;
				SignoEO = 0;

				break;

			case EAST:
				SignoNS = 0;
				SignoEO = 1;

				break;

			case SOUTH:
				SignoNS = 1;
				SignoEO = 0;

				break;

			case WEST:
				SignoEO = -1;
				SignoNS = 0;
				break;
			}

			/* FIXME WEIRD FOR */
			for (Y = (Declaraciones.Npclist[NpcIndex].Pos.Y); ((IIf(SignoNS = 0, 1, SignoNS)) > 0)
					? (Y <= (Declaraciones.Npclist[NpcIndex].Pos.Y + SignoNS * AI.RANGO_VISION_Y))
					: (Y >= (Declaraciones.Npclist[NpcIndex].Pos.Y + SignoNS * AI.RANGO_VISION_Y)); Y = Y
							+ (IIf(SignoNS = 0, 1, SignoNS))) {
				/* FIXME WEIRD FOR */
				for (X = (Declaraciones.Npclist[NpcIndex].Pos.X); ((IIf(SignoEO = 0, 1, SignoEO)) > 0)
						? (X <= (Declaraciones.Npclist[NpcIndex].Pos.X + SignoEO * AI.RANGO_VISION_X))
						: (X >= (Declaraciones.Npclist[NpcIndex].Pos.X + SignoEO * AI.RANGO_VISION_X)); X = X
								+ (IIf(SignoEO = 0, 1, SignoEO))) {
					if (X >= Declaraciones.MinXBorder && X <= Declaraciones.MaxXBorder && Y >= Declaraciones.MinYBorder
							&& Y <= Declaraciones.MaxYBorder) {
						NI = Declaraciones.MapData[Declaraciones.Npclist[NpcIndex].Pos.Map][X][Y].NpcIndex;
						if (NI > 0) {
							if (Declaraciones.Npclist[NpcIndex].TargetNPC == NI) {
								bNoEsta = true;
								if (Declaraciones.Npclist[NpcIndex].Numero == AI.ELEMENTALFUEGO) {
									NpcLanzaUnSpellSobreNpc(NpcIndex, NI);
									if (Declaraciones.Npclist[NI].NPCtype == DRAGON) {
										Declaraciones.Npclist[NI].CanAttack = 1;
										NpcLanzaUnSpellSobreNpc(NI, NpcIndex);
									}
								} else {
									/*
									 * 'aca verificamosss la distancia de ataque
									 */
									if (Matematicas.Distancia(Declaraciones.Npclist[NpcIndex].Pos,
											Declaraciones.Npclist[NI].Pos) <= 1) {
										SistemaCombate.NpcAtacaNpc(NpcIndex, NI);
									}
								}
								return;
							}
						}
					}
				}
			}
		} else {
			for (Y = (Declaraciones.Npclist[NpcIndex].Pos.Y
					- AI.RANGO_VISION_Y); Y <= (Declaraciones.Npclist[NpcIndex].Pos.Y + AI.RANGO_VISION_Y); Y++) {
				for (X = (Declaraciones.Npclist[NpcIndex].Pos.X
						- AI.RANGO_VISION_Y); X <= (Declaraciones.Npclist[NpcIndex].Pos.X + AI.RANGO_VISION_Y); X++) {
					if (X >= Declaraciones.MinXBorder && X <= Declaraciones.MaxXBorder && Y >= Declaraciones.MinYBorder
							&& Y <= Declaraciones.MaxYBorder) {
						NI = Declaraciones.MapData[Declaraciones.Npclist[NpcIndex].Pos.Map][X][Y].NpcIndex;
						if (NI > 0) {
							if (Declaraciones.Npclist[NpcIndex].TargetNPC == NI) {
								bNoEsta = true;
								if (Declaraciones.Npclist[NpcIndex].Numero == AI.ELEMENTALFUEGO) {
									NpcLanzaUnSpellSobreNpc(NpcIndex, NI);
									if (Declaraciones.Npclist[NI].NPCtype == DRAGON) {
										Declaraciones.Npclist[NI].CanAttack = 1;
										NpcLanzaUnSpellSobreNpc(NI, NpcIndex);
									}
								} else {
									/*
									 * 'aca verificamosss la distancia de ataque
									 */
									if (Matematicas.Distancia(Declaraciones.Npclist[NpcIndex].Pos,
											Declaraciones.Npclist[NI].Pos) <= 1) {
										SistemaCombate.NpcAtacaNpc(NpcIndex, NI);
									}
								}
								if (Declaraciones.Npclist[NpcIndex].flags.Inmovilizado == 1) {
									return;
								}
								if (Declaraciones.Npclist[NpcIndex].TargetNPC == 0) {
									return;
								}
								tHeading = Extra.FindDirection(Declaraciones.Npclist[NpcIndex].Pos,
										Declaraciones.Npclist[Declaraciones.MapData[Declaraciones.Npclist[NpcIndex].Pos.Map][X][Y].NpcIndex].Pos);
								NPCs.MoveNPCChar(NpcIndex, tHeading);
								return;
							}
						}
					}
				}
			}
		}

		if (! /* FIXME */bNoEsta) {
			if (Declaraciones.Npclist[NpcIndex].MaestroUser > 0) {
				NPCs.FollowAmo(NpcIndex);
			} else {
				Declaraciones.Npclist[NpcIndex].Movement = Declaraciones.Npclist[NpcIndex].flags.OldMovement;
				Declaraciones.Npclist[NpcIndex].Hostile = Declaraciones.Npclist[NpcIndex].flags.OldHostil;
			}
		}
	}

	public static void AiNpcObjeto(int NpcIndex) {
		/* '*************************************************** */
		/* 'Autor: ZaMa */
		/* 'Last Modification: 14/09/2009 (ZaMa) */
		/* '14/09/2009: ZaMa - Now npcs don't follow protected users. */
		/* '*************************************************** */
		int UserIndex = 0;
		int i = 0;
		boolean UserProtected = false;

		for (i = (1); i <= (ModAreas.ConnGroups[Declaraciones.Npclist[NpcIndex].Pos.Map].CountEntrys); i++) {
			UserIndex = ModAreas.ConnGroups[Declaraciones.Npclist[NpcIndex].Pos.Map].UserEntrys[i];

			/* 'Is it in it's range of vision?? */
			if (vb6.Abs(Declaraciones.UserList[UserIndex].Pos.X
					- Declaraciones.Npclist[NpcIndex].Pos.X) <= AI.RANGO_VISION_X) {
				if (vb6.Abs(Declaraciones.UserList[UserIndex].Pos.Y
						- Declaraciones.Npclist[NpcIndex].Pos.Y) <= AI.RANGO_VISION_Y) {

					UserProtected = ! /* FIXME */modNuevoTimer.IntervaloPermiteSerAtacado(UserIndex)
							&& Declaraciones.UserList[UserIndex].flags.NoPuedeSerAtacado;

					if (Declaraciones.UserList[UserIndex].flags.Muerto == 0
							&& Declaraciones.UserList[UserIndex].flags.invisible == 0
							&& Declaraciones.UserList[UserIndex].flags.Oculto == 0
							&& Declaraciones.UserList[UserIndex].flags.AdminPerseguible && ! /* FIXME */UserProtected) {

						/* ' No quiero que ataque siempre al primero */
						if (Matematicas.RandomNumber(1, 3) < 3) {
							if (Declaraciones.Npclist[NpcIndex].flags.LanzaSpells > 0) {
								NpcLanzaUnSpell(NpcIndex, UserIndex);
							}

							return;
						}
					}
				}
			}

		}

	}

	public static void NPCAI(int NpcIndex) {
		/* '************************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modify by: ZaMa */
		/* 'Last Modify Date: 15/11/2009 */
		/*
		 * '08/16/2008: MarKoxX - Now pets that do melï¿½ attacks have to be
		 * near the enemy to attack.
		 */
		/* '15/11/2009: ZaMa - Implementacion de npc objetos ai. */
		/* '************************************************************** */
		/* FIXME: ON ERROR GOTO ErrorHandler */
		/* '<<<<<<<<<<< Ataques >>>>>>>>>>>>>>>> */
		if (Declaraciones.Npclist[NpcIndex].MaestroUser == 0) {
			/* 'Busca a alguien para atacar */
			/* 'ï¿½Es un guardia? */
			if (Declaraciones.Npclist[NpcIndex].NPCtype == eNPCType.GuardiaReal) {
				GuardiasAI(NpcIndex, false);
			} else if (Declaraciones.Npclist[NpcIndex].NPCtype == eNPCType.Guardiascaos) {
				GuardiasAI(NpcIndex, true);
			} else if (Declaraciones.Npclist[NpcIndex].Hostile
					&& Declaraciones.Npclist[NpcIndex].Stats.Alineacion != 0) {
				HostilMalvadoAI(NpcIndex);
			} else if (Declaraciones.Npclist[NpcIndex].Hostile
					&& Declaraciones.Npclist[NpcIndex].Stats.Alineacion == 0) {
				HostilBuenoAI(NpcIndex);
			}
		} else {
			/* 'Evitamos que ataque a su amo, a menos */
			/* 'que el amo lo ataque. */
			/* 'Call HostilBuenoAI(NpcIndex) */
		}

		/* '<<<<<<<<<<<Movimiento>>>>>>>>>>>>>>>> */
		switch (Declaraciones.Npclist[NpcIndex].Movement) {
		case MueveAlAzar:
			if (Declaraciones.Npclist[NpcIndex].flags.Inmovilizado == 1) {
				return;
			}
			if (Declaraciones.Npclist[NpcIndex].NPCtype == eNPCType.GuardiaReal) {
				if (Matematicas.RandomNumber(1, 12) == 3) {
					NPCs.MoveNPCChar(NpcIndex, vb6.CByte(Matematicas.RandomNumber(eHeading.NORTH, eHeading.WEST)));
				}

				PersigueCriminal(NpcIndex);

			} else if (Declaraciones.Npclist[NpcIndex].NPCtype == eNPCType.Guardiascaos) {
				if (Matematicas.RandomNumber(1, 12) == 3) {
					NPCs.MoveNPCChar(NpcIndex, vb6.CByte(Matematicas.RandomNumber(eHeading.NORTH, eHeading.WEST)));
				}

				PersigueCiudadano(NpcIndex);

			} else {
				if (Matematicas.RandomNumber(1, 12) == 3) {
					NPCs.MoveNPCChar(NpcIndex, vb6.CByte(Matematicas.RandomNumber(eHeading.NORTH, eHeading.WEST)));
				}
			}

			/* 'Va hacia el usuario cercano */
			break;

		case NpcMaloAtacaUsersBuenos:
			IrUsuarioCercano(NpcIndex);

			/* 'Va hacia el usuario que lo ataco(FOLLOW) */
			break;

		case NPCDEFENSA:
			SeguirAgresor(NpcIndex);

			/* 'Persigue criminales */
			break;

		case GuardiasAtacanCriminales:
			PersigueCriminal(NpcIndex);

			break;

		case SigueAmo:
			if (Declaraciones.Npclist[NpcIndex].flags.Inmovilizado == 1) {
				return;
			}
			SeguirAmo(NpcIndex);
			if (Matematicas.RandomNumber(1, 12) == 3) {
				NPCs.MoveNPCChar(NpcIndex, vb6.CByte(Matematicas.RandomNumber(eHeading.NORTH, eHeading.WEST)));
			}

			break;

		case NpcAtacaNpc:
			AiNpcAtacaNpc(NpcIndex);

			break;

		case NpcObjeto:
			AiNpcObjeto(NpcIndex);

			break;

		case NpcPathfinding:
			if (Declaraciones.Npclist[NpcIndex].flags.Inmovilizado == 1) {
				return;
			}
			if (ReCalculatePath(NpcIndex)) {
				PathFindingAI(NpcIndex);
				/* 'Existe el camino? */
				/* 'Si no existe nos movemos al azar */
				if (Declaraciones.Npclist[NpcIndex].PFINFO.NoPath) {
					/* 'Move randomly */
					NPCs.MoveNPCChar(NpcIndex, Matematicas.RandomNumber(eHeading.NORTH, eHeading.WEST));
				}
			} else {
				if (! /* FIXME */PathEnd(NpcIndex)) {
					FollowPath(NpcIndex);
				} else {
					Declaraciones.Npclist[NpcIndex].PFINFO.PathLenght = 0;
				}
			}
			break;
		}
		return;

		/* FIXME: ErrorHandler : */
		General.LogError("Error en NPCAI. Error: " + Err.Number + " - " + Err.description + ". " + "Npc: "
				+ Declaraciones.Npclist[NpcIndex].Name + ", Index: " + NpcIndex + ", MaestroUser: "
				+ Declaraciones.Npclist[NpcIndex].MaestroUser + ", MaestroNpc: "
				+ Declaraciones.Npclist[NpcIndex].MaestroNpc + ", Mapa: " + Declaraciones.Npclist[NpcIndex].Pos.Map
				+ " x:" + Declaraciones.Npclist[NpcIndex].Pos.X + " y:" + Declaraciones.Npclist[NpcIndex].Pos.Y
				+ " Mov:" + Declaraciones.Npclist[NpcIndex].Movement + " TargU:"
				+ Declaraciones.Npclist[NpcIndex].Target + " TargN:" + Declaraciones.Npclist[NpcIndex].TargetNPC);

		Declaraciones.npc MiNPC;
		MiNPC = Declaraciones.Npclist[NpcIndex];
		NPCs.QuitarNPC(NpcIndex);
		NPCs.ReSpawnNpc(MiNPC);
	}

	public static boolean UserNear(int NpcIndex) {
 boolean retval = false;
 /* '*************************************************** */
 /* 'Author: Unknown */
 /* 'Last Modification: - */
 /* 'Returns True if there is an user adjacent to the npc position. */
 /* '*************************************************** */
 
  retval = ! / * FIXME * /vb6.Int(Matematicas.Distance(Declaraciones.Npclist[NpcIndex].Pos.X, Declaraciones.Npclist[NpcIndex].Pos.Y, Declaraciones.UserList[Declaraciones.Npclist[NpcIndex].PFINFO.TargetUser].Pos.X, Declaraciones.UserList[Declaraciones.Npclist[NpcIndex].PFINFO.TargetUser].Pos.Y))>1;
return retval;
}

	public static boolean ReCalculatePath(int NpcIndex) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* 'Returns true if we have to seek a new path */
		/* '*************************************************** */

		if (Declaraciones.Npclist[NpcIndex].PFINFO.PathLenght == 0) {
			retval = true;
		} else if (! /* FIXME */UserNear(NpcIndex)
				&& Declaraciones.Npclist[NpcIndex].PFINFO.PathLenght == Declaraciones.Npclist[NpcIndex].PFINFO.CurPos
						- 1) {
			retval = true;
		}
		return retval;
	}

	public static boolean PathEnd(int NpcIndex) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Gulfas Morgolock */
		/* 'Last Modification: - */
		/* 'Returns if the npc has arrived to the end of its path */
		/* '*************************************************** */
		retval = Declaraciones.Npclist[NpcIndex].PFINFO.CurPos == Declaraciones.Npclist[NpcIndex].PFINFO.PathLenght;
		return retval;
	}

	public static boolean FollowPath(int NpcIndex) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Gulfas Morgolock */
		/* 'Last Modification: - */
		/* 'Moves the npc. */
		/* '*************************************************** */
		Declaraciones.WorldPos tmpPos;
		int tHeading = 0;

		tmpPos.Map = Declaraciones.Npclist[NpcIndex].Pos.Map;
		/* ' invertï¿½ las coordenadas */
		tmpPos.X = Declaraciones.Npclist[NpcIndex].PFINFO.Path[Declaraciones.Npclist[NpcIndex].PFINFO.CurPos].Y;
		tmpPos.Y = Declaraciones.Npclist[NpcIndex].PFINFO.Path[Declaraciones.Npclist[NpcIndex].PFINFO.CurPos].X;

		/* 'Debug.Print "(" & tmpPos.X & "," & tmpPos.Y & ")" */

		tHeading = Extra.FindDirection(Declaraciones.Npclist[NpcIndex].Pos, tmpPos);

		NPCs.MoveNPCChar(NpcIndex, tHeading);

		Declaraciones.Npclist[NpcIndex].PFINFO.CurPos = Declaraciones.Npclist[NpcIndex].PFINFO.CurPos + 1;
		return retval;
	}

	public static boolean PathFindingAI(int NpcIndex) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Gulfas Morgolock */
		/* 'Last Modification: - */
		/* 'This function seeks the shortest path from the Npc */
		/* 'to the user's location. */
		/* '*************************************************** */
		int Y = 0;
		int X = 0;

		/* 'Makes a loop that looks at */
		for (Y = (Declaraciones.Npclist[NpcIndex].Pos.Y - 10); Y <= (Declaraciones.Npclist[NpcIndex].Pos.Y + 10); Y++) {
			/* '5 tiles in every direction */
			for (X = (Declaraciones.Npclist[NpcIndex].Pos.X - 10); X <= (Declaraciones.Npclist[NpcIndex].Pos.X
					+ 10); X++) {

				/* 'Make sure tile is legal */
				if (X > Declaraciones.MinXBorder && X < Declaraciones.MaxXBorder && Y > Declaraciones.MinYBorder
						&& Y < Declaraciones.MaxYBorder) {

					/* 'look for a user */
					if (Declaraciones.MapData[Declaraciones.Npclist[NpcIndex].Pos.Map][X][Y].UserIndex > 0) {
						/* 'Move towards user */
						int tmpUserIndex = 0;
						tmpUserIndex = Declaraciones.MapData[Declaraciones.Npclist[NpcIndex].Pos.Map][X][Y].UserIndex;
						if (Declaraciones.UserList[tmpUserIndex].flags.Muerto == 0
								&& Declaraciones.UserList[tmpUserIndex].flags.invisible == 0
								&& Declaraciones.UserList[tmpUserIndex].flags.Oculto == 0
								&& Declaraciones.UserList[tmpUserIndex].flags.AdminPerseguible) {
							/*
							 * 'We have to invert the coordinates, this is
							 * because
							 */
							/*
							 * 'ORE refers to maps in converse way of my
							 * pathfinding
							 */
							/* 'routines. */
							Declaraciones.Npclist[NpcIndex].PFINFO.Target.X = Declaraciones.UserList[tmpUserIndex].Pos.Y;
							/* 'ops! */
							Declaraciones.Npclist[NpcIndex].PFINFO.Target.Y = Declaraciones.UserList[tmpUserIndex].Pos.X;
							Declaraciones.Npclist[NpcIndex].PFINFO.TargetUser = tmpUserIndex;
							PathFinding.SeekPath(NpcIndex);
							return retval;
						}
					}
				}
			}
		}
		return retval;
	}

	public static void NpcLanzaUnSpell(int NpcIndex, int UserIndex) {
		/* '************************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modify by: - */
		/* 'Last Modify Date: - */
		/* '************************************************************** */
		if (Declaraciones.UserList[UserIndex].flags.invisible == 1
				|| Declaraciones.UserList[UserIndex].flags.Oculto == 1) {
			return;
		}

		int k = 0;
		k = Matematicas.RandomNumber(1, Declaraciones.Npclist[NpcIndex].flags.LanzaSpells);
		modHechizos.NpcLanzaSpellSobreUser(NpcIndex, UserIndex, Declaraciones.Npclist[NpcIndex].Spells[k]);
	}

	public static void NpcLanzaUnSpellSobreNpc(int NpcIndex, int TargetNPC) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int k = 0;
		k = Matematicas.RandomNumber(1, Declaraciones.Npclist[NpcIndex].flags.LanzaSpells);
		modHechizos.NpcLanzaSpellSobreNpc(NpcIndex, TargetNPC, Declaraciones.Npclist[NpcIndex].Spells[k]);
	}

}
