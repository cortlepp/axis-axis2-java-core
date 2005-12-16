/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.axis2.engine;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.OperationContext;
import org.apache.axis2.description.*;
import org.apache.axis2.om.OMAbstractFactory;
import org.apache.axis2.soap.SOAPFactory;
import org.apache.axis2.transport.http.CommonsHTTPTransportSender;

import javax.xml.namespace.QName;

public class EngineWithoutPhaseResolvingTest extends AbstractEngineTest {
    private MessageContext mc;
    private AxisConfiguration engineRegistry;
    private QName serviceName = new QName("axis/services/NullService");
    private QName operationName = new QName("NullOperation");
    private AxisService service;
    private ConfigurationContext engineContext;
    private AxisOperation axisOp;

    public EngineWithoutPhaseResolvingTest() {
    }

    public EngineWithoutPhaseResolvingTest(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {

        engineRegistry = new AxisConfiguration();
        engineContext = new ConfigurationContext(engineRegistry);

        TransportOutDescription transport = new TransportOutDescription(
                new QName("null"));
        transport.setSender(new CommonsHTTPTransportSender());

        TransportInDescription transportIn = new TransportInDescription(
                new QName("null"));
        axisOp = new InOutAxisOperation(operationName);

        service = new AxisService(serviceName.getLocalPart());
        axisOp.setMessageReceiver(new MessageReceiver() {
            public void receive(MessageContext messgeCtx) throws AxisFault {
                // TODO Auto-generated method stub

            }
        });
        engineRegistry.addService(service);
        service.addOperation(axisOp);

        mc =
                new MessageContext(engineContext,
                        transportIn,
                        transport);

        OperationContext opCOntext = new OperationContext(axisOp);

        mc.setOperationContext(opCOntext);
        mc.setTransportOut(transport);
        mc.setProperty(MessageContext.TRANSPORT_OUT, System.out);
        mc.setServerSide(true);
        SOAPFactory omFac = OMAbstractFactory.getSOAP11Factory();
        mc.setEnvelope(omFac.getDefaultEnvelope());


        mc.setWSAAction(operationName.getLocalPart());
        mc.setSoapAction(operationName.getLocalPart());
        System.out.flush();
    }

    public void testServerReceive() throws Exception {
        mc.setTo(
                new EndpointReference("axis/services/NullService"));
        AxisEngine engine = new AxisEngine(engineContext);
        mc.setServerSide(true);
        engine.receive(mc);
    }
}
