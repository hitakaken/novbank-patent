package com.novbank.model.util;

import com.google.common.collect.Maps;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import java.util.Map;

/**
 * Created by ken on 15-2-1.
 */
public class OntModelGenerator {
    public Map<String,String> map;
    public OntModelGenerator(){
        map= Maps.newHashMap();
    }
    public OntModelGenerator(Map<String,String> map){
        this.map=map;
    }

    public void read(){
        OntModel ontModel= ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);

    }

    public void main(String[] args){
        OntModelGenerator generator = new OntModelGenerator();

    }
}
