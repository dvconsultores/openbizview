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
				 org.eclipse.birt.report.context.BaseAttributeBean,
				 org.eclipse.birt.report.engine.api.DataExtractionFormatInfo,
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
<%
	DataExtractionFormatInfo[] dataExtractInfos = ParameterAccessor.supportedDataExtractions;
%>
<%-----------------------------------------------------------------------------
	Export data dialog fragment
-----------------------------------------------------------------------------%>
<TABLE ID="simpleExportDialogBody" CELLSPACING="2" CELLPADDING="2" CLASS="birtviewer_dialog_body">
	<TR HEIGHT="5px"><TD></TD></TR>
	<TR>
		<TD>
			<LABEL FOR="resultsets"><%= BirtResources.getMessage( "birt.viewer.dialog.exportdata.resultsets", valorCookie )%>
			</LABEL>
		</TD>
	</TR>
	<TR>
		<TD COLSPAN="4">
			<SELECT ID="resultsets" CLASS="birtviewer_exportdata_dialog_single_select">
			</SELECT>
		</TD>
	</TR>
	<TR HEIGHT="5px"><TD></TD></TR>
	<TR>
		<TD VALIGN="top">
			<TABLE STYLE="font-size:8pt;">
				<TR><TD>
					<LABEL FOR="availableColumnSelect"><%= BirtResources.getMessage( "birt.viewer.dialog.exportdata.availablecolumn", valorCookie )%></LABEL>
				</TD></TR>
				<TR><TD>
					<SELECT ID="availableColumnSelect" MULTIPLE="true" SIZE="10" CLASS="birtviewer_exportdata_dialog_select">
					</SELECT>
				</TD></TR>
			</TABLE>
		</TD>
		<TD VALIGN="middle">
			<TABLE HEIGHT="100%">
				<TR>
					<TD>
						<TABLE VALIGN="middle">
							<TR><TD>
								<INPUT TYPE="image" NAME="Addall" 
									<%
									if( !attributeBean.isRtl())
									{
									%>
									SRC="birt/images/AddAll.gif"
									<%
									}
									else
									{
									%>
									SRC="birt/images/AddAll_rtl.gif"
									<%
									}
									%>		
									ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.dialog.exportdata.addall", valorCookie )%>" 
									TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.dialog.exportdata.addall", valorCookie )%>" 
									CLASS="birtviewer_exportdata_dialog_button">
							</TD></TR>
							<TR height="2px"><TD></TD></TR>
							<TR><TD>
								<INPUT TYPE="image" NAME="Add"
									<%
									if( !attributeBean.isRtl())
									{
									%>
									SRC="birt/images/Add.gif"
									<%
									}
									else
									{
									%>
									SRC="birt/images/Add_rtl.gif"
									<%
									}
									%>									 
									ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.dialog.exportdata.add", valorCookie )%>" 
									TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.dialog.exportdata.add", valorCookie )%>" 								
									CLASS="birtviewer_exportdata_dialog_button">
							</TD></TR>
							<TR height="2px"><TD></TD></TR>
							<TR><TD>
								<INPUT TYPE="image" NAME="Remove"
									<%
									if( !attributeBean.isRtl())
									{
									%>
									SRC="birt/images/Remove_disabled.gif"
									<%
									}
									else
									{
									%>
									SRC="birt/images/Remove_disabled_rtl.gif"
									<%
									}
									%>									  
									ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.dialog.exportdata.remove", valorCookie )%>" 
									TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.dialog.exportdata.remove", valorCookie )%>" 								
									CLASS="birtviewer_exportdata_dialog_button">
							</TD></TR>
							<TR height="2px"><TD></TD></TR>
							<TR><TD>
								<INPUT TYPE="image" NAME="Removeall" 
									<%
									if( !attributeBean.isRtl())
									{
									%>
									SRC="birt/images/RemoveAll_disabled.gif"
									<%
									}
									else
									{
									%>
									SRC="birt/images/RemoveAll_disabled_rtl.gif"
									<%
									}
									%>									  
									ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.dialog.exportdata.removeall", valorCookie )%>" 
									TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.dialog.exportdata.removeall", valorCookie )%>" 								
									CLASS="birtviewer_exportdata_dialog_button">
							</TD></TR>
						</TABLE>
					</TD>
				</TR>
			</TABLE>
		</TD>
		<TD VALIGN="middle">
			<TABLE HEIGHT="100%">
				<TR>
					<TD>
						<TABLE VALIGN="middle">
							<TR><TD>
								<INPUT TYPE="image" NAME="Up" SRC="birt/images/Up_disabled.gif" 
									ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.dialog.exportdata.up", valorCookie )%>" 
									TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.dialog.exportdata.up", valorCookie )%>" 
									CLASS="birtviewer_exportdata_dialog_button">
							</TD></TR>
							<TR height="2px"><TD></TD></TR>
							<TR><TD>
								<INPUT TYPE="image" NAME="Down" SRC="birt/images/Down_disabled.gif" 
									ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.dialog.exportdata.down", valorCookie )%>" 
									TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.dialog.exportdata.down", valorCookie )%>" 								
									CLASS="birtviewer_exportdata_dialog_button">
							</TD></TR>							
						</TABLE>
					</TD>
				</TR>
			</TABLE>
		</TD>
		<TD >
			<TABLE STYLE="font-size:8pt;">
				<TR><TD>
					<LABEL FOR="selectedColumnSelect"><%= BirtResources.getMessage( "birt.viewer.dialog.exportdata.selectedcolumn", valorCookie )%></LABEL>
				</TD></TR>
				<TR><TD>
					<SELECT ID="selectedColumnSelect" MULTIPLE="true" SIZE="10" CLASS="birtviewer_exportdata_dialog_select">
					</SELECT>
				</TD></TR>
			</TABLE>
		</TD>
	</TR>
	<TR HEIGHT="5px"><TD></TD></TR>
	<TR>
		<TD COLSPAN="4">			
			<DIV>
				<%= BirtResources.getMessage( "birt.viewer.dialog.exportdata.extension", valorCookie )%> 
				<SELECT ID="exportDataExtension" CLASS="birtviewer_exportdata_dialog_select">
				<%
					for ( int i = 0; i < dataExtractInfos.length; i++ )
					{
						DataExtractionFormatInfo extensionInfo  = dataExtractInfos[i];
						if( extensionInfo.getId() == null 
							|| extensionInfo.getFormat() == null 
							|| ( extensionInfo.isHidden() != null && extensionInfo.isHidden().booleanValue() ) )
							continue;
						
						String extensionName = extensionInfo.getName( );
						if( extensionName == null )
							extensionName = "";
				%>
						<OPTION VALUE="<%= extensionInfo.getId() %>"><%= extensionName %>(*.<%= extensionInfo.getFormat() %>)</OPTION>
				<%
					}
				%>
				</SELECT>
			</DIV>
			<BR/>
			<DIV ID="exportDataEncodingSetting">
				<TABLE>
					<TR>
						<TD><%= BirtResources.getMessage( "birt.viewer.dialog.exportdata.encoding", valorCookie )%></TD>
						<TD><INPUT TYPE="radio" NAME="exportDataEncoding" ID="exportDataEncoding_UTF8" CHECKED value="UTF-8">UTF-8</TD>
					</TR>
					<TR>
						<TD></TD>
						<TD>
							<INPUT TYPE="radio" NAME="exportDataEncoding" ID="exportDataEncoding_other"><%= BirtResources.getMessage( "birt.viewer.dialog.exportdata.encoding.other", valorCookie )%>
							<INPUT TYPE="text" NAME="exportDataOtherEncoding" ID="exportDataOtherEncoding_input" CLASS="birtviewer_exportdata_dialog_input" DISABLED="true"><%= BirtResources.getMessage( "birt.viewer.dialog.exportdata.encoding.comment", valorCookie )%>
						</TD>
					</TR>
				</TABLE>				
			</DIV>
			<BR/>
			<DIV>
				<%= BirtResources.getMessage( "birt.viewer.dialog.exportdata.separator", valorCookie )%> 
				<SELECT ID="exportDataCSVSeparator" CLASS="birtviewer_exportdata_dialog_select">
					<OPTION VALUE="0" SELECTED><%= BirtResources.getMessage( "birt.viewer.sep.0", valorCookie )%></OPTION>
					<OPTION VALUE="1"><%= BirtResources.getMessage( "birt.viewer.sep.1", valorCookie )%></OPTION>
					<OPTION VALUE="2"><%= BirtResources.getMessage( "birt.viewer.sep.2", valorCookie )%></OPTION>
					<OPTION VALUE="3"><%= BirtResources.getMessage( "birt.viewer.sep.3", valorCookie )%></OPTION>
					<OPTION VALUE="4"><%= BirtResources.getMessage( "birt.viewer.sep.4", valorCookie )%></OPTION>
				</SELECT>
			</DIV>
			<BR/>
			<DIV>
				<TABLE cellpadding="0" cellspacing="0">
					<TR valign="top">
						<TD><INPUT TYPE="checkbox" ID="exportColumnDataType"></TD>
						<TD style="padding-top:2px;" nowrap="nowrap"><%= BirtResources.getMessage( "birt.viewer.dialog.exportdata.datatype", valorCookie )%></TD>
						<TD style="padding-left:20px;" valign="top"><INPUT TYPE="checkbox" ID="exportColumnLocaleNeutral"></TD>
						<TD style="padding-top:2px;" valign="top"><%= BirtResources.getMessage( "birt.viewer.dialog.exportdata.localeneutral", valorCookie )%></TD>
					</TR>
				</TABLE>
			</DIV>
		</TD>
	</TR>
	<TR HEIGHT="5px"><TD></TD></TR>
</TABLE>