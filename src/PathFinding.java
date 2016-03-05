/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"PathFinding"')] */
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

/* '####################################################### */
/* 'PathFinding Module */
/* 'Coded By Gulfas Morgolock */
/* 'morgolock@speedy.com.ar */
/* 'www.geocities.com/gmorgolock */
/* ' */
/* 'Ore is an excellent engine for introducing you not only */
/* 'to online game programming but also to general */
/* 'game programming. I am convinced that Aaron Perkings, creator */
/* 'of ORE, did a great work. He made possible that a lot of */
/* 'people enjoy for no fee games made with his engine, and */
/* 'for me, this is something great. */
/* ' */
/* 'I'd really like to contribute to this work, and all the */
/* 'projects of free ore-based MMORPGs that are on the net. */
/* ' */
/* 'I did some basic improvements on the AI of the NPCs, I */
/* 'added pathfinding, so now, the npcs are able to avoid */
/* 'obstacles. I believe that this improvement was essential */
/* 'for the engine. */
/* ' */
/* 'I'd like to see this as my contribution to ORE project, */
/* 'I hope that someone finds this source code useful. */
/* 'So, please feel free to do whatever you want with my */
/* 'pathfinging module. */
/* ' */
/* 'I'd really appreciate that if you find this source code */
/* 'useful you mention my nickname on the credits of your */
/* 'program. But there is no obligation ;). */
/* ' */
/* '......................................................... */
/* 'Note: */
/* 'There is a little problem, ORE refers to map arrays in a */
/* 'different manner that my pathfinding routines. When I wrote */
/* 'these routines, I did it without thinking in ORE, so in my */
/* 'program I refer to maps in the usual way I do it. */
/* ' */
/* 'For example, suppose we have: */
/* 'Map(1 to Y,1 to X) as MapBlock */
/* 'I usually use the first coordinate as Y, and */
/* 'the second one as X. */
/* ' */
/* 'ORE refers to maps in converse way, for example: */
/* 'Map(1 to X,1 to Y) as MapBlock. As you can see the */
/* 'roles of first and second coordinates are different */
/* 'that my routines */
/* ' */
/* '####################################################### */

import enums.*;

public class PathFinding {

	static final int ROWS = 100;
	static final int COLUMS = 100;
	static final int MAXINT = 1000;

	static public class tIntermidiateWork {
		public int DistV;
		public Queue.tVertice PrevV;
	}

private static tIntermidiateWork[] TmpArray = new tIntermidiateWork[[('1', 'PathFinding.ROWS'), ('1', 'PathFinding.COLUMS')]]; /* XXX MULTIDIMENSIONAL [('1', 'PathFinding.ROWS'), ('1', 'PathFinding.COLUMS')] */

	static boolean Limites(int vfila, int vcolu) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		retval = ((vcolu >= 1) && (vcolu <= PathFinding.COLUMS) && (vfila >= 1) && (vfila <= PathFinding.ROWS));
		return retval;
	}

	static boolean IsWalkable(int Map, int row, int Col, int NpcIndex) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		retval = ((Declaraciones.MapData[Map][row][Col].Blocked || Declaraciones.MapData[Map][row][Col].NpcIndex) == 0);

		if (Declaraciones.MapData[Map][row][Col].UserIndex != 0) {
			if (Declaraciones.MapData[Map][row][Col].UserIndex != Declaraciones.Npclist[NpcIndex].PFINFO.TargetUser) {
				retval = false;
			}
		}

		return retval;
	}

	static void ProcessAdjacents(int MapIndex,
			tIntermidiateWork[] /* FIXME BYREF!! */ T, int /* FIXME BYREF!! */ vfila,
			int /* FIXME BYREF!! */ vcolu, int NpcIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		Queue.tVertice V;
		int j = 0;

		/* 'Look to North */
		j = vfila - 1;
		if (Limites(j, vcolu)) {
			if (IsWalkable(MapIndex, j, vcolu, NpcIndex)) {
				/* 'Nos aseguramos que no hay un camino más corto */
				if (T[j][vcolu].DistV == PathFinding.MAXINT) {
					/* 'Actualizamos la tabla de calculos intermedios */
					T[j][vcolu].DistV = T[vfila][vcolu].DistV + 1;
					T[j][vcolu].PrevV.X = vcolu;
					T[j][vcolu].PrevV.Y = vfila;
					/* 'Mete el vertice en la cola */
					V.X = vcolu;
					V.Y = j;
					Queue.Push(V);
				}
			}
		}

		j = vfila + 1;
		/* 'look to south */
		if (Limites(j, vcolu)) {
			if (IsWalkable(MapIndex, j, vcolu, NpcIndex)) {
				/* 'Nos aseguramos que no hay un camino más corto */
				if (T[j][vcolu].DistV == PathFinding.MAXINT) {
					/* 'Actualizamos la tabla de calculos intermedios */
					T[j][vcolu].DistV = T[vfila][vcolu].DistV + 1;
					T[j][vcolu].PrevV.X = vcolu;
					T[j][vcolu].PrevV.Y = vfila;
					/* 'Mete el vertice en la cola */
					V.X = vcolu;
					V.Y = j;
					Queue.Push(V);
				}
			}
		}

		j = vcolu - 1;
		/* 'look to west */
		if (Limites(vfila, j)) {
			if (IsWalkable(MapIndex, vfila, j, NpcIndex)) {
				/* 'Nos aseguramos que no hay un camino más corto */
				if (T[vfila][j].DistV == PathFinding.MAXINT) {
					/* 'Actualizamos la tabla de calculos intermedios */
					T[vfila][j].DistV = T[vfila][vcolu].DistV + 1;
					T[vfila][j].PrevV.X = vcolu;
					T[vfila][j].PrevV.Y = vfila;
					/* 'Mete el vertice en la cola */
					V.X = j;
					V.Y = vfila;
					Queue.Push(V);
				}
			}
		}

		j = vcolu + 1;
		/* 'look to east */
		if (Limites(vfila, j)) {
			if (IsWalkable(MapIndex, vfila, j, NpcIndex)) {
				/* 'Nos aseguramos que no hay un camino más corto */
				if (T[vfila][j].DistV == PathFinding.MAXINT) {
					/* 'Actualizamos la tabla de calculos intermedios */
					T[vfila][j].DistV = T[vfila][vcolu].DistV + 1;
					T[vfila][j].PrevV.X = vcolu;
					T[vfila][j].PrevV.Y = vfila;
					/* 'Mete el vertice en la cola */
					V.X = j;
					V.Y = vfila;
					Queue.Push(V);
				}
			}
		}

	}

	static void SeekPath(int NpcIndex) {
		SeekPath(NpcIndex, 30);
	}

	static void SeekPath(int NpcIndex, int MaxSteps) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* 'This Sub seeks a path from the npclist(npcindex).pos */
		/* 'to the location NPCList(NpcIndex).PFINFO.Target. */
		/* 'The optional parameter MaxSteps is the maximum of steps */
		/* 'allowed for the path. */
		/* '*************************************************** */

		Queue.tVertice cur_npc_pos;
		Queue.tVertice tar_npc_pos;
		Queue.tVertice V;
		int NpcMap = 0;
		int steps = 0;

		NpcMap = Declaraciones.Npclist[NpcIndex].Pos.Map;

		cur_npc_pos.X = Declaraciones.Npclist[NpcIndex].Pos.Y;
		cur_npc_pos.Y = Declaraciones.Npclist[NpcIndex].Pos.X;

		/* ' UserList(.PFINFO.TargetUser).Pos.X */
		tar_npc_pos.X = Declaraciones.Npclist[NpcIndex].PFINFO.Target.X;
		/* ' UserList(.PFINFO.TargetUser).Pos.Y */
		tar_npc_pos.Y = Declaraciones.Npclist[NpcIndex].PFINFO.Target.Y;

		InitializeTable(TmpArray, cur_npc_pos);
		Queue.InitQueue();

		/* 'We add the first vertex to the Queue */
		Queue.Push(cur_npc_pos);

		while ((!Queue.IsEmpty())) {
			if (steps > MaxSteps) {
				break; /* FIXME: EXIT DO */
			}
			V = Queue.Pop();
			if ((V.X == tar_npc_pos.X) && (V.Y == tar_npc_pos.Y)) {
				break; /* FIXME: EXIT DO */
			}
			ProcessAdjacents(NpcMap, TmpArray, V.Y, V.X, NpcIndex);
		}

		MakePath(NpcIndex);
	}

	static void MakePath(int NpcIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* 'Builds the path previously calculated */
		/* '*************************************************** */

		int Pasos = 0;
		Queue.tVertice miV;
		int i = 0;

		Pasos = TmpArray[Declaraciones.Npclist[NpcIndex].PFINFO.Target.Y][Declaraciones.Npclist[NpcIndex].PFINFO.Target.X].DistV;
		Declaraciones.Npclist[NpcIndex].PFINFO.PathLenght = Pasos;

		if (Pasos == PathFinding.MAXINT) {
			/* 'MsgBox "There is no path." */
			Declaraciones.Npclist[NpcIndex].PFINFO.NoPath = true;
			Declaraciones.Npclist[NpcIndex].PFINFO.PathLenght = 0;
			return;
		}

		Declaraciones.Npclist[NpcIndex].PFINFO.Path = new tVertice[0];
		Declaraciones.Npclist[NpcIndex].PFINFO.Path = (Declaraciones.Npclist[NpcIndex].PFINFO.Path == null)
				? new tVertice[1 + Pasos]
				: java.util.Arrays.copyOf(Declaraciones.Npclist[NpcIndex].PFINFO.Path, 1 + Pasos);

		miV.X = Declaraciones.Npclist[NpcIndex].PFINFO.Target.X;
		miV.Y = Declaraciones.Npclist[NpcIndex].PFINFO.Target.Y;

		/* FIXME WEIRD FOR */
		for (i = (Pasos); ((-1) > 0) ? (i <= (1)) : (i >= (1)); i = i + (-1)) {
			Declaraciones.Npclist[NpcIndex].PFINFO.Path[i] = miV;
			miV = TmpArray[miV.Y][miV.X].PrevV;
		}

		Declaraciones.Npclist[NpcIndex].PFINFO.CurPos = 1;
		Declaraciones.Npclist[NpcIndex].PFINFO.NoPath = false;

	}

	static void InitializeTable(tIntermidiateWork[] /* FIXME BYREF!! */ T,
			Queue.tVertice /* FIXME BYREF!! */ S) {
		InitializeTable(T, S, 30);
	}

	static void InitializeTable(tIntermidiateWork[] /* FIXME BYREF!! */ T, Queue.tVertice /* FIXME BYREF!! */ S, int MaxSteps) {
 /* '*************************************************** */
 /* 'Author: Unknown */
 /* 'Last Modification: - */
 /* 'Initialize the array where we calculate the path */
 /* '*************************************************** */
 
 int j = 0;
 int k = 0;

	static final int anymap = 1;

	for(j=(S.Y-MaxSteps);j<=(S.Y+MaxSteps);j++)
	{
		for (k = (S.X - MaxSteps); k <= (S.X + MaxSteps); k++) {
			if (Extra.InMapBounds(PathFinding.anymap, j, k)) {
				T[j][k].DistV = PathFinding.MAXINT;
				T[j][k].PrevV.X = 0;
				T[j][k].PrevV.Y = 0;
			}
		}
	}

	T[S.Y][S.X].DistV=0;

}

}