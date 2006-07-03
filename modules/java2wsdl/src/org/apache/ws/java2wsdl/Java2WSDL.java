package org.apache.ws.java2wsdl;

import org.apache.ws.java2wsdl.utils.Java2WSDLCommandLineOptionParser;
import org.apache.ws.java2wsdl.utils.Java2WSDLOptionsValidator;
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
*
*/

public class Java2WSDL {
    public static void main(String[] args) {
        Java2WSDLCommandLineOptionParser commandLineOptionParser = new Java2WSDLCommandLineOptionParser(
                args);
        //  validate the arguments
        validateCommandLineOptions(commandLineOptionParser);
        try {
            new Java2WSDLCodegenEngine(commandLineOptionParser.getAllOptions()).generate();
        } catch (Exception e) {
            System.out.println("An error occured while generating code" + e.getMessage());
        }
    }

    public static void printUsage() {
        System.out.println("Usage java2wsdl -cn <fully qualified class name> : class file name");
        System.out.println("-o <output Location> : output file location");
        System.out.println("-cp <class path uri> : list of classpath entries - (urls)");
        System.out.println("-tn <target namespace> : target namespace");
        System.out.println("-tp <target namespace prefix> : target namespace prefix");
        System.out.println("-stn <schema target namespace> : target namespace for schema");
        System.out.println("-stp <schema target namespace prefix> : target namespace prefix for schema");
        System.out.println("-sn <service name> : service name");
        System.out.println("-of <output file name> : output file name for the WSDL");
        System.out.println("-st <binding style> : style for the WSDL");
        System.out.println("-u <binding use> : use for the WSDL");
        System.out.println("-l <soap address> : address of the port for the WSDL");
        System.out.println("-efd <unqualified> : Setting for elementFormDefault (defaults to qualified)");
        System.out.println("-afd <unqualified> : Setting for attributeFormDefault (defaults to qualified)");
        System.exit(0);
    }


    private static void validateCommandLineOptions(
            Java2WSDLCommandLineOptionParser parser) {
        if (parser.getAllOptions().size() == 0) {
            printUsage();
        } else if (parser.getInvalidOptions(new Java2WSDLOptionsValidator()).size() > 0) {
            printUsage();
        }

    }

}

