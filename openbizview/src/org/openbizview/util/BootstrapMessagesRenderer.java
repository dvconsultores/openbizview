package org.openbizview.util;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIMessages;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;
import javax.faces.context.ResponseWriter;

import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.renderkit.html_basic.MessagesRenderer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage.Severity;

/**
 * Component for twitter bootstrap alerts.
 * Overrides default JSF Message renderer with Bootstrap alert design.
 *
 * @author vlcekmi3 (https://gist.github.com/vlcekmi3/4151211)
 */
@FacesRenderer(componentFamily="javax.faces.Messages", rendererType="javax.faces.Messages")
public class BootstrapMessagesRenderer extends MessagesRenderer {

    private static final Attribute[] ATTRIBUTES = 
            AttributeManager.getAttributes(AttributeManager.Key.MESSAGESMESSAGES);

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {

        rendererParamsNotNull(context, component);
        if (!shouldEncode(component)) return;

        boolean mustRender = shouldWriteIdAttribute(component);
        UIMessages messages = (UIMessages) component;
        ResponseWriter writer = context.getResponseWriter();

        String clientId = ((UIMessages) component).getFor();
        if (clientId == null && messages.isGlobalOnly()) {
            clientId = "";
        }

        @SuppressWarnings("rawtypes")
		Iterator messageIt = getMessageIter(context, clientId, component);
        if (!messageIt.hasNext()) {
            if (mustRender) {
                if ("javax_faces_developmentstage_messages".equals(component.getId())) {
                    return;
                }
                writer.startElement("div", component);
                writeIdAttributeIfNecessary(context, writer, component);
                writer.endElement("div");
            }
            return;
        }

        writeIdAttributeIfNecessary(context, writer, component);
        RenderKitUtils.renderPassThruAttributes(context, writer, component, ATTRIBUTES);

        Map<Severity, List<FacesMessage>> msgs = new HashMap<Severity, List<FacesMessage>>();
        msgs.put(FacesMessage.SEVERITY_INFO, new ArrayList<FacesMessage>());
        msgs.put(FacesMessage.SEVERITY_WARN, new ArrayList<FacesMessage>());
        msgs.put(FacesMessage.SEVERITY_ERROR, new ArrayList<FacesMessage>());
        msgs.put(FacesMessage.SEVERITY_FATAL, new ArrayList<FacesMessage>());

        while (messageIt.hasNext()) {
            FacesMessage curMessage = (FacesMessage) messageIt.next();
            if (curMessage.isRendered() && !messages.isRedisplay()) {
                continue;
            }
            msgs.get(curMessage.getSeverity()).add(curMessage);
        }

        List<FacesMessage> severityMessages = msgs.get(FacesMessage.SEVERITY_FATAL);
        if (!severityMessages.isEmpty()){
            encodeSeverityMessages(context, messages, FacesMessage.SEVERITY_FATAL, severityMessages);
        }

        severityMessages = msgs.get(FacesMessage.SEVERITY_ERROR);
        if (!severityMessages.isEmpty()){
            encodeSeverityMessages(context, messages, FacesMessage.SEVERITY_ERROR, severityMessages);
        }

        severityMessages = msgs.get(FacesMessage.SEVERITY_WARN);
        if (!severityMessages.isEmpty()){
            encodeSeverityMessages(context, messages, FacesMessage.SEVERITY_WARN, severityMessages);
        }

        severityMessages = msgs.get(FacesMessage.SEVERITY_INFO);
        if (!severityMessages.isEmpty()){
            encodeSeverityMessages(context, messages, FacesMessage.SEVERITY_INFO, severityMessages);
        }
    }

    private void encodeSeverityMessages(FacesContext facesContext, UIMessages uiMessages, 
                                        Severity severity, List<FacesMessage> messages) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String alertSeverityClass = "", alertSeverityIcon = "";

        if (FacesMessage.SEVERITY_INFO.equals(severity)) {
            alertSeverityClass = "alert-success";
            alertSeverityIcon = "pr-3 mdi mdi-thumb-up-outline";
        } else if (FacesMessage.SEVERITY_WARN.equals(severity)) {
            alertSeverityClass = "alert-warning";
            alertSeverityIcon = "pr-3 mdi mdi-alert-outline";
        } else if (FacesMessage.SEVERITY_ERROR.equals(severity)) {
            alertSeverityClass = "alert-danger";
            alertSeverityIcon = "pr-3 mdi mdi-thumb-down-outline";
        } else if (FacesMessage.SEVERITY_FATAL.equals(severity)) {
            alertSeverityClass = "alert-danger";
            alertSeverityIcon = "pr-3 mdi-thumb-down-outline";
        }

        writer.startElement("div", null);
        writer.writeAttribute("class", "pl-2 alert " + alertSeverityClass, "alert-dismissible");
        writer.startElement("button", uiMessages);
        writer.writeAttribute("class", "close", "class");
        writer.writeAttribute("data-dismiss", "alert", "data-dismiss");
        writer.write("&times;");
        writer.endElement("button");
        for (FacesMessage msg : messages){
            String summary = msg.getSummary() != null ? msg.getSummary() : "";
            String detail = msg.getDetail() != null ? msg.getDetail() : summary;
            if (uiMessages.isShowSummary()) {
                writer.startElement("strong", uiMessages);
                writer.startElement("i", null);
                writer.writeAttribute("class", 
                		alertSeverityIcon, alertSeverityIcon);
                writer.endElement("i");
                writer.endElement("strong");
                writer.startElement("small", null);
                writer.startElement("b", null);
                writer.writeAttribute("class", 
                		"pt-0", "pt-0");
                writer.writeText(summary, uiMessages, null);
                writer.endElement("b");
                writer.endElement("small");
            }

            if (uiMessages.isShowDetail()) {
                writer.writeText(" " + detail, null);
            }
            msg.rendered();
        }
        writer.endElement("div");
    }
}