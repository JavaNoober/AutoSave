package com.noober.helper;

import com.noober.api.NeedSave;

import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by xiaoqi on 2017/1/7
 */

public class HelperSavedValues {
	VariableElement encloseElement;
	public HelperSavedValues(Element encloseElement) {
		this.encloseElement = (VariableElement)encloseElement;
	}

	public Element getEncloseElement() {
		return encloseElement;
	}

	public TypeMirror getFieldType(){
		return encloseElement.asType();
	}

	public ElementKind getKind(){
		return encloseElement.getKind();
	}

	public VariableElement getElement(){
		return encloseElement;
	}

	public Name getSimpleName(){
		return encloseElement.getSimpleName();
	}

	public boolean isPublic(){
		Set<Modifier> set = ((Element)encloseElement).getModifiers();
		for(Modifier modifier : set){
			if(modifier.equals(Modifier.PUBLIC)){
				return true;
			}
		}
		return false;
	}

	public boolean isPrivate(){
		Set<Modifier> set = ((Element)encloseElement).getModifiers();
		for(Modifier modifier : set){
			if(modifier.equals(Modifier.PRIVATE)){
				return true;
			}
		}
		return false;
	}

	public boolean isPersistable(){
		return encloseElement.getAnnotation(NeedSave.class).isPersistable();
	}
}
