package net.supertabs.server.requests;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.supertabs.server.auth.SupertabsCredentials;
import net.supertabs.server.auth.SupertabsCredentialsFactory;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class SupertabsRequestFactory {
    static public SupertabsRequest getRequest(String xml, String ip) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        
        Reader reader = new StringReader(xml);
        InputSource is = new InputSource(reader);
        
        Document doc = builder.parse(is);
        
        Element top = (Element) doc.getFirstChild();
        
        Element auth = (Element) top.getElementsByTagName("credentials").item(0);
        
        String auth_type = auth.getElementsByTagName("type").item(0).getTextContent();
        
        HashMap<String, String> auth_args = new HashMap<String, String>();
        for(Node n = auth.getElementsByTagName("arguments").item(0).getFirstChild();
                n != null;
                n = n.getNextSibling())
            auth_args.put(n.getNodeName(), n.getTextContent());
        
        SupertabsCredentials credentials = SupertabsCredentialsFactory.getSupertabsCredentials(auth_type, auth_args, ip);
        
        Element action = (Element) top.getElementsByTagName("action").item(0);
        
        String action_type = action.getElementsByTagName("type").item(0).getTextContent();
        
        HashMap<String, String> action_args = new HashMap<String, String>();
        for(Node n = action.getElementsByTagName("arguments").item(0).getFirstChild();
                n != null;
                n = n.getNextSibling())
            action_args.put(n.getNodeName(), n.getTextContent());
        
        if(action_type == PingRequest.TYPE)
            return new PingRequest(action_args, credentials);
        
        return null;
    }
}
