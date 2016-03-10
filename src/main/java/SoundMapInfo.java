
/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"SoundMapInfo"')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_GlobalNameSpace'), (5, '='), (1, 'False')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Creatable'), (5, '='), (1, 'True')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_PredeclaredId'), (5, '='), (1, 'False')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Exposed'), (5, '='), (1, 'False')] */
/* '************************************************************** */
/* ' SoundMapInfo.cls */
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

public class SoundMapInfo {

	static public class p_tSoundMapInfo {
		public int Cantidad;
		public int[] SoundIndex;
		public int[] flags;
		public float[] Probabilidad;
	}

	private p_tSoundMapInfo[] p_Mapas = new p_tSoundMapInfo[0];

	/* 'sonidos conocidos, pasados a enum para intelisense */

	void Class_Initialize() {
		/* 'armar el array */
		p_Mapas = new p_tSoundMapInfo[0];
		p_Mapas = (p_Mapas == null) ? new p_tSoundMapInfo[1 + Declaraciones.NumMaps]
				: java.util.Arrays.copyOf(p_Mapas, 1 + Declaraciones.NumMaps);
		LoadSoundMapInfo();
	}

	void LoadSoundMapInfo() {
		int i = 0;
		int j = 0;
		String Temps;
		String MAPFILE;

		MAPFILE = vb6.App.Instance().Path + Declaraciones.MapPath + "MAPA";

		/*
		 * 'Usage of Val() prevents errors when dats are corrputed or
		 * incomplete. All invalid values are assumed to be zero.
		 */

		/* 'TODO : Log the error in the dat for correction. */
		for (i = (1); i <= (vb6.UBound(p_Mapas)); i++) {
			Temps = ES.GetVar(MAPFILE + i + ".dat", "SONIDOS", "Cantidad");

			if (vb6.IsNumeric(Temps)) {
				p_Mapas[i].Cantidad = vb6.val(Temps);

				p_Mapas[i].flags = new Long[0];
				p_Mapas[i].flags = (p_Mapas[i].flags == null) ? new Long[1 + p_Mapas[i].Cantidad]
						: java.util.Arrays.copyOf(p_Mapas[i].flags, 1 + p_Mapas[i].Cantidad);
				p_Mapas[i].Probabilidad = new Single[0];
				p_Mapas[i].Probabilidad = (p_Mapas[i].Probabilidad == null) ? new Single[1 + p_Mapas[i].Cantidad]
						: java.util.Arrays.copyOf(p_Mapas[i].Probabilidad, 1 + p_Mapas[i].Cantidad);
				p_Mapas[i].SoundIndex = new Integer[0];
				p_Mapas[i].SoundIndex = (p_Mapas[i].SoundIndex == null) ? new Integer[1 + p_Mapas[i].Cantidad]
						: java.util.Arrays.copyOf(p_Mapas[i].SoundIndex, 1 + p_Mapas[i].Cantidad);

				for (j = (1); j <= (p_Mapas[i].Cantidad); j++) {
					p_Mapas[i].flags[j] = vb6.val(ES.GetVar(MAPFILE + i + ".dat", "SONIDO" + j, "Flags"));
					p_Mapas[i].Probabilidad[j] = vb6.val(ES.GetVar(MAPFILE + i + ".dat", "SONIDO" + j, "Probabilidad"));
					p_Mapas[i].SoundIndex[j] = vb6.val(ES.GetVar(MAPFILE + i + ".dat", "SONIDO" + j, "Sonido"));
				}
			} else {
				p_Mapas[i].Cantidad = 0;
			}
		}
	}

	void ReproducirSonidosDeMapas() {
		int i = 0;
		int SonidoMapa = 0;
		int posX = 0;
		int posY = 0;

		/* 'Sounds are played at a random position */
		posX = Matematicas.RandomNumber(Declaraciones.XMinMapSize, Declaraciones.XMaxMapSize);
		posY = Matematicas.RandomNumber(Declaraciones.YMinMapSize, Declaraciones.YMaxMapSize);

		for (i = (1); i <= (vb6.UBound(p_Mapas)); i++) {
			if (p_Mapas[i].Cantidad > 0) {
				SonidoMapa = Matematicas.RandomNumber(1, p_Mapas[i].Cantidad);
				if (Matematicas.RandomNumber(1, 100) <= p_Mapas[i].Probabilidad[SonidoMapa]) {
					/* 'tocarlo */
					if (Admin.Lloviendo) {
						if (p_Mapas[i].flags[SonidoMapa] ^ p_eSoundFlags.Lluvia) {
							modSendData.SendData(SendTarget.toMap, i,
									Protocol.PrepareMessagePlayWave(p_Mapas[i].SoundIndex[SonidoMapa], posX, posY));
						}
					} else {
						if (p_Mapas[i].flags[SonidoMapa] ^ p_eSoundFlags.ninguna) {
							modSendData.SendData(SendTarget.toMap, i,
									Protocol.PrepareMessagePlayWave(p_Mapas[i].SoundIndex[SonidoMapa], posX, posY));
						}
					}
				}
			}
		}
	}

	void ReproducirSonido(SendTarget Destino, int index, int SoundIndex) {
		modSendData.SendData(Destino, index, Protocol.PrepareMessagePlayWave(SoundIndex,
				Declaraciones.UserList[index].Pos.X, Declaraciones.UserList[index].Pos.Y));
	}

}