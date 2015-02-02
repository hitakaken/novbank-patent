package com.novbank.model.util;

import com.google.common.collect.Maps;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by ken on 15-2-1.
 */
public class OntModelGenerator {
    public Map<String,InputStream> inputSreams;
    public Map<String,String> formats;
    public OntModelGenerator(){
        this(null);
    }
    public OntModelGenerator(Map<String,InputStream> inputSreams){
        this(inputSreams,null);
    }
    public OntModelGenerator(Map<String,InputStream> inputSreams,Map<String,String> formats){
        this.inputSreams = inputSreams!=null?inputSreams: Maps.<String, InputStream>newLinkedHashMap();
        this.formats=formats!=null?formats: Maps.<String, String>newLinkedHashMap();
    }
    public void add(String pkgName,InputStream is){
        inputSreams.put(pkgName,is);
    }
    public void add(String pkgName,InputStream is,String format){
        inputSreams.put(pkgName,is);
        formats.put(pkgName,format);
    }

    public void read(){
        OntModel ontModel= ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        //读取本体文件
        // 读取当前路径下的文件，加载模型
        for(Map.Entry<String,InputStream> entry:inputSreams.entrySet()){
            System.out.println(entry.getKey());
            if(formats.containsKey(entry.getKey()))
                ontModel.read(entry.getValue(),"",formats.get(entry.getKey()));
            else
                ontModel.read(entry.getValue(),"");
        }
        //跟节点
        ExtendedIterator<OntClass> extiter= ontModel.listClasses();
        while(extiter.hasNext()){
            OntClass c=extiter.next();
            if(c.getURI()==null) continue;
            System.out.println(c.getModel().getGraph().getPrefixMapping().shortForm(c.getURI()));
            if(!c.isAnon()){
                // 迭代显示当前类的直接父类
                for (Iterator it = c.listSuperClasses(); it.hasNext();){
                    OntClass sp = (OntClass) it.next();
                    String str = c.getModel().getGraph().getPrefixMapping().shortForm(c.getURI())+ "'s superClass is ";//获取uri
                    String strSP = sp.getURI();
                    try {
                        str = str + ":" + strSP.substring(strSP.indexOf('#') + 1);
                        System.out.println("     Class" + str);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } // super class ends
                // 迭代显示当前类的直接子类
                for (Iterator it = c.listSubClasses(); it.hasNext();) {
                    System.out.print("     Class");
                    OntClass sb = (OntClass) it.next();
                    System.out.println(c.getModel().getGraph().getPrefixMapping().shortForm(c.getURI())+ "'s suberClass is "
                            + sb.getModel().getGraph().getPrefixMapping().shortForm(sb.getURI()));
                }// suber class ends
                // 迭代显示与当前类相关的所有属性
                for (Iterator ipp = c.listDeclaredProperties(); ipp.hasNext();) {
                    OntProperty p = (OntProperty) ipp.next();
                    if(p.isObjectProperty()){
                        System.out.println("ObjectProperty:" + p.getLocalName());
                    }
                }
            }
        }
    }

    public static void main(String[] args){
        OntModelGenerator generator = new OntModelGenerator();
        generator.add("rdf",OntModelGenerator.class.getResourceAsStream("/owl/rdf.nt"),"N3");
        generator.add("sumo",OntModelGenerator.class.getResourceAsStream("/owl/sumo.owl"));
        generator.add("pulo",OntModelGenerator.class.getResourceAsStream("/owl/pulo.owl"));
        generator.add("pmo",OntModelGenerator.class.getResourceAsStream("/owl/pmo.owl"));
        generator.add("pso",OntModelGenerator.class.getResourceAsStream("/owl/pso.owl"));
        generator.add("pdo",OntModelGenerator.class.getResourceAsStream("/owl/pdo.owl"));
        generator.read();

    }
}
