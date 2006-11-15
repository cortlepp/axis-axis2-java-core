/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *      
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.axis2.jaxws.dispatch;

import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.Future;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Response;
import javax.xml.ws.Service;
import junit.framework.TestCase;

public class SOAPMessageDispatch extends TestCase {
	private String url = "http://localhost:8080/axis2/services/ProxyDocLitWrappedService";
	private QName serviceName = new QName(
			"http://org.apache.axis2.proxy.doclitwrapped", "ProxyDocLitWrappedService");
	private QName portName = new QName("http://org.apache.axis2.proxy.doclitwrapped",
	"ProxyDocLitWrappedPort");
	
	String messageResource = "test-resources" + File.separator  + "xml" + File.separator +"soapmessage.xml";
	
	public void testSOAPMessageSyncMessageMode() throws Exception {
		
        String basedir = new File(".").getAbsolutePath();
        String messageResource = new File(basedir, this.messageResource).getAbsolutePath();
        
		System.out.println("---------------------------------------");
		System.out.println("test: " + getName());
		//Initialize the JAX-WS client artifacts
		Service svc = Service.create(serviceName);
		svc.addPort(portName, null, url);
		Dispatch<SOAPMessage> dispatch = svc.createDispatch(portName,
				SOAPMessage.class, Service.Mode.MESSAGE);

		//Create SOAPMessage Object no attachments here.
		FileInputStream inputStream = new FileInputStream(messageResource);
		MessageFactory factory = MessageFactory.newInstance();
		SOAPMessage msgObject = factory.createMessage(null, inputStream);

		//Invoke the Dispatch
		System.out.println(">> Invoking Async Dispatch");
		SOAPMessage response = dispatch.invoke(msgObject);

		assertNotNull("dispatch invoke returned null", response);
		response.writeTo(System.out);
	}
	
	public void testSOAPMessageAsyncCallbackMessageMode() throws Exception {
		
        String basedir = new File(".").getAbsolutePath();
        String messageResource = new File(basedir, this.messageResource).getAbsolutePath();
        
		System.out.println("---------------------------------------");
		System.out.println("test: " + getName());
		//Initialize the JAX-WS client artifacts
		Service svc = Service.create(serviceName);
		svc.addPort(portName, null, url);
		Dispatch<SOAPMessage> dispatch = svc.createDispatch(portName,
				SOAPMessage.class, Service.Mode.MESSAGE);

		//Create SOAPMessage Object no attachments here.
		FileInputStream inputStream = new FileInputStream(messageResource);
		MessageFactory factory = MessageFactory.newInstance();
		SOAPMessage msgObject = factory.createMessage(null, inputStream);
		
        AsyncCallback<SOAPMessage> ac = new AsyncCallback<SOAPMessage>();
		//Invoke the Dispatch
		System.out.println(">> Invoking sync Dispatch");
		Future<?> monitor = dispatch.invokeAsync(msgObject, ac);

		assertNotNull("dispatch invokeAsync returned null Future<?>", monitor);
		while (!monitor.isDone()) {
            System.out.println(">> Async invocation still not complete");
            Thread.sleep(1000);
        }
        
        SOAPMessage response = ac.getValue();
        assertNotNull("dispatch invoke returned null", response);
        response.writeTo(System.out);
	}
    
    public void testSOAPMessageAsyncPollingMessageMode() throws Exception {
        
        String basedir = new File(".").getAbsolutePath();
        String messageResource = new File(basedir, this.messageResource).getAbsolutePath();
        
        System.out.println("---------------------------------------");
        System.out.println("test: " + getName());
        //Initialize the JAX-WS client artifacts
        Service svc = Service.create(serviceName);
        svc.addPort(portName, null, url);
        Dispatch<SOAPMessage> dispatch = svc.createDispatch(portName,
                SOAPMessage.class, Service.Mode.MESSAGE);

        //Create SOAPMessage Object no attachments here.
        FileInputStream inputStream = new FileInputStream(messageResource);
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage msgObject = factory.createMessage(null, inputStream);

        //Invoke the Dispatch
        System.out.println(">> Invoking sync Dispatch");
        Response<SOAPMessage> asyncResponse = dispatch.invokeAsync(msgObject);

        assertNotNull("dispatch invokeAsync returned null Response", asyncResponse);
        while (!asyncResponse.isDone()) {
            System.out.println(">> Async invocation still not complete");
            Thread.sleep(1000);
        }
        
        SOAPMessage response = asyncResponse.get();
        assertNotNull("dispatch invoke returned null", response);
        response.writeTo(System.out);
    }
    
}
