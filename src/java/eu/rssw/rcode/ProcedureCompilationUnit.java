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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.openedge.core.reflect.IInternalFunction;
import com.openedge.core.reflect.IInternalProcedure;
import com.openedge.core.reflect.RCodeReadException;
import com.openedge.core.reflect.SignatureBlock;

@XmlRootElement(name = "procedure", namespace = "")
public class ProcedureCompilationUnit {
    @XmlElement(name = "parameter")
    public List<Parameter> parameters = new ArrayList<Parameter>();
    @XmlElement(name = "comment")
    public List<String> procComment = new ArrayList<String>();
    @XmlElement(name = "mainProcedure")
    public Procedure mainProcedure;
    @XmlElement(name = "procedure")
    public List<Procedure> procedures = new ArrayList<Procedure>();
    @XmlElement(name = "function")
    public List<Function> functions = new ArrayList<Function>();

    public void toXML(File out) throws JAXBException, IOException {
        FileOutputStream fos = new FileOutputStream(out);
        JAXBContext context = JAXBContext.newInstance(this.getClass().getPackage().getName());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(this, fos);
        fos.close();
    }

    public static ProcedureCompilationUnit read(SignatureBlock info)
            throws RCodeReadException, IOException {
        ProcedureCompilationUnit unit = new ProcedureCompilationUnit();
        unit.mainProcedure = Procedure.readProcedure(info.getMainMethod());
        for (IInternalProcedure func : info.getInternalFunctions()) {
            unit.functions.add(Function.readFunction((IInternalFunction) func));
        }

        for (IInternalProcedure proc : info.getInternalProcedures()) {
            unit.procedures.add(Procedure.readProcedure(proc));
        }

        return unit;
    }
}
