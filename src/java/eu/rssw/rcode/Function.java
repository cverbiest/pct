/**
 * Copyright 2011-2016 Riverside Software
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package eu.rssw.rcode;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.openedge.core.reflect.IInternalFunction;
import com.openedge.core.reflect.IParameter;

@XmlRootElement
public class Function {
    @XmlAttribute
    public String functionName, signature, returnType;
    @XmlAttribute
    public boolean isPrivate;
    @XmlElement(name = "comment")
    public String functionComment;
    @XmlElement(name = "parameter")
    public List<Parameter> parameters = new ArrayList<Parameter>();

    public static Function readFunction(IInternalFunction proc) {
        Function p = new Function();
        p.functionName = proc.getName();
        p.isPrivate = proc.isPrivate();
        p.returnType = proc.getReturnType().getABLName(true);
        // XXX Signature ??

        int pos = 0;
        for (IParameter prm : proc.getParameters()) {
            p.parameters.add(Parameter.readParameter(prm, pos++));
        }

        return p;
    }
}
