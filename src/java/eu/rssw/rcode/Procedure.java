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

import com.openedge.core.reflect.IInternalProcedure;
import com.openedge.core.reflect.IParameter;

@XmlRootElement
public class Procedure {
    @XmlAttribute
    public String procedureName, signature;
    @XmlAttribute
    public boolean isPrivate;
    @XmlElement(name = "comment")
    public String procedureComment;
    @XmlElement(name = "parameter")
    public List<Parameter> parameters = new ArrayList<Parameter>();

    public static Procedure readProcedure(IInternalProcedure proc) {
        Procedure p = new Procedure();
        p.procedureName = proc.getName();
        p.isPrivate = proc.isPrivate();
        // XXX Signature ??

        int pos = 0;
        for (IParameter prm : proc.getParameters()) {
            p.parameters.add(Parameter.readParameter(prm, pos++));
        }

        return p;
    }
}
