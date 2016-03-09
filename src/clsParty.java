/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"clsParty"')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_GlobalNameSpace'), (5, '='), (1, 'False')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Creatable'), (5, '='), (1, 'True')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_PredeclaredId'), (5, '='), (1, 'False')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Exposed'), (5, '='), (1, 'False')] */
/* '************************************************************** */
/* ' clsParty.cls */
/* ' */
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

public class clsParty {

	private mdParty.tPartyMember[] p_members = new mdParty.tPartyMember[1 + mdParty.PARTY_MAXMEMBERS];
	/* 'miembros */

	private int p_expTotal;
	/* 'Estadistica :D */

	private int p_Fundador;
	/* 'el creador */

	private int p_CantMiembros;
	/* 'cantidad de miembros */

	private float p_SumaNivelesElevados;
	/*
	 * 'suma de todos los niveles elevados a la ExponenteNivelParty > Esta
	 * variable se usa para calcular la experiencia repartida en la Party.
	 */

	/*
	 * 'datos en los pjs: | indexParty(indice en p_members),
	 * partyLeader(userindex del lider) |
	 */

	/* 'Constructor de clase */
	void Class_Initialize() {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 07/04/08 */
		/* 'Last Modification By: Marco Vanotti (MarKoxX) */
		/* ' - 09/29/07 p_SumaNiveles added (Tavo) */
		/*
		 * ' - 07/04/08 p_SumaNiveles changed to p_SumaNivelesElevados (MarKoxX)
		 */
		/* '*************************************************** */
		p_expTotal = 0;
		p_CantMiembros = 0;
		p_SumaNivelesElevados = 0;
	}

	/* 'Destructor de clase */
	void Class_Terminate() {
	}

	/* '' */
	/* ' Sets the new p_sumaniveleselevados to the party. */
	/* ' */
	/* ' @param lvl Specifies reference to user level */
	/*
	 * ' @remarks When a user level up and he is in a party, we update
	 * p_sumaNivelesElavados so the formula still works.
	 */
	void UpdateSumaNivelesElevados(int Lvl) {
 /* '************************************************* */
 /* 'Author: Marco Vanotti (MarKoxX) */
 /* 'Last modified: 11/24/09 */
 /* '11/24/09: Pato - Change the exponent to a variable with the exponent */
 /* '************************************************* */
 p_SumaNivelesElevados = p_SumaNivelesElevados-((Lvl-1) $ mdParty.ExponenteNivelParty)+Lvl $ mdParty.ExponenteNivelParty;
}

	int MiExperiencia(int UserIndex) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 11/27/09 */
		/* 'Last Modification By: Budi */
		/*
		 * ' - 09/29/07 Experience is round to the biggest number less than that
		 * number
		 */
		/* ' - 09/29/07 Now experience is a real-number */
		/* ' - 11/27/09 Arreglé el Out of Range. */
		/* '*************************************************** */
		/* 'Me dice cuanta experiencia tengo colectada ya en la party */
		int i = 0;
		i = 1;

		while (i <= mdParty.PARTY_MAXMEMBERS && p_members[i].UserIndex != UserIndex) {
			i = i + 1;
		}

		if (i <= mdParty.PARTY_MAXMEMBERS) {
			retval = vb6.Fix(p_members[i].Experiencia);
			/* 'esto no deberia pasar :p */
		} else {
			retval = -1;
		}

		return retval;
	}

	void ObtenerExito(int ExpGanada, int mapa, int X, int Y) {
 /* '*************************************************** */
 /* 'Author: Unknown */
 /* 'Last Modification: 07/04/08 */
 /* 'Last Modification By: Marco Vanotti (MarKoxX) */
 /* ' - 09/29/07 New formula for calculating the experience point of each user */
 /* ' - 09/29/07 Experience is round to the biggest number less than that number */
 /* ' - 09/29/07 Now experience is a real-number */
 /* ' - 04/04/08 Ahora antes de calcular la experiencia a X usuario se fija si ese usuario existe (MarKoxX) */
 /* ' - 07/04/08 New formula to calculate Experience for each user. (MarKoxX) */
 /* '*************************************************** */
 /* 'Se produjo un evento que da experiencia en la wp referenciada */
 int i = 0;
 int UI = 0;
 double expThisUser = 0.0;
 
 p_expTotal = p_expTotal+ExpGanada;
 
  for (i = (1); i <= (mdParty.PARTY_MAXMEMBERS); i++) {
  UI = p_members[i].UserIndex;
   if (UI>0) {
   /* ' Formula: Exp* (Nivel ^ ExponenteNivelParty) / sumadeNivelesElevados */
   expThisUser = vb6.CDbl(ExpGanada*(Declaraciones.UserList[p_members[i].UserIndex].Stats.ELV $ mdParty.ExponenteNivelParty) / (double) p_SumaNivelesElevados);
   
    if (mapa == Declaraciones.UserList[UI].Pos.map && Declaraciones.UserList[UI].flags.Muerto == 0) {
     if (Matematicas.Distance(Declaraciones.UserList[UI].Pos.X, Declaraciones.UserList[UI].Pos.Y, X, Y)<=mdParty.PARTY_MAXDISTANCIA) {
     p_members[i].Experiencia = p_members[i].Experiencia+expThisUser;
      if (p_members[i].Experiencia<0) {
      p_members[i].Experiencia = 0;
     }
      if (mdParty.PARTY_EXPERIENCIAPORGOLPE) {
      Declaraciones.UserList[UI].Stats.Exp = Declaraciones.UserList[UI].Stats.Exp+vb6.Fix(expThisUser);
      if (Declaraciones.UserList[UI].Stats.Exp>Declaraciones.MAXEXP) {
      Declaraciones.UserList[UI].Stats.Exp = Declaraciones.MAXEXP;
      }
      UsUaRiOs.CheckUserLevel(UI);
      Protocol.WriteUpdateUserStats(UI);
     }
    }
   }
  }
 }
 
}

	void MandarMensajeAConsola(String texto, String Sender) {
		/*
		 * 'feo feo, muy feo acceder a senddata desde aca, pero BUEEEEEEEEEEE...
		 */
		int i = 0;

		for (i = (1); i <= (mdParty.PARTY_MAXMEMBERS); i++) {
			if (p_members[i].UserIndex > 0) {
				Protocol.WriteConsoleMsg(p_members[i].UserIndex, " [" + Sender + "] " + texto,
						FontTypeNames.FONTTYPE_PARTY);
			}
		}

	}

	boolean EsPartyLeader(int UserIndex) {
		boolean retval = false;
		retval = (UserIndex == p_Fundador);
		return retval;
	}

	boolean NuevoMiembro(int UserIndex) {
 boolean retval = false;
 /* '*************************************************** */
 /* 'Author: Unknown */
 /* 'Last Modification: 07/04/08 */
 /* 'Last Modification By: Marco Vanotti (MarKoxX) */
 /* ' - 09/29/07 There is no level prohibition (Tavo) */
 /* ' - 07/04/08 Added const ExponenteNivelParty. (MarKoxX) */
 /* '*************************************************** */
 
 int i = 0;
 i = 1;
  while (i<=mdParty.PARTY_MAXMEMBERS && p_members[i].UserIndex>0) {
  i = i+1;
 }
 
  if (i<=mdParty.PARTY_MAXMEMBERS) {
  p_members[i].Experiencia = 0;
  p_members[i].UserIndex = UserIndex;
  retval = true;
  p_CantMiembros = p_CantMiembros+1;
  p_SumaNivelesElevados = p_SumaNivelesElevados+(Declaraciones.UserList[UserIndex].Stats.ELV $ mdParty.ExponenteNivelParty);
  } else {
  retval = false;
 }
 
return retval;
}

	boolean SaleMiembro(int UserIndex) {
 boolean retval = false;
 /* '*************************************************** */
 /* 'Author: Unknown */
 /* 'Last Modification: 07/04/08 */
 /* 'Last Modification By: Marco Vanotti (MarKoxX) */
 /* ' - 09/29/07 Experience is round to the biggest number less than that number */
 /* ' - 09/29/07 Now experience is a real-number (Tavo) */
 /* ' - 07/04/08 Added const ExponenteNivelParty. (MarKoxX) */
 /* '11/03/2010: ZaMa - Ahora no le dice al lider que salio de su propia party, y optimice con with. */
 /* '*************************************************** */
 /* 'el valor de retorno representa si se disuelve la party */
 int i = 0;
 int j = 0;
 int MemberIndex = 0;
 
 i = 1;
 retval = false;
  while (i<=mdParty.PARTY_MAXMEMBERS && p_members[i].UserIndex != UserIndex) {
  i = i+1;
 }
 
  if (i == 1) {
  /* 'sale el founder, la party se disuelve */
  retval = true;
  MandarMensajeAConsola("El líder disuelve la party.", "Servidor");
  
   /* FIXME WEIRD FOR */
   for (j = (mdParty.PARTY_MAXMEMBERS); ((-1) > 0) ? (j <= (1)) : (j >= (1)); j = j + (-1)) {
   
     if (p_members[j].UserIndex>0) {
     
     /* ' No envia el mensaje al lider */
      if (j != 1) {
      Protocol.WriteConsoleMsg(p_members[j].UserIndex, "Abandonas la party liderada por " + Declaraciones.UserList[p_members[1].UserIndex].name + ".", FontTypeNames.FONTTYPE_PARTY);
     }
     
     Protocol.WriteConsoleMsg(p_members[j].UserIndex, "Durante la misma has conseguido " + vb6.CStr(vb6.Fix(p_members[j].Experiencia)) + " puntos de experiencia.", FontTypeNames.FONTTYPE_PARTY);
     
      if (!mdParty.PARTY_EXPERIENCIAPORGOLPE) {
      Declaraciones.UserList[p_members[j].UserIndex].Stats.Exp = Declaraciones.UserList[p_members[j].UserIndex].Stats.Exp+vb6.Fix(p_members[j].Experiencia);
      if (Declaraciones.UserList[p_members[j].UserIndex].Stats.Exp>Declaraciones.MAXEXP) {
      Declaraciones.UserList[p_members[j].UserIndex].Stats.Exp = Declaraciones.MAXEXP;
      }
      UsUaRiOs.CheckUserLevel(p_members[j].UserIndex);
      Protocol.WriteUpdateUserStats(p_members[j].UserIndex);
     }
     
     MandarMensajeAConsola(Declaraciones.UserList[p_members[j].UserIndex].name + " abandona la party.", "Servidor");
     
     Declaraciones.UserList[p_members[j].UserIndex].PartyIndex = 0;
     p_CantMiembros = p_CantMiembros-1;
     p_SumaNivelesElevados = p_SumaNivelesElevados-(Declaraciones.UserList[UserIndex].Stats.ELV $ mdParty.ExponenteNivelParty);
     p_members[j].UserIndex = 0;
     p_members[j].Experiencia = 0;
     
    }
    
  }
  } else {
   if (i<=mdParty.PARTY_MAXMEMBERS) {
   
   MemberIndex = p_members[i].UserIndex;
   
     if (!mdParty.PARTY_EXPERIENCIAPORGOLPE) {
     Declaraciones.UserList[MemberIndex].Stats.Exp = Declaraciones.UserList[MemberIndex].Stats.Exp+vb6.Fix(p_members[i].Experiencia);
     if (Declaraciones.UserList[MemberIndex].Stats.Exp>Declaraciones.MAXEXP) {
     Declaraciones.UserList[MemberIndex].Stats.Exp = Declaraciones.MAXEXP;
     }
     
     UsUaRiOs.CheckUserLevel(MemberIndex);
     Protocol.WriteUpdateUserStats(MemberIndex);
    }
    
    MandarMensajeAConsola(Declaraciones.UserList[MemberIndex].name + " abandona la party.", "Servidor");
    /* 'TODO: Revisar que esto este bien, y no este faltando/sobrando un mensaje, ahora solo los estoy corrigiendo */
    Protocol.WriteConsoleMsg(MemberIndex, "Durante la misma has conseguido " + vb6.CStr(vb6.Fix(p_members[i].Experiencia)) + " puntos de experiencia.", FontTypeNames.FONTTYPE_PARTY);
    
    p_CantMiembros = p_CantMiembros-1;
    p_SumaNivelesElevados = p_SumaNivelesElevados-(Declaraciones.UserList[UserIndex].Stats.ELV $ mdParty.ExponenteNivelParty);
    MemberIndex = 0;
    p_members[i].Experiencia = 0;
    p_members[i].UserIndex = 0;
    ACompactMemberList();
  }
 }
 
return retval;
}

	boolean HacerLeader(int UserIndex) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 09/29/07 */
		/* 'Last Modification By: Lucas Tavolaro Ortiz (Tavo) */
		/* ' - 09/29/07 There is no level prohibition */
		/* '*************************************************** */
		int i = 0;
		int OldLeader = 0;
		double oldExp = 0.0;
		int UserIndexIndex = 0;

		UserIndexIndex = 0;
		retval = true;

		for (i = (1); i <= (mdParty.PARTY_MAXMEMBERS); i++) {
			if (p_members[i].UserIndex > 0) {
				if (p_members[i].UserIndex == UserIndex) {
					UserIndexIndex = i;
				}
			}
		}

		if (!retval) {
			return retval;
		}

		if (UserIndexIndex == 0) {
			/*
			 * 'catastrofe! esto no deberia pasar nunca! pero como es AO.... :p
			 */
			General.LogError("INCONSISTENCIA DE PARTIES");
			modSendData.SendData(SendTarget.ToAdmins, 0,
					Protocol.PrepareMessageConsoleMsg(
							"¡¡¡Inconsistencia de parties en HACERLEADER (UII = 0), AVISE A UN PROGRAMADOR ESTO ES UNA CATASTROFE!!!!",
							FontTypeNames.FONTTYPE_GUILD));
			retval = false;
			return retval;
		}

		/* 'aca esta todo bien y doy vuelta las collections */
		OldLeader = p_members[1].UserIndex;
		oldExp = p_members[1].Experiencia;

		/*
		 * 'que en realdiad es el userindex, pero no quiero inconsistencias
		 * moviendo experiencias
		 */
		p_members[1].UserIndex = p_members[UserIndexIndex].UserIndex;
		p_members[1].Experiencia = p_members[UserIndexIndex].Experiencia;

		p_members[UserIndexIndex].UserIndex = OldLeader;
		p_members[UserIndexIndex].Experiencia = oldExp;

		p_Fundador = p_members[1].UserIndex;

		/* 'no need to compact */
		return retval;
	}

	void ObtenerMiembrosOnline(int[] /* FIXME BYREF!! */ MemberList) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 09/29/07 */
		/* 'Last Modification By: Marco Vanotti (MarKoxX) */
		/*
		 * ' - 09/29/07 Experience is round to the biggest number less than that
		 * number
		 */
		/* ' - 09/29/07 Now experience is a real-number (Tavo) */
		/* ' - 08/18/08 Now TotalExperience is fixed (MarKoxX) */
		/*
		 * ' - 11/27/09 Rehice la función, ahora devuelve el array con los UI
		 * online (Budi)
		 */
		/* '*************************************************** */

		int i = 0;

		for (i = (1); i <= (mdParty.PARTY_MAXMEMBERS); i++) {
			if (p_members[i].UserIndex > 0) {
				MemberList[i] = p_members[i].UserIndex;
			}
		}

	}

	int ObtenerExperienciaTotal() {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: Budi */
		/* 'Last Modification: 11/27/09 */
		/* 'Retrieves the total experience acumulated in the party */
		/* '*************************************************** */
		retval = p_expTotal;
		return retval;
	}

	boolean PuedeEntrar(int UserIndex, String /* FIXME BYREF!! */ razon) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 09/29/07 */
		/* 'Last Modification By: Lucas Tavolaro Ortiz (Tavo) */
		/* ' - 09/29/07 There is no level prohibition */
		/* '*************************************************** */
		/* 'DEFINE LAS REGLAS DEL JUEGO PARA DEJAR ENTRAR A MIEMBROS */
		boolean esArmada = false;
		boolean esCaos = false;
		int MyLevel = 0;
		int i = 0;
		boolean rv = false;
		int UI = 0;

		rv = true;
		esArmada = (Declaraciones.UserList[UserIndex].Faccion.ArmadaReal == 1);
		esCaos = (Declaraciones.UserList[UserIndex].Faccion.FuerzasCaos == 1);
		MyLevel = Declaraciones.UserList[UserIndex].Stats.ELV;

		rv = Matematicas.Distancia(Declaraciones.UserList[p_members[1].UserIndex].Pos,
				Declaraciones.UserList[UserIndex].Pos) <= mdParty.MAXDISTANCIAINGRESOPARTY;
		if (rv) {
			rv = (p_members[mdParty.PARTY_MAXMEMBERS].UserIndex == 0);
			if (rv) {
				for (i = (1); i <= (mdParty.PARTY_MAXMEMBERS); i++) {
					UI = p_members[i].UserIndex;
					/* 'pongo los casos que evitarian que pueda entrar */
					/* 'aspirante armada en party crimi */
					if (UI > 0) {
						if (esArmada && ES.criminal(UI)) {
							razon = "Los miembros del ejército real no entran a una party con criminales.";
							rv = false;
						}
						/* 'aspirante caos en party ciuda */
						if (esCaos && !ES.criminal(UI)) {
							razon = "Los miembros de la legión oscura no entran a una party con ciudadanos.";
							rv = false;
						}
						/* 'aspirante crimi en party armada */
						if (Declaraciones.UserList[UI].Faccion.ArmadaReal == 1 && ES.criminal(UserIndex)) {
							razon = "Los criminales no entran a parties con miembros del ejército real.";
							rv = false;
						}
						/* 'aspirante ciuda en party caos */
						if (Declaraciones.UserList[UI].Faccion.FuerzasCaos == 1 && !ES.criminal(UserIndex)) {
							razon = "Los ciudadanos no entran a parties con miembros de la legión oscura.";
							rv = false;
						}

						/* 'violate una programacion estructurada */
						if (!rv) {
							break; /* FIXME: EXIT FOR */
						}
					}
				}
			} else {
				razon = "La mayor cantidad de miembros es " + mdParty.PARTY_MAXMEMBERS;
			}
		} else {
			/* '¿Con o sin nombre? */
			razon = "El usuario " + Declaraciones.UserList[UserIndex].name + " se encuentra muy lejos.";
		}

		retval = rv;

		return retval;
	}

	void FlushExperiencia() {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 09/29/07 */
		/* 'Last Modification By: Lucas Tavolaro Ortiz (Tavo) */
		/*
		 * ' - 09/29/07 Experience is round to the biggest number less than that
		 * number
		 */
		/* ' - 09/29/07 Now experience is a real-number */
		/* '*************************************************** */
		/*
		 * 'esta funcion se invoca frente a cerradas del servidor. Flushea la
		 * experiencia
		 */
		/* 'acumulada a los usuarios. */

		int i = 0;
		/* 'esto sirve SOLO cuando acumulamos la experiencia! */
		if (!mdParty.PARTY_EXPERIENCIAPORGOLPE) {
			for (i = (1); i <= (mdParty.PARTY_MAXMEMBERS); i++) {
				if (p_members[i].UserIndex > 0) {
					if (p_members[i].Experiencia > 0) {
						Declaraciones.UserList[p_members[i].UserIndex].Stats.Exp = Declaraciones.UserList[p_members[i].UserIndex].Stats.Exp
								+ vb6.Fix(p_members[i].Experiencia);
						if (Declaraciones.UserList[p_members[i].UserIndex].Stats.Exp > Declaraciones.MAXEXP) {
							Declaraciones.UserList[p_members[i].UserIndex].Stats.Exp = Declaraciones.MAXEXP;
						}
						UsUaRiOs.CheckUserLevel(p_members[i].UserIndex);
					} else {
						if (vb6.Abs(Declaraciones.UserList[p_members[i].UserIndex].Stats.Exp) > vb6
								.Abs(vb6.Fix(p_members[i].Experiencia))) {
							Declaraciones.UserList[p_members[i].UserIndex].Stats.Exp = Declaraciones.UserList[p_members[i].UserIndex].Stats.Exp
									+ vb6.Fix(p_members[i].Experiencia);
						} else {
							Declaraciones.UserList[p_members[i].UserIndex].Stats.Exp = 0;
						}
					}
					p_members[i].Experiencia = 0;
					Protocol.WriteUpdateUserStats(p_members[i].UserIndex);
				}
			}
		}

	}

	void CompactMemberList() {
		int i = 0;
		int freeIndex = 0;
		i = 1;
		while (i <= mdParty.PARTY_MAXMEMBERS) {
			if (p_members[i].UserIndex == 0 && freeIndex == 0) {
				freeIndex = i;
			} else if (p_members[i].UserIndex > 0 && freeIndex > 0) {
				p_members[freeIndex].Experiencia = p_members[i].Experiencia;
				p_members[freeIndex].UserIndex = p_members[i].UserIndex;
				p_members[i].UserIndex = 0;
				p_members[i].Experiencia = 0;
				/* 'muevo el de la pos i a freeindex */
				i = freeIndex;
				freeIndex = 0;
			}
			i = i + 1;
		}

	}

	int CantMiembros() {
		int retval = 0;
		retval = p_CantMiembros;
		return retval;
	}

}