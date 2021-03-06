/* This file is part of the OWL API.
 * The contents of this file are subject to the LGPL License, Version 3.0.
 * Copyright 2014, The University of Manchester
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0 in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License. */
package org.semanticweb.owlapi.dlsyntax.renderer;

import static org.semanticweb.owlapi.dlsyntax.renderer.DLSyntax.AND;
import static org.semanticweb.owlapi.dlsyntax.renderer.DLSyntax.BOTTOM;
import static org.semanticweb.owlapi.dlsyntax.renderer.DLSyntax.COMMA;
import static org.semanticweb.owlapi.dlsyntax.renderer.DLSyntax.COMP;
import static org.semanticweb.owlapi.dlsyntax.renderer.DLSyntax.DISJOINT_WITH;
import static org.semanticweb.owlapi.dlsyntax.renderer.DLSyntax.EQUAL;
import static org.semanticweb.owlapi.dlsyntax.renderer.DLSyntax.EQUIVALENT_TO;
import static org.semanticweb.owlapi.dlsyntax.renderer.DLSyntax.EXISTS;
import static org.semanticweb.owlapi.dlsyntax.renderer.DLSyntax.FORALL;
import static org.semanticweb.owlapi.dlsyntax.renderer.DLSyntax.IMPLIES;
import static org.semanticweb.owlapi.dlsyntax.renderer.DLSyntax.IN;
import static org.semanticweb.owlapi.dlsyntax.renderer.DLSyntax.INVERSE;
import static org.semanticweb.owlapi.dlsyntax.renderer.DLSyntax.MAX;
import static org.semanticweb.owlapi.dlsyntax.renderer.DLSyntax.MIN;
import static org.semanticweb.owlapi.dlsyntax.renderer.DLSyntax.NOT;
import static org.semanticweb.owlapi.dlsyntax.renderer.DLSyntax.NOT_EQUAL;
import static org.semanticweb.owlapi.dlsyntax.renderer.DLSyntax.OR;
import static org.semanticweb.owlapi.dlsyntax.renderer.DLSyntax.SELF;
import static org.semanticweb.owlapi.dlsyntax.renderer.DLSyntax.SUBCLASS;
import static org.semanticweb.owlapi.dlsyntax.renderer.DLSyntax.TOP;
import static org.semanticweb.owlapi.dlsyntax.renderer.DLSyntax.WEDGE;
import static org.semanticweb.owlapi.util.CollectionFactory.sortOptionally;
import static org.semanticweb.owlapi.util.OWLAPIPreconditions.checkNotNull;
import static org.semanticweb.owlapi.util.OWLAPIPreconditions.verifyNotNull;
import static org.semanticweb.owlapi.util.OWLAPIStreamUtils.asList;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.IsAnonymous;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLCardinalityRestriction;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataComplementOf;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataIntersectionOf;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataOneOf;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLDataUnionOf;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDatatypeRestriction;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointUnionAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLFacetRestriction;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLHasValueRestriction;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasSelf;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectInverseOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLObjectVisitor;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLPropertyExpression;
import org.semanticweb.owlapi.model.OWLPropertyRange;
import org.semanticweb.owlapi.model.OWLPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLQuantifiedRestriction;
import org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.model.SWRLBuiltInAtom;
import org.semanticweb.owlapi.model.SWRLClassAtom;
import org.semanticweb.owlapi.model.SWRLDataPropertyAtom;
import org.semanticweb.owlapi.model.SWRLDataRangeAtom;
import org.semanticweb.owlapi.model.SWRLDifferentIndividualsAtom;
import org.semanticweb.owlapi.model.SWRLIndividualArgument;
import org.semanticweb.owlapi.model.SWRLLiteralArgument;
import org.semanticweb.owlapi.model.SWRLObjectPropertyAtom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.SWRLSameIndividualAtom;
import org.semanticweb.owlapi.model.SWRLVariable;
import org.semanticweb.owlapi.util.IRIShortFormProvider;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleIRIShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

/**
 * Renders objects in unicode DL syntax.
 *
 * @author Matthew Horridge, The University Of Manchester, Bio-Health Informatics Group
 * @since 2.2.0
 */
public class DLSyntaxObjectRenderer implements OWLObjectRenderer, OWLObjectVisitor {

    private final IRIShortFormProvider iriShortFormProvider = new SimpleIRIShortFormProvider();
    private ShortFormProvider shortFormProvider = new SimpleShortFormProvider();
    private StringBuilder buffer = new StringBuilder();
    @Nullable
    private OWLObject focusedObject;

    protected static boolean isBracketedIfNested(OWLObject object) {
        checkNotNull(object, "object cannot be null");
        return !(object instanceof OWLEntity);
    }

    /**
     * @param focusedObject focusedObject
     */
    public void setFocusedObject(@Nullable OWLObject focusedObject) {
        this.focusedObject = focusedObject;
    }

    /**
     * @param obj obj
     * @return true if obj is equal to focusedObject
     */
    public boolean isFocusedObject(OWLObject obj) {
        if (focusedObject == null) {
            return false;
        }
        return verifyNotNull(focusedObject).equals(obj);
    }

    @Override
    public void setPrefixManager(PrefixManager shortFormProvider) {
        this.shortFormProvider =
            checkNotNull(shortFormProvider, "shortFormProvider cannot be null");
    }

    @Override
    public void setShortFormProvider(ShortFormProvider shortFormProvider) {
        this.shortFormProvider =
            checkNotNull(shortFormProvider, "shortFormProvider cannot be null");
    }

    @Override
    public String render(OWLObject object) {
        buffer = new StringBuilder();
        checkNotNull(object, "object cannot be null").accept(this);
        return buffer.toString();
    }

    @Override
    public void visit(OWLOntology ontology) {
        checkNotNull(ontology, "ontology cannot be null");
        sortOptionally(ontology.logicalAxioms()).forEach(this::println);
    }

    protected void println(OWLLogicalAxiom ax) {
        ax.accept(this);
        write("\n");
    }

    protected void write(String s) {
        buffer.append(checkNotNull(s, "s cannot be null"));
    }

    protected void writeEntity(OWLEntity entity) {
        write(shortFormProvider.getShortForm(checkNotNull(entity, "entity cannot be null")));
    }

    protected void write(DLSyntax keyword) {
        write(checkNotNull(keyword, "keyword cannot be null").toString());
    }

    protected void write(int i) {
        write(Integer.toString(i));
    }

    private void writeObject(OWLObject object, boolean nest) {
        checkNotNull(object, "object cannot be null");
        if (nest) {
            roundedAnon(object);
        } else {
            object.accept(this);
        }
    }

    private void writeRestrictionSeparator() {
        write(".");
    }

    protected void write(Collection<? extends OWLObject> objects, DLSyntax delim, boolean nest) {
        checkNotNull(objects, "objects cannot be null");
        checkNotNull(delim, "delim cannot be null");
        if (objects.size() == 2) {
            Iterator<? extends OWLObject> it = objects.iterator();
            OWLObject o1 = it.next();
            OWLObject o2 = it.next();
            if (isFocusedObject(o1) || !isFocusedObject(o2)) {
                writeObject(o1, nest);
                spaced(delim);
                writeObject(o2, nest);
            } else {
                writeObject(o2, nest);
                spaced(delim);
                writeObject(o1, nest);
            }
        } else {
            for (Iterator<? extends OWLObject> it = objects.iterator(); it.hasNext();) {
                writeObject(it.next(), nest);
                if (it.hasNext()) {
                    spaced(delim);
                }
            }
        }
    }

    protected void spaced(DLSyntax delim) {
        writeSpace();
        write(delim);
        writeSpace();
    }

    protected void braced(DLSyntax delim) {
        write("{");
        write(delim);
        write("}");
    }

    protected void braced(OWLObject o) {
        write("{");
        o.accept(this);
        write("}");
    }

    @Override
    public void visit(OWLSubClassOfAxiom axiom) {
        checkNotNull(axiom, "axiom cannot be null");
        axiom.getSubClass().accept(this);
        subClass();
        axiom.getSuperClass().accept(this);
    }

    private void writePropertyAssertion(OWLPropertyAssertionAxiom<?, ?> ax) {
        checkNotNull(ax, "ax cannot be null");
        if (ax instanceof OWLNegativeObjectPropertyAssertionAxiom
            || ax instanceof OWLNegativeDataPropertyAssertionAxiom) {
            write(NOT);
        }
        triplet(ax.getProperty(), ax.getSubject(), ax.getObject());
    }

    protected void triplet(OWLObject a, OWLObject o1, OWLObject o2) {
        a.accept(this);
        write("(");
        o1.accept(this);
        write(", ");
        o2.accept(this);
        write(")");
    }

    protected void triplet(String a, OWLObject o1, OWLObject o2) {
        write(a);
        write("(");
        o1.accept(this);
        write(", ");
        o2.accept(this);
        write(")");
    }

    @Override
    public void visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
        write(NOT);
        writePropertyAssertion(axiom);
    }

    @Override
    public void visit(OWLReflexiveObjectPropertyAxiom axiom) {
        write(TOP);
        subClass();
        existPropertySelf(axiom.getProperty());
    }

    protected void subClass() {
        writeSpace();
        write(SUBCLASS);
        writeSpace();
    }

    @Override
    public void visit(OWLDisjointClassesAxiom axiom) {
        List<OWLClassExpression> descs = asList(axiom.classExpressions());
        for (int i = 0; i < descs.size() - 1; i++) {
            for (int j = i + 1; j < descs.size(); j++) {
                descs.get(i).accept(this);
                writeSpace();
                write(DISJOINT_WITH);
                writeSpace();
                descs.get(j).accept(this);
                if (j < descs.size() - 1) {
                    write(", ");
                }
            }
            if (i < descs.size() - 2) {
                write(", ");
            }
        }
    }

    @Override
    public void visit(OWLDataPropertyDomainAxiom axiom) {
        writeDomainAxiom(axiom);
    }

    @Override
    public void visit(OWLObjectPropertyDomainAxiom axiom) {
        writeDomainAxiom(axiom);
    }

    @Override
    public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {
        write(asList(axiom.properties()), EQUIVALENT_TO, false);
    }

    @Override
    public void visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
        write(NOT);
        writePropertyAssertion(axiom);
    }

    @Override
    public void visit(OWLDifferentIndividualsAxiom axiom) {
        write(asList(axiom.individuals()), NOT_EQUAL, false);
    }

    @Override
    public void visit(OWLDisjointDataPropertiesAxiom axiom) {
        write(asList(axiom.properties()), DISJOINT_WITH, false);
    }

    @Override
    public void visit(OWLDisjointObjectPropertiesAxiom axiom) {
        write(asList(axiom.properties()), DISJOINT_WITH, false);
    }

    @Override
    public void visit(OWLObjectPropertyRangeAxiom axiom) {
        writeRangeAxiom(axiom);
    }

    @Override
    public void visit(OWLObjectPropertyAssertionAxiom axiom) {
        writePropertyAssertion(axiom);
    }

    private void writeDomainAxiom(OWLPropertyDomainAxiom<?> axiom) {
        write(EXISTS);
        writeSpace();
        axiom.getProperty().accept(this);
        writeRestrictionSeparator();
        write(TOP);
        subClass();
        roundedAnon(axiom.getDomain());
    }

    private void writeRangeAxiom(OWLPropertyRangeAxiom<?, ?> axiom) {
        checkNotNull(axiom, "axiom cannot be null");
        write(TOP);
        subClass();
        write(FORALL);
        writeSpace();
        axiom.getProperty().accept(this);
        writeRestrictionSeparator();
        roundedAnon(axiom.getRange());
    }

    private void writeFunctionalProperty(OWLPropertyExpression property) {
        checkNotNull(property, "property cannot be null");
        write(TOP);
        subClass();
        write(MAX);
        writeSpace();
        write(1);
        writeSpace();
        property.accept(this);
    }

    @Override
    public void visit(OWLFunctionalObjectPropertyAxiom axiom) {
        writeFunctionalProperty(axiom.getProperty());
    }

    @Override
    public void visit(OWLSubObjectPropertyOfAxiom axiom) {
        axiom.getSubProperty().accept(this);
        subClass();
        axiom.getSuperProperty().accept(this);
    }

    @Override
    public void visit(OWLDisjointUnionAxiom axiom) {
        axiom.getOWLClass().accept(this);
        write(EQUAL);
        write(asList(axiom.classExpressions()), OR, false);
    }

    @Override
    public void visit(OWLSymmetricObjectPropertyAxiom axiom) {
        equivalent(axiom.getProperty(), axiom.getProperty());
    }

    private void writeSpace() {
        write(" ");
    }

    @Override
    public void visit(OWLDataPropertyRangeAxiom axiom) {
        writeRangeAxiom(axiom);
    }

    @Override
    public void visit(OWLFunctionalDataPropertyAxiom axiom) {
        writeFunctionalProperty(axiom.getProperty());
    }

    @Override
    public void visit(OWLEquivalentDataPropertiesAxiom axiom) {
        write(asList(axiom.properties()), EQUIVALENT_TO, false);
    }

    @Override
    public void visit(OWLClassAssertionAxiom axiom) {
        roundedAnon(axiom.getClassExpression());
        rounded(axiom.getIndividual());
    }

    protected void rounded(OWLObject o) {
        write("(");
        o.accept(this);
        write(")");
    }

    protected <T extends OWLObject & IsAnonymous> void roundedAnon(T o) {
        if (o.isAnonymous()) {
            rounded(o);
        } else {
            o.accept(this);
        }
    }

    @Override
    public void visit(OWLEquivalentClassesAxiom axiom) {
        write(asList(axiom.classExpressions()), EQUIVALENT_TO, false);
    }

    @Override
    public void visit(OWLDataPropertyAssertionAxiom axiom) {
        writePropertyAssertion(axiom);
    }

    @Override
    public void visit(OWLTransitiveObjectPropertyAxiom axiom) {
        axiom.getProperty().accept(this);
        writeSpace();
        write(IN);
        writeSpace();
        write("R");
        write("\u207A");
    }

    @Override
    public void visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
        write(TOP);
        subClass();
        write(NOT);
        existPropertySelf(axiom.getProperty());
    }

    @Override
    public void visit(OWLAsymmetricObjectPropertyAxiom axiom) {
        axiom.getProperty().accept(this);
        writeSpace();
        write(DISJOINT_WITH);
        writeSpace();
        axiom.getProperty().accept(this);
        write(INVERSE);
    }

    @Override
    public void visit(OWLSubDataPropertyOfAxiom axiom) {
        axiom.getSubProperty().accept(this);
        subClass();
        axiom.getSuperProperty().accept(this);
    }

    @Override
    public void visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
        write(TOP);
        subClass();
        write(MAX);
        writeSpace();
        write(1);
        writeSpace();
        axiom.getProperty().accept(this);
        write(INVERSE);
    }

    @Override
    public void visit(OWLSameIndividualAxiom axiom) {
        write(asList(axiom.individuals()), EQUAL, false);
    }

    @Override
    public void visit(OWLSubPropertyChainOfAxiom axiom) {
        write(axiom.getPropertyChain(), COMP, false);
        subClass();
        axiom.getSuperProperty().accept(this);
    }

    @Override
    public void visit(OWLInverseObjectPropertiesAxiom axiom) {
        OWLObject o1 = axiom.getFirstProperty();
        OWLObject o2 = axiom.getSecondProperty();
        OWLObject first;
        OWLObject second;
        if (isFocusedObject(o1) || !isFocusedObject(o2)) {
            first = o1;
            second = o2;
        } else {
            first = o2;
            second = o1;
        }
        equivalent(first, second);
    }

    private void equivalent(OWLObject first, OWLObject second) {
        first.accept(this);
        writeSpace();
        write(EQUIVALENT_TO);
        writeSpace();
        second.accept(this);
        write(INVERSE);
    }

    @Override
    public void visit(SWRLRule rule) {
        write(asList(rule.head()), WEDGE, false);
        writeSpace();
        write(IMPLIES);
        writeSpace();
        write(asList(rule.body()), WEDGE, false);
    }

    @Override
    public void visit(OWLClass ce) {
        if (ce.isOWLThing()) {
            write(TOP);
        } else if (ce.isOWLNothing()) {
            write(BOTTOM);
        } else {
            writeEntity(ce);
        }
    }

    @Override
    public void visit(OWLObjectIntersectionOf ce) {
        write(asList(ce.operands()), AND, true);
    }

    @Override
    public void visit(OWLObjectUnionOf ce) {
        write(asList(ce.operands()), OR, true);
    }

    @Override
    public void visit(OWLObjectComplementOf ce) {
        write(NOT);
        roundedAnon(ce.getOperand());
    }

    private <T extends OWLPropertyRange> void writeCardinalityRestriction(
        OWLCardinalityRestriction<T> restriction, DLSyntax keyword) {
        write(keyword);
        writeSpace();
        write(restriction.getCardinality());
        writeSpace();
        restriction.getProperty().accept(this);
        writeRestrictionSeparator();
        roundedAnon(restriction.getFiller());
    }

    private <T extends OWLPropertyRange> void writeQuantifiedRestriction(
        OWLQuantifiedRestriction<T> restriction, DLSyntax keyword) {
        write(keyword);
        writeSpace();
        restriction.getProperty().accept(this);
        writeRestrictionSeparator();
        roundedAnon(restriction.getFiller());
    }

    private <V extends OWLObject> void writeValueRestriction(OWLHasValueRestriction<V> restriction,
        OWLPropertyExpression p) {
        write(EXISTS);
        writeSpace();
        p.accept(this);
        writeRestrictionSeparator();
        braced(restriction.getFiller());
    }

    protected void existPropertySelf(OWLObject o) {
        write(EXISTS);
        writeSpace();
        o.accept(this);
        writeRestrictionSeparator();
        write(SELF);
    }

    @Override
    public void visit(OWLObjectSomeValuesFrom ce) {
        writeQuantifiedRestriction(ce, EXISTS);
    }

    @Override
    public void visit(OWLObjectAllValuesFrom ce) {
        writeQuantifiedRestriction(ce, FORALL);
    }

    @Override
    public void visit(OWLObjectHasValue ce) {
        writeValueRestriction(ce, ce.getProperty());
    }

    @Override
    public void visit(OWLObjectMinCardinality ce) {
        writeCardinalityRestriction(ce, MIN);
    }

    @Override
    public void visit(OWLObjectExactCardinality ce) {
        writeCardinalityRestriction(ce, EQUAL);
    }

    @Override
    public void visit(OWLObjectMaxCardinality ce) {
        writeCardinalityRestriction(ce, MAX);
    }

    @Override
    public void visit(OWLObjectHasSelf ce) {
        existPropertySelf(ce.getProperty());
    }

    @Override
    public void visit(OWLObjectOneOf ce) {
        iterate(ce.individuals().iterator());
    }

    protected void iterate(Iterator<? extends OWLObject> it) {
        while (it.hasNext()) {
            spacedAndBraced(it);
        }
    }

    @Override
    public void visit(OWLDataSomeValuesFrom ce) {
        writeQuantifiedRestriction(ce, EXISTS);
    }

    @Override
    public void visit(OWLDataAllValuesFrom ce) {
        writeQuantifiedRestriction(ce, FORALL);
    }

    @Override
    public void visit(OWLDataHasValue ce) {
        writeValueRestriction(ce, ce.getProperty());
    }

    @Override
    public void visit(OWLDataMinCardinality ce) {
        writeCardinalityRestriction(ce, MIN);
    }

    @Override
    public void visit(OWLDataExactCardinality ce) {
        writeCardinalityRestriction(ce, EQUAL);
    }

    @Override
    public void visit(OWLDataMaxCardinality ce) {
        writeCardinalityRestriction(ce, MAX);
    }

    @Override
    public void visit(OWLDatatype node) {
        writeEntity(node);
    }

    @Override
    public void visit(OWLDataComplementOf node) {
        write(NOT);
        node.getDataRange().accept(this);
    }

    @Override
    public void visit(OWLDataOneOf node) {
        iterate(node.values().iterator());
    }

    protected void spacedAndBraced(Iterator<? extends OWLObject> it) {
        braced(it.next());
        if (it.hasNext()) {
            spaced(OR);
        }
    }

    @Override
    public void visit(OWLDatatypeRestriction node) {
        // XXX complete
    }

    @Override
    public void visit(OWLLiteral node) {
        write(node.getLiteral());
    }

    @Override
    public void visit(OWLFacetRestriction node) {
        // XXX complete
    }

    @Override
    public void visit(OWLObjectProperty property) {
        writeEntity(property);
    }

    @Override
    public void visit(OWLObjectInverseOf property) {
        property.getInverse().accept(this);
        write(INVERSE);
    }

    @Override
    public void visit(OWLDataProperty property) {
        writeEntity(property);
    }

    @Override
    public void visit(OWLNamedIndividual individual) {
        writeEntity(individual);
    }

    @Override
    public void visit(SWRLClassAtom node) {
        duet(node.getPredicate(), node.getArgument());
    }

    protected void duet(OWLObject a, OWLObject o) {
        duet("", a, o);
    }

    protected void duet(String prefix, OWLObject a, OWLObject o) {
        write(prefix);
        a.accept(this);
        rounded(o);
    }

    @Override
    public void visit(SWRLDataRangeAtom node) {
        duet(node.getPredicate(), node.getArgument());
    }

    @Override
    public void visit(SWRLObjectPropertyAtom node) {
        triplet(node.getPredicate(), node.getFirstArgument(), node.getSecondArgument());
    }

    @Override
    public void visit(SWRLDataPropertyAtom node) {
        triplet(node.getPredicate(), node.getFirstArgument(), node.getSecondArgument());
    }

    @Override
    public void visit(IRI iri) {
        write(iriShortFormProvider.getShortForm(iri));
    }

    @Override
    public void visit(SWRLBuiltInAtom node) {
        node.getPredicate().accept(this);
        write("(");
        write(node.getArguments(), COMMA, true);
        write(")");
    }

    @Override
    public void visit(SWRLVariable node) {
        write("?");
        node.getIRI().accept(this);
    }

    @Override
    public void visit(SWRLIndividualArgument node) {
        node.getIndividual().accept(this);
    }

    @Override
    public void visit(SWRLLiteralArgument node) {
        node.getLiteral().accept(this);
    }

    @Override
    public void visit(SWRLSameIndividualAtom node) {
        triplet("sameAs", node.getFirstArgument(), node.getSecondArgument());
    }

    @Override
    public void visit(SWRLDifferentIndividualsAtom node) {
        triplet("differentFrom", node.getFirstArgument(), node.getSecondArgument());
    }

    @Override
    public void visit(OWLDataIntersectionOf node) {
        write(asList(node.operands()), AND, true);
    }

    @Override
    public void visit(OWLDataUnionOf node) {
        write(asList(node.operands()), OR, true);
    }
}
