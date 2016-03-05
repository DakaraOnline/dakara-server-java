/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"clsEstadisticasIPC"')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_GlobalNameSpace'), (5, '='), (1, 'False')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Creatable'), (5, '='), (1, 'True')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_PredeclaredId'), (5, '='), (1, 'False')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Exposed'), (5, '='), (1, 'False')] */
/* '************************************************************** */
/* ' clsEstadisticasIPC.cls */
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

public class clsEstadisticasIPC {

	private int sMensaje;
	private int hVentana;
	private int hVentanaMia;

	static final int GW_HWNDFIRST = 0;
	static final int GW_HWNDNEXT = 2;

	/* '************************************************* */

	/* '************************************************* */

	int BuscaVentana(int Wnd, String str) {
		int retval = 0;
		int W = 0;
		int L = 0;
		String T;

		W = GetWindow(Wnd, GW_HWNDFIRST);

		while (W != 0) {
			L = GetWindowTextLength(W);

			if (L > 0) {
				T = vb6.Space(L + 1);
				L = GetWindowText(W, T, L + 1);

				if (vb6.Left(T, vb6.Len(str)) == str) {
					retval = W;
					return retval;
				}
			}

			W = GetWindow(W, GW_HWNDNEXT);
		}

		retval = 0;

		return retval;
	}

	int Informar(EstaNotificaciones QueCosa, int Parametro) {
		int retval = 0;
		BuscaWndEstadisticas();
		if (hVentana != 0) {
			retval = SendMessageLong(hVentana, sMensaje, QueCosa, Parametro);
		}

		return retval;
	}

	boolean EstadisticasAndando() {
		boolean retval = false;

		BuscaWndEstadisticas();
		/* 'Ret = SendNotifyMessage(hVentana, sMensaje, 0, 0) */
		retval = (hVentana != 0);

		return retval;
	}

	void Inicializa(int hWnd) {
		hVentanaMia = hWnd;
		sMensaje = RegisterWindowMessage("EstadisticasAO");

	}

	void BuscaWndEstadisticas() {
		hVentana = BuscaVentana(hVentanaMia, "Servidor de estadisticas AO");

	}

}