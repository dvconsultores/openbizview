<%-----------------------------------------------------------------------------
     Copyright (C) 2011 - 2016  DVCONSULTORES

    Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	    http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
-----------------------------------------------------------------------------%>
<!--
To change this template, choose Tools | Templates
and open the template in the editor.
-->
<%@page contentType="text/html" pageEncoding="UTF-8" session="true"%>
<jsp:useBean id="bd" class="org.openbizview.util.Bd" scope="session"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=utf-8">
        <link rel="shortcut icon"  href="../img/favicon.ico"/>
        <LINK REL="stylesheet" HREF="../css/styles.css" TYPE="text/css">
        <title>&nbsp;<%=bd.getMessage("tituloAce")%></title>
    </head>
    <body>
        <center>
            <b>Versión:&nbsp;</b><b style="color:red" ><%=bd.getMessage("versionbiz")%></b><br>
            <b><%=bd.getMessage("os")%></b><br>
            <b><%=bd.getMessage("gnu")%></b>&nbsp;&nbsp;<A HREF="http://es.wikipedia.org/wiki/GNU_General_Public_License" TARGET="_new"><b>Gnu</b></A><br>
            <a><%=bd.getMessage("des")%></a>&nbsp;&nbsp;<A HREF="http://www.dvconsultores.com" TARGET="_new"><b>Web</b></A><br>
        </center>
    </body>
</html>
