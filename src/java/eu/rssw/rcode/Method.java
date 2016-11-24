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
public class Method {
    public Method() {
    }

    @XmlAttribute
    public String methodName, returnType, signature;
    @XmlAttribute
    public AccessModifier modifier;
    @XmlElement(name = "comment")
    public String methodComment;
    @XmlAttribute
    public boolean isStatic, isFinal, isAbstract;
    @XmlElement(name = "parameter")
    public List<Parameter> parameters = new ArrayList<Parameter>();

    @Override
    public String toString() {
        String s = (modifier != null ? modifier + " " : "")
                + /* modifier.toString() + */(isStatic ? " STATIC " : "")
                + (isFinal ? " FINAL " : "") + returnType + " " + methodName + "(";
        boolean first = true;
        for (Parameter p : parameters) {
            s += (first ? "" : ", ") + p.toString();
            first = false;
        }
        s += ")";
        return s;
    }

    public static Method readMethodElement(MethodElement elem) {
        if ("<missing name>".equals(elem.getName()))
            return null;
        Method m = new Method();
        m.methodName = elem.getName();
        m.returnType = elem.getReturnType().getABLName(true);
        m.isStatic = elem.isStatic();
        m.isAbstract = elem.isAbstract();
        m.isFinal = elem.isFinal();
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
