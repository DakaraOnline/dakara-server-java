
/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"modUserRecords"')] */
/* 'Argentum Online 0.13.0 */
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

public class modUserRecords {

	static void LoadRecords() {
		/* '************************************************************** */
		/* 'Author: Amraphen */
		/* 'Last Modify Date: 29/11/2010 */
		/* 'Carga los seguimientos de usuarios. */
		/* '************************************************************** */
		clsIniManager Reader;
		String tmpStr;
		int i;
		int j;

		Reader = new clsIniManager();

		if (!General.FileExist(Declaraciones.DatPath + "RECORDS.DAT")) {
			CreateRecordsFile();
		}

		Reader.Initialize(Declaraciones.DatPath + "RECORDS.DAT");

		Declaraciones.NumRecords = Reader.GetValue("INIT", "NumRecords");
		if (Declaraciones.NumRecords) {
			Declaraciones.Records = new None[0];
			Declaraciones.Records = (Declaraciones.Records == null) ? new None[1 + Declaraciones.NumRecords]
					: java.util.Arrays.copyOf(Declaraciones.Records, 1 + Declaraciones.NumRecords);
		}

		for (i = (1); i <= (Declaraciones.NumRecords); i++) {
			Declaraciones.Records[i].Usuario = Reader.GetValue("RECORD" + i, "Usuario");
			Declaraciones.Records[i].Creador = Reader.GetValue("RECORD" + i, "Creador");
			Declaraciones.Records[i].Fecha = Reader.GetValue("RECORD" + i, "Fecha");
			Declaraciones.Records[i].Motivo = Reader.GetValue("RECORD" + i, "Motivo");

			Declaraciones.Records[i].NumObs = vb6.val(Reader.GetValue("RECORD" + i, "NumObs"));
			if (Declaraciones.Records[i].NumObs) {
				Declaraciones.Records[i].Obs = new None[0];
				Declaraciones.Records[i].Obs = (Declaraciones.Records[i].Obs == null)
						? new None[1 + Declaraciones.Records[i].NumObs]
						: java.util.Arrays.copyOf(Declaraciones.Records[i].Obs, 1 + Declaraciones.Records[i].NumObs);
			}

			for (j = (1); j <= (Declaraciones.Records[i].NumObs); j++) {
				tmpStr = Reader.GetValue("RECORD" + i, "Obs" + j);

				Declaraciones.Records[i].Obs[j].Creador = General.ReadField(1, tmpStr, 45);
				Declaraciones.Records[i].Obs[j].Fecha = General.ReadField(2, tmpStr, 45);
				Declaraciones.Records[i].Obs[j].Detalles = General.ReadField(3, tmpStr, 45);
			}
		}
	}

	static void SaveRecords() {
		/* '************************************************************** */
		/* 'Author: Amraphen */
		/* 'Last Modify Date: 29/11/2010 */
		/* 'Guarda los seguimientos de usuarios. */
		/* '************************************************************** */
		clsIniManager Writer;
		String tmpStr;
		int i;
		int j;

		Writer = new clsIniManager();

		Writer.ChangeValue("INIT", "NumRecords", Declaraciones.NumRecords);

		for (i = (1); i <= (Declaraciones.NumRecords); i++) {
			Writer.ChangeValue("RECORD" + i, "Usuario", Declaraciones.Records[i].Usuario);
			Writer.ChangeValue("RECORD" + i, "Creador", Declaraciones.Records[i].Creador);
			Writer.ChangeValue("RECORD" + i, "Fecha", Declaraciones.Records[i].Fecha);
			Writer.ChangeValue("RECORD" + i, "Motivo", Declaraciones.Records[i].Motivo);

			Writer.ChangeValue("RECORD" + i, "NumObs", Declaraciones.Records[i].NumObs);

			for (j = (1); j <= (Declaraciones.Records[i].NumObs); j++) {
				tmpStr = Declaraciones.Records[i].Obs[j].Creador + "-" + Declaraciones.Records[i].Obs[j].Fecha + "-"
						+ Declaraciones.Records[i].Obs[j].Detalles;
				Writer.ChangeValue("RECORD" + i, "Obs" + j, tmpStr);
			}
		}

		Writer.DumpFile(Declaraciones.DatPath + "RECORDS.DAT");
	}

	static void AddRecord(int UserIndex, String Nickname, String Reason) {
		/* '************************************************************** */
		/* 'Author: Amraphen */
		/* 'Last Modify Date: 29/11/2010 */
		/* 'Agrega un seguimiento. */
		/* '************************************************************** */
		Declaraciones.NumRecords = Declaraciones.NumRecords + 1;
		Declaraciones.Records = (Declaraciones.Records == null) ? new None[1 + Declaraciones.NumRecords]
				: java.util.Arrays.copyOf(Declaraciones.Records, 1 + Declaraciones.NumRecords);

		Declaraciones.Records[Declaraciones.NumRecords].Usuario = vb6.UCase(Nickname);
		Declaraciones.Records[Declaraciones.NumRecords].Fecha = vb6.Format(vb6.Now(), "DD/MM/YYYY hh:mm:ss");
		Declaraciones.Records[Declaraciones.NumRecords].Creador = vb6.UCase(Declaraciones.UserList[UserIndex].Name);
		Declaraciones.Records[Declaraciones.NumRecords].Motivo = Reason;
		Declaraciones.Records[Declaraciones.NumRecords].NumObs = 0;
	}

	static void AddObs(int UserIndex, int RecordIndex, String Obs) {
		/* '************************************************************** */
		/* 'Author: Amraphen */
		/* 'Last Modify Date: 29/11/2010 */
		/* 'Agrega una observación. */
		/* '************************************************************** */
		Declaraciones.Records[RecordIndex].NumObs = Declaraciones.Records[RecordIndex].NumObs + 1;
		Declaraciones.Records[RecordIndex].Obs = (Declaraciones.Records[RecordIndex].Obs == null)
				? new None[1 + Declaraciones.Records[RecordIndex].NumObs]
				: java.util.Arrays.copyOf(Declaraciones.Records[RecordIndex].Obs,
						1 + Declaraciones.Records[RecordIndex].NumObs);

		Declaraciones.Records[RecordIndex].Obs[Declaraciones.Records[RecordIndex].NumObs].Creador = vb6
				.UCase(Declaraciones.UserList[UserIndex].Name);
		Declaraciones.Records[RecordIndex].Obs[Declaraciones.Records[RecordIndex].NumObs].Fecha = vb6.Now();
		Declaraciones.Records[RecordIndex].Obs[Declaraciones.Records[RecordIndex].NumObs].Detalles = Obs;
	}

	static void RemoveRecord(int RecordIndex) {
		/* '************************************************************** */
		/* 'Author: Amraphen */
		/* 'Last Modify Date: 29/11/2010 */
		/* 'Elimina un seguimiento. */
		/* '************************************************************** */
		int i;

		if (RecordIndex == Declaraciones.NumRecords) {
			Declaraciones.NumRecords = Declaraciones.NumRecords - 1;
			if (Declaraciones.NumRecords > 0) {
				Declaraciones.Records = (Declaraciones.Records == null) ? new None[1 + Declaraciones.NumRecords]
						: java.util.Arrays.copyOf(Declaraciones.Records, 1 + Declaraciones.NumRecords);
			}
		} else {
			Declaraciones.NumRecords = Declaraciones.NumRecords - 1;
			for (i = (RecordIndex); i <= (Declaraciones.NumRecords); i++) {
				Declaraciones.Records[i] = Declaraciones.Records[i + 1];
			}

			Declaraciones.Records = (Declaraciones.Records == null) ? new None[1 + Declaraciones.NumRecords]
					: java.util.Arrays.copyOf(Declaraciones.Records, 1 + Declaraciones.NumRecords);
		}
	}

	static void CreateRecordsFile() {
		/* '************************************************************** */
		/* 'Author: Amraphen */
		/* 'Last Modify Date: 29/11/2010 */
		/* 'Crea el archivo de seguimientos. */
		/* '************************************************************** */
		int intFile;

		intFile = vb6.FreeFile();

		/* FIXME: OPEN DatPath & "RECORDS.DAT" FOR OUTPUT AS # intFile */
		/* FIXME: PRINT # intFile , "[INIT]" */
		/* FIXME: PRINT # intFile , "NumRecords=0" */
		/* FIXME: CLOSE # intFile */
	}

}