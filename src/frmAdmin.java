/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"frmAdmin"')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_GlobalNameSpace'), (5, '='), (1, 'False')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Creatable'), (5, '='), (1, 'False')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_PredeclaredId'), (5, '='), (1, 'True')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Exposed'), (5, '='), (1, 'False')] */
/* '************************************************************** */
/* ' frmAdmin.frm */
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

public class frmAdmin {

	static void cboPjs_Change() {
		ActualizaPjInfo();
	}

	static void cboPjs_Click() {
		ActualizaPjInfo();
	}

	static void Command1_Click() {
		int tIndex;

		tIndex = Extra.NameIndex(cboPjs.Text);
		if (tIndex > 0) {
			modSendData.SendData(SendTarget.ToAll, 0,
					Protocol.PrepareMessageConsoleMsg(
							"Servidor> " + Declaraciones.UserList[tIndex].Name + " ha sido echado.",
							FontTypeNames.FONTTYPE_SERVER));
			TCP.CloseSocket(tIndex);
		}

	}

	static void ActualizaListaPjs() {
		int LoopC;

		cboPjs.Clear();

		for (LoopC = (1); LoopC <= (Declaraciones.LastUser); LoopC++) {
			if (Declaraciones.UserList[LoopC].flags.UserLogged && Declaraciones.UserList[LoopC].ConnID >= 0
					&& Declaraciones.UserList[LoopC].ConnIDValida) {
				if (Declaraciones.UserList[LoopC].flags.Privilegios && PlayerType.User) {
					cboPjs.AddItem(Declaraciones.UserList[LoopC].Name);
					cboPjs.ItemData[cboPjs.NewIndex] = LoopC;
				}
			}
		}

	}

	static void Command3_Click() {
		TCP.EcharPjsNoPrivilegiados();
	}

	static void ActualizaPjInfo() {
		int tIndex;

		tIndex = Extra.NameIndex(cboPjs.Text);
		if (tIndex > 0) {
			Text1.Text = Declaraciones.UserList[tIndex].outgoingData.length + " elementos en cola." + vbCrLf;
		}

	}

}