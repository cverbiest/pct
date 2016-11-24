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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.openedge.core.reflect.PropertyElement;

@XmlRootElement
public class Property {
    @XmlAttribute
    public String name, dataType;
    @XmlAttribute
    public boolean isAbstract, isStatic;
    @XmlAttribute
    public int extent;
    @XmlAttribute
    public AccessModifier modifier;
    @XmlAttribute
    public GetSetModifier getModifier = GetSetModifier.NONE, setModifier = GetSetModifier.NONE;
    @XmlElement(name = "comment")
    public String propertyComment;

    public static Property readPropertyElement(PropertyElement elem) {
        Property prop = new Property();
        prop.name = elem.getName();
        prop.extent = elem.getExtent();
        prop.dataType = elem.getVariable().getDataType().getABLName(true);
        prop.isAbstract = elem.isAbstract();
        prop.isStatic = elem.isStatic();
        if (elem.isPublic())
            prop.modifier = AccessModifier.PUBLIC;
        else if (elem.isProtected())
            prop.modifier = AccessModifier.PROTECTED;
        else if (elem.isPrivate())
            prop.modifier = AccessModifier.PRIVATE;

        // C'est quoi elem.isIndexed() ??

        if (elem.isGetterPrivate()) {
            prop.getModifier = GetSetModifier.PRIVATE;
        } else if (elem.isGetterProtected()) {
            prop.getModifier = GetSetModifier.PROTECTED;
        } else if (elem.isGetterPublic()) {
            prop.getModifier = GetSetModifier.PUBLIC;
        }

        if (elem.isSetterPrivate()) {
            prop.setModifier = GetSetModifier.PRIVATE;
        } else if (elem.isSetterProtected()) {
            prop.setModifier = GetSetModifier.PROTECTED;
        } else if (elem.isSetterPublic()) {
            prop.setModifier = GetSetModifier.PUBLIC;
        }

        return prop;
    }

}
