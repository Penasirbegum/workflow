/*
 * #%L
 * ACS AEM Commons Bundle
 * %%
 * Copyright (C) 2017 Adobe
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.report.core.models;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * Represents a single cell within the report. Allows for detecting if the value
 * is scalar or single.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ReportCellValue {

  @ValueMapValue
  private String property;

  @RequestAttribute
  private Resource result;

  private Object value;

  public ReportCellValue() {
  }

  public ReportCellValue(Resource result, String property) {
    this.result = result;
    this.property = property;
    init();
  }
  
  public String getSingleValue() {
	    return value.toString();
  }  

  public Object getValue() {
    return value;
  }

  @PostConstruct
  private void init() {
		/*
		 * value = result != null && result.getValueMap() != null ?
		 * result.getValueMap().get(property) : null;
		 */
	  String valueStr ="";
	  String propertyValStr = property;
	  if(null != result && result.getValueMap() != null && null != property) {
		  ValueMap resVm = result.getValueMap();
		  List<String> propertyNames = getPropertyNames();
		  if(null != propertyNames && propertyNames.size() > 0) {
			 for(String propName:propertyNames){
				 String propVal = resVm.get(propName,"");
				 String patternStr = Pattern.quote("{"+propName+"}");
				 propertyValStr = propertyValStr.replaceFirst(patternStr, propVal);
			 }
		  }
		  valueStr = propertyValStr;
	  }
	  value = valueStr;
  }

  private List<String> getPropertyNames() {
	  List<String> propertyNames = new ArrayList<>();
	  String propertyStr = property;
	  while(propertyStr.contains("{") && propertyStr.contains("}")) {
			int startIndex = propertyStr.indexOf("{");
			int endIndex = propertyStr.indexOf("}");
			String propName = propertyStr.substring(startIndex+1, endIndex);
			String patternStr = Pattern.quote("{"+propName+"}");
			propertyStr = propertyStr.replaceFirst(patternStr, "");
			propertyNames.add(propName);		
		}
	  return propertyNames;
  }
  
  public boolean isArray() {
    return value != null && value.getClass().isArray();
  }
  
}
