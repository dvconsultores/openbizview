<%-----------------------------------------------------------------------------
	Copyright (c) 2004 Actuate Corporation and others.
	All rights reserved. This program and the accompanying materials 
	are made available under the terms of the Eclipse Public License v1.0
	which accompanies this distribution, and is available at
	http://www.eclipse.org/legal/epl-v10.html
	
	Contributors:
		Actuate Corporation - Initial implementation.
-----------------------------------------------------------------------------%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page session="false" buffer="none"%>
<%@ page import="org.eclipse.birt.report.presentation.aggregation.IFragment,
org.openbizview.util.BirtResources"%>
 <%-----------------------------------------------------------------------------
	Get cookie value
-----------------------------------------------------------------------------%>
 <%
        Cookie[] cookies = request.getCookies();
        String valorCookie = "";

        for(int i = 0; i < cookies.length; i++) { 
            Cookie c = cookies[i];
            if (c.getName().equals("obv_language")) {
               // out.println("lang = " + c.getValue());
            	valorCookie = c.getValue();
            }
        }  
%>	
<%-----------------------------------------------------------------------------
	Expected java beans
-----------------------------------------------------------------------------%>
<jsp:useBean id="fragment" type="org.eclipse.birt.report.presentation.aggregation.IFragment" scope="request" />
<jsp:useBean id="BirtResources" class="org.openbizview.util.BirtResources" scope="request"/>
<%-----------------------------------------------------------------------------
	Print report dialog fragment
-----------------------------------------------------------------------------%>
<TABLE CELLSPACING="2" CELLPADDING="2" CLASS="birtviewer_dialog_body">
	<TR HEIGHT="5px"><TD></TD></TR>
	<TR>
		<TD>
			<DIV ID="printFormatSetting">
				<DIV><%=BirtResources.getMessage( "birt.viewer.dialog.print.format", valorCookie )%></DIV>
				<br/>
				<DIV>
				<INPUT TYPE="radio" ID="printAsHTML" name="printFormat" CHECKED/><%=BirtResources.getMessage( "birt.viewer.dialog.print.format.html", valorCookie )%>
				</DIV>
				<DIV>
				<INPUT TYPE="radio" ID="printAsPDF" name="printFormat"/><%=BirtResources.getMessage( "birt.viewer.dialog.print.format.pdf", valorCookie )%>
				&nbsp;&nbsp;
				<SELECT	ID="printFitSetting" CLASS="birtviewer_printreport_dialog_select" DISABLED="true">
					<option value="0" selected><%=BirtResources.getMessage( "birt.viewer.dialog.export.pdf.fittoauto", valorCookie )%></option>
					<option value="1"><%=BirtResources.getMessage( "birt.viewer.dialog.export.pdf.fittoactual", valorCookie )%></option>
					<option value="2"><%=BirtResources.getMessage( "birt.viewer.dialog.export.pdf.fittowhole", valorCookie )%></option>
				</SELECT>
				</DIV>
			</DIV>
		</TD>
	</TR>
	<TR HEIGHT="5px"><TD><HR/></TD></TR>
	<TR>
		<TD>
			<DIV ID="printPageSetting">
				<TABLE>
					<TR>
						<TD>
							<INPUT TYPE="radio" ID="printPageAll" NAME="printPages" CHECKED/><%=BirtResources.getMessage( "birt.viewer.dialog.page.all", valorCookie )%>
						</TD>
						<TD STYLE="padding-left:5px">	
							<INPUT TYPE="radio" ID="printPageCurrent" NAME="printPages"/><%=BirtResources.getMessage( "birt.viewer.dialog.page.current", valorCookie )%>
						</TD>	
						<TD STYLE="padding-left:5px">
							<INPUT TYPE="radio" ID="printPageRange" NAME="printPages"/><%=BirtResources.getMessage( "birt.viewer.dialog.page.range", valorCookie )%>
							<INPUT TYPE="text" CLASS="birtviewer_printreport_dialog_input" ID="printPageRange_input" DISABLED="true"/>
						</TD>
					</TR>		
				</TABLE>
			</DIV>
		</TD>
	</TR>
	<TR>
		<TD>&nbsp;&nbsp;<%=BirtResources.getMessage( "birt.viewer.dialog.page.range.description", valorCookie )%></TD>
	</TR>
	<TR HEIGHT="5px"><TD></TD></TR>
</TABLE>
