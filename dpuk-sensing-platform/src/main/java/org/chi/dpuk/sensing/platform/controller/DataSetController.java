package org.chi.dpuk.sensing.platform.controller;

import org.chi.dpuk.sensing.platform.dataSet.DataSetService;
import org.chi.dpuk.sensing.platform.model.DataSet;
import org.chi.dpuk.sensing.platform.model.DataSource;
import org.chi.dpuk.sensing.platform.view.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;

@Controller
@RequestMapping("/api/dataSet")
public class DataSetController {

	@Autowired
	private DataSetService dataSetService;

	@RequestMapping(value = "/{dataSetId}", method = RequestMethod.GET)
	@JsonView(View.DataSet.class)
	public @ResponseBody DataSet getDataSet(@PathVariable Long dataSetId) {
		return dataSetService.getDataSet(dataSetId);
	}

	@RequestMapping(value = "/{dataSetId}", method = RequestMethod.DELETE)
	@JsonView(View.DataSet.class)
	public @ResponseBody DataSet deleteDataSet(@PathVariable Long dataSetId) /* throws ForbiddenAccessException */ {
		DataSet dataSet = dataSetService.getDataSet(dataSetId);

		if (dataSet != null) {
			// TODO: Determine if user is authorised to delete data set here.
			dataSet.setDataSource(null); // Detach the dataset from the datasource
			dataSetService.updateDataSet(dataSet);// Persist it
			dataSetService.deleteDataSet(dataSetId); // Delete it
		}

		return dataSet;
	}
	
	@RequestMapping(value = "/dataSource/{dataSetId}", method = RequestMethod.GET)
	@JsonView(View.DataSource.class)
	public @ResponseBody DataSource getDataSourceForDataSet(@PathVariable Long dataSetId) {
		return dataSetService.getDataSourceForDataSet(dataSetId);
	}

	@RequestMapping(value = "/download/{dataSetId}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> download(@PathVariable Long dataSetId) {
		DataSet dataSet = dataSetService.getDataSet(dataSetId);
		HttpHeaders httpHeaders = new HttpHeaders();
		String headerKey = "file-name";
		String headerValue = dataSet.getPayloadNameWithExtension();
		httpHeaders.set(headerKey, headerValue);
		httpHeaders.setContentDispositionFormData(dataSet.getPayloadNameWithExtension(),
				dataSet.getPayloadNameWithExtension());
		httpHeaders.setContentType(MediaType.parseMediaType(dataSet.getPayloadContentType()));
		return new ResponseEntity<byte[]>(dataSet.getPayload(), httpHeaders, HttpStatus.OK);
	}
}