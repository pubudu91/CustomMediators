/*
*  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package xyz.pubudu.mediators.xmltojsontestmediator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.gateway.core.config.ParameterHolder;
import org.wso2.carbon.gateway.core.flow.AbstractMediator;
import org.wso2.carbon.gateway.core.flow.contentaware.ConversionManager;
import org.wso2.carbon.gateway.core.flow.contentaware.MIMEType;
import org.wso2.carbon.messaging.CarbonCallback;
import org.wso2.carbon.messaging.CarbonMessage;
import org.wso2.carbon.messaging.DefaultCarbonMessage;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Mediator Implementation
 */
public class XMLtoJSONTestMediator extends AbstractMediator {

    private static final Logger log = LoggerFactory.getLogger(XMLtoJSONTestMediator.class);
    private String logMessage = "Message received at Sample Mediator";   // Sample Mediator specific variable


    @Override
    public String getName() {
        return "XMLtoJSONTestMediator";
    }

    /**
     * Mediate the message.
     * <p/>
     * This is the execution point of the mediator.
     *
     * @param carbonMessage  MessageContext to be mediated
     * @param carbonCallback Callback which can be use to call the previous step
     * @return whether mediation is success or not
     **/
    @Override
    public boolean receive(CarbonMessage carbonMessage, CarbonCallback carbonCallback) throws Exception {

        InputStream stream = convertTo(carbonMessage, MIMEType.JSON);
        Scanner sc = new Scanner(stream, "UTF-8");

        StringBuffer json = new StringBuffer();
        while (sc.hasNextLine()) {
            json.append(sc.nextLine() + "\n");
        }

        log.info("\n" + json.toString());

        DefaultCarbonMessage newMsg = new DefaultCarbonMessage();
        carbonMessage.setHeader("Content-Type", MIMEType.JSON);
        newMsg.setStringMessageBody(json.toString());

        return next(newMsg, carbonCallback);
    }

    /**
     * Set Parameters
     *
     * @param parameterHolder holder which contains key-value pairs of parameters
     */
    @Override
    public void setParameters(ParameterHolder parameterHolder) {
        logMessage = parameterHolder.getParameter("parameters").getValue();
    }


    /**
     * This is a sample mediator specific method
     */
    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }


}
