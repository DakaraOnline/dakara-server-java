/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"Statistics"')] */
/* '************************************************************** */
/* ' modStatistics.bas - Takes statistics on the game for later study. */
/* ' */
/* ' Implemented by Juan Martín Sotuyo Dodero (Maraxus) */
/* ' (juansotuyo@gmail.com) */
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

public class Statistics {

	static public class trainningData {
		public int startTick;
		public int trainningTime;
	}

	static public class fragLvlRace {
		/*
		 * FIXME: MULTIDIMENSIONAL ARRAY 2: matrix ( 1 TO 50 , 1 TO 5 ) AS Long
		 */
		public int matrix;
	}

	static public class fragLvlLvl {
		/*
		 * FIXME: MULTIDIMENSIONAL ARRAY 2: matrix ( 1 TO 50 , 1 TO 50 ) AS Long
		 */
		public int matrix;
	}

	private static trainningData[] trainningInfo = new trainningData[0];

	private static fragLvlRace[] fragLvlRaceData = new fragLvlRace[1 + 7];
	private static fragLvlLvl[] fragLvlLvlData = new fragLvlLvl[1 + 7];
private static int[] fragAlignmentLvlData = new int[[('1', '50'), ('1', '4')]]; /* XXX MULTIDIMENSIONAL [('1', '50'), ('1', '4')] */

/* 'Currency just in case.... chats are way TOO often... */
	private static double[] keyOcurrencies = new double[0 + 255];

	static void Initialize() {
		trainningInfo = new trainningData[0];
		trainningInfo = (trainningInfo == null) ? new trainningData[1 + Declaraciones.MaxUsers]
				: java.util.Arrays.copyOf(trainningInfo, 1 + Declaraciones.MaxUsers);
	}

	static void UserConnected(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* 'A new user connected, load it's trainning time count */
		trainningInfo[UserIndex].trainningTime = vb6
				.val(ES.GetVar(Declaraciones.CharPath + vb6.UCase(Declaraciones.UserList[UserIndex].Name) + ".chr",
						"RESEARCH", "TrainningTime", 30));

		trainningInfo[UserIndex].startTick = (Declaraciones.GetTickCount() && 0x7FFFFFFF);
	}

	static void UserDisconnected(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* 'Update trainning time */
		trainningInfo[UserIndex].trainningTime = trainningInfo[UserIndex].trainningTime + (modNuevoTimer
				.getInterval((Declaraciones.GetTickCount() && 0x7FFFFFFF), trainningInfo[UserIndex].startTick) / 1000);

		trainningInfo[UserIndex].startTick = (Declaraciones.GetTickCount() && 0x7FFFFFFF);

		/* 'Store info in char file */
		ES.WriteVar(Declaraciones.CharPath + vb6.UCase(Declaraciones.UserList[UserIndex].Name) + ".chr", "RESEARCH",
				"TrainningTime", vb6.CStr(trainningInfo[UserIndex].trainningTime));
	}

	static void UserLevelUp(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int handle = 0;
		handle = vb6.FreeFile();

		/* 'Log the data */
		/*
		 * FIXME: OPEN App . Path & "\\logs\\statistics.log" FOR Append Shared
		 * AS handle
		 */

		/*
		 * FIXME: PRINT # handle , UCase $ ( UserList ( UserIndex ) . Name ) &
		 * " completó el nivel " & CStr ( UserList ( UserIndex ) . Stats . ELV )
		 * & " en " & CStr ( . trainningTime + ( getInterval ( ( GetTickCount (
		 * ) AND &H7FFFFFFF ) , . startTick ) / 1000 ) ) & " segundos."
		 */

		/* FIXME: CLOSE handle */

		/* 'Reset data */
		trainningInfo[UserIndex].trainningTime = 0;
		trainningInfo[UserIndex].startTick = (Declaraciones.GetTickCount() && 0x7FFFFFFF);
	}

	static void StoreFrag(int killer, int victim) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int clase = 0;
		int raza = 0;
		int alignment = 0;

		if (Declaraciones.UserList[victim].Stats.ELV > 50 || Declaraciones.UserList[killer].Stats.ELV > 50) {
			return;
		}

		switch (Declaraciones.UserList[killer].clase) {
		case Assasin:
			clase = 1;

			break;

		case Bard:
			clase = 2;

			break;

		case Mage:
			clase = 3;

			break;

		case Paladin:
			clase = 4;

			break;

		case Warrior:
			clase = 5;

			break;

		case Cleric:
			clase = 6;

			break;

		case Hunter:
			clase = 7;

			break;

		default:
			return;
			break;
		}

		switch (Declaraciones.UserList[killer].raza) {
		case Elfo:
			raza = 1;

			break;

		case Drow:
			raza = 2;

			break;

		case Enano:
			raza = 3;

			break;

		case Gnomo:
			raza = 4;

			break;

		case Humano:
			raza = 5;

			break;

		default:
			return;
			break;
		}

		if (ES.criminal(killer)) {
			if (Extra.esCaos(killer)) {
				alignment = 2;
			} else {
				alignment = 3;
			}
		} else {
			if (Extra.esArmada(killer)) {
				alignment = 1;
			} else {
				alignment = 4;
			}
		}

		fragLvlRaceData[clase].matrix[Declaraciones.UserList[killer].Stats.ELV][raza] = fragLvlRaceData[clase].matrix[Declaraciones.UserList[killer].Stats.ELV][raza]
				+ 1;

		fragLvlLvlData[clase].matrix[Declaraciones.UserList[killer].Stats.ELV][Declaraciones.UserList[victim].Stats.ELV] = fragLvlLvlData[clase].matrix[Declaraciones.UserList[killer].Stats.ELV][Declaraciones.UserList[victim].Stats.ELV]
				+ 1;

		fragAlignmentLvlData[Declaraciones.UserList[killer].Stats.ELV][alignment] = fragAlignmentLvlData[Declaraciones.UserList[killer].Stats.ELV][alignment]
				+ 1;
	}

	static void DumpStatistics() {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int handle = 0;
		handle = vb6.FreeFile();

		String LINE;
		int i = 0;
		int j = 0;

		/* FIXME: OPEN App . Path & "\\logs\\frags.txt" FOR OUTPUT AS handle */

		/*
		 * 'Save lvl vs lvl frag matrix for each class - we use GNU Octave's
		 * ASCII file format
		 */

		/* FIXME: PRINT # handle , "# name: fragLvlLvl_Ase" */
		/* FIXME: PRINT # handle , "# type: matrix" */
		/* FIXME: PRINT # handle , "# rows: 50" */
		/* FIXME: PRINT # handle , "# columns: 50" */

		for (j = (1); j <= (50); j++) {
			for (i = (1); i <= (50); i++) {
				/*
				 * FIXME: LINE = LINE & " " & CStr ( fragLvlLvlData ( 1 ) .
				 * matrix ( i , j ) )
				 */
			}

			/* FIXME: PRINT # handle , LINE */
			/* FIXME: LINE = vbNullString */
		}

		/* FIXME: PRINT # handle , "# name: fragLvlLvl_Bar" */
		/* FIXME: PRINT # handle , "# type: matrix" */
		/* FIXME: PRINT # handle , "# rows: 50" */
		/* FIXME: PRINT # handle , "# columns: 50" */

		for (j = (1); j <= (50); j++) {
			for (i = (1); i <= (50); i++) {
				/*
				 * FIXME: LINE = LINE & " " & CStr ( fragLvlLvlData ( 2 ) .
				 * matrix ( i , j ) )
				 */
			}

			/* FIXME: PRINT # handle , LINE */
			/* FIXME: LINE = vbNullString */
		}

		/* FIXME: PRINT # handle , "# name: fragLvlLvl_Mag" */
		/* FIXME: PRINT # handle , "# type: matrix" */
		/* FIXME: PRINT # handle , "# rows: 50" */
		/* FIXME: PRINT # handle , "# columns: 50" */

		for (j = (1); j <= (50); j++) {
			for (i = (1); i <= (50); i++) {
				/*
				 * FIXME: LINE = LINE & " " & CStr ( fragLvlLvlData ( 3 ) .
				 * matrix ( i , j ) )
				 */
			}

			/* FIXME: PRINT # handle , LINE */
			/* FIXME: LINE = vbNullString */
		}

		/* FIXME: PRINT # handle , "# name: fragLvlLvl_Pal" */
		/* FIXME: PRINT # handle , "# type: matrix" */
		/* FIXME: PRINT # handle , "# rows: 50" */
		/* FIXME: PRINT # handle , "# columns: 50" */

		for (j = (1); j <= (50); j++) {
			for (i = (1); i <= (50); i++) {
				/*
				 * FIXME: LINE = LINE & " " & CStr ( fragLvlLvlData ( 4 ) .
				 * matrix ( i , j ) )
				 */
			}

			/* FIXME: PRINT # handle , LINE */
			/* FIXME: LINE = vbNullString */
		}

		/* FIXME: PRINT # handle , "# name: fragLvlLvl_Gue" */
		/* FIXME: PRINT # handle , "# type: matrix" */
		/* FIXME: PRINT # handle , "# rows: 50" */
		/* FIXME: PRINT # handle , "# columns: 50" */

		for (j = (1); j <= (50); j++) {
			for (i = (1); i <= (50); i++) {
				/*
				 * FIXME: LINE = LINE & " " & CStr ( fragLvlLvlData ( 5 ) .
				 * matrix ( i , j ) )
				 */
			}

			/* FIXME: PRINT # handle , LINE */
			/* FIXME: LINE = vbNullString */
		}

		/* FIXME: PRINT # handle , "# name: fragLvlLvl_Cle" */
		/* FIXME: PRINT # handle , "# type: matrix" */
		/* FIXME: PRINT # handle , "# rows: 50" */
		/* FIXME: PRINT # handle , "# columns: 50" */

		for (j = (1); j <= (50); j++) {
			for (i = (1); i <= (50); i++) {
				/*
				 * FIXME: LINE = LINE & " " & CStr ( fragLvlLvlData ( 6 ) .
				 * matrix ( i , j ) )
				 */
			}

			/* FIXME: PRINT # handle , LINE */
			/* FIXME: LINE = vbNullString */
		}

		/* FIXME: PRINT # handle , "# name: fragLvlLvl_Caz" */
		/* FIXME: PRINT # handle , "# type: matrix" */
		/* FIXME: PRINT # handle , "# rows: 50" */
		/* FIXME: PRINT # handle , "# columns: 50" */

		for (j = (1); j <= (50); j++) {
			for (i = (1); i <= (50); i++) {
				/*
				 * FIXME: LINE = LINE & " " & CStr ( fragLvlLvlData ( 7 ) .
				 * matrix ( i , j ) )
				 */
			}

			/* FIXME: PRINT # handle , LINE */
			/* FIXME: LINE = vbNullString */
		}

		/*
		 * 'Save lvl vs race frag matrix for each class - we use GNU Octave's
		 * ASCII file format
		 */

		/* FIXME: PRINT # handle , "# name: fragLvlRace_Ase" */
		/* FIXME: PRINT # handle , "# type: matrix" */
		/* FIXME: PRINT # handle , "# rows: 5" */
		/* FIXME: PRINT # handle , "# columns: 50" */

		for (j = (1); j <= (5); j++) {
			for (i = (1); i <= (50); i++) {
				/*
				 * FIXME: LINE = LINE & " " & CStr ( fragLvlRaceData ( 1 ) .
				 * matrix ( i , j ) )
				 */
			}

			/* FIXME: PRINT # handle , LINE */
			/* FIXME: LINE = vbNullString */
		}

		/* FIXME: PRINT # handle , "# name: fragLvlRace_Bar" */
		/* FIXME: PRINT # handle , "# type: matrix" */
		/* FIXME: PRINT # handle , "# rows: 5" */
		/* FIXME: PRINT # handle , "# columns: 50" */

		for (j = (1); j <= (5); j++) {
			for (i = (1); i <= (50); i++) {
				/*
				 * FIXME: LINE = LINE & " " & CStr ( fragLvlRaceData ( 2 ) .
				 * matrix ( i , j ) )
				 */
			}

			/* FIXME: PRINT # handle , LINE */
			/* FIXME: LINE = vbNullString */
		}

		/* FIXME: PRINT # handle , "# name: fragLvlRace_Mag" */
		/* FIXME: PRINT # handle , "# type: matrix" */
		/* FIXME: PRINT # handle , "# rows: 5" */
		/* FIXME: PRINT # handle , "# columns: 50" */

		for (j = (1); j <= (5); j++) {
			for (i = (1); i <= (50); i++) {
				/*
				 * FIXME: LINE = LINE & " " & CStr ( fragLvlRaceData ( 3 ) .
				 * matrix ( i , j ) )
				 */
			}

			/* FIXME: PRINT # handle , LINE */
			/* FIXME: LINE = vbNullString */
		}

		/* FIXME: PRINT # handle , "# name: fragLvlRace_Pal" */
		/* FIXME: PRINT # handle , "# type: matrix" */
		/* FIXME: PRINT # handle , "# rows: 5" */
		/* FIXME: PRINT # handle , "# columns: 50" */

		for (j = (1); j <= (5); j++) {
			for (i = (1); i <= (50); i++) {
				/*
				 * FIXME: LINE = LINE & " " & CStr ( fragLvlRaceData ( 4 ) .
				 * matrix ( i , j ) )
				 */
			}

			/* FIXME: PRINT # handle , LINE */
			/* FIXME: LINE = vbNullString */
		}

		/* FIXME: PRINT # handle , "# name: fragLvlRace_Gue" */
		/* FIXME: PRINT # handle , "# type: matrix" */
		/* FIXME: PRINT # handle , "# rows: 5" */
		/* FIXME: PRINT # handle , "# columns: 50" */

		for (j = (1); j <= (5); j++) {
			for (i = (1); i <= (50); i++) {
				/*
				 * FIXME: LINE = LINE & " " & CStr ( fragLvlRaceData ( 5 ) .
				 * matrix ( i , j ) )
				 */
			}

			/* FIXME: PRINT # handle , LINE */
			/* FIXME: LINE = vbNullString */
		}

		/* FIXME: PRINT # handle , "# name: fragLvlRace_Cle" */
		/* FIXME: PRINT # handle , "# type: matrix" */
		/* FIXME: PRINT # handle , "# rows: 5" */
		/* FIXME: PRINT # handle , "# columns: 50" */

		for (j = (1); j <= (5); j++) {
			for (i = (1); i <= (50); i++) {
				/*
				 * FIXME: LINE = LINE & " " & CStr ( fragLvlRaceData ( 6 ) .
				 * matrix ( i , j ) )
				 */
			}

			/* FIXME: PRINT # handle , LINE */
			/* FIXME: LINE = vbNullString */
		}

		/* FIXME: PRINT # handle , "# name: fragLvlRace_Caz" */
		/* FIXME: PRINT # handle , "# type: matrix" */
		/* FIXME: PRINT # handle , "# rows: 5" */
		/* FIXME: PRINT # handle , "# columns: 50" */

		for (j = (1); j <= (5); j++) {
			for (i = (1); i <= (50); i++) {
				/*
				 * FIXME: LINE = LINE & " " & CStr ( fragLvlRaceData ( 7 ) .
				 * matrix ( i , j ) )
				 */
			}

			/* FIXME: PRINT # handle , LINE */
			/* FIXME: LINE = vbNullString */
		}

		/*
		 * 'Save lvl vs class frag matrix for each race - we use GNU Octave's
		 * ASCII file format
		 */

		/* FIXME: PRINT # handle , "# name: fragLvlClass_Elf" */
		/* FIXME: PRINT # handle , "# type: matrix" */
		/* FIXME: PRINT # handle , "# rows: 7" */
		/* FIXME: PRINT # handle , "# columns: 50" */

		for (j = (1); j <= (7); j++) {
			for (i = (1); i <= (50); i++) {
				/*
				 * FIXME: LINE = LINE & " " & CStr ( fragLvlRaceData ( j ) .
				 * matrix ( i , 1 ) )
				 */
			}

			/* FIXME: PRINT # handle , LINE */
			/* FIXME: LINE = vbNullString */
		}

		/* FIXME: PRINT # handle , "# name: fragLvlClass_Dar" */
		/* FIXME: PRINT # handle , "# type: matrix" */
		/* FIXME: PRINT # handle , "# rows: 7" */
		/* FIXME: PRINT # handle , "# columns: 50" */

		for (j = (1); j <= (7); j++) {
			for (i = (1); i <= (50); i++) {
				/*
				 * FIXME: LINE = LINE & " " & CStr ( fragLvlRaceData ( j ) .
				 * matrix ( i , 2 ) )
				 */
			}

			/* FIXME: PRINT # handle , LINE */
			/* FIXME: LINE = vbNullString */
		}

		/* FIXME: PRINT # handle , "# name: fragLvlClass_Dwa" */
		/* FIXME: PRINT # handle , "# type: matrix" */
		/* FIXME: PRINT # handle , "# rows: 7" */
		/* FIXME: PRINT # handle , "# columns: 50" */

		for (j = (1); j <= (7); j++) {
			for (i = (1); i <= (50); i++) {
				/*
				 * FIXME: LINE = LINE & " " & CStr ( fragLvlRaceData ( j ) .
				 * matrix ( i , 3 ) )
				 */
			}

			/* FIXME: PRINT # handle , LINE */
			/* FIXME: LINE = vbNullString */
		}

		/* FIXME: PRINT # handle , "# name: fragLvlClass_Gno" */
		/* FIXME: PRINT # handle , "# type: matrix" */
		/* FIXME: PRINT # handle , "# rows: 7" */
		/* FIXME: PRINT # handle , "# columns: 50" */

		for (j = (1); j <= (7); j++) {
			for (i = (1); i <= (50); i++) {
				/*
				 * FIXME: LINE = LINE & " " & CStr ( fragLvlRaceData ( j ) .
				 * matrix ( i , 4 ) )
				 */
			}

			/* FIXME: PRINT # handle , LINE */
			/* FIXME: LINE = vbNullString */
		}

		/* FIXME: PRINT # handle , "# name: fragLvlClass_Hum" */
		/* FIXME: PRINT # handle , "# type: matrix" */
		/* FIXME: PRINT # handle , "# rows: 7" */
		/* FIXME: PRINT # handle , "# columns: 50" */

		for (j = (1); j <= (7); j++) {
			for (i = (1); i <= (50); i++) {
				/*
				 * FIXME: LINE = LINE & " " & CStr ( fragLvlRaceData ( j ) .
				 * matrix ( i , 5 ) )
				 */
			}

			/* FIXME: PRINT # handle , LINE */
			/* FIXME: LINE = vbNullString */
		}

		/*
		 * 'Save lvl vs alignment frag matrix for each race - we use GNU
		 * Octave's ASCII file format
		 */

		/* FIXME: PRINT # handle , "# name: fragAlignmentLvl" */
		/* FIXME: PRINT # handle , "# type: matrix" */
		/* FIXME: PRINT # handle , "# rows: 4" */
		/* FIXME: PRINT # handle , "# columns: 50" */

		for (j = (1); j <= (4); j++) {
			for (i = (1); i <= (50); i++) {
				/*
				 * FIXME: LINE = LINE & " " & CStr ( fragAlignmentLvlData ( i ,
				 * j ) )
				 */
			}

			/* FIXME: PRINT # handle , LINE */
			/* FIXME: LINE = vbNullString */
		}

		/* FIXME: CLOSE handle */

		/* 'Dump Chat statistics */
		handle = vb6.FreeFile();

		/*
		 * FIXME: OPEN App . Path & "\\logs\\huffman.log" FOR OUTPUT AS handle
		 */

		double Total = 0.0;

		/* 'Compute total characters */
		for (i = (0); i <= (255); i++) {
			Total = Total + keyOcurrencies[i];
		}

		/* 'Show each character's ocurrencies */
		if (Total != 0) {
			for (i = (0); i <= (255); i++) {
				/*
				 * FIXME: PRINT # handle , CStr ( i ) & "    " & CStr ( Round (
				 * keyOcurrencies ( i ) / Total , 8 ) )
				 */
			}
		}

		/* FIXME: PRINT # handle , "TOTAL =    " & CStr ( Total ) */

		/* FIXME: CLOSE handle */
	}

	static void ParseChat(String /* FIXME BYREF!! */ S) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int i = 0;
		int key = 0;

		for (i = (1); i <= (vb6.Len(S)); i++) {
			key = vb6.Asc(vb6.mid(S, i, 1));

			keyOcurrencies[key] = keyOcurrencies[key] + 1;
		}

		/* 'Add a NULL-terminated to consider that possibility too.... */
		keyOcurrencies[0] = keyOcurrencies[0] + 1;
	}

}