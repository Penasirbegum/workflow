package com.report.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

import com.adobe.acs.commons.reports.api.ReportCellCSVExporter;

/**
 * An exporter for exporting formatted string values
 */
@Model(adaptables = Resource.class)
public class StringReportCellCSVExporter implements ReportCellCSVExporter {

	  @Inject
	  private String property;

	  @Inject
	  @Optional
	  private String format;

  
	  @Override
	  public String getValue(Object result) {
	    Resource resource = (Resource) result;
	    ReportCellValue val = new ReportCellValue(resource, property);
	    List<String> values = new ArrayList<>();
	    if (val.getValue() != null) {
	        values.add(val.getSingleValue());
	    }
	    if (StringUtils.isNotBlank(format)) {
	      for (int i = 0; i < values.size(); i++) {
	        values.set(i, String.format(format, values.get(i)));
	      }
	    }
	    return StringUtils.join(values, ";");
	  }

}