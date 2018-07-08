<%-----------------------------------------------------------------------------
	Copyright (c) 2004 Actuate Corporation and others.
	All rights reserved. This program and the accompanying materials 
	are made available under the terms of the Eclipse Public License v1.0
	which accompanies this distribution, and is available at
	http://www.eclipse.org/legal/epl-v10.html
	
	Contributors:
		Actuate Corporation - Initial implementation.
-----------------------------------------------------------------------------%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="false" buffer="none" %>
<%@ page import="org.eclipse.birt.report.presentation.aggregation.IFragment,
				 org.eclipse.birt.report.utility.ParameterAccessor,
				 org.eclipse.birt.report.servlet.ViewerServlet,
				 org.openbizview.util.BirtResources" %>
				 
				 
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
<jsp:useBean id="attributeBean" type="org.eclipse.birt.report.context.BaseAttributeBean" scope="request" />
<jsp:useBean id="BirtResources" class="org.openbizview.util.BirtResources" scope="request"/>
<%-----------------------------------------------------------------------------
	Toolbar fragment
-----------------------------------------------------------------------------%>
<TR 
	<%
		if( attributeBean.isShowToolbar( ) )
		{
	%>
		HEIGHT="20px"
	<%
		}
		else
		{
	%>
		style="display:none"
	<%
		}
	%>	
>
	<TD COLSPAN='2'>
		<DIV ID="toolbar">
			<TABLE CELLSPACING="1px" CELLPADDING="1px" WIDTH="100%" CLASS="birtviewer_toolbar">
				<TR><TD></TD></TR>
				<TR>
					<TD WIDTH="6px"/>
					<TD WIDTH="15px">
					   <INPUT TYPE="image" NAME='toc' SRC="birt/images/Toc.png" style="height: 23px;margin-right: 3px;margin-top: 1px"
					   		TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.toc", valorCookie )%>"
					   		ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.toc", valorCookie )%>" CLASS="birtviewer_clickable">
					</TD>
					<TD WIDTH="6px"/>
					<TD WIDTH="15px">
					   <INPUT TYPE="image" NAME='parameter' SRC="birt/images/Report_parameters.png" style="height: 23px;margin-right: 3px;margin-top: 1px"
					   		TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.parameter", valorCookie )%>"	
					   		ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.parameter", valorCookie )%>" CLASS="birtviewer_clickable">
					</TD>
					<TD WIDTH="6px"/>
					<TD WIDTH="15px">
					   <INPUT TYPE="image" NAME='export' SRC="birt/images/Export.png" style="height: 20px;margin-top: 1px"
					   		TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.export", valorCookie )%>"
					   		ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.export", valorCookie )%>" CLASS="birtviewer_clickable">
					</TD>
					<TD WIDTH="6px"/>
					<TD WIDTH="15px">
					   <INPUT TYPE="image" NAME='exportReport' SRC="birt/images/ExportReport.png" style="height: 25px;margin-top: 1px"
					   		TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.exportreport", valorCookie )%>"
					   		ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.exportreport", valorCookie )%>" CLASS="birtviewer_clickable">
					</TD>
					<TD WIDTH="6px"/>
					<TD WIDTH="15px">
					   <INPUT TYPE="image" NAME='print' SRC="birt/images/Print.png"  style="height: 21px;margin-top: 2px"
					   		TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.print", valorCookie )%>"
					   		ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.print", valorCookie )%>" CLASS="birtviewer_clickable">
					   		
					</TD>
					<%
					if( ParameterAccessor.isSupportedPrintOnServer )
					{
					%>					
					<TD WIDTH="6px"/>
					<TD WIDTH="15px">
					   <INPUT TYPE="image" NAME='printServer' SRC="birt/images/PrintServer.gif"
					   		TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.printserver", valorCookie )%>"
					   		ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.printserver", valorCookie )%>" CLASS="birtviewer_clickable">
					</TD>
					<%
					}
					%>										
					<TD ALIGN='right'>
					</TD>
					<TD WIDTH="6px"/>
				</TR>

			</TABLE>
		</DIV>
	</TD>
</TR>
