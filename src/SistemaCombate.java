/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"SistemaCombate"')] */

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
/* ' */
/* 'Diseno y corrección del modulo de combate por */
/* 'Gerardo Saiz, gerardosaiz@yahoo.com */
/* ' */

/* '9/01/2008 Pablo (ToxicWaste) - Ahora TODOS los modificadores de Clase se controlan desde Balance.dat */

import enums.*;

public class SistemaCombate {

	static final int MAXDISTANCIAARCO = 18;
	static final int MAXDISTANCIAMAGIA = 18;

	static int MinimoInt(int a, int b) {
		int retval;
		if (a > b) {
			retval = b;
		} else {
			retval = a;
		}
		return retval;
	}

	static int MaximoInt(int a, int b) {
		int retval;
		if (a > b) {
			retval = a;
		} else {
			retval = b;
		}
		return retval;
	}

	static int PoderEvasionEscudo(int UserIndex) {
		int retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		retval = (Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Defensa]
				* Declaraciones.ModClase[Declaraciones.UserList[UserIndex].clase].Escudo) / 2;
		return retval;
	}

	static int PoderEvasion(int UserIndex) {
		int retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */
		int lTemp;
		lTemp = (Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Tacticas]
				+ Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Tacticas] / 33
						* Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Agilidad])
				* Declaraciones.ModClase[Declaraciones.UserList[UserIndex].clase].Evasion;

		retval = (lTemp + (2.5 * MaximoInt(Declaraciones.UserList[UserIndex].Stats.ELV - 12, 0)));
		return retval;
	}

	static int PoderAtaqueArma(int UserIndex) {
		int retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int PoderAtaqueTemp;

		if (Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Armas] < 31) {
			PoderAtaqueTemp = Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Armas]
					* Declaraciones.ModClase[Declaraciones.UserList[UserIndex].clase].AtaqueArmas;
		} else if (Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Armas] < 61) {
			PoderAtaqueTemp = (Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Armas]
					+ Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Agilidad])
					* Declaraciones.ModClase[Declaraciones.UserList[UserIndex].clase].AtaqueArmas;
		} else if (Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Armas] < 91) {
			PoderAtaqueTemp = (Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Armas]
					+ 2 * Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Agilidad])
					* Declaraciones.ModClase[Declaraciones.UserList[UserIndex].clase].AtaqueArmas;
		} else {
			PoderAtaqueTemp = (Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Armas]
					+ 3 * Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Agilidad])
					* Declaraciones.ModClase[Declaraciones.UserList[UserIndex].clase].AtaqueArmas;
		}

		retval = (PoderAtaqueTemp + (2.5 * MaximoInt(Declaraciones.UserList[UserIndex].Stats.ELV - 12, 0)));
		return retval;
	}

	static int PoderAtaqueProyectil(int UserIndex) {
		int retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int PoderAtaqueTemp;
		int SkillProyectiles;

		SkillProyectiles = Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Proyectiles];

		if (SkillProyectiles < 31) {
			PoderAtaqueTemp = SkillProyectiles
					* Declaraciones.ModClase[Declaraciones.UserList[UserIndex].clase].AtaqueProyectiles;
		} else if (SkillProyectiles < 61) {
			PoderAtaqueTemp = (SkillProyectiles
					+ Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Agilidad])
					* Declaraciones.ModClase[Declaraciones.UserList[UserIndex].clase].AtaqueProyectiles;
		} else if (SkillProyectiles < 91) {
			PoderAtaqueTemp = (SkillProyectiles
					+ 2 * Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Agilidad])
					* Declaraciones.ModClase[Declaraciones.UserList[UserIndex].clase].AtaqueProyectiles;
		} else {
			PoderAtaqueTemp = (SkillProyectiles
					+ 3 * Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Agilidad])
					* Declaraciones.ModClase[Declaraciones.UserList[UserIndex].clase].AtaqueProyectiles;
		}

		retval = (PoderAtaqueTemp + (2.5 * MaximoInt(Declaraciones.UserList[UserIndex].Stats.ELV - 12, 0)));
		return retval;
	}

	static int PoderAtaqueWrestling(int UserIndex) {
		int retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int PoderAtaqueTemp;
		int WrestlingSkill;

		WrestlingSkill = Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Wrestling];

		if (WrestlingSkill < 31) {
			PoderAtaqueTemp = WrestlingSkill
					* Declaraciones.ModClase[Declaraciones.UserList[UserIndex].clase].AtaqueWrestling;
		} else if (WrestlingSkill < 61) {
			PoderAtaqueTemp = (WrestlingSkill
					+ Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Agilidad])
					* Declaraciones.ModClase[Declaraciones.UserList[UserIndex].clase].AtaqueWrestling;
		} else if (WrestlingSkill < 91) {
			PoderAtaqueTemp = (WrestlingSkill
					+ 2 * Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Agilidad])
					* Declaraciones.ModClase[Declaraciones.UserList[UserIndex].clase].AtaqueWrestling;
		} else {
			PoderAtaqueTemp = (WrestlingSkill
					+ 3 * Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Agilidad])
					* Declaraciones.ModClase[Declaraciones.UserList[UserIndex].clase].AtaqueWrestling;
		}

		retval = (PoderAtaqueTemp + (2.5 * MaximoInt(Declaraciones.UserList[UserIndex].Stats.ELV - 12, 0)));
		return retval;
	}

	static boolean UserImpactoNpc(int UserIndex, int NpcIndex) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int PoderAtaque;
		int Arma;
		eSkill Skill;
		int ProbExito;

		Arma = Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex;

		/* 'Usando un arma */
		if (Arma > 0) {
			if (Declaraciones.ObjData[Arma].proyectil == 1) {
				PoderAtaque = PoderAtaqueProyectil(UserIndex);
				Skill = eSkill.Proyectiles;
			} else {
				PoderAtaque = PoderAtaqueArma(UserIndex);
				Skill = eSkill.Armas;
			}
			/* 'Peleando con punos */
		} else {
			PoderAtaque = PoderAtaqueWrestling(UserIndex);
			Skill = eSkill.Wrestling;
		}

		/* ' Chances are rounded */
		ProbExito = MaximoInt(10,
				MinimoInt(90, 50 + ((PoderAtaque - Declaraciones.Npclist[NpcIndex].PoderEvasion) * 0.4)));

		retval = (Matematicas.RandomNumber(1, 100) <= ProbExito);

		if (retval) {
			UsUaRiOs.SubirSkill(UserIndex, Skill, true);
		} else {
			UsUaRiOs.SubirSkill(UserIndex, Skill, false);
		}
		return retval;
	}

	static boolean NpcImpacto(int NpcIndex, int UserIndex) {
		boolean retval;
		/* '************************************************* */
		/* 'Author: Unknown */
		/* 'Last modified: 03/15/2006 */
		/* 'Revisa si un NPC logra impactar a un user o no */
		/*
		 * '03/15/2006 Maraxus - Evité una división por cero que eliminaba NPCs
		 */
		/* '************************************************* */
		boolean Rechazo;
		int ProbRechazo;
		int ProbExito;
		int UserEvasion;
		int NpcPoderAtaque;
		int PoderEvasioEscudo;
		int SkillTacticas;
		int SkillDefensa;

		UserEvasion = PoderEvasion(UserIndex);
		NpcPoderAtaque = Declaraciones.Npclist[NpcIndex].PoderAtaque;
		PoderEvasioEscudo = PoderEvasionEscudo(UserIndex);

		SkillTacticas = Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Tacticas];
		SkillDefensa = Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Defensa];

		/* 'Esta usando un escudo ??? */
		if (Declaraciones.UserList[UserIndex].Invent.EscudoEqpObjIndex > 0) {
			UserEvasion = UserEvasion + PoderEvasioEscudo;
		}

		/* ' Chances are rounded */
		ProbExito = MaximoInt(10, MinimoInt(90, 50 + ((NpcPoderAtaque - UserEvasion) * 0.4)));

		retval = (Matematicas.RandomNumber(1, 100) <= ProbExito);

		/* ' el usuario esta usando un escudo ??? */
		if (Declaraciones.UserList[UserIndex].Invent.EscudoEqpObjIndex > 0) {
			if (!retval) {
				/* 'Evitamos división por cero */
				if (SkillDefensa + SkillTacticas > 0) {
					/* ' Chances are rounded */
					ProbRechazo = MaximoInt(10, MinimoInt(90, 100 * SkillDefensa / (SkillDefensa + SkillTacticas)));
				} else {
					/* 'Si no tiene skills le dejamos el 10% mínimo */
					ProbRechazo = 10;
				}

				Rechazo = (Matematicas.RandomNumber(1, 100) <= ProbRechazo);

				if (Rechazo) {
					/* 'Se rechazo el ataque con el escudo */
					modSendData.SendData(SendTarget.ToPCArea, UserIndex,
							Protocol.PrepareMessagePlayWave(Declaraciones.SND_ESCUDO,
									Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y));
					/* 'Call WriteBlockedWithShieldUser(UserIndex) */
					Protocol.WriteMultiMessage(UserIndex, eMessages.BlockedWithShieldUser);
					UsUaRiOs.SubirSkill(UserIndex, eSkill.Defensa, true);
				} else {
					UsUaRiOs.SubirSkill(UserIndex, eSkill.Defensa, false);
				}
			}
		}
		return retval;
	}

	static int CalcularDano(int UserIndex) {
		return CalcularDano(UserIndex, 0);
	}

	static int CalcularDano(int UserIndex, int NpcIndex) {
		int retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 01/04/2010 (ZaMa) */
		/* '01/04/2010: ZaMa - Modifico el dano de wrestling. */
		/*
		 * '01/04/2010: ZaMa - Agrego bonificadores de wrestling para los
		 * guantes.
		 */
		/* '*************************************************** */
		int DanoArma;
		int DanoUsuario;
		Declaraciones.ObjData Arma;
		float ModifClase;
		Declaraciones.ObjData proyectil;
		int DanoMaxArma;
		int DanoMinArma;
		int ObjIndex;

		/* ''sacar esto si no queremos q la matadracos mate el Dragon si o si */
		boolean matoDragon;
		matoDragon = false;

		if (Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex > 0) {
			Arma = Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex];

			/* ' Ataca a un npc? */
			if (NpcIndex > 0) {
				if (Arma.proyectil == 1) {
					ModifClase = Declaraciones.ModClase[Declaraciones.UserList[UserIndex].clase].DanoProyectiles;
					DanoArma = Matematicas.RandomNumber(Arma.MinHIT, Arma.MaxHIT);
					DanoMaxArma = Arma.MaxHIT;

					if (Arma.Municion == 1) {
						proyectil = Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.MunicionEqpObjIndex];
						DanoArma = DanoArma + Matematicas.RandomNumber(proyectil.MinHIT, proyectil.MaxHIT);
						/* ' For some reason this isn't done... */
						/* 'DanoMaxArma = DanoMaxArma + proyectil.MaxHIT */
					}
				} else {
					ModifClase = Declaraciones.ModClase[Declaraciones.UserList[UserIndex].clase].DanoArmas;

					/* ' Usa la mata Dragones? */
					if (Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex == Declaraciones.EspadaMataDragonesIndex) {
						/* 'Ataca Dragon? */
						if (Declaraciones.Npclist[NpcIndex].NPCtype == DRAGON) {
							DanoArma = Matematicas.RandomNumber(Arma.MinHIT, Arma.MaxHIT);
							DanoMaxArma = Arma.MaxHIT;
							/*
							 * ''sacar esto si no queremos q la matadracos mate
							 * el Dragon si o si
							 */
							matoDragon = true;
							/* ' Sino es Dragon dano es 1 */
						} else {
							DanoArma = 1;
							DanoMaxArma = 1;
						}
					} else {
						DanoArma = Matematicas.RandomNumber(Arma.MinHIT, Arma.MaxHIT);
						DanoMaxArma = Arma.MaxHIT;
					}
				}
				/* ' Ataca usuario */
			} else {
				if (Arma.proyectil == 1) {
					ModifClase = Declaraciones.ModClase[Declaraciones.UserList[UserIndex].clase].DanoProyectiles;
					DanoArma = Matematicas.RandomNumber(Arma.MinHIT, Arma.MaxHIT);
					DanoMaxArma = Arma.MaxHIT;

					if (Arma.Municion == 1) {
						proyectil = Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.MunicionEqpObjIndex];
						DanoArma = DanoArma + Matematicas.RandomNumber(proyectil.MinHIT, proyectil.MaxHIT);
						/* ' For some reason this isn't done... */
						/* 'DanoMaxArma = DanoMaxArma + proyectil.MaxHIT */
					}
				} else {
					ModifClase = Declaraciones.ModClase[Declaraciones.UserList[UserIndex].clase].DanoArmas;

					if (Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex == Declaraciones.EspadaMataDragonesIndex) {
						ModifClase = Declaraciones.ModClase[Declaraciones.UserList[UserIndex].clase].DanoArmas;
						/* ' Si usa la espada mataDragones dano es 1 */
						DanoArma = 1;
						DanoMaxArma = 1;
					} else {
						DanoArma = Matematicas.RandomNumber(Arma.MinHIT, Arma.MaxHIT);
						DanoMaxArma = Arma.MaxHIT;
					}
				}
			}
		} else {
			ModifClase = Declaraciones.ModClase[Declaraciones.UserList[UserIndex].clase].DanoWrestling;

			/* ' Dano sin guantes */
			DanoMinArma = 4;
			DanoMaxArma = 9;

			/* ' Plus de guantes (en slot de anillo) */
			ObjIndex = Declaraciones.UserList[UserIndex].Invent.AnilloEqpObjIndex;
			if (ObjIndex > 0) {
				if (Declaraciones.ObjData[ObjIndex].Guante == 1) {
					DanoMinArma = DanoMinArma + Declaraciones.ObjData[ObjIndex].MinHIT;
					DanoMaxArma = DanoMaxArma + Declaraciones.ObjData[ObjIndex].MaxHIT;
				}
			}

			DanoArma = Matematicas.RandomNumber(DanoMinArma, DanoMaxArma);

		}

		DanoUsuario = Matematicas.RandomNumber(Declaraciones.UserList[UserIndex].Stats.MinHIT,
				Declaraciones.UserList[UserIndex].Stats.MaxHIT);

		/* ''sacar esto si no queremos q la matadracos mate el Dragon si o si */
		if (matoDragon) {
			retval = Declaraciones.Npclist[NpcIndex].Stats.MinHp + Declaraciones.Npclist[NpcIndex].Stats.def;
		} else {
			retval = (3 * DanoArma
					+ ((DanoMaxArma / 5) * MaximoInt(0,
							Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Fuerza] - 15))
					+ DanoUsuario) * ModifClase;
		}
		return retval;
	}

	static void UserDanoNpc(int UserIndex, int NpcIndex) {
 /* '*************************************************** */
 /* 'Author: Unknown */
 /* 'Last Modification: 07/04/2010 (Pato) */
 /* '25/01/2010: ZaMa - Agrego poder acuchillar npcs. */
 /* '07/04/2010: ZaMa - Los asesinos apunalan acorde al dano base sin descontar la defensa del npc. */
 /* '07/04/2010: Pato - Si se mata al dragón en party se loguean los miembros de la misma. */
 /* '11/07/2010: ZaMa - Ahora la defensa es solo ignorada para asesinos. */
 /* '*************************************************** */
 
 int dano;
 int DanoBase;
 int PI;
 int[] MembersOnline;
 String Text;
 int i;
 
 int BoatIndex;
 
 DanoBase = CalcularDano(UserIndex, NpcIndex);
 
 /* 'esta navegando? si es asi le sumamos el dano del barco */
  if (Declaraciones.UserList[UserIndex].flags.Navegando == 1) {
  
  BoatIndex = Declaraciones.UserList[UserIndex].Invent.BarcoObjIndex;
   if (BoatIndex>0) {
   DanoBase = DanoBase+Matematicas.RandomNumber(Declaraciones.ObjData[BoatIndex].MinHIT, Declaraciones.ObjData[BoatIndex].MaxHIT);
  }
 }
 
  dano = DanoBase-Declaraciones.Npclist[NpcIndex].Stats.def;
  
  if (dano<0) {
  dano = 0;
  }
  
  Protocol.WriteMultiMessage(UserIndex, eMessages.UserHitNPC, dano);
  CalcularDarExp(UserIndex, NpcIndex, dano);
  Declaraciones.Npclist[NpcIndex].Stats.MinHp = Declaraciones.Npclist[NpcIndex].Stats.MinHp-dano;
  
   if (Declaraciones.Npclist[NpcIndex].Stats.MinHp>0) {
   /* 'Trata de apunalar por la espalda al enemigo */
    if (UsUaRiOs.PuedeApunalar(UserIndex)) {
    
    /* ' La defensa se ignora solo en asesinos */
     if (Declaraciones.UserList[UserIndex].clase != eClass.Assasin) {
     DanoBase = dano;
    }
    
    Trabajo.DoApunalar(UserIndex, NpcIndex, 0, DanoBase);
    
   }
   
   /* 'trata de dar golpe crítico */
   Trabajo.DoGolpeCritico(UserIndex, NpcIndex, 0, dano);
   
    if (UsUaRiOs.PuedeAcuchillar(UserIndex)) {
    Trabajo.DoAcuchillar(UserIndex, NpcIndex, 0, dano);
   }
  }
  
   if (Declaraciones.Npclist[NpcIndex].Stats.MinHp<=0) {
   /* ' Si era un Dragon perdemos la espada mataDragones */
    if (Declaraciones.Npclist[NpcIndex].NPCtype == DRAGON) {
    /* 'Si tiene equipada la matadracos se la sacamos */
     if (Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex == Declaraciones.EspadaMataDragonesIndex) {
     Trabajo.QuitarObjetos(Declaraciones.EspadaMataDragonesIndex, 1, UserIndex);
    }
     if (Declaraciones.Npclist[NpcIndex].Stats.MaxHp>100000) {
     Text = Declaraciones.UserList[UserIndex].Name + " mató un dragón";
     PI = Declaraciones.UserList[UserIndex].PartyIndex;
     
      if (PI>0) {
      Declaraciones.Parties[PI].ObtenerMiembrosOnline(MembersOnline[]);
      Text = Text + " estando en party ";
      
       for (i = (1); i <= (mdParty.PARTY_MAXMEMBERS); i++) {
        if (MembersOnline[i]>0) {
        Text = Text + Declaraciones.UserList[MembersOnline[i]].Name + ", ";
       }
      }
      
      Text = vb6.Left(Text, vb6.Len(Text)-2) + ")";
     }
     
     General.LogDesarrollo(Text + ".");
    }
   }
   
   /* ' Para que las mascotas no sigan intentando luchar y */
   /* ' comiencen a seguir al amo */
    for (i = (1); i <= (Declaraciones.MAXMASCOTAS); i++) {
     if (Declaraciones.UserList[UserIndex].MascotasIndex[i]>0) {
      if (Declaraciones.Npclist[Declaraciones.UserList[UserIndex].MascotasIndex[i]].TargetNPC == NpcIndex) {
      Declaraciones.Npclist[Declaraciones.UserList[UserIndex].MascotasIndex[i]].TargetNPC = 0;
      Declaraciones.Npclist[Declaraciones.UserList[UserIndex].MascotasIndex[i]].Movement = TipoAI.SigueAmo;
     }
    }
   }
   
   NPCs.MuereNpc(NpcIndex, UserIndex);
  }
}

	static void NpcDano(int NpcIndex, int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 18/09/2010 (ZaMa) */
		/*
		 * '18/09/2010: ZaMa - Ahora se considera siempre la defensa del barco y
		 * el escudo.
		 */
		/* '*************************************************** */

		int dano;
		int Lugar;
		Declaraciones.ObjData Obj;

		int BoatDefense;
		int HeadDefense;
		int BodyDefense;

		int BoatIndex;
		int HelmetIndex;
		int ArmourIndex;
		int ShieldIndex;

		dano = Matematicas.RandomNumber(Declaraciones.Npclist[NpcIndex].Stats.MinHIT,
				Declaraciones.Npclist[NpcIndex].Stats.MaxHIT);

		/* ' Navega? */
		if (Declaraciones.UserList[UserIndex].flags.Navegando == 1) {
			/* ' En barca suma defensa */
			BoatIndex = Declaraciones.UserList[UserIndex].Invent.BarcoObjIndex;
			if (BoatIndex > 0) {
				Obj = Declaraciones.ObjData[BoatIndex];
				BoatDefense = Matematicas.RandomNumber(Obj.MinDef, Obj.MaxDef);
			}
		}

		Lugar = Matematicas.RandomNumber(PartesCuerpo.bCabeza, PartesCuerpo.bTorso);

		switch (Lugar) {

		case bCabeza:

			/* 'Si tiene casco absorbe el golpe */
			HelmetIndex = Declaraciones.UserList[UserIndex].Invent.CascoEqpObjIndex;
			if (HelmetIndex > 0) {
				Obj = Declaraciones.ObjData[HelmetIndex];
				HeadDefense = Matematicas.RandomNumber(Obj.MinDef, Obj.MaxDef);
			}

			break;

		default:

			int MinDef;
			int MaxDef;

			/* 'Si tiene armadura absorbe el golpe */
			ArmourIndex = Declaraciones.UserList[UserIndex].Invent.ArmourEqpObjIndex;
			if (ArmourIndex > 0) {
				Obj = Declaraciones.ObjData[ArmourIndex];
				MinDef = Obj.MinDef;
				MaxDef = Obj.MaxDef;
			}

			/* ' Si tiene casco absorbe el golpe */
			ShieldIndex = Declaraciones.UserList[UserIndex].Invent.EscudoEqpObjIndex;
			if (ShieldIndex > 0) {
				Obj = Declaraciones.ObjData[ShieldIndex];
				MinDef = MinDef + Obj.MinDef;
				MaxDef = MaxDef + Obj.MaxDef;
			}

			BodyDefense = Matematicas.RandomNumber(MinDef, MaxDef);

			break;
		}

		/* ' Dano final */
		dano = dano - HeadDefense - BodyDefense - BoatDefense;
		if (dano < 1) {
			dano = 1;
		}

		Protocol.WriteMultiMessage(UserIndex, eMessages.NPCHitUser, Lugar, dano);

		if (Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) {
			Declaraciones.UserList[UserIndex].Stats.MinHp = Declaraciones.UserList[UserIndex].Stats.MinHp - dano;
		}

		if (Declaraciones.UserList[UserIndex].flags.Meditando) {
			if (dano > vb6.Fix(Declaraciones.UserList[UserIndex].Stats.MinHp / 100
					* Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Inteligencia]
					* Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Meditar] / 100 * 12
					/ (Matematicas.RandomNumber(0, 5) + 7))) {
				Declaraciones.UserList[UserIndex].flags.Meditando = false;
				Protocol.WriteMeditateToggle(UserIndex);
				Protocol.WriteConsoleMsg(UserIndex, "Dejas de meditar.", FontTypeNames.FONTTYPE_INFO);
				Declaraciones.UserList[UserIndex].Char.FX = 0;
				Declaraciones.UserList[UserIndex].Char.loops = 0;
				modSendData.SendData(SendTarget.ToPCArea, UserIndex,
						Protocol.PrepareMessageCreateFX(Declaraciones.UserList[UserIndex].Char.CharIndex, 0, 0));
			}
		}

		/* 'Muere el usuario */
		if (Declaraciones.UserList[UserIndex].Stats.MinHp <= 0) {
			/* 'Le informamos que ha muerto ;) */
			Protocol.WriteMultiMessage(UserIndex, eMessages.NPCKillUser);

			/* 'Si lo mato un guardia */
			if (ES.criminal(UserIndex)) {
				if (Declaraciones.Npclist[NpcIndex].NPCtype == eNPCType.GuardiaReal) {
					RestarCriminalidad(UserIndex);
				}
			}

			if (Declaraciones.Npclist[NpcIndex].MaestroUser > 0) {
				AllFollowAmo(Declaraciones.Npclist[NpcIndex].MaestroUser);
			} else {
				/* 'Al matarlo no lo sigue mas */
				if (Declaraciones.Npclist[NpcIndex].Stats.Alineacion == 0) {
					Declaraciones.Npclist[NpcIndex].Movement = Declaraciones.Npclist[NpcIndex].flags.OldMovement;
					Declaraciones.Npclist[NpcIndex].Hostile = Declaraciones.Npclist[NpcIndex].flags.OldHostil;
					Declaraciones.Npclist[NpcIndex].flags.AttackedBy = "";
				}

			}

			UsUaRiOs.UserDie(UserIndex);
		}
	}

	static void RestarCriminalidad(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		boolean EraCriminal;
		EraCriminal = ES.criminal(UserIndex);

		if (Declaraciones.UserList[UserIndex].Reputacion.BandidoRep > 0) {
			Declaraciones.UserList[UserIndex].Reputacion.BandidoRep = Declaraciones.UserList[UserIndex].Reputacion.BandidoRep
					- Declaraciones.vlASALTO;
			if (Declaraciones.UserList[UserIndex].Reputacion.BandidoRep < 0) {
				Declaraciones.UserList[UserIndex].Reputacion.BandidoRep = 0;
			}
		} else if (Declaraciones.UserList[UserIndex].Reputacion.LadronesRep > 0) {
			Declaraciones.UserList[UserIndex].Reputacion.LadronesRep = Declaraciones.UserList[UserIndex].Reputacion.LadronesRep
					- (Declaraciones.vlCAZADOR * 10);
			if (Declaraciones.UserList[UserIndex].Reputacion.LadronesRep < 0) {
				Declaraciones.UserList[UserIndex].Reputacion.LadronesRep = 0;
			}
		}

		if (EraCriminal && !ES.criminal(UserIndex)) {

			if (Extra.esCaos(UserIndex)) {
				ModFacciones.ExpulsarFaccionCaos(UserIndex);
			}

			UsUaRiOs.RefreshCharStatus(UserIndex);
		}

	}

	static void CheckPets(int NpcIndex, int UserIndex) {
		CheckPets(NpcIndex, UserIndex, true);
	}

	static void CheckPets(int NpcIndex, int UserIndex, boolean CheckElementales) {
 /* '*************************************************** */
 /* 'Author: Unknown */
 /* 'Last Modification: 15/04/2010 */
 /* '15/04/2010: ZaMa - Las mascotas no se apropian de npcs. */
 /* '*************************************************** */
 
 int j;
 
 /* ' Si no tengo mascotas, para que cheaquear lo demas? */
 if (Declaraciones.UserList[UserIndex].NroMascotas == 0) {
 return;
 }
 
 if (!PuedeAtacarNPC(UserIndex, NpcIndex, , true)) {
 return;
 }
 
   for (j = (1); j <= (Declaraciones.MAXMASCOTAS); j++) {
    if (Declaraciones.UserList[UserIndex].MascotasIndex[j]>0) {
     if (Declaraciones.UserList[UserIndex].MascotasIndex[j] != NpcIndex) {
      if (CheckElementales || (Declaraciones.Npclist[Declaraciones.UserList[UserIndex].MascotasIndex[j]].Numero != AI.ELEMENTALFUEGO && Declaraciones.Npclist[Declaraciones.UserList[UserIndex].MascotasIndex[j]].Numero != AI.ELEMENTALTIERRA)) {
      
      if (Declaraciones.Npclist[Declaraciones.UserList[UserIndex].MascotasIndex[j]].TargetNPC == 0) {
      Declaraciones.Npclist[Declaraciones.UserList[UserIndex].MascotasIndex[j]].TargetNPC = NpcIndex;
      }
      Declaraciones.Npclist[Declaraciones.UserList[UserIndex].MascotasIndex[j]].Movement = TipoAI.NpcAtacaNpc;
     }
    }
   }
  }
}

	static void AllFollowAmo(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int j;

		for (j = (1); j <= (Declaraciones.MAXMASCOTAS); j++) {
			if (Declaraciones.UserList[UserIndex].MascotasIndex[j] > 0) {
				NPCs.FollowAmo(Declaraciones.UserList[UserIndex].MascotasIndex[j]);
			}
		}
	}

	static boolean NpcAtacaUser(int NpcIndex, int UserIndex) {
		boolean retval;
		/* '************************************************* */
		/* 'Author: Unknown */
		/* 'Last modified: - */
		/* ' */
		/* '************************************************* */

		if (Declaraciones.UserList[UserIndex].flags.AdminInvisible == 1) {
			return retval;
		}
		if ((!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) != 0
				&& !Declaraciones.UserList[UserIndex].flags.AdminPerseguible) {
			return retval;
		}

		/* ' El npc puede atacar ??? */
		if (Declaraciones.Npclist[NpcIndex].CanAttack == 1) {
			retval = true;
			CheckPets(NpcIndex, UserIndex, false);

			if (Declaraciones.Npclist[NpcIndex].Target == 0) {
				Declaraciones.Npclist[NpcIndex].Target = UserIndex;
			}

			if (Declaraciones.UserList[UserIndex].flags.AtacadoPorNpc == 0
					&& Declaraciones.UserList[UserIndex].flags.AtacadoPorUser == 0) {
				Declaraciones.UserList[UserIndex].flags.AtacadoPorNpc = NpcIndex;
			}
		} else {
			retval = false;
			return retval;
		}

		Declaraciones.Npclist[NpcIndex].CanAttack = 0;

		if (Declaraciones.Npclist[NpcIndex].flags.Snd1 > 0) {
			modSendData.SendData(SendTarget.ToNPCArea, NpcIndex,
					Protocol.PrepareMessagePlayWave(Declaraciones.Npclist[NpcIndex].flags.Snd1,
							Declaraciones.Npclist[NpcIndex].Pos.X, Declaraciones.Npclist[NpcIndex].Pos.Y));
		}

		if (NpcImpacto(NpcIndex, UserIndex)) {
			modSendData.SendData(SendTarget.ToPCArea, UserIndex,
					Protocol.PrepareMessagePlayWave(Declaraciones.SND_IMPACTO, Declaraciones.UserList[UserIndex].Pos.X,
							Declaraciones.UserList[UserIndex].Pos.Y));

			if (Declaraciones.UserList[UserIndex].flags.Meditando == false) {
				if (Declaraciones.UserList[UserIndex].flags.Navegando == 0) {
					modSendData.SendData(SendTarget.ToPCArea, UserIndex, Protocol.PrepareMessageCreateFX(
							Declaraciones.UserList[UserIndex].Char.CharIndex, Declaraciones.FXSANGRE, 0));
				}
			}

			NpcDano(NpcIndex, UserIndex);
			Protocol.WriteUpdateHP(UserIndex);

			/* '¿Puede envenenar? */
			if (Declaraciones.Npclist[NpcIndex].Veneno == 1) {
				NPCs.NpcEnvenenarUser(UserIndex);
			}

			UsUaRiOs.SubirSkill(UserIndex, eSkill.Tacticas, false);
		} else {
			Protocol.WriteMultiMessage(UserIndex, eMessages.NPCSwing);
			UsUaRiOs.SubirSkill(UserIndex, eSkill.Tacticas, true);
		}

		/* 'Controla el nivel del usuario */
		UsUaRiOs.CheckUserLevel(UserIndex);
		return retval;
	}

	static boolean NpcImpactoNpc(int Atacante, int Victima) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int PoderAtt;
		int PoderEva;
		int ProbExito;

		PoderAtt = Declaraciones.Npclist[Atacante].PoderAtaque;
		PoderEva = Declaraciones.Npclist[Victima].PoderEvasion;

		/* ' Chances are rounded */
		ProbExito = MaximoInt(10, MinimoInt(90, 50 + (PoderAtt - PoderEva) * 0.4));
		retval = (Matematicas.RandomNumber(1, 100) <= ProbExito);
		return retval;
	}

	static void NpcDanoNpc(int Atacante, int Victima) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int dano;
		int MasterIndex;

		dano = Matematicas.RandomNumber(Declaraciones.Npclist[Atacante].Stats.MinHIT,
				Declaraciones.Npclist[Atacante].Stats.MaxHIT);
		Declaraciones.Npclist[Victima].Stats.MinHp = Declaraciones.Npclist[Victima].Stats.MinHp - dano;

		if (Declaraciones.Npclist[Victima].Stats.MinHp < 1) {
			Declaraciones.Npclist[Atacante].Movement = Declaraciones.Npclist[Atacante].flags.OldMovement;

			if (vb6.LenB(Declaraciones.Npclist[Atacante].flags.AttackedBy) != 0) {
				Declaraciones.Npclist[Atacante].Hostile = Declaraciones.Npclist[Atacante].flags.OldHostil;
			}

			MasterIndex = Declaraciones.Npclist[Atacante].MaestroUser;
			if (MasterIndex > 0) {
				NPCs.FollowAmo(Atacante);
			}

			NPCs.MuereNpc(Victima, MasterIndex);
		}
	}

	static void NpcAtacaNpc(int Atacante, int Victima) {
		NpcAtacaNpc(Atacante, Victima, true);
	}

	static void NpcAtacaNpc(int Atacante, int Victima, boolean cambiarMOvimiento) {
		/* '************************************************* */
		/* 'Author: Unknown */
		/* 'Last modified: 01/03/2009 */
		/*
		 * '01/03/2009: ZaMa - Las mascotas no pueden atacar al rey si quedan
		 * pretorianos vivos.
		 */
		/*
		 * '23/05/2010: ZaMa - Ahora los elementales renuevan el tiempo de
		 * pertencia del npc que atacan si pertenece a su amo.
		 */
		/* '************************************************* */

		int MasterIndex;

		/* 'Es el Rey Preatoriano? */
		if (Declaraciones.Npclist[Victima].NPCtype == eNPCType.Pretoriano) {
			if (!Declaraciones.ClanPretoriano[Declaraciones.Npclist[Victima].ClanIndex].CanAtackMember(Victima)) {
				Protocol.WriteConsoleMsg(Declaraciones.Npclist[Atacante].MaestroUser,
						"Debes matar al resto del ejército antes de atacar al rey!", FontTypeNames.FONTTYPE_FIGHT);
				Declaraciones.Npclist[Atacante].TargetNPC = 0;
				return;
			}
		}

		/* ' El npc puede atacar ??? */
		if (Declaraciones.Npclist[Atacante].CanAttack == 1) {
			Declaraciones.Npclist[Atacante].CanAttack = 0;
			if (cambiarMOvimiento) {
				Declaraciones.Npclist[Victima].TargetNPC = Atacante;
				Declaraciones.Npclist[Victima].Movement = TipoAI.NpcAtacaNpc;
			}
		} else {
			return;
		}

		if (Declaraciones.Npclist[Atacante].flags.Snd1 > 0) {
			modSendData.SendData(SendTarget.ToNPCArea, Atacante,
					Protocol.PrepareMessagePlayWave(Declaraciones.Npclist[Atacante].flags.Snd1,
							Declaraciones.Npclist[Atacante].Pos.X, Declaraciones.Npclist[Atacante].Pos.Y));
		}

		MasterIndex = Declaraciones.Npclist[Atacante].MaestroUser;

		/* ' Tiene maestro? */
		if (MasterIndex > 0) {
			/* ' Su maestro es dueno del npc al que ataca? */
			if (Declaraciones.Npclist[Victima].Owner == MasterIndex) {
				/* ' Renuevo el timer de pertenencia */
				modNuevoTimer.IntervaloPerdioNpc(MasterIndex, true);
			}
		}

		if (NpcImpactoNpc(Atacante, Victima)) {
			if (Declaraciones.Npclist[Victima].flags.Snd2 > 0) {
				modSendData.SendData(SendTarget.ToNPCArea, Victima,
						Protocol.PrepareMessagePlayWave(Declaraciones.Npclist[Victima].flags.Snd2,
								Declaraciones.Npclist[Victima].Pos.X, Declaraciones.Npclist[Victima].Pos.Y));
			} else {
				modSendData.SendData(SendTarget.ToNPCArea, Victima,
						Protocol.PrepareMessagePlayWave(Declaraciones.SND_IMPACTO2,
								Declaraciones.Npclist[Victima].Pos.X, Declaraciones.Npclist[Victima].Pos.Y));
			}

			if (MasterIndex > 0) {
				modSendData.SendData(SendTarget.ToNPCArea, Atacante,
						Protocol.PrepareMessagePlayWave(Declaraciones.SND_IMPACTO,
								Declaraciones.Npclist[Atacante].Pos.X, Declaraciones.Npclist[Atacante].Pos.Y));
			} else {
				modSendData.SendData(SendTarget.ToNPCArea, Victima,
						Protocol.PrepareMessagePlayWave(Declaraciones.SND_IMPACTO, Declaraciones.Npclist[Victima].Pos.X,
								Declaraciones.Npclist[Victima].Pos.Y));
			}

			NpcDanoNpc(Atacante, Victima);
		} else {
			if (MasterIndex > 0) {
				modSendData.SendData(SendTarget.ToNPCArea, Atacante,
						Protocol.PrepareMessagePlayWave(Declaraciones.SND_SWING, Declaraciones.Npclist[Atacante].Pos.X,
								Declaraciones.Npclist[Atacante].Pos.Y));
			} else {
				modSendData.SendData(SendTarget.ToNPCArea, Victima,
						Protocol.PrepareMessagePlayWave(Declaraciones.SND_SWING, Declaraciones.Npclist[Victima].Pos.X,
								Declaraciones.Npclist[Victima].Pos.Y));
			}
		}
	}

	static boolean UsuarioAtacaNpc(int UserIndex, int NpcIndex) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 13/02/2011 (Amraphen) */
		/*
		 * '12/01/2010: ZaMa - Los druidas pierden la inmunidad de ser atacados
		 * por npcs cuando los atacan.
		 */
		/*
		 * '14/01/2010: ZaMa - Lo transformo en función, para que no se pierdan
		 * municiones al atacar targets inválidos.
		 */
		/*
		 * '13/02/2011: Amraphen - Ahora la stamina es quitada cuando
		 * efectivamente se ataca al NPC.
		 */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		if (!PuedeAtacarNPC(UserIndex, NpcIndex)) {
			return retval;
		}

		UsUaRiOs.NPCAtacado(NpcIndex, UserIndex);

		if (UserImpactoNpc(UserIndex, NpcIndex)) {
			if (Declaraciones.Npclist[NpcIndex].flags.Snd2 > 0) {
				modSendData.SendData(SendTarget.ToNPCArea, NpcIndex,
						Protocol.PrepareMessagePlayWave(Declaraciones.Npclist[NpcIndex].flags.Snd2,
								Declaraciones.Npclist[NpcIndex].Pos.X, Declaraciones.Npclist[NpcIndex].Pos.Y));
			} else {
				modSendData.SendData(SendTarget.ToPCArea, UserIndex,
						Protocol.PrepareMessagePlayWave(Declaraciones.SND_IMPACTO2,
								Declaraciones.Npclist[NpcIndex].Pos.X, Declaraciones.Npclist[NpcIndex].Pos.Y));
			}

			UserDanoNpc(UserIndex, NpcIndex);
		} else {
			modSendData.SendData(SendTarget.ToPCArea, UserIndex,
					Protocol.PrepareMessagePlayWave(Declaraciones.SND_SWING, Declaraciones.UserList[UserIndex].Pos.X,
							Declaraciones.UserList[UserIndex].Pos.Y));
			Protocol.WriteMultiMessage(UserIndex, eMessages.UserSwing);
		}

		/* 'Quitamos stamina */
		Trabajo.QuitarSta(UserIndex, Matematicas.RandomNumber(1, 10));

		/*
		 * ' Reveló su condición de usuario al atacar, los npcs lo van a atacar
		 */
		Declaraciones.UserList[UserIndex].flags.Ignorado = false;

		retval = true;

		return retval;

		/* FIXME: ErrHandler : */
		String UserName;

		if (UserIndex > 0) {
			UserName = Declaraciones.UserList[UserIndex].Name;
		}

		General.LogError("Error en UsuarioAtacaNpc. Error " + Err.Number + " : " + Err.description + ". User: "
				+ UserIndex + "-> " + UserName + ". NpcIndex: " + NpcIndex + ".");

		return retval;
	}

	static void UsuarioAtaca(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 13/02/2011 (Amraphen) */
		/*
		 * '13/02/2011: Amraphen - Ahora se quita la stamina en el sub
		 * UsuarioAtacaNPC.
		 */
		/* '*************************************************** */

		int index;
		Declaraciones.WorldPos AttackPos;

		/* 'Check bow's interval */
		if (!modNuevoTimer.IntervaloPermiteUsarArcos(UserIndex, false)) {
			return;
		}

		/* 'Check Spell-Magic interval */
		if (!modNuevoTimer.IntervaloPermiteMagiaGolpe(UserIndex)) {
			/* 'Check Attack interval */
			if (!modNuevoTimer.IntervaloPermiteAtacar(UserIndex)) {
				return;
			}
		}

		/* 'Chequeamos que tenga por lo menos 10 de stamina. */
		if (Declaraciones.UserList[UserIndex].Stats.MinSta < 10) {
			if (Declaraciones.UserList[UserIndex].Genero == eGenero.Hombre) {
				Protocol.WriteConsoleMsg(UserIndex, "Estás muy cansado para luchar.", FontTypeNames.FONTTYPE_INFO);
			} else {
				Protocol.WriteConsoleMsg(UserIndex, "Estás muy cansada para luchar.", FontTypeNames.FONTTYPE_INFO);
			}
			return;
		}

		AttackPos = Declaraciones.UserList[UserIndex].Pos;
		Extra.HeadtoPos(Declaraciones.UserList[UserIndex].Char.heading, AttackPos);

		/* 'Exit if not legal */
		if (AttackPos.X < Declaraciones.XMinMapSize || AttackPos.X > Declaraciones.XMaxMapSize
				|| AttackPos.Y <= Declaraciones.YMinMapSize || AttackPos.Y > Declaraciones.YMaxMapSize) {
			modSendData.SendData(SendTarget.ToPCArea, UserIndex,
					Protocol.PrepareMessagePlayWave(Declaraciones.SND_SWING, Declaraciones.UserList[UserIndex].Pos.X,
							Declaraciones.UserList[UserIndex].Pos.Y));
			return;
		}

		index = Declaraciones.MapData[AttackPos.Map][AttackPos.X][AttackPos.Y].UserIndex;

		/* 'Look for user */
		if (index > 0) {
			UsuarioAtacaUsuario(UserIndex, index);
			Protocol.WriteUpdateUserStats(UserIndex);
			Protocol.WriteUpdateUserStats(index);
			return;
		}

		index = Declaraciones.MapData[AttackPos.Map][AttackPos.X][AttackPos.Y].NpcIndex;

		/* 'Look for NPC */
		if (index > 0) {
			if (Declaraciones.Npclist[index].Attackable) {
				if (Declaraciones.Npclist[index].MaestroUser > 0
						&& Declaraciones.MapInfo[Declaraciones.Npclist[index].Pos.Map].Pk == false) {
					Protocol.WriteConsoleMsg(UserIndex, "No puedes atacar mascotas en zona segura.",
							FontTypeNames.FONTTYPE_FIGHT);
					return;
				}

				UsuarioAtacaNpc(UserIndex, index);
			} else {
				Protocol.WriteConsoleMsg(UserIndex, "No puedes atacar a este NPC.", FontTypeNames.FONTTYPE_FIGHT);
			}

			Protocol.WriteUpdateUserStats(UserIndex);

			return;
		}

		modSendData.SendData(SendTarget.ToPCArea, UserIndex, Protocol.PrepareMessagePlayWave(Declaraciones.SND_SWING,
				Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y));
		Protocol.WriteUpdateUserStats(UserIndex);

		if (Declaraciones.UserList[UserIndex].Counters.Trabajando) {
			Declaraciones.UserList[UserIndex].Counters.Trabajando = Declaraciones.UserList[UserIndex].Counters.Trabajando
					- 1;
		}

		if (Declaraciones.UserList[UserIndex].Counters.Ocultando) {
			Declaraciones.UserList[UserIndex].Counters.Ocultando = Declaraciones.UserList[UserIndex].Counters.Ocultando
					- 1;
		}
	}

	static boolean UsuarioImpacto(int AtacanteIndex, int VictimaIndex) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 21/05/2010 */
		/* '21/05/2010: ZaMa - Evito division por cero. */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int ProbRechazo;
		boolean Rechazo;
		int ProbExito;
		int PoderAtaque;
		int UserPoderEvasion;
		int UserPoderEvasionEscudo;
		int Arma;
		int SkillTacticas;
		int SkillDefensa;
		int ProbEvadir;
		eSkill Skill;

		SkillTacticas = Declaraciones.UserList[VictimaIndex].Stats.UserSkills[eSkill.Tacticas];
		SkillDefensa = Declaraciones.UserList[VictimaIndex].Stats.UserSkills[eSkill.Defensa];

		Arma = Declaraciones.UserList[AtacanteIndex].Invent.WeaponEqpObjIndex;

		/* 'Calculamos el poder de evasion... */
		UserPoderEvasion = PoderEvasion(VictimaIndex);

		if (Declaraciones.UserList[VictimaIndex].Invent.EscudoEqpObjIndex > 0) {
			UserPoderEvasionEscudo = PoderEvasionEscudo(VictimaIndex);
			UserPoderEvasion = UserPoderEvasion + UserPoderEvasionEscudo;
		} else {
			UserPoderEvasionEscudo = 0;
		}

		/* 'Esta usando un arma ??? */
		if (Declaraciones.UserList[AtacanteIndex].Invent.WeaponEqpObjIndex > 0) {
			if (Declaraciones.ObjData[Arma].proyectil == 1) {
				PoderAtaque = PoderAtaqueProyectil(AtacanteIndex);
				Skill = eSkill.Proyectiles;
			} else {
				PoderAtaque = PoderAtaqueArma(AtacanteIndex);
				Skill = eSkill.Armas;
			}
		} else {
			PoderAtaque = PoderAtaqueWrestling(AtacanteIndex);
			Skill = eSkill.Wrestling;
		}

		/* ' Chances are rounded */
		ProbExito = MaximoInt(10, MinimoInt(90, 50 + (PoderAtaque - UserPoderEvasion) * 0.4));

		/* ' Se reduce la evasion un 25% */
		if (Declaraciones.UserList[VictimaIndex].flags.Meditando) {
			ProbEvadir = (100 - ProbExito) * 0.75;
			ProbExito = MinimoInt(90, 100 - ProbEvadir);
		}

		retval = (Matematicas.RandomNumber(1, 100) <= ProbExito);

		/* ' el usuario esta usando un escudo ??? */
		if (Declaraciones.UserList[VictimaIndex].Invent.EscudoEqpObjIndex > 0) {
			/* 'Fallo ??? */
			if (!retval) {

				int SumaSkills;

				/* ' Para evitar division por 0 */
				SumaSkills = MaximoInt(1, SkillDefensa + SkillTacticas);

				/* ' Chances are rounded */
				ProbRechazo = MaximoInt(10, MinimoInt(90, 100 * SkillDefensa / SumaSkills));
				Rechazo = (Matematicas.RandomNumber(1, 100) <= ProbRechazo);
				if (Rechazo) {
					/* 'Se rechazo el ataque con el escudo */
					modSendData.SendData(SendTarget.ToPCArea, VictimaIndex,
							Protocol.PrepareMessagePlayWave(Declaraciones.SND_ESCUDO,
									Declaraciones.UserList[VictimaIndex].Pos.X,
									Declaraciones.UserList[VictimaIndex].Pos.Y));

					Protocol.WriteMultiMessage(AtacanteIndex, eMessages.BlockedWithShieldother);
					Protocol.WriteMultiMessage(VictimaIndex, eMessages.BlockedWithShieldUser);

					UsUaRiOs.SubirSkill(VictimaIndex, eSkill.Defensa, true);
				} else {
					UsUaRiOs.SubirSkill(VictimaIndex, eSkill.Defensa, false);
				}
			}
		}

		if (!retval) {
			UsUaRiOs.SubirSkill(AtacanteIndex, Skill, false);
		}

		Protocol.FlushBuffer(VictimaIndex);

		return retval;

		/* FIXME: ErrHandler : */
		String AtacanteNick;
		String VictimaNick;

		if (AtacanteIndex > 0) {
			AtacanteNick = Declaraciones.UserList[AtacanteIndex].Name;
		}
		if (VictimaIndex > 0) {
			VictimaNick = Declaraciones.UserList[VictimaIndex].Name;
		}

		General.LogError("Error en UsuarioImpacto. Error " + Err.Number + " : " + Err.description + " AtacanteIndex: "
				+ AtacanteIndex + " Nick: " + AtacanteNick + " VictimaIndex: " + VictimaIndex + " Nick: "
				+ VictimaNick);
		return retval;
	}

	static boolean UsuarioAtacaUsuario(int AtacanteIndex, int VictimaIndex) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 14/01/2010 (ZaMa) */
		/*
		 * '14/01/2010: ZaMa - Lo transformo en función, para que no se pierdan
		 * municiones al atacar targets
		 */
		/* ' inválidos, y evitar un doble chequeo innecesario */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		if (!PuedeAtacar(AtacanteIndex, VictimaIndex)) {
			return retval;
		}

		if (Matematicas.Distancia(Declaraciones.UserList[AtacanteIndex].Pos,
				Declaraciones.UserList[VictimaIndex].Pos) > SistemaCombate.MAXDISTANCIAARCO) {
			Protocol.WriteConsoleMsg(AtacanteIndex, "Estás muy lejos para disparar.", FontTypeNames.FONTTYPE_FIGHT);
			return retval;
		}

		UsuarioAtacadoPorUsuario(AtacanteIndex, VictimaIndex);

		if (UsuarioImpacto(AtacanteIndex, VictimaIndex)) {
			modSendData.SendData(SendTarget.ToPCArea, AtacanteIndex,
					Protocol.PrepareMessagePlayWave(Declaraciones.SND_IMPACTO,
							Declaraciones.UserList[AtacanteIndex].Pos.X, Declaraciones.UserList[AtacanteIndex].Pos.Y));

			if (Declaraciones.UserList[VictimaIndex].flags.Navegando == 0) {
				modSendData.SendData(SendTarget.ToPCArea, VictimaIndex, Protocol.PrepareMessageCreateFX(
						Declaraciones.UserList[VictimaIndex].Char.CharIndex, Declaraciones.FXSANGRE, 0));
			}

			/* 'Pablo (ToxicWaste): Guantes de Hurto del Bandido en acción */
			if (Declaraciones.UserList[AtacanteIndex].clase == eClass.Bandit) {
				Trabajo.DoDesequipar(AtacanteIndex, VictimaIndex);

				/* 'y ahora, el ladrón puede llegar a paralizar con el golpe. */
			} else if (Declaraciones.UserList[AtacanteIndex].clase == eClass.Thief) {
				Trabajo.DoHandInmo(AtacanteIndex, VictimaIndex);
			}

			UsUaRiOs.SubirSkill(VictimaIndex, eSkill.Tacticas, false);
			UserDanoUser(AtacanteIndex, VictimaIndex);
		} else {
			/*
			 * ' Invisible admins doesn't make sound to other clients except
			 * itself
			 */
			if (Declaraciones.UserList[AtacanteIndex].flags.AdminInvisible == 1) {
				TCP.EnviarDatosASlot(AtacanteIndex, Protocol.PrepareMessagePlayWave(Declaraciones.SND_SWING,
						Declaraciones.UserList[AtacanteIndex].Pos.X, Declaraciones.UserList[AtacanteIndex].Pos.Y));
			} else {
				modSendData.SendData(SendTarget.ToPCArea, AtacanteIndex,
						Protocol.PrepareMessagePlayWave(Declaraciones.SND_SWING,
								Declaraciones.UserList[AtacanteIndex].Pos.X,
								Declaraciones.UserList[AtacanteIndex].Pos.Y));
			}

			Protocol.WriteMultiMessage(AtacanteIndex, eMessages.UserSwing);
			Protocol.WriteMultiMessage(VictimaIndex, eMessages.UserAttackedSwing, AtacanteIndex);
			UsUaRiOs.SubirSkill(VictimaIndex, eSkill.Tacticas, true);
		}

		if (Declaraciones.UserList[AtacanteIndex].clase == eClass.Thief) {
			Trabajo.Desarmar(AtacanteIndex, VictimaIndex);
		}

		retval = true;

		return retval;

		/* FIXME: ErrHandler : */
		General.LogError("Error en UsuarioAtacaUsuario. Error " + Err.Number + " : " + Err.description);
		return retval;
	}

	static void UserDanoUser(int AtacanteIndex, int VictimaIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 12/01/2010 (ZaMa) */
		/*
		 * '12/01/2010: ZaMa - Implemento armas arrojadizas y probabilidad de
		 * acuchillar.
		 */
		/*
		 * '11/03/2010: ZaMa - Ahora no cuenta la muerte si estaba en estado
		 * atacable, y no se vuelve criminal.
		 */
		/*
		 * '18/09/2010: ZaMa - Ahora se cosidera la defensa de los barcos
		 * siempre.
		 */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int dano;
		int Lugar;
		Declaraciones.ObjData Obj;

		int BoatDefense;
		int BodyDefense;
		int HeadDefense;
		int WeaponBoost;

		int BoatIndex;
		int WeaponIndex;
		int HelmetIndex;
		int ArmourIndex;
		int ShieldIndex;

		int BarcaIndex;
		int ArmaIndex;
		int CascoIndex;
		int ArmaduraIndex;

		dano = CalcularDano(AtacanteIndex);

		UserEnvenena(AtacanteIndex, VictimaIndex);

		/* ' Aumento de dano por barca (atacante) */
		if (Declaraciones.UserList[AtacanteIndex].flags.Navegando == 1) {

			BoatIndex = Declaraciones.UserList[AtacanteIndex].Invent.BarcoObjIndex;

			if (BoatIndex > 0) {
				Obj = Declaraciones.ObjData[BoatIndex];
				dano = dano + Matematicas.RandomNumber(Obj.MinHIT, Obj.MaxHIT);
			}

		}

		/* ' Aumento de defensa por barca (victima) */
		if (Declaraciones.UserList[VictimaIndex].flags.Navegando == 1) {

			BoatIndex = Declaraciones.UserList[VictimaIndex].Invent.BarcoObjIndex;

			if (BoatIndex > 0) {
				Obj = Declaraciones.ObjData[BoatIndex];
				BoatDefense = Matematicas.RandomNumber(Obj.MinDef, Obj.MaxDef);
			}

		}

		/* ' Refuerzo arma (atacante) */
		WeaponIndex = Declaraciones.UserList[AtacanteIndex].Invent.WeaponEqpObjIndex;
		if (WeaponIndex > 0) {
			WeaponBoost = Declaraciones.ObjData[WeaponIndex].Refuerzo;
		}

		Lugar = Matematicas.RandomNumber(PartesCuerpo.bCabeza, PartesCuerpo.bTorso);

		switch (Lugar) {

		case bCabeza:

			/* 'Si tiene casco absorbe el golpe */
			HelmetIndex = Declaraciones.UserList[VictimaIndex].Invent.CascoEqpObjIndex;
			if (HelmetIndex > 0) {
				Obj = Declaraciones.ObjData[HelmetIndex];
				HeadDefense = Matematicas.RandomNumber(Obj.MinDef, Obj.MaxDef);
			}

			break;

		default:

			int MinDef;
			int MaxDef;

			/* 'Si tiene armadura absorbe el golpe */
			ArmourIndex = Declaraciones.UserList[VictimaIndex].Invent.ArmourEqpObjIndex;
			if (ArmourIndex > 0) {
				Obj = Declaraciones.ObjData[ArmourIndex];
				MinDef = Obj.MinDef;
				MaxDef = Obj.MaxDef;
			}

			/* ' Si tiene escudo, tambien absorbe el golpe */
			ShieldIndex = Declaraciones.UserList[VictimaIndex].Invent.EscudoEqpObjIndex;
			if (ShieldIndex > 0) {
				Obj = Declaraciones.ObjData[ShieldIndex];
				MinDef = MinDef + Obj.MinDef;
				MaxDef = MaxDef + Obj.MaxDef;
			}

			BodyDefense = Matematicas.RandomNumber(MinDef, MaxDef);

			break;
		}

		dano = dano + WeaponBoost - HeadDefense - BodyDefense - BoatDefense;
		if (dano < 0) {
			dano = 1;
		}

		Protocol.WriteMultiMessage(AtacanteIndex, eMessages.UserHittedUser,
				Declaraciones.UserList[VictimaIndex].Char.CharIndex, Lugar, dano);
		Protocol.WriteMultiMessage(VictimaIndex, eMessages.UserHittedByUser,
				Declaraciones.UserList[AtacanteIndex].Char.CharIndex, Lugar, dano);

		Declaraciones.UserList[VictimaIndex].Stats.MinHp = Declaraciones.UserList[VictimaIndex].Stats.MinHp - dano;

		if (Declaraciones.UserList[AtacanteIndex].flags.Hambre == 0
				&& Declaraciones.UserList[AtacanteIndex].flags.Sed == 0) {
			/* 'Si usa un arma quizas suba "Combate con armas" */
			if (WeaponIndex > 0) {
				if (Declaraciones.ObjData[WeaponIndex].proyectil) {
					/* 'es un Arco. Sube Armas a Distancia */
					UsUaRiOs.SubirSkill(AtacanteIndex, eSkill.Proyectiles, true);

					/* ' Si acuchilla */
					if (UsUaRiOs.PuedeAcuchillar(AtacanteIndex)) {
						Trabajo.DoAcuchillar(AtacanteIndex, 0, VictimaIndex, dano);
					}
				} else {
					/* 'Sube combate con armas. */
					UsUaRiOs.SubirSkill(AtacanteIndex, eSkill.Armas, true);
				}
			} else {
				/* 'sino tal vez lucha libre */
				UsUaRiOs.SubirSkill(AtacanteIndex, eSkill.Wrestling, true);
			}

			/* 'Trata de apunalar por la espalda al enemigo */
			if (UsUaRiOs.PuedeApunalar(AtacanteIndex)) {
				Trabajo.DoApunalar(AtacanteIndex, 0, VictimaIndex, dano);
			}
			/* 'e intenta dar un golpe crítico [Pablo (ToxicWaste)] */
			Trabajo.DoGolpeCritico(AtacanteIndex, 0, VictimaIndex, dano);
		}

		if (Declaraciones.UserList[VictimaIndex].Stats.MinHp <= 0) {

			/* ' No cuenta la muerte si estaba en estado atacable */
			if (Declaraciones.UserList[VictimaIndex].flags.AtacablePor != AtacanteIndex) {
				/* 'Store it! */
				Statistics.StoreFrag(AtacanteIndex, VictimaIndex);

				UsUaRiOs.ContarMuerte(VictimaIndex, AtacanteIndex);
			}

			/* ' Para que las mascotas no sigan intentando luchar y */
			/* ' comiencen a seguir al amo */
			int j;
			for (j = (1); j <= (Declaraciones.MAXMASCOTAS); j++) {
				if (Declaraciones.UserList[AtacanteIndex].MascotasIndex[j] > 0) {
					if (Declaraciones.Npclist[Declaraciones.UserList[AtacanteIndex].MascotasIndex[j]].Target == VictimaIndex) {
						Declaraciones.Npclist[Declaraciones.UserList[AtacanteIndex].MascotasIndex[j]].Target = 0;
						NPCs.FollowAmo(Declaraciones.UserList[AtacanteIndex].MascotasIndex[j]);
					}
				}
			}

			UsUaRiOs.ActStats(VictimaIndex, AtacanteIndex);
			UsUaRiOs.UserDie(VictimaIndex);
		} else {
			/* 'Está vivo - Actualizamos el HP */
			Protocol.WriteUpdateHP(VictimaIndex);
		}

		/* 'Controla el nivel del usuario */
		UsUaRiOs.CheckUserLevel(AtacanteIndex);

		Protocol.FlushBuffer(VictimaIndex);

		return;

		/* FIXME: ErrHandler : */
		String AtacanteNick;
		String VictimaNick;

		if (AtacanteIndex > 0) {
			AtacanteNick = Declaraciones.UserList[AtacanteIndex].Name;
		}
		if (VictimaIndex > 0) {
			VictimaNick = Declaraciones.UserList[VictimaIndex].Name;
		}

		General.LogError("Error en UserDanoUser. Error " + Err.Number + " : " + Err.description + " AtacanteIndex: "
				+ AtacanteIndex + " Nick: " + AtacanteNick + " VictimaIndex: " + VictimaIndex + " Nick: "
				+ VictimaNick);
	}

	static void UsuarioAtacadoPorUsuario(int AttackerIndex, int VictimIndex) {
		/* '*************************************************** */
		/* 'Autor: Unknown */
		/* 'Last Modification: 05/05/2010 */
		/* 'Last Modified By: Lucas Tavolaro Ortiz (Tavo) */
		/*
		 * '10/01/2008: Tavo - Se cancela la salida del juego si el user esta
		 * saliendo
		 */
		/*
		 * '05/05/2010: ZaMa - Ahora no suma puntos de bandido al atacar a
		 * alguien en estado atacable.
		 */
		/* '*************************************************** */

		if (TriggerZonaPelea(AttackerIndex, VictimIndex) == TRIGGER6_PERMITE) {
			return;
		}

		boolean EraCriminal;
		boolean VictimaEsAtacable;

		if (!ES.criminal(AttackerIndex)) {
			if (!ES.criminal(VictimIndex)) {
				/*
				 * ' Si la victima no es atacable por el agresor, entonces se
				 * hace pk
				 */
				VictimaEsAtacable = Declaraciones.UserList[VictimIndex].flags.AtacablePor == AttackerIndex;
				if (!VictimaEsAtacable) {
					UsUaRiOs.VolverCriminal(AttackerIndex);
				}
			}
		}

		if (Declaraciones.UserList[VictimIndex].flags.Meditando) {
			Declaraciones.UserList[VictimIndex].flags.Meditando = false;
			Protocol.WriteMeditateToggle(VictimIndex);
			Protocol.WriteConsoleMsg(VictimIndex, "Dejas de meditar.", FontTypeNames.FONTTYPE_INFO);
			Declaraciones.UserList[VictimIndex].Char.FX = 0;
			Declaraciones.UserList[VictimIndex].Char.loops = 0;
			modSendData.SendData(SendTarget.ToPCArea, VictimIndex,
					Protocol.PrepareMessageCreateFX(Declaraciones.UserList[VictimIndex].Char.CharIndex, 0, 0));
		}

		EraCriminal = ES.criminal(AttackerIndex);

		/* ' Si ataco a un atacable, no suma puntos de bandido */
		if (!VictimaEsAtacable) {
			if (!ES.criminal(VictimIndex)) {
				Declaraciones.UserList[AttackerIndex].Reputacion.BandidoRep = Declaraciones.UserList[AttackerIndex].Reputacion.BandidoRep
						+ Declaraciones.vlASALTO;
				if (Declaraciones.UserList[AttackerIndex].Reputacion.BandidoRep > Declaraciones.MAXREP) {
					Declaraciones.UserList[AttackerIndex].Reputacion.BandidoRep = Declaraciones.MAXREP;
				}

				Declaraciones.UserList[AttackerIndex].Reputacion.NobleRep = Declaraciones.UserList[AttackerIndex].Reputacion.NobleRep
						* 0.5;
				if (Declaraciones.UserList[AttackerIndex].Reputacion.NobleRep < 0) {
					Declaraciones.UserList[AttackerIndex].Reputacion.NobleRep = 0;
				}
			} else {
				Declaraciones.UserList[AttackerIndex].Reputacion.NobleRep = Declaraciones.UserList[AttackerIndex].Reputacion.NobleRep
						+ Declaraciones.vlNoble;
				if (Declaraciones.UserList[AttackerIndex].Reputacion.NobleRep > Declaraciones.MAXREP) {
					Declaraciones.UserList[AttackerIndex].Reputacion.NobleRep = Declaraciones.MAXREP;
				}
			}
		}

		if (ES.criminal(AttackerIndex)) {
			if (Declaraciones.UserList[AttackerIndex].Faccion.ArmadaReal == 1) {
				ModFacciones.ExpulsarFaccionReal(AttackerIndex);
			}

			if (!EraCriminal) {
				UsUaRiOs.RefreshCharStatus(AttackerIndex);
			}
		} else if (EraCriminal) {
			UsUaRiOs.RefreshCharStatus(AttackerIndex);
		}

		AllMascotasAtacanUser(AttackerIndex, VictimIndex);
		AllMascotasAtacanUser(VictimIndex, AttackerIndex);

		/* 'Si la victima esta saliendo se cancela la salida */
		UsUaRiOs.CancelExit(VictimIndex);
		Protocol.FlushBuffer(VictimIndex);
	}

	static void AllMascotasAtacanUser(int victim, int Maestro) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */
		/* 'Reaccion de las mascotas */
		int iCount;

		for (iCount = (1); iCount <= (Declaraciones.MAXMASCOTAS); iCount++) {
			if (Declaraciones.UserList[Maestro].MascotasIndex[iCount] > 0) {
				Declaraciones.Npclist[Declaraciones.UserList[Maestro].MascotasIndex[iCount]].flags.AttackedBy = Declaraciones.UserList[victim].Name;
				Declaraciones.Npclist[Declaraciones.UserList[Maestro].MascotasIndex[iCount]].Movement = TipoAI.NPCDEFENSA;
				Declaraciones.Npclist[Declaraciones.UserList[Maestro].MascotasIndex[iCount]].Hostile = 1;
			}
		}
	}

	static boolean PuedeAtacar(int AttackerIndex, int VictimIndex) {
		boolean retval;
		/* '*************************************************** */
		/* 'Autor: Unknown */
		/* 'Last Modification: 02/04/2010 */
		/*
		 * 'Returns true if the AttackerIndex is allowed to attack the
		 * VictimIndex.
		 */
		/*
		 * '24/01/2007 Pablo (ToxicWaste) - Ordeno todo y agrego situacion de
		 * Defensa en ciudad Armada y Caos.
		 */
		/* '24/02/2009: ZaMa - Los usuarios pueden atacarse entre si. */
		/*
		 * '02/04/2010: ZaMa - Los armadas no pueden atacar nunca a los ciudas,
		 * salvo que esten atacables.
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		/* 'MUY importante el orden de estos "IF"... */

		/* 'Estas muerto no podes atacar */
		if (Declaraciones.UserList[AttackerIndex].flags.Muerto == 1) {
			Protocol.WriteConsoleMsg(AttackerIndex, "¡¡Estás muerto!!", FontTypeNames.FONTTYPE_INFO);
			retval = false;
			return retval;
		}

		/* 'No podes atacar a alguien muerto */
		if (Declaraciones.UserList[VictimIndex].flags.Muerto == 1) {
			Protocol.WriteConsoleMsg(AttackerIndex, "No puedes atacar a un espíritu.", FontTypeNames.FONTTYPE_INFO);
			retval = false;
			return retval;
		}

		/* ' No podes atacar si estas en consulta */
		if (Declaraciones.UserList[AttackerIndex].flags.EnConsulta) {
			Protocol.WriteConsoleMsg(AttackerIndex, "No puedes atacar usuarios mientras estas en consulta.",
					FontTypeNames.FONTTYPE_INFO);
			return retval;
		}

		/* ' No podes atacar si esta en consulta */
		if (Declaraciones.UserList[VictimIndex].flags.EnConsulta) {
			Protocol.WriteConsoleMsg(AttackerIndex, "No puedes atacar usuarios mientras estan en consulta.",
					FontTypeNames.FONTTYPE_INFO);
			return retval;
		}

		/* 'Estamos en una Arena? o un trigger zona segura? */
		switch (TriggerZonaPelea(AttackerIndex, VictimIndex)) {
		case TRIGGER6_PERMITE:
			retval = (Declaraciones.UserList[VictimIndex].flags.AdminInvisible == 0);
			return retval;

			break;

		case TRIGGER6_PROHIBE:
			retval = false;
			return retval;

			break;

		case TRIGGER6_AUSENTE:
			/*
			 * 'Si no estamos en el Trigger 6 entonces es imposible atacar un gm
			 */
			if ((Declaraciones.UserList[VictimIndex].flags.Privilegios && PlayerType.User) == 0) {
				if (Declaraciones.UserList[VictimIndex].flags.AdminInvisible == 0) {
					Protocol.WriteConsoleMsg(AttackerIndex, "El ser es demasiado poderoso.",
							FontTypeNames.FONTTYPE_WARNING);
				}
				retval = false;
				return retval;
			}
			break;
		}

		/* 'Ataca un ciudadano? */
		if (!ES.criminal(VictimIndex)) {
			/* ' El atacante es ciuda? */
			if (!ES.criminal(AttackerIndex)) {
				/* ' El atacante es armada? */
				if (Extra.esArmada(AttackerIndex)) {
					/* ' La victima es armada? */
					if (Extra.esArmada(VictimIndex)) {
						/* ' No puede */
						Protocol.WriteConsoleMsg(AttackerIndex,
								"Los soldados del ejército real tienen prohibido atacar ciudadanos.",
								FontTypeNames.FONTTYPE_WARNING);
						return retval;
					}
				}

				/* ' Ciuda (o army) atacando a otro ciuda (o army) */
				if (Declaraciones.UserList[VictimIndex].flags.AtacablePor == AttackerIndex) {
					/* ' Se vuelve atacable. */
					if (UsUaRiOs.ToogleToAtackable(AttackerIndex, VictimIndex, false)) {
						retval = true;
						return retval;
					}
				}
			}
			/* ' Ataca a un criminal */
		} else {
			/* 'Sos un Caos atacando otro caos? */
			if (Extra.esCaos(VictimIndex)) {
				if (Extra.esCaos(AttackerIndex)) {
					Protocol.WriteConsoleMsg(AttackerIndex,
							"Los miembros de la legión oscura tienen prohibido atacarse entre sí.",
							FontTypeNames.FONTTYPE_WARNING);
					return retval;
				}
			}
		}

		/* 'Tenes puesto el seguro? */
		if (Declaraciones.UserList[AttackerIndex].flags.Seguro) {
			if (!ES.criminal(VictimIndex)) {
				Protocol.WriteConsoleMsg(AttackerIndex,
						"No puedes atacar ciudadanos, para hacerlo debes desactivar el seguro.",
						FontTypeNames.FONTTYPE_WARNING);
				retval = false;
				return retval;
			}
		} else {
			/* ' Un ciuda es atacado */
			if (!ES.criminal(VictimIndex)) {
				/* ' Por un armada sin seguro */
				if (Extra.esArmada(AttackerIndex)) {
					/* ' No puede */
					Protocol.WriteConsoleMsg(AttackerIndex,
							"Los soldados del ejército real tienen prohibido atacar ciudadanos.",
							FontTypeNames.FONTTYPE_WARNING);
					retval = false;
					return retval;
				}
			}
		}

		/* 'Estas en un Mapa Seguro? */
		if (Declaraciones.MapInfo[Declaraciones.UserList[VictimIndex].Pos.Map].Pk == false) {
			if (Extra.esArmada(AttackerIndex)) {
				if (Declaraciones.UserList[AttackerIndex].Faccion.RecompensasReal > 11) {
					if (Declaraciones.UserList[VictimIndex].Pos.Map == 58
							|| Declaraciones.UserList[VictimIndex].Pos.Map == 59
							|| Declaraciones.UserList[VictimIndex].Pos.Map == 60) {
						Protocol.WriteConsoleMsg(VictimIndex,
								"¡Huye de la ciudad! Estás siendo atacado y no podrás defenderte.",
								FontTypeNames.FONTTYPE_WARNING);
						/* 'Beneficio de Armadas que atacan en su ciudad. */
						retval = true;
						return retval;
					}
				}
			}
			if (Extra.esCaos(AttackerIndex)) {
				if (Declaraciones.UserList[AttackerIndex].Faccion.RecompensasCaos > 11) {
					if (Declaraciones.UserList[VictimIndex].Pos.Map == 151
							|| Declaraciones.UserList[VictimIndex].Pos.Map == 156) {
						Protocol.WriteConsoleMsg(VictimIndex,
								"¡Huye de la ciudad! Estás siendo atacado y no podrás defenderte.",
								FontTypeNames.FONTTYPE_WARNING);
						/* 'Beneficio de Caos que atacan en su ciudad. */
						retval = true;
						return retval;
					}
				}
			}
			Protocol.WriteConsoleMsg(AttackerIndex, "Esta es una zona segura, aquí no puedes atacar a otros usuarios.",
					FontTypeNames.FONTTYPE_WARNING);
			retval = false;
			return retval;
		}

		/*
		 * 'Estas atacando desde un trigger seguro? o tu victima esta en uno
		 * asi?
		 */
		if (Declaraciones.MapData[Declaraciones.UserList[VictimIndex].Pos.Map][Declaraciones.UserList[VictimIndex].Pos.X][Declaraciones.UserList[VictimIndex].Pos.Y].trigger == eTrigger.ZONASEGURA
				|| Declaraciones.MapData[Declaraciones.UserList[AttackerIndex].Pos.Map][Declaraciones.UserList[AttackerIndex].Pos.X][Declaraciones.UserList[AttackerIndex].Pos.Y].trigger == eTrigger.ZONASEGURA) {
			Protocol.WriteConsoleMsg(AttackerIndex, "No puedes pelear aquí.", FontTypeNames.FONTTYPE_WARNING);
			retval = false;
			return retval;
		}

		retval = true;
		return retval;

		/* FIXME: ErrHandler : */
		General.LogError("Error en PuedeAtacar. Error " + Err.Number + " : " + Err.description);
		return retval;
	}

	static boolean PuedeAtacarNPC(int AttackerIndex, int NpcIndex) {
		return PuedeAtacarNPC(AttackerIndex, NpcIndex, false, false);
	}

	static boolean PuedeAtacarNPC(int AttackerIndex, int NpcIndex, boolean Paraliza, boolean IsPet) {
		boolean retval;
		/* '*************************************************** */
		/* 'Autor: Unknown Author (Original version) */
		/* 'Returns True if AttackerIndex can attack the NpcIndex */
		/* 'Last Modification: 04/07/2010 */
		/*
		 * '24/01/2007 Pablo (ToxicWaste) - Orden y corrección de ataque sobre
		 * una mascota y guardias
		 */
		/*
		 * '14/08/2007 Pablo (ToxicWaste) - Reescribo y agrego TODOS los casos
		 * posibles cosa de usar
		 */
		/*
		 * 'esta función para todo lo referente a ataque a un NPC. Ya sea Magia,
		 * Físico o a Distancia.
		 */
		/* '16/11/2009: ZaMa - Agrego validacion de pertenencia de npc. */
		/*
		 * '02/04/2010: ZaMa - Los armadas ya no peuden atacar npcs no hotiles.
		 */
		/*
		 * '23/05/2010: ZaMa - El inmo/para renuevan el timer de pertenencia si
		 * el ataque fue a un npc propio.
		 */
		/* '04/07/2010: ZaMa - Ahora no se puede apropiar del dragon de dd. */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		/* 'Estas muerto? */
		if (Declaraciones.UserList[AttackerIndex].flags.Muerto == 1) {
			Protocol.WriteConsoleMsg(AttackerIndex, "¡¡Estás muerto!!", FontTypeNames.FONTTYPE_INFO);
			return retval;
		}

		/* 'Sos consejero? */
		if (Declaraciones.UserList[AttackerIndex].flags.Privilegios && PlayerType.Consejero) {
			/* 'No pueden atacar NPC los Consejeros. */
			return retval;
		}

		/* ' No podes atacar si estas en consulta */
		if (Declaraciones.UserList[AttackerIndex].flags.EnConsulta) {
			Protocol.WriteConsoleMsg(AttackerIndex, "No puedes atacar npcs mientras estas en consulta.",
					FontTypeNames.FONTTYPE_INFO);
			return retval;
		}

		/* 'Es una criatura atacable? */
		if (Declaraciones.Npclist[NpcIndex].Attackable == 0) {
			Protocol.WriteConsoleMsg(AttackerIndex, "No puedes atacar esta criatura.", FontTypeNames.FONTTYPE_INFO);
			return retval;
		}

		/* 'Es valida la distancia a la cual estamos atacando? */
		if (Matematicas.Distancia(Declaraciones.UserList[AttackerIndex].Pos,
				Declaraciones.Npclist[NpcIndex].Pos) >= SistemaCombate.MAXDISTANCIAARCO) {
			Protocol.WriteConsoleMsg(AttackerIndex, "Estás muy lejos para disparar.", FontTypeNames.FONTTYPE_FIGHT);
			return retval;
		}

		/* 'Es una criatura No-Hostil? */
		if (Declaraciones.Npclist[NpcIndex].Hostile == 0) {
			/* 'Es Guardia del Caos? */
			if (Declaraciones.Npclist[NpcIndex].NPCtype == eNPCType.Guardiascaos) {
				/* 'Lo quiere atacar un caos? */
				if (Extra.esCaos(AttackerIndex)) {
					Protocol.WriteConsoleMsg(AttackerIndex,
							"No puedes atacar Guardias del Caos siendo de la legión oscura.",
							FontTypeNames.FONTTYPE_INFO);
					return retval;
				}
				/* 'Es guardia Real? */
			} else if (Declaraciones.Npclist[NpcIndex].NPCtype == eNPCType.GuardiaReal) {
				/* 'Lo quiere atacar un Armada? */
				if (Extra.esArmada(AttackerIndex)) {
					Protocol.WriteConsoleMsg(AttackerIndex,
							"No puedes atacar Guardias Reales siendo del ejército real.", FontTypeNames.FONTTYPE_INFO);
					return retval;
				}
				/* 'Tienes el seguro puesto? */
				if (Declaraciones.UserList[AttackerIndex].flags.Seguro) {
					Protocol.WriteConsoleMsg(AttackerIndex,
							"Para poder atacar Guardias Reales debes quitarte el seguro.", FontTypeNames.FONTTYPE_INFO);
					return retval;
				} else {
					Protocol.WriteConsoleMsg(AttackerIndex, "¡Atacaste un Guardia Real! Eres un criminal.",
							FontTypeNames.FONTTYPE_INFO);
					UsUaRiOs.VolverCriminal(AttackerIndex);
					retval = true;
					return retval;
				}

				/*
				 * 'No era un Guardia, asi que es una criatura No-Hostil común.
				 */
				/* 'Para asegurarnos que no sea una Mascota: */
			} else if (Declaraciones.Npclist[NpcIndex].MaestroUser == 0) {
				/*
				 * 'Si sos ciudadano tenes que quitar el seguro para atacarla.
				 */
				if (!ES.criminal(AttackerIndex)) {

					/* ' Si sos armada no podes atacarlo directamente */
					if (Extra.esArmada(AttackerIndex)) {
						Protocol.WriteConsoleMsg(AttackerIndex,
								"Los miembros del ejército real no pueden atacar npcs no hostiles.",
								FontTypeNames.FONTTYPE_INFO);
						return retval;
					}

					/* 'Sos ciudadano, tenes el seguro puesto? */
					if (Declaraciones.UserList[AttackerIndex].flags.Seguro) {
						Protocol.WriteConsoleMsg(AttackerIndex, "Para atacar a este NPC debes quitarte el seguro.",
								FontTypeNames.FONTTYPE_INFO);
						return retval;
					} else {
						/*
						 * 'No tiene seguro puesto. Puede atacar pero es
						 * penalizado.
						 */
						Protocol.WriteConsoleMsg(AttackerIndex,
								"Atacaste un NPC no-hostil. Continúa haciéndolo y te podrás convertir en criminal.",
								FontTypeNames.FONTTYPE_INFO);
						/*
						 * 'NicoNZ: Cambio para que al atacar npcs no hostiles
						 * no bajen puntos de nobleza
						 */
						modHechizos.DisNobAuBan(AttackerIndex, 0, 1000);
						retval = true;
						return retval;
					}
				}
			}
		}

		int MasterIndex;
		MasterIndex = Declaraciones.Npclist[NpcIndex].MaestroUser;

		/* 'Es el NPC mascota de alguien? */
		if (MasterIndex > 0) {

			/* ' Dueno de la mascota ciuda? */
			if (!ES.criminal(MasterIndex)) {

				/* ' Atacante ciuda? */
				if (!ES.criminal(AttackerIndex)) {

					/*
					 * ' Si esta en estado atacable puede atacar su mascota sin
					 * problemas
					 */
					if (Declaraciones.UserList[MasterIndex].flags.AtacablePor == AttackerIndex) {
						/* ' Toogle to atacable and restart the timer */
						UsUaRiOs.ToogleToAtackable(AttackerIndex, MasterIndex);
						retval = true;
						return retval;
					}

					/* 'Atacante armada? */
					if (Extra.esArmada(AttackerIndex)) {
						/*
						 * 'El atacante es Armada y esta intentando atacar
						 * mascota de un Ciudadano
						 */
						Protocol.WriteConsoleMsg(AttackerIndex,
								"Los miembros del ejército real no pueden atacar mascotas de ciudadanos.",
								FontTypeNames.FONTTYPE_INFO);
						return retval;
					}

					/*
					 * 'El atacante es Ciudadano y esta intentando atacar
					 * mascota de un Ciudadano.
					 */
					if (Declaraciones.UserList[AttackerIndex].flags.Seguro) {
						/*
						 * 'El atacante tiene el seguro puesto. No puede atacar.
						 */
						Protocol.WriteConsoleMsg(AttackerIndex,
								"Para atacar mascotas de ciudadanos debes quitarte el seguro.",
								FontTypeNames.FONTTYPE_INFO);
						return retval;
					} else {
						/*
						 * 'El atacante no tiene el seguro puesto. Recibe
						 * penalización.
						 */
						Protocol.WriteConsoleMsg(AttackerIndex,
								"Has atacado la Mascota de un ciudadano. Eres un criminal.",
								FontTypeNames.FONTTYPE_INFO);
						UsUaRiOs.VolverCriminal(AttackerIndex);
						retval = true;
						return retval;
					}
				} else {
					/*
					 * 'El atacante es criminal y quiere atacar un elemental
					 * ciuda, pero tiene el seguro puesto (NicoNZ)
					 */
					if (Declaraciones.UserList[AttackerIndex].flags.Seguro) {
						Protocol.WriteConsoleMsg(AttackerIndex,
								"Para atacar mascotas de ciudadanos debes quitarte el seguro.",
								FontTypeNames.FONTTYPE_INFO);
						return retval;
					}
				}

				/* 'Es mascota de un caos? */
			} else if (Extra.esCaos(MasterIndex)) {
				/* 'Es Caos el Dueno. */
				if (Extra.esCaos(AttackerIndex)) {
					/*
					 * 'Un Caos intenta atacar una criatura de un Caos. No puede
					 * atacar.
					 */
					Protocol.WriteConsoleMsg(AttackerIndex,
							"Los miembros de la legión oscura no pueden atacar mascotas de otros legionarios. ",
							FontTypeNames.FONTTYPE_INFO);
					return retval;
				}
			}

			/* ' No es mascota de nadie, le pertenece a alguien? */
		} else if (Declaraciones.Npclist[NpcIndex].Owner > 0) {

			int OwnerUserIndex;
			OwnerUserIndex = Declaraciones.Npclist[NpcIndex].Owner;

			/* ' Puede atacar a su propia criatura! */
			if (OwnerUserIndex == AttackerIndex) {
				retval = true;
				/* ' Renuevo el timer */
				modNuevoTimer.IntervaloPerdioNpc(OwnerUserIndex, true);
				return retval;
			}

			/* ' Esta compartiendo el npc con el atacante? => Puede atacar! */
			if (Declaraciones.UserList[OwnerUserIndex].flags.ShareNpcWith == AttackerIndex) {
				retval = true;
				return retval;
			}

			/*
			 * ' Si son del mismo clan o party, pueden atacar (No renueva el
			 * timer)
			 */
			if (!SameClan(OwnerUserIndex, AttackerIndex) && !SameParty(OwnerUserIndex, AttackerIndex)) {

				/* ' Si se le agoto el tiempo */
				/* ' Se lo roba :P */
				if (modNuevoTimer.IntervaloPerdioNpc(OwnerUserIndex)) {
					UsUaRiOs.PerdioNpc(OwnerUserIndex);
					UsUaRiOs.ApropioNpc(AttackerIndex, NpcIndex);
					retval = true;
					return retval;

					/* ' Si lanzo un hechizo de para o inmo */
				} else if (Paraliza) {

					/*
					 * ' Si ya esta paralizado o inmobilizado, no puedo
					 * inmobilizarlo de nuevo
					 */
					if (Declaraciones.Npclist[NpcIndex].flags.Inmovilizado == 1
							|| Declaraciones.Npclist[NpcIndex].flags.Paralizado == 1) {

						/*
						 * 'TODO_ZAMA: Si dejo esto asi, los pks con seguro
						 * peusto van a poder inmobilizar criaturas con dueno
						 */
						/* ' Si es pk neutral, puede hacer lo que quiera :P. */
						if (!ES.criminal(AttackerIndex) && !ES.criminal(OwnerUserIndex)) {

							/* 'El atacante es Armada */
							if (Extra.esArmada(AttackerIndex)) {

								/* 'Intententa paralizar un npc de un armada? */
								if (Extra.esArmada(OwnerUserIndex)) {
									/*
									 * 'El atacante es Armada y esta intentando
									 * paralizar un npc de un armada: No puede
									 */
									Protocol.WriteConsoleMsg(AttackerIndex,
											"Los miembros del Ejército Real no pueden paralizar criaturas ya paralizadas pertenecientes a otros miembros del Ejército Real",
											FontTypeNames.FONTTYPE_INFO);
									return retval;

									/*
									 * 'El atacante es Armada y esta intentando
									 * paralizar un npc de un ciuda
									 */
								} else {
									/* ' Si tiene seguro no puede */
									if (Declaraciones.UserList[AttackerIndex].flags.Seguro) {
										Protocol.WriteConsoleMsg(AttackerIndex,
												"Para paralizar criaturas ya paralizadas pertenecientes a ciudadanos debes quitarte el seguro.",
												FontTypeNames.FONTTYPE_INFO);
										return retval;
									} else {
										/*
										 * ' Si ya estaba atacable, no podrá
										 * atacar a un npc perteneciente a otro
										 * ciuda
										 */
										if (UsUaRiOs.ToogleToAtackable(AttackerIndex, OwnerUserIndex)) {
											Protocol.WriteConsoleMsg(AttackerIndex,
													"Has paralizado la criatura de un ciudadano, ahora eres atacable por él.",
													FontTypeNames.FONTTYPE_INFO);
											retval = true;
										}

										return retval;

									}
								}

								/* ' El atacante es ciuda */
							} else {
								/*
								 * 'El atacante tiene el seguro puesto, no puede
								 * paralizar
								 */
								if (Declaraciones.UserList[AttackerIndex].flags.Seguro) {
									Protocol.WriteConsoleMsg(AttackerIndex,
											"Para paralizar criaturas ya paralizadas pertenecientes a ciudadanos debes quitarte el seguro.",
											FontTypeNames.FONTTYPE_INFO);
									return retval;

									/*
									 * 'El atacante no tiene el seguro puesto,
									 * ataca.
									 */
								} else {
									/*
									 * ' Si ya estaba atacable, no podrá atacar
									 * a un npc perteneciente a otro ciuda
									 */
									if (UsUaRiOs.ToogleToAtackable(AttackerIndex, OwnerUserIndex)) {
										Protocol.WriteConsoleMsg(AttackerIndex,
												"Has paralizado la criatura de un ciudadano, ahora eres atacable por él.",
												FontTypeNames.FONTTYPE_INFO);
										retval = true;
									}

									return retval;
								}
							}

							/* ' Al menos uno de los dos es criminal */
						} else {
							/* ' Si ambos son caos */
							if (Extra.esCaos(AttackerIndex) && Extra.esCaos(OwnerUserIndex)) {
								/*
								 * 'El atacante es Caos y esta intentando
								 * paralizar un npc de un Caos
								 */
								Protocol.WriteConsoleMsg(AttackerIndex,
										"Los miembros de la legión oscura no pueden paralizar criaturas ya paralizadas por otros legionarios.",
										FontTypeNames.FONTTYPE_INFO);
								return retval;
							}
						}

						/* ' El npc no esta inmobilizado ni paralizado */
					} else {
						/* ' Si no tiene dueno, puede apropiarselo */
						if (OwnerUserIndex == 0) {

							/*
							 * ' Siempre que no posea uno ya (el inmo/para no
							 * cambia pertenencia de npcs).
							 */
							if (Declaraciones.UserList[AttackerIndex].flags.OwnedNpc == 0) {
								UsUaRiOs.ApropioNpc(AttackerIndex, NpcIndex);
							}

							/*
							 * ' Si inmobiliza a su propio npc, renueva el timer
							 */
						} else if (OwnerUserIndex == AttackerIndex) {
							/* ' Renuevo el timer */
							modNuevoTimer.IntervaloPerdioNpc(OwnerUserIndex, true);
						}

						/*
						 * ' Siempre se pueden paralizar/inmobilizar npcs con o
						 * sin dueno
						 */
						/* ' que no tengan ese estado */
						retval = true;
						return retval;

					}

					/* ' No lanzó hechizos inmobilizantes */
				} else {

					/* ' El npc le pertenece a un ciudadano */
					if (!ES.criminal(OwnerUserIndex)) {

						/*
						 * 'El atacante es Armada y esta intentando atacar un
						 * npc de un Ciudadano
						 */
						if (Extra.esArmada(AttackerIndex)) {

							/* 'Intententa atacar un npc de un armada? */
							if (Extra.esArmada(OwnerUserIndex)) {
								/*
								 * 'El atacante es Armada y esta intentando
								 * atacar el npc de un armada: No puede
								 */
								Protocol.WriteConsoleMsg(AttackerIndex,
										"Los miembros del Ejército Real no pueden atacar criaturas pertenecientes a otros miembros del Ejército Real",
										FontTypeNames.FONTTYPE_INFO);
								return retval;

								/*
								 * 'El atacante es Armada y esta intentando
								 * atacar un npc de un ciuda
								 */
							} else {

								/* ' Si tiene seguro no puede */
								if (Declaraciones.UserList[AttackerIndex].flags.Seguro) {
									Protocol.WriteConsoleMsg(AttackerIndex,
											"Para atacar criaturas ya pertenecientes a ciudadanos debes quitarte el seguro.",
											FontTypeNames.FONTTYPE_INFO);
									return retval;
								} else {
									/*
									 * ' Si ya estaba atacable, no podrá atacar
									 * a un npc perteneciente a otro ciuda
									 */
									if (UsUaRiOs.ToogleToAtackable(AttackerIndex, OwnerUserIndex)) {
										Protocol.WriteConsoleMsg(AttackerIndex,
												"Has atacado a la criatura de un ciudadano, ahora eres atacable por él.",
												FontTypeNames.FONTTYPE_INFO);
										retval = true;
									}

									return retval;
								}
							}

							/* ' No es aramda, puede ser criminal o ciuda */
						} else {

							/*
							 * 'El atacante es Ciudadano y esta intentando
							 * atacar un npc de un Ciudadano.
							 */
							if (!ES.criminal(AttackerIndex)) {

								if (Declaraciones.UserList[AttackerIndex].flags.Seguro) {
									/*
									 * 'El atacante tiene el seguro puesto. No
									 * puede atacar.
									 */
									Protocol.WriteConsoleMsg(AttackerIndex,
											"Para atacar criaturas pertenecientes a ciudadanos debes quitarte el seguro.",
											FontTypeNames.FONTTYPE_INFO);
									return retval;

									/*
									 * 'El atacante no tiene el seguro puesto,
									 * ataca.
									 */
								} else {
									if (UsUaRiOs.ToogleToAtackable(AttackerIndex, OwnerUserIndex)) {
										Protocol.WriteConsoleMsg(AttackerIndex,
												"Has atacado a la criatura de un ciudadano, ahora eres atacable por él.",
												FontTypeNames.FONTTYPE_INFO);
										retval = true;
									}

									return retval;
								}

								/*
								 * 'El atacante es criminal y esta intentando
								 * atacar un npc de un Ciudadano.
								 */
							} else {
								/*
								 * ' Es criminal atacando un npc de un ciuda,
								 * con seguro puesto.
								 */
								if (Declaraciones.UserList[AttackerIndex].flags.Seguro) {
									Protocol.WriteConsoleMsg(AttackerIndex,
											"Para atacar criaturas pertenecientes a ciudadanos debes quitarte el seguro.",
											FontTypeNames.FONTTYPE_INFO);
									return retval;
								}

								retval = true;
							}
						}

						/* ' Es npc de un criminal */
					} else {
						if (Extra.esCaos(OwnerUserIndex)) {
							/* 'Es Caos el Dueno. */
							if (Extra.esCaos(AttackerIndex)) {
								/*
								 * 'Un Caos intenta atacar una npc de un Caos.
								 * No puede atacar.
								 */
								Protocol.WriteConsoleMsg(AttackerIndex,
										"Los miembros de la Legión Oscura no pueden atacar criaturas de otros legionarios. ",
										FontTypeNames.FONTTYPE_INFO);
								return retval;
							}
						}
					}
				}
			}

			/* ' Si no tiene dueno el npc, se lo apropia */
		} else {
			/* ' Solo pueden apropiarse de npcs los caos, armadas o ciudas. */
			if (!ES.criminal(AttackerIndex) || Extra.esCaos(AttackerIndex)) {
				/* ' No puede apropiarse de los pretos! */
				if (Declaraciones.Npclist[NpcIndex].NPCtype != eNPCType.Pretoriano) {
					/* ' No puede apropiarse del dragon de dd! */
					if (Declaraciones.Npclist[NpcIndex].NPCtype != DRAGON) {
						/*
						 * ' Si es una mascota atacando, no se apropia del npc
						 */
						if (!IsPet) {
							/* ' No es dueno de ningun npc => Se lo apropia. */
							if (Declaraciones.UserList[AttackerIndex].flags.OwnedNpc == 0) {
								UsUaRiOs.ApropioNpc(AttackerIndex, NpcIndex);
								/*
								 * ' Es dueno de un npc, pero no puede ser de
								 * este porque no tiene propietario.
								 */
							} else {
								/*
								 * ' Se va a aduenar del npc (y perder el otro)
								 * solo si no inmobiliza/paraliza
								 */
								if (!Paraliza) {
									UsUaRiOs.ApropioNpc(AttackerIndex, NpcIndex);
								}
							}
						}
					}
				}
			}
		}

		/* 'Es el Rey Preatoriano? */
		if (Declaraciones.Npclist[NpcIndex].NPCtype == eNPCType.Pretoriano) {
			if (!Declaraciones.ClanPretoriano[Declaraciones.Npclist[NpcIndex].ClanIndex].CanAtackMember(NpcIndex)) {
				Protocol.WriteConsoleMsg(AttackerIndex, "Debes matar al resto del ejército antes de atacar al rey.",
						FontTypeNames.FONTTYPE_FIGHT);
				return retval;
			}
		}

		retval = true;

		return retval;

		/* FIXME: ErrHandler : */

		String AtckName;
		String OwnerName;

		if (AttackerIndex > 0) {
			AtckName = Declaraciones.UserList[AttackerIndex].Name;
		}
		if (OwnerUserIndex > 0) {
			OwnerName = Declaraciones.UserList[OwnerUserIndex].Name;
		}

		General.LogError("Error en PuedeAtacarNpc. Erorr: " + Err.Number + " - " + Err.description + " Atacante: "
				+ AttackerIndex + "-> " + AtckName + ". Owner: " + OwnerUserIndex + "-> " + OwnerName + ". NpcIndex: "
				+ NpcIndex + ".");
		return retval;
	}

	static boolean SameClan(int UserIndex, int OtherUserIndex) {
		boolean retval;
		/* '*************************************************** */
		/* 'Autor: ZaMa */
		/* 'Returns True if both players belong to the same clan. */
		/* 'Last Modification: 16/11/2009 */
		/* '*************************************************** */
		retval = (Declaraciones.UserList[UserIndex].GuildIndex == Declaraciones.UserList[OtherUserIndex].GuildIndex)
				&& Declaraciones.UserList[UserIndex].GuildIndex != 0;
		return retval;
	}

	static boolean SameParty(int UserIndex, int OtherUserIndex) {
		boolean retval;
		/* '*************************************************** */
		/* 'Autor: ZaMa */
		/* 'Returns True if both players belong to the same party. */
		/* 'Last Modification: 16/11/2009 */
		/* '*************************************************** */
		retval = Declaraciones.UserList[UserIndex].PartyIndex == Declaraciones.UserList[OtherUserIndex].PartyIndex
				&& Declaraciones.UserList[UserIndex].PartyIndex != 0;
		return retval;
	}

	static void CalcularDarExp(int UserIndex, int NpcIndex, int ElDano) {
		/* '*************************************************** */
		/* 'Autor: Nacho (Integer) */
		/* 'Last Modification: 03/09/06 Nacho */
		/* 'Reescribi gran parte del Sub */
		/* 'Ahora, da toda la experiencia del npc mientras este vivo. */
		/* '*************************************************** */
		int ExpaDar;

		/*
		 * '[Nacho] Chekeamos que las variables sean validas para las
		 * operaciones
		 */
		if (ElDano <= 0) {
			ElDano = 0;
		}
		if (Declaraciones.Npclist[NpcIndex].Stats.MaxHp <= 0) {
			return;
		}
		if (ElDano > Declaraciones.Npclist[NpcIndex].Stats.MinHp) {
			ElDano = Declaraciones.Npclist[NpcIndex].Stats.MinHp;
		}

		/*
		 * '[Nacho] La experiencia a dar es la porcion de vida quitada * toda la
		 * experiencia
		 */
		ExpaDar = vb6
				.CLng(ElDano * (Declaraciones.Npclist[NpcIndex].GiveEXP / Declaraciones.Npclist[NpcIndex].Stats.MaxHp));
		if (ExpaDar <= 0) {
			return;
		}

		/*
		 * '[Nacho] Vamos contando cuanta experiencia sacamos, porque se da toda
		 * la que no se dio al user que mata al NPC
		 */
		/*
		 * 'Esto es porque cuando un elemental ataca, no se da exp, y tambien
		 * porque la cuenta que hicimos antes
		 */
		/*
		 * 'Podria dar un numero fraccionario, esas fracciones se acumulan hasta
		 * formar enteros ;P
		 */
		if (ExpaDar > Declaraciones.Npclist[NpcIndex].flags.ExpCount) {
			ExpaDar = Declaraciones.Npclist[NpcIndex].flags.ExpCount;
			Declaraciones.Npclist[NpcIndex].flags.ExpCount = 0;
		} else {
			Declaraciones.Npclist[NpcIndex].flags.ExpCount = Declaraciones.Npclist[NpcIndex].flags.ExpCount - ExpaDar;
		}

		/* '[Nacho] Le damos la exp al user */
		if (ExpaDar > 0) {
			if (Declaraciones.UserList[UserIndex].PartyIndex > 0) {
				mdParty.ObtenerExito(UserIndex, ExpaDar, Declaraciones.Npclist[NpcIndex].Pos.Map,
						Declaraciones.Npclist[NpcIndex].Pos.X, Declaraciones.Npclist[NpcIndex].Pos.Y);
			} else {
				Declaraciones.UserList[UserIndex].Stats.Exp = Declaraciones.UserList[UserIndex].Stats.Exp + ExpaDar;
				if (Declaraciones.UserList[UserIndex].Stats.Exp > Declaraciones.MAXEXP) {
					Declaraciones.UserList[UserIndex].Stats.Exp = Declaraciones.MAXEXP;
				}
				Protocol.WriteConsoleMsg(UserIndex, "Has ganado " + ExpaDar + " puntos de experiencia.",
						FontTypeNames.FONTTYPE_FIGHT);
			}

			UsUaRiOs.CheckUserLevel(UserIndex);
		}
	}

	static eTrigger6 TriggerZonaPelea(int Origen, int Destino) {
		eTrigger6 retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* 'TODO: Pero que rebuscado!! */
		/*
		 * 'Nigo: Te lo redisene, pero no te borro el TODO para que lo revises.
		 */
		/* FIXME: ON ERROR GOTO ErrHandler */
		eTrigger tOrg;
		eTrigger tDst;

		tOrg = Declaraciones.MapData[Declaraciones.UserList[Origen].Pos.Map][Declaraciones.UserList[Origen].Pos.X][Declaraciones.UserList[Origen].Pos.Y].trigger;
		tDst = Declaraciones.MapData[Declaraciones.UserList[Destino].Pos.Map][Declaraciones.UserList[Destino].Pos.X][Declaraciones.UserList[Destino].Pos.Y].trigger;

		if (tOrg == eTrigger.ZONAPELEA || tDst == eTrigger.ZONAPELEA) {
			if (tOrg == tDst) {
				retval = TRIGGER6_PERMITE;
			} else {
				retval = TRIGGER6_PROHIBE;
			}
		} else {
			retval = TRIGGER6_AUSENTE;
		}

		return retval;
		/* FIXME: ErrHandler : */
		retval = TRIGGER6_AUSENTE;
		General.LogError("Error en TriggerZonaPelea - " + Err.description);
		return retval;
	}

	static void UserEnvenena(int AtacanteIndex, int VictimaIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int ObjInd;

		ObjInd = Declaraciones.UserList[AtacanteIndex].Invent.WeaponEqpObjIndex;

		if (ObjInd > 0) {
			if (Declaraciones.ObjData[ObjInd].proyectil == 1) {
				ObjInd = Declaraciones.UserList[AtacanteIndex].Invent.MunicionEqpObjIndex;
			}

			if (ObjInd > 0) {
				if (Declaraciones.ObjData[ObjInd].Envenena == 1) {

					if (Matematicas.RandomNumber(1, 100) < 60) {
						Declaraciones.UserList[VictimaIndex].flags.Envenenado = 1;
						Protocol.WriteConsoleMsg(VictimaIndex,
								"¡¡" + Declaraciones.UserList[AtacanteIndex].Name + " te ha envenenado!!",
								FontTypeNames.FONTTYPE_FIGHT);
						Protocol.WriteConsoleMsg(AtacanteIndex,
								"¡¡Has envenenado a " + Declaraciones.UserList[VictimaIndex].Name + "!!",
								FontTypeNames.FONTTYPE_FIGHT);
					}
				}
			}
		}

		Protocol.FlushBuffer(VictimaIndex);
	}

	static void LanzarProyectil(int UserIndex, int X, int Y) {
		/* '*************************************************** */
		/* 'Autor: ZaMa */
		/* 'Last Modification: 10/07/2010 */
		/* 'Throws an arrow or knive to target user/npc. */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		int MunicionSlot;
		int MunicionIndex;
		int WeaponSlot;
		int WeaponIndex;

		int TargetUserIndex;
		int TargetNpcIndex;

		int DummyInt;

		boolean Threw;
		Threw = true;

		/* 'Make sure the item is valid and there is ammo equipped. */

		MunicionSlot = Declaraciones.UserList[UserIndex].Invent.MunicionEqpSlot;
		MunicionIndex = Declaraciones.UserList[UserIndex].Invent.MunicionEqpObjIndex;
		WeaponSlot = Declaraciones.UserList[UserIndex].Invent.WeaponEqpSlot;
		WeaponIndex = Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex;

		/* ' Tiene arma equipada? */
		if (WeaponIndex == 0) {
			DummyInt = 1;
			Protocol.WriteConsoleMsg(UserIndex, "No tienes un arco o cuchilla equipada.", FontTypeNames.FONTTYPE_INFO);

			/* ' En un slot válido? */
		} else if (WeaponSlot < 1 || WeaponSlot > Declaraciones.UserList[UserIndex].CurrentInventorySlots) {
			DummyInt = 1;
			Protocol.WriteConsoleMsg(UserIndex, "No tienes un arco o cuchilla equipada.", FontTypeNames.FONTTYPE_INFO);

			/* ' Usa munición? (Si no la usa, puede ser un arma arrojadiza) */
		} else if (Declaraciones.ObjData[WeaponIndex].Municion == 1) {

			/* ' La municion esta equipada en un slot valido? */
			if (MunicionSlot < 1 || MunicionSlot > Declaraciones.UserList[UserIndex].CurrentInventorySlots) {
				DummyInt = 1;
				Protocol.WriteConsoleMsg(UserIndex, "No tienes municiones equipadas.", FontTypeNames.FONTTYPE_INFO);

				/* ' Tiene munición? */
			} else if (MunicionIndex == 0) {
				DummyInt = 1;
				Protocol.WriteConsoleMsg(UserIndex, "No tienes municiones equipadas.", FontTypeNames.FONTTYPE_INFO);

				/* ' Son flechas? */
			} else if (Declaraciones.ObjData[MunicionIndex].OBJType != eOBJType.otFlechas) {
				DummyInt = 1;
				Protocol.WriteConsoleMsg(UserIndex, "No tienes municiones.", FontTypeNames.FONTTYPE_INFO);

				/* ' Tiene suficientes? */
			} else if (Declaraciones.UserList[UserIndex].Invent.Object[MunicionSlot].Amount < 1) {
				DummyInt = 1;
				Protocol.WriteConsoleMsg(UserIndex, "No tienes municiones.", FontTypeNames.FONTTYPE_INFO);
			}

			/* ' Es un arma de proyectiles? */
		} else if (Declaraciones.ObjData[WeaponIndex].proyectil != 1) {
			DummyInt = 2;
		}

		if (DummyInt != 0) {
			if (DummyInt == 1) {
				InvUsuario.Desequipar(UserIndex, WeaponSlot, false);
			}

			InvUsuario.Desequipar(UserIndex, MunicionSlot, true);
			return;
		}

		/* 'Quitamos stamina */
		if (Declaraciones.UserList[UserIndex].Stats.MinSta >= 10) {
			Trabajo.QuitarSta(UserIndex, Matematicas.RandomNumber(1, 10));
		} else {
			if (Declaraciones.UserList[UserIndex].Genero == eGenero.Hombre) {
				Protocol.WriteConsoleMsg(UserIndex, "Estás muy cansado para luchar.", FontTypeNames.FONTTYPE_INFO);
			} else {
				Protocol.WriteConsoleMsg(UserIndex, "Estás muy cansada para luchar.", FontTypeNames.FONTTYPE_INFO);
			}
			return;
		}

		Extra.LookatTile(UserIndex, Declaraciones.UserList[UserIndex].Pos.Map, X, Y);

		TargetUserIndex = Declaraciones.UserList[UserIndex].flags.TargetUser;
		TargetNpcIndex = Declaraciones.UserList[UserIndex].flags.TargetNPC;

		/* 'Validate target */
		if (TargetUserIndex > 0) {
			/*
			 * 'Only allow to atack if the other one can retaliate (can see us)
			 */
			if (vb6.Abs(Declaraciones.UserList[TargetUserIndex].Pos.Y
					- Declaraciones.UserList[UserIndex].Pos.Y) > AI.RANGO_VISION_Y) {
				Protocol.WriteConsoleMsg(UserIndex, "Estás demasiado lejos para atacar.",
						FontTypeNames.FONTTYPE_WARNING);
				return;
			}

			/* 'Prevent from hitting self */
			if (TargetUserIndex == UserIndex) {
				Protocol.WriteConsoleMsg(UserIndex, "¡No puedes atacarte a vos mismo!", FontTypeNames.FONTTYPE_INFO);
				return;
			}

			/* 'Attack! */
			Threw = UsuarioAtacaUsuario(UserIndex, TargetUserIndex);

		} else if (TargetNpcIndex > 0) {
			/*
			 * 'Only allow to atack if the other one can retaliate (can see us)
			 */
			if (vb6.Abs(Declaraciones.Npclist[TargetNpcIndex].Pos.Y
					- Declaraciones.UserList[UserIndex].Pos.Y) > AI.RANGO_VISION_Y
					&& vb6.Abs(Declaraciones.Npclist[TargetNpcIndex].Pos.X
							- Declaraciones.UserList[UserIndex].Pos.X) > AI.RANGO_VISION_X) {
				Protocol.WriteConsoleMsg(UserIndex, "Estás demasiado lejos para atacar.",
						FontTypeNames.FONTTYPE_WARNING);
				return;
			}

			/* 'Is it attackable??? */
			if (Declaraciones.Npclist[TargetNpcIndex].Attackable != 0) {
				/* 'Attack! */
				Threw = UsuarioAtacaNpc(UserIndex, TargetNpcIndex);
			}
		}

		/* ' Solo pierde la munición si pudo atacar al target, o tiro al aire */
		if (Threw) {

			int Slot;

			/* ' Tiene equipado arco y flecha? */
			if (Declaraciones.ObjData[WeaponIndex].Municion == 1) {
				Slot = MunicionSlot;
				/* ' Tiene equipado un arma arrojadiza */
			} else {
				Slot = WeaponSlot;
			}

			/* 'Take 1 knife/arrow away */
			InvUsuario.QuitarUserInvItem(UserIndex, Slot, 1);
			InvUsuario.UpdateUserInv(false, UserIndex, Slot);

		}

		return;

		/* FIXME: ErrHandler : */

		String UserName;
		if (UserIndex > 0) {
			UserName = Declaraciones.UserList[UserIndex].Name;
		}

		General.LogError("Error en LanzarProyectil " + Err.Number + ": " + Err.description + ". User: " + UserName + "("
				+ UserIndex + ")");

	}

}