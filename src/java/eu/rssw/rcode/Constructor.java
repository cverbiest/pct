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

import com.openedge.core.reflect.IParameter;
import com.openedge.core.reflect.MethodElement;

@XmlRootElement
public class Constructor {
    @XmlAttribute
    public String signature;
    @XmlAttribute
    public AccessModifier modifier;
    public String constrComment;
    @XmlElement(name = "parameter")
    public List<Parameter> parameters = new ArrayList<Parameter>();

    public static Constructor readMethodElement(MethodElement elem) {
        Constructor m = new Constructor();
        if (elem.isPublic())
            m.modifier = AccessModifier.PUBLIC;
        else if (elem.isProtected())
            m.modifier = AccessModifier.PROTECTED;
        else if (elem.isPrivate())
            m.modifier = AccessModifier.PRIVATE;

        int pos = 0;
        for (IParameter prm : elem.getParameters()) {
            m.parameters.add(Parameter.readParameter(prm, pos++));
        }

        return m;
    }

}
