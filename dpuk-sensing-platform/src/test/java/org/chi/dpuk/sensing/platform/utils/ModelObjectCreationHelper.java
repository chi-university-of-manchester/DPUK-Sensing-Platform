package org.chi.dpuk.sensing.platform.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.chi.dpuk.sensing.platform.model.DataSet;
import org.chi.dpuk.sensing.platform.model.DataSource;
import org.chi.dpuk.sensing.platform.model.Participant;
import org.chi.dpuk.sensing.platform.model.Study;
import org.chi.dpuk.sensing.platform.model.User;
import org.chi.dpuk.sensing.platform.model.User.Role;
import org.chi.dpuk.sensing.platform.security.HashingUtility;

/*
 * This class is used to create commonly used model objects for testing purposes
 * Some of these objects will not have all fields set, so the methods may need updating for future tests.
 */
public class ModelObjectCreationHelper {

	private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ModelObjectCreationHelper.class);

	public static User createTestResearcher() {
		User researcher = new User();
		researcher.setUserName("researcher");
		researcher.setPasswordHash(HashingUtility.hashPassword("admin123"));
		researcher.setEmail("researcher.first.name@manchester.ac.uk");
		researcher.setRole(Role.ROLE_RESEARCHER);
		researcher.setTitle("Mr");
		researcher.setFirstName("Researcher First Name");
		researcher.setLastName("Researcher Last Name");
		return researcher;
	}

	public static DataSource createTestDataSource() {
		DataSource datasource = new DataSource();
		datasource.setName("DataSource Name");
		datasource.setDescription("DataSource Description");
		datasource.setType("DataSource Type");
		datasource.setUserName("DataSourceUserName");
		datasource.setPassword("DataSource Password");
		return datasource;
	}

	public static Date parseDateTime(String dateString) {
		if (dateString == null)
			return null;
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		if (dateString.contains("T"))
			dateString = dateString.replace('T', ' ');
		if (dateString.contains("Z"))
			dateString = dateString.replace("Z", "+0000");
		try {
			return formatter.parse(dateString);
		} catch (ParseException e) {
			log.info("unable to convert string to date: " + dateString);
		}

		return null;
	}

	public static DataSet createTestDataSet() {
		String startDate = "2015-10-15T14:15:00.123Z";
		String endDate = "2015-10-15T15:15:00.456Z";

		DataSet dataSet = new DataSet();

		dataSet.setStartDate(ModelObjectCreationHelper.parseDateTime(startDate));
		dataSet.setEndDate(ModelObjectCreationHelper.parseDateTime(endDate));
		dataSet.setPayload("xmxmxmxmxmxmxmxmxmxmxmxmxmx".getBytes());
		dataSet.setPayloadContentType("Payload Content Type 1");
		dataSet.setPayloadNameWithExtension("Payload Name With Extension 1");

		return dataSet;
	}

	public static Study createTestStudy() {
		Study study = new Study();
		study.setName("Study Name");
		study.setDescription("Study Description");
		return study;
	}

	public static Participant createTestParticipant() {
		Participant participant = new Participant();
		participant.setName("some name 1");
		participant.setAddress("some address 1");
		participant.setContactNumber("01132456789");
		participant.setNhsNumber("9999999999");
		return participant;
	}
}