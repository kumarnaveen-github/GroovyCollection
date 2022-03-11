import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.Source;
import java.io.StringWriter;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.OutputKeys;

def Message processData(Message message) {
	def body = message.getBody(java.lang.String) as String;
	def messageLog = messageLogFactory.getMessageLog(message);
	def mProp = message.getProperties();
	String logState = mProp.get("LogState");
	if(messageLog != null){
		Source xmlInput = new StreamSource(new StringReader(body));

		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
//initialize StreamResult with File object to save to file
		StreamResult result = new StreamResult(new StringWriter());

		transformer.transform(xmlInput, result);
		String xmlString = result.getWriter().toString();

		messageLog.setStringProperty("Logging#1", "Printing Payload As Attachment")
		messageLog.addAttachmentAsString(logState, xmlString, "text/plain");
	}


	return message;
}
