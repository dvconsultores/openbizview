
<jsp:useBean id="BirtResources" class="org.openbizview.util.BirtResources" scope="request"/>
<%-- Map Java resource messages to Javascript constants --%>

<%-----------------------------------------------------------------------------
	Get cookie value
-----------------------------------------------------------------------------%>
	

<script type="text/javascript">
// <![CDATA[	
	// Error msgs
	Constants.error.invalidPageRange = '<%= BirtResources.getJavaScriptMessage( "birt.viewer.dialog.page.error.invalidpagerange", valorCookie )%>';
	Constants.error.parameterRequired = '<%= BirtResources.getJavaScriptMessage( "birt.viewer.error.parameterrequired", valorCookie )%>';
	Constants.error.parameterNotAllowBlank = '<%= BirtResources.getJavaScriptMessage( "birt.viewer.error.parameternotallowblank", valorCookie )%>';
	Constants.error.parameterNotSelected = '<%= BirtResources.getJavaScriptMessage( "birt.viewer.error.parameternotselected", valorCookie )%>';
	Constants.error.invalidPageNumber = '<%= BirtResources.getJavaScriptMessage( "birt.viewer.navbar.error.blankpagenum", valorCookie )%>';
	Constants.error.unknownError = '<%= BirtResources.getJavaScriptMessage( "birt.viewer.error.unknownerror", valorCookie )%>';
	Constants.error.generateReportFirst = '<%= BirtResources.getJavaScriptMessage( "birt.viewer.error.generatereportfirst", valorCookie )%>';
	Constants.error.printPreviewAlreadyOpen = '<%= BirtResources.getJavaScriptMessage( "birt.viewer.dialog.print.printpreviewalreadyopen", valorCookie )%>';
	Constants.error.confirmCancelTask = '<%= BirtResources.getJavaScriptMessage( "birt.viewer.progressbar.confirmcanceltask", valorCookie )%>';
// ]]>
</script>
