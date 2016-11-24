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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.openedge.core.reflect.EventElement;
import com.openedge.core.reflect.MethodElement;
import com.openedge.core.reflect.PropertyElement;
import com.openedge.core.reflect.RCodeClassInfo;
import com.openedge.core.reflect.RCodeReadException;
import com.openedge.core.reflect.TypeInformation;

@XmlRootElement(name = "unit", namespace = "")
public class ClassCompilationUnit {
    @XmlAttribute
    public String packageName, className, inherits;
    @XmlElement
    public List<String> interfaces = new ArrayList<String>();
    @XmlAttribute
    public boolean isAbstract, isFinal, isInterface, isEnum;

    @XmlElement(name = "classComment")
    public List<String> classComment = new ArrayList<String>();
    @XmlElement(name = "constructor")
    public List<Constructor> constructors = new ArrayList<Constructor>();
    @XmlElement(name = "method")
    public List<Method> methods = new ArrayList<Method>();
    @XmlElement(name = "property")
    public List<Property> properties = new ArrayList<Property>();
    @XmlElement(name = "event")
    public List<Event> events = new ArrayList<Event>();
    @XmlElement(name = "using")
    public List<Using> usings = new ArrayList<Using>();
    @XmlElement(name = "member")
    public List<EnumMember> enumMembers = new ArrayList<>();

    public void classToXML(File out) throws JAXBException, IOException {
        FileOutputStream fos = new FileOutputStream(out);
        JAXBContext context = JAXBContext.newInstance(this.getClass().getPackage().getName());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(this, fos);
        fos.close();
    }

    public void merge(ClassCompilationUnit unit) {
        classComment.addAll(unit.classComment);

        // Fusion des commentaires pour les propriétés
        for (Property prop : properties) {
            for (Property prop2 : unit.properties) {
                if (prop.name.equalsIgnoreCase(prop2.name))
                    prop.propertyComment = prop2.propertyComment;
            }
        }
        // Fusion des commentaires pour les événements
        for (Event evt : events) {
            for (Event evt2 : unit.events) {
                if (evt.eventName.equalsIgnoreCase(evt2.eventName))
                    evt.eventComment = evt2.eventComment;
            }
        }
        // Fusion des commentaires pour les constructeurs
        for (Constructor c : constructors) {
            for (Constructor c2 : unit.constructors) {
                if (hasSameSignature(c.parameters, c2.parameters)) {
                    c.constrComment = c2.constrComment;
                }
            }
        }
        // Fusion des commentaires pour les méthodes
        for (Method m2 : unit.methods) {
            boolean found = false;
            for (Method m : methods) {
                if (m.methodName.equalsIgnoreCase(m2.methodName)
                        && hasSameSignature(m.parameters, m2.parameters)) {
                    m.methodComment = m2.methodComment;
                    found = true;
                }
            }
            if (!found) {
                System.out.println("not found");
                methods.add(m2);
            }
        }

        // Fusion des using
        usings.addAll(unit.usings);
    }

    /**
     * Verify signature.
     * 
     * @param params1 RCode signature
     * @param params2 Parser signature
     * @return True if same signature
     */
    private static boolean hasSameSignature(List<Parameter> params1, List<Parameter> params2) {
        if (params1.size() != params2.size())
            return false;
        for (Parameter p1 : params1) {
            for (Parameter p2 : params2) {
                if (p1.mode != p2.mode)
                    return false;
                if (!p1.dataType.endsWith(p2.dataType))
                    return false;
            }
        }
        return true;
    }

    public static ClassCompilationUnit readClassInfo(RCodeClassInfo classInfo)
            throws IOException, RCodeReadException {
        ClassCompilationUnit unit = new ClassCompilationUnit();
        if (classInfo.getName().lastIndexOf('.') == -1) {
            unit.packageName = "";
            unit.className = classInfo.getName();
        } else {
            unit.className = classInfo.getName()
                    .substring(classInfo.getName().lastIndexOf('.') + 1);
            unit.packageName = classInfo.getName().substring(0,
                    classInfo.getName().lastIndexOf('.'));
        }
        unit.inherits = classInfo.getBaseType();
        unit.isAbstract = classInfo.isAbstract();
        unit.isFinal = classInfo.isFinal();
        unit.isInterface = classInfo.isInterface();

        for (TypeInformation inf : classInfo.getInterfaces()) {
            unit.interfaces.add(inf.getName());
        }
        for (PropertyElement elem : classInfo.getProperties()) {
            unit.properties.add(Property.readPropertyElement(elem));
        }
        for (EventElement elem : classInfo.getEvents()) {
            unit.events.add(Event.readEventElement(elem));
        }
        for (MethodElement elem : classInfo.getConstructors()) {
            unit.constructors.add(Constructor.readMethodElement(elem));
        }
        for (MethodElement elem : classInfo.getMethods()) {
            Method m = Method.readMethodElement(elem);
            if (m != null)
                unit.methods.add(m);
        }
        return unit;
    }

    @SuppressWarnings("unused")
    private static Comment parseComments(String comments) {
        Comment comm = new Comment();
        comments = comments.trim();
        if (comments.startsWith("/*"))
            comments = comments.substring(2);
        else
            return null;
        StringTokenizer st = new StringTokenizer(comments, "\n");
        String str = null;
        String zz = "";
        while (st.hasMoreTokens()) {
            str = st.nextToken().trim();
            if (str.charAt(0) == '*')
                str = str.substring(1).trim();
            if (str.startsWith("@param")) {
                int idx1 = str.indexOf(' ');
                if (idx1 > 0) {
                    String str2 = str.substring(idx1).trim();
                    int idx2 = str2.indexOf(' ');
                    if (idx2 > 0) {
                        comm.parameters.put(str2.substring(0, idx2), str2.substring(idx2).trim());
                    }
                }
            } else if (str.startsWith("@return")) {
                comm.returnValue = str.substring(7).trim();
            } else {
                if ((str.length() == 0) && (zz.length() > 0)) {
                    comm.comment.add(zz);
                    zz = "";
                } else if (str.length() > 0) {
                    zz = zz + " " + str;
                }
            }

        }
        if (zz.length() > 0) {
            comm.comment.add(zz);
        }
        return comm;
    }
}
