package org.chi.dpuk.sensing.platform.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.chi.dpuk.sensing.platform.dataSource.DataSourceService;
import org.chi.dpuk.sensing.platform.dto.PayloadDTO;
import org.chi.dpuk.sensing.platform.exception.DataSetEmptyException;
import org.chi.dpuk.sensing.platform.exception.DataSetNotUniqueWithinAStudyException;
import org.chi.dpuk.sensing.platform.exception.DataSetUploadException;
import org.chi.dpuk.sensing.platform.exception.DataSourceAuthenticationException;
import org.chi.dpuk.sensing.platform.exception.DataSourceSameNameException;
import org.chi.dpuk.sensing.platform.exception.InvalidDataSourceUserNameException;
import org.chi.dpuk.sensing.platform.exception.StartDateAfterEndDateException;
import org.chi.dpuk.sensing.platform.model.DataSet;
import org.chi.dpuk.sensing.platform.model.DataSource;
import org.chi.dpuk.sensing.platform.model.Participant;
import org.chi.dpuk.sensing.platform.model.Study;
import org.chi.dpuk.sensing.platform.participant.ParticipantService;
import org.chi.dpuk.sensing.platform.security.HashingUtility;
import org.chi.dpuk.sensing.platform.view.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;

@Controller
@RequestMapping("/api/dataSource")
public class DataSourceController {
	
	private static final String ALPHA_NUMERIC_REGEX = "^[0-9a-zA-Z-]+$";

	@Autowired
	private DataSourceService dataSourceService;

	@Autowired
	private ParticipantService participantService;

	@RequestMapping(method = RequestMethod.GET)
	@JsonView(View.DataSource.class)
	public @ResponseBody List<DataSource> getAllDataSources() {
		return dataSourceService.getAllDataSources();
	}

	@RequestMapping(value = "/{dataSourceId}", method = RequestMethod.GET)
	@JsonView(View.DataSource.class)
	public @ResponseBody DataSource getDataSource(@PathVariable Long dataSourceId) {
		return dataSourceService.getDataSource(dataSourceId);
	}

	// Specify an 'id' in the JSON to update, or omit 'id' to add.
	@RequestMapping(method = RequestMethod.POST)
	@JsonView(View.DataSource.class)
	public @ResponseBody DataSource addOrUpdateDataSource(@RequestBody DataSource dataSource) {
		validateDataSourceUserName(dataSource);
		encryptPassword(dataSource);
		String uiDataSourceName = dataSource.getName();
		if (dataSource != null && uiDataSourceName != null) {
			dataSource.setName(uiDataSourceName.trim());
		}
		// TODO:- This needs to be re-visited once as it might be redundant once
		// necessary database constraints have been implemented

		Long dataSourceId = dataSource.getId();
		// It is an update or edit
		if (dataSourceId != null) {
			DataSource persistedDataSource = dataSourceService.getDataSource(dataSourceId);
			if (uiDataSourceName != null) {
				if (!persistedDataSource.getName().equals(uiDataSourceName)) {
					if (validateDataSourceWithTheSameNameCanBeAddedToTheSameParticipant(
							dataSource.getParticipant().getId(), dataSourceId)) {
						throw new DataSourceSameNameException();
					}
				}
			}
		}
		return dataSourceService.addOrUpdateDataSource(dataSource);
	}

	private void validateDataSourceUserName(DataSource dataSource) {
		if (dataSource == null || dataSource.getUserName() == null) {
			throw new InvalidDataSourceUserNameException();
		}
		if (!isAlphaNumeric(dataSource.getUserName().trim())) {
			throw new InvalidDataSourceUserNameException();
		}
		dataSource.setUserName(dataSource.getUserName().trim());
	}

	private boolean isAlphaNumeric(String inputString) {
		String pattern = ALPHA_NUMERIC_REGEX;
		if (inputString.matches(pattern)) {
			return true;
		}
		return false;
	}

	@RequestMapping(value = "/{dataSourceId}", method = RequestMethod.DELETE)
	@JsonView(View.DataSource.class)
	public @ResponseBody DataSource deleteDataSource(@PathVariable Long dataSourceId) {
		DataSource dataSource = dataSourceService.getDataSource(dataSourceId);

		if (dataSource != null) {
			// TODO: Determine if user is authorised to delete data source here.
			// Detach the participant from the data source
			dataSource.setParticipant(null);
			dataSourceService.addOrUpdateDataSource(dataSource);// Persist it
			dataSourceService.deleteDataSource(dataSourceId); // Delete it
		}

		return dataSource;
	}

	@RequestMapping(value = "/participant/{dataSourceId}", method = RequestMethod.GET)
	@JsonView(View.Participant.class)
	public @ResponseBody Participant getParticipantForDataSource(@PathVariable Long dataSourceId) {
		return dataSourceService.getParticipantForDataSource(dataSourceId);
	}

	@ResponseBody
	@RequestMapping(value = "/addParticipantToDataSource/{participantId}/{dataSourceId}", method = RequestMethod.POST)
	@JsonView(View.DataSource.class)
	public DataSource addParticipantToDataSource(@PathVariable Long participantId, @PathVariable Long dataSourceId) {
		if (validateDataSourceWithTheSameNameCanBeAddedToTheSameParticipant(participantId, dataSourceId)) {
			// delete stale dataSource
			dataSourceService.deleteDataSource(dataSourceId);
			throw new DataSourceSameNameException();
		}
		return dataSourceService.addParticipantToDataSource(participantId, dataSourceId);
	}

	private boolean validateDataSourceWithTheSameNameCanBeAddedToTheSameParticipant(Long participantId,
			Long dataSourceId) {
		if (participantId == null || dataSourceId == null) {
			throw new DataSourceSameNameException();
		}
		Set<DataSource> dataSourcesForParticipant = participantService.getDataSourcesForParticipant(participantId);
		DataSource dataSource = dataSourceService.getDataSource(dataSourceId);
		if (dataSourcesForParticipant == null || dataSource == null) {
			throw new DataSourceSameNameException();
		}

		boolean doesDataSourceWithSameNameExist = false;

		for (DataSource dataSourceForParticipant : dataSourcesForParticipant) {
			if (dataSource.getName().equalsIgnoreCase(dataSourceForParticipant.getName())) {
				doesDataSourceWithSameNameExist = true;
				break;
			}
		}

		return doesDataSourceWithSameNameExist;
	}

	@RequestMapping(value = "/dataSet/{dataSourceId}", method = RequestMethod.GET)
	@JsonView(View.DataSet.class)
	public @ResponseBody Set<DataSet> getDataSetsForDataSource(@PathVariable Long dataSourceId) {
		return dataSourceService.getDataSetsForDataSource(dataSourceId);
	}

	@ResponseBody
	@RequestMapping(value = "/postDataSet", method = RequestMethod.POST)
	@JsonView(View.DataSource.class)
	public DataSource postDataSet(@RequestBody PayloadDTO payloadDTO) {
		DataSet dataSet = new DataSet();
		if (payloadDTO == null || payloadDTO.getDataSourceUserName() == null
				|| payloadDTO.getDataSourcePassword() == null) {
			throw new DataSourceAuthenticationException();
		}
		DataSource dataSource = dataSourceService.getDataSource(payloadDTO.getDataSourceUserName());
		if (dataSource == null || dataSource.getId() == null) {
			throw new DataSourceAuthenticationException();
		}
		dataSet.setDataSourceUserName(payloadDTO.getDataSourceUserName());
		dataSet.setDataSourcePassword(payloadDTO.getDataSourcePassword());
		authenticateDataSourceCredentials(dataSet, dataSource.getId());
		validateAndHandleDates(payloadDTO.getStartDate(), payloadDTO.getEndDate(), dataSet);
		validateAndHandlePayload(payloadDTO.getPayload(), payloadDTO.getPayloadNameWithExtension(),
				payloadDTO.getPayloadContentType(), dataSet);
		calculateDataSetHash(dataSet);
		if (!isDataSetUniqueWithinAStudy(dataSet.getDataSetHash(), dataSource.getId())) {
			throw new DataSetNotUniqueWithinAStudyException();
		}
		return dataSourceService.addDataSetToDataSource(dataSet, dataSource.getId());
	}

	@ResponseBody
	@RequestMapping(value = "/addDataSetToDataSource/{dataSourceId}", method = RequestMethod.POST)
	@JsonView(View.DataSource.class)
	public DataSource addDataSetToDataSource(@PathVariable Long dataSourceId, @RequestParam("file") MultipartFile file,
			@RequestParam("startDate") Date startDate, @RequestParam("endDate") Date endDate) {
		DataSet dataSet = new DataSet();
		handleFileUpload(dataSet, file);
		handleStartAndEndDate(startDate, endDate, dataSet);
		calculateDataSetHash(dataSet);
		if (!isDataSetUniqueWithinAStudy(dataSet.getDataSetHash(), dataSourceId)) {
			throw new DataSetNotUniqueWithinAStudyException();
		}
		return dataSourceService.addDataSetToDataSource(dataSet, dataSourceId);
	}

	private boolean isDataSetUniqueWithinAStudy(String dataSetHash, Long dataSourceId) {
		if (dataSourceId == null || dataSetHash == null) {
			return false;
		}
		Participant participantForDataSource = dataSourceService.getParticipantForDataSource(dataSourceId);
		if (participantForDataSource == null) {
			return false;
		}
		Long participantForDataSourceId = participantForDataSource.getId();
		if (participantForDataSourceId == null) {
			return false;
		}
		Study study = participantService.getStudyForParticipant(participantForDataSourceId);
		if (study == null || study.getId() == null) {
			return false;
		}
		Set<Participant> studyAllParticipants = study.getParticipants();
		if (studyAllParticipants == null) {
			return false;
		}
		for (Participant studyParticipant : studyAllParticipants) {
			Set<DataSource> participantDataSources = studyParticipant.getDataSources();
			if (participantDataSources == null) {
				return false;
			}
			for (DataSource participantDataSource : participantDataSources) {
				Set<DataSet> dataSourceDataSets = participantDataSource.getDataSets();
				if (dataSourceDataSets == null) {
					return false;
				}
				for (DataSet dataSourceDataSet : dataSourceDataSets) {
					if (dataSourceDataSet.getDataSetHash() == null) {
						return false;
					}
					if (dataSourceDataSet.getDataSetHash().equals(dataSetHash)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private void authenticateDataSourceCredentials(DataSet dataSet, Long dataSourceId) {
		DataSource dataSource = this.getDataSource(dataSourceId);
		if (dataSource == null) {
			throw new DataSourceAuthenticationException();
		}
		if (!dataSource.getUserName().equalsIgnoreCase(dataSet.getDataSourceUserName())
				|| !HashingUtility.doPasswordsMatch(dataSet.getDataSourcePassword(), dataSource.getPassword())) {
			throw new DataSourceAuthenticationException();
		}
	}

	private void validateAndHandleDates(String startDate, String endDate, DataSet dataSet) {
		if (startDate == null || endDate == null) {
			throw new DataSetUploadException();
		}
		if (!isValidDate(startDate) || !isValidDate(endDate)) {
			throw new DataSetUploadException();
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

		try {
			if (simpleDateFormat.parse(startDate).after(simpleDateFormat.parse(endDate))) {
				throw new StartDateAfterEndDateException();
			}
			dataSet.setStartDate(simpleDateFormat.parse(startDate));
			dataSet.setEndDate(simpleDateFormat.parse(endDate));
		} catch (ParseException e) {
			throw new DataSetUploadException();
		}
	}

	private boolean isValidDate(String inputDate) {
		boolean isValid = true;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		if (inputDate == null) {
			isValid = false;
		}
		try {
			simpleDateFormat.parse(inputDate);
		} catch (ParseException e) {
			isValid = false;
		}
		return isValid;
	}

	private void validateAndHandlePayload(String payload, String payloadNameWithExtension, String payloadContentType,
			DataSet dataSet) {
		if (payload == null || payloadNameWithExtension == null || payloadContentType == null) {
			throw new DataSetEmptyException();
		}
		dataSet.setPayload(payload.getBytes());
		dataSet.setPayloadNameWithExtension(payloadNameWithExtension);
		dataSet.setPayloadContentType(payloadContentType);
	}

	private void handleFileUpload(DataSet dataSet, MultipartFile file) {
		if (file == null) {
			throw new DataSetEmptyException();
		}
		if (!file.isEmpty()) {
			try {
				dataSet.setPayload(file.getBytes());
				dataSet.setPayloadNameWithExtension(file.getOriginalFilename());
				dataSet.setPayloadContentType(file.getContentType());
			} catch (IOException e) {
				throw new DataSetUploadException();
			}
		} else {
			throw new DataSetEmptyException();
		}
	}

	private void calculateDataSetHash(DataSet dataSet) {
		StringBuffer calculatedDataSetHash = new StringBuffer();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		calculatedDataSetHash.append(DigestUtils.md5Hex(dataSet.getPayload()));
		calculatedDataSetHash.append(DigestUtils.md5Hex(simpleDateFormat.format(dataSet.getStartDate())));
		calculatedDataSetHash.append(DigestUtils.md5Hex(simpleDateFormat.format(dataSet.getEndDate())));

		dataSet.setDataSetHash(calculatedDataSetHash.toString());
	}

	private void handleStartAndEndDate(Date startDate, Date endDate, DataSet dataSet) {
		if (startDate.after(endDate)) {
			throw new StartDateAfterEndDateException();
		}
		dataSet.setStartDate(startDate);
		dataSet.setEndDate(endDate);
	}

	private void encryptPassword(DataSource dataSource) {
		String password = dataSource.getPassword();
		if (password != null) {
			dataSource.setPassword(HashingUtility.hashPassword(password));
		}
	}

	@RequestMapping(value = "/doesDataSourceExist", method = RequestMethod.GET, params = "userName")
	@JsonView(View.DataSource.class)
	public @ResponseBody Boolean doesDataSourceExist(@RequestParam String userName) {
		return dataSourceService.doesDataSourceExist(userName);
	}
}