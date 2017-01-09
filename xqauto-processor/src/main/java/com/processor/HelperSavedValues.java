package com.processor;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by xiaoqi on 2017/1/7.
 */

public class HelperSavedValues {
	VariableElement encloseElement;

	public HelperSavedValues(Element encloseElement) {
		this.encloseElement = (VariableElement)encloseElement;
	}

	public TypeMirror getFieldType(){
		return encloseElement.asType();
	}

	public Name getSimpleName(){
		return encloseElement.getSimpleName();
	}
}
