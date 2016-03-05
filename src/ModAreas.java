/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"ModAreas"')] */
/* '************************************************************** */
/* ' ModAreas.bas - Module to allow the usage of areas instead of maps. */
/* ' Saves a lot of bandwidth. */
/* ' */
/* ' Original Idea by Juan Martín Sotuyo Dodero (Maraxus) */
/* ' (juansotuyo@gmail.com) */
/* ' Implemented by Lucio N. Tourrilhes (DuNga) */
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

/* ' Modulo de envio por areas compatible con la versión 9.10.x ... By DuNga */

import enums.*;

public class ModAreas {

	/* '>>>>>>AREAS>>>>>AREAS>>>>>>>>AREAS>>>>>>>AREAS>>>>>>>>>> */
	static public class AreaInfo {
		int AreaPerteneceX;
		int AreaPerteneceY;

		int AreaReciveX;
		int AreaReciveY;

		int MinX;
		int MinY;

		int AreaID;
	}

	static public class ConnGroup {
		int CountEntrys;
		int OptValue;
		int[] UserEntrys;
	}

	static final int USER_NUEVO = 255;

	/* 'Cuidado: */
	/* ' ¡¡¡LAS AREAS ESTÁN HARDCODEADAS!!! */
	private static int CurDay;
	private static int CurHour;

private static int[] AreasInfo = new int[[('1', '100'), ('1', '100')]]; /* XXX MULTIDIMENSIONAL [('1', '100'), ('1', '100')] */
	private static int[] PosToArea = new int[1 + 100]; /* XXX */

	private static int[] AreasRecive = new int[0 + 12]; /* XXX */

	public static ConnGroup[] ConnGroups = new ConnGroup[0]; /* XXX */

	static void InitAreas() {
 /* '************************************************************** */
 /* 'Author: Lucio N. Tourrilhes (DuNga) */
 /* 'Last Modify Date: Unknow */
 /* ' */
 /* '************************************************************** */
 int LoopC;
 int loopX;
 
 /* ' Setup areas... */
  for (LoopC = (0); LoopC <= (11); LoopC++) {
  AreasRecive[LoopC] = (2 $ LoopC) || vb6.IIf(LoopC != 0, 2 $ (LoopC-1), 0) || vb6.IIf(LoopC != 11, 2 $ (LoopC+1), 0);
 }
 
  for (LoopC = (1); LoopC <= (100); LoopC++) {
  PosToArea[LoopC] = LoopC/9;
 }
 
  for (LoopC = (1); LoopC <= (100); LoopC++) {
   for (loopX = (1); loopX <= (100); loopX++) {
   /* 'Usamos 121 IDs de area para saber si pasasamos de area "más rápido" */
   AreasInfo[LoopC][loopX] = (LoopC/9+1)*(loopX/9+1);
  }
 }
 
 /* 'Setup AutoOptimizacion de areas */
 /* 'A ke tipo de dia pertenece? */
 CurDay = vb6.IIf(vb6.Weekday(Date)>6, 1, 2);
 /* 'A ke parte de la hora pertenece */
 CurHour = vb6.Fix(vb6.Hour(vb6.time())/3);
 
 ModAreas.ConnGroups = new ConnGroup[0];
 ModAreas.ConnGroups = (ModAreas.ConnGroups == null) ? new ConnGroup[1 + Declaraciones.NumMaps] : java.util.Arrays.copyOf(ModAreas.ConnGroups, 1 + Declaraciones.NumMaps);
 
  for (LoopC = (1); LoopC <= (Declaraciones.NumMaps); LoopC++) {
  ModAreas.ConnGroups[LoopC].OptValue = vb6.val(ES.GetVar(Declaraciones.DatPath + "AreasStats.dat", "Mapa" + LoopC, CurDay + "-" + CurHour));
  
  if (ModAreas.ConnGroups[LoopC].OptValue == 0) {
  ModAreas.ConnGroups[LoopC].OptValue = 1;
  }
  ModAreas.ConnGroups[LoopC].UserEntrys = new Long[0];
  ModAreas.ConnGroups[LoopC].UserEntrys = (ModAreas.ConnGroups[LoopC].UserEntrys == null) ? new Long[1 + ModAreas.ConnGroups[LoopC].OptValue] : java.util.Arrays.copyOf(ModAreas.ConnGroups[LoopC].UserEntrys, 1 + ModAreas.ConnGroups[LoopC].OptValue);
 }
}

	static void AreasOptimizacion() {
		/* '************************************************************** */
		/* 'Author: Lucio N. Tourrilhes (DuNga) */
		/* 'Last Modify Date: Unknow */
		/*
		 * 'Es la función de autooptimizacion.... la idea es no mandar
		 * redimensionando arrays grandes todo el tiempo
		 */
		/* '************************************************************** */
		int LoopC;
		int tCurDay;
		int tCurHour;
		int EntryValue;

		if ((CurDay != vb6.IIf(vb6.Weekday(Date) > 6, 1, 2)) || (CurHour != vb6.Fix(vb6.Hour(vb6.time()) / 3))) {

			/* 'A ke tipo de dia pertenece? */
			tCurDay = vb6.IIf(vb6.Weekday(Date) > 6, 1, 2);
			/* 'A ke parte de la hora pertenece */
			tCurHour = vb6.Fix(vb6.Hour(vb6.time()) / 3);

			for (LoopC = (1); LoopC <= (Declaraciones.NumMaps); LoopC++) {
				EntryValue = vb6.val(
						ES.GetVar(Declaraciones.DatPath + "AreasStats.dat", "Mapa" + LoopC, CurDay + "-" + CurHour));
				ES.WriteVar(Declaraciones.DatPath + "AreasStats.dat", "Mapa" + LoopC, CurDay + "-" + CurHour,
						vb6.CInt((EntryValue + ModAreas.ConnGroups[LoopC].OptValue) / 2));

				ModAreas.ConnGroups[LoopC].OptValue = vb6.val(
						ES.GetVar(Declaraciones.DatPath + "AreasStats.dat", "Mapa" + LoopC, tCurDay + "-" + tCurHour));
				if (ModAreas.ConnGroups[LoopC].OptValue == 0) {
					ModAreas.ConnGroups[LoopC].OptValue = 1;
				}
				if (ModAreas.ConnGroups[LoopC].OptValue >= Declaraciones.MapInfo[LoopC].NumUsers) {
					ModAreas.ConnGroups[LoopC].UserEntrys = (ModAreas.ConnGroups[LoopC].UserEntrys == null)
							? new Long[1 + ModAreas.ConnGroups[LoopC].OptValue]
							: java.util.Arrays.copyOf(ModAreas.ConnGroups[LoopC].UserEntrys,
									1 + ModAreas.ConnGroups[LoopC].OptValue);
				}
			}

			CurDay = tCurDay;
			CurHour = tCurHour;
		}
	}

	static void CheckUpdateNeededUser(int UserIndex, int Head) {
		CheckUpdateNeededUser(UserIndex, Head, false);
	}

	static void CheckUpdateNeededUser(int UserIndex, int Head, boolean ButIndex) {
 /* '************************************************************** */
 /* 'Author: Lucio N. Tourrilhes (DuNga) */
 /* 'Last Modify Date: 28/10/2010 */
 /* 'Es la función clave del sistema de areas... Es llamada al mover un user */
 /* '15/07/2009: ZaMa - Now it doesn't send an invisible admin char info */
 /* '28/10/2010: ZaMa - Now it doesn't send a saling char invisible message. */
 /* '************************************************************** */
 if (Declaraciones.UserList[UserIndex].AreasInfo.AreaID == AreasInfo[Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y]) {
 return;
 }
 
 int MinX;
 int MaxX;
 int MinY;
 int MaxY;
 int X;
 int Y;
 int TempInt;
 int Map;
 
  MinX = Declaraciones.UserList[UserIndex].AreasInfo.MinX;
  MinY = Declaraciones.UserList[UserIndex].AreasInfo.MinY;
  
   if (Head == eHeading.NORTH) {
   MaxY = MinY-1;
   MinY = MinY-9;
   MaxX = MinX+26;
   Declaraciones.UserList[UserIndex].AreasInfo.MinX = vb6.CInt(MinX);
   Declaraciones.UserList[UserIndex].AreasInfo.MinY = vb6.CInt(MinY);
   
   } else if (Head == eHeading.SOUTH) {
   MaxY = MinY+35;
   MinY = MinY+27;
   MaxX = MinX+26;
   Declaraciones.UserList[UserIndex].AreasInfo.MinX = vb6.CInt(MinX);
   Declaraciones.UserList[UserIndex].AreasInfo.MinY = vb6.CInt(MinY-18);
   
   } else if (Head == eHeading.WEST) {
   MaxX = MinX-1;
   MinX = MinX-9;
   MaxY = MinY+26;
   Declaraciones.UserList[UserIndex].AreasInfo.MinX = vb6.CInt(MinX);
   Declaraciones.UserList[UserIndex].AreasInfo.MinY = vb6.CInt(MinY);
   
   } else if (Head == eHeading.EAST) {
   MaxX = MinX+35;
   MinX = MinX+27;
   MaxY = MinY+26;
   Declaraciones.UserList[UserIndex].AreasInfo.MinX = vb6.CInt(MinX-18);
   Declaraciones.UserList[UserIndex].AreasInfo.MinY = vb6.CInt(MinY);
   
   } else if (Head == ModAreas.USER_NUEVO) {
   /* 'Esto pasa por cuando cambiamos de mapa o logeamos... */
   MinY = ((Declaraciones.UserList[UserIndex].Pos.Y/9)-1)*9;
   MaxY = MinY+26;
   
   MinX = ((Declaraciones.UserList[UserIndex].Pos.X/9)-1)*9;
   MaxX = MinX+26;
   
   Declaraciones.UserList[UserIndex].AreasInfo.MinX = vb6.CInt(MinX);
   Declaraciones.UserList[UserIndex].AreasInfo.MinY = vb6.CInt(MinY);
  }
  
  if (MinY<1) {
  MinY = 1;
  }
  if (MinX<1) {
  MinX = 1;
  }
  if (MaxY>100) {
  MaxY = 100;
  }
  if (MaxX>100) {
  MaxX = 100;
  }
  
  Map = Declaraciones.UserList[UserIndex].Pos.Map;
  
  /* 'Esto es para ke el cliente elimine lo "fuera de area..." */
  Protocol.WriteAreaChanged(UserIndex);
  
  /* 'Actualizamos!!! */
   for (X = (MinX); X <= (MaxX); X++) {
    for (Y = (MinY); Y <= (MaxY); Y++) {
    
    /* '<<< User >>> */
     if (Declaraciones.MapData[Map][X][Y].UserIndex) {
     
     TempInt = Declaraciones.MapData[Map][X][Y].UserIndex;
     
      if (UserIndex != TempInt) {
      
      /* ' Solo avisa al otro cliente si no es un admin invisible */
       if (!(Declaraciones.UserList[TempInt].flags.AdminInvisible == 1)) {
        if (UsUaRiOs.MakeUserChar(false, UserIndex, TempInt, Map, X, Y)) {
        /* ' Si esta navegando, siempre esta visible */
         if (Declaraciones.UserList[TempInt].flags.Navegando == 0) {
         /* 'Si el user estaba invisible le avisamos al nuevo cliente de eso */
          if (Declaraciones.UserList[TempInt].flags.invisible || Declaraciones.UserList[TempInt].flags.Oculto) {
           if (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.User || PlayerType.Consejero || PlayerType.RoleMaster)) {
           Protocol.WriteSetInvisible(UserIndex, Declaraciones.UserList[TempInt].Char.CharIndex, true);
          }
         }
        }
       }
      }
      
      /* ' Solo avisa al otro cliente si no es un admin invisible */
       if (!(Declaraciones.UserList[UserIndex].flags.AdminInvisible == 1)) {
        if (UsUaRiOs.MakeUserChar(false, TempInt, UserIndex, Declaraciones.UserList[UserIndex].Pos.Map, Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y)) {
        
        /* ' Si esta navegando, siempre esta visible */
         if (Declaraciones.UserList[UserIndex].flags.Navegando == 0) {
          if (Declaraciones.UserList[UserIndex].flags.invisible || Declaraciones.UserList[UserIndex].flags.Oculto) {
           if (Declaraciones.UserList[TempInt].flags.Privilegios && PlayerType.User) {
           Protocol.WriteSetInvisible(TempInt, Declaraciones.UserList[UserIndex].Char.CharIndex, true);
          }
         }
        }
       }
      }
      
      Protocol.FlushBuffer(TempInt);
      
      } else if (Head == ModAreas.USER_NUEVO) {
       if (!ButIndex) {
       UsUaRiOs.MakeUserChar(false, UserIndex, UserIndex, Map, X, Y);
      }
     }
    }
    
    /* '<<< Npc >>> */
     if (Declaraciones.MapData[Map][X][Y].NpcIndex) {
     NPCs.MakeNPCChar(false, UserIndex, Declaraciones.MapData[Map][X][Y].NpcIndex, Map, X, Y);
    }
    
    /* '<<< Item >>> */
     if (Declaraciones.MapData[Map][X][Y].ObjInfo.ObjIndex) {
     TempInt = Declaraciones.MapData[Map][X][Y].ObjInfo.ObjIndex;
      if (!Extra.EsObjetoFijo(Declaraciones.ObjData[TempInt].OBJType)) {
      Protocol.WriteObjectCreate(UserIndex, Declaraciones.ObjData[TempInt].GrhIndex, X, Y);
      
       if (Declaraciones.ObjData[TempInt].OBJType == eOBJType.otPuertas) {
       General.Bloquear(false, UserIndex, X, Y, Declaraciones.MapData[Map][X][Y].Blocked);
       General.Bloquear(false, UserIndex, X-1, Y, Declaraciones.MapData[Map][X-1][Y].Blocked);
      }
     }
    }
    
   }
  }
  
  /* 'Precalculados :P */
  TempInt = Declaraciones.UserList[UserIndex].Pos.X/9;
  Declaraciones.UserList[UserIndex].AreasInfo.AreaReciveX = AreasRecive[TempInt];
  Declaraciones.UserList[UserIndex].AreasInfo.AreaPerteneceX = 2 $ TempInt;
  
  TempInt = Declaraciones.UserList[UserIndex].Pos.Y/9;
  Declaraciones.UserList[UserIndex].AreasInfo.AreaReciveY = AreasRecive[TempInt];
  Declaraciones.UserList[UserIndex].AreasInfo.AreaPerteneceY = 2 $ TempInt;
  
  Declaraciones.UserList[UserIndex].AreasInfo.AreaID = AreasInfo[Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y];
}

	static void CheckUpdateNeededNpc(int NpcIndex, int Head) {
 /* '************************************************************** */
 /* 'Author: Lucio N. Tourrilhes (DuNga) */
 /* 'Last Modify Date: Unknow */
 /* ' Se llama cuando se mueve un Npc */
 /* '************************************************************** */
 if (Declaraciones.Npclist[NpcIndex].AreasInfo.AreaID == AreasInfo[Declaraciones.Npclist[NpcIndex].Pos.X][Declaraciones.Npclist[NpcIndex].Pos.Y]) {
 return;
 }
 
 int MinX;
 int MaxX;
 int MinY;
 int MaxY;
 int X;
 int Y;
 int TempInt;
 
  MinX = Declaraciones.Npclist[NpcIndex].AreasInfo.MinX;
  MinY = Declaraciones.Npclist[NpcIndex].AreasInfo.MinY;
  
   if (Head == eHeading.NORTH) {
   MaxY = MinY-1;
   MinY = MinY-9;
   MaxX = MinX+26;
   Declaraciones.Npclist[NpcIndex].AreasInfo.MinX = vb6.CInt(MinX);
   Declaraciones.Npclist[NpcIndex].AreasInfo.MinY = vb6.CInt(MinY);
   
   } else if (Head == eHeading.SOUTH) {
   MaxY = MinY+35;
   MinY = MinY+27;
   MaxX = MinX+26;
   Declaraciones.Npclist[NpcIndex].AreasInfo.MinX = vb6.CInt(MinX);
   Declaraciones.Npclist[NpcIndex].AreasInfo.MinY = vb6.CInt(MinY-18);
   
   } else if (Head == eHeading.WEST) {
   MaxX = MinX-1;
   MinX = MinX-9;
   MaxY = MinY+26;
   Declaraciones.Npclist[NpcIndex].AreasInfo.MinX = vb6.CInt(MinX);
   Declaraciones.Npclist[NpcIndex].AreasInfo.MinY = vb6.CInt(MinY);
   
   } else if (Head == eHeading.EAST) {
   MaxX = MinX+35;
   MinX = MinX+27;
   MaxY = MinY+26;
   Declaraciones.Npclist[NpcIndex].AreasInfo.MinX = vb6.CInt(MinX-18);
   Declaraciones.Npclist[NpcIndex].AreasInfo.MinY = vb6.CInt(MinY);
   
   } else if (Head == ModAreas.USER_NUEVO) {
   /* 'Esto pasa por cuando cambiamos de mapa o logeamos... */
   MinY = ((Declaraciones.Npclist[NpcIndex].Pos.Y/9)-1)*9;
   MaxY = MinY+26;
   
   MinX = ((Declaraciones.Npclist[NpcIndex].Pos.X/9)-1)*9;
   MaxX = MinX+26;
   
   Declaraciones.Npclist[NpcIndex].AreasInfo.MinX = vb6.CInt(MinX);
   Declaraciones.Npclist[NpcIndex].AreasInfo.MinY = vb6.CInt(MinY);
  }
  
  if (MinY<1) {
  MinY = 1;
  }
  if (MinX<1) {
  MinX = 1;
  }
  if (MaxY>100) {
  MaxY = 100;
  }
  if (MaxX>100) {
  MaxX = 100;
  }
  
  /* 'Actualizamos!!! */
   if (Declaraciones.MapInfo[Declaraciones.Npclist[NpcIndex].Pos.Map].NumUsers != 0) {
    for (X = (MinX); X <= (MaxX); X++) {
     for (Y = (MinY); Y <= (MaxY); Y++) {
     if (Declaraciones.MapData[Declaraciones.Npclist[NpcIndex].Pos.Map][X][Y].UserIndex) {
     NPCs.MakeNPCChar(false, Declaraciones.MapData[Declaraciones.Npclist[NpcIndex].Pos.Map][X][Y].UserIndex, NpcIndex, Declaraciones.Npclist[NpcIndex].Pos.Map, Declaraciones.Npclist[NpcIndex].Pos.X, Declaraciones.Npclist[NpcIndex].Pos.Y);
     }
    }
   }
  }
  
  /* 'Precalculados :P */
  TempInt = Declaraciones.Npclist[NpcIndex].Pos.X/9;
  Declaraciones.Npclist[NpcIndex].AreasInfo.AreaReciveX = AreasRecive[TempInt];
  Declaraciones.Npclist[NpcIndex].AreasInfo.AreaPerteneceX = 2 $ TempInt;
  
  TempInt = Declaraciones.Npclist[NpcIndex].Pos.Y/9;
  Declaraciones.Npclist[NpcIndex].AreasInfo.AreaReciveY = AreasRecive[TempInt];
  Declaraciones.Npclist[NpcIndex].AreasInfo.AreaPerteneceY = 2 $ TempInt;
  
  Declaraciones.Npclist[NpcIndex].AreasInfo.AreaID = AreasInfo[Declaraciones.Npclist[NpcIndex].Pos.X][Declaraciones.Npclist[NpcIndex].Pos.Y];
}

	static void QuitarUser(int UserIndex, int Map) {
		/* '************************************************************** */
		/* 'Author: Lucio N. Tourrilhes (DuNga) */
		/* 'Last Modify Date: Unknow */
		/* ' */
		/* '************************************************************** */
		/* FIXME: ON ERROR GOTO ErrorHandler */

		int TempVal;
		int LoopC;

		/* 'Search for the user */
		for (LoopC = (1); LoopC <= (ModAreas.ConnGroups[Map].CountEntrys); LoopC++) {
			if (ModAreas.ConnGroups[Map].UserEntrys[LoopC] == UserIndex) {
				break; /* FIXME: EXIT FOR */
			}
		}

		/* 'Char not found */
		if (LoopC > ModAreas.ConnGroups[Map].CountEntrys) {
			return;
		}

		/* 'Remove from old map */
		ModAreas.ConnGroups[Map].CountEntrys = ModAreas.ConnGroups[Map].CountEntrys - 1;
		TempVal = ModAreas.ConnGroups[Map].CountEntrys;

		/* 'Move list back */
		for (LoopC = (LoopC); LoopC <= (TempVal); LoopC++) {
			ModAreas.ConnGroups[Map].UserEntrys[LoopC] = ModAreas.ConnGroups[Map].UserEntrys[LoopC + 1];
		}

		/* 'Nescesito Redim? */
		if (TempVal > ModAreas.ConnGroups[Map].OptValue) {
			ModAreas.ConnGroups[Map].UserEntrys = (ModAreas.ConnGroups[Map].UserEntrys == null) ? new Long[1 + TempVal]
					: java.util.Arrays.copyOf(ModAreas.ConnGroups[Map].UserEntrys, 1 + TempVal);
		}

		return;

		/* FIXME: ErrorHandler : */

		String UserName;
		if (UserIndex > 0) {
			UserName = Declaraciones.UserList[UserIndex].Name;
		}

		General.LogError("Error en QuitarUser " + Err.Number + ": " + Err.description + ". User: " + UserName + "("
				+ UserIndex + "). Map: " + Map);

	}

	static void AgregarUser(int UserIndex, int Map) {
		AgregarUser(UserIndex, Map, false);
	}

	static void AgregarUser(int UserIndex, int Map, boolean ButIndex) {
		/* '************************************************************** */
		/* 'Author: Lucio N. Tourrilhes (DuNga) */
		/* 'Last Modify Date: 04/01/2007 */
		/* 'Modified by Juan Martín Sotuyo Dodero (Maraxus) */
		/*
		 * ' - Now the method checks for repetead users instead of trusting
		 * parameters.
		 */
		/* ' - If the character is new to the map, update it */
		/* '************************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		int TempVal;
		boolean EsNuevo;
		int i;

		if (!General.MapaValido(Map)) {
			return;
		}

		EsNuevo = true;

		/* 'Prevent adding repeated users */
		for (i = (1); i <= (ModAreas.ConnGroups[Map].CountEntrys); i++) {
			if (ModAreas.ConnGroups[Map].UserEntrys[i] == UserIndex) {
				EsNuevo = false;
				break; /* FIXME: EXIT FOR */
			}
		}

		if (EsNuevo) {
			/* 'Update map and connection groups data */
			ModAreas.ConnGroups[Map].CountEntrys = ModAreas.ConnGroups[Map].CountEntrys + 1;
			TempVal = ModAreas.ConnGroups[Map].CountEntrys;

			/* 'Nescesito Redim */
			if (TempVal > ModAreas.ConnGroups[Map].OptValue) {
				ModAreas.ConnGroups[Map].UserEntrys = (ModAreas.ConnGroups[Map].UserEntrys == null)
						? new Long[1 + TempVal]
						: java.util.Arrays.copyOf(ModAreas.ConnGroups[Map].UserEntrys, 1 + TempVal);
			}

			ModAreas.ConnGroups[Map].UserEntrys[TempVal] = UserIndex;
		}

		/* 'Update user */
		Declaraciones.UserList[UserIndex].AreasInfo.AreaID = 0;

		Declaraciones.UserList[UserIndex].AreasInfo.AreaPerteneceX = 0;
		Declaraciones.UserList[UserIndex].AreasInfo.AreaPerteneceY = 0;
		Declaraciones.UserList[UserIndex].AreasInfo.AreaReciveX = 0;
		Declaraciones.UserList[UserIndex].AreasInfo.AreaReciveY = 0;

		CheckUpdateNeededUser(UserIndex, ModAreas.USER_NUEVO, ButIndex);

		return;
		/* FIXME: ErrHandler : */
		General.LogError("AgregarUser: num: " + Err.Number + " desc: " + Err.description + ".UserIndex: " + UserIndex
				+ ". Map: " + Map + ". ButIndex: " + ButIndex);
	}

	static void AgregarNpc(int NpcIndex) {
		/* '************************************************************** */
		/* 'Author: Lucio N. Tourrilhes (DuNga) */
		/* 'Last Modify Date: Unknow */
		/* ' */
		/* '************************************************************** */
		Declaraciones.Npclist[NpcIndex].AreasInfo.AreaID = 0;

		Declaraciones.Npclist[NpcIndex].AreasInfo.AreaPerteneceX = 0;
		Declaraciones.Npclist[NpcIndex].AreasInfo.AreaPerteneceY = 0;
		Declaraciones.Npclist[NpcIndex].AreasInfo.AreaReciveX = 0;
		Declaraciones.Npclist[NpcIndex].AreasInfo.AreaReciveY = 0;

		CheckUpdateNeededNpc(NpcIndex, ModAreas.USER_NUEVO);
	}

}