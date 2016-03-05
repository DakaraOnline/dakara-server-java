/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"frmUserList"')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_GlobalNameSpace'), (5, '='), (1, 'False')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Creatable'), (5, '='), (1, 'False')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_PredeclaredId'), (5, '='), (1, 'True')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Exposed'), (5, '='), (1, 'False')] */
/* '************************************************************** */
/* ' frmUserList.frm */
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

public class frmUserList {

	static void Command1_Click() {
		int LoopC = 0;

		Text2.Text = "MaxUsers: " + Declaraciones.MaxUsers + vbCrLf;
		Text2.Text = Text2.Text + "LastUser: " + Declaraciones.LastUser + vbCrLf;
		Text2.Text = Text2.Text + "NumUsers: " + Declaraciones.NumUsers + vbCrLf;
		/* 'Text2.Text = Text2.Text & "" & vbCrLf */

		List1.Clear();

		for (LoopC = (1); LoopC <= (Declaraciones.MaxUsers); LoopC++) {
			List1.AddItem(vb6.Format(LoopC, "000") + " "
					+ vb6.IIf(Declaraciones.UserList[LoopC].flags.UserLogged, Declaraciones.UserList[LoopC].Name, ""));
			List1.ItemData[List1.NewIndex] = LoopC;
		}

	}

	static void Command2_Click() {
		int LoopC = 0;

		for (LoopC = (1); LoopC <= (Declaraciones.MaxUsers); LoopC++) {
			if (Declaraciones.UserList[LoopC].ConnID != -1 && !Declaraciones.UserList[LoopC].flags.UserLogged) {
				TCP.CloseSocket(LoopC);
			}
		}

	}

	static void List1_Click() {
		int UserIndex = 0;
		if (List1.ListIndex != -1) {
			UserIndex = List1.ItemData[List1.ListIndex];
			if (UserIndex > 0 && UserIndex <= Declaraciones.MaxUsers) {
				Text1.Text = "UserLogged: " + Declaraciones.UserList[UserIndex].flags.UserLogged + vbCrLf;
				Text1.Text = Text1.Text + "IdleCount: " + Declaraciones.UserList[UserIndex].Counters.IdleCount + vbCrLf;
				Text1.Text = Text1.Text + "ConnId: " + Declaraciones.UserList[UserIndex].ConnID + vbCrLf;
				Text1.Text = Text1.Text + "ConnIDValida: " + Declaraciones.UserList[UserIndex].ConnIDValida + vbCrLf;
			}
		}

	}

}